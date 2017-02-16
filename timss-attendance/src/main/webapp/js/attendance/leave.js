//休假验证
function categoryValid(leaveData){
	var formData = $( "#autoform" ).iForm("getVal");
	//剩余补休天数
	var remainOvertime = formData.remainOvertimeDays;
	//剩余年假天数
	var remainAnnual = formData.remainAnnualDays;
	
	var validFlag = true;
	//休假类别
	var category = leaveData.category.toLowerCase();
	//休假天数
	var diffDay = leaveData.leaveDays;
	
	if( category === ( siteId.toLowerCase() + "_le_category_1" ) && diffDay > remainAnnual ){
        //年休假
        FW.error( "请年假天数超出剩余年休假天数!" );
        validFlag = false;
    }else if( category === ( siteId.toLowerCase() + "_le_category_6" ) &&  diffDay > remainOvertime ){
        //调休补休假
        FW.error( "补休天数超出剩余补休天数!" );
        validFlag = false;
    }else if( category === ( siteId.toLowerCase() + "_le_category_4" ) && diffDay > 3){
    	//婚假
        FW.error( "婚假天数超出3天!" );
        validFlag = false;
    }
	return validFlag;
}

function gridValid(rowDatas){
	var formData = $( "#autoform" ).iForm("getVal");
	//剩余补休天数
	var remainOvertime = formData.remainOvertimeDays;
	//剩余年假天数
	var remainAnnual = formData.remainAnnualDays;
	
	var len = rowDatas.length;
	var AnnualTotal = 0;
	var overtimeTotal = 0;
	var marryTotal = 0;
	for( var i = 0; i < len; i++ ){
		 var rowData = rowDatas[i];
		 category = rowData.category.toLowerCase();
		 leaveDays = parseFloat(rowData.leaveDays);
		 if( category === ( siteId.toLowerCase() + "_le_category_1" )){
            //年休假
          	AnnualTotal += leaveDays;
         }else if( category === ( siteId.toLowerCase() + "_le_category_6" )){
            //调休补休假
        	overtimeTotal += leaveDays;
         }else if( category === ( siteId.toLowerCase() + "_le_category_4" )){
        	//婚假
        	marryTotal += leaveDays;
         }
	}
	if(AnnualTotal>remainAnnual){
		FW.error( "请年假天数超出剩余年休假天数!" );
		return false;
	}
	if(overtimeTotal>remainOvertime){
		FW.error( "补休天数超出剩余补休天数!" );
		return false;
	}
	if(marryTotal>3){
		FW.error( "婚假天数超出3天!" );
		return false;
	}
	return true;
}