package com.timss.pms.service.sfc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.Contract;
import com.timss.pms.bean.FlowVoidParamBean;
import com.timss.pms.bean.Payplan;
import com.timss.pms.bean.PayplanTmp;
import com.timss.pms.dao.ContractDao;
import com.timss.pms.dao.PayplanTmpDao;
import com.timss.pms.dao.SFCContractDao;
import com.timss.pms.dao.WorkflowBusinessDao;
import com.timss.pms.exception.PmsBasicException;
import com.timss.pms.service.BidResultService;
import com.timss.pms.service.BidService;
import com.timss.pms.service.ContractService;
import com.timss.pms.service.FlowStatusService;
import com.timss.pms.service.PayplanService;
import com.timss.pms.service.ProjectService;
import com.timss.pms.service.SequenceService;
import com.timss.pms.util.AttachUtil;
import com.timss.pms.util.ChangeStatusUtil;
import com.timss.pms.util.FlowStatusUpdateUtil;
import com.timss.pms.util.FlowVoidUtil;
import com.timss.pms.util.InitUserAndSiteIdUtil;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.BidResultDtlVo;
import com.timss.pms.vo.ContractDtlVo;
import com.timss.pms.vo.ContractVo;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.manager.support.impl.SequenceManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * 合同service实现类
 * @ClassName:     ContractServiceImpl
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2014-7-7 下午4:48:24
 */
public class ContractServiceImpl implements ContractService {
	
	private static final String PMS_SFC_SEQ = "PMS_SFC_SEQ";

	private static final Logger LOGGER=Logger.getLogger(ContractServiceImpl.class);

	@Autowired
	ContractDao contractDao;
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	AttachmentMapper attachmentMapper;
	@Autowired
	PayplanService payplanService;
	@Autowired
	ProjectService projectService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	WorkflowBusinessDao wfbBusinessDao;
	@Autowired
	HomepageService homepageService;
	@Autowired
	PayplanTmpDao payplanTmpDao;
	@Autowired
	BidService bidService;
	@Autowired
	@Qualifier("bidResultServiceImpl")
	BidResultService bidResultService;
	@Autowired
	SequenceService sequenceService;
	@Autowired
	SFCContractDao sfcContractDao;
    @Autowired
	SequenceManager sequenceManager;
    @Autowired
	@Qualifier("contractFlowStatusServiceImpl")
	FlowStatusService flowStatusService;
	
