//初始化设备树
function initOpmmAssetTree(){
	FW.toggleSideTree(false); 
	FW.addSideFrame({
		id : "opmmAssestTree",
		src : basePath+"page/workorder/core/assetTree.jsp?jumpMode=func",
		conditions : [
		  	{tab:"^opmm$",tree:"^(equmaintain_woList)"},
		  	{tab:"^opmm$",tree:"^(equmaintain_woList)"}
		]
	});

};