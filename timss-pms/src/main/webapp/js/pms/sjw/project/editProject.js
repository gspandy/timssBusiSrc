/**
 * 提交修改年度计划信息到后台,暂存
 */
function submit(_this){
	if(!valid()){
		return ;
	}
	buttonLoading(_this);
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var milestonesData=null;
	var milestones=null;
	var workloads=null;
	var outsourcings=null;
	if(pmsPager.hasPri(pmsPager.opt,"pms-milestone-edit")){
		milestonesData=getMilestoneData();
		milestones=FW.stringify(milestonesData);
	}
	if(pmsPager.hasPri(pmsPager.opt,"pms-workload-edit")){
		workloads=getDatagridData(initWorkloadTable.dataGrid);
	}
	if(pmsPager.hasPri(pmsPager.opt,"pms-outsourcing-edit")){
		outsourcings=getDatagridData(initOutsourcingTable.dataGrid);
	}
	$.post(basePath+'pms/project/tmpUpdateProject.do',{"project":FW.stringify(data),milestones:milestones,workloads:workloads,outsourcings:outsourcings},function(result){
		data.milestones=milestonesData;
		data.outsourcings=outsourcings;
		data.workloads=workloads;
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,
				{successFunction:openNewUserDialogFirstInTab,tabOpen:true,data:data,resetId:_this});
	});
}

function shenpi(){
	if(!validShenPi()){
		return ;
	}
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var taskId=getWorkflowTaskId();
	var processInstId=getWorkflowProcessInstId();
	data.outsourcings=null;
	data.workloads=null;
	data.milestones=null;
	if(pmsPager.hasPri(pmsPager.opt,"pms-outsourcing-edit")){
		data.outsourcings=getDatagridData(initOutsourcingTable.dataGrid);
	}
	if(pmsPager.hasPri(pmsPager.opt,"pms-workload-edit")){
		data.workloads=getDatagridData(initWorkloadTable.dataGrid);
	}
	if(pmsPager.hasPri(pmsPager.opt,"pms-milestone-edit")){
		data.milestones=getMilestoneData();
	}
	$.post(basePath+'pms/project/setWFVariable.do',{taskId:taskId,processInstId:processInstId,project:FW.stringify(data)},
		function(result){
		if(result && result.flag=='success'){
			openNewUserDialog(data,stopWorkflow);
		}else{
			FW.error(result.msg || "出错了，请重试");
		}
		
	});
}

function validShenPi(){
	if(!$("#form1").valid()){
		return false;
	}
	if(!validDatagrid("#workloadList")){
		return false;
	}
	if(!validDatagrid("#outsourcingList")){
		return false;
	}
	if(!validDatagrid("#milestoneList")){
		return false;
	}
	
	return true;
}
/**
 * 流程终止函数
 */
function stopWorkflow(reason){
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var processInstId=getWorkflowProcessInstId();
	$.post(basePath+"pms/project/stopWorkflow.do",{project:FW.stringify(data),processInstId:processInstId,reason:reason},function(result){
		showBasicMessageFromServer(result,"流程终止成功","流程终止失败",{successFunction:closeWorkflowWindow});
	});
}
// 删除立项
function del(){
	var data={id:id};
	FW.confirm("确定删除吗？删除后数据不能恢复",function(){
		$.post(basePath+'pms/project/deleteProject.do',data,function(result){
			showBasicMessageFromServer(result,delSuccessMessage,delFailMessage);
		});
	});
	
}
// 作废立项
function delWorkflow(){
	FW.confirm("确定作废立项信息吗？",function(){
		$.post(basePath+'pms/project/deleteProject.do',{id:id},function(result){
			showBasicMessageFromServer(result,"作废成功","作废失败");
		});
	});
}

/**
 * 提交修改年度计划信息到后台,只是暂存
 */
