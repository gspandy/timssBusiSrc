package com.timss.inventory.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvMatSnapshot;
import com.timss.inventory.dao.InvMatSnapshotDao;
import com.timss.inventory.service.InvMatSnapshotService;
import com.timss.inventory.vo.InvMatSnapshotDetailVO;
import com.timss.inventory.vo.InvMatSnapshotVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatSnapshotServiceImpl.java
 * @author: 890166
 * @createDate: 2014-11-20
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("InvMatSnapshotServiceImpl")
public class InvMatSnapshotServiceImpl implements InvMatSnapshotService {

    @Autowired
    private InvMatSnapshotDao invMatSnapshotDao;

    /**
     * log4j输出
     */
    private static final Logger LOG = Logger.getLogger( InvMatSnapshotServiceImpl.class );

    /**
     * @description:保存当前库存信息
     * @author: 890166
     * @createDate: 2014-11-20
     * @return
     * @throws Exception:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean saveAsSnapshot(UserInfoScope userInfo, Map<String, Object> paramMap) throws Exception {
        boolean flag = true;
        String remark = paramMap.get( "remark" ) == null ? "" : String.valueOf( paramMap.get( "remark" ) );
        String type = paramMap.get( "type" ) == null ? "" : String.valueOf( paramMap.get( "type" ) );
        String userId = userInfo.getUserId();
        String siteId = userInfo.getSiteId();

        try {
            if ( !"".equals( remark ) ) {
                InvMatSnapshot ims = new InvMatSnapshot();
                ims.setOpertype( type );
                ims.setRemark( remark );
                ims.setSiteid( siteId );
                ims.setCreateuser( userId );
                ims.setModifyuser( userId );
                ims.setCreatedate( new Date() );
                ims.setModifydate( new Date() );

                int counter = invMatSnapshotDao.insertInvMatSnapshot( ims );
                if ( counter > 0 ) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put( "userId", userId );
                    map.put( "siteId", siteId );
                    map.put( "imsid", ims.getImsid() );
                    invMatSnapshotDao.insertInvMatSnapshotDetail( map );
                }
            }
        } catch (Exception e) {
            LOG.debug( "---------InvMatSnapshotServiceImpl 中的 saveAsSnapshot 方法抛出异常---------：" + e.getMessage() );
            flag = false;
        }
        return flag;
    }

    /**
     * @description: 库存快照主单查询
     * @author: 890166
     * @createDate: 2014-11-24
     * @param userInfo
     * @param imsv
     * @return
     * @throws Exception:
     */
    @Override
    public Page<InvMatSnapshotVO> queryMatSnapshotList(UserInfoScope userInfo, InvMatSnapshotVO imsv) throws Exception {
        UserInfoScope scope = userInfo;
        Page<InvMatSnapshotVO> page = scope.getPage();
        page.setParameter( "siteId", userInfo.getSiteId() );

        String sort = String.valueOf( scope.getParam( "sort" ) == null ? "" : scope.getParam( "sort" ) );
        String order = String.valueOf( scope.getParam( "order" ) == null ? "" : scope.getParam( "order" ) );
        if ( !"".equals( sort ) && !"".equals( order ) ) {
            page.setSortKey( sort );
            page.setSortOrder( order );
        } else {
            page.setSortKey( "createdate" );
            page.setSortOrder( "desc" );
        }

        if ( null != imsv ) {
            page.setParameter( "createdate", imsv.getCreatedate() );
            page.setParameter( "opertypeName", imsv.getOpertypeName() );
            page.setParameter( "createuserName", imsv.getCreateuserName() );
            page.setParameter( "remark", imsv.getRemark() );
        }
        List<InvMatSnapshotVO> ret = invMatSnapshotDao.queryMatSnapshotList( page );
        page.setResults( ret );
        return page;
    }

    /**
     * @description: 库存快照主单查询
     * @author: 890166
     * @createDate: 2014-11-24
     * @param userInfo
     * @param imsv
     * @return
     * @throws Exception:
     */
    @Override
    public Page<InvMatSnapshotDetailVO> quickMatSnapshotSearch(UserInfoScope userInfo, InvMatSnapshotDetailVO imsdv,
            String quickSearch, String imsid) throws Exception {
        quickSearch = quickSearch == null ? "" : quickSearch;

        UserInfoScope scope = userInfo;
        Page<InvMatSnapshotDetailVO> page = scope.getPage();
        page.setParameter( "siteId", userInfo.getSiteId() );
        page.setParameter( "imsid", imsid );

        String sort = String.valueOf( scope.getParam( "sort" ) == null ? "" : scope.getParam( "sort" ) );
        String order = String.valueOf( scope.getParam( "order" ) == null ? "" : scope.getParam( "order" ) );
        if ( !"".equals( sort ) && !"".equals( order ) ) {
            page.setSortKey( sort );
            page.setSortOrder( order );
        } else {
            page.setSortKey( "invcatename,itemcode" );
            page.setSortOrder( "asc" );
        }

        if ( !"".equals( quickSearch ) ) {
            page.setParameter( "quickSearch", quickSearch );
        }

        if ( null != imsdv ) {
            page.setParameter( "itemcode", imsdv.getItemcode() );
            page.setParameter( "itemname", imsdv.getItemname() );
            page.setParameter( "invcatename", imsdv.getInvcatename() );
            page.setParameter( "cusmodel", imsdv.getCusmodel() );
            page.setParameter( "warehousename", imsdv.getWarehousename() );
            page.setParameter( "unitname", imsdv.getUnitname() );
            page.setParameter( "stockQty", imsdv.getStockQty() );
            page.setParameter( "stockQtyNow", imsdv.getStockQtyNow() );
            page.setParameter( "price", imsdv.getPrice() );
            page.setParameter( "priceNow", imsdv.getPriceNow() );
            page.setParameter( "ishis", imsdv.getIshis() );
        }

        List<InvMatSnapshotDetailVO> ret = invMatSnapshotDao.quickMatSnapshotSearch( page );
        page.setResults( ret );
        return page;
    }

    /**
     * @description:查询子单数据
     * @author: 890166
     * @createDate: 2014-11-24
     * @param search
     * @return
     * @throws Exception:
     */
    @Override
    public Page<InvMatSnapshotDetailVO> queryMatSnapshotDetailList(UserInfoScope userInfo,
            InvMatSnapshotDetailVO imsdv, String imsid) throws Exception {
        UserInfoScope scope = userInfo;
        Page<InvMatSnapshotDetailVO> page = scope.getPage();

        page.setParameter( "siteId", userInfo.getSiteId() );
        page.setParameter( "imsid", imsid );
        if ( null != imsdv ) {
            page.setParameter( "itemcode", imsdv.getItemcode() );
            page.setParameter( "itemname", imsdv.getItemname() );
            page.setParameter( "invcatename", imsdv.getInvcatename() );
            page.setParameter( "cusmodel", imsdv.getCusmodel() );
            page.setParameter( "warehousename", imsdv.getWarehousename() );
            page.setParameter( "unitname", imsdv.getUnitname() );
            page.setParameter( "stockQty", imsdv.getStockQty() );
            page.setParameter( "stockQtyNow", imsdv.getStockQtyNow() );
            page.setParameter( "price", imsdv.getPrice() );
            page.setParameter( "priceNow", imsdv.getPriceNow() );
        }
        List<InvMatSnapshotDetailVO> ret = invMatSnapshotDao.queryMatSnapshotDetailList( page );
        page.setResults( ret );
        return page;
    }
}
