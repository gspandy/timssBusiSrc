var InvWarehouseBtn={
	initList:function(){
		InvWarehouseBtn.disable();
		InvWarehouseBtn.enable();
		InvWarehouseBtn.search();
	},
	search:function(){
		$("#btn_search").click(function(){
		    if(isSearchLineShow){
		    	isSearchLineShow=false;
		        $("#warehouse_grid").iDatagrid("endSearch");		        
		    }
		    else{
		    	isSearchLineShow=true;
		       	$("#warehouse_grid").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
		       		isSearchMode = true;
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
	},
	enable:function(){
		$("#btn_enable").click(function(){
		    changeWarehouseState('Y');
		});
	},
	disable:function(){
		$("#btn_disable").click(function(){
			confirmBeforeChangeWare('N');
		});
	}
};
