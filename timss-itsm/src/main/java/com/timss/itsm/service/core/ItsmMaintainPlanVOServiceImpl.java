package com.timss.itsm.service.core;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.itsm.dao.ItsmMaintainPlanVODao;
import com.timss.itsm.service.ItsmMaintainPlanVOService;
import com.timss.itsm.vo.ItsmMaintainPlanVO;
@Service
public class ItsmMaintainPlanVOServiceImpl implements ItsmMaintainPlanVOService {
	@Autowired
	ItsmMaintainPlanVODao maintainPlanVODao;
	@Override
	public List<ItsmMaintainPlanVO> queryMTPVOByAssetId(String assetId, String siteId){
		
		List<ItsmMaintainPlanVO> mtpVoList = maintainPlanVODao.queryMTPVOByAssetId(assetId,siteId);
		
		return mtpVoList;
	}

}
