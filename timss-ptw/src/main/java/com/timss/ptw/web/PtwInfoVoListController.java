package com.timss.ptw.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.ptw.service.PtwInfoService;
import com.timss.ptw.vo.PtwInfoVoList;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
/**
 * 
 * @title: 工作票列表展示的Controller
 * @description: {desc}
 * @company: gdyd
 * @className: PtwInfoVoListController.java
 * @author: 周保康
 * @createDate: 2014-6-27
 * @updateUser: 周保康
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "ptw/ptwInfo")
public class PtwInfoVoListController {
    private static final Logger log = Logger.getLogger(PtwInfoVoListController.class);
    
    @Autowired
    private PtwInfoService ptwInfoService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private IAuthorizationManager authManager;
    
    /**
     * 
     * @description:跳转至工作票列表
     * @author: 周保康
     * @createDate: 2014-6-27
     * @return:
     */
    @RequestMapping(value = "/preQueryPtwInfoVoList")
    public String preQueryPtwInfoVoList() {
        return "/ptwList.jsp";
    }
    
    /**
     * 
     * @description:历史票主目录
     * @author: zhuw
     * @createDate: 2016年6月20日
     * @return:ModelAndView
     * @throws Exception 
     */
    @RequestMapping("/queryAllHisPtwList")
    public ModelAndView queryAllFinishPtwStandardListPage( ) throws Exception{
        
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String ptwFilterType = userInfoScope.getParam( "ptwFilterType" );
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "ptwFilterType", ptwFilterType );
        
        return new ModelAndView( "queryAllHisPtwList.jsp");
    }
    
    /**
     * @description:历史票信息的获取
     * @author: zhuw
     * @createDate: 2016-6-21
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryHisPtwList")
    public Page<PtwInfoVoList> queryHisPtwList( String search ) throws Exception{
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<PtwInfoVoList> page = userInfoScope.getPage();
        String ptwFilterType = userInfoScope.getParam( "ptwFilterType" );
        
        //工作票类型和工作票所属站点都要进行设置
        page.setParameter( "ptwFilterType", ptwFilterType );
        page.setParameter( "siteId", userInfoScope.getSiteId() );
        
        //快速查询 按编号和设备名称
        if(StringUtils.isNotBlank( search )){
        	PtwInfoVoList ptwInfo = JsonHelper.fromJsonStringToBean( search , PtwInfoVoList.class );
            page.setParameter( "wtNo", ptwInfo.getWtNo() );
            page.setParameter( "wtTypeName", ptwInfo.getWtTypeName() );
            page.setParameter( "eqName", ptwInfo.getEqName() );
            page.setParameter( "workContent", ptwInfo.getWorkContent() );
        }
        
        //设置排序内容
        if ( userInfoScope.getParam( "sort" ) != null) {
            page.setSortKey( userInfoScope.getParam( "sort" ) );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        }else {
            //设置默认的排序字段
            page.setSortKey( "modifyDate" );
            page.setSortOrder( "desc" );
        }
        
        page = ptwInfoService.queryHisPtwList( page );
        return page;
    }
    
    /**
     * 
     * @description:工作票信息的获取
     * @author: 周保康
     * @createDate: 2014-6-27
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryPtwInfoVoList")
    public Page<PtwInfoVoList> queryPtwInfoVoList() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<PtwInfoVoList> page = userInfoScope.getPage();
        //查询类型  wtKind, wtNo,advSearch,treeSearch
        String searchFrom = userInfoScope.getParam( "searchFrom" );
        int isStdWt = Integer.parseInt( userInfoScope.getParam( "isStdWt" ) );
        String searchParams = userInfoScope.getParam( "searchParams" );
        
        //工作票类型和工作票所属站点都要进行设置
        page.setParameter( "isStdWt", isStdWt );
        page.setParameter( "siteId", userInfoScope.getSiteId() );
        
        Map<String, Object> searchParamsMap =  MapHelper.jsonToHashMap( searchParams );
        if ( searchFrom.equals( "wtNo")) {
            page.setFuzzyParams( searchParamsMap );
        }else if(searchFrom.equals( "advSearch" )){
            //查询时间
            if ( ! searchParamsMap.get( "searchDateFrom" ).equals( "" ) || ! searchParamsMap.get( "searchDateEnd" ).equals( "" ) ) {
                page.setParameter( "searchDateType" , searchParamsMap.get( "searchDateType" ) );
                
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                if ( ! searchParamsMap.get( "searchDateFrom" ).equals( "" ) ) {
                    page.setParameter( "searchDateFrom", df.parse( searchParamsMap.get( "searchDateFrom" ).toString()) );
                }
                if ( ! searchParamsMap.get( "searchDateEnd" ).equals( "" ) ) {
                    Date endDate = df.parse( searchParamsMap.get( "searchDateEnd" ).toString());
                    endDate = DateUtils.addDays( endDate, 1 );
                    page.setParameter( "searchDateEnd",endDate );
                }
            }
            //查询人
            if(searchParamsMap.get( "searchPersonId" ) != null && !searchParamsMap.get( "searchPersonId" ).equals( "0" )){
                page.setParameter( "searchPersonType",searchParamsMap.get( "searchPersonType" ));
                page.setParameter( "searchPersonId", searchParamsMap.get( "searchPersonId" ) );
            }
            
            //查询状态
            if(searchParamsMap.get( "wtStatus" ) != null && !searchParamsMap.get( "wtStatus" ).equals( "0" )){
                page.setParameter( "wtStatus", searchParamsMap.get( "wtStatus" ) );
            }
            
            //查询类型
            if(searchParamsMap.get( "wtType" ) != null && !searchParamsMap.get( "wtType" ).equals( "" ) && !searchParamsMap.get( "wtType" ).equals( "0" )){
                page.setParameter( "wtType", searchParamsMap.get( "wtType" ) );
            }
            
        }else if ( searchFrom.equals( "treeSearch" ) ) {
            //根据设备树查询
            String eqId = searchParamsMap.get( "eqId" ).toString();
            if( eqId!= null && !eqId.equals( "0" ) && !eqId.toUpperCase().equals( userInfoScope.getSiteId() )){
                page.setParameter( "eqId", eqId );
            }
        }        
        
        //设置排序内容
        if ( userInfoScope.getParam( "sort" ) != null) {
            page.setSortKey( userInfoScope.getParam( "sort" ) );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        }else {
            //设置默认的排序字段
            page.setSortKey( "modifyDate" );
            page.setSortOrder( "desc" );
        }
        
        page = ptwInfoService.queryPtwInfoVoList( page );
        return page;
    }
    
    
    @RequestMapping(value = "/queryPtwUsersByRole")
    public @ResponseBody ArrayList<ArrayList<Object>> queryPtwUsersByRole() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String role = userInfoScope.getParam( "role" );
        role = userInfoScope.getSiteId() + "_" + role ;
        List<SecureUser> users = authManager.retriveUsersWithSpecificRole(role, null, false, true);
        ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        ArrayList<Object> def = new ArrayList<Object>();
        String addDefaultAll = userInfoScope.getParam( "hasOther" );
        if ( addDefaultAll != null && addDefaultAll.equals( "all" ) ) {
            def.add(0);
            def.add("全部");
            def.add(true);
            result.add(def);
        }else if ( addDefaultAll != null && addDefaultAll.equals( "select" ) ) {
            def.add(-1);
            def.add("请选择");
            def.add(true);
            result.add(def);
        }
        for ( SecureUser secureUser : users ) {
            ArrayList<Object> row = new ArrayList<Object>();
            row.add(secureUser.getId());
            row.add(secureUser.getName());
            result.add(row);
        }
        return result;
    }
    
    
    @RequestMapping(value = "/queryPtwUsersByGroup")
    public @ResponseBody ArrayList<ArrayList<Object>> queryPtwUsersByGroup() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String role = userInfoScope.getParam( "role" );
        role = userInfoScope.getSiteId() + "_" + role ;
        List<SecureUser> users = authManager.retriveUsersWithSpecificGroup(role, null, false, true);
        ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        ArrayList<Object> def = new ArrayList<Object>();
        String addDefaultAll = userInfoScope.getParam( "hasOther" );
        if ( addDefaultAll != null && addDefaultAll.equals( "all" ) ) {
            def.add(0);
            def.add("全部");
            def.add(true);
            result.add(def);
        }else if ( addDefaultAll != null && addDefaultAll.equals( "select" ) ) {
            def.add(-1);
            def.add("请选择");
            def.add(true);
            result.add(def);
        }
        for ( SecureUser secureUser : users ) {
            ArrayList<Object> row = new ArrayList<Object>();
            row.add(secureUser.getId());
            row.add(secureUser.getName());
            result.add(row);
        }
        return result;
    }
    
    /**
     * 查询所有工作票负责人
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryPtwUsersByGroups")
    public @ResponseBody ArrayList<ArrayList<Object>> queryPtwUsersByGroups() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String[] types = userInfoScope.getParam( "types" ).split(",");
        List<SecureUser> secureUsers = new ArrayList<SecureUser>();
        List<SecureUser> users = new ArrayList<SecureUser>();
        for(String type:types){
        	String role = userInfoScope.getSiteId() + "_" + type;
        	secureUsers.addAll(authManager.retriveUsersWithSpecificGroup(role, null, false, true));
        }
        Iterator<SecureUser> iter=secureUsers.iterator();  
        while(iter.hasNext()){  
        	SecureUser secureUser=iter.next(); 
	         if(users.contains(secureUser)){  
	        	 iter.remove();  
	         }else{  
	        	 users.add(secureUser);  
	         }
        }  
        ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        ArrayList<Object> def = new ArrayList<Object>();
        String addDefaultAll = userInfoScope.getParam( "hasOther" );
        if ( addDefaultAll != null && addDefaultAll.equals( "all" ) ) {
            def.add(0);
            def.add("全部");
            def.add(true);
            result.add(def);
        }else if ( addDefaultAll != null && addDefaultAll.equals( "select" ) ) {
            def.add(-1);
            def.add("请选择");
            def.add(true);
            result.add(def);
        }
        for ( SecureUser secureUser : users ) {
            ArrayList<Object> row = new ArrayList<Object>();
            row.add(secureUser.getId());
            row.add(secureUser.getName());
            result.add(row);
        }
        return result;
    }
}
