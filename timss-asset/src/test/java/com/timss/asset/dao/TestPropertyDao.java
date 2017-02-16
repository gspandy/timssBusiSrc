package com.timss.asset.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.asset.bean.PropertyBean;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class TestPropertyDao extends TestUnit {
	static private Logger log = Logger.getLogger(TestPropertyDao.class);
	
	@Autowired
	private PropertyDao dao;
    
    @Test
    public void testCRUD() {
    	String siteId="SCC";
    	String parentId=dao.queryPropertyRootIdBySite(siteId);
    	
        PropertyBean bean1=new PropertyBean();
        bean1.setHouseName("一个简单房产");
        bean1.setHouseType("house");
        bean1.setParentId(parentId);
        bean1.setSiteid(siteId);
        dao.insert(bean1);
        log.debug("testCRUD.insert-->name:"+bean1.getHouseName()+" id:"+bean1.getHouseId());
        
        PropertyBean bean2=new PropertyBean();
        bean2.setHouseName("一个复杂房间");
        bean2.setHouseType("room");
        bean2.setParentId(parentId);
        bean2.setSiteid(siteId);
        bean2.setAirConditioner("AirConditioner");
        bean2.setArea(1234.56);
        bean2.setDecoration("Decoration");
        bean2.setElectricalAppliances("ElectricalAppliances");
        bean2.setFireControl("FireControl");
        bean2.setManagementCost(4321.0);
        bean2.setOfficeSupplies("OfficeSupplies");
        bean2.setOwnerName("OwnerName");
        bean2.setRightOwner("RightOwner");
        bean2.setUnit("元");
        dao.insert(bean2);
        log.debug("testCRUD.insert-->name:"+bean2.getHouseName()+" id:"+bean2.getHouseId());
        
        Page<PropertyBean>page=new Page<PropertyBean>();
        page.setParameter("propertyId", parentId);
        List<PropertyBean> list=dao.queryList(page);
        log.debug("testCRUD.queryList-->size:"+list.size());
        
        PropertyBean bean=dao.queryDetail(parentId);
        log.debug("testCRUD.queryDetail-->id:"+bean.getHouseId()+" name:"+bean.getHouseName());
        
        bean2.setHouseName("一个复杂房间(修改)");
        dao.update(bean2);
        bean2=dao.queryDetail(bean2.getHouseId());
        log.debug("testCRUD.update-->id:"+bean2.getHouseId()+" name:"+bean2.getHouseName());
        
        dao.deleteById(bean1.getHouseId());
        dao.deleteById(bean2.getHouseId());
        list=dao.queryList(page);
        log.debug("testCRUD.deleteById-->size now:"+list.size());
    }
}