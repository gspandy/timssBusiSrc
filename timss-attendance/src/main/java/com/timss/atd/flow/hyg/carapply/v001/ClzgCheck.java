package com.timss.atd.flow.hyg.carapply.v001;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.bean.CarApplyBean;
import com.timss.attendance.dao.CarApplyDao;
import com.timss.attendance.service.CarApplyService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 车辆主管审核
 */
public class ClzgCheck extends TaskHandlerBase{
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
    private WorkflowService workflowService;
	@Autowired
	private CarApplyDao carApplyDao;
	@Autowired
	private CarApplyService carApplyService;
	
	private static final Logger LOG = Logger.getLogger( ClzgCheck.class );
	
	
	public void init(TaskInfo taskInfo){
		String caId = workflowService.getVariable(taskInfo.getProcessInstanceId(),  "businessId").toString();
		String status = "clzgCheck";
		CarApplyBean bean = new CarApplyBean();
		bean.setCaId(caId);
		bean.setStatus(status);
		carApplyDao.updateCarApply(bean);
	}
	
	public void onComplete(TaskInfo taskInfo){
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String caId = workflowService.getVariable(taskInfo.getProcessInstanceId(),  "businessId").toString();
		try {
			carApplyService.updateCurrHandlerUser(caId, userInfo, "normal");
			
		} catch (Exception e) {
			LOG.error("获取审批信息失败");
		}
	}
	public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String caId = workflowService.getVariable(taskInfo.getProcessInstanceId(),  "businessId").toString();
			try {
				carApplyService.updateCurrHandlerUser(caId, userInfo, "rollback");
			} catch (Exception e) {
				 LOG.error( e.getMessage() );
			}
	}
}
