package com.timss.workorder.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.bean.Hazard;
import com.timss.workorder.bean.JPItems;
import com.timss.workorder.bean.JPJobtask;
import com.timss.workorder.bean.JPPreHazard;
import com.timss.workorder.bean.JPWorker;
import com.timss.workorder.bean.JobPlan;
import com.timss.workorder.bean.MaintainPlan;
import com.timss.workorder.bean.Precaution;
import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.dao.HazardDao;
import com.timss.workorder.dao.JPItemsDao;
import com.timss.workorder.dao.JPJobtaskDao;
import com.timss.workorder.dao.JPPreHazardDao;
import com.timss.workorder.dao.JPWorkerDao;
import com.timss.workorder.dao.JobPlanDao;
import com.timss.workorder.dao.PrecautionDao;
import com.timss.workorder.service.JobPlanService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.MvcJsonUtil;

@Service("JobPlanServiceImpl")
public class JobPlanServiceImpl implements JobPlanService {
	@Autowired
	private ItcMvcService ItcMvcService;
	@Autowired
	private JobPlanDao jobPlanDao;
	@Autowired
	private JPWorkerDao jpWorkerDao;
	@Autowired
	private JPItemsDao jpItemsDao;
	@Autowired
	private JPJobtaskDao jpJobtaskDao;
	@Autowired
	private JPPreHazardDao jpPreHazardDao;
	@Autowired
	private HazardDao hazardDao;
	@Autowired
	private PrecautionDao precautionDao;
//	@Autowired
//	private InvMatTranService invMatTranService;
	private static Logger logger = Logger.getLogger(JobPlanServiceImpl.class);
	 
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int insertJobPlan(Map<String,String> jpDataMap) throws Exception {
		UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		String deptId = userInfoScope.getOrgId();
		String userId = userInfoScope.getUserId();
		String jpSource = jpDataMap.get("jpSource"); //(standard：标准；plan：策划；actual：实际;maintainPlan:维护计划)
		/**  插入作业方案列表  */
		String jpFormData = jpDataMap.get("jpFormData");
		String mtpFormData = jpDataMap.get("mtpFormData"); 
		String woFormData = jpDataMap.get("woFormData");
		String commitStyle = jpDataMap.get("commitStyle");
		
		String preHazardDataStr = jpDataMap.get("preHazardDataStr");
		JSONObject preHazardJsonObj = JSONObject.fromObject(preHazardDataStr);
		int preHazardDatagridNum =Integer.valueOf(preHazardJsonObj.get("total").toString());  //记录数
		
		String toolDataStr = jpDataMap.get("toolDataStr");
		JSONObject itemsJsonObj = JSONObject.fromObject(toolDataStr);
		int itemsDatagridNum =Integer.valueOf(itemsJsonObj.get("total").toString());  //记录数
		
		String taskDataStr = jpDataMap.get("taskDataStr");
		JSONObject jobtaskJsonObj = JSONObject.fromObject(taskDataStr);
		int jobtaskDatagridNum =Integer.valueOf(jobtaskJsonObj.get("total").toString());  //记录数
		
		String workerDataStr = jpDataMap.get("workerDataStr");
		JSONObject workerJsonObj = JSONObject.fromObject(workerDataStr);
		JSONArray workerJsonArray = null; //人员记录数组
		int workerDatagridNum = 0;   //人员记录数
		if(workerJsonObj.size() != 0){
			workerDatagridNum =Integer.valueOf(workerJsonObj.get("total").toString()); 
			workerJsonArray = workerJsonObj.getJSONArray("rows"); //人员记录数组
		}else{
			workerDatagridNum = 0;  
		}
		
		
		JobPlan jobPlan = null ;
		if(jpFormData != null){  //从作业方案模块页面过来
			jobPlan = JsonHelper.toObject(jpFormData, JobPlan.class);
		}else if(mtpFormData != null){  //若是从维护计划模块页面过来
			MaintainPlan maintainPlan = JsonHelper.toObject(mtpFormData, MaintainPlan.class);
			jobPlan = new JobPlan();
			jobPlan.setDescription(maintainPlan.getDescription());
			jobPlan.setSpecialtyId(maintainPlan.getSpecialtyId());
			jobPlan.setRemarks(maintainPlan.getRemarks());
		}else if(woFormData != null){
			// 从工单模块页面过来
			WorkOrder workOrder = JsonHelper.toObject(woFormData, WorkOrder.class);
			
			Integer jpId = workOrder.getJobPlanId();
			String woCurrStatus = workOrder.getCurrStatus();
			
			if(jpId != null){
				jobPlan = jobPlanDao.queryJPById(jpId);
			}
			
			if(jobPlan != null){ //是否有暂存的作业方案
				if("endWOReport".equals(woCurrStatus) && "actual".equals(jobPlan.getJobPlanType())){  
					jobPlan.setId(jpId);  
				}
				if("woPlan".equals(woCurrStatus) && "plan".equals(jobPlan.getJobPlanType())){
					jobPlan.setId(jpId);  
				}else{
					jobPlan = new JobPlan();
					if("endWOReport".equals(woCurrStatus)){ // 完工汇报，没有暂存时
						jobPlan.setJobPlanType("actual");
					}
				}
			}else{
				jobPlan = new JobPlan();
			}
			
			jobPlan.setWorkOrderId(workOrder.getId());
			jobPlan.setDescription(workOrder.getDescription());
			jobPlan.setSpecialtyId(workOrder.getWoSpecCode());
			jobPlan.setRemarks(workOrder.getRemarks());
		}
		
		Integer jpId = jobPlan.getId();
		if(jpId == null || jpId == 0){  //工单策划阶段，第一次审批或暂存时
			boolean flag = (preHazardDatagridNum==0)&&(itemsDatagridNum==0)&&(jobtaskDatagridNum==0)&&(workerDatagridNum==0);
			if(flag == true){  //若没有任何的策划信息（工单页面下部的信息）
				return 0;
			}else{
				jpId = jobPlanDao.getNextJPId(); //获取要插入作业方案的ID
				jobPlan.setId(jpId);
				jobPlan.setCreateuser(userId);
				jobPlan.setCreatedate(new Date());
				jobPlan.setModifydate(new Date());
				jobPlan.setModifyuser(userId);
				jobPlan.setJobPlanType(jpSource);//(standard：标准；plan：策划；actual：实际;maintainPlan:维护计划)
				jobPlan.setSiteid(siteId);
				jobPlan.setDeptid(deptId);
				jobPlan.setYxbz(1);
				jobPlanDao.insertJobPlan(jobPlan);
			}
			
		}else{
			jobPlan.setModifydate(new Date());
			jobPlan.setModifyuser(userId);
			jobPlanDao.updateJobPlan(jobPlan);
		}
		
		//插入安全事项
		
		JSONArray preHazardJsonArray = preHazardJsonObj.getJSONArray("rows"); //记录数组
		for(int i=0; i<preHazardDatagridNum; i++){
			String preHazardRecord = preHazardJsonArray.get(i).toString();  //某条记录的字符串表示
			
			Hazard hazard = JsonHelper.fromJsonStringToBean(preHazardRecord, Hazard.class);
			Precaution precaution = JsonHelper.fromJsonStringToBean(preHazardRecord, Precaution.class);
			
			String hazardDescription = hazard.getHazardDescription();
			String precautionDescription = precaution.getPrecautionDescription();
			if( "".equals(hazardDescription) && "".equals(precautionDescription)){ //如果未空行，则直接跳出
				continue ;
			}
			int preHazardId ;
			//  插入安全事项表
			int hazardId = hazardDao.getNextHazardId(); 
			hazard.setId(hazardId);
			hazard.setSiteid(siteId);
			hazard.setYxbz(1);
			hazard.setDeptid(deptId);
			
			hazardDao.insertHazard(hazard);
			//  插入预控措施表
			int precautionId = precautionDao.getNextPrecautionId(); 
			precaution.setId(precautionId);
			precaution.setSiteid(siteId);
			precaution.setYxbz(1);
			precaution.setDeptid(deptId);
			
			precautionDao.insertPrecaution(precaution);
			//插入  安全事项-预控措施关联表
			HashMap<String,Object> preHazardMap = new HashMap<String,Object>();
			preHazardId = hazardDao.getNextHazardId();  //借用hazardDao生成关联表的id
			preHazardMap.put("id", preHazardId);
			preHazardMap.put("jpId", jpId);
			preHazardMap.put("hazardId",hazardId );
			preHazardMap.put("precautionId", precautionId);
			preHazardMap.put("siteid", siteId);
			preHazardMap.put("deptid", deptId);
			hazardDao.insertPreHazard(preHazardMap);    //借用hazardDao插入关联表记录
		
		}		
		
		// 插入工具表  (#{id},#{jobPlanId},#{itemsCode},#{count},#{siteId}
		JSONArray itemsJsonArray = itemsJsonObj.getJSONArray("rows"); //记录数组
		//TODO 直接调用库存接口，进行领料申请
		if("commit".equals(commitStyle)){ 
			//TODO 调用接口 (已经在工单那边处理，这里可以空着不做任何处理)
//			WorkOrder workOrder = JsonHelper.fromJsonStringToBean(woFormData, WorkOrder.class);
//			HashMap<String, Object> itemsHashMap = new HashMap<String, Object>();
//			JSONObject itemsJsonObject = new JSONObject();
//			for(int i=0; i<itemsDatagridNum; i++){
//				String itemsRecord = itemsJsonArray.get(i).toString();  //某条记录的字符串表示
//				JPItems jpItems = JsonHelper.toObject(itemsRecord, JPItems.class);
//				String jpItemsCode = jpItems.getItemsCode();
//				if("".equals(jpItemsCode)){ // 如果未空行，则直接跳出
//					continue ;
//				}
//				itemsJsonObject.put(jpItems.getId(), jpItems.getCount());
//			}
//			itemsHashMap.put("Items", itemsJsonObject);
//			itemsHashMap.put("applyUser", userId);
//			itemsHashMap.put("type", "out");
//			itemsHashMap.put("woId",workOrder.getId() );
//			itemsHashMap.put("woCode", workOrder.getWorkOrderCode());
//			invMatTranService.workOrderTriggerProcesses(itemsHashMap);
		}else if("save".equals(commitStyle)){
			for(int i=0; i<itemsDatagridNum; i++){
				String itemsRecord = itemsJsonArray.get(i).toString();  //某条记录的字符串表示
				JPItems jpItems = JsonHelper.fromJsonStringToBean(itemsRecord, JPItems.class);
				String jpItemsCode = jpItems.getItemsCode();
				if("".equals(jpItemsCode)){ // 如果未空行，则直接跳出
					continue ;
				}
				int jpItemsId = jpItems.getId();
				jpItemsId = jpItemsDao.getNextJPItemsId(); //获取要插入维护计划的ID
				jpItems.setId(jpItemsId);
				jpItems.setJobPlanId(jpId);
				jpItems.setSiteid(siteId);
				jpItems.setDeptid(deptId);
				jpItemsDao.insertJPItems(jpItems);
			}
		}
		
		//添加工作内容
		JSONArray jobTaskJsonArray = jobtaskJsonObj.getJSONArray("rows"); //记录数组
		for(int i=0; i<jobtaskDatagridNum; i++){
			String jobtaskRecord = jobTaskJsonArray.get(i).toString();  //某条记录的字符串表示
			
			JPJobtask jpJobtask = JsonHelper.fromJsonStringToBean(jobtaskRecord, JPJobtask.class);
		
			String description = jpJobtask.getDescription();
			
			if("".equals(description)){ // 如果未空行，则直接跳出
				continue ;
			}
			int jpJobtaskId = jpJobtask.getId();

			jpJobtaskId = jpJobtaskDao.getNextJPJobtaskId(); //获取要插入维护计划的ID
			jpJobtask.setId(jpJobtaskId);

			jpJobtask.setJobPlanId(jpId);
			jpJobtask.setSiteid(siteId);
			jpJobtask.setDeptid(deptId);
			jpJobtask.setYxbz(1);
//			ID,JOBPLAN_ID,DESCRIPTION,PROJ,ITEM,APPLY,
//			SITEID,DEPTID,YXBZ,REMARKS
			jpJobtaskDao.insertJPJobtask(jpJobtask);
		}
				
		//添加人员信息
		for(int i=0; i<workerDatagridNum; i++){
			String workerRecord = workerJsonArray.get(i).toString();  //某条人员记录的字符串表示
			JPWorker jpWorker = JsonHelper.fromJsonStringToBean(workerRecord, JPWorker.class);
			String manager = jpWorker.getManagerInfo();
			if("".equals(manager)){ // 如果未空行，则直接跳出
				continue ;
			}
			int jpWorkerId = jpWorker.getId();
//			if(jpWorkerId == 0){
				jpWorkerId = jpWorkerDao.getNextJPWorkerId(); //获取要插入人员记录的ID
				jpWorker.setId(jpWorkerId);
//			}
			jpWorker.setJobPlanId(jpId);
			jpWorker.setSiteid(siteId);
			jpWorker.setDeptid(deptId);
			jpWorkerDao.insertJPWorker(jpWorker);
		}
		return jpId;
	}
	 
