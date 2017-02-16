<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%
	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
	String sessId = request.getSession().getId();
%>
<!DOCTYPE html>
<html>
<head>
<title>编辑考勤异常信息</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}/js/homepage/homepageService.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/commonWorkflow.js?ver=${iVersion}"></script>
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonBtnSecurity.js?ver=${iVersion}'></script>

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
//文件是否需要endEdit
var fileEditFlag = false;
//当前页面暂存过
var secLoadFlag = false;
var id = '<%=request.getParameter("id") %>';
var siteId = '<%=request.getParameter("siteId") %>';

var fields = [
				{title : "id", id : "id",type:"hidden"},
				{title : "申请人id", id : "createBy",type:"hidden"},
				{title : "申请人部门id", id : "deptId",type:"hidden"},
				{title : "申请编号", id : "num",type:"label"},
				{title : "申请人", id : "createByStr",type:"label"},
	  			{title : "申请事由", id : "category",rules : {required:true},
					type : "combobox",
					dataType : "enum",
					enumCat : "ATD_AB_CATEGORY",
					options:{
						allowEmpty:true
					},
					render : function(id){
						$("#" + id).iCombo("setTxt","");
					}
				},
				{title : "申请时间", id : "createDay", rules : {required:true},type : "datetime",
						 dataType : "datetime"},
	  			{title : "开始时间", id : "startDate",rules : {required:true}, type : "datetime",dataType : "datetime"},
	  			{title : "结束时间", id : "endDate",rules : {required:true,greaterEqualThan:"#f_startDate"}, 
	  				type : "datetime",dataType : "datetime"},
	  			{title : "备注", id : "reason",type:"textarea",linebreak:true,wrapXsWidth:8,wrapMdWidth:8,height:50}
	  		];
