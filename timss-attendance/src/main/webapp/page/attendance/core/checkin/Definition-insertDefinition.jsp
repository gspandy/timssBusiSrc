<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>新建考勤系统参数</title>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonForm.js?ver=${iVersion}'></script>

<script>


var fields = [
				{title : "享受年假工龄(年)", id : "serviceYear",rules : {required:true,digits:true} },
	  			{title : "年休假折算比率", id : "yearRatio",rules : {required:true,number:true,min:0}},
	  			{title : "补休假折算比率", id : "compensateRatio",rules : {required:true,number:true,min:0}},
	  			{title : "补假有效期限天数", id : "effectiveDays",rules : {required:true,number:true,min:0}},
	  			{title : "10年以下年假天数", id : "firstLevelDays",rules : {required:true,digits:true}},
	  			{title : "20年以下年假天数", id : "secondLevelDays",rules : {required:true,digits:true}},
	  			{title : "20年以上年假天数", id : "thirdLevelDays",rules : {required:true,digits:true}},
	  			{title : "上午上班开始时间", id : "foreStartDate",rules : {required:true,regex:"^(([0-1][0-9])|(2[0-3]))([0-5][0-9])$"},messages:{"regex":"请按照时间格式hhmm填写"} },
	  			{title : "上午下班结束时间", id : "foreEndDate",rules : {required:true,regex:"^(([0-1][0-9])|(2[0-3]))([0-5][0-9])$"},messages:{"regex":"请按照时间格式hhmm填写"} },
	  			{title : "下午上班开始时间", id : "afterStartDate",rules : {required:true,regex:"^(([0-1][0-9])|(2[0-3]))([0-5][0-9])$"},messages:{"regex":"请按照时间格式hhmm填写"} },
	  			{title : "下午下班结束时间", id : "afterEndDate",rules : {required:true,regex:"^(([0-1][0-9])|(2[0-3]))([0-5][0-9])$"},messages:{"regex":"请按照时间格式hhmm填写"} },
	  			{title : "行政人员上班时长", id : "workHours",rules : {required:true,number:true,min:0.001}},
	  			{title : "运行人员上班时长", id : "oprWorkHours",rules : {required:true,number:true,min:0.001}},
	  			{title : "打卡时间点有效分钟数", id : "validMin",rules : {required:true,digits:true}},
	  			{title : "打卡时间点宽容分钟数", id : "toleranceMin",rules : {required:true,digits:true}},
	  			{title : "定时任务更新打卡管理", id : "isCheckWorkstatus",rules : {required:true},type : "radio",
		             data : [
		                 ["Y","是",true],
		                 ["N","否"]
		             ]
		        },
	  			{title : "更新打卡管理开始日期", id : "lastCheck",type:"date",formatter:function(val){
						return FW.long2date(val);
					}
				}
	  		];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true,
		labelFixWidth:150
	};
	
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		
		$( "#saveButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			var formData = getFormData( "autoform" );
			var url = basePath + "attendance/definition/insertDefinition.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:{"formData":formData},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "新增考勤系统参数成功 ！");
						//resetForm();
						closeTab();
					}else{
						if( data.reason != null ){
							FW.error( data.reason );
						}else {
							FW.error( "新增考勤系统参数失败 ！");
						}
					}
				}
			});
		});
		
	});
		
</script>

</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-success" id="saveButton">保存</button>
            </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		新建考勤系统参数
	</div>
	<form id="autoform"></form>
</body>
</html>