function addBidMethod(){
	if(!addBidMethod.init){
		addBidMethod.init=true;
		$("#bidMethodWrapper").iFold();
		$("#b-add-bidmethod").html('提交');
		$('#form2').iForm("init",{"fields":bidMethodFormFields,"options":{fieldPrefix:"f2_"}});
		initBidMethodAttachForm([],$('#bidMethodAttachForm'),$('#bidMethodAttachFormWrapper'),false);
	}else{
		if(!$("#form2").valid()){
			return ;
		}
		var data=$('#form2').iForm('getVal');
		data['bidId']=id;
		$.fn.extend(true,data,$('#bidMethodAttachForm').iForm('getVal'));
		$.post(basePath+"pms/bid/insertBidMethod.do",{"bidMethod":FW.stringify(data)},function(result){
			showBasicMessageFromServer(result,"新建成功","新建失败");
		});
	}
}

function addBidResult(){
	if(!addBidResult.init){
		addBidResult.init=true;
		$("#bidResultWrapper").iFold();
		$("#b-add-bidresult").html('提交');
		$('#form3').iForm("init",{"fields":bidResultFormFields,"options":{fieldPrefix:"f3_"}});
		initBidResultAttachForm([],$('#bidResultAttachForm'),$('#bidResultAttachFormWrapper'),false);
		
		beginSupplierEdit();
		
	}else{
		if(!$("#form3").valid()){
			return ;
		}
		var data=$('#form3').iForm('getVal');
		data['bidId']=id;
		$.fn.extend(true,data,$('#bidResultAttachForm').iForm('getVal'));
		var rowdata=getDataGridRowData(dataGrid);
		$.post(basePath+"pms/bid/insertBidResult.do",{"bidResult":FW.stringify(data),"bidCons":FW.stringify(rowdata)},function(result){
			showBasicMessageFromServer(result,"新建成功","新建失败");
		});
	}
}

function initBidMethodForm(data,readOnly){
	if(data.bidMethod && data.bidMethod.length){
		$("#bidMethodWrapper").iFold();
		var form=$('#form2');
		//$.fn.extend(bidMethodAttachMap,data.bidMethodAttachMap);
		form.iForm("init",{"fields":bidMethodFormFields,"options":{fieldPrefix:"f2_"}});
		form.iForm('setVal',data.bidMethod[0]);
		if(readOnly==true){
			form.iForm('endEdit');
		}
		initBidMethodAttachForm(data.bidMethodAttachMap,$('#bidMethodAttachForm'),$('#bidMethodAttachFormWrapper'),readOnly);
	}
}

function initBidResultForm(data,readOnly){
	if(data.bidResult && data.bidResult.length){
		$("#bidResultWrapper").iFold();
		var form=$('#form3');
		//$.fn.extend(bidResultAttachMap,data.bidResultAttachMap);
		form.iForm("init",{"fields":bidResultFormFields,"options":{fieldPrefix:"f3_"}});
		form.iForm('setVal',data.bidResult[0]);
		if(readOnly==true){
			form.iForm('endEdit');
		}
		initBidResultAttachForm(data.bidResultAttachMap,$('#bidResultAttachForm'),$('#bidResultAttachFormWrapper'),readOnly);
	}
}

function initBidConDataGrid(data,readOnly){
	if(data && data.bidConVos && data.bidConVos.length){
		$("#supplierListWrapper").iFold();
		initSupplierDataGrid(data.bidConVos);
		if(data.bidResult && data.bidResult.length){
			showDataGridColumn();
		}
	}
	if(readOnly){
		$("#b-add-bid-con").hide();
	}else{
		$("#supplierListWrapper").iFold();
	}
}
function initOther(opt){
	var data=opt.data;
	var form=opt.form;
	initBidAttachForm(data.data.bidAttachMap,$('#bidAttachForm'),$('#bidAttachFormWrapper'),opt.readOnly);

	initBidMethodForm(data.data,opt.readOnly);
	initBidResultForm(data.data,opt.readOnly);
	initBidConDataGrid(data.data,opt.readOnly);
}

function addNewContract(){
	var projectId=pmsPager.opt.data.data.projectId;
	openTab('pms/contract/addContractJsp.do?projectId='+projectId+"&bidId="+id,'合同','pmsNewContractTab');
}