package com.timss.itsm.service.core;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.itsm.service.ItsmWorkTimeService;
import com.timss.itsm.util.DateFormatUtil;
import com.timss.itsm.vo.ItsmWorkTimeVo;
import com.yudean.itc.manager.support.IDateManager;

/**
 * 
 * @title:计算上班时间
 * @description: {desc}
 * @company: gdyd
 * @className: WorkTimeUtil.java
 * @author: fengzt
 * @createDate: 2014年12月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service
public class ItsmWorkTimeServiceImpl implements ItsmWorkTimeService {
    
    @Autowired
    private IDateManager iDateManager;
    
    /**
     * 
     * @description:计算工作日期差
     * @author: fengzt
     * @createDate: 2014年12月3日
     * @param workTimeVo
     * @return double 天数
     *                  --如果需要小时，自己乘上 工作小时
     * @throws Exception:
     */
    @Override
    public double calDay( ItsmWorkTimeVo workTimeVo ) throws Exception {
        
        String siteId = workTimeVo.getSiteId();
        //start 00:00
        Date startTemp = DateFormatUtil.getFormatDate( workTimeVo.getStart(), "yyyy-MM-dd" );
        Date endTemp = DateFormatUtil.getFormatDate( workTimeVo.getEnd(), "yyyy-MM-dd" );
        //下午下班时间
        //String afternoon = "1700";
        String afternoon = workTimeVo.getAfternoon();
        double sumDay = 0D;
        //是否上班
        boolean workFlag = false;
        //是否计算自然天     true--计算
        boolean isMarray = workTimeVo.isFlag();
        
        //开始时间和结束时间
        int diffDays =  (int)DateFormatUtil.dateDiff( "dd", startTemp, endTemp ) ;
        
        if( diffDays == 0 ){
            if( isMarray ){
                workFlag = true;
            }else{
                workFlag = iDateManager.checkIsWorkDate(  DateFormatUtil.addDate( startTemp, "s", 1 ), siteId );
            }
            if( workFlag ){
                sumDay = countLeaveDays( workTimeVo.getStart(), workTimeVo.getEnd(), siteId, workTimeVo );
            }
        }else{
            for( int i = 0; i <= diffDays; i++ ){
                Date temp = DateFormatUtil.addDate( startTemp, "d", i );
                Date afternoonDate = DateFormatUtil.parseDate( DateFormatUtil.dateToString( temp, "yyyy-MM-dd " ) + afternoon, "yyyy-MM-dd HHmm" ); 
                if( isMarray ){
                    workFlag = true;
                }else{
                    workFlag = iDateManager.checkIsWorkDate(  DateFormatUtil.addDate( temp, "s", 1 ), siteId );
                }
                if( i == 0 ){
                    if( workFlag ){
                        sumDay = countLeaveDays( workTimeVo.getStart(), afternoonDate, siteId , workTimeVo);
                    }
                }else if( i == diffDays ){
                    if( workFlag ){
                        sumDay += countLeaveDays( temp, workTimeVo.getEnd(), siteId, workTimeVo );
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
    
    /**
     * 
     * @description:计算一天内的时间差
     * @author: fengzt
     * @createDate: 2014年12月3日
     * @param start 开始时间
     * @param end 结束时间
     * @param siteId
     * @param workTimeVo
     * @return double 天数
     * @throws Exception:
     */
    private double countLeaveDays( Date start, Date end, String siteId, ItsmWorkTimeVo workTimeVo  ) throws Exception{
        String morning = workTimeVo.getMorning();
        String forenoon = workTimeVo.getForenoon();
        String noon = workTimeVo.getNoon();
        String afternoon = workTimeVo.getAfternoon();
        double workTime = workTimeVo.getWorkTime();
        
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
                }else if(  DateFormatUtil.compareDate( forenoonDate, start )  ){
                    // start <= 1200 end > 1200
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
            }else if( DateFormatUtil.compareDateNoEqual( start, morningDate ) &&  DateFormatUtil.compareDateNoEqual( noonDate, start )
                    && DateFormatUtil.compareDateNoEqual( end, afternoonDate ) ){
                //start >0800 start < 1400 end > 1700
                // start <= 1200
                if( DateFormatUtil.compareDate( forenoonDate, start ) ){
                    //上午
                    sumDay = DateFormatUtil.dateDiff( "hh", start, forenoonDate ) / workTime;
                }
                //下午
                sumDay += 0.5;
            }else if( DateFormatUtil.compareDate( start, forenoonDate ) ){
                //start >= 1200 
                //start <= 1400 
                if( DateFormatUtil.compareDate( noonDate, start ) ){
                    //end < 1700
                    if( DateFormatUtil.compareDateNoEqual( afternoonDate, end ) ){
                        sumDay = DateFormatUtil.dateDiff( "hh", noonDate, end ) / workTime;
                    }else if( DateFormatUtil.compareDate( end, afternoonDate )  ){
                        //end >= 1700
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
}
