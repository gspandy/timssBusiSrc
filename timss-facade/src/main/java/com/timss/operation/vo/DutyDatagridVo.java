package com.timss.operation.vo;

import java.io.Serializable;

/**
 * 
 * @title: 接收页面datagrid vo 最多九个值别
 * @description: 正对排版规则详情页面
 * @company: gdyd
 * @className: DutyDatagridVo.java
 * @author: fengzt
 * @createDate: 2014年6月11日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class DutyDatagridVo implements Serializable  {
    
    /**
     * 
     */
    private static final long serialVersionUID = -9051878309493906867L;

    /**
     * 天次
     */
    private int dayTime;
    
    /**
     * 1值
     */
    private int field1; 
    
    /**
     * 2值
     */
    private int field2; 
    
    /**
     * 3值
     */
    private int field3; 
    
    /**
     * 4值
     */
    private int field4; 
    
    /**
     * 5值
     */
    private int field5; 
    
    /**
     * 6值
     */
    private int field6; 
    
    /**
     * 7值
     */
    private int field7; 
    
    /**
     * 8值
     */
    private int field8; 
    
    /**
     * 9值
     */
    private int field9;

    public int getDayTime() {
        return dayTime;
    }

    public void setDayTime(int dayTime) {
        this.dayTime = dayTime;
    }

    public int getField1() {
        return field1;
    }

    public void setField1(int field1) {
        this.field1 = field1;
    }

    public int getField2() {
        return field2;
    }

    public void setField2(int field2) {
        this.field2 = field2;
    }

    public int getField3() {
        return field3;
    }

    public void setField3(int field3) {
        this.field3 = field3;
    }

    public int getField4() {
        return field4;
    }

    public void setField4(int field4) {
        this.field4 = field4;
    }

    public int getField5() {
        return field5;
    }

    public void setField5(int field5) {
        this.field5 = field5;
    }

    public int getField6() {
        return field6;
    }

    public void setField6(int field6) {
        this.field6 = field6;
    }

    public int getField7() {
        return field7;
    }

    public void setField7(int field7) {
        this.field7 = field7;
    }

    public int getField8() {
        return field8;
    }

    public void setField8(int field8) {
        this.field8 = field8;
    }

    public int getField9() {
        return field9;
    }

    public void setField9(int field9) {
        this.field9 = field9;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dayTime;
        result = prime * result + field1;
        result = prime * result + field2;
        result = prime * result + field3;
        result = prime * result + field4;
        result = prime * result + field5;
        result = prime * result + field6;
        result = prime * result + field7;
        result = prime * result + field8;
        result = prime * result + field9;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        DutyDatagridVo other = (DutyDatagridVo) obj;
        if ( dayTime != other.dayTime )
            return false;
        if ( field1 != other.field1 )
            return false;
        if ( field2 != other.field2 )
            return false;
        if ( field3 != other.field3 )
            return false;
        if ( field4 != other.field4 )
            return false;
        if ( field5 != other.field5 )
            return false;
        if ( field6 != other.field6 )
            return false;
        if ( field7 != other.field7 )
            return false;
        if ( field8 != other.field8 )
            return false;
        if ( field9 != other.field9 )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DutyDatagridVo [dayTime=" + dayTime + ", field1=" + field1 + ", field2=" + field2 + ", field3="
                + field3 + ", field4=" + field4 + ", field5=" + field5 + ", field6=" + field6 + ", field7=" + field7
                + ", field8=" + field8 + ", field9=" + field9 + "]";
    } 
    
    
}
