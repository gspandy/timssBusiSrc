package com.timss.atd.flow.hyg.sealapply.v001;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.bean.SealApplyBean;
import com.timss.attendance.dao.SealApplyDao;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;
/**
 *公司领导审批
 */
public class GsldAudit extends TaskHandlerBase{
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired 
	private SealApplyDao sealApplyDao;
	
	private static Logger logger = Logger.getLogger(GsldAudit.class);
	/**
	 * 初始化
	 */
	@Override
	public void init(TaskInfo taskInfo){
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String instantId = taskInfo.getProcessInstanceId();
        String saId = workflowService.getVariable( instantId, "businessId").toString();
        logger.info( "GSLDAudit init --- instantId = " + instantId + "-- saId = " + saId );
        SealApplyBean sab = new SealApplyBean();
        sab.setSaId(saId);
        sab.setStatus("gsld_audit");
        sab.setModifydate(new Date());
        sab.setModifyuser(userInfo.getUserId());
        sealApplyDao.updateSealApply(sab);
        super.init( taskInfo );
	}
	
	/**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
        super.onComplete( taskInfo );
    }
}
