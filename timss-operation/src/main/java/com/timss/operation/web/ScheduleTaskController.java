package com.timss.operation.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import com.timss.operation.bean.PersonJobs;
import com.timss.operation.bean.ScheduleTask;
import com.timss.operation.bean.ScheduleTaskChangeShift;
import com.timss.operation.bean.Shift;
import com.timss.operation.service.DutyService;
import com.timss.operation.service.ScheduleTaskChangeShiftService;
import com.timss.operation.service.ScheduleTaskPlanService;
import com.timss.operation.service.ScheduleTaskService;
import com.timss.operation.service.ShiftService;
import com.timss.operation.util.DateFormatUtil;
import com.timss.operation.util.OperationConstant;
import com.timss.operation.util.ScheduleTaskDoStatus;
import com.timss.operation.vo.CalendarVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.Configuration;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.service.ItcSysConfService;
import com.yudean.workflow.process.ProcessDefKeyValidation.Result;
/**
 * 
 * @title: 运行模块目录
 * @description: {desc}
 * @company: gdyd
 * @className: MenuController.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("operation/scheduleTask")
public class ScheduleTaskController {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ScheduleTaskService scheduletaskService;
    @Autowired
    private ScheduleTaskPlanService scheduleTaskPlanService;
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private ScheduleTaskChangeShiftService taskChangeShiftService;
    @Autowired
    private DutyService dutyService;
    @Autowired
    private ItcSysConfService itcSysConfService;
    
    @RequestMapping("/todoScheduleTaskListData")
    public Page<ScheduleTask> todoScheduleTaskList() throws Exception {
        
          UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
          String queryType = userInfoScope.getParam( "queryType" );
          String  deptId= userInfoScope.getParam( "stationId" ); //工种
          String  shiftId= userInfoScope.getParam( "shiftId" ); //班次
          Page<ScheduleTask> page = userInfoScope.getPage();
          String fuzzySearchParams = userInfoScope.getParam( "search" );
          Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "scheduleTaskMap",
                  OperationConstant.MODULENAME, "ScheduleTaskDao" );
    
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
              page.setSortKey( "CODE" );
              page.setSortOrder( "desc" );
          }
          Map<String , Object> params = new HashMap<String , Object>();
          params.put( "siteid", userInfoScope.getSiteId() );
          params.put( "queryType", queryType );  //查询所有，或是查询未完成的任务
          params.put( "deptId", deptId ); //工种
          params.put( "shiftId", shiftId ); //班次
          Calendar cal = Calendar.getInstance();
          cal.set(Calendar.HOUR_OF_DAY, 0);
          cal.set(Calendar.SECOND, 0);
          cal.set(Calendar.MINUTE, 0);
          cal.set(Calendar.MILLISECOND, 0);
          cal.add(Calendar.DAY_OF_MONTH,1);
          Date tomorrow = cal.getTime();
          params.put( "tomorrow", tomorrow ); // 明天凌晨的时间，如果queryType为unfinished时，一定要这个
          
          page.setParams( params );
          
          page = scheduletaskService.querytodoScheduleTaskList( page );
          return page;
    }
    
    private Object getValueFromObj(JSONObject queryConditionobj, String key, String targetType) {
        Object result = null;
        if(queryConditionobj.containsKey( key )){
            String valueStr = queryConditionobj.getString( key );
            if( !valueStr.equals( "" )){
                if(targetType.equals( "String" )){
                    result = valueStr;
                }else if(targetType.equals( "Date" )){
                    result = DateFormatUtil.stringToDateYYYYMMDD( valueStr );
                }else if(targetType.equals( "List" )){
                    String[] arr = valueStr.split(",");
                    List<String> list = Arrays.asList(arr);
                    result = list;
                }
            }
        } 
        return result;
    }
    
    
     @RequestMapping("/scheduleTaskListData")
     public Page<ScheduleTask> scheduleTaskList() throws Exception {
         
           UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
           String queryCondition = userInfoScope.getParam( "queryCondition" );
           JSONObject queryConditionobj = JSONObject.fromObject( queryCondition );
           String stationId = (String) getValueFromObj(queryConditionobj,"deptId","String");
           List shiftIdList = (List) getValueFromObj(queryConditionobj,"shiftId","List");//班次
           List doStatusList = (List) getValueFromObj(queryConditionobj,"taskStatus","List" );//执行状态
           List typeList = (List) getValueFromObj(queryConditionobj,"type","List" ); //类型
           Date beginDate = (Date) getValueFromObj(queryConditionobj,"beginDate","Date" ); //开始时间
           Date endDate = (Date) getValueFromObj(queryConditionobj,"endDate","Date" ); //结束时间
           
           Page<ScheduleTask> page = userInfoScope.getPage();
           Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "scheduleTaskMap",
                   OperationConstant.MODULENAME, "ScheduleTaskDao" );
     
           // 设置排序内容
           if ( userInfoScope.getParamMap().containsKey( "sort" ) ) {
               String sortKey = userInfoScope.getParam( "sort" );
               // 如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
               sortKey = propertyColumnMap.get( sortKey );
               page.setSortKey( sortKey );
               page.setSortOrder( userInfoScope.getParam( "order" ) );
           } else {
               // 设置默认的排序字段
               page.setSortKey( "CODE" );
               page.setSortOrder( "desc" );
           }
           Map<String , Object> params = new HashMap<String , Object>();
           params.put( "siteid", userInfoScope.getSiteId() );
           params.put( "deptId", stationId ); //工种
           params.put( "shiftIdList", shiftIdList ); //班次
           params.put( "doStatusList", doStatusList ); //状态
           params.put( "typeList", typeList ); //类型
           params.put( "beginDate", beginDate ); //开始时间
           params.put( "endDate", endDate ); //结束时间
           
           page.setParams( params );
           
           page = scheduletaskService.queryScheduleTaskList( page );
           return page;
     }
     
     
     


    @RequestMapping("/openTaskPage")
     @ReturnEnumsBind("OPR_SCHEDULE_DOSTATUS,OPR_SCHEDULE_TYPE")
     public ModelAndView openTaskPlanPage() throws Exception{
         UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
         String id = userInfoScope.getParam( "id" );
         String listdoStatus = userInfoScope.getParam( "doStatus" );
         Map<String, Object> map = new HashMap<String, Object>();
         if(StringUtils.isBlank( id )){
             map.put( "id", null ); 
         }else{
             map.put( "id", id );
         }
         if(StringUtils.isBlank( listdoStatus )){
             map.put( "listdoStatus", null ); 
         }else{
             map.put( "listdoStatus", listdoStatus );
         }
         return new ModelAndView("scheduleTask/scheduleTask.jsp", map);
     }
     
     @RequestMapping("/queryTaskById")
     public Map<String, String> queryTaskPlanById() throws Exception {
         UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
         String userId = userInfoScope.getUserId();
         String siteId = userInfoScope.getSiteId();
         String id = userInfoScope.getParam( "id" );
         ScheduleTask scheduleTask = scheduletaskService.queryTaskById( id );
         String taskPlanId = scheduleTask.getTaskPlanId();
         String canNextShiftFlag = scheduleTaskPlanService.queryTaskPlanById( taskPlanId ).getNextShift();
         String stationId = scheduleTask.getDeptId();  //工种
         List<ScheduleTaskChangeShift> taskChangeShifts = taskChangeShiftService.querySchedTaskChangeShiftList( id );
         
         // 查询是否有“执行、不执行、转其他班次”按钮的权限，判断依据是否是同一工种，（人——>值别——>工种）
         String oprBtnPriv = "N";
         List<PersonJobs> personJobs = dutyService.queryDutyPersons( null, siteId, userId, null );
         if(personJobs.size()>0 && personJobs.get( 0 ).getStationId().equals( stationId )){
             oprBtnPriv = "Y";
         }
         //判断当前时间是否 大于等于 负责班次的日期 
         String dateFlag = "fasle";
         if(new Date().after( scheduleTask.getShiftDate() )){
             dateFlag = "true"; 
         }
         Map<String, String> mav = new HashMap<String, String>();
         mav.put( "result", "success" );
         mav.put( "scheduleTask", JsonHelper.fromBeanToJsonString( scheduleTask ) );
         mav.put( "dateFlag", dateFlag ); //判断当前时间是否 大于等于 负责班次的日期
         mav.put( "taskChangeShiftList", JsonHelper.fromBeanToJsonString( taskChangeShifts ) );
         mav.put( "oprBtnPriv", oprBtnPriv );
         mav.put( "canNextShiftFlag", canNextShiftFlag );         
         return mav;
     }
     
     @RequestMapping("/updateTask")
     public Map<String, String> updateTaskPlan() throws Exception {
         UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
         String ScheduleTaskStr = userInfoScope.getParam( "ScheduleTask" );
         ScheduleTask scheduleTask = JsonHelper.toObject( ScheduleTaskStr, ScheduleTask.class );
         Map<String, String> mav = new HashMap<String, String>();
         scheduleTask.setModifydate( new Date() );
         scheduleTask.setModifyuser( userInfoScope.getUserId() );
         int updateNum = scheduletaskService.updateScheduleTask( scheduleTask);
         String result = "fail";
         if(updateNum > 0){
             result = "success";
         }
         mav.put( "result", result );
         return mav;
     }
     
     /**
      * @description:检查当前班次是否有未完成的定期工作
      * @author: 王中华
      * @createDate: 2016-12-15
      * @param currentShiftId
      * @return:
     * @throws Throwable 
      */
     @RequestMapping("/checkUnfinishSchedultTask")
     public Map<String, Object> checkUnfinishSchedultTask() throws Throwable {
         UserInfoScope  userInfoScope = itcMvcService.getUserInfoScopeDatas( );
         String siteid = userInfoScope.getSiteId();
         //查询此站点下，交接班是否需要检查是否有未完成的工作
         String checkSiteids = "";
         boolean checkFlag = false;
         Configuration configuration = itcSysConfService.queryBSysById( "jiaojieban_check_schedule_task", "NaN" );
        if(configuration != null){
            checkSiteids = configuration.getVal();
            String[] siteids = checkSiteids.split( "," );
            for ( String tempSiteid : siteids ) { 
                if(tempSiteid.equals( siteid )){
                    checkFlag = true ;  //执行检查的站点包含本站点
                    break;
                }
            }
        }
        
         Map<String, Object> map = new HashMap<String, Object>();
         if(checkFlag){
             String currentShiftId = userInfoScope.getParam( "currentShiftId" );
             String nowShiftDateLong = userInfoScope.getParam( "nowShiftDateLong" );
             Date nowShiftDate = new Date( Long.valueOf(nowShiftDateLong) );
             List<ScheduleTask> undoTaskList = scheduletaskService.queryUndoScheduleTaskListByShift( currentShiftId, nowShiftDate );
             int count = undoTaskList.size();
             if ( count > 0  ) {
                 map.put( "result", "fail" );
             } else {
                 map.put( "result", "success" );
             }
         }else{ //执行检查的站点不包含本站点
             map.put( "result", "success" );
         }
        
         return map;
     }
     /**
      * @description:根据时间和工种查班次
      * @author: 王中华
      * @createDate: 2016-12-6
      * @return
      * @throws Exception:
      */
     @RequestMapping("/queryShiftByStationIdAndDate")
     public Map<String, Object> queryShiftByStationIdAndDate() throws Exception{
         UserInfoScope  userInfoScope = itcMvcService.getUserInfoScopeDatas( );
         String stationId = userInfoScope.getParam( "stationId" );
         String shiftDateLongStr = userInfoScope.getParam( "shiftDate" );
         Date shiftDate = new Date( Long.valueOf( shiftDateLongStr ).longValue() );
         
         List<Shift> shiftList = new ArrayList<Shift>();
         //查询某个日期shiftDate还未交接班的班次
         List<CalendarVo> calendarVoList = scheduletaskService.queryShiftByStationIdAndDate( stationId,shiftDate );
         
         for ( CalendarVo calendarVo : calendarVoList ) {
             int shiftId = calendarVo.getShiftId();
             Shift e = shiftService.queryShiftById( shiftId );
             String name = e.getName()+" ( "+calendarVo.getDutyName()+" )";
             e.setName( name );
             if(!"rest".equals( e.getType() )){ //过滤掉休息的
                 shiftList.add( e );
             }
         }
         
         Map<String, Object> dataMap = new HashMap<String, Object>();
         dataMap.put( "rows", shiftList );
         dataMap.put( "calendarVoList", calendarVoList );
         dataMap.put( "total", shiftList.size());
         return dataMap;
     }
     /**
     * @description:
     * @author: 王中华
     * @createDate: 2016-12-7
     * @return
     * @throws Exception:
     */
    @RequestMapping("/changeShiftById")
     public Map<String, String> changeShiftById() throws Exception {
         UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
         String id = userInfoScope.getParam( "id" );
         String shiftInfo = userInfoScope.getParam( "shiftInfo" );
         Map<String, String> mav = new HashMap<String, String>();
         
         ScheduleTask task = scheduletaskService.updateSchedTaskChangeShift( id,shiftInfo);
         mav.put( "result", "success" );
         mav.put( "shiftName", task.getShiftName());
         return mav;
     }
    
    @RequestMapping("/undoTaskById")
    public Map<String, String> undoTaskById() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = userInfoScope.getParam( "id" );
        String remarks = userInfoScope.getParam( "remarks" );
        ScheduleTask task = new ScheduleTask();
        task.setId( id );
        task.setDoStatus( ScheduleTaskDoStatus.NOTDO.getEnName() );//不执行
        task.setRemarks( remarks );  //不执行原因
        task.setRecorder( userInfoScope.getUserId() );  //记录人
        task.setRecordeTime( new Date() );  //记录时间
        //对于不执行的任务，执行人、执行时间都取操作人
        task.setDoTime( new Date() );
        task.setDoUserIds( userInfoScope.getUserId() );
        task.setDoUserNames( userInfoScope.getUserName());
        
        Map<String, String> mav = new HashMap<String, String>();
        int updateNum = scheduletaskService.updateScheduleTask( task );
        String result = "fail";
        if(updateNum > 0){
            result = "success";
        }
        mav.put( "result", result );
        return mav;
    }
    
    
    @RequestMapping("/doTaskById")
    public Map<String, String> doTaskById() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = userInfoScope.getParam( "id" );
        String doTaskInfo = userInfoScope.getParam( "doTaskInfo" );
        
        JSONObject jsonObject=JSONObject.fromObject(doTaskInfo);
        String doResult = (String) jsonObject.get("doResult");  //值别
        Date doTime = new Date((Long) jsonObject.get("doTime"));  //日期
        String doUserNames = (String) jsonObject.get("doUserNames");  //班次
        String remarks = (String) jsonObject.get("remarks");   //备注及原因
        
        ScheduleTask task = new ScheduleTask();
        task.setId( id );
        task.setDoStatus( ScheduleTaskDoStatus.DONE.getEnName() );//执行
        task.setDoResult( doResult );
        task.setDoTime( doTime );
        task.setDoUserNames( doUserNames );
        task.setRemarks( remarks );  //执行备注
        task.setRecorder( userInfoScope.getUserId() );  //记录人
        task.setRecordeTime( new Date() );  //记录时间
        
        Map<String, String> mav = new HashMap<String, String>();
        int updateNum = scheduletaskService.updateScheduleTask( task );
        String result = "fail";
        if(updateNum > 0){
            result = "success";
        }
        mav.put( "result", result );
        return mav;
    }
    
}
