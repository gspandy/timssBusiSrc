package com.timss.workorder.service.core;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.workorder.dao.MaintainPlanVODao;
import com.timss.workorder.service.MaintainPlanVOService;
import com.timss.workorder.vo.MaintainPlanVO;
@Service
public class MaintainPlanVOServiceImpl implements MaintainPlanVOService {
	@Autowired
	MaintainPlanVODao maintainPlanVODao;
	@Override
	public List<MaintainPlanVO> queryMTPVOByAssetId(String assetId, String siteId){
		
		List<MaintainPlanVO> mtpVoList = maintainPlanVODao.queryMTPVOByAssetId(assetId,siteId);
		
		return mtpVoList;
	}

}
