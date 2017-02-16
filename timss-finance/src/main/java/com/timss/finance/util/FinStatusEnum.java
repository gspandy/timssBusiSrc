package com.timss.finance.util;

public enum FinStatusEnum {
	//流程终止,审批通过,申请人办理, 主办会计审批,申请开始,部门经理审批,流程结束
	FIN_FLOW_STOP("finance_flow_stop"),
        FMP_CASHIER_PAY("fmp_cashier_pay"),      
        APPLY_APPROVE("apply_approve"),       
        MAIN_ACCOUNT_APPROVE("main_accounting_approve"),      
        FIN_FLOW_STR("finance_flow_str"),      
        WAIT_DEPTMGR_APPROVE("wait_deptmgr_approve"),      
        FIN_FLWO_END("finance_flow_end"),  
        FIN_APPLICANT_MODIFY("applicant_modify"),
        
        //会计审批,出纳审批,董事长审批,专责审批,财务审批,行政部经理审批
        WAIT_ACCOUNT_APPROVE("wait_accounting_approve"),    
        WAIT_CASHIER_APPROVE("wait_cashier_approve"),
        WAIT_CHAIRMAN_APPROVE("wait_chairman_approve"),
        WAIT_COMMISSIONER_APPROVE("wait_commissioner_approve"),
        WAIT_FIN_APPROVE("wait_finance_approve"),
        WAIT_JYDEPTMGR_APPROVE("wait_jydeptmgr_approve"),
        
        //工会审批 ,总经理审批,分管领导审批,报销人确认,草稿,作废,提出申请,
        WAIT_LABOURUNION_APPROVE("wait_labourunion_approve"),   
        WAIT_MAINMGR_APPROVE("wait_mainmgr_approve"),
        WAIT_SUBMAINMGR_SPPROVE("wait_submainmgr_approve"),
        WAIT_USER_CONFIRM("wait_user_confirm"),
        FIN_DRAFT("finance_draft"),
        FIN_ABOLISH("finance_abolish"),
        FMP_APPLY("fmp_apply"),
        
      //部门经理审批,行政部经理审批,分管行政副总审批,总经理审批,提交纸质材料,财务复核
        FMP_DEPTMANAGER_APPROVE("fmp_dept_manager_approve"),       
        FMP_ADMINDEPTMGR_APPROVE("fmp_administraction_dept_manager_approve"),
        FMP_ADMINDEPUTYMGR_APPOVE("fmp_administraction_deputy_manager_approve"),
        FMP_MGR_APPROVE("fmp_manager_approve"),
        FMP_SUBMIT_PAPERMATERIAL("fmp_submit_paper_material"),
        FMP_FIN_CHECK("fmp_finance_check");
	
	private String name;
	private FinStatusEnum(String name){
		this.name=name;
	}
	public String toString(){
		return name;
	}
}
