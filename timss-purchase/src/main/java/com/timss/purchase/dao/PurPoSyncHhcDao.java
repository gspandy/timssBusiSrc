package com.timss.purchase.dao;

import java.util.List;

import com.timss.purchase.bean.PurPoSyncHhc;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurPoSyncHhcDao.java
 * @author: 890166
 * @createDate: 2015-3-24
 * @updateUser: 890166
 * @version: 1.0
 */
public interface PurPoSyncHhcDao {

  /**
   * @description: 查询最新的同步信息
   * @author: 890166
   * @createDate: 2015-3-24
   * @param page
   * @return:
   */
  List<PurPoSyncHhc> queryPurPoSyncHhcData(Page<?> page);

  /**
   * @description: 插入中间表数据
   * @author: 890166
   * @createDate: 2015-3-24
   * @param PurPoSyncHhc
   * @return:
   */
  int insertPurPoSyncHhcInfo(PurPoSyncHhc purPoSyncHhc);
}
