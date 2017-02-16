package com.timss.ptw.service;

import java.util.List;

import com.timss.ptw.bean.PtwType;

/**
 * 
 * @title: 工作票类型Service
 * @description: {desc}
 * @company: gdyd
 * @className: PtwTypeService.java
 * @author: 周保康
 * @createDate: 2014-7-8
 * @updateUser: 周保康
 * @version: 1.0
 */
public interface PtwTypeService {
    /**
     * 查询一个站点下的所有工作票类型
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-8
     * @param siteId
     * @return:
     */
    List<PtwType> queryTypesBySiteId(String siteId,Integer isFireWt);
    
    /**
     * 
     * @description:根据Id查询类型
     * @author: 周保康
     * @createDate: 2014-7-14
     * @param id
     * @return:
     */
    PtwType queryPtwTypeById(int id);
}
