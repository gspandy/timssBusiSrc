package com.timss.inventory.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.inventory.bean.InvUnit;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvUnitDao.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890147
 * @version: 1.0
 */
public interface InvUnitDao {

    /**
     * @description:分页查询给定站点id的计量单位列表
     * @author: 890147
     * @createDate: 2014-8-4
     * @param page
     * @return:
     */
    List<InvUnit> queryUnitListBySiteId(Page<InvUnit> page);

    /**
     * @description:根据给定id和站点id查询计量单位
     * @author: 890147
     * @createDate: 2014-8-4
     * @param id
     * @param siteId
     * @return:
     */
    InvUnit queryUnitDetail(@Param("id") String id, @Param("siteId") String siteId);

    /**
     * @description:插入计量单位
     * @author: 890147
     * @createDate: 2014-8-4
     * @param unitInfo
     * @return:
     */
    int insertUnitInfo(InvUnit unitInfo);

    /**
     * @description:更新计量单位
     * @author: 890147
     * @createDate: 2014-8-4
     * @param unitInfo
     * @return:
     */
    int updateUnitInfo(InvUnit unitInfo);

    /**
     * @description:更新计量单位状态
     * @author: 890147
     * @createDate: 2014-8-4
     * @param id
     * @param siteId
     * @param state
     * @return:
     */
    int updateUnitState(@Param("id") String id, @Param("state") String state, @Param("siteId") String siteId);

    /**
     * @description:删除计量单位
     * @author: 890147
     * @createDate: 2014-8-7
     * @param id
     * @param siteId
     * @return:
     */
    int deleteUnit(@Param("id") String id, @Param("siteId") String siteId);

    /**
     * @description:
     * @author: 890147
     * @createDate: 2014-8-10
     * @param siteId
     * @param code
     * @return:
     */
    InvUnit queryUnitByCodeAndSiteId(@Param("siteId") String siteId, @Param("code") String code);

    /**
     * @description:根据unitname查询unit信息
     * @author: 890166
     * @createDate: 2015-5-28
     * @param map
     * @return:
     */
    List<InvUnit> queryUnitByUnitname(InvUnit unitInfo);
}
