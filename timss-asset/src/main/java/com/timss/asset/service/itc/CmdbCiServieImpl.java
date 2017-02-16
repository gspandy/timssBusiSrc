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

import com.timss.asset.bean.CmdbPubCiBean;
import com.timss.asset.bean.CmdbRelationBean;
import com.timss.asset.dao.CmdbCiDao;
import com.timss.asset.service.CmdbCiServie;
import com.yudean.itc.code.LogType;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.dto.support.AppLog;
import com.yudean.itc.dto.support.ConstructLogVo;
import com.yudean.itc.manager.support.LogTempService;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 配置项CI
 * @description: {desc}
 * @company: gdyd
 * @className: CmdbCiServieImpl.java
 * @author: fengzt
 * @createDate: 2015年8月24日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("cmdbCiServie")
public class CmdbCiServieImpl implements CmdbCiServie {
    
    @Autowired
    private CmdbCiDao cmdbCiDao;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private LogTempService logTempService;

    /**
     * @description:根据站点查询或高级查询列表
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param paramsPage
     * @return:Page<CmdbPubCiBean>
     */
    @Override
    public Page<CmdbPubCiBean> queryCmdbPubCiBySiteId(Page<CmdbPubCiBean> paramsPage) {
        paramsPage.setParameter( "isDelete", "0" );
        
        List<CmdbPubCiBean> list = cmdbCiDao.queryCmdbPubCiBySiteId( paramsPage );
        paramsPage.setResults( list );
        return paramsPage;
    }

    /**
     * 
     * @description:插入或更新用户可配置配置项
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param bean
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int insertOrUpdateCmdbPubCi(CmdbPubCiBean bean) {
        String id = bean.getId();
        //记录更新条数
        int count = 0;
        if( StringUtils.isNotBlank( id ) ){
            count = updateCmdbPubCiBean( bean );
        }else{
            count = insertCmdbPubCiBean( bean );
        }
        
        return count;
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
    public int updateCmdbPubCiBean(CmdbPubCiBean bean) {
        //页面传入的备注，放在createuser 字段当临时存储
        String remark = bean.getCreateuser();
        
        bean = setCmdbPubCiForUpdate( bean );
        //插入之前值
        CmdbPubCiBean beforeBean = queryCmdbPubCiById( bean.getId() );
        setDiffBean( LogType.UPDATE.ordinal(), beforeBean, bean, remark );
        
        return cmdbCiDao.updateCmdbPubCiBean( bean );
    }

    /**
     * 
     * @description:设置更新所需要的字段
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param bean
     * @return:
     */
    private CmdbPubCiBean setCmdbPubCiForUpdate(CmdbPubCiBean bean) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        
        bean.setModifydate( new Date() );
        bean.setModifyuser( userInfoScope.getUserId() );
        bean.setModifyUserName( userInfoScope.getUserName() );
        
