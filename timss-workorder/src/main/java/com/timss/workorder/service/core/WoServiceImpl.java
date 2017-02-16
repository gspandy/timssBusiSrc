package com.timss.workorder.service.core;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.service.MaintainPlanVOService;
import com.timss.workorder.service.WoService;
import com.timss.workorder.service.WorkOrderService;
import com.timss.workorder.service.WorkOrderVOService;
import com.timss.workorder.vo.MaintainPlanVO;
import com.timss.workorder.vo.WorkOrderVO;
@Service
public class WoServiceImpl implements WoService{
	@Autowired
	private WorkOrderService workOrderService;
	@Autowired
	private WorkOrderVOService workOrderVOService;
	@Autowired
	private MaintainPlanVOService maintainPlanVOService;
	@Override
	public List<WorkOrderVO> queryWOVOByAssetId(String assetId, String siteId) {
		return workOrderVOService.queryWOVOByAssetId(assetId, siteId);
	}

	@Override
	public List<MaintainPlanVO> queryMTPVOByAssetId(String assetId,
			String siteId) {
		return maintainPlanVOService.queryMTPVOByAssetId(assetId, siteId);
	}

	@Override
	public Map<String, Object> queryWOBaseInfoByWOCode(String woCode,
			String siteId) {
		return workOrderService.queryWOBaseInfoByWOCode(woCode, siteId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void inPtwToNextStep(int woId, String userId){
		workOrderService.inPtwToNextStep(woId, userId);
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateWOAddPTWId(int woId, int ptwId) {
		workOrderService.updateWOAddPTWId(woId, ptwId);
	}

}
