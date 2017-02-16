package com.timss.purchase.dao;

import java.util.List;

import com.timss.purchase.bean.PurHhcMaxnum;

/*
 * @description: {desc}
 * @company: gdyd
 * @className: PurHhcMaxnumDao.java
 * @author: 890166
 * @createDate: 2014-10-31
 * @updateUser: 890166
 * @version: 1.0
 */
public interface PurHhcMaxnumDao {

  /*
   * @description: 通过站点查询最大数记录
   * 
   * @author: 890166
   * 
   * @createDate: 2014-10-31
   * 
   * @param siteid
   * 
   * @return
   * 
   * @throws Exception :
   */
  List<PurHhcMaxnum> queryPurHhcMaxnum(String siteid);

  /*
   * @description:插入中间表数据
   * 
   * @author: 890166
   * 
   * @createDate: 2014-11-3
   * 
   * @param phm
   * 
   * @return
   * 
   * @throws Exception :
   */
  int insertPurHhcMaxnum(PurHhcMaxnum phm);

  /*
   * @description:更新中间表数据
   * 
   * @author: 890166
   * 
   * @createDate: 2014-11-3
   * 
   * @param phm
   * 
   * @return
   * 
   * @throws Exception :
   */
  int updatePurHhcMaxnum(PurHhcMaxnum phm);

}
