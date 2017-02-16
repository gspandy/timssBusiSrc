package com.timss.atd.flow.zjw.abnormity.v001;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.bean.AbnormityBean;
import com.timss.attendance.bean.AbnormityItemBean;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.VOUtil;
import com.yudean.itc.dto.sec.Role;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 提交请假申请
 */
public class AbnormityApplyHandler extends TaskHandlerBase {
    
    private Logger log = Logger.getLogger( AbnormityApplyHandler.class );

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private AtdUserPrivUtil privUtil;

    @Autowired
    private AtdWorkFlowUtil wfUtil;
    
    final String handlerName="ZJW AbnormityApplyHandler";
    final String siteId="ZJW";
    
    /**
     * @description:拿到用户角色( 如： 部门经理、总经理、副总、员工 )
     * @author: fengzt
     * @createDate: 2014年9月9日
     * @return:String
     */
    private String getUserRoleType(String userId) {
        String type = null;
        UserInfo userInfo=itcMvcService.getUserInfoById(userId);
        List<Role> roles = userInfo.getRoles();
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
                if( !StringUtils.equals( "banzhang", type )){
                    type = "banzu";
                }
            }else if ( roleId.equalsIgnoreCase( siteId + "_BZSH" ) ) {
                type = "banzhang";
                break;
            }else {
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
        log.info( handlerName+" beforeRollback" );
        super.beforeRollback( sourceTaskInfo, destTaskKey );
    }

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {
        log.info( handlerName+" init" );
        wfUtil.initCommonAbnormityApply(taskInfo);
        super.init( taskInfo );
    }

    @Override
    public void onShowAudit(String taskId){
    	log.info( handlerName+" onShowAudit" );
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        try {
            //update的时候拿出传入的参数
        	Map<String, Object>params=VOUtil.fromJsonToHashMap(userInfo.getParam( "businessData" ));
        	AbnormityBean bean=wfUtil.updateAbnormity(userInfo.getParam( "businessData" ));
            String instanceId = params.get( "processInstId" ).toString();
            //取第一个详情项来决定流程走向
        	AbnormityItemBean firstItem=bean.getItemList().get(0);
            // 用户角色
            String type = getUserRoleType(firstItem.getUserId());
            workflowService.setVariable( instanceId, "type", type );
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
    }
    
    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
        log.info( handlerName+" onComplete" );
        wfUtil.completeCommonAbnormityApply(taskInfo);
        super.onComplete( taskInfo );
    }
}
