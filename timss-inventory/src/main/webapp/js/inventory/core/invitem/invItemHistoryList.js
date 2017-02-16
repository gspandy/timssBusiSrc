//初始化列表字段
var columns = [[
		{field:'ck',width:10,checkbox:'true',fixed:true},
		{field:'itemcode',title:'物资编号',width:80,sortable:true,fixed:true
			,formatter:function(value,row){
				return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\",\""+row.invcateid+"\");'>"+row.itemcode+"</a>";
			}
		},
		{field:'invcateid',title:'物资类型',width:70,fixed:true,hidden:true},
		{field:'itemname',title:'物资名称',width:200,sortable:true},
		{field:'cusmodel',title:'型号规格',width:260,fixed:true,sortable:true},
		{field:'stockqty',title:'库存数量',width:60,fixed:true,sortable:true,align:'right',
			formatter:function(value,row){
				if(null === value || "" === value){
					return "--";
				}else{
					return value;
				}
			}},
		{field:'unitname',title:'单位',width:55,fixed:true},
		{field:'price',title:'平均成本(元)',width:90,fixed:true,sortable:true,align:'right',
			formatter:function(value,row){
				if(null === value || "" === value){
					return "--";
				}else{
					return value;
				}
		}},
		{field:'bin',title:'货柜',width:80,fixed:true,hidden:true},
		{field:'warehouse',title:'仓库',width:130,fixed:true},
		{field:'warehouseid',title:'仓库id',hidden:true},
		{field:'sparecode',title:'设备编号',width:70,fixed:true,hidden:true},
		{field:'manufacturer',title:'生产厂商',width:90,fixed:true,hidden:true}
	]];

//初始化数据列表
function initItemGrid(){
	var flagSelect = false;
	var	gridColumns = columns;
	var	gridUrl = basePath+"inventory/invitem/queryItemsList.do";

	$("#item_grid").iDatagrid("init",{
		singleSelect:flagSelect,
		columns:gridColumns,
		pageSize:pageSize,
		url: gridUrl,
		queryParams: {"embbed":embbed,"opentype":opentype,"ishis":1},
		onLoadSuccess: function(data){
            if(isSearchMode){
            	 if(data && data.total==0){
            		 $("#noSearchResult").show();
            	 }
            	 else{
            		 $("#noSearchResult").hide();
            	 }
            }
            else{
	            if(data && data.total==0){
	                $("#grid_wrap,#toolbar_wrap").hide();
	                $("#grid_error").show();
	            }else{
	            	$("#toolbar_wrap,#grid_wrap").show();
	                $("#grid_error").hide();
	            }
	            $("#noSearchResult").hide();
            }
            isSearchMode = false;
            setTimeout(function(){
            	$("#item_grid").datagrid("resize");
            },200);
        }
	});
}