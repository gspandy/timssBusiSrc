package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.inventory.bean.InvBin;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvBinDao.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvBinDao {

    /**
     * @description:通过物资类别查询所有对应的货柜
     * @author: 890166
     * @createDate: 2014-7-17
     * @param map
     * @return:
     */
    List<InvBin> queryBinByCategory(Map<String, Object> map);

    /**
     * @description:查询站点内所有的货柜
     * @author: 890147
     * @createDate: 2014-8-11
     * @param page
     * @return:
     */
    List<InvBin> queryBinListBySiteId(Page<InvBin> page);

    /**
     * @description:通过货柜id查询货柜详情
     * @author: 890147
     * @createDate: 2014-8-11
     * @param binid
     * @return:
     */
    InvBin queryBinDetail(@Param("binid") String binid);

    /**
     * @description:新建货柜
     * @author: 890147
     * @createDate: 2014-8-11
     * @param invBinInfo
     * @return:
     */
    int insertBinInfo(InvBin invBinInfo);

    /**
     * @description:更新货柜
     * @author: 890147
     * @createDate: 2014-8-11
     * @param invBinInfo
     * @return:
     */
    int updateBinInfo(InvBin invBinInfo);

    /**
     * @description:删除货柜
     * @author: 890147
     * @createDate: 2014-8-11
     * @param binid
     * @return:
     */
    int deleteBin(@Param("binid") String binid);

    /**
     * @description:根据仓库id和货柜名查询货柜，用于保证货柜名在仓库内是唯一的
     * @author: 890147
     * @createDate: 2014-8-11
     * @param binname
     * @param warehouseid
     * @return:
     */
    InvBin queryBinByNameAndWarehouseId(@Param("binname") String binname, @Param("warehouseid") String warehouseid);
    
    /**
     * @description:根据仓库id查询货柜
     * @author: 890191
     * @createDate: 2016-04-18
     * @param warehouseid
     * @return:
     */
    List<InvBin> queryBinByWarehouseId(@Param("warehouseid") String warehouseid);
}
