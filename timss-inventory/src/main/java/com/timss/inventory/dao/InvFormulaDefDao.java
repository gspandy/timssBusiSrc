package com.timss.inventory.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.inventory.bean.InvFormulaDef;

/**
 * @title: 计算实时库存数据DAO接口
 * @description: 计算实时库存数据DAO接口
 * @company: gdyd
 * @className: InvFormulaDefDao.java
 * @author: yuanzh
 * @createDate: 2016-5-9
 * @updateUser: yuanzh
 * @version: 1.0
 */
public interface InvFormulaDefDao {

    /**
     * @description: 通过实体类作为参数查询表中数据
     * @author: yuanzh
     * @createDate: 2016-5-9
     * @param ifd InvFormulaDef实体类
     * @return:
     */
    List< InvFormulaDef > queryInvFormulaDefByEntity ( @Param ( "InvFormulaDef" ) InvFormulaDef ifd );

}