function tmpSave(){
	if(!valid()){
		return ;
	}
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var workloads=null;
	var outsourcings=null;
	if(pmsPager.hasPri(pmsPager.opt,"pms-milestone-edit")){
		var milestonesData=getMilestoneData();
		var milestones=FW.stringify(milestonesData);
	}
	if(pmsPager.hasPri(pmsPager.opt,"pms-workload-edit")){
		workloads=getDatagridData(initWorkloadTable.dataGrid);
	}
	if(pmsPager.hasPri(pmsPager.opt,"pms-outsourcing-edit")){
		outsourcings=getDatagridData(initOutsourcingTable.dataGrid);
	}
	$.post(basePath+'pms/project/tmpUpdateProject.do',{"project":FW.stringify(data),milestones:milestones
		,workloads:FW.stringify(workloads),outsourcings:FW.stringify(outsourcings)},function(result){
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage);
	});
}

function valid(){
	if(!$("#form1").valid()){
		return false;
	}
	if(!$("#milestoneList").valid()){
		return false;
	}
	return true;
}

/**
 * 初始化招标列表
 * @param data 列表数据
 */
function initBidForm(data){
	if (data.bidResultVos && data.bidResultVos.length) {
		$('#bidListWrapper').show();
		if(initBidForm.dataGrid){
			return ;
		}
		
		$("#bidList").iFold();
		initBidForm.dataGrid=$("#test_grid1").datagrid({
			fitColumns:true,
			scrollbarSize:0,
			singleSelect:true,
		
			onDblClickRow : function(rowIndex, rowData) {
				var id=rowData["bidResultId"];
				openTab(basePath+"pms/bid/editBidResultJsp.do?id="+rowData['bidResultId'],'招标','pmsViewBidTab'+id,openTabBackReflesh);
			},
			data:data.bidResultVos,
			columns : [ [
			/* {field:'ck',checkbox:true}, */
			{
				field : 'bidId',
				hidden : true
			}, {
				field : 'type',
				hidden : true
			}, {
				field : 'name',
				title : '招标名称',
				width : 50,
				align : 'left'
			}, {
				field : 'typeName',
				title : '招标方式',
				width : 110,
				align : 'left',
				fixed : true
			}, {
				field : 'budget',
				title : '中标价(元)',
				width : 105,
				align : 'right',
				fixed : true
			}, {
				field : 'statusName',
				title : '状态',
				width : 85,
				align : 'left',
				fixed : true
			} ] ]

		});
		
	}else{
		$('#bidListWrapper').hide();
	}
}

/**
 * 初始化合同列表
 * @param data
 */
function initContractForm(data){
	if (data.contracts && data.contracts.length) {
		$('#contractListWrapper').show();
		if(initContractForm.dataGrid){
			return ;
		}
		
		$("#contractList").iFold();
		initContractForm.dataGrid=$("#contractTable").datagrid({
			fitColumns:true,
			scrollbarSize:0,
			singleSelect:true,
			pageSize : pageSize,// pageSize为全局变量，自动获取的
			onDblClickRow : function(rowIndex, rowData) {
				var id=rowData["id"];
				openTab(basePath+"pms/contract/editContractJsp.do?contractId="+rowData['id'],'合同','pmsViewContractTab'+id,openTabBackReflesh);
			},
			data:data.contracts,
			columns : [ [
			/* {field:'ck',checkbox:true}, */
			{
				field : 'id',
				hidden : true
			},{
				field : 'name',
				title : '合同名称',
				width : 50,
				align : 'left'
			}, {
				field : 'contractCode',
				title : '合同编号',
				width : 135,
				align : 'left',
				fixed : true
			}, {
				field : 'typeName',
				title : '合同类型',
				width : 80,
				align : 'left',
				fixed : true
			}, {
				field : 'signTime',
				title : '签订时间',
				width : 105,
				align : 'left',
				fixed : true,
				formatter: function(value,row,index){
 					//时间转date的string，还有long2date(value)方法
 					return FW.long2date(value);
 				}
			}, {
				field : 'totalSum',
				title : '合同金额(元)',
				width : 105,
				align : 'right',
				fixed : true
			}, {
				field : 'statusValue',
				title : '状态',
				width : 85,
				align : 'left',
				fixed : true
			} ] ]

		});
		
	}else{
		$('#contractListWrapper').hide();
	}
}

