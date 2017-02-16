package com.timss.atd.flow.dpp.leave.v001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.bean.LeaveBean;
import com.timss.attendance.bean.LeaveItemBean;
import com.timss.attendance.service.LeaveService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.VOUtil;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;

/**
 * @title: 提交请假申请
 * @description: {desc}
 * @company: gdyd
 * @className: LeaveApplyHandler.java
 * @author: yyn
 * @createDate: 2017年2月8日
 * @updateUser: gucw
 * @version: 1.0
 */
public class LeaveApplyHandler extends TaskHandlerBase {

    private Logger log = Logger.getLogger( LeaveApplyHandler.class );

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private AtdUserPrivUtil privUtil;

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private IAuthorizationManager authManager;

    String handlerName = "DPP LeaveApplyHandler";

    @Override
    public void onShowAudit(String taskId) {
        log.info( handlerName + " onShowAudit" );
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        try {
            // 拿出传入的参数
            Map<String, Object> params = VOUtil.fromJsonToHashMap( userInfo.getParam( "businessData" ) );
            if ( params.get( "leaveId" ) != null ) {
                Integer leaveId = Integer.parseInt( params.get( "leaveId" ).toString() );
                LeaveBean bean = leaveService.queryDetail( leaveId );
                if ( bean != null ) {
                    setFlowParamsByLeave( bean.getInstanceId(), bean, bean.getItemList() );
                } else {
                    throw new RuntimeException( "null leave bean of onShowAudit->taskId:" + taskId );
                }
            } else {
                throw new RuntimeException( "null leaveId in params of onShowAudit->taskId:" + taskId );
            }
        } catch (Exception e) {
            throw new RuntimeException( e );
        }
    }

    // 设置工作流需要的参数
    private void setFlowParamsByLeave(String instanceId, LeaveBean leaveBean, List<LeaveItemBean> leaveItemVos) {
        /* 确定年休假 */
        for ( LeaveItemBean v : leaveItemVos ) {
            log.info( "msg==1" + v.getCategory() );
        }
        String applicant = leaveBean.getCreateBy();
        @SuppressWarnings("deprecation")
        String orgId = leaveBean.getDeptId();
        // 设置countDay 请假天数
        workflowService.setVariable( instanceId, "countDay", leaveBean.getLeaveDays() );

        // 班组意见还是部门审批
        String goTo = "BMSP";
        String bzcyIds = privUtil.getGroupUserIdsStr( "DPP_BZCY" );// 班组成员id
        String bzzfIds = privUtil.getGroupUserIdsStr( "DPP_BZZFZ" );// 班组正副职id
        String bzzfzIds = privUtil.getGroupUserIdsStr( "DPP_BMFZR" );// 部门领导

        if ( bzzfzIds.contains( applicant + "," ) ) {
            /* 部门领导 */
            goTo = "LDSP";
        } else if ( bzcyIds.contains( applicant + "," ) && !bzzfIds.contains( applicant + "," ) ) {
            /* 班组副职, 部门副职 */
            goTo = "BZYJ";
        } else {
            goTo = "BMSP";
        }
        /* 分部正副职,部门副职 */
        workflowService.setVariable( instanceId, "goto", goTo );
        workflowService.setVariable( instanceId, "orgId", orgId );

        // 设置结束节点
        String finish = "RLZYBSP";// 默认到最后节点结束
        if ( leaveItemVos != null && !leaveItemVos.isEmpty() ) {
            Boolean isAppear = false;// 是否有病假、事假或补休假
            Double sickDay = 0.0, eventDay = 0.0, compensateDay = 0.0, annulDay = 0.0;// 病假、事假、补休假天数
            for ( LeaveItemBean vo : leaveItemVos ) {
                String category = vo.getCategory();
                Double days = vo.getLeaveDays();
                // 判断是否走领导审批分支
                List<Role> curRoles = itcMvcService.getUserInfoScopeDatas().getRoles();
                boolean isCz = false;
                boolean isFz = false;
                for ( Role role : curRoles ) {
                    if ( "DPP_CLD_CZ".equals( role.getId() ) ) {
                        isCz = true;
                        break;
                    } else if ( "DPP_CLD_FZ".equals( role.getId() ) ) {
                        isFz = true;
                        break;
                    }
                }
                if ( isCz || isFz ) {
                    workflowService.setVariable( instanceId, "type", "CLDQJ" );
                    List<SecureUser> candidateList = new ArrayList<SecureUser>( 0 );
                    StringBuffer candidateStr = new StringBuffer( "" );
                    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
                    if ( isCz ) {
                        candidateList = authManager.retriveUsersWithSpecificRoleAndSite( "DPP_CLD_FZ",
                                userInfo.getSiteId() );
                    } else if ( isFz ) {
                        candidateList = authManager.retriveUsersWithSpecificRoleAndSite( "DPP_CLD_CZ",
                                userInfo.getSiteId() );
                    }
                    for ( SecureUser secureUser : candidateList ) {
                        candidateStr.append( secureUser.getId() ).append( "," );
                    }
                    String ldspStr = candidateStr.length() > 0 ? candidateStr.substring( 0, candidateStr.length() - 1 ): "";
                    workflowService.setVariable( instanceId, "Ldsp", ldspStr );
                } else if ( "dpp_le_category_3".equals( category ) && days >= 1 ) {
                    sickDay += days;
                    workflowService.setVariable( instanceId, "type", "YWZZQR" );
                } else {
                    workflowService.setVariable( instanceId, "type", "OTHER" );
                    if ( "dpp_le_category_3".equals( category ) ) {
                        sickDay += days;
                        isAppear = true;
                    } else if ( "dpp_le_category_2".equals( category ) ) {
                        eventDay += days;
                        isAppear = true;
                    } else if ( "dpp_le_category_6".equals( category ) ) {
                        compensateDay += days;
                        isAppear = true;
                    } else if ( "dpp_le_category_1".equals( category ) ) {
                        annulDay += days;
                        isAppear = true;
                    }
                }
            }
            if ( isAppear && compensateDay <= 5 && sickDay < 1 && eventDay < 1 && annulDay <= 5 ) {
                finish = "BMSP";
            } else if (eventDay >= 3 || sickDay >= 10) {
                finish = "FGLDSP";
            }
        }
        workflowService.setVariable( instanceId, "finish", finish );
    }
}
