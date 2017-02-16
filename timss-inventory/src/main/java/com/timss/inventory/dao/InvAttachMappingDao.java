package com.timss.inventory.dao;

import java.util.List;

import com.timss.inventory.bean.InvAttachMapping;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvAttachMappingDao.java
 * @author: 890166
 * @createDate: 2014-11-18
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvAttachMappingDao {

    /**
     * @description:插入映射附件数据
     * @author: 890166
     * @createDate: 2014-11-18
     * @param iam
     * @return:
     */
    int insertInvAttachMapping(InvAttachMapping iam);

    /**
     * @description:根据sheetid删除映射表数据
     * @author: 890166
     * @createDate: 2014-11-18
     * @param sheetId
     * @return:
     */
    int deleteInvAttachMappingBySheetId(String sheetId);

    /**
     * @description:根据
     * @author: 890166
     * @createDate: 2014-11-18
     * @param istid
     * @return:
     */
    List<InvAttachMapping> queryInvAttachMappingBysheetId(String sheetId);
}
