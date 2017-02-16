var InvUnitPriv={
	init:function(){
		InvUnitPriv.set();
		InvUnitPriv.apply();
	},
	set:function(){//定义权限
		//新建
		Priv.map("privMapping.unitList_new","unitList_new");
		//启用
		Priv.map("privMapping.unitList_enable","unitList_enable");
		//停用
		Priv.map("privMapping.unitList_disable","unitList_disable");
	},
	apply:function(){//应用权限
		//应用
		Priv.apply();
	}
};