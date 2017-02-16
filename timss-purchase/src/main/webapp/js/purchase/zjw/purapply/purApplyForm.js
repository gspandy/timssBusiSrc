/*************************************************采购申请*************************************************/
//新建采购申请
var new_single = [
	    {title : "申请单名称", id : "sheetname", rules : {required:true}},
	    {title : "资产性质", id : "assetNature", rules : {required:true}, options: {
			allowEmpty:true,initOnChange: false,onChange: assetNatureChange},
    		type : "combobox",
			dataType : "enum",
			enumCat : "ASSET_NATURE",
			render : function(id){
				$('#' + id).iCombo('setTxt','');
			}
	    },{title : "采购类型", id : "sheetclassid", rules : {required:true}, options: {initOnChange: true,onChange: sheetClassidChange},
	    		type : "combobox",
				dataType : "enum",
				enumCat : "ITEMAPPLY_TYPE"
		},
	    {title : "要求到货日期", id : "dhdate",type:"date",rules : {required:true}},
	    {title : "项目名称", id : "projectAscription", rules : {required:true}, 
	    		type : "combobox",
				data : FW.parse(warehouse),
				options:{
					allowEmpty:true,
					initOnChange:false,
					onChange: projectAscriptionChange
				},
				render : function(id){
					$('#' + id).iCombo('setTxt','');
				}
		},
	    {title : "总价(元)", id : "tatolcost",type:"label"},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "申请单编号", id : "sheetno", type:"hidden"},
	    {title : "采购状态", id : "purchstatus", type:"hidden"},
	    {title : "数据来源", id : "source", type:"hidden"}
	];
	
//编辑采购申请	
var edit_single = [
	    {title : "申请单名称", id : "sheetname", rules : {required:true}},
    	{title : "申请人", id : "createaccount", type:"label"},
	    {title : "总价(元)", id : "tatolcost",type:"label"},
	    {title : "资产性质", id : "assetNature", rules : {required:true}, options: {allowEmpty:true,initOnChange: false,onChange: assetNatureChange},
    		type : "combobox",
			dataType : "enum",
			enumCat : "ASSET_NATURE"
	    },
	    {title : "采购类型", id : "sheetclassid", rules : {required:true}, options: {initOnChange: true,onChange: sheetClassidChange},
	    		type : "combobox",
				dataType : "enum",
				enumCat : "ITEMAPPLY_TYPE"
		},
	    {title : "要求到货日期", id : "dhdate",type:"date", rules : {required:true}},
	    {title : "申请单编号", id : "sheetno", type:"label"},
	    {title : "项目名称", id : "projectAscription", rules : {required:true}, 
	    		type : "combobox",
				data : FW.parse(warehouse),
				options:{
					allowEmpty:true,
					initOnChange:false,
					onChange: projectAscriptionChange
				},
				render : function(id){
					$('#' + id).iCombo('setTxt','');
				}
		},
	    {title : "是否授权项目", id : "isauth",
	    		type : "deputy_oper_manager"==process?"combobox":"hidden",
				dataType : "enum",
				enumCat : "PUR_APPLY_ISAUTH"
		},
		{title : "终止状态", id : "stopStatusLable",type : ""==stopStatus?"hidden":"text"},
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
					"projectAscription" : data.projectAscription, 
					"remark" : data.remark, 
					"sheetId" : data.sheetId,
					"purchtype" : data.purchtype, 
					"sheetno" : data.sheetno,
					"purchstatus" : data.purchstatus,
					"isauth":data.isauth,
					"stopStatusLable":FW.getEnumMap("PUR_APPLY_STOPSTATUS")[data.stopStatus],
					"assetNature":data.assetNature
				};
			$("#autoform").ITC_Form("loaddata",loaddata);
			caluPrice();
		}
	});
}
/*************************************************采购申请*************************************************/		
	