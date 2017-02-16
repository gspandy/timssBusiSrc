package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvOrganizeData;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvOrganizeDataDao.java
 * @author: 890166
 * @createDate: 2015-5-29
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvOrganizeDataDao {

    /**
     * @description: 插入InvOrganizeData数据
     * @author: 890166
     * @createDate: 2015-6-1
     * @param iod
     * @return:
     */
    int insertInvOrganizeData(InvOrganizeData iod);

    /**
     * @description: 删除InvOrganizeData数据
     * @author: 890166
     * @createDate: 2015-6-1
     * @param iod
     * @return:
     */
    int deleteInvOrganizeData(InvOrganizeData iod);

    /**
     * @description:查询InvOrganizeData数据
     * @author: 890166
     * @createDate: 2015-6-1
     * @param page
     * @return:
     */
    List<InvOrganizeData> queryInvOrganizeData(Page<InvOrganizeData> page);

    /**
     * @description: 触发调用oracle的存储过程整理数据
     * @author: 890166
     * @createDate: 2015-6-2
     * @param paramMap
     * @return:
     */
    void callOrganizeDataInit(Map<String, Object> paramMap);
}
