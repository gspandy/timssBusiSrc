package com.timss.finance.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timss.facade.util.AttachUtil;
import com.timss.facade.util.CreateReturnMapUtil;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.dao.FinanceWorkflowBusinessDao;
import com.timss.finance.service.FinanceManagementPayService;
import com.timss.finance.util.JsonUtil;
import com.timss.finance.util.ParsePrivilegeUtil;
import com.timss.finance.vo.FinanceManagementPayDtlVo;
import com.timss.pms.bean.FlowVoidParamBean;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.workflow.service.WorkflowService;

/**
 * @ClassName:FinanceexpensesController
 * @company: gdyd
 * @Description:报销模块 controller类-新
 * @author:    谷传伟
 * @date:   2015-4-22 上午9:55:00
 */
@Controller
@RequestMapping("finance/expenses")
public class FinanceManagementPayController {
	private static Logger logger=Logger.getLogger(FinanceManagementPayController.class);
	
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	FinanceManagementPayService financeManagementPayService;
	@Autowired
	FinanceWorkflowBusinessDao workflowBusinessDao;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	AttachmentMapper attachmentMapper;
	//转发方法组---开始
	/**
	 * @Title: addAdministractiveexpensesJsp
	 * @Description: 转发到新建行政报销
	 * @return
	 */
	@RequestMapping(value="addAdministractiveExpensesJsp")
	@ReturnEnumsBind("FIN_SUBJECT")
	public ModelAndView addAdministractiveexpensesJsp(){
		UserInfoScope infoScope = itcMvcService.getUserInfoScopeDatas();
		SecureUser user = infoScope.getSecureUser();
		String userId = user.getId();
		String userName = user.getName();
		Map<String, Object> map = new HashMap<String, Object>(0);
		map.put("userId", userId);
		map.put("userName", userName);
		map.put("defKey", "finance_itc_managementcostpay");
		ModelAndView modelAndView=new ModelAndView("fmp/addAdministractiveExpenses.jsp",map);
		return modelAndView;
	}
	/**
	 * @Title: editAdministractiveexpensesJsp
	 * @Description: 转发到编辑行政报销
	 * @return
	 */
	@RequestMapping(value="editAdministractiveExpensesJsp")
	@ReturnEnumsBind("FIN_SUBJECT")
	public ModelAndView editAdministractiveexpensesJsp(String id){
		Map<String, Object> map = new HashMap<String, Object>(0);
		map.put("fmpId", id);
		UserInfoScope infoScope = itcMvcService.getUserInfoScopeDatas();
		SecureUser user = infoScope.getSecureUser();
		String userId = user.getId();
		String userName = user.getName();
		map.put("userId", userId);
		map.put("userName", userName);
		map.put("defKey", "finance_itc_managementcostpay");
		ModelAndView modelAndView=new ModelAndView("fmp/editAdministractiveExpenses.jsp",map);
		return modelAndView;
	}
	/**
	 * @Title: selectNextStep
	 * @Description: 转发到下一步操作页面
	 * @return
	 */
	@RequestMapping(value="selectNextStep")
	public ModelAndView selectNextStep(String index){
		Map<String, Object> map = new HashMap<String, Object>(0);
		map.put("index", index);
		ModelAndView modelAndView=new ModelAndView("fmp/selectNextStep.jsp",map);
		return modelAndView;
	}
	//转发方法组---结束
	//从前端获取参数方法组--开始
	/**
	 * @Title: getContractFromBrower
	 * @Description: 从前端获取指定参数名数值
	 * @return
	 * @throws Exception
	 */
	private FinanceManagementPayDtlVo getExpansesFromBrower(String prefixMessage) throws Exception{
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String expensesString=userInfoScope.getParam("expenses");
		logger.info(prefixMessage+" 信息为："+expensesString);
		FinanceManagementPayDtlVo expenses=JsonHelper.fromJsonStringToBean(expensesString, FinanceManagementPayDtlVo.class);
		return expenses;
	}
	/**
	 * @Title: getFinanceMainDtlFromBrower
	 * @Description: 从前端获取报销详情
	 * @return
	 * @throws Exception
	 */
	private List<FinanceMainDetail> getFinanceMainDtlFromBrower(String prefixMessage) throws Exception{
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String expensesDtl=userInfoScope.getParam("expensesDtl");
		logger.info(prefixMessage+" 信息为："+expensesDtl);
		List<FinanceMainDetail> fmd=JsonUtil.fromJsonStringToList(expensesDtl, FinanceMainDetail.class);
		return fmd;
	}
	/**
	 * @Title: getParamFromBrower
	 * @Description: 从前端获取指定参数名数值
	 * @return
	 * @throws Exception
	 */
	private String getParamFromBrower(String prefixMessage) throws Exception{
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String paramString =userInfoScope.getParam(prefixMessage);
		logger.info(prefixMessage+" 信息为："+paramString);
		return paramString;
	}
	//从前端获取参数方法组--结束
	
