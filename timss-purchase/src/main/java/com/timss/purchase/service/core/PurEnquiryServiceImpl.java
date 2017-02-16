package com.timss.purchase.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.purchase.bean.PurEnquiry;
import com.timss.purchase.bean.PurEnquiryItem;
import com.timss.purchase.dao.PurEnquiryDao;
import com.timss.purchase.service.PurEnquiryService;
import com.timss.purchase.vo.PurEnquiryItemVO;
import com.timss.purchase.vo.PurEnquiryVO;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

@Service("PurEnquiryServiceImpl")
public class PurEnquiryServiceImpl implements PurEnquiryService {

  /**
   * 注入Dao
   */
  @Autowired
  private PurEnquiryDao purEnquiryDao;

  /**
   * 分页查询根据当前人的id查询他自己的询价单列表
   */
  @Override
  public Page<PurEnquiryVO> queryPurEnquiry(UserInfo userinfo) throws Exception {
    UserInfoScope scope = (UserInfoScope) userinfo;
    Page<PurEnquiryVO> page = scope.getPage();
    page.setParameter("createuser", userinfo.getUserId());
    List<PurEnquiryVO> ret = purEnquiryDao.queryPurEnquiry(page);
    page.setResults(ret);
    return page;
  }

  /**
   * 分页查询根据当前人的id和条件查询他自己的询价单列表
   */
  @Override
  public Page<PurEnquiryVO> queryPurEnquiry(@Operator UserInfo userinfo,
      @LogParam("purEnquiry") PurEnquiryVO purEnquiry) throws Exception {
    UserInfoScope scope = (UserInfoScope) userinfo;
    Page<PurEnquiryVO> page = scope.getPage();
    page.setParameter("createuser", userinfo.getUserId());
    if (null != purEnquiry) {
      page.setParameter("enquiryid", purEnquiry.getEnquiryid());
      page.setParameter("enquiryname", purEnquiry.getEnquiryname());
      page.setParameter("contactname", purEnquiry.getContactname());
      page.setParameter("contactphone", purEnquiry.getContactphone());
      page.setParameter("expirydate", purEnquiry.getExpirydate());
      page.setParameter("status", purEnquiry.getStatus());
    }
    List<PurEnquiryVO> ret = purEnquiryDao.queryPurEnquiry(page);
    page.setResults(ret);
    return page;
  }

  /**
   * 根据询价单ID删除询价单
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int deletePurEnquiryDataByrowId(String rowId) throws Exception {
    return purEnquiryDao.deletePurEnquiryDataByrowId(rowId);
  }

  /**
   * 根据rowid查询详细信息
   */
  @Override
  public List<PurEnquiryVO> queryPurEnquiryByrowId(String rowId) throws Exception {
    return purEnquiryDao.queryPurEnquiryByrowId(rowId);
  }

  /**
   * 根据id查询enquiry明细列表
   */
  @Override
  public Page<PurEnquiryItemVO> queryPurEnquiryItemsById(@Operator UserInfo userinfo,
      @LogParam("id") String id) throws Exception {
    UserInfoScope scope = (UserInfoScope) userinfo;
    Page<PurEnquiryItemVO> page = scope.getPage();
    List<PurEnquiryItemVO> ret = purEnquiryDao.queryPurEnquiryItemsById(id);
    page.setResults(ret);
    return page;
  }

  /**
   * 新增询价单
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public PurEnquiry insertPurEnquiry(PurEnquiry purEnquiry) throws Exception {
    return purEnquiryDao.insertPurEnquiry(purEnquiry);
  }

  /**
   * 插入询价单详细信息操作
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int insertPurEnquiryItem(PurEnquiryItem purEnquiryItem) throws Exception {
    return purEnquiryDao.insertPurEnquiryItem(purEnquiryItem);
  }

}
