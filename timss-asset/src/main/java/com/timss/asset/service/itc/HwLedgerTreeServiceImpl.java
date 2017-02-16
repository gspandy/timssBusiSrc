package com.timss.asset.service.itc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.HwLedgerBean;
import com.timss.asset.dao.HwLedgerDao;
import com.timss.asset.service.HwLedgerTreeService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 硬件台账树
 * @description: {desc}
 * @company: gdyd
 * @className: HwLedgerTreeServiceImpl.java
 * @author: fengzt
 * @createDate: 2014年12月12日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("hwLedgerTreeService")
public class HwLedgerTreeServiceImpl implements HwLedgerTreeService {
    private Logger log = Logger.getLogger( HwLedgerTreeServiceImpl.class );
    
    @Autowired
    private HwLedgerDao hwLedgerDao;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    /**
     * 
     * @description:拿到登录用户信息
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    private UserInfoScope getUserInfoScope(){
        UserInfoScope userInfoScope = null;
        try {
            userInfoScope = itcMvcService.getUserInfoScopeDatas();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        return userInfoScope;
    }
    
    /**
     * 
     * @description:通过站点查找根节点
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:String
     */
    @Override
    public String queryHwLedgerRootIdBySite() {
        String siteId = getUserInfoScope().getSiteId();
        
        return hwLedgerDao.queryHwLedgerRootIdBySite( siteId );
    }



    /**
     * 
     * @description:通过id找到子节点
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param id
     * @return:List<HwLedgerBean>
     */
    @Override
    public List<HwLedgerBean> queryHwLedgerChildren(String id) {
        return hwLedgerDao.queryHwLedgerChildren( id );
    }

    /**
     * 
     * @description:用于搜索框的查询
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param kw
     * @return:List<Map<String, Object>>
     */
    @Override
    public List<Map<String, Object>> searchHwLedgerHint(String kw) {
        String siteId = getUserInfoScope().getSiteId();
        Map<String, Object> map = new HashMap<String, Object>();
        if( StringUtils.isNotBlank( kw ) ){
            kw = kw.toUpperCase();
        }
        map.put( "keyWord", kw );
        map.put( "siteId", siteId );
        
        return hwLedgerDao.searchHwLedgerHint( map );
    }

    /**
     * 
     * @description:搜索子节点
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param id
     * @return:List<String>
     */
    @Override
    public List<String> searchHintHwLedgerParentIds(String id) {

        return hwLedgerDao.searchHintHwLedgerParentIds( id );
    }

    /**
     * 
     * @description:查找根节点信息
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return: Map<String, Object>
     */
    @Override
    public List<Map<String, Object>> queryHwLedgerByRoot() {
        String siteId = getUserInfoScope().getSiteId();
        
        return hwLedgerDao.queryHwLedgerByRoot( siteId );
    }

    /**
     * 
     * @description:拖动硬件台账树节点
     * @author: fengzt
     * @createDate: 2014年12月15日
     * @param id
     * @param parentId
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int updateDropHwlTreeNode(String id, String parentId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "id", id );
        map.put( "parentId", parentId );
        
        return hwLedgerDao.updateDropHwlTreeNode( map );
    }

}
