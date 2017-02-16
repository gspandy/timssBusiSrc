package com.timss.ptw.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timss.ptw.bean.PtwPtoStepInfo;
import com.timss.ptw.bean.PtwPtoUserInfoConfig;
import com.timss.ptw.service.AttachMatchService;
import com.timss.ptw.service.PtwPtoUserInfoService;
import com.timss.ptw.service.SptoInfoService;
import com.timss.ptw.vo.PtwPtoUserInfoVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: 两票审批人员信息的Controller
 * @description: 两票审批人员信息
 * @company: gdyd
 * @className: PtwPtoUserInfoController.java
 * @author: 谷传伟
 * @createDate: 2015-7-20
 * @updateUser: 谷传伟
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "ptw/userInfo")
public class PtwPtoUserInfoController {
    private static final Logger log = Logger.getLogger( PtwPtoUserInfoController.class );
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private SptoInfoService sptoInfoService;
    @Autowired
    private PtwPtoUserInfoService ptwPtoUserInfoService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private AttachMatchService attachMatchService;
    

    /**
     * @description:跳转至审批人员列表
     * @author: 谷传伟
     * @createDate: 2015-7-20
     * @return:
     * @throws Exception
     * @throws RuntimeException
     */
    @RequestMapping(value = "/preQueryPtwPtoUserList")
    @ReturnEnumsBind("PTW_TYPE,PTW_SPTO_TYPE,ptw_standard_type,PTW_MAJOR")
    public ModelAndView preQueryPtwPtoUserList() {
        Map<String, Object> map = new HashMap<String, Object>( 0 );
        Map<String,String> stepMap = new HashMap<String, String>(0);
        Map<String,String> stepEnumMap = new HashMap<String, String>(0);
        List<Map<String, String>> stepMapList = new ArrayList<Map<String,String>>( 0 );
        try {
            List<PtwPtoStepInfo> steps = ptwPtoUserInfoService.queryStepInfo(null);
            stepEnumMap.put( "null", "全部" );
            Map<String,String> stepTmpMap = new HashMap<String, String>(0);
            stepTmpMap.put( "null", "全部" );
            stepMapList.add( stepTmpMap );
            for ( PtwPtoStepInfo step : steps ) {
                if(!stepMap.containsKey( step.getStepName() )){
                    stepTmpMap = new HashMap<String, String>(0);
                    stepMap.put( step.getStepName(),step.getStepName() );
                    stepTmpMap.put( step.getStepName(),step.getStepName() );
                    stepMapList.add( stepTmpMap );
                }
                stepEnumMap.put( step.getId(),step.getStepName() );
            }   
        } catch (Exception e) {
            log.warn("获取流程环节异常:",e);
        }
        map.put( "ptwPtoUserInfoVo", JsonHelper.fromBeanToJsonString(new HashMap<String,Object>(0)) );
        //搜索框Map
        String stepMapStr = JsonHelper.fromBeanToJsonString(stepMapList);
        while (stepMapStr.contains( "},{" )) {
            stepMapStr = stepMapStr.replace( "},{", "," );
        }
        stepMapStr = stepMapStr.replace( "[", "" ).replace( "]", "" );
        map.put( "stepMap", stepMapStr );
        //表格数据映射Map
        map.put( "stepEnumMap", JsonHelper.fromBeanToJsonString(stepEnumMap) );
        ModelAndView modelAndView = new ModelAndView( "userinfo/userList.jsp",  map );
        return modelAndView;
    }   
    /**
     * @description:获取对应类别 类型的环节
     * @author: 谷传伟
     * @createDate: 2015-12-07
     * @return:ModelAndViewAjax
     * @throws Exception
     * @throws RuntimeException
     */
    @RequestMapping(value = "/getStepListWithCon")
    public ModelAndViewAjax getStepListWithCon() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String category = userInfoScope.getParam( "category" ).toString();
        String type = userInfoScope.getParam( "type" ).toString();
        String siteId = userInfoScope.getSiteId();
        PtwPtoStepInfo ptoStepInfoCon = new PtwPtoStepInfo();
        ptoStepInfoCon.setCategory( "null".equals( category )?null:category );;
        ptoStepInfoCon.setType( "null".equals( type )?null:type );
        ptoStepInfoCon.setSiteid( siteId );
        List<PtwPtoStepInfo> steps = ptwPtoUserInfoService.queryStepInfo(ptoStepInfoCon);
        List<List<String>> stepList = new ArrayList<List<String>>(0);
        Map<String, Object> stepMap = new HashMap<String, Object>(0);
        for ( PtwPtoStepInfo step : steps ) {
            if(!stepMap.containsKey( step.getStepName() )){
                List<String> stepTmpList = new ArrayList<String>(0);
                stepMap.put( step.getStepName(),step.getStepName() );
                stepTmpList.add( step.getStepName());
                if ( null == ptoStepInfoCon.getCategory() ) {
                    category = step.getCategory();
                    String categoryName = "spto".equals( category )?"(标准操作票)":("pto".equals( category )?"(操作票)":("sptw".equals( category )?"(标准工作票)":"ptw".equals( category )?"(工作票)":""));
                    stepTmpList.add( step.getStepName()+categoryName);
                }else {
                    stepTmpList.add( step.getStepName());
                }
                stepList.add( stepTmpList );
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put( "result", stepList );
        return itcMvcService.jsons( resultMap );
    }
    /**
     * @description: 打开新增审批人员配置信息页面
     * @author: gucw
     * @createDate: 2015-7-29
     * @return:
     */
    @RequestMapping(value = "/newUserInfo")
    @ReturnEnumsBind("PTW_TYPE,ptw_standard_type,PTW_SPTO_TYPE,PTW_MAJOR")
    public ModelAndView newUserInfo() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();        
        Map<String, Object> map = new HashMap<String, Object>( 0 );
        List<String> relatedorgsList = ptwPtoUserInfoService.queryRelatedOrgListBySiteId( siteId );
        Map<String, Object> relatedorgs = new HashMap<String, Object>(0);
        Map<String, Object> users = new HashMap<String, Object>(0);
        for ( String relatedorg : relatedorgsList ) {
            relatedorgs.put( relatedorg, true );
        }
        map.put( "id", "null" );
        map.put( "title", "" );
        map.put( "relatedorgs", JsonHelper.fromBeanToJsonString(relatedorgs) );
        map.put( "users", JsonHelper.fromBeanToJsonString(users) );
        map.put( "ptwPtoUserInfoVo", JsonHelper.fromBeanToJsonString(new HashMap<String, Object>(0)) );
        String jumpUrl = "userinfo/userInfo.jsp";
        ModelAndView modelAndView = new ModelAndView( jumpUrl, map );
        return modelAndView;
    }
    /**
     * @description: 打开编辑审批人员配置信息页面
     * @author: gucw
     * @createDate: 2015-7-30
     * @return:
     */
    @RequestMapping(value = "/editUserInfo")
    @ReturnEnumsBind("PTW_TYPE,ptw_standard_type,PTW_SPTO_TYPE,PTW_MAJOR")
    public ModelAndView editUserInfo() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        //获取人员初始化信息
        String siteId = userInfoScope.getSiteId();
        String configId = userInfoScope.getParam( "configId" );
        Map<String, Object> map = new HashMap<String, Object>( 0 );
        List<String> relatedorgsList = ptwPtoUserInfoService.queryRelatedOrgListBySiteId( siteId );
        PtwPtoUserInfoVo ptwPtoUserInfoVo = ptwPtoUserInfoService.queryUserInfoByConfigId( configId );
        List<String> userList = ptwPtoUserInfoVo.getUserList();
        Map<String, Object> relatedorgs = new HashMap<String, Object>(0);
        Map<String, Object> users = new HashMap<String, Object>(0);
        for ( String relatedorg : relatedorgsList ) {
            relatedorgs.put( relatedorg, true );
        }
        for ( String user : userList ) {
            users.put( user, "" );
        }
        map.put( "id", configId );
        map.put( "title", "" );
        map.put( "relatedorgs", JsonHelper.fromBeanToJsonString(relatedorgs) );
        map.put( "users", JsonHelper.fromBeanToJsonString(users) );
        map.put( "ptwPtoUserInfoVo", JsonHelper.fromBeanToJsonString(ptwPtoUserInfoVo) );
        String jumpUrl = "userinfo/userInfo.jsp";
        ModelAndView modelAndView = new ModelAndView( jumpUrl, map );
        return modelAndView;
    }
    /**
     * 
     * @Title:userSelect
     * @Description:跳转到用户选择页面
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/userSelect")
    public ModelAndView userSelect() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>( 0 );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String curTabId = userInfoScope.getParam( "curTabId" );
        map.put( "curTabId", curTabId );
        String jumpUrl = "userinfo/userSelect.jsp";
        ModelAndView modelAndView = new ModelAndView( jumpUrl, map );
        return modelAndView;
    }
    /**
     * @description: 查询类型和专业对应的环节列表
     * @author: gucw
     * @createDate: 2015-7-30
     * @return Page
     * @throws Exception
     * @throws JsonParseException
     * @throws Exception:
     */
    @RequestMapping(value = "/getStepList", method = RequestMethod.POST)
    public Map<String,Object> getStepList() throws JsonParseException, Exception {
        Map<String, Object> map = new HashMap<String, Object>(0);
        Map<String, Object> stepInfoMap = new HashMap<String, Object>(0);
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String type = userInfoScope.getParam( "type" );
        String category = userInfoScope.getParam( "category" );
        String siteid = userInfoScope.getSiteId();
        PtwPtoStepInfo ptwPtoStepInfo = new PtwPtoStepInfo();
        ptwPtoStepInfo.setType( type );
        ptwPtoStepInfo.setCategory( category );
        ptwPtoStepInfo.setSiteid( siteid );
        List<PtwPtoStepInfo> stepInfos = ptwPtoUserInfoService.queryStepInfo( ptwPtoStepInfo );
        for ( PtwPtoStepInfo stepInfo : stepInfos ) {
            stepInfoMap.put( stepInfo.getId(), stepInfo.getStepName() );
        }
        map.put( "stepList", JsonHelper.fromBeanToJsonString(stepInfoMap) );
        return map;
    }
    /**
     * @description: 查询两票审批人员列表数据
     * @author: gucw
     * @createDate: 2015-7-20
     * @return Page
     * @throws Exception
     * @throws JsonParseException
     * @throws Exception:
     */
    @RequestMapping(value = "/userListData", method = RequestMethod.POST)
    public Page<PtwPtoUserInfoConfig> userListData() throws JsonParseException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String siteId = userInfoScope.getSiteId();
        Page<PtwPtoUserInfoConfig> page = userInfoScope.getPage();
        String fuzzySearchParams = userInfoScope.getParam( "search" );
        Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "ptwPtoUserInfoConfigMap", "ptw", "PtwPtoUserInfoDao" );
        if ( fuzzySearchParams != null ) {
            Map<String, Object> fuzzyParams = (HashMap<String, Object>) MapHelper.jsonToHashMap( fuzzySearchParams );
            fuzzyParams = MapHelper.fromPropertyToColumnMap( fuzzyParams, propertyColumnMap );
            String stepCon = null!=fuzzyParams.get( "_stepName" )?fuzzyParams.get( "_stepName" ).toString():"";
            if ( StringUtils.isNotEmpty( stepCon ) ) {
                page.setParameter( "stepName", stepCon );
            }
            fuzzyParams.put( "_step", null );
            page.setFuzzyParams( fuzzyParams );
        }
        // 设置排序内容
        if ( userInfoScope.getParamMap().containsKey( "sort" ) ) {
            String sortKey = userInfoScope.getParam( "sort" );
            // 如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
            if ( "step".equals( sortKey ) ) {
                sortKey = "STEPNAME";
            }else {
                sortKey = propertyColumnMap.get( sortKey );
            }
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        } else {
            // 设置默认的排序字段
            page.setSortKey( "CATEGORY,TYPE,STEP,BEGINDATE" );
            page.setSortOrder( "desc" );
        }
        page.setParameter( "loginSiteid", siteId );
        page.setParameter( "loginUser", userId );
        page = ptwPtoUserInfoService.queryUserInfoConfig( page );
        return page;
    }
    
    /**
     * @description: 保存用户审批人员数据
     * @author: gucw
     * @createDate: 2015-7-30
     * @return
     * @throws Exception:
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/saveUserInfo", method = RequestMethod.POST)
    public Map<String, Object> saveUserInfo() throws Exception {
        Map<String, Object> mav = new HashMap<String, Object>(0);
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userInfoConfigForm = userInfoScope.getParam( "userInfoConfigForm" );// 获取前台传过来的form表单数据 
        String users = userInfoScope.getParam( "users" );//获取前台传过来的数据表格数据
        PtwPtoUserInfoVo ptwPtoUserInfoVo = JsonHelper.toObject( userInfoConfigForm, PtwPtoUserInfoVo.class );
        PtwPtoUserInfoConfig config = JsonHelper.toObject( userInfoConfigForm, PtwPtoUserInfoConfig.class );
        boolean isConflict = false;
        if ( StringUtils.isEmpty( config.getSiteid() ) ) {
            config.setSiteid( userInfoScope.getSiteId() );
        }
        isConflict = ptwPtoUserInfoService.isUserInfoConfigConflict( config );
        if(isConflict){
            //配置存在冲突
            mav.put( "result", "fail" );
            return mav;
        }
        Map<String,Object> usersMap = JsonHelper.toObject( users, HashMap.class );
        Set<String> keySet = usersMap.keySet();
        List<String> userList = new ArrayList<String>(0);
        for ( String key : keySet ) {
            if(StringUtils.isNotEmpty( key )){
                userList.add( key );   
            }
        }
        ptwPtoUserInfoVo.setUserList( userList );
        ptwPtoUserInfoService.saveOrUpdateUserInfo( ptwPtoUserInfoVo );
        mav.put( "result", "success" );
        return mav;
    }
}
