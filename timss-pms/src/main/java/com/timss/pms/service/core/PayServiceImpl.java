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
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.FlowVoidParamBean;
import com.timss.pms.bean.Invoice;
import com.timss.pms.bean.Pay;
import com.timss.pms.bean.WorkflowBusiness;
import com.timss.pms.dao.PayDao;
import com.timss.pms.dao.WorkflowBusinessDao;
import com.timss.pms.exception.PmsBasicException;
import com.timss.pms.exception.pay.CreatePayTwiceException;
import com.timss.pms.service.ContractService;
import com.timss.pms.service.InvoiceService;
import com.timss.pms.service.PayService;
import com.timss.pms.service.PayplanService;
import com.timss.pms.util.AttachUtil;
import com.timss.pms.util.ChangeStatusUtil;
import com.timss.pms.util.FlowVoidUtil;
import com.timss.pms.util.InitUserAndSiteIdUtil;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.ContractDtlVo;
import com.timss.pms.vo.InvoiceVo;
import com.timss.pms.vo.PayDtlVo;
import com.timss.pms.vo.PayVo;
import com.timss.pms.vo.PayplanVo;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.manager.support.impl.SequenceManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
@Service
public class PayServiceImpl implements PayService {
	
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	PayDao payDao;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	@Autowired
	PayplanService payplanService;
	@Autowired
	AttachmentMapper attachmentMapper;
	@Autowired
	WorkflowBusinessDao wfbBusinessDao;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	HomepageService homepageService;
	@Autowired
	InvoiceService invoiceService;
	@Autowired
	SequenceManager sequenceManager;
	static Logger LOGGER=Logger.getLogger(PayServiceImpl.class);
	
	/**
         * @Title: insertPay
         * @Description: 新增结算
         * @param pay
         * @param invoices
         * @return
         */
	@Override
	@Transactional
	public void insertPay(Pay pay, List<Invoice> invoices) {
		LOGGER.info("开始插入结算款信息");
		InitUserAndSiteIdUtil.initCreate(pay, itcMvcService);
		ChangeStatusUtil.changeSToValue(pay, ChangeStatusUtil.approvalCode);
		//生成结算编号处理--开始
		pay.setPaySpNo( generatePaySpNo(pay) );
		//生成结算编号处理--结束
		payDao.insertPay(pay);
		//插入发票信息
		invoiceService.insertInvoice(invoices,pay);
		payplanService.updatePayStatusApproval(pay.getPayplanId());
		AttachUtil.bindAttach(attachmentMapper, null, pay.getAttach());
		LOGGER.info("完成插入结算款信息");
	}
	
	/**
         * @Title: queryPayByContractId
         * @Description: 根据合同ID查询结算
         * @param contractId
         * @return
         */
	@Override
	public PayDtlVo queryPayByContractId(String contractId) {
		ContractDtlVo contractDtlVo=contractService.queryContractById(contractId);
		PayDtlVo payDtlVo=new PayDtlVo();
		payDtlVo.setBepay(queryHasPayByContractId(Integer.valueOf(contractId)));
		payDtlVo.setContractName(contractDtlVo.getName());
		payDtlVo.setType(contractDtlVo.getType());
		payDtlVo.setProjectName(contractDtlVo.getProjectDtlVo().getProjectName());
		payDtlVo.setProjectId( contractDtlVo.getProjectDtlVo().getId() );
		payDtlVo.setTotalSum(contractDtlVo.getTotalSum());
		payDtlVo.setContractId(Integer.valueOf(contractId));
		payDtlVo.setContractCode(contractDtlVo.getContractCode());
		if("income".equals(contractDtlVo.getType())){
			payDtlVo.setXmhzf(contractDtlVo.getFirstParty());
		}
		if("cost".equals(contractDtlVo.getType())){
			payDtlVo.setXmhzf(contractDtlVo.getSecondParty());
		}
		payDtlVo.setPayplanVos(payplanService.getPayableByContractId(Integer.valueOf(contractId)));
		return payDtlVo;
	}
	
