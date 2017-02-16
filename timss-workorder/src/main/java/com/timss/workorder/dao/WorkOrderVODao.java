package com.timss.workorder.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.workorder.vo.WorkOrderVO;

public interface WorkOrderVODao {


	
	/**
	 * @description:根据设备ID ，查设备的维修记录
	 * @author: 王中华
	 * @createDate: 2014-7-25
	 * @param assetId
	 * @param siteId
	 * @return:
	 */
	List<WorkOrderVO> queryWOVOByAssetId(@Param("assetId") String assetId, @Param("siteId") String siteId);
	
	
}
