//设备树
function initAssetTree(){
	FW.toggleSideTree(false); 
	FW.addSideFrame({
		id : "ptwAssetTree",
		src : basePath+"page/ptw/core/tree.jsp?jumpMode=func",
		conditions : [
			//{tab:"^(?!ptwbase.*)ptw.*",tree:"^(?!(ptw_ptwbase.*|ptw_islPoint|ptw_ptwStd))ptw.*"}
			{tab:"^(ptw|islInfoDetail.*|ptwInfoDetail.*|addPtwStandard.*|updatePtwStandard.*)",tree:"^(ptw_ptwlist|ptw_ptwIslList|ptw_ptwbaseIslArea|ptw_ptwStandardCore)"}
		]
	});

};

//隔离点树
function initStdPointTree(){
	FW.addSideFrame({
		id : "stdPointTree",
		src : basePath+"page/ptw/core/pointTree.jsp?jumpMode=func",
		conditions : [
			{tab:"^(ptw|addIslPoint.*)",tree:"^(ptw_ptwIslPoint)"}
		]
	});

};

//标准树
function initStandardTree(){
	FW.addSideFrame({
		id : "ptwStandardTree",
		src : basePath+"page/ptw/sjc/stdTree.jsp?jumpMode=func",
		conditions : [
			{tab:"^(ptw|addStandard.*|ptwStdDetail.*)",tree:"^(ptw_ptwStd|ptw_ptwbaseStdTree)"}
		]
	});

};
