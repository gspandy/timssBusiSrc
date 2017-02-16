package com.timss.purchase.service.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.purchase.bean.PurAttachMapping;
import com.timss.purchase.dao.PurAttachMappingDao;
import com.timss.purchase.service.PurAttachMappingService;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurAttachMappingServiceImpl.java
 * @author: 890162
 * @createDate: 2015-10-22
 * @updateUser: 890162
 * @version: 1.0
 */
@Service("PurAttachMappingServiceImpl")
public class PurAttachMappingServiceImpl implements PurAttachMappingService {

  @Autowired
  private PurAttachMappingDao purAttachMappingDao;

  /**
   * @description:插入映射附件数据
   * @author: 890162
   * @createDate: 2015-10-22
   * @param pam
   * @return:
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int insertPurAttachMapping(PurAttachMapping pam) throws Exception {
    return purAttachMappingDao.insertPurAttachMapping(pam);
  }

  /**
   * @description:插入映射附件数据
   * @author: 890162
   * @createDate: 2015-10-22
   * @param pam
   * @return:
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int deletePurAttachMappingBySheetno(String sheetno) throws Exception {
    return purAttachMappingDao.deletePurAttachMappingBySheetno(sheetno);
  }

  /**
   * @description:插入映射附件数据
   * @author: 890162
   * @createDate: 2015-10-22
   * @param pam
   * @return:
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public List<PurAttachMapping> queryPurAttachMappingBysheetno(String sheetno) {
    return purAttachMappingDao.queryPurAttachMappingBysheetno(sheetno);
  }

  /**
   * @description: 查询并获取采购合同附件
   * @author: 890162
   * @createDate: 2015-10-22
   * @param sheetno
   * @return
   * @throws Exception
   *           :
   */
  @Override
  public List<Map<String, Object>> queryPurAttach(String sheetno) throws Exception {
    List<PurAttachMapping> pamList = queryPurAttachMappingBysheetno(sheetno);
    List<String> ids = new ArrayList<String>();
    for (int i = 0; i < pamList.size(); i++) {
      PurAttachMapping pam = pamList.get(i);
      ids.add(pam.getAttachid());
    }
    List<Map<String, Object>> fileMapList = new ArrayList<Map<String, Object>>();
    fileMapList = FileUploadUtil.getJsonFileList(Constant.basePath, ids);
    if(fileMapList!=null ){
    	for (Map<String, Object> filePropertyMap : fileMapList) {
    		if(filePropertyMap.containsKey("fileName")){
    	        String fileName = filePropertyMap.get("fileName").toString();
    	        fileName = fileName.replace("'", "&apos;");
    	        filePropertyMap.put("fileName", fileName);
    		}
		}
    }
    return fileMapList;
  }

}
