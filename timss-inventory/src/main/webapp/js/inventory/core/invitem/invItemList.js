var columns = embbed == '1'? [[
	{field:'ck',width:10,checkbox:'true',fixed:true},
	{field:'listId',title:'多类型列表id',hidden:true},
	{field:'itemcode',title:'物资编号',width:80,sortable:true,fixed:true
		,formatter:function(value,row){
			return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\",\""+row.cateId+"\");'>"+row.itemcode+"</a>";
		}
	},
	{field:'itemname',title:'物资名称',width:200,fixed:true,sortable:true},
	{field:'cusmodel',title:'型号规格',width:230,fixed:true,sortable:true},
	{field:'cateType',title:'物资类型',width:140,fixed:true},
	{field:'stockqty',title:'实际库存',width:50,fixed:true,sortable:true,align:'right',
		formatter:function(value,row){
			if(null === value || "" === value){
				return "--";
			}else{
				return value;
			}
		}},
	{field:'nowqty',title:'可用库存',width:50,fixed:true,sortable:true,
		formatter:function(value,row){
			if(null === value || "" === value){
				return "--";
			}else{
				return value;
			}
		}
	},
	{field:'qtyLowInv',title:'安全库存量',fixed:true,hidden:true},
	{field:'unitname',title:'计量单位',width:55,fixed:true,hidden:true},
	{field:'noTaxPrice',title:'不含税单价',width:80,fixed:true,hidden:true},
	{field:'price',title:'平均成本(元)',width:80,fixed:true,hidden:true,
		formatter:function(value,row){
			if(null === value || "" === value){
				return "--";
			}else{
				return value;
			}
		}},
	{field:'bin',title:'货柜',width:80,fixed:true,hidden:true},
	{field:'warehouse',title:'默认仓库',width:150,fixed:true,hidden:true},
	{field:'sparecode',title:'设备编号',width:70,fixed:true,hidden:true},
	{field:'manufacturer',title:'生产厂商',width:90,fixed:true,hidden:true},
	{field:'warehouseid',title:'仓库编码',fixed:true,hidden:true},
	{field:'cateId',title:'类型id',fixed:true,hidden:true},
	{field:'cateId2',title:'类型id2',fixed:true,hidden:true}
]]:
	[[
		{field:'ck',width:10,checkbox:'true',fixed:true},
		{field:'listId',title:'多类型列表id',hidden:true},
		{field:'itemcode',title:'物资编号',width:80,sortable:true,fixed:true
			,formatter:function(value,row){
				return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\",\""+row.cateId+"\");'>"+row.itemcode+"</a>";
			}
		},
		{field:'itemid',title:'物资id',width:10,fixed:true,hidden:true},
		{field:'itemname',title:'物资名称',width:40,sortable:true},
		{field:'cusmodel',title:'型号规格',width:60,sortable:true},
		{field:'cateType',title:'物资类型',width:100,fixed:true},
		{field:'stockqty',title:'实际库存',width:60,fixed:true,sortable:true, align:'right',
			formatter:function(value,row){
				if(null === value || "" === value){
					return "--";
				}else{
					return value;
				}
			}},
		{field:'nowqty',title:'可用库存',width:60,fixed:true,sortable:true, align:'right',
			formatter:function(value,row){
				if(null === value || "" === value){
					return "--";
				}else{
					return value;
				}
			}
		},
		{field:'qtyLowInv',title:'安全库存量',width:80,fixed:true,sortable:true,hidden:true},
		{field:'unitname',title:'单位',width:55, fixed:true},
		{field:'price',title:'含税单价',width:70,fixed:true,sortable:true, align:'right',
			formatter:function(value,row){
				if(null === value || "" === value){
					return "--";
				}else{
					return parseFloat(value).toFixed(2);;
				}
			}
		},
		{field:'noTaxPrice',title:'不含税单价',width:80,fixed:true, align:'right',
			formatter:function(value,row){
				if(null === value || "" === value){
					return "--";
				}else{
					return parseFloat(value).toFixed(2);
				}
			}
		},
		{field:'bin',title:'货柜',width:80,fixed:true,hidden:true},
		{field:'warehouse',title:'默认仓库',width:130,fixed:true},
		{field:'sparecode',title:'设备编号',width:70,fixed:true,hidden:true},
		{field:'manufacturer',title:'生产厂商',width:90,fixed:true,hidden:true},
		{field:'warehouseid',title:'仓库编码',fixed:true,hidden:true},
		{field:'cateId',title:'类型id',fixed:true,hidden:true},
		{field:'cateId2',title:'类型id2',fixed:true,hidden:true}
	]];

