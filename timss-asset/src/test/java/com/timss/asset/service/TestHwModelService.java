package com.timss.asset.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.asset.bean.HwModelBean;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml",
		"classpath:config/context/applicationContext-webserviceClient-config.xml"})
public class TestHwModelService extends TestUnit {
	
	static private Logger log = Logger.getLogger(TestHwModelService.class);
	
	@Autowired
    private HwModelService service;
	
	@Test
    public void testCRUD() throws Exception{
		String siteId="ITC";
    	
    	HwModelBean bean1=new HwModelBean();
        bean1.setSiteid(siteId);
        bean1.setModelName("测试的硬件类型001");
        bean1.setModelType("raidType");
        service.insert(bean1);
        log.debug("testCRUD.insert-->name:"+bean1.getModelName()+" id:"+bean1.getModelId());
        
        HwModelBean bean2=new HwModelBean();
        bean2.setSiteid(siteId);
        bean2.setModelName("测试的硬件类型002");
        bean2.setModelType("storageModel");
        service.insert(bean2);
        log.debug("testCRUD.insert-->name:"+bean2.getModelName()+" id:"+bean2.getModelId());
        
        Page<HwModelBean>page=new Page<HwModelBean>();
        page.setParameter("siteId", siteId);
        page=service.queryList(page);
        log.debug("testCRUD.queryList-->size:"+page.getTotalRecord());
        
        HwModelBean bean=service.queryDetail(bean1.getModelId());
        log.debug("testCRUD.queryDetail-->id:"+bean.getModelId()+" name:"+bean.getModelName()+" hwLedgerDevice:"+bean.getDeviceList().size());
        
        bean2.setModelName("测试的硬件类型002(修改)");
        service.update(bean2);
        bean2=service.queryDetail(bean2.getModelId());
        log.debug("testCRUD.update-->id:"+bean2.getModelId()+" name:"+bean2.getModelName());
        
        service.deleteById(bean1.getModelId());
        service.deleteById(bean2.getModelId());
        page=service.queryList(page);
        log.debug("testCRUD.deleteById-->size now:"+page.getTotalRecord());
    }
}