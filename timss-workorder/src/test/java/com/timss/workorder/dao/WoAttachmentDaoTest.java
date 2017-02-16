package com.timss.workorder.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.workorder.bean.WoAttachment;
import com.timss.workorder.service.WoUtilService;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={
		"classpath:config/context/applicationContext-webserviceClient-config.xml",
		"classpath:config/context/applicationContext-workflow.xml"})
public class WoAttachmentDaoTest extends TestUnit  {
	@Autowired
	WoAttachmentDao woAttachmentDao;
	@Autowired
	WoUtilService woUtilService;
	//@Test
	public void testInsertWoAttachment() {
	  WoAttachment woAttachment = new WoAttachment();
	  woAttachment.setAttachId("123");
	  woAttachment.setId(String.valueOf(1));
	  woAttachment.setLoadPhase("woPlan");
	  woAttachment.setLoadTime(new Date());
	  woAttachment.setLoadUser("890107");
	  woAttachment.setSiteid("ITC");
	  woAttachment.setType("wo");
	  woAttachment.setYxbz(1);
	  woAttachmentDao.insertWoAttachment(woAttachment);
	}
	
	//@Test
	public void testDeleteAttachMatch(){
		woAttachmentDao.deleteWoAttachment("521", "142163405671096", "MTP","890107");
	}
	
	//@Test
	public void testQueryWoAttachmentById() {
		List<WoAttachment> list = woAttachmentDao.queryWoAttachmentById("1638", "WO",null);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getId()+":"+list.get(i).getAttachId());
		}
	}

	//@Test
	public void test() {
		ArrayList<String>  newIdsx = new ArrayList<String>();
		ArrayList<String>  oldIdsx = new ArrayList<String>();
		newIdsx.add("1");
		newIdsx.add("2");
		newIdsx.add("3");
		//newIdsx.add("4");
		

		oldIdsx.add("1");
		oldIdsx.add("2");
		oldIdsx.add("4");
		oldIdsx.add("6");
		
		ArrayList<String>  newIds = (ArrayList<String>) newIdsx.clone();
		ArrayList<String>  oldIds = (ArrayList<String>) oldIdsx.clone();
		
		for (int i = newIds.size()-1; i >=0; i--) {
			String str1 = newIds.get(i);
			for (int j = oldIds.size()-1; j >=0 ; j--) {
				String str2 = oldIds.get(j);
				if(str1.equals(str2)){
					newIds.remove(i);
					oldIds.remove(j);
				}
			}
		}
		System.out.println(oldIdsx);
		System.out.println(oldIds);
		System.out.println("-------------------------------");
		System.out.println(newIdsx);
		System.out.println(newIds);
	}
	
	//@Test
	public void test2() {
		String fileIds ="1,2,3,4";
		String[] ids=fileIds.split(",");
		
		ArrayList<String> newIds = (ArrayList<String>) java.util.Arrays.asList(ids);  
		
		System.out.println(newIds);
	}
}
