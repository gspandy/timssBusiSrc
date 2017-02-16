package com.timss.operation.vo;

import java.io.Serializable;

/**
 * 
 * @title: 日历渲染事件Vo
 * @description: {desc}
 * @company: gdyd
 * @className: EventsVo.java
 * @author: fengzt
 * @createDate: 2014年6月18日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class EventsVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4435599763165841855L;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 时间 (yyyy-MM-dd)
     */
    private String start;
    
    /**
     * 颜色值
     */
    private String color;

    /**
     * 值别和排班的信息
     */
    private CalendarVo dutyShift;
    
    public CalendarVo getDutyShift() {
		return dutyShift;
	}

	public void setDutyShift(CalendarVo dutyShift) {
		this.dutyShift = dutyShift;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
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
        EventsVo other = (EventsVo) obj;
        if ( color == null ) {
            if ( other.color != null )
                return false;
        } else if ( !color.equals( other.color ) )
            return false;
        if ( start == null ) {
            if ( other.start != null )
                return false;
        } else if ( !start.equals( other.start ) )
            return false;
        if ( title == null ) {
            if ( other.title != null )
                return false;
        } else if ( !title.equals( other.title ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "EventsVo [title=" + title + ", start=" + start + ", color=" + color + "]";
    }
    
    

}
