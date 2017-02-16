package com.timss.ptw.dao;

import com.timss.ptw.bean.PtwExtand;

/**
 * 延期
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PtwExtandDao.java
 * @author: 周保康
 * @createDate: 2014-8-21
 * @updateUser: 周保康
 * @version: 1.0
 */
public interface PtwExtandDao {
    /**
     * 根据工作票编号查询
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-21
     * @param wtId
     * @return:
     */
    PtwExtand queryPtwExtandByPtwId(int wtId);
    
    /**
     * 插入工作票延期信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-21
     * @param ptwExtand
     * @return:
     */
    int insertPtwExtand(PtwExtand ptwExtand);
}
