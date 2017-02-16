package com.timss.operation.service.core;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.operation.bean.Note;
import com.timss.operation.bean.PersonJobs;
import com.timss.operation.bean.Shift;
import com.timss.operation.dao.HandoverDao;
import com.timss.operation.dao.NoteDao;
import com.timss.operation.dao.PersonJobsDao;
import com.timss.operation.service.DutyService;
import com.timss.operation.service.HandoverService;
import com.timss.operation.service.JobsService;
import com.timss.operation.service.NoteService;
import com.timss.operation.service.PersonJobsService;
import com.timss.operation.service.ScheduleDetailService;
import com.timss.operation.service.ShiftService;
import com.timss.operation.util.DateFormatUtil;
import com.timss.operation.util.ReturnCodeUtil;
import com.timss.operation.util.SortUtil;
import com.timss.operation.vo.CalendarVo;
import com.timss.operation.vo.HandoverHistoryVo;
import com.timss.operation.vo.HandoverVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 运行记事service Implements
 * @description: 
 * @company: gdyd
 * @className: NoteServiceImpl.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("noteService")
@Transactional(propagation=Propagation.SUPPORTS)
public class NoteServiceImpl implements NoteService {
    
    private Logger log = Logger.getLogger( NoteServiceImpl.class );

    @Autowired
    private NoteDao noteDao;
    
    @Autowired
    private HandoverDao handoverDao;
    
    @Autowired
    private PersonJobsDao personJobsDao;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private HandoverService handoverService;
    
    @Autowired
    private ShiftService shiftService;
    
    @Autowired
    private DutyService dutyService;
    
    @Autowired
    private JobsService jobsService;
    
    @Autowired
    private ScheduleDetailService scheduleDetailService;
    
    @Autowired
    private PersonJobsService personJobsService;

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public void setNoteDao(NoteDao noteDao) {
        this.noteDao = noteDao;
    }
    
    /**
     * 
     * @description:设置记事时间
     * @author: fengzt
     * @createDate: 2014年7月8日
     * @param writeTimeStr
     * @return:Date
     */
    private Date setWriteTime( String writeTimeStr ){
        String[] arrTime = writeTimeStr.split( ":" );
        int hour = Integer.parseInt( arrTime[0] );
        int min = Integer.parseInt( arrTime[1] );
        
      //当前时间
        DateTime currentTime = new DateTime();
        //当前时间(yyyy-MM-dd)
        Date currentDate = DateFormatUtil.getCurrentDate();
        //前一天(yyyy-MM-dd)
        Date preDate = DateFormatUtil.addDate( currentDate , "d", -1 );
        
        Date writeTime = null;
        //当当前小时 < hour 填写是前一天的数据（班次跨天）
        if( currentTime.getHourOfDay() < hour ){
            Calendar calendar=Calendar.getInstance();
            calendar.setTime( preDate );
            calendar.set( Calendar.HOUR_OF_DAY, hour );
            calendar.set( Calendar.MINUTE, min );
            writeTime =  calendar.getTime();
            log.info( writeTime );
        }else{
            //当天数据
            Calendar calendar=Calendar.getInstance();
            calendar.setTime( currentDate );
            calendar.set( Calendar.HOUR_OF_DAY, hour );
            calendar.set( Calendar.MINUTE, min );
            writeTime =  calendar.getTime();
            log.info( writeTime );
        }
        return writeTime;
    }

    /**
     * @description:插入一条运行记事
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param note 其中id为自增，不需要设置
     * @return:Note
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Note insertNote(Note note) {
        //时分（格式10:18）
        String writeTimeStr = note.getWriteTimeStr();
        Date writeTime = setWriteTime( writeTimeStr );
        note.setWriteTime( writeTime );
        
        //登录用户
        String userId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            userId = userInfoScope.getUserId();
            
           /* //判断用户是否在值班
            if( !isOnDuty( userId ) ){
                note.setCreateBy( "您不是当值人员！" );
                return note;
            }*/
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        note.setCreateBy( userId );
        note.setUpdateBy( userId );
        
        note.setCreateTime( new Date() );
        note.setUpdateTime( new Date() );
        
