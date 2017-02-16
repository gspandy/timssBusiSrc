package com.timss.asset.dao;

import java.util.List;
import java.util.Map;

import com.timss.asset.bean.CmdbParamsBean;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 用户可配置参数项
 * @description: {desc}
 * @company: gdyd
 * @className: CmdbParamsDao.java
 * @author: fengzt
 * @createDate: 2015年8月11日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface CmdbParamsDao {

    /**
     * 
     * @description:查询列表或者高级查询列表
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param paramsPage
     * @return:List<CmdbParamsBean>
     */
    List<CmdbParamsBean> queryCmdbParamsBySiteId(Page<CmdbParamsBean> paramsPage);

    /**
     * 
     * @description:插入参数项
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param bean
     * @return:int
     */
    int insertCmdbParamsBean(CmdbParamsBean bean);

    /**
     * 
     * @description:更新参数项
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param bean
     * @return:int
     */
    int updateCmdbParamsBean(CmdbParamsBean bean);

    /**
     * 
     * @description:通过CI类型、参数项枚举 查询
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param map
     * @return: List<CmdbParamsBean>
     */
    List<CmdbParamsBean> queryOderTypeByType(Map<String, Object> map);

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
     * @description:通过ID删除参数项
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param bean
     * @return:int
     */
    int deleteCmdbParamsById(CmdbParamsBean bean);

    /**
     * 
     * @description:查询参数iHint
     * @author: fengzt
     * @createDate: 2015年8月26日
     * @param params
     * @return:List<Map<String, Object>>
     */
    List<Map<String, Object>> searchCmdbParamsHint(Map<String, Object> params);

}
