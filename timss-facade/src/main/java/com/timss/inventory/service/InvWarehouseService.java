package com.timss.inventory.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvWarehouse;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvWarehouseService.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvWarehouseService {

    /**
     * 查询仓库信息
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-11
     * @param userInfo
     * @param iWarehouse
     * @return:
     */
    Page<InvWarehouse> queryWarehouse(UserInfoScope userInfo, InvWarehouse iWarehouse) throws Exception;

    /**
     * 查询子节点数据
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-11
     * @param parentId
     * @return
     * @throws Exception :
     */
    List<HashMap<String, Object>> queryWarehouseNode(String parentId) throws Exception;

    /**
     * 查询子节点数据 By id
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-11
     * @param id
     * @return
     * @throws Exception :
     */
    List<HashMap<String, Object>> queryWarehouseNodeById(Map<String, Object> map) throws Exception;

    /**
     * 通过仓库名称找到它所在的id
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-12
     * @param name
     * @return
     * @throws Exception :
     */
    List<String> queryWarehouseIdByName(UserInfoScope userInfoScope, String name) throws Exception;

    /**
     * 用categoryid查询仓库所在
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-22
     * @param userInfoScope
     * @param categoryId
     * @return
     * @throws Exception :
     */
    List<InvWarehouse> queryWarehouseByCategoryId(UserInfoScope userInfo, String categoryId) throws Exception;

    /**
     * 用id查询仓库
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-22
     * @param warehouseid
     * @return
     * @throws Exception :
     */
    List<InvWarehouse> queryWarehouseById(String warehouseid) throws Exception;

    /**
     * 查询当前站点的所有仓库
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-24
     * @return
     * @throws Exception :
     */
    List<HashMap<String, String>> queryWarehouse(UserInfoScope userInfo) throws Exception;

    /**
     * @description:分页查询给定站点id的仓库列表
     * @author: 890147
     * @createDate: 2014-8-4
     * @param page
     * @return:
     */
    List<InvWarehouse> queryWarehouseListBySiteId(Page<InvWarehouse> page) throws Exception;

    /**
     * @description:根据给定id和站点id查询仓库
     * @author: 890147
     * @createDate: 2014-8-4
     * @param id
     * @param siteId
     * @return:
     */
    InvWarehouse queryWarehouseDetail(String id, String siteId) throws Exception;

    /**
     * 插入仓库
     * 
     * @description:
     * @author: 890147
     * @createDate: 2014-8-4
     * @param warehouseInfo
     * @return:
     */
    int insertWarehouseInfo(InvWarehouse warehouseInfo) throws Exception;

    /**
     * 更新仓库
     * 
     * @description:
     * @author: 890147
     * @createDate: 2014-8-4
     * @param warehouseInfo
     * @return:
     */
    int updateWarehouseInfo(InvWarehouse warehouseInfo) throws Exception;

    /**
     * @description:更新仓库状态
     * @author: 890147
     * @createDate: 2014-8-4
     * @param id
     * @param siteId
     * @param state
     * @return:
     */
    int updateWarehouseState(String id, String siteId, String state) throws Exception;

    /**
     * @description:删除仓库
     * @author: 890147
     * @createDate: 2014-8-7
     * @param id
     * @param siteId
     * @return:
     */
    int deleteWarehouse(String id, String siteId) throws Exception;

    /**
     * @description:查询站点内仓库编码是否已存在，用于确定编码是否唯一
     * @author: 890147
     * @createDate: 2014-8-7
     * @param siteId
     * @param code
     * @return:
     */
    int isWarehouseCodeExist(String siteId, String code) throws Exception;

    /**
     * @description:在站点中查询给定编码的仓库
     * @author: 890147
     * @createDate: 2014-8-10
     * @param siteId
     * @param code
     * @return:
     */
    InvWarehouse queryWarehouseByCodeAndSiteId(String siteId, String code) throws Exception;

    /**
     * @description:查询站点的所有仓库列表
     * @author: 890147
     * @createDate: 2014-8-12
     * @param siteId
     * @return:
     */
    List<InvWarehouse> queryAllWarehouseBySiteId(String siteId);
}
