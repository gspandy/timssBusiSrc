//初始化
function initFormWithEmptyData(){
	var opt={
		form:$("#form1"),
		formFields:fmpFormFields,
		initOther:initOtherWithEmptyData
	};
	pmsPager.init(opt);
	$("#f_creator").html(userName);
	//报销金额 ， 收款人，批复金额以及开支科目都是不可编辑的
}
//初始化其他数据 如附件和报销详情
function initOtherWithEmptyData(opt){
	var form=opt.form;
	//附件表单初始化
	initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
	//报销明细数据表格区域初始化
	initExpensesWrapper();
	initExpensesDataGrid();
	//给收款方和申请单ihint控件绑定值域
	initRemoteData();
}
//提交行政报销
function submit(_this){
	//校验表单和报销明细数据表格
	if(!validExpensesForm()){
		return false;
	}
	buttonLoading(_this);
	var data=$('#form1').iForm('getVal');
	var processInstId = $('#form1').iForm('getVal',"processInstId");
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	//至少输入一条报销明细
	var expensesDtlData = getExpensesDtlListData(dataGrid);
	if(0==expensesDtlData.length){
		FW.error("请输入报销明细");
		$(_this).button('reset');
		return false;
	}
	$.post(basePath+"finance/expenses/saveOrUpdateAdministrativeExpenses.do",{processInstId:processInstId,expenses:FW.stringify(data),expensesDtl:FW.stringify(expensesDtlData)},function(result){
		var fmp = result.data["fmp"];
		$("#form1").iForm('setVal',fmp);
		$("#form1").iForm('setVal',{processInstId:result.data["processInstId"]});
		//注意要把首次提交后，启动的流程实例id回写回去
		data=$('#form1').iForm('getVal');
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,{successFunction:startWorkFlow(data,result.data["taskId"],result.data["processInstId"]),tabOpen:true,resetId:_this});
	});
}
//启动行政报销--下一环节选人对话框
function startWorkFlow(data,taskId,processInstId){
	var workFlow = new WorkFlow();
	forbidTipAfterCloseTab();
	workFlow.submitApply(taskId,FW.stringify(data),closeTab,null,0,closeTabAndTip);
	function closeTabAndTip(){
		closeTab();
		FW.success("数据已暂存为草稿，请到首页草稿菜单查看");
	}
}
//暂存行政报销
function tmpSave(){
	//校验表单
	if(!validExpensesForm()){
		return false;
	}
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var expensesDtlData = getExpensesDtlListData(dataGrid); 
	if(0==expensesDtlData.length){
		FW.error("请输入报销明细");
		return false;
	}
	$.post(basePath+"finance/expenses/tmpSaveAdministrativeExpenses.do",{expenses:FW.stringify(data),expensesDtl:FW.stringify(expensesDtlData)},function(result){
		var fmp = result.data["fmp"];
		$("#form1").iForm('setVal',fmp);
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,{tabOpen:false});
	});
}
//显示流程信息
function showWorkflow(){
	var workFlow = new WorkFlow();
    workFlow.showDiagram(defKey);
}