var ptwUtil = {
	getStatusNameByWtStatus : function(wtStatus){
		var statusMap = {200:"未签发",300:"未签发",320:"部长审批",400:"已签发",410:"签发",420:"签发",430:"签发",450:"已确认",500:"已许可",600:"已结束",
			700:"已终结",800:"已作废",1000:"标准票",2000:"标准票",3000:"标准票",4000:"标准票"};
		return  statusMap[wtStatus]  ? statusMap[wtStatus] : "";
	},
	getStatusNameForFold : function (wtStatus,taskId){
		var statusStr = this.getStatusNameByWtStatus(wtStatus);
		if(taskId){
			if(wtStatus == 400){
				statusStr = "签发审批中";
			}else if(wtStatus == 500){
				statusStr = "许可审批中";
			}else if(wtStatus == 600){
				statusStr = "结束审批中";
			}
			
		}
		return statusStr == "" ? "" : "(" + statusStr + ")";
	},
	findFieldById : function (fields,id){
		for(var i in fields){
			if(fields[i].id == id){
				return fields[i];
			}
		}
	}
};

