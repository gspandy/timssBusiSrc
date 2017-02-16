package com.timss.pms.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.bean.Checkout;
import com.timss.pms.dao.CheckoutDao;
import com.timss.pms.service.FlowStatusService;

@Service
public class CheckoutFlowStatusServiceImpl implements FlowStatusService {

	@Autowired
	CheckoutDao checkoutDao;
	@Override
	public boolean updateFlowStatus(String id, String status) {
		Checkout checkout=new Checkout();
		checkout.setId(Integer.valueOf(id));
		checkout.setFlowStatus(status);
		checkoutDao.updateByPrimaryKeySelective(checkout);
		return true;
	}

}
