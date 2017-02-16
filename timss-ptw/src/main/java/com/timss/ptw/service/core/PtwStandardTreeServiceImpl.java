package com.timss.ptw.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.AssetBean;
import com.timss.asset.service.AssetInfoService;
import com.timss.ptw.dao.PtwStandardTreeDao;
import com.timss.ptw.service.PtwStandardTreeService;
import com.timss.ptw.vo.PtwStdTreeVo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 标准树
 * @description: {desc}
 * @company: gdyd
 * @className: PtwStandardTreeServiceImpl.java
 * @author: fengzt
 * @createDate: 2014年12月25日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("ptwStandardTreeService")
public class PtwStandardTreeServiceImpl implements PtwStandardTreeService {

    private Logger log = Logger.getLogger( PtwStandardTreeServiceImpl.class );
    
    @Autowired
    private PtwStandardTreeDao ptwStandardTreeDao;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    @Qualifier("assetInfoServiceImpl")
    private AssetInfoService assetInfoService;
    
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
        	log.error(e);
        }
        return userInfoScope;
    }
    
    /**
     * 
     * @description:插入或者更新标准树
     * @author: fengzt
     * @createDate: 2014年12月25日
     * @param ptwStdTreeVo
     * @return: Map<String, Object>
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> insertOrUpdateStandard(PtwStdTreeVo ptwStdTreeVo) {
        
        Map<String, Object> map = new HashMap<String, Object>();
        String siteId = getUserInfoScope().getSiteId();
        String userId = getUserInfoScope().getUserId();
        
        ptwStdTreeVo.setSiteId( siteId );
        
        if( ptwStdTreeVo.getId() > 0 ){
            ptwStdTreeVo.setModifydate( new Date() );
            ptwStdTreeVo.setModifyuser( userId );
            
            int updateCount = ptwStandardTreeDao.updateStandardTree( ptwStdTreeVo );
            map.put( "count", updateCount );
            log.info( ptwStdTreeVo.getId() + "-----更新标准树条数：" + updateCount  );
        }else{
            ptwStdTreeVo.setCreateuser( userId );
            ptwStdTreeVo.setCreatedate( new Date() );
            ptwStdTreeVo.setYxbz( 1 );
            
            int count = ptwStandardTreeDao.insertStandardTree( ptwStdTreeVo );
            log.info(  ptwStdTreeVo.getId() + "-----插入标准树条数：" + count  );
            map.put( "count", count );
        }
        
        ptwStdTreeVo = queryStandardTreeById( ptwStdTreeVo.getId() );
        
        map.put( "ptwStdTreeVo", ptwStdTreeVo );
        
        return map;
    }

    /**
     * 
     * @description:通过ID查询PtwStdTree
     * @author: fengzt
     * @createDate: 2014年12月25日
     * @param id
     * @return:PtwStdTreeVo
     */
    @Override
    public PtwStdTreeVo queryStandardTreeById(int id) {
        PtwStdTreeVo ptwStdTreeVo = ptwStandardTreeDao.queryStandardTreeById( id );
        if(  ptwStdTreeVo .getParentId() > 0 ){
            PtwStdTreeVo temp = ptwStandardTreeDao.queryStandardTreeById( ptwStdTreeVo.getParentId() );
            ptwStdTreeVo.setParentName( temp.getName() );
            
        }
        
        if( StringUtils.isNotBlank( ptwStdTreeVo.getEquipmentId() )){
            try {
                AssetBean assetBean = assetInfoService.queryAssetDetail( ptwStdTreeVo.getEquipmentId() );
                ptwStdTreeVo.setEquipmentName( assetBean.getAssetName() );
                ptwStdTreeVo.setEquipmentCode(assetBean.getAssetCode());
            } catch (Exception e) {
            	log.error(e);
            }
        }
        return ptwStdTreeVo;
    }

    /**
     * 
     * @description:查找根节点
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @return:PtwStdTreeVo
     */
    @Override
    public PtwStdTreeVo queryPtwStdRootIdBySite() {
        String siteId = getUserInfoScope().getSiteId();
        int parentId = 0;
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "siteId", siteId );
        params.put( "parentId", parentId );
        
        List<PtwStdTreeVo> result = queryPtwStdByParentId( params );
        if( result != null && result.size() > 0 ){
            return result.get( 0 );
        }
        return null;
    }

    /**
     * 
     * @description:通过parentId查找子节点
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @param params
     * @return:List<PtwStdTreeVo>
     */
    @Override
    public List<PtwStdTreeVo> queryPtwStdByParentId(Map<String, Object> params) {
        
        List<PtwStdTreeVo> result = ptwStandardTreeDao.queryPtwStdByParentId( params );
        //设置父节点名字
        for( PtwStdTreeVo vo : result ){
            //判断图标
            Map<String, Object> childMap = new HashMap<String, Object>();
            childMap.put( "parentId", vo.getId() );
            List<PtwStdTreeVo> childs = ptwStandardTreeDao.queryPtwStdByParentId( childMap );
            if( childs != null && childs.size() > 0 ){
                vo.setIconCls( "tree-folder" );
            }else{
                vo.setIconCls( "tree-file" );
            }
            
            if( vo.getParentId() != 0 ){
                PtwStdTreeVo temp = ptwStandardTreeDao.queryStandardTreeById( vo.getParentId() );
                vo.setParentName( temp.getName() );
            }
            if( StringUtils.isNotBlank( vo.getEquipmentId() )){
                try {
                    AssetBean assetBean = assetInfoService.queryAssetDetail( vo.getEquipmentId() );
                    vo.setEquipmentName( assetBean.getAssetName() );
                } catch (Exception e) {
                	log.error(e);
                }
            }
        }
        
        return result;
    }

    /**
     * 
     * @description:通过名字/编码查询标准树
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @param kw
     * @return:List<Map<String, Object>>
     */
    @Override
    public List<Map<String, Object>> queryPtwStdSearch(String kw) {
        String siteId = getUserInfoScope().getSiteId();
        Map<String, Object> map = new HashMap<String, Object>();
        if( StringUtils.isNotBlank( kw ) ){
            kw = kw.toUpperCase();
        }
        map.put( "keyWord", kw );
        map.put( "siteId", siteId );
        return ptwStandardTreeDao.queryPtwStdSearch( map );
    }

    /**
     * 
     * @description:用于搜索框的选中
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @param id
     * @return:List<String>
     */
    @Override
    public List<String> searchHintPtwStdParentIds(int id) {
        return ptwStandardTreeDao.searchHintPtwStdParentIds( id );
    }

    /**
     * 
     * @description:拖拽标准树节点
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @param id
     * @param parentId
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int updateDropStdTreeNode(int id, int parentId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "id", id );
        map.put( "parentId", parentId );
        
        return ptwStandardTreeDao.updateDropStdTreeNode( map );
    }

    /**
     * 
     * @description:通过ID删除本节点及其下属子节点
     * @author: fengzt
     * @createDate: 2014年12月29日
     * @param id
     * @return:int
     */
    @Override
    public int deleteStandardTree(int id) {
        return ptwStandardTreeDao.deleteStandardTree( id );
    }

}
