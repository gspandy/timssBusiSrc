package com.timss.purchase.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.timss.purchase.service.PurInvoiceService;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.handler.ThreadLocalHandler;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 发票相关定时任务
 * @description: {desc}
 * @company: gdyd
 * @className: CheckMachineScheduler.java
 * @author: fengzt
 * @createDate: 2015年6月18日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Component
@Lazy(false)
public class PurInvoiceScheduler {

  private Logger log = LoggerFactory.getLogger(PurInvoiceScheduler.class);

  @Autowired
  private ItcMvcService itcMvcService;

  @Autowired
  private PurInvoiceService purInvoiceService;

  /**
   * 
   * @description:发票定时任务(生物质)
   * @author: fengzt
   * @createDate: 2015年6月18日
   * @throws Exception
   *           :
   */
  @Scheduled(cron = "0 0/30 7 * * ?")
  public void remindInvoiceSwf() throws Exception {

    log.debug("------------PurInvoiceScheduler-remindInvoice----start -------");
    UserInfo userInfo1 = itcMvcService.getUserInfo("superadmin", "ITC");
    ThreadLocalHandler.createNewVarableOweUserInfo(userInfo1);
    List<String> sites = purInvoiceService.queryAllSite();
    for (String siteId : sites) {
      UserInfo userInfo = itcMvcService.getUserInfo("superadmin", siteId);
      ThreadLocalHandler.createNewVarableOweUserInfo(userInfo);
      purInvoiceService.remindInvoice(siteId);

    }

    log.debug("------------PurInvoiceScheduler-remindInvoice----end -------");
  }

}
