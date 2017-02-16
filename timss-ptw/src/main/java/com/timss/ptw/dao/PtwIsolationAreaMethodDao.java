package com.timss.ptw.dao;

import java.util.List;

import com.timss.ptw.bean.PtwIsolationAreaMethod;
import com.timss.ptw.vo.IsMethodPointVo;

/**
 * 
 * @title: 隔离证对应的隔离方法明细
 * @description: {desc}
 * @company: gdyd
 * @className: PtwIsolationAreaMethodDao.java
 * @author: fengzt
 * @createDate: 2014年10月28日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface PtwIsolationAreaMethodDao {


    /**
     * 
     * @description:插入隔离证对应的隔离方法明细
     * @author: fengzt
     * @createDate: 2014年10月28日
     * @param ptwIsolationAreaMethod
     * @return:int
     */
    int insertPtwIsolationAreaMethod(PtwIsolationAreaMethod ptwIsolationAreaMethod);
    
    /**
     * 
     * @description:删除隔离证对应的隔离方法明细
     * @author: fengzt
     * @createDate: 2014年10月28日
     * @param id
     * @return:int
     */
    int deleteIslAreaMethodByIslAreaId(int id);

    /**
     * @description:通过隔离证ID查找列表
     * @author: fengzt
     * @createDate: 2014年10月28日
     * @param areaId
     * @return:List<IsMethodPointVo>
     */
    List<IsMethodPointVo> queryIsolationMethodList(String areaId);

}
