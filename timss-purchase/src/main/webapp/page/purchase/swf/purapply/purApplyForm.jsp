<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>

<%
	//获取List页面传送过来的参数，由于没有使用el表达式，所以只能通过java代码获取
	String type = request.getParameter("type")==null?"":String.valueOf(request.getParameter("type"));
	String sheetId = request.getParameter("sheetId")==null?String.valueOf(request.getAttribute("sheetId")):String.valueOf(request.getParameter("sheetId"));
	String source = request.getParameter("source")==null?"":String.valueOf(request.getParameter("source"));
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
	String orgName = request.getAttribute("orgName")==null?"":String.valueOf(request.getAttribute("orgName"));
	String isLastStep = request.getAttribute("isLastStep")==null?"":String.valueOf(request.getAttribute("isLastStep"));
	String isContainsInQty = request.getAttribute("isContainsInQty")==null?"":String.valueOf(request.getAttribute("isContainsInQty"));
	String isCreator = request.getAttribute("isCreator")==null?"":String.valueOf(request.getAttribute("isCreator"));
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
	var processInstId = '<%=processInstId%>';
	var classType = '<%=classType%>';
	var taskId = '<%=taskId%>';			//任务id
	var defKey='<%=defKey%>';
	var stopDefKey='<%=stopDefKey%>';
	var isEdit='<%=isEdit%>';			//当前用户是否编辑
	var sheetId = '<%=sheetId%>';	//获取全局的sheetid
	<%--从采购合同物资表跳转过来的sheetId是数字不是字符，只能传sheetNo，在后台转为sheetid设置到modelandview，以el表达式获取--%>
	if('null'==sheetId){
		sheetId = '${sheetId}';
	}
	var type = '<%=type%>';
	var siteId = '<%=siteId%>';	
	var oper = '<%=oper%>';
	var sourceCome='<%=source%>';//标识采购申请的来源
	var isRollBack='<%=isRollBack%>';
	var orgName = '<%=orgName%>';
	var isLastStep = '<%=isLastStep%>';
	var isContainsInQty = '<%=isContainsInQty%>';
	var isCreator = '<%=isCreator%>';
	var editStatus = false;
	var flag = false;
	
	var proMsg = "";
	var operation = type.split("_");	

	var url = null;
	var tagId = null;
	var itemFilter = null;//表格中现有的物资
	var currOpFilter = {};//本次操作需要过滤的物资
	
	var processStatus = null;
	
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
	var initForm = false;
	/************************判断页面是否修改************************/
	var applyTypeTmp = FW.getEnumMap("ITEMAPPLY_TYPE");
	var applyType = {};
	for(var key in applyTypeTmp){
		if("BGYP"==key&&Priv.hasRole("SWF_ZHZZ")||
			"LBYP"==key&&Priv.hasRole("SWF_LZZZ")||
			"FLSS"==key&&Priv.hasRole("SWF_LZZZ")||
			"XFYP"==key&&Priv.hasRole("SWF_ZHZZ")||
			"JSJJQPJL"==key&&Priv.hasRole("SWF_XXZZ")||
			"SCLWZ"==key){
			applyType[key]=applyTypeTmp[key];
		}else{
			continue;
		}
	}  
	/************************终止采购申请相关信息*****************************/
	var stopProcInstId = '<%=stopProcInstId%>';
	var stopStatus = '<%=stopStatus%>';
	var stopProcessStatus = '<%=stopProcessStatus%>';
	var canStop = '<%=canStop%>';
	var cateName = '${cateName}';
	var activeStatus = 'submit';//用于判断是提交还是审批
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
		}else if(("stopped"==stopStatus||"stopping"==stopStatus)&&"true"!=canStop){
			FW.showBtnGroup("btn-group-process");
			$("#btn-process").hide();
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
		//表单初始化
		initApplyList(operation[1]);
		//列表操作权限控制
		FW.privilegeOperation("applypurch_listAllowEdit", "apply_item");
		FW.privilegeOperation("applypurch_btnAllowEdit", null);
		
		//页面根据流程不同做控制
		initPageProcess();
		
		if ("" != processInstId) {
			var url = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_SWF_PA_001_pdf.rptdesign&siteid="+siteId+"&sheetid="+sheetId;
			FW.initPrintButton("#btn_print",url,"采购申请打印预览",null);
		}

		initInnerTab();
		//修正按钮圆角样式
		FW.fixToolbar("#toolbar1");
		//页面关闭控制
		addFormCloseEvent();

	});
	
	//页面关闭
	function pageClose(){
		homepageService.refresh();
		FW.deleteTabById(FW.getCurrentTabId());
		//$("#apply_item").iFixCheckbox();
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
	//新建领料申请
	function newMatApply(){
		var url = basePath+ "inventory/invmatapply/invMatApplyForm.do?purApplyId="+sheetId+"&imaid=";
	    var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
	    FW.addTabWithTree({
	        id : "newMatApplyForm" + prefix,
	        url : url,
	        name : "物资领料",
	        tabOpt : {
	            closeable : true,
	            afterClose : "FW.deleteTab('$arg');FW.activeTabById('"+FW.getCurrentTabId()+"');"
	        }
	    });
	}
	//查看领料申请
	function viewMatApply(imaid){
		var url = basePath+ "inventory/invmatapply/invMatApplyForm.do?imaid="+imaid;
    	var prefix = imaid;
	    FW.addTabWithTree({
	        id : "editMatApplyForm" + prefix,
	        url : url,
	        name : "物资领料",
	        tabOpt : {
	            closeable : true,
	            afterClose : "FW.deleteTab('$arg');FW.activeTabById('"+FW.getCurrentTabId()+"');"
	        }
	    });
	}
</script>

<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/common/purchase.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/swf/purapply/purApply.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/swf/purapply/purApplyForm.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/swf/purapply/purApplyList.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/swf/purapply/purApplyOper.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/swf/purapply/purApplyBtn.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/swf/purapply/purApplyProc.js?ver=${iVersion}"></script>

<style type="text/css"> .btn-garbage{ cursor:pointer; }
.nodata{font-size:14px;}</style>
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
		    	<button type="button" class="btn btn-default priv" id="btn-business" privilege="applypurch_tobuss">提交商务网</button>
		        <button type="button" class="btn btn-default priv" id="btn-nobusiness" privilege="applypurch_nobuss">撤回商务网</button>
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
		        <button type="button" class="btn btn-default priv" id="btn-newMatApply" privilege="applypurch_newMatApplyPriv" onclick="newMatApply()" >新建领料申请</button>
		    </div>
		    <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_print" class="btn btn-default priv" style="display:none" privilege="applypurch_print">打印</button>
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
	<% if (!"".equals( sheetId )){  %>
	<ul class="nav nav-tabs">
	    <li class="active" id="tab_applyitem"><a href="#tab_applyitem_div" data-toggle="tab">申请物资</a></li>
	    <li id="tab_busi"><a href="#tab_busi_div" data-toggle="tab">已发商务网物资</a></li>
	    <li id="tab_order"><a href="#tab_order_div" data-toggle="tab">已执行采购物资</a></li>
	    <li id="tab_stock"><a href="#tab_stock_div" data-toggle="tab">已入库物资</a></li>
	    <li id="tab_mat"><a href="#tab_mat_div" data-toggle="tab">相关领料单</a></li>
	    <li id="tab_impl"><a href="#tab_impl_div" data-toggle="tab">执行情况</a></li>
	</ul>
	<div class="tab-content">
		<div class="tab-pane active" id="tab_applyitem_div">
	<% } %>
	<% if ("".equals( sheetId )){  %>
			<div id="foldable_area" grouptitle="申请物资列表">
	<% } %>
			<div id="apply_item_Grid" class="margin-title-table">
				<form id="tableform">
					<table id="apply_item" class="eu-datagrid"></table>
				</form>
			</div>
	<% if ("".equals( sheetId )){  %>
			</div>
	<% } %>
			<div class="btn-toolbar margin-foldable-button" role="toolbar">
				<div class="btn-group btn-group-xs">
			        <button type="button" class="btn btn-success" id="btn-add">添加物资</button>
			    </div>
			</div>
		</div>
	<% if (!"".equals( sheetId )){  %>
	    <div class="tab-pane" id="tab_busi_div">
	        <span class="nodata" style="display:none;" id="no_busi">无商务网物资信息</span>
	        <div id="apply_busi_Grid" style="display:none;width:100%" class="margin-title-table">
	            <table id="apply_busi" class="eu-datagrid"></table>
	        </div>
	    </div>
	    <div class="tab-pane" id="tab_order_div">
	        <span class="nodata" style="display:none;" id="no_order">无执行采购物资信息</span>
	        <div id="apply_order_Grid" style="display:none;width:100%" class="margin-title-table">
	            <table id="apply_order" class="eu-datagrid"></table>
	        </div>
	    </div>
	    <div class="tab-pane" id="tab_stock_div">
	    	<span class="nodata" style="display:none;" id="no_stock">无入库物资信息</span>
	        <div id="apply_stock_Grid" style="display:none;width:100%" class="margin-title-table">
	            <table id="apply_stock" class="eu-datagrid"></table>
	        </div>
	    </div>
	    <div class="tab-pane" id="tab_mat_div">
	        <span class="nodata" style="display:none;" id="no_mat">无相关领料单信息</span>
	        <div id="apply_mat_Grid" style="display:none;width:100%" class="margin-title-table">
	            <table id="apply_mat" class="eu-datagrid"></table>
	        </div>
	    </div>
	    <div class="tab-pane" id="tab_impl_div">
	        <span class="nodata" style="display:none;" id="no_impl">无执行情况信息</span>
	        <div id="apply_impl_Grid" style="display:none;width:100%" class="margin-title-table">
	            <table id="apply_impl" class="eu-datagrid"></table>
	        </div>
	    </div>
	</div>
	<% } %>
	<div class="margin-group"></div>
	<div grouptitle="附件"  id="uploadfileTitle">
		<div class="margin-title-table" id="uploadfile">
			<form id="uploadform" style=""></form>
		</div>
	</div>
</body>
</html>