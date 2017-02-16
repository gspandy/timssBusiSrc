package com.timss.itsm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.itsm.vo.ItsmWorkOrderVO;

public interface ItsmWorkOrderVODao {


	
	/**
	 * @description:根据设备ID ，查设备的维修记录
	 * @author: 王中华
	 * @createDate: 2014-7-25
	 * @param assetId
	 * @param siteId
	 * @return:
	 */
	List<ItsmWorkOrderVO> queryWOVOByAssetId(@Param("assetId") String assetId, @Param("siteId") String siteId);
	
	
}
