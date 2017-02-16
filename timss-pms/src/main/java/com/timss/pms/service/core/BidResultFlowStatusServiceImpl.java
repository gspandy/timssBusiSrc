package com.timss.pms.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.BidResult;
import com.timss.pms.dao.BidResultDao;
import com.timss.pms.service.FlowStatusService;

/**
 * 招标结果流程状态更新
 * @ClassName:     BidResultFlowStatusServiceImpl
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2015-9-22 下午3:30:51
 */
@Service
public class BidResultFlowStatusServiceImpl implements FlowStatusService {

	@Autowired
	BidResultDao bidResultDao;
	@Override
	@Transactional
	public boolean updateFlowStatus(String id, String status) {
		BidResult bidResult=new BidResult();
		bidResult.setBidResultId(Integer.valueOf(id));
		bidResult.setFlowStatus(status);
		bidResultDao.updateByPrimaryKeySelective(bidResult);
		return true;
	}

}
