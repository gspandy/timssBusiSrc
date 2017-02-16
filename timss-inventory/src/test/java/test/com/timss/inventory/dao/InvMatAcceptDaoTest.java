package test.com.timss.inventory.dao;


import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvMatAccept;
import com.timss.inventory.bean.InvMatAcceptDetail;
import com.timss.inventory.dao.InvMatAcceptDao;
import com.timss.inventory.dao.InvMatAcceptDetailDao;
import com.timss.inventory.vo.InvMatAcceptVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatAcceptDaoTest.java
 * @author: 890145
 * @createDate: 2015-10-30
 * @updateUser: 890145
 * @version: 1.0
 */

public class InvMatAcceptDaoTest extends TestUnit{
	
	@Autowired
	InvMatAcceptDao invMatAcceptDao;
	
	@Autowired
	InvMatAcceptDetailDao invMatAcceptDetailDao;
	
	
	
	@Transactional
	@Test
    @Rollback(true)
	public void test5(){
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");
		InvMatAccept invMatAccept=new InvMatAccept();
		InvMatAcceptDetail invMatAcceptDetail=new InvMatAcceptDetail();
		
		List<InvMatAccept> invMatAccepts=invMatAcceptDao.queryListByMatAccept(invMatAccept);
		Assert.assertEquals(true, invMatAccepts.size()!=0);
		Page<?> page=new Page<InvMatAcceptVo>();
		page.setParameter("siteid", "SWF");
		page.setParameter("deliveryDay", "2015");
		List<InvMatAcceptVo> matAcceptVos=invMatAcceptDao.queryInvMatAcceptList(page);
		Assert.assertEquals(false, matAcceptVos.size()!=0);
		
	}

}