/**
 * 打开新建招标信息页面
 */
function openBidTab(){
	var projectId=id;
	openTab('pms/bid/addBidJsp.do?projectId='+projectId,'招标','pmsNewBidTab',openMutiTabAndBackReflesh);
}

function openContractTab(){
	var toComplete = "false";
	var taskId = "";
	var closeTabId = FW.getCurrentTabId();
	if(pmsPager&&pmsPager.opt&&pmsPager.opt.workflow&&pmsPager.opt.workflow.elements&&pmsPager.opt.workflow.elements.__elementKey__){
		if("true"==pmsPager.opt.workflow.isCandidate&&"procurement"==pmsPager.opt.workflow.elements.__elementKey__){
			toComplete = "true";
			taskId = pmsPager.opt.workflow.taskId;
		}
	}
	var projectId=id;
	openTab('pms/contract/addContractJsp.do?projectId='+projectId+'&closeTabId='+closeTabId+'&toComplete='+toComplete+'&taskId='+taskId,'合同','pmsNewContractTab',openMutiTabAndBackReflesh);
}

function initOther(opt){
	
	var data=opt.data;
	var form=$(opt.form);
	var formFields=opt.formFields;
	initAttachForm(data.data.attachMap,$('#attachForm'),$('#attachFormWrapper'),opt.readOnly && !opt.attach);
	//草稿状态隐藏bidComp
	hideIfDraftStatus(form,data.data);
	//判断是否能够编辑bidComp，如果是，启动编辑状态
	editBidComp(opt,form);
	//之前逻辑是如果是非候选人进入页面，不会获取流程节点的流程信息，然而这样的话，隐藏表单字段的属性设置就无法生效了
	formFieldHide();
	preparePrint();
	initRemoteField(form);
	initBidForm(data.data);
	initContractForm(data.data);
	neededCreateProjectCodeButton(opt);
	
	initWorkloadByPri(data.data.workloadVos,opt);
	initOutsourcingByPri(data.data.outsourcingVos,opt);
	initMilestoneByPri(data.data.milestoneVos,opt);
	initMilestoneChangeForRead(data.data.milestoneHistoryVos,opt);
	$("#form1").iForm("endEdit","flowNo");
	if(pmsPager&&pmsPager.opt&&pmsPager.opt.workflow&&pmsPager.opt.workflow.elements&&pmsPager.opt.workflow.elements.__elementKey__){
		if("true"==pmsPager.opt.workflow.isCandidate&&"procurement"==pmsPager.opt.workflow.elements.__elementKey__){
			$("#pms-project-complex-add").show();
		}
	}
	/**
	 * 重新设置bidCompName的click事件
	 */
	$("#f_bidCompName").attr("icon","itcui_btn_mag").removeClass("form-control").ITCUI_Input();
	$($("[fieldid=bidCompName] div")[0]).css("width","125px");
	$("#f_bidCompName").parent().on("click",function(){
		$("#f_bidCompName").blur();
		initDialog("true");
	});
	FW.fixToolbar("#toolbar1");
}

function hideIfDraftStatus(form,data){
	if(data.status=='draft'){
		hideBidCompAndCompany(form);
	}
}

function editBidComp(opt,form){
	//获取是否可以显示邀请/询价候选单位
	initRemoteSupplierData(form);
	if(pmsPager.getWorkflowValue("editBidComp")){
		//邀请/询价候选单位启动查询功能
		form.iForm('beginEdit','bidCompName');
	}
}

