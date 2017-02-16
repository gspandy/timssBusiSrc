package com.timss.pms.util;


public class PayplanStatusUtil {

	public static boolean isPayplanPayable(String payStatus){
		if(payStatus==null || payStatus.equals("") || payStatus.equals("reset")){
			return true;
		}
		return false;
	}
	

}
