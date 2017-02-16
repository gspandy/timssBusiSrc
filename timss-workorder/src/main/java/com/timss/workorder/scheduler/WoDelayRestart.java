package com.timss.workorder.scheduler;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.timss.workorder.service.WoDelayRestartService;

/**
 * @title: {title} 工单延迟后重新启动
 * @description: {desc}
 * @company: gdyd
 * @className: WoDelayRestart.java
 * @author: 王中华
 * @createDate: 2016-6-22
 * @updateUser: 王中华
 * @version: 1.0
 */
@Component
@Lazy(false)
public class WoDelayRestart{
    @Autowired
    private WoDelayRestartService woDelayRestartService;

    private static Logger logger = Logger.getLogger( WoDelayRestart.class );
    // 定时到每30分钟扫描一次
    @Scheduled(cron = "0 0/30 * * * ?")
    public void woDelayRestart()  throws Exception {
        logger.debug( "工单延迟重启定时任务扫描开始时间" + new Date() );
        woDelayRestartService.woDelayRestart();
    }

}
