var InvMatTranPriv={
	init:function(){
		InvMatTranPriv.set();
		InvMatTranPriv.apply();
	},
	set:function(){//定义权限
		//编辑
		Priv.map("privMapping.storeinQ_edit","storeinQ_edit");
		//打印
		Priv.map("privMapping.storeinQ_export","storeinQ_export");
		//提交
		Priv.map("privMapping.storeinQ_commit","storeinQ_commit");
		//新建
		Priv.map("privMapping.storeinQ_new","storeinQ_new");
		//资产化
		Priv.map("privMapping.storeinQ_asset", "storeinQ_asset");
	},
	apply:function(){//应用权限
		//应用
		Priv.apply();
	}
};

//更改状态
var initPageProcess={
	init:function(){
		if("" != imtid){
			initPageProcess.read();
		}else{
			initPageProcess.news();
		}
	},
	read:function(){
		$("#pageTitle").html("物资接收单详情");
		initForm(show_form);
		$("#autoform").ITC_Form("readonly");
		
		$("#btn_save").hide();
		$("#btn-add").hide();
		if(siteid == "ITC"){
			$("#btn_asset").show();
		}else{
			$("#btn_asset").hide();
		}
		
		
		var urlReport = null;
		if(siteid == 'SJW'){
			urlReport = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_SJW_SR_001_pdf.rptdesign&sheetid="+imtid+"&siteid="+siteid;
		}else{
			urlReport = fileExportPath+"preview?__format=pdf&__report=report/TIMSS2_IMT_001_pdf.rptdesign&imtid="+imtid+"&siteid="+siteid;
		}
		FW.initPrintButton("#btn_print",urlReport,"接收打印预览",null);
	},
	edit:function(){
		$("#pageTitle").html("编辑物资接收单");
		initForm(edit_form);
		edit_form[8].render = getUserHint("f_operuser","f_operusername");
		edit_form[10].render = getUserHint("f_checkuser","f_checkusername");
		
		$("#btn_save").show();
		$("#btn-add").hide();
		$("#btn_edit").hide();
		$("#btn_print").hide();
		$("#btn_preview").hide();
		$("#btn_asset").hide();
		
		startEditAll();

	},
	news:function(){
		$("#pageTitle").html("新建物资接收单");
		initForm(new_form);
		new_form[2].render = purOrderNoSearch("f_pruorderno");
		new_form[5].render = getUserHint("f_checkuser","f_checkusername");
		
		$("#btn_print").hide();
		$("#btn_preview").hide();
		$("#btn_edit").hide();
		$("#btn_asset").hide();
	} 
};