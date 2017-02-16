package com.timss.atd.flow.dpp.carapply.v001;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.bean.CarApplyBean;
import com.timss.attendance.dao.CarApplyDao;
import com.timss.attendance.service.AtdAttachService;
import com.timss.attendance.service.CarApplyService;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @description:
 * @author: 
 * @createDate: 
 * @param 
 * @return:
 */
public class XcbAudit extends TaskHandlerBase{
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
    private WorkflowService workflowService;
	@Autowired
	private CarApplyDao carApplyDao;
	@Autowired
	private CarApplyService carApplyService;
	@Autowired 
    private AtdAttachService attachService;
	
	private static final Logger LOG = Logger.getLogger( XcbAudit.class );
	
	public void init(TaskInfo taskInfo){
		String caId = workflowService.getVariable(taskInfo.getProcessInstanceId(),  "businessId").toString();
		String status = "xcbAudit";
		CarApplyBean bean = new CarApplyBean();
		bean.setCaId(caId);
		bean.setStatus(status);
		carApplyDao.updateCarApply(bean);
	}
	public void onComplete(TaskInfo taskInfo){
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String caId = workflowService.getVariable(taskInfo.getProcessInstanceId(),  "businessId").toString();
		String businessData = null;
		try {
			businessData = userInfo.getParam("businessData");
			JSONObject businessDataobj = JSONObject.fromObject( businessData );
			String formData = businessDataobj.getString("carApplyData");
			CarApplyBean oldBean = JsonHelper.toObject(formData , CarApplyBean.class);
			
			CarApplyBean bean = new CarApplyBean();
			bean.setCaId(oldBean.getCaId());
			bean.setCarType(oldBean.getCarType().toUpperCase());//车牌号强制转换成大写
			bean.setDriver(oldBean.getDriver());
			carApplyDao.updateCarApply(bean);
			carApplyService.updateCurrHandlerUser(caId, userInfo, "normal");
			bean.setStatus("end");
			carApplyDao.updateCarApply(bean);
		} catch (Exception e) {
			LOG.error("跟新处理人失败");
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
