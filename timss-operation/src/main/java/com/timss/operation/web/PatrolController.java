package com.timss.operation.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.operation.bean.Jobs;
import com.timss.operation.bean.Patrol;
import com.timss.operation.service.PatrolService;
import com.timss.operation.util.DateFormatUtil;
import com.timss.operation.util.ProcessStatusUtil;
import com.timss.operation.util.VOUtil;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.bean.userinfo.impl.UserInfoScopeImpl;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 点检controller
 * @description:
 * @company: gdyd
 * @className: PatrolController.java
 * @author: fengtw
 * @createDate: 2015年10月29日
 * @updateUser: fengtw
 * @version: 1.0
 */
@Controller
@RequestMapping("operation/patrol")
public class PatrolController {
    private Logger log = Logger.getLogger( PatrolController.class );
    
    @Autowired
    private PatrolService patrolService;

    @Autowired
    private ItcMvcService itcMvcService;
    
    public PatrolService getPatrolService() {
        return patrolService;
    }

    public void setPatrolService(PatrolService patrolService) {
        this.patrolService = patrolService;
    }

    
    /**
     * 
     * @description:打开新建点检日志页面
     * @author: fengtw
     * @createDate: 2015年10月30日
     * @return:String
     */
    @RequestMapping("/insertPatrolPage")
    @ReturnEnumsBind("OPR_SPECIALITY")
    public ModelAndView insertPatrolPage(){
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        
        String userId = userInfoScope.getUserId();
        String userName = userInfoScope.getUserName();
        String deptId = userInfoScope.getOrgId();
        String deptName = userInfoScope.getOrgName();
        String siteId = userInfoScope.getSiteId();
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "userId", userId );
        dataMap.put( "userName", userName );
        dataMap.put( "deptId", deptId );
        dataMap.put( "deptName", deptName );
        dataMap.put( "siteId", siteId );
        dataMap.put( "today", DateFormatUtil.getCurrentDate() );
        dataMap.put( "draftStr", ProcessStatusUtil.DRAFT_STR );
        dataMap.put( "applyStr", ProcessStatusUtil.APPLY_STR );
        dataMap.put( "auditStr", ProcessStatusUtil.AUDIT_STR );
        dataMap.put( "doneStr", ProcessStatusUtil.DONE_STR );
        dataMap.put( "invalidStr", ProcessStatusUtil.INVALID_STR );
        return new ModelAndView( "operationlog/Patrol-insertPatrol.jsp", dataMap );
    }
 
    /**
     * @description:新建/更新点检日志
     * @author: fengtw
     * @createDate: 2015年10月30日
     * @param formData JSON String
     * @return:Map<String, Object>
     */
    @RequestMapping("/insertOrUpdatePatrol")
    @ValidFormToken
   public @ResponseBody Map<String, Object> insertOrUpdatePatrol(String formData) {
        Map<String, Object> resultMap = patrolService.insertOrUpdatePatrol( formData );
        int count = (Integer)resultMap.get( "count" );
        Map<String, Object> map = new HashMap<String, Object>();
        if ( count > 0 ) {
            map.put( "result", "success" );
            map.put( "rowData", resultMap.get( "patrolBean" ) );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }

    /**
     * 
     * @description:提交点检日志
     * @author: fengtw
     * @createDate: 2015年11月3日
     * @param formData
     * @return:Map<String, Object>
     */
    @RequestMapping("/insertPatrol")
    @ValidFormToken
    public Map<String, Object> insertPatrol( String formData ){
        Map<String, Object> map = patrolService.insertPatrol( formData );

        if (StringUtils.isNotBlank( map.get( "taskId" ).toString() )  ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * @description:删除点检日志
     * @author: fengtw
     * @createDate: 2015年10月30日
     * @param patrolId
     * @return:Map<String, Object>
     */
    @RequestMapping("/deletePatrol")
    public Map<String, Object> deletePatrol(String patrolId) {
        int count = 0 ;
        count = patrolService.deletePatrolById( patrolId );
        Map<String, Object> map = new HashMap<String, Object>();
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }

    /**
     * 
     * @description:打开编辑点检日志页面
     * @author: fengtw
     * @createDate: 2015年11月3日
     * @return:String
     */
    @RequestMapping("/updatePatrolPage")
    @ReturnEnumsBind("OPR_SPECIALITY,OPR_PATROL_STATUS")
    public ModelAndView updatePatrolPage(String patrolId){
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        
        String userId = userInfoScope.getUserId();
        String userName = userInfoScope.getUserName();
        String deptId = userInfoScope.getOrgId();
        String deptName = userInfoScope.getOrgName();
        String siteId = userInfoScope.getSiteId();
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "patrolId", patrolId );
        dataMap.put( "userId", userId );
        dataMap.put( "userName", userName );
        dataMap.put( "deptId", deptId );
        dataMap.put( "deptName", deptName );
        dataMap.put( "siteId", siteId );
        dataMap.put( "draftStr", ProcessStatusUtil.DRAFT_STR );
        dataMap.put( "applyStr", ProcessStatusUtil.APPLY_STR );
        dataMap.put( "auditStr", ProcessStatusUtil.AUDIT_STR );
        dataMap.put( "doneStr", ProcessStatusUtil.DONE_STR );
        dataMap.put( "invalidStr", ProcessStatusUtil.INVALID_STR );
        return new ModelAndView( "operationlog/Patrol-updatePatrol.jsp", dataMap );
    }
 
    /**
     * @description:更新点检日志
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/updatePatrol") 
    public Map<String, Object> updatePatrol(String formData) {

        Patrol patrol = VOUtil.fromJsonToVoUtil( formData, Patrol.class );

       int count = patrolService.updatePatrol( patrol );

        Map<String, Object> map = new HashMap<String, Object>();

        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;

    }
   
    /**
     * @description:点检日志列表 分页
     * @author: fengtw
     * @createDate: 2015年10月29日
     * @param rows 一页有多少条
     * @param page 第几页
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryAllPatrolList")
    public Map<String, Object> queryAllPatrolList(int rows, int page, String search ) {
        
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        List<Patrol> patrolList = new ArrayList<Patrol>();
        //高级搜索
        HashMap<String, Object> map = null;
        if(StringUtils.isNotBlank(search)){
        	map = new HashMap<String, Object>();
            map.put("search", search);
            patrolList = patrolService.queryPatrolBySearch( map, pageVo );
        }
        else{
            patrolList = patrolService.queryPatrolByPage(pageVo);
        }

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", patrolList );
        dataMap.put( "total", pageVo.getTotalRecord() );
        return dataMap;
    }
    
    /**
     * 
     * @description:通过主键ID查找点检日志
     * @author: fengzt
     * @createDate: 2014年7月8日
     * @param id
     * @return:map
     */
    @RequestMapping("/queryPatrolById")
    public @ResponseBody Map<String, Object> queryPatrolById( String patrolId ){
        Map<String, Object> map = patrolService.queryPatrolById( patrolId );
        
        if( !map.isEmpty() ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }
    
    /**
     * 
     * @description:作废流程
     * @author: fengtw
     * @createDate: 2015年11月4日
     * @param formData
     * @return: Map<String, Object>
     */
    @RequestMapping(value="/invalidPatrol")
    public Map<String, Object> invalidPatrol( String patrolId ){
        
        int count = patrolService.invalidPatrol( patrolId ) ;
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
}
