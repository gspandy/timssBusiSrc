package com.timss.workorder.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.workorder.dao.WorkOrderVODao;
import com.timss.workorder.service.WorkOrderVOService;
import com.timss.workorder.vo.WorkOrderVO;
@Service
public class WorkorderVOServiceImpl implements WorkOrderVOService {
	@Autowired
	WorkOrderVODao workOrderVODao;
	
	@Override
	public List<WorkOrderVO> queryWOVOByAssetId(String assetId, String siteId){
		return workOrderVODao.queryWOVOByAssetId(assetId, siteId);
	}

}
