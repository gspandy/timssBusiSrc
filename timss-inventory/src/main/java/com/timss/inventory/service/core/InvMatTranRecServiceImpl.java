package com.timss.inventory.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.inventory.bean.InvMatTranRec;
import com.timss.inventory.dao.InvMatTranRecDao;
import com.timss.inventory.service.InvMatTranRecService;


/**
 * @title: InvMatTranRecServiceImpl
 * @description: 新流水记录查询接口
 * @company: gdyd
 * @className: InvMatTranRecServiceImpl.java
 * @author: 890162
 * @createDate: 2016-6-2
 * @updateUser: 890162
 * @version: 1.0
 */
@Service ( "InvMatTranRecServiceImpl" )
public class InvMatTranRecServiceImpl implements InvMatTranRecService {
    @Autowired
    private InvMatTranRecDao invMatTranRecDao;
    @Override
    public List<InvMatTranRec> queryInvMatTranRecByImtdId(String imtdid) {
        List<InvMatTranRec> imtrList = null;
        imtrList = invMatTranRecDao.queryTranRecByimtdid( imtdid );
        return imtrList;
    }

}
