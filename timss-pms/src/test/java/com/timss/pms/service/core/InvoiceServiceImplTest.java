package com.timss.pms.service.core;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
import com.timss.pms.bean.Invoice;
import com.timss.pms.service.InvoiceService;
import com.timss.pms.vo.InvoiceVo;

@Transactional
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class InvoiceServiceImplTest extends TestUnit{
    @Autowired
    InvoiceService invoiceService;
	@Test
	public void test() {
		Invoice invoice=new Invoice();
		initInvoice(invoice);
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");
		invoiceService.insertInvoice(invoice);
		
		Invoice re=invoiceService.queryInvoiceById(invoice.getId());
		assertEquals("code", re.getCode());
		
		invoice.setCode("ss");
		invoiceService.updateInvoice(invoice);
		re=invoiceService.queryInvoiceById(invoice.getId());
		assertEquals("ss", re.getCode());
		
		List<InvoiceVo> list=invoiceService.queryInvoiceListByContractId(999);
		assertEquals(1, list.size());
		
		invoiceService.deleteInvoiceListWithPayId(999);
		list=invoiceService.queryInvoiceListByPayId(999);
		assertEquals(0, list.size());
		
		Page<InvoiceVo> page=new Page<InvoiceVo>();
		page.setFuzzyParameter("ischeck", "N");
		page=invoiceService.queryInvoiceList(page, null);
	
	}
	
	private void initInvoice(Invoice invoice){
		if(invoice!=null){
			invoice.setCode("code");
			invoice.setTax(1.0);
			invoice.setSum(3.0);
			invoice.setWithoutTax(2.0);
			invoice.setRate(50.0);
			invoice.setPayId(999);
			invoice.setContractId(999);
		}
	}

}
