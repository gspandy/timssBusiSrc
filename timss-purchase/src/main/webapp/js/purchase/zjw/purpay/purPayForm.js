/*************************************************采购付款*************************************************/
//新建付款信息
var formfield = [
		{title : "合同编号", id : "sheetNo",type:"label"},
		{title : "合同名称", id : "sheetName",type:"label"},
		{title : "合同金额", id : "sheetTotal",type:"label",precision:2},
		//到货款、结算款
		{title : "发票编号", id : "invoiceNos",
			rules : {required:true,maxlength:100},
			messages : {required:"请填写发票编号",maxlength:"不可超过100个英文或数字"}},
		//预付款、到货款、结算款
		{title : "付款比例(%)", id : "payRatio",precision:3,
		render:function(id){
			$("#"+id).on("blur",function(){
				refreshPay();
				refreshUsage();
			});
		}, 
		rules : {required:true,range:[0,100],number:true},
		messages : {required:"请填写付款比例",range:"请输入0~100的数字",number:"请输入0~100的数字"}
		},
		//预付款、到货款、结算款\质保金(质保金额)
		{title : "qualitypay"==payType?"质保金金额(元)":"付款金额(元)", id : "pay",precision:2,
		render:function(id){
			$("#"+id).on("blur",function(){
					refreshPayRatio();
					refreshActualPay();
			});
		},  
		rules : {required:true,number:true,min:0},
		messages : {required:"请输入"+"qualitypay"==payType?"质保金金额":"付款金额",number:"请输入不小于0的数字",min:"请输入不小于0的数字"}
		},
		//到货款
		{title : "质保金金额(元)", id : "qaPay",precision:2, 
		rules : {number:true,min:0},
		messages : {number:"请输入不小于0的数字",min:"请输入不小于0的数字"}
		},
		//预付款、质保金
		{title : "供应商", id : "supplierName",type:"label"},
		//质保金
		{title : "拒付金额(元)", id : "refusePay",precision:2, 
		rules : {required:true,number:true,min:0},
		messages : {required:"请输入拒付金额",number:"请输入不小于0的数字",min:"请输入不小于0的数字"},
		render: function(id){
			$("#"+id).on("blur",function(){
				refreshActualPay();
			});
		},
		linebreak:true},
		//到货款
		{title : "不含税金额", id : "noTaxTotal",precision:2,type:"label"},
		//到货款
		{title : "税额", id : "taxTotal",precision:2,type:"label"},
		//到货款
		{title : "含税金额", id : "total",precision:2,type:"label"},
		//质保金
		{title : "实际支付", id : "actualPayTotal",precision:2,type:"label"},
		//质保金
		{title : "扣除日期", id : "excludeDate",type:"label",
			formatter:function(val){
				return FW.long2date(val);
			}	
		},
		//质保金
		{title : "质保到期日期", id : "qaDeadLine",type:"date",
		rules : {required:true},
		messages : {required:"请输入质保到期日期"}
		},
		//质保金
		{title : "关联到货款记录", id : "relatePayNo",type:"label"},
		{title : "项目及用途", id : "usage",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8,
			rules:{maxlength:300},messages:{maxlength:"最多不可超过300个汉字"}	
		},
		{title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8,
			rules:{maxlength:100},messages:{maxlength:"最多不可超过100个汉字"}	
		},
		//计划合同部 部长审批环节 确定是否需要会签
		{title : "是否会签", id : "countersign",
			type :"plancontractdept_manager"!=process?"hidden":"radio",
			linebreak:true,
			data : [['Y','是'],['N','否']],
			rules : {required:"plancontractdept_manager"==process?true:false}},
		//隐藏域
		{title : "付款Id", id : "payId",type:"hidden"},
		{title : "采购合同Id", id : "sheetId",type:"hidden"},
		{title : "付款类型", id : "payType",type:"hidden"},
		{title : "关联付款Id", id : "relatePayId",type:"hidden"},
		{title : "审批通过日期", id : "auditDate",type:"hidden"},
		{title : "付款编号", id : "payNo",type:"hidden"},
		{title : "状态", id : "status",type:"hidden"},
		{title : "流程实例Id", id : "procInstId",type:"hidden"},
		{title : "处理人", id : "transactor",type:"hidden"},
		{title : "处理人姓名", id : "transactorName",type:"hidden"},
		{title : "采购类型", id : "sheetClassId",type:"hidden"},
		{title : "物资类型", id : "itemClassid",type:"hidden"}
];

function refreshPay(){
	var sheetTotal = $("#autoform").iForm("getVal","sheetTotal");
	var payRatio = $("#autoform").iForm("getVal","payRatio");
	sheetTotal = ""!=sheetTotal?sheetTotal:0;
	payRatio = ""!=payRatio?payRatio:0;
	if(""!=payRatio){
		$("#autoform").iForm("setVal",{"pay":(parseFloat(payRatio)*parseFloat(sheetTotal)/100).toFixed(2)});
	}else{
		$("#autoform").iForm("setVal",{"pay":""});
	}
}
function refreshPayRatio(){
	var sheetTotal = $("#autoform").iForm("getVal","sheetTotal");
	var pay = $("#autoform").iForm("getVal","pay");
	sheetTotal = ""!=sheetTotal?sheetTotal:0;
	pay = ""!=pay?pay:0;
	if(""!=pay){
		$("#autoform").iForm("setVal",{"payRatio":(parseFloat(pay)/parseFloat(sheetTotal)*100).toFixed(3)});
	}else{
		$("#autoform").iForm("setVal",{"payRatio":""});
	}
	refreshUsage();
}

function refreshActualPay(){
	var pay = $("#autoform").iForm("getVal","pay");
	var refusePay = $("#autoform").iForm("getVal","refusePay");
	pay = ""!=pay?pay:0;
	refusePay = ""!=refusePay?refusePay:0;
	$("#autoform").iForm("setVal",{"actualPayTotal":(parseFloat(pay)-parseFloat(refusePay)).toFixed(2)});
}

function refreshUsage(){
	var usage = $("#autoform").iForm("getVal","usage");
	var supplierName = $("#autoform").iForm("getVal","supplierName");
	if(usage.indexOf("支付")>-1&&usage.indexOf("付款比例")>-1){
		var payRatio = $("#autoform").iForm("getVal","payRatio");
		var new_usage = usage.substring(0,usage.indexOf("支付")+2)+supplierName+payRatio+"%"+usage.substring(usage.indexOf("付款比例"));
		$("#autoform").iForm("setVal",{"usage":new_usage});
	}
}

//打开采购合同页面   
FW.showPOPage = function(sheetId,sheetNo){
	var currTabId = FW.getCurrentTabId();
	var urlPath = basePath+ "purchase/purorder/purOrderForm.do?type=edit&sheetId="+sheetId+"&sheetNo="+sheetNo;
	var opts = {
        id : "editOrderFormFromPay" + sheetId,
        name : "采购合同",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + currTabId + "');" 
        }
    };
    _parent()._ITC.addTabWithTree(opts); 
};
/*************************************************采购付款*************************************************/		
	