	/**
	 * @Title: queryHasPayByContractId
	 * @Description: 查询合同已付的金额
	 * @param contractId
	 * @return
	 */
	private double queryHasPayByContractId(int contractId){
		Double sum=payDao.queryHasPayByContractId(contractId);
		if(sum==null)
			return 0.0;
		return sum;
	}
	
	/**
         * @Title: queryPayByPayplanId
         * @Description: 根据结算计划ID查询结算
         * @param payplanId
         * @param contractId
         * @return
         */
	@Override
	public PayDtlVo queryPayByPayplanId(String payplanId,String contractId) {
		PayDtlVo  payDtlVo=queryPayByContractId(contractId);
		List<PayVo> payVos=payDao.queryPayListByPayplanId(Integer.valueOf(payplanId));
		if(payVos!=null && payVos.size()!=0){
			PayVo payVo=payVos.get(0);
			payDtlVo.setType( payVo.getType() ); 
			payDtlVo.setPayplanId(payVo.getPayplanId());
			payDtlVo.setCreateUser(payVo.getCreateUser());
			payDtlVo.setCreateTime(payVo.getCreateTime());
			payDtlVo.setId(payVo.getId());
			payDtlVo.setActualpay(payVo.getActualpay());
			payDtlVo.setBepay(payVo.getBepay());
			payDtlVo.setFcondition(payVo.getFcondition());
			payDtlVo.setPayway(payVo.getPayway());
			payDtlVo.setStatus(payVo.getStatus());
			payDtlVo.setAttach(payVo.getAttach());
			payDtlVo.setAttachMap(AttachUtil.generatAttach(payVo.getAttach()));
			payDtlVo.setPayDate(payVo.getPayDate());
			payDtlVo.setSendedtoerp(payVo.getSendedtoerp());
			payDtlVo.setUndoFlowCode( payVo.getUndoFlowCode() );
	                payDtlVo.setUndoFlowId( payVo.getUndoFlowId() );
	                payDtlVo.setUndoRemark( payVo.getUndoRemark() );
	                payDtlVo.setUndoStatus( payVo.getUndoStatus() );
	                payDtlVo.setPayCondition( payVo.getPayCondition() );
	                payDtlVo.setPaySpNo( payVo.getPaySpNo() );
		}
		//初始化结算阶段信息
		PayplanVo payplanVo=payplanService.queryPayplanById(Integer.valueOf(payplanId));
		List<PayplanVo> payplanVos=new ArrayList<PayplanVo>();
		payplanVos.add(payplanVo);
		InitVoEnumUtil.initPayplanVoList(payplanVos, itcMvcService);
		payDtlVo.setPayplanVos(payplanVos);
		if ( null!=payDtlVo.getId() ) {
		    List<InvoiceVo> invoiceVos=invoiceService.queryInvoiceListByPayId(payDtlVo.getId());
	            payDtlVo.setInvoices(invoiceVos);    
                }
		return payDtlVo;
	}
	
	@Override
	@Transactional
	public Map tmpInsertPay(Pay pay,List<Invoice> invoices) {
		LOGGER.info("开始插入结算款信息,并启动流程");
		List<PayVo> payVos=payDao.queryPayListByPayplanId(pay.getPayplanId());
		if(payVos==null || payVos.size()==0){
			InitUserAndSiteIdUtil.initCreate(pay, itcMvcService);
			ChangeStatusUtil.changeSToValue(pay, ChangeStatusUtil.draftCode);
			//生成结算编号处理--开始
			pay.setPaySpNo( generatePaySpNo(pay) );
	                //生成结算编号处理--结束
			payDao.insertPay(pay);
			invoiceService.insertInvoice(invoices, pay);
			payplanService.updatePayStatusApproving(pay.getPayplanId());
			AttachUtil.bindAttach(attachmentMapper, null, pay.getAttach());
			LOGGER.info("完成插入结算款信息,并启动流程");
			Map map=startWorkflow(pay);
			return map;
		}else{
			throw new CreatePayTwiceException("不能对一个付款计划新建两次");
		}
	}
	
