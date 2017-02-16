package com.timss.pms.service.core;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.BidResult;
import com.timss.pms.bean.FlowVoidParamBean;
import com.timss.pms.bean.Milestone;
import com.timss.pms.bean.Outsourcing;
import com.timss.pms.bean.Project;
import com.timss.pms.bean.WorkflowBusiness;
import com.timss.pms.bean.Workload;
import com.timss.pms.dao.BidResultDao;
import com.timss.pms.dao.CheckoutDao;
import com.timss.pms.dao.ContractDao;
import com.timss.pms.dao.PayDao;
import com.timss.pms.dao.ProjectDao;
import com.timss.pms.dao.WorkflowBusinessDao;
import com.timss.pms.exception.PmsBasicException;
import com.timss.pms.service.BidResultService;
import com.timss.pms.service.BidService;
import com.timss.pms.service.ContractService;
import com.timss.pms.service.MilestoneHistoryService;
import com.timss.pms.service.MilestoneService;
import com.timss.pms.service.OutsourcingService;
import com.timss.pms.service.ProjectService;
import com.timss.pms.service.SequenceService;
import com.timss.pms.service.WFService;
import com.timss.pms.service.WorkloadService;
import com.timss.pms.util.AttachUtil;
import com.timss.pms.util.ChangeStatusUtil;
import com.timss.pms.util.FlowVoidUtil;
import com.timss.pms.util.InitUserAndSiteIdUtil;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.BidDtlVo;
import com.timss.pms.vo.BidResultVo;
import com.timss.pms.vo.BidVo;
import com.timss.pms.vo.CheckoutVo;
import com.timss.pms.vo.ContractVo;
import com.timss.pms.vo.MilestoneHistoryVo;
import com.timss.pms.vo.MilestoneVo;
import com.timss.pms.vo.OutsourcingVo;
import com.timss.pms.vo.PayVo;
import com.timss.pms.vo.ProjectBudgetVo;
import com.timss.pms.vo.ProjectDtlVo;
import com.timss.pms.vo.ProjectVo;
import com.timss.pms.vo.ProjectWFVo;
import com.timss.pms.vo.WorkloadVo;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.ReflectionUtils;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * 
 * @ClassName: ProjectServiceImpl
 * @company: gdyd
 * @Description:项目立项service接口类实现,core版
 * @author: 黄晓岚
 * @date: 2014-6-20 上午11:27:08
 */
