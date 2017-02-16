var grid=[[
		{title:'物资id',field:'itemid',width:90,hidden:true,fixed:true},
		{title:'物资编号',field:'itemcode',width:90,fixed:true,formatter:function(value,row){
			return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\",\""+row.invcateid+"\");'>"+row.itemcode+"</a>";
		}},
		{title:'物资名称',field:'itemname',width:90},
		{title:'物资型号',field:'cusmodel',width:70},
		{title:'物资分类',field:'invcateid',hidden:true},
		{title:'货柜id',field:'binid',hidden:true},
		{title:'仓库id',field:'warehouseid',hidden:true},
		{title:'货柜',field:'bin',width:70,fixed:true},
		{title:'单位',field:'unitname',width:60,fixed:true},
		{title:'单位id',field:'unitid',hidden:true},
		{title:'实际库存',field:'stockqty',hidden:true},
		{title:'可用库存',field:'nowqty',width:80,align:'right',fixed:true},
		{title:'账面库存量',field:'qtyBefore',width:80,align:'right',fixed:true},
		{title:'实盘库存量',field:'qtyAfter',width:80,align:'right',fixed:true,edit:true,editor:{type:'text',
			options:{
				align:"right",
				dataType:"number",
				onBlur:changeCss,
				rules : {
					required : true,
					min:0,
					messages : {required:"实盘库存量不能为空",min:"实盘库存量为正数"}
					}	
				}
			}
		},
		{title:'盈亏',field:'breakeven',width:80,fixed:true,align:'right',styler: function(value){
			if (value > 0){
				return 'color:green';
			}else if(value < 0){
				return 'color:red';
			}
		}},
		{title:'单价(元)',field:'price',hidden:true},
		{title:'调整原因',field:'remark',width:140,fixed:true,edit:true,editor:{type:'text',"options":{
			rules : {
				required : true,
				messages : {required:"调整原因不能为空"}
			}	
		}}},
		{title:'',align:'center',field:'del',width:40,fixed:true,formatter:function(value,row){
			return "<img class='btn-delete btn-garbage' onclick='delRecord(\""+row.itemid+"\",\""+row.itemname+"\");' src='"+basePath+"img/inventory/btn_garbage.gif'/>";
		}}
]];

//初始化列表（通用方法）
function initList(){
	$("#stocktakingdetail_list").iFold("init");
	$("#stocktakingdetail_grid").datagrid({
		fitColumns : true,
		columns:grid,
		idField:'itemid',
		singleSelect:true,
		url : basePath+"inventory/invstocktakingdetail/queryStocktakingDetail.do",
		queryParams: {"istid":istid},
		onLoadSuccess: function(data){
            if(data && data.total==0){
                $("#stocktakingdetail_grid").hide();
            }
			
			if("" != istid && "editable"==isEdit){
				if(processStatus != "over"){
					$("#btn_edit").trigger("click");
				}
			}
			
			if(processStatus == "over" || "editable"!=isEdit){
				$("#stocktakingdetail_grid").datagrid("hideColumn","del");
			}
			setTimeout(function(){ 
				$("#stocktakingdetail_grid").datagrid("resize"); 
			},200);
			FW.fixToolbar("#toolbar");
			var listData =$("#stocktakingdetail_grid").datagrid("getRows");
			initListStatus = FW.stringify(listData);
        }
	});
}

//列表页面查询
function initStocktakingList(){
	$("#invstocktaking_grid").iDatagrid("init",{
		singleSelect:true,
		pageSize:pageSize,
		url: basePath+"inventory/invstocktaking/queryStocktakingList.do",
		onLoadSuccess:function(data){
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
	            	$("#grid_wrap,#toolbar_wrap").show();
	                $("#grid_error").hide();
	            }
	            $("#noSearchResult").hide();
			}
			setTimeout(function(){ 
				$("#invstocktaking_grid").datagrid("resize"); 
			},200);
			isSearchMode = false;
		},
		onDblClickRow : function(rowIndex, rowData) {
			var url = basePath+ "inventory/invstocktaking/invStocktakingForm.do?istid="+rowData.istid;
	    	var prefix = rowData.istid;
		    FW.addTabWithTree({
		        id : "editStocktakingForm" + prefix,
		        url : url,
		        name : "库存盘点",
		        tabOpt : {
		            closeable : true,
		            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
		        }
		    });
		}
	});
}