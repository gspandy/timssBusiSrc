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
	<title>新建知识</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script>
		var formRead = false;
	</script>
	<script type="text/javascript" src="${basePath}js/itsm/common/itsmConstant.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/itsm/core/knowledge.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<script type="text/javascript">
		var loginUserId = ItcMvcService.user.getUserId();
		var siteId = ItcMvcService.getUser().siteId;
		var knowledgeId = '<%=request.getParameter("klId")%>'; 
		var prombleId = '<%=request.getParameter("prombleId")%>'; 
		var defKey="itsm_yudean_kl";  //流程ID前缀
		var processInstId = "";
		var createtime =""; //供审批框中显示
		var updatetime =""; //供审批框中显示
		var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
		var taskId ; //活动节点ID
		var klStatus =  '<%=request.getParameter("klStatus")%>';
		
		$(document).ready(function(){
			FW.addSideFrame({
				src:basePath+"page/itsm/core/woParamsConf/faultTypeTree.jsp?embbed=1",
				id:"itsmFaultTypeTree",
				conditions :[{tab:"^itsm_root$",tree:"^itsm_root_itsm_wo$"},
							 {tab:"^itsm_root$",tree:"^itsm_root_itsm_mtp$"},
							 {tab:"^itsm_root$",tree:"^itsm_root_itsm_proj$"},
							 {tab:"^itsm_root$",tree:"^itsm_root_itsm_woFaultTypeConf$"},
							 {tab:"^itsm_root$",tree:"^itsm_root_itsm_normKLBase$"},
							 {tab:"^openItsmKnowledge.+"},
							 {tab:"^IT.+"},
							 {tab:"^newItsm.+"},
							 {tab:"^itsmInfo.+"},
							 {tab:"^newItsmMTP.+"},
							 {tab:"^openItsmMTP.+"},
							 {tab:"^newItsmJP.+"},
							 {tab:"^openItsmJP.+"},
							 {tab:"^itsmPlanInfo.+"},
							 {tab:"^newItsmFaultType.+"},
							 {tab:"^openItsmFaultType.+"}
							]
							
			});  
			/* form表单初始化 */
			$("#konwledgeForm").iForm("init",{"fields":fields,"options":{validate:true,initAsReadonly:formRead}});
			
			if (knowledgeId!="null" && typeof(knowledgeId)!="undefined" && knowledgeId!=0){
			   	setKLFormVal(knowledgeId);
			}else if(prombleId!="null" && typeof(prombleId)!="undefined" && prombleId!=0){
				setKLFormByPrombleVal(prombleId);
			}else{
				$("#btn_kl_audit1").hide();
				$("#btn_auditInfo").hide();
				$("#btn_kl_deleteDiv").hide();
				$("#konwledgeForm").iForm("hide",["currStatus","createuserName","createdate"]);
			}
			//附件的控制
			$("#uploadfileTitle").iFold("init");
			$("#uploadfileTitle").iFold("show");  
			uploadform();
			//控制权限
			Priv.apply();
			
			FW.fixRoundButtons("#toolbar");
		});
		
		
	function triggerFaultTypeTreeSelect(data){
		var selectType = data.faultTypeCode;
		var rootflag = data.type;
		if(selectType == "SD" && formRead == false && rootflag!="root"){
			$("#konwledgeForm").iForm("setVal",{
		     		typeId : data.id,
		        	typeName:data.text
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
	function showDiagram(){
	    var workFlow = new WorkFlow();
	    workFlow.showDiagram(defKey);
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
	    	<div id="btn_wo_operDiv" class="btn-group btn-group-sm">
				<button id="btn_kl_commit" type="button" class="btn btn-default" onclick="commitKL('commit');">提交</button>
				<button id="btn_kl_audit1" type="button" class="btn btn-default" onclick="audit();">审批</button>
			</div>
	        
	        <div id="btn_kl_deleteDiv" class="btn-group btn-group-sm">
	        	<button id="btn_kl_obsolete" type="button" class="btn btn-default" onclick="obsoleteKL();" style="display: none;">作废</button>
	        	<button id="btn_kl_print1" type="button" class="btn btn-default" onclick="printKL()">打印</button>
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
		<span id="inPageTitle">新建知识</span>
	</div>
	
    <div>
    	 <form id="konwledgeForm" class="autoform"></form>
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
