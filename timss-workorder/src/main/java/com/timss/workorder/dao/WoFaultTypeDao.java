package com.timss.workorder.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.workorder.bean.WoFaultType;
import com.yudean.itc.dto.Page;
/**
 * 技能
 * @author 王中华
 * 2014-6-11
 */
public interface WoFaultTypeDao {

	/**
	 * @description:插入技能
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @param skill
	 * @return:
	 */
	int insertFaultType(WoFaultType faultType);
	
	/**
	 * @description:修改技能
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @param skill
	 * @return:
	 */
	int updateFaultType(WoFaultType faultType);
	
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
	WoFaultType queryFaultTypeById(int id);
	
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
	List<WoFaultType> queryWoFaultTypeList(Page<WoFaultType> page);

	/**
	 * @description:根据某个站点，查找站点下的故障类型的根节点
	 * @author: 王中华
	 * @createDate: 2014-9-18
	 * @param siteId
	 * @return:
	 */
	WoFaultType queryFTRootBySiteId(@Param("siteid") String siteId);

	/**
	 * @description:查询某个故障类型的子类型 
	 * @author: 王中华
	 * @createDate: 2014-9-18
	 * @param faultTypeId:
	 */
	List<WoFaultType> queryChildrenNodes(@Param("parent_id") String faultTypeId,
			@Param("siteid") String siteId, @Param("treeType") String treeType);

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
	List<WoFaultType> queryOneLevelFTBySiteId(@Param("siteid") String siteid);
	
	/**
	 * @description:查找故障数据给下拉搜索框
	 * @author: 890151
	 * @createDate: 2015年12月21日
	 * @param keyWord
	 * @return:
	 */
    List<Map<String, Object>> queryFaultTypeForHint(@Param("kw") String keyWord,@Param("siteId") String siteId);

}
