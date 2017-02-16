function submit(){
	if(!$("#form1").valid()){
		return ;
	}
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#bidAttachForm').iForm('getVal'));
	var rowdata=getDataGridRowData(dataGrid);
	$.post(basePath+'pms/bid/insertBid.do',{"bid":FW.stringify(data),"bidCons":FW.stringify(rowdata)},function(result){
		showBasicMessageFromServer(result,"新建成功","新建失败");
	});
}

function initFormWithProjectData(projectId){
	function initOtherWithData(opt){
		var value=opt.otherData;
		var form=opt.form;
		form.iForm('setVal',value);
		form.iForm('endEdit','projectName');
		$("#supplierListWrapper").iFold();
		initSupplierDataGrid();
		dataGrid.datagrid('showColumn','garbage-colunms');
		initBidAttachForm([],$('#bidAttachForm'),$('#bidAttachFormWrapper'),false);
	}
	$.post(basePath+'pms/bid/queryProjectByProjectId.do',{"id":projectId},function(result){
		var result=result.data;
		var value={
			projectName:result['projectName'],
			projectId:projectId
		};
		var opt={
			form:$("#form1"),
			formFields:bidFormFields,
			initOther:initOtherWithData,
			otherData:value
		};
		initProjectForm(result);
		pmsPager.init(opt);
	});
}

function initProjectForm(data){
	var projectForm=$('#projectForm');
	projectForm.iForm('init',{fields:projectFormFields,options:{fieldPrefix:'project_'}});
	var tmp=$.fn.extend({},data,data.budget);
	projectForm.iForm('setVal',tmp);
	projectForm.iForm('endEdit');
	
}