	@Override
	@Transactional
	public HashMap<String, Object> insertContract(Contract contract,List<Payplan> payplans) {
		LOGGER.info("开始插入合同数据数据");
		InitUserAndSiteIdUtil.initCreate(contract, itcMvcService);
		ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.approvalCode);
		//设置流程状态
		contract.setFlowStatus(FlowStatusUpdateUtil.getEnumValue(itcMvcService, ChangeStatusUtil.approvalCode));
		contractDao.insertContract(contract);
		//附件绑定
		AttachUtil.bindAttach(attachmentMapper, null, contract.getAttach());
		LOGGER.info("完成插入合同数据");
		return null;

	}
	@Override
	public List<ContractVo> queryContractListByProjectId(String id) {
		LOGGER.info("根据项目id:"+id+" 查询所属合同列表");
		List<ContractVo> contractVos=contractDao.queryContractListByProjectId(Integer.valueOf(id));
		
		InitVoEnumUtil.initContractVoList(contractVos, itcMvcService);
		return contractVos;
	}
	
	
	@Override
	public ContractDtlVo queryContractById(String id) {
		LOGGER.info("根据合同id："+id+" 查询合同详细信息");
		ContractDtlVo contractDtlVo=sfcContractDao.queryContractById(Integer.parseInt(id));
		
		//附件信息解析
		ArrayList<HashMap<String,Object>> attachMap=AttachUtil.generatAttach(contractDtlVo.getAttach());
		contractDtlVo.setAttachMap(attachMap);
		
		return contractDtlVo;
	}
	

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public Map changePayList(Contract contract, List<PayplanTmp> payplans) {
		LOGGER.info("开始变更结算计划");
		Map map=startWorkflow(contract);
		
		initPayplanTmp(payplans,contract,(String)map.get("processInstId"));
		if(payplans!=null && payplans.size()!=0){
			payplanTmpDao.insertPayplanTmpList(payplans);
		}
		updateContractApproving(contract);
		
		return map;
	}
	/**
	 * 初始化结算计划的一些数据
	 * @Title: initPayplanTmp
	 * @param payplans
	 * @param contract
	 */
	private void initPayplanTmp(List<PayplanTmp> payplans,Contract contract,String flowId){
		if(payplans!=null){
			for(int i=0;i<payplans.size();i++){
				PayplanTmp payplanTmp=payplans.get(i);
				payplanTmp.setContractId(contract.getId());
				payplanTmp.setFlowId(flowId);
			}
		}
	}
	
	//启动合同变更流程
	private Map<String,Object> startWorkflow(Contract contract){
		UserInfoScope infoScope=itcMvcService.getUserInfoScopeDatas();
		String defkey = workflowService.queryForLatestProcessDefKey("pms_itc_contract");
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("businessId", contract.getId());
		map.put("budget", contract.getTotalSum());
		map.put("belongTo", contract.getBelongTo());
		ProcessInstance processInstance=null;
		try{
			processInstance = workflowService.startLatestProcessInstanceByDefKey(defkey,infoScope.getUserId(),map);
		}catch(Exception e){
			throw new PmsBasicException("结算计划流程变更流程启动失败", e);
		}
		String processInstId=processInstance.getProcessInstanceId();
		contract.setProcessInstId(processInstId);
		//获取并设置流水号
		String flowId=sequenceManager.getGeneratedId(PMS_SFC_SEQ);
		contract.setTipNo(flowId);
		//更新合同的流水号和流程实例id
		contractDao.updateContract(contract);
		//绑定合同到待办页面
		String url="pms/contract/editContractJsp.do?contractId="+contract.getId()+"&processInstId="+processInstId;
		homepageService.createProcess(flowId, processInstId, "合同审批", "合同-" + contract.getName(), "提出申请", url, infoScope, null);
		
		
		//获取当前活动节点，根据当前活动节点获取下一个任务节点、以及执行人，并返回给前台
		List<Task> activities = workflowService.getActiveTasks(processInstId);
		//刚启动流程，第一个活动节点肯定是属于当前登录人的
		Task task = activities.get(0);

		map = new HashMap<String,Object>();
		map.put("taskId", task.getId());
		map.put("processInstId", processInstId);
		map.put("id", contract.getId());
		return map;
	}
	@Override
	@Transactional
	public int updateContract(Contract contract,List<Payplan> payplans) {
		LOGGER.info("开始修改合同数据数据");
		InitUserAndSiteIdUtil.initUpdate(contract, itcMvcService);
		ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.approvalCode);
		//设置流程状态
		contract.setFlowStatus(FlowStatusUpdateUtil.getEnumValue(itcMvcService, ChangeStatusUtil.approvalCode));
		contractDao.updateContract(contract);
		//附件绑定
		AttachUtil.bindAttach(attachmentMapper, null, contract.getAttach());
		LOGGER.info("完成修改合同数据");
		return 0;
	}
	@Override
	@Transactional
	public int updateContractApproving(Contract contract) {
		LOGGER.info("变更合同为审批中");
		Contract con=new Contract();
		con.setId(contract.getId());
		InitUserAndSiteIdUtil.initUpdate(con, itcMvcService);
		ChangeStatusUtil.changeSToValue(con, ChangeStatusUtil.approvingCode);
		//设置流程状态
		con.setFlowStatus(FlowStatusUpdateUtil.getEnumValue(itcMvcService, ChangeStatusUtil.approvingCode));
		contractDao.updateByPrimaryKeySelective(con);
		return 0;
	}
	@Override
	@Transactional
	public int updateContractApproved(Contract contract) {
		LOGGER.info("变更合同为审批通过");
		Contract con=new Contract();
		con.setId(contract.getId());
		InitUserAndSiteIdUtil.initUpdate(con, itcMvcService);
		ChangeStatusUtil.changeSToValue(con, ChangeStatusUtil.approvalCode);
		//设置流程状态
		con.setFlowStatus(FlowStatusUpdateUtil.getEnumValue(itcMvcService, ChangeStatusUtil.approvalCode));
		contractDao.updateByPrimaryKeySelective(con);
		return 0;
		
	}
	@Override
	public ContractDtlVo queryContractByBidId(String bidId) {
		//BidDtlVo bidDtlVo=bidService.queryBidByBidId(Integer.valueOf(bidId));
		BidResultDtlVo bidDtlVo=bidResultService.queryBidResultById(Integer.valueOf(bidId));
		ContractDtlVo contractDtlVo=new ContractDtlVo();
//		contractDtlVo.setBidId(bidDtlVo.getBidId());
		contractDtlVo.setBidId(bidDtlVo.getBidResultId());

		contractDtlVo.setBidName(bidDtlVo.getName());
		contractDtlVo.setProjectId(bidDtlVo.getProjectId());
		contractDtlVo.setProjectName(bidDtlVo.getProjectName());
		contractDtlVo.setType(bidDtlVo.getProjectProperty());
		contractDtlVo.setCommand(bidDtlVo.getCommand());
		
		return contractDtlVo;
	}
	@Override
	@Transactional
	public void tmpInsertContract(Contract contract, List<Payplan> payplans) {
		LOGGER.info("开始插入合同数据数据");
		InitUserAndSiteIdUtil.initCreate(contract, itcMvcService);
		ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.draftCode);
		//设置流程状态
		contract.setFlowStatus(FlowStatusUpdateUtil.getEnumValue(itcMvcService, ChangeStatusUtil.draftCode));
		contractDao.insertContract(contract);
		//附件绑定
		AttachUtil.bindAttach(attachmentMapper, null, contract.getAttach());
		
		
		LOGGER.info("完成插入合同数据");
		
	}
	
	@Override
	@Transactional
	public int tmpUpdateContract(Contract contract, List<Payplan> payplans) {
		
		LOGGER.info("开始修改合同数据数据");
		
		//获取数据库中的合同信息
		Contract oldContract=contractDao.queryContractById(contract.getId());
		//初始化合同必要的更新信息
		InitUserAndSiteIdUtil.initUpdate(contract, itcMvcService);
		ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.draftCode);
		//设置流程状态
		contract.setFlowStatus(FlowStatusUpdateUtil.getEnumValue(itcMvcService, ChangeStatusUtil.draftCode));
		contractDao.updateContract(contract);
		
		//附件绑定
		AttachUtil.bindAttach(attachmentMapper, oldContract.getAttach(), contract.getAttach());
		LOGGER.info("完成修改合同数据");
		return 0;
	}
	@Override
	@Transactional
	public void deleteContract(String contractId) {
		LOGGER.info("删除合同，id为"+contractId);
		int id=Integer.valueOf(contractId);
		//清除附件
		ContractDtlVo contract=contractDao.queryContractById(id);
		AttachUtil.bindAttach(attachmentMapper, contract.getAttach(), null);
		//删除合同
		contractDao.deleteContract(id);
		//去除首页的草稿记录
		homepageService.Delete(contract.getTipNo(), itcMvcService.getUserInfoScopeDatas());
		
	}
	/**
	 * 删除合同变更流程，并回复合同状态
	 * <p>Title: delWorkflow</p>
	 * <p>Description: </p>
	 * @param contractId
	 * @return
	 * @see com.timss.pms.service.ContractService#delWorkflow(java.lang.String)
	 */
	@Override
	@Transactional
	public int delWorkflow(String contractId) {
		LOGGER.info("删除合同变更流程，id为"+contractId);

		//修改合同状态
		ContractDtlVo contractDtlVo=queryContractById(contractId);
		Contract contract=new Contract();
		BeanUtils.copyProperties(contractDtlVo, contract);
		updateContractApproved(contract);
		//删除变更流程
		String processInstId=contractDtlVo.getProcessInstId();
		if(processInstId!=null){
			
			workflowService.delete(processInstId, "");
		}
		return 0;
	}
	@Override
	public int stopWorkflow(Contract contract, String processInstId,
			String reason) {
		return 0;
	}
	@Override
	public boolean checkContractCodeExisted(String contractCode) {
		int num=contractDao.selectByCodeAndSiteid(contractCode, itcMvcService.getUserInfoScopeDatas().getSiteId());
		if(num==0){
			return false;
		}
		return true;
	}
	@Override
	public boolean isContractCodeExisted(String contractCode, String contractId) {
		int num=sfcContractDao.selectByCodeAndSiteid(contractCode, itcMvcService.getUserInfoScopeDatas().getSiteId());
		if(num==0){
			return false;
		}
		if(contractId==null || contractId.equals("")){
			return true;
		}
		ContractDtlVo contractDtlVo=contractDao.queryContractById(Integer.valueOf(contractId));
		if(contractDtlVo!=null && contractCode.equals(contractDtlVo.getContractCode())){
			return false;
		}
		return true;
	}
	@Override
	public Page<ContractVo> queryContractList(Page<ContractVo> page,
			UserInfoScope userInfo) {
		LOGGER.info("开始查询招标结果数据");
		//根据站点id查询
		page.setFuzzyParameter("siteid", itcMvcService.getUserInfoScopeDatas().getSiteId());
		List<ContractVo> contracts = sfcContractDao.queryContractList(page);
		InitVoEnumUtil.initContractVoList(contracts,itcMvcService);
		page.setResults(contracts);
		LOGGER.info("查询招标结果数据成功");
		return page;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> queryContractListByKeyWord(String kw) {
		LOGGER.info("查询关键字为"+kw+"的合同");
		Page<ContractVo> page=new Page<ContractVo>(1,11);
		page.setFuzzyParameter("status_app", ChangeStatusUtil.approvalCode);
		page.setFuzzyParameter("siteid", itcMvcService.getUserInfoScopeDatas().getSiteId());
		if(kw!=null && !"".equals(kw) ){
			page.setFuzzyParameter("name", kw);
		}
		List<ContractVo> contracts=sfcContractDao.queryContractList(page);
		
		List<Map> maps=new ArrayList<Map>();
		if(contracts!=null){
			for(int i=0;i<contracts.size();i++){
				HashMap map=new HashMap();
				map.put("id", contracts.get(i).getId());
				map.put("name", contracts.get(i).getName());
				maps.add(map);
			}
		}
		return maps;
	}
	
	/**
	 * 合同作废流程
	 */
	@Override
	@Transactional
	public boolean voidFlow(FlowVoidParamBean params) {
		FlowVoidUtil flowVoidUtil=new FlowVoidUtil(workflowService, homepageService);
		flowVoidUtil.initFlowVoid(params);
		String processInstId=flowVoidUtil.getProcessInstId();
		//更新合同状态为作废
		updateContractToVoided(params.getBusinessId(), processInstId);
		return true;
	}
	/**
	 * 更新合同状态为作废
	 */
	@Override
	public boolean updateContractToVoided(String businessId,String processInstId){
		LOGGER.info("更新合同为作废状态，id为"+businessId);
		ContractDtlVo contractDtlVo=queryContractById(businessId);
		ChangeStatusUtil.changeToVoidedStatus(contractDtlVo);
		contractDao.updateContract(contractDtlVo);
		return true;
	}
	/**
	 * 提交合同信息(由于可能有多次提交，新增或更新，以及是否启动流程需要依据条件区分处理)
	 */
	@Override
	@Transactional
	public Map<String, Object> saveOrUpdateContractWithWorkFlow(Contract contract,boolean startWorkFlow) {
		LOGGER.info("开始提交合同数据数据");
		InitUserAndSiteIdUtil.initCreate(contract, itcMvcService);
		boolean isTmpSave = false;
		//设置审批状态为：草稿中 只有在第一次暂存的时候才会设置为草稿中。当第一个环节完成的时候，设置为审批中。
		if(null==contract.getId()){
			ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.draftCode);
			String flowId=sequenceManager.getGeneratedId(PMS_SFC_SEQ);
			contract.setTipNo(flowId);
			contractDao.insertContract(contract);
			//设置流程状态
			flowStatusService.updateFlowStatus(String.valueOf(contract.getId()), "合同专责起草");
			
			isTmpSave = true;
		}else{
			contractDao.updateContract(contract);
		}
		//附件绑定
		AttachUtil.bindAttach(attachmentMapper, null, contract.getAttach());		
		Map<String,Object> map = new HashMap<String,Object>(0); 
		//条件判断是否启动流程
		if(startWorkFlow){
			map = startAppWorkflow(contract);
		}else{
			//第一次暂存(isTmpSave&&!startWorkFlow)
			if(isTmpSave){

				String url="pms/contract/editContractJsp.do?contractId="+contract.getId();
				createOrUpdateHomepageTips(contract,"",url,"草稿");
			}
			map = getAppWorkflow(contract);
			//更新流程变量
			String processInstId=(String)map.get("processInstId");
			if(StringUtils.isNotBlank(processInstId)){
				workflowService.setVariable(processInstId, "budget", contract.getTotalSum());
				workflowService.setVariable(processInstId, "belongTo", contract.getBelongTo());
			}
			
		}
		map.put("contract", contract);
		LOGGER.info("完成提交合同数据");
		return map;
	}
	/**
	 * 更新合同附件
	 */
	@Override
	@Transactional
	public Map<String, Object> saveOrUpdateContractAttach(Contract contract) {
		LOGGER.info("开始更新合同附件");
		//附件绑定
		contractDao.updateContractAttach(contract);
		AttachUtil.bindAttach(attachmentMapper, null, contract.getAttach());		
		Map<String,Object> map = new HashMap<String,Object>(0); 
		map.put("contract", contract);
		LOGGER.info("完成更新合同附件");
		return map;
	}
	
	/**
	 * @Title:createOrUpdateHomepageTips
	 * @Description:需要使用homepageservice.create才能使同flowno的草稿与待办完成流转
	 * @param @param fmp
	 * @param @param processInstId
	 * @param @param url
	 * @param @param statusName
	 * @return void
	 * @throws
	 */
	private void createOrUpdateHomepageTips(Contract contract, String processInstId,String url,String statusName) {
		//更新待办草稿信息
	    UserInfo userinfo=itcMvcService.getUserInfoScopeDatas();
		HomepageWorkTask homepageworktask=new HomepageWorkTask();
		homepageworktask.setFlow(contract.getTipNo());
		homepageworktask.setProcessInstId(processInstId);
		homepageworktask.setUrl(url);
		homepageworktask.setTypeName("合同");
		homepageworktask.setName("合同-"+contract.getName());
		homepageworktask.setStatusName(statusName);
		homepageService.create(homepageworktask, userinfo);
	}
	/**
	 * 启动合同审批流程
	 * @param contract
	 * @param isDraft
	 * @return
	 */
	private HashMap<String,Object> startAppWorkflow(Contract contract){
		UserInfoScope infoScope=itcMvcService.getUserInfoScopeDatas();
		String defkey = workflowService.queryForLatestProcessDefKey("pms_"+infoScope.getSiteId().toLowerCase()+"_contract");
		HashMap<String,Object> map=new HashMap<String,Object>(0);
		map.put("businessId", contract.getId());
		map.put("budget", contract.getTotalSum());
		map.put("belongTo", contract.getBelongTo());
		ProcessInstance processInstance=null;
		
		try{
			processInstance = workflowService.startLatestProcessInstanceByDefKey(defkey,infoScope.getUserId(),map);
		}catch(Exception e){
			throw new PmsBasicException("合同审批流程启动失败", e);
		}
		//设置合同流程实例id
		String processInstId = processInstance.getProcessInstanceId();
		contract.setProcessInstId(processInstId);
		contractDao.updateContract(contract);
		//绑定合同到待办页面
		String url="pms/contract/editContractJsp.do?contractId="+contract.getId()+"&processInstId="+processInstId;
		createOrUpdateHomepageTips(contract,processInstId,url,"草稿");
		//homepageService.createProcess("contractApp"+wBusiness.getId(), processInstId, "合同", contract.getName(), "提交合同", url, infoScope, null);
		//获取当前活动节点，根据当前活动节点获取下一个任务节点、以及执行人，并返回给前台
		List<Task> activities = workflowService.getActiveTasks(processInstId);
		//刚启动流程，第一个活动节点肯定是属于当前登录人的
		Task task = activities.get(0);
		map.put("taskId", task.getId());
		map.put("processInstId", processInstId);
		map.put("id", contract.getId());
		return map;
	}
	/**
	 * 获取合同审批流程信息
	 * @param contract
	 * @return
	 */
	private HashMap<String,Object> getAppWorkflow(Contract contract){
		HashMap<String,Object> map=new HashMap<String,Object>(0);
		ContractDtlVo contractDtlVo=queryContractById(contract.getId().toString());
		String processInstId =contractDtlVo.getProcessInstId();
		if(null!=processInstId){
			//获取当前活动节点，根据当前活动节点获取下一个任务节点、以及执行人，并返回给前台
			List<Task> activities = workflowService.getActiveTasks(processInstId);
			//刚启动流程，第一个活动节点肯定是属于当前登录人的
			Task task = activities.get(0);
			map.put("taskId", task.getId());
			map.put("processInstId", processInstId);
		}
		return map;
	}
	/**
	 * 更新合同信息，并更新关联的结算计划
	 */
	@Override
	@Transactional
	public int updateContractWithWorkFlow(Contract contract,List<Payplan> payplans) {
		LOGGER.info("开始修改合同数据数据");
		InitUserAndSiteIdUtil.initUpdate(contract, itcMvcService);
		//因为之后更新了结算计划，所以结算计划变更的审批状态为审批通过
		ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.approvalCode);
		contractDao.updateContract(contract);
		//更新payplan结算计划信息
		if(null!=payplans&&0!=payplans.size()){
			payplanService.updatePayplan(payplans, contract);
		}
		//附件绑定
		AttachUtil.bindAttach(attachmentMapper, null, contract.getAttach());
		LOGGER.info("完成修改合同数据");
		return 0;
	}	
