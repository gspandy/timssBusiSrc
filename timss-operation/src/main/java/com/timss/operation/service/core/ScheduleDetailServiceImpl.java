package com.timss.operation.service.core;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.attendance.service.CheckMachineService;
import com.timss.attendance.service.WorkStatusService;
import com.timss.operation.bean.Duty;
import com.timss.operation.bean.PersonJobs;
import com.timss.operation.bean.RulesHistory;
import com.timss.operation.bean.ScheduleDetail;
import com.timss.operation.bean.Shift;
import com.timss.operation.dao.ScheduleDetailDao;
import com.timss.operation.service.DutyService;
import com.timss.operation.service.RulesHistoryService;
import com.timss.operation.service.ScheduleDetailService;
import com.timss.operation.service.ShiftService;
import com.timss.operation.util.ColorUtil;
import com.timss.operation.util.DateFormatUtil;
import com.timss.operation.util.SortUtil;
import com.timss.operation.vo.CalendarPageVo;
import com.timss.operation.vo.CalendarVo;
import com.timss.operation.vo.DutyPersonShiftVo;
import com.timss.operation.vo.EventsVo;
import com.timss.operation.vo.RulesHistoryVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
/**
 * 
 * @title: 日历
 * @description: 排班日历
 * @company: gdyd
 * @className: ScheduleDetailServiceImpl.java
 * @author: fengzt
 * @createDate: 2014年6月13日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("scheduleDetailService")
@Transactional(propagation=Propagation.SUPPORTS)
public class ScheduleDetailServiceImpl implements ScheduleDetailService {
    
    //log4j 
    private Logger logger = Logger.getLogger( ScheduleDetailServiceImpl.class );
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private RulesHistoryService rulesHistoryService;

    @Autowired
    private ScheduleDetailDao scheduleDetailDao;
    
    @Autowired
    private DutyService dutyService;
    
    @Autowired
    private ShiftService shiftService;

    @Autowired
    private WorkStatusService workStatusService;
    
    @Autowired
    private CheckMachineService checkMachineService;
    
    /**
     * 
     * @description:RulesHistoryVo 转 RulesHistory
     * @author: fengzt
     * @createDate: 2014年7月2日
     * @param rulesHistoryVo
     * @return:RulesHistory
     */
    private RulesHistory fromRulesHistoryVoToRulesHistory( RulesHistoryVo rulesHistoryVo){
        RulesHistory rulesHistory = new RulesHistory();

        rulesHistory.setName( rulesHistoryVo.getName() );
        rulesHistory.setUuid( rulesHistoryVo.getUuid() );
        rulesHistory.setStationId( rulesHistoryVo.getStationId() );

        Date startDate = DateFormatUtil.parseDate( rulesHistoryVo.getStartTime(), "yyyy-MM-dd" );
        Date endDate = DateFormatUtil.parseDate( rulesHistoryVo.getEndTime(), "yyyy-MM-dd" );

        rulesHistory.setStartTime( startDate );
        rulesHistory.setEndTime( endDate );
        rulesHistory.setWriteDate( new Date() );
        
        rulesHistory.setStartFlag( rulesHistoryVo.getStartFlag() );
        rulesHistory.setSiteId( rulesHistoryVo.getSiteId() );
        return rulesHistory;
    }
    
    /**
     * 
     * @description:插入使用规则历史
     * @author: fengzt
     * @createDate: 2014年6月16日
     * @param rulesHistoryVo:
     */
    private void insertRulesHistory(RulesHistoryVo rulesHistoryVo) {
        RulesHistory rulesHistory = fromRulesHistoryVoToRulesHistory( rulesHistoryVo );

        // 插入规则使用历史
        int rhCount = rulesHistoryService.insertRulesHistory( rulesHistory );
        if ( rhCount > 0 ) {
            logger.info( "使用规则历史插入成功！！" + new Date() );
        }
    }

    /**
     * 
     * @description:生成日历
     * @author: fengzt
     * @createDate: 2014年6月13日
     * @param rulesHistory
     * @return:int
     * @throws Exception 
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public int insertScheduleDetail(RulesHistoryVo rulesHistoryVo) throws Exception {
        Date startDate = DateFormatUtil.parseDate( rulesHistoryVo.getStartTime(), "yyyy-MM-dd" );
        Date endDate = DateFormatUtil.parseDate( rulesHistoryVo.getEndTime(), "yyyy-MM-dd" );
        int startFlag = rulesHistoryVo.getStartFlag();
        
        //插入使用规则历史
        insertRulesHistory( rulesHistoryVo );
        
        //datagrid 
        List<CalendarVo> calendarVos = getRuleDatagridByUuid( rulesHistoryVo.getUuid() );
        int calendarVosLen = calendarVos.size();
        
        //列数有多少个值别
        int dutyColumnsCount = getDutyColumnsByUuid( rulesHistoryVo.getUuid() );
        
        //周期
        int period = 0;
        if( dutyColumnsCount != 0 ){
            period = calendarVosLen / dutyColumnsCount;
        }
        
        List<CalendarVo> result = new ArrayList<CalendarVo>();
        
        //date for循环 一个周期插入
        for( Date temp = startDate; DateFormatUtil.compareDate( endDate, temp ); temp = DateFormatUtil.addDate( temp, "d", 1 ) ){
            //相差多少天
            int diffDay = DateFormatUtil.getIntervalOfDate( startDate, temp );
            int dayTime = ( diffDay + startFlag - 1 ) % period;
            
            //天 数据
            List<CalendarVo> dayCalendarVos = new ArrayList<CalendarVo>();
            dayCalendarVos = setDayCalendarVo( calendarVos, dayTime, dutyColumnsCount, temp, startFlag );
            
            //加入一天的数据
            result.addAll( dayCalendarVos );
        }
        
        for( CalendarVo vo : result ){
            logger.info( "----------------插入的时间数据-------------" + vo.getDateTime().toString() );
        }
        
        int scheduleDetailCount = batchInsert( result );
        //插入人员排班信息
        insertSchedulePersonFromScheduleDutyAndUserIds(null, startDate, endDate, null, null, rulesHistoryVo.getUuid(),null,null);
        return scheduleDetailCount;
    }

    /**
     * 
     * @description: 批量插入
     * @author: fengzt
     * @createDate: 2014年6月16日
     * @param result
     * @return:int
     */
    @Transactional( propagation=Propagation.REQUIRED )
    private int batchInsert(List<CalendarVo> calendarVos) {
        int num = 0;
        if( calendarVos != null && calendarVos.size() > 0 ){
        	//数据量过大，考虑分批插入
        	int count=0;//统计批量插入条数
        	int batchNum=100;//批量插入的条数
        	int insertIndex=0;//开始插入的序号
        	while ((insertIndex+batchNum)<=calendarVos.size()) {
        		count = scheduleDetailDao.batchInsert( calendarVos.subList(insertIndex, insertIndex+batchNum) );
        		logger.debug( "batchInsert插入条数 ：" + count );
                num+=count;
        		insertIndex+=batchNum;
			}
        	if(insertIndex<calendarVos.size()){
        		count = scheduleDetailDao.batchInsert( calendarVos.subList(insertIndex, calendarVos.size()) );
        		logger.debug( "batchInsert插入条数 ：" + count );
                num+=count;
        	}
        }
        
        return num;
    }


    /**
     * 
     * @description:日历datagrid每一行的数据
     * @author: fengzt
     * @createDate: 2014年6月16日
     * @param calendarVos datagrid行列
     * @param dayTime 某个周期的一天
     * @param dutyColumnsCount 值别数
     * @param temp 天数
     * @param startFlag 下标
     * @return:List<CalendarVo>
     */
    private List<CalendarVo> setDayCalendarVo(List<CalendarVo> calendarVos, int dayTime, int dutyColumnsCount, Date temp, int startFlag ) {
        
        List<CalendarVo> vos = new ArrayList<CalendarVo>( );
        
        int indexStart = dayTime* dutyColumnsCount;
        
        for( int i = 0; i < dutyColumnsCount; i ++ ){
            CalendarVo vo = new CalendarVo();
            vo = copyCalendarVo( calendarVos.get( indexStart + i ), temp, startFlag );
            if(vo!=null)//可控制无效排班不插入运行日历
            	vos.add( vo );
        }
        
        return vos;
    }


    
    /**
     * 
     * @description:复制一个CalendarVo
     * @author: fengzt
     * @createDate: 2014年6月17日
     * @param calendarVo
     * @param temp
     * @param startFlag
     * @return:CalendarVo
     */
    private CalendarVo copyCalendarVo(CalendarVo calendarVo, Date temp, int startFlag ) {
        CalendarVo vo = new CalendarVo();
        vo.setDateTime( temp );
        vo.setDayTime( calendarVo.getDayTime() );
        
        if(calendarVo.getDutyName()!=null){//排除掉不可用的值别
        	vo.setDutyId( calendarVo.getDutyId() );
            vo.setDutyName( calendarVo.getDutyName() );
        }else{
        	return null;
        }
        
        vo.setId( calendarVo.getId() );
        vo.setName( calendarVo.getName() );
        vo.setRulesId( calendarVo.getRulesId() );
        
        if(calendarVo.getShiftName()!=null){//排除掉不可用的班次
        	vo.setShiftId( calendarVo.getShiftId() );
            vo.setShiftName( calendarVo.getShiftName() );
        }else{
        	return null;
        }
        
        vo.setSortType( calendarVo.getSortType() );
        vo.setStationId( calendarVo.getStationId() );
        vo.setUuid( calendarVo.getUuid() );
        vo.setStartFlag( startFlag );
        return vo;
    }


    /**
     * 
     * @description:列数有多少个值别
     * @author: fengzt
     * @createDate: 2014年6月16日
     * @param uuid
     * @return:int
     */
    private int getDutyColumnsByUuid(String uuid) {
        return scheduleDetailDao.getDutyColumnsByUuid( uuid );
    }


    /**
     * 
     * @description:通过uuid拿出datagrid
     * @author: fengzt
     * @createDate: 2014年6月16日
     * @param uuid
     * @return:List<CalendarVo>
     */
    private List<CalendarVo> getRuleDatagridByUuid(String uuid) {
        
        return scheduleDetailDao.getRuleDatagridByUuid( uuid );
    }

    /**
     * 
     * @description:值别日历
     * @author: fengzt
     * @createDate: 2014年6月17日
     * @param dutyId
     * @param stationId
     * @param start
     * @param end:
     * @return  List<EventsVo>
     */
    public List<EventsVo> getDutyCalendarByDutyId(int dutyId, String stationId, String start, String end) {
        CalendarPageVo vo = new CalendarPageVo();
        vo.setDutyId( dutyId );
        vo.setStationId( stationId );
        
        Date startDate = DateFormatUtil.parseDate( start, "yyyy-MM-dd" );
        Date endDate = DateFormatUtil.parseDate( end, "yyyy-MM-dd" );
        
        vo.setStartDate( startDate );
        vo.setEndDate( endDate );
        
        List<CalendarVo> resultList = scheduleDetailDao.getCalendarByDSD(vo);
        
        List<EventsVo> eventsVos = new ArrayList<EventsVo>();
        //构造日历事件
        for( CalendarVo cVo : resultList ){
            String startTime = DateFormatUtil.formatDate( cVo.getDateTime(), "yyyy-MM-dd" );
            
            EventsVo eVo = new EventsVo();
            eVo.setStart( startTime );
            eVo.setTitle( cVo.getShiftName() );
            
            String color = ColorUtil.shiftNameChangToColor( cVo.getShiftName() );
            eVo.setColor( color );
            
            eVo.setDutyShift(cVo);//把值别班次信息带到前台
            
            eventsVos.add( eVo );
        }
        
        return eventsVos;
    }
    
    /**
     * 
     * @description:岗位日历
     * @author: fengzt
     * @createDate: 2014年6月17日
     * @param stationId
     * @param start
     * @param end:
     * @return  List<EventsVo>
     */
    public List<EventsVo> getDutyCalendarByStationId(String stationId, String start, String end) {
        CalendarPageVo vo = new CalendarPageVo();
        vo.setStationId( stationId );
        
        Date startDate = DateFormatUtil.parseDate( start, "yyyy-MM-dd" );
        Date endDate = DateFormatUtil.parseDate( end, "yyyy-MM-dd" );
        
        vo.setStartDate( startDate );
        vo.setEndDate( endDate );
        
        List<CalendarVo> resultList = scheduleDetailDao.getCalendarByDSD(vo);
        
        List<EventsVo> eventsVos = new ArrayList<EventsVo>();
        //构造日历事件
        for( CalendarVo cVo : resultList ){
            String startTime = DateFormatUtil.formatDate( cVo.getDateTime(), "yyyy-MM-dd" );
            
            EventsVo eVo = new EventsVo();
            eVo.setStart( startTime );
            String title = cVo.getDutyName() + ":" + cVo.getShiftName();
            eVo.setTitle( title );
            
            String color = ColorUtil.shiftNameChangToColor( cVo.getShiftName() );
            eVo.setColor( color );
            
            eVo.setDutyShift(cVo);//把值别班次信息带到前台
            
            eventsVos.add( eVo );
        }
        
        SortUtil.sortList( eventsVos, "color", true );
        
        return eventsVos;
    }
    

    /**
     * 
     * @description:通过岗位、值别、日期获取当天排班日历
     * @author: huanglw
     * @createDate: 2014年6月24日
     * @param date(String 'yyyy-MM-dd'),stationId,dutyId
     * @return:
     */
    public List<CalendarVo> getCalendarByDSD(String date,String stationId,int dutyId) {
       Date d=DateFormatUtil.stringToDateYYYYMMDD(date);
       
       CalendarPageVo vo = new CalendarPageVo();
       vo.setStationId( stationId );
       vo.setDutyId( dutyId );
       vo.setStartDate( d );
       vo.setEndDate( d );
       
        List<CalendarVo> calendarVos =   scheduleDetailDao.getCalendarByDSD(vo);
        return calendarVos;
    }


    /**
     * 
     * @description:更新单条日历数据
     * @author: huanglw
     * @createDate: 2014年6月25日
     * @param CalendarVo 表示一条日历数据（由岗位、值别、日期唯一确定）
     * @return:成功更新的数量
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateCalendar(CalendarVo cVo) {
        int count = scheduleDetailDao.updateCalendar( cVo );
        return count;
    }

    /**
     * 
     * @description:通过岗位、日期获取当天各值别的排班
     * @author: huanglw
     * @createDate: 2014年6月24日
     * @param 当天日期date(String 格式是yyyy-MM-dd 岗位IDstationId
     * @return:
     */
    public List<CalendarVo> getCalendarByDS(String date, String stationId) {
        Date d=DateFormatUtil.stringToDateYYYYMMDD(date);
        
        CalendarPageVo vo = new CalendarPageVo();
        vo.setStationId( stationId );
        vo.setStartDate( d );
        vo.setEndDate( d );
        //System.out.println(vo.getDutyId());
        
        //同样使用Dao中的getCalendarByDSD
         List<CalendarVo> calendarVos =   scheduleDetailDao.getCalendarByDSD(vo);
         SortUtil.sortList( calendarVos, "startTime", true );
         return calendarVos;
    }

    /**
     * 从班次列表中过滤出给定排班开始时间之后的班次
     * 用于交接班获得可用的下一班班次
     * @param cVos
     * @param startTime
     * @return
     */
    public List<CalendarVo>getAvailableCalendarByDS(List<CalendarVo>cVos,String startTimeStr){
    	List<CalendarVo> tmpList=new ArrayList<CalendarVo>();
    	for (CalendarVo calendarVo : cVos) {
    		if(calendarVo.getStartTime().compareTo(startTimeStr)>0){//选择开始时间大于当前班的班次
    			tmpList.add(calendarVo);
    		}
		}
    	//如果为空，要选择下一天的？
    	
    	return tmpList;
    }

    /**
     * 
     * @description:通过一个动态的HashMap,构造日历List，传给Dao
     * @author: huanglw
     * @createDate: 2014年7月1日
     * @param dataMap，其中包含stationId,dataTime,岗位各值别的班次（数量动态）
     * @return:更新数量
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateStationCalendar(HashMap<String, Object> dataMap) {
        String dateTimeString = (String)dataMap.get( "dateTime" );
        Date dateTime=DateFormatUtil.parseDate( dateTimeString, "yyyy-MM-dd" );
        dateTime=DateFormatUtil.getFormatDate( dateTime, "yyyy-MM-dd HH:mm:ss" );
        String stationId=dataMap.get( "stationId" ).toString();
        Set<String> keys = dataMap.keySet();
        List<CalendarVo> voList=new ArrayList<CalendarVo>();
        String uuId=DateFormatUtil.formatDate(new Date(),"yyyyMMddHHmmss");
        for (Iterator<String> it = keys.iterator(); it.hasNext();) {
            String s = (String) it.next();
            if(s.startsWith( "dutyId_" )){
                CalendarVo vo=new CalendarVo();
                vo.setUuid( uuId );
                vo.setDateTime( dateTime);
                vo.setStationId( stationId );
                vo.setShiftId( Integer.parseInt( (String)dataMap.get( s ) ) );
                vo.setDutyId( Integer.parseInt( s.substring( 7, s.length() ) ) );
                voList.add( vo );
            }
        }
        
        return scheduleDetailDao.updateStationCalendar(voList);
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
     * @throws ParseException 
     */
    @Override
    public Map<String, Object> queryYearCalendar(int year, String stationId) throws ParseException {
        //year 年的第一天
        Date yearFristDay = DateFormatUtil.getCurrYearFirst( year );
        
        //年的最后一天
        Date yearLastDay = DateFormatUtil.getCurrYearLast( year );
        HashMap<String, Object> map = new HashMap<String, Object>();
        
        map.put( "yearFristDay", yearFristDay );
        map.put( "yearLastDay", yearLastDay );
        
        map.put( "stationId", stationId );
        
        //判断当前年uuid 个数， 是否用相同的datagrid生成整年
        List<CalendarVo> cList = scheduleDetailDao.queryYearCalendarRules( map );
        
        Map<String,Object> result = new HashMap<String, Object>();
        
        //整年一条uuid
        if( cList != null && cList.size() == 1 ){
            String uuid = cList.get( 0 ).getUuid();
            map.put( "uuid", uuid );
            result.put( "uuid", uuid );
            
            Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
            pageVo.setPageNo( 1 );
            pageVo.setPageSize( 1 );
            pageVo.setParams( map );
            
            pageVo.setSortKey( "dateTime" );
            pageVo.setSortOrder( "asc" );
            
            //第一天
            List<CalendarVo> fristCalendarVos = scheduleDetailDao.queryFristCalendar( pageVo );
            Date fristDate = fristCalendarVos.get( 0 ).getDateTime();
            String fristDateStr = DateFormatUtil.dateToString( fristDate , "yyyy-MM-dd" );
            map.put( "fristDate", fristDate );
            result.put( "fristDateStr", fristDateStr );
            
            Page<HashMap<?, ?>> pageVo1 = new Page<HashMap<?, ?>>();
            pageVo1.setPageNo( 1 );
            pageVo1.setPageSize( 1 );
            pageVo1.setParams( map );
            
            pageVo1.setSortKey( "dateTime" );
            pageVo1.setSortOrder( "desc" );
            
            //最后一天
            List<CalendarVo> lastCalendarVos = scheduleDetailDao.queryLastCalendar( pageVo1 );
            Date lastDate = lastCalendarVos.get( 0 ).getDateTime();
            String lastDateStr = DateFormatUtil.dateToString( lastDate , "yyyy-MM-dd"  );
            map.put( "lastDate", lastDate );
            result.put( "lastDateStr", lastDateStr );
            
            //最后的下标
            result.put( "startFlag", lastCalendarVos.get( 0 ).getStartFlag() );
           /* List<RulesHistoryVo> rulesHistoryVos = rulesHistoryService.queryRulesHistoryByMap( map );
            
            if( rulesHistoryVos != null && rulesHistoryVos.size() == 1 ){
                int startFlag = rulesHistoryVos.get( 0 ).getStartFlag();
                result.put( "startFlag", startFlag );
            }*/
            
        
            List<Duty> dutyList = dutyService.queryDutyByStationId( stationId );
            if(dutyList != null && dutyList.size() > 0){
                result.put( "dutyList", dutyList );
            }
            
            List<Shift> shiftList = shiftService.queryShiftByStationId( stationId );
            if(shiftList != null && shiftList.size() > 0){
                result.put( "shiftList", shiftList );
            }
            
        }
        
        return result;
    }


    /**
     * 
     * @description:检查是否有重复数据
     * @author: fengzt
     * @createDate: 2014年7月2日
     * @param rulesHistoryVo
     * @return:boolean
     */
    @Override
    public boolean isExistScheduleDetail(RulesHistoryVo rulesHistoryVo) {
        RulesHistory rulesHistory = fromRulesHistoryVoToRulesHistory( rulesHistoryVo );
        
        List<CalendarVo> result = scheduleDetailDao.queryScheduleDetailByRulesHistory( rulesHistory );
        if( !result.isEmpty() ){
            return true;
        }
        return false;
    }

    /**
     * 
     * @description:删除数据
     * @author: fengzt
     * @createDate: 2014年7月2日
     * @param rulesHistoryVo
     * @return:int
     */
    @Override
    public int deleteScheduleDetail(RulesHistoryVo rulesHistoryVo) {
        RulesHistory rulesHistory = fromRulesHistoryVoToRulesHistory( rulesHistoryVo );
        return scheduleDetailDao.deleteScheduleDetail( rulesHistory );
    }

    /**
     * @description:传入日历id获取下一班的日历
     * @author: huanglw
     * @createDate: 2014年7月22日
     * @param nextScheduleId
     * @return:CalendarVo
     */
    public CalendarVo getNextCalendarVoById(int scheduleId) {
       List<CalendarVo> calendarVos = scheduleDetailDao.getSameDayCalendarVoById(scheduleId);
       //筛选出正常班次的日历
       List<CalendarVo> normalList = splitNormalCalendarVo( calendarVos );
       //按startTime升序排列
       SortUtil.sortList( normalList, "startTime", true );
       for ( CalendarVo calendarVo : normalList ) {
           if(calendarVo.getId() == scheduleId ){
               //如果当前scheduleId对应的日历是当天最后一班，则下一班就在明天的日历里
               if(normalList.lastIndexOf( calendarVo ) == ( normalList.size() - 1 )){
                   Date nextDate = DateFormatUtil.addDate( calendarVo.getDateTime(), "d", 1 );
                   CalendarPageVo vo = new CalendarPageVo();
                   vo.setStartDate( nextDate );
                   vo.setEndDate( nextDate );
                   vo.setStationId( calendarVo.getStationId() );
                   List<CalendarVo> nextDayCalendarVos = scheduleDetailDao.getCalendarByDSD( vo );
                   List<CalendarVo> nextNormalList = splitNormalCalendarVo( nextDayCalendarVos );
                   SortUtil.sortList( nextNormalList, "startTime", true );
                   return nextNormalList != null && nextNormalList.size() > 0 ? nextNormalList.get( 0 ) : null; 
               }else{
                   return normalList.get( normalList.indexOf( calendarVo ) + 1 );
               }
           }
       }
       return null;
    }
    
    //筛选出正常班次
    private List<CalendarVo> splitNormalCalendarVo(List<CalendarVo> calendarVos){
        List<CalendarVo> normalList = new ArrayList<CalendarVo>();
        for ( CalendarVo calendarVo : calendarVos ) {
            if(calendarVo.getShiftType().equals( "normal" )){
                normalList.add( calendarVo );
            }
        }
        return normalList;
    }
    
    @Override
    public List<PersonJobs>querySchedulePersonByScheduleId(Integer scheduleId,String isPresent)throws Exception{
    	List<PersonJobs>result=scheduleDetailDao.querySchedulePersonByScheduleId(scheduleId,isPresent);
    	return result;
    }
    
    @Override
    @Transactional( propagation=Propagation.REQUIRED )
    public Integer updateSchedulePerson(String updateType,Integer scheduleId,List<String>userIdList)throws Exception{
    	//查询排班信息
    	ScheduleDetail scheduleDetail=queryDetailByScheduleId(scheduleId);
    	List<PersonJobs> schedulePerson=querySchedulePersonByScheduleId(scheduleId,null);//所有的排班人员
    	//转成map加快查找效率
    	Map<String, PersonJobs>schedulePersonMap=new HashMap<String, PersonJobs>();
    	for (PersonJobs personJobs : schedulePerson) {
    		schedulePersonMap.put(personJobs.getUserId(), personJobs);
		}
    	Map<String, String>userIdMap=new HashMap<String, String>();
    	for (String userId:userIdList) {
    		userIdMap.put(userId, userId);
		}
    	
    	//比对查找要删除的人
    	List<String>delPerson=new ArrayList<String>();
    	for (PersonJobs personJobs : schedulePerson) {
			if(userIdMap.get(personJobs.getUserId())==null){
				delPerson.add(personJobs.getUserId());
			}
		}
    	Integer delNum=0;
    	if(delPerson.size()>0){
    		//删除该排班要删除的人
    		if("attendance".equals(updateType)){
    			delNum=updateSchedulePersonIsPresent(scheduleId, delPerson.toArray(new String[delPerson.size()]), "N");
    		}else{
    			delNum=deleteSchedulePersonFromScheduleDutyAndUserIds(new Integer[]{scheduleId},null,null,null,null,null,null,delPerson.toArray(new String[delPerson.size()]));
    		}
    		
        	logger.info("remove schedulePerson->scheduleId:"+scheduleId+" result:"+delNum+"/"+delPerson.size()+" updateType:"+updateType);
    	}
    	
    	//比对查找要增加的人
    	List<String>addPerson=new ArrayList<String>();
    	List<String>notPresentPerson=new ArrayList<String>();//已存在排班，但被置为缺勤的人，如果是考勤调班，要修改为出勤
    	for (String userId:userIdList) {
			if(schedulePersonMap.get(userId)==null){
				addPerson.add(userId);
			}else if("N".equals(schedulePersonMap.get(userId).getIsPresent())){
				notPresentPerson.add(userId);
			}
		}
    	Integer addNum=0;
    	if(addPerson.size()>0){
    		//删除与该排班同一天的要增加的人的其他排班
    		Date scheduleDate=scheduleDetail.getDateTime();
        	Integer delOtherNum=deleteSchedulePersonFromScheduleDutyAndUserIds(null,scheduleDate,scheduleDate,null,null,null,null,addPerson.toArray(new String[addPerson.size()]));
        	logger.info("remove other schedule->date:"+DateFormatUtil.dateToString(scheduleDate)+" result:"+delOtherNum+"/"+addPerson.size()+" type:schedule");
        	//给该排班添加要增加的人
        	addNum=insertSchedulePersonFromScheduleDutyAndUserIds(new Integer[]{scheduleId}, null,null,null,null,null,null,
        			addPerson.toArray(new String[addPerson.size()]));
        	logger.info("add schedulePerson->scheduleId:"+scheduleId+" result:"+addNum+"/"+addPerson.size());
    	}
    	
    	//更新该天增加的人和删除的人的打卡记录结果和打卡统计信息
    	Set<String>userIdSet=new HashSet<String>();
    	userIdSet.addAll(delPerson);
    	userIdSet.addAll(addPerson);
    	if(userIdSet.size()>0){//刷新排班影响的打卡记录和打卡结果
    		checkMachineService.refreshCheckMachineResult(null, userIdSet.toArray(new String[userIdSet.size()]), scheduleDetail.getDateTime(), scheduleDetail.getDateTime());
    		workStatusService.checkPersonsWorkStatus(scheduleDetail.getSiteId(),scheduleDetail.getDateTime(), scheduleDetail.getDateTime(), userIdSet);
    	}
    	
    	Integer updateNum=0;
    	if("attendance".equals(updateType)&&notPresentPerson.size()>0){//如果是考勤调班，把缺勤的人改成出勤
    		userIdSet.addAll(notPresentPerson);
    		updateNum=updateSchedulePersonIsPresent(scheduleId, notPresentPerson.toArray(new String[notPresentPerson.size()]), "Y");    		
    		logger.info("update schedulePersonIsPresent:Y->scheduleId:"+scheduleId+" result:"+updateNum+"/"+notPresentPerson.size()+" updateType:"+updateType);
    	}
    	
    	logger.info("finish updateSchedulePerson->scheduleId:"+scheduleId+" delNum:"+delNum+" addNum:"+addNum+" updateNum:"+updateNum+" updateType:"+updateType);
    	return userIdSet.size();
    }
    
	@Override
	@Transactional( propagation=Propagation.REQUIRED )
	public Integer insertSchedulePersonFromScheduleDutyAndUserIds(Integer[] scheduleIds,
			Date startDate, Date endDate, Integer[] dutyIds, String stationId,
			String uuid,String siteId,String[]userIds) throws Exception {
		Integer num=scheduleDetailDao.insertSchedulePersonFromScheduleDutyAndUserIds(scheduleIds, startDate, endDate, dutyIds, stationId, uuid,siteId,userIds);
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		logger.info("insertSchedulePersonFromScheduleDutyAndUserIds->scheduleIds:"+scheduleIds+" startDate:"+startDate+" endDate:"+endDate+
				" dutyIds:"+dutyIds+" stationId:"+stationId+" uuid:"+uuid+" siteId:"+siteId+" userIds:"+userIds+" by:"+userInfoScope.getUserId());
		return num;
	}

	@Override
	@Transactional( propagation=Propagation.REQUIRED )
	public Integer deleteSchedulePersonFromScheduleDutyAndUserIds(
			Integer[] scheduleIds, Date startDate, Date endDate,
			Integer[] dutyIds, String stationId, String uuid, String siteId,String[] userIds)
			throws Exception {
		Integer num=scheduleDetailDao.deleteSchedulePersonFromScheduleDutyAndUserIds(scheduleIds, startDate, endDate, dutyIds, stationId, uuid,siteId,userIds);
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		logger.info("deleteSchedulePersonFromScheduleDutyAndUserIds->scheduleIds:"+scheduleIds+" startDate:"+startDate+" endDate:"+endDate+
				" dutyIds:"+dutyIds+" stationId:"+stationId+" uuid:"+uuid+" siteId:"+siteId+" userIds:"+userIds+" by:"+userInfoScope.getUserId());
		return num;
	}

	@Override
	public ScheduleDetail queryDetailByScheduleId(Integer scheduleId)
			throws Exception {
		return scheduleDetailDao.queryDetailByScheduleId(scheduleId);
	}
	
	@Override
    public Map<String, DutyPersonShiftVo>querySchedulePersonAndShiftBySiteAndTime(String siteId,String[] userIds,String startDateStr,String endDateStr)throws Exception{
    	Map<String, DutyPersonShiftVo>result=scheduleDetailDao.querySchedulePersonAndShiftBySiteAndTime(siteId,userIds,startDateStr,endDateStr);
    	logger.info("querySchedulePersonAndShiftBySiteAndTime---->"+siteId+" userIds:"+userIds+" "+startDateStr+" "+endDateStr+" size:"+result.size());
		return result;
    }

	@Override
	@Transactional( propagation=Propagation.REQUIRED )
	public Integer refreshDutyPersonSchedule(String siteId, Date startDate,
			Date endDate, Boolean isRebuild) throws Exception {
		logger.info("start refreshDutyPersonSchedule->siteId:"+siteId+" startDate:"+startDate+" endDate:"+endDate+" isRebuild:"+isRebuild);
		Integer delNum=0;
		if(isRebuild){
			delNum=deleteSchedulePersonFromScheduleDutyAndUserIds(null, startDate, endDate, null, null, null, siteId, null);
		}
		Integer addNum=insertSchedulePersonFromScheduleDutyAndUserIds(null, startDate, endDate, null, null, null, siteId, null);
		logger.info("finish refreshDutyPersonSchedule->siteId:"+siteId+" startDate:"+startDate+" endDate:"+endDate+" isRebuild:"+isRebuild+" delNum:"+delNum+" addNum:"+addNum);
		return addNum;
	}

	@Override
	@Transactional( propagation=Propagation.REQUIRED )
	public Integer updateSchedulePersonIsPresent(Integer scheduleId, String[]userIds, String isPresent)
			throws Exception {
		return scheduleDetailDao.updateSchedulePersonIsPresent(scheduleId, userIds, isPresent);
	}
}