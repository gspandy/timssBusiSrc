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
<title>新增管理费用申请</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}/js/finance/core/fma/common.js?ver=${iVersion}"></script>
<script src="${basePath}/js/finance/core/fma/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}/js/finance/core/fma/fma.js?ver=${iVersion}"></script>
<script>
	
	var defKey="finance_itc_managementcostapply";
	var applyUserId='${applyuserid}';
	var applyName='${applyname}';
	var deptname='${deptname}';
	var deptid='${deptid}';
	//页面初始化
	$(document).ready(function() {
		var $form=$("#form1");
		$form.iForm('init',{"fields":fmaFormFields,options:{validate:true}});
		$form.iForm("hide","needZJL");
		$form.iForm("hide","needHQ");

		initFMATable([]);
		//附件处理
		initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
		//处理申请人和申请部门框
		initApplyUserAndDept($form);
		
		
	});
	function initApplyUserAndDept($form){
		$form.iForm('setVal',{applyType:"managementcostapply",applyUsername:applyName,applyUser:applyUserId,deptid:deptid,deptname:deptname});
		$form.iForm('endEdit',["applyUsername","deptname"]);
		$form.iForm('hide',"applyType");
		
	}
	//显示流程图
	function showWorkflow(){
		
		var workFlow = new WorkFlow();
	    workFlow.showDiagram(defKey);
	}
	function submit(_this){
		if(!valid()){
			return ;
		}
		buttonLoading(_this);
		var data=$("#form1").iForm('getVal');
		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		var details=getDatagridData(initFMATable.dataGrid,false);
		$.post(basePath+'finance/fma/insertFinanceManagementApplyAndStartWorkflow.do',{"financeManagementApply":FW.stringify(data),"details":FW.stringify(details)},function(result){
			data.id=result && result.data && result.data.id;
			data.details=details;
			showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,
					{successFunction:openNewUserDialogFirstInTab,tabOpen:true,data:data,resetId:_this});
		});
		

	}


	function tmpSave(_this){
		if(!valid()){
			return ;
		}
		var data=$("#form1").iForm('getVal');
		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		var details=getDatagridData(initFMATable.dataGrid,false);
		$.post(basePath+'finance/fma/tmpInsertFinanceManagementApply.do',{"financeManagementApply":FW.stringify(data),"details":FW.stringify(details)},function(result){
			showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage);
		});
	}
	function valid(){
		if(!$("#form1").valid()){
			return false;
		}
		if(!validDatagrid("#fmaList")){
			return false;
		}
		//明细总费用不能大于form表单里面的费用
		var budget=$("#form1").iForm('getVal').budget;
		var details=getDatagridData(initFMATable.dataGrid,false);
		var amount = 0;
		for(var i = 0 ; i < details.length; i++){
			amount += parseFloat(details[i].amount);
		}
		if(amount > budget){
			FW.error("明细的总金额大于申请费用"+budget);
			return false;
		}
		return true;
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
	            <button type="button" class="btn btn-default" onclick="submit(this);">提交</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="showWorkflow();">审批信息</button>
	        </div>
	        
	    </div>
	</div>
	<div class="inner-title">
		新建管理费用申请
	</div>
   
	<form id="form1"  class="margin-form-title margin-form-foldable"></form>
	
	 <div id="fmaWrapper" class="margin-group-bottom" >
		<form id="fmaList" grouptitle="申请明细" class="margin-title-table">
			 <table id="fmaTable" class="eu-datagrid">
		    </table>
		</form>
		<div class="btn-toolbar margin-foldable-button">
			<div class="btn-group btn-group-xs" >
				 <button type="button" class="btn btn-success" onclick="addFMA();" id="b-add-fma">添加</button>
			</div>
		</div>
	</div>
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm"  class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>