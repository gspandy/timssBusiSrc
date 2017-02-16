function islButtonControl(opType,islStatus){
	$("#toolbar .btn").hide();
	
	$("#closeDiv").show();
	if(!opType){
		return;
	}
	//新建隔离证
	if(opType == 'newIsolation'){
		$("#saveButtonDiv").show();
	}else if(opType == 'islDetail'){
		if(islStatus == 300){//未签发
			$("#signBtnDiv").show();
			$("#editBtn").show();
			$("#cancelBtn").show();
			$("#remarkBtn").show();
		}else if(islStatus == 400){//已签发
			$("#permitBtnDiv").show();
			$("#cancelBtn").show();
			$("#remarkBtn").show();
		}else if(islStatus == 500){//已许可
			$("#finBtnDiv").show();
			$("#remarkBtn").show();
		}else if(islStatus == 600){//已结束
			$("#endBtnDiv").show();
			$("#remarkBtn").show();
		}else{
			$("#remarkBtn").hide();
		}
		
		//打印和复制一直都能出现
		$("#printBtn").show();
		$("#copyBtn").show();
		$("#moreBtnDiv").show();
	
	}
	
	FW.fixToolbar("#toolbar");
}