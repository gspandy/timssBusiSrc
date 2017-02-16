//初始化item设备树
function initItemTree(){
	FW.addSideFrame({
		id : "itemTree",
		src : basePath+"inventory/invitem/invItemTree.do?opentype="+opentype,
		conditions : [
			{tab:"^stock$",tree:"^stock_stockquery$"},
			{tab:"^stock$",tree:"^stock_classList$"},
			{tab:"^stock.+"}
		]
	});			
};

//刷新页面
function refCurPage(){
 	$("#item_grid").datagrid("reload");
}

