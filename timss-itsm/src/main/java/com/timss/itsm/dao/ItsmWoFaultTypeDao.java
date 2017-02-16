package com.timss.itsm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.itsm.bean.ItsmWoFaultType;
import com.yudean.itc.dto.Page;

/**
 * 技能
 * 
 * @author 王中华 2014-6-11
 */
public interface ItsmWoFaultTypeDao {

    /**
     * @description:插入技能
     * @author: 王中华
     * @createDate: 2014-8-26
     * @param skill
     * @return:
     */
    int insertFaultType(ItsmWoFaultType faultType);

    /**
     * @description:修改技能
     * @author: 王中华
     * @createDate: 2014-8-26
     * @param skill
     * @return:
     */
    int updateFaultType(ItsmWoFaultType faultType);

    /**
     * @description:删除技能
     * @author: 王中华
     * @createDate: 2014-8-26
     * @param id
     * @return:
     */
    int deleteFaultType(int id);

    /**
     * @description:更加id查询
     * @author: 王中华
     * @createDate: 2014-8-26
     * @param id
     * @return:
     */
    ItsmWoFaultType queryFaultTypeById(int id);

    /**
     * @description:获取下一个插入的ID
     * @author: 王中华
     * @createDate: 2014-8-26
     * @return:
     */
    int getNextParamsConfId();

    /**
     * @description:查询故障类型列表
     * @author: 王中华
     * @createDate: 2014-9-1
     * @param page
     * @return:
     */
    List<ItsmWoFaultType> queryWoFaultTypeList(Page<ItsmWoFaultType> page);

    /**
     * @description:根据某个站点，查找站点下的故障类型的根节点
     * @author: 王中华
     * @createDate: 2014-9-18
     * @param siteId
     * @return:
     */
    ItsmWoFaultType queryFTRootBySiteId(@Param("siteid") String siteId);

    /**
     * @description:查询某个故障类型的子类型
     * @author: 王中华
     * @createDate: 2014-9-18
     * @param faultTypeId:
     */
    List<ItsmWoFaultType> queryChildrenNodes(@Param("parent_id") String faultTypeId, @Param("siteid") String siteId,
            @Param("treeType") String treeType);

    /**
     * @description:删除故障类型及其子类型
     * @author: 王中华
     * @createDate: 2014-9-18
     * @param faultTypeId
     * @return:
     */
    int deleteFaultTypeById(int faultTypeId);

    /**
     * @description: 查找一级服务目录（父目录为根节点的目录）
     * @author: 王中华
     * @createDate: 2014-11-25
     * @param siteid
     * @return:
     */
    List<ItsmWoFaultType> queryOneLevelFTBySiteId(@Param("siteid") String siteid);

    ItsmWoFaultType queryOneLeveFTById(@Param("id") int id);

    /**
     * @description:不分页，查询服务目录（不包括性质），包括对应的一级目录的信息
     * @author: 王中华
     * @createDate: 2015-3-12
     * @return:
     */
    List<ItsmWoFaultType> queryNewWoFaultTypeList();

    List<String> queryWoFaultTypeParents(@Param("id") String faultTypeId);

    List<Map<String, Object>> queryFaultTypeForHint(@Param("kw") String keyWord);
}
