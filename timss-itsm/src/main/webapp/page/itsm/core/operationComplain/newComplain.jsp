<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
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
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
    var complainRdId = '<%=request.getParameter("complainRdId")%>'; 
    var currStatus = '${currStatus}' ;
    var processInstId = '${processInstId}' ;
</script>

<script type="text/javascript" src="${basePath}/js/itsm/core/newComplain.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}/itcui/ckeditor/ckeditor.js?ver=${iVersion}"></script>
 <script type="text/javascript"> 
 
    var loginUserId = ItcMvcService.user.getUserId();
    var siteId = ItcMvcService.getUser().siteId;
    var loginUserName = ItcMvcService.user.getUserName();
    var defKey="itsm_itc_complain";  //流程ID前缀
    var processInstId = "";
    var createtime =""; //供审批框中显示
    var updatetime =""; //供审批框中显示
    var taskId ; //活动节点ID
    var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
    var hasHandleRds = false; //判断有无处理记录的参数
    $(document).ready(function() {
	    $("#complainRdForm").iForm("init",{"fields":fields,"options":{validate:true}});
		$("#anlyAndTreaForm").iForm("init",{"fields":anlyAndTreaFilds,"options":{validate:true}});//处理记录的表单
		$("#complainRdForm").iForm("hide",["complainAccept","complainActive"]);
		$("#handleRds").hide();	
		dataGrid=$("#detailsTable").datagrid({
		    pageSize:pageSize,//pageSize为全局变量
		 	columns:datailsField,
			idField:'cmtId',
			singleSelect:true,
			fitColumns:true,
			scrollbarSize:0,
			sortable:true,
			sortname:'creationTime',
			sortorder:'desc',
		    onDblClickRow : function(rowIndex, rowData) {
			    openHandleInfo(rowData);
				}
			});
		//附件的控制
		$("#uploadfileTitle").iFold("init");
		$("#uploadfileTitle").iFold("show");  
        uploadform();
        if(complainRdId == null || complainRdId == 'null' || complainRdId == ""){
			 	$("#inPageTitle").html("新建投诉记录");
				$("#btn_audit").hide(); 
				$("#btn_print").hide();
				$("#btn_obsolete").hide();
				$("#btn_delete").hide();
				$("#btn_auditInfo").hide();
				FW.fixRoundButtons("#toolbar1");
		}else{  //查看详情记录
		    $.post(basePath + "itsm/complainRecords/querycomplainRdById.do",
				  {"complainRdId":complainRdId},
			    function(result){
					var data = result.resultMap;
					$("#btn_save").hide(); 
					$("#btn_commit").hide();
					$("#btn_audit").hide();
					$("#btn_obsolete").hide();
					$("#btn_delete").hide();
					$("#btn_flowDiagram").hide();
					var complainRdFormData = JSON.parse(data.complainRdForm);
					taskId = data.taskId;
					processInstId = complainRdFormData.workflowId;
					currStatus = complainRdFormData.currStatus;
					complainHandlerUser=complainRdFormData.complainHandlerUser;
					var processRd = JSON.parse(data.processRd);
					var attachmentData = data.attachmentMap;
					if(attachmentData.length > 0){//附件显示
						$("#uploadfileTitle").iFold("show"); 
						$("#uploadform").iForm("setVal",{uploadfield:attachmentData});
						$("#uploadform").iForm("endEdit");
					}else{
						$("#uploadfileTitle").iFold("hide"); 
					};
					if(loginUserName == complainHandlerUser){ //当前登录人与当前处理人相同才显示审批
						auditInfoShowBtn = 1;
						$("#btn_audit").show();
					};
					//作废工单（非草稿，仅工单发起人可以作废）
					if(currStatus == "newCp" && loginUserName == complainHandlerUser && processInstId){
					$("#uploadfileTitle").iFold("show"); 
					$("#uploadform").iForm("beginEdit");
						$("#btn_obsolete").show();
					};
					if(processRd != null){
						hasHandleRds = true ;
						$("#handleRds").show();
						$("#inPageTitle1").show();
						$("#formDiv").hide();
						$("#datagridDiv").show(); 
     					$("#detailsTable").datagrid("resize");
						$("#detailsTable").datagrid("loadData",processRd);
					};
						$("#complainRdForm").iForm("setVal",complainRdFormData);
						$("#complainRdForm").iForm("endEdit");
						if(currStatus == "obsolete"){  //作废工单不用审批
							$("#btn_audit").hide();
							auditInfoShowBtn = 0;
						};
						if(currStatus == "draft"){ //状态为草稿显示暂存提交和删除,并且可编辑
							$("#inPageTitle").html("编辑投诉记录");
							$("#uploadfileTitle").iFold("show"); 
							$("#uploadform").iForm("beginEdit");
							$("#btn_save").show(); 
							$("#btn_commit").show();
							$("#btn_delete").show();
							$("#btn_audit").hide();
							$("#btn_print").hide();
							$("#btn_auditInfo").show();
							$("#complainRdForm").iForm("beginEdit");
							/* setTimeout(function(){
								$("#f_content").val(complainRdFormData.content);
							},2000); */
						};
						if(currStatus == "newCp"){//状态为新建，显示暂存
							$("#btn_save").show();
							$("#btn_commit").show(); 
							$("#btn_audit").hide();
							$("#btn_print").hide();
							$("#complainRdForm").iForm("beginEdit");
							/* setTimeout(function(){
								$("#f_content").val(complainRdFormData.content);
							},2000); */
						};
						if(currStatus == "cpAccept" && loginUserName == complainHandlerUser){ //投诉受理显示是否继续
						    $("#complainRdForm").iForm("show",["complainAccept"]);
						    $("#complainRdForm").iForm("beginEdit",["complainAccept"]);	
						};
						if(currStatus == "resultConfirm"&& loginUserName == complainHandlerUser){ //结果确认显示是否有效字段
							$("#complainRdForm").iForm("show",["complainActive"]);
							$("#complainRdForm").iForm("beginEdit",["complainActive"]);	
						};
						if(currStatus == "cpHandle"){
							if(loginUserName == complainHandlerUser){
									$("#handleRds").show();
								    $("#inPageTitle1").show();
								if(processRd ==null){
									$("#formDiv").show();
									$("#datagridDiv").hide();
								}else{
									$("#formDiv").show();
									$("#datagridDiv").show();
								}
							}
						}; 
						if(currStatus == "end"){ //结束 :1、处理后结束 2、不处理结束
							auditInfoShowBtn = 0;
							if(processRd != null){
								$("#complainRdForm").iForm("show",["complainActive"]);
								$("#complainRdForm").iForm("endEdit",["complainActive"]);
								$("#handleRds").show();
								$("#inPageTitle1").show();
								$("#formDiv").hide();
								$("#btn_audit").hide();
								$("#datagridDiv").show(); 
	     						$("#detailsTable").datagrid("resize");
								$("#detailsTable").datagrid("loadData",processRd);	
							}else{
								$("#btn_audit").hide();
							}
						};
						FW.fixRoundButtons("#toolbar1");  //解决按钮隐藏后的圆角问题
				},"json");
			}
	}); 
