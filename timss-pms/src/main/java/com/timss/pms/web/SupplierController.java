package com.timss.pms.web;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.timss.pms.service.SupplierService;
import com.timss.pms.util.CreateReturnMapUtil;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;

@Controller
@RequestMapping(value="pms/supplier")
public class SupplierController {
	@Autowired
	SupplierService supplierService;
	Logger LOGGER=Logger.getLogger(SupplierController.class);
	@RequestMapping("/queryFuzzyByName")
	public ModelAndViewAjax queryFuzzyByName(String kw){
		LOGGER.info("查询供应商通过名称："+kw);
		List result=supplierService.queryFuzzyByName(kw);
		LOGGER.info("成功查询供应商通过名称："+kw);
		//Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "添加发票成功",result);
		return ViewUtil.Json(result);
	}
	
	@RequestMapping("/querySupplierById")
	public ModelAndViewAjax querySupplierById(String id){
		LOGGER.info("查询供应商通过id："+id);
		Object result=supplierService.querySupplierById(id);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "",result);
		return ViewUtil.Json(map);
	}

}
