package com.timss.inventory.service.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.inventory.bean.InvMatMapping;
import com.timss.inventory.dao.InvMatMappingDao;
import com.timss.inventory.service.InvMatMappingService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatMappingServiceImpl.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("InvMatMappingServiceImpl")
public class InvMatMappingServiceImpl implements InvMatMappingService {

    /**
     * 注入Dao
     */
    @Autowired
    private InvMatMappingDao invMatMappingDao;

    /**
     * 根据外部信息查询映射表中是否存在记录
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-16
     * @param outterId
     * @param type
     * @return
     * @throws Exception:
     */
    public List<InvMatMapping> queryInvMatMappingByOutterInfo(String outterId, String type) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "outterId", outterId );
        map.put( "type", type );
        return invMatMappingDao.queryInvMatMappingByOutterInfo( map );
    }
}
