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

import com.timss.pms.bean.Bid;
import com.timss.pms.bean.BidCon;
import com.timss.pms.bean.BidMethod;
import com.timss.pms.bean.BidResult;
import com.timss.pms.bean.FlowVoidParamBean;
import com.timss.pms.service.BidQueryService;
import com.timss.pms.service.BidResultService;
import com.timss.pms.service.BidService;
import com.timss.pms.service.PrivilegeService;
import com.timss.pms.service.ProjectService;
import com.timss.pms.util.CreateReturnMapUtil;
import com.timss.pms.util.GetBeanFromBrowerUtil;
import com.timss.pms.vo.BidDtlVo;
import com.timss.pms.vo.BidResultDtlVo;
import com.timss.pms.vo.BidResultVo;
import com.timss.pms.vo.BidVo;
import com.timss.pms.vo.ProjectBudgetVo;
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
 * 
 * @ClassName:     BidController
 * @company: gdyd
 * @Description:招标模块controller
 * @author:    黄晓岚
 * @date:   2014-7-2 上午11:43:36
 */
@Controller
@RequestMapping(value="pms/bid")
public class BidController {
	
	Logger LOGGER=Logger.getLogger(BidController.class);
	
	@Autowired
	BidService bidService;
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	ProjectService projectService;
	@Autowired
	PrivilegeService privilegeService;
	@Autowired
	BidResultService bidResultService;
	@Autowired
	BidQueryService bidQueryService;
	/**
	 * 
	 * @Title: insertBidJsp
	 * @Description: 转发到新增招标信息页面
	 * @return
	 */
	@RequestMapping(value = "/addBidJsp")
	@ReturnEnumsBind(value="pms_project_property,PMS_BID_TYPE")
	public String insertBidJsp(){
		return "bid/addBidResult.jsp";
	}
	
	/**
	 * 
	 * @Title: editBidJsp
	 * @Description: 转发到查看招标详细信息页面
	 * @return
	 */
	@RequestMapping(value = "/editBidJsp")
	@ReturnEnumsBind(value="pms_project_property,PMS_BID_TYPE")
	public String editBidJsp(String id) {
		return "bid/editBidBid.jsp?id=" + id;
	}
	
	/**
	 * 
	 * @Title: insertBidResultJsp
	 * @Description: 转发到新增招标结果信息页面
	 * @return
	 */
	@RequestMapping(value = "/addBidResultJsp")
	@ReturnEnumsBind(value="pms_project_property,PMS_BID_TYPE")
	public String insertBidResultJsp(){
		return "bid/addBidResult.jsp";
	}
	
	/**
	 * 
	 * @Title: editBidResultJsp
	 * @Description: 转发到查看招标结果详细信息页面
	 * @return
	 */
	@RequestMapping(value = "/editBidResultJsp")
	@ReturnEnumsBind(value="pms_project_property,PMS_BID_TYPE")
	public String editBidResultJsp(String id) {
		return "bid/editBidResult.jsp?id=" + id;
	}
	
