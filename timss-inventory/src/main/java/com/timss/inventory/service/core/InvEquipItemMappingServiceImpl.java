package com.timss.inventory.service.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvEquipItemMapping;
import com.timss.inventory.dao.InvEquipItemMappingDao;
import com.timss.inventory.service.InvEquipItemMappingService;
import com.timss.inventory.vo.SpareBean;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvEquipItemMappingServiceImpl.java
 * @author: 890166
 * @createDate: 2014-7-23
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("InvEquipItemMappingServiceImpl")
public class InvEquipItemMappingServiceImpl implements InvEquipItemMappingService {

    /**
     * 注入Dao
     */
    @Autowired
    InvEquipItemMappingDao invEquipItemMappingDao;

    /**
     * 删除设备关联
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteMappingInfoByItemInfo(Map<String, Object> map) throws Exception {
        return invEquipItemMappingDao.deleteMappingInfoByItemInfo( map );
    }

    /**
     * @description:插入设备关联
     * @author: 890166
     * @createDate: 2014-7-23
     * @param map
     * @return:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertMappingInfoByItemInfo(Map<String, Object> map) throws Exception {
        String itemId = map.get( "itemId" ) == null ? "" : String.valueOf( map.get( "itemId" ) );
        String siteId = map.get( "siteId" ) == null ? "" : String.valueOf( map.get( "siteId" ) );
        String equipId = map.get( "equipId" ) == null ? "" : String.valueOf( map.get( "equipId" ) );
        String equipName = map.get( "equipName" ) == null ? "" : String.valueOf( map.get( "equipName" ) );

        InvEquipItemMapping ieim = new InvEquipItemMapping();
        ieim.setItemid( itemId );
        ieim.setSiteId( siteId );
        ieim.setEquipid( equipId );
        ieim.setEquipname( equipName );

        return invEquipItemMappingDao.insertMappingInfoByItemInfo( ieim );
    }

    /**
     * @description:查询关联物资
     * @author: 890166
     * @createDate: 2014-7-23
     * @param assetId
     * @return
     * @throws Exception:
     */
    @Override
    public Page<SpareBean> getSpareByAssetId(UserInfoScope userInfo) throws Exception {
        UserInfoScope scope = userInfo;
        Page<SpareBean> page = scope.getPage();
        page.setParameter( "siteId", userInfo.getSiteId() );
        page.setParameter( "assetId", userInfo.getParam( "assetId" ) );

        List<SpareBean> ret = invEquipItemMappingDao.getSpareByAssetId( page );
        page.setResults( ret );
        return page;
    }

    /**
     * @description:查询绑定设备信息
     * @author: 890166
     * @createDate: 2014-7-16
     * @param userInfo
     * @param itemId
     * @return
     * @throws Exception:
     */
    @Override
    public List<Map<String, Object>> queryEquipInfo(@Operator UserInfoScope userInfo, @LogParam("itemId") String itemId)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", userInfo.getSiteId() );
        map.put( "itemId", itemId );
        return invEquipItemMappingDao.queryEquipInfo( map );
    }
}
