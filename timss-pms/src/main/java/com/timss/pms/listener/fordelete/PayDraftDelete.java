package com.timss.pms.listener.fordelete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.service.PayService;

@Service
public class PayDraftDelete implements IDraftDelete{
    @Autowired
    PayService payService;
	@Override
	public void deleteDraft(String id) {
		payService.deletePay(id);
	}

}
