/**
 * 标准操作票数据表格列
 */
var sptoListDataGridColumn = [[ 
	{field:"code",title:"编号",width:120,fixed:true,sortable:true},
	{field:"type",title:"类型",width:70,fixed:true,sortable:true,
    	formatter: function(value,row,index){
        	return FW.getEnumMap("PTW_SPTO_TYPE")[value];
        },
        "editor" : {
        	"type":"combobox",
            "options" : {
            	"data" : FW.parseEnumData("PTW_SPTO_TYPE",_enum)
            }
        }	
    }, 
    {field:"mission",title:"操作任务",width:90},
    {field:"createUserName",title:"编写人",width:70,fixed:true},
    {field:"auditUserName",title:"审核人",width:70,fixed:true},
    {field:"permitUserName",title:"批准人",width:70,fixed:true},
    {field:"status",title:"状态",width:100,fixed:true,sortable:true,
    	formatter: function(value,row,index){
        	return FW.getEnumMap("PTW_SPTO_STATUS")[value]; 
        },
        "editor" : {
        	"type":"combobox",
            "options" : {
            	"data" : FW.parseEnumData("PTW_SPTO_STATUS",_enum)	
            }
        }
     }
]];
/**
 * 初始化数据表格
 */
function initDataGrid(){
	dataGrid = $("#sptord_table").iDatagrid("init",{
		pageSize:pageSize,//pageSize为全局变量
	    singleSelect:true,
	    url: basePath + "ptw/sptoInfo/sptoRdListData.do",	//basePath为全局变量，自动获取的       
	    columns:sptoListDataGridColumn,
	    rowStyler : rowStyler,
	    onLoadSuccess: function(data){
	    	if(isSearchMode){
				if(data && data.total==0){
			    	$("#noSearchResult").show();
			    }else{
			        $("#noSearchResult").hide();
			 	}
			}else{
			    $("#noSearchResult").hide();
		        if(data && data.total==0){
		            $("#mainContent").hide();
		            $("#grid1_empty").show();
		        }else{
		            $("#mainContent").show();
		            $("#grid1_empty").hide();
		        }
			}
			isSearchMode = false;
	    },
	    onDblClickRow : function(rowIndex, rowData) {
	    	toSptoBaseInfoPage(rowData);
		}
	});
}
function rowStyler(rowIndex, rowData) {
	if (rowData.status.indexOf("zuofei") >=0) {
		//已作废
		return 'color:#c0c0c0;';
	}

}
/**
 * 根据树的选择，对标准操作票信息进行查询
 */
function queryByTree(){
	headSearchParams.selectTreeId = selectAssetId;
    delete(_itc_grids["sptord_table"]);
    var pager = $("#sptord_table").datagrid("getPager"); 
    pager.pagination("select",1);
}
/**
 * 左边树菜单选择触发事件--这个是在woParamsConf/faultTypeTree.jsp中调用的
 */
function onTreeItemClick(data){
	headSearchParams = {};
    $("#sptord_table").iDatagrid("endSearch");
    var rootflag = data.type;
	if(rootflag!='root'){
		selectAssetId = data.id;
		selectAssetName = data.text;
	}else{
		selectAssetId = null;
		selectAssetName = null;
	}
	$("#sptord_table").datagrid('options').queryParams.search = function(){
       return JSON.stringify(headSearchParams);
    };
    queryByTree();
}
/**
 * 刷新数据方法 被下面你的方法调用
 */
function refresh(){
	headSearchParams = {};
	$("#mainContent").show();
	$("#grid1_empty").hide();
	//这里的mytable要换成datagrid对应的id
    delete(_itc_grids["sptord_table"]);
    var pager = $("#sptord_table").datagrid("getPager"); 
    pager.pagination("select",1);
}
/**
 * 关闭标签页后刷新
 */
function refreshAfterClose(){
	if(FW.get("SptolistDoNotRefresh")){
		FW.set("SptolistDoNotRefresh",false);
		return;
	}
	refresh();
}

/**
 * 打开新建标准操作票标签页
 */
function newSpto(){
    //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var categoryParams = JSON.stringify({"categoryId":selectAssetId,"categoryName":selectAssetName});
    var tabId = "initNewSpto" + rand;
    var urlPath = basePath+ "ptw/sptoInfo/newSpto.do?categoryParams="+encodeURIComponent(categoryParams);  
    var opts = {
        id : tabId,
        name : "新建标准操作票",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('ptw');FW.getFrame('ptw').refreshAfterClose();"
        }
    };
    _parent()._ITC.addTabWithTree(opts); 

};
/**
 * 打开基本信息标签页
 */
function toSptoBaseInfoPage(rowData){
	var id = rowData.id;
	//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var opts = {
        id : "ptwSpto" + rand,
        name : "标准操作票",
        url : basePath+ "ptw/sptoInfo/todolistTOSptoPage.do?id="+id,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('ptw');FW.getFrame('ptw').refreshAfterClose();"
        }
    };
    _parent()._ITC.addTabWithTree(opts); 
}