//显示附件
function uploadform() {
	var uploadFiles="";
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
              			"swf" : basePath + "js/workorder/common/uploadify.swf",
              			//"fileSizeLimit" : 20 * 1024,
              			//"initFiles" : ${uploadFiles},
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
	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar ">
		    	<div class="btn-group btn-group-sm">
					<button type="button" class="btn btn-default" onclick="closeCurPage(false)">关闭</button>
				</div>
		        <div class="btn-group btn-group-sm">
		        	<button id="btn_save" type="button" class="btn btn-default" onclick="commitcomplainRd('save');">暂存</button>
		       		<button id="btn_commit" type="button" class="btn btn-default" onclick="commitcomplainRd('commit')">提交</button>
		        	<button id="btn_audit" type="button" class="btn btn-default" onclick="audit();">审批</button>
		        </div>
		         <div class="btn-group btn-group-sm">
		        	<button id="btn_print" type="button" class="btn btn-default" onclick="print()">打印</button>
		        	<button id="btn_obsolete" type="button" class="btn btn-default" onclick="obsolete()">作废</button>
		        	<button id="btn_delete" type="button" class="btn btn-default" onclick="deleteCp()">删除</button>
		        </div>
		        <div id="btn_flowDiagramDiv" class="btn-group btn-group-sm">
	        		<button id="btn_flowDiagram" type="button" class="btn btn-default" onclick="showDiagram();">审批信息</button>
	       		</div> 
	       		<div class="btn-group btn-group-sm">
	        		<button id="btn_auditInfo"type="button" class="btn btn-default" onclick="showAuditInfo();">审批信息</button>
	        	</div>
		    </div>
		</div>
		<div  class="inner-title">
			<span id="inPageTitle">投诉记录详情</span>
		</div>
		<div>
	    	 <form id="complainRdForm" class="autoform"></form>
	    </div>
	    <div id="handleRds">
			<div class="inner-title" >
				<span id="inPageTitle1">处理记录</span>
			</div>
    	 	<div id="datagridDiv">	
    	 		<form><table id="detailsTable" class="eu-datagrid"></table></form>
    	 	</div>
			<div id="formDiv" title="分析与处理">
    	 		<form id="anlyAndTreaForm" class="autoform"></form>
    		</div>
    	</div>
	    <!-- 附件层 -->
	    <div class="margin-group"></div>
	    <div grouptitle="附件"  id="uploadfileTitle">
			<div class="margin-title-table" id="uploadfile">
				<form id="uploadform" style=""></form>
			</div>
		</div>	
	</div>
</body>
</html>