	/**
	 * 
	 * @Title: editBidResultJsp
	 * @Description: 转发到查看招标结果列表页面
	 * @return
	 */
	@RequestMapping(value = "/bidResultList")
	@ReturnEnumsBind(value="pms_project_property,PMS_BID_TYPE,PMS_STATUS")
	public String bidResultList(String id) {
		return "bid/bidResultList.jsp";
	}
	/**
	 * 
	 * @Title: queryBidListByProjectId
	 * @Description: 根据项目id查询招标列表信息
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/queryBidListByProjectId",method=RequestMethod.POST)
	public ModelAndViewAjax queryBidListByProjectId(String projectId) throws Exception{
		List<BidVo> lists=bidService.queryBidListByProjectId(projectId);
		Map map=CreateReturnMapUtil.createGridMap(CreateReturnMapUtil.SUCCESS_FLAG, -1,lists);
		return ViewUtil.Json(map);
		
	}
	/**
	 * 根据招标id，查询招标信息
	 * @Title: queryBidByBidId
	 * @param bidId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryBidByBidId",method=RequestMethod.POST)
	@SuppressWarnings("rawtypes")
	public ModelAndViewAjax queryBidByBidId(String bidId) throws Exception{
		BidDtlVo bidDtlVo=bidService.queryBidByBidId(Integer.parseInt(bidId));
		
		Map priMap=privilegeService.createBidPrivilege(bidDtlVo,itcMvcService);
		Map map=CreateReturnMapUtil.createMapWithPrivilege(CreateReturnMapUtil.SUCCESS_FLAG, "",bidDtlVo,priMap);
		return ViewUtil.Json(map);
		
	}
	/**
	 * 
	 * @Title: inserBid
	 * @Description: 插入招标信息
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/insertBid",method=RequestMethod.POST)
	public ModelAndViewAjax insertBid() throws Exception{
		Bid bid=getBidFromBrower("插入招标信息为");
		List<BidCon> bidCons=GetBeanFromBrowerUtil.getBeanListFromBrower("插入招标信息为：", "bidCons", BidCon.class, itcMvcService);
		bidService.insertBid(bid,bidCons);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "添加招标成功");
		return ViewUtil.Json(map);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/insertBidMethod",method=RequestMethod.POST)
	public ModelAndViewAjax insertBidMethod() throws Exception{
		BidMethod bidMethod=getBidMethodFromBrower("插入评标信息为");
		bidService.insertBidMethod(bidMethod);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "添加评标方法成功");
		return ViewUtil.Json(map);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/insertBidResult",method=RequestMethod.POST)
	public ModelAndViewAjax insertBidResult() throws Exception{
		BidResult bidResult=getBidResultFromBrower("插入招标结果信息为");
		List<BidCon> bidCons=GetBeanFromBrowerUtil.getBeanListFromBrower("插入招标单位信息为：", "bidCons", BidCon.class, itcMvcService);
		bidService.insertBidResult(bidResult,bidCons);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "添加招标结果成功");
		return ViewUtil.Json(map);
	}
	
	/**
	 * 根据项目id查询项目相关的信息，包含金额数据
	 * @Title: queryProjectByProjectId
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/queryProjectByProjectId",method=RequestMethod.POST)
	public ModelAndViewAjax queryProjectByProjectId(String id){
		ProjectBudgetVo projectBudgetVo=projectService.queryProjectBudgetById(id);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "", projectBudgetVo);
		return ViewUtil.Json(map);
	}
	
	/**
	 * 根据招标结果id，查询招标结果信息
	 * @Title: queryBidResultById
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/queryBidResultById",method=RequestMethod.POST)
	public ModelAndViewAjax queryBidResultById(String id){
		BidResultDtlVo bidResultDtlVo=bidResultService.queryBidResultById(Integer.valueOf(id));
		Map priMap=privilegeService.createBidResultPrivilege(bidResultDtlVo, itcMvcService);
		Map map=CreateReturnMapUtil.createMapWithPrivilege(CreateReturnMapUtil.SUCCESS_FLAG, "", bidResultDtlVo,priMap);
		return ViewUtil.Json(map);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/tmpInsertBidResult",method=RequestMethod.POST)
	@ValidFormToken
	public ModelAndViewAjax tmpInsertBidResult() throws Exception{
		BidResult bidResult=getBidResultFromBrower("插入招标结果信息为");
		
		Map mapB=bidResultService.tmpInsertBidResult(bidResult);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "添加招标结果成功",mapB);
		return ViewUtil.Json(map);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/tmpUpdateBidResult",method=RequestMethod.POST)
	@ValidFormToken
	public ModelAndViewAjax tmpUpdateBidResult() throws Exception{
		BidResult bidResult=getBidResultFromBrower("插入招标结果信息为");
		
		bidResultService.tmpUpdateBidResult(bidResult);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "添加招标结果成功");
		return ViewUtil.Json(map);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/deleteBidResult",method=RequestMethod.POST)
	public ModelAndViewAjax deleteBidResult(int id) throws Exception{
		
		
		bidResultService.deleteBidResult(id);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "添加招标结果成功");
		return ViewUtil.Json(map);
	}
	
	/**
	 * 
	 * @Title: getBidFromBrower
	 * @Description: 将前台传过来的招标信息转换为bid类
	 * @param prefix
	 * @return
	 * @throws Exception
	 */
	private Bid getBidFromBrower(String prefix) throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String bidString = userInfoScope.getParam("bid");
		LOGGER.info(prefix+":"+bidString);
		// jsonString To JavaBean的方法
		Bid bid = JsonHelper.fromJsonStringToBean(bidString,Bid.class);
		return bid;
	}
	
	private BidMethod getBidMethodFromBrower(String prefix) throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String bidString = userInfoScope.getParam("bidMethod");
		LOGGER.info(prefix+":"+bidString);
		// jsonString To JavaBean的方法
		BidMethod bid = JsonHelper.fromJsonStringToBean(bidString,BidMethod.class);
		return bid;
	}
	
	private BidResult getBidResultFromBrower(String prefix) throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String bidString = userInfoScope.getParam("bidResult");
		LOGGER.info(prefix+":"+bidString);
		// jsonString To JavaBean的方法
		BidResult bid = JsonHelper.fromJsonStringToBean(bidString,BidResult.class);
		return bid;
	}
	
	
	/**
	 * 终止招标结果审批流程
	 * @Title: stopWorkflow
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("stopWorkflow")
	public ModelAndViewAjax stopWorkflow(String processInstId,String reason) throws Exception {
		BidResult bidResult=getBidResultFromBrower("终止招标结果流程信息");
		bidResultService.stopWorkflow(bidResult,processInstId,reason);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	
	
	/**
	 * 查询招标结果信息
	 * @Title: bidResultListData
	 * @Description: 
	 * @return: Page<Project> 项目立项信息及分页信息
	 * @throws
	 */
	@RequestMapping(value = "/bidResultListData", method = RequestMethod.POST)
	public Page<BidResultVo> bidResultListData() throws Exception {
        
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		Page<BidResultVo> page = userInfoScope.getPage();

		// 查询参数处理
		Map<String, String[]> params = userInfoScope.getParamMap();
		// 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
	
		HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap("bidResultVoMap", "pms", "BidResultDao");
		

		
		if (params.containsKey("search")) {
			String fuzzySearchParams = userInfoScope.getParam("search");
			LOGGER.info("查询项目立项的条件：" + fuzzySearchParams);

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
			page.setSortKey("bid_result_id");
			page.setSortOrder("desc");
		}
		page = bidQueryService.queryBidResultListAndFilter(page, userInfoScope);
		return page;
	}
	
	/**
	 * 流程作废
	 * @Title: voidFlow
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/voidFlow")
	public ModelAndViewAjax voidFlow(String businessId,String processInstId,String taskId,String message){
		UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
		FlowVoidParamBean flowVoidParamBean=new FlowVoidParamBean(processInstId, taskId, message, userInfo.getUserId(),
				userInfo.getUserId(), userInfo, businessId);
		bidResultService.voidFlow(flowVoidParamBean);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	/**
	 * 根据招标名称模糊搜索招标列表
	 * @Title: bidResultListData
	 * @Description: 
	 * @return: Page<Project> 项目立项信息及分页信息
	 * @throws
	 */
	@RequestMapping(value = "/queryBidByKeyWord", method = RequestMethod.POST)
	public ModelAndViewAjax queryBidByKeyWord(String kw) throws Exception {
        
		List<Map<String,String>> maps=bidQueryService.queryBidResultListByKeyWord(kw);
		return ViewUtil.Json(maps);
	}
}
