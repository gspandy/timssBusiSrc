package com.timss.attendance.util;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.attendance.bean.AbnormityBean;
import com.timss.attendance.bean.DefinitionBean;
import com.timss.attendance.bean.LeaveBean;
import com.timss.attendance.bean.OvertimeBean;
import com.timss.attendance.bean.RationalizationBean;
import com.timss.attendance.bean.TrainingBean;
import com.timss.attendance.service.AbnormityService;
import com.timss.attendance.service.DefinitionService;
import com.timss.attendance.service.LeaveService;
import com.timss.attendance.service.OvertimeService;
import com.timss.attendance.service.RationalzationService;
import com.timss.attendance.service.StatService;
import com.timss.attendance.service.TrainingService;
import com.timss.attendance.service.WorkStatusService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: 考勤工作流通用函数
 */
@Service("atdWorkFlowUtil")
public class AtdWorkFlowUtil {
	@Autowired
    private WorkStatusService workStatusService;
	@Autowired
    private LeaveService leaveService;
	@Autowired
    private AbnormityService abnormityService;
	@Autowired
    private OvertimeService overtimeService;
	@Autowired
    private TrainingService trainingService;
	@Autowired
    private DefinitionService definitionService;
	@Autowired
    private WorkflowService workflowService;
	@Autowired
    private StatService statService;
	@Autowired
	private RationalzationService rationalzationService;
	
	
	/**
	 * 根据请假申请单刷新打卡统计和休假统计
	 * @param leaveId
	 * @throws Exception
	 */
	public void checkWorkStatusAndStatByLeaveId(Integer leaveId)throws Exception{
		LeaveBean bean=leaveService.queryLeaveBeanById(leaveId);
		DefinitionBean definitionBean=definitionService.queryDefinitionBySiteId(bean.getSiteId());
		workStatusService.checkLeaveWorkStatus(bean, definitionBean);
		statService.checkLeaveStat(bean,definitionBean);
	}
	/**
	 * 从taskinfo中获取申请单数据更新节点状态
	 * @param taskInfo
	 * @param status
	 * @return
	 */
	public Boolean updateLeaveAuditStatusByTask(TaskInfo taskInfo,String status){
		String instanceId = taskInfo.getProcessInstanceId();
		Integer id=Integer.parseInt(workflowService.getVariable( instanceId, "businessId" ).toString());
        return leaveService.updateAuditStatus(id,instanceId,status);
	}
	
	/**
	 * 根据考勤异常单刷新打卡统计
	 * @param abnormityId
	 * @throws Exception
	 */
	public void checkWorkStatusByAbnormityId(Integer abnormityId)throws Exception{
		AbnormityBean bean=abnormityService.queryAbnormityBeanById(abnormityId);
		DefinitionBean definitionBean=definitionService.queryDefinitionBySiteId(bean.getSiteId());
		workStatusService.checkAbnormityWorkStatus(bean, definitionBean);
	}
	public AbnormityBean updateAbnormity(String abnormityData){
		JSONObject jsonObject = JSONObject.fromObject( abnormityData );
		AbnormityBean bean=abnormityService.convertBean(jsonObject.getString( "formData" ), jsonObject.getString( "fileIds" ), 
				jsonObject.getString( "addRows" ), jsonObject.getString( "delRows" ), jsonObject.getString( "updateRows" ));
		if(abnormityService.insertOrUpdateAbnormity(bean)>0){
			return (AbnormityBean)abnormityService.queryAbnormityById(bean.getId()).get("rowData");
		}else{
			return null;
		}
	}
	/**
	 * 从taskinfo中获取申请单数据更新节点状态
	 * @param taskInfo
	 * @param status
	 * @return
	 */
	public Boolean updateAbnormityAuditStatusByTask(TaskInfo taskInfo,String status){
		String instanceId = taskInfo.getProcessInstanceId();
		Integer id=Integer.parseInt(workflowService.getVariable( instanceId, "businessId" ).toString());
        return abnormityService.updateAuditStatus(id,instanceId,status);
	}
	/**
	 * 站点通用考勤异常申请节点init操作
	 * @param taskInfo
	 * @return
	 */
	public Boolean initCommonAbnormityApply(TaskInfo taskInfo){
		String instanceId = taskInfo.getProcessInstanceId();
        return updateAbnormityAuditStatusByTask(taskInfo,
        		checkIsCommited(instanceId)?ProcessStatusUtil.ABEXCEPTIONAPPLY:ProcessStatusUtil.CAOGAO);
	}
	/**
	 * 站点通用考勤异常申请节点onComplete操作
	 * @param taskInfo
	 * @return
	 */
	public Boolean completeCommonAbnormityApply(TaskInfo taskInfo){
		Boolean result=false;
		String instanceId = taskInfo.getProcessInstanceId();
        if(!checkIsCommited(instanceId)){
        	setIsCommited(instanceId);
        	
        	AbnormityBean bean=new AbnormityBean();
    		bean.setId(Integer.parseInt(workflowService.getVariable( instanceId, "businessId" ).toString()));
        	result=abnormityService.updateAbnormityCreateDay(bean)>0;
		}
        return result;
	}
	