	@Override
	@Transactional
	public void updatePayApproving(Pay pay) {
		LOGGER.info("开始更新结算款信息为审批中");
		//清理附件
		PayDtlVo payDtlVo=payDao.queryPayById(Integer.valueOf(pay.getId()));
		AttachUtil.bindAttach(attachmentMapper, payDtlVo.getAttach(), pay.getAttach());
		InitUserAndSiteIdUtil.initCreate(pay, itcMvcService);
		ChangeStatusUtil.changeSToValue(pay, ChangeStatusUtil.approvingCode);
		payDao.updatePay(pay);
		payplanService.updatePayStatusApproving(pay.getPayplanId());
		AttachUtil.bindAttach(attachmentMapper, null, pay.getAttach());
		LOGGER.info("完成更新结算款信息为审批中");
		
	}
	
	@Override
	@Transactional
	public void updatePayApproved(Pay pay) {
		LOGGER.info("开始修改结算信息为审批通过");
		//清理附件
		PayDtlVo payDtlVo=payDao.queryPayById(Integer.valueOf(pay.getId()));
		AttachUtil.bindAttach(attachmentMapper, payDtlVo.getAttach(), pay.getAttach());
		InitUserAndSiteIdUtil.initUpdate(pay, itcMvcService);
		ChangeStatusUtil.changeSToValue(pay,ChangeStatusUtil.approvalCode);
		payDao.updatePay(pay);
		payplanService.updatePayStatusApproval(pay.getPayplanId());
		AttachUtil.bindAttach(attachmentMapper, null, pay.getAttach());
		LOGGER.info("完成修改结算信息为审批通过");
	}
	@Override
	@Transactional
	public void tmpUpdatePay(Pay pay,List<Invoice> invoices) {
		LOGGER.info("开始执行tmpUpdatePay");
		//清理附件
		PayDtlVo payDtlVo=payDao.queryPayById(Integer.valueOf(pay.getId()));
		AttachUtil.bindAttach(attachmentMapper, payDtlVo.getAttach(), pay.getAttach());
		InitUserAndSiteIdUtil.initUpdate(pay, itcMvcService);
		if(!ChangeStatusUtil.approvingCode.equals(payDtlVo.getStatus()  )){
		    ChangeStatusUtil.changeSToValue(pay,ChangeStatusUtil.draftCode);
		}else{
		    ChangeStatusUtil.changeSToValue(pay,ChangeStatusUtil.approvingCode);
		}
		//更新结算编号--开始
		pay.setPaySpNo( generatePaySpNo(pay) );
                //更新结算编号--结束
		payDao.updatePay(pay);
		invoiceService.updateInvoice(invoices, pay);
		payplanService.updatePayStatusApproving(pay.getPayplanId());
		LOGGER.info("完成执行tmpUpdatePay");
	}
	@Override
	@Transactional
	public void deletePay(String id) {
		LOGGER.info("开始删除结算信息，其id为"+id);
		//清理附件
		PayDtlVo payDtlVo=payDao.queryPayById(Integer.valueOf(id));
		AttachUtil.bindAttach(attachmentMapper, payDtlVo.getAttach(), null);
		//恢复结算计划的状态
		payplanService.resetPayStatus(payDtlVo.getPayplanId());
		//删除验收信息
		payDao.deletePay(Integer.valueOf(id));
		//TODO清除首页信息
		String prefix="";
		if("cost".equals(payDtlVo.getType())){
			prefix=WFServiceImpl.payPrefix;
		}else if("income".equals(payDtlVo.getType())){
			prefix=WFServiceImpl.receiptPrefix;
		}else{
			throw new PmsBasicException("结算中根据type解析为对应的流程时出现一个异常的type为"+payDtlVo.getType());
		}
		//删除所属发票信息
		invoiceService.deleteInvoiceListWithPayId(Integer.valueOf(id));
		String processInstId=wfbBusinessDao.queryWorkflowIdByBusinessId(prefix+id);
		if(processInstId!=null && !"".equals(processInstId)){
			LOGGER.info("删除processInstId为" + processInstId + "的流程");
			//删除保存的业务与流程关联数据
			wfbBusinessDao.deleteWorkflwoBusinessByWFId(processInstId);
			//删除流程，并且删除流程在首页的记录
			workflowService.delete(processInstId, "");
		}
		LOGGER.info("完成删除结算信息，其id为"+id);
	}
	
