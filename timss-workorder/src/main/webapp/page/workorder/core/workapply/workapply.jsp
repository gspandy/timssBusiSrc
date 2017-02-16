<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.yudean.itc.util.Constant"%>
<%@ page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@ page import="com.yudean.itc.util.FileUploadUtil"%>
<%
	String sessionid = session.getId();
	String delKey = FileUploadUtil.getValidateStr((SecureUser)session.getAttribute(Constant.secUser), FileUploadUtil.DEL_ALL);
 %>
<!DOCTYPE html>
<html>
  <head>
	<title>开工申请</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script>
		var formRead = false;
		var woapplyId = '<%=request.getParameter("woapplyId")%>'; 
		var taskId  = '<%=request.getParameter("taskId")%>'; //活动节点ID
		var sessionid = '<%=sessionid%>';
		var delKey = '<%=delKey%>';
	</script>
	<script type="text/javascript" src="${basePath}js/workorder/core/workapply.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/workorder/core/workapplydatagrid.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/workorder/common/safeItemTool.js?ver=${iVersion}"></script>
	<link rel="stylesheet" type="text/css" href="${basePath}css/workorder/ptoSafe.css"></link>
	<script type="text/javascript">
		var createtime =""; //供审批框中显示
		var updatetime =""; //供审批框中显示
		var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
		var loginUserId = ItcMvcService.user.getUserId();
		var siteId = ItcMvcService.getUser().siteId;
		var processInstId ; //工单流程实例ID
		var operPriv = false; //操作权限
		var defKey="workorder_"+siteId.toLowerCase()+"_woapply";  //流程ID前缀
		var applyStatus ="";
		
		$(document).ready(function(){
			/* form表单初始化 */
			$("#workapplyForm").iForm("init",{"fields":fields,"options":{validate:true,initAsReadonly:formRead}});
			$("#safeItemTest").iFold("init");
			$("#safeItemTest").iFold("show"); 
			//initEmptySafeItemDatagrid("safeItemTest");
			$("#safeInformForm").iForm("init",{"fields":safeinformfields,"options":{validate:true,initAsReadonly:formRead}});
			//初始化外来队伍施工人员、风险评估
			initdatagrid();
			$("#uploadfileTitle").iFold("init");
			$("#uploadfileTitle").iFold("show");
			initUploadform();
			if(woapplyId != null && woapplyId != "null"){
				setFormVal(woapplyId);
			}else{
				//安全交底初始化
				$("#safeInformForm").iForm("init",{"fields":safeinformfields,"options":{validate:true,initAsReadonly:formRead}});
				initEmptySafeItemDatagrid("safeItemTest");
				//初始化外来队伍施工人员、风险评估
				//initdatagrid();
			}
			
			if(woapplyId == null || woapplyId == "null"){
       			$("#inPageTitle").html("新建开工申请");
       			$("#workapplyForm").iForm("hide",["applyStatus"]);
       			$("#btn_wo_edit1").hide();
       			$("#btn_wo_delete").hide();
       			$("#btn_wo_obsolete").hide();
       			$("#btn_wo_audit1").hide();
       			$("#btn_woapply_print").hide();
       			$("#btn_auditInfo").hide();
			}
			FW.fixRoundButtons("#toolbar");
			//_itc_addellipsis();
   			
		});

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
	    		<button id="btn_wo_save1" type="button" class="btn btn-default" onclick="commitWoapply('save');">暂存</button>
				<button id="btn_wo_commit" type="button" class="btn btn-default" onclick="commitWoapply('commit');">提交</button>
				<button id="btn_wo_audit1" type="button" class="btn btn-default" onclick="audit();">审批</button>
			</div>
			
	        
	        <div id="btn_wo_deleteDiv" class="btn-group btn-group-sm">
	        	<button id="btn_wo_delete" type="button" class="btn btn-default" onclick="deleteWoapply();">删除</button>
	        	<button id="btn_wo_obsolete" type="button" class="btn btn-default" onclick="obsoleteWoapply();" style="display: none;">作废</button>
	        </div>
	         <div id="btn_wo_printDiv" class="btn-group btn-group-sm">
	        	<button id="btn_woapply_print" type="button" class="btn btn-default" onclick="printWoapply();">打印</button>
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
		<span id="inPageTitle">开工申请详情</span>
	</div>
	
    <div>
    	 <form id="workapplyForm" class="autoform"></form>
    </div>
    
    <script type="text" id="safeAddContentTpl">
		<div class="wrap-underline wrap-underline-single">
		    <span class="safe-number-span"></span>
		    <span class="safe-input-content" style="display: none;"></span>
			<input class="safe-input" type="text" onkeyup="bindSptoItemEvent(this);"/>
		</div>
	</script>
	
	
    <div id="woPlanDiv">
    	<div id="title_safeInform" grouptitle="安全交底">
			 <form id="safeInformForm" class="autoform"></form>
			<div id="safeItemTest" grouptitle="交底内容:"></div>
		</div>
		<div class="margin-group"></div>
		
		<div id="title_worker" grouptitle="外来队伍施工人员">
			<div class="margin-title-table">
				<form><table id="workerTable" class="eu-datagrid"></table></form>
				<!-- <div id="workerBtnDiv" class="row btn-group-xs" >
					 <button id="btn_workerTable" onclick="appendDataGridRow('workerTable')" type="button" class="btn btn-success">添加人员</button>
				</div> -->
				<div id="workerBtnDiv" class="row btn-group-xs" >
					 <button id="btn_workerTable" onclick="openAddDialog(null,'worker')" type="button" class="btn btn-success">添加人员</button>
				</div>
			</div>
		</div>
		<div class="margin-group"></div>
		
		<div id="title_risk" grouptitle="风险评估">
			<div class="margin-title-table">
				<table id="riskTable" class="eu-datagrid"></table>
				<!-- <div id="riskAssessmentBtnDiv" class="row btn-group-xs">
				    <button id="btn_riskAssessmentTable" onclick="appendDataGridRow('riskAssessmentTable')" type="button" class="btn btn-success">添加风险</button>
				</div> -->
				<div id="riskBtnDiv" class="row btn-group-xs">
				    <button id="btn_riskTable" onclick="openAddDialog(null,'risk')" type="button" class="btn btn-success">添加风险</button>
				</div>
			</div>
		</div>
		<div class="margin-group"></div>
		<div grouptitle="附件"  id="uploadfileTitle">
			<div class="margin-title-table" id="uploadfile">
				<form id="uploadform"></form>
			</div>
		</div>
    </div>
  </body>
</html>
