<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%
	//获取List页面传送过来的参数，由于没有使用el表达式，所以只能通过java代码获取
	String type = request.getParameter("type")==null?"":String.valueOf(request.getParameter("type"));
	String sheetId = request.getParameter("sheetId")==null||"".equals(request.getParameter("sheetId"))?String.valueOf(request.getAttribute("sheetId")):String.valueOf(request.getParameter("sheetId"));
	String taskId = request.getAttribute("taskId")==null?"":String.valueOf(request.getAttribute("taskId")); 
	String process = request.getAttribute("process")==null?"":String.valueOf(request.getAttribute("process"));
	String processName = request.getAttribute("processName")==null?"":String.valueOf(request.getAttribute("processName"));
	String processInstId = request.getAttribute("processInstId")==null?"":String.valueOf(request.getAttribute("processInstId"));
	String defKey = request.getAttribute("defKey")==null?"":String.valueOf(request.getAttribute("defKey"));
	String isEdit = request.getAttribute("isEdit")==null?"":String.valueOf(request.getAttribute("isEdit"));
	String operOrder = request.getAttribute("operOrder")==null?"":String.valueOf(request.getAttribute("operOrder"));
	String poData = request.getAttribute("poData")==null?"":String.valueOf(request.getAttribute("poData"));
	String poListData = request.getAttribute("poListData")==null?"":String.valueOf(request.getAttribute("poListData"));
	String oper = request.getAttribute("oper")==null?"":String.valueOf(request.getAttribute("oper"));
	String siteId = request.getAttribute("siteId")==null?"":String.valueOf(request.getAttribute("siteId"));
	String classType = request.getAttribute("classType")==null?"":String.valueOf(request.getAttribute("classType"));
	String curUserId = request.getAttribute("curUserId")==null?"":String.valueOf(request.getAttribute("curUserId"));
	String status = request.getAttribute("status")==null?"":String.valueOf(request.getAttribute("status"));
	String sessId = request.getSession().getId();
	SecureUser operator = (SecureUser) session.getAttribute(Constant.secUser);
	String valKey = FileUploadUtil.getValidateStr(operator,FileUploadUtil.DEL_OWNED);
	String uploadFiles = request.getAttribute("uploadFiles")==null?"":String.valueOf(request.getAttribute("uploadFiles"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<title>采购申请表单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/homepage/homepageService.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<style type="text/css">.title-arrow{cursor: pointer;}</style>
<script>
	var sheetNo = '${sheetNo}';
	var process = '<%=process%>';		//环节名称
	var processName = '<%=processName%>';//环节名称
	var processInstId = '<%=processInstId%>';
	var curUserId = '<%=curUserId%>';
	var taskId = '<%=taskId%>';			//任务id
	var defKey='<%=defKey%>';
	var classType = '<%=classType%>';
	var isEdit='<%=isEdit%>';			//当前用户是否编辑
	var sheetId = '<%=sheetId%>';//获取全局的sheetid
	var oper = '<%=oper%>';
	var poData = '<%=poData%>';
	var poListData = '<%=poListData.replace("'","\\'").replace("\"","\\\"")%>';
	var type = '<%=type%>';
	var siteId = '<%=siteId%>';
	var operOrder = '<%=operOrder%>';
	var status = '<%=status%>';
	status = FW.getEnumMap("PURCHORDER_FLOWSTATUS")[status];
	var queryMode = null;	// 1代表创建、草稿、结束流程   0代表流程进行中
	var processStatus = null;
	var url = null;
	var tagId = null;
	
	var delFlag =false;
	
	/************************判断页面是否修改************************/
	var initFormStatus = null;
	var currFormStatus = null;
	var initListStatus = null;
	var currListStatus = null;
	var saveFlag = false;
	var formName = "autoform";
	var listName = "order_item";
	/************************判断页面是否修改************************/
	var uploadIds = "";
	var uploadFiles = '<%=uploadFiles%>';
	var sessId = '<%=sessId%>';
	var valKey = '<%=valKey%>';		
	var deleteItems = [];
	//初始化界面
	$(document).ready(function() {
		//按钮初始化
		OrderBtn.init();
		//按钮权限控制
		PurOrderPriv.init();
		setProcessStatus();
		//是否自动生成采购单
		if(operOrder=="autoGenerate"){
			autoGenerateForm();
		}else{
			newOrderForm();
		}
		$("#f_companyName").attr("readonly",true);
		//折叠区标题
		$("#foldable_area").ITCUI_Foldable();
		//页面流程控制
		initPageProcess();
		
		if ("" != processInstId) {
			var reportUrl = null;
			$("#btn_print").show();
			if("ITC" == siteId){
				reportUrl = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_ITC_PO_001.rptdesign&siteid="+siteId+"&sheetid="+sheetId+"&userid="+curUserId;
			}else{
				reportUrl = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_PO_001_pdf.rptdesign&siteid="+siteId+"&sheetid="+sheetId;
			}
			FW.initPrintButton("#btn_print",reportUrl,"采购合同打印预览",null);
		}
		
		FW.fixToolbar("#toolbar1");
		addFormCloseEvent();
	});
	
	//关闭页面
	function pageClose(){
		homepageService.refresh();
		FW.deleteTabById(FW.getCurrentTabId());
	}
	
	//显示执行情况
	function showDoingStatus(){
			FW.dialog("init",{
				src: basePath+"purchase/purorder/purOrderDoingStatus.do?sheetNo="+sheetNo,
				btnOpts:[{
				            "name" : "关闭",
				            "float" : "right",
				            "style" : "btn-default",
				            "onclick" : function(){
				                _parent().$("#itcDlg").dialog("close");
				             }
				        }],
				dlgOpts:{width:800, height:520, closed:false, title:"合同执行情况", modal:true}
			});
	}
	
	//标识表单数据表格是否初始化
	var formInited = false;
	var datagridInited = false;
</script>
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/common/purchase.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/core/purorder/purOrder.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/core/purorder/purOrderForm.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/core/purorder/purOrderList.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/core/purorder/purOrderOper.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/core/purorder/purOrderBtn.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/core/purorder/purOrderProc.js?ver=${iVersion}"></script>
<style type="text/css">.btn-garbage{cursor:pointer;}</style>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<div class="btn-toolbar" role="toolbar" id="toolbar1">
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn btn-default" id="btn-close">关闭</button>
			</div>
			<div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default priv" id="btn-edit" privilege="exampurch_edit">编辑</button>
		        <button type="button" class="btn btn-default priv" id="btn-save" privilege="exampurch_save">暂存</button>
		    	<button type="button" class="btn btn-default priv" id="btn-submit" privilege="exampurch_approve">提交</button>
		    </div>
		    <div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default priv" id="btn-delete" privilege="exampurch_delete">作废</button>
		    </div>
		    <div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default priv" id="btn-invoice" privilege="exampurch_invoiceBtn">录入发票</button>
		    </div>
		    <div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default priv" id="btn-stopOrder" privilege="exampurch_stopOrderBtn">终止合同</button>
		    </div>
		    <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_print" class="btn btn-default" style="display:none">打印</button>
	        </div>
	        <div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default " id="btn-doingStatus" onclick="showDoingStatus()" >执行情况</button>
		    </div>
		    <div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default" id="btn-process">审批信息</button>
		    </div>
		</div>
	</div>
	<div class="inner-title" id="pageTitle"> 采购合同 </div>
	
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
	
	<div id="foldable_area" grouptitle="采购物资列表">
		<div id="order_itemGrid" class="margin-title-table">
			<form id="order_item_form">
			<table id="order_item" class="eu-datagrid">
			</table>
			</form>
		</div>
		<div class="btn-toolbar margin-foldable-button" role="toolbar">
			<div class="btn-group btn-group-xs">
		        <button type="button" class="btn btn-success" id="btn-add">添加物资</button>
		    </div>
	    </div>
	    <div class="margin-group"></div>
		<div grouptitle="附件"  id="uploadfileTitle">
			<div class="margin-title-table" id="uploadfile">
				<form id="uploadform" style=""></form>
			</div>
		</div>		    
	</div>
</body>
</html>