	@Override
	public Page<JobPlan> queryStandardJP(Page<JobPlan> page) throws Exception {

		List<JobPlan> ret = jobPlanDao.queryStandardJP(page);
		page.setResults(ret);
		logger.info("查询作业方案列表信息");
		
		return page;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int updateJobPlan(Map<String,String> jpDataMap) throws Exception {
		
		String jpFormData = jpDataMap.get("jpFormData");
		String mtpFormData = jpDataMap.get("mtpFormData");
		String woFormData = jpDataMap.get("woFormData");
		int jpId = 0 ;
		if(jpFormData != null){ // 修改标准作业方案
			JobPlan jobPlan = JsonHelper.toObject(jpFormData, JobPlan.class);
			jpId = jobPlan.getId(); //获取要修改的作业方案的ID
			jpDataMap.put("jpSource", "standard");//(standard：标准；plan：策划；actual：实际;maintainPlan:维护计划)
		}else if(mtpFormData != null){  //修改维护计划的作业方案
			MaintainPlan maintainPlan = JsonHelper.toObject(mtpFormData, MaintainPlan.class);
			jpId = maintainPlan.getJobPlanId();
			jpDataMap.put("jpSource", "maintainPlan");
		}else if(woFormData != null){  //修改工单的作业方案
			WorkOrder workOrder = JsonHelper.toObject(woFormData, WorkOrder.class);
			if(workOrder.getJobPlanId() != null){
				jpId = workOrder.getJobPlanId();
			}
			//TODO  此处还需要分情况确定是plan（策划）或者actual（实际）
			jpDataMap.put("jpSource", "unknown");
		}
		
		
		//删除所有之前的记录信息
		jpWorkerDao.deleteJPWorkerByJPId(jpId);
		jpItemsDao.deleteJPItemsByJPId(jpId);
		jpJobtaskDao.deleteJPJobtaskByJPId(jpId);
		List<JPPreHazard>  jpPreHazardList = jpPreHazardDao.queryJPPreHazardByJPId(jpId);
		for(int i=0; i<jpPreHazardList.size();i++){
			int preHazardId = jpPreHazardList.get(i).getId();
			int hazardId = jpPreHazardList.get(i).getHazardId();
			int precautionId = jpPreHazardList.get(i).getPrecautionId();
			jpPreHazardDao.deleteJPPreHazardById(preHazardId);
			hazardDao.deleteHazardById(hazardId);
			precautionDao.deletePrecautionById(precautionId);
		}
		//插入新的记录信息，但是ID，jpID等信息要保持不变
		jpId = insertJobPlan(jpDataMap);
		return jpId;
	}
	
	@Override
	public HashMap<String,Object> queryJPById(Integer jpId) {
		if(jpId == null){
			jpId = 0 ;
		}
		JobPlan jobPlan = jobPlanDao.queryJPById(jpId); //查作业方法form表单内容
		String jobPlanStr = JsonHelper.toJsonString(jobPlan);
		
		List<JPWorker> jpWorkerList = jpWorkerDao.queryJPWorkerByJPId(jpId);  //查人员信息
		JSONArray jpWorkerListStr = MvcJsonUtil.JSONArrayFromList(jpWorkerList);
		
		 
		List<JPItems> jpItemsList = jpItemsDao.queryJPItemsByJPId(jpId); //查工具信息
		JSONArray jpItemsListStr = MvcJsonUtil.JSONArrayFromList(jpItemsList);
		
		List<JPJobtask> jpJobtaskList = jpJobtaskDao.queryJPJobtaskByJPId(jpId); //查工作内容信息
		JSONArray jpJobtaskListStr = MvcJsonUtil.JSONArrayFromList(jpJobtaskList);
		
		List<JPPreHazard> jpPreHazardList = jpPreHazardDao.queryJPPreHazardByJPId(jpId); //查安全注意事项信息
		JSONArray preHazardJson = new JSONArray();
		for(int i=0; i<jpPreHazardList.size(); i++){
			JPPreHazard jpPreHazard = jpPreHazardList.get(i);
			int hazardId = jpPreHazard.getHazardId();
			int precautionId = jpPreHazard.getPrecautionId();
			Hazard hazard = hazardDao.queryHazardById(hazardId);
			Precaution precaution = precautionDao.queryPrecautionById(precautionId);
			
			JSONObject preHazardStr = new JSONObject();
			preHazardStr.put("id", jpPreHazard.getId());
			preHazardStr.put("hazardDescription", hazard.getHazardDescription());
			preHazardStr.put("precautionDescription", precaution.getPrecautionDescription());
			
			preHazardJson.add(preHazardStr.toString());
		}
		
		HashMap<String,Object> jpFullData = new HashMap<String, Object>();  //返回数据类型
		jpFullData.put("jobPlanForm", jobPlanStr);
//		System.out.println(jobPlanStr);
		jpFullData.put("preHazardData", preHazardJson.toString());
		jpFullData.put("toolData", jpItemsListStr.toString());
		jpFullData.put("taskData", jpJobtaskListStr.toString());
		jpFullData.put("workerData", jpWorkerListStr.toString());
		
		return jpFullData;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteWOReportByWOId(int woId) {
		JobPlan jobPlan = jobPlanDao.queryReportJPByWOId(woId);
		if(jobPlan != null){
			int jpId = jobPlan.getId();
			deleteJobPlanById(jpId);
		}
		
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteJobPlanById(int jpId) {
		List<JPPreHazard> jpPreHazardList = jpPreHazardDao.queryJPPreHazardByJPId(jpId);
		int jpPreHazardCount = jpPreHazardList.size();
		for(int i=0; i<jpPreHazardCount; i++){
			JPPreHazard jpPreHazard = jpPreHazardList.get(i);
			int preHazardId = jpPreHazard.getId();
			int precautionId = jpPreHazard.getPrecautionId();
			int hazardId = jpPreHazard.getHazardId();
			jpPreHazardDao.deleteJPPreHazardById(preHazardId);   //删除关联信息
			hazardDao.deleteHazardById(hazardId);  //删除安全事项
			precautionDao.deletePrecautionById(precautionId);  //删除预控措施
			
		}
		jpItemsDao.deleteJPItemsByJPId(jpId);  //删除工具
		jpJobtaskDao.deleteJPJobtaskByJPId(jpId);  //删除工作内容
		jpWorkerDao.deleteJPWorkerByJPId(jpId);   //删除人员信息
		jobPlanDao.deleteJPById(jpId);  //删除作业方案主表中信息
	}

	@Override
	public JobPlan queryPlanJPByWOId(int woId) {
		
		return jobPlanDao.queryPlanJPByWOId(woId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateJPToUnvailable(int jobPlanId) {
		jobPlanDao.updateJPToUnvailable(jobPlanId);
	}

	 

}
