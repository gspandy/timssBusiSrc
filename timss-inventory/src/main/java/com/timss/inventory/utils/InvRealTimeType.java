package com.timss.inventory.utils;

/**
 * @title: 计算库存实时数据辅助常量类
 * @description: 计算库存实时数据辅助常量类
 * @company: gdyd
 * @className: InvRealTimeType.java
 * @author: yuanzh
 * @createDate: 2016-5-9
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class InvRealTimeType {

    /**
     * 更新/插入业务相关的数据
     */
    public static final String REALTIME_BUSI = "CAN_USE_QTY,CAN_USE_QTY_OLD";

    /**
     * 更新/插入物资主项目相关的数据
     */
    public static final String REALTIME_RUNW = "ACTUAL_QTY,CAN_OUT_QTY_TOTAL," +
    		"WITH_TAX_PRICE,NO_TAX_PRICE,TAX," +
    		"WITH_TAX_PRICE_OLD,NO_TAX_PRICE_OLD,TAX_OLD," +
    		"LAST_IN_TIME,LAST_IN_QTY," +
    		"LAST_IN_PRICE,LAST_IN_NO_TAX_PRICE,LAST_IN_TAX";

    /**
     * 更新插入所有物资数据字段
     */
    public static final String REALTIME_ALL  = REALTIME_BUSI + "," + REALTIME_RUNW;

}