@Service
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private WorkflowBusinessDao wfbBusinessDao;
	@Autowired
	private AttachmentMapper attachmentMapper;
	@Autowired
	private BidService bidService;
	@Autowired
	BidResultService bidResultService;
	@Autowired
	@Qualifier("contractServiceImpl")
	private ContractService contractService;
	@Autowired
	private HomepageService homepageService;
	@Autowired
	private WFService wfService;
	@Autowired
	private CheckoutDao checkoutDao;
	@Autowired
	private PayDao payDao;
	@Autowired
	private BidResultDao bidResultDao;
	@Autowired
	private ContractDao contractDao;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private WorkloadService workloadService;
	@Autowired
	private OutsourcingService outsourcingService;
	@Autowired
	MilestoneService milestoneService;
	@Autowired
	MilestoneHistoryService milestoneHistoryService;

	private static final Logger LOGGER = Logger.getLogger(ProjectServiceImpl.class);

	@Override
	public Page<ProjectVo> queryProjectList(Page<ProjectVo> page,
			UserInfoScope userInfo) {
		LOGGER.info("开始查询项目立项数据");
		//根据站点id查询
		page.setFuzzyParameter("siteid", itcMvcService.getUserInfoScopeDatas().getSiteId());
		List<ProjectVo> projects = projectDao.queryProjectList(page);
		InitVoEnumUtil.initProjectVoList(projects, itcMvcService);
		attachProjoctListWithMilestone(projects);
		attachProjoctListWithPaySum(projects);
		page.setResults(projects);
		LOGGER.info("查询项目立项数据成功");
		
		return page;

	}


	private void attachProjoctListWithPaySum(List<ProjectVo> projects) {
		if(projects!=null){
			for(int i=0;i<projects.size();i++){
				ProjectVo projectVo=projects.get(i);
				Double paySum=projectDao.queryProjectPaySumById(projectVo.getId());
				if(paySum==null){
					paySum=0.0;
				}
				projectVo.setPaySum(paySum);
			}
		}
		
	}


	private void attachProjoctListWithMilestone(List<ProjectVo> projects) {
		if(projects!=null){
			for(int i=0;i<projects.size();i++){
				ProjectVo projectVo=projects.get(i);
				projectVo.setMilestoneVos(milestoneService.queryMilestoneListByProjectId(String.valueOf(projectVo.getId())));
			}
		}
		
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	@Deprecated //被insertWorkflow替代
	public void insertProject(Project project) {
		LOGGER.info("开始插入项目立项数据");
		projectDao.insertProject(project);
		bindAttahch(null,project);
		LOGGER.info("插入项目立项数据成功");

	}
	
	/**
	 * 
	 * @Title: bindAttahch
	 * @Description: 绑定附件文件，使旧文件失效，使新文件生效
	 * @param oldId 旧的project的id
	 * @param newProject 新project
	 */
	private void bindAttahch(String oldId,Project newProject){
		if (oldId!=null && "".equals(oldId)) {
			//改变删除旧文件
			ProjectDtlVo oldProject=projectDao.queryProjectById(Integer.parseInt(oldId));
			changeAttachStatus(oldProject, 0);
		}
		//添加新文件
		changeAttachStatus(newProject, 1);
	}
	/**
	 * 
	 * @Title: changeAttachStatus
	 * @Description: 改变附件的状态
	 * @param project
	 * @param status 0表示删除，1表示不删除
	 */
	private void changeAttachStatus(Project project,int status){
		String attach=project.getAttach();
		if(attach!=null && !"".equals(attach)){
			String []attachList=attach.split(",");
			attachmentMapper.setAttachmentsBinded(attachList, status);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void tmpInsertProject(Project project,List<Milestone> milestones){
		LOGGER.info("开始暂存项目立项数据");
		ChangeStatusUtil.changeSToValue(project, ChangeStatusUtil.draftCode);
		//附加创建人员和站点信息
		InitUserAndSiteIdUtil.initCreate(project, itcMvcService);
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String prefix = userInfo.getUserId();
                String dateStr = new SimpleDateFormat("yyMMdd").format( new Date() );
                String newFlowNo = generateNewFlowNo( prefix+"-"+dateStr+"-" );
                newFlowNo = prefix+"-"+dateStr+"-"+newFlowNo;
                project.setFlowNo( newFlowNo );
		projectDao.insertProject(project);
		//改变附件状态，使其生效
		bindAttahch(null, project);
		//插入里程碑信息
		milestoneService.insertMilestoneList(milestones, project);
		//启动流程，并存到草稿
	    startWorkflow(project);
	    
	    sequenceService.updateProjectSequece(project.getProjectCode());
		LOGGER.info("完成暂存项目立项数据");

	}

	@Override
	@Transactional
	public void updateProject(Project project,List<Milestone> milestones) {
		LOGGER.info("开始修改项目立项数据");
		ChangeStatusUtil.changeSToValue(project, ChangeStatusUtil.approvalCode);
		InitUserAndSiteIdUtil.initUpdate(project, itcMvcService);
		projectDao.updateProject(project);
		//改变附件状态，使其生效
		bindAttahch(String.valueOf(project.getId()), project);
		//更新里程碑信息
		milestoneService.updateMilestoneList(milestones, project);
		LOGGER.info("修改项目立项数据成功");

	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void tmpUpdateProject(Project project,List<Milestone> milestones) {
		LOGGER.info("开始暂存项目立项数据");

		//更新项目信息
		ChangeStatusUtil.changeSToValue(project, ChangeStatusUtil.draftCode);
		InitUserAndSiteIdUtil.initUpdate(project, itcMvcService);
		projectDao.updateProject(project);
		//改变附件状态，是旧的失效，使新的生效
		bindAttahch(String.valueOf(project.getId()), project);
		//更新里程碑信息
		milestoneService.updateMilestoneList(milestones, project);
		Map map=wfService.createProjectMap(String.valueOf(project.getId()), null, null);
		setWFVariable((String)map.get("taskId"), (String)map.get("processInstId"),project);
		LOGGER.info("完成暂存项目立项数据");

	}

	@Override
	public ProjectDtlVo queryProjectById(String id) {
		LOGGER.info("查询id为" + id + "的项目立项");
		ProjectDtlVo project = projectDao.queryProjectById(Integer.parseInt(id));
		ArrayList<HashMap<String,Object>> attachMap=AttachUtil.generatAttach(project.getAttach());
		project.setAttachMap(attachMap);
		
		List<BidResultVo> bidResults=bidResultService.queryBidResultListByProjectId(Integer.valueOf(id));
		project.setBidResultVos(bidResults);
		//查询项目的合同信息
		List<ContractVo> contracts=contractService.queryContractListByProjectId(id);
		project.setContracts(contracts);
		//查询项目的工作量信息
		List<WorkloadVo> workloadVos=workloadService.queryOutsoucingListByProjectId(id);
		project.setWorkloadVos(workloadVos);
		//查询项目的外购需求
		List<OutsourcingVo> outsourcingVos=outsourcingService.queryOutsoucingListByProjectId(id);
		project.setOutsourcingVos(outsourcingVos);
		//查询里程碑信息
		List<MilestoneVo> milestoneVos=milestoneService.queryMilestoneListByProjectId(id);
		project.setMilestoneVos(milestoneVos);
		
		List<MilestoneHistoryVo> milestoneHistoryVos=milestoneHistoryService.queryOutsoucingListByProjectId(id);
		project.setMilestoneHistoryVos(milestoneHistoryVos);
		return project;
	}
	
	@Override
	@Transactional
	public int deleteProject(String id) {
		LOGGER.info("删除id为" + id + "的项目立项");
		
		//清除附件
		ProjectDtlVo projectDtlVo=projectDao.queryProjectById(Integer.parseInt(id));
		AttachUtil.bindAttach(attachmentMapper, projectDtlVo.getAttach(),null);
		workloadService.deleteWorkloadByProjectId(id);
		outsourcingService.deleteOutsourcingByProjectId(id);
		milestoneService.deleteMilestoneListByProjectId(id);
		int count = projectDao.deleteProject(Integer.parseInt(id));
		
		
		String processInstId=wfbBusinessDao.queryWorkflowIdByBusinessId(WFServiceImpl.projectPrefix+id);
		if(processInstId!=null && !"".equals(processInstId)){
			LOGGER.info("删除processInstId为" + processInstId + "的流程");
			wfbBusinessDao.deleteWorkflwoBusinessByWFId(processInstId);
			workflowService.delete(processInstId, "");
		}
		return count;
	}
	
	/**
	 * 启动流程,并保存流程id与业务id，同时将流程信息集成的首要模块
	 * @Title: startWorkflow
	 * @param project
	 * @return
	 */
	private HashMap startWorkflow(Project project){
		UserInfoScope infoScope=itcMvcService.getUserInfoScopeDatas();
		String workflowId="pms_"+infoScope.getSiteId().toLowerCase()+"_project";
		String defkey = workflowService.queryForLatestProcessDefKey(workflowId);
		HashMap map=new HashMap();
		map.put("isRs", project.getIsRs());
		map.put("projectLeader", project.getProjectLeader() );
		map.put("businessId", project.getId());
		map.put("needDSZ", "N");
		ProcessInstance processInstance=null;
		try{
			processInstance = workflowService.startLatestProcessInstanceByDefKey(defkey,infoScope.getUserId(),map);
		}catch(Exception e){
			throw new PmsBasicException("项目流程启动失败", e);
		}
		String processInstId = processInstance.getProcessInstanceId();
		//保存流程实例id与businessid的关联
		WorkflowBusiness wBusiness=new WorkflowBusiness();
		wBusiness.setBusinessId(WFServiceImpl.projectPrefix+String.valueOf(project.getId()));
		wBusiness.setInstanceId(processInstId);
		wfbBusinessDao.insertWorkflowBusiness(wBusiness);
		//绑定合同到待办页面
		String url="pms/project/editProjectJsp.do?id="+project.getId()+"&processInstId="+processInstId;
		homepageService.createProcess(wBusiness.getId(), processInstId, "项目立项", "立项-"+project.getProjectName(), "草稿", url, infoScope, null);
		//获取当前活动节点，根据当前活动节点获取下一个任务节点、以及执行人，并返回给前台
		List<Task> activities = workflowService.getActiveTasks(processInstId);
		//刚启动流程，第一个活动节点肯定是属于当前登录人的
		Task task = activities.get(0);

		map = new HashMap();
		map.put("taskId", task.getId());
		map.put("id", project.getId());
		return map;
	}

	@Override
	@Transactional
	public HashMap insertProjectWithWorkflow(Project project,List<Milestone> milestones) {
		// TODO 验证
		LOGGER.info("开始插入项目并启动项目" + project.getProjectName());
		
		InitUserAndSiteIdUtil.initCreate(project, itcMvcService);
		ChangeStatusUtil.changeSToValue(project,  ChangeStatusUtil.draftCode);
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String prefix = userInfo.getUserId();
	        String dateStr = new SimpleDateFormat("yyMMdd").format( new Date() );
	        String newFlowNo = generateNewFlowNo( prefix+"-"+dateStr+"-" );
	        newFlowNo = prefix+"-"+dateStr+"-"+newFlowNo;
	        project.setFlowNo( newFlowNo );
		projectDao.insertProject(project);
		//改变附件状态，使其生效
		bindAttahch(null, project);
		//插入里程碑信息
		milestoneService.insertMilestoneList(milestones, project);
		Map map=startWorkflow(project);
		
		sequenceService.updateProjectSequece(project.getProjectCode());

		LOGGER.info("完成插入项目并启动项目" + project.getProjectName());
		return (HashMap) map;
	}

	@Override
	public ProjectWFVo queryProcessInstIdById(String id,String taskId) {
		LOGGER.info("查询流程id为："+id+"的项目信息");
		//查询项目id
		String pid=wfbBusinessDao.queryBusinessIdByWorkflowId(id);
		//查询项目信息
		ProjectDtlVo project =  projectDao.queryProjectById(Integer.parseInt(pid));
		ArrayList<HashMap<String,Object>> attachMap=AttachUtil.generatAttach(project.getAttach());
		project.setAttachMap(attachMap);
		
		ProjectWFVo projectWFVo=new ProjectWFVo();
		BeanUtils.copyProperties(project, projectWFVo);
		
		//增加节点附加信息
		Map map=workflowService.getElementInfo(taskId);
		projectWFVo.setElementMap(map);
		return projectWFVo; 
	}

	@Override
	@Transactional
	public void setWFVariable(String taskId, String processInstId,Project project) {
		Map<String,String> elementMap=workflowService.getElementInfo(taskId);
		if(elementMap!=null){
			String template=(String) elementMap.get("template");
			if(StringUtils.isNotBlank(template)){
				String []tems=template.split(",");
				for(int i=0;i<tems.length;i++){
					String fieldName=tems[i];
					Object value= ReflectionUtils.obtainFieldValue(project, fieldName);
					workflowService.setVariable(processInstId, fieldName, value);
				}
			}
		}
		
	}

	@Override
	public ProjectBudgetVo queryProjectBudgetById(String id) {
		ProjectDtlVo projectDtlVo=queryProjectById(id);
		ProjectBudgetVo projectBudgetVo=new ProjectBudgetVo();
		BeanUtils.copyProperties(projectDtlVo, projectBudgetVo);
		//根据招标信息获取预算使用情况
		List<BidVo> bids=projectDtlVo.getBids();
		//总金额
		double total=projectBudgetVo.getApplyBudget();
		//已用金额
		double used=0;
		//冻结金额
		double fozen=0;
		//可用金额
		double left=0;
		if(bids!=null){
			for(int i=0;i<bids.size();i++){
				BidVo bidVo=bids.get(0);
				BidDtlVo bidDtlVo=bidService.queryBidByBidId(bidVo.getBidId());
				List<BidResult> bidResult=bidDtlVo.getBidResult();
				if(bidResult!=null && !bidResult.isEmpty()
						&& ChangeStatusUtil.approvalCode.equals(bidResult.get(0).getStatus())){
					
				}else{
					fozen+=bidVo.getBudget();
				}
					
			}
		}
		left=total-used-fozen;
		HashMap budget=new HashMap();
		budget.put("total", total);
		budget.put("used", used);
		budget.put("fozen", fozen);
		budget.put("left", left);
		projectBudgetVo.setBudget(budget);
		return projectBudgetVo;
	}
	@Override
	@Transactional
	public void updateProjectApproving(Project project) {
		LOGGER.info("开始修改项目立项数据");
		ChangeStatusUtil.changeSToValue(project, ChangeStatusUtil.approvingCode);
		InitUserAndSiteIdUtil.initUpdate(project, itcMvcService);
		projectDao.updateProject(project);
		//改变附件状态，使其生效
		bindAttahch(String.valueOf(project.getId()), project);
		LOGGER.info("修改项目立项数据成功");
		
	}


	@Override
	@Transactional
	@Deprecated
	public int stopWorkflow(Project project, String processInstId,String reason) {
		
		
		return 0;
	}


	@Override
	public boolean isProjectCodeExisted(String projectCode,String projectId) {
		int num=projectDao.selectByCodeAndSiteid(projectCode, itcMvcService.getUserInfoScopeDatas().getSiteId());
		if(num==0){
			return false;
		}
		if(projectId==null || projectId.equals("")){
			return true;
		}
		ProjectDtlVo projectDtlVo=projectDao.queryProjectById(Integer.valueOf(projectId));
		if(projectDtlVo!=null && projectCode.equals(projectDtlVo.getProjectCode())){
			return false;
		}
		return true;
	}


	@Override
	public ProjectDtlVo queryProjectDetailById(String id) {
		ProjectDtlVo projectDtlVo=queryProjectById(id);
		//初始化验收信息
		List<CheckoutVo> checkoutVos=checkoutDao.queryCheckoutListByProjectId(Integer.valueOf(id));
		InitVoEnumUtil.initCheckoutVoList(checkoutVos, itcMvcService);
		projectDtlVo.setCheckoutVos(checkoutVos);
		
		//初始化结算信息
		List<PayVo> payVos=payDao.queryPayListByProjectId(Integer.valueOf(id));
		InitVoEnumUtil.initPayVoList(payVos, itcMvcService);
		projectDtlVo.setPayVos(payVos);
		return projectDtlVo;
	}


	@Override
	public Page<ProjectVo> queryProjectDetailList(Page<ProjectVo> page,
			UserInfoScope userInfo) {
		LOGGER.info("开始查询项目立项数据");
		//根据站点id查询
		page.setFuzzyParameter("siteid", itcMvcService.getUserInfoScopeDatas().getSiteId());
		List<ProjectVo> projects = projectDao.queryProjectList(page);
		InitVoEnumUtil.initProjectVoList(projects, itcMvcService);
		initProjectWithDetailMessage(projects);
		page.setResults(projects);
		LOGGER.info("查询项目立项数据成功");
		
		return page;

	}
	
	private void initProjectWithDetailMessage(List<ProjectVo> projectVos){
		if(projectVos!=null){
			for(int i=0;i<projectVos.size();i++){
				ProjectVo projectVo=projectVos.get(i);
				//获取招标数量
				List<BidResultVo> bidResultVos=bidResultDao.queryBidResultListByProjectId(projectVo.getId());
				int bidResultNum=0;
				if(bidResultVos!=null){
					bidResultNum=bidResultVos.size();
				}
				projectVo.setBidResultNum(bidResultNum);
				//获取项目的合同相关信息
				List<ContractVo> contractVos=contractDao.queryContractListByProjectId(projectVo.getId());
				double contractSum=0.0;
				int contractNum=0;
				if(contractVos!=null){
					contractNum=contractVos.size();
					for(int j=0;j<contractVos.size();j++){
						ContractVo contractVo=contractVos.get(j);
						if(contractVo.getTotalSum()!=null){
							contractSum+=contractVo.getTotalSum();
						}
						
					}
				}
				projectVo.setContractNum(contractNum);
				projectVo.setContractSum(contractSum);
				//获取结算数据
				List<PayVo> payVos=payDao.queryPayListByProjectId(projectVo.getId());
				double paySum=0.0;
				if(payVos!=null){
					for(int j=0;j<payVos.size();j++){
						PayVo payVo=payVos.get(j);
						if(ChangeStatusUtil.approvalCode.equals(payVo.getStatus())){
							paySum+=payVo.getActualpay();
						}
						
					}
				}
				projectVo.setPaySum(paySum);
				//设置项目状态
				String projectStatus="";
				if(projectVo.getStatus().equals("approving")){
					projectStatus="审批中";
				}
				if(projectVo.getStatus().equals("approved")){
					projectStatus="已立项";
				}
				if(bidResultNum>0){
					projectStatus="已招投标";
				}
				if(contractNum>0){
					projectStatus="已签合同";
				}

				projectVo.setProjectStatus(projectStatus);
			}
		}
		
		return ;
	}


	@Override
	public List<Map> queryProjectListByKeyWord(String kw) {
		LOGGER.info("查询关键字为"+kw+"的项目");
		Page<ProjectVo> page=new Page<ProjectVo>(1,11);
		page.setFuzzyParameter("status", ChangeStatusUtil.approvalCode);
		page.setFuzzyParameter("siteid", itcMvcService.getUserInfoScopeDatas().getSiteId());
		if(kw!=null && !"".equals(kw) ){
			page.setFuzzyParameter("project_name", kw);
		}
		List<ProjectVo> projects=projectDao.queryProjectList(page);
		
		List<Map> maps=new ArrayList<Map>();
		if(projects!=null){
			for(int i=0;i<projects.size();i++){
				HashMap map=new HashMap();
				map.put("id", projects.get(i).getId());
				map.put("name", projects.get(i).getProjectName());
				maps.add(map);
			}
		}
		return maps;
	}


	@Override
	public boolean isProjectNameExisted(String projectName, String projectId) {
		int num=projectDao.selectByNameAndSiteid(projectName, itcMvcService.getUserInfoScopeDatas().getSiteId());
		if(num==0){
			return false;
		}
		if(projectId==null || projectId.equals("")){
			return true;
		}
		ProjectDtlVo projectDtlVo=projectDao.queryProjectById(Integer.valueOf(projectId));
		if(projectDtlVo!=null && projectName.equals(projectDtlVo.getProjectName())){
			return false;
		}
		return true;
		
	}


	@Override
	@Transactional
	public void updateProjectApproving(Integer projectId) {
		ProjectDtlVo projectDtlVo=queryProjectById(projectId.toString());
		updateProjectApproving(projectDtlVo);
		
	}


	@Override
	@Transactional
	public void updateProjectApproved(Integer projectId) {
		ProjectDtlVo projectDtlVo=queryProjectById(projectId.toString());
		updateProject(projectDtlVo);
		
	}


	@Override
	@Transactional
	public void tmpInsertProject(Project project, List<Milestone> milestones,
			List<Outsourcing> outsourcings, List<Workload> workloads) {
	    tmpInsertProject(project, milestones);
	    workloadService.updateWorklaodList(workloads, project);
	    outsourcingService.updateOutsourcingList(outsourcings, project);
	
	}


	@Override
	@Transactional
	public HashMap insertProjectWithWorkflow(Project project,
			List<Milestone> milestones, List<Outsourcing> outsourcings,
			List<Workload> workloads) {
		HashMap map=insertProjectWithWorkflow(project, milestones);
	    workloadService.updateWorklaodList(workloads, project);
	    outsourcingService.updateOutsourcingList(outsourcings, project);
		return map;
	}


	@Override
	@Transactional
	public void updateProject(Project project, List<Milestone> milestones,
			List<Outsourcing> outsourcings, List<Workload> workloads) {
		updateProject(project, milestones);
		workloadService.updateWorklaodList(workloads, project);
	    outsourcingService.updateOutsourcingList(outsourcings, project);
	}


	@Override
	@Transactional
	public void tmpUpdateProject(Project project, List<Milestone> milestones,
			List<Outsourcing> outsourcings, List<Workload> workloads) {
		tmpUpdateProject(project, milestones);
		workloadService.updateWorklaodList(workloads, project);
	    outsourcingService.updateOutsourcingList(outsourcings, project);
		
	}


	@Override
	@Transactional
	public void updateProject(Project project) {
		LOGGER.info("开始修改项目立项数据");
		ChangeStatusUtil.changeSToValue(project, ChangeStatusUtil.approvalCode);
		InitUserAndSiteIdUtil.initUpdate(project, itcMvcService);
		projectDao.updateProject(project);
		//改变附件状态，使其生效
		bindAttahch(String.valueOf(project.getId()), project);
		//更新里程碑信息
		LOGGER.info("修改项目立项数据成功");
		
	}


	@Override
	@Transactional
	public boolean voidFlow(FlowVoidParamBean params) {
		
		FlowVoidUtil flowVoidUtil=new FlowVoidUtil(workflowService, homepageService);
		flowVoidUtil.initFlowVoid(params);
		String processInstId=flowVoidUtil.getProcessInstId();
		//更新项目状态为作废
		updateProjectToVoided(params.getBusinessId(), processInstId);
		return true;
	}
	
	private boolean updateProjectToVoided(String businessId,String processInstId){
		LOGGER.info("更新项目为作废状态，id为"+businessId);
		
		ProjectDtlVo projectDtlVo=queryProjectById(businessId);
		ChangeStatusUtil.changeToVoidedStatus(projectDtlVo);
		projectDao.updateProject(projectDtlVo);
		return true;
	}


	@Override
	public List<ProjectVo> queryProjectByYearAndProperty(String year,
			String property, String siteid) {
		LOGGER.info("开始查询项目立项数据");
		Page<ProjectVo> page=new Page<ProjectVo>(1,1000);
		//根据站点id查询
		page.setFuzzyParameter("siteid", siteid);
		page.setFuzzyParameter("pyear", year);
		page.setFuzzyParameter("property", property);
		List<ProjectVo> projects = projectDao.queryProjectList(page);
		InitVoEnumUtil.initProjectVoList(projects, itcMvcService);
		attachProjoctListWithMilestone(projects);
		attachProjoctListWithPaySum(projects);
		LOGGER.info("查询项目立项数据成功");
		return projects;
	}


	@Override
	public List<ProjectVo> queryProjectTotal(String year,String siteid) {
		LOGGER.info("根据年份和站点查询项目综合信息，year:"+year+",siteid:"+siteid);
		return projectDao.queryProjectDetailByYear(year, siteid);
	}


        @Override
        public String generateNewProjectCode(String prefix) {
            List<ProjectVo> list = projectDao.queryProjectListWithCodePrefix( prefix );
            Integer newFlowNo = 1 ;
            String newFlowStr = "001";
            for ( ProjectVo projectVo : list ) {
                String projectCode = projectVo.getProjectCode();
                String flowNoStr = projectCode.split( "-" )[2];
                if(Pattern.compile("^[\\d]+$").matcher( flowNoStr ).find()){
                    Integer flowNo = Integer.valueOf( flowNoStr );
                    if(newFlowNo.equals( flowNo )){
                        newFlowNo++;
                        newFlowStr = String.format( "%03d", newFlowNo );
                    }else {
                        break;
                    }
                }
            }
            return newFlowStr;
        }
        
        @Override
        public String generateNewFlowNo(String prefix) {
            List<ProjectVo> list = projectDao.queryProjectListWithFlowNoPrefix( prefix );
            Integer newFlowNo = 1 ;
            String newFlowStr = "01";
            for ( ProjectVo projectVo : list ) {
                String flowNo = projectVo.getFlowNo();
                String flowNoStr = flowNo.substring( flowNo.length()-2,flowNo.length() );
                if(Pattern.compile("^[\\d]+$").matcher( flowNoStr ).find()){
                    Integer seq = Integer.valueOf( flowNoStr );
                    if(newFlowNo.equals( seq )){
                        newFlowNo++;
                        newFlowStr = String.format( "%02d", newFlowNo );
                    }else {
                        break;
                    }
                }
            }
            return newFlowStr;
        }

        @Override
        public boolean isFlowNoExisted(String flowNo,String projectId) {
            int num=projectDao.selectByFlowNoAndSiteid(flowNo, itcMvcService.getUserInfoScopeDatas().getSiteId());
            if(num==0){
                return false;
            }
            if(projectId==null || projectId.equals("")){
                return true;
            }
            ProjectDtlVo projectDtlVo=projectDao.queryProjectById(Integer.valueOf(projectId));
            if(projectDtlVo!=null && flowNo.equals(projectDtlVo.getFlowNo())){
                return false;
            }
            return true;
        }
}
