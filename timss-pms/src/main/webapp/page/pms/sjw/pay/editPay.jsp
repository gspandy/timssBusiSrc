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

<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var loginUserId = ItcMvcService.user.getUserId();
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
var ctype="<%=ctype%>";
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/pay/pay.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/pay/editPay.js?ver=${iVersion}"></script>
<script>
	var contractId=getUrlParam('contractId');
	var payplanId=getUrlParam('payplanId');
	var id=getUrlParam('id');
	var isEditable = false;
	$(document).ready(function() {
		$.post(basePath+'pms/pay/queryPayByPayplanId.do?payplanId='+payplanId+'&contractId='+contractId,{id:id}
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
				};
				pmsPager.init(opt);
				FW.showContractInfo("contractName");
				FW.showProjectInfo("projectName");
			}
		});
	});
	
	function initData(data){
		var bepayPercent=data.bepay /data.totalSum;
		data.bepayPercent=NumToFix2(bepayPercent*100);
		var actualpayPercent=data.actualpay /data.totalSum;
		data.actualpayPercent=NumToFix2(actualpayPercent*100);
	}
	
	function initOther(opt){
		var data=opt.data;
		var form=opt.form;
		form.iForm('endEdit',['projectName','contractName','totalSum','xmhzf','type','contractCode','paySpNo']);
		initTitle(ctype);
		ctype= (ctype=="null")? (data && data.data && data.data.type) :ctype;
		if("income"==ctype){
			$("#pms-pay-print").hide();
			FW.fixToolbar("#toolbar1");
		}
		preparePrint(data.data.id);
		showInvoice(opt,data);
		initAttachForm(data.data.attachMap,$('#attachForm'),$('#attachFormWrapper'),opt);
		initBlurEvent();
		if(false==data.pri.readOnly){
				isEditable = true;
		}
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
	//作废
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
		if(!$("#form1").valid()){
			return ;
		}
		if(!$("#invoiceListWrapper").valid()){
			return false;
		}
		var data=$('#form1').iForm('getVal');
		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		data.invoice=getInvoiceListData();
		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		if(isEditable){
			if(!$("#form1").valid()||!$("#invoiceListWrapper").valid()){
				return ;
			}
			var data=$("#form1").iForm('getVal');
			$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
			var gridData=getInvoiceListData();
			$.post(basePath+'pms/pay/tmpUpdatePay.do',{"pay":FW.stringify(data),"invoice":FW.stringify(gridData)},function(result){	
				var taskId=getWorkflowTaskId(null);
				var processInstId=getWorkflowProcessInstId(null);
				$.post(basePath+'pms/wf/setWFVariable.do',{taskId:taskId,processInstId:processInstId,data:FW.stringify(data)},
					function(result){
					if(result && result.flag=='success'){
						var workFlow = new WorkFlow();
						var taskId=getWorkflowTaskId(null);
						forbidTipAfterCloseTab();
					    workFlow.showAudit(taskId,FW.stringify(data),closeTab,closeTab,null,null,0,resetEdit);
					}else{
						FW.error(result.msg || "出错了，请重试");
					}
				});
			});
		}else{
			shenpiTemplate(data,stopWorkflow);
		}
	}
	
	function resetEdit(){
		var rows = $("#invoiceList").datagrid("getRows");
		for(var i=0;i<rows.length;i++){
			$("#invoiceList").datagrid('beginEdit',i);
		}
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
		var contractId = $("#form1").iForm("getVal","contractId");
		var printFileName="TIMSS2_SJW_PMS_ACCOUNTSYF_001.rptdesign";
		var title="付款信息"
		if(ctype=='income'){
			title="收款信息";
		}
		var url=fileExportPath+"preview?__report=report/"+printFileName+"&__format=pdf&payId="+id+"&contractId="+contractId;
		pmsPrintHelp("#pms-pay-print",url,title);
	}
	
	function initTitle(ctype){
		var typename="结算详情";
		$("#pms-pay-title").html(typename);
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
	            <button type="button" class="btn btn-default pms-button" onclick="zuofei();" id="pms-flow-void">作废</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="shenpi();"  style="display:none;"  id="b-approve">审批</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" id="pms-pay-print">打印</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="viewWorkFlow();">审批信息</button>
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