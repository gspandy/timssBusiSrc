var OprNote={
		objs:{
			opts:{
				mode:"",//页面模式，默认view，还有edit/error
				error_type:"",//错误类型，dept_empty/jobs_empty/init_error
				handover_mode:"",//交接班的模式,no/to_confirm/to_post
				canChooseJiaojieBan:false,//是否可选择交接班
				canChooseJiaojieBanSite:[],//可选择交接班的站点
				onlySchedulePersonJiaojieBan:true,//只有当班人员可交接班
				pageSize:9999//不分页
			},
			deptArr:[],
			deptMap:{},
			jobsArr:[],
			jobsMap:{},
			userJobsStr:"",
			userJobs:[],
			selectedJobsId:0,
			schedulePerson:{},//排班人员，用于选人
			handoverPerson:{},//值班人员，用于选人
			nowScheduleId:0,//当前值班，用于操作值班人员
			noteFormHideField:[],//不显示的表单字段
			
			//基础信息表单字段
			baseFields:[
				{title : "日期", id : "dateTime", type : "label"},
	  			{title : "当值", id : "dutyName", type : "label"},
	  			{title : "当前值别Id", id : "currentDutyId", type : "hidden"},
	  			{title : "当前班次Id", id : "currentShiftId", type : "hidden"},
	  			{title : "当前日历Id", id : "nowScheduleId", type : "hidden"},
	  			{title : "动态表单KEY", id : "keyword", type : "hidden"},
	  			{title : "值班人员", id : "dutyPerson",type : "label",breakAll : true,linebreak:true,wrapXsWidth:12,wrapMdWidth :12},
	  			{title : "交班人", id : "lastUserName",type : "hidden",breakAll : true},
	  			{title : "接班人", id : "nextUserName",type : "hidden",breakAll : true},
	  			{title:"班前会",id:"preShiftMeeting",linebreak:true,type:'textarea',
   					wrapXsWidth:12,wrapMdWidth:8,height:48,rules:{maxChLength:parseInt(4000*2/3)}
   				},
   				{title:"班后会",id:"postShiftMeeting",linebreak:true,type:'textarea',
   					wrapXsWidth:12,wrapMdWidth:8,height:48,rules:{maxChLength:parseInt(4000*2/3)}
   				},
	  			{
			        title : "上一班情况", 
			        id : "lastContent_base",
			        type : "hidden",
			        breakAll : true,
			        linebreak:true,
			        wrapXsWidth:12,
			        wrapMdWidth:8,
			        height : 50
			    },
			    {
			        title : "上一班交待", 
			        id : "lastRemark_base",
			        type : "hidden",
			        breakAll : true,
			        wrapXsWidth:12,
			        wrapMdWidth:8,
			        height : 50
			    }
	  		]
		},
		
		init:function(){
			OprNote.initSite();
			//初始化运行方式Form表单
			$("#modeContentDiv").iFold("init");
			$("#modeContentDiv").prev("div").append($("#teamSelectDiv").detach());
			//初始化页面按钮事件
			OprNote.initButton();
			
			//初始化工种和岗位选择
			OprNote.deptOption(OprNote.objs.deptList);
		},
		initSite:function(){//站点个性化初始化
			//设置是否需要核定时长的操作
			if($.inArray(siteId,OprNote.objs.opts.canChooseJiaojieBanSite)>-1){
				OprNote.objs.opts.canChooseJiaojieBan=true;
			}
		},
		initButton:function(){
			//配置表单
			$("#dfromButton").click(function(){
				FW.openDDBox();
			});
			
			//保存运行方式
			$("#modeSaveBtn").click(function(){
				OprNote.modeSave();
			});
			
			//打印运行记事
			$("#printButton").click(function(){
				OprNote.printNote();
			});
			
			//交班按钮
			$("#btn_jiaoban").click(function(){
				var currentShiftId = $("#baseform").iForm("getVal").currentShiftId;
				OprNote.checkUnfinishSchedultTask(currentShiftId);
//				if($("#btn_jiaoban").is(":visible")&&OprNote.objs.opts.onlySchedulePersonJiaojieBan&&!OprNote.objs.handoverPerson[Priv.secUser.userId]){
//					FW.error("当前登录用户并非当前值班人员");
//				}else{
//					OprNote.changeHandoverMode("to_confirm");
//				}
			});
			//取消交班
			$("#jiaojieCancel").click(function(){
				OprNote.changeHandoverMode("no");
			});
			//提交交接班信息，包括账号密码和交班情况
			$("#jiaobanSaveBtn").click(function(){
				if(!$("#jiaojieForm").valid()){
					return;
			  	}
				if($("#f_nextScheduleId").iCombo("getVal") === '-1'){
					FW.error("没有排班数据无法交接班");
					return;
				}
				var formData = getFormData( "jiaojieForm" );
				if(OprHandover.currentHandoverId < 0 ){
					FW.error("当前未交接记录的Id不合法,请联系管理员");
					return;
				}
				var url = basePath + "operation/handover/insertHandover.do";
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					data:{
						currentHandoverId : OprHandover.currentHandoverId,
						formData : formData
					},
					success : function(data) {
						if( data.success == "1" ){
							FW.success( "交接班成功 ！");
							var currentNavTreeId = FW.getCurrentNavTreeId();
							FW.activeTreeItem(currentNavTreeId);
						}else{
							FW.error( data.returnCode );
						}
					}
				});
			});
			
			//选择值班人员
			$("#userButton").click(function(){
				g.users=OprNote.objs.handoverPerson;
				var src =  basePath + "page/operation/core/operationlog/select_role_user.jsp";
			    var btnOpts = [{
			            "name" : "取消",
			            "float" : "right",
			            "style" : "btn-default",
			            "onclick" : function(){
			                return true;
			            }
			        },{
			            "name" : "确定",
			            "float" : "right",
			            "style" : "btn-success",
			            "onclick" : function(){
			            	var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
			            	g.users = p.getChecked();
			            	OprNote.saveHandoverPerson();
			            	_parent().$("#itcDlg").dialog("close");
			            }
			        }
			    ];
			    var dlgOpts = {
			        width : 450,
			        height: 420,
			        closed : false,
			        title:"选择值班人员",
			        modal:true
			    };
			    Notice.dialog(src,dlgOpts,btnOpts);
			});
		},
		
		/**
		 * 页面控制的函数
		 */
		changeHandoverMode:function(mode){//切换交接班的页面
			if(!mode){
				mode="no";//默认不交接
			}
			if(OprNote.objs.opts.handover_mode==mode)
				return;
			OprNote.objs.opts.handover_mode=mode;
			
			if("no"==mode){//非交接班时
				$("#btn_before, #btn_next, #jiaojieCancel, #jiaobanSaveBtn,#jiaoJieInnerTitle,#noteJobsLevelDiv,#jiaojieDiv").hide();
				$("#noteInnerTitle,#baseInfoDiv,#noteListDiv,#modeFormDiv,#jobsDiv,#printButton").show();
				$("#modeContentDiv").iFold("hide");
				
				if(OprNote.isMode("edit")){
					$("#modeSaveBtn,#userButton, #btn_jiaoban, #dfromButton,#addDetailDiv").show();
					$("#baseform").iForm("beginEdit");
					$("#modeContentForm").iForm("beginEdit");
				}
			}else if("to_confirm"==mode){//开始交接班，确认内容
				$("#modeSaveBtn,#userButton, #btn_jiaoban, #dfromButton, #jiaojieDiv, #noteInnerTitle,#addDetailDiv, #jiaobanSaveBtn,#jobsDiv").hide();
				$("#btn_before, #btn_next, #jiaojieCancel,#jiaoJieInnerTitle,#baseInfoDiv,#noteListDiv,#noteJobsLevelDiv,#printButton").show();
				
				$("#modeContentDiv").iFold("show");
				//运行方式
				OprMode.teamSelectData(OprNote.objs.selectedJobsId);
				
				var currentDutyId = $("#f_currentDutyId").val();
				var nowScheduleId = $("#f_nowScheduleId").val();
				//$("#noteTb").datagrid("reload");
				$("#baseform").iForm("endEdit");
				$("#modeContentForm").iForm("endEdit");
				$('#noteTb').datagrid("hideColumn","updateBy");
				
				//下一步按钮
				$("#btn_next").click(function(){
					$("#modeSaveBtn").click();
					OprNote.changeHandoverMode("to_post");
				});
				//上一步按钮
				$("#btn_before").click(function(){
					if(OprNote.isMode("edit")){//显示垃圾桶
						$('#noteTb').datagrid("showColumn","updateBy");
					}
					OprNote.changeHandoverMode("no");
				});
			}else if("to_post"==mode){//选择交接班次并提交
				$("#modeSaveBtn,#userButton, #btn_jiaoban, #dfromButton, #baseInfoDiv,#noteListDiv,#addDetailDiv,#noteJobsLevelDiv,#noteInnerTitle, #btn_next,#jobsDiv,#printButton").hide();
				$("#btn_before, #jiaojieCancel, #jiaobanSaveBtn,#jiaojieDiv,#jiaoJieInnerTitle").show();
				$("#modeContentDiv").iFold("hide");
				if(!OprNote.objs.opts.canChooseJiaojieBan){
					$("#jiaojieForm").iForm("endEdit",["nextShiftDate","nextScheduleId"]);
				}
				//上一步按钮
				$("#btn_before").click(function(){
					OprNote.changeHandoverMode("to_confirm");
				});
			}
			//按钮权限
			 setNotePriv();
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
				$("#addDetailDiv,#modeSaveBtn,#userButton,#btn_jiaoban,#dfromButton,#printButton").hide();
			}else{
				$("#error_area").hide();
				$("#contentDiv").show();
				if(Priv.hasPrivilege("OPR_NOTE_PRINT")){
					$("#printButton").show();
				}
				if("view"==mode){
					$("#addDetailDiv,#modeSaveBtn,#userButton,#btn_jiaoban,#dfromButton").hide();
				}else if("edit"==mode){
					$("#addDetailDiv,#modeSaveBtn,#userButton,#btn_jiaoban,#dfromButton").show();
				}
				
				OprNote.changeHandoverMode("no");
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
						OprHandover.currentHandoverId = currentHandover.id;
						currentShiftStartTime = currentHandover.currentShiftStartTime;
						nowShiftDateLong = currentHandover.nowShiftDate;
						var lastHandover = data.lastHandover;
						var lastContent = lastHandover == null ? "没有上一班情况" : lastHandover.nextContent;
						var lastRemark = lastHandover == null ? "没有上一班交待" : lastHandover.nextRemark;
						var dateTime = new Date(currentHandover.nowShiftDate).Format("yyyy-MM-dd");
						var currentDutyName = currentHandover.currentDutyName;
						var currentShiftName = currentHandover.currentShiftName;
						var currentDeptName = currentHandover.currentDeptName;
						
						var schedulePerson={};
						if(data.schedulePersonList){
							for(var i=0;i<data.schedulePersonList.length;i++){
								schedulePerson[data.schedulePersonList[i].userId]=data.schedulePersonList[i].userName;
							}
						}
						OprNote.objs.schedulePerson=schedulePerson;	
						var handoverPerson={};
						if(data.handoverPersonList){
							for(var i=0;i<data.handoverPersonList.length;i++){
								handoverPerson[data.handoverPersonList[i].userId]=data.handoverPersonList[i].userName;
							}
						}
						OprNote.objs.handoverPerson=handoverPerson;
						
						OprNote.objs.nowScheduleId=currentHandover.nowScheduleId;
						
						//基本信息
						var keyword = jobsId + "_" + stationId + "_" + OprHandover.currentHandoverId;
						keyword = keyword.replaceAll("-", "_").replaceAll("_", "");
						var formData = {
								"currentDutyId" : currentHandover.currentDutyId,
								"currentShiftId" : currentHandover.currentShiftId,
								"nowScheduleId" : currentHandover.nowScheduleId,
								"lastContent_base" : lastContent,
								"lastRemark_base" : lastRemark,
								"dutyName" : currentDeptName+" "+currentDutyName + " " + currentShiftName,
								"dutyPerson" : OprNote.getMemberString(data.handoverPersonList/*currentPersonList*/),
								"dateTime" : dateTime,
								"keyword" :keyword,
								"preShiftMeeting":currentHandover.preShiftMeeting,
								"postShiftMeeting":currentHandover.postShiftMeeting
						};
						//动态表单
						OprNote.dynamicForm(jobsId, stationId, OprHandover.currentHandoverId);
						$("#baseform").iForm("setVal",formData);
						$("#baseform").iForm("hide",OprNote.objs.noteFormHideField);
						
						//运行记事列表
						OprNote.setNoteDatagrid( jobsId, OprHandover.currentHandoverId );
						if(!OprNote.isMode("edit")){//隐藏垃圾桶
							$('#noteTb').datagrid("hideColumn","updateBy");
						}
						
						//交接班
						var nextShiftDateString=new Date(currentHandover.nextShiftDate).Format("yyyy-MM-dd");
						var currentShiftStartTime=currentHandover.currentShiftStartTime;
						var jiaojieFormData = {
								"nowShiftDate" : dateTime,
								"currentDutyName" : currentDutyName,
								"currentShiftName" : currentShiftName,
								"currentShiftStartTime":currentShiftStartTime,
								"nextShiftDate" : nextShiftDateString,
								"lastContent" : lastContent,
								"lastRemark" : lastRemark,
								"jobsId" : jobsId,
								"stationId" : currentHandover.stationId
						};
						var jiaojieFields = OprHandover.getJiaojieField(nextShiftDateString);
						$("#jiaojieForm").iForm("init",{"options":opts,"fields":jiaojieFields});
						$("#f_nextContent").height("55px");
						$("#f_nextRemark").height("55px");
						$("#jiaojieForm").iForm("setVal",jiaojieFormData);
						OprHandover.setAccountCombo(data.personJobsOfStation,data.loginUserId);
						OprHandover.setNextScheduleIdCombo(data.nextShiftCalendars,currentHandover.nextScheduleId);
					}else{
						FW.error(data.returnCode);
						OprNote.changeErrorMode("init_error");
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
		},
		
		//动态表单初始化
		dynamicForm:function( jobsId, stationId, handoverId ){
			pageCode = "opr_note_formcode_";
			var keyword = jobsId + "_" + stationId + "_" + handoverId;
			keyword = keyword.replaceAll("-", "_").replaceAll("_", "");
			/**** -----------------------------------------------动态表单 start ------------------------------ */
			var tempStation = '';
			if( stationId != null && stationId != "" && stationId.length > 8 ){
				tempStation = stationId.replaceAll("-", "_").replaceAll("_", "").substring(8);
			}else{
				tempStation = stationId;
			}
			pageCode += jobsId + "_" + tempStation;
			pageCode = pageCode.replaceAll("_", "");
			var formOptions = {
				validate:true,
				labelFixWidth: 200
			};
			var formSearchParam = { "keyword" : keyword, "sheetId" : keyword, "formSwitch" : "on"};
			var formUrlParam = basePath + "operation/note/queryOnDutyBaseInfo.do";
			/**** -----------------------------------------------动态表单 end ------------------------------ */
			
			//初始化基础信息
			FW.dynamicForm(OprNote.objs.baseFields, formOptions, formSearchParam, formUrlParam);
			
			if(!OprNote.isMode("edit")){
				$("#"+formId).iForm("endEdit");
			}
		},
		
		//加载运行记事
		setNoteDatagrid:function( jobsId, handoverId ){
			var listUrl = basePath + "operation/note/queryNoteListByCurrentUser.do?jobsId=" + jobsId
				+ "&handoverId=" + handoverId;
			$("#noteTb").datagrid({
		        pageSize:OprNote.objs.opts.pageSize,//pageSize为全局变量，自动获取的
		        singleSelect:true,
		        pagination:false,
		        fitColumns:true,
		        nowrap : false,
		        url: listUrl,	//basePath为全局变量，自动获取的       
		        columns:[[
					{field:'id',title:'id',width:20,fixed:true,checkbox : true,hidden: true},
					{field:'writeTime',title:'记事时间',width:130,fixed:true,sortable:true,
						formatter:function(val){
							return new Date( val ).Format("yyyy-MM-dd hh:mm");
						}
					},
					{field:'type',title:'记事类型',width:140,fixed:true,sortable:true,formatter:function(val){
						return FW.getEnumMap("OPR_NOTE_TYPE")[val];
					}},
					{field:'crewNum',title:'机组号',width:70,fixed:true,sortable:true,formatter:function(val){
						return FW.getEnumMap("OPR_CREW_NUM")[val];
					}},
					{field:'content',title:'记事内容',width : 100,formatter:function(val,row,index){
						return val?val.replace(/\n|\r\n/g,"<br/>"):"";
					}},
					{field:'usedFor',title:' ',width:50,fixed:true,
						 formatter:function(value,row,index){
		  				     return '<span onclick="OprNote.showDtlIframe('+
		  				     		row.id +')" width="16" height="16" style="cursor:pointer">引用<span/>';
		  				}	
					},
					{field:'updateBy',title:' ',width:50,fixed:true,
						 formatter:function(value,row,index){
		  				     return '<img src="'+basePath+'img/operation/btn_garbage.gif" onclick="OprNote.deleteNoteById('+
		  				     		row.id +')" width="16" height="16" style="cursor:pointer" />';
		  				}	
					}
				]],
				onLoadError: function(){
					OprNote.changeErrorMode("grid1_error");
				},
		        onBeforeLoad : function() {
			    	if(OprNote.isMode("edit")){//编辑时
			    		$('#noteTb').datagrid("showColumn","updateBy");
			    	}else{
			    		$('#noteTb').datagrid("hideColumn","updateBy");
			    	}
			    	if(OprNote.objs.userJobs.length==0||OprNote.isUserJobs(jobsId)){//用户无可用岗位或自己的岗位不可引用
			    		$('#noteTb').datagrid("hideColumn","usedFor");
			    	}else{
			    		$('#noteTb').datagrid("showColumn","usedFor");
			    	}
			    	if(noteDatagridHideColumns){
			    		$.each(noteDatagridHideColumns,function(name,value){
			    			$('#noteTb').datagrid("hideColumn",value);
			    		});
		        	}
			    },
			    onDblClickRow : function(index,rowData){
			    	if(OprNote.isMode("edit")&&OprNote.isMode("no", "handover_mode")){
			    		OprNote.showDtlIframe( rowData.id );
			    	}
			    }
		    });
		},
		
		showDtlIframe:function( noteId ){
			if( noteId == null || noteId < 0 ){
				noteId = 0;
			}
			var oprName=(OprNote.isMode("edit")?(noteId>0?"更新":"添加"):"引用")+"记事";
			var src = basePath + "operation/note/insertNoteItemToPage.do?noteId=" + noteId;
			FW.set("opr_note_userJobs",OprNote.isMode("edit")?[]:OprNote.objs.userJobs);//如果是引用，则传用户的岗位列表过去，否则要传空数组过去
			var pri_dlgOpts = {
				width : 800,
				height : OprNote.isMode("edit")?535:565,
				closed : false,
				title : oprName,
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
				"name" : (OprNote.isMode("edit")?"保存":"引用"),
				"float" : "right",
				"style" : "btn-success",
				"onclick" : function() {				
					var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
					if (!conWin.valid()) {
						return false;
					}
					var formData = conWin.getFormData("noteForm");
					
					if( OprHandover.currentHandoverId == null || OprHandover.currentHandoverId < 0 || OprHandover.currentHandoverId == "" ){
						FW.error("网络错误，请联系管理员！");
						return false;
					}
					var currentDutyId = $("#f_currentDutyId").val();
					var jobsId = $("#jobsSelect").iCombo("getVal");
					
					var url = '';
					if(OprNote.isMode("edit")){
						if( noteId > 0 ){
							url = basePath + "operation/note/updateNote.do?jobsId=" + jobsId;
						}else{
							url = basePath + "operation/note/insertNote.do?dutyId=" + currentDutyId
							+ "&jobsId=" + jobsId + "&handoverId=" + OprHandover.currentHandoverId;
						}
					}else{
						url = basePath + "operation/note/useNote.do";
					}
					
					$.ajax({
						url : url,
						type : 'post',
						dataType : "json",
						data:{"formData":formData},
						success : function(data) {
							if( data.result == "success" ){
								FW.success( oprName+"成功 ！");
								$('#noteTb').datagrid('reload');
								_parent().$("#itcDlg").dialog("close");
								//resetNoteForm();
							}else if( data.reason != null && data.reason != "" && data.result == "fail" ){
								FW.error( data.reason );
							}else{
								FW.error( oprName+"失败 ！请检查引用到的岗位是否有运行记事信息");
							}
						}
					});
				}
			}];

			FW.dialog("init", {
				"src" : src,
				"dlgOpts" : pri_dlgOpts,
				"btnOpts" : btnOpts
			});
		},

		deleteNoteById:function( id ){
			FW.confirm("确定删除本条数据吗？",function(){
				var url = basePath + "operation/note/deleteNote.do?id=" +  id;
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "删除运行记事成功 ！");
							$('#noteTb').datagrid('reload');
							//resetNoteForm();
							$("#noteResetBtn").click();
						}else if( data.reason != null && data.reason != "" && data.result == "fail" ){
							FW.error( data.reason );
						}else{
							FW.error( "删除运行记事失败 ！");
						}
					}
				});
			});
		},
		//检查当前班次，是否有未完成的定期工作，如果没有，则交接班
		checkUnfinishSchedultTask:function( currentShiftId ){
			var url = basePath + "operation/scheduleTask/checkUnfinishSchedultTask.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:{"currentShiftId":currentShiftId,"nowShiftDateLong":nowShiftDateLong},
				success : function(data) {
					if( data.result == "success" ){
						if($("#btn_jiaoban").is(":visible")&&OprNote.objs.opts.onlySchedulePersonJiaojieBan&&!OprNote.objs.handoverPerson[Priv.secUser.userId]){
							FW.error("当前登录用户并非当前值班人员");
						}else{
							OprNote.changeHandoverMode("to_confirm");
						}
					}else{
						FW.error( "请完成定期工作或把工作转到其他班次后再交班");
					}
				}
			});
			
		},
		//运行方式&&基础信息动态表单 保存
		modeSave:function( ){
			if(!$("#baseform").valid()||!$("#modeContentForm").valid()){
				return;
			}
			if( OprHandover.currentHandoverId == null || OprHandover.currentHandoverId < 0 || OprHandover.currentHandoverId == "" ){
				FW.error("网络错误，请联系管理员！");
				return;
			}
			var currentDutyId = $("#f_currentDutyId").val();
			var jobsId = $("#jobsSelect").iCombo("getVal");
			//var team = $("#teamSelect").iCombo("getVal");
			var baseFormData = getFormData( "baseform" );
			//var modeContentFormData = getFormData( "modeContentForm" );
			var url = basePath + "operation/modeContent/insertOrUpdateModeContentFromMode.do";
			$.ajax({
				url : url,
				type : 'post',
				data :{
					formData : baseFormData,
					//modeContentFormData : modeContentFormData,
					dutyId : currentDutyId,
					jobsId : jobsId,
					handoverId : OprHandover.currentHandoverId//,
					//team : team
				},
				dataType : "json",
				success : function(data) {
					if( data.result == "success" ){
						if(data.dyFormMsg){
							FW.error( "运行记事保存失败！请检查内容是否过长" );
						}else{
							FW.success( "保存运行记事成功 ！");
						}
					}else{
						FW.error( "新增运行记事失败 ！");
					}
				}
			});
		},
		
		//保存修改的值班人员
		saveHandoverPerson:function(){
        	var lst = [];
        	var str="";
        	for(var k in g.users){
        		lst.push(k);
        		str+=g.users[k]+",";
        	}
        	if(str==""){
        		str="没有值班人员";
        	}else{
        		str = str.substring(0, str.length - 1);
        	}
        	if($("#baseform").iForm("getVal","dutyPerson")==str){//选择的人没有改变
        		return;
        	}
        	
        	$.ajax({
				url : basePath + "operation/handover/updateHandoverPerson.do",
				type : 'post',
				data :{
					handoverId : OprHandover.currentHandoverId,
					userIdStr : FW.stringify(lst)
				},
				dataType : "json",
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "值班人员修改成功 ！");
						OprNote.objs.handoverPerson=g.users;
						$("#baseform").iForm("setVal",{"dutyPerson":str});
					}else{
						FW.error( "值班人员修改失败 ！");
					}
				}
			});
		},
		
		printNote:function(){
			var jId = $("#jobsSelect").iCombo("getVal");
			var sId = OprNote.objs.jobsMap[$("#jobsSelect").iCombo("getVal")].stationId; 
			var userId = Priv.secUser.userId;
			var scheduleId = OprNote.objs.nowScheduleId;
			var reportName = printNoteReportName?printNoteReportName:"TIMSS2_OPRLOG_001";
			var url= fileExportPath+"preview?__format=pdf&__report=report/"+reportName+".rptdesign&jobsId="+jId
			+"&stationId="+sId
			+"&siteId="+siteId
			+"&scheduleId="+scheduleId
			+"&author="+userId
			+"&__isnull=url";
			FW.dialog("init",{
				src: url,
				btnOpts:[
						{
						    "name" : "关闭",
						    "float" : "right",
						    "style" : "btn-default",
						    "onclick" : function(){
						        _parent().$("#itcDlg").dialog("close");
						    }
						}
			        ],
				dlgOpts:{ width:800, height:650, closed:false, title:"运行记事打印", modal:true }
			});
		}
};

String.prototype.replaceAll = function(s1,s2) { 
    return this.replace(new RegExp(s1,"gm"),s2); 
};