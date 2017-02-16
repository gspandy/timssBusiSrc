package com.timss.itsm.scheduler;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Component
@Lazy(false)
public class ItsmhomePageCardStatistic {
//	@Autowired
//	private ItsmHomePageCardService  itsmHomePageCardService;
//	private static final Logger LOG = Logger.getLogger(ItsmhomePageCardStatistic.class);
	

	@Scheduled(cron = "0 0 2 * * ?")  //定时到每天凌晨2点扫描一次
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
	public void itsmhomePageCardStatistic() throws Exception{
//		itsmHomePageCardService.yearCardStatistic();
//		itsmHomePageCardService.monthCardStatistic();
//		System.out.println("---------首页卡片定时任务执行完-----------------");
	}

}