	/**
	 * @Title: queryAdministrativeExpensesById
	 * @Description: 根据id，查询行政报销信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value="queryAdministrativeExpensesById",method=RequestMethod.POST)
	public ModelAndViewAjax queryAdministrativeExpensesById(String id){
		FinanceManagementPayDtlVo expenses=financeManagementPayService.queryFMPtByMainId(id);
		Map<String,Object> workflowMap=null;
		workflowMap = createFMPMap(expenses, null, null);
		Map<String,Object> priMap=ParsePrivilegeUtil.getFMPPrivilegeMap(expenses,workflowMap,itcMvcService);
		priMap.put("workflow", workflowMap);
		Map<String,Object> map=CreateReturnMapUtil.createMapWithPrivilege(CreateReturnMapUtil.SUCCESS_FLAG, "", expenses,priMap);
		return ViewUtil.Json(map);
	}
	/** @Title: deleteAdministrativeExpenses
	 * @Description: 删除行政报销
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("deleteAdministrativeExpenses")
	public ModelAndViewAjax deleteAdministrativeExpenses(String id) throws Exception{
		financeManagementPayService.deleteFMP(id);
		Map<String,Object> map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	/**
	 * @Title: tmpSaveAdministrativeExpenses
	 * @Description: 暂存行政报销审批信息--统一的设定 暂存不启动流程。
	 * @param bidId
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("tmpSaveAdministrativeExpenses")
	@ValidFormToken
	public ModelAndViewAjax tmpSaveAdministrativeExpenses() throws Exception{
		Map<String,Object> results = new HashMap<String,Object>(0);
		FinanceManagementPayDtlVo expenses=getExpansesFromBrower("行政报销信息：");
		String onlyAttach = getParamFromBrower("onlyAttach");
		if("true".equals(onlyAttach)){
			//仅附件的暂存
			FinanceManagementPayDtlVo fmp = financeManagementPayService.queryFMPtById(expenses.getId());
			fmp.setAttach(expenses.getAttach());
			results = financeManagementPayService.saveOrUpdateFMP(fmp,fmp.getFinanceMainDetails(),false);
			AttachUtil.bindAttach(attachmentMapper, null, expenses.getAttach());	
		}else{
			//一般暂存
			String id = expenses.getId();
			if(StringUtils.isNotEmpty(id)){
				expenses.setId(id);
			}
			List<FinanceMainDetail> expensesDtl=getFinanceMainDtlFromBrower("expensesDtl");
			//不启动流程的 保存
			results = financeManagementPayService.saveOrUpdateFMP(expenses,expensesDtl,false);
		}
		Map<String,Object> map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "",results);
		return ViewUtil.Json(map);
	}
	/**
	 * @Title: saveOrUpdateAdministrativeExpenses
	 * @Description: 提交行政报销审批信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="saveOrUpdateAdministrativeExpenses",method=RequestMethod.POST)
	@ValidFormToken
	public ModelAndViewAjax saveOrUpdateAdministrativeExpenses() throws Exception{
		FinanceManagementPayDtlVo expenses=getExpansesFromBrower("行政报销信息：");
		String id = expenses.getId();
		//如果有流程实例id，说明已经启动了工作流，那么就不需要启动工作流了
		String processInstId = getParamFromBrower("processInstId");
		boolean startWorkFlow =StringUtils.isNotEmpty(processInstId)?false:true;
		Map<String,Object> results = new HashMap<String,Object>(0);
		if(StringUtils.isNotEmpty(id)){
			expenses.setId(id);
		}
		List<FinanceMainDetail> expensesDtl=getFinanceMainDtlFromBrower("expensesDtl");
		results=financeManagementPayService.saveOrUpdateFMP(expenses,expensesDtl,startWorkFlow);
		Map<String,Object> map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "",results);
		return ViewUtil.Json(map);
	}
	/**
	 * @Title: voidFlowAdministrativeExpenses
	 * @Description: 行政报销审批流程作废
	 * @return
	 */
	@RequestMapping(value="/voidFlowAdministrativeExpenses")
	public ModelAndViewAjax voidFlowAdministrativeExpenses(String businessId,String processInstId,String taskId,String message){
		UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
		FlowVoidParamBean flowVoidParamBean=new FlowVoidParamBean(processInstId, taskId, message, userInfo.getUserId(),userInfo.getUserId(), userInfo, businessId);
		financeManagementPayService.voidFlow(flowVoidParamBean);
		Map<String,Object> map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	//**--不清楚有没有对应的现成的类存放相关的方法 暂时寄放在本controller
	@RequestMapping("/querySupplierFuzzyByName")
	public ModelAndViewAjax querySupplierFuzzyByName(String kw){
		logger.info("查询供应商通过名称："+kw);
		List<Map<String,Object>> result=financeManagementPayService.queryFuzzyByName(kw,"supplier");
		logger.info("成功查询供应商通过名称："+kw);
		return ViewUtil.Json(result);
	}
	@RequestMapping("/queryUserInfoFuzzyByName")
	public ModelAndViewAjax queryUserInfoFuzzyByName(String kw){
		logger.info("查询用户信息通过名称："+kw);
		List<Map<String,Object>> result=financeManagementPayService.queryFuzzyByName(kw,"userinfo");
		logger.info("成功查询用户信息通过名称："+kw);
		return ViewUtil.Json(result);
	}
	@RequestMapping("/queryRequestNoteFuzzyByName")
	public ModelAndViewAjax queryRequestNoteFuzzyByName(String kw){
		logger.info("查询申请单通过名称："+kw);
		List<Map<String,Object>> result=financeManagementPayService.queryFuzzyByName(kw,"requestnote");
		logger.info("成功查询申请单通过名称："+kw);
		return ViewUtil.Json(result);
	}
	
	
	
	private  boolean isExisted(String string){
		if(string==null || "".equals(string)){
			return false;
		}
		return true;
	}
	private  boolean inStringList(String string,List<String> list){
		boolean result=false;
		if(list!=null && string!=null){
			for(int i=0;i<list.size();i++){
				if(list.get(i).equals(string)){
					result=true;
					break;
				}
			}
		}
		return result;
	}
	private Map<String,Object> createWFMap(String businessId,String processInstId,String taskId){
		Map<String,Object> map=new HashMap<String,Object>();
		String isCandidate="false";
		String modifiable="";
		//获取节点信息
		Map<String,String> eMap=null;
		//对基础数据初始化
		//如果processInstId不存在，则根据businessId获取
		//如果taskId不存在，则根据processInstId获取
		if(!isExisted(processInstId)||!isExisted(taskId)){
			processInstId=workflowBusinessDao.queryWorkflowIdByBusinessId(businessId);
			if(StringUtils.isNotBlank(processInstId)){
				List<Task> tasks=workflowService.getActiveTasks(processInstId);
				if(tasks==null || tasks.size()==0){
					taskId=null;
				}else{
					//当存在会签节点是，需要另外处理
					taskId=tasks.get(0).getId();
				}
			}else{
				return map;
			}
		}
		//判断是否需要显示审批按钮
		if(taskId!=null){
			List<String> users=workflowService.getCandidateUsers(taskId);
			String currentUser=itcMvcService.getUserInfoScopeDatas().getUserId();
			if(inStringList(currentUser, users)){
				isCandidate="true";
				//获取节点信息
				eMap=workflowService.getElementInfo(taskId);
				modifiable=eMap.get("modifiable");
			}
		}
		map.put("processInstId", processInstId);
		map.put("taskId", taskId);
		map.put("isCandidate", isCandidate);
		map.put("elements", eMap);
		map.put("modifiable", modifiable);
		return map;
	}
	public Map<String,Object> createFMPMap(FinanceManagementPayDtlVo financeManagementPayDtlVo,
			String processInstId, String taskId) {
		String businessId=String.valueOf(financeManagementPayDtlVo.getId());
		Map<String,Object> map=createWFMap("adexpenses_"+businessId, processInstId, taskId);
		return map;
	}
}
