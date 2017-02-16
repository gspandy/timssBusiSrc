package com.timss.workorder.service.swf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.workorder.bean.WoDelayConfig;
import com.timss.workorder.dao.WoDelayConfigDao;
import com.timss.workorder.service.WoDelayConfigService;
@Service
public class WoDelayConfigServiceImpl implements WoDelayConfigService{

    @Autowired
    private WoDelayConfigDao woDelayConfigDao;
    @Override
    public WoDelayConfig queryWoDelayConfig(String delayType, String priority, String siteid) {
        return woDelayConfigDao.queryWoDelayConfig( delayType, priority, siteid );
    }
   

}
