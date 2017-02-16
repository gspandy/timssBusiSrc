package com.timss.pms.service.sjw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.Contract;
import com.timss.pms.bean.FlowVoidParamBean;
import com.timss.pms.bean.Payplan;
import com.timss.pms.bean.PayplanTmp;
import com.timss.pms.bean.WorkflowBusiness;
import com.timss.pms.dao.ContractDao;
import com.timss.pms.dao.PayplanTmpDao;
import com.timss.pms.dao.WorkflowBusinessDao;
import com.timss.pms.exception.PmsBasicException;
import com.timss.pms.service.BidResultService;
import com.timss.pms.service.BidService;
import com.timss.pms.service.ContractService;
import com.timss.pms.service.PayplanService;
import com.timss.pms.service.ProjectService;
import com.timss.pms.service.SequenceService;
import com.timss.pms.service.core.WFServiceImpl;
import com.timss.pms.util.AttachUtil;
import com.timss.pms.util.ChangeStatusUtil;
import com.timss.pms.util.FlowVoidUtil;
import com.timss.pms.util.InitUserAndSiteIdUtil;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.BidResultDtlVo;
import com.timss.pms.vo.ContractDtlVo;
import com.timss.pms.vo.ContractVo;
import com.timss.pms.vo.PayplanVo;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dao.sec.OrganizationMapper;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.helper.RowFilterHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * 合同service实现类
 * @ClassName:     ContractServiceImpl
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-7 下午4:48:24
 */

public class ContractServiceImpl implements ContractService {
	
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
	BidResultService bidResultService;
	@Autowired
	SequenceService sequenceService;
	@Autowired
	OrganizationMapper orgMapper;
	@Override
	@Transactional
	public HashMap<String, Object> insertContract(Contract contract,List<Payplan> payplans) {
		LOGGER.info("开始插入合同数据数据");
		InitUserAndSiteIdUtil.initCreate(contract, itcMvcService);
		ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.draftCode,"App");
		contractDao.insertContract(contract);
		
		if(payplans!=null){
			initPayplans(payplans,contract);
			payplanService.insertPayplan(payplans);
		}
		//附件绑定
		AttachUtil.bindAttach(attachmentMapper, null, contract.getAttach());
		//处理合同编号
		sequenceService.updateContractSequence(contract.getContractCode());
		Map<String,Object> maps=startAppWorkflow(contract);
		LOGGER.info("完成插入合同数据");
		return (HashMap<String, Object>) maps;

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
		ContractDtlVo contractDtlVo=contractDao.queryContractById(Integer.parseInt(id));
		
		contractDtlVo.setProjectName(contractDtlVo.getProjectDtlVo().getProjectName());
		