        return bean;
    }

    /**
     * 
     * @description:插入用户可配置配置项
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param bean
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int insertCmdbPubCiBean(CmdbPubCiBean bean) {
        bean = setCmdbPubCiForInsert( bean );
        //setDiffBean( LogType.CREATE.ordinal(), new CmdbPubCiBean(), bean, null);
        setCDBeanLog( bean.getId(), "新建", LogType.CREATE.ordinal());
        return cmdbCiDao.insertCmdbPubCiBean( bean );
    }

    /**
     * 
     * @description:CI bean 日志
     * @author: fengzt
     * @createDate: 2015年9月1日
     * @param logType
     * @param beforeBean
     * @param remark 
     * @param afterBean:
     */
    private void setDiffBean( int logType, CmdbPubCiBean beforeBean, CmdbPubCiBean afterBean, String remark ){
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        //插入log
        ConstructLogVo cLogVo = new ConstructLogVo();
        cLogVo.setKeyWord( "id" );
        cLogVo.setLogType( logType );
        cLogVo.setModule( "cmdb" );
        cLogVo.setUserId( userInfo.getUserId() );
        cLogVo.setSiteId( userInfo.getSiteId() );
        cLogVo.setExcludeFields( "deptid,siteid,createuser,createUserName,modifyUserName" );
        cLogVo.setPropertiesPath( "cmdbci-field-to-zh.properties" );
        cLogVo.setRemark( remark );
        logTempService.insertLogForDiffBean( cLogVo, beforeBean, afterBean );
    }
    /**
     * 
     * @description:CI 关联关系日志
     * @author: fengzt
     * @createDate: 2015年9月1日
     * @param logType
     * @param beforeBean
     * @param keyWord 
     * @param remark 
     * @param afterBean:
     */
    private void setRelationDiffBean( int logType, CmdbRelationBean beforeBean,
            CmdbRelationBean afterBean, String keyWord){
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        //插入log
        ConstructLogVo cLogVo = new ConstructLogVo();
        cLogVo.setKeyWord( keyWord );
        cLogVo.setLogType( logType );
        cLogVo.setModule( "cmdb" );
        cLogVo.setUserId( userInfo.getUserId() );
        cLogVo.setSiteId( userInfo.getSiteId() );
        cLogVo.setExcludeFields( "deptid,siteid,createuser,createUserName,modifyUserName" );
        cLogVo.setPropertiesPath( "cmdbrelation-field-to-zh.properties" );
        cLogVo.setRemark( "变更CI关联关系" );
        logTempService.insertLogForDiffBean( cLogVo, beforeBean, afterBean );
    }
    
    /**
     * 
     * @description:新建、删除 CI 日志
     * @author: fengzt
     * @createDate: 2015年9月7日
     * @param id 主键ID
     * @param desc 描述
     * @param logType @see LogType
     */
    private void setCDBeanLog( String id, String desc, int logType ){
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        //插入log
        AppLog appLog = new AppLog();
        appLog.setAttr1( userInfo.getSiteId() );
        appLog.setAttr2( "cmdb" );
        appLog.setAttr3( id );
        
        appLog.setCategoryId( logType );
        appLog.setDescription( desc );
        appLog.setOperator( userInfo.getUserId() );
        appLog.setOperationTime( new Date() );
        logTempService.insertLog( appLog );
    }
    
    /**
     * 
     * @description:加上插入需要的参数
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param bean
     * @return:
     */
    private CmdbPubCiBean setCmdbPubCiForInsert(CmdbPubCiBean bean) {
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
     * @description:通过ID查询配置项值
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param id
     * @return:CmdbPubCiBean
     */
    @Override
    public CmdbPubCiBean queryCmdbPubCiById(String id) {
        return cmdbCiDao.queryCmdbPubCiById( id );
    }

    /**
     * 
     * @description:通过ID删除配置项值
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param id
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int deleteCmdbPubCiById(String id) {
        CmdbPubCiBean bean = new CmdbPubCiBean();
        bean.setId( id );
        bean.setIsDelete( "1" );
        bean = setCmdbPubCiForUpdate( bean );
        //设置删除日志
        setCDBeanLog( id, "删除", LogType.DELETE.ordinal());
        return cmdbCiDao.deleteCmdbPubCiById( bean );
    }

    /**
     * 
     * @description:查询上下关联关系
     * @author: fengzt
     * @createDate: 2015年8月27日
     * @param paramsPage
     * @return:Page<CmdbRelationBean>
     */
    @Override
    public Page<CmdbRelationBean> queryCiRelation(Page<CmdbRelationBean> paramsPage) {
        paramsPage.setParameter( "isDelete", "0" );
        
        List<CmdbRelationBean> list = cmdbCiDao.queryCiRelation( paramsPage );
        paramsPage.setResults( list );
        return paramsPage;
    }
    
    /**
     * 
     * @description:通过CI的名称模糊搜索
     * @author: fengzt
     * @createDate: 2015年8月28日
     * @param kw
     * @param ciId
     * @param ciType 
     * @return:List<CmdbPubCiBean>
     */
    @Override
    public List<CmdbPubCiBean> queryHintCmdbRelationByName(String kw, String ciId, String ciType ) {
        Map<String, Object> map = new HashMap<String, Object>();
        if( StringUtils.isNotBlank( kw ) ){
            map.put( "kw", "%" + kw + "%" );
        }
        map.put( "ciId", ciId );
        map.put( "isDelete", "0" );
        map.put( "ciType", ciType );
        
        return cmdbCiDao.queryHintCmdbRelationByName( map );
    }

    /**
     * 
     * @description:关联关系--新增
     * @author: fengzt
     * @createDate: 2015年8月28日
     * @param bean
     * @return:int
     */
    @Override
    public int insertOrUpdateCmdbRelation(CmdbRelationBean bean) {
        String id = bean.getId();
        //记录更新条数
        int count = 0;
        //是否已经存在这个CI
        int existCount = queryCmdbRelationIsExist( bean.getUpCiId(), bean.getDownCiId() );
        if( existCount > 0 ){
            return 999;
        }
        
        if( StringUtils.isNotBlank( id ) ){
            count = updateCmdbRelationBean( bean );
        }else{
            count = insertCmdbRelationBean( bean );
        }
        
        return count;
    }

    /**
     * 
     * @description:是否已经存在CI间关联关系
     * @author: fengzt
     * @createDate: 2015年8月28日
     * @param upCiId
     * @param downCiId
     * @return:int
     */
    private int queryCmdbRelationIsExist(String upCiId, String downCiId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "upCiId", upCiId );
        map.put( "downCiId", downCiId );
        map.put( "isDelete", "0" );
        
        return cmdbCiDao.queryCmdbRelationIsExist( map );
    }

    /**
     * 
     * @description:新建--关联关系
     * @author: fengzt
     * @createDate: 2015年8月28日
     * @param bean
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int insertCmdbRelationBean(CmdbRelationBean bean) {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        
        bean.setId( UUIDGenerator.getUUID() );
        bean.setIsDelete( "0" );
        bean.setDeptid( userInfoScope.getOrgId() );
        bean.setSiteid( userInfoScope.getSiteId() );
        
        bean.setCreateuser( userInfoScope.getUserId() );
        bean.setCreateUserName( userInfoScope.getUserName() );
        bean.setCreatedate( new Date() );
        //拿到枚举列表
        List<AppEnum> emList = itcMvcService.getEnum( "CMDB_CI_TYPE" );
        String upCiTypeName = getCategoryName(emList, bean.getUpCiType() );
        String downCiTypeName = getCategoryName(emList, bean.getDownCiType() );
        //上联名称-上联类型-下联名称-下联类型
        String name = bean.getUpCiName() + "-" + upCiTypeName + "-" + bean.getDownCiName() + "-" + downCiTypeName;
        bean.setName( name );
      
        //上下关联都要加上日志
        setCDBeanLog( bean.getUpCiId(), "新建CI关联关系：" + name, LogType.CREATE.ordinal() );
        setCDBeanLog( bean.getDownCiId(), "新建CI关联关系：" + name, LogType.CREATE.ordinal() );
        
        return cmdbCiDao.insertCmdbRelationBean( bean );
    }
    
    /**
     * 
     * @description:code to name
     * @author: fengzt
     * @createDate: 2014年10月11日
     * @param flowCode
     * @return:String
     */
    private String getCategoryName(List<AppEnum> emList,String categoryId) {
        String categoryName = categoryId;
        if( StringUtils.isNotBlank( categoryId ) ){
            if( emList != null && emList.size() > 0 ){
                for( AppEnum appVo : emList ){
                    if( categoryName.equalsIgnoreCase( appVo.getCode() ) ){
                        categoryName =  appVo.getLabel();
                        break;
                    }
                }
            }
        }
        return categoryName;
    }

    /**
     * 
     * @description:更新--关联关系
     * @author: fengzt
     * @createDate: 2015年8月28日
     * @param bean
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int updateCmdbRelationBean(CmdbRelationBean bean) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        
        bean.setModifydate( new Date() );
        bean.setModifyuser( userInfoScope.getUserId() );
        bean.setModifyUserName( userInfoScope.getUserName() );
        
        //拿到枚举列表
        List<AppEnum> emList = itcMvcService.getEnum( "CMDB_CI_TYPE" );
        String upCiTypeName = getCategoryName(emList, bean.getUpCiType() );
        String downCiTypeName = getCategoryName(emList, bean.getDownCiType() );
        //上联名称-上联类型-下联名称-下联类型
        String name = bean.getUpCiName() + "-" + upCiTypeName + "-" + bean.getDownCiName() + "-" + downCiTypeName;
        bean.setName( name );
        
        //加入上下关联关系日志
        CmdbRelationBean beforeBean = queryCmdbRelationById( bean.getId() );
        setRelationDiffBean( LogType.UPDATE.ordinal(), beforeBean, bean, "upCiId" );
        setRelationDiffBean( LogType.UPDATE.ordinal(), beforeBean, bean, "downCiId" );
        
        return cmdbCiDao.updateCmdbRelationBean( bean );
    }

    /**
     * 
     * @description:删除关联关系
     * @author: fengzt
     * @createDate: 2015年8月28日
     * @param id
     * @return:int
     */
    @Override
    public int deleteCmdbRelationById(String id) {
        CmdbRelationBean bean = new CmdbRelationBean();
        bean.setId( id );
        bean.setIsDelete( "1" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        
        bean.setModifydate( new Date() );
        bean.setModifyuser( userInfoScope.getUserId() );
        bean.setModifyUserName( userInfoScope.getUserName() );
        
        return cmdbCiDao.deleteCmdbRelationById( bean );
    }

    /**
     * 
     * @description:通过ID查询上下关联关系
     * @author: fengzt
     * @createDate: 2015年8月31日
     * @param id
     * @return:CmdbRelationBean
     */
    @Override
    public CmdbRelationBean queryCmdbRelationById(String id) {
        return cmdbCiDao.queryCmdbRelationById( id );
    }

    /**
     * 
     * @description:检查CI名称在同一个站点下是否存在
     * @author: fengzt
     * @createDate: 2015年8月31日
     * @param name
     * @param id
     * @return:List<CmdbPubCiBean>
     */
    @Override
    public List<CmdbPubCiBean> queryCheckCmdbCiName(String name, String id) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", userInfoScope.getSiteId() );
        map.put( "name", name );
        map.put( "isDelete", "0" );
        if( StringUtils.isNotBlank( id ) ){
            map.put( "id", id );
        }
        
        return cmdbCiDao.queryCheckCmdbCiName( map );
    }

	@Override
	public Page<CmdbPubCiBean> queryCmdbPubCiByCiTypeAndSubType(
			Page<CmdbPubCiBean> paramsPage) {
		paramsPage.setParameter( "isDelete", "0" );
		
        List<CmdbPubCiBean> list = cmdbCiDao.queryCmdbPubCiByCiTypeAndSubType( paramsPage );
        paramsPage.setResults( list );
        return paramsPage;
	}

}
