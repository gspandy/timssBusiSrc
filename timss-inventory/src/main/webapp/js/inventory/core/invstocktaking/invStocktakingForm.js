var new_form = [
		{title : "盘点单名称", id : "sheetname",rules : {required:true}},
	    {title : "盘点日期", id : "createdate",type:"date",rules : {required:true}},
	    {title : "仓库", id : "warehouseid",type : "combobox", data:comboxArr, rules : {required:true}},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8}
];

var edit_form = [
		{title : "istid", id : "istid", type:"hidden"},
		{title : "盘点单名称", id : "sheetname",rules : {required:true}},
	    {title : "盘点日期", id : "createdate",formatter:function(value){
			return FW.long2date(value);
		}},		
	    {title : "仓库id", id : "warehouseid",type:"hidden"},
	    {title : "仓库", id : "warehousename"},		
		{title : "盘点单号", id : "sheetno"},
	    {title : "盘点人", id : "createusername"},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "流程id", id : "instanceid",type:"hidden"}
];

//编辑表单加载数据（通用方法）
function initForm(inForm){
	$("#autoform").iForm("init",{"fields":inForm,"options":{validate:true}});
	//加载用户表单数据
	$.ajax({
		type : "POST",
		url: basePath+"inventory/invstocktaking/queryStocktakingForm.do",
		data: {"istid":istid},
		dataType : "json",
		success : function(data) {
			if("" != istid){
				var loaddata = {
						"istid":istid,
						"sheetno" : data.sheetno,
						"sheetname" : data.sheetname, 
						"createusername" : data.createusername,
						"createdate" : data.createdate, 
						"warehouseid" : data.warehouseid,
						"warehousename" : data.warehousename,
						"checkuser" : data.checkuser,
						"checkusername" : data.checkusername,
						"remark" : data.remark,
						"instanceid" : data.instanceid
					};
				$("#autoform").iForm("setVal",loaddata);
			}
			initFormStatus = $("#autoform").iForm("getVal");
		}
	});
}