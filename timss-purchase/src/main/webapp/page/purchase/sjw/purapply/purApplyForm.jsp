<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>

<%
	//获取List页面传送过来的参数，由于没有使用el表达式，所以只能通过java代码获取
	String type = request.getParameter("type")==null?"":String.valueOf(request.getParameter("type"));
	String sheetId = request.getParameter("sheetId")==null?"":String.valueOf(request.getParameter("sheetId"));
	String siteId = request.getAttribute("siteId")==null?"":String.valueOf(request.getAttribute("siteId"));
	String taskId = request.getAttribute("taskId")==null?"":String.valueOf(request.getAttribute("taskId")); 
	String process = request.getAttribute("process")==null?"":String.valueOf(request.getAttribute("process"));
	String processName = request.getAttribute("processName")==null?"":String.valueOf(request.getAttribute("processName"));
	String processInstId = request.getAttribute("processInstId")==null?"":String.valueOf(request.getAttribute("processInstId"));
	String defKey = request.getAttribute("defKey")==null?"":String.valueOf(request.getAttribute("defKey"));
	String stopDefKey = request.getAttribute("stopDefKey")==null?"":String.valueOf(request.getAttribute("stopDefKey"));
	String isEdit = request.getAttribute("isEdit")==null?"":String.valueOf(request.getAttribute("isEdit"));
	String oper = request.getAttribute("oper")==null?"":String.valueOf(request.getAttribute("oper"));
	String classType = request.getAttribute("classType")==null?"":String.valueOf(request.getAttribute("classType"));
	String isRollBack = request.getAttribute("isRollBack")==null?"":String.valueOf(request.getAttribute("isRollBack"));
	// 20160106 add by yuanzh 附件上传
	String sessId = request.getSession().getId();
	SecureUser operator = (SecureUser) session.getAttribute(Constant.secUser);
	String valKey = FileUploadUtil.getValidateStr(operator,FileUploadUtil.DEL_OWNED);
	String uploadFiles = request.getAttribute("uploadFiles")==null?"":String.valueOf(request.getAttribute("uploadFiles"));
	// 20160714 add by gucw 安全库存物资分类id
	String cateName = request.getAttribute("cateName")==null?"":String.valueOf(request.getAttribute("cateName"));
	// 20161130 add by gucw 终止采购申请相关信息
	String stopProcInstId  =request.getAttribute("stopProcInstId")==null?"":String.valueOf(request.getAttribute("stopProcInstId"));
	String stopStatus  =request.getAttribute("stopStatus")==null?"":String.valueOf(request.getAttribute("stopStatus"));
	String stopProcessStatus  =request.getAttribute("stopProcessStatus")==null?"":String.valueOf(request.getAttribute("stopProcessStatus"));
	String canStop  =request.getAttribute("canStop")==null?"":String.valueOf(request.getAttribute("canStop"));
	// 20170207 add by gucw
	String hasItemApplying =  request.getAttribute("hasItemApplying")==null?"":String.valueOf(request.getAttribute("hasItemApplying"));
	String isLastStepAssignee = request.getAttribute("isLastStepAssignee")==null?"":String.valueOf(request.getAttribute("isLastStepAssignee"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<title>采购申请表单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/homepage/homepageService.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script>
	var process = '<%=process%>';		//环节名称
	var processName = '<%=processName%>';//环节名称
	var taskId = '<%=taskId%>';			//任务id
	var defKey='<%=defKey%>';
	var stopDefKey='<%=stopDefKey%>';
	var classType = '<%=classType%>';
	var isEdit='<%=isEdit%>';			//当前用户是否编辑
	var processInstId = '<%=processInstId%>';
	var sheetId = '<%=sheetId%>';	//获取全局的sheetid
	var type = '<%=type%>';
	var siteId = '<%=siteId%>';	
	var oper = '<%=oper%>';
	var isRollBack='<%=isRollBack%>';
	var editStatus = false;
	var flag = false;
	
	var url = null;
	var tagId = null;
	var itemFilter = null;//表格中现有的物资
	
	var operation = type.split("_");	
	var currOpFilter = {};//本次操作需要过滤的物资
	var proMsg = "";
	
	// 20160106 add by yuanzh 说是要添加附件
	var uploadIds = "";
	var uploadFiles = '<%=uploadFiles%>';
	var sessId = '<%=sessId%>';
	var valKey = '<%=valKey%>';
	/************************判断页面是否修改************************/
	var initFormStatus = null;
	var currFormStatus = null;
	var initListStatus = null;
	var currListStatus = null;
	var saveFlag = false;
	var formName = "autoform";
	var listName = "apply_item";
	/************************判断页面是否修改************************/
	var cateName = '${cateName}';
	var activeStatus = 'submit';//用于判断是提交还是审批
	/************************终止采购申请相关信息*****************************/
	var stopProcInstId = '<%=stopProcInstId%>';
	var stopStatus = '<%=stopStatus%>';
	var stopProcessStatus = '<%=stopProcessStatus%>';
	var canStop = '<%=canStop%>';
	var hasItemApplying = '<%=hasItemApplying%>';
	var isLastStepAssignee = '<%=isLastStepAssignee%>';
	var browserType = ( navigator.userAgent.indexOf("Trident/7.0")>-1||navigator.userAgent.indexOf("IE")>-1||navigator.userAgent.indexOf("Edge")>-1)?"IE":"OTHER";
	function stopBtnCss(){
		//终止相关的按钮控制--begin
		if(""==stopStatus&&(process.indexOf("procurement")>-1||"over"==processStatus)&&Priv.vPriv["applypurch_stop"]){
			//可以提交终止 还未提交终止
			$("#btn-stop").show();
			FW.showBtnGroup("btn-group-process");
			$("#btn-process").hide();
		}else if(""==stopStatus&&(process.indexOf("procurement")>-1||"over"==processStatus)&&!Priv.vPriv["applypurch_stop"]){
			//不可提交终止 且未提交终止
			FW.showBtnGroup("btn-group-process");
			$("#btn-process").hide();
		}else if("stopping"==stopStatus&&"true"==canStop){
			//已提交终止 可以审批终止 且未审批完终止
			$("#btn-stop").show();
			FW.showBtnGroup("btn-group-process");
			$("#btn-process").hide();
			//禁用生成采购合同
			FW.disableBtn("btn-submit");
		}else if(("stopped"==stopStatus||"stopping"==stopStatus)&&"true"!=canStop){
			FW.showBtnGroup("btn-group-process");
			$("#btn-process").hide();
			//禁用生成采购合同
			FW.disableBtn("btn-submit");
		}
		//作废终止的按钮的显示控制
		if("stopping"==stopStatus&&"procurement"==stopProcessStatus&&"true"==canStop){
			$("#btn-nullifyStop").show();
		}
		//终止相关的按钮控制--end
	}
	//初始化界面
	$(document).ready(function() {
		//按钮初始化
		ApplyBtn.init();
		//为不同流程设置不同的代号
		setProcessStatus();
		//按钮虚拟权限初始化
		PurApplyPriv.init();
		//折叠区标题
		$("#foldable_area").ITCUI_Foldable();
		//列表初始化
		initList(operation[1]);
		FW.privilegeOperation("applypurch_listAllowEdit","apply_item");
		FW.privilegeOperation("applypurch_btnAllowEdit",null);
		//页面根据流程不同做控制
		initPageProcess();
		//修正按钮圆角样式
		FW.fixToolbar("#toolbar1");
		addFormCloseEvent();
	});
	
	//页面关闭
	function pageClose(){
		homepageService.refresh();
		FW.deleteTabById(FW.getCurrentTabId());
	}
	//显示执行情况
	function showDoingStatus(){
			FW.dialog("init",{
				src: basePath+"purchase/purorder/queryPurApplyImplemetationStatus.do?sheetId="+sheetId,
				btnOpts:[{
				            "name" : "关闭",
				            "float" : "right",
				            "style" : "btn-default",
				            "onclick" : function(){
				                _parent().$("#itcDlg").dialog("close");
				             }
				        }],
				dlgOpts:{width:800, height:520, closed:false, title:"采购申请执行情况", modal:true}
			});
	}
</script>
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/common/purchase.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/sjw/purapply/purApply.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/sjw/purapply/purApplyForm.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/sjw/purapply/purApplyList.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/sjw/purapply/purApplyOper.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/sjw/purapply/purApplyBtn.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/sjw/purapply/purApplyProc.js?ver=${iVersion}"></script>

<style type="text/css"> .btn-garbage{ cursor:pointer; }</style>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<div class="btn-toolbar" id="toolbar1">
			<div class="btn-group btn-group-sm">
			    <button type="button" class="btn btn-default" id="btn-close">关闭</button>
			</div>
			<div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default priv" id="btn-edit" privilege="applypurch_edit">编辑</button>
		        <button type="button" class="btn btn-default priv" id="btn-save" privilege="applypurch_save">暂存</button>
		    	<button type="button" class="btn btn-default priv" id="btn-submit" privilege="applypurch_commitApply">提交</button>
		    </div>
		    <div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default " id="btn-nullifyStop" >作废终止</button>
		    </div>
		    <div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default priv" id="btn-stop" privilege="applypurch_stop">终止</button>
		    </div>
		    <div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default priv" id="btn-delete" privilege="applypurch_delete">作废</button>
		    </div>
		    <div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default priv" id="btn-doingStatus" privilege="applypurch_doingStatusPriv" onclick="showDoingStatus()" >执行情况</button>
		    </div>
		    <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_print" class="btn btn-default" style="display:none">打印</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_exportApplyItem" class="btn btn-default" style="display:none">导出物资清单</button>
	        </div> 
		    <div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default" id="btn-process">审批信息</button>
		    </div>
		    <div class="btn-group btn-group-sm" id="btn-group-process">
		    	<button type="button" class="btn btn-default dropdown-toggle"  id="btn-group-process-btn" data-toggle="dropdown">
			                            &nbsp;审批信息&nbsp;
			        <span class="caret"></span>
			    </button>
			    <ul class="dropdown-menu">
			        <li id="btn-process"><a href="javascript:void(0)" >采购申请审批信息</a></li>
			        <li id="btn-stopprocess"><a href="javascript:void(0)" >采购终止审批信息</a></li>
			    </ul>
			</div>
		</div>
	</div>
	<div class="inner-title" id="pageTitle">
		采购申请
	</div>
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
	<form id="tableform">
	<div id="foldable_area" grouptitle="申请物资列表">
		<div id="apply_item_Grid" class="margin-title-table">
			<table id="apply_item" class="eu-datagrid"></table>
		</div>
	</div>
	</form>
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
</body>
</html>