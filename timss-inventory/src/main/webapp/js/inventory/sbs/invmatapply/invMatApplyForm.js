var new_form = [
		{title : "名称", id : "sheetname", rules : {required:true}},
    	{title : "领料单类型", id : "applyType", rules:{required:true}, options: {initOnChange: true,onChange: applyTypeChange},
    		type : "combobox",
			dataType : "enum",
			enumCat : "INV_APPLY_TYPE"
    	},
	    {title : "领用成本(元)", id : "totalPrice",type:"label",
	    	formatter:function(value){
	    		return parseFloat(value).toFixed(2);
			}
	    },
	    {title : "关联工单", id : "workOrderNo"},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8}
];
	
var edit_form = [
		{title : "id信息", id : "imaid",type:"hidden"},
		{title : "申请单名称", id : "sheetname", rules : {required:true}},
	    {title : "领料成本(元)", id : "totalPrice",type:"label",
	    	formatter:function(value){
	    		return parseFloat(value).toFixed(2);
			}
	    },
		{title : "申请单号", id : "sheetno",type:"label"},
    	{title : "领料单类型", id : "applyType", rules:{required:true}, options: {initOnChange: true,onChange: applyTypeChange},
    		type : "combobox",
			dataType : "enum",
			enumCat : "INV_APPLY_TYPE"
    	},
		{title : "关联工单", id : "workOrderNo"},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
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
				var loaddata = {
						"imaid":imaid,
						"sheetname" : data.sheetname,
						"sheetno" : data.sheetno, 
						"applyType" : data.applyType,
						"workOrderNo" : data.workOrderNo,
						"totalPrice" : data.totalPrice, 
						"remark" : data.remark,
						"createuser" : data.createuser
					};
				$("#autoform").iForm("setVal",loaddata);
				caluPrice();
			}	
			initFormStatus = $("#autoform").iForm("getVal");
		}
	});
}
