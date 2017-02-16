//硬件台账树
function initAssetHwTree(){
	FW.addSideFrame({
		id : "assetHwTree",
		src : basePath+"page/asset/itc/hardware/hwTree.jsp?jumpMode=func",
		conditions : [
			{tab:"^(equipment|addHwLedger.*)",tree:"^equipment_assetHardware$"}
		]
	});

};