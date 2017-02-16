package com.timss.itsm.service;

import java.util.List;
import java.util.Map;

import com.timss.itsm.bean.ItsmWoFaultType;
import com.yudean.itc.dto.Page;

/**
 * 故障类型 Service操作
 * 
 * @author 王中华 2014-6-11
 */
public interface ItsmWoFaultTypeService {

    /**
     * @description:修改故障类型
     * @author: 王中华
     * @createDate: 2014-8-27
     * @param addWoSkillDataMap:
     */
    void updateWoFaultType(Map<String, String> addWoFaultTypeDataMap);

    /**
     * @description:添加故障类型
     * @author: 王中华
     * @createDate: 2014-8-27
     * @param addWoSkillDataMap:
     */
    void insertWoFaultType(Map<String, String> addWoFaultTypeDataMap);

    /**
     * 根据ID 查询故障类型
     * 
     * @param id
     * @return
     */
    ItsmWoFaultType queryWoFaultTypeById(int id);

    /**
     * @description:删除故障类型
     * @author: 王中华
     * @createDate: 2014-8-27
     * @param id:
     */
    void deleteWoFaultType(int id);

    /**
     * @description:查询故障类型列表
     * @author: 王中华
     * @createDate: 2014-8-27
     * @param page
     * @return:
     */
    Page<ItsmWoFaultType> queryAllFaultType(Page<ItsmWoFaultType> page);

    /**
     * @description:查询某个故障类型的子类型
     * @author: 王中华
     * @createDate: 2014-9-18
     * @param parentId
     * @param treeTyoe 树类型，（SD：只查找服务目录，不查服务性质）
     * @return:
     */
    List<Map<String, Object>> queryChildrenNodes(String parentId, String treeType,String siteid);

    /**
     * @description:查询根目录的一级子类型
     * @author: 王中华
     * @createDate: 2014-9-18
     * @param parentId
     * @param treeTyoe 树类型，（SD：只查找服务目录，不查服务性质）
     * @return:
     */
    List<Map<String, Object>> queryOneLevelChildrenNodes(String parentId, String treeType);
    
    /**
     * @description:根据某个站点，查找站点下的故障类型的根节点
     * @author: 王中华
     * @createDate: 2014-9-18
     * @param locationId
     * @return:
     */
    ItsmWoFaultType queryFaultTypeRootBySiteId(String siteId);

    /**
     * @description: 删除故障类型以及其子类型
     * @author: 王中华
     * @createDate: 2014-9-18
     * @param faultTypeId
     * @return:
     */
    int deleteFaultTypeById(int faultTypeId);

    /**
     * @description:查找一级目录（父节点为根节点的服务目录）
     * @author: 王中华
     * @createDate: 2014-11-25
     * @param siteId
     * @return:
     */
    List<ItsmWoFaultType> queryOneLevelFTBySiteId(String siteId);

    /**
     * @description: 根据站点查找服务目录的根
     * @author: 王中华
     * @createDate: 2015-2-6
     * @param siteid
     * @return:
     */
    ItsmWoFaultType queryFTRootBySiteId(String siteid);

    List<ItsmWoFaultType> queryWoFaultTypeList(Page<ItsmWoFaultType> page);

    List<String> queryFaultTypeParents(String faultTypeId);

    List<Map<String, Object>> queryFaultTypeForHint(String keyWord);

    /**
     * @description: 查找子节点
     * @author: 王中华
     * @createDate: 2016-8-18
     * @param parentId
     * @param type "SD"服务目录；“SC”服务性质 
     * @return:
     */
    List<ItsmWoFaultType> querychildrenFtById(String parentId, String type);

    /**
     * @description:根据站点，查询各单位自己的服务目录根节点 
     * @author: 王中华
     * @createDate: 2016-11-1
     * @param siteId
     * @return:
     */
    ItsmWoFaultType querySerCatalogRootBySiteId(String siteId);

    /**
     * @description:查询各站点的一级服务目录
     * @author: 王中华
     * @createDate: 2016-11-1
     * @param parentIdString
     * @param treeType
     * @param siteid
     * @return:
     */
    List<Map<String, Object>> queryOneLevelSerCataNodes(String parentIdString, String treeType, String siteid);


}
