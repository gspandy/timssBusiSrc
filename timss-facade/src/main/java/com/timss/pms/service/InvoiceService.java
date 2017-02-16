package com.timss.pms.service;

import java.util.List;

import com.timss.pms.bean.Invoice;
import com.timss.pms.bean.InvoiceConfirm;
import com.timss.pms.bean.Pay;
import com.timss.pms.vo.InvoiceDtlVo;
import com.timss.pms.vo.InvoiceVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * 发票查询
 * @ClassName:     InvoiceService
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-8-11 下午2:27:09
 */
public interface InvoiceService {
	void insertInvoice(List<Invoice> invoices,Pay pay);
	void updateInvoice(List<Invoice> invoices,Pay pay);
	
	int insertInvoice(Invoice invoice);
	int deleteInvoice(int id);
	int deleteInvoiceListWithPayId(int id);
	int updateInvoice(Invoice invoice);
	/**
	 * 根据付款或者收款id，查询所属的发票信息
	 * @Title: queryInvoiceListByPayId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param payId
	 * @return
	 */
	List<InvoiceVo> queryInvoiceListByPayId(int payId);
	InvoiceDtlVo queryInvoiceById(int id);
	/**
	 * 根据合同id，查询所属的发票信息
	 * @Title: queryInvoiceByContractId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param contractId
	 * @return
	 */
	List<InvoiceVo> queryInvoiceListByContractId(int contractId);
	
	public Page<InvoiceVo> queryInvoiceList(Page<InvoiceVo> page,
			UserInfoScope userInfo);
	
	/**
	 * 对收款信息进行确认
	 * @Title: checkReceipt
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param invoice
	 * @param invoiceConfirms
	 * @return
	 */
	int checkReceipt(Invoice invoice,List<InvoiceConfirm> invoiceConfirms,boolean isInvoiceReceiptable);
}
