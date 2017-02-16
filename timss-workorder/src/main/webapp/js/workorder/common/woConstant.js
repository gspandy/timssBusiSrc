var woQxSignUserGroup={  "ONDUTYUSER":"SBS_WO_qx_onduty",
							"SOLVEDEFECTUSER":"SBS_WO_qx_solvedefect",
							"RUNNINGUSER":"SBS_WO_qx_running",
							"AUDITLEADER":"SBS_WO_qx_auditleader"
						 };



/**  判断myId 是否在候选人 candidateUsers 中
 * @param candidateUsers
 * @param myId
 */
function isMyActivityWO(candidateUsers,myId){
	for(var i=0; i<candidateUsers.length; i++){
		if(candidateUsers[i] == myId ){
			auditInfoShowBtn = 1 ;
			return true;
		}
	}
	return false;
}