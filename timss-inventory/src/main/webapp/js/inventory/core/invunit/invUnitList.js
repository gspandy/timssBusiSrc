function initList(){
	$("#unit_grid").iDatagrid("init",{
		singleSelect:true,
		pageSize:pageSize,
		url: basePath+"inventory/invunit/queryList.do",
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
				$("#unit_grid").datagrid("resize"); 
			},200);
			isSearchMode = false;
        	$("#btn_enable").attr("disabled",true);
        	$("#btn_disable").attr("disabled",true);
		},
		onDblClickRow:function(rowIndex, rowData){
			var url = basePath+"inventory/invunit/invUnitForm.do?mode=view&id="+rowData.unitid;
			var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
			FW.addTabWithTree({
			        id : "editUnitForm" + prefix,
			        url : url,
			        name : "计量单位",
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
