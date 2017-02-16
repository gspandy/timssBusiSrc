package com.timss.pms.listener.fordelete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.service.CheckoutService;

@Service
public class CheckoutDraftDelete implements IDraftDelete{
    @Autowired
    CheckoutService checkoutService;
	@Override
	public void deleteDraft(String id) {
		checkoutService.deleteCheckout(id);
	}

}
