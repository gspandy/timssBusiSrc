package com.timss.inventory.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvAttachMapping;
import com.timss.inventory.dao.InvAttachMappingDao;
import com.timss.inventory.service.InvAttachMappingService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvAttachMappingServiceImpl.java
 * @author: 890166
 * @createDate: 2014-11-18
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvAttachMappingServiceImpl implements InvAttachMappingService {

    /**
     * 注入Dao
     */
    @Autowired
    private InvAttachMappingDao invAttachMappingDao;

    /**
     * @description:插入映射附件数据
     * @author: 890166
     * @createDate: 2014-11-18
     * @param iam
     * @return:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertInvAttachMapping(InvAttachMapping iam) throws Exception {
        return invAttachMappingDao.insertInvAttachMapping( iam );
    }

}
