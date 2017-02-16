<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>新建工单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script type="text/javascript">_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script type="text/javascript" src="${basePath}js/workorder/swf/workOrderAdd.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/workorder/common/util.js?ver=${iVersion}"></script>
	<script type="text/javascript">
		//状态英文字符串
		var draftStr = '${draftStr}';//0草稿
		var woCommitStr = '${woCommitStr}';//1提交
		var dutyConfirmDefectStr = '${dutyConfirmDefectStr}';//2值长确认缺陷
		var expertDealStr = '${expertDealStr}';//3专工下发缺陷
		
		var planningGroupAuditStr = '${planningGroupAuditStr}';//13策划组重新分配
		var safeClearAuditStr = '${safeClearAuditStr}';//14生安分部审批
		var dutyClearAuditStr = '${dutyClearAuditStr}';//15值长清除缺陷
		
		var supervisorPlanStr = '${supervisorPlanStr}';//4项目负责人策划
		var supervisorReportStr = '${supervisorReportStr}';//5项目负责人回填
		
		var delayEquipAuditStr = '${delayEquipAuditStr}';//7设备部审批
		var delaySafeAuditStr = '${delaySafeAuditStr}';//8生安分部审批
		var delayOperationAuditStr = '${delayOperationAuditStr}';//9运行部审批
		var delayLeaderAuditStr = '${delayLeaderAuditStr}';//10分管生产领导审批		
		var delayDutyAuditStr = '${delayDutyAuditStr}';//11值长确认延期
		var delayDutyRestartStr = '${delayDutyRestartStr}';//12值长启动工单
		
		var delayConfirmOverStr = '${delayConfirmOverStr}';//6值长确认结束
		var obseleteStr = '${obseleteStr}';//16已作废
		var doneStr = '${doneStr}';//17已完
		
		var priorityArray = '${priorityArray}';//优先级
		priorityArray = eval(priorityArray);
		var woId = '${woId}';
	
		var mtpId = '<%=request.getParameter("mtpId")%>'; 
		var mtpType = '<%=request.getParameter("mtpType")%>'; 
		var woQxId = '<%=request.getParameter("woQxId")%>'; 
		var equipParams = '<%=request.getParameter("equipParams")%>';

		var equipId = null;
		var equipName = null;
		var equipCode = null;
		
		var woStatus;
		var woIsDelay;
		var loginUserId = ItcMvcService.user.getUserId();
		var siteId = ItcMvcService.getUser().siteId;
		var createtime = ""; //供审批框中显示
		var updatetime = ""; //供审批框中显示
		var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
		var processInstId ; //工单流程实例ID
		var taskId ; //活动节点ID

		$(document).ready(function(){
			//根据id是否存在判断是新建还是查看,draft状态也可能id存在
			if(woId != null && woId != ""){
				setFormVal(woId);
			}
			else{
				//按钮控制
				setWoButtonPriv();
				$( ".priv" ).hide();
				$("#btn_wo_save").show();
				$("#btn_wo_commit").show();
				FW.fixRoundButtons("#toolbar");
				
				//form表单初始化
				$("#workOrderForm").iForm("init",{"fields":fields,"options":{validate:true}});
				$("#workOrderForm").iForm("hide","currStatus");
				$("#workOrderForm").iForm("hide","faultDegreeCode");
				$("#workOrderForm").iForm("hide","woExpertHandleStyle");
				$("#workOrderForm").iForm("hide","woIsReAssign");
				$("#f_priorityId").iCombo("init",{
				    data :priorityArray
				});
			
				//设置标题
				$("#workOrderFormTitle").html("新建工单");

       			//表单赋值
				if(equipParams != ""){
					equipParams = eval("("+equipParams+")");
					equipId = equipParams.equipId;
					equipName = equipParams.equipName;
					equipCode = equipParams.equipCode;
					//设置表单值
					if(equipName != null && equipName.length>0){
						$("#workOrderForm").iForm("setVal",{
							"equipId":equipId,
							"equipName":equipName,
							"equipNameCode":equipCode
						});	
					}
				}
				//生成和选择设备树(通过回调函数选中设备)
				initWoInfoAssetTree();
			}
			
			//父工单超链接
			$("body").on("click", "#readonly_f_parentWOCode", function(e){
				openWoPageByCode($("#readonly_f_parentWOCode").text());
			});
		});
	</script>
	<style>
		#readonly_f_parentWOCode{
			color:#428bca;
			cursor:pointer;
		}
		#readonly_f_parentWOCode:hover{
			text-decoration:underline;
		}
	</style>	
</head>

<body style="height: 100%; " class="bbox">
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	<div id="toolbar" class="btn-toolbar ">
		<div class="btn-group btn-group-sm">
			<button id="btn_close" type="button" class="btn btn-default" onclick="closeCurPage(true);">关闭</button>
		</div>
		<div id="btn_wo_operDiv" class="btn-group btn-group-sm">
			<button id="btn_wo_save" type="button" class="btn btn-default priv" privilege="WO_SAVE" onclick="commitWO('save');">暂存</button>
			<button id="btn_wo_edit" type="button" class="btn btn-default priv" privilege="WO_EDIT" onclick="editWO();">编辑</button>
			<button id="btn_wo_commit" type="button" class="btn btn-default priv" privilege="WO_COMMIT" onclick="commitWO('commit');">提交</button>
			<button id="btn_wo_audit" type="button" class="btn btn-default priv" privilege="WO_AUDIT" onclick="audit();">审批</button>
		</div>
		<div id="btn_wo_deleteDiv" class="btn-group btn-group-sm">
			<button id="btn_wo_delete" type="button" class="btn btn-default priv" privilege="WO_DELETE" onclick="deleteWO();">删除</button>
			<button id="btn_wo_obsolete" type="button" class="btn btn-default priv" privilege="WO_AUDIT" onclick="obsoleteWO();" style="display: none;">作废</button>
		</div>
		<div class="btn-group btn-group-sm">
			<button id="btn_auditInfo" type="button" class="btn btn-default" onclick="showAuditInfo();">审批信息</button>
		</div>		
	</div>
</div>

<div id="workOrderFormDiv">
<div class="inner-title">
	<span id="workOrderFormTitle">工单详情</span>
</div>
<div>
	<form id="workOrderForm" class="autoform"></form>
</div>
</div>

<div id="planFormDiv" style="display:none">
<div class="inner-title">
	<span id="planFormTitle">策划信息</span>
</div>
<div>
	<form id="planForm" class="autoform"></form>
</div>
</div>


<div id="reportFormDiv" style="display:none">
<div class="inner-title">
	<span id="reportFormTitle">回填信息</span>
</div>
<div style="margin-bottom: 10px;">
	<form id="reportForm" class="autoform"></form>
</div>
</div>
</body>
</html>
