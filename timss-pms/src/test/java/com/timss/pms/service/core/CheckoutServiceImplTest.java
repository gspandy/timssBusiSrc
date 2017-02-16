package com.timss.pms.service.core;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.testunit.TestUnit;
import com.timss.pms.bean.Checkout;
import com.timss.pms.service.CheckoutService;
import com.timss.pms.vo.CheckoutDtlVo;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml",
"classpath:config/context/applicationContext-webserviceClient-config.xml"})
@Transactional
public class CheckoutServiceImplTest extends TestUnit{

	@Autowired
	CheckoutService checkoutService;
	@Autowired
	ItcMvcService itcMvcService;
	@Test
	public void test() {
		Checkout checkout=new Checkout();
		checkout.setContractId(999);
		checkout.setCheckStage("hello");
		checkout.setPayplanId(999);
		checkoutService.insertCheckOut(checkout);
		int id=checkout.getId();
        CheckoutDtlVo checkoutDtlVo=checkoutService.queryCheckoutById(String.valueOf(id));
        assertEquals("hello", checkoutDtlVo.getCheckStage());
        
        checkout.setCheckStage("nihoa");
		checkoutService.updateCheckoutApproved(checkout);
		checkoutDtlVo=checkoutService.queryCheckoutById(String.valueOf(id));
        assertEquals("nihoa", checkoutDtlVo.getCheckStage());
        
        checkoutService.deleteCheckout(String.valueOf(id));
        checkoutDtlVo=checkoutService.queryCheckoutById(String.valueOf(id));
        assertEquals(null, checkoutDtlVo);
	}

}