var opts={
		validate:true,
		fixLabelWidth:true
	};
	
	//加载表单数据,在请求岗位数据的ajax返回时调用
	function loadFormData( rowData ){
		processInstId = rowData.instantId;
		formJsonData = rowData;
		businessId = rowData.id;
		var createByStr = rowData.userName + " / " + rowData.deptName;
		var data = {
				"id" : rowData.id,
				"num":rowData.num,
				"createBy" : rowData.createBy,
				"deptId":rowData.deptId,
				"createByStr" : createByStr,
				"category" : rowData.category,
				"reason" : rowData.reason,
				"startDate" : rowData.startDate,
				"endDate" : rowData.endDate,
				"createDay" : rowData.createDay
			};
			$("#autoform").iForm("setVal",data);
			$("#autoform").iForm("endEdit");
			//控制工具条按钮的隐藏
			//当有审批权限 状态是草稿，说明还在申请阶段
			 if( userId == rowData.createBy && ( rowData.status == "草稿" || 
					 rowData.status =="提交考勤异常申请" ) ){
				$( "#closeDiv" ).hide();
				$( "#fristNodeButtonDiv" ).show();
				$( "#approveButtonDiv" ).hide();
				$("#autoform").iForm("beginEdit",["createBy","category","reason","startDate","endDate"]);
				fileEditFlag = true;
				
				if(rowData.status == "草稿" ){
					$("#deleteButtonDiv").show();
					$("#invalidButtonDiv").hide();
				}else if(rowData.status =="提交考勤异常申请" ){
					$("#deleteButtonDiv").hide();
					$("#invalidButtonDiv").show();
				}
				
			}else if( userId != rowData.createBy && applyFlag =="approver" ){
				auditInfoShowBtn = 1;
				$( "#closeDiv" ).hide();
				$( "#fristNodeButtonDiv" ).hide();
				$( "#approveButtonDiv" ).show();
				$("#deleteButtonDiv, #invalidButtonDiv").hide();
			}else if( rowData.status == "已归档" || rowData.status == "终止" || applyFlag =="others" ){
				$( "#fristNodeButtonDiv" ).hide();
				$( "#approveButtonDiv" ).hide();
				$( "#closeDiv" ).show();
				$("#deleteButtonDiv, #invalidButtonDiv").hide();
			}else if( userId == rowData.createBy && applyFlag =="approver" ){
				$("#deleteButtonDiv, #invalidButtonDiv").hide();
				auditInfoShowBtn = 1;
			}
			if( rowData.status =="作废"  ){
				$( "#fristNodeButtonDiv,#approveButtonDiv,#deleteButtonDiv,#invalidButtonDiv" ).hide();
				$( "#closeDiv" ).show();
			}
			FW.fixRoundButtons("#toolbar");
			//附件
			initFile( rowData.id );
			//初始化sec_function 按钮权限
			initBtnSec( );
	}
	
	//初始化信息
	function initForm( id ){
		
		var url = basePath + "attendance/abnormity/queryAbnormityById.do?id=" + id;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				if( data.result == "success"){
					//任务ID
					taskId = data.taskId;
					//审批状态
					applyFlag = data.applyFlag;
					//当前登录人
					userId = data.userId;
					loadFormData( data.rowData );
				}
			}
		});
	}
	
	//为空判断函数
	function isNull( arg1 ){
		return !arg1 && arg1!==0 && typeof arg1!=="boolean"?true:false;
	}
	
	//初始化附件
	function setFile( fileMaps ){
		var fileFields = [
			{id:"attachment",title:" ",type:"fileupload",wrapXsWidth:12,wrapMdWidth:12,options:{
			    "uploader" : basePath + "upload?method=uploadFile&jsessionid=<%=sessId%>",
			    "delFileUrl" : basePath + "upload?method=delFile&key=<%=valKey%>",
			    "downloadFileUrl" :  basePath + "upload?method=downloadFile",
			    "swf" : basePath + "itcui/js/uploadify.swf",
			    "fileSizeLimit":10*1024,
			    "initFiles" : fileMaps,
			    "delFileAfterPost" : true
			}}];
		if( !secLoadFlag ){
			$("#fileDiv").iFold("init");
		}
		$("#fileForm").iForm('init',{"fields":fileFields,"options":{
		    labelFixWidth : 1,
		    labelColon : false
		}});
		if( !fileEditFlag ){
			$("#fileForm").iForm('endEdit');
		} 
		if( isNull(fileMaps) && !fileEditFlag ){
			$("#fileDiv").iFold("hide");
		}
	}
	
	//初始化信息
	function initFile( id ){
		var url = basePath + "attendance/abnormity/queryFileByAbnormityId.do?abnormityId=" + id;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				if( data.result == "success"){
					//文件ID
					var fileMap = data.fileMap;
					setFile( fileMap );
				}else{
					//隐藏附件
					setFile("");
				}
			}
		});
	}
	
	//审批
	function audit(){
		var workFlow = new WorkFlow();
		workFlow.showAudit(taskId,null,closeTab,null,stop,"",0);
	}
	
	function commitAbnormity(type){
		var isOnlyBeian="beian"==type?"Y":"N";
		
		if(!$("#autoform").valid()){
			return;
		}
		$( "#commitButton,#commitButtonBeian" ).button("loading");
		var category=$("#autoform").iForm("getVal")["category"];
		var formData = getFormData( "autoform" );
		//文件ids
		var fileIds = $("#fileForm").iForm("getVal").attachment;
		if( isNull( fileIds )){
			fileIds = "";
		}
		var dataArr = { 
			formData : formData ,
			fileIds : fileIds,
			editFlag : "other",
			addRows:[],delRows:[],updateRows:[]
		};		
		//退回提交
		if( processInstId != null && processInstId != "" ){
			dataArr.editFlag = "edit";
			var workFlow = new WorkFlow();
			workFlow.showAudit(taskId,JSON.stringify($.extend({},{
				isBeian:isOnlyBeian,
				processInstId:processInstId
			},dataArr)),closeTab,null,null,null,0,function(){
				$( "#commitButton,#commitButtonBeian" ).button("reset");
			});
		}else{
			var url = basePath + "attendance/abnormity/submitAbnormity.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:dataArr,
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "提交成功 ！");
						var taskId = data.taskId;
						if(taskId != null){
							var workFlow = new WorkFlow();
							processInstId = data.processInstId;
							var businessId = data.businessId;
							workFlow.submitApply(taskId,JSON.stringify($.extend({},{
								isBeian:isOnlyBeian,
								processInstId:processInstId
							},dataArr)),closeTab,null, 0,function(){
								$( "#commitButton,#commitButtonBeian" ).button("reset");
							});
						}
					}else{
						if( data.reason != null ){
							FW.error( data.reason );
						}else {
							FW.error( "提交失败 ！");
						}
						$( "#commitButton,#commitButtonBeian" ).button("reset");
					}
				}
			});
		}
	}
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		
		initForm( id );
		
		if("SWF"!=siteId){
			$( "#commitButton" ).html("提交");
			$( "#commitButtonBeian,#printDiv" ).hide();
		}
		
		//显示流程图
		$("#flowDiagramBtn").click(function(){
			if( processInstId == null || processInstId == "" ){
				showFlowDialog( "atd_" + siteId.toLowerCase() + "_abnormity" );
			}else{
				var workFlow = new WorkFlow();
				//showAuditInfo( processInstId, formJsonData );
				workFlow.showAuditInfo(processInstId,JSON.stringify(formJsonData),auditInfoShowBtn,audit);
			}
		});
		
		//审批
		$("#approveBtn").click(function(){
			var workFlow = new WorkFlow();
			workFlow.showAudit(taskId,null,closeTab,closeTab,stop,"",0);
		});
		
		//提交
		$( "#commitButton" ).click(function(){
			commitAbnormity();
		});
		//提交备案
		$( "#commitButtonBeian" ).click(function(){
			commitAbnormity("beian");
		});
		
		//暂存
		$( "#saveButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			var formData = getFormData( "autoform" );
			//文件ids
			var fileIds = $("#fileForm").iForm("getVal").attachment;
			if( isNull( fileIds )){
				fileIds = "";
			}
			var url = basePath + "attendance/abnormity/saveAbnormity.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:{"formData":formData,
					fileIds : fileIds},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "保存成功 ！");
						secLoadFlag = true;
						loadFormData( data.rowData );
					}else{
						FW.error( "保存失败 ！");
					}
				}
			});
		});
		
		//删除
		$( "#deleteButton" ).click(function(){
			var formData = $( "#autoform" ).iForm("getVal");
			
			FW.confirm("确定删除本条数据吗？该操作无法恢复。", function() {
				var url = basePath + "attendance/abnormity/deleteAbnormity.do?id=" +  formData.id;
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "删除成功 ！");
							closeTab();
						}else{
							FW.error( "删除失败 ！");
						}
					}
				});
			});
		});
		
		//作废
		$( "#invalidButton" ).click(function(){
			var formData = $( "#autoform" ).iForm("getVal");
			FW.confirm("确定作废本条数据吗？该操作无法恢复。", function() {
				var url = basePath + "attendance/abnormity/invalidAbnormity.do?id=" +  formData.id;
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "作废成功 ！");
							closeTab();
						}else{
							FW.error( "作废失败 ！");
						}
					}
				});
			});
		});
		
		$( "#printBtn" ).click(function(){
			var url = fileExportPath + "preview?__report=report/TIMSS2_"+siteId+"_KQYC_001_pdf.rptdesign&__format=pdf&id=" + id;
			window.open(url);
		});
	});
	
	//终止流程
	function stop(){
		var workFlow = new WorkFlow();
	    var flowData = workFlow.getFormData();
	    var data={};
	    data['taskId'] = taskId;
	    data['message'] = flowData.reason;
	    data['businessId'] = businessId;
	   // data['owner'] = userId;
	  //  data['assignee'] = assignee;
	    var url = basePath + "attendance/abnormity/deleteFlowAbnormity.do";
	    $.post(url, data, function(data){
	        if(data.result=='success'){
	        	_parent().$("#itcDlg").dialog("close");
	            FW.success("提交成功");
                homepageService.refresh();
                closeTab();
	        }
	        else{
	            FW.success("提交失败");
	        }
	    });
	}
		