function preparePrint(){
	var proc_inst_id=getWorkflowProcessInstId();
	//fileExportPath
	var url=fileExportPath+"preview?__report=report/TIMSS2_PMS_SJW_PROJECT_001.rptdesign" +
			"&__format=pdf&siteid=SJW&sheet_id="+id+"&proc_inst_id="+proc_inst_id;
	url=url+"&url="+url+"&author="+loginUserId;
	pmsPrintHelp("#pms-project-print",url,"项目信息");
}
/**
 * 获取项目编号并赋值
 */
function createProjectCode(){
	$.post(basePath+"pms/sequence/getProjectSequence.do",{},function(result){
		var data=result.data;
		pmsPager.opt.form.iForm('setVal',{projectCode:data});
	});
}

function neededCreateProjectCodeButton(opt){
	if(opt && opt.createProjectCode){
		$('#b-create-project-code').show();
	}
}

function initWorkloadForEdit(data){
	$('#workloadWrapper').show();
	initWorkloadTable(data);
	
	
	//使表格处于编辑状态
	var dataGrid=initWorkloadTable.dataGrid;
	var rows=dataGrid.datagrid('getRows');
	for(var i in rows){
		dataGrid.datagrid('beginEdit',i);
	}
	

	
}
function initOutsourcingForEdit(data){
	$('#outsourcingWrapper').show();
	initOutsourcingTable(data);
	//使表格处于编辑状态
	var dataGrid=initOutsourcingTable.dataGrid;
	var rows=dataGrid.datagrid('getRows');
	for(var i in rows){
		dataGrid.datagrid('beginEdit',i);
	}
}


function initMilestoneForEdit(data){
	$('#milestoneWrapper').show();
	initMilestoneTable(data);
	//使表格处于编辑状态
	var dataGrid=initMilestoneTable.dataGrid;
	dataGrid.datagrid('hideColumn',"actualTime");
	dataGrid.datagrid('hideColumn',"expectedTime");
	var rows=dataGrid.datagrid('getRows');
	for(var i in rows){
		dataGrid.datagrid('beginEdit',i);
	}
}

function initOutsourcingForRead(data){
	if(data && data.length){
		$('#outsourcingWrapper').show();
		initOutsourcingTable(data);
		initOutsourcingTable.dataGrid.datagrid('hideColumn',"garbage-colunms");
		$('#b-add-outsourcing').hide();
	}
	
}

function initMilestoneForRead(data){
	if(data && data.length){
		$('#milestoneWrapper').show();
		initMilestoneTable(data);
		initMilestoneTable.dataGrid.datagrid('hideColumn',"garbage-colunms");
		$('#b-add-milestone').hide();
		$('#b-save-milestone-model').hide();
		$('#b-load-milestone-model').hide();
	}
	
}

function initWorkloadForRead(data){
	if(data && data.length){
		$('#workloadWrapper').show();
		initWorkloadTable(data);
		initWorkloadTable.dataGrid.datagrid('hideColumn',"garbage-colunms");
		$('#b-add-workload').hide();
	}
	
}

function initMilestoneChangeForRead(data){
	if(data){
		$('#milestoneChangeWrapper').show();
		initMilestoneChangeTable(data);
	}
	
}


/**
	根据权限以及工作量内容决定工作量信息如何显示
 */
function initWorkloadByPri(data,opt){
	
	var editable = pmsPager.hasPri(opt,"pms-workload-edit");
	if(editable){
		initWorkloadForEdit(data);
	}else {
		initWorkloadForRead(data);
	}
}
/**
根据权限以及外购内容决定外购需求如何显示
*/
function initOutsourcingByPri(data,opt){
	
	var editable = pmsPager.hasPri(opt,"pms-outsourcing-edit");
	if(editable){
		initOutsourcingForEdit(data);
	}else {
		initOutsourcingForRead(data);
	}
	
}

function initMilestoneByPri(data,opt){
	var editable = pmsPager.hasPri(opt,"pms-milestone-edit");
	if(editable){
		initMilestoneForEdit(data);
	}else {
		initMilestoneForRead(data);
	}
}



