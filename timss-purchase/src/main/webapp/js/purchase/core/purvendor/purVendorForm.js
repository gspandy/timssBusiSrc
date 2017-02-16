//新建时field
var new_field = [
		    {title : "公司名称", id : "name", rules : {required:true,maxlength:255}},
		    {title : "公司类型", id : "type", rules : {required:true,maxlength:255},
		    	type : "combobox",
				dataType : "enum",
				enumCat : "PUR_COMPANYTYPE"
		    },
		    {title : "公司地址", id : "address", rules : {maxlength:255}},
		    {title : "联系电话", id : "tel", rules : {maxlength:64,digits:true}},
		    {title : "传真号码", id : "fax", rules : {maxlength:64}},
		    {title : "联系人", id : "contact", rules : {maxlength:255}},
		    {title : "开户银行", id : "bankName", rules : {maxlength:255}},
		    {title : "银行账号", id : "bankAccount", rules : {maxlength:255}},
		    {title : "父级银行编码", id : "parentCompanycode", rules : {maxlength:64}}
		];

//编辑时field	
var edit_field = [
	    {title : "公司名称", id : "name", rules : {required:true,maxlength:255}},
		    {title : "公司类型", id : "type", rules : {required:true,maxlength:255},
		    	type : "combobox",
				dataType : "enum",
				enumCat : "PUR_COMPANYTYPE"
		    },
		    {title : "公司地址", id : "address", rules : {maxlength:255}},
		    {title : "联系电话", id : "tel", rules : {maxlength:64,digits:true}},
		    {title : "传真号码", id : "fax", rules : {maxlength:64}},
		    {title : "公司编码", id : "companyNo", rules : {maxlength:64}},
		    {title : "联系人", id : "contact", rules : {maxlength:255}},
		    {title : "开户银行", id : "bankName", rules : {maxlength:255}},
		    {title : "银行账号", id : "bankAccount", rules : {maxlength:255}},
		    {title : "父级银行编码", id : "parentCompanycode", rules : {maxlength:64}}
	];

//编辑表单加载数据
function editForm(inForm){
	$("#autoform").ITC_Form({validate:true,fixLabelWidth:true},inForm);
	//加载用户表单数据
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"/purchase/purvendor/queryPurVendorDetail.do",
		data: {"companyNo":cNo},
		dataType : "json",
		success : function(data) {
			var loaddata = {
				"name" : data.name,
				"type" : data.type,
				"address" : data.address,
				"tel" : data.tel,
				"fax" : data.fax,
				"companyNo" : data.companyNo,
				"contact" : data.contact,
				"bankName": data.bankName,
				"bankAccount": data.bankAccount,
				"parentCompanycode": data.parentCompanycode
			};
			$("#autoform").ITC_Form("loaddata",loaddata);
			initFormStatus = $("#autoform").iForm("getVal");
		}
	});
}