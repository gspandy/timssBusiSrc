package test.com.timss.inventory.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.bean.InvBin;
import com.timss.inventory.service.InvBinService;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class TestBinService extends TestUnit {
	
	private static final Logger LOG = Logger.getLogger(TestBinService.class);
	
	@Autowired
    private InvBinService service;
	
	@Test
	public void testBinCRUD() throws Exception{
		String site="ITC";
		String name="这是一个测试货柜";
		String warehouseid="IWHI3521";
		InvBin prevBin=service.queryBinByNameAndWarehouseId(name, warehouseid);
		if(prevBin==null){
			LOG.debug("TestBinService.isBinNameExist()->N");
		}else {
			LOG.debug("TestBinService.isBinNameExist()->Y");
		}
		
		InvBin wh=new InvBin();
		wh.setActive("Y");
		wh.setWarehouseid(warehouseid);
		wh.setSiteId(site);
		wh.setBinname(name);
		Integer result=service.insertBinInfo(wh);
		LOG.debug("TestBinService.insertBinInfo()->"+result);
		
		Page<InvBin>page=new Page<InvBin>();
		page.setParameter("siteId", site);
		List<InvBin>list=service.queryBinListBySiteId(page);
		LOG.debug("TestBinService.queryBinListBySiteId()->"+list.size());
		
		LOG.debug("TestBinService.queryBinDetail()->"+service.queryBinDetail(wh.getBinid()).getBinname());
		
		wh.setDescriptions("这是用来测试更新方法");
		result=service.updateBinInfo(wh);
		LOG.debug("TestBinService.updateBinInfo()->"+result);
		
		result=service.deleteBin(wh.getBinid());
		LOG.debug("TestBinService.deleteBin()->"+result);
	}
}
