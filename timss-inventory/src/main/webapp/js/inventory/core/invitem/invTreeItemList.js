var columns =embbed == '1'? [[
		{field:'ck',width:10,checkbox:'true',fixed:true},
		{field:'listId',title:'多类型列表id',hidden:true},
		{field:'itemcode',title:'物资编号',width:80,sortable:true,fixed:true
			,formatter:function(value,row){
				return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\",\""+row.cateId+"\");'>"+row.itemcode+"</a>";
			}
		},
		{field:'itemid',title:'物资id',width:10,fixed:true,hidden:true},
		{field:'itemname',title:'物资名称',width:200,fixed:true,sortable:true},
		{field:'cusmodel',title:'型号规格',width:230,sortable:true},
		{field:'cateType',title:'物资类型',width:120,fixed:true},
		{field:'stockqty',title:'实际库存',width:60,fixed:true,sortable:true,
			formatter:function(value,row){
				if(null == value){
					return 0;
				}else{
					return value;
				}
			}},
		{field:'nowqty',title:'可用库存',width:60,fixed:true,sortable:true,
			formatter:function(value,row){
				if(null == value){
					return 0;
				}else{
					return value;
				}
			}
		},
		{field:'qtyLowInv',title:'安全库存量',fixed:true,hidden:true},
		{field:'unit1',title:'单位id',hidden:true},
		{field:'unitname',title:'单位',width:55,fixed:true,hidden:true},
		{field:'noTaxPrice',title:'不含税单价',width:80,fixed:true,hidden:true},
		{field:'price',title:'平均成本(元)',width:90,fixed:true,hidden:true},
		{field:'binid',title:'货柜id',fixed:true,hidden:true},
		{field:'bin',title:'货柜',fixed:true,hidden:true},
		{field:'warehouse',title:'默认仓库',width:150,hidden:true},
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
		{field:'unit1',title:'单位id',hidden:true},
		{field:'unitname',title:'单位',width:55,fixed:true},
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

//初始化数据列表
function initGrid(cateId,wareId){
	var flagSelect = false;
	$("#item_grid").iDatagrid("init",{
		singleSelect:flagSelect,
		columns:columns,
		pageNumber:pageNum,
		pageSize:pageSize,
		fitColumns : true,
		idField:"listId",
		url: basePath+"/inventory/invitem/queryItemsList.do",
		queryParams: {"cateId":cateId,
					  "embbed":embbed,
					  "opentype":opentype,
					  "isspare":isspare,
					  "invmatapply":invmatapply,
					  "categoryname":categoryname,
					  "ishis":0,
					  "warehouseId" :wareId,
					  "active": active
					  },
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
	                $("#grid_wrap").hide();
	                $("#grid_error").show();
	                $("#toolbar_wrap").hide();
	            }else{
	            	$("#toolbar_wrap").show();
	            	$("#grid_wrap").show();
	                $("#grid_error").hide();
	            }
	            $("#noSearchResult").hide();
			 }
			 isSearchMode = false;
            //渲染后修正datagrid的宽度
            setTimeout(function(){
            	$("#item_grid").datagrid("resize");
            },200);
            
            pageNum = $("#item_grid").datagrid("getPager").data("pagination").options.pageNumber;
        },
		onSelect:function(rowIndex, rowData){
        	$("#tohistory").attr("disabled",false);
        	FW.fixToolbar("#toolbar");
		},
        onSelectAll : function(){
        	$("#tohistory").attr("disabled",false);
        	FW.fixToolbar("#toolbar");
        },
		onUnselect:function(){
        	var selecteds = $("#item_grid").datagrid("getSelections");
        	if(selecteds.length<=0){
            	$("#tohistory").attr("disabled",true);
        		FW.fixToolbar("#toolbar");
        	}
		},
        onUnselectAll : function(){
        	$("#tohistory").attr("disabled",true);
    		FW.fixToolbar("#toolbar");
        }
	});
	
	if(embbed=='1'){
		$("#item_grid").datagrid('hideColumn','price');
		$("#item_grid").datagrid('showColumn','ck');
		$("#item_grid").datagrid('hideColumn','nowqty');
	}
}