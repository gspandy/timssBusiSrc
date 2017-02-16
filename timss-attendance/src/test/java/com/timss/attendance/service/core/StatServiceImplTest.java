package com.timss.attendance.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.timss.attendance.dao.StatDao;
import com.timss.attendance.service.StatService;
import com.timss.attendance.util.DateFormatUtil;
import com.timss.attendance.vo.StatVo;
import com.yudean.itc.manager.support.IDateManager;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@TransactionConfiguration( defaultRollback = true )
public class StatServiceImplTest extends TestUnit  {

    @Autowired 
    private StatDao statDao;
    
    @Autowired 
    private StatService statService;
    @Autowired
    private IDateManager iDateManager;
    
    
    @Test
    public void testQueryStatBySiteId() {
        Map<String, Object> params = new HashMap<String, Object>();
        Date startDate = DateFormatUtil.parseDate( "2014-01-01 00:00", "yyyy-MM-dd hh:mm" );
        Date endDate = DateFormatUtil.parseDate( "2014-12-30 23:59", "yyyy-MM-dd hh:mm" );
        params.put( "startDate", startDate );
        params.put( "endDate", endDate );
        params.put( "siteId", "ITC" );
        params.put( "status", "已归档" );
        
        //已归档请假单
        List<StatVo> leaveList = statDao.queryLeaveByDate( params );
        
        System.out.println( leaveList.size() );
        
    }
    
