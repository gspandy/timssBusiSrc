package com.timss.pms.service.core;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.PrifixSequence;
import com.timss.pms.dao.PrifixSequenceDao;
import com.timss.pms.service.PrifixSequenceService;
import com.yudean.mvc.service.ItcMvcService;
@Service
public class PrifixSequenceServiceImpl implements PrifixSequenceService {

	@Autowired
	PrifixSequenceDao prifixSequenceDao;
	@Autowired
	ItcMvcService itcMvcService;
	private BigDecimal defaultNextVal= BigDecimal.valueOf(1);
	private BigDecimal defaultStep=BigDecimal.valueOf(1);
	private String defaultPattern="0000";
	@Override
	public String getNextSequenceVal(String prifix, String type) {
		String sequenceVal=getNextSequenceVal(prifix, type, null);
		return sequenceVal;
	}

	@Override
	@Transactional
	public String getNextSequenceVal(String prifix, String type,
			PrifixSequence prifixSequence) {
		
		PrifixSequence sequence=prifixSequenceDao.getNextSequenceVal(prifix, type);
		
		if(sequence==null){
			prifixSequence=initPrifixSequence(prifix, type, prifixSequence);
			prifixSequenceDao.insertSequence(prifixSequence);
			sequence=prifixSequence;
		}
		String sequenceVal=getSequenceVal(sequence);
		return sequenceVal;
	}

	@Override
	public void increaseSequence(String prifix, String type) {
		increaseSequence(prifix, type, null);
		
	}

	@Override
	@Transactional
	public void increaseSequence(String prifix, String type,
			PrifixSequence prifixSequence) {
	
		int count=prifixSequenceDao.increaseSequence(prifix, type);
		if(count==0){
			prifixSequence=initPrifixSequence(prifix, type, prifixSequence);
			increatePrifixSequence(prifixSequence);
			prifixSequenceDao.insertSequence(prifixSequence);
		}
		
	}
	

	private PrifixSequence initPrifixSequence(String prifix, String type,PrifixSequence prifixSequence){
		if(prifixSequence==null){
			prifixSequence=new PrifixSequence();
			prifixSequence.setNextVal(defaultNextVal);
			prifixSequence.setStep(defaultStep);
			prifixSequence.setPattern(defaultPattern);
			prifixSequence.setSiteid(itcMvcService.getUserInfoScopeDatas().getSiteId());
			prifixSequence.setPrifix(prifix);
			prifixSequence.setType(type);
		}
		return prifixSequence;
	} 
	
	private PrifixSequence increatePrifixSequence(PrifixSequence prifixSequence){
		BigDecimal bigDecimal=prifixSequence.getNextVal();
		BigDecimal nextVal=bigDecimal.add(prifixSequence.getStep());
		prifixSequence.setNextVal(nextVal);
		return prifixSequence;
	}
	
	private String getSequenceVal(PrifixSequence prifixSequence){
		return prifixSequence.getPrifix()+prifixSequence.getNextVal().toString();
	}

}
