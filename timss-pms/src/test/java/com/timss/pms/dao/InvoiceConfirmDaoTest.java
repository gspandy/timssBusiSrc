package com.timss.pms.dao;
import static org.junit.Assert.*;

import java.util.List;

import org.hamcrest.core.AllOf;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.InvoiceConfirm;
import com.timss.pms.vo.InvoiceConfirmVo;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@Transactional
public class InvoiceConfirmDaoTest extends TestUnit{

	@Autowired
	InvoiceConfirmDao invoiceConfirmDao;
	@Test
	public void test() {
		InvoiceConfirm invoiceConfirm=new InvoiceConfirm();
		invoiceConfirm.setInvoiceId(999);
		invoiceConfirm.setConfirmSum(94.4);
		invoiceConfirmDao.insertInvoiceConfirm(invoiceConfirm);
		
		List<InvoiceConfirmVo> lists=invoiceConfirmDao.queryInvoiceConfirmByInvoiceId(999);
		assertEquals(1, lists.size());
		
		invoiceConfirmDao.deleteInvoiceConfrimByInvoiceId(999);
		lists=invoiceConfirmDao.queryInvoiceConfirmByInvoiceId(999);
		assertEquals(0, lists.size());
		
		//assertThat(null,new IsNull<Object>());
	}

}
