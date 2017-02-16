var InvAcceptPriv={
	init:function(data){
		InvAcceptPriv.set(data);
		InvAcceptPriv.apply(data);
	},
	initFormBefore:function(data){
		InvAcceptPriv.initPrintButtion(data);
		InvAcceptPriv.initSubmitButtion(data);
		InvAcceptPriv.initApprovalButtion(data);
		InvAcceptPriv.initVoidButtion(data);
		InvAcceptPriv.initAddDetailButtion(data);
		FW.fixToolbar("#toolbar");
	},
	initFormAfter:function(data){
		
	},
	initAddDetailButtion:function(data){
		if(data.isEdit && !InvAcceptPriv.isVoidStatus(data)){
			$("#btn_add").hide();
		}
	},
	initPrintButtion:function(data){
		if(!data.isEdit){
			$("#btn_print").hide();
		}
	},
	initVoidButtion:function(data){
		
		if(!InvAcceptPriv.isVoidStatus(data)){
			
			$("#btn_void").hide();
		}
	},
	isVoidStatus:function(data){
		var modi=InvAcceptPriv.getFlowElementMod(data);
		var firstNode= modi && modi.firstNode;
		var res=data.isEdit && firstNode && data.createuseid== data.curUserId ;
	
		return res;
	},
	//审批完成
	isPassApproved:function(data){
		if(data.isEdit){
			var modi=InvAcceptPriv.getFlowElementMod(data);
			//没有流程节点信息
			if(!modi){
				//不是作废状态
				if(data.status!='voided'){
					return true;
				}
			}
		}
		return false;
	},
	initDeleteButtion:function(data){
		if(!InvAcceptPriv.isDeleteStatus(data)){
			$("#btn_delete").hide();
		}
		
	},
	initSubmitButtion:function(data){
		if(!InvAcceptPriv.isSubmitStatus(data)){
			$("#btn_submit").hide();
		}
		
	},
	isSubmitStatus:function(data){
		var modi=InvAcceptPriv.getFlowElementMod(data);
		var firstNode= modi && modi.firstNode;
		var res=(!data.isEdit) || (data.isEdit && firstNode && data.createuseid== data.curUserId ) ;
	
		return res;
	},
	isDeleteStatus:function(data){
		var res= data.isEdit && data.status=="DRAFT"  && data.createuseid!= data.curUserId ;
		return res;
	},
	initApprovalButtion:function(data){
		if(!InvAcceptPriv.isApprovalStatus(data)){
			$("#btn_approve").hide();
		}
		
	},
	isApprovalStatus:function(data){
		var modi=InvAcceptPriv.getFlowElementMod(data);
		var firstNode= modi && modi.firstNode;
		var isApproval=data && data.flow && data.flow.isApproval;
		var res=data.isEdit && isApproval && !firstNode;
		return res;
	},
	set:function(){//定义权限
		//新建
		Priv.map("privMapping.acceptList_new","acceptList_new");
	},
	apply:function(){//应用权限
		//应用
		Priv.apply();
	},
	getFlowElementMod:function(data){
		var modi= data && data.flow && data.flow.element && data.flow.element.modifiable;
		if(modi){
			modi=JSON.parse(modi);
		}
		return modi;
	},
	getWorkflowField:function(data,name){
    	var field=null;
    	var modi=InvAcceptPriv.getFlowElementMod(data);
		if(modi){
			var field=modi[name];
		}
		return field;
    }
};

//新建新的页面
function createNewAccept(){
	//?poId=8141
	var url = basePath+"inventory/invmataccept/invMatAcceptFormJsp.do";
	var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
	FW.addTabWithTree({
	        id : "newAcceptForm" + prefix,
	        url : url,
	        name : "物资验收",
	        tabOpt : {
	            closeable : true,
	            afterClose : "FW.deleteTab('$arg');FW.activeTabById('stock');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
	        }
	});
}

function getEnumValueByCode(enumList,enumCode){
	var r="";
	for(var i in enumList){
		if(enumList[i][0]==enumCode){
			r=enumList[i][1];
			break;
		}
	}
	return r;
}
