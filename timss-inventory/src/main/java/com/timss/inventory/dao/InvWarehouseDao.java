package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.inventory.bean.InvWarehouse;
import com.timss.inventory.vo.TreeBean;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvWarehouseDao.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvWarehouseDao {

    /**
     * @description:查询仓库信息
     * @author: 890166
     * @createDate: 2014-7-11
     * @param userInfo
     * @param iWarehouse
     * @return:
     */
    List<InvWarehouse> queryWarehouse(Page<?> page);

    /**
     * @description:查询子节点数据
     * @author: 890166
     * @createDate: 2014-7-11
     * @param parentId
     * @return
     * @throws Exception:
     */
    List<TreeBean> queryWarehouseNode(String parentId);

    /**
     * @description:查询子节点数据 By id
     * @author: 890166
     * @createDate: 2014-7-11
     * @param id
     * @return
     * @throws Exception:
     */
    List<TreeBean> queryWarehouseNodeById(Map<String, Object> map);

    /**
     * @description:通过仓库名称找到它所在的id
     * @author: 890166
     * @createDate: 2014-7-12
     * @param name
     * @return
     * @throws Exception:
     */
    List<String> queryWarehouseIdByName(Map<String, Object> map);

    /**
     * @description:用categoryid查询仓库所在
     * @author: 890166
     * @createDate: 2014-7-22
     * @param map
     * @return:
     */
    List<InvWarehouse> queryWarehouseByCategoryId(Map<String, Object> map);

    /**
     * @description:用id查询仓库
     * @author: 890166
     * @createDate: 2014-7-22
     * @param warehouseid
     * @return:
     */
    List<InvWarehouse> queryWarehouseById(String warehouseid);

    /**
     * @description:分页查询给定站点id的仓库列表
     * @author: 890147
     * @createDate: 2014-8-4
     * @param page
     * @return:
     */
    List<InvWarehouse> queryWarehouseListBySiteId(Page<InvWarehouse> page);

    /**
     * @description:根据给定id和站点id查询仓库
     * @author: 890147
     * @createDate: 2014-8-4
     * @param id
     * @param siteId
     * @return:
     */
    InvWarehouse queryWarehouseDetail(@Param("id") String id, @Param("siteId") String siteId);

    /**
     * @description:插入仓库
     * @author: 890147
     * @createDate: 2014-8-4
     * @param warehouseInfo
     * @return:
     */
    int insertWarehouseInfo(InvWarehouse warehouseInfo);

    /**
     * @description:更新仓库
     * @author: 890147
     * @createDate: 2014-8-4
     * @param warehouseInfo
     * @return:
     */
    int updateWarehouseInfo(InvWarehouse warehouseInfo);

    /**
     * @description:更新仓库状态
     * @author: 890147
     * @createDate: 2014-8-4
     * @param id
     * @param siteId
     * @param state
     * @return:
     */
    int updateWarehouseState(@Param("id") String id, @Param("siteId") String siteId, @Param("state") String state);

    /**
     * @description:删除仓库
     * @author: 890147
     * @createDate: 2014-8-7
     * @param id
     * @param siteId
     * @return:
     */
    int deleteWarehouse(@Param("id") String id, @Param("siteId") String siteId);

    /**
     * @description:查询站点内仓库编码是否已存在，用于确定编码是否唯一
     * @author: 890147
     * @createDate: 2014-8-7
     * @param siteId
     * @param code
     * @return:
     */
    int isWarehouseCodeExist(@Param("siteId") String siteId, @Param("code") String code);

    /**
     * @description:使用站点id和代码查询仓库
     * @author: 890147
     * @createDate: 2014-8-10
     * @param siteId
     * @param code
     * @return:
     */
    InvWarehouse queryWarehouseByCodeAndSiteId(@Param("siteId") String siteId, @Param("code") String code);

    /**
     * @description:查询站点的所有仓库列表
     * @author: 890147
     * @createDate: 2014-8-12
     * @param siteId
     * @return:
     */
    List<InvWarehouse> queryAllWarehouseBySiteId(@Param("siteId") String siteId);
}
