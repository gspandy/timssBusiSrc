<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%
 	String path = request.getContextPath();
 	
 	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
 	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
 	String sessionid=session.getId();
 	String ctype=request.getParameter("ctype");
%>
<head>
<title>项目付款信息</title>

<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var loginUserId = ItcMvcService.user.getUserId();
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
var ctype="<%=ctype%>";
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/itc/pay/pay.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/itc/pay/editPay.js?ver=${iVersion}"></script>
<style>
#contractName{
cursor:pointer;
color:#236DA1;
}
#contractName:hover{
text-decoration:underline;
}
</style>
<script>
	var contractId=getUrlParam('contractId');
	var payplanId=getUrlParam('payplanId');
	var id=getUrlParam('id');
	var undoProcInstId='';
	var undoTaskId='';
	var undoApprove =false;
	var editFlag = '';
    Priv.map("hasPmsFlowVoidPrip()","pms-flow-void");
    Priv.map("hasApplyUndoPriv()","pms-apply-undo");
    Priv.map("hasAuditUndoPriv()","pms-audit-undo");
    Priv.map("hasPmsSendMessageToERP()","pms-sendMessageToERP");
    function hasPmsFlowVoidPrip(){
    	var data=pmsPager.opt.data.data;
    	if(data.status!='approving'){
    		return false;
    	}else{
    		if(pmsPager.hasPri(pmsPager.opt,"voidFlow")){
    			return true;
    		}
    	}
    }
        
    function hasPmsSendMessageToERP(){
    	var data=pmsPager.opt.data.data;
    	if(data.sendedtoerp==1){
    		//已经发送过凭证就不能再次发送了
    		return false;
    	}
    	if(!isApproving()){
    		//不是当前审批人就不显示
    		return false;
    	}
    	if(!pmsPager.hasPriInOpt("sendMessageToERP")){
    		//有发送凭证权限，来源于工作流配置
			return false;
		}
    	return true;
    }
    
    function hasApplyUndoPriv(){
    	var data=pmsPager.opt.data.data;
    	if(data.status!='approved'||data.type!='income'){
    		return false;
    	}else{
    		if(pmsPager.hasPri(pmsPager.opt,"applyUndo")){
    			return true;
    		}
    	}
    }
    function hasAuditUndoPriv(){
    	var data=pmsPager.opt.data.data;
    	if(data.status!='undoing'){
    		return false;
    	}else{
    		if(pmsPager.hasPri(pmsPager.opt,"auditUndo")){
    			return true;
    		}
    	}
    }
    function zuofei(){
    	var flowType=(ctype=='income')?"收款":"付款";
    	var options={
    		destUrl:basePath+"pms/pay/voidFlow.do",
    		businessId:pmsPager.opt.data.data.id,
    		processInstId:getWorkflowProcessInstId(),
    		taskId:getWorkflowTaskId(),
    		tipMessage:"确认作废|确认作废该"+flowType+"流程？"
    	};
    	voidFlow(options);
    }
	$(document).ready(function() {
		$.post(basePath+'pms/pay/queryPayByPayplanId.do?payplanId='+payplanId+'&contractId='+contractId,{
			id:id
		}
		,function(data){
			var $form=$("#form1");
			if(!contractId){
				var opt={
					form:$("#form1"),
					formFields:payFormFields
				};
				pmsPager.init(opt);
				
			}else{
				if(data && data.data && data.data.payplanVos && data.data.payplanVos.length){
					$.fn.extend(payplanIdData,getPayplanData(data.data.payplanVos));
				}
				initData(data && data.data);
				var opt={
					form:$("#form1"),
					formFields:payFormFields,
					data:data
					//otherData:data,
					//initOther:initOtherWithData
				};
				$.ajaxSetup({'async':false});
				editFlag = getModifiedValue(data.pri,'beginEdit');
				if(data.pri.readOnly == true && editFlag != 'all'){
				     data.data.actualpay = data.data.actualpay.toFixed(2) + '   ('+data.data.actualpayPercent+'%)';
				     if(data.data.bepay != null){
				         data.data.bepay = data.data.bepay.toFixed(2) + '   ('+data.data.bepayPercent+'%)';
				     }
	                 data.data.contractName = data.data.contractName + '(' + data.data.contractCode + ')';
				}
				pmsPager.init(opt);
				FW.showContractInfo("contractName");
				FW.showProjectInfo("projectName");
				//默认隐藏退票说明字段
				$("#form1").iForm("hide","undoRemark");
				//退票管理流程实例id
				undoProcInstId = data.data.undoFlowId; 
				undoTaskId = data.pri.workflow.undotaskId;
				//申请退票管理的审批按钮
				var pri=opt.data.pri;
				if(pri.workflow && pri.workflow.undoApply==true){
					$('#b-undo-apply').show();
				}
				//退票管理的审批按钮
				if(pri.workflow && pri.workflow.undoApprove==true){
					undoApprove = true;
					$('#b-undo-approve').show();
					$("#form1").iForm("show","undoRemark");
				}
				//退票管理的审批信息按钮
				if(pri.workflow && pri.workflow.undoApproveInfo==true){
					$('#b-undo-approve-info').show();
					$('#b-view-workflow').hide();
					$("#form1").iForm("show","undoRemark");
					$("#pms-pay-title").html("退票信息");
				}
				//退票管理的作废按钮
				if(pri.workflow && pri.workflow.undoApproveRollBack==true){
					$('#b-undo-approve-rollback').show();
					$("#form1").iForm("show","undoRemark");
					$("#form1").iForm("beginEdit","undoRemark");
				}
				FW.fixToolbar("#toolbar1");
				$.ajaxSetup({'async':true});
			}
		});
		
			
	});
	//退票--审批
	function approveUndo(){
		var taskId=undoTaskId;
		var processInstId=undoProcInstId;
		var data=$('#form1').iForm('getVal');
		data.invoice=getInvoiceListData();
		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		//先更新流程变量
		$.post(basePath+'pms/wf/setWFVariable.do',{taskId:taskId,processInstId:processInstId,data:FW.stringify(data)},
			function(result){
			if(result && result.flag=='success'){
				var workFlow = new WorkFlow();
				var taskId=undoTaskId;
				forbidTipAfterCloseTab();
			    workFlow.showAudit(taskId,FW.stringify(data),closeTab,closeTab,stopUndo,null,0);
			}else{
				FW.error(result.msg || "出错了，请重试");
			}
		});
	}
	
	//退票--流程终止函数
	function stopUndo(reason){
		var data=$('#form1').iForm('getVal');
		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		var processInstId=undoProcInstId;
		$.post(basePath+"pms/pay/stopUndoWorkflow.do",{pay:FW.stringify(data),processInstId:processInstId,reason:reason},function(result){
			showBasicMessageFromServer(result,"流程终止成功","流程终止失败",{successFunction:closeWorkflowWindow});
		});
	}
	
	//退票--审批信息
	function viewUndoWorkFlow(){
		var workflow=new WorkFlow();
		var processInstId=undoProcInstId;
		if(processInstId){
			var businessData={};
			var fields = [{
		        title : "创建时间", 
	            id : "createtime",
	            type : "label"
		    }];
		    var data={'createtime':FW.long2time(pmsPager.createTime)};
		    businessData['fields'] = fields;
		    businessData['data'] = data;
		    //是否是审批状态，流程信息对话下面显示审批按钮，否则不显示
		    if(undoApprove){
		    	workflow.showAuditInfo(processInstId,JSON.stringify(businessData),1,approveUndo,null);
		    }else{
		    	workflow.showAuditInfo(processInstId,JSON.stringify(businessData));
		    }
		}else{
			//显示流程图
			workflow.showDiagram("pms_itc_payundo");
		}
	}
	//退票--作废
	function rollBackUndo(){
		var options={
	    	destUrl:basePath+"pms/pay/voidUndoFlow.do",
	    	businessId:pmsPager.opt.data.data.id,
	    	processInstId:undoProcInstId,
	    	taskId:undoTaskId,
	    	tipMessage:"确认作废|确认作废该退票流程？"
	    };
	    voidFlow(options);
	}
	
	function initData(data){
		var bepayPercent=data.bepay /data.totalSum;
		data.bepayPercent=NumToFix2(bepayPercent*100);
		var actualpayPercent=data.actualpay /data.totalSum;
		data.actualpayPercent=NumToFix2(actualpayPercent*100);
		//将合同与合同编号合并显示，为合同名称、合作方添加超链接
		
	}
	
	function initOther(opt){
		var data=opt.data;
		var form=opt.form;
		form.iForm('endEdit',['contractName','projectName','totalSum','xmhzf']);
		if(opt.data.pri.readOnly == true && editFlag != 'all'){
		    form.iForm('hide',['contractCode','type','payplanId','bepayPercent','actualpayPercent']);
		}
		//初始化结算日期字段
		initFieldPayDate(form);
		initTitle(ctype);
		ctype= (ctype=="null")? (data && data.data && data.data.type) :ctype;
		preparePrint(data.data.id);
		showInvoice(opt,data);
		
		initAttachForm(data.data.attachMap,$('#attachForm'),$('#attachFormWrapper'),opt);
		initBlurEvent();
	}
	function initBlurEvent(){
		$('#f_bepay').blur(function(){
			calculatePercent("#f_bepay","#f_bepayPercent");
		});
		$('#f_actualpay').blur(function(){
			calculatePercent("#f_actualpay","#f_actualpayPercent");
		});
		$('#f_bepayPercent').blur(function(){
			calculatePay("#f_bepay","#f_bepayPercent");
		});
		$('#f_actualpayPercent').blur(function(){
			calculatePay("#f_actualpay","#f_actualpayPercent");
		});
	}
	
	function initFieldPayDate(form){
		var payDateInput=$("#f_payDate");
		if(pmsPager.getWorkflowValue("hasPayDate")){
			form.iForm('beginEdit','payDate');
		}
		else if(payDateInput && payDateInput.length && payDateInput[0].tagName.toLowerCase()=='input'){
		
				//form.iForm("hide","payDate");
			
		}
	};

	function submit(_this){
		if(!$("#form1").valid()){
			return ;
		}
		if(!$("#invoiceListWrapper").valid()){
			return false;
		}
		buttonLoading(_this);
		var data=$("#form1").iForm('getVal');

		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		var gridData=getInvoiceListData();
		$.post(basePath+'pms/pay/tmpUpdatePay.do',{"pay":FW.stringify(data),"invoice":FW.stringify(gridData)},function(result){
			
			showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,
					{successFunction:openNewUserDialogFirstInTab,tabOpen:true,data:data,resetId:_this});
		});
		
	}
	
	function tmpSave(){
		if(!$("#form1").valid()){
			return ;
		}
		if(!$("#invoiceListWrapper").valid()){
			return false;
		}
		var data=$("#form1").iForm('getVal');

		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		var gridData=getInvoiceListData();
		$.post(basePath+'pms/pay/tmpUpdatePay.do',{"pay":FW.stringify(data),"invoice":FW.stringify(gridData)},function(result){
			
			showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage);
		});
		
	}
	function del(){
		var id=pmsPager.opt.data.data.id;
		FW.confirm("确定删除吗？删除后数据不能恢复",function(){
			$.post(basePath+'pms/pay/deletePay.do',{id:id},function(result){
				
				showBasicMessageFromServer(result,delSuccessMessage,delFailMessage);
			});
		});
		
	}
	function shenpi(){
		if(!valid()){
			return ;
		}
		var data=$('#form1').iForm('getVal');
		//对合同名称、累计付款、本次付款的数据进行转换，因为在页面显示时附加了其他数据
		var cnIndex = data.contractName.lastIndexOf('(');
		var apIndex = data.actualpay.lastIndexOf('(');
		var bpIndex = data.bepay.lastIndexOf('(');
		if(cnIndex != -1){
		    data.contractName = data.contractName.substring(0,cnIndex);
		}
		if(apIndex != -1){
		    data.actualpay = data.actualpay.substring(0,apIndex);
		}
		if(bpIndex != -1){
		    data.bepay = data.bepay.substring(0,bpIndex);
		}
		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		data.invoice=getInvoiceListData();
		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		shenpiTemplate(data,stopWorkflow);
	}
	
	function valid(){
		var form=$("#form1");
		if(!form.valid()){
			return false;
		}
		var invoiceListWrapper = $("#invoiceListWrapper");
		if(!invoiceListWrapper.valid()){
		    return false;
		}
		if(pmsPager.getWorkflowValue("hasPayDate"))
		{
			var payDateVal=form.iForm('getVal').payDate;
			if(!payDateVal){
				FW.error("结算日期不能为空");
				return false;
			}
		}
		return true;
	}
	
	function getModifiedValue(pridata,key){
	    var modified=pridata && pridata.workflow && pridata.workflow.elements && pridata.workflow.elements.modifiable;
		if(modified){
			var modifiedJson=JSON.parse(modified);
			var result = modifiedJson;
			if(result && result[key]){
				return result[key];
			}
		}
		return null;
	}
	
	/**
	 * 流程终止函数
	 */
	function stopWorkflow(reason){
		var data=$('#form1').iForm('getVal');
		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		var processInstId=getWorkflowProcessInstId();
		$.post(basePath+"pms/pay/stopWorkflow.do",{pay:FW.stringify(data),processInstId:processInstId,reason:reason},function(result){
			showBasicMessageFromServer(result,"流程终止成功","流程终止失败",{successFunction:closeWorkflowWindow});
		});
	}
	
	function getInvoiceListData(){
		var res=null;
		if(dataGrid){
			var rows=dataGrid.datagrid('getRows');
			for(var i=0;i<rows.length;i++){
				dataGrid.datagrid('endEdit',i);
				
			}
			res=dataGrid.datagrid('getRows');
		}
		return res;
	}
	function addInvoice(){
		initInvoice();
		var row={};
		dataGrid.datagrid('appendRow',row);
		var rowindex=dataGrid.datagrid('getRowIndex',row);
		dataGrid.datagrid('beginEdit',rowindex);
	
		$('#b-add-invoice').html('继续添加发票');
	}
	
	function viewContract(){
		openTab(basePath+"pms/contract/editContractJsp.do?contractId="+contractId,
				'合同','pmsViewContractTab');
	}
	
	function preparePrint(id){
		var proc_inst_id=getWorkflowProcessInstId();
		var printFileName="TIMSS_PMS_PAY_001.rptdesign";
		var title="付款信息"
		if(ctype=='income'){
			printFileName='TIMSS_PMS_RECEIPT_001.rptdesign';
			title="收款信息";
		}
		var url=fileExportPath+"preview?__report=report/"+printFileName+"&__format=pdf&siteid=ITC&sheet_id="+id+"&proc_inst_id="+proc_inst_id;
		url=url+"&url="+url+"&author="+loginUserId;
		//__asattachment=true
		pmsPrintHelp("#pms-pay-print",url,title);
	}
	
	function initTitle(ctype){
		var typename="结算详情";
		/*
		if(ctype=='income'){
			typename="收款信息";
		}else{
			typename="付款信息";
		}
		*/
		$("#pms-pay-title").html(typename);
	}
	
	function openERPPage(){
		var src=basePath+"pms/erp/inputERPJSP.do?payId="+id;
		var btnOpts = [{
			"name" : "取消",
			"float" : "right",
			"style" : "btn-default",
			"onclick" : function() {
				return true;
			}
		},{
			"name" : "确定",
			"float" : "right",
			"style" : "btn-success",
			"onclick" : function() {
				var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
				var erpForm=conWin.$("#erpForm").iForm('getVal');
				var erpTable=conWin.$("#erpTable").datagrid('getRows');
				var buttonReturnFlag=false;
				$.ajax({
					type:"post",
					url:basePath+"pms/erp/sendReceiptMessageToErp.do",
					data:{
						erpForm:FW.stringify(erpForm),
						erpTable:FW.stringify(erpTable),
						payId:id
					},
					complete:function(res){
						var response=res.responseJSON;
						var result= response && response.result;
						
						if(result==1){
							//执行成功，则关掉对话框
							buttonReturnFlag=true;
							$("#b-pms-sendMessageToERP").hide();
						}else{
							//执行失败,则通知用户错误原因，同时不关闭对话框
							var msg=result && result.msg;
							if(msg){
								FW.error(msg);
							}else{
								FW.error("系统执行错误");
							}
						}
					},
					dataType:"json",
					async:false
				});
				return buttonReturnFlag;
			}
		}];
		
		//ERP对话框
		var erpDlgOpts = {
			width : 1000,
			height : 500,
			closed : false,
			title : "凭证预览",
			modal : true
		};

		FW.dialog("init", {
			"src" : src,
			"dlgOpts" : erpDlgOpts,
			"btnOpts" : btnOpts
		});
	}
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm  ">
	        	<button type="button" class="btn btn-default pms-readOnly"  onclick="closeTab();">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm  ">
	            <button type="button" class="btn btn-default pms-editOnly"  onclick="closeTab();">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm  ">
	            <button type="button" class="btn btn-default pms-button" onclick="tmpSave();" id="b-tmp-save">暂存</button>
	            <button type="button" class="btn btn-default pms-button" onclick="submit(this);" id="b-save" data-loading-text="提交">提交</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-button" onclick="del();"  id="b-del">删除</button>
	        </div>
	        <div class="btn-group btn-group-sm"   >
	            <button type="button" class="btn btn-default priv" privilege="pms-flow-void" onclick="zuofei();" id="b-pms-void-flow">作废</button>
	        </div>
	        <div class="btn-group btn-group-sm " >
	            <button type="button" class="btn btn-default" style="display:none;"  id="b-undo-approve-rollback" onclick="rollBackUndo();">作废</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="shenpi();"  style="display:none;"  id="b-approve">审批</button>
	        </div>
	        <div class="btn-group btn-group-sm " >
	            <button type="button" class="btn btn-default" style="display:none;"  id="b-undo-approve" onclick="approveUndo();">审批</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default priv" privilege="pms-sendMessageToERP" id="b-pms-sendMessageToERP" onclick="openERPPage();">生成凭证</button>
	        </div>
	        <div class="btn-group btn-group-sm " >
	            <button type="button" class="btn btn-default" style="display:none;"  id="b-undo-apply" onclick="applyUndo();">申请退票</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" id="pms-pay-print">打印</button>
	            
	        </div>
	        <div class="btn-group btn-group-sm " >
	            <button type="button" class="btn btn-default" style="display:none;"  id="b-undo-approve-info" onclick="viewUndoWorkFlow();">审批信息</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" id="b-view-workflow" onclick="viewWorkFlow();">审批信息</button>
	        </div>
			
	    </div>
	</div>
	<div class="inner-title" id="pms-pay-title">
		结算详情
	</div>

	<form id="form1"  class="margin-form-title margin-form-foldable"></form>
	<div  class="margin-group-bottom" style="display:none;" id="addInvoiceWrapper">
		<form id="invoiceListWrapper" grouptitle="发票信息" class="margin-title-table">
			<table id="invoiceList" class="eu-datagrid"></table>
		</form>
        <div class="btn-toolbar margin-foldable-button">
			<div class="btn-group btn-group-xs" >
				 <button type="button" class="btn btn-success" onclick="addInvoice();" id="b-add-invoice">添加发票</button>
			</div>
		</div>
        
	</div>
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm"  class="margin-form-title margin-form-foldable"></form>
	</div>
	
</body>
</html>