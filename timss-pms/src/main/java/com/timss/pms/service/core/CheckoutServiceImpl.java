package com.timss.pms.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.Checkout;
import com.timss.pms.bean.FlowVoidParamBean;
import com.timss.pms.bean.WorkflowBusiness;
import com.timss.pms.dao.CheckoutDao;
import com.timss.pms.dao.WorkflowBusinessDao;
import com.timss.pms.exception.PmsBasicException;
import com.timss.pms.service.CheckoutService;
import com.timss.pms.service.ContractService;
import com.timss.pms.service.PayplanService;
import com.timss.pms.util.AttachUtil;
import com.timss.pms.util.ChangeStatusUtil;
import com.timss.pms.util.FlowVoidUtil;
import com.timss.pms.util.InitUserAndSiteIdUtil;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.CheckoutDtlVo;
import com.timss.pms.vo.CheckoutVo;
import com.timss.pms.vo.ContractDtlVo;
import com.timss.pms.vo.PayplanVo;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * 项目验收service接口实现类
 * @ClassName:     CheckoutServiceImpl
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2014-7-22 上午9:50:51
 */
@Service
public class CheckoutServiceImpl implements CheckoutService {

	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	CheckoutDao checkoutDao;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	@Autowired
	AttachmentMapper attachmentMapper;
	@Autowired
	PayplanService payplanService;
	@Autowired
	ISecurityMaintenanceManager iSecurityMaintenanceManager;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	WorkflowBusinessDao wfbBusinessDao;
	@Autowired
	HomepageService homepageService;
	
