package com.timss.purchase.service.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.purchase.bean.PurProcessMapping;
import com.timss.purchase.dao.PurProcessMappingDao;
import com.timss.purchase.service.PurProcessMappingService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurProcessMappingServiceImpl.java
 * @author: 890166
 * @createDate: 2014-8-12
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("PurProcessMappingServiceImpl")
public class PurProcessMappingServiceImpl implements PurProcessMappingService {

  @Autowired
  private PurProcessMappingDao purProcessMappingDao;

  /**
   * 新增流程映射记录
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-7-4
   * @param sheetId
   * @param siteId
   * @param modelType
   * @return
   * @throws Exception
   *           :
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int insertPurProcessMapping(PurProcessMapping ppm) throws Exception {
    return purProcessMappingDao.insertPurProcessMapping(ppm);
  }

  /**
   * 更新流程映射记录
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-7-4
   * @param sheetId
   * @param siteId
   * @param modelType
   * @return
   * @throws Exception
   *           :
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int updatePurProcessMapping(PurProcessMapping ppm) throws Exception {
    return purProcessMappingDao.updatePurProcessMapping(ppm);
  }

  /**
   * @description:通过sheetid、siteid和modelType找到所在流程id
   * @author: 890166
   * @createDate: 2014-7-4
   * @param sheetId
   * @param siteId
   * @param modelType
   * @return
   * @throws Exception
   *           :
   */
  @Override
  public String queryProcessIdByParams(PurProcessMapping ppm) throws Exception {
    Map<String, String> map = new HashMap<String, String>();
    map.put("sheetId", ppm.getMasterkey());
    map.put("siteId", ppm.getSiteid());
    map.put("modelType", ppm.getModeltype());
    return purProcessMappingDao.queryProcessIdByParams(map);
  }
}