    @Test
    public void testQueryAllStat() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "siteId", "ITC" );
        params.put( "year", "2014" );
        
        //已归档请假单
        List<StatVo> leaveList = statDao.queryAllStat( params );
        
        System.out.println( leaveList.size() );
        
    }
    
    @Test
    public void testCountStatData() {
        TestUnitGolbalService.SetCurentUserById("180811", "ZJW");
        statService.countStatData( );
    }
    @Test
    public void testQueryPersonStat() {
        TestUnitGolbalService.SetCurentUserById("890167", "ITC");
        StatVo vo = statService.queryPersonStat( );
        System.out.println( vo.getUserName() + "----" + vo.getCountDays() );
    }

    @Test
    public void testQueryStatByDiffDate() {
        Map<String, Object> params = new HashMap<String, Object>();
        Date startDate = DateFormatUtil.parseDate( "2014-01-01 00:00", "yyyy-MM-dd hh:mm" );
        Date endDate = DateFormatUtil.parseDate( "2014-12-30 23:59", "yyyy-MM-dd hh:mm" );
        params.put( "startDate", startDate );
        params.put( "endDate", endDate );
        params.put( "siteId", "ITC" );
        
        statService.queryStatByDiffDate( startDate, endDate );
        
    }
    @Test
    public void testCheckIsWorkDate() {
        Date endDate = DateFormatUtil.parseDate( "2015-02-24 00:00:01", "yyyy-MM-dd hh:mm:ss" );
        Date date = new Date( Long.parseLong( "1424707201000" ) );
        boolean workFlag = iDateManager.checkIsWorkDate( endDate ,"ITC" );
        boolean flag = iDateManager.checkIsWorkDate( date ,"ITC" );

        System.out.println( "workFlag = " + workFlag );
    }
    
    
    
    @Test
    public void testGetLeavesDays() throws Exception {
        Date start1 = DateFormatUtil.parseDate( "2014-04-28 07:00", "yyyy-MM-dd HH:mm" );
        Date start12 = DateFormatUtil.parseDate( "2014-04-28 16:00", "yyyy-MM-dd HH:mm" );
        Date start121 = DateFormatUtil.parseDate( "2014-04-28 11:00", "yyyy-MM-dd HH:mm" );
        Date end1 = DateFormatUtil.parseDate( "2014-04-30 17:00", "yyyy-MM-dd HH:mm" );
        Date start2 = DateFormatUtil.parseDate( "2014-05-01 08:00", "yyyy-MM-dd HH:mm" );
        Date end2 = DateFormatUtil.parseDate( "2014-05-07 17:00", "yyyy-MM-dd HH:mm" );
        Date end21 = DateFormatUtil.parseDate( "2014-05-15 17:00", "yyyy-MM-dd HH:mm" );
        
        Date oct1 = DateFormatUtil.parseDate( "2014-10-08 15:00", "yyyy-MM-dd HH:mm" );
        Date oct8 = DateFormatUtil.parseDate( "2014-10-08 18:00", "yyyy-MM-dd HH:mm" );
       // double count = calDay( start1 ,end2 );
        
        Date start3 = DateFormatUtil.parseDate( "2014-05-01 00:00", "yyyy-MM-dd HH:mm" );
        String siteId = "ITC";
        double leaveDays8 = calDay( oct1, oct8, siteId );
        System.out.println( "1001-1008 ===" + leaveDays8 );
      /*  //28 - 30 
        double leaveDays = iDateManager.getLeavesDays( start1, end1, siteId );
        //1 - 7
        double leaveDays1 = iDateManager.getLeavesDays( start2, end2, siteId );
        //0点开始计算
        double leaveDays3 = iDateManager.getLeavesDays( start3, end2, siteId );
        //28 - 7
        double leaveDays2 = iDateManager.getLeavesDays( start1, end2, siteId );*/
        
        //28 - 30 
        double leaveDays = calDay( start1, end1, siteId );
        //428 
        double leaveDays11 = calDay( start1, start12, siteId );
        double leaveDays111 = calDay( start1, start121, siteId );
        double leaveDays1111 = calDay( start121, start12, siteId );
        //1 - 7
        double leaveDays1 = calDay( start2, end2, siteId );
        //0点开始计算
        double leaveDays3 = calDay( start3, end2, siteId );
        //28 - 7
        double leaveDays2 = calDay( start1, end2, siteId );
        double leaveDays21 = calDay( start1, end21, siteId );
        
        System.out.println( "4.28 - 4.30 ----- " + leaveDays );
        System.out.println( "5.1 - 5.7 ----- " + leaveDays1 );
        System.out.println( "5.1 - 5.7 0点开始计算 ----- " + leaveDays3 );
        System.out.println( "4.28 - 5.7 ----- " + leaveDays2 );
        System.out.println( "4.28 - 428 6h ----- " + leaveDays11 );
        System.out.println( "4.28 - 428 3h ----- " + leaveDays111 );
        System.out.println( "4.28 - 428 3h ----- " + leaveDays1111 );
        System.out.println( "4.28 - 515  ----- " + leaveDays21 );
        
        
        
    }
    
    //计算每一天
    private double countLeaveDays( Date start, Date end, String siteId  ) throws Exception{
        String morning = "0800";
        String forenoon = "1200";
        String noon = "1400";
        String afternoon = "1700";
        double workTime = 8;
        
        //start 00:00
        Date startTemp = DateFormatUtil.getFormatDate( start, "yyyy-MM-dd" );
        Date endTemp = DateFormatUtil.getFormatDate( end, "yyyy-MM-dd" );
        double sumDay = 0D;
        
        //开始时间跟结束时间都在同一天
        if( startTemp.equals( endTemp ) ){
            Date morningDate = DateFormatUtil.parseDate( DateFormatUtil.dateToString( startTemp, "yyyy-MM-dd " ) + morning, "yyyy-MM-dd HHmm" ); 
            Date forenoonDate = DateFormatUtil.parseDate( DateFormatUtil.dateToString( startTemp, "yyyy-MM-dd " ) + forenoon, "yyyy-MM-dd HHmm" ); 
            Date noonDate = DateFormatUtil.parseDate( DateFormatUtil.dateToString( startTemp, "yyyy-MM-dd " ) + noon, "yyyy-MM-dd HHmm" ); 
            Date afternoonDate = DateFormatUtil.parseDate( DateFormatUtil.dateToString( startTemp, "yyyy-MM-dd " ) + afternoon, "yyyy-MM-dd HHmm" ); 
            
            if( DateFormatUtil.compareDate( morningDate, start ) &&  DateFormatUtil.compareDate( end, afternoonDate ) ){
                // start <= 0800 end >= 1700
                sumDay = 1D;
            }else if( DateFormatUtil.compareDate( morningDate, start ) &&  DateFormatUtil.compareDate( forenoonDate, end ) ){
                // start <= 0800 end <= 1200
                //两个时间点相差小时
                sumDay = DateFormatUtil.dateDiff( "hh", morningDate, end ) / workTime;
            }else if( DateFormatUtil.compareDate( morningDate, start ) &&  DateFormatUtil.compareDate( noonDate, end ) ){
                // start <= 0800 end <= 1400
                //两个时间点相差小时
                sumDay = DateFormatUtil.dateDiff( "hh", morningDate, forenoonDate ) / workTime;
            }else if( DateFormatUtil.compareDate( morningDate, start ) &&  DateFormatUtil.compareDate( afternoonDate, end ) ){
                // start <= 0800 end <= 1700
                //上午
                sumDay += DateFormatUtil.dateDiff( "hh", morningDate, forenoonDate ) / workTime;
                //下午
                // = 1700时
                if( afternoonDate.equals( end) ){
                    sumDay += 0.5;
                }else{
                    sumDay += DateFormatUtil.dateDiff( "hh", noonDate, end ) / workTime;
                }
            }else if( DateFormatUtil.compareDateNoEqual( start, morningDate ) &&  DateFormatUtil.compareDate( noonDate, end ) ){
                //start >0800 end <= 1400
                // end <= 1200
                if( DateFormatUtil.compareDate( forenoonDate, end ) ){
                    sumDay = DateFormatUtil.dateDiff( "hh", start, end ) / workTime;
                }else{
                    //end > 1200
                    sumDay = DateFormatUtil.dateDiff( "hh", start, forenoonDate ) / workTime;
                }
            }else if( DateFormatUtil.compareDateNoEqual( start, morningDate ) &&  DateFormatUtil.compareDateNoEqual( noonDate, start )
                    && DateFormatUtil.compareDate( afternoonDate, end ) ){
                //start >0800 start < 1400 end <= 1700
                // start <= 1200
                if( DateFormatUtil.compareDate( forenoonDate, start ) ){
                    //上午
                    sumDay = DateFormatUtil.dateDiff( "hh", start, forenoonDate ) / workTime;
                }
                //下午
                // = 1700时
                if( afternoonDate.equals( end) ){
                    sumDay += 0.5;
                }else{
                    sumDay += DateFormatUtil.dateDiff( "hh", noonDate, end ) / workTime;
                }
            }else if( DateFormatUtil.compareDate( start, forenoonDate ) ){
                //start >= 1200 
                //start <= 1400 
                if( DateFormatUtil.compareDate( noonDate, start ) ){
                    //end <= 1700
                    if( DateFormatUtil.compareDate( afternoonDate, end ) ){
                        sumDay = DateFormatUtil.dateDiff( "hh", noonDate, end ) / workTime;
                    }else if( DateFormatUtil.compareDateNoEqual( end, afternoonDate )  ){
                        //end > 1700
                        sumDay = 0.5;
                    }
                }else if( DateFormatUtil.compareDateNoEqual( start, noonDate ) ){
                    //start > 1400 
                    //start <= 1700 end <= 1700
                    if( DateFormatUtil.compareDate( afternoonDate, start ) && DateFormatUtil.compareDate( afternoonDate, end )  ){
                        sumDay = DateFormatUtil.dateDiff( "hh", start, end ) / workTime;
                    }else if ( DateFormatUtil.compareDate( afternoonDate, start ) && DateFormatUtil.compareDate( end, afternoonDate ) ){
                        //start <= 1700 end >= 1700
                        sumDay = DateFormatUtil.dateDiff( "hh", start, afternoonDate ) / workTime;
                    }
                }
            }
        }
        return sumDay;
    }
    
    //计算时间
    private  double calDay(Date start, Date end, String siteId ) throws Exception {
        //start 00:00
        Date startTemp = DateFormatUtil.getFormatDate( start, "yyyy-MM-dd" );
        Date endTemp = DateFormatUtil.getFormatDate( end, "yyyy-MM-dd" );
        String afternoon = "1700";
        double sumDay = 0D;
        //是否上班
        boolean workFlag = false;
        
        //开始时间和结束时间
        int diffDays =  (int)DateFormatUtil.dateDiff( "dd", startTemp, endTemp ) ;
        
        
        if( diffDays == 0 ){
            sumDay = countLeaveDays( start, end, siteId );
        }else{
            for( int i = 0; i <= diffDays; i++ ){
                Date temp = DateFormatUtil.addDate( startTemp, "d", i );
                Date afternoonDate = DateFormatUtil.parseDate( DateFormatUtil.dateToString( temp, "yyyy-MM-dd " ) + afternoon, "yyyy-MM-dd HHmm" ); 
                workFlag = iDateManager.checkIsWorkDate( DateFormatUtil.addDate( temp, "s", 1 ), siteId );
                if( i == 0 ){
                    if( workFlag ){
                        sumDay = countLeaveDays( start, afternoonDate, siteId );
                    }
                }else if( i == diffDays ){
                    if( workFlag ){
                        sumDay += countLeaveDays( temp, end, siteId );
                    }
                }else{
                    if( workFlag ){
                        sumDay += 1D;
                    }
                }
            }
        }
        
        return sumDay;
    }
   
   public static void main(String[] args) {
   
}

}
