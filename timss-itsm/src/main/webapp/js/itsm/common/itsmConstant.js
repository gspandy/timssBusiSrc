
var itsmSiteCode ={
		"INFOCENTERSITEID":"YDZ",
		"ITCSITEID":"ITC"
};
var itsmDeptId = {
		"INFOCENTERDEPTID":"121883302"  //新：121883302、旧：1218835
};
var itsmWoStatus={  "DRAFT":"draft",
					"NEWWO":"newWO",
					"APPLICANTAUDIT":"applicantAudit",
					"COMAUDIT":"initComAudit",
					"CENTERAUDIT":"infoCenterAudit",
					"GROUPAUDIT":"groupDeptAudit",
					"SENDWO":"sendWO",
					"WORKPLAN":"workPlan",
					"CHIEFAUDIT":"chiefAudit",
					"DELAYAUDIT":"delayAudit",
					"ENDREPORT":"endWorkReport",
					"APPLICANTCONFIRM":"applicantConfirm",
					"FEEBACK":"woFeedback",
					"FILING":"woFiling",
					"OBSOLETE":"woObsolete"};


var itsmKLStatus={  "KL_NEW":"klNew",
					"KL_AUDIT":"klAudit",
					"KL_AUDIT_END":"klAuditEnd",
					"KL_OBSOLETE":"klObsolete"
					};

var itsmKlSource={
		"DAILYKL":"dailyKl",
		"EVENTKL":"eventKl",
		"PROBLEMKL":"problemKl"
	};

var itsmInfoWoSerType = {
		"BUSINESS":"businessApply",
		"FAULT":"faultMaintenance",
		"BORROW":"equipmentBorrow"
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