//是否为标准工作票 0 ： 不是， 1：是
var isStdWt = 0;
var subGrid = null;
var searchFrom = "wtKind"; // wtKind, wtNo,advSearch,treeSearch
var searchParams = {};
//当前datagrid的参数
var currentData = null;

var modelFlag = false;

//工作票类型
var ptwTypeCodes = "";

var columns = [[
        		{field:'wtNo',title:'编号',width:180,sortable:true,fixed:true},
        		{field:'wtStatus',title:'状态',width:85,sortable:true,fixed:true,
        			formatter : function(value,row,index){
        				return ptwUtil.getStatusNameByWtStatus(value);
        			}
        		},
        		{field:'eqName',title:'设备名称',width:200,sortable:true,fixed:true},
        		{field:'workContent',title:'工作内容',width:180,sortable:true},
        		{field:'workPlace',title:'工作地点',width:160,sortable:true,fixed:true}
        	]];

var subColumns = [[
	{field:'issuer',title:'签发人',width:70,fixed:true},
	{field:'issuedTime',title:'签发时间',width:130,
		formatter: function(value,row,index){
			return FW.long2time(value);
		}},
	{field:'licWl',title:'许可人',width:70,fixed:true},
	{field:'licTime',title:'许可时间',width:130,
		formatter: function(value,row,index){
			return FW.long2time(value);
		}},
	{field:'preWorkEndTime',title:'预计结束时间',width:130,
		formatter: function(value,row,index){
			return FW.long2time(value);
		}},
	{field:'finTime',title:'实际结束时间',width:130,
		formatter: function(value,row,index){
			return FW.long2time(value);
		}},
	{field:'endWl',title:'终结人',width:70,fixed:true},
	{field:'endTime',title:'终结时间',width:130,
		formatter: function(value,row,index){
			return FW.long2time(value);
		}}
]];

/**工作票基本信息展示的grid*/
function loadSubGridData(index){
	if(currentData != undefined && currentData != null && currentData.total > 0 && isStdWt == 0){
		$("#subGridDiv").show();
		var data = {"rows":[currentData.rows[index]],"total":1};
		subGrid.datagrid('loadData',data);
	}else{
		$("#subGridDiv").hide();
	}		
}

/**查询*/
function search(){
	modelFlag = true;
    //这里的mytable要换成datagrid对应的id
    delete(_itc_grids["ptwGrid"]);
    var pager = $("#ptwGrid").datagrid("getPager"); 
    pager.pagination("select",1);
};

/**查询*/
function searchAfterClosePtw(){
	if(FW.get("PtwDoNotRefresh")){
		FW.set("PtwDoNotRefresh",false);
		return;
	}
    search();
};

/**打开或关闭高级搜索的框*/
function preAdvSearch(){
    var btn = $("#btnSearch");
    if(!btn.hasClass("active")){
        $("#advSearchBar").show();
    }else{
        $("#advSearchBar").hide();
    }
}

/**高级搜索*/
function advSearch(){
	searchFrom = "advSearch";
	var searchDateType = $("#searchDateType").iCombo('getVal');
	var searchDateFrom = $("#sDateFrom").val();
	var searchDateEnd = $("#sDateEnd").val();
	var wtStatus = $("#wtStatus").iCombo('getVal');
	var wtType = $("#wtType").iCombo('getVal');
	var searchPersonType = $("#searchPersonType").iCombo('getVal');
	var searchPersonId = $("#searchPersonName").iCombo('getVal');
	searchParams = {
		searchDateEnd : searchDateEnd,
		searchDateFrom : searchDateFrom,
		searchDateType : searchDateType,
		searchPersonType : searchPersonType,
		searchPersonId :searchPersonId,
		wtStatus : wtStatus,
		wtType : wtType
	};
	search();
};

/**根据工作票种类查询*/
function searchFromKind(wtKind){
	isStdWt = wtKind;
	if(isStdWt  == 1){
		$("#wtTypeDiv").hide();
		$("#searchPerson").hide();
	}else{
		$("#wtTypeDiv").show();
		$("#searchPerson").show();
	}
	searchFrom = "wtKind";
	search();
}

/**刷新工作票*/
function refreshGrid(){
	searchFromKind(isStdWt);
	//取消树的选中		
	var p = _parent().window.document.getElementById("ptwAssetTree").contentWindow;
	if(p){
		p.unSelectTree();
	}
}


function toShow( node ){
	if(node.parentId == 0){
		refreshGrid();
	}else{
		searchFrom = "treeSearch";
		searchParams = {eqId:node.obj.equipmentId};
		search();
	}
}

function onTreeItemClick(node){
	if(node.type == 'root'){
		refreshGrid();
	}else{
		searchFrom = "treeSearch";
		searchParams = {eqId:node.id};
		search();
	}
}

function rowStyler(rowIndex, rowData) {
	if (rowData.isStdWt == 1) {
		//标准票
		if (rowData.wtStatus == 4000) {
			//已作废
			return 'color:#c0c0c0;';
		} else {
			return 'color:#008000;';
		}
	} else {
		if (rowData.wtStatus == 500) {
			//已许可
			return 'color:red;';
		} else if (rowData.wtStatus == 800) {
			//已作废
			return 'color:#c0c0c0;';
		}
	}
}

