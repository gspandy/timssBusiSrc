package com.timss.pms.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.timss.pms.bean.Invoice;
import com.timss.pms.bean.InvoiceConfirm;
import com.timss.pms.service.InvoiceService;
import com.timss.pms.service.PrivilegeService;
import com.timss.pms.util.CreateReturnMapUtil;
import com.timss.pms.util.GetBeanFromBrowerUtil;
import com.timss.pms.vo.InvoiceDtlVo;
import com.timss.pms.vo.InvoiceVo;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;

/**
 * 
 * @ClassName:     InvoiceController
 * @company: gdyd
 * @Description:招标模块controller
 * @author:    黄晓岚
 * @date:   2014-7-2 上午11:43:36
 */
@Controller
@RequestMapping(value="pms/invoice")
public class InvoiceController {
	
	Logger LOGGER=Logger.getLogger(InvoiceController.class);
	
	@Autowired
	InvoiceService invoiceService;
	@Autowired
	ItcMvcService itcMvcService;

	@Autowired
	PrivilegeService privilegeService;
	/**
	 * 
	 * @Title: insertInvoiceJsp
	 * @Description: 转发到新增发票信息页面
	 * @return
	 */
	@RequestMapping(value = "/insertInvoiceJsp")
	public String insertInvoiceJsp(){
		return "invoice/addInvoice.jsp";
	}
	/**
	 * 转发到招标列表页面
	 * @Title: invoiceListJsp
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return
	 */
	@RequestMapping(value = "/invoiceListJsp")
	public String invoiceListJsp(){
		return "invoice/invoiceList.jsp";
	}
	
	/**
	 * 
	 * @Title: editInvoiceJsp
	 * @Description: 转发到查看发票详细信息页面
	 * @return
	 */
	@RequestMapping(value = "/editInvoiceJsp")
	public String editInvoiceJsp(String id) {
		return "invoice/editInvoice.jsp?id=" + id;
	}
	
	@RequestMapping("insertInvoice")
	public ModelAndViewAjax insertInvoice(){
		Invoice invoice=GetBeanFromBrowerUtil.getBeanFromBrower("插入发票信息：", "invoice", Invoice.class, itcMvcService);
		
		invoiceService.insertInvoice(invoice);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "添加发票成功");
		return ViewUtil.Json(map);
	}
	@RequestMapping("queryInvoiceById")
	public ModelAndViewAjax queryInvoiceById(String id){
		InvoiceDtlVo invoiceDtlVo=invoiceService.queryInvoiceById(Integer.parseInt(id));
		Map priMap=privilegeService.createInvoicePrivilege(invoiceDtlVo, itcMvcService);
		//TODO 权限信息
		Map map=CreateReturnMapUtil.createMapWithPrivilege(CreateReturnMapUtil.SUCCESS_FLAG, "",invoiceDtlVo,priMap);
		return ViewUtil.Json(map);
	}
	
	/**
	 * 对发票进行收款确认操作
	 * @Title: queryInvoiceById
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("checkReceipt")
	public ModelAndViewAjax checkReceipt(String id,boolean isInvoiceReceiptable) throws Exception{
		Invoice invoice=GetBeanFromBrowerUtil.getBeanFromBrower("插入发票信息：", "invoice", Invoice.class, itcMvcService);
		List<InvoiceConfirm> invoiceConfirms=GetBeanFromBrowerUtil.getBeanListFromBrower("插入发票信息", "invoiceConfirm", InvoiceConfirm.class, itcMvcService);
		invoiceService.checkReceipt(invoice,invoiceConfirms,isInvoiceReceiptable);
		
		//TODO 权限信息
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	@RequestMapping(value = "/invoiceListData", method = RequestMethod.POST)
	public Page<InvoiceVo> invoiceListData(String invoiceName) throws Exception {
       
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		Page<InvoiceVo> page = userInfoScope.getPage();

		// 查询参数处理
		Map<String, String[]> params = userInfoScope.getParamMap();
		// 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
		long ts1 = System.currentTimeMillis();
		HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap("invoiceVoMap", "pms", "InvoiceDao");
		
	
		
		if (params.containsKey("search")) {
			String fuzzySearchParams = userInfoScope.getParam("search");
			LOGGER.info("查询项目立项的条件：" + fuzzySearchParams);

			// 调用工具类将jsonString转为HashMap
			Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap(fuzzySearchParams);
					

			// 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
			fuzzyParams = MapHelper.fromPropertyToColumnMap((HashMap<String, Object>) fuzzyParams,
					propertyColumnMap);
			
			// 自动的会封装模糊搜索条件
			page.setFuzzyParams(fuzzyParams);
			
			

		}
		
		//设置查询的默认值
		String ctype=userInfoScope.getParam("ctype");
		page.setFuzzyParameter("ctype", ctype);
		String ischeck=userInfoScope.getParam("ischeck");
		page.setFuzzyParameter("ischeck", ischeck);

		// 设置排序内容
		if (params.containsKey("sort")) {
			String sortKey = userInfoScope.getParam("sort");

			// 如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
			sortKey = propertyColumnMap.get(sortKey);

			page.setSortKey(sortKey);
			page.setSortOrder(userInfoScope.getParam("order"));
		} else {
			// 设置默认的排序字段
			page.setSortKey("id");
			page.setSortOrder("desc");
		}
		
		
		page = invoiceService.queryInvoiceList(page, userInfoScope);
		
		return page;
	}
	
}