//重新加载datagrid信息
function pmsReload(){
	$.post(basePath+'pms/project/queryProjectById.do',{id:id,processInstId:processInstId},function(data){
		
		if(data && data.flag=='success'){
			var opt=data;
				
			initBidForm(data.data);
			initContractForm(data.data);
			if(data && data.data && data.data.bidResultVos && data.data.bidResultVos.length){
				initBidForm.dataGrid.datagrid("loadData",data.data.bidResultVos);
			}
			
			if(data && data.data && data.data.contracts && data.data.contracts.length){
				initContractForm.dataGrid.datagrid("loadData",data.data.contracts);
			}
		}
	});
}

function editMilestoneActualtime(){
	FW.set('pms-milestones',initMilestoneTable.dataGrid.datagrid("getRows"));
	FW.dialog("init",{
		src: basePath+"page/pms/itc/project/milestone.jsp",
		btnOpts:[{
	            "name" : "关闭",
	            "float" : "right",
	            "style" : "btn-default",
	            "onclick" : function(){
	                _parent().$("#itcDlg").dialog("close");
	             }
	        },
	        {
	            "name" : "确定",
	            "float" : "right",
	            "style" : "btn-success",
	            "onclick" : function(){
	            	var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
	            	//获取里程碑数据
	                var dataGrid=p.dataGrid;
	                var msdata=getDatagridData(dataGrid);
	                var msPostData=FW.stringify(msdata);
	                //记录提交处理是否成功
	                var result=false;
	                //提交后台处理
	                $.ajax({
	                	url:basePath+"pms/project/updateMilestone.do",
	                	type:"post",
	                	async:false,
	                	data:{milestones:msPostData,projectId:id},
	                	complete:function(data){
	                		var data=data.responseJSON;
	                		if(data && data.flag=="success"){
	                			result=true;
	                			 if(msdata && msdata.length){
	         	                	initMilestoneTable.dataGrid.datagrid('loadData',msdata);
	         	                }
	                		}
	                	}
	                });
	                return result;
	             }
	        }],
		dlgOpts:{ width:800, height:650, closed:false, title:"录入实施进度", modal:true }
	});
}

function changeMilestone(){
	if(initMilestoneTable.dataGrid){
		FW.set('pms-milestones',initMilestoneTable.dataGrid.datagrid("getRows"));
	}else{
		FW.set('pms-milestones',[]);
	}
	
	FW.dialog("init",{
		src: basePath+"page/pms/itc/project/milestone1.jsp?edit=all",
		btnOpts:[{
	            "name" : "关闭",
	            "float" : "right",
	            "style" : "btn-default",
	            "onclick" : function(){
	                _parent().$("#itcDlg").dialog("close");
	             }
	        },
	        {
	            "name" : "确定",
	            "float" : "right",
	            "style" : "btn-success",
	            "onclick" : function(){
	            	var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
	            	//获取里程碑数据
	                var dataGrid=p.dataGrid;
	                var msdata=getDatagridData(dataGrid);
	                var msPostData=FW.stringify(msdata);
	                //记录提交处理是否成功
	                var result=false;
	                //提交后台处理
	                $.ajax({
	                	url:basePath+"pms/project/changeMilestone.do",
	                	type:"post",
	                	async:false,
	                	data:{milestones:msPostData,projectId:id},
	                	complete:function(data){
	                		var data=data.responseJSON;
	                		if(data && data.flag=="success"){
	                			result=true;
	                			 if(msdata && msdata.length){
	                				initMilestoneForRead(msdata);
	         	                	initMilestoneTable.dataGrid.datagrid('loadData',msdata);
	         	                }
	                			pmsRefleshMilestoneHistroy();
	                		}
	                	}
	                });
	                return result;
	             }
	        }],
		dlgOpts:{ width:800, height:650, closed:false, title:"变更里程碑", modal:true }
	});
}
function initMilestoneChangeForRead(data){
	if(data && data.length){
		$('#milestoneChangeWrapper').show();
		initMilestoneChangeTable(data);
	}
	
}
function initMilestoneChangeTable(data){
	if(!initMilestoneChangeTable.dataGrid){
		$('#milestoneChangeList').iFold();
		initMilestoneChangeTable.dataGrid=$("#milestoneChangeTable").datagrid({
			fitColumns:true,
			scrollbarSize:0,
			singleSelect:true,
		
			onDblClickRow : function(rowIndex, rowData) {
				
			},
			data:data,
			onClickCell:function(rowIndex, field, value){
				if(field=="garbage-colunms"){
					initMilestoneChangeTable.dataGrid.datagrid('deleteRow',rowIndex);
				}
				
			},
			columns : [ [
			/* {field:'ck',checkbox:true}, */
			 {
				field : 'userName',
				title : '人员',
				width : 80,
				fixed :true,
				align : 'left',
				editor:{ 
					type : 'text'
				}
			}, {
				field : 'time',
				title : '日期',
				width : 105,
				fixed :true,
				align : 'left',
				editor:{
					type:"text"
				},
				formatter:function(value){
					return FW.long2date(value);
				}
			}, {
				field : 'content',
				title : '操作内容',
				width : 100,
				
				align : 'left',
				editor:{
					type:"text"
				}
			} ] ]
		});
	}
}

