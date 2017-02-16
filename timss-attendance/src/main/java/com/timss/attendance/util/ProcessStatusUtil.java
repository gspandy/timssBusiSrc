package com.timss.attendance.util;

/**
 * 
 * @title: 流程状态定义
 * @description: {desc}
 * @company: gdyd
 * @className: ProcessStatusUtil.java
 * @author: fengzt
 * @createDate: 2014年9月2日
 * @updateUser: yyn
 * @version: 1.0
 */
public class ProcessStatusUtil {
	/**通用状态-start**/
    public static final String CAOGAO = "草稿";
    public static final String CLOSED = "已归档";
    public static final String END = "终止";
    public static final String INVALID = "作废";
    /**通用状态-end**/
    
    
    /**混用的状态-start**/
    public static final String ABEXCEPTIONAPPLY = "提交考勤异常申请";
    public static final String TJSQ = "提交考勤异常申请";
    public static final String LEAVEAPPLY = "提交请假审批表";
    public static final String JBSQ = "提交加班申请";
    
    public static final String BMKQYHS = "部门考勤员核实";
    public static final String XZBSH = "行政部审核";
    public static final String BMKQYSH = "部门考勤员审核";
    
    public static final String DEPTMGRHD = "部门经理核定加班时长";
    public static final String DEPTMGRHDSH = "部门经理核定加班时长审核";
    
    public static final String DEPTMGR = "部门经理审批";
    public static final String FGFZJLPZ = "分管副总经理批准";
    public static final String ZJLSP = "总经理审批";
    
    public static final String XZBBEIAN = "行政部核查备案";
    public static final String XZBDJBA = "行政部登记备案";
    /**混用的状态-end**/
    
    
    /**请假的状态-start**/
    public static final String DPP_LEAVE_BZYJ = "班组意见";
    public static final String DPP_LEAVE_BMSP = "部门审批";
    public static final String DPP_LEAVE_RLZYBSP = "人力资源部审批";
    public static final String DPP_LEAVE_FGLDSP = "分管领导审批";
    public static final String DPP_LEAVE_YSZZQR = "医务专责确认";
    public static final String DPP_LEAVE_LDSP = "领导审批";
    /**请假的状态-end**/
    
    
    /**加班的状态-start**/
    public static final String DPP_OVERTIME_BZYJ = "班组意见";
    public static final String DPP_OVERTIME_FBYJ = "分部意见";
    public static final String DPP_OVERTIME_BMYJ = "部门意见";
    public static final String DPP_OVERTIME_ZGLDYJ = "公司主管领导意见";
    /**加班的状态-end**/
    
    
    /**考勤异常的状态-start**/
    
    /**考勤异常的状态-end**/
    
    /**培训的状态-start**/
    public static final String TRAINING_TJSQ = "提交申请";
    
    public static final String DPP_TRAINING_BMFZR = "部门负责人审批";
    public static final String DPP_TRAINING_GSLD = "公司领导审批";
    public static final String DPP_TRAINING_PXFBZR = "培训分部主任审批";
    public static final String DPP_TRAINING_PXFBZZ = "培训分部专责审批";
    public static final String DPP_TRAINING_RLZYBBZ = "人力资源部部长审批";
    public static final String DPP_TRAINING_SQR = "申请人添加培训结果材料";
    public static final String DPP_TRAINING_PXZZ = "培训专责确认归档";
    /**培训的状态-end**/
    
    /**
     * 湛江风电请假流程
     */
    public static final String BZSH = "班长审核";
    public static final String ZZHD = "专责核定假期";
    public static final String BMBZSH = "部门部长审核";
    public static final String ZJLSH = "总经理审核";
    public static final String GHSH = "工会审核";
    public static final String RSSH = "人事审核";
    public static final String FGLDSH = "分管领导审核";
    public static final String BEIAN = "备案";
    
    /**
     * 生物质
     */
    public static final String SWF_BZYJ = "班组意见";
    public static final String SWF_BMLD = "部门领导审核";
    public static final String SWF_GHHS = "工会核实";
    public static final String SWF_JSHS = "计生办核实";
    public static final String SWF_RLDQ = "人力党群部审核";
    public static final String SWF_FGLD = "分管领导审核";
    public static final String SWF_ZJL = "总经理审核";
    public static final String SWF_BEIAN = "行政备案";
    
    
    
    /**合理化建议-start-**/
    public static final String   DPP_RATION_TJSQ  = "提交申请";
    public static final String   DPP_RATION_BMJYYSD = "部门建议员审定";
    public static final String   DPP_RATION_SQBMFZRSP = "行政部门负责人审批";
    public static final String   DPP_RATION_PSXZSP = "对口专业评审小组审批";
    public static final String   DPP_RATION_DKZYFZRSP = "对口专业负责人审批";
    public static final String   DPP_RATION_YWBMFZRZP = "业务相关部门指派";
    public static final String   DPP_RATION_YWBMZX = "业务相关部门执行";
    public static final String   DPP_RATION_SCJYZZCP = "生产经营部专责初评";
    public static final String   DPP_RATION_PSWYHPJ = "评审委员会评奖";
    public static final String   DPP_RATION_SQRDR = "申请人确认";
    public static final String   DPP_RATION_GD = "工会归档";
    public static final String   DPP_RATION_END = "已归档";
    /**合理化建议-end-**/
    
    /**
     * 海运公司加班申请
     */
    public static final String HYG_OVERTIME_BMJL = "部门经理审批";
    public static final String HYG_OVERTIME_FGLD = "分管领导审批";
    public static final String HYG_OVERTIME_ZJL = "总经理审批";
    public static final String HYG_OVERTIME_CHECK = "核定加班时长";
    public static final String HYG_OVERTIME_GD = "加班归档";
    public static final String HYG_OVERTIME_END = "已归档";
    
    /*大浦考勤异常状态start*/
    public static final String DPP_ABNORMITY_TJSQ = "提交申请";
    public static final String DPP_ABNORMITY_BZSP = "班组审批";
    public static final String DPP_ABNORMITY_BMYJ = "部门审批";
    public static final String DPP_ABNORMITY_RLZYSP = "人力资源部审批";
    public static final String DPP_ABNORMITY_CLOSED = "已归档";
    /*大浦考勤异常状态end*/
    
    /*海运请假申请状态start*/
    public static final String HYG_LEAVE_CAOGAO = "草稿";
    public static final String HYG_LEAVE_TJSQ = "提交请假审批表";
    public static final String HYG_LEAVE_BMJLPZ = "部门经理批准";
    public static final String HYG_LEAVE_ZHBSH = "综合部审核";
    public static final String HYG_LEAVE_FGFZ = "分管副总审批";
    public static final String HYG_LEAVE_ZJLSP = "总经理审批";
    public static final String HYG_LEAVE_QJGD = "请假归档";
    public static final String HYG_LEAVE_CLOSED = "已归档";
    /*海运请假申请状态end*/
}
