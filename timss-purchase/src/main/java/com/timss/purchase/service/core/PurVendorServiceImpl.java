package com.timss.purchase.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.purchase.bean.PurVendor;
import com.timss.purchase.dao.PurVendorDao;
import com.timss.purchase.service.PurVendorService;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurVendorServiceImpl.java
 * @author: 890166
 * @createDate: 2014-6-17
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("PurVendorServiceImpl")
public class PurVendorServiceImpl implements PurVendorService {

  /**
   * 注入Dao
   */
  @Autowired
  private PurVendorDao purVendorDao;

  /**
   * log4j输出
   */
  private static final Logger LOG = Logger.getLogger(PurVendorServiceImpl.class);

  /**
   * 获取供应商列表
   */
  @Override
  public Page<PurVendor> queryPurVendor(@Operator UserInfo userinfo,
      @LogParam("purVendor") PurVendor purVendor) throws Exception {
    UserInfoScope scope = (UserInfoScope) userinfo;
    Page<PurVendor> page = scope.getPage();
    page.setParameter("siteid", userinfo.getSiteId());

    String sort = String.valueOf(scope.getParam("sort") == null ? "" : scope.getParam("sort"));
    String order = String.valueOf(scope.getParam("order") == null ? "" : scope.getParam("order"));
    if (!"".equals(sort) && !"".equals(order)) {
      page.setSortKey(sort);
      page.setSortOrder(order);
    } else {
      page.setSortKey("name");
      page.setSortOrder("asc");
    }

    if (null != purVendor) {
      page.setParameter("name", purVendor.getName());
      page.setParameter("type", purVendor.getType());
      page.setParameter("contact", purVendor.getContact());
      page.setParameter("tel", purVendor.getTel());
      page.setParameter("fax", purVendor.getFax());
    }
    List<PurVendor> ret = purVendorDao.queryPurVendor(page);
    page.setResults(ret);
    return page;
  }

  /**
   * 根据companyno查询供应商详细信息
   */
  @Override
  public List<PurVendor> queryPurVendorDetailByCompanyNo(String companyNo, String siteId)
      throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("companyNo", companyNo);
    map.put("siteId", siteId);
    List<PurVendor> list = purVendorDao.queryPurVendorDetailByCompanyNo(map);
    return list;
  }

  /**
   * @description:提供过个companyNo查询返回多个供应商
   * @author: 890166
   * @createDate: 2014-7-22
   * @param companyNos
   * @return
   * @throws Exception
   *           :
   */
  @Override
  public List<PurVendor> queryPurVendorBycompanyNos(List<String> companyNos, String siteId)
      throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    StringBuilder sb = new StringBuilder("");
    for (String companyNo : companyNos) {
      sb.append("'").append(companyNo).append("',");
    }
    String idStr = sb.toString().substring(0, sb.toString().length() - 1);
    map.put("ids", idStr);
    map.put("siteId", siteId);
    return purVendorDao.queryPurVendorBycompanyNos(map);
  }

  /**
   * @description:查询供应商信息
   * @author: 890166
   * @createDate: 2014-7-1
   * @param userInfo
   * @return
   * @throws Exception
   */
  @Override
  // 暂时移除日志
  public Page<PurVendor> queryCompanyList(@Operator UserInfoScope userInfo,
      @LogParam("purVendor") PurVendor purVendor) throws Exception {
    LOG.info("查询供应商列表---------------------");
    UserInfoScope scope = userInfo;
    Page<PurVendor> page = scope.getPage();
    page.setParameter("siteId", userInfo.getSiteId());
    LOG.info("查询供应商列表siteid---------------------" + userInfo.getSiteId());
    if (null != purVendor) {
      LOG.info("查询供应商列表name---------------------" + purVendor.getName());
      page.setParameter("name", purVendor.getName());
      LOG.info("查询供应商列表tel---------------------" + purVendor.getTel());
      page.setParameter("tel", purVendor.getTel());
      LOG.info("查询供应商列表contact---------------------" + purVendor.getContact());
      page.setParameter("contact", purVendor.getContact());
      LOG.info("查询供应商列表type---------------------" + purVendor.getType());
      page.setParameter("type", purVendor.getType());
    }
    List<PurVendor> ret = purVendorDao.queryCompanyList(page);
    page.setResults(ret);
    return page;
  }

  /**
   * save or update操作
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int purVendorSaveOrUpdate(@Operator UserInfoScope userInfo,
      @LogParam("purVendor") PurVendor purVendor) throws Exception {
    int count = 0;
    String companyNo = purVendor.getCompanyNo() == null ? "" : purVendor.getCompanyNo();
    purVendor.setSiteid(userInfo.getSiteId());
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("companyNo", companyNo);
    map.put("siteId", userInfo.getSiteId());
    List<PurVendor> list = purVendorDao.queryPurVendorDetailByCompanyNo(map);
    if (null != list && list.size() == 1) {
      purVendor.setModifyuser(userInfo.getUserId());
      purVendor.setModifydate(new Date());
      count = purVendorDao.updatePurVendor(purVendor);
    } else {
      purVendor.setCreateuser(userInfo.getUserId());
      purVendor.setCreatedate(new Date());
      count = purVendorDao.insertPurVendor(purVendor);
    }
    return count;
  }
}
