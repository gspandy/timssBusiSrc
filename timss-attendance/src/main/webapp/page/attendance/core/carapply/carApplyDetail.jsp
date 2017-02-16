<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%
	SecureUser operator = (SecureUser) session
			.getAttribute(Constant.secUser);
	String sessId = request.getSession().getId();
	String valKey = FileUploadUtil.getValidateStr(operator,
			FileUploadUtil.DEL_OWNED);
	
 %>
<!DOCTYPE html>
<html style="height:99%">
<head>
<title>用车申请详情</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/attendance/carApplyDetail.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
<script>
    var caId = '<%=request.getParameter("caId")%>';
    var defKey="atd_dpp_carapply";
    var loginUserId = ItcMvcService.user.getUserId();
    var loginUserName = ItcMvcService.user.getUserName();
    var processInstId= null;
	var taskId = null;
	var currHandUser = "";
	var createtime =""; //供审批框中显示
    var updatetime =""; //供审批框中显示
    $(document).ready(function(){
    	 $("#carApplyForm").iForm("init",{"fields":fields,"options":{validate:true}});
    	 $("#carApplyForm").iForm("hide",["carType","driver"]);
    	 //附件的控制
		 $("#uploadfileTitle").iFold("init");
		 $("#uploadfileTitle").iFold("show");  
		 uploadform();
         
    	 if(caId == "" || caId == null || caId == 'null'){
    		$("#inPageTitle").html("新建用车申请");
    		$("#carApplyForm").iForm("hide",["caNum","createUserName","deptName","createDate"]);
			$("#btn_audit").hide();
			$("#btn_delete").hide();
			$("#btn_obsolete").hide();
			$("#fin-print").hide(); 
			FW.fixRoundButtons("#toolbar1");
		 }else {
			 initPageData(caId);
		 }		
    });
//显示附件
function uploadform() {
	var uploadFiles=[];
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
              			"swf" : basePath + "itcui/js/uploadify.swf",
              			"initFiles" : uploadFiles,
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
</head>
<body style="height: 100%;" class="bbox">
    <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
		    <div class="btn-group btn-group-sm">
				<button type="button" class="btn btn-default" onclick="closeCurPage()">关闭</button>
		    </div>
		    <div class="btn-group btn-group-sm">
	            <button type="button" id="btn_save" class="btn btn-default" onclick="save();">暂存</button>
	            <button type="button" id="btn_commit" class="btn btn-default" onclick="submit(this);">提交</button>
	            <button type="button" id="btn_audit" class="btn btn-default "  onclick="audit();">审批</button>
	        </div>
	         <div class="btn-group btn-group-sm">
	            <button type="button" id="btn_delete" class="btn btn-default"  onclick="del();">删除</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" id="btn_obsolete" class="btn btn-default"  onclick="obsolete();">作废</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" id="fin-print"  class="btn btn-default"  onclick="print();">打印</button>
	        </div> 
	        <div class="btn-group btn-group-sm">
	            <button type="button" id="btn_flowInfo" class="btn btn-default" onclick="showWorkflow();">审批信息</button>
	        </div>
        </div>
    </div>
	<div class="inner-title">
		<span id="inPageTitle">用车申请详情</span>
	</div>
	<div>
	   <form id="carApplyForm" class="margin-form-title margin-form-foldable"></form>
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