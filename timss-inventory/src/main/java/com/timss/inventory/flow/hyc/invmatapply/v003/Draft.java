package com.timss.inventory.flow.hyc.invmatapply.v003;

import java.util.List;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.bean.InvMatApplyDetail;
import com.timss.inventory.service.InvMatApplyService;
import com.timss.inventory.utils.CommonUtil;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.HistoryInfoService.HistoricTask;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: Draft.java
 * @author: 890166
 * @createDate: 2014-7-8
 * @updateUser: 890166
 * @version: 1.0
 */
public class Draft extends TaskHandlerBase {
	private static Logger LOG = Logger.getLogger( Draft.class );

    @Autowired
    public ItcMvcService itcMvcService;
    @Autowired
    public IAuthorizationManager authManager;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    InvMatApplyService invMatApplyService;

    /**
     * hyc站点首环节提交后必做的一个判断，判断申请人是否经理，若是经理则不再走“申请部门经理审批”环节
     */
    @Override
    public void init(TaskInfo taskInfo) {
        boolean mgrFlag = false;
        boolean inteMgrFlag = false;

        String userId = null;
        UserInfo ui = null;
        // 拿到当前环节实例id
        String processId = taskInfo.getProcessInstanceId();
        // 拿到“经理”角色的编码
        String mgr = CommonUtil.getProperties( "manager" );
        String zhbMgr = CommonUtil.getProperties( "ZHBManager" );

        List<HistoricTask> htList = workflowService.getPreviousTasks( processId );
        if ( null != htList && !htList.isEmpty() ) {
            HistoricTask ht = htList.get( 0 );
            userId = ht.getOwner();
        }

        if ( null == userId ) {
            ui = itcMvcService.getUserInfoScopeDatas();
            userId = ui.getUserId();
        } else {
            ui = itcMvcService.getUserInfoById( userId );
        }

        String siteId = ui.getSiteId();
        SecureUser su = authManager.retriveUserById( userId, siteId );
        // 当前站点下的人员角色列表
        List<Role> roList = su.getRoles();
        if ( null != roList && !roList.isEmpty() ) {
            for ( Role role : roList ) {
                // 系统角色编码与properties中的对比，若出现相同则认定这人是经理
                if ( mgr.indexOf( role.getId() ) > -1 && !mgrFlag ) {
                    mgrFlag = true;
                }

                if ( zhbMgr.indexOf( role.getId() ) > -1 && !inteMgrFlag ) {
                    inteMgrFlag = true;
                }
            }
        }

        if ( mgrFlag ) {
            workflowService.setVariable( processId, "isMgr", "Y" );
        } else {
            workflowService.setVariable( processId, "isMgr", "N" );
        }

        if ( inteMgrFlag ) {
            workflowService.setVariable( processId, "isInteMgr", "Y" );
        } else {
            workflowService.setVariable( processId, "isInteMgr", "N" );
        }
    }
    
    @Override
    public void onComplete(TaskInfo taskInfo) {
    	String processInstId = taskInfo.getProcessInstanceId();
    	Task task = workflowService.getTaskByTaskId(taskInfo.getTaskInstId());
    	List< String > taskNextList = workflowService.getNextTaskDefKeys( processInstId , task.getTaskDefinitionKey() );
	    if ( null != taskNextList && !taskNextList.isEmpty() ) {
	    	String status = taskNextList.get( 0 );
	    	InvMatApply ima = new InvMatApply();
	    	ima.setStatus(status);
	    	Object imaidObj = workflowService.getVariable( processInstId, "imaid" );
	    	Object imaList = workflowService.getVariable( processInstId, "imaList" );
	    	List< InvMatApplyDetail > listData = (List<InvMatApplyDetail>) imaList;
            String imaid = imaidObj == null ? "" : String.valueOf( imaidObj );
	        if ( !"".equals( imaid ) ) {
	        	ima.setImaid(imaid);
	        	try {
					invMatApplyService.updateInvMatApply( listData,ima );
				} catch (Exception e) {
					 LOG.info( ">>>>>>>>>>>>>>>>>>> 领料onComplete update 报错：" + e );
				}
	        }
	    }
    } 
}
