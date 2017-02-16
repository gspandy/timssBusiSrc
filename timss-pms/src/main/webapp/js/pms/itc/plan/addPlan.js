/**
 * 新建年度计划时，提交年度计划
 */
function submit(_this){
	
	if(!$("#form1").valid()){
		
		return ;
	}
	buttonLoading(_this);
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	$.post(basePath+'pms/plan/insertPlan.do',{"plan":FW.stringify(data)},function(result){
		showBasicMessageFromServer(result,saveSuccessMessage,saveFailMessage,{resetId:_this});
	});
}
/**
 * 暂存年度计划，提交
 */
function temSave(_this){
	if(!$("#form1").valid()){
		
		return ;
	}
	buttonLoading(_this);
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	$.post(basePath+'pms/plan/tmpInsertPlan.do',{"plan":FW.stringify(data)},function(result){
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,{resetId:_this});
	});
}

function initOther(opt){
	initProfit();
	initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
}