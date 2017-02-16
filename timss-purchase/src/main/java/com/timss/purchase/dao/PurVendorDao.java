package com.timss.purchase.dao;

import java.util.List;
import java.util.Map;

import com.timss.purchase.bean.PurVendor;
import com.yudean.itc.dto.Page;

/*
 * @description: {desc}
 * @company: gdyd
 * @className: PurVendorDao.java
 * @author: 890166
 * @createDate: 2014-6-17
 * @updateUser: 890166
 * @version: 1.0
 */
public interface PurVendorDao {

  /*
   * @description:获取供应商列表
   * 
   * @author: 890166
   * 
   * @createDate: 2014-6-17
   * 
   * @param page
   * 
   * @return:
   */
  List<PurVendor> queryPurVendor(Page<?> page);

  /*
   * @description:根据companyNo找到供应商
   * 
   * @author: 890166
   * 
   * @createDate: 2014-6-19
   * 
   * @param companyNo
   * 
   * @return:
   */
  List<PurVendor> queryPurVendorDetailByCompanyNo(Map<String, Object> map);

  /*
   * @description:提供过个companyNo查询返回多个供应商
   * 
   * @author: 890166
   * 
   * @createDate: 2014-7-22
   * 
   * @param map
   * 
   * @return:
   */
  List<PurVendor> queryPurVendorBycompanyNos(Map<String, Object> map);

  /*
   * @description:查询供应商信息
   * 
   * @author: 890166
   * 
   * @createDate: 2014-7-1
   * 
   * @param page
   * 
   * @return:
   */
  List<PurVendor> queryCompanyList(Page<?> page);

  /*
   * @description:更新供应商信息
   * 
   * @author: 890166
   * 
   * @createDate: 2014-6-19
   * 
   * @param _purVendor
   * 
   * @return:
   */
  int updatePurVendor(PurVendor purVendor);

  /*
   * @description:插入供应商信息
   * 
   * @author: 890166
   * 
   * @createDate: 2014-6-19
   * 
   * @param _purVendor
   * 
   * @return:
   */
  int insertPurVendor(PurVendor purVendor);

}
