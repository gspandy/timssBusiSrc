package com.timss.workorder.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.workorder.bean.MaintainPlan;
import com.timss.workorder.bean.WoAttachment;
import com.timss.workorder.service.JobPlanService;
import com.timss.workorder.service.MaintainPlanService;
import com.timss.workorder.service.WoAttachmentService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
@Controller
@RequestMapping(value = "/workorder/maintainPlan")
public class MaintainPlanController {
	
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private MaintainPlanService maintainPlanService;
	
	@Autowired
	@Qualifier("JobPlanServiceImpl")
	private JobPlanService jobPlanService;
	@Autowired
	private WoAttachmentService woAttachmentService;
	@Autowired
    private IAuthorizationManager authManager;
	
	private static Logger logger = Logger.getLogger(MaintainPlanController.class);
	/**
	 * @description: 点击左边菜单，跳转到维护计划列表页面
	 * @author: 王中华
	 * @createDate: 2014-6-19
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value="/maintainPlanList" )
	@ReturnEnumsBind("WO_SPEC,WO_MAINTAINPLAN_FROM")
	public String maintainPlanList(){
		return "/maintainPlanList.jsp";
	}
	
	/**
	 * @description:弹出框中显示父维护计划列表
	 * @author: 王中华
	 * @createDate: 2014-7-24
	 * @return:
	 */
	@RequestMapping(value="/parentMTPList" )
	@ReturnEnumsBind("WO_SPEC")
	public String parentMTPList(){
		return "/operationMTP/parentMTPList.jsp";
	}
	
