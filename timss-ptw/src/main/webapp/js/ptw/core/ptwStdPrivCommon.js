/***
 * 标准工作票权限
 */
function setPriv(){
	Priv.map("privMapping.PTW_STD_CLOSE","PTW_STD_CLOSE");
	Priv.map("privMapping.PTW_STD_SAVE","PTW_STD_SAVE");
	Priv.map("privMapping.PTW_STD_COMMIT","PTW_STD_COMMIT");
	
	Priv.map("privMapping.PTW_STD_APPR","PTW_STD_APPR");
	Priv.map("privMapping.PTW_STD_DELETE","PTW_STD_DELETE");
	
	Priv.map("privMapping.PTW_STD_CANCEL","PTW_STD_CANCEL");
	Priv.map("privMapping.PTW_STD_MODIFY","PTW_STD_MODIFY");
	Priv.map("privMapping.PTW_STD_PRINT","PTW_STD_PRINT");
	
	Priv.map("privMapping.PTW_STD_FLOWINFO","PTW_STD_FLOWINFO");
	Priv.map("privMapping.PTW_STD_SEARCH","PTW_STD_SEARCH");
	//Priv.map("privMapping.PTW_STD_TOPTW","PTW_STD_TOPTW");
	
	Priv.apply();
	FW.fixRoundButtons("#toolbar");
}