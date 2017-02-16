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
	<title>信息工单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script>
		var formRead = false;
	</script>
	<script type="text/javascript" src="${basePath}js/itsm/common/itsmConstant.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/itsm/core/infoWo/infoWoInfo.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/itsm/core/infoWo/infoWoCommon.js?ver=${iVersion}"></script>
	<script type="text/javascript">
		var infoWoId = '<%=request.getParameter("id")%>';  
		var loginUserId = ItcMvcService.user.getUserId();
		var siteId = ItcMvcService.getUser().siteId;
		var defKey="itsm_"+siteId.toLowerCase()+"_infowo";  //流程ID前缀
		var taskId ; //活动节点ID
		var processInstId = "";
		var createtime =""; //供审批框中显示
		var updatetime =""; //供审批框中显示
		var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
		var status = "";
		
		$(document).ready(function(){
			initLeftTree();
			FW.toggleSideTree(false);   //展开服务目录树
			/* form表单初始化 */
			$("#infoWoForm").iForm("init",{"fields":fields,"options":{validate:true,initAsReadonly:formRead}});
			//附件的控制
			$("#uploadfileTitle").iFold("init");
			$("#uploadfileTitle").iFold("show");  
			uploadform();
			
			initFormData(infoWoId);
			
			//控制权限
			Priv.apply();			
			FW.fixRoundButtons("#toolbar");
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
	function showAuditInfo(){
	    var businessData={};
	    var fields = [{
			title : "创建时间", 
		    id : "createtime",
		    type : "label"
			},{
			title : "修改时间", 
		    id : "updatetime",
		    type : "label"
		}];
		
		var data={'createtime':createtime,'updatetime':updatetime};
	    businessData['fields'] = fields;
	    businessData['data'] = data;
	    
	    var workFlow = new WorkFlow();
	    
	    workFlow.showAuditInfo(processInstId,JSON.stringify(businessData),auditInfoShowBtn,audit);
	}
	
	</script>

  </head>
  
  <body style="height: 100%; " class="bbox">
   	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button id="btn_close" type="button" class="btn btn-default" onclick="closeCurPage(true);">关闭</button>
	        </div>
	    	<div class="btn-group btn-group-sm">
				<button id="btn_audit" type="button" class="btn btn-default" onclick="audit();">审批</button>
			</div>
	        
	        <div class="btn-group btn-group-sm">
	        	<button id="btn_showPrincipal" type="button" class="btn btn-default" onclick="showPrincipal()">查看系统负责人</button>
	        	<button id="btn_print" type="button" class="btn btn-default" onclick="print()">打印</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button id="btn_auditInfo"type="button" class="btn btn-default" onclick="showAuditInfo();">审批信息</button>
	        </div>
	    </div>
	</div>
	<div  class="inner-title">
		<span id="inPageTitle">信息工单详情</span>
	</div>
	
    <div>
    	 <form id="infoWoForm" class="autoform"></form>
    </div>
    <div id="infoWoEquipmentDiv" >
			<div id="title_infoWoEquipment" grouptitle="设备清单">
				<div class="margin-title-table">
					<form id="mydatagridform"><table id="infoWoEquipmentTable" class="eu-datagrid"></table></form>
					<div id="woPriConfBtnDiv" class="row btn-group-xs" >
						 <button id="btn_infoWoEquipmentTable" onclick="appendInfoWoEquipment();" type="button" class="btn btn-success">添加</button>
					</div>
				</div>
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
