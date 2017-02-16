package com.timss.inventory.dao;

import com.timss.inventory.bean.InvMatMap;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatMapDao.java
 * @author: 890151
 * @createDate: 2016-5-27
 * @updateUser: 890151
 * @version: 1.0
 */
public interface InvMatMapDao {

    /**
     * @description:插入映射信息
     * @author: 890151
     * @createDate: 2016-5-27
     * @param imm
     * @return:
     */
    int insertInvMatMap(InvMatMap imm);
    /**
     * @description:通过imtdid删除映射信息
     * @author: 890151
     * @createDate: 2016-6-22
     * @param imtdid
     * @return:
     */
    int deleteInvMatMapByImtdid(String imtdid);

}
