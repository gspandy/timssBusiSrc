<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
	<title>登塔申请</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script>
		var formRead = false;
	</script>
	<script type="text/javascript" src="${basePath}js/workorder/core/towerApply.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<script type="text/javascript">
		var towerApplyId = '<%=request.getParameter("id")%>'; 
		var taskId  = '<%=request.getParameter("taskId")%>'; //活动节点ID
		var createtime =""; //供审批框中显示
		var updatetime =""; //供审批框中显示
		var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
		var loginUserId = ItcMvcService.user.getUserId();
		var siteId = ItcMvcService.getUser().siteId;
		var processInstId ; //工单流程实例ID
		var taskId ; //活动节点ID
		
		var defKey="workorder_"+siteId.toLowerCase()+"_towerapply";  //流程ID前缀
		var operPriv = false; //操作权限
		var applyStatus ="";
		
		$(document).ready(function(){
			/* form表单初始化 */
			$("#towerApplyForm").iForm("init",{"fields":fields,"options":{validate:true,initAsReadonly:formRead}});
			if(towerApplyId != null && towerApplyId != "null"){
				setFormVal(towerApplyId);
			}
			
			if(towerApplyId == null || towerApplyId == "null"){
       			$("#inPageTitle").html("新建登塔申请");
       			$("#btn_wo_edit1").hide();
       			$("#btn_wo_delete").hide();
       			$("#btn_wo_obsolete").hide();
       			$("#towerApplyForm").iForm("endEdit",["applyStatus","applyCode"]);
       			$("#btn_wo_audit1").hide();
       			$("#btn_auditInfo").hide();
       			$("#btn_towerApply_print").hide();
			}
			FW.fixRoundButtons("#toolbar");
			_itc_addellipsis();
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
	    		<button id="btn_wo_save1" type="button" class="btn btn-default" onclick="commitTowerApply('save');">暂存</button>
				<button id="btn_wo_commit" type="button" class="btn btn-default" onclick="commitTowerApply('commit');">提交</button>
				<button id="btn_wo_audit1" type="button" class="btn btn-default" onclick="audit();">审批</button>
			</div>
			
	        
	        <div id="btn_wo_deleteDiv" class="btn-group btn-group-sm">
	        	<button id="btn_wo_delete" type="button" class="btn btn-default" onclick="deleteTowerApply();">删除</button>
	        	<button id="btn_wo_obsolete" type="button" class="btn btn-default" onclick="obsoleteTowerApply();" style="display: none;">作废</button>
	        </div>
	        <div id="btn_wo_printDiv" class="btn-group btn-group-sm">
	        	<button id="btn_towerApply_print" type="button" class="btn btn-default" onclick="printTowerApply();">打印</button>
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
		<span id="inPageTitle">登塔详情</span>
	</div>
	
    <div>
    	 <form id="towerApplyForm" class="autoform"></form>
    </div>
    
  </body>
</html>
