<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	<title>工单策划</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<script type="text/javascript">
		var formRead = false;
	</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<!-- js引入的示例,basePath使用EL表达式获取 -->
	<script type="text/javascript" src="${basePath}/js/itsm/common/itsmConstant.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/itsm/core/woPlan.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/itsm/common/workOrder.js?ver=${iVersion}"></script>
	<script type="text/javascript">
		var loginUserId = ItcMvcService.user.getUserId();
		var woId = '<%=request.getParameter("woId")%>';  
		var woStatus = '<%=request.getParameter("woStatus")%>'; 
		var faultTypeId = null;
		var processInstId ; //工单流程实例ID
		var taskId ;  //活动节点ID
		var candidateUsers ;  //当前活动节点的候选人
		var remainFaultFormDate="";  //遗留问题的输入信息
		var createtime =""; //供审批框中显示
		var updatetime =""; //供审批框中显示
		var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
		//var woDiscoverTime = 0 ; //工单报障时间
		var ownFlag = false; //标识此时工单是否在登录人的手上
		
		$(document).ready(function(){
			//添加左边类型树
			FW.addSideFrame({
				src:basePath+"page/itsm/core/woParamsConf/faultTypeTree.jsp?embbed=1",
				id:"itsmFaultTypeTree",
				conditions :[{tab:"^itsm_root$",tree:"^itsm_root_itsm_wo$"},
							 {tab:"^itsm_root$",tree:"^itsm_root_itsm_mtp$"},
							 {tab:"^itsm_root$",tree:"^itsm_root_itsm_proj$"},
							 {tab:"^itsm_root$",tree:"^itsm_root_itsm_woFaultTypeConf$"},
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
			initWoPlanPage(); 
			//附件的控制
			$("#uploadfileTitle").iFold("init");
			$("#uploadfileTitle").iFold("show");  
			uploadform();
			//控制权限
			Priv.apply();
		});
		//选中左边的服务目录树触发事件
		function triggerFaultTypeTreeSelect(data){
			if(ownFlag == false || woStatus != "endWorkReport"){ //如果不是完工汇报环节则直接退回，只有完工汇报环节才能修改服务目录、服务性质、故障描述
				return ; 
			}
			var selectType = data.faultTypeCode;
			var rootflag = data.type;
			if(selectType == "SD" && formRead == false && rootflag!="root"){
				$("#woBaseForm").iForm("setVal",{
			     	faultTypeId : data.id,
		        	faultTypeName:data.text
			    });
			}else if(selectType != "SCROOT" && selectType.substring(0,2) == "SC" && formRead == false){
				$("#woBaseForm").iForm("setVal",{
			     	serCharacterId : data.id,
		        	serCharacterName:data.text
			    });
			}
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
  
  <body style="height: 100%;">
   	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	 <button type="button" class="btn btn-default" onclick="closeCurPage(true)">关闭</button>
	        </div>
	    	<div id="btn_wo_operDiv" class="btn-group btn-group-sm">
	    		<button id="btn_wo_audit2" type="button" class="btn btn-default" onclick="audit()">审批</button>
			</div>
			<div id="btn_wo_operDiv2" class="btn-group btn-group-sm">
	            <button id="btn_wo_print2" type="button" class="btn btn-default" onclick="printWO()">打印</button>
			</div>
			<div id="btn_wo_selectJobPlanDiv" class="btn-group btn-group-sm">
	        	<button id="btn_wo_selectJobPlan" type="button" class="btn btn-default" onclick="selectStandJP();">选择标准作业方案</button>
	        </div>
	       
	        <div class="btn-group btn-group-sm">
	        	<button type="button" class="btn btn-default" onclick="showAuditInfo();">审批信息</button>
	        </div>
	    </div>
	</div>
	<div class="inner-title">
		<span id="inPageTitle">工单详情</span>
	</div>
    <div>
    	 <form id="woBaseForm" class="autoform"></form>
    	 <div id="title_plan" grouptitle="工作准备">
    		 <form id="woPlanForm" class="autoform"></form>
    	 </div>
    	 <div id="title_report" grouptitle="完工报告">
    	 	<form id="endWOReportForm" class="autoform"></form>
    	 </div>
    	 <div id="title_feedback" grouptitle="回访信息">
    	 	<form id="woAcceptanceForm" class="autoform"></form>
    	 </div>
    </div>
   
    <div id="woPlanDiv">
		<div id="title_tool" grouptitle="工具">
			<div class="margin-title-table">
				<form><table id="toolTable" class="eu-datagrid"></table></form>
				<div id="toolBtnDiv" class="row btn-group-xs" >
					 <button id="btn_toolTable" onclick="appendTool();" type="button" class="btn btn-success">添加工具</button>
				</div>
			</div>
		</div>
		<div class="margin-group"></div>
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
