//编辑列表中所有的行
function startEditAll(){
	var rows = $("#matreturnsdetail_grid").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		$("#matreturnsdetail_grid").datagrid("beginEdit",i);
	}
}

//关闭编辑列表
function endEditAll(){
	var rows = $("#matreturnsdetail_grid").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		$("#matreturnsdetail_grid").datagrid("endEdit",i);
	}
}

//动态判断对货数量是否合法
function dynaCalcReturnsSum(){
	var returnsNum = parseInt($(this).val()); //退库数量
	 //已入库数量
	var instockNum = parseInt($(this).parents(".datagrid-cell").parent().siblings("td[field=returnableqty]").children(0).html());
	if(returnsNum<0 || returnsNum>instockNum ){
		FW.error("请输入正确的退货数量！")
	}
}

//关闭当前选项卡
function closeCurTab(){
	homepageService.refresh();
	FW.deleteTabById(FW.getCurrentTabId());
	FW.activeTabById("stock");
}


