var InvUnitBtn={
	initList:function(){
		InvUnitBtn.disable();
		InvUnitBtn.enable();
		InvUnitBtn.search();
	},
	search:function(){
		$("#btn_search").click(function(){
		    if(isSearchLineShow){
		    	isSearchLineShow=false;
		        $("#unit_grid").iDatagrid("endSearch");		        
		    }
		    else{
		    	isSearchLineShow=true;
		       	$("#unit_grid").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
		       		isSearchMode = true;
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
	},
	enable:function(){
		$("#btn_enable").click(function(){
			   changeUnitState('Y');
		});
	},
	disable:function(){
		$("#btn_disable").click(function(){
			   changeUnitState('N');
		});
	}
};
