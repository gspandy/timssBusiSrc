package test.com.timss.inventory.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.bean.InvBin;
import com.timss.inventory.dao.InvBinDao;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvBinDaoTest.java
 * @author: 890166
 * @createDate: 2014-7-19
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class InvBinDaoTest extends TestUnit{

	private static final Logger LOG = Logger.getLogger(InvBinDaoTest.class);
	
	@Autowired
	InvBinDao invBinDao;
	
	@Test
	public void testQueryBinByCategory(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("categoryId", "ICI0001001");
		List<InvBin> ibList = invBinDao.queryBinByCategory(map);
		for(InvBin ib: ibList){
			LOG.debug("货柜名称："+ib.getBinname());
		}
	}
	
	@Test
	public void testBinCRUD(){
		String site="ITC";
		String name="这是一个测试货柜";
		String warehouseid="IWHI3521";
		InvBin prevBin=invBinDao.queryBinByNameAndWarehouseId(name, warehouseid);
		if(prevBin==null){
			LOG.debug("InvBinDaoTest.isBinNameExist()->N");
		}else {
			LOG.debug("InvBinDaoTest.isBinNameExist()->Y");
		}
		
		InvBin wh=new InvBin();
		wh.setActive("Y");
		wh.setWarehouseid(warehouseid);
		wh.setSiteId(site);
		wh.setBinname(name);
		Integer result=invBinDao.insertBinInfo(wh);
		LOG.debug("InvBinDaoTest.insertBinInfo()->"+result);
		
		Page<InvBin>page=new Page<InvBin>();
		page.setParameter("siteId", site);
		List<InvBin>list=invBinDao.queryBinListBySiteId(page);
		LOG.debug("InvBinDaoTest.queryBinListBySiteId()->"+list.size());
		
		LOG.debug("InvBinDaoTest.queryBinDetail()->"+invBinDao.queryBinDetail(wh.getBinid()).getBinname());
		
		wh.setDescriptions("这是用来测试更新方法");
		result=invBinDao.updateBinInfo(wh);
		LOG.debug("InvBinDaoTest.updateBinInfo()->"+result);
		
		result=invBinDao.deleteBin(wh.getBinid());
		LOG.debug("InvBinDaoTest.deleteBin()->"+result);
	}
}
