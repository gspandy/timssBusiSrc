

function showForm() {
	formFields2 = [];
	//动态添加form表单
	for(var i=0 ; i<finPageConfData.length;i++){
		if(finTypeEn == finPageConfData[i].reimburseType){ 
			var addFields = finPageConfData[i].formConf;
			appendFormFields(formFields2,addFields,formBaseFields);
			break;
		}
	}
	$("#autoform").removeData();  
	$("#autoform").iForm("init",{"options":{validate:true},"fields":formFields2});
	$("#autoform").iForm("setVal",{"finance_typeid":finTypeEn});
	if(bussinessStatus != finWorkFlowStatusCodeConstant.FMP_DEPTMANAGER_APPROVE){
		$("#autoform").iForm( "hide", "accType" ); //隐藏账务类型
	}
	if(bussinessStatus != finWorkFlowStatusCodeConstant.MAIN_ACCOUNT_APPROVE){
		$("#autoform").iForm( "hide", "needModify" );
	}
	
	initFormVal(); //对表单赋值
	
	initHint("f_applyName");
	
	if(oprModeEn !="edit" && applyId != "" && applyId != null){
		setFormValFromApply(applyId);
	}
}


