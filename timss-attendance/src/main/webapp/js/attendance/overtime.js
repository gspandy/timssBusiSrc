var Overtime={
		objs:{
			formFields:[],
	  		fileFields:[],
	  		datagridFields:[],
	  		taskId:"",//任务Id
    		processInstId:"",//流程实例ID
    		applyFlag:"",//审批标志
    		overtimeId:"",//加班单id
			status:"",//加班单状态
			userId:"",//当前登录人
    		applicant:{},//申请人信息
    		flowDiagram:"atd_?_overtime",
    		siteId:"",
    		opts:{
    			mode:"view",//页面模式，add/edit/view
    			formOpts:{
					validate:true,
					fixLabelWidth:true
				},
				statusToApproveRealOverHours:["部门经理核定加班时长"],//核定加班时长的状态
				statusMapToApproveRealOverHours:{//指定站点的核定加班时长的状态
					"SWF":["部门考勤员审核"],
					"DPP":["分部意见","部门意见"],
					"HYG":["核定加班时长"]
				},
				isNeedRealOverHours:true,//是否需要核定时长
				siteNotNeedRealOverHours:[],//不需要核定时长的站点id
				statusToApproveTransferCompensate:["行政部登记备案"],//核定转补休时长的状态
				statusMapToApproveTransferCompensate:{//指定站点的核定转补休时长的状态
					"SWF":["行政部登记备案"],
					"HYG":["加班归档"]
				},
				isNeedTransferCompensate:false,//是否需要核定转补休时长
				siteNeedTransferCompensate:["SWF","HYG"],//需要核定转补休时长的站点id
				sessId:"",
				valKey:"",
				printSite:["SWF"],//有打印的站点
				isFileEndEdit:false,//文件编辑是否需要endEdit
				isShowAuditBtn:false,//是否在审批框中显示审批按钮
				isChangeSaved:true//修改是否已提交完成
    		}
		},

		changeMode:function(mode){
			if(!mode){
				mode="view";
			}
			if(mode==Overtime.objs.opts.mode){
				return;
			}else{
				Overtime.objs.opts.mode=mode;
			}
			
			//Overtime.refreshModeShow();
		},
		refreshModeShow:function(){//根据mode展示页面元素
			if(!Overtime.objs.opts.isChangeSaved){
				$("#overtimeDatagrid").datagrid('acceptChanges');
				Overtime.objs.opts.isChangeSaved=true;
			}
			
			if(Overtime.isMode("view")){
				
			}else if(Overtime.isMode("add")){
				Overtime.changeEditDatagrid(true);
				$("#printDiv").hide();
				if(Priv.hasPrivilege("atd_ot_insert_delete")){
					if(Overtime.objs.overtimeId){
						$("#delBtnDiv,#deleteButton").show();
					}else{
						$("#deleteButton").hide();
					}
				}
			}else if(Overtime.isMode("edit")){
				//控制工具条按钮的隐藏
				//当有审批权限 状态是草稿，说明还在申请阶段
				if( Overtime.objs.userId == Overtime.objs.applicant.userId && ( Overtime.objs.status == "草稿" 
						  || Overtime.objs.status == "提交加班申请" ) ){
					$( "#approveButtonDiv" ).hide();
					$( "#editButtonDiv,#unifyButtonDiv,#delBtnDiv" ).show();
					$("#autoform").iForm("beginEdit",["overTimeReason"]);
					$("#autoform").iForm("endEdit",["createDay"]);
					Overtime.changeEditDatagrid(true);
					
					if(Overtime.objs.status == "草稿" ){
						$("#autoform").iForm("hide",["createDay"]);
						if(Overtime.objs.opts.isNeedRealOverHours){
							$("#overtimeDatagrid").datagrid("hideColumn","realOverHours");
						}
						if(Overtime.objs.opts.isNeedTransferCompensate){
							$("#overtimeDatagrid").datagrid("hideColumn","transferCompensate");
						}
						if(Priv.hasPrivilege("atd_ot_insert_delete"))$("#deleteButton").show();
						$("#invalidButton").hide();
						$("#inner-title").html("编辑加班申请");
					}else if(Overtime.objs.status =="提交加班申请" ){
						$("#deleteButton").hide();
						if(Priv.hasPrivilege("atd_ot_insert_invalid"))$("#invalidButton").show();
						$("#inner-title").html("编辑加班申请");
					}
				}else if(Overtime.objs.applyFlag =="approver"){
					Overtime.objs.opts.isShowAuditBtn = true;
					Overtime.objs.opts.isFileEndEdit = true;
					$( "#editButtonDiv,#unifyButtonDiv,#delBtnDiv" ).hide();
					$( "#approveButtonDiv" ).show();
					$("#autoform").iForm("endEdit");
					
					if(Overtime.checkStatusToApproveRealOverHours()){
						//$("#inner-title").html("核定加班时长");
						$("#inner-title").html("加班申请详情");
						Overtime.changeEditDatagrid(true,false,true);
					}else if(Overtime.checkStatusToApproveTransferCompensate()){
						//$("#inner-title").html("核定转补休时长");
						$("#inner-title").html("加班申请详情");
						Overtime.changeEditDatagrid(true,false,true);
					}else{
						$("#inner-title").html("加班申请详情");
						Overtime.changeEditDatagrid(false);
					}
				}else{
					Overtime.objs.opts.isFileEndEdit = true;
					$( "#approveButtonDiv,#editButtonDiv,#unifyButtonDiv,#delBtnDiv" ).hide();
					$("#autoform").iForm("endEdit");
					Overtime.changeEditDatagrid(false);
					$("#inner-title").html("加班申请详情");
				}
			}
			FW.fixRoundButtons("#toolbar");
		},
		
		init:function(mode){
			Overtime.initSite();
			Overtime.changeMode(mode);
			Overtime.initFields();
			$("#autoform").iForm("init",{"options":Overtime.objs.opts.formOpts,"fields":Overtime.objs.formFields});
			$("#fileDiv").iFold("init");
			$("#detailTitle").iFold("init");
			Overtime.initBtn();
			Overtime.loadData();
		},
		initSite:function(){//站点个性化初始化
			//设置是否需要核定时长的操作
			if($.inArray(Overtime.objs.siteId,Overtime.objs.opts.siteNotNeedRealOverHours)>-1){
				Overtime.objs.opts.isNeedRealOverHours=false;
			}
			if($.inArray(Overtime.objs.siteId,Overtime.objs.opts.siteNeedTransferCompensate)>-1){
				Overtime.objs.opts.isNeedTransferCompensate=true;
			}
			//打印
			if($.inArray(Overtime.objs.siteId,Overtime.objs.opts.printSite)<0){
				$("#printDiv").hide();
			}
			//核定加班时长的节点名
			if(Overtime.objs.opts.statusMapToApproveRealOverHours[Overtime.objs.siteId]){//如果有指定，否则用默认的
				Overtime.objs.opts.statusToApproveRealOverHours=Overtime.objs.opts.statusMapToApproveRealOverHours[Overtime.objs.siteId];
			}
			//核定转补休时长的节点名
			if(Overtime.objs.opts.statusMapToApproveTransferCompensate[Overtime.objs.siteId]){//如果有指定，否则用默认的
				Overtime.objs.opts.statusToApproveTransferCompensate=Overtime.objs.opts.statusMapToApproveTransferCompensate[Overtime.objs.siteId];
			}
		},
		initFields:function(){//初始化字段
			Overtime.objs.formFields=[
				{title : "id", id : "id",type:"hidden"},
				{title : "申请人id", id : "createBy",type:"hidden"},
				{title : "申请人部门id", id : "deptId",type:"hidden"}
	  		];
			if(!Overtime.isMode("add")){
				Overtime.objs.formFields.push(
					{title : "申请编号", id : "num",type:"label"}                         
				);
			}
			Overtime.objs.formFields.push({title : "申请人", id : "userName",type:"label"});
			if(!Overtime.isMode("add")){
				Overtime.objs.formFields.push(
					{title : "申请时间", id : "createDay",type : "datetime",
						 dataType : "datetime"}                         
				);
			}
			Overtime.objs.formFields.push({title : "申请事由",id: "overTimeReason",linebreak:true,type:'textarea',
				wrapXsWidth:12,wrapMdWidth:8,height:48,rules : {maxChLength : parseInt(1000*2/3),required:true}
			});
			
			Overtime.objs.fileFields=[
  	    		{id:"attachment",title:" ",type:"fileupload",wrapXsWidth:12,wrapMdWidth:12,options:{
				    "uploader" : basePath + "upload?method=uploadFile&jsessionid="+Overtime.objs.opts.sessId,
				    "delFileUrl" : basePath + "upload?method=delFile&key="+Overtime.objs.opts.valKey,
				    "downloadFileUrl" :  basePath + "upload?method=downloadFile",
				    "swf" : basePath + "itcui/js/uploadify.swf",
				    "fileSizeLimit":10*1024,
				    "delFileAfterPost" : true
				}}
    		];
			
			Overtime.objs.flowDiagram=(Overtime.objs.flowDiagram&&Overtime.objs.siteId)
				?(Overtime.objs.flowDiagram.replace("?", Overtime.objs.siteId.toLowerCase())):"";
				
			Overtime.objs.datagridFields=[
			    {field:'userId',title:'工号',width:60,fixed:true},                          
				{field:'userName',title:'加班人员',width:60,fixed:true},
				{field:'startDate',title:'开始时间',width:140,fixed:true,
					editor:{
						type:"datebox",
						"options" : {
							onChange : 	Overtime.calOvertimeHourTime,
							dataType:"datetime",
							rules : {
								required : true
							}
						}
					},
					formatter:function(value,row,index){
						return FW.long2time(value);
					}
				},
				{field:'endDate',title:'结束时间',width:140,fixed:true, 
					editor : {
						type:"datebox",
						"options" : {
							onChange : 	Overtime.calOvertimeHourTime,
							rules : {
								required : true,
								greaterThan : '%startDate',
								messages : {
									greaterThan : "结束时间必须大于开始时间"
								}
							},
							dataType:"datetime"
						}
					},
					formatter:function(value,row,index){
						return FW.long2time(value);
					}
				}
			];
			if(Overtime.isMode("add")){
				Overtime.objs.datagridFields.push({title:"合计工时",field:"planOverHours",width:80,fixed:true,
					editor:{
				        type:"text",
				        "options":{
				        	rules:{required:true,number:true,min:0.001}	
				        }
					},
					formatter:function(value,row,index){
						return value?AtdDataFormat.formatDoubleDays(value):0;
					}
				});
			}else{
				Overtime.objs.datagridFields.push(
					{title:"申请时长",field:"planOverHours",width:100,fixed:true,
						editor:{
					        type:"text",
					        "options":{
					        	rules:{required:true,number:true,min:0.001}	
					        }
						},
						formatter:function(value,row,index){
							return value?AtdDataFormat.formatDoubleDays(value):0;
						}
					}                   
				);
				if(Overtime.objs.opts.isNeedRealOverHours){
					Overtime.objs.datagridFields.push(
						{title:"核定时长",field:"realOverHours",width:100,fixed:true,
							editor:{
						        type:"text",
						        "options":{
						        	rules:{required:true,number:true,min:0}
						        }
							},
							formatter:function(value,row,index){
								return value?AtdDataFormat.formatDoubleDays(value):0;
							}
						}
					);
				}
				if(Overtime.objs.opts.isNeedTransferCompensate){
					Overtime.objs.datagridFields.push(
						{title:"转补休时长",field:"transferCompensate",width:100,fixed:true,
							editor:{
						        type:"text",
						        "options":{
						        	rules:{required:true,number:true,min:0}
						        }
							},
							formatter:function(value,row,index){
								return value?AtdDataFormat.formatDoubleDays(value):0;
							}
						}
					);
				}
			}
			Overtime.objs.datagridFields.push(
				{field:'totalHours',title:'当月加班时长',width:100,fixed:true,
					formatter:function(value,row,index){
						return value?AtdDataFormat.formatDoubleDays(value):0;
					}
				},	
				{title:"备注",field:"remarks",width:100,
					editor:{
				        type:"text",
				        "options" : {
							rules : {
								maxChLength:parseInt(400*2/3)
							}
						}
					}
				},
				{field:'id',title:' ',width:50,fixed:true,
					 formatter:function(value,row,index){
	  				     return '<img src="'+basePath+'img/attendance/btn_garbage.gif" width="16" height="16" style="cursor:pointer" />';
	  				}	
				}                       
			);
		},
		
		initBtn:function(){
			//添加加班详情
			$( "#addDetail" ).click(function(){
				Overtime.showDtlIframe();
			});
			//统一加班时间
			$( "#unifyButton" ).click(function(){
				Overtime.showDtlIframe(true);
			});
			//打印
			$( "#printBtn" ).click(function(){
				if(Overtime.isMode("add")){//新建模式没有打印按钮
					return;
				}
				var url = fileExportPath + "preview?__report=report/TIMSS2_"+Overtime.objs.siteId+
											"_OVERTIME_001_pdf.rptdesign&__format=pdf&id=" + Overtime.objs.overtimeId+"&siteid="+Priv.secUser.siteId;
				//window.open(url);
				FW.dialog("init",{src: url,
					btnOpts:[{
					"name" : "关闭",
					"float" : "right",
					"style" : "btn-default",
					"onclick" : function(){
					 _parent().$("#itcDlg").dialog("close");
					 }
					}
				    ],
					dlgOpts:{ width:800, height:650, closed:false, title:"打印加班申请", modal:true }
				 });
			});
			//提交
			$( "#commitButton" ).click(function(){
				if(!$("#autoform").valid()||!Overtime.validDatagrid()){
					return;
				}
				var dataCommit=Overtime.getDataForSubmit();
				
				if( Overtime.objs.processInstId != null && Overtime.objs.processInstId != "" ){//非新建的提交包含了退回的操作
					var workFlow = new WorkFlow();
					workFlow.showAudit(Overtime.objs.taskId,JSON.stringify($.extend({},{
						processInstId:Overtime.objs.processInstId
					},dataCommit)),closeTab,null,null,null,null,Overtime.loadData);
				}else{//新建和启动流程
					$(this).button("loading");
					var url = basePath + "attendance/overtime/submitOvertime.do" ;
					$.ajax({
						url : url,
						type : 'post',
						dataType : "json",
						data:dataCommit,
						success : function(data) {
							if( data.result == "success" ){
								FW.success( "提交成功 ！");
								Overtime.loadData(data);
								var taskId = data.taskId;
								if(taskId != null){
									var workFlow = new WorkFlow();
									dataCommit=Overtime.getDataForSubmit();
									workFlow.submitApply(taskId,JSON.stringify($.extend({},{
										processInstId:data.processInstId
									},dataCommit)),closeTab,null,0);
								}
							}else{
								if( data.reason != null ){
									FW.error( data.reason );
								}else {
									FW.error( "提交失败 ！");
								}
							}
							$( "#commitButton" ).button("reset");
						}
					});
				}
			});
			
			//暂存
			$( "#saveButton" ).click(function(){
				if(!$("#autoform").valid()||!Overtime.validDatagrid()){
					return;
				}
				var data=Overtime.getDataForSubmit();
				
				var url = basePath + "attendance/overtime/saveOvertime.do" ;
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					data:data,
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "保存成功 ！");
							Overtime.loadData(data);
						}else{
							FW.error( "保存失败 ！");
						}
					}
				});
			});
			
			//显示流程图
			$("#flowDiagramBtn").click(function(){
				if( isNull( Overtime.objs.processInstId ) ){
					if(Overtime.objs.flowDiagram){
						showFlowDialog(Overtime.objs.flowDiagram);
					}else{
						FW.error("没有流程图信息");
					}
				}else{
					var workFlow = new WorkFlow();
					workFlow.showAuditInfo(Overtime.objs.processInstId,"",Overtime.objs.opts.isShowAuditBtn,function(){$("#approveBtn").click();}/*Overtime.audit*/);
				}
					
			});
			
			//审批
			$("#approveBtn").click(function(){
				if(Overtime.checkStatusToApproveRealOverHours()){
					FW.confirm("请确认已核定加班时长",function(){
						Overtime.audit();
					});
				}else if(Overtime.checkStatusToApproveTransferCompensate()){
					FW.confirm("请确认已核定转补休时长",function(){
						Overtime.audit();
					});
				}else{
					Overtime.audit();
				}
			});
			
			//删除
			$( "#deleteButton" ).click(function(){
				var formData = $( "#autoform" ).iForm("getVal");
				
				FW.confirm("确定删除本条数据吗？该操作无法恢复。", function() {
					var url = basePath + "attendance/overtime/deleteOvertime.do?id=" +  formData.id;
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
			
			//作废
			$( "#invalidButton" ).click(function(){
				var formData = $( "#autoform" ).iForm("getVal");
				FW.confirm("确定作废本条数据吗？该操作无法恢复。", function() {
					var url = basePath + "attendance/overtime/invalidOvertime.do?id=" +  formData.id;
					$.ajax({
						url : url,
						type : 'post',
						dataType : "json",
						success : function(data) {
							if( data.result == "success" ){
								FW.success( "作废成功 ！");
								closeTab();
							}else{
								FW.error( "作废失败 ！");
							}
						}
					});
				});
			});
			
			Priv.apply();//控制按钮权限
		},

		isMode:function(mode,modeName){
			if(!modeName)modeName="mode";
			return Overtime.objs.opts[modeName]==mode;
		},
		
		validDatagrid:function(){
			var rowDatas = $("#overtimeDatagrid").datagrid('getRows');
			if( rowDatas.length <= 0 ){
				FW.error( "请添加加班 ！");
				return false;
			}else{
				return $("#overtimeDatagridForm").valid();
			}
		},
		
		getDataForSubmit:function(isOnlyUpdate){
			Overtime.objs.opts.isChangeSaved=false;
			Overtime.changeEditDatagrid(false);
			var addRows=$("#overtimeDatagrid").datagrid('getChanges','inserted');
			var delRows=$("#overtimeDatagrid").datagrid('getChanges','deleted');
			var updateRows=$("#overtimeDatagrid").datagrid('getChanges','updated');
			Overtime.changeEditDatagrid(true,false,isOnlyUpdate);
			
			var formData = getFormData( "autoform" );
			//文件ids
			var fileIds = $("#fileForm").iForm("getVal").attachment;
			
			return {
				formData:formData,
				addRows:JSON.stringify(addRows),
				delRows:JSON.stringify(delRows),
				updateRows:JSON.stringify(updateRows),
				fileIds:fileIds
			};
		},
		
		loadData:function(data){
			if(data){
				Overtime.mountData(data);
			}else{
				if(Overtime.isMode("add")){//如果是新建，加载新建的数据
					$("#autoform").iForm("setVal",{
						userName:Overtime.objs.applicant.userName + " / " + Overtime.objs.applicant.deptName,
						deptId:Overtime.objs.applicant.deptId,
						createBy:Overtime.objs.applicant.userId
					});
					Overtime.setOvertimeDatagrid(null);
					Overtime.refreshModeShow();
					Overtime.setFile(null);
				}else{//否则获取加载加班单的数据
					Overtime.getData(Overtime.objs.overtimeId);
				}
			}
		},
		
		//获取加班单信息
		getData:function( id ){
			var url = basePath + "attendance/overtime/queryOvertimeById.do?id=" + id;
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				success : function(data) {
					if( data.result == "success"){
						Overtime.mountData(data);
					}
				}
			});
		},
		//装载数据
		mountData:function(data){
			//任务ID
			Overtime.objs.taskId = data.taskId;
			//审批状态
			Overtime.objs.applyFlag = data.applyFlag;
			//当前登录人
			Overtime.objs.userId = data.userId;
			
			var overtimeData=data.rowData;
			Overtime.objs.processInstId = overtimeData.instantId;
			Overtime.objs.overtimeId = overtimeData.id;
			Overtime.objs.status=overtimeData.status;
			Overtime.objs.applicant={
				userId:overtimeData.createBy,
				userName:overtimeData.userName,
				deptId:overtimeData.deptId,
				deptName:overtimeData.deptName,
				siteId:Overtime.objs.siteId
			}
			var formData = {
				"id" : overtimeData.id,
				"userName" : overtimeData.userName + " / " + overtimeData.deptName,
				deptId:Overtime.objs.applicant.deptId,
				createBy:Overtime.objs.applicant.userId,
				num:overtimeData.num,
				"overTimeReason" : overtimeData.overTimeReason,
				"createDay" : overtimeData.createDay
			};
			$("#autoform").iForm("setVal",formData);
			
			Overtime.setOvertimeDatagrid(overtimeData.itemList);
			Overtime.refreshModeShow();
			Overtime.getFile(overtimeData.id);
			Overtime.changeDategridDefaultVal();
		},
		
		//获取附件信息
		getFile:function( id ){
			if(!id)return;
			var url = basePath + "attendance/overtime/queryFileByOvertimeId.do?overtimeId=" + id;
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				success : function(data) {
					if( data.result == "success"){
						Overtime.setFile( data.fileMap );
					}else{
						Overtime.setFile("");//隐藏附件
					}
				}
			});
		},
		//设置页面附件
		setFile:function( fileMaps ){
			Overtime.objs.fileFields[0]["options"]["initFiles"]=fileMaps;
			$("#fileForm").iForm('init',{"fields":Overtime.objs.fileFields,"options":{
			    labelFixWidth : 1,
			    labelColon : false
			}});
			
			if( Overtime.objs.opts.isFileEndEdit ){
				$("#fileForm").iForm('endEdit');
				if(isNull(fileMaps)){
					$("#fileDiv").iFold("hide");
				}
			}
		},
		
		//审批
		audit:function(){
			if(!$("#autoform").valid()||!Overtime.validDatagrid()){
				return;
			}
			var dataCommit=(Overtime.checkStatusToApproveRealOverHours()||Overtime.checkStatusToApproveTransferCompensate())?Overtime.getDataForSubmit(true):{};
			var workFlow = new WorkFlow();
			dataCommit["edit"]="other";
			
			workFlow.showAudit(Overtime.objs.taskId,JSON.stringify( dataCommit ),closeTab,closeTab,null,"",1);
		},
		//终止流程
		stop:function(){
			var workFlow = new WorkFlow();
		    var flowData = workFlow.getFormData();
		    var data={};
		    data['taskId'] = Overtime.objs.taskId;
		    data['message'] = flowData.reason;
		    data['businessId'] = Overtime.objs.overtimeId;
		    var url = basePath + "attendance/overtime/deleteFlowOvertime.do";
		    $.post(url, data, function(data){
		        if(data.result=='success'){
		        	_parent().$("#itcDlg").dialog("close");
		            FW.success("提交成功");
	                homepageService.refresh();
	                closeTab();
		        }
		        else{
		            FW.success("提交失败");
		        }
		    });
		},
		
		deleteOvertimeDatagridRow:function(rowIndex, field, value) {
			if (field == 'id') {
				FW.confirm("删除？<br/>确定删除所选项吗？该操作无法撤销。", function() {
					$("#overtimeDatagrid").datagrid("deleteRow",rowIndex);
					Overtime.changeAddDetailBtn();
				});
			}
		},
		
		checkStatusToApproveRealOverHours:function(){
			return Overtime.objs.opts.isNeedRealOverHours&&
				$.inArray(Overtime.objs.status,Overtime.objs.opts.statusToApproveRealOverHours)>-1;
		},
		checkStatusToApproveTransferCompensate:function(){
			return Overtime.objs.opts.isNeedTransferCompensate&&
				$.inArray(Overtime.objs.status,Overtime.objs.opts.statusToApproveTransferCompensate)>-1;
		},
		
		changeDategridDefaultVal:function(){
			if(Overtime.objs.applyFlag =="approver"&&
					(Overtime.checkStatusToApproveRealOverHours()||
							Overtime.checkStatusToApproveTransferCompensate())){
				var rows=$("#overtimeDatagrid").datagrid('getRows');
				for( var i = 0 ; i < rows.length; i++ ){
					var row=rows[i];
					if(Overtime.checkStatusToApproveRealOverHours()&&!row.realOverHours){
						$("input[name=realOverHours_"+i+"]").val(row.planOverHours);
					}
					if(Overtime.checkStatusToApproveTransferCompensate()&&!row.transferCompensate){
						$("input[name=transferCompensate_"+i+"]").val(row.realOverHours);
					}
				}
			}
		},
		
		changeEditDatagrid:function(isBegin,isOnlyLast,isOnlyUpdate){
			var type=(isBegin?"begin":"end")+"Edit";
			var rowSize = $("#overtimeDatagrid").datagrid('getRows').length;
			if(isOnlyLast){
				$("#overtimeDatagrid").datagrid(type, rowSize-1);
			}else{
				for( var i = 0 ; i < rowSize; i++ ){
			    	$("#overtimeDatagrid").datagrid(type, i);
				}
			}
			if(isBegin&&!isOnlyUpdate){
				Overtime.changeAddDetailBtn(rowSize);
				$("#addDetailBtn_toolbar").show();
				$("#overtimeDatagrid").datagrid("showColumn","id");
			}else{
				$("#addDetailBtn_toolbar").hide();
				$("#overtimeDatagrid").datagrid("hideColumn","id");
			}
		},
		
		changeAddDetailBtn:function(num){
			if(!num){
				num=$("#overtimeDatagrid").datagrid('getRows').length;
			}
			var textVal =  num > 0 ? "继续添加加班" : "添加加班";
			$("#addDetail").text( textVal );
		},
		
		calOvertimeHourTime:function(){
			if(!$("#overtimeDatagridForm").valid()){
				return;
			}
			var me = $(this);
			var tbl = me.parents(".datagrid-row");
			var startTime = tbl.children("td[field='startDate']").find("input").val();
			var endTime = tbl.children("td[field='endDate']").find("input").val();
			
			var s1 = FW.time2long(startTime);
			var s2 = FW.time2long(endTime);
			var total = (s2 - s1)/1000;
			var hour = parseFloat( total / ( 60 * 60 ) ).toFixed(3);
			
			tbl.find("td[field='planOverHours']").find("input").val(hour);
			
			Overtime.queryTotalHours(
				tbl.find("td[field='userId']").find("div").html(),
				tbl.find("td[field='startDate']").find("input").val(),
				tbl.find("td[field='endDate']").find("input").val(),
				function(data){
					if( data.result == "success"){
						tbl.find("td[field='totalHours']").find("div").html(data.totalHours);
					}else{
						tbl.find("td[field='totalHours']").find("div").html("");
					}
				}
			);
		},
		queryTotalHours:function(userId,startTimeStr,endTimeStr,rollbackFunc,params){
			var url = basePath + "attendance/overtime/queryOvertimeHoursByTime.do";
			$.ajax({
				url : url,
				type : 'get',
				data:{siteId:Overtime.objs.siteId,userId:userId,startTimeStr:startTimeStr,endTimeStr:endTimeStr},
				dataType : "json",
				success : function(data) {
					data["params"]=params;
					if(rollbackFunc){
						rollbackFunc(data);
					}
				}
			});
		},
		changeDatagridFields:function(){
			//修改datagrid的column
			var readFields=[];
			if(Overtime.isMode("edit")&&Overtime.objs.applyFlag =="approver"&&(
					Overtime.checkStatusToApproveRealOverHours()||//核定加班时，使核定加班的字段可编辑，其他字段不可编辑
					Overtime.checkStatusToApproveTransferCompensate())){//核定转补休时，使核定转补休的字段可编辑，其他字段不可编辑
				readFields=["startDate","endDate","planOverHours","remarks"];
				if(Overtime.checkStatusToApproveRealOverHours()){
					readFields.push("transferCompensate");
				}else if(Overtime.checkStatusToApproveTransferCompensate()){
					readFields.push("realOverHours");
				}
			}else{
				readFields=["realOverHours","transferCompensate"];
			}
			for(var i=0;i<Overtime.objs.datagridFields.length;i++){
				var item=Overtime.objs.datagridFields[i];
				if($.inArray(item.field,readFields)>=0){
					delete(item.editor);
				}
			}
		},
		
		setOvertimeDatagrid:function( dataArray ){
			var data=null;
			if(dataArray&&dataArray.length>0){
				data={rows:dataArray,total:dataArray.length};
			}
			Overtime.changeDatagridFields();
			$("#overtimeDatagrid").datagrid({
		        pagination:false,
		        singleSelect:true,
		        fitColumns:true,
		        nowrap : false,
		        data : data,
		        columns:[Overtime.objs.datagridFields],
				onLoadError: function(){
					//加载错误的提示 可以根据需要添加
					$("#noteListDiv").hide();
				},
		        onLoadSuccess: function(data){
		        	
		        },
		        onClickCell : function(rowIndex, field, value) {
					if (field == 'id') {
						Overtime.deleteOvertimeDatagridRow(rowIndex, field, value);
					}
				}
		    });
		},
		
		showDtlIframe:function(isToUnify){
			var src = basePath + "attendance/overtime/insertOvertimeItemToPage.do?isToUnify="+(isToUnify?"1":"0");
			
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
					var formdata = conWin.getFormOriginalData("overtimeItemFrom");
					if(isToUnify){//统一加班时间
						//Overtime.changeEditDatagrid(false);
						var rows = $("#overtimeDatagrid").datagrid('getRows');
						var rowMap={};
						for( var i = 0 ; i < rows.length; i++ ){
							var row=rows[i];
							row.startDate=formdata.startDate;
							row.endDate=formdata.endDate;
							row.planOverHours=formdata.planOverHours;
							Overtime.queryTotalHours(
								row.userId,
								FW.long2time(row.startDate),
								FW.long2time(row.endDate),
								function(data){
									var row=data.params.row;
									if( data.result == "success"){
										row.totalHours=data.totalHours;
									}else{
										row.totalHours="";
									}
									$("#overtimeDatagrid").datagrid('updateRow', {
										index: data.params.i,
										row: row
									});
									Overtime.changeEditDatagrid(true);
								},{"i":i,"row":row}
							);
						}
					}else{//添加加班明细
						Overtime.queryTotalHours(
							formdata.userId,
							FW.long2time(formdata.startDate),
							FW.long2time(formdata.endDate),
							function(data){
								var row=data.params.row;
								if( data.result == "success"){
									row.totalHours=data.totalHours;
								}else{
									row.totalHours="";
								}
								$('#overtimeDatagrid').datagrid('appendRow',row);
								Overtime.changeEditDatagrid(true,true);
							},{"row":formdata}
						);
					}
					return true;
				}
			} ];

			FW.dialog("init", {
				"src" : src,
				"dlgOpts" : {
					width : 550,
					height : (isToUnify?200:310),
					closed : false,
					title : (isToUnify?"统一加班时间":"添加加班"),
					modal : true
				},
				"btnOpts" : btnOpts
			});
		}
};

//为空判断函数
function isNull( arg1 ){
	return !arg1 && arg1!==0 && typeof arg1!=="boolean"?true:false;
}
