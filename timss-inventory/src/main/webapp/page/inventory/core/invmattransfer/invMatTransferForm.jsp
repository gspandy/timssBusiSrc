<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>移库申请单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script type="text/javascript">_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script type="text/javascript" src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
	<script type="text/javascript">
	var createtime = ""; //供审批框中显示
	var updatetime = ""; //供审批框中显示
	var auditInfoShowBtn = 0 ; //控制流程信息弹出框中是否显示“审批”按钮
	var instanceId ; //流程实例ID
	var taskId ; //活动节点ID
	var status;//申请状态
	var imtId = '${imtId}';//申请id
	var imtCodes = '${imtCodes}';//申请编码
	var itemIds = '${itemIds}';//物资ID
	var itemCodes = '${itemCodes}';//物资CODE
	var cateTypeIds = '${cateTypeIds}';//物资类型编码
	var siteId = '${siteId}';
	var userId = '${userId}';
	var defKey = '${defKey}';
	var wareHouseFromId = '${wareHouseFromId}';//移出仓库ID
	var wareHouseFromName = '${wareHouseFromName}';//移出仓库名称
	var wareHouseArray = ${wareHouseArray};//移入仓库数组
	
	/************************状态常量************************/
	var DRAFT = '${DRAFT}';//0草稿
	var TRANSFER_APPLY_COMMIT = '${TRANSFER_APPLY_COMMIT}';//1提交移库申请
	var STOREMAN_AUDIT = '${STOREMAN_AUDIT}';//2接收方仓管员审批
	var OBSOLETE = '${OBSOLETE}';//3作废
	var DONE = '${DONE}';//4完成

	$(document).ready(function(){
		//form表单初始化
		$("#invMatTransferForm").iForm("init",{"fields":invMatTransferFields,"options":{validate:true}});
		
		//物资
		$("#title_tool").show();
    	$("#toolTable").datagrid({
		    columns:toolGridField,
		    idField:'toolDelteId',
		    singleSelect:true,
		    fitColumns:true,
		    scrollbarSize:0,
		    nowrap:false
		}); 	
		$("#title_tool").iFold("init");			
		
		//权限控制
		initButtonPriv();					
		
		//根据id是否存在判断是新建还是查看,draft状态也可能id存在
		if(imtId != null && imtId != ""){
			setFormVal(imtId);
		}
		else{
			//按钮控制
			$("#invMatTransferForm").iForm("hide","status");
			$( ".priv" ).hide();
			$("#btn_save").show();
			$("#btn_commit").show();
			FW.fixRoundButtons("#toolbar");
			//列表带过来的物资
			listToMatApply();
		}
	});
	</script>
	<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
	<script src="${basePath}js/inventory/core/invmattransfer/invMatTransferForm.js?ver=${iVersion}"></script>
	<script src="${basePath}js/inventory/core/invmattransfer/invMatTransferDetail.js?ver=${iVersion}"></script>
	<script src="${basePath}js/inventory/core/invmattransfer/invMatTransferBtn.js?ver=${iVersion}"></script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_close" class="btn btn-default" onclick="closeCurPage(true);">关闭</button>
	        </div>		    
	    	<div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_save" class="btn btn-default priv" privilege="INVMATTRANSFER_SAVE" onclick="commitApply('save');">暂存</button>
	        	<button type="button" id="btn_edit" class="btn btn-default priv" privilege="INVMATTRANSFER_EDIT" onclick="beginEditApply();">编辑</button>
	        	<button type="button" id="btn_commit" class="btn btn-default priv" privilege="INVMATTRANSFER_COMMIT" onclick="commitApply('commit');">提交</button>
	        	<button type="button" id="btn_audit" class="btn btn-default priv" privilege="INVMATTRANSFER_AUDIT" onclick="audit();">审批</button>
	        </div>
	    	<div id="btnDeleteDiv" class="btn-group btn-group-sm">
	        	<button type="button" id="btn_delete" class="btn btn-default priv" privilege="INVMATTRANSFER_DELETE" onclick="deleteApply();">删除</button>
	        	<button type="button" id="btn_obsolete" class="btn btn-default priv" privilege="INVMATTRANSFER_INVALID" onclick="obsoleteApply();">作废</button>
	        </div>	        
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_processinfo" class="btn btn-default" onclick="showAuditInfo();">审批信息</button>
	        </div>
	    </div>
	</div>

	<div>
		<div class="inner-title">
			<span id="pageTitle">移库申请详情</span>
		</div>
		<div>
			<form id="invMatTransferForm" class="autoform"></form>
		</div>
	</div>
	
	<div id="title_tool_old" grouptitle="移出仓库">
		<div class="margin-title-table">
			<form><table id="toolTableOld" class="eu-datagrid"></table></form>
		</div>
	</div>
	<div class="margin-group"></div>
	
	<div id="title_tool" grouptitle="物资信息">
		<div class="margin-title-table">
			<form><table id="toolTable" class="eu-datagrid"></table></form>
			<div id="toolBtnDiv" class="row btn-group-xs" >
				 <button id="btn_toolTable" onclick="appendTool();" type="button" class="btn btn-success">添加物料</button>
			</div>
		</div>
	</div>
	<div class="margin-group"></div>
	
</body>
</html>