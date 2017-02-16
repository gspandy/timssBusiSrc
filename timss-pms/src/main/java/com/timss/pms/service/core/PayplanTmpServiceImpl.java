package com.timss.pms.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.bean.Contract;
import com.timss.pms.bean.PayplanTmp;
import com.timss.pms.dao.PayplanTmpDao;
import com.timss.pms.service.PayplanTmpService;
import com.timss.pms.vo.PayplanTmpVo;
@Service
public class PayplanTmpServiceImpl implements PayplanTmpService {
    @Autowired
    PayplanTmpDao payplanTmpDao;
	@Override
	public int updatePayplanTmp(List<PayplanTmp> payplanTmps,
			Contract contract, String flowId) {
		initPayplanTmp(payplanTmps,contract,flowId);
		payplanTmpDao.deletePayplanTmpByFlowId(flowId);
		if(payplanTmps!=null&& payplanTmps.size()!=0){
			payplanTmpDao.insertPayplanTmpList(payplanTmps);
			
		}
		return 0;
	}
	
	
	private void initPayplanTmp(List<PayplanTmp> payplanTmps,
			Contract contract, String flowId){
		if(payplanTmps!=null){
			for(int i=0;i<payplanTmps.size();i++){
				PayplanTmp payplanTmp=payplanTmps.get(i);
				payplanTmp.setContractId(contract.getId());
				payplanTmp.setFlowId(flowId);
			}
		}
	}


	@Override
	public List<PayplanTmpVo> queryPayplanTmpByFlowId(String flowId) {
		List<PayplanTmpVo> payplanTmps=payplanTmpDao.queryPayplanTmpListByFlowId(flowId);
		return payplanTmps;
	}

}
