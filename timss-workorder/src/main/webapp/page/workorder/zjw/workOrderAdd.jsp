<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>新建工单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script type="text/javascript">_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script type="text/javascript" src="${basePath}js/workorder/zjw/workOrderAdd.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/workorder/common/util.js?ver=${iVersion}"></script>
	<script type="text/javascript">
		//状态英文字符串
		var DRAFT = '${DRAFT}';//0草稿
		var WORK_ORDER_COMMIT = '${WORK_ORDER_COMMIT}';//1提交
		
		var MONITOR_AUDIT = '${MONITOR_AUDIT}';//2班长审核
		var CHAIRMAN_AUDIT = '${CHAIRMAN_AUDIT}';//3场长(助理)审核
		var MONITOR_DISTRIBUTE = '${MONITOR_DISTRIBUTE}';//4班长分发
		
		var WORK_ORDER_PLAN = '${WORK_ORDER_PLAN}';//5工作策划
		var WORK_TICKET_PROCEDURE = '${WORK_TICKET_PROCEDURE}';//6工作票流程
		
		var WORK_ORDER_REPORT = '${WORK_ORDER_REPORT}';//7填写故障缺陷处理单
		
		var WORK_ORDER_CHECK = '${WORK_ORDER_CHECK}';//8验收工作
		
		var SHIELD_OPR_DIRECTOR_AUDIT = '${SHIELD_OPR_DIRECTOR_AUDIT}';//9运检主管审批
		var SHIELD_OPR_MINISTER_AUDIT = '${SHIELD_OPR_MINISTER_AUDIT}';//10运检部长审批
		var SHIELD_SAFE_CLERK_AUDIT = '${SHIELD_SAFE_CLERK_AUDIT}';//11生安专责审批
		var SHIELD_SAFE_MINISTER_AUDIT = '${SHIELD_SAFE_MINISTER_AUDIT}';//12生安部部长审批
		var SHIELD_DUTY_RESET = '${SHIELD_DUTY_RESET}';//13值长复位
		var SUSPEND = '${SUSPEND}';//14挂起
		
		var OBSELETE = '${OBSELETE}';//15已作废
		var DONE = '${DONE}';//16已完
		
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
		var loginUserId = ItcMvcService.user.getUserId();
		var siteId = ItcMvcService.getUser().siteId;
		var createtime = ""; //供审批框中显示
		var updatetime = ""; //供审批框中显示
		var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
		var processInstId ; //工单流程实例ID
		var taskId ; //活动节点ID
		var isToPTW = null;  //是否走工作票
		
		$(document).ready(function(){
			//根据id是否存在判断是新建还是查看,draft状态也可能id存在
			if(woId != null && woId != ""){
				setFormVal(woId);
			}else{
				//按钮控制
				setWoButtonPriv();
				$( ".priv" ).hide();
				$("#btn_deallog").hide();//新建时打印按钮隐藏
				$("#btn_wo_save").show();
				$("#btn_wo_commit").show();
				FW.fixRoundButtons("#toolbar");
				
				//form表单初始化
				$("#workOrderForm").iForm("init",{"fields":fields,"options":{validate:true}});
				$("#workOrderForm").iForm("hide","relMatApplyIds");
				$("#workOrderForm").iForm("hide","relPtwIds");
				$("#workOrderForm").iForm("hide","endReportUserName");
				$("#workOrderForm").iForm("hide","woMaintainExecutorName");
				$("#workOrderForm").iForm("hide","createUserName");
				$("#workOrderForm").iForm("hide","createdate");
				$("#workOrderForm").iForm("hide","currStatus");
				$("#workOrderForm").iForm("hide","isToWorkTicket");
				$("#f_priorityId").iCombo("init",{
				    data :priorityArray
				});
			
				//设置标题
				$("#workOrderFormTitle").html("新建缺陷");
				
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
				//生成和选择设备树
				initWoInfoAssetTree();
			}
			initDefectHint(); 
			
			//父工单超链接
			$("body").on("click", "#readonly_f_parentWOCode", function(e){
				openWoPageByCode($("#readonly_f_parentWOCode").text());
			});
			intPrintButton();
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
	<div id="toolbar" class="btn-toolbar">
		<div class="btn-group btn-group-sm">
			<button id="btn_close" type="button" class="btn btn-default" onclick="closeCurPage(true);">关闭</button>
		</div>
		<div id="btn_wo_operDiv" class="btn-group btn-group-sm">
			<button id="btn_wo_save" type="button" class="btn btn-default priv" privilege="WO_SAVE_ZJW" onclick="commitWO('save');">暂存</button>
			<button id="btn_wo_edit" type="button" class="btn btn-default priv" privilege="WO_EDIT_ZJW" onclick="editWO();">编辑</button>
			<button id="btn_wo_commit" type="button" class="btn btn-default priv" privilege="WO_COMMIT_ZJW" onclick="commitWO('commit');">提交</button>
			<button id="btn_wo_audit" type="button" class="btn btn-default priv" privilege="WO_AUDIT_ZJW" onclick="audit();">审批</button>
			<button id="btn_monitor_audit1" type="button" class="btn btn-default monitorAudit" onclick="monitorAudit('REPAIR_NOW');" style="display:none">处理</button>
			<button id="btn_monitor_audit2" type="button" class="btn btn-default monitorAudit" onclick="monitorAudit('SUSPEND');" style="display:none">挂起</button>
			<button id="btn_monitor_audit3" type="button" class="btn btn-default monitorAudit" onclick="monitorAudit('TO_CHAIRMAN');" style="display:none">交场长(助理)</button>
			<button id="btn_chairman_audit1" type="button" class="btn btn-default chairmanAudit" onclick="chairmanAudit('REPAIR_NOW');" style="display:none">处理</button>
			<button id="btn_chairman_audit2" type="button" class="btn btn-default chairmanAudit" onclick="chairmanAudit('SUSPEND');" style="display:none">挂起</button>
			<button id="btn_ptw_new" type="button" class="btn btn-default reportUserAudit" onclick="newPtw();" style="display:none">新建工作票</button>
			<button id="btn_restart" type="button" class="btn btn-default restart" onclick="audit();" style="display:none">重新启动</button>
		</div>
		<div id="btn_wo_deleteDiv" class="btn-group btn-group-sm">
			<button id="btn_wo_delete" type="button" class="btn btn-default priv" privilege="WO_DELETE_ZJW" onclick="deleteWO();">删除</button>
			<button id="btn_wo_obsolete" type="button" class="btn btn-default priv" privilege="WO_AUDIT_ZJW" onclick="obsoleteWO();" style="display: none;">作废</button>
		</div>
		<div class="btn-group btn-group-sm">
			<button id="btn_deallog" type="button" class="btn btn-default">打印</button>
		</div>		
		<div class="btn-group btn-group-sm">
			<button id="btn_auditInfo" type="button" class="btn btn-default" onclick="showAuditInfo();">审批信息</button>
		</div>			
	</div>
</div>

<div id="workOrderFormDiv">
	<div class="inner-title">
		<span id="workOrderFormTitle">缺陷详情</span>
	</div>
	<div>
		<form id="workOrderForm" class="autoform"></form>
	</div>
</div>

<div id="title_tool_old" grouptitle="物料领用情况" style="display:none">
	<div class="margin-title-table">
		<form><table id="toolTableOld" class="eu-datagrid"></table></form>
	</div>
</div>
<div class="margin-group"></div>
	
	
<div id="title_tool" grouptitle="领用物料" style="display:none">
	<div class="margin-title-table">
		<form id="toolTableForm"><table id="toolTable" class="eu-datagrid"></table></form>
		<div id="toolBtnDiv" class="row btn-group-xs" >
			 <button id="btn_toolTable" onclick="appendTool();" type="button" class="btn btn-success">添加物料</button>
		</div>
	</div>
</div>
<div class="margin-group"></div>

<div id="reportFormDiv" style="display:none">
	<div class="inner-title">
		<span id="reportFormTitle">故障缺陷处理情况</span>
	</div>
	<div style="margin-bottom: 10px;">
		<form id="reportForm" class="autoform"></form>
	</div>
</div>

<div id="checkFormDiv" style="display:none">
	<div class="inner-title">
		<span id="checkFormTitle">验收情况</span>
	</div>
	<div style="margin-bottom: 10px;">
		<form id="checkForm" class="autoform"></form>
	</div>
</div>
</body>
</html>
