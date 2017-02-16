package com.timss.workorder.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.workorder.bean.WoDelayConfig;
import com.timss.workorder.bean.WoFaultType;
import com.timss.workorder.bean.WoPriConfig;
import com.timss.workorder.bean.WoPriority;
import com.timss.workorder.service.WoDelayConfigService;
import com.timss.workorder.service.WoFaultTypeService;
import com.timss.workorder.service.WoPriorityService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.view.ModelAndViewAjax;

@Controller
@RequestMapping(value = "/workorder/woParamsConf")
public class ItcWoParamsConfController{
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WoFaultTypeService woFaultTypeService;
	@Autowired
	private WoPriorityService woPriorityService;
	@Autowired
	private WoDelayConfigService woDelayConfigService;
	@Autowired
    private IAuthorizationManager authManager;
	
	/**
	 * @description:故障类型列表页面
	 * @author: 王中华
	 * @createDate: 2014-8-22
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value="/faultTypeList")
	public String faultTypeList() throws Exception{
		return "/woFaultTypeList.jsp";
	}
	
	@RequestMapping(value="/faultTypeListData")
	@ResponseBody
	public Map<String, Object> faultTypeListData() throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        Page<WoFaultType> page = userInfoScope.getPage();
        
        String fuzzySearchParams = userInfoScope.getParam("search");
        String faultTypeId = userInfoScope.getParam("faultTypeId");
        
        HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "faultTypeMap", "workorder","WoFaultTypeDao");
        
        if(fuzzySearchParams!=null){
        	HashMap<String, Object> fuzzyParams = (HashMap<String, Object>)MapHelper.jsonToHashMap( fuzzySearchParams );
            fuzzyParams = MapHelper.fromPropertyToColumnMap(fuzzyParams, propertyColumnMap);
        	page.setFuzzyParams(fuzzyParams);
        } 
        if(faultTypeId!=null){  //精确查找
        	page.setParameter("id", faultTypeId);
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
			page.setSortKey("DEFAULT_SCORE");
			page.setSortOrder("desc");
		}
		page.setParameter("siteid", siteId);
        page = woFaultTypeService.queryAllFaultType(page);
        
        WoFaultType woFaultType = woFaultTypeService.queryFaultTypeRootBySiteId(siteId);
		int rootId = woFaultType.getId();
		
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", page.getResults() == null ? new String[0] : page.getResults());
        result.put("total", page.getTotalRecord());
        result.put("rootId", rootId);
        return result;
	}
	
	/**
	 * @description:打开故障类型树页面
	 * @author: 王中华
	 * @createDate: 2014-9-18
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value="/openFaultTypeTreePage")
	public String openFaultTypeTreePage() throws Exception{
		return "/woParamsConf/faultTypeTree.jsp";
	}
	/**
	 * @description:获取故障类型树数据
	 * @author: 王中华
	 * @createDate: 2014-9-18
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/getFaultTypeTree" )
	public ModelAndViewAjax getFaultTypeTree() throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String faultTypeId = userInfoScope.getParam("id");
		String treeType = userInfoScope.getParam("treeType");  //"SD"：只显示服务目录，不显示服务性质
		
		List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
		Map<String, Object> pNode = new HashMap<String, Object>();
		
		if(faultTypeId == null){
			WoFaultType woFaultType = woFaultTypeService.queryFaultTypeRootBySiteId(userInfoScope.getSiteId());
			pNode.put("id", woFaultType.getId());
			pNode.put("text",woFaultType.getName() );
			pNode.put("state", "open");
			pNode.put("type", "root");
			pNode.put("faultTypeCode", woFaultType.getFaultTypeCode());
			String parentIdString  = String.valueOf(woFaultType.getId());
			pNode.put("children",woFaultTypeService.queryChildrenNodes(parentIdString,treeType));
			ret.add(pNode);
			
		}else{
			ret = woFaultTypeService.queryChildrenNodes(faultTypeId,treeType);
		}
		
		return itcMvcService.jsons(ret);
	}
	
	
	
	@RequestMapping(value = "/openFaultTypePage" )
	public String openFaultTypePage(){
		return "/woParamsConf/newFaultType.jsp";
	}
	
	/**
	 * @description:故障类型查询
	 * @author: 王中华
	 * @createDate: 2014-9-18
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/queryFaultTypeDataById" )
    public Map<String, Object> queryFaultTypeDataById() throws Exception{
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	int faultTypeId = Integer.valueOf(userInfoScope.getParam("faultTypeId"));
    	WoFaultType woFaultType = woFaultTypeService.queryWoFaultTypeById(faultTypeId);
    	 
    	Map<String,Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("faultTypeForm", woFaultType);
    	return resultMap;
    } 
	/**
	 * @description: 提交故障类型
	 * @author: 王中华
	 * @createDate: 2014-9-18
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/commitFaultType" )
	public Map<String, String> commitFaultType() throws Exception {
		
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		int faultTypeId = Integer.valueOf(userInfoScope.getParam("faultTypeId"));
		
		String faultTypeForm = userInfoScope.getParam("faultTypeForm");//获取前台传过来的form表单数据
		 
		
		Map<String,String> addFaultTypeDataMap = new HashMap<String, String>();
		addFaultTypeDataMap.put("woFaultTypeForm", faultTypeForm);
		
		if(faultTypeId==0){
			woFaultTypeService.insertWoFaultType(addFaultTypeDataMap);
		}else{
			woFaultTypeService.updateWoFaultType(addFaultTypeDataMap);
		}
		
		Map<String,String> mav = new HashMap<String,String>();
		mav.put("result", "success");
		return mav;
	}
	
	@RequestMapping(value = "/deleteFaultType" )
    public Map<String, String> deleteFaultType() throws Exception{
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	int faultTypeId = Integer.valueOf(userInfoScope.getParam("faultTypeId"));
    	woFaultTypeService.deleteFaultTypeById(faultTypeId);
    	 
    	Map<String,String> mav = new HashMap<String,String>();
		mav.put("result", "success");
		return mav;
    } 
	/**
	 * @description:工单标识列表页面
	 * @author: 王中华
	 * @createDate: 2014-8-22
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value="/woLabelList")
	public String woLabelList() throws Exception{
		return "/woLabelList.jsp";
	}
	
	@RequestMapping(value = "/openWoLabelPage" )
	public String openWoLabelPage(){
		return "/woParamsConf/newWoLabel.jsp";
	}
	
	/**
	 * @description:工单优先级列表
	 * @author: 王中华
	 * @createDate: 2014-8-25
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value="/PriorityList")
	@ReturnEnumsBind("WO_URGENCY_DEGREE,WO_INFLUENCE_SCOPE")
	public String woPriorityList() throws Exception{
		return "/woPriorityList.jsp";
	}
	
	@RequestMapping(value = "/openWoPriorityPage" )
	@ReturnEnumsBind("WO_URGENCY_DEGREE,WO_INFLUENCE_SCOPE")
	public String openWoPriorityPage(){
		return "/woParamsConf/newWoPriority.jsp";
	}
	
	/**
	 * @description:新建工单优先级页面
	 * @author: 890151
	 * @createDate: 2015年12月4日
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/openWorkPriorityAddPage" )
	public String openWorkPriorityAddPage(){
		return "/workOrderPriorityAdd.jsp";
	}
	
	/**
	 * @description:新建工单优先级
	 * @author: 王中华
	 * @createDate: 2014-8-30
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/commitWoPriority" )
	public Map<String, String> commitWoPriority() throws Exception {
		
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		int woPriorityId = Integer.valueOf(userInfoScope.getParam("woPriorityId")); 
		
		String woPriorityForm = userInfoScope.getParam("woPriorityForm");//获取前台传过来的form表单数据
		String woPriConfData = userInfoScope.getParam("woPriConfData");//获取前台传过来的form表单数据
		 
		
		Map<String,String> addWoPriorityDataMap = new HashMap<String, String>();
		addWoPriorityDataMap.put("woPriorityForm", woPriorityForm);
		addWoPriorityDataMap.put("woPriConfData", woPriConfData);
		
		if(woPriorityId==0){
			woPriorityService.insertWoPriority(addWoPriorityDataMap);
		}else{
			woPriorityService.updateWoPriority(addWoPriorityDataMap);
		}
		
		Map<String,String> mav = new HashMap<String,String>();
		mav.put("result", "success");
		return mav;
	}
	
	/**
	 * @description:优先级数据传回前台，填充combobox
	 * @author: 王中华
	 * @createDate: 2014-10-27
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/comboboxPriority" )
	public @ResponseBody List<ArrayList<Object>> comboboxPriority() throws Exception {
		
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		
		Page<WoPriority> page = userInfoScope.getPage();
		page.setPageSize(100);
		page.setSortKey("RESPOND_LEN");
		page.setSortOrder("ASC");
		page.setParameter("siteid", userInfoScope.getSiteId());
		List<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
		List<WoPriority> priorityList = woPriorityService.queryWoPriorityList(page).getResults();
		
		for ( WoPriority priority : priorityList ) {
			ArrayList<Object> row = new ArrayList<Object>();
            row.add(priority.getId());
            double hour1 = priority.getRespondLength();
            double hour2 =  priority.getSolveLength();
            int a = (int)hour1;
            int b = (int)hour2;
            //控制显示时，小数点后为0时隐藏
            String timeString1 = "";
            String timeString2 = "";
            if((int)hour1 == a){
            	timeString1 = String.valueOf(a);
            }else{
            	timeString1 = String.valueOf(hour1);
            }
            if((int)hour2 == b){
            	timeString2 = String.valueOf(b);
            }else{
            	timeString2 = String.valueOf(hour2);
            }
            
            row.add(priority.getName()+"    (解决时长"+timeString2+"小时)");
            result.add(row);
        }
		return result;
	}
	/**
	 * @description:优先级列表
	 * @author: 王中华
	 * @createDate: 2014-8-30
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value="/woPriorityListData")
	public Page<WoPriority> woPriorityListData() throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        Page<WoPriority> page = userInfoScope.getPage();
        
        String fuzzySearchParams = userInfoScope.getParam("search");
        HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "priorityMap", "workorder","WoPriorityDao");
        
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
			page.setSortKey("SORT_NUM");
			page.setSortOrder("ASC");
			page.setParameter("siteid", siteId);
		}
        
        page = woPriorityService.queryWoPriorityList(page);
        
        return page;
	}
	
	@RequestMapping(value = "/queryWoPriorityDataById" )
	public Map<String, String> queryWoPriorityDataById() throws Exception {
		
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteid = userInfoScope.getSiteId();
    	int woPriorityId = Integer.valueOf(userInfoScope.getParam("woPriorityId"));
    	Map<String, Object> resultMap = woPriorityService.queryWoPriorityById(woPriorityId,siteid);
    	String woPriorityJsonStr = JsonHelper.toJsonString(resultMap.get("baseData"));
    	String woPriConfigJsonStr = JsonHelper.toJsonString(resultMap.get("datagridData"));
    	Map<String, String> returnData = new HashMap<String, String>();
    	returnData.put("baseData", woPriorityJsonStr);
    	returnData.put("datagridData", woPriConfigJsonStr);
    	return returnData;
	}
	
	@RequestMapping(value = "/deletePriority" )
	public Map<String, String> deletePriority() throws Exception {
		
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteid = userInfoScope.getSiteId();
    	int woPriorityId = Integer.valueOf(userInfoScope.getParam("priId"));
    	woPriorityService.deleteWoPriority(woPriorityId,siteid);
    	Map<String, String> returnData = new HashMap<String, String>();
    	returnData.put("result", "success");
    	return returnData;
	}
	/**
	 * @description: 根据影响度和紧急度，查询对应的服务级别ID
	 * @author: 王中华
	 * @createDate: 2014-12-9
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/getPriIdValByUrgentInfluence" )
	public Map<String, String> getPriIdValByUrgentInfluence() throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String urgentVal = userInfoScope.getParam("urgentVal");
    	String influenceVal = userInfoScope.getParam("influenceVal");
    	
    	String priId = woPriorityService.getPriIdValByUrgentInfluence(urgentVal,influenceVal).get("priId");
    	
    	Map<String, String> returnData = new HashMap<String, String>();
    	returnData.put("woPriorityId", String.valueOf(priId));
    	
    	return returnData;
	}
	
	/**
	 * @description: 根据影响度和紧急度，查询同一个站点下是否有相同的配置，如果有，则将相同配置记录返回
	 * @author: 王中华
	 * @createDate: 2014-12-9
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/checkHasConfigData" )
	public Map<String, String> checkHasConfigData() throws Exception {
		Map<String, String> returnData = new HashMap<String, String>();
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String woPriConfData = userInfoScope.getParam("woPriConfData");
		String priId = userInfoScope.getParam("priId");
		JSONObject woPriConfJsonObj = JSONObject.fromObject(woPriConfData);
		int woPriConfDatagridNum =Integer.valueOf(woPriConfJsonObj.get("total").toString());  //记录数
		JSONArray woPriConfJsonArray = woPriConfJsonObj.getJSONArray("rows"); //记录数组
		 
		for(int i=0; i<woPriConfDatagridNum; i++){
			String itemsRecord = woPriConfJsonArray.get(i).toString();  //某条记录的字符串表示
			WoPriConfig woPriConfig = JsonHelper.fromJsonStringToBean(itemsRecord, WoPriConfig.class);
			String urgentVal = woPriConfig.getUrgentDegree();
	    	String influenceVal = woPriConfig.getInfluenceScope();
	    	Map<String, String> priConfHashMap = woPriorityService.getPriIdValByUrgentInfluence(urgentVal,influenceVal);
	    	String priIdInDb = priConfHashMap.get("priId");
	    	String priName = priConfHashMap.get("priName");
	    	if(!("0".equals(priId) && "".equals(priName))){ //如果查到有重复的配置，直接返回
	    		returnData.put("urgentVal", urgentVal);
	        	returnData.put("influenceVal", influenceVal);
	    		if("0".equals(priId)){ //新建时的验证
	    			returnData.put("woPriId", priIdInDb);
		        	returnData.put("woPriName", priName);
		        	return returnData;
	    		}else{  //编辑修改状态的验证
	    			if(!priId.equals(priIdInDb)){
	    				returnData.put("woPriId", priIdInDb);
			        	returnData.put("woPriName", priName);
			        	return returnData;
	    			}
	    		}
	        	
	    	}
		}
    	
    	returnData.put("woPriId", "0");
    	returnData.put("woPriName", "");
    	return returnData;
	}
	

	/**
	 * @description:返回工单维护责任人
	 * @author: 890151
	 * @createDate: 2015年11月24日
	 * @return
	 */
	@RequestMapping(value = "/comboboxMaintainExecutor" )
	public @ResponseBody List<ArrayList<Object>> comboboxMaintainExecutor() throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        SecureUser secureuser = userInfoScope.getSecureUser();
		List<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
		String orgId = userInfoScope.getOrgId();
		if(orgId!=null){
			List<SecureUser> resultList = authManager.retriveActiveUsersOfGivenOrg(orgId.substring(0,orgId.length()-2), true, secureuser);
			//authManager.retriveUsersWithSpecificRole( roleId, null, true, true );
			//List<SecureUser> suList = selectUserInfo.getAm().retriveUsersWithSpecificRole( siteId, orgCode, true, true );
	        for ( SecureUser user : resultList) {
				ArrayList<Object> row = new ArrayList<Object>();
	            row.add(user.getId());
	            row.add(user.getName());
				result.add(row);
	        }
		}

        return result;
	}

	/**
	 * @description:查询对应的工单延期最大时长配置
	 * @author: 王中华
	 * @createDate: 2016-6-22
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/queryWoDelayConfig" )
        public Map<String, Object> queryWoDelayConfig() throws Exception {
                
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            String siteid = userInfoScope.getSiteId();
            String woPriorityId = userInfoScope.getParam("priority");
            String delayType = userInfoScope.getParam("delayType");
            
            WoDelayConfig woDelayConfig = woDelayConfigService.queryWoDelayConfig(delayType,woPriorityId,siteid);
            Map<String, Object> returnData = new HashMap<String, Object>();
            
            returnData.put( "woDelayConfig", JsonHelper.toJsonString(woDelayConfig) );
            return returnData;
        }
	
}
