package com.timss.asset.service;

import java.util.List;
import java.util.Map;

import com.timss.asset.bean.CmdbParamsBean;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 用户可配置参数项
 * @description: {desc}
 * @company: gdyd
 * @className: CmdbParamsServie.java
 * @author: fengzt
 * @createDate: 2015年8月11日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface CmdbParamsServie {

    /**
     * 
     * @description:根据站点查询或高级查询列表
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param paramsPage
     * @return:Page<CmdbParamsBean>
     */
    Page<CmdbParamsBean> queryCmdbParamsBySiteId(Page<CmdbParamsBean> paramsPage);

    /**
     * 
     * @description:插入或更新用户可配置参数项
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param bean
     * @return:int
     */
    int insertOrUpdateCmdbParams(CmdbParamsBean bean);

    /**
     * 
     * @description:插入用户可配置参数项
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param bean
     * @return:int
     */
    int insertCmdbParamsBean(CmdbParamsBean bean);

    /**
     * 
     * @description:更新用户可配置项参数
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param bean
     * @return:int
     */
    int updateCmdbParamsBean(CmdbParamsBean bean);

    /**
     * 
     * @description:通过类型和参数项查询
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param ciType
     * @param paramType
     * @param sort --排序
     * @param paramVal 
     * @return:List<CmdbParamsBean>
     */
    List<CmdbParamsBean> queryOderTypeByType(String ciType, String paramType, String sort, String paramVal);

    /**
     * 
     * @description:通过ID查询参数项值
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param id
     * @return:CmdbParamsBean
     */
    CmdbParamsBean queryCmdbParamsById(String id);

    /**
     * 
     * @description:通过ID删除参数项值
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param id
     * @return:int
     */
    int deleteCmdbParamsById(String id);

    /**
     * 
     * @description:查询参数iHint
     * @author: fengzt
     * @createDate: 2015年8月26日
     * @param kw
     * @param paramType
     * @return:List<Map<String, Object>>
     */
    List<Map<String, Object>> searchCmdbParamsHint(String kw, String paramType);

}
