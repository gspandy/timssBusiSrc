package com.timss.purchase.dao;

import java.util.List;

import com.timss.purchase.bean.PurAttachMapping;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurAttachMappingDao.java
 * @author: 890162
 * @createDate: 2015-10-22
 * @updateUser: 890162
 * @version: 1.0
 */
public interface PurAttachMappingDao {

    /**
     * @description:插入映射附件数据
     * @author: 890162
     * @createDate: 2014-11-18
     * @param pam
     * @return:
     */
    int insertPurAttachMapping(PurAttachMapping pam);

    /**
     * @description:根据sheetid删除映射表数据
     * @author: 890162
     * @createDate: 2014-11-18
     * @param sheetno
     * @return:
     */
    int deletePurAttachMappingBySheetno(String sheetno);

    /**
     * @description:根据sheetno返回AttachIds
     * @author: 890162
     * @createDate: 2014-11-18
     * @param istid
     * @return:
     */
    List<PurAttachMapping> queryPurAttachMappingBysheetno(String sheetno);
}
