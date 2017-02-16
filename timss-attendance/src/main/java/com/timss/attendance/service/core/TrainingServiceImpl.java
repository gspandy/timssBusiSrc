package com.timss.attendance.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.attendance.bean.TrainingBean;
import com.timss.attendance.bean.TrainingItemBean;
import com.timss.attendance.dao.TrainingDao;
import com.timss.attendance.service.AtdAttachService;
import com.timss.attendance.service.TrainingService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.timss.attendance.util.VOUtil;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.workflow.service.WorkflowService;

@Service
public class TrainingServiceImpl implements TrainingService {
    @Autowired
    private TrainingDao trainingDao;
    static Logger logger = Logger.getLogger( TrainingServiceImpl.class );
	@Autowired
    private WorkflowService workflowService;
	@Autowired
    private HomepageService homepageService;
	@Autowired
    private AtdAttachService atdAttachService;
	@Autowired
    private AtdUserPrivUtil privUtil;
	
	@Override
	public Page<TrainingBean> queryList(Page<TrainingBean> page)
			throws Exception {
		//查询列表
		List<TrainingBean> list=trainingDao.queryList(page);
		page.setResults(list);
		return page;
	}
	
	@Override
	public TrainingBean queryDetail(String trainingId) throws Exception {
		//查询详情
		TrainingBean bean=trainingDao.queryDetail(trainingId);
		if(bean!=null){
			bean.setItemList(trainingDao.queryItemList(trainingId));
		}
		return bean;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public Integer insert(@CUDTarget TrainingBean bean,@Operator SecureUser operator) throws Exception {
        bean.setSiteid(operator.getCurrentSite());
        String deptId=(operator.getOrganizations()!=null&&operator.getOrganizations().size()>0)?operator.getOrganizations().get(0).getCode():null;
        bean.setDeptid(deptId);
        bean.setCreatedate(new Date());
        bean.setCreateuser(operator.getId());
        if(StringUtils.isBlank(bean.getStatus())){
        	bean.setStatus(ProcessStatusUtil.CAOGAO);
        }
        Integer result=trainingDao.insert(bean);
        logger.info("insert training->id:"+bean.getTrainingId()+" result:"+result);
        
        if(bean.getAddItemList()!=null&&bean.getAddItemList().size()>0){
        	List<TrainingItemBean>addList=new ArrayList<TrainingItemBean>();
        	for (TrainingItemBean itemBean : bean.getAddItemList()) {
				itemBean.setTrainingItemId(UUIDGenerator.getUUID());
				addList.add(itemBean);
			}
        	Integer itemResult=trainingDao.batchInsertItem(bean.getTrainingId(), addList);
        	logger.info("insert training item->id:"+bean.getTrainingId()+" result:"+itemResult+"/"+bean.getAddItemList().size());
        }
        if(bean.getFileIds()!=null&&bean.getFileIds().length>0){
        	atdAttachService.insert("training", bean.getTrainingId(), bean.getFileIds());
        }
        
        return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public Integer update(@CUDTarget TrainingBean bean,@Operator SecureUser operator) throws Exception {
		bean.setModifydate(new Date());
        bean.setModifyuser(operator.getId());
		Integer result=trainingDao.update(bean);
		logger.info("update training->id:"+bean.getTrainingId()+" result:"+result);
		
		//更新详情项
		Integer addItemCount = 0;
		if(bean.getAddItemList()!=null&&bean.getAddItemList().size()>0){
        	List<TrainingItemBean>addList=new ArrayList<TrainingItemBean>();
        	for (TrainingItemBean itemBean : bean.getAddItemList()) {
				itemBean.setTrainingItemId(UUIDGenerator.getUUID());
				addList.add(itemBean);
			}
        	addItemCount=trainingDao.batchInsertItem(bean.getTrainingId(), addList);
        }		
		Integer updateItemCount = (bean.getUpdateItemList()!=null&&bean.getUpdateItemList().size()>0)?
				trainingDao.batchUpdateItem( bean.getUpdateItemList() ):0;    		
		Integer delItemCount = (bean.getDelItemList()!=null&&bean.getDelItemList().size()>0)?
				trainingDao.batchDeleteItem( bean.getDelItemList() ):0;
		logger.info("update training item->id:"+bean.getTrainingId()+" add:"+bean.getAddItemList().size()+"/"+addItemCount+
				" update:"+bean.getUpdateItemList().size()+"/"+updateItemCount+
				" del:"+bean.getDelItemList().size()+"/"+delItemCount);		
		
		atdAttachService.delete("training", bean.getTrainingId(), null);
		if(bean.getFileIds()!=null&&bean.getFileIds().length>0){
        	atdAttachService.insert("training", bean.getTrainingId(), bean.getFileIds());
        }
		
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public Integer delete(@CUDTarget TrainingBean bean,@Operator SecureUser operator) throws Exception {
		bean.setModifydate(new Date());
        bean.setModifyuser(operator.getId());
        //删除首页草稿
        homepageService.Delete( bean.getTrainingNo(), privUtil.getUserInfoScope() ); 
		return trainingDao.delete(bean);
	}

	@Override
    @Transactional(propagation=Propagation.REQUIRED)
    public TrainingBean invalid(@CUDTarget TrainingBean bean,@Operator SecureUser operator) throws Exception {
		//取id刷新掉bean
		bean=queryDetail(bean.getTrainingId());
		Boolean result=updateAuditStatus(bean.getTrainingId(), null, ProcessStatusUtil.INVALID, operator)>0;
		queryWorkFlow(bean, operator);
        workflowService.stopProcess(bean.getTaskId(),operator.getId(),operator.getId(),ProcessStatusUtil.INVALID+"。");
        //办毕
        homepageService.complete(bean.getInstanceId(),privUtil.getUserInfoScope(),ProcessStatusUtil.INVALID);
        logger.info( "流程instanceId = " + bean.getInstanceId() + "---流水号为： " + bean.getTrainingNo() );
        return result?bean:null;
    }
	
	@Override
    @Transactional(propagation=Propagation.REQUIRED)
    public TrainingBean commit(@CUDTarget TrainingBean bean,@Operator SecureUser operator) throws Exception{
    	Boolean isExist=StringUtils.isNotBlank(bean.getTrainingId());//之前已暂存过了的标识
    	Boolean result=insertOrUpdate(bean, operator)>0;
    	if(result){//更新成功
    		bean=queryDetail(bean.getTrainingId());
    		
    		UserInfoScope userInfo = privUtil.getUserInfoScope();
    		//启动流程
            String processKey = "atd_" + userInfo.getSiteId().toLowerCase() + "_training";
            //获取最新流程定义版本
            String defkey = workflowService.queryForLatestProcessDefKey( processKey );
            Map<String, Object> map = new HashMap<String, Object>();
            map.put( "businessId", bean.getTrainingId() );
            ProcessInstance processInstance = null;
            try {
                processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey, userInfo.getUserId(), map );
            } catch (Exception e) {
                logger.error( e.getMessage(), e );
            }
            //获取流程实例ID
            String processInstId = processInstance.getProcessInstanceId();
            bean.setInstanceId(processInstId);
            
            //获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks(processInstId);
            //刚启动流程，第一个活动节点肯定是属于当前登录人的
            Task task = activities.get(0);
            bean.setTaskId(task.getId());
            
            //加入或更新待办
            String flowCode = bean.getTrainingNo();
            if(isExist){//之前已存在要删除先
            	homepageService.Delete( flowCode, userInfo );
            }
            // 加入待办列表
            String reason = bean.getReason();
            if( StringUtils.isNotBlank( reason ) && reason.length() > 50 ){
            	reason = reason.substring( 0, 47 ) + "...";
            }
            String jumpPath="attendance/training/detailPage.do?mode=view&trainingId="+bean.getTrainingId();
            homepageService.createProcess( flowCode, processInstId, "培训申请", reason,
            		task.getName(), jumpPath, userInfo, null );
    	}
        return result?bean:null;
    }
	
	@Override
	public Integer insertOrUpdate(TrainingBean bean, SecureUser operator)throws Exception {
		if(StringUtils.isBlank(bean.getTrainingId())){
			return insert(bean, operator);
		}else{
			return update(bean, operator);
		}
	}

	@Override
	public TrainingBean save(TrainingBean bean, SecureUser operator)throws Exception {
		Boolean isExist=StringUtils.isNotBlank(bean.getTrainingId());//之前已暂存过了的标识
    	Boolean result=insertOrUpdate(bean,operator)>0;
    	if(result){//更新成功
    		bean=queryDetail(bean.getTrainingId());
    		if(StringUtils.isBlank(bean.getInstanceId())){//没有启动流程的时候，需要更新草稿内容，否则是退回情况，不用更新草稿
    			UserInfoScope userInfo = privUtil.getUserInfoScope();
    			String flowCode = bean.getTrainingNo();
    			if(isExist){
    				homepageService.Delete( flowCode, userInfo );//先删除草稿
    			}
    			
    			String jumpPath = "attendance/training/detailPage.do?mode=edit&trainingId=" + bean.getTrainingId();
                String reason = bean.getReason();
                if( StringUtils.isNotBlank( reason ) && reason.length() > 50 ){
                	reason = reason.substring( 0, 47 ) + "...";
                }
                // 构建Bean
                HomepageWorkTask homeworkTask = new HomepageWorkTask();
                homeworkTask.setFlow(flowCode);// 编号，如采购申请 WO20140902001
                homeworkTask.setTypeName("培训申请");// 名称
                homeworkTask.setProcessInstId(flowCode); // 草稿时流程实例ID可以不用设置
                homeworkTask.setStatusName( ProcessStatusUtil.CAOGAO ); // 状态
                homeworkTask.setType(HomepageWorkTask.TaskType.Draft); // 枚举类型定义是草稿还是流程,Draft 草稿;Process 流程实例
                homeworkTask.setName( reason ); // 类别
                homeworkTask.setUrl(jumpPath);// 扭转的URL
                homepageService.create( homeworkTask, userInfo ); // 调用接口创建草稿
    		}
    	}
    	return result?bean:null;
	}
	
	@Override
	public TrainingBean convertBean(String formData, String fileIds,
			String addRows, String delRows, String updateRows) throws Exception {
		TrainingBean bean=VOUtil.fromJsonToVoUtil( formData, TrainingBean.class );
		if(bean!=null){
			bean.setAddItemList(VOUtil.fromJsonToListObject(addRows, TrainingItemBean.class));
	    	bean.setDelItemList(VOUtil.fromJsonToListObject(delRows, TrainingItemBean.class));
	    	bean.setUpdateItemList(VOUtil.fromJsonToListObject(updateRows, TrainingItemBean.class));
	    	if ( !StringUtils.isBlank( fileIds ) ) {
	    		String[] fileArr = fileIds.split( "," );
	            bean.setFileIds(fileArr);
	    	}
		}
    	
    	return bean;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Integer updateAuditStatus(String trainingId, String instanceId,
			String status, SecureUser operator) throws Exception {
		return trainingDao.updateAuditStatus(trainingId, instanceId, status, operator.getId());
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Integer updateCreateDate(String trainingId, Date createDate,
			SecureUser operator) throws Exception {
		if(createDate==null){
			createDate=new Date();
		}
		return trainingDao.updateCreateDate(trainingId, createDate, operator.getId());
	}

	@Override
	public TrainingBean queryWorkFlow(TrainingBean bean, SecureUser operator) throws Exception {
		if(bean!=null&&StringUtils.isNotBlank(bean.getInstanceId())){
        	List<Task> activities = workflowService.getActiveTasks(bean.getInstanceId());
            if(!activities.isEmpty()){
                Task task = activities.get(0);
                bean.setTaskId(task.getId());
                //拿到审批人的列表
                List<String> userList = workflowService.getCandidateUsers( task.getId() );
                //判断是否具有审批状态
                bean.setIsAudit(userList.contains(operator.getId()));
            }
        }
		return bean;
	}
	
	public String getUserJob(String userId) throws Exception{
		TrainingItemBean bean = trainingDao.queryUserItemByUserId(userId);
		String job = "无";
		if(bean != null && bean.getJob() != null){
			job = bean.getJob();
		}
		return job;
	}
}