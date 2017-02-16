<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.Constant"%>
<% 
	String imrid = request.getParameter("imrid") == null ? "":String.valueOf(request.getParameter("imrid"));
	String embed = request.getParameter("embed") == null ? "":String.valueOf(request.getParameter("embed"));
	String imaid = request.getAttribute("imaid")==null?"":String.valueOf(request.getAttribute("imaid"));
	String taskId = request.getAttribute("taskId")==null?"":String.valueOf(request.getAttribute("taskId")); 
	String siteid = request.getAttribute("siteid")==null?"":String.valueOf(request.getAttribute("siteid"));
	String defKey = request.getAttribute("defKey")==null?"":String.valueOf(request.getAttribute("defKey"));
	String process = request.getAttribute("process")==null?"":String.valueOf(request.getAttribute("process"));
	String processName = request.getAttribute("processName")==null?"":String.valueOf(request.getAttribute("processName"));
	String processInstId = request.getAttribute("processInstId")==null?"":String.valueOf(request.getAttribute("processInstId"));
	String isEdit = request.getAttribute("isEdit")==null?"":String.valueOf(request.getAttribute("isEdit"));
	String oper = request.getAttribute("oper")==null?"":String.valueOf(request.getAttribute("oper"));
	//String isWarehouse = request.getAttribute("isWarehouse")==null?"":String.valueOf(request.getAttribute("isWarehouse"));
	//附件相关
	SecureUser operator = (SecureUser) session
			.getAttribute(Constant.secUser);
	String sessId = request.getSession().getId();
	String valKey = FileUploadUtil.getValidateStr(operator,
			FileUploadUtil.DEL_OWNED);
	String uploadFiles = request.getAttribute("uploadFiles")==null?"":String.valueOf(request.getAttribute("uploadFiles"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>物资发料表单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/homepage/homepageService.js?ver=${iVersion}"></script>

<script>
	var taskId = '<%=taskId%>';			//任务id
	var siteid = '<%=siteid%>';
	var defKey='<%=defKey%>';
	var imrid = '<%=imrid%>';
	var imaid = '<%=imaid%>';
	var embed = '<%=embed%>';
	var process = '<%=process%>';		//环节名称
	var processName = '<%=processName%>';//环节名称
	var processInstId = '<%=processInstId%>';
	var isEdit='<%=isEdit%>';			//当前用户是否编辑
	var oper = '<%=oper%>';
	var imaCreateUserId = '${imaCreateUserId}';
	var imaCreateUserName = '${imaCreateUserName}';
	var deliveryDate;//发料日期
	var form = null;
	var uploadFiles = '<%=uploadFiles%>';
	
	$(document).ready(function() {
		InvMatRecipientsPriv.init();
		InvMatRecipientsBtn.init();
		IMRLoadingForm.init();
		initList();
		$("#uploadfileTitle").iFold("init");
		uploadform();
		//附件的控制
		if(embed == "1" || isEdit != "editable"){
			$("#btn_attach").hide();
			$("#uploadform").iForm("endEdit");
			if(uploadFiles == null || uploadFiles == "[]"){
				$("#uploadfileTitle").iFold("hide");  
			}
		}else{
			$("#btn_attach").show();
			$("#uploadform").iForm("beginEdit");
		}
		FW.fixToolbar("#toolbar");
		FW.privilegeOperation("materiReQ_btnAllowEdit",null);
		$("#btn_attach").click(function(){
			var fileIds = $("#uploadform").ITC_Form("getdata");
			var uploadIds=JSON.stringify(fileIds.uploadfield);
			$.ajax({
				type : "POST",
				async : false,
				url : basePath+"inventory/invmatrecipients/saveInvMatRecipientsAttach.do",
				data : {
					"uploadIds" : uploadIds,
					"imrid":imrid
				},
				dataType : "json",
				success : function(data){
					FW.success("附件保存成功");
				}
			})
		});
	});
	
	//显示附件
	function uploadform() {
		var file = null;
	
		if("" != uploadFiles){
			file = JSON.parse(uploadFiles);
		}
		$("#uploadform").iForm('init', {
			"fields" : [
	              	{
	              		id:"uploadfield", 
	              		title:" ",
	              		type:"fileupload",
	              		linebreak:true,
	              		wrapXsWidth:12,
	              		wrapMdWidth:12,
	              		options:{
	              		    "uploader" : basePath+"upload?method=uploadFile&jsessionid=<%=sessId%>",
	              		    "delFileUrl" : basePath+"upload?method=delFile&key=<%=valKey%>",
	              			"downloadFileUrl" : basePath + "upload?method=downloadFile",
	              			"swf" : basePath + "js/inventory/common/uploadify.swf",
	              			"initFiles" : file,
	              			"delFileAfterPost" : true
	              		}
	              	}
	              ],
			"options" : {
				"labelFixWidth" : 6,
				"labelColon" : false
			}
		});
	}
</script>
<style type="text/css">.btn-garbage{ cursor:pointer;}</style>
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatapply/invMatApply.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatrecipients/invMatRecipientsForm.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatrecipients/invMatRecipientsList.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatrecipients/invMatRecipientsBtn.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatrecipients/invMatRecipientsProc.js?ver=${iVersion}"></script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_close" class="btn btn-default">关闭</button>
	        </div>	    
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_submit" class="btn btn-default priv" privilege="materiReQ_applyend">发料</button>
	        </div>
	        <!-- <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_attach" class="btn btn-default priv" privilege="materiReQ_attach">保存附件</button>
	        </div> -->
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_borrow" class="btn btn-default priv" onclick="openBorrowWindow()" privilege="materiReQ_borrow">登记资产领用</button>
	        </div>	        
		    <div class="btn-group btn-group-sm">
			    <button type="button" class="btn btn-default priv dropdown-toggle" id="btn-selectRange" data-toggle="dropdown">
			        <label id="currentRangeNameLabel">打印</label>
			        <span class="caret"></span>
			    </button>
		        <ul class="dropdown-menu" id="selectRangeMenu">
		        	<li><a id="btn_print_apply">领料申请</a></li>
		        	<li><a id="btn_print">发料单</a></li>
		        </ul>
		    </div>	  		        
	    </div>
	</div>
	<div class="inner-title" id="pageTitle">
		物资发料单详情
	</div>
	
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
		
	<div id="matrecdetail_list" grouptitle="物资明细">
		<div id="matrecdetailGrid" class="margin-title-table">
			<table id="matrecdetail_grid" class="eu-datagrid"></table>
		</div>
	</div>
	
	<!-- 附件层 -->
	<div class="margin-group"></div>
	<div grouptitle="附件"  id="uploadfileTitle">
		<div class="margin-title-table" id="uploadfile">
			<form id="uploadform" style=""></form>
		</div>
	</div>
</body>
</html>