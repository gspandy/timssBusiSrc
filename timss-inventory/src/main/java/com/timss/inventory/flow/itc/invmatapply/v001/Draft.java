package com.timss.inventory.flow.itc.invmatapply.v001;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.bean.InvMatBudget;
import com.timss.inventory.dao.InvMatApplyDetailDao;
import com.timss.inventory.service.InvMatApplyDetailService;
import com.timss.inventory.service.InvMatApplyService;
import com.timss.inventory.service.InvMatBudgetService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvMatApplyDetailVO;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: AdminManager.java
 * @author: 890166
 * @createDate: 2014-9-17
 * @updateUser: 890166
 * @version: 1.0
 */
public class Draft extends TaskHandlerBase {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private IAuthorizationManager iAuthorizationManager;
    @Autowired
    private InvMatBudgetService invMatBudgetService;
    @Autowired
    private InvMatApplyDetailService invMatApplyDetailService;
    @Autowired
    private InvMatApplyDetailDao invMatApplyDetailDao;
    @Autowired
    private InvMatApplyService invMatApplyService;

    @Override
    public void init(TaskInfo taskInfo) {
        super.init( taskInfo );
    }

    /**
     * 首环节完成了之后执行更新计算操作
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
        Object imaidObj = workflowService.getVariable( taskInfo.getProcessInstanceId(), "imaid" );
        try {
            Map<String, Object> paramMap = calcThisYearRemainBudget( taskInfo );
            
            String imaid = imaidObj == null ? "" : String.valueOf( imaidObj );
            if ( !"".equals( imaid ) ) {
                // 更新预算信息
                InvMatApply ima = new InvMatApply();
                String processInstId = taskInfo.getProcessInstanceId();
                Task task = workflowService.getTaskByTaskId(taskInfo.getTaskInstId());
            	List< String > taskNextList = workflowService.getNextTaskDefKeys( processInstId , task.getTaskDefinitionKey() );
        	    if ( null != taskNextList && !taskNextList.isEmpty() ) {
        	    	String status = taskNextList.get( 0 );
        	    	ima.setStatus(status);
        	    }
                ima.setImaid( imaid );
                ima.setApplyBudget( new BigDecimal( String.valueOf( paramMap.get( "applyBudget" ) == null ? "0"
                        : paramMap.get( "applyBudget" ) ) ) );
                ima.setRemainBudget( new BigDecimal( String.valueOf( paramMap.get( "remainBudget" ) == null ? "0"
                        : paramMap.get( "remainBudget" ) ) ) );
                ima.setIsOver( String.valueOf( paramMap.get( "isOver" ) ) );
                List<InvMatApplyDetailVO> imadvList = invMatApplyDetailDao.queryApplyDetailList(imaid);
                invMatApplyService.updateInvMatApply( CommonUtil.conventJsonToInvMatApplyDetailList(imadvList), ima );
            }
        } catch (Exception e) {
            throw new RuntimeException( "---------Draft 中的 onComplete 方法抛出异常---------：", e );
        }
    }

    /**
     * @description:年度预算总额
     * @author: 890166
     * @createDate: 2014-9-18
     * @param nowMonthBudget
     * @return
     * @throws Exception :
     */
    private Map<String, Object> calcThisYearRemainBudget(TaskInfo taskInfo) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        try {
            // 获取本月预算成本
            double nowMonthB = calcNowMonthBudget( true );
            // 获取累计预算成本
            double lastMonthB = calcGrandTotalMonthBudget();
            // 获取未来预算成本
            double futureMonthB = calcFutureMonthBudget( nowMonthB );
            // 获取年度预算成本
            double yearTotalB = nowMonthB + lastMonthB + futureMonthB;

            // 领料申请成本
            double applyBudget = calcApplyBudget();
            workflowService.setVariable( taskInfo.getProcessInstanceId(), "applyBudget", applyBudget );
            reMap.put( "applyBudget", applyBudget );

            // 年度预算剩余金额
            double remainBudget = yearTotalB - applyBudget;
            workflowService.setVariable( taskInfo.getProcessInstanceId(), "remainBudget", remainBudget );
            reMap.put( "remainBudget", remainBudget );

            Object totalPriceObj = workflowService.getVariable( taskInfo.getProcessInstanceId(), "totalPrice" );
            BigDecimal totalPriceBD = new BigDecimal( String.valueOf( totalPriceObj ) );
            // 是否超标
            String isOver = "0";
            if ( remainBudget - totalPriceBD.doubleValue() < 0 ) {
                isOver = "1";
            }
            workflowService.setVariable( taskInfo.getProcessInstanceId(), "isOver", isOver );
            reMap.put( "isOver", isOver );
        } catch (Exception e) {
            throw new RuntimeException( "---------Draft 中的 calcThisYearRemainBudget 方法抛出异常---------：", e );
        }
        return reMap;
    }

    /**
     * @description:计算未来月份的预算成本
     * @author: 890166
     * @createDate: 2014-9-18
     * @param nowMonthBudget
     * @return
     * @throws Exception :
     */
    private double calcFutureMonthBudget(double nowMonthBudget) throws Exception {
        // 获取当前月份
        Calendar c = Calendar.getInstance();
        String nowMonth = new SimpleDateFormat( "MM" ).format( c.getTime() );

        // 今年还剩多少个月
        int hasMonth = 12 - Integer.parseInt( nowMonth );

        // 计算未来月份预算成本（当月预算总额 * （12 - 当前月）= 未来月份的预算总额）
        double futureMonthBudget = nowMonthBudget * hasMonth;
        return futureMonthBudget;
    }

    /**
     * @description:计算截止上月为止的累计成本预算
     * @author: 890166
     * @createDate: 2014-9-18
     * @return
     * @throws Exception :
     */
    private double calcGrandTotalMonthBudget() throws Exception {
        // 申请部门id
        String orgId = null;
        // 站点id
        String siteId = null;

        double grandTotal = 0.0D;

        // 获取上一月份
        Calendar c = Calendar.getInstance();
        c.add( Calendar.MONTH, -1 );
        String lastMonth = new SimpleDateFormat( "yyyy-MM" ).format( c.getTime() );
        String[] dateArr = lastMonth.split( "-" );

        UserInfo ui = itcMvcService.getUserInfoScopeDatas();
        SecureUser su = ui.getSecureUser();
        List<Organization> orgList = su.getOrganizations();
        if ( null != orgList && !orgList.isEmpty() ) {
            Organization org = orgList.get( 0 );
            orgId = org.getCode();
            siteId = ui.getSiteId();
        }

        if ( !"12".equals( dateArr[1] ) ) {
            // 查询查询过去累计成本预算（一月预算总额 到 当月的上个月预算总额 之和 = 已过去月份的预算总额）
            grandTotal = invMatBudgetService.queryBudgetGrandTotalLastMonth( orgId, lastMonth, siteId );
        }

        return grandTotal;
    }

    /**
     * @description:计算当月预算成本
     * @author: 890166
     * @createDate: 2014-9-18
     * @return:
     * @throws Exception
     */
    private double calcNowMonthBudget(boolean needSave) throws Exception {
        // 申请部门id
        String orgId = null;
        // 站点id
        String siteId = null;
        // 每个人限额
        double perBudget = Double.parseDouble( CommonUtil.getProperties( "perBudget" ) );
        // 申请人所在部门人数
        int perCount = 0;
        InvMatBudget imb = null;
        UserInfo ui = itcMvcService.getUserInfoScopeDatas();
        SecureUser su = ui.getSecureUser();
        List<Organization> orgList = su.getOrganizations();
        if ( null != orgList && !orgList.isEmpty() ) {
            Organization org = orgList.get( 0 );
            orgId = org.getCode();
            siteId = ui.getSiteId();
            List<SecureUser> suList = iAuthorizationManager.retriveActiveUsersOfGivenOrg( orgId, true, su );
            perCount = suList.size();
        }

        // 当月预算总额 （当月申请人所在部门人数 * 25 = 当月预算总额）
        double nowMonthBudget = perBudget * perCount;

        if ( needSave ) {
            // 获取当前月份
            Calendar c = Calendar.getInstance();
            String nowMonth = new SimpleDateFormat( "yyyy-MM" ).format( c.getTime() );
            // 查询每月预算成本表中看是否存在当月记录
            List<InvMatBudget> imbList = invMatBudgetService.queryInvMatBudget( orgId, nowMonth, siteId );

            if ( null != imbList && !imbList.isEmpty() ) {
                imb = imbList.get( 0 );
                imb.setBudget( new BigDecimal( nowMonthBudget ) );
                imb.setModifydate( new Date() );
                imb.setModifyuser( su.getId() );
                imb.setSetdate( new Date() );
                invMatBudgetService.updateInvMatBudget( imb );
            } else {
                imb = new InvMatBudget();
                imb.setBudget( new BigDecimal( nowMonthBudget ) );
                imb.setCreatedate( new Date() );
                imb.setCreateuser( su.getId() );
                imb.setDeptid( orgId );
                imb.setModifydate( new Date() );
                imb.setModifyuser( su.getId() );
                imb.setSetdate( new Date() );
                imb.setSiteId( siteId );
                invMatBudgetService.insertInvMatBudget( imb );
            }
        }
        return nowMonthBudget;
    }

    /**
     * @description:当月及当月以前领料申请总额
     * @author: 890166
     * @createDate: 2014-9-18
     * @return
     * @throws Exception :
     */
    private double calcApplyBudget() throws Exception {
        double applyBudget = 0.00D;

        String siteId = null;
        StringBuilder userIdBuf = new StringBuilder( "'" );
        String userIds = "";

        // 当前月
        Calendar c = Calendar.getInstance();
        String nowMonth = new SimpleDateFormat( "yyyy-MM" ).format( c.getTime() );

        // 获取申请人所在部门人员信息
        UserInfo ui = itcMvcService.getUserInfoScopeDatas();
        SecureUser su = ui.getSecureUser();
        List<Organization> orgList = su.getOrganizations();
        if ( null != orgList && !orgList.isEmpty() ) {
            Organization org = orgList.get( 0 );
            siteId = ui.getSiteId();
            List<SecureUser> suList = iAuthorizationManager.retriveActiveUsersOfGivenOrg( org.getCode(), true, su );
            if ( null != suList && !suList.isEmpty() ) {
                for ( SecureUser sus : suList ) {
                    userIdBuf.append( sus.getId() ).append( "','" );
                }
                userIds = userIdBuf.toString().substring( 0, userIdBuf.toString().length() - 2 );

                applyBudget = invMatApplyDetailService.queryApplyBudget( userIds, nowMonth, siteId );
            }
        }

        return applyBudget;
    }
}
