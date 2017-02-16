var OprMode={
		objs:{
			opts:{
				mode:""//页面模式，默认view，还有edit/error
			}
		},
		
		//根据岗位获取运行方式的分组数据
		teamSelectData:function( jobsId ){
			var url = basePath + "operation/mode/queryModeTeamByJobsId.do?jobsId=" + jobsId;
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				success : function(data) {
					if( data.result == 'success' && data.teams.length > 0){
						var teamObj=eval(data.teams);
						var teamsArr = [];
						for( var index in teamObj ){
							teamsArr.push( [teamObj[index],teamObj[index]]);
						}
						$("#teamSelect").iCombo("init", {
							data : teamsArr,
							"onChange" : function(val) {
								OprMode.constructModeContenFields( jobsId, val);
							}
						});
					}else{
						OprMode.changeMode("error",true);
					}
				}
			});
		},
		
		//构造运行方式fields
		constructModeContenFields:function( jobId, team ){
			var url = basePath + "operation/mode/queryModeListByJobId.do?jobId=" + jobId ;
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:{"team":team},
				success : function(data) {
					if( data.total > 0){
						OprMode.changeMode("view");
						var beans = data.rows;
						//构造fields
						var modeContentFields = [];
						for( var i in beans ){
							var data=[];
							var optArr = beans[i].modeVal.split("/");
							for( var j in optArr ){
								data.push(optArr[j]==beans[i].nowModeVal?[optArr[j],optArr[j],true]:[optArr[j],optArr[j]]);
							}
							var field={
								title:beans[i].assetName,
								id:beans[i].assetId,
								type:"combobox",
								data:data,
								options:{
									onChange:function(val,obj){
										OprMode.saveMode(jobId,team,obj.selector.substr(3),val);
									},
									initOnChange:false
								}
							}
							modeContentFields.push(field);
						}
						var modeOpt =opts;
						modeOpt.fixLabelWidth = false;
						modeOpt.labelFixWidth = "250px";
						$("#modeContentForm").iForm("init",{"options":modeOpt,"fields":modeContentFields});
						
						//加载运行方式值
						//OprMode.loadModeContentDataByDutyJobsHandover( dutyId, jobId, handoverId, team );
						
						if(!OprNote.isMode("edit")||!OprNote.isMode("no", "handover_mode")){
							$("#modeContentForm").iForm("endEdit");
						}
					}else{
						OprMode.changeMode("error");
					}
				}
			});
		},
		
		//加载运行方式的值
		loadModeContentDataByDutyJobsHandover:function( dutyId, jobsId, handoverId, team ){
			var url = basePath + "operation/modeContent/queryModeContentByDutyJobsHandover.do";
			$.ajax({
				url : url,
				type : 'post',
				data :{
					dutyId : dutyId,
					jobsId : jobsId,
					handoverId : handoverId,
					team : team
				},
				dataType : "json",
				success : function(data) {
					if( data.result == "success" ){
						var vos = data.vos;
						var data = {};
						for( var index in vos ){
							data[vos[index].assetId] = vos[index].content;
						}
						$("#modeContentForm").iForm("setVal", data);
					}
					if(!OprNote.isMode("edit")||!OprNote.isMode("no", "handover_mode")){
						$("#modeContentForm").iForm("endEdit");
					}
				}
			});
		},
		
		changeMode:function(mode,isTeamHide){
			if(!mode){
				mode="view";
			}
			
			if(isTeamHide){
				$("#teamSelectDiv").hide();
			}else{
				$("#teamSelectDiv").show();
			}
			
			if(OprMode.isMode(mode))
				return;
			
			OprMode.objs.opts.mode=mode;
			
			if("error"==mode){
				$("#modeFormDiv").hide();
				$("#mode_error_area").show();
			}else{
				$("#modeFormDiv").show();
				$("#mode_error_area").hide();
			}
		},
		isMode:function(mode,modeName){
			return modeName?OprMode.objs.opts[modeName]==mode:OprMode.objs.opts.mode==mode;
		},
		
		saveMode:function(jobId,team,assetId,val){//单独保存运行方式的值
			$.ajax({
				url : basePath + "operation/mode/updateNowModeVal.do",
				type : 'get',
				data :{
					assetId : assetId,
					val : val,
					jobId : jobId,
					team : team
				},
				dataType : "json",
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "保存运行方式成功 ！");
					}else{
						FW.error( "保存运行方式失败 ！");
					}
				}
			});
		}
}