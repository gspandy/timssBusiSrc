package com.timss.attendance.service.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.attendance.bean.RationalizationBean;
import com.timss.attendance.dao.RationalizationDao;
import com.timss.attendance.service.AtdAttachService;
import com.timss.attendance.service.RationalzationService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.timss.attendance.util.VOUtil;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

@Service
public class RationalzationServiceImpl implements RationalzationService {
	@Autowired
    private RationalizationDao rationalizationDao;
    @Autowired
    private HomepageService homepageService;
    @Autowired
    private AtdUserPrivUtil privUtil;
    @Autowired
    private AtdAttachService atdAttachService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
	ItcMvcService itcMvcService;
	
    private Logger logger = Logger.getLogger(RationalzationServiceImpl.class);
	
	@Override
	public Page<RationalizationBean> queryList(Page<RationalizationBean> page)
			throws Exception {
		//查询列表
		List<RationalizationBean> list=rationalizationDao.queryList(page);
		list=doList(list);
		page.setResults(list);
		return page;
	}

	private List<RationalizationBean> doList(List<RationalizationBean> list) throws Exception{
		List<RationalizationBean> beanList = new ArrayList<RationalizationBean>();
		for (RationalizationBean bean:list) {
			if (!bean.getRationalType().contains("DPP_RATION_")) {
        		beanList.add(bean);
        		continue;
			}else{
				StringBuffer categoryListName = new StringBuffer();
                List<AppEnum> emList = itcMvcService.getEnum( "ATD_RATION_TYPE" );
                String categoryName = getCategoryName(emList, bean.getRationalType()); 
                categoryListName.append("," + categoryName );
                bean.setRationalType(categoryListName.toString().substring( 1 ) );
                beanList.add(bean);
			}
		}
		return beanList;
	}

	@Override
	public RationalizationBean queryDetail(String rationalId) throws Exception {
		RationalizationBean bean= rationalizationDao.queryDetail(rationalId);
		return bean;
	}

	@Override
	public RationalizationBean convertBean(String formData) throws Exception {
		RationalizationBean bean=VOUtil.fromJsonToVoUtil( formData, RationalizationBean.class );
		return bean;
	}

	@Override
	public RationalizationBean save(RationalizationBean bean,
		SecureUser operator) throws Exception {
		Boolean isExist=StringUtils.isNotBlank(bean.getRationalId());//之前已暂存过了的标识
    	Boolean result=insertOrUpdate(bean,operator)>0;
    	if(result){//更新成功
    		bean=queryDetail(bean.getRationalId());
    		if(StringUtils.isBlank(bean.getInstanceId())){//没有启动流程的时候，需要更新草稿内容，否则是退回情况，不用更新草稿
    			UserInfoScope userInfo = privUtil.getUserInfoScope();
    			String flowCode = bean.getRationalNo();
    			if(isExist){
    				homepageService.Delete( flowCode, userInfo );//先删除草稿
    			}
    			String categoryName = null;
    			if (bean.getRationalType().contains("DPP_RATION_")) {
	        		StringBuffer categoryListName = new StringBuffer();
	                List<AppEnum> emList = itcMvcService.getEnum( "ATD_RATION_TYPE" );
	                categoryName = getCategoryName(emList, bean.getRationalType()); 
	            }
    			String jumpPath = "attendance/Rationalization/detailPage.do?mode=edit&rationalId=" + bean.getRationalId();
    			// 构建Bean
                HomepageWorkTask homeworkTask = new HomepageWorkTask();
                homeworkTask.setFlow(flowCode);// 编号，如采购申请 WO20140902001
                homeworkTask.setTypeName("合理化建议");// 名称
                homeworkTask.setProcessInstId(flowCode); // 草稿时流程实例ID可以不用设置
                homeworkTask.setStatusName( ProcessStatusUtil.CAOGAO ); // 状态
                homeworkTask.setType(HomepageWorkTask.TaskType.Draft); // 枚举类型定义是草稿还是流程,Draft 草稿;Process 流程实例
                homeworkTask.setName(categoryName+"合理化建议"); // 类别
                homeworkTask.setUrl(jumpPath);// 扭转的URL
                homepageService.create( homeworkTask, userInfo ); // 调用接口创建草稿
    		}
    	}
    	return result?bean:null;
	}
	
