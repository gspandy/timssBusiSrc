package com.timss.itsm.service.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.itsm.dao.ItsmWorkOrderVODao;
import com.timss.itsm.service.ItsmWorkOrderVOService;
import com.timss.itsm.vo.ItsmWorkOrderVO;
@Service
public class ItsmWorkorderVOServiceImpl implements ItsmWorkOrderVOService {
	@Autowired
	ItsmWorkOrderVODao workOrderVODao;
	private static final Logger LOG = Logger.getLogger(ItsmWorkorderVOServiceImpl.class);
	@Override
	public List<ItsmWorkOrderVO> queryWOVOByAssetId(String assetId, String siteId){
		LOG.info("-----------根据资产id获取工单对象----------------");
		return workOrderVODao.queryWOVOByAssetId(assetId, siteId);
	}

}
