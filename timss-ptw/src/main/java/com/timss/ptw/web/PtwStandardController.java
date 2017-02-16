package com.timss.ptw.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;

import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.ptw.bean.PtwInfo;
import com.timss.ptw.bean.PtwSafe;
import com.timss.ptw.bean.PtwStandardBean;
import com.timss.ptw.bean.PtwStdSafeBean;
import com.timss.ptw.bean.PtwType;
import com.timss.ptw.service.PtwInfoService;
import com.timss.ptw.service.PtwPtoSelectUserService;
import com.timss.ptw.service.PtwSafeService;
import com.timss.ptw.service.PtwStandardService;
import com.timss.ptw.service.PtwTypeService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.bean.userinfo.impl.UserInfoScopeImpl;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * 
 * @title: 标准工作票controller
 * @description: 
 * @company: gdyd
 * @className: PtwStandardController.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("ptw/ptwStandard")
public class PtwStandardController {
    private Logger log = Logger.getLogger( PtwStandardController.class );
    
    @Autowired
    private PtwStandardService ptwStandardService;
   
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private WorkflowService workflowService;
    
    @Autowired
    private PtwInfoService ptwInfoService;
    
    @Autowired
    private PtwSafeService ptwSafeService;
    
    @Autowired
    private PtwPtoSelectUserService ptwPtoSelectUserService;
    
    @Autowired
    private PtwTypeService ptwTypeService;
    
    @RequestMapping(value = "/sptwCodeMultiSearch")
    @ResponseBody
    public List<Object> sptwCodeMultiSearch() throws Exception {
        List<Object> resultMap = new ArrayList<Object>();

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String value = userInfoScope.getParam( "kw" );

        Page<PtwStandardBean> page = new Page<PtwStandardBean>();
        page = new Page<PtwStandardBean>();
        page.setPageSize( 20 );
        page.setParameter( "sptwCode", value );
        page.setParameter( "siteid", userInfoScope.getSiteId() );
        page = ptwStandardService.querySptoInfoByMultiCode( page );
        int size = page.getResults().size();
        if ( size > 11 ) {
            size = 11;
        }

        for ( int i = 0; i < size; i++ ) {
            net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
            jsonObject.put( "id", page.getResults().get( i ).getId() );
            jsonObject.put( "wtNo", page.getResults().get( i ).getWtNo() );
            jsonObject.put( "workContent", page.getResults().get( i ).getWorkContent() );
            resultMap.add( jsonObject );
        }
        
        return resultMap;
    }
    
    
    /**
     * 
     * @description:标准工作票主目录(已审批通过弹出框) --- core
     * @author: fengzt
     * @createDate: 2015年7月17日
     * @return:ModelAndView
     * @throws Exception 
     */
    @RequestMapping("/queryAllFinishPtwStandardListPage")
    @ReturnEnumsBind("PTW_STD_STATUS,ptw_standard_type")
    public ModelAndView queryAllFinishPtwStandardListPage( ) throws Exception{
        
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String sptwFilterType = userInfoScope.getParam( "sptwFilterType" );
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "sptwFilterType", sptwFilterType );
        
