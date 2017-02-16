//编辑列表中所有的行
function startEditAll(){
	var rows = $("#matrefunddetail_grid").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		$("#matrefunddetail_grid").datagrid("beginEdit",i);
	}
}

//关闭编辑列表
function endEditAll(){
	var rows = $("#matrefunddetail_grid").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		$("#matrefunddetail_grid").datagrid("endEdit",i);
	}
}

//关闭当前tab
function closeCurTab(){
	homepageService.refresh();
	FW.deleteTabById(FW.getCurrentTabId());
	FW.activeTabById("stock");
}



