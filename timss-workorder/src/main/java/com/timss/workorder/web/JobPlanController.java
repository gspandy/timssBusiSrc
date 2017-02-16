package com.timss.workorder.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.timss.workorder.bean.JobPlan;
import com.timss.workorder.service.JobPlanService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;


@Controller
@RequestMapping(value = "/workorder/jobPlan")
public class JobPlanController {
	 @Autowired
	 @Qualifier("JobPlanServiceImpl")
    private JobPlanService jobPlanService;
    
    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * @description: 跳转至jobPlanList首页
     * @author: 王中华
     * @createDate: 2014-6-18
     * @return:
     */
    @RequestMapping(value = "/jobPlanList")
    @ReturnEnumsBind("WO_SPEC")
    public String jobPlanList(){
        return "/jobPlanList.jsp";
    }
	
    
    /**
     * 查询Demo数据的列表
     * @description:
     * @author: 周保康
     * @createDate: 2014-6-16
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/jobPlanListData")
    public Page<JobPlan> JobPlanListData() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        Page<JobPlan> page = userInfoScope.getPage();
        
        String fuzzySearchParams = userInfoScope.getParam("search");
        HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "jobPlanMap", "workorder","JobPlanDao");
        
        if(fuzzySearchParams!=null){
        	HashMap<String, Object> fuzzyParams = (HashMap<String, Object>)MapHelper.jsonToHashMap( fuzzySearchParams );
            fuzzyParams = MapHelper.fromPropertyToColumnMap(fuzzyParams, propertyColumnMap);
        	page.setFuzzyParams(fuzzyParams);
        } 
        
        // 设置排序内容
		if (userInfoScope.getParamMap().containsKey("sort")) {
			String sortKey = userInfoScope.getParam("sort");
	
			// 如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
			sortKey = propertyColumnMap.get(sortKey);
	
			page.setSortKey(sortKey);
			page.setSortOrder(userInfoScope.getParam("order"));
		} else {
			// 设置默认的排序字段
			page.setSortKey("MODIFYDATE");
			page.setSortOrder("desc");
			page.setParameter("siteid", siteId);
		}
        
        page = jobPlanService.queryStandardJP(page);
        
        return page;
    }
    
    /**
     * @description: 跳转到新建标准作业方案页面
     * @author: 王中华
     * @createDate: 2014-6-19
     * @return:
     */
    @RequestMapping(value = "/openNewJPPage" )
	public String openNewJPPage(){
		return "/operationJP/newJobPlan.jsp";
	}
    
    @RequestMapping(value = "/commitJobPlandata",method=RequestMethod.POST)
	public HashMap<String,String> commitJobPlan() throws Exception {
		
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		int jobPlanId = Integer.valueOf(userInfoScope.getParam("jobPlanId")); //获取作业方案的ID，若是新建的，则为0
		
		String jpFormData = userInfoScope.getParam("jobPlanForm");//获取前台传过来的form表单数据
		String preHazardDataStr = userInfoScope.getParam("preHazardData");
		String toolDataStr =userInfoScope.getParam("toolData");
		String taskDataStr = userInfoScope.getParam("taskData");
		String workerDataStr = userInfoScope.getParam("workerData");
		
		HashMap<String,String> addJPDataMap = new HashMap<String, String>();
		addJPDataMap.put("jpFormData", jpFormData);
		addJPDataMap.put("preHazardDataStr", preHazardDataStr);
		addJPDataMap.put("toolDataStr", toolDataStr);
		addJPDataMap.put("taskDataStr", taskDataStr);
		addJPDataMap.put("workerDataStr", workerDataStr);
		addJPDataMap.put("jpSource", "standard"); //作业方案的来源：standard:标准作业方案；maintainPlan:维护计划；
												  //plan：工单策划；actual:实际
		addJPDataMap.put("commitStyle", "save");  //此处的save代表存储到业务表中，而在工单模块里面commit代表不需要存储
		
		if(jobPlanId==0){
			jobPlanService.insertJobPlan(addJPDataMap);
		}else{
			jobPlanService.updateJobPlan(addJPDataMap);
		}
		
		
		HashMap<String,String> mav = new HashMap<String,String>();
		mav.put("result", "success");
		return mav;
	}
    
    
    
    /**
     * @description:禁用作业方案
     * @author: 王中华
     * @createDate: 2014-7-23
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/unavailableJP",method=RequestMethod.POST)
   	public HashMap<String,String> unavailableJP() throws Exception {
   		
   		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
   		int jobPlanId = Integer.valueOf(userInfoScope.getParam("jobPlanId")); //获取作业方案的ID，若是新建的，则为0
   		jobPlanService.updateJPToUnvailable(jobPlanId);
   		HashMap<String,String> mav = new HashMap<String,String>();
   		mav.put("result", "success");
   		return mav;
   	}
    
    /**
     * @description:查找维护计划完整信息
     * @author: 王中华
     * @createDate: 2014-6-27
     * @param jpId
     * @return:
     */
    @RequestMapping(value = "/queryFullJPPage" )
    public String  queryJPById() throws Exception{
    	return "/operationJP/newJobPlan.jsp";
    }
    
    @RequestMapping(value = "/queryJPDataById" )
    public Map<String, Object> queryJPDataById() throws Exception{
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	int jpId = Integer.valueOf(userInfoScope.getParam("jobPlanId"));
    	Map<String,Object> returnData = jobPlanService.queryJPById(jpId);
    	return returnData;
    }
    
}
