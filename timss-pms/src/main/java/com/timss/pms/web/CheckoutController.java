package com.timss.pms.web;



import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timss.pms.bean.Checkout;
import com.timss.pms.bean.FlowVoidParamBean;
import com.timss.pms.service.CheckoutQueryService;
import com.timss.pms.service.CheckoutService;
import com.timss.pms.service.ContractService;
import com.timss.pms.service.PrivilegeService;
import com.timss.pms.util.CreateReturnMapUtil;
import com.timss.pms.vo.CheckoutDtlVo;
import com.timss.pms.vo.CheckoutVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;

/**
 * 项目验收controller
 * @ClassName:     CheckoutController
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2014-7-22 上午10:35:36
 */
@Controller
@RequestMapping(value="pms/checkout")
public class CheckoutController {
	
	@Autowired
	CheckoutService checkoutService;
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	@Autowired
	PrivilegeService privilegeService;
	@Autowired
	CheckoutQueryService checkoutQueryService;
	
	Logger LOGGER=Logger.getLogger(CheckoutController.class);
	
	/**
	 * 转发到新建验收信息页面
	 * @Title: insertCheckoutJsp
	 * @return
	 */
	@RequestMapping(value = "/insertCheckoutJsp",method=RequestMethod.GET)
	@ReturnEnumsBind(value="PMS_CHECKOUT_TYPE")
	public ModelAndView insertCheckoutJsp(String contractId){
		ModelAndView modelAndView=new ModelAndView("checkout/addCheckout.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/queryCheckoutByContractId",method=RequestMethod.GET)
	public ModelAndView queryCheckoutByContractId(String contractId){
		
		Map map=new HashMap();
		CheckoutDtlVo checkoutDtlVo=checkoutService.queryCheckoutByContractId(contractId, null);
		map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "", checkoutDtlVo);
		
		
		return ViewUtil.Json(map);
	}
	
	/**
	 * 转发到编辑项目验收页面
	 * @Title: editCheckoutJsp
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/editCheckoutJsp")
	@ReturnEnumsBind(value="PMS_CHECKOUT_TYPE")
	public ModelAndView editCheckoutJsp(String id) {
		ModelAndView modelAndView=new ModelAndView("checkout/editCheckout.jsp");
		return modelAndView;
	}
	
	/**
	 * 转发到验收列表页面
	 * @Title: editCheckoutJsp
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/checkoutList")
	@ReturnEnumsBind(value="PMS_CHECKOUT_TYPE,PMS_PAYPLAN_STAGE,PMS_STATUS")
	public String checkoutList(String id) {
		return "checkout/checkoutList.jsp";
	}
	
	/**
	 * 插入验收信息
	 * @Title: addCheckout
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/insertCheckout")
	@ReturnEnumsBind(value="PMS_CHECKOUT_TYPE")
	@ValidFormToken
	public ModelAndViewAjax insertCheckout() throws Exception{
		Checkout checkout=getCheckoutFromBrower("插入验收：");
		checkoutService.insertCheckOut(checkout);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	/**
	 * 暂存验收信息，并提交流程
	 * @Title: tmpInsertCheckout
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/tmpInsertCheckout")
	@ValidFormToken
	public ModelAndViewAjax tmpInsertCheckout() throws Exception{
		Checkout checkout=getCheckoutFromBrower("插入验收：");
		Map iMap=checkoutService.tmpInsertCheckout(checkout);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "",iMap);
		return ViewUtil.Json(map);
	}
	
	/**
	 * 暂存验收信息
	 * @Title: tmpUpdateCheckout
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/tmpUpdateCheckout")
	@ValidFormToken
	public ModelAndViewAjax tmpUpdateCheckout() throws Exception{
		Checkout checkout=getCheckoutFromBrower("暂存验收：");
		checkoutService.tmpUpdateCheckout(checkout);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	

	/**
	 * 删除验收记录
	 * @Title: deleteCheckout
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deleteCheckout")
	public ModelAndViewAjax deleteCheckout(String id) throws Exception{
		
		checkoutService.deleteCheckout(id);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	/**
	 * 根据结算id,获取验收的详细信息
	 * @Title: queryCheckoutByContractId
	 * @param contractId
	 * @param payplanId
	 * @return
	 */
	@RequestMapping(value="/queryCheckoutByContractId")
	public ModelAndViewAjax queryCheckoutByContractId(String contractId,String payplanId){
		
		CheckoutDtlVo checkoutDtlVo=checkoutService.queryCheckoutByContractId(contractId, null);
		Map m=privilegeService.createCheckoutPrivilege(checkoutDtlVo, itcMvcService);
		Map map=CreateReturnMapUtil.createMapWithPrivilege(CreateReturnMapUtil.SUCCESS_FLAG, "", checkoutDtlVo,m);
		return ViewUtil.Json(map);
	}
	
