package com.timss.attendance.service.core;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.attendance.bean.DefinitionBean;
import com.timss.attendance.bean.LeaveBean;
import com.timss.attendance.bean.LeaveItemBean;
import com.timss.attendance.bean.OvertimeBean;
import com.timss.attendance.bean.OvertimeItemBean;
import com.timss.attendance.bean.StatBean;
import com.timss.attendance.bean.StatItemBean;
import com.timss.attendance.dao.StatDao;
import com.timss.attendance.service.DefinitionService;
import com.timss.attendance.service.LeaveService;
import com.timss.attendance.service.OvertimeService;
import com.timss.attendance.service.StatService;
import com.timss.attendance.util.AtdDataBaseUtil;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.DateFormatUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.timss.attendance.vo.LeaveContainItemVo;
import com.timss.attendance.vo.StatDetailVo;
import com.timss.attendance.vo.StatVo;
import com.timss.operation.service.DutyService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 统计
 */
@Service("statService")
@Transactional(propagation=Propagation.SUPPORTS)
public class StatServiceImpl implements StatService {
    
    private Logger log = LoggerFactory.getLogger( StatServiceImpl.class );
    
    @Autowired
    private StatDao statDao;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private ISecurityMaintenanceManager iSecurityMaintenanceManager;
    
    @Autowired
    private DefinitionService definitionService;
    
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private OvertimeService overtimeService;
    
    @Autowired
    private AtdUserPrivUtil privUtil;
    @Autowired
    private AtdDataBaseUtil dbUtil;
    
    @Autowired
    private DutyService dutyService;
    
