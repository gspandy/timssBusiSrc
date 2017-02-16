package com.timss.inventory.service;

import java.util.List;
import java.util.Map;

import com.timss.inventory.vo.SpareBean;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvEquipItemMappingService.java
 * @author: 890166
 * @createDate: 2014-7-23
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvEquipItemMappingService {

    /**
     * @description:查询关联物资
     * @author: 890166
     * @createDate: 2014-7-23
     * @param assetId
     * @return
     * @throws Exception :
     */
    Page<SpareBean> getSpareByAssetId(UserInfoScope userInfo) throws Exception;

    /**
     * @description:删除设备关联
     * @author: 890166
     * @createDate: 2014-7-23
     * @param map
     * @return:
     */
    int deleteMappingInfoByItemInfo(Map<String, Object> map) throws Exception;

    /**
     * @description:插入设备关联
     * @author: 890166
     * @createDate: 2014-7-23
     * @param map
     * @return:
     */
    int insertMappingInfoByItemInfo(Map<String, Object> map) throws Exception;

    /**
     * 查询绑定设备信息
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-16
     * @param userInfo
     * @param itemId
     * @return
     * @throws Exception :
     */
    List<Map<String, Object>> queryEquipInfo(UserInfoScope userInfo, String itemId) throws Exception;
}
