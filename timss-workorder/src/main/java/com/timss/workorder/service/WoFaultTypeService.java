package com.timss.workorder.service;

import java.util.List;
import java.util.Map;

import com.timss.workorder.bean.WoFaultType;
import com.yudean.itc.dto.Page;
/**
 * 故障类型 Service操作
 * @author 王中华
 * 2014-6-11
 */
public interface WoFaultTypeService {

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
	 * @param id
	 * @return
	 */
	WoFaultType queryWoFaultTypeById(int id);
	
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
	Page<WoFaultType> queryAllFaultType(Page<WoFaultType> page);

	/**
	 * @description:查询某个故障类型的子类型
	 * @author: 王中华
	 * @createDate: 2014-9-18 
	 * @param parentId
	 * @param treeTyoe 树类型，（SD：只查找服务目录，不查服务性质）
	 * @return:
	 */
	List<Map<String, Object>> queryChildrenNodes(String parentId,String treeTyoe);

	/**
	 * @description:根据某个站点，查找站点下的故障类型的根节点
	 * @author: 王中华
	 * @createDate: 2014-9-18
	 * @param locationId
	 * @return:
	 */
	WoFaultType queryFaultTypeRootBySiteId(String siteId);

	/**
	 * @description: 删除故障类型以及其子类型
	 * @author: 王中华
	 * @createDate: 2014-9-18
	 * @param faultTypeId
	 * @return:
	 */
	int deleteFaultTypeById(int faultTypeId);

	
	/**
	 * @description:查找以及目录（父节点为根节点的服务目录）
	 * @author: 王中华
	 * @createDate: 2014-11-25
	 * @param siteId
	 * @return:
	 */
	List<WoFaultType> queryOneLevelFTBySiteId(String siteId);
	
	/**
	 * @description:查找故障数据给下拉搜索框
	 * @author: 890151
	 * @createDate: 2015年12月21日
	 * @param keyWord
	 * @return:
	 */
    List<Map<String, Object>> queryFaultTypeForHint(String keyWord,String siteId);
	
}
