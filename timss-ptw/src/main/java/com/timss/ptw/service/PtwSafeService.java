package com.timss.ptw.service;

import java.util.HashMap;
import java.util.List;

import com.timss.ptw.bean.PtwSafe;

/**
 * 
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PtwSafeService.java
 * @author: 周保康
 * @createDate: 2014-7-18
 * @updateUser: 周保康
 * @version: 1.0
 */
public interface PtwSafeService {
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
    int batchInsertPtwSafe(int ptwId,String safeItems);
    
   
    /**
     * 将safeItems的string转换为list
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-29
     * @param safeItems
     * @param ptwId
     * @return:
     */
    List<PtwSafe> fromSafeItemsToList(String safeItems,int ptwId);
    
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
    int batchUpdatePtwSafeRemover(int ptwId,String safeItems);
    
    public HashMap<String, Object> genPtwSafeMap(List<PtwSafe> ptwSafes);
}