	/**
	 * @description: 维护计划列表中datagrid数据获取
	 * @author: 王中华
	 * @createDate: 2014-6-19
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/maintainPlanListdata",method=RequestMethod.POST)
	public Page<MaintainPlan> maintainPlanListData() throws Exception {
		logger.debug("------------进入到web层函数-----------");
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		Page<MaintainPlan> page = userInfoScope.getPage();
		
		String fuzzySearchParams = userInfoScope.getParam("search");
//		System.out.println(fuzzySearchParams);
        HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "maintainPlanMap", "workorder","MaintainPlanDao");
        
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
			
		page = maintainPlanService.queryAllMTP(page);
		
		return page;
	}
	/**
	 * @description: 父维护计划列表中datagrid数据获取
	 * @author: 王中华
	 * @createDate: 2014-6-19
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/parentMTPListdata",method=RequestMethod.POST)
	public Page<MaintainPlan> parentMTPListdata() throws Exception {
		logger.debug("------------进入到web层函数-----------");
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		Page<MaintainPlan> page = userInfoScope.getPage();
		
		String fuzzySearchParams = userInfoScope.getParam("search");
//		System.out.println(fuzzySearchParams);
        HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "maintainPlanMap", "workorder","MaintainPlanDao");
        
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
			page.setSortKey("CREATEDATE");
			page.setSortOrder("desc");
		}
		
		page = maintainPlanService.queryAllParentMTP(page);
		
		return page;
	}
	
	
	/**
	 * @description: 跳转到新建维护计划页面 workorder/maintainPlan/openNewMTPPage.do
	 * @author: 王中华
	 * @createDate: 2014-6-19
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/openNewMTPPage" )
	public String openNewMTPPage(){
		return "/operationMTP/newMaintainPlan.jsp" ;
	}

	
	/**
	 * @description: 提交“维护计划”数据
	 * @author: 王中华
	 * @createDate: 2014-6-19
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/commitMaintainPlandata",method=RequestMethod.POST)
	public HashMap<String,String> commitMaintainPlan() throws Exception {
		
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		
		int maintainPlanId = Integer.valueOf(userInfoScope.getParam("maintainPlanId")); //获取维护计划的ID，若是新建的，则为0
		String mtpFormData = userInfoScope.getParam("maintainPlanForm");//获取前台传过来的form表单数据
		String preHazardDataStr = userInfoScope.getParam("preHazardData");
		String toolDataStr =userInfoScope.getParam("toolData");
		String taskDataStr = userInfoScope.getParam("taskData");
		String workerDataStr = userInfoScope.getParam("workerData");
		String uploadIds = userInfoScope.getParam("uploadIds"); //附件编号
		
		HashMap<String,String> addMTPDataMap = new HashMap<String, String>();
		addMTPDataMap.put("mtpFormData", mtpFormData);
		addMTPDataMap.put("preHazardDataStr", preHazardDataStr);
		addMTPDataMap.put("toolDataStr", toolDataStr);
		addMTPDataMap.put("taskDataStr", taskDataStr);
		addMTPDataMap.put("workerDataStr", workerDataStr);
		addMTPDataMap.put("uploadIds", uploadIds); 
		addMTPDataMap.put("mtpSource", "cycle_maintainPlan"); //周期性维护计划
		addMTPDataMap.put("jpSource", "maintainPlan"); //作业方案的来源：standard:标准作业方案；maintainPlan:维护计划；
		  											 //plan：工单策划；actual:实际
		addMTPDataMap.put("commitStyle", "save");  //此处的save代表存储到业务表中，而在工单模块里面commit代表不需要存储
		
		if(maintainPlanId==0){
			maintainPlanService.insertMaintainPlan(addMTPDataMap);
		}else{
			maintainPlanService.updateMaintainPlan(addMTPDataMap);
		}
		
		HashMap<String,String> mav = new HashMap<String,String>();
		mav.put("result", "success");
		return mav;
	}
	
	@RequestMapping(value = "/unavailableMTP",method=RequestMethod.POST)
	public HashMap<String,String> unavailableMTP() throws Exception {
		
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		
		int maintainPlanId = Integer.valueOf(userInfoScope.getParam("maintainPlanId")); //获取维护计划的ID
		maintainPlanService.updateMTPToUnvailable(maintainPlanId);
		
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
    @RequestMapping(value = "/queryFullMTPPage" )
    public String  queryMTPById() throws Exception{
//    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
//    	String siteId = userInfoScope.getSiteId();
//    	HashMap<String,String> resultMap = new HashMap<String,String>();
        
//        List<AppEnum> list =  itcMvcService.getEnum("WO_WORKTEAM");
//        for(int i=0; i<list.size();i++){
//        	String workteamCode = list.get(i).getCode();
//        	String roleCode = siteId + "_wo_"+workteamCode+"Principal";
//        	
//            List<SecureUser> users = authManager.retriveUsersWithSpecificRole(roleCode, null, false, true);
//             
//            JSONObject obj = new JSONObject ();
//            for ( SecureUser secureUser : users ) {
//            	obj.put(secureUser.getId(), secureUser.getName());
//            }
//            resultMap.put(roleCode, obj.toString());
//             
//        }
//    	return "/operationMTP/newMaintainPlan.jsp?params=" + resultMap.toString();
    	return "/operationMTP/newMaintainPlan.jsp";
    }
    
    @RequestMapping(value = "/queryMTPDataById" )
    public Map<String, Object> queryMTPDataById() throws Exception{
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	int mtpId = Integer.valueOf(userInfoScope.getParam("maintainPlanId"));
    	MaintainPlan maintainPlan = maintainPlanService.queryMTPById(mtpId);
    	Integer jpId = maintainPlan.getJobPlanId();
    	Map<String,Object> resultMap = jobPlanService.queryJPById(jpId);
    	
    	resultMap.put("maintainPlanForm", JsonHelper.toJsonString(maintainPlan));
    	
    	List<WoAttachment> attachList = woAttachmentService.queryWoAttachmentById(String.valueOf(mtpId), "MTP");
    	//TODO  根据附件id，转化数据传前台，前台显示附件数据
    	ArrayList<String> aList=new ArrayList<String>();
    	for (int i = 0; i < attachList.size(); i++) {
    		aList.add(attachList.get(i).getAttachId());
		}
    	List<Map<String, Object>> map= FileUploadUtil.getJsonFileList(Constant.basePath, aList);
    	resultMap.put("attachmentMap", map);
    	
    	return resultMap;
    }
   

    /**
     * @description:通过角色查找人员列表（返回到combobox中显示）
     * @author: 王中华
     * @createDate: 2014-7-9
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryUserByUserGroup")
    public @ResponseBody ArrayList<ArrayList<Object>> queryUserByUserGroup() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userGroup = userInfoScope.getParam("userGroup");
        List<SecureUser> users = authManager.retriveUsersWithSpecificGroup(userGroup, null, false, true);
        ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
      
        for ( SecureUser secureUser : users ) {
            ArrayList<Object> row = new ArrayList<Object>();
            row.add(secureUser.getId());
            row.add(secureUser.getName());
            result.add(row);
        }
        return result;
    }
    
    
    @RequestMapping(value = "/queryWorkTeamPrincipal")
    public @ResponseBody HashMap<String ,String> queryWorkTeamPrincipal() throws Exception{
    	 UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	 String siteId = userInfoScope.getSiteId();
    	 
        HashMap<String,String> resultMap = new HashMap<String,String>();
        
        List<AppEnum> list =  itcMvcService.getEnum("WO_WORKTEAM");
        for(int i=0; i<list.size();i++){
        	String workteamCode = list.get(i).getCode();
        	String roleCode = siteId+"_wo_"+workteamCode+"Principal";
        	
             List<SecureUser> users = authManager.retriveUsersWithSpecificRole(roleCode, null, false, true);
             
             JSONObject obj = new JSONObject ();
             for ( SecureUser secureUser : users ) {
            	 obj.put(secureUser.getId(), secureUser.getName());
             }
             resultMap.put(roleCode, obj.toString());
             
        }
        
       return resultMap;
       
       
        
        
    }
}
