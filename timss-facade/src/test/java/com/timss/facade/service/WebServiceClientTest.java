package com.timss.facade.service;

//import static org.junit.Assert.assertTrue;
//
//import java.util.Date;
//
//import org.apache.log4j.Logger;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.yudean.itc.code.ActiveStatus;
//import com.yudean.itc.webservice.HhcWebEamItemService;
//import com.yudean.itc.webservice.HhcWebEamPrService;
//import com.yudean.itc.webservice.bean.EamItem;
//import com.yudean.itc.webservice.bean.EamPR;
//import com.yudean.itc.webservice.bean.EamPRCom;
//import com.yudean.itc.webservice.bean.EamPRLine;
//import com.yudean.mvc.testunit.TestUnit;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml",
//		"classpath:config/context/applicationContext-webserviceClient-config.xml" })
//public class WebServiceClientTest extends TestUnit {
//private static final Logger log = Logger.getLogger(WebServiceClientTest.class);
//	
////	@Autowired
////	HhcWebEamItemService eamItemService;//物资主项目接口
////	
////	@Autowired
////	HhcWebEamPrService eamPrService;//采购订单接口
////	
////	
////	@Test
////	@Transactional
////	//测试增删改查物资主项目数据
////	public void eamItemTest(){
////		try {
////			String itemNUm = "M89012333";
////			String siteId = "SBS";
////			
////			//测试查询物资主表物资
////			EamItem eamItem = eamItemService.getEamItem("M200441", "SBS");
////			log.info(eamItem);
////			assertTrue(null != eamItem);
////			
////			//测试新增物资主表
////			EamItem eamItemInsert = new EamItem();
////			eamItemInsert.setClaes("工具");
////			eamItemInsert.setCufigurenum("101");
////			eamItemInsert.setCumaterial("90");
////			eamItemInsert.setCumodel("测试物资");
////			eamItemInsert.setDescription("测试物资描述");
////			eamItemInsert.setIssueunit("个");
////			eamItemInsert.setItemid(909120123);
////			eamItemInsert.setItemnum(itemNUm);
////			eamItemInsert.setOrderunit("个");
////			eamItemInsert.setRowstamp("123");
////			eamItemInsert.setStatus(ActiveStatus.ACTIVE);
////			eamItemInsert.setStatusdate(new Date());
////			eamItemInsert.setYudeannum("899ss");
////			eamItemInsert.setCusiteid(siteId);
////			int ret = eamItemService.addEamItem(eamItemInsert);
////			log.info(ret);
////			assertTrue(ret > 0);
////			
////			//测试修改物资主表
////			eamItemInsert.setClaes("工具修改物资类别");
////			eamItemInsert.setDescription("测试修改物资");
////			ret = eamItemService.modifyEamItem(eamItemInsert);
////			log.info(ret);
////			assertTrue(ret > 0);
////			
////			//测试删除物资主表
////			ret = eamItemService.deleteEamItem(itemNUm, siteId);
////			log.info(ret);
////			assertTrue(ret > 0);
////		} catch (Exception e) {
////			log.error("测试异常", e);
////			assertTrue(false);
////		}
////		
////	}
////	
////	@Test
////	@Transactional
////	//测试增删改查采购订单数据
////	public void eamPrTest(){
////		try{
////			EamPRCom eamPrCom = new EamPRCom();
////			
////			//测试添加采购申请单
////			String prnum = "PA20140319005";
////			String siteId = "ITC";
////			
////			Long prLineId1 = 123123L;
////			String setItemId1 = "M200441";
////			
////			Long prLineId2 = 123124L;
////			String setItemId2 = "M200440";
////			
////			EamPR eamPrInsert = new EamPR();
////			eamPrInsert.setPrnum(prnum);
////			eamPrInsert.setRequireddate(new Date());
////			eamPrInsert.setRequestedby("TEST");
////			eamPrInsert.setDescription("測試物資");
////			eamPrInsert.setChangeby("TESTCHANGE");
////			eamPrInsert.setChangedate(new Date());
////			eamPrInsert.setSiteid(siteId);
////			eamPrInsert.setStatusdate(new Date());
////			
////			eamPrCom.setEamPR(eamPrInsert);
////			//测试添加采购申请物资
////			EamPRLine eamPRLine1 = new EamPRLine();
////
////			eamPRLine1.setLoadedcost(123.123D);
////			eamPRLine1.setDescription("添加物资测试1");
////			eamPRLine1.setPrnum(prnum);
////			eamPRLine1.setPrlineid(prLineId1);
////			eamPRLine1.setPrlinenum(1L);
////			eamPRLine1.setItemnum(setItemId1);
////			eamPRLine1.setOrderqty(21.92D);
////			eamPRLine1.setOrderunit("台");
////			eamPRLine1.setModelnum("规格型号");
////			eamPRLine1.setEnterdate(new Date());
////			eamPRLine1.setEnterby("测试用户");
////			eamPRLine1.setRequestedby("测试需求用户");
////			eamPRLine1.setLinecost(123.01D);
////			eamPRLine1.setTax1(1.01D);
////			eamPRLine1.setSiteid(siteId);
////			eamPRLine1.setOrgid("YUDEAN");
////			
////			//测试添加采购申请物资
////			EamPRLine eamPRLine2 = new EamPRLine();
////
////			eamPRLine2.setLoadedcost(123.123D);
////			eamPRLine2.setDescription("添加物资测试2");
////			eamPRLine2.setPrnum(prnum);
////			eamPRLine2.setPrlineid(prLineId2);
////			eamPRLine2.setPrlinenum(1L);
////			eamPRLine2.setItemnum(setItemId2);
////			eamPRLine2.setOrderqty(21.92D);
////			eamPRLine2.setOrderunit("台1");
////			eamPRLine2.setModelnum("规格型号");
////			eamPRLine2.setEnterdate(new Date());
////			eamPRLine2.setEnterby("测试用户2");
////			eamPRLine2.setRequestedby("测试需求用户2");
////			eamPRLine2.setLinecost(123.01D);
////			eamPRLine2.setTax1(1.01D);
////			eamPRLine2.setSiteid(siteId);
////			eamPRLine2.setOrgid("YUDEAN");
////			
////			eamPrCom.addEamPRLines(eamPRLine1);
////			eamPrCom.addEamPRLines(eamPRLine2);
////			
////			int ret = eamPrService.addEamPR(eamPrCom);
////			assertTrue(ret > 0);
////			
////			EamPRCom eamPRCom = eamPrService.getEamPR(prnum, siteId);
////			assertTrue(null != eamPRCom);
////			
////			//测试修改采购申请单
////			EamPR eamPrModify = eamPRCom.getEamPR();
////			eamPrModify.setDescription("测试物资修改");
////			eamPrModify.setChangeby("890128");
////			eamPrModify.setChangedate(new Date());
////			ret = eamPrService.modifyEamPR(eamPRCom);
////			assertTrue(ret > 0);
////			eamPRCom = eamPrService.getEamPR(prnum, siteId);
////			assertTrue(null != eamPRCom);
////
////			//测试删除采购申请单
////			ret = eamPrService.deleteEamPR(prnum, siteId);
////			assertTrue(ret > 0);
////			
////			log.info("测试成功");
////		}catch(Exception e){
////			log.error("测试异常", e);
////			assertTrue(false);
////		}
////	}
//}
