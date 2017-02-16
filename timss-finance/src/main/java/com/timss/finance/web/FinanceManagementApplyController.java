package com.timss.finance.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.timss.facade.util.CreateReturnMapUtil;
import com.timss.facade.util.GetBeanFromBrowerUtil;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.service.FinanceManagementApplyService;
import com.timss.finance.service.FinanceManagementApplySpecialService;
import com.timss.finance.util.ParsePrivilegeUtil;
import com.timss.finance.vo.FinanceManagementApplyDtlVo;
import com.timss.finance.vo.FinanceManagementApplyVo;
import com.timss.pms.bean.FlowVoidParamBean;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.workflow.service.WorkflowService;

@Controller
@RequestMapping(value="finance/fma")
public class FinanceManagementApplyController {
	@Autowired
	FinanceManagementApplyService financeManagementApplyService;
	@Autowired
	FinanceManagementApplySpecialService finaceManagementApplySpecialService;
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	
	Logger logger=Logger.getLogger(FinanceManagementApplyController.class);
	
	@RequestMapping("/editFMAJsp")
	@ReturnEnumsBind("FIN_SUBJECT,FIN_APPLY_TYPE,FIN_ALLOWANCE_TYPE,FIN_FLOW_TYPE,FIN_VEHICLE")
	public ModelAndView editFMAJsp() throws Exception{
	    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
	    String id = userInfoScope.getParam("id");
	    String applyType = userInfoScope.getParam("applyType");
	   
	    Map<String, Object> map=new HashMap<String, Object>();
	    map.put("id",id);
	    map.put("applyType",applyType);
	    ModelAndView result  = null;
	   if(applyType != null){
	       if( "managementcostapply".equals( applyType) ){
	            result = new ModelAndView("fma/editFMA.jsp",map);
	       }else if("travelapply".equals( applyType)){
	            result = new ModelAndView("fma/addTravelapply.jsp",map);
	       }else if("businessentertainment".equals( applyType)){
	           result = new ModelAndView("fma/addbusientertainapply.jsp",map);
	       }
	   }
	    return result;
	}
	
	@RequestMapping("/addFMAJsp")
	@ReturnEnumsBind("FIN_SUBJECT,FIN_APPLY_TYPE,FIN_ALLOWANCE_TYPE,FIN_FLOW_TYPE,FIN_VEHICLE")
	public ModelAndView addFMAJsp() throws Exception{
		Map<String, Object> valueMap=new HashMap<String, Object>();
		UserInfoScope userInfo=itcMvcService.getUserInfoScopeDatas();
		valueMap.put("applyuserid", userInfo.getUserId());
		valueMap.put("applyname", userInfo.getUserName());
		valueMap.put("deptid", userInfo.getOrgId());
		valueMap.put("deptname", userInfo.getOrgName());
		
		String applyType = userInfo.getParam( "applyType" );
		String urlString = "fma/addFMA.jsp";  //管理费用申请
		if(applyType.equals( "travelapply" )){  //出差申请
		    urlString = "fma/addTravelapply.jsp";
		}else if(applyType.equals( "businessentertainment" )){
		    urlString = "fma/addbusientertainapply.jsp";
		}
		return new ModelAndView(urlString,valueMap);
	}
	
	@RequestMapping("/FMAListJsp")
	@ReturnEnumsBind("FIN_SUBJECT,FIN_APPLY_TYPE")
	public String FMAListJsp(){
		return "fma/FMAList.jsp";
	}
	/**
	 * @description:出差申请列表
	 * @author: 王中华
	 * @createDate: 2016-11-15
	 * @return:
	 */
	@RequestMapping("/travelListJsp")
        @ReturnEnumsBind("FIN_SUBJECT,FIN_APPLY_TYPE,FIN_ALLOWANCE_TYPE")
        public String travelListJsp(){
                return "fma/travelApplyList.jsp";
        }
	
