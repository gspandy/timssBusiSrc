package com.timss.operation.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.itc.dto.Page;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.testunit.TestUnit;

import com.timss.operation.bean.Patrol;
import com.timss.operation.util.ProcessStatusUtil;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@Transactional
public class PatrolDaoTest extends TestUnit{
	
	@Autowired
	PatrolDao patrolDao;
	
	@Test
	public void insertPatrol() {
		Patrol patrolBean = new Patrol();
        patrolBean.setPatrolId(UUIDGenerator.getUUID());
    	patrolBean.setCreatedate( new Date() );
    	patrolDao.insertPatrol(patrolBean);
    	String patrolId = patrolBean.getPatrolId();
        assertNotNull(patrolId);
	}
	
	@Test
	public void deletePatrolById() {
        int count = patrolDao.deletePatrolById( "aaa" );
        assertEquals(0, count);
        
        count = patrolDao.deletePatrolById( "2jj3y4iz56o3m2e0" );
        assertEquals(1, count);
	}
	
	@Test
	public void updatePatrol() {
		Patrol patrolBean = new Patrol();
    	patrolBean.setModifydate( new Date() );
        patrolBean.setPatrolId("2jj3y4iz56o3m2e0");
    	int count = patrolDao.updatePatrol( patrolBean );
        assertEquals(1, count);
        
        patrolBean = new Patrol();
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "instantId", "000");
        parmas.put( "status", "作废" );//业务数据状态更新为已作废
        parmas.put( "patrolId", "2jj3y4iz56o3m2e0" );
        count = patrolDao.updatePatrolStatus(parmas);
        assertEquals(1, count);
	}	
	
	@Test
	public void queryPatrol(){
		Patrol patrolBean = patrolDao.queryPatrolById("wujd25plj3c7njsk");
		System.out.println(patrolBean.toString());
		
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( 1 );
        pageVo.setPageSize( 15 );
    	pageVo.setSortKey( "createdate" );
    	pageVo.setSortOrder( "DESC" );
        List<Patrol> patrolList = patrolDao.queryPatrolByPage( pageVo );
        System.out.println(patrolList);
        
        HashMap<String, Object> map = new HashMap<String, Object>();
    	pageVo.setParams( map );
        map.put("search", "无");
        patrolList = patrolDao.queryPatrolBySearch( pageVo );
        System.out.println(patrolList);
        
	}

}
