package com.timss.itsm.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.timss.itsm.bean.ItsmCustomerLoc;
import com.timss.itsm.service.ItsmWoUtilService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.sec.SecureUserGroup;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.manager.sec.ISecResourceSyncManager;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.service.ItcSiteService;
import com.yudean.workflow.service.SelectUserService;

@Controller
@RequestMapping(value = "/itsm/woUtil")
public class ItsmWoUtilController {
    @Autowired
    private IAuthorizationManager authManager;

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    ISecurityMaintenanceManager iSecManager;
    @Autowired
    private ItsmWoUtilService woUtilService;
    @Autowired
    private ItcSiteService siteService;
    @Autowired
    private ISecResourceSyncManager syncManager;
    @Autowired
    private SelectUserService userService;
    
    @RequestMapping(value = "/userMultiSearch")
    @ResponseBody
    public List<JSONObject> initPageBaseData() throws Exception {
        List<JSONObject> resultMap = new ArrayList<JSONObject>();

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String searchStyle = userInfoScope.getParam( "searchStyle" );// 获取前台传过来的form表单数据
        String value = userInfoScope.getParam( "kw" );

        Page<SecureUser> page = new Page<SecureUser>();
        page = new Page<SecureUser>();
        page.setPageSize( 20 );
        page.setParameter( "userStatus", "Y" ); // 有效的用户
        page.setParameter( searchStyle, value );
        page = authManager.retrieveUsersInAllSites( page );
        int size = page.getResults().size();
        if ( size > 11 ) {
            size = 11;
        }

        for ( int i = 0; i < size; i++ ) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put( "name", page.getResults().get( i ).getName() );
            jsonObject.put( "phone", page.getResults().get( i ).getOfficeTel() );
            String userId = page.getResults().get( i ).getId();
            jsonObject.put( "id", userId );
            // 获取模块存储的用户信息
            ItsmCustomerLoc customerLocBean = woUtilService.queryCustomerLocInfo( userId );
            if ( customerLocBean != null ) {
                jsonObject.put( "location", customerLocBean.getCustomerLoc() ); // 用最近存储的信息
            } else {
                jsonObject.put( "location", page.getResults().get( i ).getOfficeAddr() );
            }

            jsonObject.put( "orgName", page.getResults().get( i ).getCurrOrgName() );
            jsonObject.put( "comName", page.getResults().get( i ).getCurrSiteName() );
            jsonObject.put( "job", page.getResults().get( i ).getJob() );
            resultMap.add( jsonObject );
        }