//新建卡片
function newPtwInfo(){
	var params = {opType:"newPtw", ptwTypeCodes:ptwTypeCodes};
	
	var p = null;
	if(stdFlagFromMenu){
		p = _parent().window.document.getElementById("ptwStandardTree").contentWindow;
	}else{
		p = _parent().window.document.getElementById("ptwAssetTree").contentWindow;
	}
	if(p){
		var node = undefined;
		if(stdFlagFromMenu){
			node = p.getStdSelectedNode();
			if(node){
				params.eqId = node.obj.equipmentId;
				params.eqName = node.obj.equipmentName;
				params.eqNo = node.obj.equipmentCode;
				params.stdFlagFromMenu = 1;
			}
		}else{
			node = p.getSelectedNode();
			if(node){
				params.eqId = node.id;
				params.eqName = node.text;
				params.eqNo = node.assetCode;
			}
		}
	}
	var tabId = new Date().getTime();
	if(isStdWt == 1){
		tabId = "ptwInfoDetail" + tabId;
	}else{
		tabId = "ptwStdDetail" + tabId;
	}
	openNewTab(tabId,"新建工作票","ptw/ptwInfo/preQueryPtwInfo.do",params);
}

function exportPtwRecord(){
	if(isStdWt == 1){
		FW.error("不统计标准票");
		return;
	}
	var searchDateType = $("#searchDateType").iCombo('getVal');
	if(searchDateType != "createDate"){
		FW.error("统计工作记录报表时，请选择时间的查询类型为创建时间");
		return;
	}
	var searchDateFrom = $("#sDateFrom").val();
	var searchDateEnd = $("#sDateEnd").val();
	if(searchDateFrom == null || searchDateFrom == ''){
		FW.error("请选择一个月份导出工作记录");
		return;
	}
	var searchDateFromobj = new Date(searchDateFrom);
	var searchDateEndobj = new Date(searchDateEnd);
	var aMonthFlag = aMonthCheck(searchDateFromobj,searchDateEndobj);
	if(!aMonthFlag){
		FW.error("请按严格月份导出区间，如：（2015-05-01 ~ 2015-06-01）");
		return;
	}
	
	var exportWtType = $("#wtType").iCombo('getVal'); //导出工作票类型
	if(exportWtType==0||exportWtType==""){
		FW.error("请选择一种工作票类型");
		return;
	}
	
	var siteId = ItcMvcService.getUser().siteId;
	
	var url = fileExportPath + "preview?__report=report/TIMSS2_PTW_PTWJL.rptdesign&__format=pdf"
	+"&siteid="+siteId+"&searchDateFrom="+searchDateFrom+"&searchDateEnd="+searchDateEnd
	+"&wtTypeId="+exportWtType;

	var title ="工作记录导出"
	FW.dialog("init",{
		src: url,
		btnOpts:[{
	            "name" : "关闭",
	            "float" : "right",
	            "style" : "btn-default",
	            "onclick" : function(){
	                _parent().$("#itcDlg").dialog("close");
	             }
	        }],
		dlgOpts:{ width:800, height:650, closed:false, title:title, modal:true }
	});
}

function exportStaInfo(){
	if(isStdWt == 1){
		FW.error("不统计标准票");
		return;
	}
	var searchDateType = $("#searchDateType").iCombo('getVal');
	if(searchDateType != "createDate"){
		FW.error("统计报表时，请选择时间的查询类型为创建时间");
		return;
	}
	var searchDateFrom = $("#sDateFrom").val();
	var searchDateEnd = $("#sDateEnd").val();
	if(searchDateFrom == null || searchDateFrom == ''){
		searchDateFrom = new Date().getFullYear() + "-1-1";
	}
	
	var searchPersonType = $("#searchPersonType").iCombo('getVal');
	if(searchPersonType != "licWpicNo"){
		FW.error("统计报表时，请选择人员的查询类型为工作负责人");
		return;
	}
	var searchPersonId = $("#searchPersonName").iCombo('getVal');
	var searchPersonName = $("#searchPersonName").iCombo('getTxt');
	
	var siteId = ItcMvcService.getUser().siteId;
	var url = fileExportPath + "preview?__report=report/TIMSS2_PTW_STA.rptdesign&__format=pdf"
	+"&siteId="+siteId+"&searchDateFrom="+searchDateFrom+"&searchDateEnd="+searchDateEnd;
	if(searchPersonName == "全部"){
		url += "&__isnull=searchPersonName&__isnull=searchPersonId";
	}else{
		url += "&searchPersonName=" + searchPersonName+"&searchPersonId=" + searchPersonId;
	}
	//window.open(url);

	var title ="工作票统计"
	FW.dialog("init",{
		src: url,
		btnOpts:[{
	            "name" : "关闭",
	            "float" : "right",
	            "style" : "btn-default",
	            "onclick" : function(){
	                _parent().$("#itcDlg").dialog("close");
	             }
	        }],
		dlgOpts:{ width:800, height:650, closed:false, title:title, modal:true }
	});
}
