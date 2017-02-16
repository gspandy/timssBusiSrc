package com.timss.inventory.service;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvItemBaseField;
import com.timss.inventory.bean.InvMatMapping;
import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.bean.InvMatTranRec;
import com.yudean.itc.annotation.CalulateItem;
import com.yudean.itc.annotation.CalulateItemEntry;

/**
 * @title: 实时计算库存数量接口
 * @description: 实时计算库存数量接口
 * @company: gdyd
 * @className: InvRealTimeDataService.java
 * @author: 890166
 * @createDate: 2016-4-20
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvRealTimeDataService {

    /**
     * @description:对外部调用封装接口（内部进行分发）
     * @author: yuanzh
     * @createDate: 2016-4-27
     * @param caluItemEntry
     * @param type:
     */
    void realTimeCaluByType ( List< InvItemBaseField > caluItemEntry , String type );

    /**
     * @description:出库算法，物资出库时更新流水表中字段
     * @author: yuanzh
     * @createDate: 2016-5-4
     * @updateUser: 890151
     * @updateDate: 2016-5-25
     * @param imtd:
     */
    Map< String , Object > realTimeCaluAndUpdateTran ( InvMatTranRec imtr, Boolean update );

    /**
     * @description:查询当前站点下所有物资条目
     * @author: yuanzh
     * @createDate: 2016-5-4
     * @return List< InvItemBaseField >
     */
    List< InvItemBaseField > queryInvItemBaseField4AllItem ( String siteId, String itemCodes );

    /**
     * @description:全局计算库存数据
     * @author: yuanzh
     * @createDate: 2016-5-4
     * @param siteId: 站点id
     */
    void caluSiteInvData ( String siteId, String itemCodes );

    /**
     * @description:根据旧的流水和映射关系插入新的流水和映射关系
     * @author: 890151
     * @createDate: 2016-6-2
     * @param imtd: 旧的流水
     * @param imm: 旧的映射关系
     * @param falg: 价格控制标识（flag=1批次价 flag=2实时价 flag=3自定义价格）
     */
    @CalulateItem ( caluType = "All" )
	Map<String, Object> insertNewRecAndMap(@CalulateItemEntry InvMatTranDetail imtd, InvMatMapping imm, String flag);

}
