var InvWarehousePriv={
	init:function(){
		InvWarehousePriv.set();
		InvWarehousePriv.apply();
	},
	set:function(){//定义权限
		//新建
		Priv.map("privMapping.storeList_new","storeList_new");
		//启用
		Priv.map("privMapping.storeList_enable","storeList_enable");
		//停用
		Priv.map("privMapping.storeList_disable","storeList_disable");
	},
	apply:function(){//应用权限
		//应用
		Priv.apply();
	}
};