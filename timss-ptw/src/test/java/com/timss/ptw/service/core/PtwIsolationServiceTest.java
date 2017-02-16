package com.timss.ptw.service.core;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.ptw.bean.PtwIsolationBean;
import com.timss.ptw.service.PtwIsolationService;
import com.yudean.itc.code.NotificationType;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.manager.support.INotificationManager;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

/**
 * 
 * @title: 隔离证测试
 * @description: {desc}
 * @company: gdyd
 * @className: PtwIsolationServiceTest.java
 * @author: fengzt
 * @createDate: 2014年10月30日
 * @updateUser: fengzt
 * @version: 1.0
 */
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PtwIsolationServiceTest extends TestUnit {
    
    @Autowired
    private PtwIsolationService ptwIsolationService;
    @Autowired
    private IAuthorizationManager authManager;
    @Autowired
    private INotificationManager iNotificationManager;
    
    @Test
    public void testInsertPtwIsolation() {
        TestUnitGolbalService.SetCurentUserById("890167", "ITC");
        PtwIsolationBean ptwIsolationBean = new PtwIsolationBean();
        
        ptwIsolationBean.setWtId( 1374 );
        ptwIsolationBean.setNo( "1111" );
        ptwIsolationBean.setWorkContent( "setWorkContent" );
        
        ptwIsolationBean.setWorkPlace( "setWorkPlace" );
        ptwIsolationBean.setKeyBoxId( 188 );
        ptwIsolationBean.setEqId( "11" );
        
        ptwIsolationBean.setEqNo( "eqNo" );
        ptwIsolationBean.setEqName( "eqName" );
        ptwIsolationBean.setIssuerNo( "200624" );
        
        ptwIsolationBean.setIssuer( "issuer" );
        ptwIsolationBean.setIssuedTime( new Date() );
        ptwIsolationBean.setIssueSuper( "200624" );
        
        ptwIsolationBean.setIssueSuperNo( "200624" );
        ptwIsolationBean.setExecuter( "200624" );
        ptwIsolationBean.setExecuterNo( "200624" );
        
        ptwIsolationBean.setExecuterTime( new Date() );
        ptwIsolationBean.setWithDraw( "200624" );
        ptwIsolationBean.setWithDrawNo( "200624" );
        
        ptwIsolationBean.setWithDrawTime( new Date() );
        ptwIsolationBean.setRemover( "200624" );
        ptwIsolationBean.setRemoverNo( "200624" );
        
        ptwIsolationBean.setRemoverTime( new Date() );
        ptwIsolationBean.setRemark( "remark" );
        ptwIsolationBean.setStatus( 1 );
        
        ptwIsolationBean.setIsStdWt( 1 );
        
      //  int count = ptwIsolationService.insertPtwIsolation( ptwIsolationBean  );
       // System.out.println( "insert = " + count );
    }

    @Test
    public void testQueryPtwIsolationBySite() {
        TestUnitGolbalService.SetCurentUserById("890167", "ITC");
        
        Page<PtwIsolationBean> page = new Page<PtwIsolationBean>();
        page = ptwIsolationService.queryPtwIsolationList( page );
        System.out.println( page.getResults() + "----------");
    }

    @Test
    public void testUpdatePtwIsolation() {
        fail( "Not yet implemented" );
    }

}
