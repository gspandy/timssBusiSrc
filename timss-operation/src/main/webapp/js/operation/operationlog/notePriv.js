/***
 * 运行记事权限
 */
function setNotePriv(){
	Priv.map("privMapping.OPR_NOTE_BEFOREBTN","OPR_NOTE_BEFOREBTN");
	Priv.map("privMapping.OPR_NOTE_NEXTBTN","OPR_NOTE_NEXTBTN");
	Priv.map("privMapping.OPR_NOTE_SAVE","OPR_NOTE_SAVE");
	
	Priv.map("privMapping.OPR_NOTE_CANCEL","OPR_NOTE_CANCEL");
	Priv.map("privMapping.OPR_NOTE_FINAL","OPR_NOTE_FINAL");
	Priv.map("privMapping.OPR_NOTE_JIAOBAN","OPR_NOTE_JIAOBAN");
	
	Priv.map("privMapping.OPR_NOTE_CONFIG","OPR_NOTE_CONFIG");
	Priv.map("privMapping.OPR_NOTE_ADD","OPR_NOTE_ADD");
	Priv.apply();
	FW.fixRoundButtons("#toolbar");
}