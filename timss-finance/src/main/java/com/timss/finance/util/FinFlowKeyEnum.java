package com.timss.finance.util;

public enum FinFlowKeyEnum {
	//
	CAR_COST_MORE("finance_itc_carcostmore"),
        CAR_COST_ONE("finance_itc_carcostone"),      
        COMMON_DEPTCHOOSE_ONE("finance_itc_commondeptchooseone"),       
        COMMON_LABOURUNION_MORE("finance_itc_commonlabourunionmore"),      
        COMMON_LABOURUNION_ONE("finance_itc_commonlabourunionone"),      
        COMMON_MORE("finance_itc_commonmore"),      
        COMMON_ONE("finance_itc_commonone"),  
        COMMON_RESP_MORE("finance_itc_commonrespmore"),
        
        COMMON_RESP_ONE("finance_itc_commonrespone"),    
        PETTY_CHASH_MORE("finance_itc_pettycashmore"),
        PETTY_CHASH_ONE("finance_itc_pettycashone");
        
        
	
	private String name;
	private FinFlowKeyEnum(String name){
		this.name=name;
	}
	public String toString(){
		return name;
	}
}
