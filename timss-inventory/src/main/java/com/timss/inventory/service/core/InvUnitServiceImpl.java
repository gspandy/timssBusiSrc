package com.timss.inventory.service.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvUnit;
import com.timss.inventory.dao.InvUnitDao;
import com.timss.inventory.service.InvUnitService;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvUnitServiceImpl.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("InvUnitServiceImpl")
public class InvUnitServiceImpl implements InvUnitService {

    /**
     * 注入Dao
     */
    @Autowired
    private InvUnitDao invUnitDao;

    /**
     * log4j输出
     */
    private static final Logger LOG = Logger.getLogger( InvUnitServiceImpl.class );

    @Override
    public List<InvUnit> queryUnitListBySiteId(Page<InvUnit> page) throws Exception {
        List<InvUnit> list = invUnitDao.queryUnitListBySiteId( page );
        page.setResults( list );
        return list;
    }

    @Override
    public InvUnit queryUnitDetail(String id, String siteId) throws Exception {
        InvUnit wh = invUnitDao.queryUnitDetail( id, siteId );
        return wh;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertUnitInfo(@CUDTarget InvUnit unitInfo) throws Exception {
        Integer result = invUnitDao.insertUnitInfo( unitInfo );
        LOG.debug( "新建计量单位id->" + unitInfo.getUnitid() + " name->" + unitInfo.getUnitname() );
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateUnitInfo(@CUDTarget InvUnit unitInfo) throws Exception {
        Integer result = invUnitDao.updateUnitInfo( unitInfo );
        LOG.debug( "更新计量单位id->" + unitInfo.getUnitid() + " name->" + unitInfo.getUnitname() );
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateUnitState(String id, String siteId, String state) throws Exception {
        Integer result = invUnitDao.updateUnitState( id, state, siteId );
        LOG.debug( "更新计量单位状态id->" + id + " active->" + state );
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteUnit(String id, String siteId) throws Exception {
        Integer result = invUnitDao.deleteUnit( id, siteId );
        LOG.debug( "删除计量单位id->" + id );
        return result;
    }

    @Override
    public InvUnit queryUnitByCodeAndSiteId(String siteId, String code) {
        InvUnit result = invUnitDao.queryUnitByCodeAndSiteId( siteId, code );
        return result;
    }
}
