package test.com.timss.inventory.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.bean.InvCategory;
import com.timss.inventory.dao.InvCategroyDao;
import com.timss.inventory.vo.InvCategoryVO;
import com.timss.inventory.vo.TreeBean;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvCategroyDaoTest.java
 * @author: 890166
 * @createDate: 2014-7-19
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class InvCategroyDaoTest extends TestUnit{
	
	private static final Logger LOG = Logger.getLogger(InvCategroyDaoTest.class);
	
	@Autowired
	InvCategroyDao invCategroyDao;
	
	@Test
	public void testQueryCategroyNodeById(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("categoryid", "'ICI0001001'");
		map.put("siteId", "ITC");
		
		List<TreeBean> tbList = invCategroyDao.queryCategroyNodeById(map);
		for(TreeBean tb: tbList){
			LOG.debug("物资分类内容=========>"+tb.getText());
		}
	}
	
	@Test
	public void testQueryCategroyIdByName(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("siteId", "ITC");
		
		List<String> sList = invCategroyDao.queryCategroyIdByName(map);
		for(String s: sList){
			LOG.debug(s);
		}
	}
	
	@Test
	public void testQueryInvCategroyDetail(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("cateId", "ICI890019");
		map.put("siteId", "ITC");
		List<InvCategoryVO> icvList = invCategroyDao.queryInvCategroyDetail(map);
		for(InvCategoryVO icv:icvList){
			LOG.debug("物资分类名称=========>"+icv.getInvcatename());
		}
	}
	
	@Test
	public void testQueryInvCategroyDetailByParentId(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("parentid", "IWHI3522");
		map.put("siteId", "ITC");
		
		List<InvCategoryVO> icvList = invCategroyDao.queryInvCategroyDetailByParentId(map);
		for(InvCategoryVO icv:icvList){
			LOG.debug("物资分类名称=========>"+icv.getInvcatename());
		}
	}
	
	@Test
	public void testQueryCategroyLevelOne(){
		Page<InvCategoryVO> page = new Page<InvCategoryVO>();
		page.setParameter("parentid", "IWHI3522");
		page.setParameter("siteId", "ITC");
		
		List<InvCategoryVO> icvList = invCategroyDao.queryCategroyLevelOne(page);
		for(InvCategoryVO icv:icvList){
			LOG.debug("物资分类名称=========>"+icv.getInvcatename());
		}
	}
	
	@Test
	public void testOperInvCategroy(){
		InvCategory ic = new InvCategory();
		ic.setInvcatename("testing");
		ic.setSiteId("ITC");
		int count = invCategroyDao.insertInvCategroy(ic);
		LOG.debug("插入数据数=========>"+count);
		
		ic.setStatus("ACTIVE");
		count = invCategroyDao.updateInvCategroy(ic);
		LOG.debug("更新数据数=========>"+count);
		
		count = invCategroyDao.deleteCategroyById(ic.getInvcateid());
		LOG.debug("删除数据数=========>"+count);
	}
	
}
