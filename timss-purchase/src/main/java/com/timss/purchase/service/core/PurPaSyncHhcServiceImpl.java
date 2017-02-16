package com.timss.purchase.service.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.purchase.bean.PurApply;
import com.timss.purchase.bean.PurPaSyncHhc;
import com.timss.purchase.dao.PurPaSyncHhcDao;
import com.timss.purchase.service.PurPaSyncHhcService;
import com.timss.purchase.vo.PurBuisCalaVO;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurPaSyncHhcServiceImpl.java
 * @author: 890166
 * @createDate: 2015-3-24
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("PurPaSyncHhcServiceImpl")
public class PurPaSyncHhcServiceImpl implements PurPaSyncHhcService {

  @Autowired
  private PurPaSyncHhcDao purPaSyncHhcDao;

  /**
   * @description: 保存中间表映射信息
   * @author: 890166
   * @createDate: 2015-3-24
   * @param pa
   * @param userInfo
   * @throws Exception
   *           :
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public PurPaSyncHhc savePurPaSyncHhc(PurApply pa, UserInfoScope userInfo) {

    PurPaSyncHhc ppsh = new PurPaSyncHhc();
    ppsh.setCreatedate(new Date());
    ppsh.setCreateuser(userInfo.getUserId());
    ppsh.setSiteid(userInfo.getSiteId());
    ppsh.setStatus("1"); // 发送成功
    ppsh.setTimPano(pa.getSheetno());
    purPaSyncHhcDao.insertPurPaSyncHhcInfo(ppsh);

    return ppsh;

  }

  /**
   * @description:数据查询是否存在中间记录表
   * @author: 890166
   * @createDate: 2015-3-24
   * @param map
   * @return:
   */
  @Override
  public List<PurPaSyncHhc> queryPurPaSyncHhcByMap(Map<String, Object> map) {
    return purPaSyncHhcDao.queryPurPaSyncHhcByMap(map);
  }

  /**
   * @description:根据发送商务网流水号查询本地采购申请物资是否已经采购
   * @author: user
   * @createDate: 2016-1-20
   * @param map
   * @return:
   */
  @Override
  public PurBuisCalaVO queryPurBuisCala(Map<String, Object> map) {
    return purPaSyncHhcDao.queryPurBuisCala(map);
  }

}
