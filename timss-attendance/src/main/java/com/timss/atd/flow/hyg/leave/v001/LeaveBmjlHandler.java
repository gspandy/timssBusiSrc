package com.timss.atd.flow.hyg.leave.v001;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.bean.LeaveBean;
import com.timss.attendance.bean.LeaveItemBean;
import com.timss.attendance.dao.LeaveDao;
import com.timss.attendance.service.LeaveService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.DateFormatUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.timss.attendance.util.VOUtil;
import com.timss.attendance.vo.LeaveItemVo;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: 部门经理审核
 * @description: {desc}
 * @company: gdyd
 * @className: LeaveApplyHandler.java
 * @author: fengzt
 * @createDate: 2014年9月1日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class LeaveBmjlHandler extends TaskHandlerBase {
    
    private Logger log = Logger.getLogger( LeaveBmjlHandler.class );

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private LeaveDao leaveDao;
    
    @Autowired
    private LeaveService leaveService;

    @Autowired
    private AtdUserPrivUtil privUtil;

    @Autowired
    private AtdWorkFlowUtil wfUtil;
    
    final String handlerName="HYG LeaveBmjlHandler";
    
    /**
     * @description:拿到用户角色( 如： 部门经理、总经理、副总、员工 )
     * @author: fengzt
     * @createDate: 2014年9月9日
     * @return:String
     */
    private String getUserRoleType() {
        String type = null;
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        List<Role> roles = userInfo.getRoles();
        String siteId = userInfo.getSiteId();
        //部长
        String deptBz = siteId + "_CWBBZ," + siteId + "_GCBBZ," + siteId + "_JHHTBBZ,"
                        + siteId + "_SABBZ," + siteId + "_XMBBZ," + siteId + "_YXBBZ," + siteId + "_ZHBBZ,";
        //副总
        String viceMgrStr = siteId + "_FSJ," + siteId + "_FZJL,";
        for ( Role role : roles ) {
            String roleId = role.getId();
            if ( roleId.equalsIgnoreCase( siteId + "_ZJL" ) ) {
                type = "chiefMgr";
                break;
            } else if ( viceMgrStr.indexOf( roleId +  "," ) >= 0 ) {
                type = "viceMgr";
                break;
            } else if ( deptBz.indexOf( roleId +  "," ) >= 0 ) {
                type = "deptMgr";
                break;
            }else if ( roleId.equalsIgnoreCase( siteId + "_BZRY" ) ) {
                type = "banzu";
                break;
            } else {
                type = "normal";
            }
        }
        return type;
    }

    /**
     * 回滚前
     */
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
        log.info( handlerName + " beforeRollback" );
        super.beforeRollback( sourceTaskInfo, destTaskKey );
    }

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {

        log.info(handlerName + "init" );

        String instanceId = taskInfo.getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId" ).toString();
        LeaveBean leaveBean = leaveService.queryLeaveBeanById(Integer.parseInt( id ));
        List<LeaveItemBean> leaveItemVos = leaveBean.getItemList();
        // 设置参数 部门id
        //workflowService.setVariable( instanceId, "orgId", leaveBean.getDeptId() );
        // 设置参数 请假天数
        //workflowService.setVariable( instanceId, "countDay", leaveBean.getLeaveDays() );

        if ( leaveItemVos != null && !leaveItemVos.isEmpty() ) {
            String applyType = null;
            String birthStr = "zjw_le_category_4,zjw_le_category_5,zjw_le_category_9,zjw_le_category_10,zjw_le_category_12,";
            for ( LeaveItemBean vo : leaveItemVos ) {
                // 含有多条明细
                if ( birthStr.indexOf( vo.getCategory() + "," ) >= 0 ) {
                    applyType = "birth";
                    break;
                }else{
                    applyType = "other";
                }
            }
            // 设置参数 是否病假
            workflowService.setVariable( instanceId, "applyType", applyType );
            
            String isAnnualLeave="N";
            for ( LeaveItemBean vo : leaveItemVos ) {
                // 含有多条明细
                if ( "zjw_le_category_1".equals( vo.getCategory())) {
                	isAnnualLeave="Y";
                    break;
                }
            }
            // 设置参数 是否病假
            workflowService.setVariable( instanceId, "isAnnualLeave", isAnnualLeave );
        }
        
        String typeTemp = (String)workflowService.getVariable( instanceId, "type" );
        if( StringUtils.isBlank( typeTemp ) ){
            // 用户角色
            String type = getUserRoleType();
    
            workflowService.setVariable( instanceId, "type", type );
        }

        List<Task> activities = workflowService.getActiveTasks(instanceId);
        
        String processName = activities.get( 0 ).getName();
        log.info( "现在流程的节点名字：" + processName );
        
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", ProcessStatusUtil.HYG_LEAVE_BMJLPZ );
        parmas.put( "instanceId", instanceId );
        parmas.put( "id", id );
        leaveDao.updateOperUserById( parmas );
        super.init( taskInfo );
    }

    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
        log.info( "LeaveApplyHandler onComplete" );
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        String instanceId = taskInfo.getProcessInstanceId();
        
        wfUtil.setIsCommited(instanceId);
        super.onComplete( taskInfo );
    }

    /**
     * @description:转化为List bean
     * @author: fengzt
     * @createDate: 2014年9月9日
     * @param rowDatas
     * @param leaveId  
     * @return:List<LeaveItemBean>
     */
    
    @Override
    public void onShowAudit(String taskId) {
    	String instanceId = workflowService.getTaskByTaskId(taskId).getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId" ).toString();
        LeaveBean leaveBean = leaveService.queryLeaveBeanById(Integer.parseInt( id ));
        List<LeaveItemBean> leaveItemVos = leaveBean.getItemList();
    	String orgId=leaveBean.getDeptId();
        workflowService.setVariable(instanceId, "regId", orgId);
		super.onShowAudit(taskId);
    }
}
