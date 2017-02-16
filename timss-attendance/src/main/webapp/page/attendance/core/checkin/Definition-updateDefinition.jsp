<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>休假规则信息</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonBtnSecurity.js?ver=${iVersion}'></script>

<script>

var fields = [
				{title : "ID", id : "id",type: "hidden" },
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
	  			{title : "定时任务更新打卡管理", id : "isCheckWorkstatus",rules : {required:true},type : "hidden",
		             data : [
		                 ["Y","是",true],
		                 ["N","否"]
		             ]
		        },
	  			{title : "更新打卡管理开始日期", id : "lastCheck",type:"date",formatter:function(val){
						return FW.long2date(val);
					}
				},
	  			{title : "重算休假统计年份", id : "statYear",rules : {required:true,digits:true}}
	  		];
var opts={
		validate:true,
		fixLabelWidth:true,
		labelFixWidth:150
	};
	
	//加载表单数据,在请求岗位数据的ajax返回时调用
	function loadFormData( rowData ){
		var data = {
				"id" : rowData.id,
				"serviceYear" : rowData.serviceYear,
				"yearRatio" : rowData.yearRatio,
				"compensateRatio" : rowData.compensateRatio,
				"effectiveDays" : rowData.effectiveDays,
				"firstLevelDays" : rowData.firstLevelDays,
				"secondLevelDays" : rowData.secondLevelDays,
				"thirdLevelDays" : rowData.thirdLevelDays,
				"foreStartDate" : rowData.foreStartDate,
				"foreEndDate" : rowData.foreEndDate,
				"afterStartDate" : rowData.afterStartDate,
				"afterEndDate" : rowData.afterEndDate,
				"workHours" : rowData.workHours,
				"oprWorkHours" : rowData.oprWorkHours,
				"validMin" : rowData.validMin,
				"toleranceMin" : rowData.toleranceMin,
				"isCheckWorkstatus":rowData.isCheckWorkstatus,
				"lastCheck":rowData.lastCheck,
				"statYear":new Date().getFullYear()
			};
			$("#autoform").iForm("setVal",data);
			$("#autoform").iForm("endEdit");
			
			if(!privMapping.atd_def_update_stat){
				$("#autoform").iForm("hide","statYear");
			}
			if(!privMapping.atd_def_update_carddata){
				$("#autoform").iForm("hide",["validMin","toleranceMin"]);
			}
			if(!privMapping.atd_def_update_workstatus){
				$("#autoform").iForm("hide","lastCheck");
			}
	}
	
	//初始化信息
	function initForm(){
		var url = basePath + "attendance/definition/queryDefinitionBySite.do";
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				if( data.result == "success"){
					loadFormData( data.rowData );
				}else if(data.redirect){
					window.location.href=data.redirect;
				}else{
					$("body").html( "没有考勤系统参数定义" );
				}
			}
		});
	}
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		
		initForm();
		
		//初始化sec_function 按钮权限
		initBtnSec( );
		
		$( "#editButton" ).click(function(){
			$( "#editButtonDiv" ).hide();
			$( "#updateButtonDiv,#cancelButtonDiv" ).show();
			$("#autoform").iForm("beginEdit");
		});
		
		$( "#btn_cancel" ).click(function(){
			$( "#editButtonDiv" ).show();
			$( "#updateButtonDiv,#cancelButtonDiv" ).hide();
			initForm();
		});
		
		$( "#updateButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			var formData = getFormData( "autoform" );
			var url = basePath + "attendance/definition/updateDefinition.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:{"formData":formData},
				success : function(data) {
					if (data.result == "success") {
						FW.success("修改成功 ！");
						$( "#btn_cancel" ).click();
					} else {
						FW.error("修改失败 ！");
					}
				}
			});
		});
		
		$( "#btn_rebuildStat" ).click(function(){
			$( "#btn_rebuildStat" ).attr("disabled",true);
			var url = basePath + "attendance/stat/rebuildStat.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:{"siteId":"","year":$("#f_statYear").val()},
				success : function(data) {
					if (data.result == "success") {
						FW.success("重算月度休假统计成功 ！");
					} else {
						FW.error("重算月度休假统计失败 ！");
					}
					$( "#btn_rebuildStat" ).attr("disabled",false);
				}
			});
		});
		$( "#btn_createStat" ).click(function(){
			var year=$("#f_statYear").val();
			FW.confirm("重建年度休假统计会清空"+year+"年的年度休假统计数据并重建，清空的数据不可恢复，请确认",function(){
				$( "#btn_createStat" ).attr("disabled",true);
				var url = basePath + "attendance/stat/createStat.do";
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					data:{"siteId":"","year":year},
					success : function(data) {
						if (data.result == "success") {
							FW.success("重建年度休假统计成功 ！");
						} else {
							FW.error("重建年度休假统计失败 ！");
						}
						$( "#btn_createStat" ).attr("disabled",false);
					}
				});
			});
			
		});
		$( "#btn_checkPersonStat" ).click(function(){
			var year=$("#f_statYear").val();
			$( "#btn_checkPersonStat" ).attr("disabled",true);
			var url = basePath + "attendance/stat/checkPersonStat.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:{"siteId":"","year":year},
				success : function(data) {
					if (data.result == "success") {
						FW.success("检查休假统计人员信息成功 ！");
					} else {
						FW.error("检查休假统计人员信息失败 ！");
					}
					$( "#btn_checkPersonStat" ).attr("disabled",false);
				}
			});
		});
		
		$( "#btn_checkMachineStatus" ).click(function(){
			$( "#btn_checkMachineStatus" ).attr("disabled",true);
			var url = basePath + "attendance/definition/checkMachineStatus.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				success : function(data) {
					if (data.result == "success") {
						FW.success("检查打卡记录同步情况已完成 ！请注意查收通知信息");
					} else {
						FW.error("检查打卡记录同步情况失败 ！");
					}
					$( "#btn_checkMachineStatus" ).attr("disabled",false);
				}
			});
		});
		$( "#btn_checkMachine" ).click(function(){
			$( "#btn_checkMachine" ).attr("disabled",true);
			var url = basePath + "attendance/definition/checkMachine.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				success : function(data) {
					if (data.result == "success") {
						FW.success("更新打卡记录成功 ！");
					} else {
						FW.error("更新打卡记录失败 ！");
					}
					$( "#btn_checkMachine" ).attr("disabled",false);
				}
			});
		});
		
		$( "#btn_checkWorkStatus" ).click(function(){
			$( "#btn_checkWorkStatus" ).attr("disabled",true);
			var url = basePath + "attendance/definition/checkWorkStatus.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				success : function(data) {
					if (data.result == "success") {
						FW.success("更新打卡管理成功 ！");
						initForm();
					} else {
						FW.error("更新打卡管理失败 ！");
					}
					$( "#btn_checkWorkStatus" ).attr("disabled",false);
				}
			});
		});
	});
		
