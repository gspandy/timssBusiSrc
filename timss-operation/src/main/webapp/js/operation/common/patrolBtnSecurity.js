/***
 * 点检日志权限
 */
function setPatrolPriv(){
	//var pri=_parent().privMapping;
	//console.log(pri);
	Priv.map("privMapping.OPR_PATROl_ADD","OPR_PATROL_ADD");
	Priv.map("privMapping.OPR_PATROL_SAVE","OPR_PATROL_SAVE");
	Priv.map("privMapping.OPR_PATROL_COMMIT","OPR_PATROL_COMMIT");
	Priv.map("privMapping.OPR_PATROL_DELETE","OPR_PATROL_DELETE");
	Priv.map("privMapping.OPR_PATROL_EDIT","OPR_PATROL_EDIT");
	Priv.map("privMapping.OPR_PATROL_INVALID","OPR_PATROL_INVALID");
	Priv.map("privMapping.OPR_PATROL_AUDIT","OPR_PATROL_AUDIT");
	Priv.map("privMapping.OPR_PATROL_FLOW","OPR_PATROL_FLOW");
	Priv.apply();
	FW.fixRoundButtons("#toolbar");	
}

function canDelete(){
	if(currentRowData && currentRowData.status == draftStr){
		return true;
	}
	else{
		return false;
	}
}