package com.timss.attendance.vo;

import java.io.Serializable;

/***
 * 
 * @title: 汉王考勤机 -- 考勤记录的vo
 * @description: {desc}
 * @company: gdyd
 * @className: CardResultVo.java
 * @author: fengzt
 * @createDate: 2015年6月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class CardResultVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1285730026647090174L;

    /**
     * 对应userId
     */
    private String id;
    
    /**
     * 对应userName
     */
    private String name;
    
    /**
     * 打卡时间
     */
    private String time;
    
    /**
     * 
     */
    private String workcode;
    
    /**
     * 
     */
    private String status;
    
    /**
     * 
     */
    private String authority;
    
    /**
     * card_src
     */
    private String card_src;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWorkcode() {
        return workcode;
    }

    public void setWorkcode(String workcode) {
        this.workcode = workcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getCard_src() {
        return card_src;
    }

    public void setCard_src(String card_src) {
        this.card_src = card_src;
    }

    @Override
    public String toString() {
        return "CardResultVo [id=" + id + ", name=" + name + ", time=" + time + ", workcode=" + workcode + ", status="
                + status + ", authority=" + authority + ", card_src=" + card_src + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authority == null) ? 0 : authority.hashCode());
        result = prime * result + ((card_src == null) ? 0 : card_src.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        result = prime * result + ((workcode == null) ? 0 : workcode.hashCode());
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
        CardResultVo other = (CardResultVo) obj;
        if ( authority == null ) {
            if ( other.authority != null )
                return false;
        } else if ( !authority.equals( other.authority ) )
            return false;
        if ( card_src == null ) {
            if ( other.card_src != null )
                return false;
        } else if ( !card_src.equals( other.card_src ) )
            return false;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        } else if ( !id.equals( other.id ) )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        if ( status == null ) {
            if ( other.status != null )
                return false;
        } else if ( !status.equals( other.status ) )
            return false;
        if ( time == null ) {
            if ( other.time != null )
                return false;
        } else if ( !time.equals( other.time ) )
            return false;
        if ( workcode == null ) {
            if ( other.workcode != null )
                return false;
        } else if ( !workcode.equals( other.workcode ) )
            return false;
        return true;
    }
    
    
    
}
