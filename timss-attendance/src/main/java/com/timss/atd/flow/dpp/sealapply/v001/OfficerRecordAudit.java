package com.timss.atd.flow.dpp.sealapply.v001;

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
 * @title: 文书员汇总
 * @company: gdyd
 * @author: 890199
 * @createDate: 2016-09-07
 * @updateUser: 890199
 * @version:1.0
 */
public class OfficerRecordAudit extends TaskHandlerBase{
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired 
	private SealApplyDao sealApplyDao;
	
	private static Logger logger = Logger.getLogger(OfficerRecordAudit.class);
	/**
	 * 初始化
	 */
	@Override
	public void init(TaskInfo taskInfo){
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String instantId = taskInfo.getProcessInstanceId();
        String saId = workflowService.getVariable( instantId, "businessId").toString();
        logger.info( "OfficerRecordAudit init --- instantId = " + instantId + "-- saId = " + saId );
        SealApplyBean sab = new SealApplyBean();
        sab.setSaId(saId);
        sab.setStatus("officer_record_audit");
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
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String instantId = taskInfo.getProcessInstanceId();
        String saId = workflowService.getVariable( instantId, "businessId").toString();
        logger.info( "OfficerRecordAudit init --- instantId = " + instantId + "-- saId = " + saId );
        SealApplyBean sab = new SealApplyBean();
        sab.setSaId(saId);
        sab.setStatus("end");
        sab.setModifydate(new Date());
        sab.setModifyuser(userInfo.getUserId());
        sealApplyDao.updateSealApply(sab);
        super.init( taskInfo );
    }
}
