//新建采购单
var new_form = [
	{title : "合同名称", id : "sheetname", rules : {required:true}},
    {title : "采购类型", id : "sheetIType", rules : {required:true},
    	type : "combobox",
		dataType : "enum",
		enumCat : "ITEMORDER_TYPE"
    },
    {title : "供应商", id : "companyName",rules : {required:true}},
    {title : "供应商编码", id : "companyNo",type:"hidden"},
    {title : "要求到货日期", id : "dhdate",type:"date",rules : {required:true}},
    {title : "质保天数", id : "qualityOkLen",type:"text",
   	 rules : { "digits" : true,range:[1,9999]} 
    },
    {title : "总价(元)", id : "totalPrice",type:"label"},
    {title : "统一税率(%)", id : "taxRate",
    	type : "combobox",
		dataType : "enum",
		enumCat : "PUR_ORDER_TAXRATE",
		initOnChange: false,
		options : {
	      	  onChange : function(val){
	      		allSelectControlCombox(val);
	      	  }
        }
    },
    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
    {title : "申请单编码", id : "sheetno", type:"hidden"}
];

//编辑采购单	
var edit_form = [
	{title : "合同名称", id : "sheetname", rules : {required:true}},
	{title : "经办人", id : "username", type:"label"},
	{title : "总价(元)", id : "totalPrice",type:"label"},
    {title : "采购类型", id : "sheetIType", rules : {required:true},
    	type : "combobox",
		dataType : "enum",
		enumCat : "ITEMORDER_TYPE"
    },
    {title : "合同编号", id : "sheetno", type:"label"},
    {title : "商务网订单号", id : "businessno", type:"label",formatter:function(value){return null==value?"":value.replace(Priv.secUser.siteId,'')}},
    {title : "供应商编码", id : "companyNo",type:"hidden"},
    {title : "供应商", id : "companyName",rules : {required:true}},
    {title : "要求到货日期", id : "dhdate",type:"date", rules : {required:true}},
    {title : "质保天数", id : "qualityOkLen",type:"text",
     	  rules : { "digits" : true,range:[1,9999]} 
      },
    
    {title : "统一税率(%)", id : "taxRate",
    	type : "combobox",
		dataType : "enum",
		enumCat : "PUR_ORDER_TAXRATE",
		initOnChange: false,
		options : {
	      	  onChange : function(val){
	      		if(formInited&&datagridInited){
	      		allSelectControlCombox(val);
	      		}
	      	  }
        }
    },
    {title : "是否提交总经理", id : "purOrderIsGm",
    	type : "combobox",
		dataType : "enum",
		enumCat : "PUR_ORDER_ISGM"
    },
    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8}
];

//编辑表单加载数据（通用方法）
function editForm(inForm){
	$("#autoform").ITC_Form({validate:true,fixLabelWidth:true},inForm);
	//加载用户表单数据
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"purchase/purorder/queryPurOrderDetail.do",
		data: {"sheetId":sheetId },
		dataType : "json",
		success : function(data) {
			if(data.totalPrice>20000){
				data.purOrderIsGm = "Y";
			}else{
				data.purOrderIsGm = "N";
			}
			var loaddata = {
					"username" : data.username, 
					"sheetname" : data.sheetname,
					"sheetIType" : data.sheetIType, 
					"companyName" : data.companyName,
					"companyNo" : data.companyNo,
					"qualityOkLen":data.qualityOkLen==0?null:data.qualityOkLen,
					"dhdate" : data.dhdate, 
					"totalPrice" : data.totalPrice, 
					"remark" : data.remark,
					"sheetno" : data.sheetno,
					"taxRate" : data.taxRate,
					"purOrderIsGm" : data.purOrderIsGm,
					"businessno" : data.businessno
				};
			var status = data.status;
			var createaccount = data.createaccount;
			var curaccount = Priv.secUser.userId;
			//不是创建者打开不可见录入发票
			if(createaccount!=curaccount){
				$("#btn-stopOrder").hide();
			}
			status= new Number(status);
			if(5==status||10==status){
				var invoiceBtn = $("#btn-invoice");
				var stopOrderBtn = $("#btn-stopOrder");
				var createInvAcceptBtn = $("#btn-createInvAccept");
				if(undefined!=invoiceBtn){
					$("#btn-invoice").hide();
				}else{
					$("#btn-invoice").hide();  //永远隐藏录入发票按钮，以后将用付款报账功能所替代
				}
				if(undefined!=stopOrderBtn){
					$("#btn-stopOrder").hide();
				}
				if(undefined!=createInvAcceptBtn){
					$("#btn-createInvAccept").hide();
				}
			}else{
				$("#btn-invoice").hide();  //永远隐藏录入发票按钮，以后将用付款报账功能所替代
			}
			if("作废"!=status){
				$("#btn-doingStatus").hide();
			}
			FW.fixToolbar("#toolbar1");
			$("#autoform").ITC_Form("loaddata",loaddata);
			
			setTimeout(function(){ formInited = true; },0);
			initFormStatus = $("#autoform").iForm("getVal");
		}
	});
}