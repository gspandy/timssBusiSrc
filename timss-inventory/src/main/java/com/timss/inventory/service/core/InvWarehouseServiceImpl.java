package com.timss.inventory.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvWarehouse;
import com.timss.inventory.dao.InvWarehouseDao;
import com.timss.inventory.service.InvWarehouseService;
import com.timss.inventory.vo.TreeBean;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvWarehouseServiceImpl.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("InvWarehouseServiceImpl")
public class InvWarehouseServiceImpl implements InvWarehouseService {

    /**
     * 注入Dao
     */
    @Autowired
    private InvWarehouseDao invWarehouseDao;

    /**
     * log4j输出
     */
    private static final Logger LOG = Logger.getLogger( InvWarehouseServiceImpl.class );

    /**
     * @description:查询仓库信息
     * @author: 890166
     * @createDate: 2014-7-11
     * @param userInfo
     * @param iWarehouse
     * @return:
     */
    @Override
    public Page<InvWarehouse> queryWarehouse(@Operator UserInfoScope userInfo,
            @LogParam("iWarehouse") InvWarehouse iWarehouse) throws Exception {
        UserInfoScope scope = userInfo;
        Page<InvWarehouse> page = scope.getPage();
        page.setParameter( "siteId", userInfo.getSiteId() );

        if ( null != iWarehouse ) {
            page.setParameter( "warehouseid", iWarehouse.getWarehouseid() );
            page.setParameter( "warehousename", iWarehouse.getWarehousename() );
            page.setParameter( "descriptions", iWarehouse.getDescriptions() );
            page.setParameter( "active", iWarehouse.getActive() );
        }
        List<InvWarehouse> ret = invWarehouseDao.queryWarehouse( page );
        page.setResults( ret );
        return page;
    }

    /**
     * @description:查询子节点数据
     * @author: 890166
     * @createDate: 2014-7-11
     * @param parentId
     * @return
     * @throws Exception:
     */
    @Override
    public List<HashMap<String, Object>> queryWarehouseNode(String parentId) throws Exception {
        List<HashMap<String, Object>> ret = new ArrayList<HashMap<String, Object>>();
        List<TreeBean> tbList = invWarehouseDao.queryWarehouseNode( parentId );
        if ( null != tbList && !tbList.isEmpty() ) {
            for ( TreeBean tb : tbList ) {
                HashMap<String, Object> pNode = new HashMap<String, Object>();
                pNode.put( "id", tb.getId() );
                pNode.put( "text", tb.getText() );
                pNode.put( "state", "open" );
                pNode.put( "type", tb.getType() );
                ret.add( pNode );
            }
        }
        return ret;
    }

    /**
     * @description:查询子节点数据 By id
     * @author: 890166
     * @createDate: 2014-7-11
     * @param id
     * @return
     * @throws Exception:
     */
    @Override
    public List<HashMap<String, Object>> queryWarehouseNodeById(Map<String, Object> map) throws Exception {
        List<HashMap<String, Object>> ret = new ArrayList<HashMap<String, Object>>();
        List<TreeBean> tbList = invWarehouseDao.queryWarehouseNodeById( map );
        if ( null != tbList && !tbList.isEmpty() ) {
            for ( TreeBean tb : tbList ) {
                HashMap<String, Object> pNode = new HashMap<String, Object>();
                pNode.put( "id", tb.getId() );
                pNode.put( "text", tb.getText() );
                pNode.put( "state", "open" );
                pNode.put( "type", tb.getType() );
                pNode.put( "children", "" );
                ret.add( pNode );
            }
        }
        return ret;
    }