	private static final Logger LOGGER=Logger.getLogger(CheckoutServiceImpl.class);
	@Override
	@Transactional
	public void insertCheckOut(Checkout checkout) {
		LOGGER.info("开始插入项目验收信息");
		InitUserAndSiteIdUtil.initCreate(checkout, itcMvcService);
		ChangeStatusUtil.changeSToValue(checkout,ChangeStatusUtil.approvalCode);
		checkoutDao.insertCheckout(checkout);
		payplanService.updateCheckStatusApproval(checkout.getPayplanId());
		AttachUtil.bindAttach(attachmentMapper, null, checkout.getAttach());
		LOGGER.info("完成插入项目验收信息");

	}
	@Override
	public List<CheckoutVo> queryCheckoutListByContractId(String id) {
		LOGGER.info("查询合同所属的验收列表，合同id为："+id);
		return checkoutDao.queryCheckoutListByContractId(Integer.parseInt(id));
	}
	@Override
	//先查询合同信息，再查询验收信息，最后查询结算计划信息，并整合为实际的验收信息
	public CheckoutDtlVo queryCheckoutById(String id) {
		LOGGER.info("查询项目验收的详细信息，id为："+id);
		//从验收表查询信息
		CheckoutDtlVo checkoutVo=checkoutDao.queryCheckoutById(Integer.parseInt(id));
		String contractId=checkoutVo.getContractId().toString();
		String payplanId=checkoutVo.getPayplanId().toString();
		//查询验收对应的合同信息
		CheckoutDtlVo checkoutDtlVo=queryCheckoutByContractId(contractId, null);
	    //验收信息与合同信息整合
		checkoutDtlVo.setPayplanId(checkoutVo.getPayplanId());
		checkoutDtlVo.setTime(checkoutVo.getTime());
		checkoutDtlVo.setType(checkoutVo.getType());
		checkoutDtlVo.setStatus(checkoutVo.getStatus());
		checkoutDtlVo.setAttach(checkoutVo.getAttach());
		checkoutDtlVo.setId(checkoutVo.getId());
		checkoutDtlVo.setCreateUser(checkoutVo.getCreateUser());
		checkoutDtlVo.setCreateTime(checkoutVo.getCreateTime());
		checkoutDtlVo.setCommand(checkoutVo.getCommand());
		
		checkoutDtlVo.setCheckUser(checkoutVo.getCheckUser());
		checkoutDtlVo.setIsProjectChange(checkoutVo.getIsProjectChange());
		checkoutDtlVo.setStartDate(checkoutVo.getStartDate());
		checkoutDtlVo.setEndDate(checkoutVo.getEndDate());
		checkoutDtlVo.setPayDecription(checkoutVo.getPayDecription());
		
		checkoutDtlVo.setAttachMap(AttachUtil.generatAttach(checkoutVo.getAttach()));
		
		//查询结算计化信息
		PayplanVo payplanVo=payplanService.queryPayplanById(Integer.valueOf(payplanId));
		List<PayplanVo> payplanVos=new ArrayList<PayplanVo>();
		payplanVos.add(payplanVo);
		InitVoEnumUtil.initPayplanVoList(payplanVos, itcMvcService);
		checkoutDtlVo.setPayplanVos(payplanVos);
		
		
		return checkoutDtlVo;
	}
	@Override
	//查询合同中包含的验收信息
	public CheckoutDtlVo queryCheckoutByContractId(String contractId,CheckoutDtlVo checkoutDtlVo) {
		if(checkoutDtlVo==null){
			checkoutDtlVo=new CheckoutDtlVo();
		}
		//查询合同信息
		ContractDtlVo contractDtlVo=contractService.queryContractById(contractId);
		//提出出合同中包含的验收信息
		checkoutDtlVo.setContractId(Integer.parseInt(contractId));
		checkoutDtlVo.setContractName(contractDtlVo.getName());
		checkoutDtlVo.setProjectName(contractDtlVo.getProjectDtlVo().getProjectName());
		checkoutDtlVo.setProjectId(String.valueOf( contractDtlVo.getProjectId()));
		UserInfo userInfoScope=itcMvcService.getUserInfoById(contractDtlVo.getProjectDtlVo().getProjectLeader());
		checkoutDtlVo.setProjectLeader(userInfoScope.getUserName());
		
		if("income".equals(contractDtlVo.getType())){
			checkoutDtlVo.setXmhzf(contractDtlVo.getFirstParty());
			checkoutDtlVo.setHzffzr(contractDtlVo.getFpLeader());
		}
		if("cost".equals(contractDtlVo.getType())){
			checkoutDtlVo.setXmhzf(contractDtlVo.getSecondParty());
			checkoutDtlVo.setHzffzr(contractDtlVo.getSpLeader());
		}
		
		List<PayplanVo> payplanVos=payplanService.getCheckableByContractId(Integer.valueOf(contractId));
		checkoutDtlVo.setPayplanVos(payplanVos);
		
		return checkoutDtlVo;
	}
	@Override
    //查询结算计划中包含的验收信息
	public CheckoutDtlVo queryCheckoutByPayplanId(String payplanId,
			String contractId) {
		CheckoutDtlVo checkoutDtlVo=queryCheckoutByContractId(contractId, null);
		List<CheckoutVo> checkoutVos=checkoutDao.queryCheckoutListByPayplanId(Integer.valueOf(payplanId));
		if(checkoutVos!=null && !checkoutVos.isEmpty()){
			CheckoutVo checkoutVo=checkoutVos.get(0);
			checkoutDtlVo.setPayplanId(checkoutVo.getPayplanId());
			checkoutDtlVo.setTime(checkoutVo.getTime());
			checkoutDtlVo.setType(checkoutVo.getType());
			checkoutDtlVo.setStatus(checkoutVo.getStatus());
			checkoutDtlVo.setAttach(checkoutVo.getAttach());
			checkoutDtlVo.setId(checkoutVo.getId());
			checkoutDtlVo.setCreateUser(checkoutVo.getCreateUser());
			checkoutDtlVo.setCreateTime(checkoutVo.getCreateTime());
			checkoutDtlVo.setCommand(checkoutVo.getCommand());
			
			checkoutDtlVo.setCheckUser(checkoutVo.getCheckUser());
			checkoutDtlVo.setIsProjectChange(checkoutVo.getIsProjectChange());
			checkoutDtlVo.setStartDate(checkoutVo.getStartDate());
			checkoutDtlVo.setEndDate(checkoutVo.getEndDate());
			checkoutDtlVo.setPayDecription(checkoutVo.getPayDecription());
			
			checkoutDtlVo.setAttachMap(AttachUtil.generatAttach(checkoutVo.getAttach()));
			
			
		}
		//初始化结算阶段信息
		PayplanVo payplanVo=payplanService.queryPayplanById(Integer.valueOf(payplanId));
		List<PayplanVo> payplanVos=new ArrayList<PayplanVo>();
		payplanVos.add(payplanVo);
		InitVoEnumUtil.initPayplanVoList(payplanVos, itcMvcService);
		checkoutDtlVo.setPayplanVos(payplanVos);
		return checkoutDtlVo;
	}
	@Override
	@Transactional
	public Map tmpInsertCheckout(Checkout checkout) {
		LOGGER.info("开始插入项目验收信息,并启动流程");
		InitUserAndSiteIdUtil.initCreate(checkout, itcMvcService);
		ChangeStatusUtil.changeSToValue(checkout,ChangeStatusUtil.draftCode);
		//插入验收信息
		checkoutDao.insertCheckout(checkout);
		//更新结算计划状态
		payplanService.updateCheckStatusApproving(checkout.getPayplanId());
		//附件信息绑定
		AttachUtil.bindAttach(attachmentMapper, null, checkout.getAttach());
		//启动工作流
		Map map=startWorkflow(checkout);
		LOGGER.info("开始插入项目验收信息,并启动流程");
		return map;
	}
	
