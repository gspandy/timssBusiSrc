package com.timss.pms.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timss.pms.bean.Contract;
import com.timss.pms.bean.FlowVoidParamBean;
import com.timss.pms.bean.Payplan;
import com.timss.pms.bean.PayplanTmp;
import com.timss.pms.service.ContractQueryService;
import com.timss.pms.service.ContractService;
import com.timss.pms.service.PayplanService;
import com.timss.pms.service.PayplanTmpService;
import com.timss.pms.service.PrivilegeService;
import com.timss.pms.util.CreateReturnMapUtil;
import com.timss.pms.util.JsonUtil;
import com.timss.pms.vo.ContractDtlVo;
import com.timss.pms.vo.ContractVo;
import com.timss.pms.vo.PayplanTmpVo;
import com.timss.pms.vo.PayplanVo;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.workflow.service.WorkflowService;

/**
 * 合同模块 controller类
 * @ClassName:     ContractController
 * @company: gdyd

 * @author:    黄晓岚
 * @date:   2014-7-7 下午4:51:00
 */
@Controller
@RequestMapping("pms/contract")
public class ContractController {
	private static final Logger LOGGER=Logger.getLogger(ContractController.class);
	@Autowired
	ContractService contractService;
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	PrivilegeService privilegeService;
	@Autowired
	PayplanTmpService payplanTmpService;
	@Autowired
	ContractQueryService contractQueryService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
        PayplanService payplanService;
	@Autowired
	HomepageService homepageService;
	/**
	 * 转发请求的合同新建页面
	 * @Title: addContractJsp
	 * @return
	 */
	@RequestMapping(value="addContractJsp")
	@ReturnEnumsBind(value="PMS_CONTRACT_TYPE,PMS_PAYPLAN_STAGE,PMS_CONTRACT_CATEGORY,PMS_CONTRACT_BELONGTO")
	public ModelAndView addContractJsp(){
	    UserInfoScope infoScope = itcMvcService.getUserInfoScopeDatas();
	    Map<String, Object> map = new HashMap<String, Object>(0);
	    SecureUser manager = privilegeService.getManagerInfo( infoScope );
            if(null != manager){
                map.put( "managerName", manager.getName() );
                map.put( "managerTel", !"".equals( manager.getMobile() )?manager.getMobile():manager.getOfficeTel() );    
            }
	    ModelAndView modelAndView=new ModelAndView("/contract/addContract.jsp",map);
            return modelAndView;
	}
	
	/**
	 * 转发到编辑合同页面
	 * @Title: editContractJsp
	 * @return
	 */
	@RequestMapping(value="editContractJsp")
	@ReturnEnumsBind(value="PMS_CONTRACT_TYPE,PMS_PAYPLAN_STAGE,PMS_CONTRACT_CATEGORY,PMS_CONTRACT_BELONGTO")
	public ModelAndView editContractJsp(){
		Map<String, Object> map = new HashMap<String, Object>(0);
    	        UserInfoScope infoScope = itcMvcService.getUserInfoScopeDatas();
		SecureUser user = infoScope.getSecureUser();
		String userId = user.getId();
		map.put("userId", userId);
		SecureUser manager = privilegeService.getManagerInfo( infoScope );
	        if(null != manager){
	            map.put( "managerName", manager.getName() );
	            map.put( "managerTel", !"".equals( manager.getMobile() )?manager.getMobile():manager.getOfficeTel() );    
	        }
	        try {
                    map.put("contractId",infoScope.getParam( "contractId" ));
                } catch (Exception e) {
                    LOGGER.error( "editContractJsp获取参数异常", e );
                }
		ModelAndView modelAndView=new ModelAndView("/contract/editContract.jsp",map);
		return modelAndView;
	}
	
	/**
	 * 转发到合同列表页面
	 * @Title: editContractJsp
	 * @return
	 */
	@RequestMapping(value="contractList")
	@ReturnEnumsBind(value="PMS_CONTRACT_TYPE,PMS_PAYPLAN_STAGE,PMS_CONTRACT_CATEGORY,PMS_CONTRACT_BELONGTO,PMS_PROJECT_SUBCOMP,PMS_STATUS")
	public String contractList(){
		return "contract/contractList.jsp";
	}
	
