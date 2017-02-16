package com.timss.pms.listener.fordelete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.timss.pms.service.ContractService;

@Service
public class ContractDraftDelete implements IDraftDelete{
    @Autowired
    @Qualifier("contractServiceImpl")
    ContractService contractService;
	@Override
	public void deleteDraft(String id) {
		contractService.deleteContract(id);
	}

}