	private Map startWorkflow(Pay pay ){
		UserInfoScope infoScope=itcMvcService.getUserInfoScopeDatas();
		//TODO 动态生成
		String ctype=pay.getType();
		String processKey="";
		String prefix="";
		String processName="";
		String namePrefix = "";
		if("cost".equals(ctype)){
			processKey="pms_"+infoScope.getSiteId().toLowerCase()+"_pay";
			prefix=WFServiceImpl.payPrefix;
			processName="项目付款";
			namePrefix="付款";
		}else if("income".equals(ctype)){
			processKey="pms_"+infoScope.getSiteId().toLowerCase()+"_receipt";
			prefix=WFServiceImpl.receiptPrefix;
			processName="项目收款";
			namePrefix = "收款";
		}else{
			throw new PmsBasicException("获取一个错误的合同类型，无法失败要启动的流程，type为："+pay.getType());
		}
		String defkey = workflowService.queryForLatestProcessDefKey(processKey);
		ContractDtlVo contractDtlVo=contractService.queryContractById(String.valueOf(pay.getContractId()));
		HashMap map=new HashMap();
		//map.put("isRs", project.getIsRs());
		map.put("pay", pay.getActualpay());
		map.put("businessId", pay.getId());
		ProcessInstance processInstance=null;
		try{
			processInstance = workflowService.startLatestProcessInstanceByDefKey(defkey,infoScope.getUserId(),map);
		}catch(Exception e){
			throw new PmsBasicException(processKey+"流程启动失败", e);
		}
		String processInstId = processInstance.getProcessInstanceId();
		//保存流程实例id与businessid的关联
		WorkflowBusiness wBusiness=new WorkflowBusiness();
		wBusiness.setBusinessId(prefix+String.valueOf(pay.getId()));
		wBusiness.setInstanceId(processInstId);
		wfbBusinessDao.insertWorkflowBusiness(wBusiness);
		//绑定合同到待办页面
		String url="pms/pay/editPayJsp.do?id="+pay.getId()+"&processInstId="
				+processInstId+"&payplanId="+pay.getPayplanId()+"&contractId="+pay.getContractId()+"&ctype="+ctype+
				"&id="+pay.getId();
		//20151222 zhx 在待办名称中，增加模块名，删除合同编号
		homepageService.createProcess(wBusiness.getId(), processInstId, processName, 
				namePrefix+"-"+contractDtlVo.getName(), "草稿", url, infoScope, null);
		//获取当前活动节点，根据当前活动节点获取下一个任务节点、以及执行人，并返回给前台
		List<Task> activities = workflowService.getActiveTasks(processInstId);
		//刚启动流程，第一个活动节点肯定是属于当前登录人的
		Task task = activities.get(0);
		map = new HashMap();
		map.put("taskId", task.getId());
		map.put("id", pay.getId());
		map.put("payplanId", pay.getPayplanId());
		return map;
	}
	@Override
	@Deprecated
	public int stopWorkflow(Pay pay, String processInstId, String reason) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Page<PayVo> queryPayList(Page<PayVo> page, UserInfoScope userInfo) {
		LOGGER.info("开始查询结算列表数据");
		//根据站点id查询
		page.setFuzzyParameter("siteid", itcMvcService.getUserInfoScopeDatas().getSiteId());
		List<PayVo> pays = payDao.queryPayList(page);
		InitVoEnumUtil.initPayVoList(pays,itcMvcService);
		page.setResults(pays);
		LOGGER.info("查询招结算列表成功");
		return page;
	}
	
