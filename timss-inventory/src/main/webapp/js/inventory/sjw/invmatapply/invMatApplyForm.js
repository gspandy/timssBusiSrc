var new_form = [
		{title : "名称", id : "sheetname", rules : {required:true}},
    	{title : "领料分类", id : "applyType", rules:{required:true}, options: {initOnChange: true,onChange: applyTypeChange},
    		type : "combobox",
			dataType : "enum",
			enumCat : "INV_APPLY_TYPE"
    	},
	    {title : "领料成本(含税)", id : "totalPrice",type:"label",linebreak:true,
	    	formatter:function(value){
	    		return parseFloat(value).toFixed(2);
			}
	    },
	    {title : "领料成本(不含税)", id : "totalNoTaxPrice",type:"label",
	    	formatter:function(value){
	    		return parseFloat(value).toFixed(2);
			}
	    },
	    {title : "关联工单", id : "workOrderNo"},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "用途说明", id : "applyUse",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8}
];
	
var edit_form = [
		{title : "id信息", id : "imaid",type:"hidden"},
		{title : "申请单名称", id : "sheetname", rules : {required:true}},
	    {title : "领用部门", id : "dept",type:"label"},
	    {title : "领料成本(含税)", id : "totalPrice",type:"label",
	    	formatter:function(value){
	    		return parseFloat(value).toFixed(2);
			}
	    },
	    {title : "领料成本(不含税)", id : "totalNoTaxPrice",type:"label",
	    	formatter:function(value){
	    		return parseFloat(value).toFixed(2);
			}
	    },	    
		{title : "申请单号", id : "sheetno",type:"label"},
    	{title : "领料分类", id : "applyType", rules:{required:true}, options: {initOnChange: true,onChange: applyTypeChange},
    		type : "combobox",
			dataType : "enum",
			enumCat : "INV_APPLY_TYPE"
    	},
	    {title : "是否特殊物资", id : "spmaterial", rules : {required:true},
	    	type : "oper_manager"==process?"combobox":"hidden",
			dataType : "enum",
			enumCat : "INV_SPMATERIAL"
	    },
	    {title : "年度剩余预算(元)", id : "remainBudget",type:"hidden"},
	    {title : "年度累计领用总额(元)", id : "applyBudget",type:"hidden"},
	    {title : "是否超标", id : "isOver",type:"draft"==process?"hidden":"label",formatter:function(value){
			if(value == '1'){
				return "是";
			}else{
				return "否";
			}
		}},
		{title : "关联工单", id : "workOrderNo"},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "用途说明", id : "applyUse",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "创建人id", id : "createuser",type:"hidden"}
];
	
//编辑表单加载数据（通用方法）
function initForm(inForm){
	if(processStatus == "first"){
		$("#autoform").iForm("init",{"fields":inForm,"options":{validate:true}});
	}else{
		$("#autoform").iForm("init",{"fields":inForm,"options":{validate:true,labelFixWidth:160,xsWidth:4}});
	}
	
	//加载用户表单数据
	$.ajax({
		type : "POST",
		async:false,
		url: basePath+"inventory/invmatapply/queryMatApplyForm.do",
		data: {"imaid":imaid},
		dataType : "json",
		success : function(data) {
			if("" != imaid){
				var loaddata = null;
				if(data.applyType == 'office_supplies'){
					loaddata = {
						"imaid":imaid,
						"sheetname" : data.sheetname,
						"sheetno" : data.sheetno, 
						"applyType" : data.applyType,
						"totalPrice" : data.totalPrice, 
						"remark" : data.remark,
						"spmaterial" : data.spmaterial,
						"remainBudget" : data.remainBudget,
						"applyBudget" : data.applyBudget,
						"isOver" : data.isOver,
						"createuser" : data.createuser,
						"workOrderNo" : data.workOrderNo,
						"dept" : data.dept,
						"applyUse" : data.applyUse
					};
				}else{
					loaddata = {
						"imaid":imaid,
						"sheetname" : data.sheetname,
						"sheetno" : data.sheetno, 
						"applyType" : data.applyType,
						"totalPrice" : data.totalPrice, 
						"remark" : data.remark,
						"spmaterial" : data.spmaterial,
						"createuser" : data.createuser,
						"workOrderNo" : data.workOrderNo,
						"dept" : data.dept,
						"applyUse" : data.applyUse
					};
				}
				$("#autoform").iForm("setVal",loaddata);
				caluPrice();
			}
			initFormStatus = $("#autoform").iForm("getVal");
		}
	});
}