package com.timss.inventory.service;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvWarehouseItem;
import com.timss.inventory.vo.InvWarehouseItemVO;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvWarehouseItemService.java
 * @author: 890166
 * @createDate: 2014-7-17
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvWarehouseItemService {

    /**
     * 保存到仓库
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-17
     * @param userInfo
     * @param iwiv
     * @param paramMap
     * @return
     * @throws Exception :
     */
    boolean saveInvWarehouseItem(UserInfoScope userInfo, InvWarehouseItemVO iwiv, Map<String, Object> paramMap)
            throws Exception;

    /**
     * 通过itemid查询所在仓库
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-18
     * @param userInfo
     * @param itemId
     * @return
     * @throws Exception :
     */
    List<InvWarehouseItemVO> queryInvWarehouseItem(UserInfoScope userInfo, String itemId) throws Exception;

    /**
     * 批量更新物资安全库存值
     * @description:
     * @author: 890151
     * @createDate: 2016-9-22
     * @param userInfoScope 
     * @param invWarehouseItemList
     * @return
     * @throws Exception :
     */    
    Map<String, Object> batchUpdateSafeQty(UserInfoScope userInfoScope, List<InvWarehouseItem> invWarehouseItemList) throws Exception;
}
