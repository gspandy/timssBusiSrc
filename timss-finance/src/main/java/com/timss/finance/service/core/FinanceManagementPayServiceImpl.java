package com.timss.finance.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timss.facade.util.AttachUtil;
import com.timss.facade.util.FlowVoidUtil;
import com.timss.facade.util.InitUserAndSiteIdNewUtil;
import com.timss.finance.bean.FinanceMain;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.bean.FinanceManagementPay;
import com.timss.finance.dao.FinanceMainDao;
import com.timss.finance.dao.FinanceMainDetailDao;
import com.timss.finance.dao.FinanceManagementPayDao;
import com.timss.finance.dao.FinanceWorkflowBusinessDao;
import com.timss.finance.service.FinanceMainDetailService;
import com.timss.finance.service.FinanceMainService;
import com.timss.finance.service.FinanceManagementApplyService;
import com.timss.finance.service.FinanceManagementPayService;
import com.timss.finance.util.ChangeStatusUtil;
import com.timss.finance.util.FinStatusEnum;
import com.timss.finance.util.FinanceUtil;
import com.timss.finance.vo.FinanceManagementApplyVo;
import com.timss.finance.vo.FinanceManagementPayDtlVo;
import com.timss.pms.bean.FlowVoidParamBean;
import com.timss.pms.bean.WorkflowBusiness;
import com.timss.purchase.bean.PurVendor;
import com.timss.purchase.service.PurVendorService;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.manager.support.IEnumerationManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
@Service
public class FinanceManagementPayServiceImpl implements FinanceManagementPayService {
	private static String FMPPREFIX="adexpenses_";
	private static String DRAFTCODE="draft";
	private static String APPROVINGCODE="approving";
	private static String APPROVEDCODE="approved";
	private static String VOIDCODE="voided";
	@Autowired
	HomepageService homepageService;
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	FinanceMainService financeMainService;
	@Autowired
	FinanceMainDetailService financeMainDetailService;
	@Autowired
	FinanceManagementApplyService financeManagementApplyService;
	@Autowired
	PurVendorService purVendorService;
	@Autowired
	FinanceManagementPayDao financeManagementPayDao;
	@Autowired
	FinanceMainDetailDao financeMainDetailDao;
	@Autowired
	FinanceMainDao financeMainDao;
	@Autowired
	FinanceWorkflowBusinessDao wfbBusinessDao;
	@Autowired
	AttachmentMapper attachmentMapper;
	@Autowired
	IAuthorizationManager authManager;
	@Autowired
        private IEnumerationManager iEnumManager;
	Logger logger=Logger.getLogger(FinanceManagementPayServiceImpl.class);
	/**
	 * 根据id查询行政报销
	 */
	@Override
	public FinanceManagementPayDtlVo queryFMPtById(String id) {
		logger.info("根据id："+id+" 查询行政报销");
		//查询行政报销
		FinanceManagementPayDtlVo expenses=financeManagementPayDao.queryFMPById(Integer.valueOf(id));
		//附件信息解析
		ArrayList<HashMap<String,Object>> attachMap=AttachUtil.generatAttach(expenses.getAttach());
		expenses.setAttachMap(attachMap);
		//查询expenses行政报销明细
		List<FinanceMainDetail> financeMainDetails=financeMainDetailService.queryFinanceMainDetailByFid(expenses.getMainId());
		expenses.setFinanceMainDetails(financeMainDetails);
		return expenses;
	}
	/**
	 * 根据mainid查询行政报销
	 */
	@Override
	public FinanceManagementPayDtlVo queryFMPtByMainId(String id) {
		logger.info("根据mainid："+id+" 查询行政报销");
		int fmpId = financeManagementPayDao.queryFMPIdByFMPMainId(id);
		//查询行政报销
		FinanceManagementPayDtlVo expenses=financeManagementPayDao.queryFMPById(fmpId);
		//附件信息解析
		List<HashMap<String,Object>> attachMap=AttachUtil.generatAttach(expenses.getAttach());
		expenses.setAttachMap(attachMap);
		//查询expenses行政报销明细
		List<FinanceMainDetail> financeMainDetails=financeMainDetailService.queryFinanceMainDetailByFid(expenses.getMainId());
		expenses.setFinanceMainDetails(financeMainDetails);
		return expenses;
	}
	/**
	 * 删除行政报销
	 */
	@Override
	@Transactional
	public void deleteFMP(String id) {
		logger.info("删除行政报销，mainid为"+id);
		//清除附件
		int fmpId = financeManagementPayDao.queryFMPIdByFMPMainId(id);
		FinanceManagementPayDtlVo expenses=financeManagementPayDao.queryFMPById(fmpId);
		if(null!=expenses){
			AttachUtil.bindAttach(attachmentMapper, expenses.getAttach(), null);
			//删除行政报销主表
			financeMainDao.deleteFinanceMainLogically(id);
			//删除行政报销一对一关联表
			financeManagementPayDao.deleteFMP(String.valueOf(fmpId));
			//去除首页的草稿记录
			homepageService.Delete(expenses.getMainId(), itcMvcService.getUserInfoScopeDatas());
		}
	}
	/**
	 * 保存或更新行政报销
	 */
	@Override
	@Transactional
	public Map<String, Object> saveOrUpdateFMP(FinanceManagementPayDtlVo fmp,List<FinanceMainDetail> fmd,boolean startWorkFlow) throws Exception {
		logger.info("开始提交行政报销数据");
		InitUserAndSiteIdNewUtil.initCreate(fmp, itcMvcService);
		boolean isTmpSave = false;
		UserInfo userinfo=itcMvcService.getUserInfoScopeDatas();
		String siteid = userinfo.getSiteId();
		//设置审批状态为：草稿中 只有在第一次暂存的时候才会设置为草稿中。当流程第一个环节完成的时候，设置为审批中。
		if(StringUtil.isEmpty(fmp.getId())){
			FinanceMain fm = new FinanceMain();
			String financeType = fmp.getType();
//			String financeFlowNameCn = FinanceUtil.genFinNameCn("administrativeExpenses") + "报销";
		       // AppEnum appEnum = iEnumManager.retrieveEnumeration("FIN_FLOW_TYPE","administrativeExpenses");
			AppEnum appEnum = getAppEnumObj("FIN_FLOW_TYPE","administrativeExpenses",siteid);
//			List<AppEnum> tempList = iEnumManager.retrieveEnumeration("FIN_WORKFLOW_APPROVE_STATUS","administrativeExpenses");
//		               AppEnum appEnum = null;
//		               for(AppEnum temp:tempList){
//		                   if(siteid.equals(temp.getSiteId())){
//		                       appEnum = temp; 
//		                   }
//		               }
//		        
		        
		        String financeFlowNameCn = appEnum.getLabel();
			String financeFlowTypeCn = FinanceUtil.genFinTypeCn(financeType) + "报销";
			String createUserId = fmp.getCreateuser();
			String createUserSiteId = fmp.getSiteid();
			SecureUser createUser = authManager.retriveUserById(createUserId, createUserSiteId);
			String createUserName = createUser.getName();
			fm.setApplyId(fmp.getApplyId());
			fm.setFname(fmp.getName());
			fm.setDeptid(fmp.getDeptid());
			fm.setTotal_amount(Double.valueOf(fmp.getPrice()));
			fm.setFinance_flow(financeFlowNameCn);
			fm.setFinance_flowid( appEnum.getCode() );
			fm.setFinance_type(financeFlowTypeCn);
			fm.setFinance_typeid(financeType);
			//-----------以下新建专属属性------------
			fm.setCreateid(fmp.getCreateuser());
			fm.setCreatorname(createUserName);
			fm.setIs_show("Y"); //初始为显示报销单
			fm.setFlag_item("0"); //初始为"未生成ERP凭证"
			InitUserAndSiteIdNewUtil.initCreate(fm, itcMvcService);
			financeMainDao.insertFinanceMain(fm);
			//按枚举名称更新状态值
			financeMainService.updateFinanceMainStatusByFid( "fmp_apply", fm.getFid() );
			fmp.setMainId(fm.getFid());
			fmp.setDelFlag("0");
			fmp.setStatus(DRAFTCODE);
			InitUserAndSiteIdNewUtil.initCreate(fmp, itcMvcService);
			financeManagementPayDao.insertFMP(fmp);
			//新增报销明细
			if(!fmd.isEmpty()){
				financeMainDetailService.deleteFinanceMainDetail(fm);
				for (FinanceMainDetail financeMainDetail : fmd) {
					financeMainDetail.setFid(fm.getFid());
					financeMainDetailDao.insertFinanceMainDetail(financeMainDetail);
				}
			}
			isTmpSave = true;
		}else{
			FinanceMain fm = financeMainDao.queryFinanceMainByFid(fmp.getMainId());
			String financeType = fmp.getType();
//			String financeFlowNameCn = FinanceUtil.genFinNameCn("administractiveExpenses") + "报销";
//			AppEnum appEnum = iEnumManager.retrieveEnumeration("FIN_FLOW_TYPE","administrativeExpenses");
			AppEnum appEnum = getAppEnumObj("FIN_FLOW_TYPE","administrativeExpenses",siteid);
			
//			List<AppEnum> tempList = iEnumManager.retrieveEnumeration("FIN_WORKFLOW_APPROVE_STATUS","administrativeExpenses");
//                         AppEnum appEnum = null;
//                         for(AppEnum temp:tempList){
//                             if(siteid.equals(temp.getSiteId())){
//                                 appEnum = temp; 
//                             }
//                         }
                         
			String financeFlowNameCn = appEnum.getLabel();
			String financeFlowTypeCn = FinanceUtil.genFinTypeCn(financeType) + "报销";
			fm.setApplyId(fmp.getApplyId());
			fm.setFname(fmp.getName());
			fm.setDeptid(fmp.getDeptid());
			fm.setTotal_amount(Double.valueOf(fmp.getPrice()));
			fm.setFinance_flow(financeFlowNameCn);
			fm.setFinance_flowid( appEnum.getCode() );
			fm.setFinance_type(financeFlowTypeCn);
			fm.setFinance_typeid(financeType);
			InitUserAndSiteIdNewUtil.initUpdate(fm, itcMvcService);
			financeMainDao.updateFinanceMainByFid(fm);
			InitUserAndSiteIdNewUtil.initUpdate(fmp, itcMvcService);
			fmp.setDelFlag("0");
			financeManagementPayDao.updateFMP(fmp);
			//更新报销明细
			financeMainDetailService.deleteFinanceMainDetail(fm);
			for (FinanceMainDetail financeMainDetail : fmd) {
				financeMainDetail.setFid(fm.getFid());
				financeMainDetailDao.insertFinanceMainDetail(financeMainDetail);
			}
		}
		//附件绑定
		AttachUtil.bindAttach(attachmentMapper, null, fmp.getAttach());		
		Map<String,Object> map = new HashMap<String,Object>(0); 
		//条件判断是否启动流程
		if(startWorkFlow){
			map = startAppWorkflow(fmp);
		}else{
			//第一次暂存(isTmpSave&&!startWorkFlow)
			if(isTmpSave){
//				UserInfoScope infoScope=itcMvcService.getUserInfoScopeDatas();
				String url="finance/expenses/editAdministractiveExpensesJsp.do?id="+fmp.getMainId();
				createOrUpdateHomepageTips(fmp,"",url,"草稿");
				//homepageService.createProcess(fmp.getMainId(), "fin_fmp_"+fmp.getId(), "行政报销", fmp.getName(), "草稿", url, infoScope, null);
			}
			map = getAppWorkflow(fmp);
		}
		map.put("fmp", fmp);
		logger.info("完成提交行政报销数据");
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
	private void createOrUpdateHomepageTips(FinanceManagementPay fmp, String processInstId,String url,String statusName) {
		//更新待办草稿信息
	    UserInfo userinfo=itcMvcService.getUserInfoScopeDatas();
		HomepageWorkTask homepageworktask=new HomepageWorkTask();
		homepageworktask.setFlow(fmp.getMainId());
		homepageworktask.setProcessInstId(processInstId);
		homepageworktask.setUrl(url);
		homepageworktask.setTypeName("行政报销");
		homepageworktask.setName(fmp.getName());
		homepageworktask.setStatusName(statusName);
		homepageService.create(homepageworktask, userinfo);
	}
	/**
	 * 启动行政报销审批流程
	 * @param fmp
	 * @return
	 * @throws Exception 
	 */
	private Map<String,Object> startAppWorkflow(FinanceManagementPay fmp) throws Exception{
		UserInfoScope infoScope=itcMvcService.getUserInfoScopeDatas();
		String defkey = workflowService.queryForLatestProcessDefKey("finance_itc_managementcostpay");
		Map<String,Object> map=new HashMap<String,Object>(0);
		//根据用户所属部门是否为科技公司行政部 设置流程变量
		String currOrgId=infoScope.getOrgId();
		//行政部部门编号 1232602
		if("1232602".equals(currOrgId)){
			map.put("isAdministrationDeptProj", "Y");
		}else{
			map.put("isAdministrationDeptProj", "N");
		}
		map.put("needSubmitToManager", "Y");
		map.put("businessId", fmp.getId());
		ProcessInstance processInstance=null;
		try{
			processInstance = workflowService.startLatestProcessInstanceByDefKey(defkey,infoScope.getUserId(),map);
		}catch(Exception e){
			throw new Exception("行政报销审批流程启动失败", e);
		}
		String processInstId = processInstance.getProcessInstanceId();
		//保存流程实例id与businessid的关联
		WorkflowBusiness wBusiness=new WorkflowBusiness();
		wBusiness.setBusinessId(FMPPREFIX+fmp.getId());
		wBusiness.setInstanceId(processInstId);
		wfbBusinessDao.insertWorkflowBusiness(wBusiness);
		//绑定合同到待办页面
		String url="finance/expenses/editAdministractiveExpensesJsp.do?id="+fmp.getMainId()+"&processInstId="+processInstId;
		//homepageService.createProcess(fmp.getMainId(), processInstId, "行政报销", fmp.getName(), "提交行政报销", url, infoScope, null);
		createOrUpdateHomepageTips(fmp,processInstId,url,"提交行政报销");
		//获取当前活动节点，根据当前活动节点获取下一个任务节点、以及执行人，并返回给前台
		List<Task> activities = workflowService.getActiveTasks(processInstId);
		//刚启动流程，第一个活动节点肯定是属于当前登录人的
		Task task = activities.get(0);
		map.put("taskId", task.getId());
		map.put("processInstId", processInstId);
		map.put("id", fmp.getId());
		return map;
	}
	/**
	 * 获取行政报销审批流程信息
	 * @param fmp
	 * @return
	 */
	private Map<String,Object> getAppWorkflow(FinanceManagementPay fmp){
		Map<String,Object> map=new HashMap<String,Object>(0);
		map.put("businessId", fmp.getId());
		String processInstId =wfbBusinessDao.queryWorkflowIdByBusinessId(FMPPREFIX+String.valueOf(fmp.getId()));
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
	 * 作废流程
	 */
	@Override
	public boolean voidFlow(FlowVoidParamBean params) {
		FlowVoidUtil flowVoidUtil=new FlowVoidUtil(workflowService, homepageService);
		flowVoidUtil.initFlowVoid(params);
		String processInstId=flowVoidUtil.getProcessInstId();
		//更新报销状态为作废
		updateFMPToVoided(params.getBusinessId(), processInstId);
		return true;
	}
	/**
	 * 更新行政报销状态为作废
	 */
	private boolean updateFMPToVoided(String businessId,String processInstId){
		logger.info("更新行政审批状态为作废状态，id为"+businessId);
		FinanceManagementPayDtlVo fmp=queryFMPtById(businessId);
		//将主表的状态设为作废
		String mainId = fmp.getMainId();
		FinanceMain fm = financeMainDao.queryFinanceMainByFid(mainId);
//		fm.setStatus("作废");
		fm.setStatus(FinStatusEnum.FIN_ABOLISH.toString());
		financeMainDao.updateFinanceMainByFid(fm);
		ChangeStatusUtil.changeSToValue(fmp, VOIDCODE);
		financeManagementPayDao.updateFMP(fmp);
		return true;
	}
	/**
	 * 更新行政报销状态为审批中
	 */
	@Override
	public int updateFMPApproving(FinanceManagementPayDtlVo fmp) {
		logger.info("变更行政审批"+fmp.getId()+" 状态为审批中");
		InitUserAndSiteIdNewUtil.initUpdate(fmp, itcMvcService);
		ChangeStatusUtil.changeSToValue(fmp, APPROVINGCODE);
		financeManagementPayDao.updateFMP(fmp);
		return 0;
	}
	/**
	 * 更新行政报销状态为审批中
	 */
	@Override
	public int updateFMPApproving(String id) {
		logger.info("变更行政审批"+id+" 状态为审批中");
		FinanceManagementPayDtlVo fmp=queryFMPtById(id);
		updateFMPApproving(fmp);
		return 0;
	}
	/**
	 * 更新行政报销状态为审批通过
	 */
	@Override
	public int updateFMPApproved(FinanceManagementPayDtlVo fmp) {
		logger.info("变更行政审批"+fmp.getId()+" 状态为审批通过");
		InitUserAndSiteIdNewUtil.initUpdate(fmp, itcMvcService);
		ChangeStatusUtil.changeSToValue(fmp, APPROVEDCODE);
		financeManagementPayDao.updateFMP(fmp);
		return 0;
	}
	/**
	 * 更新行政报销状态为审批通过
	 */
	@Override
	public int updateFMPApproved(String id) {
		logger.info("变更行政审批"+id+" 状态为审批通过");
		FinanceManagementPayDtlVo fmp=queryFMPtById(id);
		updateFMPApproved(fmp);
		return 0;
	}
	/**
	 * 一组模糊查找的方法 查找供应商或用户或报销申请单
	 */
	@Override
	public List<Map<String, Object>> queryFuzzyByName(String name,String type) {
		//初始化参数userInfo
		UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
		List<Map<String, Object>> result=new ArrayList<Map<String,Object>>(0);
		if("supplier".equals(type)){
			//供应商的查询
			PurVendor purVendor=new PurVendor();
			purVendor.setName(name);
			Page<PurVendor> purVendors=new Page<PurVendor>(1,11);
			try {
				//根据供应商名称，模糊查询供应商信息
				purVendors=purVendorService.queryCompanyList((UserInfoScope) userInfo, purVendor);
			} catch (Exception e) {
				logger.error("根据名称："+name+"查询供应商信息时，出错",e);
			}//将Page数据转换为[{id:""},{name:""}]格式的数据
			List<PurVendor> originList=purVendors.getResults();
			if(purVendors!=null){
				for(int i=0;i<originList.size();i++){
					PurVendor purVendorTmp=originList.get(i);
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("id", purVendorTmp.getCompanyNo());
					map.put("name", purVendorTmp.getName());
					result.add(map);
				}
			}
		}else if("userinfo".equals(type)){
			//用户信息的查询-来自中华工单的方法
			Page<SecureUser> page = new Page<SecureUser>();
			page = new Page<SecureUser>();
			page.setPageSize(20);
			page.setParameter("userStatus", "Y");  //有效的用户
			page.setParameter("searchBy", name);
			page = authManager.retrieveUsersInAllSites(page);
			int size = page.getResults().size();
			if(size>11){
				size = 11;
			}
			List<SecureUser> users = page.getResults();
			for (int i = 0; i < size; i++) {
				Map<String, Object> map = new HashMap<String, Object>(0);
				map.put("id", users.get(i).getId());
				map.put("name", users.get(i).getName());
				result.add(map);
			}
		}else if("requestnote".equals(type)){
			//申请单的查询
			FinanceManagementApply fma = new FinanceManagementApply();
			fma.setName(name);
			Page<FinanceManagementApplyVo> fmas=new Page<FinanceManagementApplyVo>(1,11);
			try {
				fmas.setFuzzyParameter("name", name);
				//审批通过的申请单才显示
				fmas.setFuzzyParameter("status", "AE");
				//根据申请单名称，模糊查询申请单信息
				fmas=financeManagementApplyService.queryFinanceManagementApplyLsit(fmas);
			} catch (Exception e) {
				logger.error("根据名称："+name+"查询申请单信息时，出错",e);
			}//将Page数据转换为[{id:""},{name:""}]格式的数据
			List<FinanceManagementApplyVo> originList=fmas.getResults();
			if(fmas!=null){
				for(int i=0;i<originList.size();i++){
					FinanceManagementApplyVo fmaTmp=originList.get(i);
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("id", fmaTmp.getId());
					map.put("name", null==fmaTmp.getName()?"-":fmaTmp.getName());
					map.put("budget", fmaTmp.getBudget());
					map.put("subject", fmaTmp.getSubject());
					result.add(map);
				}
			}
		}
		return result;
	}
	
	 /**
	     * @description:
	     * @author: 王中华
	     * @createDate: 2015-9-21
	     * @param cat 枚举类型
	     * @param statusKey 枚举值
	     * @param siteid 站点
	     * @return:
	     */
	    private AppEnum getAppEnumObj(String cat, String statusKey, String siteid) {
	        List<AppEnum> tempList = iEnumManager.retrieveEnumeration( cat, statusKey );
	        AppEnum tempAppEnum = null;
	        for ( AppEnum temp : tempList ) {
	            if ( siteid.equals( temp.getSiteId() ) ) {
	                tempAppEnum = temp;
	            }
	        }
	        if ( tempAppEnum == null ) {
	            for ( AppEnum temp : tempList ) {
	                if ( "NaN".equals( temp.getSiteId() ) ) {
	                    tempAppEnum = temp;
	                }
	            }
	        }
	        return tempAppEnum;
	    }
}
