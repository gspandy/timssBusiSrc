<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.Constant"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%
 	
 	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
 	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
 	String sessionid=session.getId();
%>
<head>
<title>工作票列表</title>
<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sfc/checkout/checkout.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sfc/checkout/addCheckout.js?ver=${iVersion}"></script>
<script>
	var contractId=getUrlParam('contractId');
	var defKey="pms_sfc_checkout";

	//页面初始化
	$(document).ready(function() {
		var $form=$("#form1");
		
		if(!contractId){
			var opt={
				form:$("#form1"),
				formFields:checkoutFormFields,
				initOther:initOtherWithoutContract//完全新建验收
			};
			//页面初始化
			pmsPager.init(opt);
			
		}else{
			//从合同新建验收
			$.post(basePath+'pms/checkout/queryCheckoutByContractId.do?contractId='+contractId,{},function(data){
				//处理合同的结算计划信息
				if(data && data.data && data.data.payplanVos && data.data.payplanVos.length){
					$.fn.extend(payplanIdData,getPayplanData(data.data.payplanVos));
				}
				var opt={
					form:$("#form1"),
					formFields:checkoutFormFields,
					otherData:data,
					initOther:initOtherWithData 
				};
			    //页面初始化
				pmsPager.init(opt);
			});
			
		};
	});
	//从合同新建验收处理
	function initOtherWithData(opt){
		var data=opt.otherData;
		var form=opt.form;
		
		if(data && data.flag=='success'){
			var result=data.data;
			form.iForm("setVal",result);
			//隐藏总经理审批字段
			form.iForm("hide","zjlsp");
			form.iForm('endEdit',['projectName','contractName','projectLeader','xmhzf','hzffzr']);
			//附件处理
			initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
		}
	}
	//提交按钮处理
	function submit(_this){
		if(!$("#form1").valid()){
			return ;
		}
		buttonLoading(_this);
		var data=$("#form1").iForm('getVal');
		data.id=null;
		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		$.post(basePath+'pms/checkout/tmpInsertCheckout.do',{"checkout":FW.stringify(data)},function(result){
			data.id=result && result.data && result.data.id;
			showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,
					{successFunction:openNewUserDialogFirstInTab,tabOpen:true,data:data,resetId:_this});
		});
		
	}
	//显示流程图
	function showWorkflow(){
		
		var workFlow = new WorkFlow();
	    workFlow.showDiagram(defKey);
	}
	//暂存
	function tmpSave(){
		if(!$("#form1").valid()){
			return ;
		}
		var data=$("#form1").iForm('getVal');
		data.id=null;
		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		$.post(basePath+'pms/checkout/tmpInsertCheckout.do',{"checkout":FW.stringify(data)},function(result){
			data.id=result && result.data && result.data.id;
			showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage);
		});
	}
	//完全新建验收处理
	function initOtherWithoutContract(opt){
		var form=opt.form;
		form.iForm("hide","zjlsp");
		//初始化所属合同字段，使其能够搜索到所有合同
		initRemoteData(form);
		initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
	}
	//初始化所属合同字段
	function initRemoteData(form){
		var $contractInput = $('#f_contractName');
    	var contractInit = {
    		datasource : basePath + "pms/contract/queryContractByKeyWord.do",
    		clickEvent : function(id, name) {
    			$contractInput.val(name);
    			//设置所属的结算计划字段
    			$.post(basePath+"pms/contract/queryContractById.do",{id:id},function(data){
    				var result=data.data;
    				var values={
    					firstParty:result.secondParty,
    					fpLeader:result.spLeader,
    					projectName:result.projectName,
    					contractName:result.contractName,
    					budget:result.totolSum,
    					belongTO:result.belongTo,
    					contractId:id
    				}
    				form.iForm("setVal", values);
    			});
    		}
    	};
    	//使用前端iHint组件
    	$contractInput.iHint('init', contractInit);
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
		新建验收
	</div>

	<form id="form1"  class="margin-form-title margin-form-foldable"></form>
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm"  class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>