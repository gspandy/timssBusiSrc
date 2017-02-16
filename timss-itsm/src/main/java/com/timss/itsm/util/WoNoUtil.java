package com.timss.itsm.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import com.timss.itsm.bean.ItsmJPItems;

public class WoNoUtil {
    /**
     * @description: 公用生成编号
     * @author: 王中华
     * @createDate: 2014-6-25
     * @param woId
     * @param preFix
     * @return:
     */
    public static String genCode(int id, String preFix) {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat( "yyyyMMdd" );
        String sheetNO = preFix + df.format( date );

        String sheetidstr = Integer.toString( id );
        if ( sheetidstr.length() == 1 ) {
            sheetNO += "000" + sheetidstr;
        } else if ( sheetidstr.length() == 2 ) {
            sheetNO += "00" + sheetidstr;
        } else if ( sheetidstr.length() == 3 ) {
            sheetNO += "0" + sheetidstr;
        } else if ( sheetidstr.length() > 3 ) {
            sheetNO += sheetidstr.substring( sheetidstr.length() - 4, sheetidstr.length() );
        } else {
            sheetNO += sheetidstr;
        }
        return sheetNO;
    }

    /**
     * @description: 生成工单编号
     * @author: 王中华
     * @createDate: 2014-6-25
     * @param woId
     * @return:
     */
    public static String genWoCode(int woId) {
        return genCode( woId, "WO" );
    }

    /**
     * @description: 生成维护计划编号
     * @author: 王中华
     * @createDate: 2014-6-25
     * @param mtpId
     * @return:
     */
    public static String genMTPCode(int mtpId) {
        return genCode( mtpId, "MTP" );
    }

    /**
     * @description: 生成作业方案编号
     * @author: 王中华
     * @createDate: 2014-6-25
     * @param jpId
     * @return:
     */
    public static String genJPCode(int jpId) {
        return genCode( jpId, "JP" );
    }

    /**
     * @description:完工汇报时，比较策划时领的工具和物资，和实际消耗的工具和物资，得出消耗物资的数量
     * @author: 王中华
     * @createDate: 2014-7-12
     * @param list1 策划时领取的物资bean list
     * @param list2 汇报时领取的物资bean list
     * @return: 以json字符串的方式返回（"87655"：2,……） 87655代表物资ID， 2
     *          代表策划时多领了2个，（0，代表消耗和领取是一样的，正数代表多领了，负数代表少领了）
     */
    public static JSONObject getItemsNumSubtraction(List<ItsmJPItems> list1, List<ItsmJPItems> list2) {
        int size1 = list1.size();
        int size2 = list2.size();
        JSONObject resultJsonObject = new JSONObject();
        for ( int i = 0; i < size1; i++ ) {
            ItsmJPItems jpItems1 = list1.get( i );
            String itemsId1 = String.valueOf( jpItems1.getItemsId() );
            resultJsonObject.put( itemsId1, jpItems1.getApplyCount() );
        }
        for ( int i = 0; i < size2; i++ ) {
            ItsmJPItems jpItems2 = list2.get( i );
            String itemsId2 = String.valueOf( jpItems2.getItemsId() );
            if ( resultJsonObject.containsKey( itemsId2 ) ) { // 如果汇报时的工具，策划时有选择
                int count1 = resultJsonObject.getInt( itemsId2 );
                int count2 = jpItems2.getApplyCount();
                resultJsonObject.remove( itemsId2 );
                if ( count1 - count2 > 0 ) {
                    resultJsonObject.put( itemsId2, count1 - count2 );
                }
            }
        }
        return resultJsonObject;
    }

    /**
     * @description:获取本月的第一天
     * @author: 王中华
     * @createDate: 2014-8-12
     * @param yearCode
     * @param monthCode
     * @return:
     */
    public static Date getFirstDayOfMonth(int yearCode, int monthCode) {
        Calendar calendar = Calendar.getInstance();
        calendar.set( yearCode, monthCode - 1, 1, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );
        return calendar.getTime();
    }

    /**
     * @description:获取上个月的第一天
     * @author: 王中华
     * @createDate: 2014-8-12
     * @param yearCode
     * @param monthCode
     * @return:
     */
    public static Date getFirstDayOfLastMonth(int yearCode, int monthCode) {
        Calendar calendar = Calendar.getInstance();
        calendar.set( yearCode, monthCode - 2, 1, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );
        return calendar.getTime();
    }

    public static void main(String[] args) {
        // Date d1 = getFirstDayOfMonth(2014, 12);
        // System.out.println(d1);
        // System.out.println(getFirstDayOfLastMonth(2014, 12));
        // boolean flag = isNumeric( "568498655" );
        // System.out.println(flag);
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile( "[0-9]*" );
        Matcher isNum = pattern.matcher( str );
        if ( !isNum.matches() ) {
            return false;
        }
        return true;
    }
}
