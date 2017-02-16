package com.timss.pms.service.core;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.Invoice;
import com.timss.pms.bean.InvoiceConfirm;
import com.timss.pms.bean.Pay;
import com.timss.pms.dao.InvoiceConfirmDao;
import com.timss.pms.dao.InvoiceDao;
import com.timss.pms.service.InvoiceService;
import com.timss.pms.util.InitUserAndSiteIdUtil;
import com.timss.pms.vo.InvoiceConfirmVo;
import com.timss.pms.vo.InvoiceDtlVo;
import com.timss.pms.vo.InvoiceVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
@Service
public class InvoiceServiceImpl implements InvoiceService{
	
	@Autowired
	InvoiceDao invoiceDao;
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	InvoiceConfirmDao invoiceConfirmDao;
	
	Logger LOGGER=Logger.getLogger(InvoiceServiceImpl.class);
	@Override
	@Transactional
	public int insertInvoice(Invoice invoice) {
		LOGGER.info("开始插入发票信息");
		InitUserAndSiteIdUtil.initCreate(invoice, itcMvcService);
		invoiceDao.insertInvoice(invoice);
		LOGGER.info("完成插入发票信息");
		return 0;
	}

	@Override
	@Transactional
	public int deleteInvoice(int id) {
		LOGGER.info("开始删除发票信息,id为："+id);
	
		invoiceDao.deleteInvoice(id);
		LOGGER.info("完成删除发票信息，id为："+id);
		return 0;
	}

	@Override
	@Transactional
	public int deleteInvoiceListWithPayId(int id) {
		LOGGER.info("开始删除付款下发票，付款id："+id);
		
		invoiceDao.deleteInvoiceListWithPayId(id);
		LOGGER.info("完成删除付款下发票，付款id："+id);
		return 0;
	}

	@Override
	@Transactional
	public int updateInvoice(Invoice invoice) {
		LOGGER.info("开始修改发票信息");
		InitUserAndSiteIdUtil.initUpdate(invoice, itcMvcService);
		invoiceDao.updateInvoice(invoice);
		LOGGER.info("完成修改发票信息");
		return 0;
	}

	@Override
	public List<InvoiceVo> queryInvoiceListByPayId(int payId) {
		LOGGER.info("开始查询付款下的结算列表信息，付款id："+payId);
		List<InvoiceVo> invoiceVos=invoiceDao.queryInvoiceListByPayId(payId);
		return invoiceVos;
	}

	@Override
	public InvoiceDtlVo queryInvoiceById(int id) {
		LOGGER.info("查询发票信息，id为："+id);
		InvoiceDtlVo invoiceDtlVo=invoiceDao.queryInvoiceById(id);
		//附件发票的付款记录信息
		List<InvoiceConfirmVo> invoiceConfirmVos=invoiceConfirmDao.queryInvoiceConfirmByInvoiceId(id);
		invoiceDtlVo.setInvoiceConfirmVos(invoiceConfirmVos);
		return invoiceDtlVo;
	}

	@Override
	public List<InvoiceVo> queryInvoiceListByContractId(int contractId) {
		LOGGER.info("查询合同下发票信息，合同id："+contractId);
		List<InvoiceVo> invoiceVos=invoiceDao.queryInvoiceListByContractId(contractId);
		return invoiceVos;
	}

	@Override
	@Transactional
	public void insertInvoice(List<Invoice> invoices,Pay pay) {
		if(invoices!=null && invoices.size()!=0){
			for(int i=0;i<invoices.size();i++){
				Invoice invoice=invoices.get(i);
				//初始化发票的一些额外信息
				initInvoice(invoice, pay);
				//插入发票
				insertInvoice(invoice);
			}
		}
		
	}
	
	private void initInvoice(Invoice invoice,Pay pay){
		if(invoice!=null){
			InitUserAndSiteIdUtil.initCreate(invoice, itcMvcService);
			invoice.setPayId(pay.getId());
			invoice.setPayplanId(pay.getPayplanId());
			invoice.setContractId(pay.getContractId());
		}
	}

	@Override
	@Transactional
	public void updateInvoice(List<Invoice> invoices, Pay pay) {
		deleteInvoiceListWithPayId(pay.getId());
		insertInvoice(invoices, pay);
		
	}

	@Override
	public Page<InvoiceVo> queryInvoiceList(Page<InvoiceVo> page,
			UserInfoScope userInfo) {
		LOGGER.info("开始查询项目立项数据");
		//根据站点id查询
		page.setFuzzyParameter("siteid", itcMvcService.getUserInfoScopeDatas().getSiteId());
		List<InvoiceVo> invoiceVos=invoiceDao.queryInvoiceList(page);
		page.setResults(invoiceVos);
		LOGGER.info("查询项目立项数据成功");
		return page;
	}

	@Override
	@Transactional
	public int checkReceipt(Invoice invoice,List<InvoiceConfirm> invoiceConfirms,boolean isInvoiceReceiptable) {
		invoice.setCheckTime(new Date());
		invoice.setCheckUser(itcMvcService.getUserInfoScopeDatas().getUserId());
		
		invoiceConfirmDao.deleteInvoiceConfrimByInvoiceId(invoice.getId());
		insertInvoiceConfirmList(invoice,invoiceConfirms);
		
		//如果需要更新发票状态，则更新发票状态
		setInvoiceReceipted(invoice,isInvoiceReceiptable);
		
		return 0;
	}

	private void setInvoiceReceipted(Invoice invoice,
			boolean isInvoiceReceiptable) {
		if(isInvoiceReceiptable){
			invoice.setCheckDate(new Date());
			invoice.setCheckTime(new Date());
			invoice.setCheckUser(itcMvcService.getUserInfoScopeDatas().getUserId());
			invoice.setIscheck("Y");
			invoiceDao.checkReceipt(invoice);
		}
		
	}

	private void insertInvoiceConfirmList(Invoice invoice,List<InvoiceConfirm> invoiceConfirms) {
		if(invoiceConfirms!=null){
			initInvoiceConfirmList(invoice,invoiceConfirms);
			//插入信息
			for(int i=0;i<invoiceConfirms.size();i++){
				invoiceConfirmDao.insertInvoiceConfirm(invoiceConfirms.get(i));
			}
		}
		
	}

	private void initInvoiceConfirmList(Invoice invoice,
			List<InvoiceConfirm> invoiceConfirms) {
		for(int i=0;i<invoiceConfirms.size();i++){
			InvoiceConfirm invoiceConfirm=invoiceConfirms.get(i);
			invoiceConfirm.setInvoiceId(invoice.getId());
			InitUserAndSiteIdUtil.initCreate(invoiceConfirm, itcMvcService);
		}
		
	}
	
	
}
