package com.timss.workorder.service;

import java.util.List;

import com.timss.workorder.vo.MaintainPlanVO;


public interface MaintainPlanVOService {
	
	/**
	 * @description:通过设备编码查找与该设备相关的维护计划信息
	 * @author: 王中华
	 * @createDate: 2014-7-9
	 * @param equipCode
	 * @return
	 * @throws Exception:
	 */
	List<MaintainPlanVO> queryMTPVOByAssetId(String assetId, String siteId);

}
