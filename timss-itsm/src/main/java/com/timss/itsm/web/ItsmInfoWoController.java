package com.timss.itsm.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.itsm.bean.ItsmInfoWo;
import com.timss.itsm.bean.ItsmInfoWoEquipment;
import com.timss.itsm.service.ItsmInfoWoEquipmentService;
import com.timss.itsm.service.ItsmInfoWoService;
import com.timss.itsm.util.ItsmConstant;
import com.timss.itsm.util.ItsmInfoWoStatus;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;


/**
 * @title: {title}
 * @description: {desc}电厂信息工单
 * @company: gdyd
 * @className: ItsmInfoWoController.java
 * @author: 王中华
 * @createDate: 2016-10-31
 * @updateUser: 王中华
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "/itsm/infoWo")
public class ItsmInfoWoController {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmInfoWoService itsmInfoWoService;
    @Autowired
    private ItsmInfoWoEquipmentService itsmInfoWoEquipmentService;
    @Autowired
    private IAuthorizationManager iAuthorizationManager;
   
    
    private static final Logger logger = Logger.getLogger( ItsmInfoWoController.class );
    
    @RequestMapping(value = "/infoWoListPage", method = RequestMethod.GET)
    @ReturnEnumsBind("ITSM_INFOWO_SERTYPE,ITSM_INFOWO_STATUS")
    public String infoWoListPage() throws Exception {
        return "/infoWo/infoWoList.jsp";
    }
    
    @RequestMapping(value = "/infoWoListData")
    @ReturnEnumsBind("ITSM_INFOWO_SERTYPE")
    public Page<ItsmInfoWo> infoWoListData() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<ItsmInfoWo> page = userInfoScope.getPage();

        String fuzzySearchParams = userInfoScope.getParam( "search" );
        Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "itsmInfoWoMap",
                ItsmConstant.MODULENAME, "ItsmInfoWoDao" );

        if ( fuzzySearchParams != null ) {
            Map<String, Object> fuzzyParams = (HashMap<String, Object>) MapHelper.jsonToHashMap( fuzzySearchParams );
            fuzzyParams = MapHelper.fromPropertyToColumnMap( fuzzyParams, propertyColumnMap );
            page.setFuzzyParams( fuzzyParams );
        }

        // 设置排序内容
        if ( userInfoScope.getParamMap().containsKey( "sort" ) ) {
            String sortKey = userInfoScope.getParam( "sort" );
            // 如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
            sortKey = propertyColumnMap.get( sortKey );
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        } else {
            // 设置默认的排序字段
            page.setSortKey( "CREATEDATE" );
            page.setSortOrder( "desc" );
        }
        Map<String , Object> params = new HashMap<String , Object>();
        params.put( "siteid", userInfoScope.getSiteId() );
        page.setParams( params );
        
        page = itsmInfoWoService.queryItsmInfoWoList( page );
        return page;
    }
    
    
    @RequestMapping(value = "/newInfoWoPage", method = RequestMethod.GET)
    @ReturnEnumsBind("ITSM_INFOWO_SERTYPE,ITSM_INFOWO_BUSINESSTYPE")
    public ModelAndView openItsmSubWoPage() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        //登录用户
        String userId = userInfoScope.getUserId();
        String userName = userInfoScope.getUserName();
        String deptId = userInfoScope.getOrgId();
        String deptName = userInfoScope.getOrgName();
        SecureUser  SecureUser  = iAuthorizationManager.retriveUserById(userId,siteId);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "userId", userId );
        map.put( "userName", userName );
        map.put( "deptId", deptId );
        map.put( "deptName", deptName );
        map.put( "siteId", siteId );
        map.put( "phone", SecureUser.getOfficeTel() );
        
        ModelAndView modelAndView = new ModelAndView( "/infoWo/newInfoWo.jsp", map );
        return modelAndView;
    }


    @RequestMapping(value = "/openInfoWoPage", method = RequestMethod.GET)
    @ReturnEnumsBind("ITSM_INFOWO_STATUS,ITSM_INFOWO_SERTYPE,ITSM_INFOWO_BUSINESSTYPE,ITSM_INFOWO_HANDLERTYPE")
    public ModelAndView openItsmSubWoPageById() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String infoWoId = userInfoScope.getParam( "id" );// 获取前台传过来的form表单数据
        ItsmInfoWo infoWo = itsmInfoWoService.queryItsmInfoWoById( infoWoId );
        String infoWoStatus = infoWo.getStatus();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "id", infoWoId );
        String urlString = "/infoWo/infoWoInfo.jsp";
        if("draft".equals( infoWoStatus )||"newApply".equals( infoWoStatus )){
            
            urlString = "/infoWo/newInfoWo.jsp";
        }
             
        ModelAndView modelAndView = new ModelAndView( urlString, map );
        
        return modelAndView;
    }
    
    
    
    @RequestMapping(value = "/commitInfoWodata", method = RequestMethod.POST)
    public Map<String, String> commitInfoWodata() throws Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String infoWoFormDate = userInfoScope.getParam( "infoWoForm" );// 获取前台传过来的form表单数据
        String commitStyle = userInfoScope.getParam( "commitStyle" );// 提交方式（用于确定是点击的“提交”还是“暂存”）
        ItsmInfoWo itsmInfoWo = JsonHelper.toObject( infoWoFormDate, ItsmInfoWo.class );
        setCommonValue(itsmInfoWo);
        itsmInfoWo.setCurrHandler( userInfoScope.getUserId() );
        itsmInfoWo.setCurrHandlerName( userInfoScope.getUserName() );
        String infoWoId = itsmInfoWo.getId();
        String infoWoCode = itsmInfoWo.getInfoWoCode();
        String workflowId = itsmInfoWo.getWorkflowId();
        
        String taskId = "noFlow";
        if ( "".equals( workflowId ) || workflowId == null ) { // 确定不是因为回退的节点
            if ( "".equals( infoWoId ) || infoWoId == null  ) { // 初次提交或暂存
                itsmInfoWo.setId( null );
                if ( "commit".equals( commitStyle ) ) { // 提交，启动流程
                    itsmInfoWoService.insertItsmInfoWoWithFlow( itsmInfoWo, userInfoScope);
                    taskId = itsmInfoWo.getTaskId();
                    infoWoId = itsmInfoWo.getId();
                    infoWoCode = itsmInfoWo.getInfoWoCode();
                    workflowId = itsmInfoWo.getWorkflowId();
                    logger.info( "-------------web层：信息工单提交完成,工单编号：" +infoWoCode + "  工单流程：" + workflowId + "-------------" );
                } else if ( "save".equals( commitStyle ) ) { // 暂存，不启动流程
                    itsmInfoWoService.saveItsmInfoWo( itsmInfoWo , userInfoScope);
                    infoWoId = itsmInfoWo.getId();
                    infoWoCode = itsmInfoWo.getInfoWoCode();
                    logger.info( "-------------web层：工单暂存完成,工单编号：" +itsmInfoWo.getInfoWoCode()+ "-------------" );
                }
            } else { // 对暂存的工单提交 或 再次暂存 或其他状态下的提交
                if ( "commit".equals( commitStyle ) ) { // 提交，启动流程
                    itsmInfoWoService.updateItsmInfoWoAndStartFlow( itsmInfoWo , userInfoScope);
                    taskId = itsmInfoWo.getTaskId();
                    infoWoId = itsmInfoWo.getId();
                    infoWoCode = itsmInfoWo.getInfoWoCode();
                    workflowId = itsmInfoWo.getWorkflowId();
                } else if ( "save".equals( commitStyle ) ) { // 再次暂存，不启动流程
                    itsmInfoWoService.updateItsmInfoWo(itsmInfoWo);
                }
                logger.info( "-------------web层：暂存的工单再次提交或暂存完成,工单流程：" + "-------------" );
            }
        }else{  //启动流程之后的暂存
            itsmInfoWoService.updateItsmInfoWo( itsmInfoWo );
        }
        
        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        if ( "save".equals( commitStyle ) ) {
            taskId = "noFlow";
        }
        mav.put( "taskId", taskId );
        mav.put( "infoWoId", String.valueOf( infoWoId ) );
        mav.put( "infoWoCode", infoWoCode );
        mav.put( "workflowId", workflowId );
        return mav;
    }

    private void setCommonValue(ItsmInfoWo itsmInfoWo) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        String deptid = userInfoScope.getOrgId();
        String userid = userInfoScope.getUserId();        
        itsmInfoWo.setCreateuser( userid );
        itsmInfoWo.setCreatedate( new Date() );
        itsmInfoWo.setModifydate( new Date() );
        itsmInfoWo.setModifyuser( userid );
        itsmInfoWo.setSiteid( siteid );
        itsmInfoWo.setDeptid( deptid );
        itsmInfoWo.setStatus( ItsmInfoWoStatus.DRAFT.getEnName() );
        itsmInfoWo.setDelFlag( "N" );
        
    }

    @RequestMapping(value = "/queryInfoWoById")
    public @ResponseBody
    Map<String, Object> queryInfoWoById() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String infoWoId = userInfoScope.getParam( "id" ) ;
        ItsmInfoWo infoWo = itsmInfoWoService.queryItsmInfoWoById( infoWoId );
        String taskId = infoWo.getTaskId();
        
        List<ItsmInfoWoEquipment> equipmentList =  itsmInfoWoEquipmentService.queryItsmInfoWoEquipmentList( infoWoId );
        //附件
        String attachmentIds =  infoWo.getUploadIds();
        List<String> aList = new ArrayList<String>();
        if(attachmentIds!= null && !"".equals( attachmentIds )){
            String[] attachmentArray = attachmentIds.split( "," );
            aList = Arrays.asList(attachmentArray);  
        }
        List<Map<String, Object>> attachmentMap = FileUploadUtil.getJsonFileList( Constant.basePath, aList );
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put( "attachmentMap", attachmentMap );

        resultMap.put( "infoWoForm", JsonHelper.toJsonString( infoWo ) );
        resultMap.put( "equipmentList",  JsonHelper.toJsonString( equipmentList));
        
        resultMap.put( "taskId", taskId );

        return resultMap;

    }
    
    
    
    @RequestMapping(value = "/obsoleteInfoWo", method = RequestMethod.POST)
    public Map<String, String> obsoleteInfoWo() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String infoWoId = userInfoScope.getParam( "id" );

        itsmInfoWoService.obsoleteItsmInfoWo( infoWoId , userInfoScope);

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }
    
    
    @RequestMapping(value = "/deleteInfoWo", method = RequestMethod.POST)
    public Map<String, String> deleteInfoWo() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String infoWoId = userInfoScope.getParam( "id" );

        itsmInfoWoService.deleteItsmInfoWo( infoWoId );

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }
}
