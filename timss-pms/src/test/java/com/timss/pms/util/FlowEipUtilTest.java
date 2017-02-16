package com.timss.pms.util;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.timss.pms.vo.BidResultDtlVo;
import com.timss.pms.vo.CheckoutDtlVo;
import com.timss.pms.vo.ContractDtlVo;
import com.timss.pms.vo.PayDtlVo;
import com.timss.pms.vo.PayplanVo;
import com.timss.pms.vo.ProjectDtlVo;
import com.yudean.itc.util.json.JsonHelper;

public class FlowEipUtilTest {


	public void test() {
		ProjectDtlVo projectDtlVo=new ProjectDtlVo();
		projectDtlVo.setProjectName("11");
		projectDtlVo.setPlanName("hello");
		List<Object> list=FlowEipUtil.getReturnForm(projectDtlVo, "itc-project-form");
		
		System.out.println(JsonHelper.fromBeanToJsonString(list));
		
		BidResultDtlVo bidResultDtlVo=new BidResultDtlVo();
		bidResultDtlVo.setName("hello");
		list=FlowEipUtil.getReturnForm(bidResultDtlVo, "itc-bidresult-form");
		System.out.println(JsonHelper.fromBeanToJsonString(list));
		
		CheckoutDtlVo checkoutDtlVo=new CheckoutDtlVo();
		checkoutDtlVo.setCommand("s");
		list=FlowEipUtil.getReturnForm(checkoutDtlVo, "itc-checkout-form");
		System.out.println(JsonHelper.fromBeanToJsonString(list));
		
		PayDtlVo payDtlVo=new PayDtlVo();
		payDtlVo.setActualpay(1.0);
		list=FlowEipUtil.getReturnForm(payDtlVo, "itc-pay-form");
		System.out.println(JsonHelper.fromBeanToJsonString(list));
		
		list=FlowEipUtil.getReturnForm(payDtlVo, "itc-receipt-form");
		System.out.println(JsonHelper.fromBeanToJsonString(list));
		
		ContractDtlVo contractDtlVo=new ContractDtlVo();
		contractDtlVo.setCommand("sss00");
		list=FlowEipUtil.getReturnForm(contractDtlVo, "itc-contract-form");
		System.out.println(JsonHelper.fromBeanToJsonString(list));
		
		PayplanVo payplanVo=new PayplanVo();
		payplanVo.setCheckStatus("s");
		list=FlowEipUtil.getReturnForm(payplanVo, "itc-payplan-table");
		System.out.println(JsonHelper.fromBeanToJsonString(list));
		
	}
	@Test
	public void testAll(){
		//Test 
		Resource resource=new ClassPathResource("config-monitor.properties");
		Properties properties;
		try {
			properties = PropertiesLoaderUtils.loadProperties(resource);
			properties.setProperty("mon.processInstId", "dse");
			OutputStream outputStream=new FileOutputStream(resource.getFile().getAbsolutePath());
			properties.store(outputStream, "");
			outputStream.close();
			
		}catch(Exception e){
			throw new RuntimeException("", e);
			
		}
		
	}

}
