package com.timss.ptw.dao;

import com.timss.ptw.bean.PtwTypeDefine;

/**
 * 
 * @title: 工作票类型定义Dao
 * @description: {desc}
 * @company: gdyd
 * @className: PtwTypeDao.java
 * @author: 周保康
 * @createDate: 2014-7-8
 * @updateUser: 周保康
 * @version: 1.0
 */
public interface PtwTypeDefineDao {
    /**
     * 
     * @description:查询指定工作票类型的参数信息
     * @author: 周保康
     * @createDate: 2014-7-10
     * @param id
     * @return:
     */
    PtwTypeDefine queryPtwTypeDefineById(int id);
}
