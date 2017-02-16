package com.timss.operation.web;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.operation.bean.PersonJobs;
import com.timss.operation.bean.Shift;
import com.timss.operation.service.DutyService;
import com.timss.operation.service.ScheduleDetailService;
import com.timss.operation.service.ShiftService;
import com.timss.operation.util.DateFormatUtil;
import com.timss.operation.util.OprUserPrivUtil;
import com.timss.operation.util.VOUtil;
import com.timss.operation.vo.CalendarVo;
import com.timss.operation.vo.EventsVo;
import com.timss.operation.vo.RulesHistoryVo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.view.ModelAndViewAjax;

/**
 * 
 * @title: ScheduleDetail排班详情（日历）
 * @description: {desc}
 * @company: gdyd
 * @className: ScheduleDetailController.java
 * @author: fengzt
 * @createDate: 2014年6月13日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("/operation/scheduleDetail")
public class ScheduleDetailController {
    private Logger log = Logger.getLogger( ScheduleDetailController.class );
    
    @Autowired
    private ScheduleDetailService scheduleDetailService;
    
    @Autowired
    private ShiftService shiftService;
    
    @Autowired
    private DutyService dutyService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private OprUserPrivUtil userPrivUtil;
    
    /**
     * 
     * @description:插入日历
     * @author: fengzt
     * @createDate: 2014年6月13日
     * @param formData formJson
     * @return:String
     * @throws Exception 
     */
    @RequestMapping("/insertScheduleDetail")
    public @ResponseBody Map<String, Object> insertScheduleDetail( String formData, String deal ) throws Exception{
        RulesHistoryVo rulesHistoryVo = VOUtil.fromJsonToVoUtil( formData, RulesHistoryVo.class );
        //用户登录的站点
        String siteId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            siteId = userInfoScope.getSecureUser().getCurrentSite();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        rulesHistoryVo.setSiteId( siteId );
        
        boolean flag = false;
        Map<String, Object> map = new HashMap<String, Object>();
        
        //日期校验，开始日期要选择后天及之后的日期，结束日期不能小于开始日期
        Date startDate = DateFormatUtil.parseDate( rulesHistoryVo.getStartTime(), "yyyy-MM-dd" );
        Date endDate = DateFormatUtil.parseDate( rulesHistoryVo.getEndTime(), "yyyy-MM-dd" );
        Date validDate=DateFormatUtil.addDate(DateFormatUtil.getCurrentDate(), "d", 2);
        Boolean isDateValid=DateFormatUtil.compareDate( startDate, validDate )&&DateFormatUtil.compareDate( endDate, startDate );
        if(!isDateValid){
        	map.put( "isDateValid", isDateValid );
            return map;
        }
        
        if( StringUtils.isNotBlank( deal ) ){
            //不为空的时候，删掉重复数据
            scheduleDetailService.deleteScheduleDetail( rulesHistoryVo );
        }else{
            //检查是否有重复数据
            flag = scheduleDetailService.isExistScheduleDetail( rulesHistoryVo );
        }
        
        if ( !flag ) {
            int count = scheduleDetailService.insertScheduleDetail( rulesHistoryVo );

            if ( count > 0 ) {
                map.put( "result", "success" );
            } else {
                map.put( "result", "fail" );
            }
        }
        
        map.put( "flag", flag );
        return map;
    }
    
    /**
     * 
     * @description:值别视图
     * @author: fengzt
     * @createDate: 2014年6月17日
     * @param dutyId
     * @param stationId
     * @param start
     * @param end
     * @return:
     */
    @RequestMapping("/getDutyCalendarByDutyId")
    public Map<String, Object> getDutyCalendarByDutyId( int dutyId, String stationId, String start, String end ){
        
        List<EventsVo> result = scheduleDetailService.getDutyCalendarByDutyId( dutyId, stationId, start, end );
        
        Map<String , Object> map = new HashMap<String, Object>();
        map.put( "result", result );
        
        return map;
    }
    
    /**
     * 
     * @description:岗位视图
     * @author: fengzt
     * @createDate: 2014年6月17日
     * @param stationId
     * @param start
     * @param end
     * @return:Map<String, Object>
     */
    @RequestMapping("/getDutyCalendarByStationId")
    public Map<String, Object> getDutyCalendarByStationId( String stationId, String start, String end ){
        
        List<EventsVo> result = scheduleDetailService.getDutyCalendarByStationId( stationId, start, end );
        
        Map<String , Object> map = new HashMap<String, Object>();
        map.put( "result", result );
        
        return map;
    }
    
    /**
     * 
     * @description:通过岗位、值别、日期获取当天排班日历
     * @author: huanglw
     * @createDate: 2014年6月24日
     * @param date,stationId,dutyId
     * @return:
     */
    @RequestMapping("/getCalendarByDSD")
    public Map<String, Object> getCalendarByDSD(String date,String stationId,int dutyId){
        List<CalendarVo> scheduleDetail = scheduleDetailService.getCalendarByDSD( date, stationId,dutyId);
        List<Shift> shiftList = shiftService.queryShiftByStationId(stationId);
        
        Map<String , Object> resultMap = new HashMap<String, Object>();
        resultMap.put( "scheduleDetail", scheduleDetail );
        resultMap.put( "shiftResult", shiftList );
       
        return resultMap;
    }
    
    @RequestMapping("/getCalendarByDSDWithPerson")
    public ModelAndViewAjax getCalendarByDSDWithPerson(String date,String stationId,int dutyId) throws Exception{
    	Map<String , Object> resultMap = new HashMap<String, Object>();
        List<CalendarVo> scheduleDetail = scheduleDetailService.getCalendarByDSD( date, stationId,dutyId);
        if(scheduleDetail!=null&&scheduleDetail.size()>0){
        	CalendarVo vo=scheduleDetail.get(0);
        	resultMap.put("bean",vo);
        	List<PersonJobs> schedulePerson=scheduleDetailService.querySchedulePersonByScheduleId(vo.getId(),null);
            resultMap.put("schedulePerson",schedulePerson);
        	resultMap.put("result","success");
        }else{
        	resultMap.put("result", "fail");
        }
        return itcMvcService.jsons(resultMap);
    }
    
    /**
     * 
     * @description:更新单条日历数据
     * @author: huanglw
     * @createDate: 2014年6月25日
     * @param CalendarVo 表示一条日历数据（由岗位、值别、日期唯一确定）
     * @return:成功更新的数量
     */
    @RequestMapping("/updateDutyCalendar")
    public Map<String, Object> updateDutyCalendar(String formData){
        CalendarVo cVo = VOUtil.fromJsonToVoUtil( formData, CalendarVo.class );
        cVo.setUuid( DateFormatUtil.formatDate(new Date(),"yyyyMMddhhmmss" ));
        int count = scheduleDetailService.updateCalendar( cVo );
        Map<String, Object> map = new HashMap<String, Object>();

        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
   
    /**
     * 保存运行值班人员修改
     * @param formData
     * @return
     * @throws Exception 
     */
    @RequestMapping("/updateSchedulePerson")
    public Map<String, Object> updateSchedulePerson(String updateType,Integer nowScheduleId,String userIdStr) throws Exception{
        List<String> userIdList = VOUtil.fromJsonToListObject(userIdStr, String.class);
        int count = scheduleDetailService.updateSchedulePerson(updateType,nowScheduleId, userIdList);
        
        Map<String, Object> map = new HashMap<String, Object>();
        if ( count >= 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }

    /**
     * 刷新今年的排班的值班人员
     * @param siteId
     * @return
     * @throws Exception
     */
    @RequestMapping("/refreshSchedulePerson")
    public Map<String, Object> refreshSchedulePerson(String siteId,Boolean isWithHistory) throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();
    	if(userPrivUtil.getUserInfoScope().getSecureUser().isSuperAdmin()){
    		Integer year=Calendar.getInstance().get(Calendar.YEAR);
    		Integer count=-1;
    		if(isWithHistory){//刷全年的数据
    			count=scheduleDetailService.refreshDutyPersonSchedule(siteId, DateFormatUtil.getCurrYearFirst(year), DateFormatUtil.getCurrYearLast(year), true);
    		}else{//刷以后的数据
    			Date tomorrow=DateFormatUtil.addDate(DateFormatUtil.getCurrentDate(), "d", 1);
                count=scheduleDetailService.refreshDutyPersonSchedule(siteId, tomorrow, DateFormatUtil.getCurrYearLast(year), true);
    		}
    		
            if ( count >= 0 ) {
                map.put( "result", "success" );
            } else {
                map.put( "result", "fail" );
            }
    	}else{
    		map.put( "result", "no priv" );
    	}
    	
        return map;
    }
    
    /**
     * 
     * @description:通过岗位、日期获取当天各值别的排班，可指定开始时间之后的班次
     * @author: huanglw
     * @createDate: 2014年6月24日
     * @param 当天日期date 岗位IDstationId
     * @return:
     */
    @RequestMapping("/getCalendarByDS")
    public Map<String, Object> getCalendarByDS(String date,String stationId,String startTimeStr){
        
        List<CalendarVo> scheduleDetail = scheduleDetailService.getCalendarByDS( date, stationId);
        //如果有给定开始时间，则过滤出开始时间之后的班次
        if(StringUtils.isNotBlank(startTimeStr)){
        	scheduleDetail=scheduleDetailService.getAvailableCalendarByDS(scheduleDetail, startTimeStr);
        }
        List<Shift> shiftList = shiftService.queryShiftByStationId(stationId);
        Map<String , Object> resultMap = new HashMap<String, Object>();
        resultMap.put( "scheduleDetail", scheduleDetail );
        resultMap.put( "shiftResult", shiftList );
       
        return resultMap;
    }
    
    /**
     * 
     * @description:更新岗位视图日历
     * @author: huanglw
     * @createDate: 2014年7月1日
     * @param 前端传过来的formData（其中值别数量是动态）
     * @return:
     */
    @RequestMapping("/updateStationCalendar")
    public Map<String, Object> updateStationCalendar(String formData){
        HashMap<String, Object> dataMap=VOUtil.fromJsonToHashMap( formData );
        
        int count = scheduleDetailService.updateStationCalendar( dataMap );
        Map<String, Object> map = new HashMap<String, Object>();
        System.out.println("成功更新："+count);
        if ( count > 0 || count == -1 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * 
     * @description:年视图
     * @author: fengzt
     * @createDate: 2014年7月1日
     * @param year
     * @param stationId
     * @param dutyId
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryYearCalendar")
    public @ResponseBody Map<String, Object> queryYearCalendar(int year, String stationId ){
        Map<String, Object> map = new HashMap<String, Object>();
        
        try {
            //空白代表多条uuid使用或者有修改过月视图或者岗位视图
            map = scheduleDetailService.queryYearCalendar( year, stationId);
        } catch (ParseException e) {
            log.error( e.getMessage() );
        }
        
        //是否同一个uuid 只有一条
        if( map.isEmpty() ){
            map.put( "success", "0" );
        }else{
            map.put( "success", "1" );
        }
        
        return map;
    }
    
}
