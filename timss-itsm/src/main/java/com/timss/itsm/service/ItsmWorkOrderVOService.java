package com.timss.itsm.service;

import java.util.List;

import com.timss.itsm.vo.ItsmWorkOrderVO;

public interface ItsmWorkOrderVOService {
	
	/**
	 * @description: 根据设备编号，查看该设备出现过的维修记录
	 * @author: 王中华
	 * @createDate: 2014-7-9
	 * @param equipCode
	 * @return
	 * @throws Exception:
	 */
	List<ItsmWorkOrderVO> queryWOVOByAssetId(String assetId, String siteId);
	
	

}
