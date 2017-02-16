package com.timss.ptw.service;

import java.util.List;

import com.timss.ptw.bean.PtwWaitRestore;

/**
 * 间断和转移
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PtwWaitRestoreService.java
 * @author: 周保康
 * @createDate: 2014-8-23
 * @updateUser: 周保康
 * @version: 1.0
 */
public interface PtwWaitRestoreService {
    /**
     * 根据工作票编号查询
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-23
     * @param wtId
     * @return:
     */
    List<PtwWaitRestore> queryPtwWaitRestoreByPtwId(int wtId);
    
    /**
     * 插入间断信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-23
     * @param ptwWaitRestore
     * @return:
     */
    int insertPtwWait(PtwWaitRestore ptwWaitRestore);
    
    /**
     * 更新重新开工时间
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-23
     * @param ptwWaitRestore
     * @return:
     */
    int updatePtwRestore(PtwWaitRestore ptwWaitRestore);
    
    /**
     * 判断工作票能否进行需要重新开工的工作
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-23
     * @param ptwWaitRestores
     * @return:true:能重新开工；false：不能重新开工
     */
    boolean ptwNeedRestore(List<PtwWaitRestore> ptwWaitRestores);
    
}
