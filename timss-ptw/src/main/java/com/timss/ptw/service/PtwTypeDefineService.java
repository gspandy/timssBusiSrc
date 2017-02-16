package com.timss.ptw.service;

import com.timss.ptw.bean.PtwTypeDefine;

/**
 * 
 * @title: 工作票类型定义Service
 * @description: {desc}
 * @company: gdyd
 * @className: PtwTypeService.java
 * @author: 周保康
 * @createDate: 2014-7-8
 * @updateUser: 周保康
 * @version: 1.0
 */
public interface PtwTypeDefineService {
    /**
     * 
     * @description:查询指定工作票类型的参数信息
     * @author: 周保康
     * @createDate: 2014-7-10
     * @param id
     * @return:
     */
    PtwTypeDefine queryPtwTypeDefineById(int id);
    /**
     * 根据工作票Id查询
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-27
     * @param wtId
     * @return:
     */
    PtwTypeDefine queryPtwTypeDefineByPtwId(int wtId);
    
    
}