        return resultMap;
    }

    @RequestMapping(value = "/userMultiSearchByRole")
    @ResponseBody
    public List<JSONObject> userMultiSearchByRole() throws Exception {
        List<JSONObject> resultMap = new ArrayList<JSONObject>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String searchStyle = userInfoScope.getParam( "searchStyle" );// 获取前台传过来的form表单数据
        String value = userInfoScope.getParam( "kw" );
        Page<SecureUser> page = new Page<SecureUser>();
        page = new Page<SecureUser>();
        page.setPageSize( 20 );
        page.setParameter( "userStatus", "Y" ); // 有效的用户
        page.setParameter( searchStyle, value );
        page.setParameter( "roleId", "ITC_ITSM_WHGCS" );
        page = authManager.retrieveUsersByRoleId( page );
        int size = page.getResults().size();
        if ( size > 11 ) {
            size = 11;
        }
        for ( int i = 0; i < size; i++ ) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put( "name", page.getResults().get( i ).getName() );
            String userId = page.getResults().get( i ).getId();
            jsonObject.put( "id", userId );
            resultMap.add( jsonObject );
        }
        return resultMap;
    }
    
    @RequestMapping(value = "/userInfoSearchById")
    @ResponseBody
    public List<JSONObject> userInfoSearchById() throws Exception {
        List<JSONObject> resultMap = new ArrayList<JSONObject>();

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getParam( "userId" );

        String searchStyle = "searchByUserId";
        String value = userId;

        Page<SecureUser> page = new Page<SecureUser>();
        page = new Page<SecureUser>();
        page.setPageSize( 20 );
        page.setParameter( "userStatus", "Y" ); // 有效的用户
        page.setParameter( searchStyle, value );
        page = authManager.retrieveUsersInAllSites( page );
        int size = page.getResults().size();

        // 获取模块存储的用户信息
        ItsmCustomerLoc customerLocBean = woUtilService.queryCustomerLocInfo( userId );

        if ( size > 0 ) {
            SecureUser userInfoSecureUser = page.getResults().get( 0 );
            JSONObject jsonObject = new JSONObject();
            jsonObject.put( "name", userInfoSecureUser.getName() );
            jsonObject.put( "phone", userInfoSecureUser.getOfficeTel() );
            jsonObject.put( "id", userInfoSecureUser.getId() );
            if ( customerLocBean != null ) {
                jsonObject.put( "location", customerLocBean.getCustomerLoc() ); // 用最近存储的信息
            } else {
                jsonObject.put( "location", userInfoSecureUser.getOfficeAddr() );
            }

            jsonObject.put( "orgName", userInfoSecureUser.getCurrOrgName() );
            jsonObject.put( "comName", userInfoSecureUser.getCurrSiteName() );
            jsonObject.put( "job", userInfoSecureUser.getJob() );
            resultMap.add( jsonObject );
        }
        return resultMap;
    }

    @RequestMapping(value = "/userGroupFilter")
    @ResponseBody
    public List<List<Object>> userGroupFilter() throws Exception {
        List<List<Object>> resultMap = new ArrayList<List<Object>>();

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();

        String filterStr = userInfoScope.getParam( "filterStr" );// 以xxx开头的用户组标识(此处需要将用户组中的ID变成小写)

        Page<SecureUserGroup> page = new Page<SecureUserGroup>();
        page = new Page<SecureUserGroup>();
        page.setPageSize( 100 );
        Map<String, Object> searchBy = new HashMap<String, Object>();

        searchBy.put( "searchBy", filterStr );
        page.setParams( searchBy );
        // page.setParameter("searchBy", filterStr);

        SecureUser operator = userInfoScope.getSecureUser();

        Page<SecureUserGroup> qResult = iSecManager.retrieveGroups( page, operator );

        List<SecureUserGroup> groups = qResult.getResults();

        for ( int i = 0; i < groups.size(); i++ ) {
            List<Object> row = new ArrayList<Object>();
            SecureUserGroup group = groups.get( i );
            row.add( group.getId() );
            row.add( group.getName() );
            resultMap.add( row );
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
    public List<List<Object>> engineerRole() throws Exception {
        List<List<Object>> resultMap = new ArrayList<List<Object>>();

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();

        List<SecureUser> resultList = new ArrayList<SecureUser>();
        resultList = authManager.retriveUsersWithSpecificRole( siteId + "_ITSM_WHGCS", null, false, true );

        for ( int i = 0; i < resultList.size(); i++ ) {
            List<Object> row = new ArrayList<Object>();
            SecureUser engineer = resultList.get( i );
            row.add( engineer.getId() );
            row.add( engineer.getName() );
            resultMap.add( row );
        }
        return resultMap;
    }

    @RequestMapping(value = "/userGroupGetUserIds")
    @ResponseBody
    public List<String> userGroupGetUserIds() throws Exception {
        List<String> result = new ArrayList<String>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userGroupIds = userInfoScope.getParam( "userGroupIds" );

        String[] userGroupIdList = userGroupIds.split( "," );

        List<SecureUser> resultList = new ArrayList<SecureUser>();
        for ( int i = 0; i < userGroupIdList.length; i++ ) {
            List<SecureUser> tempResultList = authManager.retriveUsersWithSpecificGroup( userGroupIdList[i], null,
                    false, true );
            resultList.removeAll( tempResultList );
            resultList.addAll( tempResultList );
        }

        for ( int i = 0; i < resultList.size(); i++ ) {
            result.add( resultList.get( i ).getId() );
        }
        return result;
    }
    /**
     * @description: 获取站点下所有部门
     * @author: yangk
     * @createDate: 2016年10月8日
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/getDeptments")
    public @ResponseBody 
    List<List<Object>> getDeptments() throws Exception {
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        //根据站点id获取根节点的orgcode
        List<Map<String, Object>> list =  siteService.selectSiteOrg(null, siteId);
        String ordCode = (String) list.get(0).get("ORG_CODE");
        //根据根节点获取子节点
        List<Organization> depts = syncManager.getDeptByParents(ordCode);
        List<List<Object>> result = new ArrayList<List<Object>>();
        for (Organization organization : depts) {
        	List<Object> row = new ArrayList<Object>();
        	if(!"待定".equals(organization.getName()) && !"公司领导".equals(organization.getName())){
        		row.add(organization.getCode());
        		row.add(organization.getName());
        		result.add(row);
        	}
		}
    	return result;
    }
    /**
     * @description: 根据部门id获取部门负责人
     * @author: yangk
     * @createDate: 2016-10-9
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/getBmfzrIds")
    @ResponseBody
    public List<JSONObject> getBmfzrIds() throws Exception { 
         UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
         String orgId = userInfoScope.getParam( "orgId" );
         String groupId = "DPP_BMFZR";
         List<SecureUser> list = userService.byGroupAndOrgCode(groupId, orgId, "U");
         List<JSONObject> result = new ArrayList<JSONObject>();
         for (SecureUser su : list) {
        	 JSONObject row = new JSONObject();
        	 row.put("Id", su.getId());
        	 result.add(row);
		}
    	 return result;
    }
}
