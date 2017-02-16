package com.timss.ptw.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.ptw.bean.PtwType;

/**
 * 
 * @title: 工作票类型Dao
 * @description: {desc}
 * @company: gdyd
 * @className: PtwTypeDao.java
 * @author: 周保康
 * @createDate: 2014-7-8
 * @updateUser: 周保康
 * @version: 1.0
 */
public interface PtwTypeDao {
    /**
     * 查询一个站点下的所有工作票类型
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-8
     * @param siteId
     * @param isFireWt
     * @return:
     */
    List<PtwType> queryTypesBySiteId(@Param("siteId") String siteId,@Param("isFireWt") Integer isFireWt);
    
    /**
     * 
     * @description:根据Id查询类型
     * @author: 周保康
     * @createDate: 2014-7-14
     * @param id
     * @return:
     */
    PtwType queryPtwTypeById(int id);
    
    /**
     * 查询一个站点下的满足typeCodes的所有工作票类型
     * @description:
     * @author: 朱旺
     * @createDate: 2015-12-01
     * @param siteId
     * @param typeCodes
     * @param isFireWt
     * @return:
     */
    List<PtwType> queryTypesByTypeCode(@Param("siteId") String siteId, @Param("typeCodes") String[] typeCodes, @Param("isFireWt") Integer isFireWt);
}
