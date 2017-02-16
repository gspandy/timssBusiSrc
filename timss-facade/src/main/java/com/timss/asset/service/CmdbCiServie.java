package com.timss.asset.service;

import java.util.List;

import com.timss.asset.bean.CmdbPubCiBean;
import com.timss.asset.bean.CmdbRelationBean;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 配置项CI
 * @description: {desc}
 * @company: gdyd
 * @className: CmdbCiServie.java
 * @author: fengzt
 * @createDate: 2015年8月24日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface CmdbCiServie {

    /**
     * 
     * @description:根据站点查询或高级查询列表
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param paramsPage
     * @return:Page<CmdbPubCiBean>
     */
    Page<CmdbPubCiBean> queryCmdbPubCiBySiteId(Page<CmdbPubCiBean> paramsPage);

    /**
     * @description:根据ci类型和子类型检索ci
     * @author: yaoyn
     * @createDate: 2015年11月25日
     * @param paramsPage
     * @return:Page<CmdbPubCiBean>
     */
    Page<CmdbPubCiBean> queryCmdbPubCiByCiTypeAndSubType(Page<CmdbPubCiBean> paramsPage);
    
    /**
     * 
     * @description:插入或更新 配置项
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param bean
     * @return:int
     */
    int insertOrUpdateCmdbPubCi(CmdbPubCiBean bean);

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
     * @description:更新配置项参数
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param bean
     * @return:int
     */
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
     * @description:通过ID删除配置项值
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param id
     * @return:int
     */
    int deleteCmdbPubCiById(String id);

    /**
     * 
     * @description:查询上下关联关系
     * @author: fengzt
     * @createDate: 2015年8月27日
     * @param paramsPage
     * @return:Page<CmdbRelationBean>
     */
    Page<CmdbRelationBean> queryCiRelation(Page<CmdbRelationBean> paramsPage);
    
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
    List<CmdbPubCiBean> queryHintCmdbRelationByName(String kw, String ciId, String ciType);

    /**
     * 
     * @description:关联关系--新增
     * @author: fengzt
     * @createDate: 2015年8月28日
     * @param bean
     * @return:int
     */
    int insertOrUpdateCmdbRelation(CmdbRelationBean bean);
    
    /**
     * 
     * @description:更新--关联关系
     * @author: fengzt
     * @createDate: 2015年8月28日
     * @param bean
     * @return:int
     */
    int updateCmdbRelationBean(CmdbRelationBean bean);

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
     * @description:删除关联关系
     * @author: fengzt
     * @createDate: 2015年8月28日
     * @param id
     * @return:int
     */
    int deleteCmdbRelationById(String id);

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
     * @param name
     * @param id
     * @return:List<CmdbPubCiBean>
     */
    List<CmdbPubCiBean> queryCheckCmdbCiName(String name, String id);

}
