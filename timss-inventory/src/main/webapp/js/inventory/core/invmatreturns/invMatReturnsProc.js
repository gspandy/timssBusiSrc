//更改状态
var initPageProcess={
	init:function(){
		if("" != imtid){
			initPageProcess.read();
		}
	},
	read:function(){
		$("#pageTitle").html("新建物资退货单");
		initForm(show_form);
		$("#autoform").iForm("endEdit",["sheetno","createdate","operusername","checkusername","pruorderno","remark"]);
		
		var urlReport = null;
		if(siteid == 'SJW'){
			urlReport = fileExportPath+"preview?__format=pdf&__asattachment=true&__report=report/TIMSS2_SJW_SR_001_pdf.rptdesign&sheetid="+imtid+"&siteid="+siteid;
		}else{
			urlReport = fileExportPath+"preview?__format=pdf&__asattachment=true&__report=report/TIMSS2_IMT_001_pdf.rptdesign&imtid="+imtid+"&siteid="+siteid;
		}
	}
};