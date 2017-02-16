var new_form = [
		{title : "名称", id : "sheetname", rules : {required:true}},
    	{title : "领料分类", id : "applyType",type:"hidden"},
	    {title : "领用成本(元)", id : "totalPrice",type:"label",
	    	formatter:function(value){
	    		return parseFloat(value).toFixed(4);
			}
	    },
	    {title : "领用部门", id : "dept",type:"label"},
	    {title : "关联工单", id : "workOrderNo",type:"hidden"},
	    {title : "用途说明", id : "applyUse",rules : {required:true}, type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "关联领料申请单", id : "relatePurApplyIdsList",type:"hidden" }
];
	
var edit_form = [
		{title : "id信息", id : "imaid",type:"hidden"},
		{title : "申请单名称", id : "sheetname", rules : {required:true}},
	    {title : "领用部门", id : "dept",type:"label"},
	    {title : "领料成本(元)", id : "totalPrice",type:"label",
	    	formatter:function(value){
	    		return parseFloat(value).toFixed(4);
			}
	    },	    
		{title : "申请单号", id : "sheetno",type:"label"},
    	{title : "领料分类", id : "applyType",type:"hidden"},
	    {title : "是否特殊物资", id : "spmaterial", rules : {required:true},
	    	type : "oper_manager"==process?"combobox":"hidden",
			dataType : "enum",
			enumCat : "INV_SPMATERIAL"
	    },
	    {title : "年度剩余预算(元)", id : "remainBudget",type:"draft"==process?"hidden":"label"},
	    {title : "年度累计领用总额(元)", id : "applyBudget",type:"draft"==process?"hidden":"label"},
	    {title : "是否超标", id : "isOver",type:"draft"==process?"hidden":"label",formatter:function(value){
			if(value == '1'){
				return "是";
			}else{
				return "否";
			}
		}},
		{title : "关联工单", id : "workOrderNo",type:"hidden"},
		{title : "用途说明", id : "applyUse",rules : {required:true}, type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "创建人id", id : "createuser",type:"hidden"},
	    {title : "关联领料申请单", id : "relatePurApplyIdsList",type:"hidden" }
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
						"applyUse" : data.applyUse,
						"relatePurApplyIdsList" : data.relatePurApplyIdsList
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
						"applyUse" : data.applyUse,
						"relatePurApplyIdsList" : data.relatePurApplyIdsList
					};
				}
				$("#autoform").iForm("setVal",loaddata);
				caluPrice();
			}else{
				var forOrg = mvcService.getUser().orgs;
				if(forOrg.length>0){
					var deptName = mvcService.getUser().orgs[0].name;
				}else{
					var deptName = "该账户不属于任何部门";
				}
				$("#autoform").iForm("setVal",{dept:deptName});
			}
			initFormStatus = $("#autoform").iForm("getVal");
		}
	});
}