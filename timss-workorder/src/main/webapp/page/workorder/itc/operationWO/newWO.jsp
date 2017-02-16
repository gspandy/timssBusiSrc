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
	<title>新建工单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script>
		var woStatus = '<%=request.getParameter("woStatus")%>';
		var formRead = false;
		if(woStatus !="null" &&woStatus!="newWO" &&woStatus != "draft"){
			formRead = true;
		}
		
	</script>
	<script type="text/javascript" src="${basePath}js/workorder/itc/newWorkOrder.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<script type="text/javascript">
		var woId = '<%=request.getParameter("woId")%>'; 
		var mtpId = '<%=request.getParameter("mtpId")%>'; 
		var mtpType = '<%=request.getParameter("mtpType")%>'; 
		var faultTypeParams = '<%=request.getParameter("faultTypeParams")%>';
		var createtime =""; //供审批框中显示
		var updatetime =""; //供审批框中显示
		var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
	
		var woType = null;
		var faultTypeId = null;
		var faultTypeName = null;
		var isITCSer = false;
		var priorityId = null;
		if(faultTypeParams != "null"){
			faultTypeParams = eval("("+faultTypeParams+")");
			faultTypeId = faultTypeParams.faultTypeId;
			faultTypeName = faultTypeParams.faultTypeName;
		}
		
		var loginUserId = ItcMvcService.user.getUserId();
		var siteId = ItcMvcService.getUser().siteId;
		
		var candidateUsers;  //当前活动节点的候选人
		var processInstId ; //工单流程实例ID
		var taskId ; //活动节点ID
		
		var defKey="workorder_"+siteId.toLowerCase()+"_wo";  //流程ID前缀
		
		$(document).ready(function(){
			FW.addSideFrame({
				src:basePath+"page/workorder/itc/woParamsConf/faultTypeTree.jsp?embbed=1",
					id:"faultTypeTree",
					conditions :[{tab:"^equmaintain$",tree:"^equmaintain_euqbz$"},
								 {tab:"^equmaintain$",tree:"^equmaintain_euqwx$"},
								 {tab:"^equmaintain$",tree:"^equmaintain_equproj$"},
								 {tab:"^equmaintain$",tree:"^equmaintain_itcFaultTypeConf$"},
								 {tab:"^newWO.+"},
								 {tab:"^woInfo.+"},
								 {tab:"^newMTP.+"},
								 {tab:"^openMTP.+"},
								 {tab:"^newJP.+"},
								 {tab:"^openJP.+"},
								 {tab:"^woPlanInfo.+"},
								 {tab:"^WO.+"},
								 {tab:"^newWoFaultType.+"},
								 {tab:"^openWoFaultType.+"}
								]
								
				}); 
			$.ajaxSetup({'async':false}); 
			//判断登录用户是否是客服
			isCusSer();
			/* form表单初始化 */
			$("#workOrderForm").iForm("init",{"fields":fields,"options":{validate:true,initAsReadonly:formRead}});
			
			//附件的控制
			$("#uploadfileTitle").iFold("init");
			$("#uploadfileTitle").iFold("show");  
			uploadform();
			//控制权限
			Priv.apply();
			
			//当选择了左边设备树然后新建时，直接将设备信息带过去
			if(faultTypeId){
				$("#workOrderForm").iForm("setVal",{"faultTypeId":faultTypeId,"faultTypeName":faultTypeName});
			}
			
			if(woId != null && woId != "null"){
				setFormVal(woId);
			}else if(mtpId != null && mtpId != "null"){
				setFormValFromMtp(mtpId);
			}
			$.ajaxSetup({'async':true});
			
			$("#workOrderForm").iForm("setVal",{"priorityId":priorityId}); 
			if(woId == null || woId == "null"){  //点击新建产生的页面
				$("#inPageTitle").html("新建工单");
				$("#btn_wo_edit1Div").hide();
       			$("#btn_wo_deleteDiv").hide();
       			$("#workOrderForm").iForm("hide",["currStatus","currHandUserName"]);
       			$("#btn_wo_audit1").hide();
       			$("#btn_auditInfo").hide();
			}
			FW.fixRoundButtons("#toolbar");
		});
		
	
	function triggerFaultTypeTreeSelect(data){
		var selectType = data.faultTypeCode;
		var rootflag = data.type;
		if(selectType == "SD" && formRead == false && rootflag!="root"){
			$("#workOrderForm").iForm("setVal",{
		     	faultTypeId : data.id,
	        	faultTypeName:data.text
	        	
		    });
		}else if(selectType != "SCROOT" && selectType.substring(0,2) == "SC" && formRead == false){
			$("#workOrderForm").iForm("setVal",{
		     	serCharacterId : data.id,
	        	serCharacterName:data.text
	    	});
		}
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
	function isCusSer(){  //判断是否是客服
		$.post(basePath + "workorder/workorder/loginUserIsCusSer.do",{},
			function(data){
				if(data.result==true){
					//$("#workOrderForm").iForm("setVal",{"sendWoNow":"yes"});
					isITCSer = true;
				}else{
					//$("#workOrderForm").iForm("hide",["sendWoNow"]);
					isITCSer = false;
				}
			},"json");
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
  
  <body style="height: 100%; " class="bbox">
   	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button id="btn_close" type="button" class="btn btn-default" onclick="closeCurPage(true);">关闭</button>
	        </div>
	    	<div id="btn_wo_operDiv" class="btn-group btn-group-sm">
	    		<button id="btn_wo_save1" type="button" class="btn btn-default" onclick="commitWO('save');">暂存</button>
				<button id="btn_wo_commit" type="button" class="btn btn-default" onclick="commitWO('commit');">提交</button>
				<button id="btn_wo_audit1" type="button" class="btn btn-default" onclick="audit();">审批</button>
			</div>
	        
	        <div id="btn_wo_deleteDiv" class="btn-group btn-group-sm">
	        	<button id="btn_wo_delete" type="button" class="btn btn-default" onclick="deleteWO();">删除</button>
	        	<button id="btn_wo_obsolete" type="button" class="btn btn-default" onclick="obsoleteWO();" style="display: none;">作废</button>
	        	<button id="btn_wo_print1" type="button" class="btn btn-default" onclick="printWO()">打印</button>
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
		<span id="inPageTitle">工单详情</span>
	</div>
	
    <div>
    	 <form id="workOrderForm" class="autoform"></form>
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
