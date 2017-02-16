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
<title>意见和建议详情</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
    var complainRdId = '<%=request.getParameter("complainRdId")%>'; 
    var currStatus = '${currStatus}' ;
    var processInstId = '${processInstId}';
</script>
<script type="text/javascript" src="${basePath}js/itsm/dpp/newComplain.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}/itcui/ckeditor/ckeditor.js?ver=${iVersion}"></script>
<script>
   
    var loginUserId = ItcMvcService.user.getUserId();
    var siteId = ItcMvcService.getUser().siteId;
    var loginUserName = ItcMvcService.user.getUserName();
    var defKey="itsm_dpp_complain";  //流程ID前缀
    var processInstId = "";
    var createtime =""; //供审批框中显示
    var updatetime =""; //供审批框中显示
    var taskId ; //活动节点ID
    var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
    var hasHandleRds = false; //判断有无处理记录的参数
    var bmferId=[];//部门负责人的id
    
    $(document).ready(function(){
    	 $("#complainRdForm").iForm("init",{"fields":fields,"options":{validate:true}});
    	 $("#anlyAndTreaForm").iForm("init",{"fields":anlyAndTreaFilds,"options":{validate:true}});//处理记录的表单
    	 $("#complainRdForm").iForm("hide",["handleDept","complainActive"]);
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
    		$("#inPageTitle").html("新建意见和建议");
    		fillCustomerInfo(loginUserId);
    		$("#complainRdForm").iForm("endEdit",["createUserName","deptName"]); 
    		$("#complainRdForm").iForm("hide",["code","createdate","handleDept","complainActive"]);
			$("#btn_audit").hide(); 
			$("#btn_obsolete").hide();
			$("#btn_delete").hide();
			$("#btn_auditInfo").hide();
			FW.fixRoundButtons("#toolbar1");
		 }else {
			 initPageData(complainRdId);
		 }		
    });
	//显示附件
	function uploadform() {
		var uploadFiles=[];
		$("#uploadform").iForm('init', {
			"fields" : [
              	{id:"uploadfield", 
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
	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
			<div id="org_tree" style="width:185px">
 			</div>
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
			<span id="inPageTitle">意见和建议详情</span>
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