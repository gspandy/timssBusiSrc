package com.timss.ptw.dao;

import com.timss.ptw.bean.PtwRemarkTask;

/**
 * 增加任务
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PtwRemarkTaskDao.java
 * @author: 周保康
 * @createDate: 2014-8-23
 * @updateUser: 周保康
 * @version: 1.0
 */
public interface PtwRemarkTaskDao {
    /**
     * 根据工作票编号查询
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-23
     * @param wtId
     * @return:
     */
    PtwRemarkTask queryPtwRemarkTaskByPtwId(int wtId);
    
    /**
     * 插入新增任务
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-23
     * @param ptwRemarkTask
     * @return:
     */
    int insertPtwRemarkTask(PtwRemarkTask ptwRemarkTask);
}
