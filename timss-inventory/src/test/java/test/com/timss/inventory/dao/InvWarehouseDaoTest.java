package test.com.timss.inventory.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.bean.InvWarehouse;
import com.timss.inventory.dao.InvWarehouseDao;
import com.timss.inventory.vo.TreeBean;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvWarehouseDaoTest.java
 * @author: 890166
 * @createDate: 2014-7-19
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class InvWarehouseDaoTest extends TestUnit{

	private static final Logger LOG = Logger.getLogger(InvWarehouseDaoTest.class);
	
	@Autowired
	InvWarehouseDao invWarehouseDao;
	
	@Test
	public void testQueryWarehouse(){
		Page<InvWarehouse> page = new Page<InvWarehouse>();
		page.setParameter("parentId", "ITC");
		List<InvWarehouse> iwList = invWarehouseDao.queryWarehouse(page);
		for(InvWarehouse iw : iwList){
			LOG.debug(iw.getWarehousename());
		}
	}
	
	@Test
	public void testQueryWarehouseNode(){
		List<TreeBean> tbList = invWarehouseDao.queryWarehouseNode("ITC");
		for(TreeBean tb: tbList){
			LOG.debug(tb.getText());
		}
	}
	
	@Test
	public void testQueryWarehouseNodeById(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("siteId", "ITC");
		map.put("warehouseid", "'IWHI001'");
		List<TreeBean> tbList = invWarehouseDao.queryWarehouseNodeById(map);
		for(TreeBean tb:tbList){
			LOG.debug(tb.getText());
		}
	}
	
	@Test
	public void testQueryWarehouseIdByName(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("siteId", "ITC");
		List<String> sList = invWarehouseDao.queryWarehouseIdByName(map);
		for(String s: sList){
			LOG.debug(s);
		}
	}
	
	@Test
	public void testWarehouseCRUD(){
		String code="code";
		String site="ITC";
		Integer codeNum=invWarehouseDao.isWarehouseCodeExist(site, code);
		LOG.debug("InvWarehouseDaoTest.isWarehouseCodeExist()->"+codeNum);
		
		InvWarehouse wh=new InvWarehouse();
		wh.setActive("Y");
		wh.setWarehousecode(code);
		wh.setSiteId(site);
		wh.setWarehousename("这是一个测试仓库");
		Integer result=invWarehouseDao.insertWarehouseInfo(wh);
		LOG.debug("InvWarehouseDaoTest.insertWarehouseInfo()->"+result);
		
		Page<InvWarehouse>page=new Page<InvWarehouse>();
		page.setParameter("siteId", site);
		List<InvWarehouse>list=invWarehouseDao.queryWarehouseListBySiteId(page);
		LOG.debug("InvWarehouseDaoTest.queryWarehouseListBySiteId()->"+list.size());
		
		LOG.debug("InvWarehouseDaoTest.queryWarehouseListBySiteId()->"+invWarehouseDao.queryWarehouseDetail(wh.getWarehouseid(), site).getWarehousename());
		
		wh.setDescriptions("这是用来测试更新方法");
		result=invWarehouseDao.updateWarehouseInfo(wh);
		LOG.debug("InvWarehouseDaoTest.updateWarehouseInfo()->"+result);
		
		result=invWarehouseDao.updateWarehouseState(wh.getWarehouseid(), site, "N");
		LOG.debug("InvWarehouseDaoTest.updateWarehouseState()->"+result);
		
		result=invWarehouseDao.deleteWarehouse(wh.getWarehouseid(), site);
		LOG.debug("InvWarehouseDaoTest.deleteWarehouse()->"+result);
	}
}
