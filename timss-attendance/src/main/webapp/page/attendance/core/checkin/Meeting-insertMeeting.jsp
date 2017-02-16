<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>预定会议室</title>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonBtnSecurity.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/formatterDate.js?ver=${iVersion}'></script>

<script>
//是否改变会议室编号
var isChangMeetingNo = false;
//是否改变过开始、结束时间
var isChangDate = false;
//间隔时间，默认是 1h
var diffTime = 1;

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
							isChangMeetingNo = true;
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
		//当会议室 + 日期都改变过才触发验证
		if( isChangDate && isChangMeetingNo ){
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
	}
	
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		var userId = '${userId}';
		var userName = '${userName}';
		var deptId = '${deptId}';
		var deptName = '${deptName}';
		var roleFlag = '${roleFlag}';
		var meetingNo = '${meetingNo}';
		
		var todayTime = new Date() ;
		var dateTime = '${ date }' == '' ? todayTime.getTime() - todayTime.getMinutes() * 60 * 1000 : new Date( parseInt( '${date}' ) ).dateAdd("h", 8 ).getTime();
		//默认1个小时差
		var endTime = new Date( dateTime ).dateAdd("n", diffTime * 60 ).getTime();
		var createByStr = userName;
		if( !isNull( deptName ) ){
			createByStr = userName + " / " + deptName;
		}
		$("#autoform").iForm("setVal",{createBy: createByStr, startDate : dateTime, endDate : endTime, meetingNo : meetingNo });

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
						closeTab();
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
		
		//开始时间，字段改变的时候
		$("#f_startDate").change( function(){
			isChangDate = true;
			var fData = $("#autoform").iForm("getVal");
			
			var endDate = new Date(fData.startDate).dateAdd( "n", diffTime * 60 ).getTime();
			$("#autoform").iForm("setVal",{ endDate : endDate });
			
			changeDate();
		} );
		//结束时间
		$("#f_endDate").change( function(){
			isChangDate = true;
			
			var fData = $("#autoform").iForm("getVal");
			diffTime = fData.startDate < fData.endDate ? ( fData.endDate - fData.startDate ) / ( 60 * 60 * 1000 ) : 1;
			
			changeDate();
		} );
	});
		
</script>

</head>
<body>
	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	    		<button id="btn_close" type="button" class=" btn btn-default" onclick="closeTab();">关闭</button>
	    	</div>
	    	<div id="commitButtonDiv" class="btn-group btn-group-sm">
				<button id="commitButton" type="button" class="btn btn-default" >保存</button>
			</div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		新建会议室预定
	</div>
	<form id="autoform"></form>
</body>
</html>