        return new ModelAndView( "queryAllFinishPtwStandardList.jsp");
    }
    
    /**
     * 
     * @description:标准工作票主目录 --- core
     * @author: fengzt
     * @createDate: 2015年7月17日
     * @return:ModelAndView
     * @throws Exception 
     */
    @RequestMapping("/queryPtwStandardListMenu")
    @ReturnEnumsBind("PTW_STD_STATUS,ptw_standard_type")
    public ModelAndView queryPtwStandardListMenu( ) throws Exception{
        //新建按钮是否显示(检查是否有新建某个标准工作票的权限)
        boolean newBtnShowFlag = ptwPtoSelectUserService.hasAuditPrivilege( "sptw", null , "dev_dept_write");
        String jumpUrl = "queryAllPtwStandardList.jsp";  //跳转页面
        Map<String, Object> map = new HashMap<String, Object>( 0 );
        map.put( "newBtnShowFlag", newBtnShowFlag );
        
        ModelAndView modelAndView = new ModelAndView( jumpUrl, map );
        return modelAndView ;
    }
    
    
    /**
     * 
     * @description:拿到登录用户信息
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    private UserInfoScope getUserInfoScope(){
        UserInfoScope userInfoScope = new UserInfoScopeImpl();
        try {
            userInfoScope = itcMvcService.getUserInfoScopeDatas();
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        return userInfoScope;
    }
    
    /**
     * 
     * @description:标准工作票 新建页面
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     * @throws Exception 
     */
    @RequestMapping("/insertPtwStandardMenu")
    @ReturnEnumsBind("ptw_standard_type")
    public ModelAndView insertPtwStandardMenu( ) throws Exception{
        //获得登录用户可以建的标准工作票列表
        List<String> newTypePrivList = getUserNewSptwTypeList("sptw","dev_dept_write");
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", getUserInfoScope().getSiteId() );
        map.put( "privTypes",JsonHelper.fromBeanToJsonString( newTypePrivList ));
        map.put( "bean", "{}" );
        map.put( "items", "{}" );
        
        return new ModelAndView( "insertPtwStandard.jsp", map);
    }
    /**
     * 
     * @description:标准工作票 详情页面
     * @author: fengzt
     * @createDate: 2015年7月20日
     * @return:String
     * @throws Exception 
     */
    @RequestMapping("/updatePtwStandardMenu")
    @ReturnEnumsBind("PTW_STD_STATUS,ptw_standard_type")
    public ModelAndView updatePtwStandardMenu( String id ) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "id", id );
        map.put( "siteId", getUserInfoScope().getSiteId() );
      //获得登录用户可以建的标准工作票列表
        List<String> newSptwTypePrivList = getUserNewSptwTypeList("sptw","dev_dept_write");
        map.put( "privTypes",JsonHelper.fromBeanToJsonString( newSptwTypePrivList ));
        List<String> newPtwTypePrivList = getUserNewSptwTypeList("ptw","xj");
        map.put( "newPtwPrivTypes",JsonHelper.fromBeanToJsonString( newPtwTypePrivList ));
        
        return new ModelAndView( "updatePtwStandard.jsp", map );
    }

    /**
     * 
     * @description:通过站点查询标准工作票
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return: Map<String, Object> 
     * @throws Exception 
     * @throws  
     */
    @RequestMapping("/queryPtwStandardBySiteId")
    public @ResponseBody Map<String, Object> queryPtwStandardBySiteId( int rows, int page, String search,String sort, String order ) throws Exception{
        log.info( "params search = " + search + " --sort =" + sort );
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        List<PtwStandardBean> ptwStandardBeans = new ArrayList<PtwStandardBean>();
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setParameter( "sort", sort );
            pageVo.setParameter( "order", order );
        }
        
      //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            //加入模糊搜索--begin
            Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "sptwInfoMap", "ptw", "PtwStandardDao" );
            Map<String, Object> fuzzyParams = (HashMap<String, Object>) MapHelper.jsonToHashMap( search );
            fuzzyParams = MapHelper.fromPropertyToColumnMap( fuzzyParams, propertyColumnMap );
            pageVo.setFuzzyParams( fuzzyParams );
            //加入模糊搜索--end
            HashMap<String, Object> map = JsonHelper.toObject( search, HashMap.class );
            String eqId = (String)map.get( "eqId" );
            //处理selectTreeId参数--begin
            if ( fuzzyParams.get( "eqId" ) != null ) {
                eqId = fuzzyParams.get( "eqId" ).toString();
                fuzzyParams.remove( "eqId" ); // 因为选择左边树的查询不同与表头查询，所有要移除
            }
            if ( eqId != null && !"null".equals( eqId ) ) {
                pageVo.setParameter( "eqId", eqId ); // 设置树查询参数
            }
            //处理selectTreeId参数--end
            //查询树当前节点及其子节点所有的标准工作票
            if( StringUtils.isNotBlank( eqId ) ){
                ptwStandardBeans = ptwStandardService.queryPtwStandardByEqId( eqId, pageVo );
            }else{
                //流程枚举
                String flowStatus = (String)map.get( "_flowStatus" );
                if( StringUtils.isNotBlank( flowStatus ) ){
                    map.put( "status", flowStatus );
                }
                ptwStandardBeans = ptwStandardService.queryPtwStandardBySearch( map, pageVo );
            }
            
        }else{
            //默认分页
            ptwStandardBeans = ptwStandardService.queryPtwStandardBySiteId( pageVo );
        }
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", ptwStandardBeans );
        if( ! ptwStandardBeans.isEmpty() ){
            dataMap.put( "total",  pageVo.getTotalRecord() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
        
    }
    
    /**
     * 
     * @description:通过站点查询标准工作票
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return: Map<String, Object> 
     * @throws Exception 
     * @throws  
     */
    @RequestMapping("/queryAllFinishPtwStandardBySiteId")
    public @ResponseBody Map<String, Object> queryAllFinishPtwStandardBySiteId( int rows, int page, String search,String sort, String order ) throws Exception{
        log.info( "params search = " + search + " --sort =" + sort );
        //获取要过滤的类型数字
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        String sptwFilterType = userInfoScope.getParam( "sptwFilterType" );
        String sptwType = null;
        if(!StringUtil.isEmpty( sptwFilterType )){
            sptwType = siteid.toLowerCase()+"_ptw_std_type_"+sptwFilterType;
        }
        
        
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        pageVo.setParameter( "sptwType", sptwType );
        pageVo.setParameter( "onRunning", "Y" );
        pageVo.setParameter( "nowTime", new Date() );
        
        List<PtwStandardBean> ptwStandardBeans = new ArrayList<PtwStandardBean>();
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setParameter( "sort", sort );
            pageVo.setParameter( "order", order );
        }
        
        //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            HashMap<String, Object> map = JsonHelper.toObject( search, HashMap.class );
            String eqId = (String)map.get( "eqId" );
            //查询树当前节点及其子节点所有的标准工作票
            if( StringUtils.isNotBlank( eqId ) ){
                ptwStandardBeans = ptwStandardService.queryFinishPtwStandardByEqId( eqId, pageVo );
            }else{
                //流程枚举
                String flowStatus = (String)map.get( "_flowStatus" );
                if( StringUtils.isNotBlank( flowStatus ) ){
                    map.put( "status", flowStatus );
                }
                String sptwType1 = (String)map.get( "_wtTypeId" );
                if( StringUtils.isNotBlank( sptwType1 ) && !"null".equals( sptwType1 )){
                    map.put( "sptwType", sptwType1 );
                }
                ptwStandardBeans = ptwStandardService.queryFinishPtwStandardBySearch( map, pageVo );
            }
            
        }else{
            //默认分页
            ptwStandardBeans = ptwStandardService.queryFinishPtwStandardBySiteId( pageVo );
        }
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", ptwStandardBeans );
        if( ! ptwStandardBeans.isEmpty() ){
            dataMap.put( "total",  pageVo.getTotalRecord() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
        
    }
    
    /**
     * 
     * @description:插入、更新 for 暂存
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @return: Map<String, Object>
     * @throws JSONException 
     */
    @RequestMapping("/insertOrUpdatePtwStandard")
    public @ResponseBody Map<String, Object> insertOrUpdatePtwStandard( String formData, String safeItems ) throws JSONException{
        log.info( "insertOrUpdatePtwStandard params formData = " + formData + "---- safeitems = " + safeItems );
        
        //{"safeType":"1","content":"修改安全措施步骤控件0011","showOrder":1}转成{"safeType":"1","safeContent":"修改安全措施步骤控件0011","safeOrder":1}
        JSONArray myJsonArray = new JSONArray(safeItems);
        safeItems = propertyReplace(myJsonArray);
        Map<String,Object> resultMap = ptwStandardService.insertOrUpdatePtwStandard( formData, safeItems );
        
        return resultMap;
    }
    
    private String propertyReplace(JSONArray myJsonArray) throws JSONException {
        for(int i=0 ; i < myJsonArray.length() ;i++){
          //获取每一个JsonObject对象
            JSONObject myjObject = myJsonArray.getJSONObject(i);
            //获取每一个对象中的值
            String safeContent = myjObject.getString("content");
            int safeOrder = myjObject.getInt("showOrder");
            myjObject.remove( "content" );
            myjObject.remove( "showOrder" );
            myjObject.put( "safeContent", safeContent );
            myjObject.put( "safeOrder", safeOrder );
        }
        String temp = myJsonArray.toString();
        return temp;
    }

    /**
     * 
     * @description:新建插入页面---修改按钮
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @return: Map<String, Object>
     * @throws Exception 
     */
    @RequestMapping("/insertPtwStdPageForModify")
    @ReturnEnumsBind("ptw_standard_type")
    public ModelAndView insertPtwStdPageForModify( String id ) throws Exception{
        
        log.info( "insertPtwStdPageForModify params id = " + id );
        /*PtwStandardBean bean = JsonHelper.fromJsonStringToBean( formData, PtwStandardBean.class );
        List<PtwStdSafeBean> items = VOUtil.fromJsonToListObject( safeItems, PtwStdSafeBean.class );*/
        PtwStandardBean bean = ptwStandardService.queryPtwStandardById( id );
//        bean.setParentWtId( bean.getId() );
        bean.setId( null );
        List<PtwStdSafeBean> items = ptwStandardService.queryPtwStdSafeByWtId( id );
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "bean", JsonHelper.fromBeanToJsonString( bean ) );
        map.put( "items", JsonHelper.fromBeanToJsonString( items ) );
        map.put( "siteId", getUserInfoScope().getSiteId() );
        //获得登录用户可以建的标准工作票列表
        List<String> newTypePrivList = getUserNewSptwTypeList("sptw","dev_dept_write");
        
        map.put( "privTypes",JsonHelper.fromBeanToJsonString( newTypePrivList ));
        
        return new ModelAndView( "insertPtwStandard.jsp", map );
    }
    
    
    
    private List<String> getUserNewSptwTypeList(String category,String step) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
      //查询用户有新建哪些类型标准工作票的权限
        List<String> newTypePrivConfigList = ptwPtoSelectUserService.queryPrivilegeTypes( category, step );
        List<String> newTypePrivList = new ArrayList<String>();
        //将库中配置的类型转换成页面的类型
        List<PtwType> ptwTypeList = ptwTypeService.queryTypesBySiteId( siteid, 0 );
        for ( int i = 0; i < newTypePrivConfigList.size(); i++ ) {
            //组装成类似：swf_ptw_std_type_36
            for ( int j = 0; j <ptwTypeList.size(); j++ ) {
                if(newTypePrivConfigList.get( i ).equals( ptwTypeList.get( j ).getTypeCode() )){
                    newTypePrivList.add( siteid.toLowerCase()+"_ptw_std_type_"+ptwTypeList.get( j ).getId() );
                }
            }
        }
        return newTypePrivList;
    }

    @RequestMapping("/queryNewSptwPriv")
    public  @ResponseBody Map<String, Object> queryNewSptwPriv() throws Exception{
        
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String type = userInfoScope.getParam( "sptwTypeVal" );
        int indexOfLine = Integer.valueOf( type.lastIndexOf( "_" ) );
        String typeIdString = type.substring( indexOfLine + 1 );
        PtwType  ptwType = ptwTypeService.queryPtwTypeById( Integer.valueOf( typeIdString ) );
        type = ptwType.getTypeCode();
        
        //判断是否拥有新建此类标准工作票的权限
        boolean flag = ptwPtoSelectUserService.hasAuditPrivilege( "sptw", type, "dev_dept_write" );
       
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "hasNewPriv", flag );
        
        return  map ;
    }
    
    
    /**
     * 
     * @description:工作票复制成标准票
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @return: Map<String, Object>
     */
    @RequestMapping("/copyPtwToStdPtwPage")
    @ReturnEnumsBind("ptw_standard_type")
    public ModelAndView copyPtwToStdPtwPage( int ptwId )  throws Exception{
        
        log.info( "copyPtwToStdPtwPage params ptwId = " + ptwId );
        
        //工作票基础信息
        PtwInfo ptwInfo = ptwInfoService.queryPtwInfoById( ptwId ); 
        //工作票隔离措施的信息
        List<PtwSafe> ptwSafes = ptwSafeService.queryPtwSafeListByWtId( ptwId );
        
        //构造标准票的bean
        PtwStandardBean bean = toInitPtwStandardBean( ptwInfo );
        
        //构造标准票的隔离措施
        List<PtwStdSafeBean> items = toInitPtwStdSafeBean( ptwSafes );
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "bean", JsonHelper.fromBeanToJsonString( bean ) );
        map.put( "items", JsonHelper.fromBeanToJsonString( items ) );
        map.put( "siteId", getUserInfoScope().getSiteId() );
        //获得登录用户可以建的标准工作票列表
        List<String> newTypePrivList = getUserNewSptwTypeList("sptw","dev_dept_write");
        map.put( "privTypes",JsonHelper.fromBeanToJsonString( newTypePrivList ));
        
        return new ModelAndView( "insertPtwStandard.jsp", map );
    }
    
    /**
     * 
     * @description:隔离措施构造成标准的隔离措施
     * @author: fengzt
     * @createDate: 2015年8月13日
     * @param ptwSafes
     * @return:List<PtwStdSafeBean>
     */
    private List<PtwStdSafeBean> toInitPtwStdSafeBean(List<PtwSafe> ptwSafes) {
        String siteId = getUserInfoScope().getSiteId();
        siteId = siteId.toLowerCase();
        
        //标准工作票的隔离措施
        List<PtwStdSafeBean> ptwStdSafeBeans = new ArrayList<PtwStdSafeBean>();
        
        //遍历标准工作票的隔离措施   ptw to stdPtw
        for( PtwSafe ptwSafe : ptwSafes ){
            //必须采取的安全措施 
            if( 1 == ptwSafe.getSafeType() ){
                PtwStdSafeBean bean = new PtwStdSafeBean();
                bean.setSafeContent( ptwSafe.getSafeContent() );
                bean.setSafeType( siteId + "_ptw_safe_type_" + ptwSafe.getSafeType() );
                bean.setSafeOrder( ptwSafe.getSafeOrder() );
                ptwStdSafeBeans.add( bean );
            }
        }
        
        return ptwStdSafeBeans;
    }


    /**
     * 
     * @description:工作票bean转换成标准工作票bean
     * @author: fengzt
     * @createDate: 2015年8月13日
     * @param ptwInfo
     * @return:
     */
    private PtwStandardBean toInitPtwStandardBean(PtwInfo ptwInfo) {
        String siteId = getUserInfoScope().getSiteId();
        siteId = siteId.toLowerCase();
        
        PtwStandardBean bean = new PtwStandardBean();
        
        bean.setWtTypeId( siteId + "_ptw_std_type_" + ptwInfo.getWtTypeId() );
        bean.setEqNo( ptwInfo.getEqId() );
        bean.setEqName( ptwInfo.getEqName() );
        
        bean.setWorkContent( ptwInfo.getWorkContent() );
        bean.setWorkPlace( ptwInfo.getWorkPlace() );
        return bean;
    }


    /**
     * 
     * @description:保存 有流程 
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @return: Map<String, Object>
     * @throws JSONException 
     */
    @RequestMapping("/insertPtwStandard")
    public @ResponseBody Map<String, Object> insertPtwStandard( String formData, String safeItems ) throws JSONException{
        
        log.info( "insertPtwStandard params formData = " + formData + "---- safeitems = " + safeItems );
        JSONArray myJsonArray = new JSONArray(safeItems);
        safeItems = propertyReplace(myJsonArray);
        Map<String,Object> map = ptwStandardService.insertPtwStandardAll( formData, safeItems );
        
        Object taskId = map.get( "taskId" );
        if ( taskId  != null && StringUtils.isNotBlank( taskId.toString() ) ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * 
     * @description:通过ID查询标准工作票的信息 - FOR ALL 
     * @author: fengzt
     * @createDate: 2015年7月22日
     * @param formData
     * @return: Map<String, Object>
     */
    @RequestMapping("/queryPtwStandardInfo")
    public @ResponseBody Map<String, Object> queryPtwStandardInfo( String id ){
        
        log.info( "queryPtwStandardInfo params id = " + id );
        Map<String,Object> map = new HashMap<String, Object>();
        //通过ID查找相关信息
        PtwStandardBean bean = ptwStandardService.queryPtwStandardById( id );
        List<PtwStdSafeBean> items = ptwStandardService.queryPtwStdSafeByWtId( id );
        //当前登录人
        String currentUser = getUserInfoScope().getUserId();
        map.put( "currentUser", currentUser );
        
        List<Task> activities = new ArrayList<Task>();
        if( StringUtils.isNotBlank( bean.getInstantId()  )){
            activities = workflowService.getActiveTasks( bean.getInstantId() );
        }
        
        if( !activities.isEmpty() ){
            Task task = activities.get(0);
            map.put( "taskId", task.getId() );
            
            //拿到审批人的列表
            List<String> userList = workflowService.getCandidateUsers( task.getId() );
            
            //判断是否具有审批状态
            String applyFlag = null;
            if( userList.contains( currentUser )){
                applyFlag ="approver";
            }else{
                applyFlag = "others";
            }
            
            map.put( "applyFlag", applyFlag );
        }
        
        if( bean != null && StringUtils.isNotBlank( bean.getId() ) ){
            map.put( "result", "success" );
            map.put( "bean", bean );
            map.put( "items", items );
        }else{
            map.put( "result", "fail" );
        }
        return map;
    }
    
    
    @RequestMapping(value = "/sameCodeSptwListData", method = RequestMethod.POST)
    public Page<PtwStandardBean> sameCodeSptwListData() throws JsonParseException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        String wtNo = userInfoScope.getParam( "wtNo" );
        String sptwId = userInfoScope.getParam( "id" );
        Page<PtwStandardBean> page = userInfoScope.getPage();
        page.setParameter( "wtNo", wtNo );
        page.setParameter( "id", sptwId );
        page.setParameter( "siteid", siteid );
        Page<PtwStandardBean> sameCodeSptwList = ptwStandardService.querySameCodeSptwList(page );
        return sameCodeSptwList;
    }
    
    
    @RequestMapping(value = "/hasSameCodeSptwInAudit", method = RequestMethod.POST)
    public Map<String, Object> hasSameCodeSptoInAudit() throws JsonParseException, Exception {
        Map<String, Object> mav = new HashMap<String, Object>();
        mav.put( "result", false );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        String sptwCode = userInfoScope.getParam( "sptwCode" );
        String sptwId = userInfoScope.getParam( "id" );
        if("".equals( sptwId )){
            sptwId = null ;
        }
        Page<PtwStandardBean> page = userInfoScope.getPage();
        int pageSize = page.getPageSize();
        page.setPageSize( 100 );  //设置100条记录
        page.setParameter( "wtNo", sptwCode );
        page.setParameter( "id", sptwId );
        page.setParameter( "siteid", siteid );
        Page<PtwStandardBean> sameCodeSptwList =  ptwStandardService.querySameCodeSptwList(page );
        page.setPageSize( pageSize );  //恢复数据
        List<PtwStandardBean> sptwList = sameCodeSptwList.getResults();
        for ( PtwStandardBean sptwInfoBean : sptwList ) {
            String sptwStatus = sptwInfoBean.getFlowStatus();
            String inAuditStatusSetString = siteid.toLowerCase()+"_flow_std_status_1,"+
                                            siteid.toLowerCase()+"_flow_std_status_2"+
                                            siteid.toLowerCase()+"_flow_std_status_3";
            if(inAuditStatusSetString.indexOf( sptwStatus ) >= 0 ){
                //非 已通过、作废 、草稿状态，即有在审批中的同编号的标准票
                mav.put( "result", true );
                return mav;
            }
        }
        
        return mav;
    }
    
    
    
    /**
     * 
     * @description:保存 有流程 
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @return: Map<String, Object>
     */
    @RequestMapping("/deletePtwStandardBaseInfo")
    public @ResponseBody Map<String, Object> deletePtwStandardBaseInfo( String id, String flag ){
        
        log.info( "deletePtwStandardBaseInfo params id = " + id + "---- flag = " + flag );
        Map<String,Object> map = new HashMap<String, Object>();
        int count = ptwStandardService.deletePtwStandardBaseInfo( id, flag );
        
        if( count > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        return map;
    }
    
    
    
    /**
     * @description:设置有效时间
     * @author: 王中华
     * @createDate: 2016-4-18
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/setValidTimeData")
    public  Map<String, Object>  setValidTimeData() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = userInfoScope.getParam( "id" );// 获取前台传过来的form表单
        String beginTimeStr = userInfoScope.getParam( "beginTime" );
        String endTimeStr = userInfoScope.getParam( "endTime" );
        Date beginTime = new Date(  Long.parseLong(beginTimeStr) );
        Date endTime = new Date(  Long.parseLong(endTimeStr) );
        Map<String, Object> mav = new HashMap<String, Object>();
        int flag = ptwStandardService.updateValidTime(id,beginTime,endTime);
        if(flag > 0){
            mav.put( "result", "success" );
        }else{
            mav.put( "result", "fail" );
        }
        return mav;
    }
    
    
    /**
     * @description:检查设置的有效时间是否合法
     * @author: 王中华
     * @createDate: 2016-4-18
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/checkValidTimeData")
    public  Map<String, Object>  checkValidTimeData() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String sptwId = userInfoScope.getParam( "id" );
        if("".equals( sptwId )){
            sptwId = null;
        }
        String wtNo = userInfoScope.getParam( "wtNo" );// 获取前台传过来的form表单
        String beginTimeStr = userInfoScope.getParam( "beginTime" );
        String endTimeStr = userInfoScope.getParam( "endTime" );
        Date beginTime = new Date(  Long.parseLong(beginTimeStr) );
        Date endTime = new Date(  Long.parseLong(endTimeStr) );
        Map<String, Object> mav = new HashMap<String, Object>();
        boolean flag = ptwStandardService.checkValidTimeData(sptwId,wtNo,beginTime,endTime);
        mav.put( "result", flag );
        return mav;
    }
    
    
}
