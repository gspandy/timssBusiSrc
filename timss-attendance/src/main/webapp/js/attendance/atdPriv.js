//考勤机的权限
var MachinePriv={
		init:function(){
			MachinePriv.set();
			MachinePriv.apply();
		},
		
		set:function(){//定义权限
			//新建
			Priv.map("privMapping.ATD_MACHINE_NEW","VIRTUAL-NEW");
			//编辑
			Priv.map("privMapping.ATD_MACHINE_EDIT","VIRTUAL-EDIT");
			//删除
			Priv.map("privMapping.ATD_MACHINE_DELETE","VIRTUAL-DELETE");
		},
		
		apply:function(){//应用权限
			//应用
			Priv.apply();
			FW.fixToolbar(".btn-toolbar");
		}
};