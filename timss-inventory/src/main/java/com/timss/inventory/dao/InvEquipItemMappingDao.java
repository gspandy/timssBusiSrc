package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvEquipItemMapping;
import com.timss.inventory.vo.SpareBean;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvEquipItemMappingDao.java
 * @author: 890166
 * @createDate: 2014-7-17
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvEquipItemMappingDao {

    /**
     * @description:删除已绑定的映射数据
     * @author: 890166
     * @createDate: 2014-7-17
     * @param itemId
     * @param siteId
     * @return:
     */
    int deleteMappingInfoByItemInfo(Map<String, Object> map);

    /**
     * @description:插入关联设备信息
     * @author: 890166
     * @createDate: 2014-7-17
     * @param ieim
     * @return:
     */
    int insertMappingInfoByItemInfo(InvEquipItemMapping ieim);

    /**
     * @description:查询关联物资
     * @author: 890166
     * @createDate: 2014-7-23
     * @param assetId
     * @return:
     */
    List<SpareBean> getSpareByAssetId(Page<?> page);

    /**
     * 查询绑定设备信息
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-16
     * @param map
     * @return:
     */
    List<Map<String, Object>> queryEquipInfo(Map<String, Object> map);
}
