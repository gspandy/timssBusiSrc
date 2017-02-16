package com.timss.pms.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timss.pms.bean.FlowVoidParamBean;
import com.timss.pms.bean.Invoice;
import com.timss.pms.bean.Pay;
import com.timss.pms.service.PayQueryService;
import com.timss.pms.service.PayService;
import com.timss.pms.service.PrivilegeService;
import com.timss.pms.util.CreateReturnMapUtil;
import com.timss.pms.util.GetBeanFromBrowerUtil;
import com.timss.pms.vo.PayDtlVo;
import com.timss.pms.vo.PayVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;

@Controller
@RequestMapping("pms/pay")
public class PayController {
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	PayService payService;
	@Autowired
	PrivilegeService privilegeService;
	@Autowired
	PayQueryService payQueryService;
	Logger LOGGER=Logger.getLogger(this.getClass());
	
	@RequestMapping("/insertPayJsp")
	@ReturnEnumsBind(value="PMS_PAY_PAYWAY,PMS_CONTRACT_TYPE")
	public String insertPayJsp(){
		return "pay/addPay.jsp";
	}
	
	@RequestMapping("/payList")
	@ReturnEnumsBind(value="PMS_CONTRACT_TYPE,PMS_PAYPLAN_STAGE,PMS_STATUS")
	public String payList(){
		return "pay/payList.jsp";
	}
	@RequestMapping("/queryPayByContractId")
	public ModelAndViewAjax queryPayByContractId(String contractId){
		PayDtlVo payDtlVo=payService.queryPayByContractId(contractId);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "", payDtlVo);
		return ViewUtil.Json(map);
	}
	@RequestMapping("insertPay")
	@ValidFormToken
	public ModelAndViewAjax insertPay() throws Exception{
		Pay pay=GetBeanFromBrowerUtil.getBeanFromBrower("插入结算信息", "pay", Pay.class, itcMvcService);
		List<Invoice> invoices=GetBeanFromBrowerUtil.getBeanListFromBrower("插入发票信息", "invoice", Invoice.class, itcMvcService);
		
		payService.insertPay(pay,invoices);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	@RequestMapping("tmpInsertPay")
	@ValidFormToken
	public ModelAndViewAjax tmpInsertPay() throws Exception{
		Pay pay=GetBeanFromBrowerUtil.getBeanFromBrower("插入结算信息", "pay", Pay.class, itcMvcService);
		List<Invoice> invoices=GetBeanFromBrowerUtil.getBeanListFromBrower("插入发票信息", "invoice", Invoice.class, itcMvcService);
		Map pMap=payService.tmpInsertPay(pay,invoices);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "",pMap);
		return ViewUtil.Json(map);
	}
	
	@RequestMapping(value="/queryPayByPayplanId")
	public ModelAndViewAjax queryPayByPayplanId(String contractId,String payplanId,String id){
		
		PayDtlVo payDtlVo=null;
		if(StringUtils.isNotBlank(id)){
			payDtlVo=payService.queryPayById(id);
		}else{
			payDtlVo=payService.queryPayByPayplanId(payplanId, contractId);
		}
		
		Map m=privilegeService.createPayPrivilege(payDtlVo, itcMvcService);
		Map map=CreateReturnMapUtil.createMapWithPrivilege(CreateReturnMapUtil.SUCCESS_FLAG, "", payDtlVo,m);
		return ViewUtil.Json(map);
	}
	
	@RequestMapping(value = "/editPayJsp")
	@ReturnEnumsBind(value="PMS_PAY_PAYWAY,PMS_CONTRACT_TYPE")
	public ModelAndView editCheckoutJsp(String id) {
		ModelAndView modelAndView=new ModelAndView("pay/editPay.jsp");
		return modelAndView;
	}
	
	@RequestMapping("/tmpUpdatePay")
	@ValidFormToken
	public ModelAndViewAjax tmpUpdatePay() throws Exception{
		Pay pay=GetBeanFromBrowerUtil.getBeanFromBrower("暂存的结算信息", "pay", Pay.class, itcMvcService);
		List<Invoice> invoices=GetBeanFromBrowerUtil.getBeanListFromBrower("插入发票信息", "invoice", Invoice.class, itcMvcService);
		payService.tmpUpdatePay(pay,invoices);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	@RequestMapping("/deletePay")
	public ModelAndViewAjax deletePay(String id) throws Exception{
	
		payService.deletePay(id);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	/**
	 * 终止收付款流程
	 * @Title: stopWorkflow
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("stopWorkflow")
	public ModelAndViewAjax stopWorkflow(String processInstId,String reason) throws Exception {
		Pay pay=GetBeanFromBrowerUtil.getBeanFromBrower("终止收付款流程信息", "pay", Pay.class, itcMvcService);
		payService.stopWorkflow(pay,processInstId,reason);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	/**
         * @Title: stopUndoWorkflow
         * @Description: 终止退票流程
         * @return
         * @throws Exception
         */
        @RequestMapping("stopUndoWorkflow")
        public ModelAndViewAjax stopUndoWorkflow(String processInstId,String reason) throws Exception {
                Pay pay=GetBeanFromBrowerUtil.getBeanFromBrower("终止退票流程信息", "pay", Pay.class, itcMvcService);
                //这个方法中没有实质的内容
                payService.stopWorkflow(pay,processInstId,reason);
                Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
                return ViewUtil.Json(map);
        }
	
	
	/**
	 * 查询结算列表
	 * @Title: payListData
	 * @Description: 
	 * @return: Page<PayVo> 结算信息及分页信息
	 * @throws
	 */
	@RequestMapping(value = "/payListData", method = RequestMethod.POST)
	public Page<PayVo> payListData() throws Exception {
        
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		Page<PayVo> page = userInfoScope.getPage();

		// 查询参数处理
		Map<String, String[]> params = userInfoScope.getParamMap();
		// 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
	
		HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap("payVoMap", "pms", "PayDao");
		

		
		if (params.containsKey("search")) {
			String fuzzySearchParams = userInfoScope.getParam("search");
			LOGGER.info("查询合同列表的条件：" + fuzzySearchParams);

			// 调用工具类将jsonString转为HashMap
			Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap(fuzzySearchParams);
			//处理此处的状态枚举
			Object statusNameObj = fuzzyParams.get( "statusName" );		
			fuzzyParams.remove( "statusName" );
			if(null!=statusNameObj&&!"".equals( statusNameObj )){
			    List<AppEnum> statusEnums=itcMvcService.getEnum("PMS_STATUS");
			    AppEnum appEnum = new AppEnum();
	                    appEnum.setCategoryCode( "PMS_STATUS" );
	                    appEnum.setCode( "undoing" );
	                    appEnum.setLabel( "退票中" );
	                    appEnum.setSiteId( "ITC" );
	                    statusEnums.add( appEnum );
	                    String status = "invalid";
	                    for ( AppEnum appEnumTmp : statusEnums ) {
                                if(appEnumTmp.getLabel().equals( statusNameObj.toString() )){
                                    status = appEnumTmp.getCode();
                                }
                            }
	                    fuzzyParams.put( "status", status );
			}
			// 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
			fuzzyParams = MapHelper.fromPropertyToColumnMap((HashMap<String, Object>) fuzzyParams,
					propertyColumnMap);
			
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
		page = payQueryService.queryPayListAndFilter(page, userInfoScope);
		return page;
	}
	
	/**
	 * 流程作废
	 * @Title: voidFlow
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return
	 */
	@RequestMapping(value="/voidFlow")
	public ModelAndViewAjax voidFlow(String businessId,String processInstId,String taskId,String message){
		UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
		FlowVoidParamBean flowVoidParamBean=new FlowVoidParamBean(processInstId, taskId, message, userInfo.getUserId(),
				userInfo.getUserId(), userInfo, businessId);
		payService.voidFlow(flowVoidParamBean);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	/**
         * @Title: voidUndoFlow
         * @Description: 作废退票流程
         * @return
         */
        @RequestMapping(value="/voidUndoFlow")
        public ModelAndViewAjax voidUndoFlow(String businessId,String processInstId,String taskId,String message){
                UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
                FlowVoidParamBean flowVoidParamBean=new FlowVoidParamBean(processInstId, taskId, message, userInfo.getUserId(),
                                userInfo.getUserId(), userInfo, businessId);
                payService.voidUndoFlow(flowVoidParamBean);
                Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
                return ViewUtil.Json(map);
        }
	/**
	 * 
	 * @Title:applyUndo
	 * @Description:申请退票
	 * @throws Exception
	 * @return ModelAndViewAjax
	 */
	@RequestMapping(value="/applyUndo",method=RequestMethod.POST)
        public ModelAndViewAjax applyUndo() throws Exception{
	    UserInfoScope infoScope = itcMvcService.getUserInfoScopeDatas();
	    Map<String, Object> results = new HashMap<String, Object>(0);
	    String id = infoScope.getParam("id");
            String undoFlowId = infoScope.getParam("undoFlowId");
            String undoRemark = infoScope.getParam("undoRemark");
            Pay pay = new Pay();
            pay.setId( Integer.parseInt( id ) );
            pay.setUndoRemark( undoRemark );
            if ( StringUtils.isEmpty( undoFlowId ) ) {
                //启动退票流程
                results = payService.startUndoFlow( pay );
            }else {
                //更新退票信息
                results = payService.updateUndoInfo( pay );
            }
            results.put( "undoFlowId", results.get( "processInstId" ) );
            Map<String,Object> map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "",results);
            return ViewUtil.Json(map);
        }
}