		//如果合同有招标信息,设置招标信息
		if(contractDtlVo.getBidId()!=null && !"".equals(contractDtlVo.getBidId())){
			//BidDtlVo bidDtlVo=bidService.queryBidByBidId(contractDtlVo.getBidId());
			BidResultDtlVo bidDtlVo=bidResultService.queryBidResultById(contractDtlVo.getBidId());
			contractDtlVo.setBidName(bidDtlVo.getName());
		}
		//附件信息解析
		ArrayList<HashMap<String,Object>> attachMap=AttachUtil.generatAttach(contractDtlVo.getAttach());
		contractDtlVo.setAttachMap(attachMap);
		//查询合同的结算计划信息
		List<PayplanVo> payplans=payplanService.queryPayplanListByContractId(Integer.parseInt(id));
		contractDtlVo.setPayplans(payplans);
		return contractDtlVo;
	}
	
	/**
	 * 对payplan进行初始化，如创建人，站点等信息的赋值
	 * @Title: initPayplans
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param payplans
	 */
	private void initPayplans(List<Payplan> payplans,Contract contract){
		if(payplans!=null){
			for(int i=0;i<payplans.size();i++){
				Payplan payplan=payplans.get(i);
				InitUserAndSiteIdUtil.initCreate(payplan, itcMvcService);
				payplan.setContractId(contract.getId());
			}
		}
	}
	@Override
	@Transactional
	public Map changePayList(Contract contract, List<PayplanTmp> payplanTmps) {
		
		initPayplanTmp(payplanTmps, contract, "");
		List<Payplan> payplans=convert2PayplanList(payplanTmps);
		payplanService.updatePayplan(payplans, contract);
		return null;
	}
	/**
	 * 初始化结算计划的一些数据
	 * @Title: initPayplanTmp
	 * @Description: TODO(这里用一句话描述这个方法的作用)
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
	
	private List<Payplan> convert2PayplanList(List<PayplanTmp> payplanTmps){
		List<Payplan> payplans=new ArrayList<Payplan>();
		if(payplanTmps!=null){
			for(int i=0;i<payplanTmps.size();i++){
				PayplanTmp payplanTmp=payplanTmps.get(i);
				Payplan payplan=new Payplan();
				
				payplan.setId(payplanTmp.getpayplanId());
				payplan.setPayType(payplanTmp.getPayType());
				payplan.setPaySum(payplanTmp.getPaySum());
				payplan.setNeedchecked(payplanTmp.getNeedchecked());
				payplan.setPayStatus(payplanTmp.getPayStatus());
				payplan.setCheckStatus(payplanTmp.getCheckStatus());
				payplan.setCommand(payplanTmp.getCommand());
				payplan.setContractId(payplanTmp.getContractId());
				payplan.setPercent(payplanTmp.getPercent());
				payplan.setSiteid(payplanTmp.getSiteid());
				
				payplans.add(payplan);
			}
		}
		return payplans;
	}
	
	//启动合同变更流程
	private HashMap startWorkflow(Contract contract){
		UserInfoScope infoScope=itcMvcService.getUserInfoScopeDatas();
		String workflowId="pms_"+infoScope.getSiteId().toLowerCase()+"_contractapp";
		String defkey = workflowService.queryForLatestProcessDefKey(workflowId);
		HashMap map=new HashMap();
		map.put("businessId", contract.getId());
		
		ProcessInstance processInstance=null;
		try{
			processInstance = workflowService.startLatestProcessInstanceByDefKey(defkey,infoScope.getUserId(),map);
		}catch(Exception e){
			throw new PmsBasicException("合同审批流程启动失败", e);
		}
		String processInstId = processInstance.getProcessInstanceId();
		//保存流程实例id与businessid的关联
		WorkflowBusiness wBusiness=new WorkflowBusiness();
		wBusiness.setBusinessId(WFServiceImpl.contractAppPrefix+String.valueOf(contract.getId()));
		wBusiness.setInstanceId(processInstId);
		wfbBusinessDao.insertWorkflowBusiness(wBusiness);
		//绑定合同到待办页面
		//changeContractStatus用于标识是否显示变更计划
		String url="pms/contract/editContractJsp.do?contractId="+contract.getId()+"&processInstId="+processInstId+"";
		homepageService.createProcess(wBusiness.getId(), processInstId, "合同审批", "合同-" + contract.getName(), "提出申请", url, infoScope, null);
		
		
		//获取当前活动节点，根据当前活动节点获取下一个任务节点、以及执行人，并返回给前台
		List<Task> activities = workflowService.getActiveTasks(processInstId);
		//刚启动流程，第一个活动节点肯定是属于当前登录人的
		Task task = activities.get(0);

		map = new HashMap();
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
		ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.draftCode,"App");
		contractDao.updateContract(contract);
		
		//更新payplan结算计划信息
		payplanService.updatePayplan(payplans, contract);
		//附件绑定
		AttachUtil.bindAttach(attachmentMapper, null, contract.getAttach());
		
		LOGGER.info("完成修改合同数据");
		return 0;
	}
	@Override
	@Transactional
	public int updateContractApproving(Contract contract) {
		InitUserAndSiteIdUtil.initUpdate(contract, itcMvcService);
		ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.approvingCode);
		contractDao.updateContract(contract);
		return 0;
	}
	@Override
	@Transactional
	public int updateContractApproved(Contract contract) {
		InitUserAndSiteIdUtil.initUpdate(contract, itcMvcService);
		ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.approvalCode);
		contractDao.updateContract(contract);
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
		ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.draftCode,"App");
		contractDao.insertContract(contract);
		
		if(payplans!=null){
			initPayplans(payplans,contract);
			payplanService.insertPayplan(payplans);
		}
		//附件绑定
		AttachUtil.bindAttach(attachmentMapper, null, contract.getAttach());
		//在首页创建一条草稿记录
                String url="pms/contract/editContractJsp.do?contractId="+contract.getId();
                homepageService.createProcess(contract.getTipNo(), createIdInHome(contract.getId()), "合同", "合同-" + contract
                                .getName(), "草稿", url, (UserInfo)itcMvcService.getUserInfoScopeDatas(), null);
                
		//处理合同编号
		sequenceService.updateContractSequence(contract.getContractCode());
		LOGGER.info("完成插入合同数据");
		
	}
	
	//构造在首页中唯一的id，防止与其他模块冲突
	private String createIdInHome(int id){
		return "pms_contract_"+id;
	}
	@Override
	@Transactional
	public int tmpUpdateContract(Contract contract, List<Payplan> payplans) {
		
		LOGGER.info("开始修改合同数据数据");
		
		//获取数据库中的合同信息
		Contract oldContract=contractDao.queryContractById(contract.getId());
		//初始化合同必要的更新信息
		InitUserAndSiteIdUtil.initUpdate(contract, itcMvcService);
		ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.draftCode,"App");
		contractDao.updateContract(contract);
		
		//更新payplan结算计划信息
		payplanService.updatePayplan(payplans, contract);
		
		//附件绑定
		AttachUtil.bindAttach(attachmentMapper, oldContract.getAttach(), contract.getAttach());
		LOGGER.info("完成修改合同数据");
		return 0;
	}
	@Override
	@Transactional
	public void deleteContract(String contractId) {
		int id=Integer.valueOf(contractId);
		//清除附件
		ContractDtlVo contract=contractDao.queryContractById(id);
		AttachUtil.bindAttach(attachmentMapper, contract.getAttach(), null);
		//删除合同
		contractDao.deleteContract(id);
		
		//去除首页的草稿记录
		String processInstId=wfbBusinessDao.queryWorkflowIdByBusinessId(WFServiceImpl.contractAppPrefix+id);
		if(processInstId!=null && !"".equals(processInstId)){
			LOGGER.info("删除processInstId为" + processInstId + "的流程");
			wfbBusinessDao.deleteWorkflwoBusinessByWFId(processInstId);
			workflowService.delete(processInstId, "");
		}
		
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
		int id=Integer.valueOf(contractId);

		//修改合同状态
		ContractDtlVo contractDtlVo=queryContractById(contractId);
		Contract contract=new Contract();
		BeanUtils.copyProperties(contractDtlVo, contract);
		updateContractApproved(contract);
		//删除变更流程
		String processInstId=wfbBusinessDao.queryWorkflowIdByBusinessId(WFServiceImpl.contractPrefix+id);
		if(processInstId!=null){
			wfbBusinessDao.deleteWorkflwoBusinessByWFId(processInstId);
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
	public boolean isContractCodeExisted(String contractCode, String contractId) {
		int num=contractDao.selectByCodeAndSiteid(contractCode, itcMvcService.getUserInfoScopeDatas().getSiteId());
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
	
	private void getRecurComp(Organization org ,List<String> list){
	    if(null!=org ){
	        Organization parentOrg = orgMapper.selectOrgByID( org.getParentCode() );
	        getRecurComp(parentOrg,list);
	        list.add( "'"+org.getName()+"'" );
	    }
	}
	@Override
	public Page<ContractVo> queryContractList(Page<ContractVo> page,
			UserInfoScope userInfo) {
		LOGGER.info("开始查询招标结果数据");
		//根据站点id查询
		page.setFuzzyParameter("siteid", itcMvcService.getUserInfoScopeDatas().getSiteId());
		List<Role> roles = userInfo.getRoles();
		String excludeString = RowFilterHelper.getExclusiveRoles( "PMS_EXCLUDE" );
		String[] excludeStringArray = excludeString.split( "," );
		boolean needLimit = true;
		for ( String exclude : excludeStringArray ) {
		    for ( Role role : roles ){
		        if ( role.getId().equals( exclude )&&!"SJW_MANAGER".equals( exclude ) ) {
		            needLimit = false;
                        }
		    }
                }
		if ( needLimit ) {
		    for ( Role role : roles ) {
	                    if ( "SJW_MANAGER".equals( role.getId() ) ) {
	                        List<String> orgList = new ArrayList<String>(0);
	                        List<Organization>orgs = userInfo.getOrgs();
	                        for ( Organization organization : orgs ) {
	                            getRecurComp(organization,orgList);
	                        }
	                        page.setParameter( "orgLimit",  0<orgList.size()?orgList.toString().replace( "[", "" ).replace( "]", "" ):"");
	                        break;
	                    }
	                }
                }
		List<ContractVo> contracts = contractDao.queryContractListForSJW(page);
		InitVoEnumUtil.initContractVoList(contracts,itcMvcService);
		page.setResults(contracts);
		LOGGER.info("查询招标结果数据成功");
		return page;
	}
	@Override
	public List<Map> queryContractListByKeyWord(String kw) {
		LOGGER.info("查询关键字为"+kw+"的合同");
		Page<ContractVo> page=new Page<ContractVo>(1,11);
		page.setFuzzyParameter("status_app", ChangeStatusUtil.approvalCode);
		page.setFuzzyParameter("siteid", itcMvcService.getUserInfoScopeDatas().getSiteId());
		if(kw!=null && !"".equals(kw) ){
			page.setFuzzyParameter("name", kw);
		}
		List<ContractVo> contracts=contractDao.queryContractList(page);
		
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
	@Override
	public Map<String, Object> saveOrUpdateContractWithWorkFlow(Contract contract,boolean startWorkFlow) {
    	        LOGGER.info("开始提交合同数据数据");
                InitUserAndSiteIdUtil.initCreate(contract, itcMvcService);
                boolean isTmpSave = false;
                //设置审批状态为：草稿中 只有在第一次暂存的时候才会设置为草稿中。当第一个环节完成的时候，设置为审批中。
                if(null==contract.getId()){
                        ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.draftCode,"App");
                        contractDao.insertContract(contract);
                        //处理合同编号
                        sequenceService.updateContractSequence(contract.getContractCode());
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
                }
                map.put("contract", contract);
                LOGGER.info("完成提交合同数据");
                return map;
	}
	/**
         * 启动合同审批流程
         * @param contract
         * @param isDraft
         * @return
         */
        private HashMap<String,Object> startAppWorkflow(Contract contract){
                UserInfoScope infoScope=itcMvcService.getUserInfoScopeDatas();
                String defkey = workflowService.queryForLatestProcessDefKey("pms_sjw_contractapp");
                HashMap<String,Object> map=new HashMap<String,Object>(0);
                map.put("businessId", contract.getId());
                ProcessInstance processInstance=null;
                String processInstId = wfbBusinessDao.queryWorkflowIdByBusinessId( WFServiceImpl.contractAppPrefix+String.valueOf(contract.getId()) ) ;
                if(StringUtils.isEmpty( processInstId )){
                    try{
                        processInstance = workflowService.startLatestProcessInstanceByDefKey(defkey,infoScope.getUserId(),map);
                    }catch(Exception e){
                        throw new PmsBasicException("合同审批流程启动失败", e);
                    }
                    processInstId = processInstance.getProcessInstanceId();
                    WorkflowBusiness wBusiness=new WorkflowBusiness();
                    wBusiness.setId(contract.getTipNo());
                    wBusiness.setBusinessId(WFServiceImpl.contractAppPrefix+String.valueOf(contract.getId()));
                    wBusiness.setInstanceId(processInstId);
                    wfbBusinessDao.insertWorkflowBusiness(wBusiness);
                }
                //绑定合同到待办页面
                String url="pms/contract/editContractJsp.do?contractId="+contract.getId()+"&processInstId="+processInstId;
                createOrUpdateHomepageTips(contract,processInstId,url,"提交合同");
                //获取当前活动节点，根据当前活动节点获取下一个任务节点、以及执行人，并返回给前台
                List<Task> activities = workflowService.getActiveTasks(processInstId);
                //刚启动流程，第一个活动节点肯定是属于当前登录人的
                Task task = activities.get(0);
                map.put("taskId", task.getId());
                map.put("processInstId", processInstId);
                map.put("id", contract.getId());
                return map;
        }
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
         * 获取合同审批流程信息
         * @param contract
         * @return
         */
        private HashMap<String,Object> getAppWorkflow(Contract contract){
                HashMap<String,Object> map=new HashMap<String,Object>(0);
                map.put("businessId", contract.getId());
                String processInstId =wfbBusinessDao.queryWorkflowIdByBusinessId(WFServiceImpl.contractAppPrefix+String.valueOf(contract.getId()));
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
	@Override
	@Transactional
	public Map<String, Object> saveOrUpdateContractAttach(Contract contract) {
		return null;
	}
	@Override
	public int updateContractApprovedWithSuffix(Contract contract,String suffix) {
    	        LOGGER.info("变更合同"+contract.getId()+" 状态"+suffix+"为审批通过");
                InitUserAndSiteIdUtil.initUpdate(contract, itcMvcService);
                ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.approvalCode,suffix);
                contractDao.updateContract(contract);
                return 0;
	}
	@Override
	public int updateContractApprovedWithSuffix(int contractId,String suffix) {
    	        LOGGER.info("变更合同"+contractId+" 状态"+suffix+"为审批通过");
                Contract contract=queryContractById(String.valueOf(contractId));
                InitUserAndSiteIdUtil.initUpdate(contract, itcMvcService);
                ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.approvalCode,suffix);
                contractDao.updateContract(contract);
                return 0;
	}
	@Override
	public int updateContractApproved(int arg0) {
		return 0;
	}
	@Override
	public int updateContractApprovingWithSuffix(Contract contract,String suffix) {
    	        LOGGER.info("变更合同"+contract.getId()+" 状态"+suffix+"为审批中");
                InitUserAndSiteIdUtil.initUpdate(contract, itcMvcService);
                ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.approvingCode,suffix);
                contractDao.updateContract(contract);
                return 0;
	}
	@Override
	public int updateContractApprovingWithSuffix(int contractId,String suffix) {
    	        LOGGER.info("变更合同"+contractId+" 状态"+suffix+"为审批中");
                Contract contract=queryContractById(String.valueOf(contractId));
                updateContractApprovingWithSuffix(contract,suffix);
                return 0;
	}
	@Override
	public int updateContractApproving(int arg0) {
		return 0;
	}
	@Override
	public int updateContractWithWorkFlow(Contract arg0, List<Payplan> arg1) {
		return 0;
	}
	@Override
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
                ChangeStatusUtil.changeToVoidedStatus(contractDtlVo,"App");
                contractDao.updateContract(contractDtlVo);
                contractDao.updateContractDelFlag( contractDtlVo );
                return true;
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
        public String generateNewContractCode(String prefix) {
            List<ContractVo> list = contractDao.queryContractListWithCodePrefix( prefix );
            Integer newFlowNo = 1;
            String newFlowStr = "001";
            for ( ContractVo contractVo : list ) {
                String contractCode = contractVo.getContractCode();
                String flowNoStr = contractCode.split( "-" )[2];
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
        public List<Map> queryContractListByKeyWordWithoutRowFilter(String kw) {
            LOGGER.info("查询关键字为"+kw+"的合同");
            Page<ContractVo> page=new Page<ContractVo>(1,11);
            page.setFuzzyParameter("status_app", ChangeStatusUtil.approvalCode);
            page.setFuzzyParameter("siteid", itcMvcService.getUserInfoScopeDatas().getSiteId());
            if(kw!=null && !"".equals(kw) ){
                    page.setParameter("kw", kw);
            }
            List<ContractVo> contracts=contractDao.queryContractListWithoutRowFilter(page);
            
            List<Map> maps=new ArrayList<Map>();
            if(contracts!=null){
                    for(int i=0;i<contracts.size();i++){
                            HashMap map=new HashMap();
                            map.put("id", contracts.get(i).getId());
                            map.put("name", contracts.get( i ).getContractCode()+"/"+contracts.get(i).getName());
                            maps.add(map);
                    }
            }
            return maps;
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
