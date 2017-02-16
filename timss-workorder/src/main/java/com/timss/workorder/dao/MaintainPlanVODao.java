package com.timss.workorder.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.workorder.vo.MaintainPlanVO;
 
/**
 * 
 * @title: 维护计划 列表DAO
 * @description: {desc}
 * @company: gdyd
 * @className: MaintainPlanVODao.java
 * @author: 王中华
 * @createDate: 2014-6-16
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface MaintainPlanVODao {
	
	
	/**
	 * @description:查询与某个资产相关的所有维护计划信息
	 * @author: 王中华
	 * @createDate: 2014-7-25
	 * @param assetId
	 * @param siteId
	 * @return:
	 */
	List<MaintainPlanVO> queryMTPVOByAssetId(@Param("assetId") String assetId, @Param("siteId") String siteId);
	
}
