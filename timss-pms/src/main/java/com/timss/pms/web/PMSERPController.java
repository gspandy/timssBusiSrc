package com.timss.pms.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.timss.pms.bean.BodyPartOfERPIB;
import com.timss.pms.bean.ERPInBrower;
import com.timss.pms.bean.ERPResponse;
import com.timss.pms.bean.HeaderPartOfERPIB;
import com.timss.pms.service.PMSToERPService;
import com.timss.pms.service.PayService;
import com.timss.pms.util.GetBeanFromBrowerUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.impl.UserInfoScopeImpl;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;

@Controller
@RequestMapping("pms/erp")
public class PMSERPController {
	@Autowired
	PayService payService;
	@Autowired
	PMSToERPService pmsToERPService;
	@Autowired
	ItcMvcService itcMvcService;
	
	@RequestMapping("inputERPJSP")
	public ModelAndView inputERPJSP(){
		
		UserInfoScopeImpl userInfo=(UserInfoScopeImpl) itcMvcService.getUserInfoScopeDatas();
		String payId=userInfo.getParam("payId");
		ERPInBrower erpInBrower=pmsToERPService.getERPDataFromReceiptId(payId, userInfo);
		
		ModelAndView modelAndView=new ModelAndView("erp/inputERP.jsp");
		modelAndView.addObject("erpForm", JsonHelper.toJsonString(erpInBrower.getHeader()));
		modelAndView.addObject("erpTable", JsonHelper.toJsonString(erpInBrower.getBodys()));
		return modelAndView;
	}
	
	@RequestMapping("sendReceiptMessageToErp")
	public ModelAndViewAjax sendReceiptMessageToErp(String payId) throws Exception{
		UserInfoScopeImpl userInfo=(UserInfoScopeImpl) itcMvcService.getUserInfoScopeDatas();
		HeaderPartOfERPIB hPartOfERPIB=GetBeanFromBrowerUtil.getBeanFromBrower("获取到收款erpForm为：", "erpForm", HeaderPartOfERPIB.class, itcMvcService);
		List<BodyPartOfERPIB> bOfERPIBs=GetBeanFromBrowerUtil.getBeanListFromBrower("获取到的收款erpTable为:", "erpTable", BodyPartOfERPIB.class, itcMvcService);
		ERPResponse response=pmsToERPService.sendReceiptMessage(payId,hPartOfERPIB,bOfERPIBs, userInfo);
		return ViewUtil.Json(response);
	}

}
