package com.timss.workorder.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.sec.SecureUserGroup;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Controller
@RequestMapping(value = "/workorder/woUtil")
public class WoUtilController{
	@Autowired
	private IAuthorizationManager authManager;

	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	ISecurityMaintenanceManager iSecManager;
	
//	private static Logger logger = Logger.getLogger(WoUtilController.class);
	
	@RequestMapping(value = "/userMultiSearch")
	@ResponseBody
	public ArrayList<JSONObject> initPageBaseData() throws Exception {
		ArrayList<JSONObject> resultMap = new ArrayList<JSONObject>();
		
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String searchStyle = userInfoScope.getParam("searchStyle");//获取前台传过来的form表单数据
		String value = userInfoScope.getParam("kw");//获取前台传过来的form表单数据
		
		Page<SecureUser> page = new Page<SecureUser>();
		page = new Page<SecureUser>();
		page.setPageSize(20);
		page.setParameter("userStatus", "Y");  //有效的用户
		page.setParameter(searchStyle, value);
		page = authManager.retrieveUsersInAllSites(page);
		int size = page.getResults().size();
		if(size>11){
			size = 11;
		}

		for (int i = 0; i < size; i++) {
			JSONObject jsonObject = new JSONObject();
			//jsonObject.put("name", page.getResults().get(i).getName()+page.getResults().get(i).getId());
			jsonObject.put("name", page.getResults().get(i).getName());
			jsonObject.put("phone", page.getResults().get(i).getOfficeTel());
			jsonObject.put("id", page.getResults().get(i).getId());
			jsonObject.put("location", page.getResults().get(i).getOfficeAddr());
			jsonObject.put("orgName", page.getResults().get(i).getCurrOrgName());
			jsonObject.put("comName", page.getResults().get(i).getCurrSiteName());
			jsonObject.put("job", page.getResults().get(i).getJob());
			resultMap.add(jsonObject);
		}

		return resultMap;
	}
	
	@RequestMapping(value = "/userGroupFilter")
	@ResponseBody
	public ArrayList<ArrayList<Object>> userGroupFilter() throws Exception {
		ArrayList<ArrayList<Object>> resultMap = new ArrayList<ArrayList<Object>>();
		
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		
		String filterStr = userInfoScope.getParam("filterStr");//以xxx开头的用户组标识(此处需要将用户组中的ID变成小写)
		
		
		Page<SecureUserGroup>  page = new Page<SecureUserGroup>();
		page = new Page<SecureUserGroup>();
		page.setPageSize(100);
		HashMap<String, Object> searchBy = new HashMap<String, Object>();
		
		searchBy.put("searchBy", filterStr);
		page.setParams(searchBy);
//		page.setParameter("searchBy", filterStr);
		
		SecureUser operator = userInfoScope.getSecureUser();
		
		Page<SecureUserGroup> qResult = iSecManager.retrieveGroups(page, operator);
		
		List<SecureUserGroup> groups = qResult.getResults();
		
		for (int i = 0; i < groups.size(); i++) {	
			ArrayList<Object> row = new ArrayList<Object>();
			SecureUserGroup group = groups.get(i);
			row.add(group.getId());
            row.add(group.getName());
            resultMap.add(row);
		}
		return resultMap;
	}
	
	/**
	 * @description: 查找工程师角色的人员
	 * @author: 王中华
	 * @createDate: 2015-1-26
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/engineerRole")
	@ResponseBody
	public ArrayList<ArrayList<Object>> engineerRole() throws Exception {
		ArrayList<ArrayList<Object>> resultMap = new ArrayList<ArrayList<Object>>();
		
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		
		List<SecureUser> resultList = new ArrayList<SecureUser>();
		resultList = authManager.retriveUsersWithSpecificRole(siteId+"_WO_WHGCS", null, false, true);
		
		for (int i = 0; i < resultList.size(); i++) {	
			ArrayList<Object> row = new ArrayList<Object>();
			SecureUser engineer = resultList.get(i);
			row.add(engineer.getId());
            row.add(engineer.getName());
            resultMap.add(row);
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/userGroupGetUserIds")
	@ResponseBody
	public ArrayList<String> userGroupGetUserIds() throws Exception {
		ArrayList<String> result = new ArrayList<String>();
		
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
//		String siteId = userInfoScope.getSiteId();
		String userGroupIds = userInfoScope.getParam("userGroupIds");
		
		String[] userGroupIdList = userGroupIds.split(",");
		
		List<SecureUser> resultList = new ArrayList<SecureUser>();
		for (int i = 0; i < userGroupIdList.length; i++) {
			List<SecureUser> tempResultList = authManager.retriveUsersWithSpecificGroup(userGroupIdList[i], null, false, true);
			resultList.removeAll(tempResultList);
			resultList.addAll(tempResultList);
		}
		
		for (int i = 0; i < resultList.size(); i++) {	
            result.add(resultList.get(i).getId());
		}
		return result;
	}
}
