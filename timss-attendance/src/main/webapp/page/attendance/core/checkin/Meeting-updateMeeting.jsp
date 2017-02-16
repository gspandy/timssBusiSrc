<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>预定会议室</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonForm.js?ver=${iVersion}'></script>

<script>
var id = '${id}';
var userId = '${ userId }';
var roleFlag = '${roleFlag}';

//间隔时间，默认是 1h
var diffTime = 1;

/* 得到日期年月日等加数字后的日期 */   
Date.prototype.dateAdd = function(interval,number)   
{   
 var d = this;   
 var k={'y':'FullYear', 'q':'Month', 'm':'Month', 'w':'Date', 'd':'Date', 'h':'Hours', 'n':'Minutes', 's':'Seconds', 'ms':'MilliSeconds'};   
 var n={'q':3, 'w':7};   
 eval('d.set'+k[interval]+'(d.get'+k[interval]+'()+'+((n[interval]||1)*number)+')');   
 return d;   
} ;

var timeArr=[["0.5","0.5",true],["1","1"],["1.5","1.5"],["2","2"],["2.5","2.5"],["3","3"],["3.5","3.5"],["4","4"],["4.5","4.5"],["5","5"],["5.5","5.5"],["6","6"],["6.5","6.5"],["7","7"],["7.5","7.5"],["8","8"],["8.5","8.5"],["9","9"],["8.5","8.5"],["9","9"],["9.5","9.5"],["10","10"],["10.5","10.5"],["11","11"],["11.5","11.5"],["12","12"]];
var fields = [
				{title : "id", id : "id",type:"hidden"},
	  			{title : "会议名称", id : "meetingName",rules : {required:true} },
	  			{title : "申请人", id : "createBy",type:"label"},
	  			{title : "会议室", id : "meetingNo",rules : {required:true},
					type : "combobox",
					dataType : "enum",
					enumCat : "ATD_MT_NO",
					options:{
						allowEmpty:true,
						onChange : function( val ){
							changeDate();
						}
					},
					render : function(id){
						$("#" + id).iCombo("setTxt","");
					}
				},
	  			{title : "开始时间", id : "startDate",rules : {required:true}, type : "datetime",dataType : "datetime"},
	  			{
  			        title : "时长(小时)", 
  			        id : "countTime",
  			        type : "hidden",
  			        data : timeArr,
  			        rules : {required:true},
  			      	options:{
	  			      	onChange : function( val ){
	  			      		changeDate( );
						}
  			      	}
  			        
	  			},
	  			{title : "结束时间 ", id : "endDate",rules : {required:true,greaterEqualThan:"#f_startDate"},
	  				type : "datetime",dataType : "datetime"},
	  			{
  			        title : "是否重要", 
  			        id : "isLeader",
  			        type : "radio",
  			        data : [
  			            ['N','否',true],
  			            ['Y','是']
  			        ],rules : {required:true}
	  			},
	  			{title : "会议内容", id : "reason", linebreak:true,
	  				wrapXsWidth:12,
	 		        wrapMdWidth:8,
	 		        height : 50,type:"textarea"
		 		}
	  		];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true
	};
	
	//初始化form
	function initFormData( id ){
		var url = basePath + "attendance/meeting/queryMeetingById.do?id=" + id;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				if( data.result == "success" ){
					loadFormData( data.meetingBean );
				}else{
					FW.error( "初始化失败！" );
				}
			}
		});
	}
	
	function loadFormData( rowData ){
		var createBy = rowData.userName;
		if( !isNull( rowData.deptName ) ){
			createBy = rowData.userName + " / " + rowData.deptName;
		}
		var data = {
				"id" : rowData.id,
				"createBy" : createBy,
				"meetingName" : rowData.meetingName,
				"meetingNo" : rowData.meetingNo,
				"startDate" : rowData.startDate,
				"countTime" : rowData.countTime,
				"endDate" : rowData.endDate,
				"isLeader" : rowData.isLeader,
				"reason" : rowData.reason
			};
			$("#autoform").iForm("setVal",data);
			$("#autoform").iForm("endEdit");
			
			//是创建人或者有权限
			if( rowData.createBy == userId || roleFlag == 'lingdao'){
				$("#editButtonDiv,#deleteButtonDiv").show();
			}else{
				$("#editButtonDiv,#deleteButtonDiv").hide();
			}
			$("#btn_closeDiv").show();
			$("#backButtonDiv,#commitButtonDiv").hide();
			FW.fixRoundButtons("#toolbar");
	}
	
	//删除
	function deleteMeeting( remark ){
		var formData = $( "#autoform" ).iForm("getVal");
		
		FW.confirm("确定删除本条数据吗？该操作无法恢复。", function() {
			var url = basePath + "attendance/meeting/deleteMeeting.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data :{
					id : formData.id,
					remark : remark
				},
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
	}
	
	//删除对话框
	function deleteDtlIframe(){
		var src = basePath + "page/attendance/core/checkin/MeetingItem-updateMeetingItem.jsp";
		var pri_dlgOpts = {
				width : 550,
				height : 150,
				closed : false,
				title : "删除预定记录",
				modal : true
			};
		
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
				var formdata = conWin.getFormData("statItemFrom");
				var remark = JSON.parse(formdata).remark1;
				deleteMeeting( remark );
				return true;
			}
		} ];

		FW.dialog("init", {
			"src" : src,
			"dlgOpts" : pri_dlgOpts,
			"btnOpts" : btnOpts
		});
	}
	
	//为空判断函数
	function isNull( arg1 ){
		return !arg1 && arg1!==0 && typeof arg1!=="boolean"?true:false;
	}
	
	//当时长发生改变时
	function changeCountTime( ){
		var formData = $("#autoform").iForm("getVal");
		var val = formData.countTime;
		var endDate = !isNull( formData.startDate) ? new Date(formData.startDate).dateAdd( "n", val * 60 ).getTime() : "";
		if( !isNull( endDate ) ){
			$("#autoform").iForm("setVal",{ endDate : endDate });
			changeDate();
		}
	}
	
	
	//当日期、会议室发生改变时
	function changeDate(){
		var formData = getFormData( "autoform" );
		var fData = $("#autoform").iForm("getVal");
		if( !isNull( fData.startDate ) && !isNull( fData.endDate) && !isNull( fData.meetingNo )){
			var url = basePath + "attendance/meeting/isValidateMeeting.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data :{
					formData : formData
				},
				success : function(data) {
					if( data.result == "fail"){
						var diffBeans = data.diffBeans;
						var reason = "与会议 ";
						for( i in diffBeans ){
							reason += diffBeans[i].meetingName + ", ";
						}
						reason += "时间冲突！";
						FW.error( reason );
					}
				}
			});
		}
	}
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		initFormData( id );
		
		//提交
		$( "#commitButton" ).click(function(){
			if(!$("#autoform").valid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			var formData = getFormData( "autoform" );
			var url = basePath + "attendance/meeting/insertOrUpdateMeeting.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data :{
					formData : formData
				},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "保存成功 ！");
						loadFormData( data.meetingBean );
					}else{
						var diffBeans = data.diffBeans;
						var reason = "保存失败，与会议 ";
						for( i in diffBeans ){
							reason += diffBeans[i].meetingName + ", ";
						}
						reason += "时间冲突！";
						FW.error( reason );
					}
				}
			});
		});
		
		//点击编辑
		$("#editButton").click(function(){
			$("#autoform").iForm("beginEdit");
			$("#btn_closeDiv,#editButtonDiv").hide();
			$("#backButtonDiv,#commitButtonDiv").show();
			$(".inner-title").html("编辑会议室预定");
			FW.fixRoundButtons("#toolbar");
		});
		
		//点击返回
		$("#backButton").click(function(){
			//$("#autoform").iForm("endEdit");
			initFormData( id );
			$("#btn_closeDiv,#editButtonDiv").show();
			$("#backButtonDiv,#commitButtonDiv").hide();
			$(".inner-title").html("会议室预定详情");
			FW.fixRoundButtons("#toolbar");
		});
		
		//开始时间，字段改变的时候
		$("#f_startDate").change( function(){
			isChangDate = true;
			var fData = $("#autoform").iForm("getVal");
			
			var endDate = new Date(fData.startDate).dateAdd( "n", diffTime * 60 ).getTime();
			$("#autoform").iForm("setVal",{ endDate : endDate });
			
			changeDate();
		});
		
		//结束时间
		$("#f_endDate").change( function(){
			isChangDate = true;
			
			var fData = $("#autoform").iForm("getVal");
			diffTime = fData.startDate < fData.endDate ? ( fData.endDate - fData.startDate ) / ( 60 * 60 * 1000 ) : 1;
			
			changeDate();
		});
		
		
		//删除
		$( "#deleteButton" ).click(function(){
			deleteDtlIframe();
		});
		
	});
		
</script>

</head>
<body>
	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div id="btn_closeDiv" class="btn-group btn-group-sm">
	        	<button id="btn_close" type="button" class=" btn btn-default" onclick="closeTab();">关闭</button>
			</div>
		  	<div class="btn-group btn-group-sm" style="" id="editButtonDiv">
				<button id="editButton" type="button" class="btn btn-default" >编辑</button>
	        </div>
		  	<div class="btn-group btn-group-sm" style="display: none;" id="backButtonDiv">
				<button id="backButton" type="button" class="btn btn-default" >取消</button>
	        </div>
		  	<div class="btn-group btn-group-sm" style="display: none;" id="commitButtonDiv">
				<button id="commitButton" type="button" class="btn btn-default" >保存</button>
	        </div>
		  	<div class="btn-group btn-group-sm" style="display: none;" id="deleteButtonDiv">
				<button id="deleteButton" type="button" class="btn btn-default" >删除</button>
	        </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		会议室预定详情
	</div>
	<form id="autoform"></form>
</body>
</html>