package test.com.timss.inventory.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.bean.InvUnit;
import com.timss.inventory.service.InvUnitService;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class TestUnitService extends TestUnit {
	
	private static final Logger LOG = Logger.getLogger(TestUnitService.class);
	
	@Autowired
    private InvUnitService service;
	
	@Test
	public void testUnitCRUD() throws Exception{
		String code="code";
		String site="ITC";
		InvUnit prev=service.queryUnitByCodeAndSiteId(site, code);
		if(prev==null){
			LOG.debug("TestUnitService.isUnitCodeExist()->N");
		}else {
			LOG.debug("TestUnitService.isUnitCodeExist()->Y");
		}
		
		InvUnit wh=new InvUnit();
		wh.setActive("Y");
		wh.setUnitcode(code);
		wh.setSiteId(site);
		wh.setUnitname("这是一个测试计量单位");
		Integer result=service.insertUnitInfo(wh);
		LOG.debug("TestUnitService.insertUnitInfo()->"+result);
		
		Page<InvUnit>page=new Page<InvUnit>();
		page.setParameter("siteId", site);
		List<InvUnit>list=service.queryUnitListBySiteId(page);
		LOG.debug("TestUnitService.queryUnitListBySiteId()->"+list.size());
		
		LOG.debug("TestUnitService.queryUnitListBySiteId()->"+service.queryUnitDetail(wh.getUnitid(), site).getUnitname());
		
		wh.setDescriptions("这是用来测试更新方法");
		result=service.updateUnitInfo(wh);
		LOG.debug("TestUnitService.updateUnitInfo()->"+result);
		
		result=service.updateUnitState(wh.getUnitid(), site, "N");
		LOG.debug("TestUnitService.updateUnitState()->"+result);
		
		result=service.deleteUnit(wh.getUnitid(), site);
		LOG.debug("TestUnitService.deleteUnit()->"+result);
	}
}
