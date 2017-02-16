<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head> 
	<title>工单策划</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<!-- js引入的示例,basePath使用EL表达式获取 -->
	<script type="text/javascript" src="${basePath}js/workorder/core/woPlan.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
	<script type="text/javascript">
		var loginUserId = ItcMvcService.user.getUserId();
		var woId = '<%=request.getParameter("woId")%>';  
		var woStatus = '<%=request.getParameter("woStatus")%>'; 
		var ptwId = 0;
		var processInstId ; //工单流程实例ID
		var taskId ;  //活动节点ID
		var candidateUsers ;  //当前活动节点的候选人
		var remainFaultFormData="";  //遗留问题的输入信息
		var createtime =""; //供审批框中显示
		var updatetime =""; //供审批框中显示
		var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
		$(document).ready(function(){
			initWoPlanPage(woId); 
			woPrintHelp("#btn_wo_printWind","windPrint");
			woPrintHelp("#btn_wo_printBdz","bdzPrint");
		});
		
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
  
  <body style="height: 100%;">
   	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	 <button type="button" class="btn btn-default" onclick="closeCurPage(true)">关闭</button>
	        </div>
	    	<div id="btn_wo_operDiv" class="btn-group btn-group-sm">
	    		<button id="btn_wo_audit2" type="button" class="btn btn-default" onclick="audit()">审批</button>
	            <button id="btn_wo_save2" type="button" class="btn btn-default" onclick="saveWO()">暂存</button>
			</div>
			<div id="btn_wo_selectJobPlanDiv" class="btn-group btn-group-sm">
	        	<button id="btn_wo_selectJobPlan" type="button" class="btn btn-default" onclick="selectStandJP();">选择标准作业方案</button>
	        </div>
	        <div id="btn_wo_operPtwDiv" class="btn-group btn-group-sm">
	        	<button id="btn_wo_queryPtw" type="button" class="btn btn-default" onclick="queryPtw();">查看工作票</button>
	        	<button id="btn_wo_backWoPlan" type="button" class="btn btn-default" onclick="backToPlan();">回退到策划</button>
	        	<button id="btn_wo_newPtw" type="button" class="btn btn-default" onclick="newPtw();">新建工作票</button>
	        </div>
	         <div id="btn_wo_PrintDiv" class="btn-group btn-group-sm">
	        	<button id="btn_wo_printWind" type="button" class="btn btn-default" >风机打印</button>
	        	<button id="btn_wo_printBdz" type="button" class="btn btn-default" >变电站打印</button>
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
    	 <form id="woPlanForm" class="autoform"></form>
    	 <form id="endWOReportForm" class="autoform"></form>
    	 <form id="woAcceptanceForm" class="autoform"></form>
    </div>
   
    <div id="woPlanDiv">
    	<div id="title_preHazard" grouptitle="安全注意事项">
			<div class="margin-title-table">
				<table id="preHazardTable" class="eu-datagrid"></table>
				<div id="preHazardBtnDiv" class="row btn-group-xs" >
				    <button id="btn_preHazardTable" onclick="appendDataGridRow('preHazardTable');"  type="button" class="btn btn-success">添加安全注意事项</button>
				</div>
			</div>
		</div>
		<div class="margin-group"></div>
		
		<div id="title_tool" grouptitle="物料">
			<div class="margin-title-table">
				<form id="toolTableForm"><table id="toolTable" class="eu-datagrid"></table></form>
				<div id="toolBtnDiv" class="row btn-group-xs" >
					 <button id="btn_toolTable" onclick="appendTool();" type="button" class="btn btn-success">添加物料</button>
				</div>
			</div>
		</div>
		<div class="margin-group"></div>
		
		<div id="title_task" grouptitle="工作内容">
			<div class="margin-title-table">
				<table id="taskTable" class="eu-datagrid"></table>
				<div id="taskBtnDiv" class="row btn-group-xs">
				    <button id="btn_taskTable" onclick="appendDataGridRow('taskTable')" type="button" class="btn btn-success">添加工作内容</button>
				</div>
			</div>
		</div>
		<div class="margin-group"></div>
		
		<div id="title_worker" grouptitle="人员">
			<div class="margin-title-table">
				<table id="workerTable" class="eu-datagrid"></table>
				<div id="workerBtnDiv" class="row btn-group-xs">
				    <button id="btn_workerTable" onclick="appendDataGridRow('workerTable')" type="button" class="btn btn-success">添加人员</button>
				</div>
			</div>
		</div>
		
    </div>
   
  </body>
</html>
