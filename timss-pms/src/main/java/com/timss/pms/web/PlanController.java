package com.timss.pms.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.timss.pms.bean.Plan;
import com.timss.pms.service.PlanService;
import com.timss.pms.service.PrivilegeService;
import com.timss.pms.util.CreateReturnMapUtil;
import com.timss.pms.vo.PlanDtlVo;
import com.timss.pms.vo.PlanVo;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;



/**
 * 
 * @ClassName:     PlanController
 * @company: gdyd
 * @Description: 年度计划controll类
 * @author:    黄晓岚
 * @date:   2014-6-18 下午3:58:14
 */
@Controller
@RequestMapping(value = "pms/plan")
public class PlanController {
	
	Logger LOGGER=Logger.getLogger(PlanController.class);
	
	@Autowired
	private PlanService planService;
	
	@Autowired
	private ItcMvcService itcMvcService;
	
	@Autowired
	private AttachmentMapper attachmentMapper;
	@Autowired
	PrivilegeService privilegeService;
	@Autowired
	HomepageService homepageService;


    /**
     * 
     * @Title: planList
     * @Description: 转发请求到年度计划列表页
     * @param: @return   
     * @return: String   
     * @throws
     */
	@RequestMapping(value = "/planList")
	@ReturnEnumsBind(value="PMS_STATUS,pms_project_type")
	public String planList() {
		return "plan/planList.jsp";
	}
	/**
	 * 
	 * @Title: insertPlanJsp
	 * @Description: 转发到新建年度计划页面
	 * @return
	 */
	@RequestMapping(value = "/addPlanJsp")
	@ReturnEnumsBind(value="pms_project_type")
	public String insertPlanJsp() {
		return "plan/addPlan.jsp";
	}
	/**
	 * 
	 * @Title: editPlanJsp
	 * @Description: 转发到查看年度计划详细信息页面
	 * @return
	 */
	@RequestMapping(value = "/editPlanJsp")
	@ReturnEnumsBind(value="pms_project_type")
	public String editPlanJsp(String id) {
		return "plan/editPlan.jsp";
	}
	
	/**
	 * 
	 * @Title: planListData
	 * @Description: 查询年度计划信息入口
	 * @param planName 要查询的年度计划的名称，采用模糊搜索
	 * @return: Page<Plan> 年度计划信息及分页信息  
	 * @throws
	 */
	@RequestMapping(value = "/planListData", method = RequestMethod.POST)
	public Page<PlanVo> planListData() throws Exception {
		
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		Page<PlanVo> page = userInfoScope.getPage();
		//查询参数处理
		Map<String, String[]> params = userInfoScope.getParamMap();
		//如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
		HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "planVoMap", "pms","PlanDao");
		
		if (params.containsKey("search")) {
			String fuzzySearchParams = userInfoScope.getParam("search");
			LOGGER.info("查询年度计划的条件："+fuzzySearchParams);
			
			//调用工具类将jsonString转为HashMap
			Map fuzzyParams = MapHelper.jsonToHashMap( fuzzySearchParams );
			if(fuzzyParams.containsKey( "startTime" )){
			    String startTime = String.valueOf( fuzzyParams.get( "startTime" ) );
			    fuzzyParams.put( "startTimeStr", startTime );
			    fuzzyParams.remove( "startTime" );
			}
			if(fuzzyParams.containsKey( "endTime" )){
                            String endTime = String.valueOf( fuzzyParams.get( "endTime" ) );
                            fuzzyParams.put( "endTimeStr", endTime );
                            fuzzyParams.remove( "endTime" );
                        }
			
			//如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
			fuzzyParams = MapHelper.fromPropertyToColumnMap( (HashMap<String, Object>) fuzzyParams,propertyColumnMap  );
			// 自动的会封装模糊搜索条件
			page.setFuzzyParams(fuzzyParams);

		}
		