//更新不同类型的状态-begin
	@Override
	@Transactional
	public int updateContractApprovingWithSuffix(Contract contract,String suffix) {
		LOGGER.info("变更合同"+contract.getId()+" 状态"+suffix+"为审批中");
		InitUserAndSiteIdUtil.initUpdate(contract, itcMvcService);
		ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.approvingCode,suffix);
		contractDao.updateContract(contract);
		return 0;
	}
	@Override
	@Transactional
	public int updateContractApprovingWithSuffix(int contractId,String suffix) {
		LOGGER.info("变更合同"+contractId+" 状态"+suffix+"为审批中");
		Contract contract=queryContractById(String.valueOf(contractId));
		updateContractApprovingWithSuffix(contract,suffix);
		return 0;
	}
	@Override
	@Transactional
	public int updateContractApprovedWithSuffix(Contract contract,String suffix) {
		LOGGER.info("变更合同"+contract.getId()+" 状态"+suffix+"为审批通过");
		InitUserAndSiteIdUtil.initUpdate(contract, itcMvcService);
		ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.approvalCode,suffix);
		contractDao.updateContract(contract);
		return 0;
	}
	@Override
	@Transactional
	public int updateContractApprovedWithSuffix(int contractId,String suffix) {
		LOGGER.info("变更合同"+contractId+" 状态"+suffix+"为审批通过");
		Contract contract=queryContractById(String.valueOf(contractId));
		InitUserAndSiteIdUtil.initUpdate(contract, itcMvcService);
		ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.approvalCode,suffix);
		contractDao.updateContract(contract);
		return 0;
	}
	@Override
	@Transactional
	public int updateContractApproved(int contractId) {
		LOGGER.info("变更合同为审批通过");
		Contract contract=new Contract();
		contract.setId(contractId);
		return updateContractApproved(contract);
		
	}
	@Override
	@Transactional
	public int updateContractApproving(int contractId) {
		LOGGER.info("变更合同为审批中");
		Contract contract=new Contract();
		contract.setId(contractId);
		return updateContractApproving(contract);
		
	}
        @Override
        public String generateNewContractCode(String arg0) {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        public List<Map> queryContractListByKeyWordWithoutRowFilter(String arg0) {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        public Map<String, Object> startNullifyWorkflow(Contract contract) throws Exception{
            return null;
        }
        @Override
        @Transactional
        public int updateNullifyProcInstId(String contractId,String nullifyProcInstId) {
                LOGGER.info("更新作废流程实例id");
                contractDao.updateNullifyProcInstId( contractId, nullifyProcInstId );
                return 0;
        }
}
