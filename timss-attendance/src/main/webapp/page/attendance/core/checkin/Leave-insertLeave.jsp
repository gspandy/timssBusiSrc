<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
	String sessId = request.getSession().getId();
%>
<html>
<head>
<title>新建 请假单</title>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/commonWorkflow.js?ver=${iVersion}"></script>
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/map.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonBtnSecurity.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/leave.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/dataFormat.js?ver=${iVersion}'></script>

<script>
//提交是否有计算日期错误
var flag = false;
//错误的原因
var reasonCount = "";

var siteId = mvcService.getUser().siteId;

function formatterDays(val,title){
	return AtdDataFormat.formatDoubleDays(val);
}

var fields = [
				{title : "id", id : "id",type:"hidden"},
				{title : "申请人id", id : "createBy",type:"hidden"},
				{title : "申请人部门id", id : "deptId",type:"hidden"},
				{title : "申请人", id : "createByStr",type:"label"},
	  			{title : "入职时间  ", id : "entryDate",type:"label"},
	  			{title : "可享受年假天数   ", id : "annualDays",type:"label",formatter:formatterDays},
	  			{title : "结转年假天数", id : "compensateAnnualDays",type:"label",formatter:formatterDays},
	  			{title : "已休与核减年假天数 ", id : "hasAnnualDays",type:"label",formatter:formatterDays},
	  			{title : "剩余年假天数", id : "remainAnnualDays",type:"label",formatter:formatterDays},
	  			{title : "加班转补休天数", id : "overtimeDays",type:"label",formatter:formatterDays},
	  			{title : "结转补休天数", id : "overtimeComDays",type:"label",formatter:formatterDays},
	  			{title : "已补休天数", id : "hasOvertimeDays",type:"label",formatter:formatterDays},
	  			{title : "剩余补休天数", id : "remainOvertimeDays",type:"label",formatter:formatterDays},
	  			{title : "本次请假天数", id : "leaveDays",type:"label",formatter:formatterDays},
	  			{title : "请假事由", id : "reason",rules : {required:true,maxChLength:parseInt(300*2/3)},type:"textarea",linebreak:true,wrapXsWidth:8,wrapMdWidth:8,height:50}
	  		];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true,
		labelFixWidth:150
	};
	
	var processInstId = null;
	
	//日历onchange
	function calcDay(){
		$("#leaveFormDatagrid").valid();
		
		var me = $(this);
		var val = me.val();
		var tbl = me.parents(".datagrid-row");
		var startTime = tbl.children("td[field='startDate']").find("input").val();
		var endTime = tbl.children("td[field='endDate']").find("input").val();
		var rowIdArr = me.parents(".datagrid-row").attr("id").split("-");
		var rowIndex = rowIdArr[rowIdArr.length - 1 ];
		$("#leaveDatagrid").datagrid("selectRow",rowIndex);
		var category = $("#leaveDatagrid").datagrid("getSelected").category;
		
		var url = basePath + "attendance/leave/queryDiffLeaveDay.do?startDate=" + startTime + "&endDate=" + endTime + "&category=" + category;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				if( data.result == "success" ){
					var val = AtdDataFormat.formatDoubleDays(data.diffDay);
					
					//tbl.find("td[field='leaveDays']").find("div").html(val);
					$("#leaveDatagrid").datagrid( 'endEdit', rowIndex );
			    	var vo = $("#leaveDatagrid").datagrid('getRows')[rowIndex];
			    	vo["leaveDays"]=val;
			    	$("#leaveDatagrid").datagrid("updateRow",{index:rowIndex,row:vo});
			    	$("#leaveDatagrid").datagrid('beginEdit', rowIndex);
			    	
					setApplyLeaveDays();
					flag = false;
				}else{
					flag = true;
					reasonCount = data.reason;
					FW.error( data.reason );
				}
			}
		});
		
	}
	
	
	//删除一行datagrid数据
	function deleteThisRow(rowIndex, field, value) {
		if (field == 'id') {
			FW.confirm("删除？<br/>确定删除所选项吗？该操作无法撤销。", function() {
				$("#leaveDatagrid").datagrid("deleteRow",rowIndex);
				setApplyLeaveDays();
			});
		}
	}

	
	//设置明细datagrid
	function setLeaveApplyDatagrid( dataArray ){
		$("#leaveDatagrid").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect:true,
	        fitColumns:true,
	        nowrap : false,
	        data : dataArray,
	        columns:[[
				{field:'category',title:'请假类别',width:100,fixed:true,formatter:function(val){
						return FW.getEnumMap("ATD_LEI_CATEGORY")[val];
					}
				},
				{field:'startDate',title:'开始时间',width:180,fixed:true,editor:
					{
						type:"datebox",
						"options" : {
							onChange : 	calcDay,
							minView:1,
							dataType : 'datetime'
						},
						rules : {
							required : true
						}
					},
					formatter:function(value,row,index){
						return FW.long2time(value);
					}
				},
				{field:'endDate',title:'结束时间',width:180,fixed:true, editor : {
						type:"datebox",
						"options" : {
							onChange : 	calcDay,
							minView:1,
							dataType : 'datetime',
							rules : {
								required : true,
								greaterThan : '%startDate',
								messages : {
									greaterThan : "结束时间必须大于开始时间"
								}
							}	
						}
					},
					formatter:function(value,row,index){
						return FW.long2time(value);
					}
				},
				{field:'leaveDays',title:'请假天数',width:100,fixed:true/* , editor : {
				 	type:"text","options" : {
						onChange : 	setApplyLeaveDays,
			        	rules : {
			        		required:true,number:true,min:0.001,messages: {
			        			"min" : "请假天数必须大于0"
			        		}
						}	
			        }
		    	} */},
		    	{field:'id',title:' ',width:50,
					 formatter:function(value,row,index){
	  				     return '<img src="'+basePath+'img/attendance/btn_garbage.gif" width="16" height="16" style="cursor:pointer" />';
	  				}	
				}
				]],
			onLoadError: function(){
				//加载错误的提示 可以根据需要添加
				$("#noteListDiv").hide();
			},
	        onLoadSuccess: function(data){
	        	
	        },onClickCell : function(rowIndex, field, value) {
				if (field == 'id') {
					deleteThisRow(rowIndex, field, value);
				}
			}

	    });
	}
	
	//设置请假天数合计
	function setApplyLeaveDays(){
		var datagridData = $("#leaveDatagrid").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row");
		var len = datagridData.length;
		var leaveDays = 0;
		for( var i = 0 ; i < len; i++ ){
			/* var row = $(datagridData[i]);
			var days = row.children("td[field='leaveDays']").find("div").html();

	    	leaveDays +=  parseFloat( days ); */
	    	
	    	$("#leaveDatagrid").datagrid( 'endEdit', i );
	    	var vo = $("#leaveDatagrid").datagrid('getRows')[i];
	    	leaveDays +=  parseFloat( vo.leaveDays );
	    	$("#leaveDatagrid").datagrid('beginEdit', i);
		}
		$("#autoform").iForm("setVal",{leaveDays: parseFloat( leaveDays )} );
		var textVal =  len > 0 ? "继续添加请假" : "添加请假";
		$("#addDetail").text( textVal );
		
	}
	
	//为空判断函数
	function isNull( arg1 ){
		return !arg1 && arg1!==0 && typeof arg1!=="boolean"?true:false;
	}
	
	//开启编辑模式
	function beginEditor( ){
		var rowSize = $("#leaveDatagrid").datagrid('getRows').length;
		for( var i = 0 ; i < rowSize; i++ ){
	    	$("#leaveDatagrid").datagrid('beginEdit', i);
		}
		
		//设置请假天数合计
		setApplyLeaveDays();
	}
	
	//初始化form
	function initFormData( rowData ){
		var zeroFloat = 0;
		//可享受年假= 今年年假 + 上年结转年假
		var annualDays =  rowData.annual + rowData.annualRemain;
		//已休年假 = 请年假 + 核减年假
		var hasAnnualDays =  rowData.subAnualLeave + rowData.annualLevel;
		//剩余年假
		var remainAnnualDays = annualDays - hasAnnualDays;
		//剩余补休天数 = 上年结转补休天数 + 加班天数 - 已补休天数
		var remainOvertimeDays = rowData.compensateRemain + rowData.transferCompensate - rowData.compensateLeave;
		var data = {
				"annualDays" : rowData.annual ,
				"compensateAnnualDays" : rowData.annualRemain ,
				"hasAnnualDays" :hasAnnualDays,
				"remainAnnualDays" : remainAnnualDays,
				"overtimeDays" : rowData.transferCompensate,
				"overtimeComDays" : rowData.compensateRemain,
				"hasOvertimeDays" :rowData.compensateLeave,
				"remainOvertimeDays" : remainOvertimeDays,
				"leaveDays" : zeroFloat
			};
			$("#autoform").iForm("setVal",data);
			//$("#autoform").iForm("endEdit");
	}
	
	//初始化用户信息
	function setUserInfo(){
		var url = basePath + "attendance/stat/queryStatByLogin.do";
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				if( data.result == "success" ){
					initFormData( data.rowData);
				}else{
					FW.error( "加载个人信息失败！");
				}
			}
		});
		
	}
	
	
	//请假单明细对话框
	var pri_dlgOpts = {
		width : 550,
		height : 235,
		closed : false,
		title : "添加请假",
		modal : true
	};
	

	//添加请假单
	function showDtlIframe(){
		var src = basePath + "attendance/leave/insertLeaveItemToPage.do";
		
		var btnOpts = [{
			"name" : "取消",
			"float" : "right",
			"style" : "btn-default",
			"onclick" : function() {
				return true;
			}
		},{
			"name" : "确定",
			"float" : "right",
			"style" : "btn-success",
			"onclick" : function() {				
				var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
				if (!conWin.valid()) {
					return false;
				}
				var leaveformData = conWin.getFormOriginalData("leaveItemFrom");
				if(!categoryValid(leaveformData)){
					return false;	
				}
				var formdata = conWin.getFormData("leaveItemFrom");
				$('#leaveDatagrid').datagrid('appendRow',JSON.parse(formdata));
				beginEditor();
				return true;
			}
		} ];

		FW.dialog("init", {
			"src" : src,
			"dlgOpts" : pri_dlgOpts,
			"btnOpts" : btnOpts
		});
	}
	
	var fileFields = [
		{id:"attachment",title:" ",type:"fileupload",wrapXsWidth:12,wrapMdWidth:12,options:{
		    "uploader" : basePath + "upload?method=uploadFile&jsessionid=<%=sessId%>",
		    "delFileUrl" : basePath + "upload?method=delFile&key=<%=valKey%>",
		    "swf" : basePath + "itcui/js/uploadify.swf",
		    "fileSizeLimit":10*1024,
		    "delFileAfterPost" : true
		}}];

	//暂存之后load
	function loadFormData( rowData ){
		var createByStr = rowData.userName + " / " + rowData.deptName;
		var data = {
				"id" : rowData.id,
				"createBy" : rowData.createBy,
				"deptId":rowData.deptId,
				"createByStr" : createByStr,
				"entryDate" : rowData.entryDate,
				"annualDays" : rowData.annualDays,
				"compensateAnnualDays" : rowData.compensateAnnualDays ,
				"hasAnnualDays" : rowData.hasAnnualDays,
				"remainAnnualDays" : rowData.remainAnnualDays,
				"overtimeDays" : rowData.overtimeDays,
				"overtimeComDays" :rowData.overtimeComDays,
				"hasOvertimeDays" :rowData.hasOvertimeDays,
				"remainOvertimeDays" : rowData.remainOvertimeDays,
				"leaveDays" : rowData.leaveDays,
				"reason" : rowData.reason
			};
			$("#autoform").iForm("setVal",data);
			
			if(  rowData.status == "草稿" ){
				$("#deleteButtonDiv").show();
			}
			//$("#autoform").iForm("endEdit");
			setAfterLeaveApplyDatagrid( rowData.id );
			initFile( rowData.id );
	}
	
	//设置明细datagrid
	function setAfterLeaveApplyDatagrid( id ){
		var url = basePath + "attendance/leave/queryLeaveItemList.do?leaveId=" + id;
		$("#leaveDatagrid").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect:true,
	        fitColumns:true,
	        nowrap : false,
	        url : url,
	        columns:[[
				{field:'category',title:'请假类别',width:100,fixed:true,formatter:function(val){
						return FW.getEnumMap("ATD_LEI_CATEGORY")[val];
					}
				},
				{field:'startDate',title:'开始时间',width:180,fixed:true,editor:
					{
						type:"datebox",
						"options" : {
							format:"yyyy-mm-dd hh:00",
							onChange : 	calcDay,
							dataType : "datetime",
							minView:1,
							dataType : 'datetime'
						},
						rules : {
							required : true
						}
					},formatter:function(val){
						return FW.long2time(val);
					} 
				},
				{field:'endDate',title:'结束时间',width:180,fixed:true, editor : {
					type:"datebox",
					"options" : {
						format:"yyyy-mm-dd hh:00",
						onChange : 	calcDay,
						minView:1,
						dataType : 'datetime',
						rules : {
							required : true,
							greaterEqualThan : '%startDate',
							messages : {
								greaterEqualThan : "结束时间必须大于开始时间"
							}
						}	
					}
				},formatter:function(val){
					return FW.long2time(val);
				}},
				{field:'leaveDays',title:'请假天数',width:100,fixed:true/* , editor : {
			        type:"text","options" : {
			        	onChange : 	setApplyLeaveDays,
			        	rules : {
			        		required:true,number:true,min:0.001,messages: {
			        			"min" : "请假天数必须大于0"
			        		}
						}	
			        }
		    	} */},
		    	{field:'id',title:' ',width:50,
					 formatter:function(value,row,index){
	  				     return '<img src="'+basePath+'img/attendance/btn_garbage.gif" '+
	  				    +' width="16" height="16" style="cursor:pointer" />';
	  				}	
				}
				]],
			onLoadError: function(){
				//加载错误的提示 可以根据需要添加
				$("#noteListDiv").hide();
			},
	        onLoadSuccess: function(data){
	        		$("#addDetailBtn_toolbar").show();
	        		$("#leaveDatagrid").datagrid("showColumn","id");
	        		var rows=$("#leaveDatagrid").datagrid("getRows");
	        		var len = rows.length;
	        		FW.fixRoundButtons("#toolbar");
	        		
	    			for( var i = 0; i < len; i++ ){
	    				$("#leaveDatagrid").datagrid("beginEdit", i );
	    			}
	    			var textVal =  len > 0 ? "继续添加请假" : "添加请假";
	    			$("#addDetail").text( textVal );
	    			
	        },onClickCell : function(rowIndex, field, value) {
				if (field == 'id') {
					deleteThisRow(rowIndex, field, value);
				}
			}
	    });
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
		var url = basePath + "attendance/leave/queryFileByLeaveId.do?leaveId=" + id;
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
	
	$(document).ready(function() {
		//初始化页面
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		
		$("#fileForm").iForm('init',{"fields":fileFields,"options":{
		    labelFixWidth : 1,
		    labelColon : false
		}});
		$("#fileDiv").iFold("init");
		//加载个人信息
		setUserInfo();

		//初始化sec_function 按钮权限
		initBtnSec( );
		
		var userId = '${userId}';
		var userName = '${userName}';
		var deptId = '${deptId}';
		var deptName = '${deptName}';
		var siteId = '${siteId}';
		var dateArrival = '${ dateArrival }';
		//临时填充入职时间
		if( !isNull( dateArrival ) ){
			dateArrival = FW.long2date(dateArrival);
		}else{
			dateArrival = ' ';
		}
		
		$("#autoform").iForm("setVal",{createByStr:userName + " / " + deptName,entryDate: dateArrival,createBy:userId,deptId:deptId});
		
		//初始化datagrid
		setLeaveApplyDatagrid();
		$("#detailTitle").iFold("init");
		
		//提交
		$( "#commitButton" ).click(function(){
			if(!$("#autoform").valid() || !$("#leaveFormDatagrid").valid() ){
				return;
			}
			
			//标志日期是否有计算错误
			if( flag ){
				FW.error( reasonCount );
				return;
			}
			var rowDatas = $("#leaveDatagrid").datagrid('getRows');
			var len = rowDatas.length;
			if( len <= 0 ){
				FW.error( "请添加请假 ！");
				return ;
			}		
			
			for( var i = 0; i < len; i++ ){
				 $("#leaveDatagrid").datagrid( 'endEdit', i );
			}
			rowDatas = $("#leaveDatagrid").datagrid('getRows');
			
			if(!gridValid(rowDatas)){
				for( var i = 0; i < len; i++ ){
					 $("#leaveDatagrid").datagrid( 'beginEdit', i );
				}
				return;
			}
			$("#autoform").iForm("endEdit");
			$("#fileForm").iForm('endEdit');
			//文件ids
			var fileIds = $("#fileForm").iForm("getVal").attachment;
			if( isNull( fileIds )){
				fileIds = "";
			}
			
			var formData = getFormData( "autoform" );
			var dataArr = { 
					formData : formData ,
					rowDatas : JSON.stringify( rowDatas ),
					fileIds : fileIds,
					editFlag : "other"
				};			
			
			$(this).button("loading");
			$("#saveButton").css("opacity",0.65);
			$("#saveButton").attr('forbid',true);
			var url = basePath + "attendance/leave/insertLeave.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data :{
					formData :  formData,
					rowData : JSON.stringify( rowDatas ),
					fileIds : fileIds
				},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "提交成功 ！");
						var taskId = data.taskId;
						if(taskId != null){
							var workFlow = new WorkFlow();
							processInstId = data.processInstId;
							var businessId = data.businessId;
							workFlow.submitApply(taskId, JSON.stringify( dataArr ),closeTab,null,1);
						}
					}else{
						if( data.reason != null ){
							FW.error( data.reason );
						}else {
							FW.error( "提交失败 ！");
						}
					}
					$(this).button("reset");
				}
			});
		});
		
		//暂存
		$( "#saveButton" ).click(function(){
			if(!$("#autoform").valid() || !$("#leaveFormDatagrid").valid()  ){
				return;
			}
			
			var rowDatas = $("#leaveDatagrid").datagrid('getRows');
			var len = rowDatas.length;
			if( len <= 0 ){
				FW.error( "请添加请假 ！");
				return ;
			}
			for( var i = 0; i < len; i++ ){
				 $("#leaveDatagrid").datagrid( 'endEdit', i );
			}
			rowDatas = $("#leaveDatagrid").datagrid('getRows');
			if(!gridValid(rowDatas)){
				for( var i = 0; i < len; i++ ){
					 $("#leaveDatagrid").datagrid( 'beginEdit', i );
				}
				return;
			}
			//文件ids
			var fileIds = $("#fileForm").iForm("getVal").attachment;
			if( isNull( fileIds )){
				fileIds = "";
			}
			
			var formData = getFormData( "autoform" );

			var url = basePath + "attendance/leave/insertOrUpdateLeave.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data :{
					formData :  formData,
					rowData : JSON.stringify( rowDatas ),
					fileIds : fileIds
				},
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
		
		//删除
		$( "#deleteButton" ).click(function(){
			var formData = $( "#autoform" ).iForm("getVal");
			
			FW.confirm("确定删除本条数据吗？该操作无法恢复。", function() {
				var url = basePath + "attendance/leave/deleteLeave.do?id=" +  formData.id;
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
		
		//显示流程图
		$("#flowDiagramBtn").click(function(){
			//showFlowDialog( "atd_itc_leave" );
			var workFlow = new WorkFlow();
			var defKey = "atd_" + siteId.toLowerCase() + "_leave";
			workFlow.showDiagram( defKey );
		});
	});
		
</script>

</head>
<body>
	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button id="btn_close" type="button" class="btn btn-default" onclick="closeTab();">关闭</button>
	    	</div>
	    	<div id="btn_wo_operDiv" class="atd_btn_pri atd_le_insert_save btn-group btn-group-sm">
				<button id="saveButton" type="button" class="btn btn-default">暂存</button>
				<button id="commitButton" type="button" class="btn btn-default" >提交</button>
			</div>
			<div class="atd_btn_pri atd_le_insert_delete btn-group btn-group-sm" style="display: none;" id="deleteButtonDiv">
				<button id="deleteButton" type="button" class="btn btn-default" >删除</button>
	        </div>
	        <div id="btn_flowDiagramDiv" class="atd_btn_pri atd_le_insert_flow btn-group btn-group-sm">
	        	<button id="flowDiagramBtn" type="button" class="btn btn-default">审批信息</button>
	        </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		新建请假申请
	</div>
	<div id="formDiv">
		<form id="autoform"></form>
	</div>
	<!-- 请假明细 -->
	<div grouptitle="请假天数明细 " id="detailTitle">
		<div class="margin-title-table">
			<form id="leaveFormDatagrid">
			<table id="leaveDatagrid" style="" class="eu-datagrid"></table>
			</form>
		</div>
		<div class="btn-toolbar margin-foldable-button" role="toolbar" id="addDetailBtn_toolbar">
			<div privilege="F-ROLE-ADD" class="btn-group btn-group-xs" id="addDetailDiv">
				<button type="button" class="btn btn-success" id="addDetail" onclick="showDtlIframe()">添加请假</button>
			</div>
		</div>
	</div>
	<!-- 附件 -->
	<div id="fileDiv" grouptitle="附件">
	    <form id="fileForm" style="width:100%">
	    </form>
	</div>
</body>
</html>