package com.timss.purchase.scheduler;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.timss.purchase.service.PurApplyService;
import com.timss.purchase.service.PurOrderService;
import com.timss.purchase.service.PurPoSyncHhcService;
import com.timss.purchase.utils.CommonUtil;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.handler.ThreadLocalHandler;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: HhcSyncScheduler.java
 * @author: 890166
 * @createDate: 2014-10-30
 * @updateUser: 890166
 * @version: 1.0
 */
@Component
@Lazy(false)
public class HhcSyncScheduler {

  private static final Logger LOG = Logger.getLogger(HhcSyncScheduler.class);

  @Autowired
  private PurApplyService purApplyService;

  @Autowired
  private ItcMvcService itcMvcService;

  @Autowired
  private PurPoSyncHhcService purPoSyncHhcService;

  @Autowired
  private PurOrderService purOrderService;

  /**
   * @description: 每天晚上同步到商务网
   * @author: 890166
   * @createDate: 2014-10-30
   * @throws Exception
   *           :
   */
  public void syncDataToHhcEam() throws Exception {
    LOG.info("=============================> 开始增量同步物资数据");
    purApplyService.syncDataToEamItem(new Date());
    LOG.info("=============================> 增量同步结束");
  }

  /**
   * @description: 每隔5分钟获取一次商务网最新审批完毕的采购订单
   * @author: 890166
   * @createDate: 2015-3-17
   * @throws Exception
   *           :
   */
  @Scheduled(cron = "0 0/5 * * * ?")
  public void processApprovalCompleteData() throws Exception {
    LOG.info("---------------------------------------------------------------------------- ");
    LOG.info("---------------------------- 开始增量获取商务网采购订单数据 ----------------------------");
    String siteUsed = CommonUtil.getProperties("hhcSite");
    String[] siteIds = siteUsed.split(",");

    for (String siteId : siteIds) {
      UserInfo uInfo = itcMvcService.getUserInfo(siteId + "scheduler", siteId);
      ThreadLocalHandler.createNewVarableOweUserInfo(uInfo);
      Date lastDate = purPoSyncHhcService.queryLastTimeGetData(uInfo);
      // 获取商务网数据并自动生成采购订单
      purOrderService.processACDataFormBusi(lastDate);
    }
    LOG.info("---------------------------- 增量获取结束 ----------------------------");
    LOG.info("---------------------------------------------------------------------------- ");
  }
}
