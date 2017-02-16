function submit(){
	if(!valid(submit)){
		return ;
	}
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var payplanData=getPayplanListData(dataGrid);
	$.post(basePath+"pms/contract/insertContract.do",{contract:FW.stringify(data),payplans:FW.stringify(payplanData)},function(result){
		showBasicMessageFromServer(result,saveSuccessMessage,saveFailMessage,closeTab);
	});
}

function tmpSave(){
	if(!valid(tmpSave)){
		return ;
	}
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var payplanData=getPayplanListData(dataGrid);
	$.post(basePath+"pms/contract/tmpInsertContract.do",{contract:FW.stringify(data),payplans:FW.stringify(payplanData)},function(result){
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,closeTab);
	});
}

function initFormWithProjectData(projectId){
	
	function initOtherWithData(opt){
		var form=opt.form;
		var value=opt.otherData;
		form.iForm('endEdit',['projectName','bidName']);
		form.iForm('hide','createuser');
		form.iForm('setVal',value);
		addContractCodeEvent();
		initRemoteFirstPartyData(form);
		initRemoteSecondPartyData(form);
		initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
		initPayplanWrapper();
		initPayplansDataGrid();
		hideColumn();
	}
	$.post(basePath+'pms/project/queryProjectById.do',{id:projectId},function(result){
		result=result.data;

		var value={
			projectId:result['id'],
			projectName:result['projectName'],
			bidName:"",
			name:result['projectName'],
			type:result["property"]
		};
		var opt={
			form:$('#form1'),
			otherData:value,
			formFields:contractFormFields,
			initOther:initOtherWithData
		};
		pmsPager.init(opt);

		
	});
}

function initFormWithBidAndProject(bidId,projectId){
	
	function initOther(opt){
		var form=opt.form;
		var data=opt.otherData;
		form.iForm('endEdit',['projectName','bidName']);
		form.iForm('hide','createuser');
		form.iForm('setVal',data);
		addContractCodeEvent();

		initBidSupplierDataByBidId(form,data.bidId);
		initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
		initPayplanWrapper();
		initPayplansDataGrid();
		hideColumn();
	}
	$.post(basePath+'pms/contract/queryContractByBidId.do',{projectId:projectId,bidId:bidId},function(result){
		if(result && result.flag=="success"){
			var data=result.data;
			data.name=data['projectName'];
			var value={
				projectId:data["projectId"],
				bidId:bidId,
				firstPartyId:data["firstPartyId"],
				secondPartyId:data["secondPartyId"],
				projectName:data["projectName"],
				name:data["projectName"],
				bidName:data["bidName"],
				type:data["type"]
			};
			var opt={
				form:$('#form1'),
				otherData:value,
				formFields:contractFormFields,
				initOther:initOther
			};
			pmsPager.init(opt);
		}
	});
}

//初始化表单时没有包含项目和招标信息
function initFormWithEmptyData(){
	var opt={
		form:$("#form1"),
		formFields:contractFormFields,
		initOther:initOtherWithEmptyData
	};
	pmsPager.init(opt);
}

function initOtherWithEmptyData(opt){
	var form=opt.form;
	initRemoteData(form);
	initRemoteFirstPartyData(form);
	initRemoteSecondPartyData(form);
	addContractCodeEvent();
	initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
	initPayplanWrapper();
	initPayplansDataGrid();
	hideColumn();
	$("#form1").iForm("hide",'createuser');
}
//初始化项目信息，使其能够获取远程项目数据
function initRemoteData(form){
	//招标相关
	var bidInput=$('#f_bidName');
	var bidInit={
		datafunc:function(){
			return changeBidResultSourceToiHint(pmsPager.bidResultSource);
		},
		clickEvent : function(id, name){
            var value={
            	bidId:id,
            	bidName:name
            };
            form.iForm('setVal',value);
			initBidSupplierDataByBidId(form,id);
		}
	};
	var $projectInput = $('#f_projectName');
	var projectInit = {
		datasource : basePath + "pms/project/queryProjectByKeyWord.do",
		clickEvent : function(id, name) {
			$projectInput.val(name);
			$.post(basePath+'pms/project/queryProjectById.do',{id:id},function(result){
				result=result.data;

				var value={
					projectId:result['id'],
					projectName:result['projectName'],
					type:result["property"]
				};
				form.iForm("setVal", value);
				pmsPager.bidResultSource=result && result.bidResultVos;
				if(pmsPager.bidResultSource&&pmsPager.bidResultSource.length){
					form.iForm('show','bidName');
					bidInput.iHint('init', bidInit);
				}else{
					form.iForm('hide','bidName');
					var value={
							bidId:null,
							bidName:null
						};
					form.iForm("setVal", value);
				}
			});
		}
	};
	
	$projectInput.iHint('init', projectInit);
}

