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
<title>工作票列表</title>

<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
var ctype="<%=ctype%>";
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/pay/pay.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/pay/addPay.js?ver=${iVersion}"></script>
<script>
	var contractId=getUrlParam('contractId');
	var payplanId=getUrlParam("payplanId");
    function initFormWithoutContract(){
    	var opt={
			form:$("#form1"),
			formFields:payFormFields,
			initOther:initOtherWithoutContract
		};
		pmsPager.init(opt);
    }
    
    function initOtherWithoutContract(opt){
    	var form=opt.form;
    	initRemoteData(form);
		initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
		specialDisplaySetting();
    }
    //初始化表单，使其能够获取远程合同数据
    function initRemoteData(form){
    	var $contractInput = $('#f_contractName');
    	var contractInit = {
    		datasource : basePath + "pms/contract/queryContractByKeyWord.do?type=pay",
    		clickEvent : function(id, name) {
    			$contractInput.val(name);
    			$.post(basePath+"pms/pay/queryPayByContractId.do",{contractId:id},function(data){
    				payplanIdData = [];
    				if(data && data.data && data.data.payplanVos && data.data.payplanVos.length){
    					$.fn.extend(payplanIdData,getPayplanData(data.data.payplanVos));
    				}
    				pmsPager.totalSum=data.data.totalSum;
    				pmsPager.otherData=data.data.payplanVos;
    				form.iForm("setVal", data.data);
    				$("#f_payplanId").iCombo("loadData",payplanIdData);
    				form.iForm("setVal", {"payplanId":""});
    				showInvoiceByCtype(data.data.type);
    				initEvent();
    			});
    		}
    	};
    	$contractInput.iHint('init', contractInit);
    }
	
	$(document).ready(function() {
		var $form=$("#form1");
		if(!contractId){
			initFormWithoutContract();
		}else if(!payplanId){
			$.post(basePath+"pms/pay/queryPayByContractId.do",{contractId:contractId},function(data){
				if(data && data.data && data.data.payplanVos && data.data.payplanVos.length){
					$.fn.extend(payplanIdData,getPayplanData(data.data.payplanVos));
				}
				var opt={
						form:$("#form1"),
						formFields:payFormFields,
						otherData:data,
						initOther:initOtherWithData
					};
				pmsPager.init(opt);
			});
		}else{
			initFormWithContractAndPayPlanId();
		}
	});
	function initOtherWithData(opt){
		var data=opt.otherData;
		var form=opt.form;
		if(data && data.flag=='success'){
			var result=data.data;
			//防止清空默认的本次付款值
			delete result.actualpay;
			delete result.actualpayPercent;
			form.iForm("setVal",result);
			form.iForm('endEdit',['projectName','contractName','totalSum','xmhzf','type','contractCode']);
			showInvoiceByCtype(ctype);
			initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
			pmsPager.totalSum=data.data.totalSum;
			initEvent();
		}
		specialDisplaySetting();
	}
	
	function initFormWithContractAndPayPlanId(){
		$.post(basePath+"pms/pay/queryPayByContractId.do",{contractId:contractId},function(data){
			if(data && data.data && data.data.payplanVos && data.data.payplanVos.length){
				$.fn.extend(payplanIdData,getPayplanData(data.data.payplanVos));
			}
			var opt={
					form:$("#form1"),
					formFields:payFormFields,
					otherData:data,
					initOther:initOtherWithPayPlanData
				};
			pmsPager.init(opt);
		});
	}
	
	function initOtherWithPayPlanData(opt){
		var data=opt.otherData;
		var form=opt.form;
		if(data && data.flag=='success'){
			var result=data.data;
			//防止清空默认的本次付款值
			delete result.actualpay;
			delete result.actualpayPercent;
			result.payplanId=payplanId;
			form.iForm("setVal",result);
			form.iForm('endEdit',['projectName','contractName','totalSum','xmhzf','type','payplanId','contractCode']);
			showInvoiceByCtype(result.type);
			initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
			pmsPager.totalSum=data.data.totalSum;
			initEvent();
		}
		specialDisplaySetting();
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
		$.ajax({
			url:basePath+'pms/pay/tmpInsertPay.do',
			type:"POST",
			dataType:"json",
			data:{"pay":FW.stringify(data),"invoice":FW.stringify(gridData)},
			success:function(result){
				data.id=result && result.data && result.data.id;
				showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,
						{successFunction:openNewUserDialogFirstInTab,tabOpen:true,data:data,resetId:_this});
			},
			error:function(XMLHttpRequest, textStatus, thrownError){
				if(XMLHttpRequest && XMLHttpRequest.responseJSON && XMLHttpRequest.responseJSON.msg){
					FW.error(XMLHttpRequest.responseJSON.msg);
				}
			}
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
		$.ajax({
			url:basePath+'pms/pay/tmpInsertPay.do',
			type:"POST",
			dataType:"json",
			data:{"pay":FW.stringify(data),"invoice":FW.stringify(gridData)},
			success:function(result){
				data.id=result && result.data && result.data.id;
				showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage);
			},
			error:function(XMLHttpRequest, textStatus, thrownError){
				if(XMLHttpRequest && XMLHttpRequest.responseJSON && XMLHttpRequest.responseJSON.msg){
					FW.error(XMLHttpRequest.responseJSON.msg);
				}
			}
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
	
	function showWorkflow(){
		var defKey;
		if(ctype=="cost"){
			defKey="pms_sjw_pay";
		}else{
			defKey="pms_sjw_receipt";
		}
		var workFlow = new WorkFlow();
	    workFlow.showDiagram(defKey);
	}
	function showInvoiceByCtype(ctype){
		if(ctype=='cost'){
			$('#addInvoiceWrapper').show();
			if(!showInvoiceByCtype.first){
				showInvoiceByCtype.first=true;
				$("#invoiceListWrapper").iFold();
				initInvoice();
			}
		}else{
			$('#addInvoiceWrapper').hide();
		}
	}
	
	function addInvoice(){
		initInvoice();
		var row={};
		dataGrid.datagrid('appendRow',row);
		var rowindex=dataGrid.datagrid('getRowIndex',row);
		dataGrid.datagrid('beginEdit',rowindex);
		$('#b-add-invoice').html('继续添加发票');
	}
	
	function specialDisplaySetting(){
		$("#form1").iForm('endEdit',['projectName','totalSum','xmhzf','type','contractCode','paySpNo']);
		$("[fieldId='projectName']").show();
		$("[fieldId='totalSum']").show();
		$("[fieldId='xmhzf']").show();
		$("[fieldId='type']").show();
		$("[fieldId='contractCode']").show();
	}
	
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="closeTab();">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="tmpSave();">暂存</button>
	            <button type="button" class="btn btn-default" onclick="submit(this);" data-loading-text="提交">提交</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="showWorkflow();">审批信息</button>
	        </div>
	    </div>
	</div>
	<div class="inner-title">
		新建结算
	</div>
	<form id="form1" class="margin-form-title margin-form-foldable"></form>
	<div  class="margin-group-bottom" style="display:none;" id="addInvoiceWrapper">
		<form id="invoiceListWrapper" grouptitle="发票信息" class="margin-title-table">
			<table id="invoiceList" class="eu-datagrid"></table>
		</form>
        <div class="btn-toolbar margin-foldable-button">
			<div class="btn-group btn-group-xs">
				 <button type="button" class="btn btn-success" onclick="addInvoice();" id="b-add-invoice">添加发票</button>
			</div>
		</div>
	</div>
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm" class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>