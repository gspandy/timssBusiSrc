package com.timss.operation.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.operation.bean.Handover;
import com.timss.operation.bean.PersonJobs;
import com.timss.operation.dao.HandoverDao;
import com.timss.operation.service.DutyService;
import com.timss.operation.service.HandoverService;
import com.timss.operation.service.NoteService;
import com.timss.operation.service.ScheduleDetailService;
import com.timss.operation.service.ShiftService;
import com.timss.operation.util.DateFormatUtil;
import com.timss.operation.util.ReturnCodeUtil;
import com.timss.operation.vo.CalendarVo;
import com.timss.operation.vo.HandoverVo;
import com.timss.operation.vo.NoteBaseVo;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 运行记事service Implements
 * @description: 
 * @company: gdyd
 * @className: HandoverServiceImpl.java
 * @author: huanglw
 * @createDate: 2014年7月3日
 * @updateUser: huanglw
 * @version: 1.0
 */
@Service("handoverService")
@Transactional(propagation=Propagation.SUPPORTS)
public class HandoverServiceImpl implements HandoverService {
    //log4j 
    private Logger logger = Logger.getLogger(HandoverServiceImpl.class);

    @Autowired
    private HandoverDao handoverDao;
    
    @Autowired
    private ShiftService shiftService;
    
    @Autowired
    private NoteService noteService;
    
    @Autowired
    private DutyService dutyService;
    
    @Autowired
    private ScheduleDetailService scheduleDetailService;
    
    @Autowired
    private IAuthorizationManager iAuthorizationManager;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    /**
     * @description:提交交接班数据
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param formData JSON String
     * @return:Map<String, Object>
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> commitHandover(HandoverVo handoverVo) throws Exception{
        Map<String, Object> resultMap = validateHandoverUsers(handoverVo);
        //判断用户输入的工号是否存在,前提条件是同一个岗位（stationId）下
        if(resultMap.containsKey( "returnCode" )){
            return resultMap;
        }
        Date dateTime = new Date();
        handoverVo.setUpdateDate( dateTime );
        handoverVo.setIsOver( "Y" );
        int updateNum = updateHandover( handoverVo );
        if( updateNum <= 0 ){
            resultMap.put( "returnCode", ReturnCodeUtil.UPDATE_HANDOVER_FAIL);
        }else{
            Handover handover = insertHandoverByLastHandoverVo( handoverVo );
            if( handover != null && handover.getId() > 0 ){
                resultMap.put( "returnCode", ReturnCodeUtil.SUCCESS );
            }else{
                resultMap.put( "returnCode", ReturnCodeUtil.INSERT_HANDOVER_FAIL );
            }  
        }
        return resultMap;
    }
    
    /**
     * 
     * @description:根据上一班的交接班记录，插入下一班的交接班记录
     * @author: huanglw
     * @createDate: 2014年7月22日
     * @param lasthandoverVo 上一班的交接班记录
     * @return:Handover 返回更新了id的 handover
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Handover insertHandoverByLastHandoverVo(HandoverVo lasthandoverVo) throws Exception {
        Handover handover = new Handover();
        handover.setJobsId( lasthandoverVo.getJobsId() );
        handover.setWriteDate( new Date() );
        handover.setIsOver( "N" );
        int nowScheduleId = lasthandoverVo.getNextScheduleId();
        handover.setNowScheduleId ( nowScheduleId );
        CalendarVo nextCalendarVo = scheduleDetailService.getNextCalendarVoById( nowScheduleId );
        if( nextCalendarVo == null){
            return null;
        }
        handover.setNextScheduleId( nextCalendarVo.getId() );
        handoverDao.insertHandover( handover );
        
        if(0!=handover.getId()){//插入成功
        	//插入下一班的值班人员，额外添加接班人
        	//List<PersonJobs> personJobs = dutyService.queryDutyPersons( lasthandoverVo.getNextDutyId(),null,null,null );
        	List<PersonJobs> personJobs = scheduleDetailService.querySchedulePersonByScheduleId(handover.getNowScheduleId(), null);
        	List<String>handoverPersonList=new ArrayList<String>();
        	Boolean isCurrentPersonExist=false;
        	for (PersonJobs tmp : personJobs) {
        		handoverPersonList.add(tmp.getUserId());
        		if(tmp.getUserId().equals(lasthandoverVo.getNextPerson())){
        			isCurrentPersonExist=true;
        		}
			}
        	if(!isCurrentPersonExist){
        		handoverPersonList.add(lasthandoverVo.getNextPerson());
        	}
        	updateHandoverPerson(handover.getId(), handoverPersonList, itcMvcService.getUserInfoScopeDatas().getSecureUser());
        }
        
        return handover;
    }
    
    /**
     * 
     * @description:插入一条交接班记录
     * @author: huanglw
     * @createDate: 2014年7月22日
     * @param Handover
     * @return:Handover 返回更新了id的 handover
     */
    public Handover insertHandover(Handover handover){
        handoverDao.insertHandover( handover );
        return handover;
    }

