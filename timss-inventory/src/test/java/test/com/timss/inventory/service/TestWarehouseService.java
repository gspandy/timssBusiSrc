package test.com.timss.inventory.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.bean.InvWarehouse;
import com.timss.inventory.service.InvWarehouseService;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class TestWarehouseService extends TestUnit {
	
	private static final Logger LOG = Logger.getLogger(TestWarehouseService.class);
	
	@Autowired
    private InvWarehouseService service;
	
	@Test
	public void testWarehouseCRUD() throws Exception{
		String code="code";
		String site="ITC";
		Integer codeNum=service.isWarehouseCodeExist(site, code);
		LOG.debug("TestWarehouseService.isWarehouseCodeExist()->"+codeNum);
		
		InvWarehouse wh=new InvWarehouse();
		wh.setActive("Y");
		wh.setWarehousecode(code);
		wh.setSiteId(site);
		wh.setWarehousename("这是一个测试仓库");
		Integer result=service.insertWarehouseInfo(wh);
		LOG.debug("TestWarehouseService.insertWarehouseInfo()->"+result);
		
		Page<InvWarehouse>page=new Page<InvWarehouse>();
		page.setParameter("siteId", site);
		List<InvWarehouse>list=service.queryWarehouseListBySiteId(page);
		LOG.debug("TestWarehouseService.queryWarehouseListBySiteId()->"+list.size());
		
		LOG.debug("TestWarehouseService.queryWarehouseListBySiteId()->"+service.queryWarehouseDetail(wh.getWarehouseid(), site).getWarehousename());
		
		wh.setDescriptions("这是用来测试更新方法");
		result=service.updateWarehouseInfo(wh);
		LOG.debug("TestWarehouseService.updateWarehouseInfo()->"+result);
		
		result=service.updateWarehouseState(wh.getWarehouseid(), site, "N");
		LOG.debug("TestWarehouseService.updateWarehouseState()->"+result);
		
		result=service.deleteWarehouse(wh.getWarehouseid(), site);
		LOG.debug("TestWarehouseService.deleteWarehouse()->"+result);
	}
}
