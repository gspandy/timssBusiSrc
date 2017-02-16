package com.timss.workorder.dao;

import org.apache.ibatis.annotations.Param;

import com.timss.workorder.bean.WoDelayConfig;

public interface WoDelayConfigDao {

    WoDelayConfig queryWoDelayConfig( @Param("delayType")String delayType, 
            @Param("priority")String priority,@Param("siteid")String siteId);

}
