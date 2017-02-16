package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvOutterMapping;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvOutterMappingDao.java
 * @author: 890166
 * @createDate: 2014-8-11
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvOutterMappingDao {

    /**
     * @description:插入映射表操作
     * @author: 890166
     * @createDate: 2014-8-11
     * @param iom
     * @return:
     */
    int insertInvOutterMapping(InvOutterMapping iom);

    /**
     * @description: 通过outterNo查询出中间表数据
     * @author: 890166
     * @createDate: 2014-10-20
     * @return:
     */
    List<InvOutterMapping> queryInvOutterMappingByOutterNo(Map<String, Object> map);

    /**
     * @description:通过实体查询映射表
     * @author: 890166
     * @createDate: 2015-3-20
     * @param iom
     * @return:
     */
    List<InvOutterMapping> queryInvOutterMapping(InvOutterMapping iom);

    /**
     * @description: 删除外部映射信息
     * @author: 890166
     * @createDate: 2015-3-18
     * @param map
     * @return:
     */
    int deleteOutterMappingByMap(Map<String, Object> map);
}