function pmsRefleshMilestoneHistroy(){
	$.post(basePath+"pms/project/queryProjectById.do?id="+id,{},function(data){
		if(data && data.data && data.data.milestoneHistoryVos){
			initMilestoneChangeForRead(data);
		}
		if(data && data.data && data.data.milestoneHistoryVos && data.data.milestoneHistoryVos.length){
			initMilestoneChangeTable.dataGrid.datagrid("loadData",data.data.milestoneHistoryVos);
		}
	});
}
//隐藏表单字段
function formFieldHide(){
	var data = pmsPager.opt.data;
	//当不是候选人查看的时候，workflow.elements为null,用workflow.formHide来实现隐藏，尽可能不改之前的逻辑
	if(data.pri&&data.pri.workflow&&!data.pri.workflow.elements){
		var formHide = data.pri.workflow.formHide;
		if(formHide){
			$("#form1").iForm('hide',formHide.split(",") );
		}
	}
}

//初始化弹出修改邀请单位对话框，
function initDialog(editable){
	var btnOpts = [{
		name:"取消",
		onclick:function(){
		   return true;
		}},
		{name:"确定",style:"btn-success",onclick:function(){
			getBidComp();
		}}
	];
	var id = "";
	if(pmsPager.opt&&pmsPager.opt.data&&pmsPager.opt.data.data&&pmsPager.opt.data.data.id){
		id = pmsPager.opt.data.data.id;
	}
	var id = $("#form1").iForm("getVal","id");
	
	var src = basePath +"pms/project/updateBidCompName.do?editable="+editable+"&id="+id+"&bidids="+$("#form1").iForm("getVal","bidCompId");
	var dlgWidth = 450;
	var dlgHeight = 400;
	FW.dialog("init",{
	    src : src,
	    dlgOpts : {
	    	title:"选择邀请/询价候选单位",
	    	width:dlgWidth,
	    	height:dlgHeight
	    },
	    btnOpts : btnOpts
	});
}
//获取选择的邀请单位
function getBidComp(){
	var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
	var d=p.getDataForSubmit();
	var ids = [];
	var names = [];
	for(var i =0;i<d.length;i++){
		ids.push(d[i]["key"]);
		names.push(d[i]["value"]);
	}
	$("#form1").iForm("setVal",{bidCompId:ids});
	$("#form1").iForm("setVal",{bidCompName:names});
	_parent().$("#itcDlg").dialog("close");
}