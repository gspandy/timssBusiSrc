package com.timss.purchase.service.core;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.purchase.bean.PurPoSyncHhc;
import com.timss.purchase.dao.PurPoSyncHhcDao;
import com.timss.purchase.service.PurPoSyncHhcService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.ClassCastUtil;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.impl.UserInfoScopeImpl;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurPoSyncHhcServiceImpl.java
 * @author: 890166
 * @createDate: 2015-3-24
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("PurPoSyncHhcServiceImpl")
public class PurPoSyncHhcServiceImpl implements PurPoSyncHhcService {

  @Autowired
  private PurPoSyncHhcDao purPoSyncHhcDao;

  /**
   * @description: 查询最后一次同步信息
   * @author: 890166
   * @createDate: 2015-3-24
   * @return:
   */
  @Override
  public Date queryLastTimeGetData(UserInfo uInfo) {
    // 如果没有查询到最后一次的时间，则获取昨天的数据
    Date reDate = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(reDate);
    c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
    reDate = c.getTime();

    try {
      UserInfoScopeImpl scope = ClassCastUtil.castAllField2Class(UserInfoScopeImpl.class, uInfo);

      Page<PurPoSyncHhc> page = scope.getPage();
      page.setParameter("siteid", uInfo.getSiteId());
      page.setSortKey("createdate");
      page.setSortOrder("desc");

      List<PurPoSyncHhc> ppshList = purPoSyncHhcDao.queryPurPoSyncHhcData(page);
      if (null != ppshList && !ppshList.isEmpty()) {
        PurPoSyncHhc ppsh = ppshList.get(0);
        reDate = ppsh.getCreatedate();
      }
    } catch (Exception e) {
      throw new RuntimeException(
          "---------PurPoSyncHhcServiceImpl 中的 queryLastTimeGetData 方法抛出异常---------：", e);
    }
    return reDate;
  }

  /**
   * @description: 插入中间表数据
   * @author: 890166
   * @createDate: 2015-3-24
   * @param PurPoSyncHhc
   * @return:
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int insertPurPoSyncHhcInfo(PurPoSyncHhc purPoSyncHhc) {
    return purPoSyncHhcDao.insertPurPoSyncHhcInfo(purPoSyncHhc);
  }

}
