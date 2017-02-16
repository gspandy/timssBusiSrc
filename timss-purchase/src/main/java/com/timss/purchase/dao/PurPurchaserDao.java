package com.timss.purchase.dao;

import org.apache.ibatis.annotations.Param;
import com.timss.purchase.bean.PurOrderPurchaserBean;
import com.timss.purchase.bean.PurPurchaserBean;

/*
 * @description: {desc}
 * @company: gdyd
 * @className: PurPurchaserDao.java
 * @author: 890191
 * @createDate: 2015-9-28
 * @updateUser: 890166
 * @version: 1.0
 */
public interface PurPurchaserDao {

  /*
   * @description:新增买方资料
   * 
   * @author: 890191
   * 
   * @createDate: 2015-9-24
   * 
   * @param purPurchaserTemp
   * 
   * @return
   * 
   */
  int insertPurPurchaser(PurPurchaserBean purPurchaserBean);

  
  /*
   * @description:修改买方资料
   * 
   * @author: 890191
   * 
   * @createDate: 2015-9-24
   * 
   * @param purPurchaserTemp
   * 
   * @return
   */
  int updatePurPurchaser(PurPurchaserBean purPurchaserBean);
  
  /*
   * @description:根据站点查询买方资料
   * 
   * @author: 890191
   * 
   * @createDate: 2015-9-28
   * 
   * @param siteId
   * 
   * @return
   */
  PurPurchaserBean queryPurPurchaserBySiteId(String siteId);
  
  /*
   * @description:根据sheetID查询买方资料副本
   * 
   * @author: gucw
   * 
   * @createDate: 2015-10-09
   * 
   * @param sheetId siteid
   * 
   * @return
   */
  PurOrderPurchaserBean queryPurchaserBySheetId(@Param("sheetId") String sheetId,@Param("siteid") String siteid);
  /*
   * @description:为采购申请增加买方资料副本
   * 
   * @author: gucw
   * 
   * @createDate: 2015-10-09
   * 
   * @param PurOrderPurchaserBean
   * 
   * @return
   */
  int insertPurOrderPurchaser(PurOrderPurchaserBean purOrderPurchaserBean);
}
