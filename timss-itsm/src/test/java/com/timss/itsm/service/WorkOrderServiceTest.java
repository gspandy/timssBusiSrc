package com.timss.itsm.service;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.itsm.bean.ItsmWorkOrder;
import com.timss.itsm.dao.ItsmWorkOrderDao;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.timss.itsm.util.ItsmConstant;
import com.timss.itsm.vo.ItsmWorkTimeVo;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class WorkOrderServiceTest  extends TestUnit  {
	@Autowired
	ItsmWorkOrderService workOrderService ;
	@Autowired
	ItsmWorkOrderDao workOrderDao;
	@Autowired
	ItsmWoStatisticUtilService itsmWoStatisticUtilService;
	@Autowired
	ItsmWorkTimeService workTimeService;
	
	//@Test
	public void testInsertWorkOrder() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWorkOrder() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWOAddPTWId() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateOperUserById() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryAllWO() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryWOById() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryItWOById() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryWOBaseInfoByWOCode() {
		fail("Not yet implemented");
	}

	//@Test
	public void testGetNextWOId() {
		fail("Not yet implemented");
	}

	//@Test
	public void testSaveWorkOrder() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWOStatus() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWOHandlerStyle() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWOOnPlan() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWOOnAcceptance() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWOOnReport() {
		fail("Not yet implemented");
	}

	//@Test
	public void testDeleteWorkOrder() {
		fail("Not yet implemented");
	}

	//@Test
	public void testInPtwToNextStep() {
		fail("Not yet implemented");
	}

	//@Test
	public void testStopWorkOrder() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWorkflowId() {
		fail("Not yet implemented");
	}

	//@Test
	public void testLoginUserIsCusSer() {
		TestUnitGolbalService.SetCurentUserById("890170", "ITC");
		boolean flag = workOrderService.userInGroupOrRole("ITC_WO_KF","role","890152","ITC");
		System.out.println("result:"  +  flag);
	}
	
	@Test
	public void testaddResponTimeLen() throws Exception {
		TestUnitGolbalService.SetCurentUserById("890152", "ITC");
		//设置分段查询统计的参数
		int recordSum = workOrderDao.queryAddResponTimeLenSum();
		int selectSize = ItsmConstant.selectSize ;  //每次查询出selectSize条记录
		int selectNum = (recordSum + selectSize-1)/selectSize ; //需要查询多少次
		for (int n = 0; n < selectNum; n++) {
			List<ItsmWorkOrder> woListOfMonth  = workOrderDao.queryAddResponTimeLen(n,selectSize);//上月参加统计的工单
			Date discovertime = null;
			Date begintime = null;
			Date endtime = null;
			for (int i = 0; i < woListOfMonth.size(); i++) {
				int respondLen = 0 ;
				int solveLen = 0 ;
				ItsmWorkOrder tempWo = woListOfMonth.get(i);
				discovertime = tempWo.getDiscoverTime();
				begintime = tempWo.getBeginTime();
				endtime = tempWo.getEndTime();
				
				if(begintime != null){
					ItsmWorkTimeVo workTimeVo1 = itsmWoStatisticUtilService.setWorkTimeVo(discovertime, begintime);
					respondLen = (int)(workTimeService.calDay(workTimeVo1)*workTimeVo1.getWorkTime()*60*60);
					if(respondLen<0){
						respondLen=0;
					}
				}
				if(endtime != null){
					ItsmWorkTimeVo workTimeVo1 = itsmWoStatisticUtilService.setWorkTimeVo(discovertime, endtime);
					solveLen = (int)(workTimeService.calDay(workTimeVo1)*workTimeVo1.getWorkTime()*60*60);
				}
				
				Map<String, Integer> parmas = new HashMap<String, Integer>();
				parmas.put("id", tempWo.getId());
				if(respondLen >= 0){
					parmas.put("respondLen", respondLen);
				}
				if(solveLen >= 0){
					parmas.put("solveLen", solveLen);
				}
//				System.out.println(tempWo.getDiscoverTime()+","+tempWo.getBeginTime()+","+tempWo.getEndTime());
//				System.out.println(tempWo.getWorkOrderCode()+","+respondLen+","+solveLen);
				workOrderDao.updateRespondSolveLen(parmas);
				
			}
		
		}
	}

}