	private Map startWorkflow(Checkout checkout){
		UserInfoScope infoScope=itcMvcService.getUserInfoScopeDatas();
		String workflowId="pms_"+infoScope.getSiteId().toLowerCase()+"_checkout";
		String defkey = workflowService.queryForLatestProcessDefKey(workflowId);
		ContractDtlVo contractDtlVo=contractService.queryContractById(String.valueOf(checkout.getContractId()));
		Map map=new HashMap();
		map.put("projectLeader", contractDtlVo.getProjectDtlVo().getProjectLeader());
		map.put("businessLeader", contractDtlVo.getProjectDtlVo().getBusinessLeader());
		map.put("businessId", checkout.getId());
		ProcessInstance processInstance=null;
		
		try{
			processInstance = workflowService.startLatestProcessInstanceByDefKey(defkey,infoScope.getUserId(),map);
		}catch(Exception e){
			throw new PmsBasicException("项目流程启动失败", e);
		}
		String processInstId = processInstance.getProcessInstanceId();
		//保存流程实例id与businessid的关联
		WorkflowBusiness wBusiness=new WorkflowBusiness();
		wBusiness.setBusinessId(WFServiceImpl.checkoutPrefix+String.valueOf(checkout.getId()));
		wBusiness.setInstanceId(processInstId);
		wfbBusinessDao.insertWorkflowBusiness(wBusiness);
		
		//绑定合同到待办页面
		String url="pms/checkout/editCheckoutJsp.do?id="+checkout.getId()+"&processInstId="
				+processInstId+"&payplanId="+checkout.getPayplanId()+"&contractId="+checkout.getContractId()+
				"&id="+checkout.getId();
		homepageService.createProcess(wBusiness.getId(), processInstId, "项目验收", "验收-" + contractDtlVo.getName(), "草稿", url, infoScope, null);
		
		
		//获取当前活动节点，根据当前活动节点获取下一个任务节点、以及执行人，并返回给前台
		List<Task> activities = workflowService.getActiveTasks(processInstId);
		//刚启动流程，第一个活动节点肯定是属于当前登录人的
		Task task = activities.get(0);

		map = new HashMap();
		map.put("taskId", task.getId());
		map.put("id", checkout.getId());
		map.put("payplanId", checkout.getPayplanId());
		return map;
	}
	@Override
	@Transactional
	public void updateCheckoutApproving(Checkout checkout) {
		LOGGER.info("开始插入项目验收信息,并更改验收信息状态为审批中");
		//旧附件清理，新附件增加
		CheckoutDtlVo checkoutDtlVo=checkoutDao.queryCheckoutById(checkout.getId());
		AttachUtil.bindAttach(attachmentMapper, checkoutDtlVo.getAttach(), checkout.getAttach());
		
		InitUserAndSiteIdUtil.initUpdate(checkout, itcMvcService);
		ChangeStatusUtil.changeSToValue(checkout,ChangeStatusUtil.approvingCode);
		
		checkoutDao.updateCheckout(checkout);
		//更新结算计划状态
		payplanService.updateCheckStatusApproving(checkout.getPayplanId());
		
		LOGGER.info("完成插入项目验收信息,并更改验收信息状态为审批中");
		
	}
	@Override
	@Transactional
	public void updateCheckoutApproved(Checkout checkout) {
		LOGGER.info("开始修改项目验收信息,并更改验收信息状态为审批完成");
		//旧附件清理，新附件增加
		CheckoutDtlVo checkoutDtlVo=checkoutDao.queryCheckoutById(checkout.getId());
		AttachUtil.bindAttach(attachmentMapper, checkoutDtlVo.getAttach(), checkout.getAttach());
		
		//更新验收为审批完成状态
		InitUserAndSiteIdUtil.initUpdate(checkout, itcMvcService);
		ChangeStatusUtil.changeSToValue(checkout,ChangeStatusUtil.approvalCode);
		checkoutDao.updateCheckout(checkout);
		
		//更新结算计划状态
		payplanService.updateCheckStatusApproval(checkout.getPayplanId());
		
		LOGGER.info("完成修改项目验收信息,并更改验收信息状态为审批完成");
		
	}
	@Override
	@Transactional
	public void tmpUpdateCheckout(Checkout checkout) {
		LOGGER.info("开始修改项目验收信息,并更改验收信息状态为审批完成");
		//旧附件清理，新附件增加
		CheckoutDtlVo checkoutDtlVo=checkoutDao.queryCheckoutById(checkout.getId());
		AttachUtil.bindAttach(attachmentMapper, checkoutDtlVo.getAttach(), checkout.getAttach());
		
		InitUserAndSiteIdUtil.initUpdate(checkout, itcMvcService);
		ChangeStatusUtil.changeSToValue(checkout,ChangeStatusUtil.draftCode);
		checkoutDao.updateCheckout(checkout);
		//更新结算计划状态
		payplanService.updateCheckStatusApproving(checkout.getPayplanId());
		LOGGER.info("完成修改项目验收信息,并更改验收信息状态为审批完成");
		
	}
	@Override
	@Transactional
	public void deleteCheckout(String id) {
		LOGGER.info("开始删除id为"+id+"的验收信息");
		//清理附件
		CheckoutDtlVo checkoutDtlVo=checkoutDao.queryCheckoutById(Integer.valueOf(id));
		AttachUtil.bindAttach(attachmentMapper, checkoutDtlVo.getAttach(), null);
		//恢复结算计划的状态
		payplanService.resetCheckStatus(checkoutDtlVo.getPayplanId());
		//删除验收信息
		checkoutDao.deleteCheckout(Integer.valueOf(id));
		//TODO清除首页信息
		String processInstId=wfbBusinessDao.queryWorkflowIdByBusinessId(WFServiceImpl.checkoutPrefix+id);
		if(processInstId!=null && !"".equals(processInstId)){
			LOGGER.info("删除processInstId为" + processInstId + "的流程");
			//删除保存的业务与流程关联数据
			wfbBusinessDao.deleteWorkflwoBusinessByWFId(processInstId);
			//删除流程，并且删除流程在首页的记录
			workflowService.delete(processInstId, "");
		}
		LOGGER.info("完成删除id为"+id+"的验收信息");
		
	}
	@Override
	@Deprecated
	public int stopWorkflow(Checkout checkout, String processInstId,
			String reason) {
		return 0;
	}
	@Override
	public Page<CheckoutVo> queryCheckoutList(Page<CheckoutVo> page,
			UserInfoScope userInfo) {
		LOGGER.info("开始查询验收列表数据");
		//根据站点id查询
		page.setFuzzyParameter("siteid", itcMvcService.getUserInfoScopeDatas().getSiteId());
		List<CheckoutVo> checkouts = checkoutDao.queryCheckoutList(page);
		InitVoEnumUtil.initCheckoutVoList(checkouts,itcMvcService);
		page.setResults(checkouts);
		LOGGER.info("查询查询验收列表成功");
		return page;
	}
	@Override
	@Transactional
	public void updateCheckoutApproving(int checkoutId) {
		CheckoutDtlVo checkoutDtlVo=queryCheckoutById(String.valueOf(checkoutId));
		updateCheckoutApproving(checkoutDtlVo);
		
	}
	@Override
	@Transactional
	public void updateCheckoutApproved(int checkoutId) {
		CheckoutDtlVo checkoutDtlVo=queryCheckoutById(String.valueOf(checkoutId));
		updateCheckoutApproved(checkoutDtlVo);
		
	}
	@Override
	@Transactional
	public boolean voidFlow(FlowVoidParamBean params) {
		FlowVoidUtil flowVoidUtil=new FlowVoidUtil(workflowService, homepageService);
		flowVoidUtil.initFlowVoid(params);
		String processInstId=flowVoidUtil.getProcessInstId();
		//更新验收状态为作废
		updateCheckoutToVoided(params.getBusinessId(), processInstId);
		return true;
	}
	//更新验收状态为作废状态
	private void updateCheckoutToVoided(String businessId, String processInstId) {
		LOGGER.info("更新验收为作废状态，id为"+businessId);
		CheckoutDtlVo checkoutDtlVo=checkoutDao.queryCheckoutById(Integer.valueOf(businessId));
		ChangeStatusUtil.changeToVoidedStatus(checkoutDtlVo);
		checkoutDao.updateCheckout(checkoutDtlVo);
		
		payplanService.resetCheckStatus(checkoutDtlVo.getPayplanId());
	}
	
}
