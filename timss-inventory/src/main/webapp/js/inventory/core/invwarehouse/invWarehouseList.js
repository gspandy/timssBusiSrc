function initList(){
	$("#warehouse_grid").iDatagrid("init",{
		singleSelect:true,
		pageSize:pageSize,
		url: basePath+"inventory/invwarehouse/queryList.do",
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
	                $("#grid1_wrap,#toolbar_wrap").hide();
	                $("#grid1_empty").show();
	            }else{
	            	$("#grid1_wrap,#toolbar_wrap").show();
	                $("#grid1_empty").hide();
	            }
				$("#noSearchResult").hide();
			}
			setTimeout(function(){ 
				$("#warehouse_grid").datagrid("resize"); 
			},200);
			isSearchMode = false;
        	$("#btn_enable").attr("disabled",true);
        	$("#btn_disable").attr("disabled",true);
		},
		onDblClickRow:function(rowIndex, rowData){
			var url = basePath+"inventory/invwarehouse/invWarehouseForm.do?mode=view&id="+rowData.warehouseid;
			var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
			FW.addTabWithTree({
			        id : "editWarehouseForm" + prefix,
			        url : url,
			        name : "仓库",
			        tabOpt : {
			            closeable : true,
			            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
			        }
			});
		},
		onSelect:function(rowIndex, rowData){
			changeStateBtnState(rowData.active);
			FW.fixToolbar("#toolbar1");
		},
		onUnselect:function(){
			$("#btn_enable,#btn_disable").hide();
			FW.fixToolbar("#toolbar1");
		}
	});
	
}