//初始化设备树
function initWoListAssetTree(){
	FW.toggleSideTree(false); 
	FW.addSideFrame({
		id : "woListAssestTree",
		src : basePath+"page/workorder/core/assetTree.jsp?jumpMode=func",
		conditions : [
		  	{tab:"^equmaintain$",tree:"^(equmaintain_woList)"}
		]
	});

};

//初始化设备树
function initWoInfoAssetTree(){
	FW.toggleSideTree(false); 
	FW.addSideFrame({
		id : "woInfoAssestTree",
		src : basePath+"page/workorder/core/assetTree.jsp?jumpMode=func",
		conditions : [
			{tab:"^workOrderAdd.+"},
			{tab:"^WO.+"}
		]
	});
	FW.set("AssetTreeInitRollBackFunc",assetTreeRollBackFunc);//第一次生成，采用回调的方式选中设备
};
