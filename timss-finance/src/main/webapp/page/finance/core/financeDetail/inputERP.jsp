<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>录入ERP</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />

<!-- 私有部分 -->
<script type="text/javascript" src="${basePath}js/finance/common/financeUtilJS.js?ver=${iVersion}"></script>

<script type="text/javascript">
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
		//var beneficiaryStr = FW.get("beneficiaryStr");
		//var beneficiaryArr = beneficiaryStr.split(",");
		//var creatorname = FW.get("creatorname");
		
		var fid = FW.get("fid");
		var fDocNbr = FW.get("fDocNbr");
		var getReimbursementMan = FW.get("getReimbursementMan");
		
		$("#erpForm").iForm("init", {"options": {validate: true}, "fields": erpFormFields});
		
		var fCertHeadDesc = "付" + getReimbursementMan + "报销款";
		var curMonth = getCurrMonth();
		var curDate = getCurrDate();
		
		//加载数据
		$("#erpForm").ITC_Form("loaddata", {
			"fCertHeadDesc": fCertHeadDesc,
			"fCertType": "记帐凭证", 
			"fSubType": "收付转",
			"fAccMonth": curMonth,
			"fAccDate": curDate,
			"fFid": fid,
			"fCcy": "CNY",
			"fDocNbr": fDocNbr
		});
		
		//加载数据
		/*
		$("#erpForm").ITC_Form("loaddata", {
			"fCertHeadDesc": "付刘思颖报销款",
			"fCertType": "记帐凭证", 
			"fSubType": "收付转",
			"fAccMonth": "2014-09",
			"fAccDate": "2014-09-25",
			"fCcy": "CNY"
		});
		*/
	}
	
	//ERP明细列定义
	var erpColumns = [[{
		field : 'id',
		title : 'id',
		width : 10,
		hidden : true
	}
	/*, {
		field : 'doc_nbr',
		title : '行号',
		fixed : true,
		width : 50
	}*/, {
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
		fixed : true,
		editor: {
			type: "textarea"
		},
		width : 90/*,
		formatter: function(value, row, index) {
			var amount = row.debitamt;
			amount = chgAmtFormat(amount);
			return amount;
		}*/
	}, {
		field : 'creditamt',
		title : '贷方金额(元)',
		fixed : true,
		editor: {
			type: "textarea"
		},
		width : 90/*,
		formatter: function(value, row, index) {
			var amount = row.creditamt;
			amount = chgAmtFormat(amount);
			return amount;
		}*/
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
		var fid = FW.get("fid");
		var getReimbursementMan = FW.get("getReimbursementMan");
		var finNameEn = FW.get("finNameEn");
		var finTypeEn = FW.get("finTypeEn");
		
		$("#accountDetail").iFold("init"); //显示标题
		
		//getReimbursementMan = encodeURI(encodeURI(getReimbursementMan));
		
		//alert(basePath + "finance/financeInfoController/queryFinanceGeneralLedgerInfoListByFid.do?fid=" + fid + "&getReimbursementMan=" + encodeURI(JSON.stringify(getReimbursementMan)) + "&finNameEn=" + finNameEn + "&finTypeEn=" + finTypeEn );
		
		$("#erpTable").datagrid({
			//url: basePath + "finance/financeInfoController/queryFinanceGeneralLedgerInfoListByFid.do?fid=" + fid + "&getReimbursementMan=" + encodeURI(JSON.stringify(getReimbursementMan)) + "&finNameEn=" + finNameEn + "&finTypeEn=" + finTypeEn,
			url: basePath + "finance/financeInfoController/queryFinanceGeneralLedgerInfoListByFid2.do",
			queryParams: {
				fid: fid,
				getReimbursementMan: getReimbursementMan,
				finNameEn: finNameEn,
				finTypeEn: finTypeEn
			},
			resizeHandle: 'both',
			nowrap: false,
			fitColumns: true,
			singleSelect: true,
			collapsible: true,
			scrollbarSize: 0,
			columns: erpColumns,
			onRenderFinish : function() {
				setTimeout(
					function() {
						beginEditTable("erpTable");
					}, 50);
			}
		});
		
		/*
		$("#erpTable").datagrid('appendRow', {
			subject : "ITC.1232605.660209.0.0.0.0.0.0.0",
			//subjectRemark : "科技公司.经营部.(业务及)管理费用-业务招待费.-.-.-.-.-.-.-",
			subjectRemark : "科技公司.经营部.(业务及)管理费用-业务招待费.-.-.-.-.-.-.-",
			debitAmt : "300.9",
			creditAmt : "",
			cashItem : "",
			intervalUnit : "",
			certRowDesc : "付刘思颖报销款"
		});
		
		$("#erpTable").datagrid('appendRow', {
			subject : "ITC.1232604.660209.0.0.0.0.0.0.0",
			subjectRemark : "科技公司.维护服务部.(业务及)管理费用-业务招待费.-.-.-.-.-.-.-",
			debitAmt : "200",
			creditAmt : "",
			cashItem : "",
			intervalUnit : "",
			certRowDesc : "付张宇报销款"
		});
		
		$("#erpTable").datagrid('appendRow', {
			subject : "ITC.1232603.660209.0.0.0.0.0.0.0",
			subjectRemark : "科技公司.开发部.(业务及)管理费用-业务招待费.-.-.-.-.-.-.-",
			debitAmt : "199",
			creditAmt : "",
			cashItem : "",
			intervalUnit : "",
			certRowDesc : "付周保康报销款"
		});
		
		$("#erpTable").datagrid('appendRow', {
			subject : "ITC.0.100202.19710020203.0.0.0.0.0.0",
			subjectRemark : "科技公司.-.银行存款-外部银行.建行广州粤电支行报账易.-.-.-.-.-.- ",
			debitAmt : "",
			creditAmt : "699.9",
			cashItem : "80010401",
			intervalUnit : "A001",
			certRowDesc : "贷方总账"
		});
		*/
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
	<div>
		<div style="width:100%;margin-top: 9px;">
			<table style="width:100%;">
				<tr>
					<td>
						<form id="erpForm"></form>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div grouptitle="会计分录" id="accountDetail">
		<div class="margin-title-table">
			<table id="erpTable" style="" class="eu-datagrid"></table>
		</div>
	</div>
</body>
</html>
