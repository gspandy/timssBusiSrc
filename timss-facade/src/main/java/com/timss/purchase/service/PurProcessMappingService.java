package com.timss.purchase.service;

import com.timss.purchase.bean.PurProcessMapping;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurProcessMappingService.java
 * @author: 890166
 * @createDate: 2014-8-12
 * @updateUser: 890166
 * @version: 1.0
 */
public interface PurProcessMappingService {

    /**
     * @description:新增流程映射记录
     * @author: 890166
     * @createDate: 2014-7-4
     * @param sheetId
     * @param siteId
     * @param modelType
     * @return
     * @throws Exception:
     */
    int insertPurProcessMapping(PurProcessMapping ppm) throws Exception;

    /**
     * @description:更新流程映射记录
     * @author: 890166
     * @createDate: 2014-7-4
     * @param sheetId
     * @param siteId
     * @param modelType
     * @return
     * @throws Exception:
     */
    int updatePurProcessMapping(PurProcessMapping ppm) throws Exception;

    /**
     * @description:通过sheetid、siteid和modelType找到所在流程id
     * @author: 890166
     * @createDate: 2014-7-4
     * @param sheetId
     * @param siteId
     * @param modelType
     * @return
     * @throws Exception:
     */
    String queryProcessIdByParams(PurProcessMapping ppm) throws Exception;
}
