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
<title>新建考勤系统参数</title>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/commonWorkflow.js?ver=${iVersion}"></script>
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonBtnSecurity.js?ver=${iVersion}'></script>

<script>


var fields = [
				{title : "id", id : "id",type:"hidden"},
				{title : "申请人id", id : "createBy",type:"hidden"},
				{title : "申请人部门id", id : "deptId",type:"hidden"},
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
	  			{title : "开始时间", id : "startDate",rules : {required:true}, type : "datetime",dataType : "datetime"},
	  			{title : "结束时间", id : "endDate",rules : {required:true,greaterEqualThan:"#f_startDate"},
	  				type : "datetime",dataType : "datetime"},
	  			{title : "备注", id : "reason",type:"textarea",linebreak:true,wrapXsWidth:8,wrapMdWidth:8,height:50}
	  		];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true
	};
	
	var fileFields = [
		{id:"attachment",title:" ",type:"fileupload",wrapXsWidth:12,wrapMdWidth:12,options:{
		    "uploader" : basePath + "upload?method=uploadFile&jsessionid=<%=sessId%>",
		    "delFileUrl" : basePath + "upload?method=delFile&key=<%=valKey%>",
		    "swf" : basePath + "itcui/js/uploadify.swf",
		    "fileSizeLimit":10*1024,
		    "delFileAfterPost" : true
		}}];
		
	function loadFormData( rowData ){
		var createByStr = rowData.userName + " / " + rowData.deptName;
		var data = {
				"id" : rowData.id,
				"deptId":rowData.deptId,
				"createBy" : rowData.createBy,
				"createByStr" : createByStr,
				"category" : rowData.category,
				"reason" : rowData.reason,
				"startDate" : rowData.startDate,
				"endDate" : rowData.endDate
			};
			$("#autoform").iForm("setVal",data);
			
			if(  rowData.status == "草稿" ){
				$("#deleteButtonDiv").show();
				//初始化sec_function 按钮权限
				initBtnSec( );
			}
			
			initFile( rowData.id );
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
		//$("#fileDiv").iFold("init");
		$("#fileForm").iForm('init',{"fields":fileFields,"options":{
		    labelFixWidth : 1,
		    labelColon : false
		}});
		/* if( !isNull( fileMaps) ){
			$("#fileForm").iForm('endEdit');
		}else{
			$("#fileForm").iForm("beginEdit");
		} */
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
					//$("#fileDiv").hide();
				}
			}
		});
	}
	
	var processInstId = null;
	
	function commitAbnormity(type){
		var isOnlyBeian="beian"==type?"Y":"N";
		
		if(!$("#autoform").valid()){
			return;
		}
		$( "#commitButton,#commitButtonBeian" ).button("loading");
		
		var formData = getFormData( "autoform" );
		//文件ids
		var fileIds = $("#fileForm").iForm("getVal").attachment;
		if( isNull( fileIds )){
			fileIds = "";
		}
		var businessData={fileIds : fileIds,"formData":formData,addRows:[],delRows:[],updateRows:[]};
		
		var url = basePath + "attendance/abnormity/submitAbnormity.do";
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			data:businessData,
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
						},businessData)),closeTab,null,0);
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
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		var userId = '${userId}';
		var userName = '${userName}';
		var deptId = '${deptId}';
		var deptName = '${deptName}';
		var siteId = '${siteId}';
		$("#autoform").iForm("setVal",{createByStr:(userName + " / " + deptName),createBy:userId,deptId:deptId});

		$("#fileForm").iForm('init',{"fields":fileFields,"options":{
		    labelFixWidth : 1,
		    labelColon : false
		}});
		$("#fileDiv").iFold("init");
		
		if("SWF"!=siteId){
			$( "#commitButton" ).html("提交");
			$( "#commitButtonBeian" ).hide();
		}
		
		//初始化sec_function 按钮权限
		initBtnSec( );
		
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
						loadFormData( data.rowData );
					}else{
						FW.error( "保存失败 ！");
					}
				}
			});
		});
		
		//显示流程图
		$("#flowDiagramBtn").click(function(){
			showFlowDialog( "atd_" + siteId.toLowerCase() + "_abnormity" );
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
		
	});
		
</script>

</head>
<body>
	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div id="btn_wo_operDiv" class="atd_btn_pri atd_ab_insert_close btn-group btn-group-sm">
	        	<button id="btn_close" type="button" class=" btn btn-default" onclick="closeTab();">关闭</button>
				<button id="saveButton" type="button" class="btn btn-default">暂存</button>
				<button id="commitButton" type="button" class="btn btn-default" >提交审批</button>
				<button id="commitButtonBeian" type="button" class="btn btn-default" >提交备案</button>
			</div>
		  	<div class="atd_btn_pri atd_ab_update_delete btn-group btn-group-sm" style="display: none;" id="deleteButtonDiv">
				<button id="deleteButton" type="button" class="btn btn-default" >删除</button>
	        </div>
	        <div id="btn_flowDiagramDiv" class="atd_btn_pri atd_ab_insert_flow btn-group btn-group-sm">
	        	<button id="flowDiagramBtn" type="button" class="btn btn-default">审批信息</button>
	        </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		新建考勤异常
	</div>
	<form id="autoform"></form>
	
	<!-- 附件 -->
	<div id="fileDiv" grouptitle="附件">
	    <form id="fileForm" style="width:100%">
	    </form>
	</div>
</body>
</html>