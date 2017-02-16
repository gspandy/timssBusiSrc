package com.timss.inventory.service.core;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvOrganizeData;
import com.timss.inventory.dao.InvOrganizeDataDao;
import com.timss.inventory.service.InvOrganizeDataService;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvOrganizeDataServiceImpl.java
 * @author: 890166
 * @createDate: 2015-5-29
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("InvOrganizeDataServiceImpl")
public class InvOrganizeDataServiceImpl implements InvOrganizeDataService {

    @Autowired
    private InvOrganizeDataDao invOrganizeDataDao;

    /**
     * @description: 插入InvOrganizeData数据
     * @author: 890166
     * @createDate: 2015-6-1
     * @param iod
     * @return:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertInvOrganizeData(InvOrganizeData iod) {
        return invOrganizeDataDao.insertInvOrganizeData( iod );
    }

    /**
     * @description: 删除InvOrganizeData数据
     * @author: 890166
     * @createDate: 2015-6-1
     * @param iod
     * @return:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteInvOrganizeData(InvOrganizeData iod) {
        return invOrganizeDataDao.deleteInvOrganizeData( iod );
    }

    /**
     * @description: 查询InvOrganizeData信息
     * @author: 890166
     * @createDate: 2015-6-1
     * @param iod
     * @return:
     */
    @Override
    public Page<InvOrganizeData> queryInvOrganizeData(UserInfoScope userInfo, InvOrganizeData iod) {
        UserInfoScope scope = userInfo;
        Page<InvOrganizeData> page = scope.getPage();
        page.setParameter( "siteId", userInfo.getSiteId() );
        page.setParameter( "createUser", userInfo.getUserId() );

        if ( null != iod ) {
            page.setParameter( "warehouseName", iod.getWarehouseName() );
            page.setParameter( "containerName", iod.getContainerName() );
            page.setParameter( "categroyO", iod.getCategroyO() );
            page.setParameter( "categroyS", iod.getCategroyS() );
            page.setParameter( "itemName", iod.getItemName() );
            page.setParameter( "cusmodel", iod.getCusmodel() );
            page.setParameter( "qty", iod.getQty() );
            page.setParameter( "unitName", iod.getUnitName() );
            page.setParameter( "price", iod.getPrice() );
            page.setParameter( "status", iod.getStatus() );
            page.setParameter( "remark", iod.getRemark() );
        }
        List<InvOrganizeData> ret = invOrganizeDataDao.queryInvOrganizeData( page );
        page.setResults( ret );
        return page;
    }

    /**
     * @description: 触发调用oracle的存储过程整理数据
     * @author: 890166
     * @createDate: 2015-6-2
     * @param paramMap
     * @return:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean callOrganizeDataInit(Map<String, Object> paramMap) {
        invOrganizeDataDao.callOrganizeDataInit( paramMap );
        return Boolean.valueOf( String.valueOf( paramMap.get( "flag" ) ) );
    }
}
