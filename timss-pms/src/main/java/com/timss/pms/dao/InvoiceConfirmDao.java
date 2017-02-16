package com.timss.pms.dao;

import java.util.List;

import com.timss.pms.bean.InvoiceConfirm;
import com.timss.pms.vo.InvoiceConfirmVo;

public interface InvoiceConfirmDao {
	int insertInvoiceConfirm(InvoiceConfirm invoiceConfirm);
	
	int deleteInvoiceConfrimByInvoiceId(int invoiceId);
	
	List<InvoiceConfirmVo> queryInvoiceConfirmByInvoiceId(int invoiceId);
}
