package com.timss.asset.dao;

import java.util.List;
import java.util.Map;

import com.timss.asset.bean.CmdbPubCiBean;
import com.timss.asset.bean.CmdbRelationBean;
import com.yudean.itc.annotation.DynamicFormBind;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 配置项
 * @description: {desc}
 * @company: gdyd
 * @className: CmdbCiDao.java
 * @author: fengzt
 * @createDate: 2015年8月24日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface CmdbCiDao {

    /**
     * 
     * @description:查询列表
     * @author: fengzt
     * @createDate: 2015年8月24日
     * @param paramsPage
     * @return:List<CmdbPubCiBean>
     */
    List<CmdbPubCiBean> queryCmdbPubCiBySiteId(Page<CmdbPubCiBean> paramsPage);
    
    /**
     * @description:根据ci类型和子类型检索ci
     * @author: yaoyn
     * @createDate: 2015年11月25日
     * @param paramsPage
     * @return:Page<CmdbPubCiBean>
     */
    List<CmdbPubCiBean> queryCmdbPubCiByCiTypeAndSubType(Page<CmdbPubCiBean> paramsPage);
    
    /**
     * 
     * @description:插入配置项
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param bean
     * @return:int
     */
    int insertCmdbPubCiBean(CmdbPubCiBean bean);

    /**
     * 
     * @description:更新配置项
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param bean
     * @return:int
     */
    @DynamicFormBind(masterKey="id")
    int updateCmdbPubCiBean(CmdbPubCiBean bean);

    /**
     * 
     * @description:通过ID查询配置项值
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param id
     * @return:CmdbPubCiBean
     */
    CmdbPubCiBean queryCmdbPubCiById(String id);

    /**
     * 
     * @description:通过ID删除配置项
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param bean
     * @return:int
     */
    int deleteCmdbPubCiById(CmdbPubCiBean bean);

    /**
     * 
     * @description:查询上下关联关系
     * @author: fengzt
     * @createDate: 2015年8月27日
     * @param paramsPage
     * @return:List<CmdbRelationBean>
     */
    List<CmdbRelationBean> queryCiRelation(Page<CmdbRelationBean> paramsPage);

    /**
     * 
     * @description:通过CI的名称模糊搜索(前20条)
     * @author: fengzt
     * @createDate: 2015年8月28日
     * @param map
     * @return: List<CmdbPubCiBean>
     */
    List<CmdbPubCiBean> queryHintCmdbRelationByName(Map<String, Object> map);

    /**
     * 
     * @description:新建--关联关系
     * @author: fengzt
     * @createDate: 2015年8月28日
     * @param bean
     * @return:int
     */
    int insertCmdbRelationBean(CmdbRelationBean bean);

    /**
     * 
     * @description:更新关联关系
     * @author: fengzt
     * @createDate: 2015年8月28日
     * @param bean
     * @return:int
     */
    int updateCmdbRelationBean(CmdbRelationBean bean);

    /**
     * 
     * @description:是否已经存在关联关系
     * @author: fengzt
     * @createDate: 2015年8月28日
     * @param map
     * @return:int
     */
    int queryCmdbRelationIsExist(Map<String, Object> map);

    /**
     * 
     * @description:更新关联关系
     * @author: fengzt
     * @createDate: 2015年8月28日
     * @param bean
     * @return:int
     */
    int deleteCmdbRelationById(CmdbRelationBean bean);

    /**
     * 
     * @description:通过ID查询上下关联关系
     * @author: fengzt
     * @createDate: 2015年8月31日
     * @param id
     * @return:CmdbRelationBean
     */
    CmdbRelationBean queryCmdbRelationById(String id);

    /**
     * 
     * @description:检查CI名称在同一个站点下是否存在
     * @author: fengzt
     * @createDate: 2015年8月31日
     * @param map
     * @return:List<CmdbPubCiBean>
     */
    List<CmdbPubCiBean> queryCheckCmdbCiName(Map<String, Object> map);

}
