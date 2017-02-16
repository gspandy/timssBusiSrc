package com.timss.asset.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.asset.bean.SwLedgerAppBean;
import com.timss.asset.bean.SwLedgerBean;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml"/*,
"classpath:config/context/applicationContext-webserviceClient-config.xml"*/ })
public class TestSwLedgerDao extends TestUnit {
	static private Logger log = Logger.getLogger(TestSwLedgerDao.class);
	
	@Autowired
	SwLedgerDao dao;
	
	String siteId="ITC"; 
	SwLedgerBean bean1,bean2;
	List<SwLedgerBean> list;
	Page<SwLedgerBean>page;
	SwLedgerAppBean app;
	List<SwLedgerAppBean> list2;
	
	//@Before
	public void before(){
		log.debug("添加前————————>");
		page=new Page<SwLedgerBean>();
		page.setParameter("siteId", siteId);
		queryList();
		
		bean1=new SwLedgerBean();
        bean1.setSiteid(siteId);
        bean1.setSwName("测试的软件台账001");
        if(dao.querySwLedgerByName(siteId, bean1.getSwName())==null){
        	dao.insert(bean1);
            log.debug("testCRUD.insert-->name:"+bean1.getSwName()+" id:"+bean1.getSwId());
        }
        
        bean2=new SwLedgerBean();
        bean2.setSiteid(siteId);
        bean2.setSwName("测试的软件台账002");
        if(dao.querySwLedgerByName(siteId, bean2.getSwName())==null){
	        dao.insert(bean2);
	        log.debug("testCRUD.insert-->name:"+bean2.getSwName()+" id:"+bean2.getSwId());
        }
        
        log.debug("添加后————————>");
        queryList();
	}
	
	//@After
	public void after(){
		log.debug("删除前————————>");
		queryList();
		
		dao.deleteById(bean1.getSwId());
        dao.deleteById(bean2.getSwId());
        
        log.debug("删除后————————>");
        queryList();
	}
	
    //@Test
    public void testCRUD() {
                        
        SwLedgerBean bean=dao.queryDetail(bean1.getSwId());
        log.debug("testCRUD.queryDetail-->id:"+bean.getSwId()+" name:"+bean.getSwName());
        
        bean2.setSwName("测试的软件台账002(修改)");
        dao.update(bean2);
        bean2=dao.queryDetail(bean2.getSwId());
        log.debug("testCRUD.update-->id:"+bean2.getSwId()+" name:"+bean2.getSwName());     
        
        //app
        list2=dao.queryApps(bean1.getSwId());
        log.debug("testCRUD.queryApps-->size:"+list2.size());       

        app=new SwLedgerAppBean();
        app.setAppName("测试的应用001");
        app.setAppType("web");
        app.setSiteid(siteId);
        app.setSwl(bean1);
        if(dao.querySwLedgerAppByName(app.getSwl().getSwId(), app.getAppName())==null){
	        dao.insertApp(app);
	        log.debug("testCRUD.insertApp-->name:"+app.getAppName()+" id:"+app.getAppId());
        }
        
        list2=dao.queryApps(bean1.getSwId());
        log.debug("testCRUD.queryApps-->size:"+list2.size());
        
        app.setAppName("测试的应用001(修改)");
        if(dao.querySwLedgerAppByName(app.getSwl().getSwId(), app.getAppName())==null){
	        dao.updateApp(app);
	        log.debug("testCRUD.updateApp-->name:"+app.getAppName()+" id:"+app.getAppId());
        }
        list2=dao.queryAppsDetail(bean1.getSwId());
        log.debug("testCRUD.queryAppsDetail-->size:"+list2.size()+" name:"+list2.get(0).getAppName());
        
        dao.deleteAppById(app.getAppId());
        list2=dao.queryAppsDetail(bean1.getSwId());
        log.debug("testCRUD.queryAppsDetail-->size:"+list2.size());
    }
    
    private void queryList(){
    	list=dao.queryList(page);
        log.debug("testCRUD.queryList-->size:"+list.size());
    }
    
    @Test
    public void testQuerySwLedgerAppByHwId() {
		List<SwLedgerAppBean> list=dao.querySwLedgerAppByHwId("HW-00000019");
		log.debug("testQuerySwLedgerAppByHwId-->size:"+list.size());
	}
}