</script>
</head>
<body>
<div class="atd_btn_pri atd_def_update_edit bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar " >
	        <div class="atd_btn_pri atd_def_update_close btn-group btn-group-sm" style="display: none;" id="cancelButtonDiv">
	            <button type="button" class="btn btn-default" id="btn_cancel">取消</button>
	        </div>
	        <div class="atd_btn_pri atd_def_update_edit btn-group btn-group-sm" id="editButtonDiv">
				<button type="button" class="btn btn-default" id="editButton">编辑</button>
	        </div>
	        <div class="atd_btn_pri atd_def_update_save btn-group btn-group-sm" style="display: none;" id="updateButtonDiv">
				<button type="button" class="btn btn-success" id="updateButton" >保存</button>
	        </div>
	        <div class="atd_btn_pri atd_def_update_carddata btn-group btn-group-sm">
	           <button type="button" class="btn btn-default" id="btn_checkMachine">更新打卡记录</button>
	    	</div>
	    	<div class="atd_btn_pri atd_def_update_carddata btn-group btn-group-sm" style='display:none;'>
	           <button type="button" class="btn btn-default" id="btn_checkMachineStatus" title="检查考勤机的打卡记录是否同步到今天，否则发邮件通知管理员">检查打卡记录同步情况</button>
	    	</div>
	        <div class="atd_btn_pri atd_def_update_workstatus btn-group btn-group-sm">
	           <button type="button" class="btn btn-default" id="btn_checkWorkStatus">更新打卡管理</button>
	    	</div>
	    	<div class="atd_btn_pri atd_def_insert_stat btn-group btn-group-sm">
	           <button type="button" class="btn btn-default" id="btn_createStat" title="会重新建立指定年度的休假统计数据（清空原数据）">重建年度休假统计</button>
	    	</div>
	    	<div class="atd_btn_pri atd_def_update_stat btn-group btn-group-sm">
	           <button type="button" class="btn btn-default" id="btn_rebuildStat" title="会重新计算指定年度的休假统计的月度数据">重算月度休假统计</button>
	    	</div>
	    	<div class="atd_btn_pri atd_def_insert_stat btn-group btn-group-sm">
	           <button type="button" class="btn btn-default" id="btn_checkPersonStat" title="会检查指定年度的休假统计的人员信息，包括是否离职、可休年假等">检查休假统计人员信息</button>
	    	</div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		休假规则
	</div>
	<form id="autoform"></form>
</body>
</html>