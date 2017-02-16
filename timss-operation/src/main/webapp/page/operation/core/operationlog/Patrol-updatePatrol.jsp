<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>编辑点检日志信息</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}/js/homepage/homepageService.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/operation/common/commonWorkflow.js?ver=${iVersion}"></script>
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/patrolBtnSecurity.js?ver=${iVersion}'></script>
<!-- add by yangk 点检情况变成富文本编辑-->
<script type="text/javascript" src="${basePath}/itcui/ckeditor/ckeditor.js?ver=${iVersion}"></script>
<script>
	//流程实例ID
	var processInstId = null;
	//数据库表单数据
	var formJsonData = null;
	//任务Id
	var taskId = null;
	//审批标志
	var applyFlag = null;
	//业务ID
	var businessId = null;
	//当前登录人
	var userId = null;
	//判定是否显示信息中的审批按钮(1--show/ 0--hide)
	var auditInfoShowBtn = 0;
	//是否从草稿箱打开
	var fromDraftBox = '<%=request.getParameter("fromDraftBox") %>';
	
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
	  			{title : "状态", id : "status", type:"label",
	  				formatter:function(val){
						return FW.getEnumMap("OPR_PATROL_STATUS")[val];
					}
	  			},
	  			{title : "审批人", id : "approveUserName", type:"label"},
	  			{title : "审批时间", id : "approveTime",  type:"label" ,
			  		formatter: function(value,row,index){
			  			return FW.long2time(value);
		          	}
	  			},
				{title : "点检情况", id : "patrolSituation", type : "richtext", /* rules : {required:true ,maxlength:500}, */ wrapXsWidth : 12, wrapMdWidth : 8, height : 50, linebreak:true,breakAll:true},
	  			{title : "缺陷情况", id : "defectSituation", type : "textarea", rules : {maxlength:500}, wrapXsWidth : 12, wrapMdWidth : 8, height : 50, linebreak:true},
	  			{title : "设备启停情况", id : "startStopSituation", type : "textarea", rules : {maxlength:500}, wrapXsWidth : 12, wrapMdWidth : 8, height : 50, linebreak:true}
	  		];
	var opts={
		validate:true,
		fixLabelWidth:true
	};
	
	//加载表单数据,在请求岗位数据的ajax返回时调用
	function loadFormData( rowData ){
		//按钮权限控制
		setPatrolPriv();
		//解决富文本回车空行的问题
		CKEDITOR.config.enterMode = CKEDITOR.ENTER_BR;
		var createDate = FW.long2date(rowData.createDate);
		var data = {
				"patrolId" : rowData.patrolId,
				"createBy" : rowData.createUserName,
				"checkDate" : rowData.checkDate,
				"approveUserName" : rowData.approveUserName,
				"approveTime" : rowData.approveTime,
				"speciality" : rowData.speciality,
				"status" : rowData.status,
				"patrolSituation" : rowData.patrolSituation,
				"defectSituation" : rowData.defectSituation,
				"startStopSituation" : rowData.startStopSituation
			};
			$("#autoform").iForm("setVal",data);
			$("#autoform").iForm("endEdit");
			//控制工具条按钮的隐藏
			//当有审批权限 状态是草稿，说明还在申请阶段
			$( ".priv" ).hide();
			$( "#flowDiagramButton" ).show();
			if( rowData.status == draftStr || rowData.status == applyStr){
				if(userId == rowData.createuser){
					if(applyFlag =="approver"){
						$( "#editButton" ).show();//可编辑
						$( "#commitButton" ).show();//可提交
						$( "#invalidButton" ).show();//可作废			
						$( "#autoform" ).iForm("endEdit");
					}
					else{
						$( "#saveButton" ).show();//草稿箱打开   直接是可编辑状态      可暂存	
						$( "#commitButton" ).show();//可提交
						$( "#deleteButton" ).show();//可删除
						$( "#autoform" ).iForm("hide",["approveTime","approveUserName"]);
						$( "#autoform" ).iForm("beginEdit");
					}
				}
				else{
					//只有关闭和查看流程
				}
			}
			else if( rowData.status == doneStr || rowData.status == invalidStr){
				//只有关闭和查看流程
			}
			else{
				if(applyFlag == "approver"){
					auditInfoShowBtn = 1;
					$( "#auditButton" ).show();//如果是当前审批人则可审批
				}
				else{
					auditInfoShowBtn = 0;
					$( "#auditButton" ).hide();//如果是当前审批人则可审批
				}				
			}
			FW.fixRoundButtons("#toolbar");	
	}
	
	//初始化信息
	function initForm( patrolId ){
		var url = basePath + "operation/patrol/queryPatrolById.do?patrolId=" + patrolId;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				if( data.result == "success"){
					//任务ID
					taskId = data.taskId;
					//审批状态
					applyFlag = data.applyFlag; //  approver or other
					//当前登录人
					userId = data.userId;
					//流程实例id
					processInstId = data.rowData.instantId;
					//业务id
					businessId = data.rowData.patrolId;
					//数据库表单数据
					formJsonData = data.rowData;
					//表单数据加载
					loadFormData( data.rowData );
				}
			}
		});
	}
	
	//审批
	function audit(){
		var workFlow = new WorkFlow();
		workFlow.showAudit(taskId,null,closeTab,closeTab,stop,"",0);
	}
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		var patrolId = '${patrolId}';
		var siteId = '${siteId}';
		initForm( patrolId );
		
		//显示流程图
		$("#flowDiagramButton").click(function(){
			if( processInstId == null || processInstId == "" ){
				showFlowDialog( "operation_" + siteId.toLowerCase() + "_patrol" );
			}else{
				var workFlow = new WorkFlow();
				//showAuditInfo( processInstId, formJsonData );
				workFlow.showAuditInfo(processInstId,formJsonData,auditInfoShowBtn,audit);
			}
		});
		
		//审批
		$("#auditButton").click(function(){
			var workFlow = new WorkFlow();
			workFlow.showAudit(taskId,null,closeTab,closeTab,stop,"",0);
		});
		
		//提交
		$( "#commitButton" ).click(function(){
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
					FW.error("点检情况长度不能超过1500字符");	
					$(this).button('reset');
					return ;
				}
			}
			//退回提交
			if( processInstId != null && processInstId != "" ){
				var workFlow = new WorkFlow();
				workFlow.showAudit(taskId,formData,closeTab,null,null,"",0);
			}else{
				var url = basePath + "operation/patrol/insertPatrol.do";
				$.ajax({
					url : url,
					type : 'post',
					data: {
						formData:formData
				    },
					dataType : "json",
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "提交成功 ");
							var taskId = data.taskId;
							if(taskId != null){
								var workFlow = new WorkFlow();
								processInstId = data.processInstId;
								workFlow.submitApply(taskId,null,closeTab,null, 0);
							}
						}else{
							if( data.reason != null ){
								FW.error( data.reason );
							}else {
								FW.error( "提交失败");
							}
						}
					}
				});
			}
		});
		
		//编辑
		$( "#editButton" ).click(function(){
			$( "#editButton" ).hide();
			$( "#saveButton" ).show();
			$( "#autoform" ).iForm("hide",["approveTime","approveUserName"]);
			$( "#autoform" ).iForm("beginEdit");
			FW.fixRoundButtons("#toolbar");	
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
					}else{
						FW.error( "暂存失败");
					}
				}
			});
		});
		
		//删除
		$( "#deleteButton" ).click(function(){
			var formData = $( "#autoform" ).iForm("getVal");
			
			FW.confirm("确定删除本条数据吗|该操作无法恢复", function() {
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
		
		//作废
		$( "#invalidButton" ).click(function(){
			var formData = $( "#autoform" ).iForm("getVal");
			FW.confirm("确定作废本条数据吗|该操作无法恢复", function() {
				var url = basePath + "operation/patrol/invalidPatrol.do?patrolId=" +  formData.patrolId;
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "作废成功");
							closeTab();
						}else{
							FW.error( "作废失败");
						}
					}
				});
			});
		});
		
	});
	
	//终止流程
	function stop(){
	}
		
</script>
</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm" style="margin-top: 2px!important;">
	           <button type="button" class="btn btn-default" onclick="closeTab();">关闭</button>
	    	</div>
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-default priv " privilege="OPR_PATROL_EDIT" id="editButton" style="display: none;">编辑</button>
	           <button type="button" class="btn btn-default priv " privilege="OPR_PATROL_SAVE" id="saveButton" style="display: none;">暂存</button>
	           <button type="button" class="btn btn-default priv " privilege="OPR_PATROL_COMMIT" id="commitButton" style="display: none;">提交</button>
			</div>		    	
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-default priv" privilege="OPR_PATROL_DELETE" id="deleteButton" style="display: none;">删除</button>
			</div>		    	
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-default priv" privilege="OPR_PATROL_AUDIT" id="auditButton" style="display: none;">审批</button>
			</div>	
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-default priv" privilege="OPR_PATROL_INVALID" id="invalidButton" style="display: none;">作废</button>
			</div>				
	        <div class="btn-group btn-group-sm">
	           <button type="button" class="btn btn-default priv " privilege="OPR_PATROL_FLOW" id="flowDiagramButton" style="display: none;">审批信息</button>
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