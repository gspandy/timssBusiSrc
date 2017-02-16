package com.timss.itsm.service;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.timss.itsm.service.ItsmWoStatisService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={
		"classpath:config/context/applicationContext-webserviceClient-config.xml",
		"classpath:config/context/applicationContext-workflow.xml"})
public class ItWoStatisServiceTest  extends TestUnit {

	@Autowired
	ItsmWoStatisService itWoStatisService ;
	@Autowired
    private IAuthorizationManager authManager;
	
	//@Test
	@Transactional
	public void testQueryItWoStatisticVO() throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date beginTime = sdf.parse("2014-11-08");
		itWoStatisService.queryItWoStatisticVO(beginTime, new Date(), "ITC", "requstStatistic");
	}

	
//	@Test
	@Transactional
	public void testListRemoveAll() throws Exception {
		String faultTypePrincipalGroup = "ITC_WO_FTTYGTPT";
		List<SecureUser> resultList = authManager.retriveUsersWithSpecificGroup(faultTypePrincipalGroup, null, false, true);
		//TODO 根据工单的服务目录，选择用户组或者用户，然后计算出每个用户的手上工单数量
		List<SecureUser> nextResultList = authManager.retriveUsersWithSpecificRole("ITC_ITSM_WHGCS", null, false, true);
		nextResultList.removeAll(resultList);
		resultList.addAll(nextResultList);
	}
	
	@Test
	@Transactional
	public void testGetMaxIndex() throws Exception {
		double[] ratioArray = new double[5];
		ratioArray[0]=5.6;
		ratioArray[1]=3.4;
		ratioArray[2]=5.7;
		ratioArray[3]=2.0;
		ratioArray[4]=8.4;
		for (int i = 0; i < ratioArray.length; i++) {
			int index = getMaxItemIndx(ratioArray);
			System.out.println(index);
			ratioArray[index]=0;
		}
		
	}
	
	private int getMaxItemIndx(double[] ratioArray) {
		int index = -1 ;
		double value = 0;
		for (int i = 0; i < ratioArray.length; i++) {
			double tempValue = ratioArray[i];
			if(tempValue > value){
				value = tempValue;
				index = i;
			}
		}
		return index;
	}
}
