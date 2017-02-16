package com.timss.ptw.dao;

import java.util.List;

import com.timss.ptw.bean.PtwProcess;

/**
 * 流程相关
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PtwProcessDao.java
 * @author: 周保康
 * @createDate: 2014-8-26
 * @updateUser: 周保康
 * @version: 1.0
 */
public interface PtwProcessDao {
    /**
     * 根据工作票编号查询
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-26
     * @param wtId
     * @return:
     */
    List<PtwProcess> queryPtwProcessByPtwId(int wtId);
    
    /**
     * 根据工作票编号和工作票的状态查询
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-26
     * @param ptwProcess
     * @return:
     */
    PtwProcess queryPtwProcessByWtIdAndStatus(PtwProcess ptwProcess);
    
    /**
     * 插入工作票审批信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-26
     * @param ptwProcess
     * @return:
     */
    int insertPtwProcess(PtwProcess ptwProcess);
    
    
}
