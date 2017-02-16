<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>新建点检日志</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/operation/common/commonWorkflow.js?ver=${iVersion}"></script>
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/patrolBtnSecurity.js?ver=${iVersion}'></script>
<!-- add by yangk 点检情况变成富文本-->
<script type="text/javascript" src="${basePath}/itcui/ckeditor/ckeditor.js?ver=${iVersion}"></script>
<script>
	//状态英文字符串
	var draftStr = '${draftStr}';
	var applyStr = '${applyStr}';
	var auditStr = '${auditStr}';
	var doneStr = '${doneStr}';
	var invalidStr = '${invalidStr}';
	
	var fields = [
				{title : "ID", id : "patrolId", type:"hidden"},
	  			{title : "点检员", id : "createBy", type:"label"},
	  			{title : "点检日期", id : "checkDate", rules : {required:true}, type : "date", dataType:"date"},
	  			{title : "专业 ", id : "speciality",rules : {required:true},
					type : "combobox",
					dataType : "enum",
					enumCat : "OPR_SPECIALITY",
					options:{
						allowEmpty:true
					},
					render : function(id){
						$("#" + id).iCombo("setTxt","");
					}
				},
				{title : "点检情况", id : "patrolSituation", type : "richtext", /* rules : {required:true ,maxlength:500},  wrapXsWidth : 12, wrapMdWidth : 8, height : 50,*/ 
					linebreak:true,breakAll:true},
	  			{title : "缺陷情况", id : "defectSituation", type : "textarea", rules : {maxlength:500}, wrapXsWidth : 12, wrapMdWidth : 8, height : 50, linebreak:true},
	  			{title : "设备启停情况", id : "startStopSituation", type : "textarea", rules : {maxlength:500}, wrapXsWidth : 12, wrapMdWidth : 8, height : 50, linebreak:true}
	  		];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true
	};
	
	function loadFormData( rowData ){
		var data = {
				"patrolId" : rowData.patrolId,
				"patrolSituation" : rowData.patrolSituation,
				"defectSituation" : rowData.defectSituation,
				"startStopSituation" : rowData.startStopSituation,
				"speciality" : rowData.speciality,
				"checkDate" : rowData.checkDate
			};
			$("#autoform").iForm("setVal",data);
	}
	
	var processInstId = null;
	
	
	$(document).ready(function() {
		//初始化sec_function 按钮权限
		setPatrolPriv();
		$( "#deleteButton" ).hide();
		//解决富文本回车空行的问题
		CKEDITOR.config.enterMode = CKEDITOR.ENTER_BR;
		
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		var userId = '${userId}';
		var userName = '${userName}';
		var deptId = '${deptId}';
		var deptName = '${deptName}';
		var siteId = '${siteId}';
		var today = '${today}';
		$("#autoform").iForm("setVal",{createBy:userName + " / " + deptName, checkDate:today});
		
		//提交
		$( "#commitButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			$(this).button("loading");
			var formData = getFormData( "autoform" );
			//add by yangk 富文本做非空和长度校验
			var patrolSituation = $("#autoform").iForm("getVal").patrolSituation;
			if(null==patrolSituation || patrolSituation==''){
				FW.error("点检情况不能为空");
				$(this).button('reset');
				return ;
			}else{
				if(patrolSituation.length>1000){
					FW.error("点检情况长度不能超过1500字符");	
					$(this).button('reset');
					return ;
				}
			}
			var url = basePath + "operation/patrol/insertPatrol.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data: {
					formData:formData
				},				
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "提交成功");
						var taskId = data.taskId;
						if(taskId != null){
							var workFlow = new WorkFlow();
							workFlow.submitApply(taskId, null, closeTab, null, 0);
						}
					}else{
						if( data.reason != null ){
							FW.error( data.reason );
						}else {
							FW.error( "提交失败");
						}
					}
					$(this).button("reset");
				}
			});
		});
		
		//暂存
		$( "#saveButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			var formData = getFormData( "autoform" );
			//add by yangk 富文本做非空和长度校验
			var patrolSituation = $("#autoform").iForm("getVal").patrolSituation;
			if(null==patrolSituation || patrolSituation==''){
				FW.error("点检情况不能为空");
				$(this).button('reset');
				return ;
			}else{
				if(patrolSituation.length>1000){
					FW.error("点检情况长度过长，最好不要超过1500字符");	
					$(this).button('reset');
					return ;
				}
			}
			var url = basePath + "operation/patrol/insertOrUpdatePatrol.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data: {
					formData:formData
				},					
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "暂存成功");
						//$("#autoform").iForm("setVal",{patrolId:data.rowData.patrolId});
						loadFormData( data.rowData );
					}else{
						FW.error( "暂存失败");
					}
				}
			});
		});
		
		//显示流程图
		$("#flowDiagramButton").click(function(){
			showFlowDialog( "operation_" + siteId.toLowerCase() + "_patrol" );
		});
		
		//删除
		$( "#deleteButton" ).click(function(){
			var formData = $( "#autoform" ).iForm("getVal");
			
			FW.confirm("确定删除本条数据吗？该操作无法恢复。", function() {
				var url = basePath + "operation/patrol/deletePatrol.do?patrolId=" +  formData.patrolId;
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "删除成功");
							closeTab();
						}else{
							FW.error( "删除失败");
						}
					}
				});
			});
		});
		
	});
		
</script>

</head>
<body>
	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm" style="margin-top: 2px!important;">
	           <button type="button" class="btn btn-default" onclick="closeTab();">关闭</button>
	    	</div>
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-default priv " privilege="OPR_PATROL_SAVE" id="saveButton" style="display: none;">暂存</button>
	           <button type="button" class="btn btn-default priv " privilege="OPR_PATROL_COMMIT" id="commitButton" style="display: none;">提交</button>
			</div>		    	
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-default priv" privilege="OPR_PATROL_DELETE" id="deleteButton" style="display: none;">删除</button>
			</div>	
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-default priv" privilege="OPR_PATROL_FLOW" id="flowDiagramButton" style="display: none;">审批信息</button>
			</div>	    			    	
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		点检日志详情
	</div>
	<form id="autoform"></form>
</body>
</html>