</script>
</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar " >
	        <div class="atd_btn_pri atd_ab_update_close btn-group btn-group-sm" style="display: none;" id="closeDiv">
	            <button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
	        </div>
	        <div class="atd_btn_pri atd_ab_update_approve btn-group btn-group-sm" style="" id="approveButtonDiv">
	            <button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
				<button type="button" class="btn btn-default" id="approveBtn">审批</button>
	        </div>
	        <div class="atd_btn_pri atd_ab_update_commit btn-group btn-group-sm" style="display: none;" id="fristNodeButtonDiv">
	        	<button id="btn_close" type="button" class="btn btn-default" onclick="closeTab();">关闭</button>
				<button id="saveButton" type="button" class="btn btn-default">暂存</button>
				<button id="commitButton" type="button" class="btn btn-default" >提交审批</button>
				<button id="commitButtonBeian" type="button" class="btn btn-default" >提交备案</button>
	        </div>
	        <div class="atd_btn_pri atd_ab_update_delete btn-group btn-group-sm" style="" id="deleteButtonDiv">
				<button id="deleteButton" type="button" class="btn btn-default" >删除</button>
	        </div>
	        <div class="atd_btn_pri atd_ab_update_invalid btn-group btn-group-sm" style="" id="invalidButtonDiv">
				<button id="invalidButton" type="button" class="btn btn-default" >作废</button>
	        </div>
	         <div id="btn_flowDiagramDiv" class="atd_btn_pri atd_ab_update_flow btn-group btn-group-sm">
	        	<button id="flowDiagramBtn" type="button" class="btn btn-default">审批信息</button>
	        </div>
	        <div id="printDiv" class="btn-group btn-group-sm">
				<button type="button" class="btn btn-default" id="printBtn">打印</button>
			</div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		考勤异常信息
	</div>
	<form id="autoform"></form>
	<!-- 附件 -->
	<div id="fileDiv" grouptitle="附件">
	    <form id="fileForm" style="width:100%">
	    </form>
	</div>
</body>
</html>