/*************************************************采购申请*************************************************/
//新建单独申请
var new_single = [
	    {title : "申请单名称", id : "sheetname", rules : {required:true}},
	    {title : "采购类型", id : "sheetclassid", rules : {required:true},
	    		type : "combobox",
				dataType : "enum",
				enumCat : "ITEMAPPLY_TYPE"
		},
	    {title : "要求到货日期", id : "dhdate",type:"date",rules : {required:true}},
	    {title : "总价(元)", id : "tatolcost",type:"label"},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "申请单编码", id : "sheetno", type:"hidden"},
	    {title : "采购状态", id : "purchstatus", type:"hidden"}
];

//编辑单独申请	
var edit_single = [
        {title : "申请单编号", id : "sheetno", type:"label"},
	    {title : "申请单名称", id : "sheetname", rules : {required:true}},
    	{title : "申请人", id : "createaccount", type:"label"},
	    {title : "采购类型", id : "sheetclassid", rules : {required:true},
	    		type : "combobox",
				dataType : "enum",
				enumCat : "ITEMAPPLY_TYPE"
		},
	    {title : "要求到货日期", id : "dhdate",type:"date", rules : {required:true}},
	    {title : "总价(元)", id : "tatolcost",type:"label"},
	    {title : "终止状态", id : "stopStatusLable",type : ""==stopStatus?"hidden":"text"},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "审批单编号", id : "sheetId", type:"hidden"},
	    {title : "采购状态", id : "purchstatus", type:"hidden"}
];

//编辑表单加载数据（通用方法）
function editForm(inForm){
	$("#autoform").ITC_Form({validate:true,fixLabelWidth:true},inForm);
	//加载用户表单数据
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"purchase/purapply/queryPurApplyDetail.do",
		data: {"sheetId":sheetId },
		dataType : "json",
		success : function(data) {
			var loaddata = {
					"createaccount" : data.createaccount, "sheetname" : data.sheetname,
					"sheetclassid" : data.sheetclassid, "dhdate" : FW.long2date(data.dhdate),
					"remark" : data.remark, "sheetId" : data.sheetId,
					"purchtype" : data.purchtype, "sheetno" : data.sheetno,
					"purchstatus" : data.purchstatus,
					"stopStatusLable":FW.getEnumMap("PUR_APPLY_STOPSTATUS")[data.stopStatus]
				};
			$("#autoform").ITC_Form("loaddata",loaddata);
			caluPrice();
		}
	});
}
/*************************************************采购申请*************************************************/		
	