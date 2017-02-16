<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title>录入ERP</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />

<!-- 私有部分 -->

<script type="text/javascript">
    var erpForm=${erpForm};
    var erpTable=${erpTable};
	$(document).ready(function() {
		showERPForm(); //显示ERP表单
		showERPTable(); //显示ERP表格
		//loadERPData(); //加载数据到页面
	});
	
	var erpFormFields = [{
   		title : "凭证头描述",
   		id : "fCertHeadDesc",
   		rules: {
			required:true
   		}
	}, {
   		title : "凭证类别",
   		id : "fCertType",
   		type: "label"
	}, {
   		title : "子类",
   		id : "fSubType",
   		type: "label"
	}, {
   		title : "期间",
   		id : "fAccMonth",
   		type: "label"
	}, {
   		title : "记账日期",
   		id : "fAccDate",
   		type: "label"
	}, {
   		title : "币种",
   		id : "fCcy",
   		type: "label"
	}, {
   		title : "单据张数",
   		id : "fDocNbr",
   		type: "label"
	}, {
   		title : "报销单编号",
   		id : "fFid",
   		type: "hidden"
	}];
	
	//显示ERP表单
	function showERPForm() {
		$("#erpForm").iForm("init", {"options": {validate: true}, "fields": erpFormFields});
		//加载数据
		$("#erpForm").iForm("setVal", erpForm);
	}
	
	//ERP明细列定义
	var erpColumns = [[{
		field : 'id',
		title : 'id',
		width : 10,
		hidden : true
	}
	, {
		field : 'subject',
		title : '科目',
		fixed : true,
		editor: {
			type: "textarea"
		},
		width : 190
	}, {
		field : 'subjectremark',
		title : '科目描述',
		fixed : true,
		editor: {
			type: "textarea"
		},
		width : 250
	}, {
		field : 'debitamt',
		title : '借方金额(元)',
		align : 'right',
		fixed : true,
		editor: {
			type: "textarea"
		},
		width : 105
	}, {
		field : 'creditamt',
		title : '贷方金额(元)',
		align : 'right',
		fixed : true,
		editor: {
			type: "textarea"
		},
		width : 105
	}, {
		field : 'cashitem',
		title : '现金流项目',
		fixed : true,
		editor: {
			type: "textarea"
		},
		width : 90
	}, {
		field : 'intervalunit',
		title : '内部单位',
		fixed : true,
		editor: {
			type: "textarea"
		},
		width : 60
	}, {
		field : 'certrowdesc',
		title : '凭证行描述',
		editor: {
			type: "textarea"
		},
		width : 200
	}]];
	
	//显示ERP表格
	function showERPTable() {
		
		$("#accountDetail").iFold("init"); //显示标题
		$("#erpTable").datagrid({
			data:erpTable,
			resizeHandle: 'both',
			nowrap: false,
			fitColumns: true,
			singleSelect: true,
			collapsible: true,
			scrollbarSize: 0,
			columns: erpColumns
		});
		$("#erpTable").datagrid("beginEdit",0);
		$("#erpTable").datagrid("beginEdit",1);
		$("#erpTable").datagrid("beginEdit",2);
	} 
	
	//加载ERP数据到页面
	function loadERPData() {
		return true;
	}
	
	//验证ERP表单
	function validateERPForm(){
	    return true;
	}
	</script>
</head>

<body>
	
	<form id="erpForm"  class="margin-form-title margin-form-foldable"></form>
	
	<div  class="margin-group-bottom"  id="addInvoiceWrapper">
		<form id="accountDetail" grouptitle="会计分录" class="margin-title-table">
			<table id="erpTable" class="eu-datagrid"></table>
		</form>
	</div>
</body>
</html>
