/**
 * 标准操作票数据表格列
 */
var ptoListDataGridColumn = [[ 
	{field:"code",title:"编号",width:160,fixed:true,sortable:true},
	{field:"type",title:"类型",width:60,fixed:true,
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
    {field:"task",title:"操作任务",width:90},
    {field:"createuser",title:"开票人",width:70,fixed:true},
    {field:"createdate",title:"开票时间",width:150,fixed:true,sortable:true,
    	formatter: function(value,row,index){
			return FW.long2time(value);
		}},
    {field:"currStatus",title:"状态",width:100,fixed:true,sortable:true,
    	formatter: function(value,row,index){
        	return FW.getEnumMap("PTW_PTO_STATUS")[value]; 
        },
        "editor" : {
        	"type":"combobox",
            "options" : {
            	"data" : FW.parseEnumData("PTW_PTO_STATUS",_enum)	
            }
        }
     }
]];
var subColumns = [[
	   	{field:'operatorName',title:'操作人',width:250,fixed:true},
	   	{field:'guardianName',title:'监护人',width:250,fixed:true},
	   	{field:"beginOperTime",title:"操作开始时间",width:130,sortable:true,
	    	formatter: function(value,row,index){
				return FW.long2time(value);
			}},
		{field:"endOperTime",title:"操作结束时间",width:130,sortable:true,
	    	formatter: function(value,row,index){
				return FW.long2time(value);
			}}
]];
/**
 * 初始化数据表格
 */
function initDataGrid(){
	dataGrid = $("#ptolist_table").iDatagrid("init",{
		pageSize:pageSize,//pageSize为全局变量
	    singleSelect:true,
	    url: basePath + "ptw/ptoInfo/ptoListData.do",	//basePath为全局变量，自动获取的       
	    columns:ptoListDataGridColumn,
	    rowStyler : rowStyler,
	    onSelect:function(rowIndex,rowData){
	    	if(isNeedFlow){
	    		loadSubGridData(rowIndex);
	    	}
        },
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
			currentData = data;
			if(isNeedFlow){
				loadSubGridData(0);
			}else{
				$("#subGridDiv").hide();
			}
	    },
	    onDblClickRow : function(rowIndex, rowData) {
	    	toPtoBaseInfoPage(rowData);
		}
	});
	subGrid = $("#subPtwGrid").datagrid({
		columns:subColumns,
		fitColumns:true,
		border:false,
		scrollbarSize:0
	});

}
function rowStyler(rowIndex, rowData) {
	if (rowData.currStatus.indexOf("obsolete") >=0) {
		//已作废
		return 'color:#c0c0c0;';
	}

}
/**
 * 根据树的选择，对标准操作票信息进行查询
 */
function queryByTree(){
	headSearchParams.selectTreeId = selectAssetId;
    delete(_itc_grids["ptolist_table"]);
    var pager = $("#ptolist_table").datagrid("getPager"); 
    pager.pagination("select",1);
}
/**
 * 左边树菜单选择触发事件--这个是在woParamsConf/faultTypeTree.jsp中调用的
 */
function onTreeItemClick(data){
	headSearchParams = {};
    $("#ptolist_table").iDatagrid("endSearch");
    var rootflag = data.type;
	if(rootflag!='root'){
		selectAssetId = data.id;
		selectAssetName = data.text;
	}
	$("#ptolist_table").datagrid('options').queryParams.search = function(){
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
    delete(_itc_grids["ptolist_table"]);
    var pager = $("#ptolist_table").datagrid("getPager"); 
    pager.pagination("select",1);
}
/**
 * 关闭标签页后刷新
 */
function refreshAfterClose(){
	if(FW.get("PtolistDoNotRefresh")){
		FW.set("PtolistDoNotRefresh",false);
		return;
	}
	refresh();
}

/**
 * 打开新建操作票标签页
 */
function newPto(){
    //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var tabId = "newPto" + rand;
    var urlPath = basePath+ "ptw/ptoInfo/newPto.do";  
    var opts = {
        id : tabId,
        name : "新建操作票",
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
function toPtoBaseInfoPage(rowData){
	var id = rowData.id;
	  //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    
    var opts = {
        id : "ptwPto" + rand,
        name : "操作票",
        url : basePath+ "ptw/ptoInfo/todolistToPtoPage.do?id="+id,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('ptw');FW.getFrame('ptw').refreshAfterClose();"
        }
    }
    _parent()._ITC.addTabWithTree(opts); 
}


/**操作票基本信息展示的subGrid*/
function loadSubGridData(index){
	if(currentData != undefined && currentData != null && currentData.total > 0){
		$("#subGridDiv").show();
		var data = {"rows":[currentData.rows[index]],"total":1};
		subGrid.datagrid('loadData',data);
	}else{
		$("#subGridDiv").hide();
	}
}