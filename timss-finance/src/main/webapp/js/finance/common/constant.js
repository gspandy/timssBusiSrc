var finWorkFlowStatusCodeConstant ={
		'FIN_FLOW_STOP' : 'finance_flow_stop',       //流程终止
		'FMP_CASHIER_PAY' : 'fmp_cashier_pay',       // 审批通过
		'APPLY_APPROVE' : 'apply_approve',       //申请人办理
		'MAIN_ACCOUNT_APPROVE' : 'main_accounting_approve',       // 主办会计审批、复核
		'FIN_FLOW_STR' : 'finance_flow_str',       //申请开始
		'WAIT_DEPTMGR_APPROVE' : 'wait_deptmgr_approve',       //部门经理审批
		'FIN_FLWO_END': 'finance_flow_end',       //流程结束
		'WAIT_ACCOUNT_APPROVE': 'wait_accounting_approve',       //会计审批,会计复核
		'WAIT_CASHIER_APPROVE': 'wait_cashier_approve',       //出纳审批
		'WAIT_CHAIRMAN_APPROVE': 'wait_chairman_approve',       //董事长审批
		'WAIT_COMMISSIONER_APPROVE': 'wait_commissioner_approve',    //专责审批
		'WAIT_FIN_APPROVE': 'wait_finance_approve',       //财务审批
		'WAIT_JYDEPTMGR_APPROVE': 'wait_jydeptmgr_approve',       //行政部经理审批
		'WAIT_LABOURUNION_APPROVE': 'wait_labourunion_approve',   //工会审批
		'WAIT_MAINMGR_APPROVE': 'wait_mainmgr_approve',       //总经理审批
		'WAIT_SUBMAINMGR_SPPROVE': 'wait_submainmgr_approve',       //分管领导审批
		'WAIT_USER_CONFIRM': 'wait_user_confirm',       //报销人确认
		'FIN_DRAFT': 'finance_draft',          //草稿
		'FIN_ABOLISH': 'finance_abolish',       //作废
		'FMP_APPLY': 'fmp_apply',       //提出申请
		"APPLICANT_MODIFY":"applicant_modify",    //申请人修改
		'FMP_DEPTMANAGER_APPROVE': 'fmp_dept_manager_approve',       //部门经理审批
		'FMP_ADMINDEPTMGR_APPROVE': 'fmp_administraction_dept_manager_approve',       //行政部经理审批
		'FMP_ADMINDEPUTYMGR_APPOVE': 'fmp_administraction_deputy_manager_approve',       //分管行政副总审批
		'FMP_MGR_APPROVE': 'fmp_manager_approve',              //总经理审批
		'FMP_SUBMIT_PAPERMATERIAL': 'fmp_submit_paper_material',       //提交纸质材料
		'FMP_FIN_CHECK': 'fmp_finance_check'          //财务复核
};

var finFlowKeyConstant ={
		"BUSINESS":"businessentertainment", //业务招待费报销
		"TRAVEL":"travelcost",//差旅费报销
		"CAR":"carcost",//汽车费报销
		"OFFICE":"officecost",//办公费报销
		"WELFARISM":"welfarism",//福利费报销
		"MEETING":"meetingcost",//会议费报销
		"TRAIN":"traincost",//培训费报销
		"PETTY":"pettycash",//备用金报销
		"HOMETRIP":"hometripcost",//探亲路费报销
		"MEDICAL":"medicalinsurance",//补充医疗保险报销
		"FAMILYMEDICINE":"familymedicinecost",//家属医药费报销
		"ADMINISTRATIVE":"administrativeExpenses"//行政报销
}