    //验证交接班的双方是否存在以及密码是否正确
    private Map<String, Object> validateHandoverUsers(HandoverVo handoverVo) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<PersonJobs> personJobs = handoverDao.validateHandoverUserJobsId(handoverVo);
        boolean currentPersonExist = false;
        boolean nextPersonExist = false;
        for ( PersonJobs p : personJobs ) {
            if(p.getUserId().equals( handoverVo.getCurrentPerson())){
                currentPersonExist = true;
            }
            if(p.getUserId().equals( handoverVo.getNextPerson())){
                nextPersonExist = true;
            }
        }
        
        if( !nextPersonExist ){
            resultMap.put( "returnCode", ReturnCodeUtil.NEXTPERSON_NOT_EXIST ); 
        }else if( !currentPersonExist ){
            resultMap.put( "returnCode", ReturnCodeUtil.CURRENTPERSON_NOT_EXIST );
        }else{
        	//校验交班人密码
            boolean currentFlag = iAuthorizationManager.verifyPassword( handoverVo.getCurrentPerson(), handoverVo.getCurrentUserPassword() );
            if( !currentFlag ){
                resultMap.put( "returnCode", ReturnCodeUtil.CURRENT_PWD_ERROE);
            }else{
            	//校验接班人密码
                boolean nextFlag = iAuthorizationManager.verifyPassword( handoverVo.getNextPerson(), handoverVo.getNextUserPassword() );
                if( !nextFlag ){
                    resultMap.put( "returnCode", ReturnCodeUtil.NEXT_PWD_ERROE);
                }
            }
        }
        return resultMap;
    }

    /**
     * @description:更新运行记事表
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param handover:
     * @return int
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateHandover(HandoverVo handoverVo) {
        
        return handoverDao.updateHandover( handoverVo );
    }

    /**
     * @description:通过Id拿到运行记事表
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param id
     * @return:Handover
     */
    public Handover queryHandoverById(int id) {

        return handoverDao.queryHandoverById( id );
    }

    /**
     * @description:通过ID 删除 handover
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param id:
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteHandoverById(int id) {

        return handoverDao.deleteHandoverById( id );
    }

    /**
     * @description:handover分页
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param page
     * @return:
     */
    public List<Handover> queryHandoverByPage(Page<HashMap<?, ?>> page) {
        page.setSortKey( "id" );
        page.setSortOrder( "asc" );
        
        return handoverDao.queryHandoverByPage( page );
    }

    /**
     * @description:交接班记录高级搜索(暂没使用）
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param map HashMap
     * @param page HashMap
     * @return:List<Handover>
     */
    public List<Handover> queryHandoverBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> page) {
        page.setParameter( "id", map.get( "id" ) );
        page.setParameter( "content", map.get( "content" ) );
        page.setParameter( "currentPerson", map.get( "currentPerson" ) );
        page.setParameter( "nextPerson", map.get( "nextPerson" ) );
        page.setParameter( "writeDate", map.get( "writeDate" ) );
        page.setParameter( "dutyId", map.get( "dutyId" ) );
        page.setParameter( "shiftId", map.get( "shiftId" ) );
        
        page.setSortKey( "id" );
        page.setSortOrder( "asc" );

        List<Handover> handoverList = handoverDao.queryHandoverBySearch( page );
        return handoverList;
    }

   /* *//**
     * 
     * @description:获取上一班的交接班Handover
     * @author: huanglw
     * @createDate: 2014年7月16日
     * @param dateTime 日期时间 
     * @param stationId 岗位id
     * @return:List<Handover>
     *//*
    public List<Handover> queryLastHandover(Date dateTime, String stationId) {
        Map<String, Object> map = queryCLNShiftByDateTime( dateTime, stationId );
        if(map == null || !(map.get( "returnCode" ).equals( returnCodeUtil.SUCCESS ))){
            return null;
        }
        CalendarVo lastCVo = (CalendarVo) map.get( "lastCalendarVo" );
        return handoverDao.queryHandoverByCalendarVo(lastCVo);
    }*/
    
    /**
     * @description:对传入的班次列表筛选出非休息类型的班次并且按startTime升序排列
     * @author: huanglw
     * @createDate: 2014年7月14日
     * @param shiftList
     * @return:List<Shift>
     *//*
    private List<Shift> splitNormalShifts(List<Shift> shiftList){
        List<Shift> normalShifts = new ArrayList<Shift>();
        //正常班才加入
        for( Shift shift : shiftList ){
            String type = shift.getType();
            if( "normal".equalsIgnoreCase( type ) ){
                normalShifts.add( shift );
            }
        }
        
        //对班次进行排序，安装startTime升序排列
        SortUtil.sortList( normalShifts, "startTime", true );
        return normalShifts;
    }*/
    
    /**
     * @description:通过传入的时间和岗位id获取CLN ： current Last Next shift
     * @author: huanglw
     * @createDate: 2014年7月16日
     * @param dateTime 日期时间 
     * @param stationId 岗位id     
     * @return:Map<String, Object> returnCode == success时表示成功
     *//*
    public Map<String, Object> queryCLNShiftByDateTime(Date dateTime,String stationId){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Shift> shiftList = shiftService.queryShiftByStationId( stationId );
        //从该岗位下所有班次中筛选出类型不等于rest的班次,并且按startTime升序排序
        List<Shift> shiftWithOutRest = splitNormalShifts(shiftList);
        //非reset类型的班次如果少于2个，是否有交接班过程？待考虑
        if( shiftWithOutRest.size() < 2 ){
            logger.info("岗位" + stationId + "的非休息班次只有" + shiftWithOutRest.size() + "个, 不足2个");
            resultMap.put("returnCode",returnCodeUtil.NOT_ENOUGH_SHIFT);
            return resultMap;
        }
        //在shiftwithOutRest里面找到传入时间所对应的班次以及相邻班次
        resultMap = findCLNShift( shiftWithOutRest, dateTime );
        
        if( resultMap.containsKey( "returnCode" )){
            return resultMap;
        }
        String dateString = DateFormatUtil.formatDate( dateTime, "yyyy-MM-dd");
        Shift currentShift = (Shift) resultMap.get( "currentShift" );
        //如果当前班次是当天第一班，则要取前一天的最后一班交接班情况
        if( shiftWithOutRest.get( 0 ).getId() == currentShift.getId() ){
            logger.info( "当班是当天第一班，正在获取前一天的交接班数据" );
            Date lastDate = DateFormatUtil.addDate( dateTime, "d", -1 );
            String lastDateString = DateFormatUtil.formatDate( lastDate, "yyyy-MM-dd" );
            CalendarVo lastCalendarVo = scheduleDetailService.getCalendarByDateShift( lastDateString, stationId, currentShift.getId());
            if( lastCalendarVo != null ){
                resultMap.put( "lastCalendarVo",lastCalendarVo );
            }
        }
        List<CalendarVo> cVoList = scheduleDetailService.getCalendarByDS( dateString, stationId);
        for ( CalendarVo calendarVo : cVoList ) {
            if( calendarVo.getShiftId() == currentShift.getId()){
                resultMap.put( "currentCalendarVo",calendarVo );
            }
            if( !(resultMap.containsKey( "lastCalendarVo" )) && calendarVo.getShiftId() == ((Shift)resultMap.get( "lastShift" )).getId()){
                resultMap.put( "lastCalendarVo",calendarVo );
            }
            if( calendarVo.getShiftId() == ((Shift)resultMap.get( "nextShift" )).getId()){
                resultMap.put( "nextCalendarVo",calendarVo );
            }
        }
        if(!(resultMap.containsKey("currentCalendarVo")
                &&resultMap.containsKey("lastCalendarVo")
                &&resultMap.containsKey( "nextCalendarVo" )))
        {
            logger.info("Current Last Next calendar 不完整");
            resultMap.put( "returnCode", returnCodeUtil.LACK_OF_CLN_CALENDARVO );
            return resultMap;
        }
        //如果正常至此处，则返回成功
        resultMap.put( "returnCode", returnCodeUtil.SUCCESS );
        return resultMap;
    }
    
    *//**
     * 传入非休息班次列表和时间，找到匹配当前时间的班次以及相邻的班次
     *//*
    private Map<String, Object> findCLNShift(List<Shift> shiftWithOutRest,Date dateTime){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int nextIndex = -1;
        int lastIndex = -1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( dateTime );
        int hour = calendar.get( Calendar.HOUR_OF_DAY );
        //在非rest类型班次list里找传入的calendar是否有对应班次
        for(int index = 0;index < shiftWithOutRest.size();index++){
            Shift tempShift = shiftWithOutRest.get( index );
            String startTime = shiftWithOutRest.get( index ).getStartTime();
            int longTime = tempShift.getLongTime();
            int startHour = Integer.parseInt( startTime.substring( 0, 2 ) );
            if(hour >= startHour && (hour-startHour) < longTime){
                resultMap.put( "currentShift", shiftWithOutRest.get( index ) );
                if(index == shiftWithOutRest.size() - 1){
                    nextIndex = 0;
                    lastIndex = index - 1;
                }else if(index == 0){
                    nextIndex = 1;
                    lastIndex = shiftWithOutRest.size() - 1;
                }else{
                    nextIndex = index + 1;
                    lastIndex = index - 1;
                }
                resultMap.put( "nextShift", shiftWithOutRest.get( nextIndex ) );
                resultMap.put( "lastShift", shiftWithOutRest.get( lastIndex ) );
              break; 
            }
        }
        if(!(resultMap.containsKey( "currentShift" ))){
            logger.info( "该岗位下没有对应'" + DateFormatUtil.formatDate( dateTime, "yyyy-MM-dd HH:mm:ss" ) + "'的班次" );
            resultMap.put("returnCode",returnCodeUtil.NO_MATCHED_SHIFT);
            return resultMap;
        }
        return resultMap;
    }*/
    
    /**
     * 
     * @description:通过岗位、日期、班次获取交接班记录，jobsId可选
     * @author: huanglw
     * @createDate: 2014年7月29日
     * @param stationId
     * @param dateYMD
     * @param shiftId
     * @param jobsId（若不指定，可以为null）
     * @return:
     */
    public List<HandoverVo> queryHandoverBySDS(String stationId,Date dateYMD,String shiftId,String jobsId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "stationId", stationId );
        map.put( "dateYMD", dateYMD );
        map.put( "shiftId", shiftId );
        map.put( "jobsId", jobsId );
        return handoverDao.queryHandoverBySDS(map);
    }

    /**
     * 
     * @description:
     * 获取当班基本信息和上一班基本信息，以map形式返回
     * 其中的rerutnMap的key至少有nowHandoverVo lastHandoverVo returnCode
     * @author: huanglw
     * @createDate: 2014年7月23日
     * @param jobsId
     * @return:Map<String, Object>
     */
    public Map<String, Object> queryNowAndLastHandoverVo(String jobsId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        
        List<HandoverVo> handoverVos = handoverDao.queryNowAndLastHandoverVo(jobsId);
        for ( HandoverVo handoverVo : handoverVos ) {
            if( handoverVo.getIsOver().equals( "N" )){
                resultMap.put( "currentHandover", handoverVo );
            }
            if( handoverVo.getIsOver().equals( "Y" )){
                resultMap.put( "lastHandover", handoverVo );
            }
        }
        
        return resultMap;
    }

    /**
     * 
     * @description:通过当前时间和岗位id初始化一条未交接的记录(系统初始化时候)
     * @author: huanglw
     * @createDate: 2014年7月23日
     * @param stationId
     * @return:新创建的HandoverVo
     */
    public HandoverVo initHandover(String stationId,int jobsId) {
        Date nowDate = new Date();
        String dateString = DateFormatUtil.formatDate(nowDate, "yyyy-MM-dd");
        int hour = Integer.parseInt( DateFormatUtil.formatDate( nowDate, "HH" ) );
        List<CalendarVo> cVos= scheduleDetailService.getCalendarByDS( dateString, stationId );
        CalendarVo nowCalendarVo = null;
        CalendarVo nextCalendarVo = null;
        for ( CalendarVo calendarVo : cVos ) {
            int startHour = Integer.parseInt( calendarVo.getStartTime().substring( 0, 2 ) );
            //当前时间08:52 临界时候要等号
            if(calendarVo.getShiftType().equals( "normal" ) && hour >= startHour && hour < (startHour + calendarVo.getLongTime())){
                nowCalendarVo = calendarVo;
                nextCalendarVo = scheduleDetailService.getNextCalendarVoById( nowCalendarVo.getId() );
                break;
            }
        }
        HandoverVo handoverVo = fillHandoverVoByCalendarVo( nowCalendarVo, nextCalendarVo );
        if( handoverVo != null){
            handoverVo.setIsOver( "N" );
            handoverVo.setJobsId( jobsId );
            handoverVo.setStationId( stationId );
            handoverVo = insertHandover( handoverVo );
        }
        return handoverVo;
        
    }
    
    /**
     * 
     * @description:插入一条交接班记录
     * @author: huanglw
     * @createDate: 2014年7月23日
     * @param handoverVo
     * @return:HandoverVo（插入成功时id会自增）
     */
    public HandoverVo insertHandover(HandoverVo handoverVo) {
        Handover handover = new Handover();
        handover.setJobsId( handoverVo.getJobsId() );
        handover.setWriteDate( new Date() );
        handover.setIsOver( "N" );
        handover.setNowScheduleId ( handoverVo.getNowScheduleId() );
        handover.setNextScheduleId ( handoverVo.getNextScheduleId() );
        insertHandover( handover );
        handoverVo.setId( handover.getId() );
        return handoverVo;
    }

    /**
     * 
     * @description:通过当前日历和下一班日历的信息填充HandoverVo，包括值别班次的id和名字，下一班的日期时间等
     * @author: huanglw
     * @createDate: 2014年7月23日
     * @param nowCalendarVo
     * @param nextCalendarVo
     * @return:
     */
    private HandoverVo fillHandoverVoByCalendarVo(CalendarVo nowCalendarVo,CalendarVo nextCalendarVo){
        HandoverVo handoverVo = null;
        if( nowCalendarVo != null && nextCalendarVo != null){
            handoverVo =  new HandoverVo();
            handoverVo.setNowScheduleId( nowCalendarVo.getId() );
            handoverVo.setNextScheduleId( nextCalendarVo.getId() );
            handoverVo.setCurrentDutyId( nowCalendarVo.getDutyId() );
            handoverVo.setCurrentDutyName( nowCalendarVo.getDutyName() );
            handoverVo.setCurrentShiftId( nowCalendarVo.getShiftId() );
            handoverVo.setCurrentShiftName( nowCalendarVo.getShiftName() );
            handoverVo.setNextDutyId( nextCalendarVo.getDutyId() );
            handoverVo.setNextDutyName( nextCalendarVo.getDutyName() );
            handoverVo.setNextShiftId( nextCalendarVo.getShiftId() );
            handoverVo.setNextShiftName( nextCalendarVo.getShiftName() );
            handoverVo.setNowShiftDate( nowCalendarVo.getDateTime() );
            handoverVo.setNextShiftDate( nextCalendarVo.getDateTime() );
            handoverVo.setCurrentShiftStartTime(nowCalendarVo.getStartTime());
            handoverVo.setNextShiftStartTime(nextCalendarVo.getStartTime());
        }
        return handoverVo;
    }
    
    /**
     * @description:判断指定日期、岗位、值别（可选）的日历是否对应有交接班记录
     * @author: huanglw
     * @createDate: 2014年7月9日
     * @param dateYMD String
     * @param stationId String
     * @param dutyId String
     * @return:boolean
     */
    @Override
    public boolean isExistHandoverByDSD(String dateYMD, String stationId, String dutyId) {
        Date date = DateFormatUtil.parseDate( dateYMD, "yyyy-MM-dd" );
        List<HandoverVo> handoverVos = queryHandoverByDSD( date, stationId, dutyId );
        return handoverVos != null && handoverVos.size() > 0;
    }
    
    /**
     * 
     * @description:通过日期、岗位id，值别获取交接班记录
     * @author: huanglw
     * @createDate: 2014年7月29日
     * @param dateYMD（date类型）
     * @param stationId
     * @param dutyId
     * @return:List<HandoverVo>
     */
    @Override
    public List<HandoverVo> queryHandoverByDSD(Date dateYMD, String stationId, String dutyId){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "stationId", stationId );
        map.put( "dateYMD", dateYMD );
        if(!dutyId.equals( "0" )){
        map.put( "dutyId", dutyId );
        } 
        return handoverDao.queryHandoverByDSD(map);
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Integer updateHandoverNote(NoteBaseVo vo) {
		return handoverDao.updateHandoverNote(vo);
	}

	@Override
    public List<PersonJobs>queryHandoverPersonByHandoverId(Integer handoverId)throws Exception{
    	List<PersonJobs>result=handoverDao.queryHandoverPersonByHandoverId(handoverId);
    	return result;
    }
    
    @Override
    @Transactional( propagation=Propagation.REQUIRED )
    public Integer updateHandoverPerson(Integer handoverId,List<String>userIdList,@Operator SecureUser operator)throws Exception{
    	if(operator==null)operator=itcMvcService.getUserInfoScopeDatas().getSecureUser();
    	List<PersonJobs> handoverPerson=queryHandoverPersonByHandoverId(handoverId);//所有的值班人员
    	//转成map加快查找效率
    	Map<String, PersonJobs>handoverPersonMap=new HashMap<String, PersonJobs>();
    	for (PersonJobs personJobs : handoverPerson) {
    		handoverPersonMap.put(personJobs.getUserId(), personJobs);
		}
    	Map<String, String>userIdMap=new HashMap<String, String>();
    	for (String userId:userIdList) {
    		userIdMap.put(userId, userId);
		}
    	
    	//比对查找要删除的人
    	List<String>delPerson=new ArrayList<String>();
    	for (PersonJobs personJobs : handoverPerson) {
			if(userIdMap.get(personJobs.getUserId())==null){
				delPerson.add(personJobs.getUserId());
			}
		}
    	Integer delNum=0;
    	if(delPerson.size()>0){
    		//删除该交接班要删除的人
    		delNum=handoverDao.deleteHandoverPerson(handoverId,delPerson.toArray(new String[delPerson.size()]));
        	logger.info("remove handoverPerson->handoverId:"+handoverId+" result:"+delNum+"/"+delPerson.size()+" by:"+operator.getId());
    	}
    	
    	//比对查找要增加的人
    	List<String>addPerson=new ArrayList<String>();
    	for (String userId:userIdList) {
			if(handoverPersonMap.get(userId)==null){
				addPerson.add(userId);
			}
		}
    	Integer addNum=0;
    	if(addPerson.size()>0){
    		//给该交接班添加要增加的人
        	addNum=handoverDao.insertHandoverPerson(handoverId,addPerson.toArray(new String[addPerson.size()]));
        	logger.info("add handoverPerson->handoverId:"+handoverId+" result:"+addNum+"/"+addPerson.size()+" by:"+operator.getId());
    	}
    	
    	logger.info("finish updateHandoverPerson->handoverId:"+handoverId+" delNum:"+delNum+" addNum:"+addNum+" by:"+operator.getId());
    	return delNum+addNum;
    }
}