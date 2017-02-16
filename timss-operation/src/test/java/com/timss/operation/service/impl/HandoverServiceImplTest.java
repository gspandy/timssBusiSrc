package com.timss.operation.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.timss.operation.service.HandoverService;
import com.timss.operation.vo.HandoverVo;
import com.yudean.mvc.testunit.TestUnit;

/**
 * 
 * @title: 测试HandoverServiceImpl
 * @description: {desc}
 * @company: gdyd
 * @className: HandoverServiceImplTest.java
 * @author: huanglw
 * @createDate: 2014年7月22日
 * @updateUser: huanglw
 * @version: 1.0
 */
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@TransactionConfiguration( defaultRollback = true )
public class HandoverServiceImplTest extends TestUnit {

    @Autowired
    private HandoverService handoverService;
    @Test
    public void testCommitHandover() {
        HandoverVo vo = new HandoverVo();
        vo.setCurrentPerson( "890161" );
        vo.setNextPerson( "890167" );
        vo.setNextContent( "测试交班情况" );
        vo.setNextRemark( "测试交班交待" );
        vo.setJobsId( 1 );
        vo.setId( 2 );
        //vo.setNextShiftDate( DateFormatUtil.stringToDateYYYYMMDD( "2014-07-18" ) );
        vo.setNextScheduleId( 18321 );
        //handoverService.commitHandover( vo );
    }

    @Test
    public void testInsertHandoverHandoverVo() {
        HandoverVo vo = new HandoverVo();
        vo.setCurrentPerson( "890161" );
        vo.setNextPerson( "890167" );
        vo.setNextContent( "测试交班情况" );
        vo.setNextRemark( "测试交班交待" );
        vo.setJobsId( 1 );
        vo.setId( 2 );
        //vo.setNextShiftDate( DateFormatUtil.stringToDateYYYYMMDD( "2014-07-18" ) );
        vo.setNextScheduleId( 18321 );
      //  handoverService.insertHandover( vo );
    }


}
