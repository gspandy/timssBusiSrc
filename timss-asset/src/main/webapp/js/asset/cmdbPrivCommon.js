/***
 * 参数项权限
 */
function setParamsPriv(){
	Priv.map("privMapping.CMDB_PARAMS_ADD","CMDB_PARAMS_ADD");
	Priv.map("privMapping.CMDB_PARAMS_SEARCH","CMDB_PARAMS_SEARCH");
	Priv.map("privMapping.CMDB_PARAMS_CLOSE","CMDB_PARAMS_CLOSE");
	
	Priv.map("privMapping.CMDB_PARAMS_BACK","CMDB_PARAMS_BACK");
	Priv.map("privMapping.CMDB_PARAMS_EDIT","CMDB_PARAMS_EDIT");
	Priv.map("privMapping.CMDB_PARAMS_DELETE","CMDB_PARAMS_DELETE");
	Priv.map("privMapping.CMDB_PARAMS_SAVE","CMDB_PARAMS_SAVE");
	
	Priv.apply();
	FW.fixRoundButtons("#toolbar");
}

/***
 * CI权限
 */
function setCiPriv(){
	Priv.map("privMapping.CMDB_CI_ADD","CMDB_CI_ADD");
	Priv.map("privMapping.CMDB_CI_SEARCH","CMDB_CI_SEARCH");
	Priv.map("privMapping.CMDB_CI_DOWNLOAD","CMDB_CI_DOWNLOAD");
	
	Priv.map("privMapping.CMDB_CI_CLOSE","CMDB_CI_CLOSE");
	Priv.map("privMapping.CMDB_CI_SAVE","CMDB_CI_SAVE");
	Priv.map("privMapping.CMDB_CI_DFROM","CMDB_CI_DFROM");
	
	Priv.map("privMapping.CMDB_CI_CLOSEUPDATE","CMDB_CI_CLOSEUPDATE");
	Priv.map("privMapping.CMDB_CI_BACKUPDATE","CMDB_CI_BACKUPDATE");
	Priv.map("privMapping.CMDB_CI_EDITUPDATE","CMDB_CI_EDITUPDATE");
	
	Priv.map("privMapping.CMDB_CI_DELETEUPDATE","CMDB_CI_DELETEUPDATE");
	Priv.map("privMapping.CMDB_CI_SAVEUPDATE","CMDB_CI_SAVEUPDATE");
	Priv.map("privMapping.CMDB_CI_LOGUPDATE","CMDB_CI_LOGUPDATE");
	Priv.map("privMapping.CMDB_CI_RELATIONUPDATE","CMDB_CI_RELATIONUPDATE");
	
	Priv.apply();
	FW.fixRoundButtons("#toolbar");
}