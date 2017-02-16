var hasInitedFileDiv = false;
function genFileField(id,options){
	return {id:id,title:" ",type:"fileupload",options:{
			"buttonText":options.buttonText,
			"width":200,
			"uploader" : basePath + "upload?method=uploadFile&jsessionid="+sessionid,
			"delFileUrl" : basePath + "upload?method=delFile&key="+ delKey,
			"downloadFileUrl" : basePath + "upload?method=downloadFile",
			"swf" : basePath + "itcui/js/uploadify.swf",
			//"fileSizeLimit":10 * 1024,
			"initFiles" :attachFiles == null ? [] : attachFiles[id],
			"delFileAfterPost" : true
		}};
}
var attachFileFields = null;
function initUploadFiles(){
	if(isFireWt){
		attachFileFields = [
			genFileField("fireWorkPic",{"buttonText":"附加动火工作票示意图"})
		];
	}else{
		attachFileFields = [
			genFileField("addFile1",{"buttonText":"附加安全隔离示意图"}),
			genFileField("addFile3",{"buttonText":"附加可燃气体、粉尘检测单"}),
			genFileField("addFile4",{"buttonText":"附加工作风险分析单"}),
			genFileField("addFileOtherNo",{"buttonText":"附加其他文件"})
		];
	}
	$("#uploadFileForm").iForm('init',{"fields":attachFileFields,"options":{
	    labelFixWidth : 1,
	    labelColon : false
	}});	
	$("#uploadFileDiv").show().iFold("init");
	hasInitedFileDiv = true;
}

function beginCopyUploadfile(){
	if(hasInitedFileDiv){
		var values = {};
		for(var i in attachFileFields){
			values[attachFileFields[i].id] = [];
		}
		$("#uploadFileDiv").iFold("hide");
		$("#uploadFileForm").iForm("beginEdit");
		$("#uploadFileForm").iForm("setVal",values);
	}
	
}


function showUploadFileDiv(readOnly){
	//只读模式下，需要隐藏不存在的
	if(readOnly && JSON.stringify(attachFiles) == "{}"){
		return;
	}	
	if(hasInitedFileDiv){
		$("#uploadFileDiv").iFold("show");
	}else{
		initUploadFiles();
	}
	if(readOnly){
		$("#uploadFileForm").iForm("endEdit");
	}else{
		$("#uploadFileForm").iForm("beginEdit");
	}
}
