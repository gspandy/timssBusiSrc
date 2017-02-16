package com.timss.ptw.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;
import com.timss.ptw.bean.PtwFireInfo;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PtwFireInfoDaoTest extends TestUnit{
    @Autowired
    PtwFireInfoDao ptwFireInfoDao;
    //@Test
    public void testQueryPtwFireInfoByWtId() {
        System.out.println(ptwFireInfoDao.queryPtwFireInfoByWtId( 1354 ));
    }

//    @Test
    public void testQueryFireIdsByAttachWtId() {
        System.out.println(ptwFireInfoDao.queryFireIdsByAttachWtId( 1 ));
    }

    //@Test
    public void testInsertPtwFireBaseInfo() {
        PtwFireInfo ptwFireInfo = new PtwFireInfo();
        ptwFireInfo.setWtId( 1563 );
        ptwFireInfo.setAttachWtId( 1561 );
        ptwFireInfo.setFireUnit( "test" );
        ptwFireInfo.setFireWc( "test" );
        ptwFireInfoDao.insertPtwFireBaseInfo( ptwFireInfo );
    }

    @Test
    public void testUpdatePtwFireBaseInfo() {
        PtwFireInfo ptwFireInfo = new PtwFireInfo();
        ptwFireInfo.setWtId( 1563 );
        ptwFireInfoDao.updatePtwFireBaseInfo( ptwFireInfo );
    }

    //@Test
    public void testUpdatePtwFireLicInfo() {
    }

    //@Test
    public void testUpdatePtwFireFlowApprInfo() {
    }

    //@Test
    public void testUpdatePtwFireFlowConfirmInfo() {
    }

    //@Test
    public void testUpdatePtwFireFlowFinInfo() {
    }

    //@Test
    public void testUpdatePtwFirePic() {
    }

}
