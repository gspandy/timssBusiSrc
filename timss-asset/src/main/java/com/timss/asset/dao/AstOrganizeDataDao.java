package com.timss.asset.dao;

import java.util.List;
import java.util.Map;

import com.timss.asset.bean.AstOrganizeData;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: AstOrganizeDataDao.java
 * @author: 890166
 * @createDate: 2015-5-29
 * @updateUser: 890166
 * @version: 1.0
 */
public interface AstOrganizeDataDao {

    /**
     * @description: 插入AstOrganizeData数据
     * @author: 890166
     * @createDate: 2015-6-1
     * @param iod
     * @return:
     */
    int insertAstOrganizeData(AstOrganizeData aod);

    /**
     * @description: 删除AstOrganizeData数据
     * @author: 890166
     * @createDate: 2015-6-1
     * @param iod
     * @return:
     */
    int deleteAstOrganizeData(AstOrganizeData aod);

    /**
     * @description:查询AstOrganizeData数据
     * @author: 890166
     * @createDate: 2015-6-1
     * @param page
     * @return:
     */
    List<AstOrganizeData> queryAstOrganizeData(Page<AstOrganizeData> page);

    /**
     * @description: 触发调用oracle的存储过程整理数据
     * @author: 890166
     * @createDate: 2015-6-2
     * @param paramMap
     * @return:
     */
    void callAstOrganizeDataInit(Map<String, Object> paramMap);
}
