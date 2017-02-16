/**
*	用来在页面模式间切换
*	默认是详情（view），可为空
*	可选：新建（add）编辑（edit）
**/
var PageMode={
	objs:{
		mode:"",
		pageName:"列表项",//required
		buttonDivId:{
			toCreate:"",//新建
			create:"",//新建提交
			toEdit:"editButtonDiv",//编辑
			update:"updateButtonDiv",//编辑提交
			toDel:"deleteButtonDiv",//删除
			print:"printButtonDiv",//打印
			cancel:"cancelButtonDiv"//取消
		},
		/**工作流用这一组按钮
		buttonDivId:{
			save:"saveButtonDiv",//暂存
			del:"delButtonDiv",//删除
			cancel:"cancelButtonDiv",//取消
			commit:"commitButtonDiv",//提交
			audit:"auditButtonDiv",//审批
			print:"printButtonDiv",//打印
			invalid:"invalidButtonDiv"//作废
		},
		 */
		withWorkFlow:false,//是否使用工作流
		isCommited:false,//工作流使用，是否提交过了（是否启动了流程）
		isAudit:false,//工作流使用，是否能审批
		buttonShow:[],
		buttonHide:[],
		formId:"autoform"//required
	},
	
	init:function(initParams){
		if(initParams){
			$.extend(true,PageMode.objs,initParams);
		}else{
			FW.error("初始化信息缺失，请联系管理员！");
			return;
		}
	},
	
	changeTitle:function(type){
		var title=$(".inner-title");
		var name=PageMode.objs.pageName;
		if(type=="add"){
			title.html("新建"+name);
		}else if(type=="edit"){
			title.html("编辑"+name);
		}else{
			title.html(name+"详情");
		}
	},
	
	isMode:function(mode){
		return PageMode.objs.mode==mode;
	},
	
	changeMode:function(type){
		PageMode.objs.mode=type;
		if(type=="add"){
			if(PageMode.objs.withWorkFlow){
				PageMode.objs.buttonHide=["del","audit","invalid","print"];
				PageMode.objs.buttonShow=["save","cancel","commit"];
			}else{
				PageMode.objs.buttonHide=["toCreate","toEdit","update","toDel","print"];
				PageMode.objs.buttonShow=["create","cancel"];
			}
			$("#"+PageMode.objs.formId).iForm("beginEdit");
		}else if(type=="edit"){
			if(PageMode.objs.withWorkFlow){
				PageMode.objs.buttonHide=["audit"];
				PageMode.objs.buttonShow=["save","cancel","commit","print"];
				if(PageMode.objs.isCommited){
					PageMode.objs.buttonHide=PageMode.objs.buttonHide.concat(["del"]);
					PageMode.objs.buttonShow=PageMode.objs.buttonShow.concat(["invalid"]);
				}else{
					PageMode.objs.buttonHide=PageMode.objs.buttonHide.concat(["invalid"]);
					PageMode.objs.buttonShow=PageMode.objs.buttonShow.concat(["del"]);
				}
			}else{
				PageMode.objs.buttonHide=["toCreate","create","toEdit"];
				PageMode.objs.buttonShow=["update","toDel","cancel","print"];
			}
			$("#"+PageMode.objs.formId).iForm("beginEdit");
		}else{
			if(PageMode.objs.withWorkFlow){
				PageMode.objs.buttonHide=["save","cancel","commit","del","invalid"];
				PageMode.objs.buttonShow=["print"];
				if(PageMode.objs.isAudit){
					PageMode.objs.buttonShow=PageMode.objs.buttonShow.concat(["audit"]);
				}else{
					PageMode.objs.buttonHide=PageMode.objs.buttonHide.concat(["audit"]);
				}
			}else{
				PageMode.objs.buttonHide=["create","update","toDel","cancel"];
				PageMode.objs.buttonShow=["toCreate","toEdit","print"];
			}
			$("#"+PageMode.objs.formId).iForm("endEdit");
		}
		PageMode.changeButton();
		PageMode.changeTitle(type);
	},
	
	changeButton:function(){
		$.each(PageMode.objs.buttonHide,function(name,value){
			var obj=$("#"+PageMode.objs.buttonDivId[value]);
			if(obj){obj.hide();}
		});
		$.each(PageMode.objs.buttonShow,function(name,value){
			var obj=$("#"+PageMode.objs.buttonDivId[value]);
			if(obj){obj.show();}
		});
	}
};