	/**
	 * 根据加班申请单刷新打卡统计和休假统计
	 * @param overtimeId
	 * @throws Exception
	 */
	public void checkWorkStatusAndStatByOvertimeId(Integer overtimeId)throws Exception{
		OvertimeBean bean=overtimeService.queryOvertimeBeanById(overtimeId);
		DefinitionBean definitionBean=definitionService.queryDefinitionBySiteId(bean.getSiteId());
		statService.checkOvertimeStat(bean, definitionBean);
	}
	/**
	 * 更新加班单的核定时长
	 * @param overtimeData
	 * @return
	 */
	public Boolean updateOvertimeRealOverHours(String overtimeData){
		JSONObject jsonObject = JSONObject.fromObject( overtimeData );
		OvertimeBean bean=overtimeService.convertBean(jsonObject.getString( "formData" ), jsonObject.getString( "fileIds" ), 
				jsonObject.getString( "addRows" ), jsonObject.getString( "delRows" ), jsonObject.getString( "updateRows" ));
        return overtimeService.updateOvertimeRealOverHours(bean)>0;
	}
	/**
	 * 更新加班单的核定转补休时长
	 * @param overtimeData
	 * @return
	 */
	public Boolean updateOvertimeTransferCompensate(Integer overtimeId,String overtimeData){
		OvertimeBean bean;
		Boolean isFromRealOverHours;
		if(overtimeId!=null){
			bean=overtimeService.queryOvertimeBeanById(overtimeId);
			isFromRealOverHours=true;
		}else{
			JSONObject jsonObject = JSONObject.fromObject( overtimeData );
			bean=overtimeService.convertBean(jsonObject.getString( "formData" ), jsonObject.getString( "fileIds" ), 
					jsonObject.getString( "addRows" ), jsonObject.getString( "delRows" ), jsonObject.getString( "updateRows" ));
			isFromRealOverHours=false;
		}
		return overtimeService.updateOvertimeTransferCompensate(bean,isFromRealOverHours)>0;
	}
	/**
	 * 更新加班单的信息
	 * @param overtimeData
	 * @return
	 */
	public OvertimeBean updateOvertime(String overtimeData){
		JSONObject jsonObject = JSONObject.fromObject( overtimeData );
		OvertimeBean bean=overtimeService.convertBean(jsonObject.getString( "formData" ), jsonObject.getString( "fileIds" ), 
				jsonObject.getString( "addRows" ), jsonObject.getString( "delRows" ), jsonObject.getString( "updateRows" ));
        if(overtimeService.insertOrUpdateOvertime(bean)>0){
			return (OvertimeBean)overtimeService.queryOvertimeBeanById(bean.getId());
		}else{
			return null;
		}
	}
	/**
	 * 从taskinfo中获取申请单数据更新节点状态
	 * @param taskInfo
	 * @param status
	 * @return
	 */
	public Boolean updateOvertimeAuditStatusByTask(TaskInfo taskInfo,String status){
		String instanceId = taskInfo.getProcessInstanceId();
		Integer id=Integer.parseInt(workflowService.getVariable( instanceId, "businessId" ).toString());
        return overtimeService.updateAuditStatus(id,instanceId,status);
	}
	/**
	 * 站点通用加班申请节点init操作
	 * @param taskInfo
	 * @return
	 */
	public Boolean initCommonOvertimeApply(TaskInfo taskInfo){
		String instanceId = taskInfo.getProcessInstanceId();
        return updateOvertimeAuditStatusByTask(taskInfo,
        		checkIsCommited(instanceId)?ProcessStatusUtil.JBSQ:ProcessStatusUtil.CAOGAO);
	}
	/**
	 * 站点通用加班申请节点onComplete操作
	 * @param taskInfo
	 * @return
	 */
	public Boolean completeCommonOvertimeApply(TaskInfo taskInfo){
		Boolean result=false;
		String instanceId = taskInfo.getProcessInstanceId();
        if(!checkIsCommited(instanceId)){
        	setIsCommited(instanceId);
        	
        	OvertimeBean bean=new OvertimeBean();
    		bean.setId(Integer.parseInt(workflowService.getVariable( instanceId, "businessId" ).toString()));
        	result=overtimeService.updateOvertimeCreateDay(bean)>0;
		}
        return result;
	}
	
