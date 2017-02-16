package test.com.timss.inventory.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.bean.InvWarehouseItem;
import com.timss.inventory.dao.InvWarehouseItemDao;
import com.timss.inventory.vo.InvWarehouseItemVO;
import com.yudean.mvc.testunit.TestUnit;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvWarehouseItemDaoTest.java
 * @author: 890166
 * @createDate: 2014-7-19
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class InvWarehouseItemDaoTest extends TestUnit{

	private static final Logger LOG = Logger.getLogger(InvWarehouseItemDaoTest.class);
	
	@Autowired
	InvWarehouseItemDao invWarehouseItemDao;
	
	@Test
	public void testInsertInvWarehouseItem(){
		InvWarehouseItem iwi = new InvWarehouseItem();
		iwi.setSiteId("ITC");
		iwi.setCreatedate(new Date());
		int count = invWarehouseItemDao.insertInvWarehouseItem(iwi);
		LOG.debug("插入数据条数为=========>"+count);
	}
	
	@Test
	public void testQueryInvWarehouseItem(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("itemid", "1");
		map.put("siteid", "ITC");
		List<InvWarehouseItemVO> iwivList = invWarehouseItemDao.queryInvWarehouseItem(map);
		for(InvWarehouseItemVO iwiv:iwivList){
			LOG.debug("查询仓库名为=========>"+iwiv.getWarehousename());
		}
	}
}
