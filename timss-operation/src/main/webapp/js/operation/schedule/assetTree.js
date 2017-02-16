//设备树
function initAssetTree(){
	FW.toggleSideTree(false); 
	FW.addSideFrame({
		id : "oprAssetTree",
		src : basePath+"page/operation/core/schedule/tree.jsp?jumpMode=func",
		conditions : [
			//{tab:"^(?!ptwbase.*)ptw.*",tree:"^(?!(ptw_ptwbase.*|ptw_islPoint|ptw_ptwStd))ptw.*"}
			{tab:"^ModeDetail.*",tree:"^opmm_opmmmode$"}
		]
	});

};
