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
<title>添加发票</title>
<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/itc/invoice/invoice.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/itc/invoice/addInvoice.js?ver=${iVersion}"></script>
<script>
	var contractId=getUrlParam('contractId');
	
	$(document).ready(function() {
		if(contractId){
			
			$.post(basePath+"pms/contract/queryContractById.do",{id:contractId},function(data){
				if(data && data.data && data.data.payplanVos && data.data.payplanVos.length){
					$.fn.extend(payplanIdData,getPayplanData(data.data.payplanVos));
				}
				var opt={
						form:$("#form1"),
						formFields:invoiceFormFields,
						otherData:data,
						initOther:initOtherWithData
					};
				pmsPager.init(opt);
			});
		}
		
	});
	function initOtherWithData(opt){
		var form=opt.form;
		var value=opt.otherData;
		var formData={};
		if(value && value.data){
			value=value.data;
			formData={
				contractId:value.id,
				contractName:value.name,
				contractSum:value.totalSum,
				contractCode:value.contractCode
			};
		};
		form.iForm('setVal',formData);
		form.iForm('endEdit',['contractName','contractSum','contractCode']);
	}
	function submit(){
		if(!$("#form1").valid()){
			return ;
		}
		var data=$("#form1").iForm('getVal');
		//$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		$.post(basePath+'pms/invoice/insertInvoice.do',{"invoice":FW.stringify(data)},function(result){
			data.id=result && result.data && result.data.id;
			showBasicMessageFromServer(result,"新建成功","新建失败");
		});
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
	            <button type="button" class="btn btn-default" onclick="submit();">提交</button>
	        </div>
	        
	    </div>
	</div>
	<div class="inner-title">
		新建发票
	</div>

	<form id="form1" class="margin-form-title margin-form-foldable"></form>
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm" class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>