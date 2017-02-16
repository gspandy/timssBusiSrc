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
	var id="${id}";
	var defKey="finance_itc_managementcostapply";
	var pager={};
	var loginUserId = ItcMvcService.user.getUserId();
	//页面初始化
	$(document).ready(function() {
		$.ajax({
			   url:"finance/fma/queryFinanceManagementApplyById.do",
			   type:"post",
			   data:{id:id},
			   success:function(data){
				   pager.data=data;
				   initForm(data);
				   initPrint(data);
			   }
		});
	});
	
	function initForm(data){
		initButton(data);
		initFormField(data);
		initAttach(data);
		initDetailList(data);
	}
	
	function initButton(data){
		var buttonPri=data && data.pri && data.pri.buttons;
		for(var i in buttonPri){
			if(buttonPri[i]){
				Priv.map("true",i);
			}
		}
		Priv.apply();
		FW.fixToolbar("#toolbar1");
	}
	
	function initFormField(data){
		var $form=$("#form1");
		$form.iForm('init',{"fields":fmaFormFields,options:{validate:true}});
		$form.iForm('setVal',data.data);
		var editForm=data && data.pri && data.pri.formEdit;
		$form.iForm("endEdit");
		//根据editForm属性对表单字段设置
		if(editForm){
			if(editForm=='all'){
				$form.iForm("beginEdit");
			}else{
				$form.iForm("beginEdit",editForm);
			}
			
		}	
		//申请人和申请部门不可编辑
		$form.iForm("endEdit",["deptname","applyUsername"]);
		//判断总经理审批是否存在
		var needZJL=data && data.pri && data.pri.needZJL;
		//可以考虑删除，删除时需要把表单定义对应字段删除。
		if(needZJL){
			//如果金额数量过大，直接交总经理审批，否则可以选择是否总经理审批
			if(isMoneyLarge(data)){
				$form.iForm('hide','needZJL');
			}else{
				$form.iForm('beginEdit','needZJL');
			}
			
		}else{
			$form.iForm('hide','needZJL');
		}
		
		var needHQ=data && data.pri && data.pri.needHQ;
		if(needHQ){
			$form.iForm('beginEdit','needHQ');
		}else{
			$form.iForm('hide','needHQ');
		}
	}
	
	function isMoneyLarge(data){
		if(data && data.data && data.data.budget>10000){
			return true;
		}
	}
	
	function initAttach(data){
		if(isAttachEditable(data)){
			initAttachForm(data.data.attachMap,$('#attachForm'),$('#attachFormWrapper'),false);
		}else{
			initAttachForm(data.data.attachMap,$('#attachForm'),$('#attachFormWrapper'),true);
		}
	}
	function isAttachEditable(data){
		return data && data.pri && data.pri.attach=='Y';
	}
	
	function initDetailList(data){
		if(isDetailListEditable(data)){
			initDetailListForEdit(data.data.financeMainDetailVos);
		}else{
			initDetailListForRead(data.data.financeMainDetailVos);
		}
	}
	function isDetailListEditable(data){
		return data && data.pri && data.pri.editDetailList=='Y';
	}
	
	function initDetailListForEdit(data){
		initFMATable(data);
		if(data && data.length){
			var dataGrid=initFMATable.dataGrid;
			for(var i=0;i<data.length;i++){
				dataGrid.datagrid('beginEdit',i);
			}
		}
	}
	function initDetailListForRead(data){
		if(data && data.length){
			initFMATable(data);
			$("#b-add-fma").hide();
			var dataGrid=initFMATable.dataGrid;
			dataGrid.datagrid('hideColumn','garbage-colunms');
		}else{
			$("#b-add-fma").hide();
		}
		
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
		$.post(basePath+'finance/fma/updateFinanceManagementApplyAndStartWorkflow.do',{"financeManagementApply":FW.stringify(data),"details":FW.stringify(details)},function(result){
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
		$.post(basePath+'finance/fma/tmpUpdateFinanceManagementApply.do',{"financeManagementApply":FW.stringify(data),"details":FW.stringify(details)},function(result){
			showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage);
		});
	}
	
	function shenpi(){
		if(!valid()){
			return ;
		}
		var data=$('#form1').iForm('getVal');
		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		var taskId=getWorkflowTaskId();
		var processInstId=getProcessInstId();
		
		data.details=getDatagridData(initFMATable.dataGrid,true);
		
		$.post(basePath+'finance/fma/setVariables.do',{taskId:taskId,processInstId:processInstId,financeManagementApply:FW.stringify(data)},
			function(result){
			if(result && result.flag=='success'){
				openNewUserDialog(data);
			}else{
				FW.error(result.msg || "出错了，请重试");
			}
			
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
	
	function showWorkflow(){
		viewWorkFlow(id,defKey);
	}
	
	//删除按钮
	function del(){
		
		FW.confirm("确定删除吗？删除后数据不能恢复",function(){
			$.post(basePath+'finance/fma/delFMAById.do',{"id":id},function(result){
				
				showBasicMessageFromServer(result,delSuccessMessage,delFailMessage);
			});
		});
		
	}
	
	function initPrint(data){
		//初始化打印按钮
			var url = "http://" + window.location.host ;
		    var proc_inst_id=getProcessInstId();
			var commonPath = "__report=report/TIMSS2_FIN_FMA_001.rptdesign&id=" + id
				+ "&siteid=ITC&proc_inst_id="+proc_inst_id+"&author="+loginUserId;
			//预览PDF并提供打印
			$("#fin-print").click(function(){
				FW.dialog("init",{
					src: fileExportPath+"preview?__format=pdf&" + commonPath,
					btnOpts:[{
						"name" : "关闭",
						"float" : "right",
					    "style" : "btn-default",
					    "onclick" : function(){
					    	_parent().$("#itcDlg").dialog("close");
					    }
					}],
					dlgOpts:{ width:800, height:650, closed:false, title:"管理费用申请单", modal:true }
				});
			});
		}
	
	function zuofei(){
		var options={
			destUrl:basePath+"finance/fma/voidFlow.do",
			businessId:id,
			processInstId:getProcessInstId(),
			taskId:getWorkflowTaskId(),
			tipMessage:"确认作废|确认作废该管理费用审批流程？"
		};
		voidFlow(options);
	}
	
	function getProcessInstId(){
		return pager.data && pager.data.pri && pager.data.pri.processInstId;
	}
	
	function getWorkflowTaskId(){
		return pager.data && pager.data.pri && pager.data.pri.taskId;
	}
	
	//重新查看流程函数
	function showWorkFlow(){
		var workflow=new WorkFlow();
		var processInstId=getProcessInstId();
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
		    
		    var buttonPri=pager.data && pager.data.pri && pager.data.pri.buttons;
		    if(buttonPri["fin-shenpi"]){
		    	workflow.showAuditInfo(processInstId,JSON.stringify(businessData),1,shenpi,null);
		    }else{
		    	workflow.showAuditInfo(processInstId,JSON.stringify(businessData));
		    }
			
		}else{
			//显示流程图
			workflow.showDiagram(defKey);
		}
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
	            <button type="button" class="btn btn-default priv" privilege="fin-submit" onclick="tmpSave();">暂存</button>
	            <button type="button" class="btn btn-default priv" privilege="fin-submit" onclick="submit(this);" data-loading-text="提交">提交</button>
	            <button type="button" class="btn btn-default priv" privilege="fin-shenpi" onclick="shenpi();">审批</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default priv" privilege="fin-del" onclick="del();">删除</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default priv" privilege="fin-zuofei" onclick="zuofei();">作废</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" id="fin-print">打印</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="showWorkFlow();">审批信息</button>
	        </div>
	        
	    </div>
	</div>
	<div class="inner-title">
		管理费用申请详情
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