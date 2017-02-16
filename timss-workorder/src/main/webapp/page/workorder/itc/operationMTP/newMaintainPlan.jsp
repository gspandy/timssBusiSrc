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
<html>
  <head>
	<title>新建维护计划</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<!-- js引入的示例,basePath使用EL表达式获取 -->
	<script>
		var mtpId = '<%=request.getParameter("maintainPlanId")%>';  
		var todoId = '<%=request.getParameter("todoId")%>';  
		var faultTypeParams = '<%=request.getParameter("faultTypeParams")%>';
		var faultTypeId = null;
		var faultTypeName = null;
		if(faultTypeParams != "null"){
			faultTypeParams = eval("("+faultTypeParams+")");
			faultTypeId = faultTypeParams.faultTypeId;
			faultTypeName = faultTypeParams.faultTypeName;
		}
		var loginUser = ItcMvcService.user.getUserId();
		var siteId = ItcMvcService.getUser().siteId;
		var list = "WO_WORKTEAM";
		
		var formRead = false;
		if(mtpId != "null"){
			formRead = true;
		}
		
	</script>
	<script type="text/javascript" src="${basePath}js/workorder/itc/newMaintainPlan.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
	<script type="text/javascript">
		var mtpType = "";
		
		$(document).ready(function() {
		
			$("#uploadfileTitle").iFold("init");
			$("#uploadfileTitle").iFold("show");  
			uploadform();
			//控制权限
			Priv.apply();
			
			if(mtpId == null || mtpId == 'null'){  //新建作业方案,参数定为0传过去
				initMTPPage(0); 
			}else{  //查看工作方案
				$.post(basePath + "workorder/maintainPlan/queryMTPDataById.do",{maintainPlanId:mtpId},
					function(mtpFullData){
						initMTPPage(mtpFullData);
					},"json");
			}
			
		});
		function triggerFaultTypeTreeSelect(data){
			var faultTypeCode = data.faultTypeCode;
			if(formRead == false && faultTypeCode.substring(0,2) =="SD"){
				$("#maintainPlanForm").iForm("setVal",{
			     	faultTypeId : data.id,
		        	faultTypeName:data.text
			    });
			}
			
		} 
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
                   			//"fileSizeLimit" : 10 * 1024,
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
  
  <body style="height: 100%;">
   	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button id="btn_close" type="button" class="btn btn-default" onclick="closeCurPage(true);">关闭</button>
	        </div>
	    	<div id="btn_mtp_saveDiv" class="btn-group btn-group-sm">
				<button id="btn_mtp_save" type="button" class="btn btn-success priv" privilege="wo-maitainPlan-oper" onclick="commitMTP()">保存</button>
			</div>
			<div id="btn_mtp_toWoDiv" class="btn-group btn-group-sm">
				<button id="btn_mtp_toWo" type="button" class="btn btn-success" onclick="mtpToWoBtn()">生成工单</button>
			</div>
	        <div id="btn_mtp_editDiv" class="btn-group btn-group-sm">
	        	<button id="btn_mtp_edit" type="button" class="btn btn-default priv" privilege="wo-maitainPlan-oper"  onclick="editMTP()">编辑</button>
	        </div>
	         <div id="btn_mtp_unavailableDiv" class="btn-group btn-group-sm">
	        	<button id="btn_mtp_unavailable" type="button" class="btn btn-default priv" privilege="wo-maitainPlan-oper"  onclick="unavailableMTP()">禁用</button>
	        </div>
	        
	    </div>
	</div>
    <div>
    	 <form id="maintainPlanForm" class="autoform"></form>
    </div>
    <div id="mtpDataGridDiv">
		<div id="title_tool" grouptitle="工具">
			<div class="margin-title-table">
				<form id="toolTableForm"><table id="toolTable" class="eu-datagrid"></table></form>
				<div id="toolBtnDiv" class="row btn-group-xs" >
					 <button id="btn_toolTable" onclick="appendTool();" type="button" class="btn btn-success">添加工具</button>
				</div>
			</div>
		</div>
    </div>
    <!-- 6.附件层 -->
	<div class="margin-group"></div>
	<div grouptitle="附件"  id="uploadfileTitle">
		<div class="margin-title-table" id="uploadfile">
			<form id="uploadform" style=""></form>
		</div>
	</div>
    
  </body>
</html>
