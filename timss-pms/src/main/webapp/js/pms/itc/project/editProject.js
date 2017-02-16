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
/**
 * 删除年度计划
 */
function del(){
	var data={id:id};
	FW.confirm("确定删除吗？删除后数据不能恢复",function(){
		$.post(basePath+'pms/project/deleteProject.do',data,function(result){
			showBasicMessageFromServer(result,delSuccessMessage,delFailMessage);
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
	var projectId=id;
	openTab('pms/contract/addContractJsp.do?projectId='+projectId,'合同','pmsNewContractTab',openMutiTabAndBackReflesh);
}

function initOther(opt){
	
	var data=opt.data;
	var form=$(opt.form);
	var formFields=opt.formFields;
	//附件组件附件权限信息
	initAttachFormFieldWithPri(attachFormFields);
	initAttachForm(data.data.attachMap,$('#attachForm'),$('#attachFormWrapper'),opt.readOnly && !opt.attach);
	//判断是否在副总经理审批界面，显示是否需要总经理审核字段，并显示为编辑状态
	if(opt.showNeedDSZ){
		form.iForm('beginEdit',"needDSZ");
		
	}else{
		form.iForm('hide',"needDSZ");
	}
	
	preparePrint();
	initRemoteField(form);
	initBidForm(data.data);
	initContractForm(data.data);
	neededCreateProjectCodeButton(opt);
	
	initWorkloadByPri(data.data.workloadVos,opt);
	initOutsourcingByPri(data.data.outsourcingVos,opt);
	initMilestoneByPri(data.data.milestoneVos,opt);
	initMilestoneChangeForRead(data.data.milestoneHistoryVos,opt);
}
function initAttachFormFieldWithPri(attachFormFields){
	var attachreserved=pmsPager.getWorkflowValue("attachPri") || 1;
	attachFormFields[0].options.uploader=attachFormFields[0].options.uploader+"&attachreserved="+attachreserved;
	pmsPager.attachreserved=attachreserved;
}
function preparePrint(){
	var proc_inst_id=getWorkflowProcessInstId();
	
	var url=fileExportPath+"preview?__report=report/TIMSS_PMS_PROJECT_001.rptdesign" +
			"&__format=pdf&siteid=ITC&sheet_id="+id+"&proc_inst_id="+proc_inst_id;
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
//项目负责人可编辑状态
function editProjectLeader(){
	$("#form1").iForm("beginEdit","projectLeader");
	$("#pms-projectLeader-change").html("保存项目负责人变更");
	$("#pms-projectLeader-change").unbind('click').removeAttr('onclick').click(function(){submitProjectLeader()});
}
//提交项目负责人编辑
function submitProjectLeader(){
	var data=$('#form1').iForm('getVal');
	$.post(basePath+'pms/project/updateProjectLeader.do',{"params":"projectLeader","project":FW.stringify(data)},function(result){
		showBasicMessageFromServer(result,saveSuccessMessage,saveFailMessage);
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

function zuofei(){
	var options={
		destUrl:basePath+"pms/project/voidFlow.do",
		businessId:id,
		processInstId:getWorkflowProcessInstId(),
		taskId:getWorkflowTaskId(),
		tipMessage:"确认作废|确认作废该项目立项流程？"
	};
	voidFlow(options);
}