		@Override
	public Integer insertOrUpdate(RationalizationBean bean, SecureUser operator)
			throws Exception {
		logger.info("id="+bean.getRationalId()+ "\t type="+bean.getRationalType());
		if(StringUtils.isBlank(bean.getRationalId())){
			return insert(bean, operator);
		}else{
			 return update(bean, operator);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public Integer insert(RationalizationBean bean, SecureUser operator)
			throws Exception {
		logger.info("msg111="+bean.getRationalType());
		bean.setSiteid(operator.getCurrentSite());
		String deptpartId=(operator.getOrganizations()!=null&&operator.getOrganizations().size()>0)?operator.getOrganizations().get(0).getCode():null;
		//因为传回来是空字符串，所以要把No改为null
		if(bean.getRationalNo().equals("")){
			bean.setRationalNo(null);
		}
	    bean.setDeptid(deptpartId);
	    bean.setCreatedate(new Date());
        bean.setUserId(operator.getId());
        bean.setUserName(operator.getName());
        bean.setDeptName(operator.getOrganizations().get(0).getName());
        //bean.setCreateuser(operator.getId());
        if(StringUtils.isBlank(bean.getStatus())){
        	bean.setStatus(ProcessStatusUtil.CAOGAO);
        }
        //加入草稿
        logger.info("string="+JsonHelper.toJsonString(bean));
        Integer result=rationalizationDao.insert(bean);
        if(bean.getFileIds()!=null&&bean.getFileIds().length>0){
        	atdAttachService.insert("ration", bean.getRationalId(), bean.getFileIds());
        }
        return result;
	}

	@Override
	public RationalizationBean commit(RationalizationBean bean,
			SecureUser operator) throws Exception {
		Boolean isExist=StringUtils.isNotBlank(bean.getRationalId());//之前已暂存过了的标识
    	Boolean result=insertOrUpdate(bean, operator)>0;
    	if(result){//更新成功
    		bean=queryDetail(bean.getRationalId());
    		UserInfoScope userInfo = privUtil.getUserInfoScope();
    		//启动流程
            String processKey = "atd_" + userInfo.getSiteId().toLowerCase() + "_rationalization";
            //获取最新流程定义版本
            String defkey = workflowService.queryForLatestProcessDefKey( processKey );
            Map<String, Object> map = new HashMap<String, Object>();
            map.put( "businessId", bean.getRationalId() );
            ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey, userInfo.getUserId(), map );
            //获取流程实例ID
            String processInstId = processInstance.getProcessInstanceId();
            logger.info("processId="+processInstId);
            bean.setInstanceId(processInstId);
            
            //获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks(processInstId);
            //刚启动流程，第一个活动节点肯定是属于当前登录人的
            Task task = activities.get(0);
            bean.setTaskId(task.getId());
            //加入或更新待办
            String flowCode = bean.getRationalNo();
            logger.info(task.getId());
            if(isExist){//之前已存在要删除先
            	homepageService.Delete( flowCode, userInfo );
            }
            String categoryName = null;
            if (bean.getRationalType().contains("DPP_RATION_")) {
        		StringBuffer categoryListName = new StringBuffer();
                List<AppEnum> emList = itcMvcService.getEnum( "ATD_RATION_TYPE" );
                categoryName = getCategoryName(emList, bean.getRationalType()); 
            }
            workflowService.setVariable(processInstId, "userId", userInfo.getUserId());
            workflowService.setVariable(processInstId, "org_code", userInfo.getOrgs().get(0).getCode());/*这里好像有问题*/
            workflowService.setVariable(processInstId, "rationType", bean.getRationalType());
            workflowService.setVariable(processInstId, "orgId", userInfo.getOrgs().get(0).getCode());
            String jumpPath="attendance/Rationalization/detailPage.do?mode=view&rationalId="+bean.getRationalId();
            homepageService.createProcess( flowCode, processInstId, "合理化建议", categoryName+"合理化建议",
            		task.getName(), jumpPath, userInfo, null );
    	}
        return result?bean:null;
	}

	@Override
	public RationalizationBean queryWorkFlow(RationalizationBean bean,
		SecureUser operator) throws Exception {
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

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Integer updateCreateDate(String rationalId, Date createDate,
			SecureUser operator) throws Exception {
		if(createDate==null){
			createDate=new Date();
		}
		return rationalizationDao.updateCreateDate(rationalId, createDate, operator.getId());
	}



	@Override
	public Boolean updateRationAuditStatusByTask(TaskInfo taskInfo, String status,
			SecureUser operator,String userId) throws Exception {
		String instanceId = taskInfo.getProcessInstanceId();
		String id=workflowService.getVariable( instanceId, "businessId" ).toString();
		return rationalizationDao.updateAuditStatus(id,instanceId,status,operator.getId(),userId)>0;
	}

	@Override
	public Integer delete(RationalizationBean bean, SecureUser operator)
			throws Exception {
		bean.setModifydate(new Date());
        bean.setModifyuser(operator.getId());
        //删除首页草稿
        homepageService.Delete( bean.getRationalNo(),privUtil.getUserInfoScope() ); 
		return rationalizationDao.delete(bean);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public Integer update(RationalizationBean bean, SecureUser operator)
			throws Exception {
			if (bean.getBonusSplit()!=null&&bean.getBonusSplit().length()>0||bean.getImpDept()!=null&&bean.getImpDept().length()>0) {
				BigDecimal bonusSplit = new BigDecimal(Float.parseFloat(bean.getBonusSplit()));
				BigDecimal impDept = new BigDecimal(Float.parseFloat(bean.getImpDept()));
				bean.setBonusSplit(bonusSplit.setScale(2, RoundingMode.HALF_UP).toString());
				bean.setImpDept(impDept.setScale(2, RoundingMode.HALF_UP).toString());
			}
		 bean.setModifydate(new Date());
  		 bean.setModifyuser(operator.getId());
  		 logger.info("msg="+bean.getRationalType());
  		 Integer result=rationalizationDao.update(bean);
  		 logger.info("update rationalization->id:"+bean.getRationalId()+" result:"+result);
	 
  		 //更新详情项
		atdAttachService.delete("ration", bean.getRationalId(), null);
		if(bean.getFileIds()!=null&&bean.getFileIds().length>0){
			atdAttachService.insert("ration", bean.getRationalId(), bean.getFileIds());
		}
		return result;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Integer updateAuditStatus(String rationalId, String instanceId,
			String status, SecureUser operator,String userId) throws Exception {
		return rationalizationDao.updateAuditStatus(rationalId, instanceId, status, operator.getId(),userId);
	}

	@Override
	public Integer updateById(RationalizationBean bean) {
		return rationalizationDao.updateById(bean);
	}

	@Override
    @Transactional(propagation=Propagation.REQUIRED)
	public RationalizationBean invalid(RationalizationBean bean,
		SecureUser operator) throws Exception {
		//取id刷新掉bean
		bean=queryDetail(bean.getRationalId());
		rationalizationDao.setRecomNameNull(bean.getRationalId());
		Boolean result=rationalizationDao.updateAuditStatus(bean.getRationalId(), "", ProcessStatusUtil.INVALID, operator.getId(),"")>0;
		queryWorkFlow(bean, operator);
	    workflowService.stopProcess(bean.getTaskId(),operator.getId(),operator.getId(),ProcessStatusUtil.INVALID+"。");
	    //办毕
	    homepageService.complete(bean.getInstanceId(),privUtil.getUserInfoScope(),ProcessStatusUtil.INVALID);
	    logger.info( "流程instanceId = " + bean.getInstanceId() + "---流水号为： " + bean.getRationalNo());
	    return result?bean:null;
	}

	@Override
	public String getCategoryName(List<AppEnum> emList, String enumId)
			throws Exception {
		 String categoryName = enumId;
         if( StringUtils.isNotBlank( enumId ) ){
             if( emList != null && emList.size() > 0 ){
                 for( AppEnum appVo : emList ){
                	 if( categoryName.equalsIgnoreCase( appVo.getCode() ) ){
                		 logger.info("appVo.getCode()="+appVo.getCode());
                         categoryName =  appVo.getLabel();
                         break;
                     }
                	 if( categoryName.equalsIgnoreCase( appVo.getLabel() ) ){
                		 logger.info("appVo.getLabel()="+appVo.getLabel());
                         categoryName =  appVo.getCode();
                         break;
                     }
                 }
             }
         }
         return categoryName;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor ={Exception.class})
	public void updateCurrHandlerUser(String rationId,
			UserInfoScope userInfoScope, String flag) throws Exception{
		String userIds = "";
		if ( "normal".equals( flag ) ) {
			userIds = userInfoScope.getParam( "userIds" );
		Map<String, List<String>> userIdsMap = (Map<String, List<String>>) new ObjectMapper().readValue(userIds, Map.class);
		Iterator<String> iterator = userIdsMap.keySet().iterator();
		while (iterator.hasNext()) {
             List<String> auditUserId = userIdsMap.get( iterator.next() );
             String nextAuditUserNames = "";
             for ( int i = 0; i < auditUserId.size(); i++ ) {
                 String tempUserIds = auditUserId.get( i );
                 if ( tempUserIds.indexOf( "," ) > 0 ) {
                	 String[] auditUserNames = tempUserIds.split( "," );
                	 for ( int j = 0; j < auditUserNames.length; j++ ) {
                		 nextAuditUserNames += itcMvcService.getUserInfoById( auditUserNames[j] ).getUserName() + ",";
                    }
                 } else {
                         nextAuditUserNames = itcMvcService.getUserInfoById( auditUserId.get( i ) ).getUserName() + ",";
                     }
                 }
                 nextAuditUserNames = nextAuditUserNames.substring( 0, nextAuditUserNames.length() - 1 );
                 Map<String, String> parmas = new HashMap<String, String>();
                 parmas.put( "rationId", rationId );
                 parmas.put( "HandlerName", nextAuditUserNames );
                 rationalizationDao.updateCurrHandUserById( parmas );
				}
				}else if ( "rollback".equals( flag ) ) { // 回退的时候是单个人
	                String nextAuditUserId = "";
	                String nextAuditUserName = "";
	                nextAuditUserId = userInfoScope.getParam( "userId" );
	                nextAuditUserName = itcMvcService.getUserInfoById( nextAuditUserId ).getUserName();
	                Map<String, String> parmas = new HashMap<String, String>();
	                parmas.put( "rationId", rationId );
	                parmas.put( "HandlerName", nextAuditUserName );
	                rationalizationDao.updateCurrHandUserById( parmas );
				}
		
	}
}

	