package com.timss.purchase.service.core;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.purchase.bean.PurOrderPurchaserBean;
import com.timss.purchase.bean.PurPurchaserBean;
import com.timss.purchase.dao.PurPurchaserDao;
import com.timss.purchase.service.PurPurchaserService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurPurchaserServiceImpl.java
 * @author: 890191
 * @createDate: 2015-9-28
 * @updateUser: 
 * @version: 1.0
 */
@Service("PurPurchaserServiceImpl")
public class PurPurchaserServiceImpl implements PurPurchaserService {

    Logger LOG = Logger.getLogger( PurPurchaserServiceImpl.class );
  /**
   * 注入Dao
   */
  @Autowired
  private PurPurchaserDao purchaserDao;
	
	 /**
	   * @description: 新增买方资料
	   * @author: 890191
	   * @createDate: 2015-9-24
	   * @return
	   */
	@Override
	public int insertPurPurchaser(UserInfoScope userInfo, PurPurchaserBean purPurchaserBean) {
		purPurchaserBean.setPurchaserId(null);
		purPurchaserBean.setCreatedate(new Date());
		purPurchaserBean.setCreateuser(userInfo.getUserId());
		purPurchaserBean.setSiteid(userInfo.getSiteId());
		 return purchaserDao.insertPurPurchaser(purPurchaserBean);
	}
	
	 /**
	   * @description: 编辑买方资料
	   * @author: 890191
	   * @createDate: 2015-9-24
	   * @return
	   */
	@Override
	public int updatePurPurchaser(UserInfoScope userInfo, PurPurchaserBean purPurchaserBean) {
		purPurchaserBean.setModifydate(new Date());
		purPurchaserBean.setModifyuser(userInfo.getUserId());
		purPurchaserBean.setSiteid(userInfo.getSiteId());
		return purchaserDao.updatePurPurchaser(purPurchaserBean);
	}

	 /**
	   * @description: 根据站点查询买方资料
	   * @author: 890191
	   * @createDate: 2015-9-24
	   * @return
	   */
	@Override
	public PurPurchaserBean queryPurPurchaserBySiteId(UserInfoScope userInfo) {
		return purchaserDao.queryPurPurchaserBySiteId(userInfo.getSiteId());
	}

    @Override
    public int insertPurOrderPurchaser(UserInfoScope userInfo, PurOrderPurchaserBean purOrderPurchaserBean) {
        return purchaserDao.insertPurOrderPurchaser(purOrderPurchaserBean);
    }

    @Override
    public PurOrderPurchaserBean queryPurOrderPurchaserBySheetId(String sheetId,String siteid) {
        return purchaserDao.queryPurchaserBySheetId( sheetId,siteid);
    }
}
