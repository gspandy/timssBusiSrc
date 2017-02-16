package com.timss.inventory.service;

import java.util.List;

import com.timss.inventory.bean.InvUnit;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvUnitService.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvUnitService {
    /**
     * @description:分页查询给定站点id的计量单位列表
     * @author: 890147
     * @createDate: 2014-8-4
     * @param page
     * @return:
     */
    List<InvUnit> queryUnitListBySiteId(Page<InvUnit> page) throws Exception;

    /**
     * @description:根据给定id和站点id查询计量单位
     * @author: 890147
     * @createDate: 2014-8-4
     * @param id
     * @param siteId
     * @return:
     */
    InvUnit queryUnitDetail(String id, String siteId) throws Exception;

    /**
     * 插入计量单位
     * 
     * @description:
     * @author: 890147
     * @createDate: 2014-8-4
     * @param unitInfo
     * @return:
     */
    int insertUnitInfo(InvUnit unitInfo) throws Exception;

    /**
     * 更新计量单位
     * 
     * @description:
     * @author: 890147
     * @createDate: 2014-8-4
     * @param unitInfo
     * @return:
     */
    int updateUnitInfo(InvUnit unitInfo) throws Exception;

    /**
     * @description:更新计量单位状态
     * @author: 890147
     * @createDate: 2014-8-4
     * @param id
     * @param siteId
     * @param state
     * @return:
     */
    int updateUnitState(String id, String siteId, String state) throws Exception;

    /**
     * @description:删除计量单位
     * @author: 890147
     * @createDate: 2014-8-7
     * @param id
     * @param siteId
     * @return:
     */
    int deleteUnit(String id, String siteId) throws Exception;

    /**
     * @description:在站点中查询给定编码的计量单位
     * @author: 890147
     * @createDate: 2014-8-10
     * @param siteId
     * @param code
     * @return:
     */
    InvUnit queryUnitByCodeAndSiteId(String siteId, String code) throws Exception;
}
