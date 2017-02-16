var PageDetail={//basePath框架中已有
		objs:{
			isBackListAble:false,//是否支持本页面返回列表，需要配置url.list，否则直接关闭本页面tab
			namePrefix:"列表项",
			url:{
				list:"",//返回列表的url
				query:"",//获取表单数据的url
				create:basePath+"insertBean.do",//required
				update:basePath+"updateBean.do",//required
				del:basePath+"delBean.do",//required
				invalid:basePath+"invalidBean.do",
				commit:basePath+"commit.do",
				save:basePath+"save.do"
			},
			mode:"view",//默认，可选view/create/edit
			form:{
				id:"form_baseinfo",//required
				areaId:"base_info",//required
				obj:null,//$("#"+PageDetail.objs.form.id)
				fields:[],//required
				idField:"",//required
				nameField:"",//required
				opts:{
					fixLabelWidth:true,
					validate:true,
					labelFixWidth:120
				},
				queryData:null,//执行query返回的数据，可用于afterLoadData
				beanId:"",//用url获取bean时使用
				bean:{},//同步初始化使用，如果没有这个，就要用异步初始化，需要赋值url.query、form.beanId、form.blankBean
				blankBean:{}//用于新建时填充表单（无初始化数据时）
			},
			withWorkFlow:false,//是否有工作流
			workFlow:{
				obj:null,//工作流对象
				status:"",//工作流状态,required
				editStatus:["草稿","提交申请"],//进入编辑模式的工作流状态
				isApplicant:false,//是否申请人,required
				isAudit:false,//是否审批人,required
				isCommited:false,//是否提交过了（是否启动了流程）
				taskId:"",//任务Id
				instanceId:"",//流程实例ID
	    		flowDiagram:"atd_?_training"//流程图
			},
			withAttach:false,//是否有附件
			attach:{//附件参数
				divId:"attachDiv",
				sessId:"",
				valKey:"",
				fields:[{
					id:"attachment",title:" ",type:"fileupload",wrapXsWidth:12,wrapMdWidth:12,options:{
					    "uploader" : basePath + "upload?method=uploadFile&jsessionid=",
					    "delFileUrl" : basePath + "upload?method=delFile&key=",
					    "downloadFileUrl" :  basePath + "upload?method=downloadFile",
					    "swf" : basePath + "itcui/js/uploadify.swf",
					    "fileSizeLimit":10*1024,
					    "delFileAfterPost" : true
					}
				}],
				options:{
					labelFixWidth : 1,
				    labelColon : false
				},
				isEndEdit:false,
				formId:"attachForm",
				itemType:"training",//插入附件使用
				url:basePath+"attendance/atdAttach/query.do"
			},
			withItem:false,//是否有详情项列表
			item:{//详情项参数
				divId:"itemDiv",
				formId:"itemForm",
				datagridId:"itemDatagrid",
				addItemBtnId:"addItemBtn",
				addItemBtnName:"添加人员",
				addItemToolbarId:"addItemToolbar",
				isSaved:true,
				isEndEdit:false,
				itemName:"培训人员",
				fields:[],//required
				idField:"",//required
				delField:"",//required
				blankItem:{}//required
			},
			print:{
				url:"",//非空会覆盖id配置的url
				id:"PXSQ_001",
				format:"pdf"
			}
		},
		
		isMode:function(mode){
			return PageDetail.objs.mode==mode;
		},
		setMode:function(mode){
			PageDetail.objs.mode=mode;
		},
		init:function(initParams){
			if(initParams){
				$.extend(true,PageDetail.objs,initParams);
			}else{
				FW.error("初始化信息缺失，请联系管理员！");
				return;
			}

			var modeObjs={
				pageName:PageDetail.objs.namePrefix,
				formId:PageDetail.objs.form.id,
				withWorkFlow:PageDetail.objs.withWorkFlow
			};
			if(PageDetail.objs.withWorkFlow){
				modeObjs["buttonDivId"]={
					save:"btnSave",//暂存
					del:"btnDel",//删除
					cancel:"btnBack",//取消
					commit:"btnCommit",//提交
					audit:"btnAudit",//审批
					invalid:"btnInvalid"//作废
				};
				PageDetail.objs.workFlow.obj=new WorkFlow();
			}else{
				modeObjs["buttonDivId"]={
					create:"btnCreate",
					toEdit:"btnEdit",
					update:"btnSave",
					toDel:"btnDel",
					cancel:"btnBack"
				};
			}
			modeObjs["buttonDivId"]["print"]="btnPrint";
			PageMode.init(modeObjs);
			
			$("#"+PageDetail.objs.form.areaId).ITCUI_Foldable();
			PageDetail.objs.form.obj=$("#"+PageDetail.objs.form.id);
			PageDetail.objs.form.obj.ITC_Form(PageDetail.objs.form.opts,PageDetail.objs.form.fields); //主卡片信息	
			if(PageDetail.objs.withItem){
				$("#"+PageDetail.objs.item.divId).iFold("init");
			}
			if(PageDetail.objs.withAttach){
				$("#"+PageDetail.objs.attach.divId).iFold("init");
			}
			
			if($.isEmptyObject(PageDetail.objs.form.bean)){
				if(PageDetail.isMode("create")){
					PageDetail.objs.form.bean=PageDetail.objs.form.blankBean;
					PageDetail.loadData();
				}else{
					PageDetail.queryBean();
				}
			}else{
				PageDetail.loadData();
			}
		},
		
		toEdit:function(){
			PageDetail.setMode("edit");
			PageDetail.beforeChangeShow();
			PageDetail.changeShow();
			PageDetail.afterChangeShow();
		},
		toDelete:function(){
			var title=FW.specialchars(substr(PageDetail.objs.form.obj.iForm("getVal",PageDetail.objs.form.nameField),30));
			Notice.confirm("确认删除|是否确定要删除"+PageDetail.objs.namePrefix+(title?("“"+title+
					"”"):"")+"？该操作无法撤销。",PageDetail.delBean);
		},
		toBack:function(){//返回的操作
			if(PageDetail.objs.mode=="view"){
				PageDetail.toList();
			}else if(PageDetail.objs.mode=="create"){
				PageDetail.toList();
			}else if(PageDetail.objs.mode=="edit"){
				PageDetail.objs["mode"]="view";
				PageDetail.loadData();
			}
		},
		toList:function(){
			if(PageDetail.objs.toBackListAble){
				FW.navigate(PageDetail.objs.url.list);
			}else{
				PageDetail.toClose();
			}
		},
		toClose:function(){
			closeTab();
		},
		toShowFlow:function(){
			if(PageDetail.isMode("create")||!PageDetail.objs.workFlow.isCommited){
				PageDetail.objs.workFlow.flowDiagram=(PageDetail.objs.workFlow.flowDiagram&&PageDetail.objs.form.bean.siteid)
					?(PageDetail.objs.workFlow.flowDiagram.replace("?", PageDetail.objs.form.bean.siteid.toLowerCase())):"";
				if(PageDetail.objs.workFlow.flowDiagram){
					PageDetail.objs.workFlow.obj.showDiagram(PageDetail.objs.workFlow.flowDiagram);
				}else{
					FW.error("没有流程图信息");
				}
			}else{
				PageDetail.objs.workFlow.obj.showAuditInfo(PageDetail.objs.workFlow.instanceId,"",PageDetail.objs.workFlow.isAudit,PageDetail.toAudit);
			}
		},
		toPrint:function(){
			if(PageDetail.isMode("create")){//新建模式没有打印按钮
				return;
			}
			var url = PageDetail.objs.print.url?PageDetail.objs.print.url:
								(fileExportPath + "preview?__report=report/TIMSS2_"+PageDetail.objs.form.bean.siteid+
										"_"+PageDetail.objs.print.id+".rptdesign&__format="+PageDetail.objs.print.format+
										"&"+PageDetail.objs.form.idField+"=" + PageDetail.objs.form.beanId+
										"&siteId="+PageDetail.objs.form.bean.siteid);
			//window.open(url);
			FW.dialog("init",{src: url,
				btnOpts:[{
					"name" : "关闭",
					"float" : "right",
					"style" : "btn-default",
					"onclick" : function(){
					 _parent().$("#itcDlg").dialog("close");
				}}],
				dlgOpts:{ width:800, height:650, closed:false, title:"打印"+PageDetail.objs.namePrefix, modal:true }
			});
		},
		toInvalid:function(){
			FW.confirm("确定作废本条数据吗？该操作无法恢复。", function() {
				PageDetail.invalidBean();
			});
		},
		toAudit:function(){
			PageDetail.audit();
		},
		toCommit:function(){
			if(PageDetail.objs.workFlow.instanceId){//非新建的提交则审批
				PageDetail.audit();
			}else{//新建和启动流程
				PageDetail.commit();
			}
		},
		toSave:function(){
			PageDetail.save();
		},
		
		beforeChangeShow:function(){
			
		},
		changeShow:function(){//切换模式
			if(PageDetail.objs.withAttach){
				PageDetail.objs.attach.isEndEdit=false;//默认附件可编辑
			}
			if(PageDetail.objs.withItem){
				PageDetail.objs.item.isEndEdit=false;//默认详情项可编辑
			}
			if(PageDetail.objs.withItem&&!PageDetail.objs.item.isSaved){//每次刷新前确定一遍数据修改
				PageDetail.toAcceptItem();
			}
			
			if(PageDetail.objs.mode=="view"){
				if(PageDetail.objs.withWorkFlow&&PageDetail.objs.workFlow.isApplicant
						&&$.inArray(PageDetail.objs.workFlow.status,PageDetail.objs.workFlow.editStatus)>-1){
					//工作流下申请人在可编辑状态
					PageMode.objs.isCommited=PageDetail.objs.workFlow.isCommited;
					PageMode.changeMode("edit");
				}else{
					if(PageDetail.objs.withWorkFlow){
						PageMode.objs.isAudit=PageDetail.objs.workFlow.isAudit;
					}
					PageMode.changeMode("view");
					if(PageDetail.objs.withItem){
						PageDetail.objs.item.isEndEdit=true;
					}
					if(PageDetail.objs.withAttach){
						PageDetail.objs.attach.isEndEdit=true;
					}
				}
			}else if(PageDetail.objs.mode=="create"){
				PageMode.changeMode("add");
				if(!PageDetail.objs.isBackListAble){
					$("#"+PageMode.objs.buttonDivId["cancel"]).hide();//不支持返回列表时，隐藏返回按钮
				}
			}else if(PageDetail.objs.mode=="edit"){
				PageMode.changeMode("edit");
			}
			
			if(PageDetail.objs.withItem){
				PageDetail.toEditItem(!PageDetail.objs.item.isEndEdit);
			}
			
			FW.fixRoundButtons(".btn-toolbar");
		},
		afterChangeShow:function(){
			
		},
		
		afterLoadData:function(){
			if(PageDetail.objs.withWorkFlow){
				//设置工作流的参数
				
			}
		},
		loadData:function(){//加载数据
			PageDetail.objs.form.obj.ITC_Form("loaddata",PageDetail.objs.form.bean);
			if(PageDetail.objs.form.bean[PageDetail.objs.form.idField]&&!PageDetail.objs.form.beanId){
				PageDetail.objs.form.beanId=PageDetail.objs.form.bean[PageDetail.objs.form.idField];
			}
			if(PageDetail.objs.withItem){
				PageDetail.setItem(PageDetail.objs.form.bean.itemList);
			}
			PageDetail.afterLoadData();
			PageDetail.beforeChangeShow();
			PageDetail.changeShow();
			PageDetail.afterChangeShow();
			if(PageDetail.objs.withAttach){
				if(PageDetail.isMode("create")){
					PageDetail.setAttach(null);
				}else{
					PageDetail.queryAttach();
				}
			}
		},

		getDataForQuery:function(){
			var params={};
			params[PageDetail.objs.form.idField]=PageDetail.objs.form.beanId;
			return params;
		},
		queryCallback:function(data){
			PageDetail.objs.form.queryData=data;
			PageDetail.objs.form.bean=data.bean;
			PageDetail.loadData();
		},
		queryBean:function(){//异步获取数据加载
			$.get(
				PageDetail.objs.url.query,
				PageDetail.getDataForQuery(),
				PageDetail.queryCallback,
				"json"
			);
		},
		
		toValid:function(){
			return PageDetail.objs.form.obj.valid()&&(PageDetail.objs.withItem?PageDetail.toValidItem():true);
		},
		toValidItem:function(){
			var rowDatas = $("#"+PageDetail.objs.item.datagridId).datagrid('getRows');
			if( rowDatas.length <= 0 ){
				FW.error( "请添加"+PageDetail.objs.item.itemName);
				return false;
			}else{
				return $("#"+PageDetail.objs.item.formId).valid();
			}
		},
		
		getDataForSubmit:function(isEndEditItem,isOnlyUpdateItem){
			var result={};
			if(PageDetail.objs.withWorkFlow){
				$.extend(result,{formData:JSON.stringify(PageDetail.objs.form.obj.iForm("getVal"))});
			}else{
				$.extend(result,{jsonData:JSON.stringify(PageDetail.objs.form.obj.iForm("getVal"))});
			}
			if(PageDetail.objs.withItem){
				$.extend(result,PageDetail.getDataForSubmitItem(isEndEditItem,isOnlyUpdateItem));
			}
			if(PageDetail.objs.withAttach){
				$.extend(result,PageDetail.getDataForSubmitAttach());
			}
			return result;
		},
		
		getDataForCreate:function(){
			return PageDetail.getDataForSubmit();
		},
		createCallback:function(data){
			if(data.result=="success"){
				FW.success("新建"+PageDetail.objs.namePrefix+"成功");
				PageDetail.objs.form.bean=data.bean;
				PageDetail.objs["mode"]="view";
				PageDetail.loadData();
			}else{
				FW.error("新建"+PageDetail.objs.namePrefix+"失败，请稍后重试或联系管理员");
			}
		},
		createBean:function(){
			if(!PageDetail.toValid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			$.post(
				PageDetail.objs.url.create,
				PageDetail.getDataForCreate(),
				PageDetail.createCallback,
				"json"
			);
		},
		
		getDataForUpdate:function(){
			return PageDetail.getDataForSubmit();
		},
		updateCallback:function(data){
			if(data.result=="success"){
				FW.success("更新"+PageDetail.objs.namePrefix+"成功");
				PageDetail.objs.form.bean=data.bean;
				PageDetail.toBack();
			}else{
				FW.error("更新"+PageDetail.objs.namePrefix+"失败，请稍后重试或联系管理员");
			}
		},
		updateBean:function(){
			if(!PageDetail.toValid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			$.post(
				PageDetail.objs.url.update,
				PageDetail.getDataForUpdate(),
				PageDetail.updateCallback,
				"json"
			);
		},
		
		getDataForDel:function(){
			return PageDetail.getDataForSubmit();
		},
		delCallback:function(data){
			if(data.result=="success"){
				FW.success("删除"+PageDetail.objs.namePrefix+"成功");
				PageDetail.toList();
			}else{
				FW.error("删除"+PageDetail.objs.namePrefix+"失败，请稍后重试或联系管理员");
			}
		},
		delBean:function(){
			$.post(
				PageDetail.objs.url.del,
				PageDetail.getDataForDel(),
				PageDetail.delCallback,
				"json"
			);
		},
		
		getDataForInvalid:function(){
			return PageDetail.getDataForSubmit();
		},
		invalidCallback:function(data){
			if(data.result=="success"){
				FW.success("作废"+PageDetail.objs.namePrefix+"成功");
				PageDetail.toClose();
			}else{
				FW.error("作废"+PageDetail.objs.namePrefix+"失败，请稍后重试或联系管理员");
			}
		},
		invalidBean:function(){
			if(!PageDetail.toValid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			$.post(
				PageDetail.objs.url.invalid,
				PageDetail.getDataForInvalid(),
				PageDetail.invalidCallback,
				"json"
			);
		},
		
		getDataForAudit:function(){
			return JSON.stringify(PageDetail.getDataForSubmit(PageDetail.objs.item.isEndEdit));
		},
		audit:function(){
			if(!PageDetail.toValid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			PageDetail.objs.workFlow.obj.showAudit(PageDetail.objs.workFlow.taskId,PageDetail.getDataForAudit(),closeTab,closeTab,closeTab,"",0,PageDetail.closeAuditCallback);
		},
		closeAuditCallback:function(){
			if(PageDetail.objs.withItem&&!PageDetail.objs.item.isSaved){//每次刷新前确定一遍数据修改
				PageDetail.toAcceptItem();
				PageDetail.toEditItem(!PageDetail.objs.item.isEndEdit);
			}
		},

		getDataForSubmitApply:function(data){
			return JSON.stringify({
				notSave:"Y",
				instanceId:data.bean.instanceId
			});
		},
		getDataForCommit:function(){
			return PageDetail.getDataForSubmit();
		},
		commitCallback:function(data){
			if(data.result=="success"){
				FW.success("提交"+PageDetail.objs.namePrefix+"成功");
				var taskId = data.bean.taskId;
				if(taskId){
					PageDetail.objs.workFlow.obj.submitApply(taskId,PageDetail.getDataForSubmitApply(data),closeTab,closeTab,0,PageDetail.closeCommitCallback);
				}
			}else{
				FW.error("提交"+PageDetail.objs.namePrefix+"失败，请稍后重试或联系管理员");
			}
		},
		commit:function(){
			if(!PageDetail.toValid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			$.post(
				PageDetail.objs.url.commit,
				PageDetail.getDataForCommit(),
				PageDetail.commitCallback,
				"json"
			);
		},
		closeCommitCallback:function(){
			
		},
		
		getDataForSave:function(){
			return PageDetail.getDataForSubmit();
		},
		saveCallback:function(data){
			if(data.result=="success"){
				FW.success("暂存"+PageDetail.objs.namePrefix+"成功");
				PageDetail.objs.form.bean=data.bean;
				PageDetail.objs["mode"]="edit";
				PageDetail.loadData();
			}else{
				FW.error("暂存"+PageDetail.objs.namePrefix+"失败，请稍后重试或联系管理员");
			}
		},
		save:function(){
			if(!PageDetail.toValid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			$.post(
				PageDetail.objs.url.save,
				PageDetail.getDataForSave(),
				PageDetail.saveCallback,
				"json"
			);
		},
		
		refreshAttachFields:function(){
			PageDetail.objs.attach.fields[0].options.uploader=basePath+"upload?method=uploadFile&jsessionid="+PageDetail.objs.attach.sessId;
			PageDetail.objs.attach.fields[0].options.delFileUrl=basePath + "upload?method=delFile&key="+PageDetail.objs.attach.valKey;
		},
		setAttach:function( fileMaps ){
			PageDetail.objs.attach.fields[0]["options"]["initFiles"]=fileMaps;
			PageDetail.refreshAttachFields();
			var formObj=$("#"+PageDetail.objs.attach.formId);
			formObj.iForm('init',{"fields":PageDetail.objs.attach.fields,"options":PageDetail.objs.attach.options});
			
			if(PageDetail.objs.attach.isEndEdit){
				formObj.iForm('endEdit');
				if(!(fileMaps)){
					$("#"+PageDetail.objs.attach.divId).iFold("hide");
				}
			}
		},
		getDataForQueryAttach:function(){
			return {
				itemType:PageDetail.objs.attach.itemType,
				itemId:PageDetail.objs.form.beanId
			};
		},
		queryAttachCallback:function(data){
			if( data.result == "success"){
				PageDetail.setAttach(data.fileMap);
			}else{
				PageDetail.setAttach("");//隐藏附件
			}
		},
		queryAttach:function(){
			$.post(
				PageDetail.objs.attach.url,
				PageDetail.getDataForQueryAttach(),
				PageDetail.queryAttachCallback,
				"json"
			);
		},
		getDataForSubmitAttach:function(){
			return {
				fileIds:$("#"+PageDetail.objs.attach.formId).iForm("getVal").attachment
			};
		},
		
		getDataForSubmitItem:function(isEndEdit,isOnlyUpdate){
			PageDetail.objs.item.isSaved=false;
			PageDetail.toEditItem(false);
			var obj=$("#"+PageDetail.objs.item.datagridId);
			var addRows=obj.datagrid('getChanges','inserted');
			var delRows=obj.datagrid('getChanges','deleted');
			var updateRows=obj.datagrid('getChanges','updated');
			PageDetail.toEditItem(!isEndEdit,false,isOnlyUpdate);
			
			return {
				addRows:JSON.stringify(addRows),
				delRows:JSON.stringify(delRows),
				updateRows:JSON.stringify(updateRows)
			};
		},
		toEditItem:function(isToEdit,isOnlyLast,isOnlyUpdate){
			var type=(isToEdit?"begin":"end")+"Edit";
			var obj=$("#"+PageDetail.objs.item.datagridId);
			var rowSize=obj.datagrid('getRows').length;
			if(isOnlyLast){
				obj.datagrid(type, rowSize-1);
			}else{
				for( var i = 0 ; i < rowSize; i++ ){
					obj.datagrid(type, i);
				}
			}
			if(isToEdit&&!isOnlyUpdate){
				PageDetail.toChangeAddItemBtn(rowSize);
				$("#"+PageDetail.objs.item.addItemToolbarId).show();
				obj.datagrid("showColumn",PageDetail.objs.item.delField);
			}else{
				$("#"+PageDetail.objs.item.addItemToolbarId).hide();
				obj.datagrid("hideColumn",PageDetail.objs.item.delField);
			}
		},
		toAddItem:function(){
			$("#"+PageDetail.objs.item.datagridId).datagrid('appendRow',$.extend({},PageDetail.objs.item.blankItem));
			PageDetail.toEditItem(true,true);
		},
		toDelItem:function(rowIndex,field,value) {
			FW.confirm("删除？<br/>确定删除该"+PageDetail.objs.item.itemName+"吗？该操作无法撤销。", function() {
				$("#"+PageDetail.objs.item.datagridId).datagrid("deleteRow",rowIndex);
				PageDetail.toChangeAddItemBtn();
			});
		},
		toAcceptItem:function(){
			$("#"+PageDetail.objs.item.datagridId).datagrid('acceptChanges');
			PageDetail.objs.item.isSaved=true;
		},
		toChangeAddItemBtn:function(num){
			if(!num){
				num=$("#"+PageDetail.objs.item.datagridId).datagrid('getRows').length;
			}
			var textVal=(num>0?"继续":"")+PageDetail.objs.item.addItemBtnName;
			$("#"+PageDetail.objs.item.addItemBtnId).text(textVal);
		},
		setItem:function(dataArray){
			var data={rows:[],total:0};
			if(dataArray&&dataArray.length>0){
				data={rows:dataArray,total:dataArray.length};
			}
			$("#"+PageDetail.objs.item.datagridId).datagrid({
		        pagination:false,
		        singleSelect:true,
		        fitColumns:true,
		        nowrap:false,
		        data:data,
		        columns:[PageDetail.objs.item.fields],
				onLoadError:PageDetail.errorSetItemCallback,
		        onLoadSuccess:PageDetail.successSetItemCallback,
		        onClickCell : function(rowIndex,field,value) {
		        	if(field==PageDetail.objs.item.delField){
						PageDetail.toDelItem(rowIndex,field,value);
					}
				}
		    });
		},
		successSetItemCallback:function(data){
			
		},
		errorSetItemCallback:function(data){
			
		}
};