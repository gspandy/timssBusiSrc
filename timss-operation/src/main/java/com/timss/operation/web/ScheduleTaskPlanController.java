package com.timss.operation.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.timss.operation.bean.ScheduleTaskPlan;
import com.timss.operation.service.ScheduleTaskPlanService;
import com.timss.operation.util.OperationConstant;
import com.timss.operation.util.ReflectionUtils;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;


@Controller
@RequestMapping("operation/scheduleTaskPlan")
public class ScheduleTaskPlanController {
   @Autowired
   private ItcMvcService itcMvcService;
   @Autowired
   private ScheduleTaskPlanService taskPlanService;
    
    @RequestMapping("/scheduleTaskPlanListData")
    public Page<ScheduleTaskPlan> scheduleTaskPlanList() throws Exception {
        
          UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
          Page<ScheduleTaskPlan> page = userInfoScope.getPage();
    
          String fuzzySearchParams = userInfoScope.getParam( "search" );
          Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "scheduleTaskPlanMap",
                  OperationConstant.MODULENAME, "ScheduleTaskPlanDao" );
    
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
          
          page = taskPlanService.queryScheduleTaskPlanList( page );
          return page;
    }
    
    
    @RequestMapping("/openTaskPlanPage")
    @ReturnEnumsBind("OPR_SCHEDULE_TYPE,OPR_SCHEDULE_CYCLE")
    public ModelAndView openTaskPlanPage() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = userInfoScope.getParam( "id" );
        Map<String, Object> map = new HashMap<String, Object>();
        //通过JobId查询工种、岗位信息
        if(StringUtils.isBlank( id )){
            map.put( "id", null );
//            map.put( "taskPlan", "" ); 
        }else{
//            ScheduleTaskPlan taskPlan = taskPlanService.queryTaskPlanById( id );
            map.put( "id", id );
//            map.put( "taskPlan", JsonHelper.fromBeanToJsonString( taskPlan ) );
        }
        return new ModelAndView("scheduleTask/scheduleTaskPlan.jsp", map);
    }
    
    @RequestMapping("/queryTaskPlanById")
    public Map<String, String> queryTaskPlanById() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = userInfoScope.getParam( "id" );
        ScheduleTaskPlan taskPlan = taskPlanService.queryTaskPlanById( id );
        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        mav.put( "taskPlan", JsonHelper.fromBeanToJsonString( taskPlan ) );
        return mav;
    }
    
    
    @RequestMapping("/insertTaskPlan")
    @ValidFormToken
    public Map<String, String> insertTaskPlan() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String scheduleTaskPlanStr = userInfoScope.getParam( "scheduleTaskPlan" );
        ScheduleTaskPlan scheduleTaskPlan = JsonHelper.toObject( scheduleTaskPlanStr, ScheduleTaskPlan.class );
        ReflectionUtils.setTimssCommonFieldValue( scheduleTaskPlan, userInfoScope );
        Map<String, String> mav = new HashMap<String, String>();
        taskPlanService.insertScheduleTaskPlan( scheduleTaskPlan );
        String result = "fail";
        if(!StringUtils.isBlank( scheduleTaskPlan.getId())){
            result = "success";
            mav.put( "id", scheduleTaskPlan.getId() );
            mav.put( "code", scheduleTaskPlan.getCode() );
        }
        mav.put( "result", result );
        return mav;
    }

    @RequestMapping("/updateTaskPlan")
    public Map<String, String> updateTaskPlan() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String scheduleTaskPlanStr = userInfoScope.getParam( "scheduleTaskPlan" );
        ScheduleTaskPlan scheduleTaskPlan = JsonHelper.toObject( scheduleTaskPlanStr, ScheduleTaskPlan.class );
        Map<String, String> mav = new HashMap<String, String>();
        scheduleTaskPlan.setModifydate( new Date() );
        scheduleTaskPlan.setModifyuser( userInfoScope.getUserId() );
        int updateNum = taskPlanService.updateScheduleTaskPlan( scheduleTaskPlan);
        String result = "fail";
        if(updateNum > 0){
            result = "success";
        }
        mav.put( "result", result );
        return mav;
    }
    
    
    @RequestMapping("/delTaskPlanById")
    public Map<String, String> delTaskPlanById() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = userInfoScope.getParam( "id" );
       
        Map<String, String> mav = new HashMap<String, String>();
        int updateNum = taskPlanService.deleteScheduleTaskPlan( id);
        String result = "fail";
        if(updateNum > 0){
            result = "success";
        }
        mav.put( "result", result );
        return mav;
    }
    
}
