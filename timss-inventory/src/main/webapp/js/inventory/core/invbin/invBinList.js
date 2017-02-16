function initList(){
	$("#bin_grid").iDatagrid("init",{
		singleSelect:true,
		pageSize:pageSize,
		url: basePath+"inventory/invbin/queryList.do",
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
				$("#bin_grid").datagrid("resize"); 
			},200);
			isSearchMode = false;
		},
		onDblClickRow:function(rowIndex, rowData){
			var url = basePath+"inventory/invbin/invBinForm.do?mode=view&id="+rowData.binid;
			var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
			FW.addTabWithTree({
			        id : "editBinForm" + prefix,
			        url : url,
			        name : "货柜",
			        tabOpt : {
			            closeable : true,
			            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
			        }
			});
		}
	});
}