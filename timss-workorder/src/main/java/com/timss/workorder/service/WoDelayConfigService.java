package com.timss.workorder.service;

import com.timss.workorder.bean.WoDelayConfig;


public interface WoDelayConfigService {

    WoDelayConfig queryWoDelayConfig(String delayType, String priority, String siteid);
	
}
