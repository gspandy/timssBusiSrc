/**
 * 标准操作票对话框数据表格列
 */
var sptoListDlgDataGridColumn = [[ 
	{field:"id","hidden":"true"},
	{field:"code",title:"编号",width:150,fixed:true,sortable:true},
	{field:"type",title:"类型",width:80,fixed:true,sortable:true,
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
    {field:"mission",title:"操作任务",width:90,sortable:true},
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
	    queryParams:{"search":JSON.stringify({'_status':'passed'})},
	    columns:sptoListDlgDataGridColumn,
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
	    }
	});
}