    /**
     * 
     * @description:拿到站点信息
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    private String getSiteId(){
        return privUtil.getUserInfoScope().getSecureUser().getCurrentSite();
    }
    
    /**
     * 
     * @description:高级搜索
     * @author: fengzt
     * @createDate: 2014年9月15日
     * @param map
     * @param pageVo
     * @param year
     * @return:List<StatVo>
     */
    @Override
    public List<StatVo> queryStatBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo, int year) {
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );
        pageVo.setParams( map );
        
        //1-含离职  0-不含离职
        Integer onStatus = (Integer) map.get( "onStatus" );
        if( onStatus == 0 ){
            pageVo.setParameter( "checkStatus", "在职" );
        }
        
        pageVo.setParameter( "siteId", getSiteId() );
        pageVo.setParameter( "year", year);
        
        UserInfoScope userInfo = privUtil.getUserInfoScope();
                
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        }else{
            pageVo.setSortKey( "USERSTATUS asc, USERNAME " );
            pageVo.setSortOrder( "ASC" );
        }
        
        //用户拥有的角色
        String roleFlag = privUtil.getStatPrivLevel();
       
        List<StatVo> rowData = new ArrayList<StatVo>();
        if( roleFlag.equals( "deptMgr" ) ){
        	String deptId = userInfo.getOrgId();
            pageVo.setParameter( "deptId", deptId );
        }else if( roleFlag.equals( "hrMgr" ) ){
            
        }else{
        	String userId = userInfo.getUserId();
            pageVo.setParameter( "userId", userId );
        }
        rowData = statDao.queryStatBySiteIdSearch( pageVo );
        
        return rowData;
    }

    /**
     * 
     * @description:更新一年的统计数据
     * @author: fengzt
     * @createDate: 2014年9月24日
     * @return:int
     */
    @Override
    public int updateCurrentYearStat(){
        Date yearFrist = DateFormatUtil.getYearFrist();
        Date yearLast = DateFormatUtil.getYearLast();
        int year = DateFormatUtil.fromDateToCalendar( yearFrist ).get( Calendar.YEAR );
        
        //通过时间间隔统计报表
        List<StatVo> result = queryStatByDiffDate( yearFrist, yearLast );
        
        //更新一整年的数据
        int count = insertOrUpdateBatch( result, year );
        log.info( "更新的数据为： " + count );
        return count;
    }
    
    /**
     * 
     * @description:默认分页列表
     * @author: fengzt
     * @createDate: 2014年9月15日
     * @param pageVo
     * @param year
     * @return:List<StatVo>
     */
    @Override
    public List<StatVo> queryStatBySiteId(Page<HashMap<?, ?>> pageVo, int year) {

        Date yearFrist = DateFormatUtil.getYearFirstMMDD( year );
        Date yearLast = DateFormatUtil.getYearLastMMDD( year );
        String siteId = getSiteId();
        
        //通过时间间隔统计报表
        List<StatVo> result = queryStatByDiffDate( yearFrist, yearLast );
        
        //更新2014年1月到2014年3月的数据
        if( year == 2014 ){
            result = addStatHistory( result, year, siteId );
        }
        
        //更新一整年的数据
        int count = insertOrUpdateBatch( result, year );
        log.info( "更新的数据为： " + count );
        
        pageVo.setParameter( "siteId", siteId );
        pageVo.setParameter( "year", year);
        
        //当年不显示离职信息
        int currentYear = DateFormatUtil.fromDateToCalendar( new Date() ).get( Calendar.YEAR );
        boolean isCurrentYear = year == currentYear ? true : false;
        pageVo.setParameter( "isCurrentYear", isCurrentYear);
        pageVo.setParameter( "onStatus", "在职");
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );
        
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        }else{
            pageVo.setSortKey( "USERSTATUS asc, USERNAME " );
            pageVo.setSortOrder( "ASC" );
        }
        
        //用户拥有的角色
        String roleFlag = privUtil.getStatPrivLevel();
       
        List<StatVo> rowData = new ArrayList<StatVo>();
        if( roleFlag.equals( "deptMgr" ) ){
        	String deptId = userInfo.getOrgId();
            pageVo.setParameter( "deptId", deptId );
        }else if( roleFlag.equals( "hrMgr" ) ){
            
        }else{
        	String userId = userInfo.getUserId();
            pageVo.setParameter( "userId", userId );
        }
        rowData = statDao.queryStatBySiteId( pageVo );
        
        return rowData;
    }
    
    /**
     * 
     * @description:更新2014年1月到2014年3月的数据
     * @author: fengzt
     * @createDate: 2014年9月24日
     * @param result
     * @param year
     * @param siteId
     * @return:List<StatVo>
     */
    private List<StatVo> addStatHistory(List<StatVo> result, int year, String siteId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "year", year );
        map.put( "siteId", siteId );
        //结果
        List<StatVo> list = new ArrayList<StatVo>();
        
        //查询2014年1月到2014年3月的数据
        List<StatVo> statVos = statDao.queryAllStatHistory( map );
        
        for( StatVo vo : result ){
            String userId = vo.getUserId();
            boolean flag = false;
            for( StatVo historyVo : statVos ){
                if( userId.equalsIgnoreCase( historyVo.getUserId() ) ){
                    flag = true;
                    vo = addStatVo( vo, historyVo);
                    list.add( vo );
                    break;
                }
            }
            
            if( !flag ){
                list.add( vo );
            }
        }
        
        for( StatVo sVo : list ){
            //未补休 = 上年结转加班天数 + 加班天数 - 已补休
            double noCompensateLeave = sVo.getCompensateRemain() +  sVo.getOverTime() - sVo.getCompensateLeave();
            sVo.setNoCompensateLeave( noCompensateLeave );
            
            //请假天数合计 = 年休假 + 事假 + 病假 + 婚假 + 产假 + 其他 + 已补休
            /*double countDays = sVo.getAnnualLevel() + sVo.getEnventLeave() + sVo.getSickLeave()
                            + sVo.getMarryLeave() + sVo.getBirthLeave() + sVo.getOtherLeave()
                            + sVo.getCompensateLeave();*/
            double countDays = countLeaveDays( sVo );
            sVo.setCountDays( countDays );
        }
        
        return list;
    }

    /**
     * 
     * @description:批量插入或更新stat
     * @author: fengzt
     * @createDate: 2014年9月17日
     * @param result
     * @param year
     * @return:int
     */
    @Transactional(propagation=Propagation.REQUIRED)
    private int insertOrUpdateBatch( List<StatVo> result, int year) {
        //查找站点下年份的相应
        List<StatVo> statList =  queryAllStatListBySite( getSiteId(),year  );
        List<StatVo> updateList = new ArrayList<StatVo>();
        List<StatVo> insertList = new ArrayList<StatVo>();
        
        //分离出那些需要update 那些需要插入
        for( StatVo resultVo : result ){
            //用户ID
            String userId = resultVo.getUserId();
            //标记是否已经放到更新列表
            boolean flag = false;
            for( StatVo queryVo : statList ){
                if( queryVo.getUserId().equals( userId ) ){
                    updateList.add( resultVo );
                    flag = true;
                    break;
                }
            }
            
            if( !flag ){
                insertList.add( resultVo );
            }
        }
        
        //批量插入stat
        int insertCount = 0;
        if( insertList != null && insertList.size() > 0 ){
            insertCount = statDao.insertBatchStat( insertList );
        }
        
        //批量更新
        int updateCount = 0;
        if( updateList != null && updateList.size() > 0 ){
            updateCount = statDao.updateBatchStat( updateList );
        }
        
        return insertCount + updateCount;
    }


    /**
     * 
     * @description:通过站点获取所有人的统计信息
     * @author: fengzt
     * @createDate: 2014年9月17日
     * @param siteId
     * @return:
     */
    private List<StatVo> queryAllStatListBySite(String siteId, int year ) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "siteId", siteId );
        params.put( "year", year );
        return statDao.queryAllStat(params);
    }

    /**
     * 
     * @description:通过时间间隔统计报表
     * @author: fengzt
     * @createDate: 2014年9月15日
     * @param pageVo
     * @return:List<StatVo>
     */
    @Override
    public List<StatVo> queryStatByDiffDate(Date startDate, Date endDate) {
    	String siteId=getSiteId();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "startDate", startDate );
        params.put( "endDate", endDate );
        params.put( "siteId", siteId );
        
        Date yearFrist = DateFormatUtil.getYearFrist();
        Date yearLast = DateFormatUtil.getYearLast();
        params.put( "yearFrist", yearFrist );
        params.put( "yearLast", yearLast );
        params.put( "status", ProcessStatusUtil.CLOSED );
        //构造请假类别的sql
        String category = categoryLeaveField( "ATD_LEI_CATEGORY" );
        params.put( "categorySql", category );
        
        List<StatVo> result = new ArrayList<StatVo>();
        Map<String, StatVo> resultMap = new  HashMap<String, StatVo>();
        //已归档请假单
        List<StatVo> leaveList = statDao.queryLeaveByDate( params );
        //已归档加班申请单
        List<StatVo> overtimeList = statDao.queryOvertimeByDate( params );
        //站点下所有用户信息
        Page<SecureUser> pageVo = new Page<SecureUser>( 1, 99999 );
        SecureUser secureUser = new SecureUser();
        secureUser.setCurrentSite( siteId );
        secureUser.setId( privUtil.getUserInfoScope().getUserId() );
        //secureUser.setId( "890167" );
        Page<SecureUser> userList = iSecurityMaintenanceManager.retrieveUniqueUsers( pageVo, secureUser );
        List<SecureUser> personList = userList.getResults();
        //年份
        int year = DateFormatUtil.fromDateToCalendar( endDate ).get( Calendar.YEAR );
        
        //用户信息加入resultMap
        resultMap = setUserList( personList, resultMap );
        //请假申请已归档加入resultMap
        resultMap = setLeaveList( leaveList, startDate, endDate , resultMap);
        //加班申请加入resultMap
        resultMap = setOvertimeList( overtimeList,resultMap );
        //加入结转
        resultMap = setCompensateList( resultMap, year, siteId );
        
        //取站点的definition
        DefinitionBean definitionBean = definitionService.queryDefinitionBySiteId(siteId);
        String oprPersons=null;
        try {
			oprPersons=dutyService.queryOprPersonsBySite(siteId);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
        
        Set<String> keySet = resultMap.keySet();
        for( String userId : keySet ){
            StatVo sVo = resultMap.get( userId ) ;
            //加班
            double overTime = sVo.getOverTime()/(checkIsOpr(userId,oprPersons)?definitionBean.getOprWorkHours():definitionBean.getWorkHours());
            sVo.setOverTime( overTime );
            
            //事假 + 病假 >= 15 可享受年假 = 0 
            double sickEventDays = sVo.getEnventLeave() + sVo.getSickLeave();
            if( sickEventDays >= 15 ){
                sVo.setAnnual( 0 );
            }
            
            //未补休 = 上年结转加班天数 +  加班天数 - 已补休
            double noCompensateLeave = sVo.getCompensateRemain() + sVo.getOverTime() - sVo.getCompensateLeave();
            sVo.setNoCompensateLeave( noCompensateLeave );
            
            //请假天数合计 = 年休假 + 事假 + 病假 + 婚假 + 产假 + 其他 + 已补休
            /*double countDays = sVo.getAnnualLevel() + sVo.getEnventLeave() + sVo.getSickLeave()
                            + sVo.getMarryLeave() + sVo.getBirthLeave() + sVo.getOtherLeave()
                            + sVo.getCompensateLeave();*/
            double countDays = countLeaveDays( sVo );
            sVo.setCountDays( countDays );
            
            sVo.setYearLeave( year );
            sVo.setCreateDate( new Date() );
            
            //用户状态
            String userStatus = sVo.getUserStatus();
            if( StringUtils.isBlank( userStatus ) || userStatus.equals( "YES" ) || userStatus.equals( "Y" )){
                sVo.setUserStatus( "在职" );
            }else{
                sVo.setUserStatus( "离职" );
            }
            
            result.add( sVo );
        }
        
        return result;
    }

    private Boolean checkIsOpr(String userId,String oprPersons){
		return oprPersons!=null&&oprPersons.contains(userId+",");
	}
    
    /**
     * 
     * @description:请假累计天数
     * @author: fengzt
     * @param sVo 
     * @createDate: 2015年3月26日
     * @return:
     */
    private double countLeaveDays(StatVo sVo) {
        List<AppEnum> emList = itcMvcService.getEnum( "ATD_LEI_CATEGORY" );
        double countDays = 0;
        if(emList!=null){
        	for( AppEnum appVo : emList ){
                String field = fromCategoryToField( appVo.getCode() );
                String methodName = "get" + field.substring(0,1).toUpperCase() + field.substring(1);
                
                //反射拿出vo的值
                try {
                    Method method = sVo.getClass().getMethod( methodName, new Class[]{} );
                    double leaveDay = (Double)method.invoke( sVo, null );
                    log.info( method.getName() + " ---> " + leaveDay );
                    if( leaveDay > 0 ){
                        log.info( method.getName() + " ---> " + leaveDay );
                    }
                    countDays += leaveDay;
                } catch (Exception e) {
                    log.info( e.getMessage(), e );
                } 
            }
        }
        
        return countDays;
        
    }

    /**
     * 
     * @description:根据枚举构造列名
     * @author: fengzt
     * @createDate: 2015年3月25日
     * @param string
     * @return:
     */
    private String categoryLeaveField(String category) {
        List<AppEnum> emList = itcMvcService.getEnum( category );
        StringBuffer sqlBuffer = new StringBuffer();
        if(emList!=null){
        	for( AppEnum appVo : emList ){
                String code = appVo.getCode();
                //转换成相应的请假stat字段
                String fieldId = fromCategoryToField( code );
               
                sqlBuffer.append( "(case when item.category='"+ code +"' then ITEM.leaveDays else 0.0 end) " + fieldId + ", " );
            }
        }
        
        return sqlBuffer.toString();
    }
    
    /**
     * 
     * @description:转换成相应的请假stat字段
     * @author: fengzt
     * @createDate: 2014年11月12日
     * @param field
     * @return:
     */
    private String fromCategoryToField( String field ) {
        String siteId = StringUtils.lowerCase( getSiteId() );
        String category = "";
        if( field.equals( siteId + "_le_category_1" ) ){
            category = "annualLevel";
        }else if( field.equals( siteId + "_le_category_2" ) ){
            category = "enventLeave";
        }else if( field.equals( siteId + "_le_category_3" )  ){
            category = "sickLeave";
        }else if( field.equals( siteId + "_le_category_4" ) ){
            category = "marryLeave";
        }else if( field.equals( siteId + "_le_category_5" )  ){
            category = "birthLeave";
        }else if(field.equals( siteId + "_le_category_6" )  ){
            category = "compensateLeave";
        }else if( field.equals( siteId + "_le_category_7" )  ){
            category = "otherLeave";
        }else{
            if( StringUtils.isNotBlank( field ) ){
                String[] codeArr = field.split( "_" );
                category = "category_" + codeArr[ codeArr.length - 1 ];
            }
        }
        
        return category;
    }

    /**
     * 
     * @description:设置结转时间
     * @author: fengzt
     * @createDate: 2014年9月24日
     * @param resultMap
     * @param year
     * @param siteId
     * @return:Map<String, StatVo>
     */
    private Map<String, StatVo> setCompensateList(Map<String, StatVo> resultMap, int year, String siteId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "year", year );
        map.put( "siteId", siteId );
        
        List<StatVo> list = statDao.queryAllStat( map );
        
        for( StatVo vo : list ){
            if( resultMap.containsKey( vo.getUserId() )){
                StatVo temp = resultMap.get( vo.getUserId() );
                temp.setAnnualRemain( vo.getAnnualRemain() );
                temp.setCompensateRemain( vo.getCompensateRemain() );
            }
        }
        return resultMap;
    }

    /**
     * 
     * @description:用户信息设置resultMap个人信息
     * @author: fengzt
     * @createDate: 2014年9月16日
     * @param personList
     * @param resultMap
     * @return:Map<String, StatVo>
     */
    private Map<String, StatVo> setUserList(List<SecureUser> personList, Map<String, StatVo> resultMap) {
        //定义规则
        DefinitionBean definitionBean = definitionService.queryDefinitionBySite();
        for(SecureUser user : personList ){
            StatVo vo = new StatVo();
            vo.setUserId( user.getId() );
            vo.setUserName( user.getName() );
            String deptId = user.getCurrOrgCode();
            vo.setDeptId( deptId  );
            vo.setDeptName( user.getCurrOrgName() );
            vo.setSiteId( getSiteId() );
            
            String userStatus = null;
            if(  user.getActive() != null ){
                userStatus = user.getActive().name();
            }
            vo.setUserStatus( userStatus );
            Date dateArrival = user.getDateArrival();
            Date currentDate = new Date();
            if( dateArrival != null ){
                int diffYears = DateFormatUtil.getIntervalOfDate( dateArrival, currentDate )/365;
                
                int annual = setAnnual( diffYears, definitionBean );
                vo.setAnnual( annual );
            }
            resultMap.put( user.getId(), vo );
        }
        
        return resultMap;
    }

    /**
     * 
     * @description:设置年假
     * @author: fengzt
     * @createDate: 2014年9月16日
     * @param diffYears
     * @param definitionBean
     * @return:
     */
    private int setAnnual(int diffYears, DefinitionBean definitionBean) {
        if( definitionBean.getServiceYear() <= diffYears ){
            if( diffYears < 10 ){
                return definitionBean.getFirstLevelDays();
            }else if( diffYears < 20 ){
                return definitionBean.getSecondLevelDays();
            }else if( diffYears >= 20 ){
                return definitionBean.getThirdLevelDays();
            }
        }
        return 0;
    }

    /**
     * 
     * @description:加班申请
     * @author: fengzt
     * @createDate: 2014年9月16日
     * @param overtimeList
     * @param resultMap
     * @return:
     */
    private Map<String, StatVo> setOvertimeList(List<StatVo> overtimeList, Map<String, StatVo> resultMap) {
        for( StatVo vo : overtimeList ){
            if( resultMap.containsKey( vo.getUserId() )){
                StatVo temp = resultMap.get( vo.getUserId() );
                temp.setOverTime( vo.getOverTime() );
            }else{
                vo.setUserStatus( "N" );
                resultMap.put( vo.getUserId(), vo );
            }
        }
        return resultMap;
    }

    /**
     * 
     * @description:合并请假单信息
     * @author: fengzt
     * @createDate: 2014年9月16日
     * @param result
     * @param endDate 
     * @param startDate 
     * @param resultMap 
     * @param leaveList:
     */
    private Map<String, StatVo> setLeaveList(List<StatVo> leaveList, Date startDate, Date endDate, Map<String, StatVo> resultMap) {
        if( leaveList != null && leaveList.size() > 0 ){
            String siteId = getSiteId();
            for( StatVo vo : leaveList ){
                //比较请假申请开始时间、结束时间和查询的时间段相比较
                Date startDay = vo.getStartDate();
                Date endDay = vo.getEndDate();
                if( vo.getUserId().equals( "890107" )){
                    log.info( startDate + "------" + endDay );
                }
                
                if( DateFormatUtil.compareDate( startDay, startDate ) && DateFormatUtil.compareDate( endDate, endDay ) ){
                    // vo 保持一样 3
                    
                }else if( DateFormatUtil.compareDate( startDate, startDay ) && DateFormatUtil.compareDate( endDay, startDate ) 
                        && DateFormatUtil.compareDate( endDate, endDay ) ){
                    vo = setStatVo( vo, startDate, endDay, siteId );
                }else if( DateFormatUtil.compareDate( startDate, startDay ) && DateFormatUtil.compareDate( endDay, endDate ) ){
                    vo = setStatVo( vo, startDate, endDate, siteId );
                }else if( DateFormatUtil.compareDate( startDay, startDate ) && DateFormatUtil.compareDate( endDate, startDay ) 
                        &&  DateFormatUtil.compareDate( endDay, endDate )){
                    vo = setStatVo( vo, startDay, endDate, siteId );
                }
                
                //如果已经存在就相加 不存在直接put
                if( resultMap.containsKey( vo.getUserId() ) ){
                    StatVo tempVo = resultMap.get( vo.getUserId() );
                    resultMap.remove( vo.getUserId() );
                    tempVo = addStatVo( tempVo , vo );
                    resultMap.put( vo.getUserId(), tempVo );
                }else{
                    vo.setUserStatus( "N" );
                    resultMap.put( vo.getUserId(), vo );
                }
            }
        }
        return resultMap;
    }
    
    /**
     * 
     * @description:设置statVO
     * @author: fengzt
     * @createDate: 2014年9月16日
     * @param vo
     * @param leaveDays
     * @return:
     */
    private StatVo setStatVo(StatVo vo, Date startDate, Date endDate, String siteId ) {
        //正常上班天数
        double leaveDays = 0D;
        //是否是电厂运行班组人员
        boolean flag = leaveService.isRoleBanzu( vo.getUserId(), siteId );
        if( flag ){
            leaveDays = leaveService.queryLeaveDaysByBanzu( startDate, endDate, vo.getUserId() );
        }else{
            leaveDays = leaveService.getLeavesDays( startDate, endDate, siteId );
        }
        //产假、婚假 算自然天
        double diffDay = leaveService.getLeavesDaysContainFes( startDate, endDate, siteId );
        
        //判断是否存在人工改过，且人工修改值 <= leaveDays 就不修改VO，就是说跨月的时候，如果人工修改值就放到上个月，有可能出现统计误差
        List<AppEnum> emList = itcMvcService.getEnum( "ATD_LEI_CATEGORY" );
        for( AppEnum appVo : emList ){
            String field = fromCategoryToField( appVo.getCode() );
            String getMethodName = "get" + field.substring(0,1).toUpperCase() + field.substring(1);
            String setMethodName = "set" + field.substring(0,1).toUpperCase() + field.substring(1);
            
            //反射拿出vo的值
            try {
                Method getMethod = vo.getClass().getMethod( getMethodName, null );
                Method setMethod = vo.getClass().getMethod( setMethodName,  new Class[] {double.class} );
                double lDay = (Double)getMethod.invoke( vo, null );
                log.info( getMethod.getName() + " ---> " + lDay );
                if( lDay > 0 && lDay > leaveDays ){
                    if("getBirthLeave,getMarryLeave".indexOf( getMethodName ) >= 0 ){
                        setMethod.invoke( vo, diffDay );
                    }else{
                        setMethod.invoke( vo, leaveDays );
                    }
                }
            } catch (Exception e) {
                log.info( e.getMessage(), e );
            } 
        }
        return vo;
    }

    /**
     * 
     * @description:两个vo相加
     * @author: fengzt
     * @createDate: 2014年9月16日
     * @param vo
     * @param tempVo
     * @return:
     */
    private StatVo addStatVo(StatVo vo, StatVo tempVo) {
        List<AppEnum> emList = itcMvcService.getEnum( "ATD_LEI_CATEGORY" );
        for( AppEnum appVo : emList ){
            String field = fromCategoryToField( appVo.getCode() );
            String getMethodName = "get" + field.substring(0,1).toUpperCase() + field.substring(1);
            String setMethodName = "set" + field.substring(0,1).toUpperCase() + field.substring(1);
            
            //反射拿出vo的值
            try {
                Method getMethod = vo.getClass().getMethod( getMethodName, new Class[]{} );
                Method getMethodTemp = tempVo.getClass().getMethod( getMethodName, new Class[]{} );
                Method setMethod = vo.getClass().getMethod( setMethodName,  new Class[] {double.class} );
                double day = (Double)getMethod.invoke( vo, null );
                double temDay = (Double)getMethodTemp.invoke( tempVo, null );
                log.info( getMethod.getName() + " ---> " + day + " -- " + temDay );
                setMethod.invoke( vo, day + temDay );
            } catch (Exception e) {
                log.info( e.getMessage(), e );
            } 
        }
        
        return vo;
    }

    /**
     * 
     * @description:时间段查询
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @param map
     * @param pageVo
     * @return: List<StatVo>
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public List<StatVo> queryStatByTimeSearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo) {
        String searchDateFrom = (String) map.get( "searchDateFrom" ); 
        String searchDateEnd = (String) map.get( "searchDateEnd" ); 
        
        if( StringUtils.isBlank( searchDateEnd ) || StringUtils.isBlank( searchDateFrom )){
            return null;
        }
        
        searchDateFrom += " 00:00";
        searchDateEnd += " 23:59";
        
        Date startDate = DateFormatUtil.parseDate( searchDateFrom, "yyyy-MM-dd hh:mm" );
        Date endDate = DateFormatUtil.parseDate( searchDateEnd, "yyyy-MM-dd hh:mm" );
        List<StatVo> result = queryStatByDiffDate(startDate, endDate);
        //年份
        int year = DateFormatUtil.fromDateToCalendar( endDate ).get( Calendar.YEAR );
        
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        String siteId = userInfo.getSiteId();
        
        //更新2014年1月到2014年3月的数据
        if( year == 2014 && StringUtils.equals( searchDateFrom, "2014-01-01 00:00" ) 
                && "2014-03-31 23:59".compareTo( searchDateEnd ) < 0 ){
            result = addStatHistory( result, year, siteId );
        }
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "startDate", startDate );
        params.put( "endDate", endDate );
        params.put( "siteId", siteId );
        params.put( "year", year );
        
        List<StatVo> yearStatLst = statDao.queryAllStat( params );
        
        //如果已经存在先删除这个站点下的这个时间段，防止数据过期
        int delCount = statDao.deleteStatTempByMap( params );
        
        for( StatVo vo : result){
            vo.setStartDate( startDate );
            vo.setEndDate( endDate );
            for( StatVo temp : yearStatLst ){
                if( temp.getUserId().equalsIgnoreCase( vo.getUserId() ) ){
                    vo.setNoCompensateLeave( temp.getNoCompensateLeave() );
                    break;
                }
            }
        }
        //批量插入statTemp
        int insertCount = 0;
        if( result != null && result.size() > 0 ){
            insertCount = statDao.insertBatchStatTemp( result );
        }
        log.info( "删掉：" + delCount + " 插入：" + insertCount );
        
        pageVo.setParams( map );
        pageVo.setParameter( "startDate", startDate );
        pageVo.setParameter( "endDate", endDate );
        pageVo.setParameter( "siteId", siteId );
        
        //1-含离职  0-不含离职
        Integer onStatus = (Integer) map.get( "onStatus" );
        if( onStatus == 0 ){
            pageVo.setParameter( "checkStatus", "在职" );
        }
        
        List<StatVo> rowData = new ArrayList<StatVo>();
        String roleFlag = privUtil.getStatPrivLevel();
        //查询临时表
        if( roleFlag.equals( "deptMgr" ) ){
        	pageVo.setParameter( "deptId", userInfo.getOrgId() );
        }else if( roleFlag.equals( "hrMgr" ) ){
            
        }else{
        	pageVo.setParameter( "userId", userInfo.getUserId() );
        }
        rowData = statDao.queryStatTemp( pageVo );
        
        return rowData;
    }

    /**
     * 
     * @description:核减年假
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @param subAnnualDays
     * @param  remark
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> updateStatSubAnnual(double subAnualLeave, String remark) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "siteId", getSiteId() );
        params.put( "subAnualLeave", subAnualLeave );
        params.put( "remark", remark );
        //年份
        int year = DateFormatUtil.fromDateToCalendar( new Date() ).get( Calendar.YEAR );
        params.put( "year", year );
        String roleFlag = privUtil.getStatPrivLevel();
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        
        if( roleFlag.equals( "hrMgr" ) ){
           int count = statDao.updateStatSubAnnual( params );
           if( count != 0 ){
               resultMap.put( "result", "success" );
           }
        }else{
            resultMap.put( "result", "fail" );
            resultMap.put( "reason", "权限不够" );
        }
        return resultMap;
    }

    /**
     * 
     * @description:拿到登录用户的统计信息
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @return:StatVo
     */
    @Override
    public StatVo queryStatByLogin() {
        int year = DateFormatUtil.fromDateToCalendar( new Date() ).get( Calendar.YEAR );
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        String userId = userInfo.getUserId();
        String siteId = userInfo.getSiteId();
        Map<String, Object> params = new HashMap<String, Object>();
        
        params.put( "year", year );
        params.put( "userId", userId );
        params.put( "siteId", siteId );
        List<StatVo> vos = statDao.queryAllStat( params );
        if( vos != null && vos.size() > 0 ){
            return vos.get( 0 );
        }
        
        return null;
    }

    /**
     * 
     * @description:定时任务-考勤统计数据
     * @author: fengzt
     * @createDate: 2014年10月17日:
     */
    @Override
    public void countStatData() {
        Date yearFrist = DateFormatUtil.getYearFrist();
        Date yearLast = DateFormatUtil.getYearLast();
        int year = DateFormatUtil.fromDateToCalendar( yearFrist ).get( Calendar.YEAR );
        String siteId = getSiteId();
        
        //通过时间间隔统计报表
        List<StatVo> result = queryStatByDiffDate( yearFrist, yearLast );
        result = setCompensateForYear( result, year, siteId );
        
        //更新一整年的数据
        int count = insertOrUpdateBatch( result, year );
        //如果有人在年末已经请了下一年的年假，可能触发这个的时候要单独更新结转信息
        int updateCount = statDao.updateCompensateBatch( result );
        log.info( "更新的数据为： " + count + "条 !; 更新结转为：" + updateCount + "条！" );
    }

    /**
     * 
     * @description:定时任务--设置结转时间
     * @author: fengzt
     * @createDate: 2014年9月24日
     * @param resultMap
     * @param year
     * @param siteId
     * @return:Map<String, StatVo>
     */
    private List<StatVo> setCompensateForYear(List<StatVo> result, int year, String siteId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "year", year - 1 );
        map.put( "siteId", siteId );
        
        //拿到上一年的数据
        List<StatVo> lastYearStatlist = statDao.queryAllStat( map );
        //定义规则
        DefinitionBean definitionBean = definitionService.queryDefinitionBySite();
        //年假折算率
        double yearRatio = definitionBean.getYearRatio();
        //补休假结余折算比率
        double compensateRatio = definitionBean.getCompensateRatio();
        
        for( StatVo vo : result ){
            for( StatVo temp : lastYearStatlist ){
                if( vo.getUserId().equals( temp.getUserId() ) ){
                    //上一年剩余年假 = 结转年假 + 可享受年假 - 已休年假 - 核减年假
                    double remainAnnual = temp.getAnnualRemain() + temp.getAnnual()
                            - temp.getAnnualLevel() - temp.getSubAnualLeave();
                    //当年假余数的时候，置0
                    if( remainAnnual < 0 ){
                        remainAnnual = 0;
                    }
                    //当结转大于当年可享受年假，表示有部分年假结转超过了两年（ITC规定不能结转不能超过两年）
                    if( remainAnnual > temp.getAnnual() ){
                        remainAnnual = temp.getAnnual();
                    }
                    
                    remainAnnual = remainAnnual * yearRatio;
                    vo.setAnnualRemain( remainAnnual );
                    double noCompensateLeave = temp.getNoCompensateLeave() * compensateRatio;
                    vo.setCompensateRemain( noCompensateLeave );
                    break;
                }
            }
        }
        
        return result;
    }
    

    /**
     * 
     * @description:通过ID查询stat
     * @author: fengzt
     * @createDate: 2014年11月11日
     * @param id
     * @return:StatVo
     */
    @Override
    public StatVo queryStatById(int id) {
        
        return statDao.queryStatById( id );
        
    }

    /**
     * 
     * @description:更新结转信息
     * @author: fengzt
     * @createDate: 2014年11月11日
     * @param formData
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int updateStatRemain(String formData) {
        StatVo statVo = JsonHelper.fromJsonStringToBean( formData, StatVo.class );
        
        int count = statDao.updateStatRemain( statVo );
        
        //更新统计信息
        //updateCurrentYearStat();
        
        return count;
    }

    /**
     * 
     * @description:查询请假（加班）明细
     * @author: fengzt
     * @createDate: 2014年11月12日
     * @param userId
     * @param searchDateFrom
     * @param searchDateEnd
     * @param field
     * @return:List<StatDetailVo>
     */
    @Override
    public List<StatDetailVo> queryStatLeaveDetail(String userId, String searchDateFrom, String searchDateEnd,
            String field) {
        List<StatDetailVo> result = new ArrayList<StatDetailVo>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "userId", userId );
        //已归档
        params.put( "status", ProcessStatusUtil.CLOSED );
        
        if( !StringUtils.isBlank( searchDateFrom) && !StringUtils.isBlank( searchDateEnd ) ){
            searchDateFrom += " 00:00";
            searchDateEnd += " 23:59";
            
            Date startDate = DateFormatUtil.parseDate( searchDateFrom, "yyyy-MM-dd hh:mm" );
            Date endDate = DateFormatUtil.parseDate( searchDateEnd, "yyyy-MM-dd hh:mm" );
            params.put( "startDate", startDate );
            params.put( "endDate", endDate );
        }else{
            Date yearFrist = DateFormatUtil.getYearFrist();
            Date yearLast = DateFormatUtil.getYearLast();
            
            params.put( "startDate", yearFrist );
            params.put( "endDate", yearLast );
        }
        
        //加班类型
        if( "overTime".equals( field ) ){
        	String siteId=getSiteId();
        	
        	//取站点的definition，算工作时长
            DefinitionBean definitionBean = definitionService.queryDefinitionBySiteId(siteId);
            String oprPersons=null;
            try {
    			oprPersons=dutyService.queryOprPersonsBySite(siteId);
    		} catch (Exception e) {
    			log.error(e.getMessage());
    		}
            Boolean isOpr=checkIsOpr(userId,oprPersons);
            double workHours=isOpr?definitionBean.getOprWorkHours():definitionBean.getWorkHours();
            
            result = statDao. queryStatOvertimeDetail( params );
            if( result.size() > 0 ){
                for( StatDetailVo vo : result ){
                    //加班是小时计数 8h = 1day
                    DecimalFormat df = new DecimalFormat("#.000");
                    double countDays = vo.getCountDays()/workHours;
                    
                    vo.setCountDays( Double.parseDouble( df.format( countDays ) ) );
                }
            }
        }else{
            //请假明细
            String category = fromFieldToCategory( field );
            params.put( "category", category );
            result = statDao. queryStatLeaveDetail( params );
        }
        return result;
    }

    /**
     * 
     * @description:转换成相应的请假类别
     * @author: fengzt
     * @createDate: 2014年11月12日
     * @param field
     * @return:
     */
    private String fromFieldToCategory(String field) {
        String siteId = StringUtils.lowerCase( getSiteId() );
        String category = "";
        if( "annualLevel".equals( field ) ){
            category = siteId + "_le_category_1";
        }else if( "enventLeave".equals( field ) ){
            category = siteId + "_le_category_2";
        }else if( "sickLeave".equals( field ) ){
            category = siteId + "_le_category_3";
        }else if( "marryLeave".equals( field ) ){
            category = siteId + "_le_category_4";
        }else if( "birthLeave".equals( field ) ){
            category = siteId + "_le_category_5";
        }else if( "compensateLeave".equals( field ) ){
            category = siteId + "_le_category_6";
        }else if( "otherLeave".equals( field ) ){
            category = siteId + "_le_category_7";
        }else{
            if( StringUtils.isNotBlank( field ) ){
                String[] arr = field.split( "_" );
                category = siteId + "_le_category_" + arr[ arr.length - 1 ];
            }
        }
        
        return category;
    }
    

    /**
     * 
     * @description:查询个人考勤统计信息
     * @author: fengzt
     * @createDate: 2015年2月28日
     * @return:StatVo
     */
    @Override
    public StatVo queryPersonStat() {
        String userId = privUtil.getUserInfoScope().getUserId();
        
        Date yearFrist = DateFormatUtil.getYearFrist();
        Date yearLast = DateFormatUtil.getYearLast();
        
        //通过时间间隔统计报表
        List<StatVo> result = queryStatByDiffDateForCache( yearFrist, yearLast );
        
        for( StatVo vo : result ){
            if( StringUtils.equalsIgnoreCase( vo.getUserId(), userId ) ){
              //可享受年假= 今年年假 + 上年结转年假
                double annualDays =  vo.getAnnual() + vo.getAnnualRemain();
                //已休年假 = 请年假 + 核减年假
                double hasOvertimeDays =  vo.getSubAnualLeave() + vo.getAnnualLevel();
                //剩余年假
                double remainAnnualDays = annualDays - hasOvertimeDays;
                
                vo.setAnnual( remainAnnualDays );
                return vo;
            }
        }
        return null;
    }
    
    /***
     * 
     * @description:通过时间间隔统计报表--用来缓存
     * @author: fengzt
     * @createDate: 2015年2月28日
     * @param startDate
     * @param endDate
     * @return:List<StatVo> 
     */
    @Override
    public List<StatVo> queryStatByDiffDateForCache(Date startDate, Date endDate){
        return queryStatByDiffDate( startDate, endDate );
    }
    
    public static void main(String[] args) {
        String show = "";
        for( int i = 0; i < 24; i ++ ){
            show += "_" + i;
        }
        show += "_";
        System.out.println( show );
    }

    /**
     * 
     * @description:批量更新用户状态
     * @author: fengzt
     * @createDate: 2015年5月5日
     * @param uList
     * @return:int --更新条目
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int updateBatchStatStatus(List<StatVo> uList) {
        return statDao.updateBatchStatStatus( uList );
    }

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean rebuildStatItem(Boolean isUpdateStat,String siteId,DefinitionBean definitionBean,String[]userIds,Integer startYear,Integer endYear) throws Exception {
		if(definitionBean==null){
			definitionBean=definitionService.queryDefinitionBySiteId(siteId);
		}
		UserInfoScope user=privUtil.getUserInfoScope();
		log.info("rebuildStatItem->siteId:"+siteId+" startYear:"+startYear+" endYear:"+endYear+" operator:"+user.getUserId()+user.getUserName());
		//清空子表信息
		Integer delNum=statDao.deleteStatItemByYear(siteId,userIds, startYear, endYear);
		log.debug("statDao.deleteStatItemByYear->siteId:"+siteId+(userIds!=null?" userIds:"+userIds.toString():"")+" startYear:"+startYear+" endYear:"+endYear+" num:"+delNum);
		
		//查询主表信息
		Map<String, StatBean>existStat=queryStatMap(siteId,userIds, startYear, endYear);
		//要新建的statItem，以year-month_userid为key
		Map<String, StatItemBean>insertStatItem=new HashMap<String, StatItemBean>();
		//查询所有的请假和加班
		String startTimeStr=startYear+"-01-01 00:00";
		String endTimeStr=endYear+"-12-31 23:59";
        List<LeaveContainItemVo> leaveList = leaveService.queryLeaveByDiffDay( siteId,userIds, startTimeStr, endTimeStr,null );
        List<LeaveContainItemVo> overtimeList = overtimeService.queryOvertimeByDiffDay( siteId,userIds, startTimeStr, endTimeStr );
        //运行人员初始化
        String oprPersons=dutyService.queryOprPersonsBySite(siteId);
        
		for (LeaveContainItemVo item : leaveList) {
			convertToStatItem(definitionBean,item,existStat,insertStatItem,oprPersons);
		}
		for (LeaveContainItemVo item : overtimeList) {
			convertToStatItem(definitionBean,item,existStat,insertStatItem,oprPersons);
		}
		
		//批量插入
		if(!insertStatItem.isEmpty()){
			List<StatItemBean> addList=new ArrayList<StatItemBean>();
			addList.addAll(insertStatItem.values());
			dbUtil.limitedBatchOperateList(addList, this.getClass(), "batchInsertStatItem",this);
			if(isUpdateStat){//如果要更新主表，则给主表增加天数
				dbUtil.limitedBatchOperateList(addList, this.getClass(), "batchUpdateStatPlusDaysByStatItem",this);
			}
		}
		
		return true;
	}

	/**
	 * LeaveContainItemVo转化成StatItemBean
	 * 如果是加班，把核定加班时长折算为天数加入申请单开始时间所在月份
	 * 否则请假单，按月拆分请假时间段，把请假天数加入该月份
	 * @param item
	 * @param statMap
	 * @param statItemMap
	 * @return
	 * @throws Exception
	 */
	private void convertToStatItem(DefinitionBean definitionBean,LeaveContainItemVo item,
			Map<String, StatBean>statMap,Map<String, StatItemBean>statItemMap,String oprPersons) throws Exception {
		
		//按年月拆分
		Calendar tmpCalendar=DateFormatUtil.fromDateToCalendar(item.getStartDate());
		Calendar endCalendar=DateFormatUtil.fromDateToCalendar(item.getEndDate());
		String tmpStr=DateFormatUtil.dateToString(tmpCalendar.getTime(), "yyyy-MM");
		String endStr=DateFormatUtil.dateToString(endCalendar.getTime(), "yyyy-MM");
		
		String userId=item.getCreateBy();
		String category=item.getCategory();
		
		//debug here
		/*if("890117".equals(userId)){
			System.out.println("aa");
		}*/
		//运行人员数据初始化
		Boolean isOpr=checkIsOpr(userId,oprPersons);
		Boolean isContinue=true;//是否继续循环
		while(isContinue&&tmpStr.compareTo(endStr)<=0){//当开始年月小于结束年月，统计入该月份
			String statItemFlag=tmpStr+"_"+userId;
			StatItemBean statItem=statItemMap.get(statItemFlag);
			if(statItem==null){//新建
				Integer year=Integer.parseInt(tmpStr.substring(0,4));
				Integer month=Integer.parseInt(tmpStr.substring(5,7));
				StatBean stat=statMap.get(year+"_"+userId);
				if(stat==null){
					//不存在主表数据时退出，由定时任务去创建主表后自动更新这个item
					return;
				}
				statItem=new StatItemBean();
				statItem.setStatId(stat.getId());
				statItem.setYearLeave(year);
				statItem.setMonthLeave(month);
				statItem.setStat(stat);
			}
			
			Double days=0.0;//该月份的item的请假天数
			if("overtime".equals(category)){
				days=item.getLeaveDays()/(isOpr?definitionBean.getOprWorkHours():definitionBean.getWorkHours());
				setStatItemDays(statItem,getMethodNameFromLeaveCat("transferCompensate"),days);//加班直接把所有转补休时长当成开始时间所在月份的转补休时长
				Double originalDays=item.getOriginalLeaveDays()/(isOpr?definitionBean.getOprWorkHours():definitionBean.getWorkHours());
				setStatItemDays(statItem,getMethodNameFromLeaveCat(category),originalDays);//加班的直接把所有核定加班时长当成开始时间所在月份的加班时长
				isContinue=false;
			}else{
				//计算每月请假天数
				if(tmpStr.compareTo(endStr)==0){//同一个月份
					days=countStatItemDays(definitionBean, userId, isOpr, category, tmpCalendar.getTime(), endCalendar.getTime());
				}else{
					//不同月份则到下月第一天
					Calendar nextMonthFirstDay=(Calendar)tmpCalendar.clone();
					nextMonthFirstDay.add(Calendar.MONTH, 1);
					nextMonthFirstDay.set( nextMonthFirstDay.get(Calendar.YEAR), nextMonthFirstDay.get(Calendar.MONTH), 1, 0, 0, 0);
					days=countStatItemDays(definitionBean, userId, isOpr, category, tmpCalendar.getTime(), nextMonthFirstDay.getTime());
				}

				//设置为下月1号
				tmpCalendar.add(Calendar.MONTH, 1);
				tmpCalendar.set( tmpCalendar.get(Calendar.YEAR), tmpCalendar.get(Calendar.MONTH), 1, 0, 0, 0);
				tmpStr=DateFormatUtil.dateToString(tmpCalendar.getTime(), "yyyy-MM");//下月1号的字符串，进入下个循环
				setStatItemDays(statItem,getMethodNameFromLeaveCat(category),days);
				statItem.setCountDays((statItem.getCountDays()==null?0.0:statItem.getCountDays())+days);//给合计赋值
			}
			
			if(days!=0){
				statItemMap.put(statItemFlag,statItem);
			}
		}
	}
	/**
	 * 根据类别计算StatItem的天数
	 * @return
	 * @throws Exception 
	 */
	private Double countStatItemDays(DefinitionBean definitionBean,String userId,Boolean isOpr,
			String category,Date startDate,Date endDate) throws Exception{
		Double days=0.0;
		if("overtime".equals(category)){
			days=overtimeService.countOvertimeHours(definitionBean,userId,isOpr,startDate,endDate)/(isOpr?definitionBean.getOprWorkHours():definitionBean.getWorkHours());
		}else{
			days=leaveService.countLeaveDays(category,definitionBean,userId,isOpr,startDate,endDate);
		}
		
		return days;
	}
	/**
	 * 设置statItem指定字段的天数（原来值加新值）
	 * @param field
	 * @param val
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	private void setStatItemDays(StatItemBean item,String field,Double val) throws Exception{
		if(field==null){
			log.error("setStatItemDays->itemid:"+item.getId()+" field:"+field+" val:"+val);
		}else if(val==0){
			log.debug("setStatItemDays->itemid:"+item.getId()+" field:"+field+" val:"+val);
		}else{
			Method getMethod = StatItemBean.class.getMethod( "get"+field, new Class[]{} );
			Method setMethod = StatItemBean.class.getMethod( "set"+field, new Class[]{Double.class} );
			Double tmp=(Double)(getMethod.invoke( item, new Object[]{} )==null?0.0:getMethod.invoke( item, new Object[]{} ));
			setMethod.invoke( item, tmp+val );
		}
	}
	/**
	 * 从类别获取请假类型的方法名
	 * @param category
	 * @return
	 */
	private String getMethodNameFromLeaveCat(String category){
		String result=null;
		if("overtime".equals(category)){//加班时长
			return result="OverTime";
		}else if("transferCompensate".equals(category)){//转补休时长
			return result="TransferCompensate";
		}else{//请假的
			String[]cat=category.split("_");
			if(cat.length!=4){
				log.error("getMethodNameFromItemCat->category:"+category);
			}else{
				Integer num=Integer.parseInt(cat[3]);
				if(num<8){
					switch (num) {
					case 1:
						result="AnnualLevel";
						break;
					case 2:
						result="EnventLeave";
						break;
					case 3:
						result="SickLeave";
						break;
					case 4:
						result="MarryLeave";
						break;
					case 5:
						result="BirthLeave";
						break;
					case 6:
						result="CompensateLeave";
						break;
					case 7:
						result="OtherLeave";
						break;
					default:
						break;
					}
				}else{
					result="Category_"+num;
				}
			}
		}
		return result;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Integer createStatBySiteUser(String siteId, 
			Integer startYear,Integer endYear) throws Exception {
		//查询站点的在职人员
		Page<SecureUser> pageVo = new Page<SecureUser>( 1, 99999 );
        SecureUser secureUser = new SecureUser();
        secureUser.setCurrentSite( siteId );
        secureUser.setId("blankUser");
        pageVo.setParameter( "userStatus", "Y" );
        List<SecureUser> userList = iSecurityMaintenanceManager.retrieveUniqueUsers( pageVo, secureUser ).getResults();
        
        List<Integer>yearList=new ArrayList<Integer>();
        for(int i=startYear;i<=endYear;i++){
        	yearList.add(i);
        }
        
        Integer addNum=statDao.createStatByUserAndYear(siteId,userList,yearList);
        log.debug("statDao.createStatByUserAndYear->siteId:"+siteId+" userListSize:"+userList.size()+" yearListSize:"+yearList.size()+" addNum:"+addNum);
		return addNum;
	}
    
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
    public Integer batchInsertStatItem(List<StatItemBean> list){
    	int num=0;//插入条数
    	try {
    		//尝试批量插入，如果出错则单条插入
			num=statDao.batchInsertStatItem(list);
		} catch (Exception e) {
			log.error("批量插入休假统计月份数据失败---->",e);
			for (StatItemBean bean : list) {
				try {
					num+=statDao.insertStatItem(bean);
				} catch (Exception e2) {
					log.error("单条插入休假统计月份数据失败---->"+bean.getYearLeave()+"-"+bean.getMonthLeave()+" statId:"+bean.getStatId(),e2);
				}
			}
		}
        return num<0?0:num;
    }
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
    public Integer batchUpdateStatItemPlusDays(List<StatItemBean> list){
    	int num=0;//更新条数
    	try {
    		//尝试批量更新，如果出错则单条更新
			num=statDao.batchUpdateStatItemPlusDays(list);
		} catch (Exception e) {
			log.error("批量更新休假统计月份数据失败---->",e);
			for (StatItemBean bean : list) {
				try {
					num+=statDao.updateStatItemPlusDays(bean);
				} catch (Exception e2) {
					log.error("单条更新休假统计月份数据失败---->"+bean.getYearLeave()+"-"+bean.getMonthLeave()+" statId:"+bean.getStatId(),e2);
				}
			}
		}
        return num<0?0:num;
    }
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
    public Integer batchInsertStat(List<StatBean> list){
    	int num=0;//插入条数
    	try {
    		//尝试批量插入，如果出错则单条插入
			num=statDao.batchInsertStat(list);
		} catch (Exception e) {
			log.error("批量插入休假统计年份数据失败---->",e);
			for (StatBean bean : list) {
				try {
					num+=statDao.insertStat(bean);
				} catch (Exception e2) {
					log.error("单条插入休假统计年份数据失败---->"+bean.getYearLeave()+"-"+bean.getUserId(),e2);
				}
			}
		}
        return num<0?0:num;
    }
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
    public Integer batchUpdateStatAnnualAndUser(List<StatBean> list){
    	int num=0;//更新条数
    	try {
    		//尝试批量更新，如果出错则单条更新
			num=statDao.batchUpdateStatAnnualAndUser(list);
		} catch (Exception e) {
			log.error("批量更新休假统计年份数据的可休年假和用户信息失败---->",e);
			for (StatBean bean : list) {
				try {
					num+=statDao.updateStatAnnualAndUser(bean);
				} catch (Exception e2) {
					log.error("单条更新休假统计年份数据的可休年假和用户信息失败---->"+bean.getYearLeave()+"-"+bean.getUserId(),e2);
				}
			}
		}
        return num<0?0:num;
    }
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
    public Integer batchUpdateStatPlusDaysByStatItem(List<StatItemBean> list){
    	int num=0;//更新条数
    	try {
    		//尝试批量更新，如果出错则单条更新
			num=statDao.batchUpdateStatPlusDaysByStatItem(list);
		} catch (Exception e) {
			log.error("批量更新休假统计年份数据失败---->",e);
			for (StatItemBean bean : list) {
				try {
					num+=statDao.updateStatPlusDaysByStatItem(bean);
				} catch (Exception e2) {
					log.error("单条更新休假统计年份数据失败---->"+bean.getYearLeave()+"-"+(bean.getStat()==null?"":bean.getStat().getUserId()),e2);
				}
			}
		}
        return num<0?0:num;
    }
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
    public Boolean checkPersonStat(Boolean isRebuild,Boolean isInherited,String siteId,Integer checkYear,DefinitionBean definitionBean,List<SecureUser> userList) throws Exception{
		List<StatBean>insertStat=new ArrayList<StatBean>();
		List<StatBean>updateStat=new ArrayList<StatBean>();
		List<String>resignUser=new ArrayList<String>();//要更新状态的离职的用户
		String resignStr="离职";
		String workingStr="在职";
		
		Calendar currCalendar=Calendar.getInstance();
		Date currentDate = currCalendar.getTime();
		Integer currYear=checkYear==null?currCalendar.get(Calendar.YEAR):checkYear;//今年
		Integer prevYear=currYear-1;//去年
		
		Map<String, StatBean>historyStat=null;//历史的stat，用于继承的方式新建stat
		if(isRebuild){
			String delUserStatus=null;
			if(isInherited){
				historyStat=queryStatMap(siteId,null, currYear, currYear);//先保留数据再删除
				delUserStatus=workingStr;//保留离职的数据
			}
			Integer clearStatNum=statDao.deleteStatByYear(siteId, currYear, currYear,delUserStatus);
			log.info("checkPersonStat deleteStatByYear->siteId:"+siteId+" year:"+currYear+" clearStatNum:"+clearStatNum+" isInherited:"+isInherited);
		}
		
		if(userList==null){//获取所有人员（包括离职）
			Page<SecureUser> pageVo = new Page<SecureUser>( 1, 99999 );
	        SecureUser secureUser = new SecureUser();
	        secureUser.setCurrentSite( siteId );
	        secureUser.setId("blankUser");
	        //pageVo.setParameter( "userStatus", "Y" );
	        userList = iSecurityMaintenanceManager.retrieveUniqueUsers( pageVo, secureUser ).getResults();
	        
	        //因为检索出来的用户不包括没有部门的人，这些人已存在的休假统计应处理成离职状态
	        Map<String, StatBean>existStat=queryStatMap(siteId,null, currYear, currYear);//已存在的stat
	        if(existStat.size()>0){
	        	Map<String, SecureUser>userMap=new HashMap<String, SecureUser>();
	        	for (SecureUser user : userList) {
	        		userMap.put(user.getId(), user);
				}
	        	for (StatBean stat : existStat.values()) {
					if(!resignStr.equals(stat.getUserStatus())&&userMap.get(stat.getUserId())==null){
						//非离职且不存在用户的stat，处理成离职
						resignUser.add(stat.getUserId());
					}
				}
	        }
	        log.info("checkPersonStat->allSiteUser siteId:"+siteId+" checkYear:"+checkYear+" resignStat:"+resignUser.size());
		}
		
		log.info("checkPersonStat->userSize:"+userList.size()+" siteId:"+siteId+" checkYear:"+checkYear+" isRebuild:"+isRebuild+" isInherited:"+isInherited);
		
		List<String>userIds=new ArrayList<String>();
		for (SecureUser user : userList) {
			userIds.add(user.getId());
		}
		Map<String, StatBean>existStat=queryStatMap(siteId,userIds.toArray(new String[userIds.size()]), prevYear, currYear);
		if(definitionBean==null){
			definitionBean=definitionService.queryDefinitionBySiteId(siteId);
		}
		
		for (SecureUser user : userList) {
			String currFlag=currYear+"_"+user.getId();
			String prevFlag=prevYear+"_"+user.getId();
			
			//debug here
			String debugStr="2016_126104";
			if(debugStr.equals(currFlag)){
				log.debug("debug here:"+debugStr);
			}
			
			if(isOnJob(user)){//在职
				StatBean currStat=existStat.get(currFlag);
				if(currStat==null){//不存在则新建
					if(isInherited&&historyStat.get(currFlag)!=null){//如果是继承数据且有数据
						insertStat.add(createInheritedStat(historyStat.get(currFlag)));
					}else{
						insertStat.add(createCurrYearStatFromPrevYearStat(currYear, user, definitionBean, existStat.get(prevFlag)));
					}
				}else{//存在则检查
					//检查可休年假
					Date dateArrival = user.getDateArrival();
					Double originalVal=currStat.getAnnual();//原来的可休年假的值
					//重算的可休年假的值
					currStat.setAnnual(dateArrival==null?0:setAnnual(DateFormatUtil.getIntervalOfDate( dateArrival, currentDate )/365, definitionBean));
					//检查部门
					String deptId=currStat.getDeptId();
					String deptName=currStat.getDeptName();
					currStat.setDeptId(user.getCurrOrgCode());
					currStat.setDeptName(user.getCurrOrgName());
					//检查用户状态
					String userStatus=currStat.getUserStatus();
					currStat.setUserStatus(workingStr);
					
					//两值不等则更新
					if(originalVal!=currStat.getAnnual()||//可休年假有变
							(deptId==null?(currStat.getDeptId()!=null):!deptId.equals(currStat.getDeptId()))||
							(deptName==null?(currStat.getDeptName()!=null):!deptName.equals(currStat.getDeptName()))||//部门有变
							!userStatus.equals(currStat.getUserStatus())//离职变成在职的
							){
						updateStat.add(currStat);
					}
				}
			}else{//离职
				Integer resignYear=Integer.parseInt(DateFormatUtil.dateToString(new Date(user.getResignDateAsLong()),"yyyy"));//离职年份
				if(resignYear==currYear){//今年离职
					String resignFlag=resignYear+"_"+user.getId();
					StatBean resignStat=existStat.get(resignFlag);//离职年份的stat
					if(resignStat==null){//不存在则新建
						if(isInherited&&historyStat.get(resignFlag)!=null){//如果是继承数据且有数据
							insertStat.add(createInheritedStat(historyStat.get(resignFlag)));
						}else{
							insertStat.add(createCurrYearStatFromPrevYearStat(currYear, user, definitionBean, existStat.get(prevFlag)));
						}
					}else{//存在则检查是否已更新用户状态
						if(!resignStat.getUserStatus().equals(getUserStatus(user))){//状态没更新则更新
							resignUser.add(user.getId());
						}
					}
				}else{//以前离职
					//看去年的stat的用户状态是否已更新，再远的不管了
					StatBean prevStat=existStat.get(prevFlag);
					if(prevStat!=null&&!prevStat.getUserStatus().equals(getUserStatus(user))){//状态没更新则更新
						resignUser.add(user.getId());
					}
				}
			}
		}
		
		//批量插入
		dbUtil.limitedBatchOperateList(insertStat, this.getClass(), "batchInsertStat",this);
		//批量更新用户可休年假
		dbUtil.limitedBatchOperateList(updateStat, this.getClass(), "batchUpdateStatAnnualAndUser",this);
		
		//插入item
		if(insertStat.size()>0){
			List<String>insertStatUserIds=new ArrayList<String>();
			for (StatBean stat : insertStat) {
				insertStatUserIds.add(stat.getUserId());
			}
			log.info("checkPersonStat-rebuildStatItem->userSize:"+insertStatUserIds.size()+" siteId:"+siteId);
			rebuildStatItem(true,siteId,definitionBean,insertStatUserIds.toArray(new String[insertStatUserIds.size()]), currYear, currYear);
		}
		
		//更新离职用户状态
		if(resignUser.size()>0){
			Integer updateStatNum=statDao.updateUserStatStatus(resignUser,resignStr);
			log.info("statDao.updateUserStatStatus->"+resignStr+" size:"+updateStatNum);
		}
		
		return true;
	}
	private String getUserStatus(SecureUser user){
		String userStatus=user.getActive()==null?"":user.getActive().name();
		String result="离职";
        if( StringUtils.isBlank( userStatus ) || userStatus.equals( "YES" ) || userStatus.equals( "Y" )){
        	result="在职";
        }
        return result;
	}
	private StatBean createInheritedStat(StatBean srcStat){
		if(srcStat==null)return null;
		StatBean newStat=new StatBean();
		newStat.setUserId(srcStat.getUserId());
		newStat.setUserName(srcStat.getUserName());
		newStat.setYearLeave(srcStat.getYearLeave());
		newStat.setSiteId(srcStat.getSiteId());
		newStat.setDeptId(srcStat.getDeptId());
		newStat.setDeptName(srcStat.getDeptName());
		newStat.setUserStatus(srcStat.getUserStatus());
		newStat.setAnnual(srcStat.getAnnual());
		newStat.setAnnualRemain(srcStat.getAnnualRemain());
		newStat.setCompensateRemain(srcStat.getCompensateRemain());
		newStat.setSubAnualLeave(srcStat.getSubAnualLeave());
		newStat.setRemark1(srcStat.getRemark1());
		newStat.setRemark2(srcStat.getRemark2());
		return newStat;
	}
	/**
	 * 用去年的stat结转创建今年的stat
	 * @param currYear
	 * @param user
	 * @param definitionBean
	 * @param prevStat
	 * @return
	 */
	private StatBean createCurrYearStatFromPrevYearStat(Integer currYear,SecureUser user,DefinitionBean definitionBean,StatBean prevStat){
		StatBean currStat=new StatBean();
		currStat.setUserId(user.getId());
		currStat.setUserName(user.getName());
		currStat.setYearLeave(currYear);
		currStat.setSiteId(definitionBean.getSiteId());
		currStat.setDeptId(user.getCurrOrgCode());
		currStat.setDeptName(user.getCurrOrgName());
        Date dateArrival = user.getDateArrival();
        Date currentDate = new Date();
		currStat.setUserStatus(getUserStatus(user));
		currStat.setAnnual(dateArrival==null?0:setAnnual(DateFormatUtil.getIntervalOfDate( dateArrival, currentDate )/365, definitionBean));
		
		Double remainAnnual=0.0;
		Double remainCompensate=0.0;
		if(prevStat!=null){//计算结转
			remainAnnual=prevStat.getSurplusAnnual()*definitionBean.getYearRatio();
			remainCompensate=prevStat.getSurplusCompensate()*definitionBean.getCompensateRatio();
		}
		currStat.setAnnualRemain(remainAnnual<0?0.0:remainAnnual);
		currStat.setCompensateRemain(remainCompensate<0?0.0:remainCompensate);
		
		return currStat;
	}
	/**
	 * 检查用户是否在职
	 * @param user
	 * @return
	 */
	private Boolean isOnJob(SecureUser user){
		return user.getResignDateAsLong()==null||"".equals(user.getResignDateAsLong());//离职时间为空
	}

	@Override
	public void checkLeaveStat(LeaveBean bean, DefinitionBean definitionBean)
			throws Exception {
		List<LeaveContainItemVo>voList=new ArrayList<LeaveContainItemVo>();
		Integer startYear=9999,endYear=0;//找到最小的开始年份和最大的结束年份，用来查询主表数据
		
		//把leaveItem转成LeaveContainItemVo
		for (LeaveItemBean item : bean.getItemList()) {
			LeaveContainItemVo vo=new LeaveContainItemVo();
			vo.setId(bean.getId());
			vo.setNum(bean.getNum());
			vo.setUserName(bean.getUserName());
			vo.setCreateBy(bean.getCreateBy());
			vo.setLeaveDays(item.getLeaveDays());
			vo.setStartDate(item.getStartDate());
			vo.setEndDate(item.getEndDate());
			vo.setCategory(item.getCategory());
			vo.setSiteId(bean.getSiteId());
			voList.add(vo);
			
			Integer sY=Integer.parseInt(DateFormatUtil.dateToString(vo.getStartDate(), "yyyy"));
			if(startYear>sY){
				startYear=sY;
			}
			Integer eY=Integer.parseInt(DateFormatUtil.dateToString(vo.getEndDate(), "yyyy"));
			if(endYear<eY){
				endYear=eY;
			}
		}
		
		log.info("checkLeaveStat->leaveId:"+bean.getId()+" listSize:"+voList.size()+" startYear:"+startYear+" endYear:"+endYear);
		checkStatByLeaveContainItemVoList(voList,definitionBean,new String[]{bean.getCreateBy()}, startYear, endYear);
	}
	/**
	 * 检查LeaveContainItemVo列表的stat
	 * 把请假项和加班项转成LeaveContainItemVo统一处理
	 * @param voList
	 * @param definitionBean
	 * @param userIds 涉及的userId
	 * @param startYear 涉及的开始年份
	 * @param endYear 涉及的结束年份
	 * @throws Exception
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	private void checkStatByLeaveContainItemVoList(List<LeaveContainItemVo>voList,DefinitionBean definitionBean,String[]userIds,
			Integer startYear,Integer endYear)throws Exception{
		String siteId=definitionBean.getSiteId();
		String oprPersons=dutyService.queryOprPersonsBySite(siteId);
		Map<String, StatBean>existStat=queryStatMap(siteId,userIds, startYear, endYear);
		//要增加的statItem，以year-month_userid为key
		Map<String, StatItemBean>insertStatItem=new HashMap<String, StatItemBean>();
		for (LeaveContainItemVo vo : voList) {
			convertToStatItem(definitionBean, vo, existStat, insertStatItem,oprPersons);
		}
		
		//找出已存在的statItem，进行更新，其他的进行插入操作
		Map<String, StatItemBean>existStatItem=statDao.queryStatItemMap(siteId,userIds, startYear, endYear);
		List<StatItemBean>insertStatItemList=new ArrayList<StatItemBean>();
		List<StatItemBean>updateStatItemList=new ArrayList<StatItemBean>();
		for (String flag : insertStatItem.keySet()) {
			if(existStatItem.get(flag)!=null){
				StatItemBean item=insertStatItem.get(flag);
				item.setId(existStatItem.get(flag).getId());
				updateStatItemList.add(item);
			}else{
				insertStatItemList.add(insertStatItem.get(flag));
			}
		}
		
		if(!insertStatItemList.isEmpty()){
			//插入statItem并更新stat
			dbUtil.limitedBatchOperateList(insertStatItemList, this.getClass(), "batchInsertStatItem",this);
			dbUtil.limitedBatchOperateList(insertStatItemList, this.getClass(), "batchUpdateStatPlusDaysByStatItem",this);
		}
		if(!updateStatItemList.isEmpty()){
			//更新statItem并更新stat
			dbUtil.limitedBatchOperateList(updateStatItemList, this.getClass(), "batchUpdateStatItemPlusDays",this);
			dbUtil.limitedBatchOperateList(updateStatItemList, this.getClass(), "batchUpdateStatPlusDaysByStatItem",this);
		}
		
		log.info("checkStatByLeaveContainItemVoList->LeaveContainItemVoSize:"+voList.size()+
				" userIds:"+Arrays.asList(userIds)+" startYear:"+startYear+" endYear:"+endYear+
				" convertedStatItemSize:"+insertStatItem.size()+
				" insertStatItemSize:"+insertStatItemList.size()+" updateStatItemSize:"+updateStatItemList.size());
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void checkOvertimeStat(OvertimeBean bean,
			DefinitionBean definitionBean) throws Exception {
		List<LeaveContainItemVo>voList=new ArrayList<LeaveContainItemVo>();
		Integer startYear=9999,endYear=0;//找到最小的开始年份和最大的结束年份，用来查询主表数据
		List<String>userIds=new ArrayList<String>();
		
		//把OvertimeItem转成LeaveContainItemVo
		for (OvertimeItemBean item : bean.getItemList()) {
			LeaveContainItemVo vo=new LeaveContainItemVo();
			vo.setId(bean.getId());
			vo.setNum(bean.getNum());
			vo.setUserName(item.getUserName());
			vo.setCreateBy(item.getUserId());
			vo.setLeaveDays(item.getTransferCompensate());
			vo.setOriginalLeaveDays(item.getRealOverHours());
			vo.setStartDate(item.getStartDate());
			vo.setEndDate(item.getEndDate());
			vo.setCategory("overtime");
			vo.setSiteId(bean.getSiteId());
			voList.add(vo);
			
			Integer sY=Integer.parseInt(DateFormatUtil.dateToString(vo.getStartDate(), "yyyy"));
			if(startYear>sY){
				startYear=sY;
			}
			Integer eY=Integer.parseInt(DateFormatUtil.dateToString(vo.getEndDate(), "yyyy"));
			if(endYear<eY){
				endYear=eY;
			}
			
			userIds.add(vo.getCreateBy());
		}
		
		log.info("checkOvertimeStat->overtimeId:"+bean.getId()+" listSize:"+voList.size()+" startYear:"+startYear+" endYear:"+endYear);
		checkStatByLeaveContainItemVoList(voList,definitionBean,userIds.toArray(new String[userIds.size()]), startYear, endYear);
	}

	@Override
	public Page<StatBean> queryStatList(Page<StatBean> page) throws Exception {
		UserInfoScope userInfo=privUtil.getUserInfoScope();
        String siteId = userInfo.getSiteId();
        String deptId=null,userId=null;
        
        String fuzzySearchParams = (String)userInfo.getParam("search");
        if (StringUtils.isNotBlank( fuzzySearchParams )) {
			Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( fuzzySearchParams );
			page.setFuzzyParams(fuzzyParams);
		}
        
		String sort = (String)userInfo.getParam( "sort" );
        String order = (String)userInfo.getParam( "order" );
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
        	page.setSortKey( sort );
        	page.setSortOrder( order );
        }else{
        	page.setSortKey( "yearleave desc, deptname, id" );
        	page.setSortOrder( "asc" );
        }
        
        String roleFlag = privUtil.getStatPrivLevel();
        if( roleFlag.equals( "deptMgr" ) ){
        	deptId = userInfo.getOrgId();
        }else if( roleFlag.equals( "hrMgr" ) ){
            
        }else{
        	userId = userInfo.getUserId();
        }
        
        Integer year=Integer.parseInt(userInfo.getParam("year").toString());
        String month=userInfo.getParam("month").toString();
        month="0".equals(month)?"":month;
        page.setParameter("year", year);
        page.setParameter("month", month);
        page.setParameter("siteId", siteId);
        page.setParameter("deptId", deptId);
        page.setParameter("userId", userId);
        List<StatBean>result= StringUtils.isBlank( month )?
        	statDao.queryStatList(page):
        	statDao.queryStatListInMonth(page);
        	
        page.setResults(result);
        return page;
	}

	@Override
	public List<LeaveContainItemVo> queryStatDetail(String userId, Integer year,
			Integer month, String field) throws Exception {
		String siteId=getSiteId();
        DefinitionBean definitionBean = definitionService.queryDefinitionBySiteId(siteId);
        String oprPersons=null;
        try {
			oprPersons=dutyService.queryOprPersonsBySite(siteId);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
        String[] userIds=new String[]{userId};
        Boolean isOpr=checkIsOpr(userId,oprPersons);
        
        //查询年份详情
        String startTimeStr=year+"-01-01 00:00";
		String endTimeStr=year+"-12-31 23:59";
        if( month!=0 ){//查询月份详情
        	Calendar calendar=Calendar.getInstance();
        	calendar.set(Calendar.YEAR, year);
        	calendar.set(Calendar.MONTH, month);
        	Integer maxDay=calendar.getMaximum(Calendar.DAY_OF_MONTH);
        	String monthStr=month<10?("0"+month):(""+month);
        	startTimeStr=year+"-"+monthStr+"-01 00:00";
    		endTimeStr=year+"-"+monthStr+"-"+maxDay+" 23:59";
        }
        Date startTime = DateFormatUtil.parseDate( startTimeStr, "yyyy-MM-dd HH:mm" );
        Date endTime = DateFormatUtil.parseDate( endTimeStr, "yyyy-MM-dd HH:mm" );
        
      	List<LeaveContainItemVo> result = new ArrayList<LeaveContainItemVo>();
      	String category = ("overTime".equals( field )||"transferCompensate".equals( field ))?"overtime":fromFieldToCategory( field );
        if( "overtime".equals( category ) ){//加班类型
        	result = overtimeService.queryOvertimeByDiffDay( siteId,userIds, startTimeStr, endTimeStr );
        }else{//请假明细
            result = leaveService.queryLeaveByDiffDay(siteId, userIds, startTimeStr, endTimeStr, category);
        }
        
        for (int i=0;i<result.size();i++) {
        	LeaveContainItemVo item=result.get(i);
        	Double days=0.0;
        	if("overTime".equals( field )||"transferCompensate".equals( field )){
        		if(month!=0&&!startTimeStr.substring(0, 7).equals(DateFormatUtil.dateToString(item.getStartDate(), "yyyy-MM"))){
        			result.remove(i--);
        			continue;
        		}else{
        			days=("overTime".equals( field )?item.getOriginalLeaveDays()://加班用原来的核定加班时长
        						item.getLeaveDays())//转补休用原来的核定转补休时长
        						/(isOpr?definitionBean.getOprWorkHours():definitionBean.getWorkHours());
        		}
        	}else{//非加班算出实际请假天数
        		days=countStatItemDays(definitionBean, userId, isOpr, category, 
            			DateFormatUtil.compareDate(item.getStartDate(), startTime)?item.getStartDate():startTime, 
            			DateFormatUtil.compareDate(item.getEndDate(), endTime)?endTime:item.getEndDate());
        	}
        	item.setLeaveDays(days);
		}
        
        return result;
	}

	@Override
	public Map<String, StatBean> queryStatMap(String siteId, String[] userIds,
			Integer startYear, Integer endYear) {
		return statDao.queryStatMap(siteId, userIds, startYear, endYear);
	}
}
