package com.timss.asset.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.asset.bean.HwLedgerDeviceBean;
import com.timss.asset.bean.HwModelBean;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml",
"classpath:config/context/applicationContext-webserviceClient-config.xml" })
public class TestHwModelDao extends TestUnit {
	static private Logger log = Logger.getLogger(TestHwModelDao.class);
	
	@Autowired
	HwModelDao dao;
	
	String siteId="ITC"; 
	HwModelBean bean1,bean2;
	List<HwModelBean> list;
	Page<HwModelBean>page;
	
	@Before
	public void before(){
		bean1=new HwModelBean();
        bean1.setSiteid(siteId);
        bean1.setModelName("测试的硬件类型001");
        bean1.setModelType("raidType");
        dao.insert(bean1);
        log.debug("testCRUD.insert-->name:"+bean1.getModelName()+" id:"+bean1.getModelId());
        
        bean2=new HwModelBean();
        bean2.setSiteid(siteId);
        bean2.setModelName("测试的硬件类型002");
        bean2.setModelType("storageModel");
        dao.insert(bean2);
        log.debug("testCRUD.insert-->name:"+bean2.getModelName()+" id:"+bean2.getModelId());
        
        page=new Page<HwModelBean>();
	}
	
	@After
	public void after(){
		dao.deleteById(bean1.getModelId());
        dao.deleteById(bean2.getModelId());
        list=dao.queryList(page);
        log.debug("testCRUD.deleteById-->size now:"+list.size());
	}
	
    @Test
    public void testCRUD() {
        page.setParameter("siteId", siteId);
        list=dao.queryList(page);
        log.debug("testCRUD.queryList-->size:"+list.size());
        
        HwModelBean bean=dao.queryDetail(bean1.getModelId());
        log.debug("testCRUD.queryDetail-->id:"+bean.getModelId()+" name:"+bean.getModelName());
        
        HwModelBean bean3=dao.queryHwModelByNameAndType("raidType", "测试的硬件类型001", siteId);
        log.debug("testCRUD.queryHwModelByNameAndType-->must be true-->id:"+bean3.getModelId()+" name:"+bean3.getModelName());
        bean3=dao.queryHwModelByNameAndType("storageModel", "测试的硬件类型001", siteId);
        log.debug("testCRUD.queryHwModelByNameAndType-->must be empty-->is null? "+(bean3==null));
        
        bean2.setModelName("测试的硬件类型002(修改)");
        dao.update(bean2);
        bean2=dao.queryDetail(bean2.getModelId());
        log.debug("testCRUD.update-->id:"+bean2.getModelId()+" name:"+bean2.getModelName());      
    }
    
    //@Test
    public void testQueryDeviceByModelId() {
    	List<HwLedgerDeviceBean> list=dao.queryDeviceByModelId("1", "storageType");
        log.debug("testQueryDeviceByModelId-->list size:"+list.size());
    }
}