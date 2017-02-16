var OprHandover={
		objs:{
			currentHandoverId:-1//当前未交接的handoverId，用于交接班时连同form一起提交过去
		},
		//设置交接班账号icombo
		setAccountCombo:function(personJobsOfStation,defaultId){
			var comboData = [];
			for ( var index in personJobsOfStation) {
				comboData.push([personJobsOfStation[index].userId,personJobsOfStation[index].userId + " " + personJobsOfStation[index].userName]);
			}
			$("#f_currentPerson").iCombo("init", {
				data : comboData
			});
			$("#f_nextPerson").iCombo("init", {
				data : comboData
			});
			$("#f_currentPerson").iCombo("setVal",defaultId);
			$("#jiaojieForm").iForm("endEdit","currentPerson");
		},
		
		//设置下一班的comboBox的数据,其中nextShiftCalendars包含了非normal班次
		setNextScheduleIdCombo:function(nextShiftCalendars,nextScheduleId){
			var nextShiftSchedule = [];
			for ( var index in nextShiftCalendars) {
				if(nextShiftCalendars[index].shiftType == "normal"){
					if( nextShiftCalendars[index].id == nextScheduleId ){
						nextShiftSchedule.push( [nextShiftCalendars[index].id,nextShiftCalendars[index].shiftName,true]);
					}else{
						nextShiftSchedule.push( [nextShiftCalendars[index].id,nextShiftCalendars[index].shiftName]);
					}
				}
			}
			$("#f_nextScheduleId").iCombo("init", {
				data : nextShiftSchedule,
				"onChange" : function(val) {
					OprHandover.setNextDutyName(val,nextShiftCalendars);
				}
			});
			if(nextScheduleId > 0){
				OprHandover.setNextDutyName(nextScheduleId,nextShiftCalendars);
			}else{
				OprHandover.setNextDutyName(nextShiftSchedule[0][0],nextShiftCalendars);
			}
		},
		
		//根据用户选择的日期请求排班数据
		dateChanged:function(){
			var dateSelect = $("#f_nextShiftDate").val();
			var nowDate=$("#f_nowShiftDate").text();
			if(dateSelect<nowDate){
				FW.error( "接班日期不能选择当前班日期之前");
				return;
			}
			var stationId = $("#f_stationId").val();
			var currentShiftStartTime=nowDate==dateSelect?$("#f_currentShiftStartTime").val():"";
			var url = basePath + "operation/scheduleDetail/getCalendarByDS.do?date=" + dateSelect + "&&stationId=" + stationId+"&&startTimeStr="+currentShiftStartTime;
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				success : function(data) {
					if(data.scheduleDetail.length>0){
						OprHandover.setNextScheduleIdCombo(data.scheduleDetail);
					}else{
						$("#f_nextScheduleId").iCombo("init", {
							data : [["-1","没有排班数据"]]
						});
					}
					FW.success( "接班班次已更新");
				}
			});
		},
		
		getJiaojieField:function(nextShiftDateString){
			//交接班formFields
			var jiaojieFields = [
				{title : "jobsId", id : "jobsId",type : "hidden"},
				{title : "当前岗位Id", id : "stationId", type : "hidden"},
				{title : "当班日期", id : "nowShiftDate",type : "label"},
				{title : "当前班次", id : "currentShiftName",type : "label"},
				{title : "当前值别", id : "currentDutyName",type : "label"},
				{title : "当前班次开始时间", id : "currentShiftStartTime",type : "hidden"},
				{title : "接班日期", id : "nextShiftDate",type : "date",
					options : {
							startDate : nextShiftDateString
					},
					rules : { 
						required:true,
						greaterEqualThan:'#f_nowShiftDate'
					},
					render: function(id){
						$("#" + id).on("change",function(){
							OprHandover.dateChanged();
						});
					}
				},
				{title : "接班班次", id : "nextScheduleId",type : "combobox",rules : {required:true}},
				{title : "接班值别id", id : "nextDutyId",type : "hidden"},
				{title : "接班值别", id : "nextDutyName",type : "label"},
			    {
				   title : "上一班情况", 
				   id : "lastContent",
				   type : "hidden",
				   breakAll : true,
				   linebreak:true,
				   wrapXsWidth:12,
			        wrapMdWidth:8,
			        height : 50
				},
			    {
				   title : "上一班交待", 
				   id : "lastRemark",
				   type : "hidden",
				   breakAll : true,
				   wrapXsWidth:12,
			        wrapMdWidth:8,
			        height : 50
				},
				{title : "交班情况", id : "nextContent",rules : {required:true},type : "hidden",linebreak:true, wrapXsWidth:12,wrapMdWidth:8,height : 50},
				{title : "交班交待", id : "nextRemark",rules : {required:true},type : "hidden", wrapXsWidth:12,wrapMdWidth:8,height : 50},
				{title : "交班人账号", id : "currentPerson", type : "combobox" ,rules : {required:true},linebreak : true},
				{title : "交班人密码", id : "currentUserPassword", rules : {required:true}, dataType : "password"},
				{title : "接班人账号", id : "nextPerson", type : "combobox" ,rules : {required:true},linebreak : true},
				{title : "接班人密码", id : "nextUserPassword", rules : {required:true}, dataType : "password"}
			];
			return jiaojieFields;
		},
		
		//设置接班值别的名称，日期变化时，接班值别名称联动变化
		setNextDutyName:function(scheduleId,CalendarArr){
			for ( var index in CalendarArr) {
				if(CalendarArr[index].id == scheduleId){
					$("#jiaojieForm").iForm("setVal",{"nextDutyName":CalendarArr[index].dutyName,"nextDutyId":CalendarArr[index].dutyId});
					break;
				}
			}	
		}
}