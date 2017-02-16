package com.timss.ptw.bean;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 两票用户信息bean
 * @description: 两票用户信息
 * @company: gdyd
 * @className: PtwPtoUserInfo.java
 * @author: gucw
 * @createDate: 2015年7月10日
 * @updateUser:
 * @version: 1.0
 */
public class PtwPtoUserInfo extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = -1768293188924829857L;
    private String configId;
    private String userId;
    

    public String getConfigId() {
        return configId;
    }


    public void setConfigId(String configId) {
        this.configId = configId;
    }


    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "PtwPtoUserInfo [configId=" + configId + ", userId=" + userId + "]";
    }

}
