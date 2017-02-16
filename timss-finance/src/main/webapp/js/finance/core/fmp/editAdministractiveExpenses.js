//报销明细编辑模式
function showEditableExpensesList(data){
	initExpensesWrapper();
	initExpensesDataGrid(data && data.data && data.data.financeMainDetails);
	dataGrid.datagrid('showColumn',"garbage-colunms")	
	$('#b-add-expensesdtl').show();
	var rows=dataGrid.datagrid('getRows');
	for(var index in rows){
		dataGrid.datagrid('beginEdit',index);
	}
}
//报销明细只读模式
function showReadOnlyExpensesList(data){
	if(data && data.data && data.data.financeMainDetails){
		$("#expensesListWrapper").show();
		initExpensesWrapper();
		initExpensesDataGrid(data.data.financeMainDetails);
		hideDataGridColumns(dataGrid,['garbage-colunms']);
		$('#b-add-expensesdtl').hide();
	}else{
		//隐藏报销明细列表，防止出现异常的高度间距
		$("#expensesListWrapper").hide();
	}
}
//根据状态决定结算的状态显示
function showExpensesList(opt,data){
	if(opt.readOnly==true){
		showReadOnlyExpensesList(data);
	}else{
		showEditableExpensesList(data);
	}
}
//从后端获得的buttonPriv中得到按钮权限
function showPriv(priv){
	if(pmsPager.opt.data.pri.buttonPriv[priv]){
		return true;
	}else{
		return false;
	}
}
//初始化行政报销表单
function initFmpForm(fmpId){
	var $form=$('#form1');
	$.post(basePath+'finance/expenses/queryAdministrativeExpensesById.do',{id:fmpId},function(result){
		if(result && result.flag=='success'){
			var opt={
				form:$form,
				formFields:fmpFormFields,
				data:result
			};
			//设置权限控制映射
			Priv.map("showPriv('fin-workflow-show')","fin-workflow-show");
			Priv.map("showPriv('fin-close-readOnly')","fin-close-readOnly");
			Priv.map("showPriv('fin-close-edit')","fin-close-edit");
			Priv.map("showPriv('fin-tmpSave')","fin-tmpSave");
			Priv.map("showPriv('fin-submit')","fin-submit");
			Priv.map("showPriv('fin-del')","fin-del");
			Priv.map("showPriv('fin-approve')","fin-approve");
			Priv.map("showPriv('fin-workflow-del')","fin-workflow-del");
			Priv.map("showPriv('fin-approve-submitPaperMaterial')","fin-approve-submitPaperMaterial");
			Priv.map("showPriv('fin-approve-rvk')","fin-approve-rvk");
			Priv.map("showPriv('fin-print')","fin-print");
			
			pmsPager.init(opt);
			setTimeout(function(){
				if(result.data && result.data.fmstatus && result.data.fmstatus =="fmp_apply"){
					$("#inPageTitle").html("编辑行政报销");
				}
				//由于读取到的申请单id是applyId ，由于ihint组件如此只能写到隐藏域。
				$form.iForm("setVal",{"requestNoteName":result.data["applyName"],"creator":userName,
										"payeeOnly":result.data["payeeName"],"payeeOther":result.data["payeeName"],
										"payeeMore":result.data["payeeName"]});
				
				//由于只读状态下，没有执行type的onchange事件，导致表单的格式就没有按规则生成
				if(showPriv('fin-close-readOnly')){
					formElementDisplay(result.data["type"]);
				}
				//初始化流程实例ID
				$('#form1').iForm("setVal",{processInstId:opt.data.pri.workflow.processInstId});
				
				$("#pms-b-fma-view").click(function (){
					openViewFMA(opt.data.data.applyId);
				});
			},200);
			
		}else{
			FW.error(result.msg || "服务器出错，请重新打开页面");
		}
	})
}
//初始化其他数据 如附件和报销详情
function initOther(opt){
	var data=opt.data;
	var form=opt.form;
	//判断是否为提交纸质材料环节
	var isSubmitPaperMaterialStep = false;
	if(opt&&opt.data&&opt.data.pri&&opt.data.pri.workflow&&opt.data.pri.workflow.elements
			&&opt.data.pri.workflow.elements.__elementKey__){
		if("submit_paper_material"==opt.data.pri.workflow.elements.__elementKey__){
			isSubmitPaperMaterialStep = true;
		}
	}
	//附件表单初始化且赋初值
	if(!isSubmitPaperMaterialStep){
		initAttachForm(data.data.attachMap,$('#attachForm'),$('#attachFormWrapper'),opt.readOnly);
	}else{
		initAttachForm(data.data.attachMap,$('#attachForm'),$('#attachFormWrapper'),false);
		initPrint(data);
	}
	//根据后台返回的只读情况，只读/可编辑报销明细
	showExpensesList(opt,data);
	//给收款方和申请单ihint控件绑定值域
	initRemoteData();
}
function initPrint(data){
//初始化打印按钮
	var url = "http://" + window.location.host + "/timss-finance";
	var path1 = "__report=report/TIMSS2_FIN_002.rptdesign&fid=" + data.data.mainId
		+ "&url=" + url
		+ "&author=" + userId;
	var path2 = "__report=report/TIMSS2_FIN_FMA_001.rptdesign&id=" + data.data.applyId
	+ "&url=" + url
	+ "&author=" + userId
	+ "&siteid=" + data.data.siteid;
	//预览PDF并提供打印
	$("#fin-print").click(function(){
		FW.dialog("init",{
			src: fileExportPath+"preview?__format=pdf&" + path1,
			btnOpts:[{
				"name" : "关闭",
				"float" : "right",
			    "style" : "btn-default",
			    "onclick" : function(){
			    	_parent().$("#itcDlg").dialog("close");
			    }
			}],
			dlgOpts:{ width:800, height:650, closed:false, title:"报销单", modal:true }
		});
	});
	$("#fin-print-apply").click(function(){
		FW.dialog("init",{
			src:fileExportPath+"preview?__format=pdf&" + path2,
			btnOpts:[{
				"name" : "关闭",
				"float" : "right",
			    "style" : "btn-default",
			    "onclick" : function(){
			    	_parent().$("#itcDlg").dialog("close");
			    }
			}],
			dlgOpts:{ width:800, height:650, closed:false, title:"申请单", modal:true }
		})
	});
}
//提交行政报销
function submit(_this){
	//校验表单以及报销明细的数据表格
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
//启动行政报销--设置流程变量，弹出第一环节选人对话框
function startWorkFlow(data,taskId,processInstId){
	var parameters = {taskId:taskId,processInstId:processInstId,data:FW.stringify(data)};
    $.post(basePath+'fin/wf/setWFVariable.do',parameters,
    	function(result){
        	if(result && result.flag=='success'){
        		var workFlow = new WorkFlow();
            	forbidTipAfterCloseTab();
            	workFlow.showAudit(taskId,FW.stringify(data),closeTab,closeTab,null,null,0);
            }else{
            	FW.error(result.msg || "出错了，请重试");
            }
    });
}
//暂存行政报销
function tmpSave(){
	//校验表单以及报销明细的数据表格
	if(!validExpensesForm()){
		return false;
	}
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	//至少输入一条报销明细
	var expensesDtlData = getExpensesDtlListData(dataGrid); 
	if(0==expensesDtlData.length){
		FW.error("请输入报销明细");
		return false;
	}
	$.post(basePath+"finance/expenses/tmpSaveAdministrativeExpenses.do",{expenses:FW.stringify(data),expensesDtl:FW.stringify(expensesDtlData)},function(result){
		var contract = result.data["contract"];
		$("#form1").iForm('setVal',contract);
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,{tabOpen:false});
	});
}
//行政付审批流程的审批操作
function approve(){
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var taskId=null;
	var processInstId=null;
	var elementKey=null;
	if(pmsPager && pmsPager.opt && pmsPager.opt.data && pmsPager.opt.data.pri &&
			pmsPager.opt.data.pri.workflow ){
		taskId = pmsPager.opt.data.pri.workflow.taskId;
		processInstId=pmsPager.opt.data.pri.workflow.processInstId;
		elementKey=pmsPager.opt.data.pri.workflow.elements.__elementKey__;
	}
	var parameters = {taskId:taskId,processInstId:processInstId,data:FW.stringify(data)};
	if("administration_deputy_manager_approve"==elementKey){
		//分管行政副总经理审批 环节，需要选择下一步 是否需要提交给总经理再设置流程变量后审批
		selectNextStep(taskId,processInstId,data,parameters);
	}else{
		//设置流程变量并审批
		workFlowOperation(taskId,data,parameters,0);
	}
}
//行政付款审批流程 提交纸质材料后的审批（比普通的审批多了一个保存附件的过程）
function approveSubmitPaperMaterial(){
	//校验表单以及报销明细的数据表格
	if(!validExpensesForm()){
		return false;
	}
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	//onlyAttach：true 后台只保存附件
	$.post(basePath+"finance/expenses/tmpSaveAdministrativeExpenses.do",{onlyAttach:true,expenses:FW.stringify(data)},function(result){		
		//更新完毕后，执行审批
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,{successFunction:approve(),tabOpen:true});
	});
}
//行政付款审批流程 被退回到第一环节后的审批（比一般的审批多了一个保存的过程）
function approveRvk(){
	//校验表单以及报销明细的数据表格
	if(!validExpensesForm()){
		return false;
	}
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	//如果报销明细为空，不予保存
	var expensesDtlData = getExpensesDtlListData(dataGrid); 
	if(0==expensesDtlData.length){
		FW.error("请输入报销明细");
		return false;
	}
	$.post(basePath+"finance/expenses/tmpSaveAdministrativeExpenses.do",{expenses:FW.stringify(data),expensesDtl:FW.stringify(expensesDtlData)},function(result){
		var contract = result.data["contract"];
		$("#form1").iForm('setVal',contract);
		//更新完毕后，执行审批
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,{successFunction:approve(),tabOpen:true});
	});
}
//分管行政副总审批环节-选择下一个步操作
function selectNextStep(taskId,processInstId,data,parameters){
    var src = basePath + "finance/expenses/selectNextStep.do?index=0";
    var btnOpts = [{
            "name" : "确定",
            "style" : "btn-success",
            "onclick" : function(){
                var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
                var flag = p.$("#form1").iForm("getVal","flag");
                //param会让后台从variables中获取值，具体获取哪些key的值，则要从流程的属性设置的template项中定义
                _parent().$("#itcDlg").dialog("close"); 
                data["needSubmitToManager"] = flag;
        		var parameters = {taskId:taskId,processInstId:processInstId,data:FW.stringify(data)};
        		$.post(basePath+'fin/wf/setWFVariable.do',parameters,
        			function(result){
        				if(result && result.flag=='success'){
        					var workFlow = new WorkFlow();
        					forbidTipAfterCloseTab();
        				    workFlow.showAudit(taskId,FW.stringify(data),closeTab,closeTab,null,null,1);
        				}else{
        					FW.error(result.msg || "出错了，请重试");
        				}
        		});
            }
        }
    ];
    var dlgOpts = {
        width :360,
        height:200,
        title:"下一步操作"
    };
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
}
//设置流程变量并弹出工作流审批对话框
function workFlowOperation(taskId,data,parameters,multiSelect){
	$.post(basePath+'fin/wf/setWFVariable.do',parameters,
			function(result){
			if(result && result.flag=='success'){
				var workFlow = new WorkFlow();
				forbidTipAfterCloseTab();
				//暂时屏蔽流程终止功能
			    workFlow.showAudit(taskId,FW.stringify(data),closeTab,closeTab,null,null,multiSelect);
			}else{
				FW.error(result.msg || "出错了，请重试");
			}
		});
}
//获取审批流程详情
function showWorkflow(){
	var workFlow = new WorkFlow();
	if(pmsPager && pmsPager.opt && pmsPager.opt.data && pmsPager.opt.data.pri &&
			pmsPager.opt.data.pri.workflow ){
		var tempPri = pmsPager.opt.data.pri;
		var processInstId=tempPri.workflow.processInstId;
		var approvePri = tempPri.buttonPriv["fin-approve"];//是否有审批权限
		var approvePri2 = tempPri.buttonPriv["fin-approve-rvk"];
		var approvePri3 = tempPri.buttonPriv["fin-approve-submitPaperMaterial"];
		if(approvePri3){
			workFlow.showAuditInfo(processInstId,null,1,approveSubmitPaperMaterial);
		}else if(approvePri2){
			workFlow.showAuditInfo(processInstId,null,1,approveRvk);
		}else if(approvePri){
			workFlow.showAuditInfo(processInstId,null,1,approve);
		}else {
			workFlow.showAuditInfo(processInstId);
		}
		
	}
}
//删除行政报销
function del(id){
	FW.confirm("确定删除吗？删除后数据不能恢复",function(){
		$.post(basePath+'finance/expenses/deleteAdministrativeExpenses.do',{id:id},function(result){
			showBasicMessageFromServer(result,delSuccessMessage,delFailMessage);
		});
	});
}
//作废行政报销
function nullify(){
	var taskId=null;
	var processInstId=null;
	var id = null;
	if(pmsPager && pmsPager.opt && pmsPager.opt.data && pmsPager.opt.data.pri &&
			pmsPager.opt.data.pri.workflow ){
		taskId = pmsPager.opt.data.pri.workflow.taskId;
		processInstId=pmsPager.opt.data.pri.workflow.processInstId;
		id=pmsPager.opt.data.data.id;
	}
	if(!taskId||!processInstId||!id){
		return false;
	}
	var options={
		destUrl:basePath+"finance/expenses/voidFlowAdministrativeExpenses.do",
		businessId:id,
		processInstId:processInstId,
		taskId:taskId,
		tipMessage:"确认作废|确认作废该行政报销流程？"
	};
	voidFlow(options);
}
//显示所属申请单
function openViewFMA(id){
	var newId="finEditFMATab";
	var pageName="查看管理费用申请";
	var pageUrl=basePath + "finance/fma/editFMAJsp.do?id="+id+"&applyType=managementcostapply";
	var oldId=FW.getCurrentTabId();
	addEventTab( newId, pageName, pageUrl, oldId );
}