package com.timss.pms.flow.itc.receipt.v001;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.pms.bean.Invoice;
import com.timss.pms.bean.Pay;
import com.timss.pms.exception.PmsBasicException;
import com.timss.pms.service.InvoiceService;
import com.timss.pms.service.PayService;
import com.timss.pms.util.GetBeanFromBrowerUtil;
import com.timss.pms.util.JsonUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;


/**
 * 项目立项 提出申请节点类
 * @ClassName:     Apply
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-9 上午11:42:11
 */
public class Cwkpqr extends TaskHandlerBase{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	PayService payService;
	@Autowired
	InvoiceService invoiceService;
	private static final Logger LOGGER=Logger.getLogger(Cwkpqr.class);
	
	public void onComplete(TaskInfo taskInfo) {
		UserInfoScope userInfoScope=(UserInfoScope) itcMvcService.getUserInfoScopeDatas();
		Map map=GetBeanFromBrowerUtil.getMapFromBrower("插入的收款信息为", "businessData", itcMvcService);

		
		List<Invoice> invoices=getInvoices();
//		String invoiceString=invoiceList.toString();
//		System.out.println(invoiceString);
//		List<Invoice> invoices=JsonUtil.fromJsonStringToList(invoiceString, Invoice.class);
		Pay pay=new Pay();
		pay.setId((Integer) map.get("id"));
		pay.setContractId((Integer) map.get("contractId"));
		pay.setPayplanId((Integer) map.get("payplanId"));
		invoiceService.updateInvoice(invoices, pay);
		LOGGER.info("完成项目收款工作流节点"+taskInfo.getTaskDefKey()+"对收款信息的更新");
	}
	
	private List<Invoice> getInvoices(){
		UserInfoScope userInfoScope=(UserInfoScope) itcMvcService.getUserInfoScopeDatas();
		String businessDataString=null;
		try {
			businessDataString=userInfoScope.getParam("businessData");
		} catch (Exception e) {
			throw new PmsBasicException("获取前端的businessData出错",e);
		}
		JSONObject jsonObject=JSONObject.fromObject(businessDataString);
		JSONArray jsonArray=(JSONArray) jsonObject.get("invoice");
		List<Invoice> lists=JsonUtil.fromJsonStringToList(jsonArray.toString(), Invoice.class);
		return lists;
	}

}
