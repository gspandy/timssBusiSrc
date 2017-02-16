package com.timss.inventory.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.inventory.bean.InvItemBaseField;
import com.timss.inventory.bean.InvRealTimeData;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvRealTimeDataDao.java
 * @author: 890151
 * @createDate: 2016-5-5
 * @updateUser: 890151
 * @version: 1.0
 */
public interface InvRealTimeDataDao {

    /**
     * @description: 根据联合主键查询物资库存实时数据
     * @author: 890151
     * @createDate: 2016-5-5
     * @return
     * @throws Exception :
     */
    InvRealTimeData queryInvRealTimeDataByCompositeKey ( @Param ( "itemId" ) String itemId ,
	    @Param ( "invCateId" ) String invCateId , @Param ( "siteId" ) String siteId );

    /**
     * @description: 通过脚本执行获取InvRealTimeData数据
     * @author: yuanzh
     * @createDate: 2016-5-9
     * @param execSql 执行脚本
     * @return:
     */
    InvRealTimeData queryInvRealTimeDataByScript ( @Param ( "execSql" ) String execSql );

    /**
     * @description:插入库存实时数据
     * @author: 890151
     * @createDate: 2016-5-5
     * @param imad
     * @return:
     */
    int insertInvRealTimeData ( InvRealTimeData invRealTimeData );

    /**
     * @description: 更新库存实时数据
     * @author: 890151
     * @createDate: 2016-5-5
     * @param irtd
     * @return:
     */
    int updateInvRealTimeData ( InvRealTimeData invRealTimeData );

    /**
     * @description:查询当前站点下所有物资条目
     * @author: yuanzh
     * @createDate: 2016-5-10
     * @param siteId 站点id
     * @param itemCodes 物资范围，若为空，则查询站点下所有物资
     * @return:
     */
    List< InvItemBaseField > queryInvItemBaseField4AllItem ( @Param ( "siteId" ) String siteId, @Param ( "itemCodes" ) String itemCodes );

}