//物资类型初始列表	
var whcolumns = [[
		{field:'ck',title:'id',hidden:true},
		{field:'warehousename',title:'仓库名称',width:200,fixed:true},
		{field:'descriptions',title:'仓库描述',width:300},
		{field:'active',title:'状态',width:70,fixed:true
			,formatter:function(value,row){
				if(row.active == 'Y'){ 
					return '启用';
				}else{ 
					return '停用';
				}
			}
		}
]];


//初始化数据列表
function initItemGrid(){
	var flagSelect = false;
	var gridColumns = null;
	var gridUrl = null;
	var param = null;
	var idFields = null;
	if("categroy" == opentype){
		$("#btn_search").hide();
		gridColumns = whcolumns;
		gridUrl = basePath+"/inventory/invwarehouse/queryWarehouse.do";
		flagSelect = true;
		idFields = "warehousename";
		param = {"embbed":embbed,"opentype":opentype,"ishis":2};//当等于2的时候不再按照ishis来查询
	}else{
		gridColumns = columns;
		gridUrl = basePath+"inventory/invitem/queryItemsList.do";
		
		if("new" == opentype){
			param = {"embbed":embbed,"opentype":opentype,"ishis":2};//当等于2的时候不再按照ishis来查询
			
			
			gridColumns = [[
			            	{field:'ck',width:10,checkbox:'true',fixed:true},
							{field:'itemcode',title:'物资编号',width:80,sortable:true,fixed:true},
							{field:'itemid',title:'物资id',width:10,fixed:true,hidden:true},
							{field:'itemname',title:'物资名称',width:250,fixed:true,sortable:true},
							{field:'cusmodel',title:'型号规格',width:350,fixed:true,sortable:true},
							{field:'unitname',title:'计量单位',width:55}
			          ]];
			idFields = "itemid";
			
		}else{
			param = {"embbed":embbed,"opentype":opentype,"ishis":0};
			idFields = "listId";
		}
		
		flagSelect = false;
	}

	$("#item_grid").iDatagrid("init",{
		singleSelect:flagSelect,
		columns:gridColumns,
		pageNumber:pageNum,
		pageSize:pageSize,
		url: gridUrl,
		queryParams: param,
		idField:idFields,
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
            pageNum = $("#item_grid").datagrid("getPager").data("pagination").options.pageNumber;
            setTimeout(function(){
            	$("#item_grid").datagrid("resize");
            },200);
        },
		onSelect:function(rowIndex, rowData){
        	$("#tohistory").attr("disabled",false);
        	FW.fixToolbar("#toolbar1");
		},
        onSelectAll : function(){
        	$("#tohistory").attr("disabled",false);
        	FW.fixToolbar("#toolbar1");
        },
		onUnselect:function(){
        	var selecteds = $("#item_grid").datagrid("getSelections");
        	if(selecteds.length<=0){
            	$("#tohistory").attr("disabled",true);
        		FW.fixToolbar("#toolbar1");
        	}
		},
        onUnselectAll : function(){
        	$("#tohistory").attr("disabled",true);
    		FW.fixToolbar("#toolbar1");
        }
	});
}