        noteDao.insertNote( note );
        return note;
    }

    /**
     * @description:更新运行记事表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param note:
     * @return int
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateNote(Note note) {
        //时分（格式10:18）
        String writeTimeStr = note.getWriteTimeStr();
        Date writeTime = setWriteTime( writeTimeStr );
        note.setWriteTime( writeTime );
        
        //登录用户
        String userId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            userId = userInfoScope.getUserId();
           /* //判断用户是否在值班
            if( !isOnDuty( userId ) ){
                return 10000;
            }*/
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        note.setUpdateBy( userId );
        note.setUpdateTime( new Date() );
        
        return noteDao.updateNote( note );
    }

    /**
     * @description:通过Id拿到运行记事表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param id
     * @return:Note
     */
    public Note queryNoteById(int id) {

        return noteDao.queryNoteById( id );
    }

    /**
     * @description:通过ID 删除 note
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param id:
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteNoteById(int id) {
        /*try {
            if( !isOnDutyByLoginUser() ){
                return 10000;
            }
        } catch (ParseException e) {
            log.error( e.getMessage() );
        }*/
        return noteDao.deleteNoteById( id );
    }

    /**
     * @description:运行记事列表 高级搜索
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param map HashMap
     * @param page HashMap
     * @return:List<Note>
     */
    public List<Note> queryNoteBySearch(Page<HashMap<?, ?>> page) {
        List<Note> noteList = noteDao.queryNoteBySearch( page );
        return noteList;
    }

    /**
     * 
     * @description:获取当值的值别、班次、值别人员、上一班情况,若没有数据则根据当前时间初始化
     * @author: huanglw
     * @param jobsId 
     * @createDate: 2014年7月15日
     * @return:
     * @throws Exception 
     */
    public Map<String, Object> queryOnDutyBaseInfo(String stationId,String jobsId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        if(stationId == null || stationId.equals( "" )){
            map.put( "returnCode", ReturnCodeUtil.STATIONID_NOT_EXIST );
            return map;
        }
        if(jobsId == null || jobsId.equals( "" )){
            map.put( "returnCode", ReturnCodeUtil.JOBSID_NOT_EXIST );
            return map;
        }
        map = handoverService.queryNowAndLastHandoverVo( jobsId );
        HandoverVo currentHandoverVo = null;
        if( map.containsKey( "currentHandover" )){
            currentHandoverVo = ( HandoverVo )map.get( "currentHandover" );
        }else{
            //如果当班交接班记录为空，说明需要根据当前时间初始化一条未交接的handover
            currentHandoverVo = handoverService.initHandover(stationId,Integer.parseInt( jobsId ));
            if(currentHandoverVo != null){
                map.put( "currentHandover", currentHandoverVo );
            }else{
                map.put( "returnCode", ReturnCodeUtil.INIT_HANDOVER_FAIL );
                return map;
            }
        }
        Date nextShiftDate = currentHandoverVo.getNextShiftDate();
        String dateString =  DateFormatUtil.formatDate(nextShiftDate,"yyyy-MM-dd" );
        String nowDateStr=DateFormatUtil.formatDate(currentHandoverVo.getNowShiftDate(),"yyyy-MM-dd" );
        List<CalendarVo> cVos= scheduleDetailService.getCalendarByDS(dateString, stationId);
        //如果下一班日期和当前班日期同一天，要过滤掉当前班和之前的班次，休息的开始时间是0000，所以也被过滤了
        if(nowDateStr.equals(dateString)){
        	cVos=scheduleDetailService.getAvailableCalendarByDS(cVos,currentHandoverVo.getCurrentShiftStartTime());
        }
        int currentDutyId = currentHandoverVo.getCurrentDutyId();
        List<PersonJobs> personJobs = dutyService.queryDutyPersons( currentDutyId,null,null,null );
        List<PersonJobs> personJobsOfStation = jobsService.queryJobsPersons( Integer.parseInt( jobsId ) );
        List<PersonJobs> schedulePerson=scheduleDetailService.querySchedulePersonByScheduleId(currentHandoverVo.getNowScheduleId(),"Y");
        List<PersonJobs> handoverPerson=handoverService.queryHandoverPersonByHandoverId(currentHandoverVo.getId());
        
        map.put( "currentPersonList", personJobs );//当前值别的人
        map.put( "schedulePersonList", schedulePerson );//当前排班实际值班的人
        map.put( "handoverPersonList", handoverPerson );//当前交接班实际值班的人
        map.put( "nextShiftCalendars", cVos );
        map.put( "personJobsOfStation", personJobsOfStation );//同岗位的人交接班
        map.put( "returnCode", ReturnCodeUtil.SUCCESS );
        
        return map;
    }
    
    /**
     * 
     * @description:根据userId去掉重复的personJobs
     * @author: fengzt
     * @createDate: 2015年1月1日
     * @param personJobs
     * @return:
     */
    private List<PersonJobs> dropRepeatPersonJobs(List<PersonJobs> personJobs) {
        List<PersonJobs> list = new ArrayList<PersonJobs>();
        for( PersonJobs vo : personJobs ){
            //是否存在
            boolean flag = false;
            for( PersonJobs temp : list ){
                if( vo.getUserId().equals( temp.getUserId() )  ){
                    flag = true;
                    break;
                }
            }
            if( !flag ){
                list.add( vo );
            }
        }
        
        return list;
    }

    /**
     * 
     * @description:现在用户是否值班
     * @author: fengzt
     * @createDate: 2014年7月18日
     * @param userId
     * @return:boolean
     * @throws ParseException 
     */
    public boolean isOnDuty( String userId ) throws ParseException{
        //判断班次 startTime + longTime 是否跟现在时间匹配
        //yyyy-MM-dd
        Date currentDate = DateFormatUtil.getCurrentDate();
        Date date = new Date();
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "userId", userId );
        map.put( "currentDate", currentDate );
        
        List<Shift> shiftList = noteDao.isOnDuty( map );
        
        if( !shiftList.isEmpty() ){
            Shift shift = shiftList.get( 0 );
            String startTime = shift.getStartTime();
            int longTime = shift.getLongTime();
            
            String dateStr = DateFormatUtil.dateToString( currentDate, "yyyy-MM-dd" ) + startTime;
            Date startDate = DateFormatUtil.parseDate( dateStr, "yyyy-MM-ddhhmm" );
            Date endDate = DateFormatUtil.addDate( startDate, "H", longTime );
            
            if( date.compareTo( startDate ) > 0 && date.compareTo( endDate ) < 0 ){
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 
     * @description:登录用户是否值班
     * @author: fengzt
     * @createDate: 2014年7月18日
     * @return:boolean
     * @throws ParseException 
     */
    public boolean isOnDutyByLoginUser() throws ParseException{
        //登录用户
        String userId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            userId = userInfoScope.getUserId();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        return isOnDuty( userId );
    }

    /**
     * 
     * @description:查询运行日志时获取基本信息和交接班信息
     * @author: huanglw
     * @createDate: 2014年7月21日
     * @param stationId
     * @param jobsId 工种id
     * @param shiftId 班次id
     * @param date 日期
     * @return:Map<String, Object>
     * @throws Exception 
     */
    public Map<String, Object> queryNoteBaseInfoBySearch(String stationId, String jobsId, String shiftId, String date) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        
        //查询交接班记录
        List<HandoverVo> handovers = queryHandoverHistory(stationId, jobsId, shiftId, date ) ;
        
        //查询人员值别信息
        //List<PersonJobs> personJobs = queryPersonInfoHistory(stationId, "0", shiftId,date ) ;
        
        //查询排班日历
        List<CalendarVo> calendarVos = scheduleDetailService.getCalendarByDS( date, stationId );
        /*if(handovers != null && personJobs != null){
            map.put( "handovers", handovers );
            map.put( "personJobs", personJobs );
            map.put( "calendars", calendarVos );
            map.put( "returnCode", returnCodeUtil.SUCCESS );
        }else{
            map.put( "returnCode", returnCodeUtil.FAIl );
        }*/
        List<HandoverHistoryVo> handoverHistoryVos = new ArrayList<HandoverHistoryVo>();
        int shiftIdOfInt = StringUtils.isBlank(shiftId)?0:Integer.parseInt( shiftId );
        if(handovers != null /*&& personJobs != null*/ && calendarVos != null){
            SortUtil.sortList( calendarVos, "startTime", true );
            for ( CalendarVo cVo : calendarVos ) {
                if("normal".equals( cVo.getShiftType() ) && (shiftIdOfInt == 0 || shiftIdOfInt == cVo.getShiftId())){
                    HandoverVo tempHandoverVo = getHandoverVoByScheduleId( handovers, cVo.getId() );
                    HandoverHistoryVo temp = new HandoverHistoryVo();
                    temp.setDutyId( cVo.getDutyId() );
                    temp.setDutyName( cVo.getDutyName() );
                    temp.setShiftId( cVo.getShiftId() );
                    temp.setShiftName( cVo.getShiftName() );
                    temp.setDeptName(cVo.getDeptName());
                    temp.setIsOver( "未交接" );
                    temp.setNowScheduleId(cVo.getId());
                    
                    if( tempHandoverVo != null ){
                        temp.setHandoverId( tempHandoverVo.getId() );
                        temp.setPreShiftMeeting(tempHandoverVo.getPreShiftMeeting());
                        temp.setPostShiftMeeting(tempHandoverVo.getPostShiftMeeting());
                    }
                    if(tempHandoverVo != null && tempHandoverVo.getIsOver().equals( "Y" )){
                        temp.setIsOver( "已交接" );
                        temp.setNextContent( tempHandoverVo.getNextContent() );
                        temp.setNextRemark( tempHandoverVo.getNextRemark() );
                        temp.setLastUserName(tempHandoverVo.getCurrentPersonName());
                        temp.setNextUserName(tempHandoverVo.getNextPersonName());
                    }
                    
                    List<PersonJobs> personJobs = handoverService.queryHandoverPersonByHandoverId(temp.getHandoverId());//scheduleDetailService.querySchedulePersonByScheduleId(cVo.getId(),"Y");//dutyService.queryDutyPersons(cVo.getDutyId(),null,null,null);
                    String memberString = getMemberStringByDutyId(personJobs);
                    temp.setPersons( memberString );
                    handoverHistoryVos.add( temp );
                }
            }  
        }
        map.put( "rows", handoverHistoryVos );
        map.put( "total", handoverHistoryVos == null ? 0 : handoverHistoryVos.size() );
        return map;
    }
    
    /**
     * 
     * @description:
     * @author: huanglw
     * @createDate: 2014年7月31日
     * @param nextPerson
     * @return:
     */
    private String getUserNameByUserId(List<PersonJobs> personJobs,String userId) {
        String result = "无名氏";
        for ( PersonJobs p : personJobs ) {
            if( p.getUserId().equals( userId ) ){
                result = p.getUserName();
                break;
            }
        }
        return result;
    }

    /**
     * 
     * @description:通过值别id获取同一值别的人员，并拼接成字符串
     * @author: huanglw
     * @createDate: 2014年7月31日
     * @param dutyId
     * @return:String
     */
    private String getMemberStringByDutyId(List<PersonJobs> personJobs) {
        String result = "";
        for ( PersonJobs p : personJobs ) {
            result += p.getUserName() + ",";
        }
        if(!result.equals( "" )){
            result = result.substring( 0, result.length() - 1 );
        }
        return result;
    }

    /**
     * 
     * @description:通过scheduleid获取对应的handover记录，没有则返回null
     * @author: huanglw
     * @createDate: 2014年7月31日
     * @param cVos
     * @param scheduleId
     * @return:HandoverVo
     */
    private HandoverVo getHandoverVoByScheduleId(List<HandoverVo> cVos,int scheduleId){
       for ( HandoverVo handoverVo : cVos ) {
           if( handoverVo.getNowScheduleId() == scheduleId){
               return handoverVo;
           }
       }
       return null;
    }
    
    /**
     * 
     * @description:查询对应日期的排班下对应值班、班次、工种的人员信息
     * @author: huanglw
     * @createDate: 2014年7月21日
     * @param stationId
     * @param jobsId
     * @param shiftId
     * @param date
     * @return:List<PersonJobs>
     */
    private List<PersonJobs> queryPersonInfoHistory(String stationId, String jobsId, String shiftId,String date) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        
        paramsMap.put( "stationId", stationId );
        paramsMap.put( "jobsId", Integer.parseInt( jobsId ) );
        paramsMap.put( "shiftId", Integer.parseInt( shiftId ) );
        Date dateYMD = DateFormatUtil.parseDate( date, "yyyy-MM-dd" );
        paramsMap.put( "dateYMD", dateYMD );
        
        return personJobsDao.queryPersonInfoHistory(paramsMap);
    }

    /**
     * 
     * @description:查询对应日期下排班的交接班记录
     * @author: huanglw
     * @createDate: 2014年7月21日
     * @param stationId
     * @param jobsId
     * @param shiftId
     * @param date
     * @return:
     */
    private List<HandoverVo> queryHandoverHistory(String stationId, String jobsId, String shiftId, String date) {
        Date dateYMD = DateFormatUtil.parseDate( date, "yyyy-MM-dd" );        
       /* Map<String, Object> paramsMap = new HashMap<String, Object>();
        
        paramsMap.put( "dateYMD", dateYMD );
        paramsMap.put( "stationId", stationId );
        paramsMap.put( "jobsId", jobsId );
        paramsMap.put( "shiftId", shiftId );*/
        
        List<HandoverVo> handovers = handoverService.queryHandoverBySDS( stationId, dateYMD, shiftId, jobsId );
        return handovers;
    }

}

