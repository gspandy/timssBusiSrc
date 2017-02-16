package com.timss.pms.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.Contract;
import com.timss.pms.dao.ContractDao;
import com.timss.pms.service.FlowStatusService;

/**
 * 合同更新流程状态
 * @ClassName:     ContractFlowStatusServiceImpl
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2015-9-23 下午4:30:20
 */
@Service
public class ContractFlowStatusServiceImpl implements FlowStatusService {

	@Autowired
	ContractDao contractDao;
	@Override
	@Transactional
	public boolean updateFlowStatus(String id, String status) {
		Contract contract=new Contract();
		contract.setId(Integer.valueOf(id));
		contract.setFlowStatus(status);
		contractDao.updateByPrimaryKeySelective(contract);
		return true;
	}

}
