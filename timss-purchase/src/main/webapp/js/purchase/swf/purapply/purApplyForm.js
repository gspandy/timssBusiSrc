/*************************************************采购申请*************************************************/
//新建采购申请
var new_single = [
	    {title : "申请单名称", id : "sheetname", rules : {required:true}},
	    {title : "采购类型", id : "sheetclassid", rules : {required:true}, options: {initOnChange: true,onChange: sheetClassidChange},
	    		type : "combobox",
				data: applyType
		},
		{title : "物资分类", id : "itemclassid", rules : {required:true}, options: {initOnChange: true,onChange: itemClassidChange},
    		type : "combobox",
			dataType : "enum",
			enumCat : "ITEMCLASS_TYPE"
		},
		{title : "项目归类", id : "projectclassid", rules : {required:true}, options: {initOnChange: true,onChange: projectClassidChange},
    		type : "combobox",
			dataType : "enum",
			enumCat : "PROJECTCLASS_TYPE"
		},
		{title : "资产性质", id : "assetNature", rules : {required:true},
    		type : "combobox",
			dataType : "enum",
			enumCat : "PUR_ASSETNATURE"
		},
		{title : "项目编号", id : "projectCode"},
		{title : "要求到货日期", id : "dhdate",type:"date",rules : {required:true}},
	    {title : "项目名称", id : "projectAscription", rules : {maxlength:30}},
		{title : "专业", id : "major", rules : {required:true}, options:{allowEmpty:true},
    		type : "combobox",
			data :FW.parseEnumData("ITEMMAJOR_TYPE",_enum)
		},
		{title : "是否紧急采购", id : "isUrgentPurchase",type : "radio",data : [['Y','是'],['N','否',true]]},
	    {title : "总价(元)", id : "tatolcost",type:"label"},
	    {title : "用途", id : "usage",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "申请单编码", id : "sheetno", type:"hidden"},
	    {title : "采购状态", id : "purchstatus", type:"hidden"},
	    {title : "数据来源", id : "source", type:"hidden"}
	];
	
//编辑采购申请	
var edit_single = [
	    {title : "申请单名称", id : "sheetname", rules : {required:true}},
		{title : "申请人", id : "createaccount", type:"label"},
    	{title : "总价(元)", id : "tatolcost",type:"label"},
    	{title : "所属部门", id : "dept",type:"label"},
	    {title : "采购类型", id : "sheetclassid", rules : {required:true}, options: {initOnChange: true,onChange: sheetClassidChange},
	    		type : "combobox",
				dataType : "enum",
				enumCat : "ITEMAPPLY_TYPE"
		},
		{title : "物资分类", id : "itemclassid", rules : {required:true}, options: {initOnChange: true},
    		type : "combobox",
			dataType : "enum",
			enumCat : "ITEMCLASS_TYPE"
		},
		{title : "项目归类", id : "projectclassid", rules : {required:true}, options: {initOnChange: true,onChange: projectClassidChange},
    		type : "combobox",
			dataType : "enum",
			enumCat : "PROJECTCLASS_TYPE"
		},
		{title : "资产性质", id : "assetNature", rules : {required:true},
    		type : "combobox",
			dataType : "enum",
			enumCat : "PUR_ASSETNATURE"
		},
		{title : "项目编号", id : "projectCode"},
		{title : "要求到货日期", id : "dhdate",type:"date", rules : {required:true}},
	    {title : "申请单编号", id : "sheetno", type:"label"},
	    {title : "项目名称", id : "projectAscription", rules : {maxlength:30}},
		{title : "专业", id : "major", rules : {required:true},  options:{initOnChange: true},
    		type : "combobox",
			dataType : "enum",
			enumCat : "ITEMMAJOR_TYPE"
		},
		{title : "是否紧急采购", id : "isUrgentPurchase",type : "radio",data : [['Y','是'],['N','否',true]]},
	    
	    {title : "是否授权项目", id : "isauth",
	    		type : "deputy_oper_manager"==process?"combobox":"hidden",
				dataType : "enum",
				enumCat : "PUR_APPLY_ISAUTH"
		},
		{title : "是否提交商务网", id : "isToBusiness",
				type :"draft"==process?"hidden":"radio",
				data : [['Y','是'],['N','否',true]],
				rules : {required:"draft"==process?false:true}},
		{title : "终止状态", id : "stopStatusLable",type : ""==stopStatus?"hidden":"text"},
		{title : "用途", id : "usage",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "审批单编号", id : "sheetId", type:"hidden"},
	    {title : "采购状态", id : "purchstatus", type:"hidden"},
	    {title : "数据来源", id : "source", type:"hidden"}
	    
];

