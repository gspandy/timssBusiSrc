var Abnormity={
		objs:{
			formFields:[],
	  		fileFields:[],
	  		datagridFields:[],
	  		taskId:"",//任务Id
    		processInstId:"",//流程实例ID
    		applyFlag:"",//审批标志
    		abnormityId:"",//考勤异常单id
			status:"",//考勤异常单状态
    		applicant:{},//申请人信息
    		currentUser:{},//当前登录人信息
    		flowDiagram:"atd_?_abnormity",
    		opts:{
    			mode:"view",//页面模式，add/edit/view
    			formOpts:{
					validate:true,
					fixLabelWidth:true
				},
				sessId:"",
				valKey:"",
				isFileEndEdit:false,//文件编辑是否需要endEdit
				isShowAuditBtn:false,//是否在审批框中显示审批按钮
				printSite:["SWF","ZJW"],//有打印的站点
				commitBeianSite:["SWF"],//有提交备案操作的站点
				isChangeSaved:true//修改是否已提交完成
    		}
		},

		changeMode:function(mode){
			if(!mode){
				mode="view";
			}
			if(mode==Abnormity.objs.opts.mode){
				return;
			}
			Abnormity.objs.opts.mode=mode;
		
			//Abnormity.refreshModeShow();
		},
		refreshModeShow:function(){//根据mode展示页面元素
			if(!Abnormity.objs.opts.isChangeSaved){//每次刷新前确定一遍数据修改
				$("#abnormityDatagrid").datagrid('acceptChanges');
				Abnormity.objs.opts.isChangeSaved=true;
			}
			
			if(Abnormity.isMode("view")){
				
			}else if(Abnormity.isMode("add")){
				$("#inner-title").html("新建考勤异常");
				Abnormity.changeEditDatagrid(true);
				$("#invalidButton,#printDiv").hide();
				if(Priv.hasPrivilege("atd_ab_update_delete")){
					if(Abnormity.objs.abnormityId){
						$("#delBtnDiv,#deleteButton").show();
					}else{
						$("#deleteButton").hide();
					}
				}
			}else if(Abnormity.isMode("edit")){
				//控制工具条按钮的隐藏
				//当有审批权限 状态是草稿，说明还在申请阶段
				if( Abnormity.objs.currentUser.userId == Abnormity.objs.applicant.userId && ( Abnormity.objs.status == "草稿" 
						  || Abnormity.objs.status == "提交考勤异常申请" ) ){
					$( "#approveButtonDiv" ).hide();
					$( "#editButtonDiv,#unifyButtonDiv,#delBtnDiv" ).show();
					$("#autoform").iForm("beginEdit",["reason"]);
					$("#autoform").iForm("endEdit",["createDay"]);
					Abnormity.changeEditDatagrid(true);
					
					if(Abnormity.objs.status == "草稿" ){
						$("#autoform").iForm("hide",["createDay"]);
						if(Priv.hasPrivilege("atd_ab_update_delete"))$("#deleteButton").show();
						$("#invalidButton").hide();
						$("#inner-title").html("编辑考勤异常");
					}else if(Abnormity.objs.status =="提交考勤异常申请" ){
						$("#deleteButton").hide();
						if(Priv.hasPrivilege("atd_ab_update_invalid"))$("#invalidButton").show();
						$("#inner-title").html("编辑考勤异常");
					}
				}else{
					if(Abnormity.objs.applyFlag =="approver"){
						Abnormity.objs.opts.isShowAuditBtn = true;
						$( "#approveButtonDiv" ).show();
					}else{
						$( "#approveButtonDiv" ).hide();
					}
					Abnormity.objs.opts.isFileEndEdit = true;
					$( "#editButtonDiv,#unifyButtonDiv,#delBtnDiv" ).hide();
					$("#autoform").iForm("endEdit");
					Abnormity.changeEditDatagrid(false);
					$("#inner-title").html("考勤异常详情");
				}
			}
			FW.fixRoundButtons("#toolbar");
		},
		
		init:function(mode){
			Abnormity.changeMode(mode);
			Abnormity.initFields();
			$("#autoform").iForm("init",{"options":Abnormity.objs.opts.formOpts,"fields":Abnormity.objs.formFields});
			$("#fileDiv").iFold("init");
			$("#detailTitle").iFold("init");
			Abnormity.initBtn();
			Abnormity.initSite();
			Abnormity.loadData();
		},
		initSite:function(){//站点个性化初始化
			//提交备案
			if($.inArray(Abnormity.objs.currentUser.siteId,Abnormity.objs.opts.commitBeianSite)<0){
				$( "#commitButton" ).html("提交");
				$( "#commitButtonBeian" ).hide();
			}
			//打印
			if($.inArray(Abnormity.objs.currentUser.siteId,Abnormity.objs.opts.printSite)<0){
				$("#printDiv").hide();
			}
		},
		initFields:function(){//初始化字段
			Abnormity.objs.formFields=[
				{title : "id", id : "id",type:"hidden"},
				{title : "申请人id", id : "createBy",type:"hidden"},
				{title : "申请人部门id", id : "deptId",type:"hidden"}
	  		];
			if(!Abnormity.isMode("add")){
				Abnormity.objs.formFields.push(
					{title : "申请编号", id : "num",type:"label"}                         
				);
			}
			Abnormity.objs.formFields.push(
				{title : "申请人", id : "userName",type:"label"},
				{title : "异常类型", id : "category",type:"combobox",
					data : FW.parseEnumData("ATD_AB_CATEGORY",_enum,""),
					rules : {required:true},
					allowEmpty:true, 
					formatter:function(value,row,index){
						return FW.getEnumMap("ATD_AB_CATEGORY")[value];
					},
					render : function(id){
						$("#" + id).iCombo("setVal","");
					}
				}
			);
			if(!Abnormity.isMode("add")){
				Abnormity.objs.formFields.push(
					{title : "申请时间", id : "createDay",type : "datetime",
						 dataType : "datetime"}                         
				);
			}
			Abnormity.objs.formFields.push({title : "申请事由",id: "reason",linebreak:true,type:'textarea',
				wrapXsWidth:12,wrapMdWidth:8,height:48,rules : {maxChLength : parseInt(1000*2/3),required:true}
			});
			
			Abnormity.objs.fileFields=[
  	    		{id:"attachment",title:" ",type:"fileupload",wrapXsWidth:12,wrapMdWidth:12,options:{
				    "uploader" : basePath + "upload?method=uploadFile&jsessionid="+Abnormity.objs.opts.sessId,
				    "delFileUrl" : basePath + "upload?method=delFile&key="+Abnormity.objs.opts.valKey,
				    "downloadFileUrl" :  basePath + "upload?method=downloadFile",
				    "swf" : basePath + "itcui/js/uploadify.swf",
				    "fileSizeLimit":10*1024,
				    "delFileAfterPost" : true
				}}
    		];
			
			Abnormity.objs.flowDiagram=(Abnormity.objs.flowDiagram&&Abnormity.objs.currentUser.siteId)
				?(Abnormity.objs.flowDiagram.replace("?", Abnormity.objs.currentUser.siteId.toLowerCase())):"";
				
			Abnormity.objs.datagridFields=[
			    {field:'userId',title:'工号',width:60,fixed:true},                          
				{field:'userName',title:'异常人员',width:60,fixed:true},
				{field:'category',title:'异常类型',width:120,fixed:true,hidden:true,
					editor:{
						type : "combobox",
						options:{
							data : FW.parseEnumData("ATD_AB_CATEGORY",_enum,""),
							//rules : {required:true},
							allowEmpty:true
						}
					},
					formatter:function(value,row,index){
						return FW.getEnumMap("ATD_AB_CATEGORY")[value];
					}
				},
				{field:'startDate',title:'开始时间',width:140,fixed:true,
					editor:{
						type:"datebox",
						"options" : {
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
				},
				{title:"备注",field:"remarks",width:100,
					editor:{
				        type:"text",
				        "options":{
				    	      rules:{
				    	    	  maxChLength:parseInt(450*2/3)
				    	      }
				    	}
					}
				},
				{field:'id',title:' ',width:50,fixed:true,
					 formatter:function(value,row,index){
	  				     return '<img src="'+basePath+'img/attendance/btn_garbage.gif" width="16" height="16" style="cursor:pointer" />';
	  				}	
				}   
			];
		},
		
		commit:function(type){
			var isOnlyBeian="beian"==type?"Y":"N";
			if(!$("#autoform").valid()||!Abnormity.validDatagrid()){
				return;
			}
			$( "#commitButton,#commitButtonBeian" ).attr("disabled","disabled");
			var dataCommit=Abnormity.getDataForSubmit();
			
			if( Abnormity.objs.processInstId != null && Abnormity.objs.processInstId != "" ){//非新建的提交包含了退回的操作
				var workFlow = new WorkFlow();
				workFlow.showAudit(Abnormity.objs.taskId,JSON.stringify($.extend({},{
					isBeian:isOnlyBeian,
					processInstId:Abnormity.objs.processInstId
				},dataCommit)),closeTab,null,null,null,0,function(){
					$( "#commitButton,#commitButtonBeian" ).button("reset");
				});
			}else{//新建和启动流程
				if("Y"==isOnlyBeian){
					$( "#commitButtonBeian" ).button("loading");
				}else{
					$( "#commitButton" ).button("loading");
				}
				$(this).button("loading");
				$("#saveButton").attr("disabled","disabled");
				var url = basePath + "attendance/abnormity/submitAbnormity.do" ;
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					data:dataCommit,
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "提交成功 ！");
							var taskId = data.taskId;
							if(taskId != null){
								var workFlow = new WorkFlow();
								workFlow.submitApply(taskId,JSON.stringify($.extend({},{
									isBeian:isOnlyBeian,
									processInstId:data.processInstId
								},dataCommit)),closeTab,null,0,function(){
									$( "#commitButton,#commitButtonBeian" ).button("reset");
								});
							}
						}else{
							if( data.reason != null ){
								FW.error( data.reason );
							}else {
								FW.error( "提交失败 ！");
							}
							$( "#commitButton,#commitButtonBeian" ).button("reset");
						}
					}
				});
			}
		},
		
		initBtn:function(){
			//添加考勤异常详情
			$( "#addDetail" ).click(function(){
				Abnormity.showDtlIframe();
			});
			//统一考勤异常时间
			$( "#unifyButton" ).click(function(){
				Abnormity.showDtlIframe(true);
			});
			
			//提交
			$( "#commitButton" ).click(function(){
				Abnormity.commit();
			});
			//提交备案
			$( "#commitButtonBeian" ).click(function(){
				Abnormity.commit("beian");
			});
			//打印
			$( "#printBtn" ).click(function(){
				if(Abnormity.isMode("add")){//新建模式没有打印按钮
					return;
				}
				var url = fileExportPath + "preview?__report=report/TIMSS2_"+Abnormity.objs.currentUser.siteId+
											"_KQYC_001_pdf.rptdesign&__format=pdf&id=" + Abnormity.objs.abnormityId;
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
					dlgOpts:{ width:800, height:650, closed:false, title:"打印考勤异常", modal:true }
				 });
			});
			
			//暂存
			$( "#saveButton" ).click(function(){
				if(!$("#autoform").valid()||!Abnormity.validDatagrid()){
					return;
				}
				var data=Abnormity.getDataForSubmit();
				
				var url = basePath + "attendance/abnormity/saveAbnormity.do" ;
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					data:data,
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "保存成功 ！");
							Abnormity.loadData(data);
						}else{
							FW.error( "保存失败 ！");
						}
					}
				});
			});
			
			//显示流程图
			$("#flowDiagramBtn").click(function(){
				if( isNull( Abnormity.objs.processInstId ) ){
					if(Abnormity.objs.flowDiagram){
						showFlowDialog(Abnormity.objs.flowDiagram);
					}else{
						FW.error("没有流程图信息");
					}
				}else{
					var workFlow = new WorkFlow();
					workFlow.showAuditInfo(Abnormity.objs.processInstId,"",Abnormity.objs.opts.isShowAuditBtn,Abnormity.audit);
				}
					
			});
			
			//审批
			$("#approveBtn").click(function(){
				Abnormity.audit();
			});
			
			//删除
			$( "#deleteButton" ).click(function(){
				var formData = $( "#autoform" ).iForm("getVal");
				
				FW.confirm("确定删除本条数据吗？该操作无法恢复。", function() {
					var url = basePath + "attendance/abnormity/deleteAbnormity.do?id=" +  formData.id;
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
					var url = basePath + "attendance/abnormity/invalidAbnormity.do?id=" +  formData.id;
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
			return Abnormity.objs.opts[modeName]==mode;
		},
		
		validDatagrid:function(){
			var rowDatas = $("#abnormityDatagrid").datagrid('getRows');
			if( rowDatas.length <= 0 ){
				FW.error( "请添加考勤异常 ！");
				return false;
			}else{
				return $("#abnormityDatagridForm").valid();
			}
		},
		
		getDataForSubmit:function(isOnlyUpdate){
			Abnormity.objs.opts.isChangeSaved=false;
			Abnormity.changeEditDatagrid(false);
			var addRows=$("#abnormityDatagrid").datagrid('getChanges','inserted');
			var delRows=$("#abnormityDatagrid").datagrid('getChanges','deleted');
			var updateRows=$("#abnormityDatagrid").datagrid('getChanges','updated');
			Abnormity.changeEditDatagrid(true,false,isOnlyUpdate);
			
			var formData = getFormData( "autoform" );
			//文件ids
			var fileIds = $("#fileForm").iForm("getVal").attachment;
			
			//赋值异常类型
			var cate=$( "#autoform" ).iForm("getVal").category;
			for(var i=0;i<addRows.length;i++){
				addRows[i].category=cate;
			}
			for(var i=0;i<delRows.length;i++){
				delRows[i].category=cate;
			}
			for(var i=0;i<updateRows.length;i++){
				updateRows[i].category=cate;
			}
			
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
				Abnormity.mountData(data);
			}else{
				if(Abnormity.isMode("add")){//如果是新建，加载新建的数据
					Abnormity.objs.applicant=Abnormity.objs.currentUser;
					$("#autoform").iForm("setVal",{
						userName:Abnormity.objs.applicant.userName + " / " + Abnormity.objs.applicant.deptName,
						deptId:Abnormity.objs.applicant.deptId,
						createBy:Abnormity.objs.applicant.userId
					});
					Abnormity.setAbnormityDatagrid(null);
					Abnormity.refreshModeShow();
					Abnormity.setFile(null);
				}else{//否则获取加载考勤异常单的数据
					Abnormity.getData(Abnormity.objs.abnormityId);
				}
			}
		},
		
		//获取考勤异常单信息
		getData:function( id ){
			var url = basePath + "attendance/abnormity/queryAbnormityById.do?id=" + id;
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				success : function(data) {
					if( data.result == "success"){
						Abnormity.mountData(data);
					}
				}
			});
		},
		//装载数据
		mountData:function(data){
			//任务ID
			Abnormity.objs.taskId = data.taskId;
			//审批状态
			Abnormity.objs.applyFlag = data.applyFlag;
			
			var abnormityData=data.rowData;
			Abnormity.objs.processInstId = abnormityData.instantId;
			Abnormity.objs.abnormityId = abnormityData.id;
			Abnormity.objs.status=abnormityData.status;
			Abnormity.objs.applicant={
				userId:abnormityData.createBy,
				userName:abnormityData.userName,
				deptId:abnormityData.deptId,
				deptName:abnormityData.deptName
			}
			var formData = {
				"id" : abnormityData.id,
				"userName" : abnormityData.userName + " / " + abnormityData.deptName,
				deptId:Abnormity.objs.applicant.deptId,
				createBy:Abnormity.objs.applicant.userId,
				num:abnormityData.num,
				"reason" : abnormityData.reason,
				"createDay" : abnormityData.createDay,
				category:abnormityData.category?abnormityData.category:abnormityData.itemList[0].category
			};
			$("#autoform").iForm("setVal",formData);
			
			Abnormity.setAbnormityDatagrid(abnormityData.itemList);
			Abnormity.refreshModeShow();
			Abnormity.getFile(abnormityData.id);
		},
		
		//获取附件信息
		getFile:function( id ){
			if(!id)return;
			var url = basePath + "attendance/abnormity/queryFileByAbnormityId.do?abnormityId=" + id;
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				success : function(data) {
					if( data.result == "success"){
						Abnormity.setFile( data.fileMap );
					}else{
						Abnormity.setFile("");//隐藏附件
					}
				}
			});
		},
		//设置页面附件
		setFile:function( fileMaps ){
			Abnormity.objs.fileFields[0]["options"]["initFiles"]=fileMaps;
			$("#fileForm").iForm('init',{"fields":Abnormity.objs.fileFields,"options":{
			    labelFixWidth : 1,
			    labelColon : false
			}});
			
			if( Abnormity.objs.opts.isFileEndEdit ){
				$("#fileForm").iForm('endEdit');
				if(isNull(fileMaps)){
					$("#fileDiv").iFold("hide");
				}
			}
		},
		
		//审批
		audit:function(){
			if(!$("#autoform").valid()||!Abnormity.validDatagrid()){
				return;
			}
			var dataCommit=Abnormity.objs.status == "部门经理核定考勤异常时长"?Abnormity.getDataForSubmit(true):{};
			var workFlow = new WorkFlow();
			dataCommit["edit"]="other";
			
			workFlow.showAudit(Abnormity.objs.taskId,JSON.stringify( dataCommit ),closeTab,closeTab,null,"",1);
		},
		//终止流程
		stop:function(){
			var workFlow = new WorkFlow();
		    var flowData = workFlow.getFormData();
		    var data={};
		    data['taskId'] = Abnormity.objs.taskId;
		    data['message'] = flowData.reason;
		    data['businessId'] = Abnormity.objs.abnormityId;
		    var url = basePath + "attendance/abnormity/deleteFlowAbnormity.do";
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
		
		deleteAbnormityDatagridRow:function(rowIndex, field, value) {
			if (field == 'id') {
				FW.confirm("删除？<br/>确定删除所选项吗？该操作无法撤销。", function() {
					$("#abnormityDatagrid").datagrid("deleteRow",rowIndex);
					Abnormity.changeAddDetailBtn();
				});
			}
		},
		
		changeEditDatagrid:function(isBegin,isOnlyLast,isOnlyUpdate){
			var type=(isBegin?"begin":"end")+"Edit";
			var rowSize = $("#abnormityDatagrid").datagrid('getRows').length;
			if(isOnlyLast){
				$("#abnormityDatagrid").datagrid(type, rowSize-1);
			}else{
				for( var i = 0 ; i < rowSize; i++ ){
			    	$("#abnormityDatagrid").datagrid(type, i);
				}
			}
			if(isBegin&&!isOnlyUpdate){
				Abnormity.changeAddDetailBtn(rowSize);
				$("#addDetailBtn_toolbar").show();
				$("#abnormityDatagrid").datagrid("showColumn","id");
			}else{
				$("#addDetailBtn_toolbar").hide();
				$("#abnormityDatagrid").datagrid("hideColumn","id");
			}
		},
		
		changeAddDetailBtn:function(num){
			if(!num){
				num=$("#abnormityDatagrid").datagrid('getRows').length;
			}
			var textVal =  num > 0 ? "继续添加异常" : "添加异常";
			$("#addDetail").text( textVal );
		},
		
		setAbnormityDatagrid:function( dataArray ){
			var data=null;
			if(dataArray&&dataArray.length>0){
				data={rows:dataArray,total:dataArray.length};
			}
			$("#abnormityDatagrid").datagrid({
		        pagination:false,
		        singleSelect:true,
		        fitColumns:true,
		        nowrap : false,
		        data : data,
		        columns:[Abnormity.objs.datagridFields],
				onLoadError: function(){
					//加载错误的提示 可以根据需要添加
					$("#noteListDiv").hide();
				},
		        onLoadSuccess: function(data){
		        	
		        },
		        onClickCell : function(rowIndex, field, value) {
					if (field == 'id') {
						Abnormity.deleteAbnormityDatagridRow(rowIndex, field, value);
					}
				}

		    });
		},
		
		showDtlIframe:function(isToUnify){
			var src = basePath + "attendance/abnormity/item.do?mode=add&isToUnify="+(isToUnify?"1":"0");
			
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
					var formdata = conWin.$("#abnormityItemFrom").iForm("getVal");
					if(isToUnify){//统一考勤异常时间
						Abnormity.changeEditDatagrid(false);
						var rows = $("#abnormityDatagrid").datagrid('getRows');
						for( var i = 0 ; i < rows.length; i++ ){
							var row=rows[i];
							row.startDate=formdata.startDate;
							row.endDate=formdata.endDate;
							row.category=formdata.category;
					    	$("#abnormityDatagrid").datagrid('updateRow', {
								index: i,
								row: row
							});
						}
						Abnormity.changeEditDatagrid(true);
					}else{//添加考勤异常明细
						$('#abnormityDatagrid').datagrid('appendRow',formdata);
						Abnormity.changeEditDatagrid(true,true);
					}
					return true;
				}
			} ];

			FW.dialog("init", {
				"src" : src,
				"dlgOpts" : {
					width : 550,
					height : (isToUnify?170:280),
					closed : false,
					title : (isToUnify?"统一考勤异常":"添加考勤异常"),
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
