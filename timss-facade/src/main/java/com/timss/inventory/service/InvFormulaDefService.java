package com.timss.inventory.service;

import com.timss.inventory.bean.InvItemBaseField;

/**
 * @title: 实时库存计算表字段存放公式接口
 * @description: 实时库存计算表字段存放公式接口
 * @company: gdyd
 * @className: InvFormulaDefService.java
 * @author: yuanzh
 * @createDate: 2016-4-20
 * @updateUser: yuanzh
 * @version: 1.0
 */
public interface InvFormulaDefService {

    /**
     * 通过查询公式表得到各字段对应的公式
     * 
     * @param type 查询字段类型
     * @param siteId 站点id
     * @return
     */
    String queryAndReturnFormulaByType ( String type , String siteId );

    /**
     * 将脚本中需要替换的地方用真实的物资数据替换掉
     * 
     * @param caluItem 物资数据
     * @param formulaSql 查询脚本
     * @return
     */
    String setRealData2TheScript ( InvItemBaseField caluItem , String formulaSql );
}