//首环节编辑采购申请	
var editdraft_single = [
        {title : "申请单编号", id : "sheetno", type:"label"},
	    {title : "申请单名称", id : "sheetname", rules : {required:true}},
		{title : "申请人", id : "createaccount", type:"label"},
    	{title : "所属部门", id : "dept",type:"label"},
	    {title : "采购类型", id : "sheetclassid", rules : {required:true}, options: {initOnChange: true,onChange: sheetClassidChange},
	    		type : "combobox",
	    		data: applyType
		},
		{title : "物资分类", id : "itemclassid", rules : {required:true}, options: {initOnChange: true},
    		type : "combobox",
			dataType : "enum",
			enumCat : "ITEMCLASS_TYPE"
		},
		{title : "项目归类", id : "projectclassid", rules : {required:true}, options: {initOnChange: true,onChange: projectClassidChange},
    		type : "combobox",
			dataType : "enum",
			enumCat : "PROJECTCLASS_TYPE"
		},
		{title : "资产性质", id : "assetNature", rules : {required:true},
    		type : "combobox",
			dataType : "enum",
			enumCat : "PUR_ASSETNATURE"
		},
		{title : "项目编号", id : "projectCode"},
		{title : "要求到货日期", id : "dhdate",type:"date", rules : {required:true}},
	    {title : "项目名称", id : "projectAscription", rules : {maxlength:30}},
		{title : "专业", id : "major", rules : {required:true},  options:{initOnChange: true},
    		type : "combobox",
			dataType : "enum",
			enumCat : "ITEMMAJOR_TYPE"
		},
		{title : "是否紧急采购", id : "isUrgentPurchase",type : "radio",data : [['Y','是'],['N','否',true]]},
	    {title : "总价(元)", id : "tatolcost",type:"label"},
	    {title : "是否授权项目", id : "isauth",
	    		type : "deputy_oper_manager"==process?"combobox":"hidden",
				dataType : "enum",
				enumCat : "PUR_APPLY_ISAUTH"
		},
		{title : "是否提交商务网", id : "isToBusiness",
				type :"draft"==process?"hidden":"radio",
				data : [['Y','是'],['N','否',true]],
				rules : {required:"draft"==process?false:true}},
		{title : "终止状态", id : "stopStatusLable",type : ""==stopStatus?"hidden":"text"},
		{title : "用途", id : "usage",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "审批单编号", id : "sheetId", type:"hidden"},
	    {title : "采购状态", id : "purchstatus", type:"hidden"},
	    {title : "数据来源", id : "source", type:"hidden"}
	    
];

//编辑表单加载数据（通用方法）
function editApplyForm(inForm){
	$("#autoform").ITC_Form({validate:true,fixLabelWidth:true},inForm);
	//加载用户表单数据
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"purchase/purapply/queryPurApplyDetail.do",
		data: {"sheetId":sheetId},
		dataType : "json",
		success : function(data) {
			var loaddata = {
					"createaccount" : data.createaccount, 
					"sheetname" : data.sheetname,
					"sheetclassid" : data.sheetclassid, 
					"dhdate" : FW.long2date(data.dhdate),
					"remark" : data.remark, 
					"sheetId" : data.sheetId,
					"purchtype" : data.purchtype, 
					"sheetno" : data.sheetno,
					"purchstatus" : data.purchstatus,
					"isauth":data.isauth,
					"usage":data.usage,
					"dept":data.dept,
					"isToBusiness":data.isToBusiness,
					"itemclassid":data.itemclassid,
					"projectclassid" : data.projectclassid,
					"major":data.major,
					"isUrgentPurchase":data.isUrgentPurchase,
					"projectCode":data.projectCode,
					"assetNature":data.assetNature,
					"projectAscription" : data.projectAscription, //TIM-782 增加项目名称
					"stopStatusLable":FW.getEnumMap("PUR_APPLY_STOPSTATUS")[data.stopStatus]
				};
			$("#autoform").ITC_Form("loaddata",loaddata);
			caluPrice();
		}
	});
}
/*************************************************采购申请*************************************************/		
	