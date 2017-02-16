//资产台账的权限
var AssetPriv={
		init:function(){
			AssetPriv.set();
			AssetPriv.apply();
		},
		
		set:function(){//定义权限
			//新建
			Priv.map("privMapping.AST_NEW","VIRTUAL-NEW");
			//编辑
			Priv.map("privMapping.AST_EDIT","VIRTUAL-EDIT");
			//删除
			Priv.map("privMapping.AST_DELETE","VIRTUAL-DELETE");
			//移动
			Priv.map("privMapping.AST_MOVE","VIRTUAL-MOVE");
			//领用
			Priv.map("privMapping.AST_BORROW","VIRTUAL-BORROW");
			//归还
			Priv.map("privMapping.AST_RETURN","VIRTUAL-RETURN");
			//动态表单配置
			Priv.map("privMapping.AST_CONFIG","VIRTUAL-CONFIG");
		},
		
		apply:function(){//应用权限
			//应用
			Priv.apply();
		}
};

//软件台账的权限
var SwLedgerPriv={
		init:function(){
			SwLedgerPriv.set();
			SwLedgerPriv.apply();
		},
		
		set:function(){//定义权限
			//新建
			Priv.map("privMapping.AST_SWL_NEW","VIRTUAL-NEW");
			//编辑
			Priv.map("privMapping.AST_SWL_EDIT","VIRTUAL-EDIT");
			//删除
			Priv.map("privMapping.AST_SWL_DELETE","VIRTUAL-DELETE");
			//导出硬件台账，拥有最高权限
			//Priv.map("privMapping.AST_SWL_NEW&&privMapping.AST_SWL_EDIT&&privMapping.AST_SWL_DELETE","AST_SWL_EXPORT");
			Priv.map("hasPrivilege('VIRTUAL-NEW')&&hasPrivilege('VIRTUAL-EDIT')&&hasPrivilege('VIRTUAL-DELETE')","AST_SWL_EXPORT");
		},
		
		apply:function(){//应用权限
			//应用
			Priv.apply();
			FW.fixToolbar(".btn-toolbar");
		}
};

//硬件台账的权限
var HwLedgerPriv={
		init:function(){
			HwLedgerPriv.set();
			HwLedgerPriv.apply();
		},
		
		set:function(){//定义权限
			//新建
			Priv.map("privMapping.AST_HWL_NEW","AST_HWL_NEW");
			//编辑
			Priv.map("privMapping.AST_HWL_EDIT","AST_HWL_EDIT");
			//导出硬件台账
			Priv.map("hasPrivilege('AST_HWL_NEW')&&hasPrivilege('AST_HWL_EDIT')&&rootFlag","AST_HWL_EXPORT");
		},
		
		apply:function(){//应用权限
			//应用
			Priv.apply();
		}
};

//硬件类型维护的权限
var HwModelPriv={
		init:function(){
			HwModelPriv.set();
			HwModelPriv.apply();
		},
		
		set:function(){//定义权限
			//新建
			Priv.map("privMapping.AST_HWM_NEW","VIRTUAL-NEW");
			//编辑
			Priv.map("privMapping.AST_HWM_EDIT","VIRTUAL-EDIT");
			//删除
			Priv.map("privMapping.AST_HWM_DELETE","VIRTUAL-DELETE");
		},
		
		apply:function(){//应用权限
			//应用
			Priv.apply();
			FW.fixToolbar(".btn-toolbar");
		}
};