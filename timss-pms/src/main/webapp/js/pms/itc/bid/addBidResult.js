function submit(_this){
	if(!$("#form1").valid()){
		return ;
	}
	buttonLoading(_this);
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#bidAttachForm').iForm('getVal'));
	
	$.post(basePath+'pms/bid/tmpInsertBidResult.do',{"bidResult":FW.stringify(data)},function(result){
		data.bidResultId=result && result.data && result.data.bidResultId;
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,
				{successFunction:openNewUserDialogFirstInTab,tabOpen:true,data:data,resetId:_this});
	});
}

function tmpSave(){
	if(!$("#form1").valid()){
		return ;
	}
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#bidAttachForm').iForm('getVal'));
	$.post(basePath+'pms/bid/tmpInsertBidResult.do',{"bidResult":FW.stringify(data)},function(result){
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage);
	});
}

function initFormWithProjectData(projectId){
	function initOtherWithData(opt){
		var value=opt.otherData;
		var form=opt.form;
		form.iForm('setVal',value);
		form.iForm('endEdit','projectName');
		form.iForm('hide','needFZSH');
		initRemoteSupplierData(form);
		initBidAttachForm([],$('#bidAttachForm'),$('#bidAttachFormWrapper'),false);
	}
	$.post(basePath+'pms/bid/queryProjectByProjectId.do',{"id":projectId},function(result){
		var result=result.data;
		var value={
			projectName:result['projectName'],
			projectId:projectId,
			type:"yqzb"
		};
		var opt={
			form:$("#form1"),
			formFields:bidFormFields,
			initOther:initOtherWithData,
			otherData:value
		};
		
		pmsPager.init(opt);
	});
}
//初始化表单，不是从项目新建的表单
function initFormWitoutProjectData(){
	var opt={
			form:$("#form1"),
			formFields:bidFormFields,
			initOther:initOther
		};
	
	pmsPager.init(opt);
}

function initOther(opt){
	var form=opt.form;
	initRemoteProjectData(form);
	initRemoteSupplierData(form);
	form.iForm('setVal',{type:"yqzb"});
	form.iForm('hide','needFZSH');
	initBidAttachForm([],$('#bidAttachForm'),$('#bidAttachFormWrapper'),false);
}

//项目信息字段初始化，使其能够远程读取项目信息
function initRemoteProjectData(form){
	var $projectInput = $('#f_projectName');
	var projectInit = {
		datasource : basePath + "pms/project/queryProjectByKeyWord.do",
		clickEvent : function(id, name) {
			$projectInput.val(name);
			$.post(basePath+'pms/project/queryProjectById.do',{id:id},function(result){
				result=result.data;
				var value={
						projectName:result['projectName'],
						projectId:id
				};
				form.iForm("setVal", value);
			});
		}
	};
	$projectInput.iHint('init', projectInit);
}