	@Override
	@Transactional
	public void updatePayApproving(int payId) {
		PayDtlVo payDtlVo=payDao.queryPayById(payId);
		updatePayApproving(payDtlVo);
	}
	
	@Override
	@Transactional
	public void updatePayApproved(int payId) {
		PayDtlVo payDtlVo=payDao.queryPayById(payId);
		updatePayApproved(payDtlVo);
	}
	
	@Override
	@Transactional
	public boolean voidFlow(FlowVoidParamBean params) {
		FlowVoidUtil flowVoidUtil=new FlowVoidUtil(workflowService, homepageService);
		flowVoidUtil.initFlowVoid(params);
		String processInstId=flowVoidUtil.getProcessInstId();
		//更新验收状态为作废
		updatePayToVoided(params.getBusinessId(), processInstId);
		return true;
	}
	
	/**
	 * 作废退票流程
	 */
	@Override
        @Transactional
        public boolean voidUndoFlow(FlowVoidParamBean params) {
                FlowVoidUtil flowVoidUtil=new FlowVoidUtil(workflowService, homepageService);
                flowVoidUtil.initFlowVoid(params);
                //更新状态
                LOGGER.info("更新结算退票状态为作废，id为"+params.getBusinessId());
                PayDtlVo payDtlVo=payDao.queryPayById(Integer.valueOf(params.getBusinessId()));
                payDtlVo.setUndoStatus( ChangeStatusUtil.voidedCode );
                payDtlVo.setStatus( ChangeStatusUtil.approvalCode );
                payDtlVo.setUndoRemark( null );
                payDtlVo.setUndoFlowId( null );
                payDtlVo.setUndoFlowCode( null );
                payDao.updatePay(payDtlVo);
                return true;
        }
	private void updatePayToVoided(String businessId, String processInstId) {
		LOGGER.info("更新结算为作废状态，id为"+businessId);
		PayDtlVo payDtlVo=payDao.queryPayById(Integer.valueOf(businessId));
		ChangeStatusUtil.changeToVoidedStatus(payDtlVo);
		payDao.updatePay(payDtlVo);
		payplanService.resetPayStatus(payDtlVo.getPayplanId());
	}
	@Override
	//根据id，查询结算信息
	public PayDtlVo queryPayById(String payId) {
		PayDtlVo payVo=payDao.queryPayById(Integer.valueOf(payId));
		String contractId=payVo.getContractId().toString();
		String payplanId=payVo.getPayplanId().toString();
		PayDtlVo  payDtlVo=queryPayByContractId(contractId);
		payDtlVo.setPayplanId(payVo.getPayplanId());
		payDtlVo.setCreateUser(payVo.getCreateUser());
		payDtlVo.setCreateTime(payVo.getCreateTime());
		payDtlVo.setId(payVo.getId());
		payDtlVo.setActualpay(payVo.getActualpay());
		payDtlVo.setBepay(payVo.getBepay());
		payDtlVo.setFcondition(payVo.getFcondition());
		payDtlVo.setPayway(payVo.getPayway());
		payDtlVo.setStatus(payVo.getStatus());
		payDtlVo.setSendedtoerp(payVo.getSendedtoerp());
		payDtlVo.setAttach(payVo.getAttach());
		payDtlVo.setAttachMap(AttachUtil.generatAttach(payVo.getAttach()));
		payDtlVo.setPayDate(payVo.getPayDate());
		payDtlVo.setPayCondition( payVo.getPayCondition() );
		//初始化结算阶段信息
		PayplanVo payplanVo=payplanService.queryPayplanById(Integer.valueOf(payplanId));
		List<PayplanVo> payplanVos=new ArrayList<PayplanVo>();
		payplanVos.add(payplanVo);
		InitVoEnumUtil.initPayplanVoList(payplanVos, itcMvcService);
		payDtlVo.setPayplanVos(payplanVos);
		List<InvoiceVo> invoiceVos=invoiceService.queryInvoiceListByPayId(payDtlVo.getId());
		payDtlVo.setInvoices(invoiceVos);
		payDtlVo.setUndoFlowCode( payVo.getUndoFlowCode() );
		payDtlVo.setUndoFlowId( payVo.getUndoFlowId() );
		payDtlVo.setUndoRemark( payVo.getUndoRemark() );
		payDtlVo.setUndoStatus( payVo.getUndoStatus() );
		return payDtlVo;
	}
	/**
         * @Title: updateUndoInfo
         * @Description: 更新退票信息
         * @param pay 结算记录
         * @throws
         */
	@Override
	public Map<String, Object> updateUndoInfo(Pay pay){
	    Map<String, Object> map = new HashMap<String, Object>(0);
	    String undoRemark = pay.getUndoRemark();
	    pay = payDao.queryPayById( pay.getId() );
	    pay.setUndoRemark( undoRemark );
            payDao.updatePay( pay );
            String processInstanceId = pay.getUndoFlowId();
            List<Task> activities = workflowService.getActiveTasks(processInstanceId);
            //刚启动流程，第一个活动节点肯定是属于当前登录人的
            Task task = activities.get(0);
            map.put("taskId", task.getId());
            map.put("processInstId", processInstanceId);
            map.put("id", pay.getId());
            return map;
        }
        /**
         * @Title: startUndoFlow
         * @Description: 更新退票信息
         * @param pay 结算记录
         * @return 退票流程实例id
         * @throws
         */
	@Override
	public Map<String, Object> startUndoFlow(Pay pay){
            UserInfoScope infoScope=itcMvcService.getUserInfoScopeDatas();
            String defKeyStr = "pms_itc_payundo"; 
            String undoRemark = pay.getUndoRemark();
            String undoFlowCode = sequenceManager.getGeneratedId("PMS_PAY_UNDO");
            pay = payDao.queryPayById( pay.getId() );
            pay.setUndoRemark( undoRemark );
            pay.setUndoFlowCode( undoFlowCode );
            ContractDtlVo contractDtlVo=contractService.queryContractById(String.valueOf(pay.getContractId()));
            String defkey = workflowService.queryForLatestProcessDefKey(defKeyStr);
            String processName="退票申请"; 
            HashMap<String,Object> map=new HashMap<String,Object>(0);
            map.put("businessId", pay.getId());
            ProcessInstance processInstance=null;
            try{
                processInstance = workflowService.startLatestProcessInstanceByDefKey(defkey,infoScope.getUserId(),map);
            }catch(Exception e){
                throw new PmsBasicException("退票申请"+defkey+"流程启动失败", e);
            }
            String processInstanceId = processInstance.getProcessInstanceId();
            pay.setUndoFlowId( processInstanceId );
            //更新结算编号--开始
            pay.setPaySpNo( generatePaySpNo(pay) );
            //更新结算编号--结束
            payDao.updatePay( pay );
            //绑定结算退票到待办页面
            String url="pms/pay/editPayJsp.do?id="+pay.getId()+"&processInstId="
                                +processInstanceId+"&payplanId="+pay.getPayplanId()+"&contractId="+pay.getContractId()+"&ctype="+pay.getType()+
                                "&id="+pay.getId();
          //20151222 zhx 在待办名称中，增加模块名，删除合同编号
            homepageService.createProcess(pay.getUndoFlowCode(), processInstanceId, processName, "付款-"+contractDtlVo.getName(), "草稿", url, infoScope, null);
            //获取当前活动节点，根据当前活动节点获取下一个任务节点、以及执行人，并返回给前台
            List<Task> activities = workflowService.getActiveTasks(processInstanceId);
            //刚启动流程，第一个活动节点肯定是属于当前登录人的
            Task task = activities.get(0);
            map.put("taskId", task.getId());
            map.put("processInstId", processInstanceId);
            map.put("id", pay.getId());
            return map;
        }
	
