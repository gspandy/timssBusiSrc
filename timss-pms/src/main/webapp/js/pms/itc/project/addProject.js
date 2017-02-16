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
	if(hasEditMilestonePrip()){
		milestonesData=getMilestoneData();
		milestones=FW.stringify(milestonesData);
		
	}
	if(hasEditWorkloadPrip()){
		workloads=getDatagridData(initWorkloadTable.dataGrid);
	}
	if(hasEditOutsourcingPrip()){
		outsourcings=getDatagridData(initOutsourcingTable.dataGrid);
	}
	$.post(basePath+'pms/project/insertProjectWithWorkflow.do',{"project":FW.stringify(data),milestones:milestones,
		workloads:FW.stringify(workloads),outsourcings:FW.stringify(outsourcings)},function(result){
		data.id=result && result.data && result.data.id;
		data.milestones=milestonesData;
		data.outsourcings=outsourcings;
		data.workloads=workloads;
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,
				{successFunction:openNewUserDialogFirstInTab,tabOpen:true,data:data,resetId:_this});
	});
	

}


function tmpSave(_this){
	if(!valid()){
		return ;
	}
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var milestones=null;
	var workloads=null;
	var outsourcings=null;
	if(hasEditMilestonePrip()){
		milestones=getMilestoneData();
	}
	if(hasEditWorkloadPrip()){
		workloads=getDatagridData(initWorkloadTable.dataGrid);
	}
	if(hasEditOutsourcingPrip()){
		outsourcings=getDatagridData(initOutsourcingTable.dataGrid);
	}
	$.post(basePath+'pms/project/tmpInsertProject.do',{"project":FW.stringify(data),milestones:FW.stringify(milestones),
		workloads:FW.stringify(workloads),outsourcings:FW.stringify(outsourcings)},function(result){
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage);
	});
}
function valid(){
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
用年度计划信息初始化表单
*/
function initFormWithPlanData(planId){
	function initOtherWithData(opt){
		var form=opt.form;
		var values=opt.otherData;
		form.iForm('setVal',values);
		form.iForm('endEdit','planName');
		addProjectCodeEvent();
		hideDMZFormField(form);
		initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
		initMileStoneForAdd();
		initOutsourcingForAdd();
		initWorkloadForAdd();
	}
	$.post(basePath+'pms/plan/queryPlanById.do',{id:planId},function(result){
		result=result.data;
		var values={
				'planId':result["id"],
				'planName':result['planName'],
				'pyear':result['year'],
				'ptype':result['type'],
				'property':result['property'],
				'projectLeader':result["projectLeader"],
				'customManager':result['customManager'],
				'startTime':result['startTime'],
				'endTime':result['endTime'],
				'command':result['command']
			};
		var opt={
			form:$('#form1'),
			formFields:projectFormFields,
			initOther:initOtherWithData,
			otherData:values
		};
		pmsPager.init(opt);
	});
}

function showWorkflow(){
	var defKey="pms_itc_project";
	var workFlow = new WorkFlow();
    workFlow.showDiagram(defKey);
}

function initOther(opt){
	var form=opt.form;
	initRemoteField(form);
	hideDMZFormField(form);
	initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
	initMileStoneForAdd();
	initOutsourcingForAdd();
	initWorkloadForAdd();
}

function createProjectCode(){
	FW.dialog("init",{
		src:basePath+"pms/project/produceProjectCode.do",
		dlgOpts : {
			title : "生成项目编号",
			idSuffix : "pms-produce-project-code",
			width : 440,
	        height : 170},
		
		btnOpts:[{
	        "name" : "取消",
	        "float" : "right",
	        "style" : "btn-default",
	        "onclick" : function(){
	            _parent().$("#itcDlgpms-produce-project-code").dialog("close");
	        }
	    },{
	        "name" : "确定",
	        "float" : "right",
	        "style" : "btn-success",
	        "onclick" : function(){
	            var p = _parent().window.document.getElementById("itcDlgpms-produce-project-codeContent").contentWindow;
	            if(!p.valid()){
	            	return false;
	            }
	            var result=false;
	            $.ajax({
	            	url:basePath+'pms/sequence/getProjectSequence.do',
	            	data:p.getData(),
	            	complete:function(data){
	            		var data=data.responseJSON;
		            	if(data && data.flag=='success'){
		            		var code=data.data;
			            	$('#form1').iForm('setVal',{projectCode:code});
			            	result=true;
		            	}
	            	},
	            	async:false
	            });
	            return result;
	        }
	    }
	   ]
	});
}

//根据框架的权限判断是否需要添加里程碑节点
function initMileStoneForAdd(data){
	var editable=hasEditMilestonePrip();
	if(editable){
		$("#milestoneWrapper").show();
		initMilestoneTable(data);
		initMilestoneTable.dataGrid.datagrid("hideColumn","actualTime");
		initMilestoneTable.dataGrid.datagrid("hideColumn","expectedTime");
	}
}

function initOutsourcingForAdd(data){
	var editable=hasEditOutsourcingPrip();
	if(editable){
		$("#outsourcingWrapper").show();
		initOutsourcingTable(data);
	//	initOutsourcingTable.dataGrid.datagrid("hideColumn",["actualTime"]);
	}
}

function initWorkloadForAdd(data){
	var editable=hasEditWorkloadPrip();
	if(editable){
		$("#workloadWrapper").show();
		initWorkloadTable(data);
		//initWorkloadTable.dataGrid.datagrid("hideColumn",["actualTime"]);
	}
}



function hasEditMilestonePrip(){
	return _parent().privMapping["pms-milestone-edit"];
}

function hasEditOutsourcingPrip(){
	return _parent().privMapping["pms-outsourcing-edit"];
}

function hasEditWorkloadPrip(){
	return _parent().privMapping["pms-workload-edit"];
}