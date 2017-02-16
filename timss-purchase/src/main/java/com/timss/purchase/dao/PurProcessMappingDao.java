package com.timss.purchase.dao;

import java.util.Map;

import com.timss.purchase.bean.PurProcessMapping;

/*
 * @description: {desc}
 * @company: gdyd
 * @className: PurProcessMappingDao.java
 * @author: 890166
 * @createDate: 2014-8-12
 * @updateUser: 890166
 * @version: 1.0
 */
public interface PurProcessMappingDao {

  /*
   * @description:新增流程映射记录
   * 
   * @author: 890166
   * 
   * @createDate: 2014-7-4
   * 
   * @param ppm
   * 
   * @return:
   */
  int insertPurProcessMapping(PurProcessMapping ppm);

  /*
   * @description:更新流程映射记录
   * 
   * @author: 890166
   * 
   * @createDate: 2014-7-4
   * 
   * @param ppm
   * 
   * @return:
   */
  int updatePurProcessMapping(PurProcessMapping ppm);

  /*
   * @description:通过sheetid、siteid和modelType找到所在流程id
   * 
   * @author: 890166
   * 
   * @createDate: 2014-7-4
   * 
   * @param map
   * 
   * @return:
   */
  String queryProcessIdByParams(Map<String, String> map);

  /*
   * @description:通过实体类查询processId
   * 
   * @author: 890166
   * 
   * @createDate: 2015-1-6
   * 
   * @param ppm
   * 
   * @return:
   */
  String queryProcessIdByEntity(PurProcessMapping ppm);
}