	/**
         * @Title: updateStatus
         * @Description: 更新退票信息状态
         * @param pay 结算记录
         * @return int
         * @throws
         */
	@Override
        public int updateStatus(Pay pay){
            int result = 0;
            String status = pay.getStatus();
            String undoStatus = pay.getUndoStatus();
            pay = payDao.queryPayById( pay.getId() );
            pay.setStatus( status );
            pay.setUndoStatus( undoStatus );
            result = payDao.updatePay( pay );
            return result;
        }
	
	/**
	 * @Title: queryBusinessIdByUndoFlowId
         * @Description: 根据退票流程id查业务id
         * @param undoFlowId 退票流程id
         * @return String
         * @throws
	 */
	@Override
	public String queryBusinessIdByUndoFlowId(String undoFlowId) {
	    String businessId = "";
	    Pay payCon = new Pay();
	    payCon.setUndoFlowId( undoFlowId );
	    List<PayVo> pays = payDao.queryPayByCondition( payCon );
	    if(1==pays.size()){
	        businessId = pays.get( 0 ).getId().toString();
	    }
	    return businessId;
	}
	
        @Override
        public String generateNewPaySpNo(String prefix) {
            List<PayVo> list = payDao.queryPayListWithPaySpNoPrefix( prefix );
            Integer newPaySpNo = 1;
            String newPaySpNoStr = "01";
            for ( PayVo payVo : list ) {
                String paySpNo = payVo.getPaySpNo();
                String paySpNoStr = paySpNo.substring( 11 );
                if(Pattern.compile("^[\\d]+$").matcher( paySpNoStr ).find()){
                    Integer flowNo = Integer.valueOf( paySpNoStr );
                    if(newPaySpNo.equals( flowNo )){
                        newPaySpNo++;
                        newPaySpNoStr = String.format( "%02d", newPaySpNo );
                    }else {
                        break;
                    }
                }
            }
            return prefix+newPaySpNoStr;
        }
        