	@RequestMapping("/tmpInsertFinanceManagementApply")
	@ValidFormToken
	public ModelAndViewAjax tmpInsertFinanceManagementApply() throws Exception{
		FinanceManagementApply financeManagementApply=GetBeanFromBrowerUtil.getBeanFromBrower("暂存时获取行政报销详细信息", "financeManagementApply", FinanceManagementApply.class, itcMvcService);
		List<FinanceMainDetail> financeMainDetails=GetBeanFromBrowerUtil.getBeanListFromBrower("暂存时获取报销明细", "details", FinanceMainDetail.class, itcMvcService);
		String id=finaceManagementApplySpecialService.tmpInsertFinanceManagementApply(financeManagementApply,financeMainDetails);
		@SuppressWarnings("unchecked")
		Map<String, Object> map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "", id);
		return ViewUtil.Json(map);
	}
	
	@RequestMapping("/insertFinanceManagementApplyAndStartWorkflow")
	@ValidFormToken
	public ModelAndViewAjax insertFinanceManagementApplyAndStartWorkflow() throws Exception{
		FinanceManagementApply financeManagementApply=GetBeanFromBrowerUtil.getBeanFromBrower("", "financeManagementApply", FinanceManagementApply.class, itcMvcService);
		List<FinanceMainDetail> financeMainDetails=GetBeanFromBrowerUtil.getBeanListFromBrower("暂存时获取报销明细", "details", FinanceMainDetail.class, itcMvcService);
		Map<String, Object> data=finaceManagementApplySpecialService.insertFinanceManagementApplyAndStartWorkflow(financeManagementApply,financeMainDetails);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "", data);
		return ViewUtil.Json(map);
	}
	
	@RequestMapping("/updateFinanceManagementApplyAndStartWorkflow")
	@ValidFormToken
	public ModelAndViewAjax updateFinanceManagementApplyAndStartWorkflow() throws Exception{
		FinanceManagementApply financeManagementApply=GetBeanFromBrowerUtil.getBeanFromBrower("", "financeManagementApply", FinanceManagementApply.class, itcMvcService);
		List<FinanceMainDetail> financeMainDetails=GetBeanFromBrowerUtil.getBeanListFromBrower("暂存时获取报销明细", "details", FinanceMainDetail.class, itcMvcService);
		Map<String, Object> data=finaceManagementApplySpecialService.updateFinanceManagementApplyAndStartWorkflow(financeManagementApply,financeMainDetails);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "", data);
		return ViewUtil.Json(map);
	}
	
	@RequestMapping("/tmpUpdateFinanceManagementApply")
	public ModelAndViewAjax tmpUpdateFinanceManagementApply() throws Exception{
		FinanceManagementApply financeManagementApply=GetBeanFromBrowerUtil.getBeanFromBrower("", "financeManagementApply", FinanceManagementApply.class, itcMvcService);
		List<FinanceMainDetail> financeMainDetails=GetBeanFromBrowerUtil.getBeanListFromBrower("暂存时获取报销明细", "details", FinanceMainDetail.class, itcMvcService);
		String id=finaceManagementApplySpecialService.tmpUpdateFinanceManagementApply(financeManagementApply,financeMainDetails);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "", id);
		return ViewUtil.Json(map);
	}
	
	@RequestMapping(value = "/queryFinanceManagementApplyListData")
	public Page<FinanceManagementApplyVo> queryFinanceManagementApplyListData(String id,String search) throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		Page<FinanceManagementApplyVo> page = userInfoScope.getPage();
		// 查询参数处理
		Map<String, String[]> params = userInfoScope.getParamMap();
		// 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
		HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap("financeManagementApplyMap", "finance", "FinanceManagementApplyDao");
		if (params.containsKey("search")) {
			String fuzzySearchParams = userInfoScope.getParam("search");
			logger.info("查询管理费用的条件：" + fuzzySearchParams);

			// 调用工具类将jsonString转为HashMap
			Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap(fuzzySearchParams);
					

			// 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
			fuzzyParams = MapHelper.fromPropertyToColumnMap((HashMap<String, Object>) fuzzyParams,
					propertyColumnMap);
			
			// 自动的会封装模糊搜索条件
			page.setFuzzyParams(fuzzyParams);

		}

		// 设置排序内容
		if (params.containsKey("sort")) {
			String sortKey = userInfoScope.getParam("sort");

			// 如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
			sortKey = propertyColumnMap.get(sortKey);

			page.setSortKey(sortKey);
			page.setSortOrder(userInfoScope.getParam("order"));
		} else {
			// 设置默认的排序字段
			page.setSortKey("id");
			page.setSortOrder("desc");
		}
		page = financeManagementApplyService.queryFinanceManagementApplyLsit(page);
		return page;
	}
	
	
	
	@RequestMapping("/queryApplyInfoFuzzyByName")
        public ModelAndViewAjax queryApplyInfoFuzzyByName(String kw){
                logger.info("查询申请单通过名称："+kw);
                List<Map<String,Object>> result=financeManagementApplyService.queryApplyInfoFuzzyByName(kw,"requestnote");
                logger.info("成功查询申请单通过名称："+kw);
                return ViewUtil.Json(result);
        }
	
	@RequestMapping("/queryFinanceManagementApplyById")
	public ModelAndViewAjax queryFinanceManagementApplyById(String id){
		FinanceManagementApplyDtlVo financeManagementApplyDtlVo=financeManagementApplyService.queryFinanceManagementApplyById(id);
		Map<String,Object> priMap=ParsePrivilegeUtil.getFMAPrivilegeMap(financeManagementApplyDtlVo,itcMvcService.getUserInfoScopeDatas(),workflowService);
		@SuppressWarnings("unchecked")
		Map<String , Object> map=CreateReturnMapUtil.createMapWithPrivilege(CreateReturnMapUtil.SUCCESS_FLAG, "", financeManagementApplyDtlVo,priMap);
		return ViewUtil.Json(map);
	}
	
	@RequestMapping("/setVariables")
	public ModelAndViewAjax setVariables(String processInstId,String taskId){
		FinanceManagementApply fma=GetBeanFromBrowerUtil.getBeanFromBrower("", "financeManagementApply", FinanceManagementApply.class, itcMvcService);
		finaceManagementApplySpecialService.setWFVariable(taskId, processInstId, fma);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	@RequestMapping("/delFMAById")
	public ModelAndViewAjax delFMAById(String id){
		
		finaceManagementApplySpecialService.deleteFinanceManagementApply(id);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	@RequestMapping("/voidFlow")
	public ModelAndViewAjax voidFlow(String businessId,String processInstId,String taskId,String message){
		UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
		FlowVoidParamBean flowVoidParamBean=new FlowVoidParamBean(processInstId, taskId, message, userInfo.getUserId(),
				userInfo.getUserId(), userInfo, businessId);
		finaceManagementApplySpecialService.voidFlow(flowVoidParamBean);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	/**
	 * @description:不弹出审批框直接结束流程
	 * @author: 杨坤
	 * @createDate: 2016-12-30
	 * @return:
	 */
	@RequestMapping("/endFlow")
	public ModelAndViewAjax endFlow(String businessId,String processInstId,String taskId,String message){
		UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
		FlowVoidParamBean flowVoidParamBean=new FlowVoidParamBean(processInstId, taskId, message, userInfo.getUserId(),
				userInfo.getUserId(), userInfo, businessId);
		finaceManagementApplySpecialService.endFlow(flowVoidParamBean);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
}
