package com.timss.purchase.dao;

import java.util.List;
import java.util.Map;

import com.timss.purchase.bean.PurPaSyncHhc;
import com.timss.purchase.vo.PurBuisCalaVO;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurPaSyncHhcDao.java
 * @author: 890166
 * @createDate: 2015-3-24
 * @updateUser: 890166
 * @version: 1.0
 */
public interface PurPaSyncHhcDao {

  /**
   * @description:数据查询是否存在中间记录表
   * @author: 890166
   * @createDate: 2015-3-24
   * @param map
   * @return:
   */
  List<PurPaSyncHhc> queryPurPaSyncHhcByMap(Map<String, Object> map);

  /**
   * @description: 插入中间表信息
   * @author: 890166
   * @createDate: 2015-3-24
   * @param ppsh
   * @return:
   */
  int insertPurPaSyncHhcInfo(PurPaSyncHhc ppsh);

  /**
   * @description:根据发送商务网流水号查询本地采购申请物资是否已经采购
   * @author: user
   * @createDate: 2016-1-20
   * @param map
   * @return:
   */
  PurBuisCalaVO queryPurBuisCala(Map<String, Object> map);

}