	/**
	 * 根据合同id,获取验收的详细信息
	 * @Title: queryCheckoutByPayplanId
	 * @param contractId
	 * @param payplanId
	 * @return
	 */
	@RequestMapping(value="/queryCheckoutByPayplanId")
	public ModelAndViewAjax queryCheckoutByPayplanId(String contractId,String payplanId,String id){
		
		CheckoutDtlVo checkoutDtlVo=null;
		if(StringUtils.isNotBlank(id)){
			checkoutDtlVo=checkoutService.queryCheckoutById(id);
		}else{
			checkoutDtlVo=checkoutService.queryCheckoutByPayplanId(payplanId, contractId);
		}
		Map m=privilegeService.createCheckoutPrivilege(checkoutDtlVo, itcMvcService);
		Map map=CreateReturnMapUtil.createMapWithPrivilege(CreateReturnMapUtil.SUCCESS_FLAG, "", checkoutDtlVo,m);
		return ViewUtil.Json(map);
	}
	/**
	 * 解析从前天传来的验收信息，变为checkout类
	 * @Title: getCheckoutFromBrower
	 * @param prefix
	 * @return
	 * @throws Exception
	 */
	private Checkout getCheckoutFromBrower(String prefix) throws Exception{
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String checkoutString=userInfoScope.getParam("checkout");
		LOGGER.info(prefix+" 验收信息："+checkoutString);
		Checkout checkout=JsonHelper.fromJsonStringToBean(checkoutString, Checkout.class);
		return checkout;
	}
	
	/**
	 * 终止项目验收审批流程
	 * @Title: stopWorkflow
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("stopWorkflow")
	public ModelAndViewAjax stopWorkflow(String processInstId,String reason) throws Exception {
		Checkout checkout=getCheckoutFromBrower("终止项目验收信息：");
		checkoutService.stopWorkflow(checkout,processInstId,reason);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	/**
	 * 查询验收信息
	 * @Title: checkoutListData
	 * @Description: 
	 * @return: Page<CheckoutVo> 验收信息及分页信息
	 * @throws
	 */
	@RequestMapping(value = "/checkoutListData", method = RequestMethod.POST)
	public Page<CheckoutVo> checkoutListData() throws Exception {
        
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		Page<CheckoutVo> page = userInfoScope.getPage();

		// 查询参数处理
		Map<String, String[]> params = userInfoScope.getParamMap();
		// 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
	
		HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap("checkoutVoMap", "pms", "CheckoutDao");
		

		
		if (params.containsKey("search")) {
			String fuzzySearchParams = userInfoScope.getParam("search");
			LOGGER.info("查询合同列表的条件：" + fuzzySearchParams);

			// 调用工具类将jsonString转为HashMap
			Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap(fuzzySearchParams);
					

			// 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
			fuzzyParams = MapHelper.fromPropertyToColumnMap((HashMap<String, Object>) fuzzyParams,
					propertyColumnMap);
			String timestr = String.valueOf( fuzzyParams.get( "time" ) );
			if ( StringUtils.isNotEmpty( timestr ) ) {
			    fuzzyParams.put( "timestr", timestr );
			    fuzzyParams.remove( "time" );
                        }
			
			// 自动的会封装模糊搜索条件
			page.setFuzzyParams(fuzzyParams);

		}

		// 设置排序内容
		if (params.containsKey("sort")) {
    		        String sortKey = userInfoScope.getParam("sort");
                        if(StringUtils.isNotEmpty( sortKey ) &&sortKey.contains( "statusName" )){
                            sortKey = sortKey.replace( "statusName", "status" );
                        }
                        // 如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
                        propertyColumnMap.put( "status", "status" );
                        sortKey = propertyColumnMap.get(sortKey);
    
                        page.setSortKey(sortKey);
                        page.setSortOrder(userInfoScope.getParam("order"));
		} else {
			// 设置默认的排序字段
			page.setSortKey("id");
			page.setSortOrder("desc");
		}
		page = checkoutQueryService.queryCheckoutListAndFilter(page, userInfoScope);
		return page;
	}
	
	/**
	 * 流程作废
	 * @Title: voidFlow
	 * @return
	 */
	@RequestMapping(value="/voidFlow")
	public ModelAndViewAjax voidFlow(String businessId,String processInstId,String taskId,String message){
		UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
		FlowVoidParamBean flowVoidParamBean=new FlowVoidParamBean(processInstId, taskId, message, userInfo.getUserId(),
				userInfo.getUserId(), userInfo, businessId);
		checkoutService.voidFlow(flowVoidParamBean);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
}
