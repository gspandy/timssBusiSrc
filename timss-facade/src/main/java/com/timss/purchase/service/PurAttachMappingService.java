package com.timss.purchase.service;

import java.util.List;
import java.util.Map;

import com.timss.purchase.bean.PurAttachMapping;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurAttachMappingService.java
 * @author: 890162
 * @createDate: 2015-10-22
 * @updateUser: 890162
 * @version: 1.0
 */
public interface PurAttachMappingService {

    /**
     * @description:插入映射附件数据
     * @author: 890162
     * @createDate: 2015-10-22
     * @param pam
     * @return
     * @throws Exception:
     */
    int insertPurAttachMapping(PurAttachMapping pam) throws Exception;

    /**
     * @description:根据sheetno删除映射表数据
     * @author: 890162
     * @createDate: 2015-10-22
     * @param sheetno
     * @throws Exception:
     * @return:
     */
    int deletePurAttachMappingBySheetno(String sheetno) throws Exception;

    /**
     * @description:根据sheetno返回attachids
     * @author: 890162
     * @createDate: 2015-10-22
     * @param sheetno
     * @return:
     */
    List<PurAttachMapping> queryPurAttachMappingBysheetno(String sheetno);

    /**
     * @description:查询采购合同附件
     * @author: 890162
     * @createDate: 2015-10-22
     * @param sheetno
     * @return List<Map<String, Object>>
     * @throws Exception :
     */
    List<Map<String, Object>> queryPurAttach(String sheetno) throws Exception;
}
