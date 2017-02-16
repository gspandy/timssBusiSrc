package com.timss.inventory.service;

import java.util.List;

import com.timss.inventory.bean.InvMatTranRec;


/**
 * @title: InvMatTranRecService
 * @description: 新流水表接口 跨包调用方法只能用service
 * @company: gdyd
 * @className: InvMatTranRecService.java
 * @author: 890162
 * @createDate: 2016-6-2
 * @updateUser: 890162
 * @version: 1.0
 */
public interface InvMatTranRecService {

    /**
     * @Title:queryInvMatTranRecByImtdId
     * @Description:根据imtdid查询新流水记录
     * @param imtdid
     * @return List<InvMatTranRec>
     * @throws
     */
    List<InvMatTranRec> queryInvMatTranRecByImtdId(String imtdid);
    
}
