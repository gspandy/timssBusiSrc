package com.timss.operation.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.annotations.Case;
import org.apache.log4j.Logger;

/**
 * @ClassName: DateFormatUtil
 * @Description: 日期格式化工具类,包括将时间进行格式化并转为字符串形式
 * @author: fengzhutai
 * @date: 2014-6-13
 */
public class DateFormatUtil {
    
    private static Logger log = Logger.getLogger( DateFormatUtil.class );

    /**
     * 按指定格式格式化日期
     * 
     * @param date 待格式化的日期
     * @param formatString 　 日期格式 　取值为： 1、 yyyy-MM-dd hhmm(例：2013-07-17 1400)
     *            2、yyyy-MM-dd hh:mm(例：2013-07-17 14:00) 3、 yyyy/MM/dd hhmm 4、dd
     *            hhmm(例：17 1400) 5、 hhmm(例：1400) 6、 yyyy-MM-dd 7、 其它
     *            格式　　　　　　　　　　　　　　　结果 "yyyy.MM.dd G 'at' HH:mm:ss z" 2001.07.04
     *            AD at 12:08:56 PDT "EEE, MMM d, ''yy" Wed, Jul 4, '01 "h:mm a"
     *            12:08 PM "hh 'o''clock' a, zzzz" 12 o'clock PM, Pacific
     *            Daylight Time "K:mm a, z" 0:08 PM, PDT
     *            "yyyyy.MMMMM.dd GGG hh:mm aaa" 02001.July.04 AD 12:08 PM
     *            "EEE, d MMM yyyy HH:mm:ss Z" Wed, 4 Jul 2001 12:08:56 -0700
     *            "yyMMddHHmmssZ" 010704120856-0700 "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
     *            2001-07-04T12:08:56.235-0700
     * @return String
     */
    public static String formatDate(Date date, String formatString) {
        String result = null;
        try {
            if ( date != null ) {
                DateFormat df = new SimpleDateFormat( formatString );
                result = df.format( date );
            }
        } catch (Exception e) {
            result = "";
            log.error( e.getMessage() );
        }// END TRY-CATCH

        return result;
    }

    /**
     * @description: 获取指定的日期格式
     * @author: fengzhutai
     * @createDate: 2013-8-27
     * @param date 日期的字符串
     * @param formatString 日期的格式
     * @return:
     */
    public static Date getFormatDate(Date date, String formatString) {
        Date result = null;
        if ( date != null ) {
            DateFormat df = new SimpleDateFormat( formatString );
            try {
                result = df.parse( df.format( date ) );
            } catch (ParseException e) {
                log.error( e.getMessage() );
            }
        }
        return result;
    }

    /**
     * @description: 按指定格式把字符串转换成日期
     * @author: fengzhutai
     * @createDate: 2013-8-4
     * @param date 日期的字符串
     * @param formatString 日期的格式
     * @return:
     */
    public static Date parseDate(String date, String formatString) {
        Date result = null;
        try {
            if ( !"".equals( date ) && date != null ) {
                SimpleDateFormat sdf = new SimpleDateFormat( formatString );
                result = sdf.parse( date );
            }
        } catch (ParseException e) {
            log.error( e.getMessage() );
        }
        return result;
    }

