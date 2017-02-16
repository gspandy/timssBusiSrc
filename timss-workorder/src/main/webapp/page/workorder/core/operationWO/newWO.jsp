<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
		if(woStatus !="null" &&woStatus!="newWO"){
			formRead = true;
		}
		
	</script>
	<script type="text/javascript" src="${basePath}js/workorder/core/newWorkOrder.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<script type="text/javascript">
		var woId = '<%=request.getParameter("woId")%>'; 
		var mtpId = '<%=request.getParameter("mtpId")%>'; 
		var mtpType = '<%=request.getParameter("mtpType")%>'; 
		var woQxId = '<%=request.getParameter("woQxId")%>'; 
		var equipParams = '<%=request.getParameter("equipParams")%>';
		var createtime =""; //供审批框中显示
		var updatetime =""; //供审批框中显示
		var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
		var equipId = null;
		var equipName = null;
		var equipCode = null;
		if(equipParams != "null"){
			equipParams = eval("("+equipParams+")");
			equipId = equipParams.equipId;
			equipName = equipParams.equipName;
			equipCode = equipParams.equipCode;
			//增加equipCode的赋值
		}
		var hasRollback = "no";  //标识是否是回退到报障人的工单（仅新建状态时有用，用于回退提交后取消的情况）
		var loginUserId = ItcMvcService.user.getUserId();
		var siteId = ItcMvcService.getUser().siteId;
		
		var candidateUsers;  //当前活动节点的候选人
		var processInstId ; //工单流程实例ID
		var taskId ; //活动节点ID
		
		var defKey="workorder_"+siteId.toLowerCase()+"_wo";  //流程ID前缀
		
		$(document).ready(function(){
			/* form表单初始化 */
			$("#workOrderForm").iForm("init",{"fields":fields,"options":{validate:true,initAsReadonly:formRead}});
			//当选择了左边设备树然后新建时，直接将设备信息待过去
			if(equipId){
				$("#workOrderForm").iForm("setVal",{"equipId":equipId,"equipName":equipName,"equipNameCode":equipCode});
			}
			if(woId != null && woId != "null"){
				setFormVal(woId);
			}
			if(mtpId != null && mtpId != "null"){
				setFormValFromMtp(mtpId);
			}
			if(woQxId != null && woQxId != "null"){
				setFormValFromQx(woQxId);
			}else{
    			$("#workOrderForm").iForm("hide","woDefectCode");
    		}
			if(woId == null || woId == "null"){
       			$("#inPageTitle").html("新建工单");
       			$("#btn_wo_edit1").hide();
       			$("#btn_wo_delete").hide();
       			$("#btn_wo_obsolete").hide();
       			$("#workOrderForm").iForm("endEdit","currStatus");
       			$("#btn_wo_audit1").remove();
       			
       			$("#btn_auditInfo").hide();
       			
       			if(mtpId==null ||mtpId=="null"){
       				FW.createAssetTree({multiSelect:false});
       				FW.setTreeStat("fold");
       			}
			}
			FW.fixRoundButtons("#toolbar");
			_itc_addellipsis();
			//展开树，显示选中的树
			if(equipId != null){
   				setTimeout(function(){
   							document.getElementById("itcEmbbedAssetTree").contentWindow.expandForHintById(equipId);
   							},500);
   			}
   			
		});
	function onSelectAssetTreeNode(node){
	}	
	function passAssetSelect(data){
		 $("#workOrderForm").iForm("setVal",{
	        equipId : data.id,
	        equipNameCode:data.assetCode,
	        equipName : data.text
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
	    		<button id="btn_wo_save1" type="button" class="btn btn-default" onclick="commitWO('save');">暂存</button>
				<button id="btn_wo_commit" type="button" class="btn btn-default" onclick="commitWO('commit');">提交</button>
				<button id="btn_wo_edit1" type="button" class="btn btn-default" onclick="editWO();">编辑</button>
				<button id="btn_wo_audit1" type="button" class="btn btn-default" onclick="audit();">审批</button>
			</div>
			
	        
	        <div id="btn_wo_deleteDiv" class="btn-group btn-group-sm">
	        	<button id="btn_wo_delete" type="button" class="btn btn-default" onclick="deleteWO();">删除</button>
	        	<button id="btn_wo_obsolete" type="button" class="btn btn-default" onclick="obsoleteWO();" style="display: none;">作废</button>
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
    	 <form id="handlerStyleForm" class="autoform"></form>
    </div>
    
  </body>
</html>
