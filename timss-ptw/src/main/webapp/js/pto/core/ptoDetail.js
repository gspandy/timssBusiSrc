function showPage(ptoInfoVoData){
	//控制权限
	Priv.apply();
	if(newBtnShowFlag){  //控制复制按钮的显示与隐藏
		$("#pto_btn_copy").show();
	}else{
		$("#pto_btn_copy").hide();
	}
	var flag = false;
	var currStatus = ptoInfoVoData.currStatus;
	var isProper = ptoInfoVoData.isProper;
	
	if(newSptoPrivFlag === true){
		$("#btn_newSpto").show();
	}else{
		$("#btn_newSpto").hide();
	}
	if(isProper === "Y"||isProper === "N"){
		$("#btn_ptoCheck").hide();
	}
	
	//表单赋值
	$("#ptoForm").iForm("setVal",ptoInfoVoData);
	//操作项赋值
	var operItemList = {};
	var operItemListObj = ptoInfoVoData.ptoOperItemList;
	
	if(!isNeedFlow){
		$("#btn_auditInfo").hide();
		for(var i = 0 ;i<operItemListObj.length; i++)
			
			if(currStatus == "end"){
				operItemListObj[i].hasOper = "Y";
			}else if(currStatus == "new" ||currStatus =="obsolete"){
				
				operItemListObj[i].hasOper = "N";
			}
		
		operItemList.safeDatas = ptoInfoVoData.ptoOperItemList;
		
		if(currStatus == "end" ||currStatus =="obsolete"){
			showOperSafeItemListByData("safeItemTest",operItemList);
			$("#operItemTitle_time").hide();
			if(currStatus =="obsolete"){
				$("#pto_btn_copy").hide();
				$("#btn_ptoCheck").hide();
				$("#btn_newSpto").hide();
				$("#btn_print").hide();
				$("#inPageTitle").html("操作票详情(已作废)")
			}
		}else if(currStatus == "new" ){
			initSafeItemListByData("safeItemTest",operItemList);
			$("#operItemTitle_flag").hide();
			$("#operItemTitle_time").hide();
			$("#operItemTitle").css({"padding-left":"40px"});
		}
	}else{ //走流程
		operItemList.safeDatas = ptoInfoVoData.ptoOperItemList;
		showOperSafeItemListByData("safeItemTest",operItemList);
	}
	
	
	
	if (ptoAttachment.length > 0) {
		$("#uploadfileTitle").iFold("show");
		$("#uploadform").iForm("setVal", {uploadfield : ptoAttachment});
	}else if(ptoAttachment.length === 0){
		$("#uploadfileTitle").iFold("hide");
	}
	
	$("#ptoForm").iForm("endEdit");
	$("#uploadform").iForm("endEdit");
	
	

}