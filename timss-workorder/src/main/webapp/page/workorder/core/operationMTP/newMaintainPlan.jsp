<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html> 
<html>
  <head>
	<title>新建维护计划</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<!-- js引入的示例,basePath使用EL表达式获取 -->
	<script>
		var mtpId = '<%=request.getParameter("maintainPlanId")%>'; 
		var todoId = '<%=request.getParameter("todoId")%>';  
		var equipParams = '<%=request.getParameter("equipParams")%>';
		var equipId = null;
		var equipName = null;
		var equipCode = null;
		if(equipParams != "null"){
			equipParams = eval("("+equipParams+")");
			equipId = equipParams.equipId;
			equipName = equipParams.equipName;
			equipCode = equipParams.equipCode;
			 
		}
		var loginUser = ItcMvcService.user.getUserId();
		var siteId = ItcMvcService.getUser().siteId;
		var list = "WO_WORKTEAM";
		
		var formRead = false;
		if(mtpId != "null"){
			formRead = true;
		}
		
	</script>
	<script type="text/javascript" src="${basePath}js/workorder/core/newMaintainPlan.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
	<script type="text/javascript">
		var mtpType = "";
		
		$(document).ready(function() {
			
			FW.createAssetTree({multiSelect:false}); //左边设备树
			//FW.setTreeStat("fold");
			if(mtpId == null || mtpId == 'null'){  //新建作业方案,参数定为0传过去
				initMTPPage(0); 
			}else{  //查看工作方案
				$.post(basePath + "workorder/maintainPlan/queryMTPDataById.do",{maintainPlanId:mtpId},
					function(mtpFullData){
						initMTPPage(mtpFullData);
					},"json");
			}
			//控制权限
			Priv.apply();
			_itc_addellipsis();
			//展开树，显示选中的树
	        if(equipId != null){
				setTimeout(function(){
					document.getElementById("itcEmbbedAssetTree").contentWindow.expandForHintById(equipId);
				},500);
			}
		});
		function passAssetSelect(data){
		    $("#maintainPlanForm").iForm("setVal",{
		     	equipId : data.id,
	        	equipNameCode:data.assetCode,
	        	equipName : data.text
		    });
		}
		 
	</script>
  </head>
  
  <body style="height: 100%;">
   	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button id="btn_close" type="button" class="btn btn-default" onclick="closeCurPage(true);">关闭</button>
	        </div>
	    	<div id="btn_mtp_saveDiv" class="btn-group btn-group-sm">
				<button id="btn_mtp_save" type="button" class="btn btn-success priv" privilege="wo-maitainPlan-oper" onclick="commitMTP()">保存</button>
			</div>
			<div id="btn_mtp_toWoDiv" class="btn-group btn-group-sm">
				<button id="btn_mtp_toWo" type="button" class="btn btn-success" onclick="mtpToWoBtn()">生成工单</button>
			</div>
	        <div id="btn_mtp_editDiv" class="btn-group btn-group-sm">
	        	<button id="btn_mtp_edit" type="button" class="btn btn-default priv" privilege="wo-maitainPlan-oper" onclick="editMTP()">编辑</button>
	        </div>
	         <div id="btn_mtp_unavailableDiv" class="btn-group btn-group-sm">
	        	<button id="btn_mtp_unavailable" type="button" class="btn btn-default priv" privilege="wo-maitainPlan-oper" onclick="unavailableMTP()">禁用</button>
	        </div>
	        
	    </div>
	</div>
    <div>
    	 <form id="maintainPlanForm" class="autoform"></form>
    </div>
    <div id="mtpDataGridDiv">
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
    </div>
  </body>
</html>
