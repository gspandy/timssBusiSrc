package com.timss.inventory.service.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvBin;
import com.timss.inventory.dao.InvBinDao;
import com.timss.inventory.service.InvBinService;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvBinServiceImpl.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("InvBinServiceImpl")
public class InvBinServiceImpl implements InvBinService {

    /**
     * 注入Dao
     */
    @Autowired
    private InvBinDao invBinDao;

    /**
     * log4j输出
     */
    private static final Logger LOG = Logger.getLogger( InvBinServiceImpl.class );

    /**
     * @description: 通过物资类别查询所有对应的货柜
     * @author: 890166
     * @createDate: 2014-7-17
     * @param userInfo
     * @param categoryId
     * @return
     * @throws Exception:
     */
    @Override
    public Map<String, Object> queryBinByCategory(@Operator UserInfoScope userInfo,
            @LogParam("categoryId") String categoryId) throws Exception {
        Map<String, Object> reMap = new HashMap<String, Object>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", userInfo.getSiteId() );
        map.put( "categoryId", categoryId );
        List<InvBin> ibList = invBinDao.queryBinByCategory( map );
        String reData = null;
        String warehouse = null;
        InvBin ib = null;
        if ( null != ibList && !ibList.isEmpty() ) {
            StringBuilder sb = new StringBuilder( "{" );
            for ( int i = 0; i < ibList.size(); i++ ) {
                ib = ibList.get( i );
                sb.append( "\"" ).append( ib.getBinid() ).append( "\":\"" ).append( ib.getBinname() ).append( "\"," );
                if ( i == 0 ) {
                    warehouse = ib.getWarehouseid();
                }
            }
            reData = sb.toString();
            reData = reData.substring( 0, reData.length() - 1 );
            reData += "}";
        }
        reMap.put( "defBin", reData );
        reMap.put( "warehouseid", warehouse );
        return reMap;
    }

    @Override
    public List<InvBin> queryBinListBySiteId(Page<InvBin> page) throws Exception {
        List<InvBin> list = invBinDao.queryBinListBySiteId( page );
        page.setResults( list );
        return list;
    }

    @Override
    public InvBin queryBinDetail(String id) throws Exception {
        InvBin wh = invBinDao.queryBinDetail( id );
        return wh;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertBinInfo(@CUDTarget InvBin binInfo) throws Exception {
        Integer result = invBinDao.insertBinInfo( binInfo );
        LOG.debug( "新建货柜id->" + binInfo.getBinid() + " name->" + binInfo.getBinname() );
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateBinInfo(@CUDTarget InvBin binInfo) throws Exception {
        Integer result = invBinDao.updateBinInfo( binInfo );
        LOG.debug( "更新货柜id->" + binInfo.getBinid() + " name->" + binInfo.getBinname() );
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteBin(String id) throws Exception {
        Integer result = invBinDao.deleteBin( id );
        LOG.debug( "删除货柜id->" + id );
        return result;
    }

    @Override
    public InvBin queryBinByNameAndWarehouseId(String binname, String warehouseid) {
        InvBin result = invBinDao.queryBinByNameAndWarehouseId( binname, warehouseid );
        return result;
    }

	@Override
	public List<InvBin> queryBinByWarehouseId(String warehouseid) throws Exception {
		List<InvBin> ibList = invBinDao.queryBinByWarehouseId(warehouseid);
		return ibList;
	}
}
