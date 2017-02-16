package com.timss.itsm.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.itsm.dao.ItsmWoStatisticVODao;
import com.timss.itsm.service.ItsmWoStatisticVOService;
import com.timss.itsm.vo.ItsmWoStatisticVO;

@Service
public class ItsmWoStatisticVOServiceImpl implements ItsmWoStatisticVOService {

	@Autowired
	ItsmWoStatisticVODao itsmWoStatisticVODao;
	@Override
	public int insertBatchItWoStatisticVO(List<ItsmWoStatisticVO> itWoStatisticVOList) {
		return itsmWoStatisticVODao.insertBatchItWoStatisticVO(itWoStatisticVOList);
	}

	@Override
	public int getNextPrintNum() {
		return itsmWoStatisticVODao.getNextPrintNum();
	}
	
	
}
