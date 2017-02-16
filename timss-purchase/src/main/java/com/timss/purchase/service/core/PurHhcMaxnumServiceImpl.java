package com.timss.purchase.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.purchase.bean.PurHhcMaxnum;
import com.timss.purchase.dao.PurHhcMaxnumDao;
import com.timss.purchase.service.PurHhcMaxnumService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurHhcMaxnumServiceImpl.java
 * @author: 890166
 * @createDate: 2014-10-31
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("PurHhcMaxnumServiceImpl")
public class PurHhcMaxnumServiceImpl implements PurHhcMaxnumService {

  @Autowired
  private PurHhcMaxnumDao purHhcMaxnumDao;

  /**
   * @description: 通过站点查询最大数记录
   * @author: 890166
   * @createDate: 2014-10-31
   * @param siteid
   * @return
   * @throws Exception
   *           :
   */
  @Override
  public List<PurHhcMaxnum> queryPurHhcMaxnum(String siteid) throws Exception {
    return purHhcMaxnumDao.queryPurHhcMaxnum(siteid);
  }

  /**
   * @description:插入中间表数据
   * @author: 890166
   * @createDate: 2014-11-3
   * @param phm
   * @return
   * @throws Exception
   *           :
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int insertPurHhcMaxnum(PurHhcMaxnum phm) throws Exception {
    return purHhcMaxnumDao.insertPurHhcMaxnum(phm);
  }

  /**
   * @description:更新中间表数据
   * @author: 890166
   * @createDate: 2014-11-3
   * @param phm
   * @return
   * @throws Exception
   *           :
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int updatePurHhcMaxnum(PurHhcMaxnum phm) throws Exception {
    return purHhcMaxnumDao.updatePurHhcMaxnum(phm);
  }
}