		//设置排序内容
		if ( params.containsKey( "sort" ) ) {
			String sortKey = userInfoScope.getParam( "sort" );
	    
			//如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
            sortKey = propertyColumnMap.get( sortKey );
            
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        }else {
            //设置默认的排序字段
            page.setSortKey( "id" );
            page.setSortOrder( "desc" );
        }
		page = planService.queryPlanList(page, userInfoScope);
		return page;
	}
	
	@RequestMapping(value = "/queryPlanById", method = RequestMethod.POST)
	public ModelAndViewAjax queryPlanById(String id) throws Exception {
		
		PlanDtlVo plan=planService.queryPlanById(id);
		Map priMap=privilegeService.createPlanPrivilege(plan,itcMvcService);
		Map map=null;
		if(plan!=null){
			map=CreateReturnMapUtil.createMapWithPrivilege(CreateReturnMapUtil.SUCCESS_FLAG, "查询成功",plan,priMap);
		}else{
			map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.FAIL_FLAG, "查询结果为空");
		}
		return ViewUtil.Json(map);
	}
	
	/**
	 * 
	 * @Title: insertPlan
	 * @Description: 插入年度计划,并提交
	 * @param plan  年度计划信息
	 * @throws Exception 
	 */
	@RequestMapping(value="/insertPlan", method = RequestMethod.POST)
	@ValidFormToken
	public ModelAndViewAjax insertPlan() throws Exception{
		//TODO 验证
		Plan plan=getPlanFromBrower("插入并提交年度计划数据为");
		planService.insertPlan(plan);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "新建年度计划成功");
		return ViewUtil.Json(map);
	}
	
	/**
	 * 
	 * @Title: insertPlan
	 * @Description: 变更年度计划
	 * @param plan  年度计划信息
	 * @throws Exception 
	 */
	@RequestMapping(value="/changePlan", method = RequestMethod.POST)
	public ModelAndViewAjax changePlan(String processInstId) throws Exception{
		//TODO 验证
		Plan plan=getPlanFromBrower("变更后的年度计划数据为");
		planService.changePlan(plan,processInstId);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "变更年度计划成功");
		return ViewUtil.Json(map);
	}
	
	/**
	 * 
	 * @Title: tmpInsertPlan
	 * @Description: 暂存年度计划
	 * @param plan
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/tmpInsertPlan", method = RequestMethod.POST)
	@ValidFormToken
	public ModelAndViewAjax tmpInsertPlan() throws Exception{
		//TODO 验证
		
		Plan plan=getPlanFromBrower("插入并暂存年度计划数据为");
		planService.tmpInsertPlan(plan);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "新建年度计划成功");
		return ViewUtil.Json(map);
	}
	
	/**
	 * 
	 * @Title: updatePlan
	 * @Description: 修改年度计划信息,并提交
	 * @param plan
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updatePlan", method = RequestMethod.POST)
	@ValidFormToken
	public ModelAndViewAjax updatePlan() throws Exception{
		//TODO 验证
		Plan plan=getPlanFromBrower("修改并提交年度计划数据为");
		planService.updatePlan(plan);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "修改年度计划成功");
		return ViewUtil.Json(map);
	}
	
	/**
	 * 
	 * @Title: updatePlan
	 * @Description: 修改年度计划信息,只是暂存
	 * @param plan
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/tmpUpdatePlan", method = RequestMethod.POST)
	public ModelAndViewAjax tmpUpdatePlan() throws Exception{
		
		//TODO 验证
		Plan plan=getPlanFromBrower("修改并暂存年度计划数据为");
		planService.tmpUpdatePlan(plan);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "暂存年度计划成功");
		return ViewUtil.Json(map);
	}
	
	@RequestMapping(value="/deletePlan", method = RequestMethod.POST)
	public ModelAndViewAjax deletePlan(String id) throws Exception{
		//TODO 验证
		int count=planService.deletePlan(id);
		Map map=null;
		if(count==1){
			map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "删除年度计划成功");
		}else{
			map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.FAIL_FLAG, "删除年度计划失败，请检查该年度计划是否存在");
		}
		
		return ViewUtil.Json(map);
	}
	/**
	 * 
	 * @Title: queryPlanByKeyWord
	 * @Description: 根据关键字即年度计划名称查询10条记录，用于前端hint模块的调用
	 * @param kw
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryPlanByKeyWord", method = RequestMethod.POST)
	public ModelAndViewAjax queryPlanByKeyWord(String kw) throws Exception{
		//TODO 验证
		
		List<Map> map=planService.queryPlanListByKeyWord(kw);
		
		return ViewUtil.Json(map);
	}
	/**
	 * 获取从前端传过来的plan数据
	 * @Title: getPlanFromBrower
	 * @Description: 获取从前端传过来的plan数据
	 * @return
	 * @throws Exception 
	 */
	private Plan getPlanFromBrower(String prefix) throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String planString = userInfoScope.getParam("plan");
		LOGGER.info(prefix+":"+planString);
		// jsonString To JavaBean的方法
		Plan plan = JsonHelper.fromJsonStringToBean(planString,Plan.class);
		
		return plan;
	}
	
	/**
	 * 取消待办提醒
	 * @Title: cancelNotice
	 * @param processInstId
	 * @return
	 */
	@RequestMapping(value="/cancelNotice")
	public ModelAndViewAjax cancelNotice(String processInstId){
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		homepageService.Delete(processInstId, userInfoScope);
		HashMap<String, Object> map=(HashMap<String, Object>) CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "删除年度计划成功");
		return ViewUtil.Json(map);
	}

}
