var PurVendorPriv={
	init:function(){
		PurVendorPriv.set();
		PurVendorPriv.apply();
	},
	set:function(){//定义权限
		//编辑
		Priv.map("privMapping.companyinfo_modify","companyinfo_modify");
		//提交
		Priv.map("privMapping.companyinfo_commitApply","companyinfo_commitApply");
		//新建
		Priv.map("privMapping.companyinfo_new","companyinfo_new");
	},
	apply:function(){//应用权限
		//应用
		Priv.apply();
	}
};