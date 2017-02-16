package com.timss.ptw.dao;

import java.util.List;

import com.timss.ptw.bean.PtwSafe;

/**
 * 
 * @title: 安全隔离措施Dao
 * @description: {desc}
 * @company: gdyd
 * @className: PtwSafeDao.java
 * @author: 周保康
 * @createDate: 2014-7-18
 * @updateUser: 周保康
 * @version: 1.0
 */
public interface PtwSafeDao {
    /**
     * 查询一个站点下的所有工作票类型
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-18
     * @param siteId
     * @param isFireWt
     * @return:
     */
    List<PtwSafe> queryPtwSafeListByWtId(int wtId);
    
    /**
     * 插入一条安全措施
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-18
     * @param ptwSafe
     * @return:插入成功的条数
     */
    int insertPtwSafe(PtwSafe ptwSafe);
    
    /**
     * 批量插入安全措施
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-18
     * @param ptwSafes
     * @return:插入成功的条数
     */
    int batchInsertPtwSafe(List<PtwSafe> ptwSafes);
    
    /**
     * 根据工作票Id删除
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-18
     * @param wtId
     * @return:删除成功的条数
     */
    int deletePtwSafeByWtId(int wtId);
    
    /**
     * 批量更新解除人
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-31
     * @param ptwSafes
     * @return:
     */
    int batchUpdatePtwSafeRemover(List<PtwSafe> ptwSafes);
    
}
