var OprNote={
		objs:{
			opts:{
				mode:"",//页面模式，默认view，还有edit/error
				error_type:"",//错误类型，dept_empty/jobs_empty/init_error
				handover_mode:"no"//交接班的模式,no/to_confirm/to_post
			},
			deptArr:[],
			deptMap:{},
			jobsArr:[],
			jobsMap:{},
			userJobsStr:"",
			userJobs:[],
			selectedJobsId:0,
			//基础信息表单字段
			baseFields:[
				{title : "日期", id : "dateTime", type : "label"},
	  			{title : "当值", id : "dutyName", type : "label"},
	  			{title : "当前值别Id", id : "currentDutyId", type : "hidden"},
	  			{title : "当前班次Id", id : "currenShiftId", type : "hidden"},
	  			{title : "当前日历Id", id : "nowScheduleId", type : "hidden"},
	  			{title : "值班人员", id : "dutyPerson",type : "label",breakAll : true,linebreak:true,wrapXsWidth:12,wrapMdWidth :12}
	  		]
		},
		
		init:function(){
			//初始化运行方式Form表单
			//$("#modeContentDiv").iFold("init");
			//$("#modeContentDiv").prev("div").append($("#teamSelectDiv").detach());
			$("#baseform").iForm("init",{"options":{
				validate:true,
				labelFixWidth: 150
			},"fields":OprNote.objs.baseFields});
			//初始化工种和岗位选择
			OprNote.deptOption(OprNote.objs.deptList);
		},
		
		changeMode:function(mode){//切换页面模式
			if(!mode){
				mode="view";
			}
			if(OprNote.isMode(mode))
				return;
			
			OprNote.objs.opts.mode=mode;
			
			if("error"==mode){
				$("#error_area").show();
				$("#contentDiv").hide();
			}else{
				$("#error_area").hide();
				$("#contentDiv").show();
			}
		},
		isMode:function(mode,modeName){
			return modeName?OprNote.objs.opts[modeName]==mode:OprNote.objs.opts.mode==mode;
		},
		changeErrorMode:function(type){
			if(OprNote.objs.opts.error_type==type&&OprNote.isMode("error"))
				return;
			OprNote.objs.opts.error_type=type;
			$("#error_msg div").hide();//隐藏所有错误
			$("#"+type).show();
			OprNote.changeMode("error");
		},
		
		/**
		 * 功能流程的函数
		 */
		deptOption:function(deptList){
			if(deptList==null||deptList.length<=0){//无可用数据
				OprNote.changeErrorMode("dept_empty");
			}else{
				var chooseDeptId=0;
				for( var index in deptList ){
					for( var j in deptList[index].jobsList ){
						if(OprNote.isUserJobs(deptList[index].jobsList[j].id)){
							if(!chooseDeptId){//找到一个用户所属的岗位所在的工种，默认选中
								chooseDeptId=deptList[index].deptId;
							}
							OprNote.objs.userJobs.push([deptList[index].jobsList[j].id,deptList[index].name+" "+deptList[index].jobsList[j].name]);
						}
					}
					if(chooseDeptId==deptList[index].deptId){
						OprNote.objs.deptArr.push([deptList[index].deptId,deptList[index].name,true]);
					}else{
						OprNote.objs.deptArr.push([deptList[index].deptId,deptList[index].name]);
					}
					OprNote.objs.deptMap[deptList[index].deptId]=deptList[index];
				}
				$("#deptSelect").iCombo("init", {
					data : OprNote.objs.deptArr,
					"onChange" : function(val) {
						OprNote.jobsOption(OprNote.objs.deptMap[val].jobsList);
				  	}
				});
			}
		},
		jobsOption:function(jobsList){
			if(jobsList==null||jobsList.length<=0){//无可用数据
				OprNote.changeErrorMode("jobs_empty");
			}else{
				OprNote.objs.jobsArr=[];
				OprNote.objs.jobsMap={};
				var isChooseJobsId=false;
				for( var index in jobsList ){
					if(!isChooseJobsId&&OprNote.isUserJobs(jobsList[index].id)){//找到用户所属的岗位，默认选中
						OprNote.objs.jobsArr.push([jobsList[index].id,jobsList[index].name,true]);
						isChooseJobsId=true;
					}else{
						OprNote.objs.jobsArr.push([jobsList[index].id,jobsList[index].name]);
					}
					OprNote.objs.jobsMap[jobsList[index].id]=jobsList[index];
				}
				$("#jobsSelect").iCombo("init", {
					data : OprNote.objs.jobsArr,
					"onChange" : function(val) {
						OprNote.objs.selectedJobsId=val;
						var stationId=OprNote.objs.jobsMap[val].stationId;
						OprNote.getOprNoteByJobsId(val,stationId);
				  	}
				});
			}
		},
		//获取选择的岗位的当前班次信息及运行记事
		getOprNoteByJobsId:function(jobsId,stationId){
			var url = basePath + "operation/note/queryOnDutyBaseInfo.do?jobsId=" + jobsId + "&stationId="+stationId;
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				success : function(data) {
					if(data.success == 1){
						if(OprNote.isUserJobs(jobsId)){//如果用户属于该岗位，则可编辑,要放datagrid之前否则datagrid宽度为0
							OprNote.changeMode("edit");
						}else{
							OprNote.changeMode("view");
						}
						
						var currentHandover = data.currentHandover;
						OprNote.objs.currentHandoverId = currentHandover.id;
						var dateTime = new Date(currentHandover.nowShiftDate).Format("yyyy-MM-dd");
						var currentDutyName = currentHandover.currentDutyName;
						var currentShiftName = currentHandover.currentShiftName;
						var currentDeptName = currentHandover.currentDeptName;
						
						//基本信息
						var formData = {
								"currentDutyId" : currentHandover.currentDutyId,
								"currentShiftId" : currentHandover.currentShiftId,
								"nowScheduleId" : currentHandover.nowScheduleId,
								"dutyName" : currentDeptName+" "+currentDutyName + " " + currentShiftName,
								"dutyPerson" : OprNote.getMemberString(data.schedulePersonList/*currentPersonList*/),
								"dateTime" : dateTime
						};
						$("#baseform").iForm("setVal",formData);
						
						//运行方式
						OprMode.teamSelectData(jobsId);
					}else{
						FW.error(data.returnCode);
						OprNote.changeErrorMode("init_error");
						$("#teamSelectDiv").hide();
					}
				}
			});
		},
		
		isUserJobs:function(jobsId){
			return OprNote.objs.userJobsStr&&OprNote.objs.userJobsStr.indexOf(jobsId+",")>=0;
		},
		
		//传入一个人员list，拼接他们的名字，以逗号隔开
		getMemberString:function(memberList){
			var members = "";
			if(memberList != null && memberList.length > 0){
				for ( var index in memberList) {
					members = members + memberList[index].userName + ",";
				}
				members = members.substring(0, members.length - 1);
			}else{
				members = "没有值班人员";
			}
			return members;
		}
		
};

String.prototype.replaceAll = function(s1,s2) { 
    return this.replace(new RegExp(s1,"gm"),s2); 
};