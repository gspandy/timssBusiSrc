package com.timss.pms.dao;

import java.util.List;

import com.timss.pms.bean.Invoice;
import com.timss.pms.vo.InvoiceDtlVo;
import com.timss.pms.vo.InvoiceVo;
import com.yudean.itc.dto.Page;

/**
 * 发票dao类
 * @ClassName:     InvoiceDao
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-8-11 下午2:19:04
 */
public interface InvoiceDao {
	int insertInvoice(Invoice invoice);
	int deleteInvoice(int id);
	int deleteInvoiceListWithPayId(int payId);
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
	
	
	/**
	 * 查询所有未确认的收款发票
	 * @Title: queryUncheckInvoice
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return
	 */
	List<InvoiceVo> queryInvoiceList(Page<InvoiceVo> page);
	
	/**
	 * 更新发票状态为已收款
	 * @Title: checkReceipt
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param invoice
	 * @return
	 */
	int checkReceipt(Invoice invoice);
}
