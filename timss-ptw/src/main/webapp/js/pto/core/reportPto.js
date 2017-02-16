/**
 * 判断myId 是否在候选人 candidateUsers 中
 * 
 * @param candidateUsers
 * @param myId
 */
function isMyActivityPto(candidateUsers, myId) {
	for (var i = 0; i < candidateUsers.length; i++) {
		if (candidateUsers[i] == myId) {
			auditInfoShowBtn = 1;
			return true;
		}
	}
	return false;
}

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
	var currHandlerUser = ptoInfoVoData.currHandlerUser;
	if(currHandlerUser != "" && currHandlerUser != null){
		var currHandlerUserArray = currHandlerUser.split(",");
		flag = isMyActivityPto(currHandlerUserArray,loginUserId);
	}
	$("#ptoForm").iForm("hide",["isProper","problem"]);
	if(typeof(hasWindStation)=="undefined"){
		$("#ptoForm").iForm("hide",["windStation"]);
	}
	
	
	//表单赋值
	$("#ptoForm").iForm("setVal",ptoInfoVoData);
	$("#ptoForm").iForm("endEdit",["currStatus"]);
	//操作项赋值
	var operItemList = {};
	operItemList.safeDatas = ptoInfoVoData.ptoOperItemList;
	
	if (ptoAttachment.length > 0) {
		$("#uploadfileTitle").iFold("show");
		$("#uploadform").iForm("setVal", {uploadfield : ptoAttachment});
	}else if(ptoAttachment.length === 0 && currHandlerUser.indexOf(loginUserId) < 0){
		$("#uploadfileTitle").iFold("hide");
	}
	
	if(flag){
		if(currStatus=="report"){
			if(typeof(hasWindStation)!="undefined"&&hasWindStation){
				$("#ptoForm").iForm("endEdit",["windStation","task","assetId","preBeginOperTime","preEndOperTime","type",
												"guardian","commander","operItemRemarks"]);
			}else{
				$("#ptoForm").iForm("endEdit",["task","assetId","preBeginOperTime","preEndOperTime","type","operItemRemarks"]);
			}
			editOperSafeItemListByData("safeItemTest",operItemList); 
		}else{
			$("#btn_pto_audit1").hide();
			$("#ptoForm").iForm("endEdit");
			$("#uploadform").iForm("endEdit");
			showOperSafeItemListByData("safeItemTest",operItemList);
		}
	}else{  //不在自己手上
		$("#btn_pto_audit1").hide();
		$("#ptoForm").iForm("endEdit");
		$("#uploadform").iForm("endEdit");
		if(currStatus=="report"){  
			$("#operItemTitle").hide();
			//$("#ptoForm").iForm("hide",["operator","ondutyPrincipal","ondutyMonitor",""]);
			initSafeItemListByData("safeItemTest",operItemList); 
			endEditSafeItemList("safeItemTest");
		}else{
			showOperSafeItemListByData("safeItemTest",operItemList);
		}
		
	}
}




