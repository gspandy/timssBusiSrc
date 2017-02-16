var InvBinBtn={
	initList:function(){
		InvBinBtn.search();
		InvBinBtn.create();
	},
	search:function(){
		$("#btn_search").click(function(){
		    if(isSearchLineShow){
		    	isSearchLineShow=false;
		        $("#bin_grid").iDatagrid("endSearch");		        
		    }
		    else{
		    	isSearchLineShow=true;
		       	$("#bin_grid").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
		       		isSearchMode = true;
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
	},
	create:function(){
		$("#btn_new").click(function(){
			createNewBin();
		});
	}
};
