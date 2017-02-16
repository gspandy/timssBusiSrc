package com.timss.inventory.service;

import java.util.Map;

import com.timss.inventory.bean.InvOrganizeData;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvOrganizeDataService.java
 * @author: 890166
 * @createDate: 2015-5-29
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvOrganizeDataService {

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
     * @description: 查询InvOrganizeData信息
     * @author: 890166
     * @createDate: 2015-6-1
     * @param iod
     * @return:
     */
    Page<InvOrganizeData> queryInvOrganizeData(UserInfoScope userInfo, InvOrganizeData iod);

    /**
     * @description: 触发调用oracle的存储过程整理数据
     * @author: 890166
     * @createDate: 2015-6-2
     * @param paramMap
     * @return:
     */
    boolean callOrganizeDataInit(Map<String, Object> paramMap);
}