//根据招标id和选中的合同类型初始化相关的招标信息。
function initBidSupplierDataByBidId(form,id){
	$.post(basePath+"pms/bid/queryBidResultById.do?id="+id,{},function(data){
		var bidResultVo=data.data;
		var formData=form.iForm('getVal');
		var type=formData["type"];
		var supplierId=bidResultVo.supplierId;
		if(type=='income'){
			initFirstPartyDataBySupplierId(form,supplierId);
		}else{
			initSecondPartyDataBySupplierId(form,supplierId);
		}
	});
	
}

//将招标结果转换为iHint可识别的数据源
function changeBidResultSourceToiHint(source){
	var result=[];
	for(var k in source){
		var record={
			id:source[k]["bidResultId"],
			name:source[k]["name"]
		};
		result.push(record);
	}
	return result;
}

//生成合同编码修改
function createContractCode(){
	var contractType = pmsPager.opt.form.iForm('getVal',"contractCategory");
	if(""==contractType){
		FW.error("请先选择合同类别");
		return;
	}
	$.post(basePath+'pms/sequence/getContractSequence.do?siteName=ITC&type='+contractType,{},function(result){
		var data=result.data;
		pmsPager.opt.form.iForm('setVal',{contractCode:data});
	});
}
//提交合同
function submitWithWorkFlow(_this){
	if(!$("#form1").valid()){
		return false;
	}
	buttonLoading(_this);
	var data=$('#form1').iForm('getVal');
	var processInstId = $('#form1').iForm('getVal',"processInstId");
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	$.post(basePath+"pms/contract/saveOrUpdateContractWithWorkFlow.do",{processInstId:processInstId,contract:FW.stringify(data)},function(result){
		var contract = result.data["contract"];
		//$("#form1").iForm('setVal',contract);
		$("#form1").iForm('setVal',{processInstId:result.data["processInstId"]});
		$("#form1").iForm('setVal',{id:result.data["id"]});
		//注意要把回写的值补充回去
		data["id"] = $('#form1').iForm('getVal',"id");
		data["processInstId"] = $('#form1').iForm('getVal',"processInstId");
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,{successFunction:turnToSecStep(data,result.data["taskId"],result.data["processInstId"]),tabOpen:true,resetId:_this});
	});
}
//提交合同--流转到第二环节
function turnToSecStep(data,taskId,processInstId){    
	var workFlow = new WorkFlow();
	forbidTipAfterCloseTab();
	//暂时屏蔽流程终止功能
	workFlow.submitApply(taskId,FW.stringify(data),closeTab,null,0,closeTab);
}
//暂存合同
function tmpSaveWithWorkFlow(){
	if(!$("#form1").valid()){
		return false;
	}
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	$.post(basePath+"pms/contract/tmpSaveContractWithWorkFlow.do",{contract:FW.stringify(data)},function(result){
		var contract = result.data["contract"];
		$("#form1").iForm('setVal',contract);
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,{tabOpen:false});
	});
}
//显示流程信息
function showWorkflow(){
	var workFlow = new WorkFlow();
    workFlow.showDiagram(defKey);
}