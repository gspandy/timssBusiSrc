var InvBinPriv={
	init:function(){
		InvBinPriv.set();
		InvBinPriv.apply();
	},
	set:function(){//定义权限
		//新建
		Priv.map("privMapping.binList_new","binList_new");
	},
	apply:function(){//应用权限
		//应用
		Priv.apply();
	}
};

//新建新的货柜
function createNewBin(){
	var url = basePath+"inventory/invbin/invBinForm.do?mode=create";
	var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
	FW.addTabWithTree({
	        id : "newBinForm" + prefix,
	        url : url,
	        name : "货柜",
	        tabOpt : {
	            closeable : true,
	            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
	        }
	});
}
