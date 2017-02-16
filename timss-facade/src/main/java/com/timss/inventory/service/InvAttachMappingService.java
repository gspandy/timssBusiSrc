package com.timss.inventory.service;

import com.timss.inventory.bean.InvAttachMapping;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvAttachMappingService.java
 * @author: 890166
 * @createDate: 2014-11-18
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvAttachMappingService {

    /**
     * @description:插入映射附件数据
     * @author: 890166
     * @createDate: 2014-11-18
     * @param iam
     * @return
     * @throws Exception:
     */
    int insertInvAttachMapping(InvAttachMapping iam) throws Exception;

}