        @Override
        public boolean isPaySpNoExisted(String paySpNo, String payId) {
                int num=payDao.selectByPaySpNoAndSiteid(paySpNo, itcMvcService.getUserInfoScopeDatas().getSiteId());
                if(num==0){
                    return false;
                }
                if(StringUtils.isEmpty( payId )){
                    return true;
                }
                PayDtlVo payDtlVo=payDao.queryPayById(Integer.valueOf(payId));
                if(null!=payDtlVo && paySpNo.equals(payDtlVo.getPaySpNo())){
                        return false;
                }
                return true;
        }
        
        private String generatePaySpNo(Pay pay) {
            String paySpNo = pay.getPaySpNo();
            Integer payplanId = pay.getPayplanId();
            PayplanVo payplanVo = payplanService.queryPayplanById( payplanId );
            String payplanType = payplanVo.getPayType();
            if(StringUtils.isEmpty( paySpNo )){
                //新增时，创建结算编号
                paySpNo = generateNewPaySpNo( payplanType.toUpperCase()+new SimpleDateFormat("yyyyMMdd").format( new Date()) );
            }else if(!paySpNo.contains( payplanType.toUpperCase() )){
                //更新时，如果发现结算阶段变更，更新结算编号
                paySpNo = generateNewPaySpNo( payplanType.toUpperCase()+new SimpleDateFormat("yyyyMMdd").format( pay.getCreatedate()) );
            }
            return paySpNo;
        }
}
