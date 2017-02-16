var columns = [[
		{field:'ck',width:10,checkbox:'true',fixed:true},
		{field:'itemid',title:'物资ID',hidden:true},
		{field:'itemcode',title:'物资编号',width:80,fixed:true,sortable:true},
		{field:'itemname',title:'物资名称',sortable:true,width:200},
		{field:'cusmodel',title:'型号规格',sortable:true,width:200,fixed:true},
		{field:'warehouseid',title:'仓库id',hidden:true},
		{field:'warehouse',title:'仓库名称',width:120,fixed:true},		
		{field:'cateName',title:'物资分类',width:80,fixed:true},		
		{field:'qtyStock',title:'可用库存量',sortable:true,width:85,fixed:true,align:'right',formatter:function(value){
			return parseFloat(value).toFixed(2);
		}},
		{
			field:'qtyLowInv',title:'安全库存量',sortable:true,width:85,fixed:true,align:'right',
			editor:{
				type:'text',options:{align:"right",dataType:"number"}
			},
			formatter:function(value){
				return parseFloat(value).toFixed(2);
			}
		},
		{field:'qtyEconomic',title:'经济订购量',width:85,fixed:true,align:'right',hidden:true,formatter:function(value){
			return parseFloat(value).toFixed(2);
		}},
		{field:'itemnum',title:'采购中数量',sortable:true,width:85,fixed:true,align:'right',formatter:function(value){
			return parseFloat(value).toFixed(2);
		}},
		{field:'unitname',title:'单位',sortable:true,width:55,fixed:true},
		{field:'price',title:'价格',hidden:true},
		{field:'cateId2',title:'物资分类id',hidden:true},
		{field:'status',title:'状态',width:90,hidden:true,fixed:true,formatter:function(value,row){
			return row.itemnum>0?"采购中":"未采购";
		}}
	]];
	
//初始化数据列表
function initItemGrid(){
	$("#item_grid").iDatagrid("init",{
		idField:'itemid',
		columns:columns,
		pageSize:pageSize,
		url: basePath+"inventory/invitem/querySafetyStock.do",
		queryParams: {
			search: FW.stringify({warehouse:currentWareHouseName}),
			type:currentRangeVal,
			itemInfo:currentItemInfo
		},
		onBeforeLoad: function(param){
			//如果处于编辑状态，则提示先保存，不允许翻页。
			if($('#btn_saveSafeQty').css('display')!="none"){
				FW.error( "请先保存当前页面安全库存值！" );
				return false;
			}
			else{
				return true;
			}
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
	            }else{
	            	$("#grid_wrap").show();
	                $("#grid_error").hide();
	            }
	            $("#noSearchResult").hide();
            }
            $(window).resize();
            changeCss();
            isSearchMode = false;
            //每次刷新完数据 编辑安全库存按钮显示，保存按钮隐藏
        	$("#btn_editSafeQty").show();
        	$("#btn_saveSafeQty").hide();
        	FW.fixToolbar("#toolbar1");
        },
        onSelect : function(){
        	$("#btn_purapply").attr("disabled",false);
        	FW.fixToolbar("#toolbar1");
        },
        onSelectAll : function(){
        	$("#btn_purapply").attr("disabled",false);
        	FW.fixToolbar("#toolbar1");
        },
        onUnselect : function(){
        	var selecteds = $("#item_grid").datagrid("getSelections");
        	if(selecteds.length<=0){
            	$("#btn_purapply").attr("disabled",true);
        		FW.fixToolbar("#toolbar1");
        	}
        },
        onUnselectAll : function(){
        	$("#btn_purapply").attr("disabled",true);
    		FW.fixToolbar("#toolbar1");
        },
        onDblClickRow:function(rowIndex, rowData){
        	FW.showInvMatDetailPage(rowData.itemid);
        }
	});
}