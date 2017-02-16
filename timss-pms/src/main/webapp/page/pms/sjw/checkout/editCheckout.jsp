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
%>
<head>
<title>验收信息</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
var loginUserId = ItcMvcService.user.getUserId();
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/checkout/checkout.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/checkout/editCheckout.js?ver=${iVersion}"></script>
<script>
	var contractId=getUrlParam('contractId');
	var payplanId=getUrlParam('payplanId');
	$(document).ready(function() {
		$.post(basePath+'pms/checkout/queryCheckoutByPayplanId.do?payplanId='+payplanId+'&contractId='+contractId,{}
		,function(data){
			var $form=$("#form1");
			if(!contractId){
				var opt={
					form:$("#form1"),
					formFields:checkoutFormFields
				};
				pmsPager.init(opt);
				
			}else{
				if(data && data.data && data.data.payplanVos && data.data.payplanVos.length){
					$.fn.extend(payplanIdData,getPayplanData(data.data.payplanVos));
				}
				var opt={
					form:$("#form1"),
					formFields:checkoutFormFields,
					data:data
					//otherData:data,
					//initOther:initOtherWithData
				};
				pmsPager.init(opt);
				FW.showProjectInfo("projectName");
				FW.showContractInfo("contractName");
			}
		});
		
			
	});
	function initOther(opt){
		var data=opt.data;
		var form=opt.form;
		setPmsPagerId(data);
		
		if(opt.showForm){
			form.iForm('beginEdit',"zjlsp");
			
		}else{
			form.iForm('hide',"zjlsp");
		}
		//初始化打印按钮
		preparePrint();
		form.iForm('endEdit',['projectName','contractName','projectLeader','payplanId','xmhzf','hzffzr']);
		initAttachForm(data.data.attachMap,$('#attachForm'),$('#attachFormWrapper'),opt);
	}
	function shenpi(){
		if(!$("#form1").valid()){
			return ;
		}
		var data=$('#form1').iForm('getVal');
		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		shenpiTemplate(data,stopWorkflow);
	}
	/**
	 * 流程终止函数
	 */
	function stopWorkflow(reason){
		var data=$('#form1').iForm('getVal');
		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		var processInstId=getWorkflowProcessInstId();
		$.post(basePath+"pms/checkout/stopWorkflow.do",{checkout:FW.stringify(data),processInstId:processInstId,reason:reason},function(result){
			showBasicMessageFromServer(result,"流程终止成功","流程终止失败",{successFunction:closeWorkflowWindow});
		});
	}
	function submit(_this){
		if(!$("#form1").valid()){
			return ;
		}
		buttonLoading(_this);
		var data=$("#form1").iForm('getVal');

		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		$.post(basePath+'pms/checkout/tmpUpdateCheckout.do',{"checkout":FW.stringify(data)},function(result){
			
			showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,
					{successFunction:openNewUserDialogFirstInTab,tabOpen:true,data:data,resetId:_this});
		});
		
	}
	
	function tmpSave(){
		if(!$("#form1").valid()){
			return ;
		}
		var data=$("#form1").iForm('getVal');

		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		$.post(basePath+'pms/checkout/tmpUpdateCheckout.do',{"checkout":FW.stringify(data)},function(result){
			
			showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage);
		});
		
	}
	function del(){
		var id=pmsPager.opt.data.data.id;
		FW.confirm("确定删除吗？删除后数据不能恢复",function(){
			$.post(basePath+'pms/checkout/deleteCheckout.do',{"id":id},function(result){
				
				showBasicMessageFromServer(result,delSuccessMessage,delFailMessage);
			});
		});
		
	}
	
	function openNewPpayTab(){
		//var contractId=$('#f_id').val();
		$("#pms-b-pay-add").hide();
		openTab('pms/pay/insertPayJsp.do?contractId='+contractId+"&payplanId="+payplanId,'结算','pmsNewPayTab',canOpenMutiTab);
	}
	
	function print(){
		var proc_inst_id=getWorkflowProcessInstId();
		var id=getPmsPagerId();
		var url=fileExportPath+"preview?__report=/report/TIMSS_PMS_SJW_CHECKOUT_001.rptdesign&__format=doc&siteid=ITC&sheet_id="+id+"&proc_inst_id="+proc_inst_id;
		url=url+"&url="+url+"&author="+loginUserId;
		window.location.href=url;
	}
	function preparePrint(){
		var proc_inst_id=getWorkflowProcessInstId();
		var id=getPmsPagerId();
		
		var url=fileExportPath+"preview?__report=/report/TIMSS_PMS_SJW_CHECKOUT_001.rptdesign&__format=pdf&siteid=ITC&sheet_id="+id+"&proc_inst_id="+proc_inst_id;
		url=url+"&url="+url+"&author="+loginUserId;
		pmsPrintHelp("#pms-checkout-print",url,"验收信息");
	}
	
	function viewContract(){
		openTab(basePath+"pms/contract/editContractJsp.do?contractId="+contractId,
				'合同','pmsViewContractTab');
	}
	
	//作废
	function zuofei(){
    	var options={
    		destUrl:basePath+"pms/checkout/voidFlow.do",
    		businessId:pmsPager.opt.data.data.id,
    		processInstId:getWorkflowProcessInstId(),
    		taskId:getWorkflowTaskId(),
    		tipMessage:"确认作废|确认作废该验收流程？"
    	};
    	voidFlow(options);
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
	        <div class="btn-group btn-group-sm" >
	            <button type="button" class="btn btn-default" onclick="shenpi();" style="display:none;"  id="b-approve">审批</button>
	        </div>
	        <div class="btn-group btn-group-sm "  >
	            <button type="button" class="btn btn-default pms-complex-privilege"  id="pms-b-pay-add"
	               onclick="openNewPpayTab();" >新建结算</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" id="pms-checkout-print">打印</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="viewWorkFlow();">审批信息</button>
	        </div>
			</div>
	</div>
	<div class="inner-title">
		验收详情
	</div>

	<form id="form1" class="margin-form-title margin-form-foldable"></form>
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm" class="margin-form-title margin-form-foldable"></form>
	</div>
	
</body>
</html>