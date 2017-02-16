package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvMatMapping;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatMappingDao.java
 * @author: 890166
 * @createDate: 2014-7-18
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatMappingDao {
    /*------------------------旧的映射表增删改在此处，新的映射表增删改在InvMatMapDao中-------------------------------------------*/
	/**
     * @description:插入映射信息
     * @author: 890166
     * @createDate: 2014-7-18
     * @param imm
     * @return:
     */
    int insertInvMatMapping(InvMatMapping imm);

    /**
     * @description:通过imtdid删除映射信息
     * @author: 890166
     * @createDate: 2014-7-25
     * @param imtdid
     * @return:
     */
    int deleteMatMappingByImtdid(String imtdid);


    
    
    
    /*------------------------新的映射表查询都放在该Dao中-------------------------------------------*/
    /**
     * @description:查询映射信息
     * @author: 890166
     * @createDate: 2014-7-18
     * @param map
     * @return:
     */
    List<InvMatMapping> queryInvMatMappingInfo(Map<String, Object> map);
    
    /**
     * @description:根据外部信息查询映射表中是否存在记录
     * @author: 890166
     * @createDate: 2014-7-16
     * @param map
     * @return:
     */
    List<InvMatMapping> queryInvMatMappingByOutterInfo(Map<String, Object> map);
}