	/**
	 * 保存
	 * @param trainingData
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	public TrainingBean updateTraining(String trainingData,SecureUser operator) throws Exception{
		JSONObject jsonObject = JSONObject.fromObject( trainingData );
		TrainingBean bean=trainingService.convertBean(jsonObject.getString( "formData" ), jsonObject.getString( "fileIds" ), 
				jsonObject.getString( "addRows" ), jsonObject.getString( "delRows" ), jsonObject.getString( "updateRows" ));
		if(trainingService.insertOrUpdate(bean,operator)>0){
			return trainingService.queryDetail(bean.getTrainingId());
		}else{
			return null;
		}
	}
	/**
	 * 从taskinfo中获取申请单数据更新节点状态
	 * @param taskInfo
	 * @param status
	 * @return
	 * @throws Exception 
	 */
	public Boolean updateTrainingAuditStatusByTask(TaskInfo taskInfo,String status,SecureUser operator) throws Exception{
		String instanceId = taskInfo.getProcessInstanceId();
		String id=workflowService.getVariable( instanceId, "businessId" ).toString();
        return trainingService.updateAuditStatus(id,instanceId,status,operator)>0;
	}
	/**
	 * 站点通用考勤异常申请节点init操作
	 * @param taskInfo
	 * @return
	 * @throws Exception 
	 */
	public Boolean initCommonTrainingApply(TaskInfo taskInfo,SecureUser operator) throws Exception{
		String instanceId = taskInfo.getProcessInstanceId();
		return updateTrainingAuditStatusByTask(taskInfo,
        		checkIsCommited(instanceId)?ProcessStatusUtil.TRAINING_TJSQ:ProcessStatusUtil.CAOGAO,operator);
	}
	/**
	 * 站点通用考勤异常申请节点onComplete操作
	 * @param taskInfo
	 * @return
	 * @throws Exception 
	 */
	public Boolean completeCommonTrainingApply(TaskInfo taskInfo,SecureUser operator) throws Exception{
		Boolean result=true;
		String instanceId = taskInfo.getProcessInstanceId();
        if(!checkIsCommited(instanceId)){
        	setIsCommited(instanceId);
        	result=trainingService.updateCreateDate(workflowService.getVariable( instanceId, "businessId" ).toString(),null,operator)>0;
		}
        return result;
	}
	
	/**
	 * 检查流程第一个节点是否已提交过<br/>
	 * 用于判断是草稿还是退回的节点
	 * @param instanceId
	 * @return
	 */
	public Boolean checkIsCommited(String instanceId){
		return workflowService.getVariable( instanceId, "ATD_isCommited" )!=null;
	}
	/**
	 * 设置流程第一个节点是否已提交过<br/>
	 * 在流程的第一个节点的onComplete设置
	 * @param instanceId
	 */
	public void setIsCommited(String instanceId){
		workflowService.setVariable( instanceId, "ATD_isCommited", "Y" );
	}
	
	/**
	 * 站点通用考勤异常申请节点onComplete操作
	 * @param taskInfo
	 * @return
	 * @throws Exception 
	 */
	public Boolean completeCommonRationalizationApply(TaskInfo taskInfo,SecureUser operator) throws Exception{
		Boolean result=true;
		String instanceId = taskInfo.getProcessInstanceId();
        if(!checkIsCommited(instanceId)){
        	setIsCommited(instanceId);
        	result=rationalzationService.updateCreateDate(workflowService.getVariable( instanceId, "businessId" ).toString(),null,operator)>0;
		}
        return result;
	}
	
	
	/**
	 * 从taskinfo中获取申请单数据更新节点状态
	 * @param taskInfo
	 * @param status
	 * @return
	 * @throws Exception 
	 */
	public Boolean updateRationAuditStatusByTask(TaskInfo taskInfo,String status,SecureUser operator,String userId) throws Exception{
		String instanceId = taskInfo.getProcessInstanceId();
		//workflowService.setVariable(instanceId,"orgId",operator.getId());
		String id=workflowService.getVariable( instanceId, "businessId" ).toString();
	    return rationalzationService.updateAuditStatus(id,instanceId,status,operator,userId)>0;
	}
	
	
	
	
	public RationalizationBean updateRationalization(String rationData,
			SecureUser secureUser) throws Exception{
		JSONObject jsonObject = JSONObject.fromObject( rationData );
		RationalizationBean bean = VOUtil.fromJsonToVoUtil( jsonObject.getString( "formData" ), RationalizationBean.class);
		 if ( !StringUtils.isBlank( jsonObject.getString( "fileIds" ) ) ) {
	    		String[] fileArr = jsonObject.getString( "fileIds" ) .split( "," );
	            bean.setFileIds(fileArr);
	    	}
		if(rationalzationService.insertOrUpdate(bean,secureUser)>0){
			return rationalzationService.queryDetail(bean.getRationalId());
		}else{
			return null;
		}
	}
	
	/**
	 * 站点通用考勤异常申请节点init操作
	 * @param taskInfo
	 * @return
	 * @throws Exception 
	 */
	public Boolean initCommonRationApply(TaskInfo taskInfo,SecureUser operator,String userId) throws Exception{
		String instanceId = taskInfo.getProcessInstanceId();
	    return rationalzationService.updateRationAuditStatusByTask(taskInfo,
        		checkIsCommited(instanceId)?ProcessStatusUtil.DPP_RATION_TJSQ:ProcessStatusUtil.CAOGAO,operator,userId);
	}
	
	/*通过userId来更新atd_rationalization表中的HandlerName*/
	public Integer updateCommonRationApply(RationalizationBean bean) {
		 return rationalzationService.updateById(bean);
	}
	
}
