package com.timss.inventory.service;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvBin;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvBinService.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvBinService {

    /**
     * @description: 通过物资类别查询所有对应的货柜
     * @author: 890166
     * @createDate: 2014-7-17
     * @param userInfo
     * @param categoryId
     * @return
     * @throws Exception :
     */
    Map<String, Object> queryBinByCategory(UserInfoScope userInfo, String categoryId) throws Exception;

    /**
     * 查询站点内所有的货柜
     * 
     * @description:
     * @author: 890147
     * @createDate: 2014-8-11
     * @param page
     * @return:
     */
    List<InvBin> queryBinListBySiteId(Page<InvBin> page) throws Exception;

    /**
     * 通过货柜id查询货柜详情
     * 
     * @description:
     * @author: 890147
     * @createDate: 2014-8-11
     * @param binid
     * @return:
     */
    InvBin queryBinDetail(String binid) throws Exception;

    /**
     * 新建货柜
     * 
     * @description:
     * @author: 890147
     * @createDate: 2014-8-11
     * @param invBinInfo
     * @return:
     */
    int insertBinInfo(InvBin invBinInfo) throws Exception;

    /**
     * 更新货柜
     * 
     * @description:
     * @author: 890147
     * @createDate: 2014-8-11
     * @param invBinInfo
     * @return:
     */
    int updateBinInfo(InvBin invBinInfo) throws Exception;

    /**
     * 删除货柜
     * 
     * @description:
     * @author: 890147
     * @createDate: 2014-8-11
     * @param binid
     * @return:
     */
    int deleteBin(String binid) throws Exception;

    /**
     * 根据仓库id和货柜名查询货柜，用于保证货柜名在仓库内是唯一的
     * 
     * @description:
     * @author: 890147
     * @createDate: 2014-8-11
     * @param binname
     * @param warehouseid
     * @return:
     */
    InvBin queryBinByNameAndWarehouseId(String binname, String warehouseid) throws Exception;
    
    /**
     * 根据仓库id查询货柜
     * 
     * @description:
     * @author: 890191
     * @createDate: 2016-04-18
     * @param warehouseid
     * @return:
     */
    List<InvBin> queryBinByWarehouseId(String warehouseid) throws Exception;

}