    /**
     * @description:通过仓库名称找到它所在的id
     * @author: 890166
     * @createDate: 2014-7-12
     * @param name
     * @return
     * @throws Exception:
     */
    @Override
    public List<String> queryWarehouseIdByName(@Operator UserInfoScope userInfoScope, @LogParam("name") String name)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", userInfoScope.getSiteId() );
        map.put( "name", name );
        return invWarehouseDao.queryWarehouseIdByName( map );
    }

    /**
     * @description:用categoryid查询仓库所在
     * @author: 890166
     * @createDate: 2014-7-22
     * @param userInfoScope
     * @param categoryId
     * @return
     * @throws Exception:
     */
    @Override
    public List<InvWarehouse> queryWarehouseByCategoryId(@Operator UserInfoScope userInfo,
            @LogParam("categoryId") String categoryId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", userInfo.getSiteId() );
        map.put( "categoryId", categoryId );
        return invWarehouseDao.queryWarehouseByCategoryId( map );
    }

    /**
     * @description:用id查询仓库
     * @author: 890166
     * @createDate: 2014-7-22
     * @param warehouseid
     * @return
     * @throws Exception:
     */
    @Override
    public List<InvWarehouse> queryWarehouseById(String warehouseid) throws Exception {
        return invWarehouseDao.queryWarehouseById( warehouseid );
    }

    /**
     * @description:查询当前站点的所有仓库
     * @author: 890166
     * @createDate: 2014-7-24
     * @return
     * @throws Exception:
     */
    @Override
    public List<HashMap<String, String>> queryWarehouse(UserInfoScope userInfo) throws Exception {
        List<HashMap<String, String>> hmList = new ArrayList<HashMap<String, String>>();
        UserInfoScope scope = userInfo;
        Page<InvWarehouse> page = scope.getPage();
        page.setParameter( "siteId", userInfo.getSiteId() );
        page.setParameter( "active", "Y" );

        List<InvWarehouse> iwList = invWarehouseDao.queryWarehouse( page );
        for ( InvWarehouse iw : iwList ) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put( "key", iw.getWarehouseid() );
            map.put( "value", iw.getWarehousename() );
            hmList.add( map );
        }
        return hmList;
    }

    @Override
    public List<InvWarehouse> queryWarehouseListBySiteId(Page<InvWarehouse> page) throws Exception {
        List<InvWarehouse> list = invWarehouseDao.queryWarehouseListBySiteId( page );
        page.setResults( list );
        return list;
    }

    @Override
    public InvWarehouse queryWarehouseDetail(String id, String siteId) throws Exception {
        InvWarehouse wh = invWarehouseDao.queryWarehouseDetail( id, siteId );
        return wh;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertWarehouseInfo(@CUDTarget InvWarehouse warehouseInfo) throws Exception {
        Integer result = invWarehouseDao.insertWarehouseInfo( warehouseInfo );
        LOG.debug( "新建仓库id->" + warehouseInfo.getWarehouseid() + " name->" + warehouseInfo.getWarehousename() );
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateWarehouseInfo(@CUDTarget InvWarehouse warehouseInfo) throws Exception {
        Integer result = invWarehouseDao.updateWarehouseInfo( warehouseInfo );
        LOG.debug( "更新仓库id->" + warehouseInfo.getWarehouseid() + " name->" + warehouseInfo.getWarehousename() );
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateWarehouseState(String id, String siteId, String state) throws Exception {
        Integer result = invWarehouseDao.updateWarehouseState( id, siteId, state );
        LOG.debug( "更新仓库状态id->" + id + " active->" + state );
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteWarehouse(String id, String siteId) throws Exception {
        Integer result = invWarehouseDao.deleteWarehouse( id, siteId );
        LOG.debug( "删除仓库id->" + id );
        return result;
    }

    @Override
    public int isWarehouseCodeExist(String siteId, String code) throws Exception {
        Integer result = invWarehouseDao.isWarehouseCodeExist( siteId, code );
        return result;
    }

    @Override
    public InvWarehouse queryWarehouseByCodeAndSiteId(String siteId, String code) {
        InvWarehouse result = invWarehouseDao.queryWarehouseByCodeAndSiteId( siteId, code );
        return result;
    }

    @Override
    public List<InvWarehouse> queryAllWarehouseBySiteId(String siteId) {
        List<InvWarehouse> list = invWarehouseDao.queryAllWarehouseBySiteId( siteId );
        return list;
    }
}