    /**
     * 返回yyyy-MM-dd格式的日期类型
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date stringToDateOfYYYY_MM_DD(String dateStr) throws ParseException {
        return getDateFormatByForAppoint( "yyyy-MM-dd" ).parse( dateStr );
    }

    /**
     * 以yyyy-MM-dd HHMM日期转字符串
     * 
     * @param Date
     * @return String
     * @throws ParseException
     */
    public static String dateToString(Date dt) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
        String strDate = df.format( dt );
        return strDate;
    }
    
    /**
     * 以yyyy-MM-dd HHMM日期转字符串
     * 
     * @param Date
     * @return String
     * @return formatString
     * @throws ParseException
     */
    public static String dateToString(Date dt, String formatString ) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat( formatString );
        String strDate = df.format( dt );
        return strDate;
    }

    /**
     * 以"yyyy-MM-dd HH:mm"字符串转换成Date
     * 
     * @param String
     * @return Date
     * @throws ParseException
     */
    public static Date stringToDate(String time) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
        Date dt = df.parse( time );
        return dt;

    }

    /**
     * 字符串转换成Date(参数格式需要是yyyy-MM-dd的形式)
     * 
     * @param stringDate
     * @return
     */
    public static Date stringToDateYYYYMMDD(String stringDate) {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        try {
            date = simpleDateFormat.parse( stringDate );
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        return date;
    }

    /**
     * 以string字符串转换成string 类型可自己定义
     * 
     * @param String
     * @param dateString time时间格式
     * @param formatString 转换string类型
     * @return string
     * @throws ParseException
     */
    public static String stringToString(String time, String dateString, String formatString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat( dateString );
            SimpleDateFormat sdf1 = new SimpleDateFormat( formatString );
            Date dt = sdf.parse( time );
            String s = sdf1.format( dt );
            return s;
        } catch (ParseException e) {
            log.error( e.getMessage() );
        }
        return null;
    }

    /**
     * 返回指定格式的DateFormat对象
     * 
     * @param format
     * @return
     */
    private static DateFormat getDateFormatByForAppoint(String format) {
        return new SimpleDateFormat( format );
    }

    /**
     * 获取当前日期时间
     * 
     * @return
     */
    public static Date getCurrentTime() {
        return new Date();
    }

    /**
     * 获取当前日期
     * 
     * @description:
     * @author: fengzt
     * @createDate: 2013-8-30
     * @param format
     * @return:
     */
    public static Date getCurrentDate() {
        Date today = new Date();
        return DateFormatUtil.getFormatDate( today, "yyyy-MM-dd" );
    }

    /**
     * 计算两个日期相隔多少天
     * 
     * @param fromDate 起始日期 yyyy-MM-dd
     * @param toDate 终止日期 yyyy-MM-dd
     * @return
     */
    public static int getIntervalOfDate(Date fromDate, Date toDate) {
        long day = 0;
        day = toDate.getTime() - fromDate.getTime();
        day = (int)(day / 1000 / 60 / 60 / 24);
        int diff = new Long( day ).intValue();
        return diff;
    }

    /**
     * @description: 日期字符串转化成想要格式的字符串
     *               DateFormatUtil.fromatDateString("20120808",
     *               "yyyyMMdd","yyyy-MM-dd HH:mm")
     * @author: fengzhutai
     * @createDate: 2013-8-29
     * @param dateString 日期字符串
     * @param sourceFormat 日期字符串的格式
     * @param targetString 目标日期字符串
     * @return:String
     */
    public static String fromatDateString(String dateString, String sourceFormat, String targetString) {
        SimpleDateFormat sdf = new SimpleDateFormat( sourceFormat );
        SimpleDateFormat sdf1 = new SimpleDateFormat( targetString );
        Date date = null;
        try {
            date = sdf.parse( dateString );
        } catch (ParseException e) {
            log.error( e.getMessage() );
        }

        if ( null != date ) {
            return sdf1.format( date );
        }

        return null;
    }

    /**
     * @description: 日期加减 ：如今天加2天 addDate(new Date() ,"d",2)
     * @author: fengzhutai
     * @createDate: 2013-8-29
     * @param destDate 目标日期
     * @param dateType 日期类型：y：年 M：月 d：日 H：小时 m：分 s：秒
     * @param amount 基数
     * @return:
     */
    public static Date addDate(Date destDate, String dateType, int amount) {

        if ( "y".equals( dateType ) ) {
            destDate = DateUtils.addYears( destDate, amount );
        } else if ( "M".equals( dateType ) ) {
            destDate = DateUtils.addMonths( destDate, amount );
        } else if ( "d".equals( dateType ) ) {
            int addDay = amount;
            destDate = DateUtils.addDays( destDate, addDay );
        } else if ( "H".equals( dateType ) ) {
            destDate = DateUtils.addHours( destDate, amount );
        } else if ( "m".equals( dateType ) ) {
            destDate = DateUtils.addMinutes( destDate, amount );
        } else if ( "s".equals( dateType ) ) {
            destDate = DateUtils.addSeconds( destDate, amount );
        }
        return destDate;
    }

  
    /**
     * 计算两个日期之间的相隔的年、月、日。注意：只有计算相隔天数是准确的，相隔年和月都是 近似值，按一年365天，一月30天计算，忽略闰年和闰月的差别。
     * 
     * @param datepart 两位的格式字符串，yy表示年，MM表示月，dd表示日
     * @param startdate 开始日期
     * @param enddate 结束日期
     * @return double 如果enddate>startdate，返回一个大于0的实数，否则返回一个小于等于0的实数
     */
    public static double dateDiff(String datepart, Date startdate, Date enddate) {
        if ( datepart == null || datepart.equals( "" ) ) {
            throw new IllegalArgumentException( "DateUtil.dateDiff()方法非法参数值：" + datepart );
        }

        double distance = (double) (enddate.getTime() - startdate.getTime()) / ((double) 60 * 60 * 24 * 1000);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( enddate.getTime() - startdate.getTime() );
        if ( datepart.equals( "yy" ) ) {
            distance = distance / 365;
        } else if ( datepart.equals( "MM" ) ) {
            distance = distance / 30;
        } else if ( datepart.equals( "dd" ) ) {
            distance = (enddate.getTime() - startdate.getTime()) / (double) (60 * 60 * 24 * 1000);
        } else if ( datepart.equals( "hh" ) ) {
            distance = (enddate.getTime() - startdate.getTime()) / (double) (60.0 * 60 * 1000);
        } else if ( datepart.equals( "ss" ) ) { // 得到秒
            distance = (enddate.getTime() - startdate.getTime()) / (double) 1000;
        } else if ( datepart.equals( "mm" ) ) {
            distance = (enddate.getTime() - startdate.getTime()) / (double) 1000 / (double) 60;
        } else {
            throw new IllegalArgumentException( "DateUtil.dateDiff()方法非法参数值：" + datepart );
        }
        return distance;
    }

    /**
     * @description:比较两个时间大小 dateBig > dateSm return true
     * @author: fengzhutai
     * @createDate: 2013-11-27
     * @param dateBg 第一个时间
     * @param dateSm 第二个时间
     * @param dateType 时间格式：eg: yyyyMMddHHmmss
     * @return: boolean
     */
    public static boolean compareTime(String dateBig, String dateSm, String dateType) {
        SimpleDateFormat df = new SimpleDateFormat( dateType );
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = df.parse( dateBig );
            dt2 = df.parse( dateSm );
            Calendar calDt1 = Calendar.getInstance();
            Calendar calDt2 = Calendar.getInstance();
            calDt1.setTime( dt1 );
            calDt2.setTime( dt2 );
            if ( calDt1.compareTo( calDt2 ) >= 0 ) {
                return true;
            }
        } catch (Exception e) {
            log.error( e.getMessage() );
        }

        return false;
    }

    /**
     * 
     * @description:比较两个时间 大于等于 >=
     * @author: fengzt
     * @createDate: 2014年6月16日
     * @param dateBig 大的时间
     * @param dageSm 小的时间
     * @return:boolean
     */
    public static boolean compareDate( Date dateBig , Date dateSm ){
        long bigTime = dateBig.getTime();
        
        long smTime = dateSm.getTime();
        
        if( bigTime >= smTime){
            return true;
        }
        
        return false;
    }
    
    /** 
     * 获取某年第一天日期 
     * @param year 年份 
     * @return Date 
     */  
    public static Date getCurrYearFirst(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        Date currYearFirst = calendar.getTime();  
        return getFormatDate( currYearFirst, "yyyy-MM-dd" );  
    }  
      
    /** 
     * 获取某年最后一天日期 
     * @param year 年份 
     * @return Date 
     */  
    public static Date getCurrYearLast(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.roll(Calendar.DAY_OF_YEAR, -1);  
        Date currYearLast = calendar.getTime();  
          
        return getFormatDate( currYearLast, "yyyy-MM-dd" );  
    }  
  
    
    
    public static void main(String[] args) {
        System.out.println( getCurrYearFirst( 2013 ));
        System.out.println( getCurrYearLast( 2013 ));
        
        Date big = addDate( getCurrentDate(), "d", 1 );
        
        Date sm = getCurrentDate();
        
        System.out.println( compareDate( big, sm ) );
        System.out.println( DateFormatUtil.stringToString( "2013-11-04 08:00", "yyyy-MM-dd HH:mm", "MM月dd日 HH时mm分" ) );
        // System.out.println( DateFormatUtil.fromatDateString( "20120808",
        // "yyyyMMdd", "yyyy-MM-dd HH:mm" ) );
        //
        // try {
        // System.out.println( DateFormatUtil.getIntervalOfDate(
        // DateFormatUtil.stringToDateOfYYYY_MM_DD( "2013-07-12" ),
        // DateFormatUtil.stringToDateOfYYYY_MM_DD( "2013-07-17" ) ) );
        //
        // Date today = new Date();
        // long time = today.getTime();
        // time = time - (time % (1000 * 60 * 60 * 24));
        //
        // long time2 = DateFormatUtil.getFormatDate( today, "yyyy-MM-dd"
        // ).getTime();
        // DateFormatUtil.addDate( today, "d", -1 );
        // System.out.println( time + "/" + time2 + "/" + new Date(
        // 1377792000000L ) );
        // } catch (ParseException e) {
        // e.printStackTrace();
        // }
        
        
        System.out.println( DateFormatUtil.addDate( getFormatDate( getCurrentDate(), "yyyy-MM-dd" ) , "d", 17 ));
    }
}
