package com.timss.workorder.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 计算工作时间的参数
 * @description: {desc}
 * @company: gdyd
 * @className: WorkTimeVo.java
 * @author: fengzt
 * @createDate: 2014年12月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class WorkTimeVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6903543516295334339L;
    
    /**
     * 开始时间（精确到分钟）
     */
    private Date start; 
    
    /**
     * 结束时间（精确到分钟）
     */
    private Date end;
    
    /**
     * 站点
     */
    private String siteId;
    
    /**
     * 早上上班时间( 例如：0800 时间格式：HHmm )
     */
    private String morning;
    
    /**
     *  早上下班时间( 例如：1200 时间格式：HHmm )
     */
    private String forenoon;
    
    /**
     *  下午上班时间( 例如：1400 时间格式：HHmm )
     */
    private String noon;
    
    /**
     * 下午下班时间( 例如：1700 时间格式：HHmm )
     */
    private String afternoon;
    
    /**
     * 工作时长（ 例如：8 ）
     */
    private double workTime;
    
    /**
     * 是否计算自然天（例如：休婚假是要计算自然天的  true --包括周末、节假日  false--正常上班时间 ）
     */
    private boolean isFlag;
    

    public boolean isFlag() {
        return isFlag;
    }

    public void setFlag(boolean isFlag) {
        this.isFlag = isFlag;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getMorning() {
        return morning;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public String getForenoon() {
        return forenoon;
    }

    public void setForenoon(String forenoon) {
        this.forenoon = forenoon;
    }

    public String getNoon() {
        return noon;
    }

    public void setNoon(String noon) {
        this.noon = noon;
    }

    public String getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(String afternoon) {
        this.afternoon = afternoon;
    }

    public double getWorkTime() {
        return workTime;
    }

    public void setWorkTime(double workTime) {
        this.workTime = workTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((afternoon == null) ? 0 : afternoon.hashCode());
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + ((forenoon == null) ? 0 : forenoon.hashCode());
        result = prime * result + (isFlag ? 1231 : 1237);
        result = prime * result + ((morning == null) ? 0 : morning.hashCode());
        result = prime * result + ((noon == null) ? 0 : noon.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        long temp;
        temp = Double.doubleToLongBits( workTime );
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        WorkTimeVo other = (WorkTimeVo) obj;
        if ( afternoon == null ) {
            if ( other.afternoon != null )
                return false;
        } else if ( !afternoon.equals( other.afternoon ) )
            return false;
        if ( end == null ) {
            if ( other.end != null )
                return false;
        } else if ( !end.equals( other.end ) )
            return false;
        if ( forenoon == null ) {
            if ( other.forenoon != null )
                return false;
        } else if ( !forenoon.equals( other.forenoon ) )
            return false;
        if ( isFlag != other.isFlag )
            return false;
        if ( morning == null ) {
            if ( other.morning != null )
                return false;
        } else if ( !morning.equals( other.morning ) )
            return false;
        if ( noon == null ) {
            if ( other.noon != null )
                return false;
        } else if ( !noon.equals( other.noon ) )
            return false;
        if ( siteId == null ) {
            if ( other.siteId != null )
                return false;
        } else if ( !siteId.equals( other.siteId ) )
            return false;
        if ( start == null ) {
            if ( other.start != null )
                return false;
        } else if ( !start.equals( other.start ) )
            return false;
        if ( Double.doubleToLongBits( workTime ) != Double.doubleToLongBits( other.workTime ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "WorkTimeVo [start=" + start + ", end=" + end + ", siteId=" + siteId + ", morning=" + morning
                + ", forenoon=" + forenoon + ", noon=" + noon + ", afternoon=" + afternoon + ", workTime=" + workTime
                + ", isFlag=" + isFlag + "]";
    }
    
    

}
