
/** 判断两个时间，是不是都是一1日开始，且是连续的两个月
 * @param beginTime
 * @param endTime
 * @returns {Boolean}
 */
function aMonthCheck(beginTime, endTime){
	var beginTimeYear = beginTime.getFullYear(); //年
	var endTimeYear = endTime.getFullYear();
	var beginTimeMonth = beginTime.getMonth(); //月
	var endTimeMonth = endTime.getMonth();
	var beginTimeDate = beginTime.getDate(); //日期
	var endTimeDate = endTime.getDate();
	if(beginTimeDate!=1||endTimeDate!=1){  //是否每个月的第一天
		return false;	
	}
	if(endTimeYear-beginTimeYear==0){ 
		if(endTimeMonth-beginTimeMonth!=1){ //同年不同月
			return false;
		}
	}else if(endTimeYear-beginTimeYear==1){
		if(beginTimeMonth-endTimeMonth!=11){ //不同年，且不是一个12月一个1
			return false;
		}
	}else{
		return false;
	}
	return true;
}