	/**
	 * 根据合同id，查询合同详细信息
	 * @Title: queryContractById
	 * @param id
	 * @return
	 */
	@RequestMapping(value="queryContractById",method=RequestMethod.POST)
	public ModelAndViewAjax queryContractById(String id){
		ContractDtlVo contractDtlVo=contractService.queryContractById(id);
		//priMap 获得了权限 以及 结算计划更改的流程信息
		Map priMap=privilegeService.createContractPrivilege(contractDtlVo, itcMvcService);
		Map map=CreateReturnMapUtil.createMapWithPrivilege(CreateReturnMapUtil.SUCCESS_FLAG, "", contractDtlVo,priMap);
		return ViewUtil.Json(map);
	}
	
	/**
	 * 插入合同信息
	 * @Title: insertContract
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="insertContract",method=RequestMethod.POST)
	@ValidFormToken
	public ModelAndViewAjax insertContract() throws Exception{
		Contract contract=getContractFromBrower("合同信息：");
		List<Payplan> payplans=getPayplanListFromBrower("payplans");
		HashMap<String, Object> results=contractService.insertContract(contract,payplans);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "",results);
		UserInfoScope userInfoScopeInfo = itcMvcService.getUserInfoScopeDatas();
		String taskId = userInfoScopeInfo.getParam("taskId");
		String toComplete = userInfoScopeInfo.getParam("toComplete");
		if ( "true".equals( toComplete )&&StringUtils.isNotEmpty( taskId ) ) {
                    workflowService.complete(taskId, userInfoScopeInfo.getUserId(), null, null, "已执行", true);
                }
		return ViewUtil.Json(map);
	}
	@RequestMapping(value="changePayList")
	public ModelAndViewAjax changePayList() throws Exception{
		Contract contract=getContractFromBrower("变更结算计划时合同信息：");
		List<PayplanTmp> payplans=getPayplanTmpListFromBrower("payplans");
		Map cMap=contractService.changePayList(contract, payplans);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "",cMap);
		return ViewUtil.Json(map);
	}
	
	@RequestMapping(value="getPayplanTmpListByFlowId")
	public ModelAndViewAjax getPayplanTmpListByFlowId(String flowId){
		List<PayplanTmpVo> payplanTmpVos=payplanTmpService.queryPayplanTmpByFlowId(flowId);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "",payplanTmpVos);
		return ViewUtil.Json(map);
	}
	
	/**
	 * 根据招标id和项目id，查询新建合同时需要的相关信息
	 * @Title: queryContractByBidId
	 * @param bidId
	 * @param projectId
	 * @return
	 */
	@RequestMapping("queryContractByBidId")
	public ModelAndViewAjax queryContractByBidId(String bidId,String projectId){
		ContractDtlVo contractDtlVo=contractService.queryContractByBidId(bidId);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "", contractDtlVo);
		return ViewUtil.Json(map);
	}
	/**
	 * 修改合同并提交
	 * @Title: updateContract
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("updateContract")
	@ValidFormToken
	public ModelAndViewAjax updateContract() throws Exception{
		Contract contract =getContractFromBrower("修改合同信息:");
		List<Payplan> payplans=getPayplanListFromBrower("payplans");
		contractService.updateContract(contract, payplans);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	@RequestMapping("deleteContract")
	public ModelAndViewAjax deleteContract(String contractId) throws Exception{
		contractService.deleteContract(contractId);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	/**
	 * 暂存合同信息
	 * @Title: tmpUpdateContract
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("tmpUpdateContract")
	public ModelAndViewAjax tmpUpdateContract() throws Exception{
		Contract contract =getContractFromBrower("修改合同信息:");
		List<Payplan> payplans=getPayplanListFromBrower("payplans");
		contractService.tmpUpdateContract(contract, payplans);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	/**
	 * 删除合同变更流程
	 * @Title: deWorkflow
	 * @param contractId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("delWorkflow")
	public ModelAndViewAjax delWorkflow(String contractId) throws Exception{
		contractService.delWorkflow(contractId);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	
	/**
	 * 暂存合同信息
	 * @Title: tmpInsertContract
	 * @param bidId
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("tmpInsertContract")
	@ValidFormToken
	public ModelAndViewAjax tmpInsertContract(String bidId,String projectId) throws Exception{
		Contract contract=getContractFromBrower("合同信息：");
		List<Payplan> payplans=getPayplanListFromBrower("payplans");
		contractService.tmpInsertContract(contract,payplans);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		UserInfoScope userInfoScopeInfo = itcMvcService.getUserInfoScopeDatas();
                String taskId = userInfoScopeInfo.getParam("taskId");
                String toComplete = userInfoScopeInfo.getParam("toComplete");
                if ( "true".equals( toComplete )&&StringUtils.isNotEmpty( taskId ) ) {
                    workflowService.complete(taskId, userInfoScopeInfo.getUserId(), null, null, "已执行", true);
                }
		return ViewUtil.Json(map);
	}
	
	private Contract getContractFromBrower(String prefixMessage) throws Exception{
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String contractString=userInfoScope.getParam("contract");
		LOGGER.info(prefixMessage+" 信息为："+contractString);
		Contract contract=JsonHelper.fromJsonStringToBean(contractString, Contract.class);
		return contract;
	}
	
	private List<Payplan> getPayplanListFromBrower(String prefixMessage) throws Exception{
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String contractString=userInfoScope.getParam("payplans");
		LOGGER.info(prefixMessage+" 信息为："+contractString);
		List<Payplan> payplans=JsonUtil.fromJsonStringToList(contractString, Payplan.class);
		return payplans;
	}
	private List<PayplanTmp> getPayplanTmpListFromBrower(String prefixMessage) throws Exception{
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String contractString=userInfoScope.getParam("payplans");
		LOGGER.info(prefixMessage+" 信息为："+contractString);
		List<PayplanTmp> payplans=JsonUtil.fromJsonStringToList(contractString, PayplanTmp.class);
		if(payplans!=null){
			for(int i=0;i<payplans.size();i++){
				PayplanTmp payplanTmp=payplans.get(i);
				payplanTmp.setpayplanId(payplanTmp.getId());
			}
		}
		return payplans;
	}
	
	

	/**
	 * 终止合同变更审批流程
	 * @Title: stopWorkflow
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("stopWorkflow")
	public ModelAndViewAjax stopWorkflow(String processInstId,String reason) throws Exception {
		Contract contract=getContractFromBrower("终止合同变更流程 合同信息：");
		contractService.stopWorkflow(contract,processInstId,reason);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	/**
	 * 判断合同编码编码是否存在
	 * @Title: isContractCodeExisted
	 * @param contractCode
	 * @return
	 */
	@RequestMapping("isContractCodeExisted")
	public ModelAndViewAjax isContractCodeExisted(String contractCode,String contractId){
		boolean b=contractService.isContractCodeExisted(contractCode,contractId);
		String result="";
		if(!b){
			result="true";
		}
		return ViewUtil.Json(result);
		
	}
	
	/**
	 * 查询合同信息
	 * @Title: contractListData
	 * @Description: 
	 * @return: Page<ContractVo> 合同信息及分页信息
	 * @throws
	 */
	@RequestMapping(value = "/contractListData", method = RequestMethod.POST)
	public Page<ContractVo> contractListData() throws Exception {
        
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		Page<ContractVo> page = userInfoScope.getPage();

		// 查询参数处理
		Map<String, String[]> params = userInfoScope.getParamMap();
		// 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
	
		HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap("contractVoMap", "pms", "ContractDao");
		

		
		if (params.containsKey("search")) {
			String fuzzySearchParams = userInfoScope.getParam("search");
			LOGGER.info("查询合同列表的条件：" + fuzzySearchParams);

			// 调用工具类将jsonString转为HashMap
			Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap(fuzzySearchParams);
                                        

			// 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
			fuzzyParams = MapHelper.fromPropertyToColumnMap((HashMap<String, Object>) fuzzyParams,
					propertyColumnMap);
			if ( null!=fuzzyParams.get( "company_id" ) ) {
			    fuzzyParams.put( "company_name", fuzzyParams.get( "company_id" ).toString() );
			    fuzzyParams.remove( "company_id" );
                        }
			if ( null!=fuzzyParams.get( "status" )){
                            fuzzyParams.put( "statusname", fuzzyParams.get( "status" ).toString() );
                            fuzzyParams.remove( "status" );
                        }
                        if ( null!=fuzzyParams.get( "typeName" )){
                            fuzzyParams.put( "typenamestr", fuzzyParams.get( "typeName" ).toString() );
                            fuzzyParams.remove( "typeName" );
                        }
                        if ( null!=fuzzyParams.get( "sign_time" )){
                            fuzzyParams.put( "signtimestr", fuzzyParams.get( "sign_time" ).toString() );
                            fuzzyParams.remove( "sign_time" );
                        }
			// 自动的会封装模糊搜索条件
			page.setFuzzyParams(fuzzyParams);

		}

		// 设置排序内容
		if (params.containsKey("sort")) {
		        String sortKey = userInfoScope.getParam( "sort" );
		        if(StringUtils.isNotEmpty( sortKey ) &&sortKey.contains( "statusValue" )){
		            sortKey = sortKey.replace( "statusValue", "status" );
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
		page = contractQueryService.queryContractListAndFilter(page, userInfoScope);
		return page;
	}
	
	/**
	 * 
	 * @Title: queryContractByKeyWord
	 * @Description: 根据关键字即项目名称查询10条记录，用于前端hint模块的调用
	 * @param kw
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryContractByKeyWord", method = RequestMethod.POST)
	public ModelAndViewAjax queryContractByKeyWord(String kw,String type) throws Exception{
	        List<Map> list=contractService.queryContractListByKeyWord(kw);
		return ViewUtil.Json(getRelationPayInfo(list,type));
	}
	/**
         * @Title: queryContractByKeyWordWithOutRowFilter
         * @Description: 根据关键字即项目名称查询10条记录，用于前端hint模块的调用，剥离行权限
         * @param kw
         * @return
         * @throws Exception
         */
        @RequestMapping(value="/queryContractByKeyWordWithOutRowFilter", method = RequestMethod.POST)
        public ModelAndViewAjax queryContractByKeyWordWithOutRowFilter(String kw,String type) throws Exception{
                List<Map> list=contractService.queryContractListByKeyWordWithoutRowFilter(kw);
                return ViewUtil.Json(getRelationPayInfo(list,type));
        }
        
        private List<Map> getRelationPayInfo(List<Map> list,String type) {
            List<Map> resultList = new ArrayList<Map>(0);
            for ( Map map : list ) {
                if("chkOut".equals( type )){
                    List<PayplanVo> payplanVos=payplanService.getCheckableByContractId(Integer.valueOf(map.get( "id" ).toString()));
                    if ( 0 < payplanVos.size() ) {
                        resultList.add( map );
                    }
                }else if("pay".equals( type )){
                    List<PayplanVo> payplanVos=payplanService.getPayableByContractId(Integer.valueOf(map.get( "id" ).toString()));
                    if ( 0 < payplanVos.size() ) {
                        resultList.add( map );
                    }
                }else {
                    resultList.add( map );
                }
            }
            return resultList;
        }
	/**
	 * @Title: stopAppWorkflow
	 * @Description: 终止合同审批流程
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("stopAppWorkflow")
	public ModelAndViewAjax stopAppWorkflow(String processInstId,String reason) throws Exception {
		Contract contract=getContractFromBrower("终止合同审批流程 合同信息：");
		contractService.stopWorkflow(contract,processInstId,reason);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	/**
	 * @Title: getParamFromBrower
	 * @Description: 从前端获取指定参数名数值
	 * @return
	 * @throws Exception
	 */
	private String getParamFromBrower(String prefixMessage) throws Exception{
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String needWorkFlow=userInfoScope.getParam(prefixMessage);
		LOGGER.info(prefixMessage+" 信息为："+needWorkFlow);
		return needWorkFlow;
	}
	/**
	 * @Title: tmpSaveContractWithWorkFlow
	 * @Description: 暂存合同信息(带审批流程)--统一的设定 暂存不启动流程。
	 * @param bidId
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("tmpSaveContractWithWorkFlow")
	@ValidFormToken
	public ModelAndViewAjax tmpSaveContractWithWorkFlow() throws Exception{
		Contract contract=getContractFromBrower("合同信息：");
		Integer contractId = contract.getId();
		Map<String,Object> results = new HashMap<String,Object>(0);
		if(null!=contractId){
			contract.setId(contractId);
		}
		//不启动流程的 保存
		results = contractService.saveOrUpdateContractWithWorkFlow(contract,false);
		Map<String,Object> map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "",results);
		return ViewUtil.Json(map);
	}
	/**
	 * @Title: saveOrUpdateContractWithWorkFlow
	 * @Description: 提交合同信息(带审批流程 )
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="saveOrUpdateContractWithWorkFlow",method=RequestMethod.POST)
	@ValidFormToken
	public ModelAndViewAjax saveOrUpdateContractWithWorkFlow() throws Exception{
		Contract contract=getContractFromBrower("合同信息：");
		Integer contractId = contract.getId();
		//如果有流程实例id，说明已经启动了工作流，那么就不需要启动工作流了
		String processInstId = getParamFromBrower("processInstId");
		boolean startWorkFlow =StringUtils.isNotEmpty(processInstId)?false:true;
		Map<String,Object> results = new HashMap<String,Object>(0);
		if(null!=contractId){
			contract.setId(contractId);
		}
		results=contractService.saveOrUpdateContractWithWorkFlow(contract,startWorkFlow);
		Map<String,Object> map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "",results);
		return ViewUtil.Json(map);
	}
	/**
         * @Title: applyNullifyWorkflow
         * @Description: 提交作废合同信息(带审批流程 )
         * @return
         * @throws Exception
         */
        @RequestMapping(value="applyNullifyWorkflow",method=RequestMethod.POST)
        @ValidFormToken
        public ModelAndViewAjax applyNullifyWorkflow() throws Exception{
                Contract contract=getContractFromBrower("申请作废合同信息：");
                Map<String,Object> results = new HashMap<String,Object>(0);
                results=contractService.startNullifyWorkflow(contract);
                Map<String,Object> map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "",results);
                return ViewUtil.Json(map);
        }
        /**
         * @description:删除提交作废合同申请待办
         * @author: 890162
         * @createDate: 2016-12-09
         * @param sheetId 采购申请id
         * @return
         * @throws Exception
         */
        @RequestMapping(value = "/removeApplyNullifyWorkflow", method = RequestMethod.POST)
        @ValidFormToken
        public Map<String, Object> removeApplyNullifyWorkflow(String sheetId,String procInstId) throws Exception {
            Map<String, Object> result = new HashMap<String, Object>(0);
            boolean resultVal = false;
            UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
            homepageService.Delete( procInstId, userInfo );
            contractService.updateNullifyProcInstId(sheetId,"");
            resultVal = true;
            result.put("result", String.valueOf( resultVal ));
            return result;
        }
        
        
	/**
	 * @Title: selectNextStep
	 * @Description: 转发到下一步操作页面
	 * @return
	 */
	@RequestMapping(value="selectNextStep")
	public ModelAndView selectNextStep(){
	    ModelAndView modelAndView=new ModelAndView("contract/selectNextStep.jsp");
	    return modelAndView;
	}
	/**
         * @Title: selectNextStep
         * @Description: 转发到下一步操作页面(三步选择)
         * @return
         */
        @RequestMapping(value="selectNextStepThree")
        public ModelAndView selectNextStepThree(){
            ModelAndView modelAndView=new ModelAndView("contract/selectNextStepThreeSelection.jsp");
            return modelAndView;
        }
	/**
	 * @Title: voidFlow
	 * @Description: 合同流程作废
	 * @return
	 */
	@RequestMapping(value="/voidFlow")
	public ModelAndViewAjax voidFlow(String businessId,String processInstId,String taskId,String message){
		UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
		FlowVoidParamBean flowVoidParamBean=new FlowVoidParamBean(processInstId, taskId, message, userInfo.getUserId(),
				userInfo.getUserId(), userInfo, businessId);
		contractService.voidFlow(flowVoidParamBean);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	/**
         * @Title:generateNewContractCode
         * @Description:生成新的合同编号
         * @return ModelAndViewAjax
         * @throws
         */
        @RequestMapping(value="/generateNewContractCode")
        public ModelAndViewAjax generateNewContractCode(){
            String prefix = "DJ";
            String dateStr = new SimpleDateFormat("yyyyMMdd").format( new Date() );
            String newCode = contractService.generateNewContractCode( prefix+"-"+dateStr+"-" );
            newCode = prefix+"-"+dateStr+"-"+newCode;
            Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
            map.put( "newCode", newCode );
            return ViewUtil.Json(map);
        }
}
