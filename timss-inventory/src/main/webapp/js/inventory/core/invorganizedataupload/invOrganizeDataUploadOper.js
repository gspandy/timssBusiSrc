function doInitData(){
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"inventory/invorganizedata/callOrganizeDataInit.do",
		dataType : "json",
		success : function(data) {
			if(data.result == 'success'){
				FW.success("初始化数据完成");
			}else{
				FW.error("数据分发失败");
			}
		}
	});
}
