package com.timss.purchase.service;

import com.timss.purchase.bean.PurOrderPurchaserBean;
import com.timss.purchase.bean.PurPurchaserBean;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurPurchaserService.java
 * @author: 890191
 * @createDate: 2015-9-28
 * @version: 1.0
 */
public interface PurPurchaserService {
    
    /**
     * @description:新增买方合同资料
     * @author: 890191
     * @createDate: 2015-9-24
     * @param PurPurchaserBean
     * @return
     */
    int insertPurPurchaser(UserInfoScope userInfo, PurPurchaserBean purPurchaserBean);

    
    /**
     * @description:修改买方合同资料
     * @author: 890191
     * @createDate: 2015-9-24
     * @param PurPurchaserBean
     * @return
     */
    int updatePurPurchaser(UserInfoScope userInfo, PurPurchaserBean purPurchaserBean);
    
    /**
     * @description:根据找点查询买方合同资料
     * @author: 890191
     * @createDate: 2015-9-28
     * @param purPurchaserTemp
     * @return
     */
    PurPurchaserBean queryPurPurchaserBySiteId(UserInfoScope userInfo);
    
    /**
     * @description:新增合同买方资料副本
     * @author: gucw
     * @createDate: 2015-10-09
     * @param PurOrderPurchaserBean
     * @return int
     */
    int insertPurOrderPurchaser(UserInfoScope userInfo, PurOrderPurchaserBean purOrderPurchaserBean);
    
    /**
     * @description:根据合同id查询买方资料信息
     * @author: gucw
     * @createDate: 2015-10-09
     * @return PurOrderPurchaserBean
     */
    PurOrderPurchaserBean queryPurOrderPurchaserBySheetId(String sheetId,String siteid);
}
