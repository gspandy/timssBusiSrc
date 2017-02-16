package com.timss.asset.service.itc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.CmdbParamsBean;
import com.timss.asset.dao.CmdbParamsDao;
import com.timss.asset.service.CmdbParamsServie;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 用户可配置参数项
 * @description: {desc}
 * @company: gdyd
 * @className: CmdbParamsServieImpl.java
 * @author: fengzt
 * @createDate: 2015年8月11日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("cmdbParamsServie")
public class CmdbParamsServieImpl implements CmdbParamsServie {

    @Autowired
    private CmdbParamsDao cmdbParamsDao;
    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * @description:根据站点查询或高级查询列表
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param paramsPage
     * @return:Page<CmdbParamsBean>
     */
    @Override
    public Page<CmdbParamsBean> queryCmdbParamsBySiteId(Page<CmdbParamsBean> paramsPage) {
        paramsPage.setParameter( "isDelete", "0" );
        
        List<CmdbParamsBean> list = cmdbParamsDao.queryCmdbParamsBySiteId( paramsPage );
        paramsPage.setResults( list );
        return paramsPage;
    }

    /**
     * 
     * @description:插入或更新用户可配置参数项
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param bean
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int insertOrUpdateCmdbParams(CmdbParamsBean bean) {
        String id = bean.getId();
        //记录更新条数
        int count = 0;
        if( StringUtils.isNotBlank( id ) ){
            count = updateCmdbParamsBean( bean );
        }else{
            count = insertCmdbParamsBean( bean );
        }
        
        return count;
    }

    /**
     * 
     * @description:查询参数iHint
     * @author: fengzt
     * @createDate: 
     * @param kw
     * @param paramType
     * @return: List<Map<String, Object>> 
     */
    @Override
    public List<Map<String, Object>> searchCmdbParamsHint(String kw, String paramType) {
        Map<String, Object> params = new HashMap<String, Object>();
        
        if( StringUtils.isNotBlank( kw ) ){
            kw = kw.toUpperCase();
        }
        params.put( "keyWord", kw );
        params.put( "paramType", paramType );
        
        List<Map<String, Object>> result = cmdbParamsDao.searchCmdbParamsHint( params );
        
        return result;
    }
    
    /**
     * 
     * @description:更新用户可配置项参数
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param bean
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int updateCmdbParamsBean(CmdbParamsBean bean) {
        bean = setCmdbParamsForUpdate( bean );
        
        return cmdbParamsDao.updateCmdbParamsBean( bean );
    }

    /**
     * 
     * @description:设置更新所需要的字段
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param bean
     * @return:
     */
    private CmdbParamsBean setCmdbParamsForUpdate(CmdbParamsBean bean) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        
        bean.setModifydate( new Date() );
        bean.setModifyuser( userInfoScope.getUserId() );
        bean.setModifyUserName( userInfoScope.getUserName() );
        
        return bean;
    }

    /**
     * 
     * @description:插入用户可配置参数项
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param bean
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int insertCmdbParamsBean(CmdbParamsBean bean) {
        bean = setCmdbParamsForInsert( bean );
        
        return cmdbParamsDao.insertCmdbParamsBean( bean );
    }

    /**
     * 
     * @description:加上插入需要的参数
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param bean
     * @return:
     */
    private CmdbParamsBean setCmdbParamsForInsert(CmdbParamsBean bean) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        
        bean.setId( UUIDGenerator.getUUID() );
        bean.setIsDelete( "0" );
        bean.setDeptid( userInfoScope.getOrgId() );
        bean.setSiteid( userInfoScope.getSiteId() );
        
        bean.setCreateuser( userInfoScope.getUserId() );
        bean.setCreateUserName( userInfoScope.getUserName() );
        bean.setCreatedate( new Date() );
        
        return bean;
    }

    /**
     * 
     * @description:通过类型和参数项查询
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param ciType
     * @param paramType
     * @param sort --排序
     * @return:List<CmdbParamsBean>
     */
    @Override
    public List<CmdbParamsBean> queryOderTypeByType(String ciType, String paramType, String sort, String paramVal ) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "ciType", ciType );
        map.put( "paramType", paramType );
        map.put( "sort", sort );
        map.put( "paramVal", paramVal );
        map.put( "isDelete", "0" );
        
        return cmdbParamsDao.queryOderTypeByType( map );
    }

    /**
     * 
     * @description:通过ID查询参数项值
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param id
     * @return:CmdbParamsBean
     */
    @Override
    public CmdbParamsBean queryCmdbParamsById(String id) {
        return cmdbParamsDao.queryCmdbParamsById( id );
    }

    /**
     * 
     * @description:通过ID删除参数项值
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param id
     * @return:int
     */
    @Override
    public int deleteCmdbParamsById(String id) {
        CmdbParamsBean bean = new CmdbParamsBean();
        bean.setId( id );
        bean.setIsDelete( "1" );
        bean = setCmdbParamsForUpdate( bean );
        
        return cmdbParamsDao.deleteCmdbParamsById( bean );
    }

}
