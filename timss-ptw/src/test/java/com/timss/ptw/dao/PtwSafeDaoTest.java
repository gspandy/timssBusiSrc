package com.timss.ptw.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;
import com.timss.ptw.bean.PtwInfo;
import com.timss.ptw.bean.PtwSafe;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PtwSafeDaoTest extends TestUnit{
    
    @Autowired
    private PtwSafeDao ptwSafeDao;

    //@Test
    public void testInsertPtwSafe() {
        PtwSafe ptwSafe = new PtwSafe();
        ptwSafe.setWtId( 1612 );
        ptwSafe.setSafeOrder( 1 );
        ptwSafe.setSafeType( 1 );
        ptwSafe.setSafeContent( "abc" );
        ptwSafeDao.insertPtwSafe( ptwSafe );
    }
    
    @Test
    public void testQueryPtwSafeListByWtId() {
        List<PtwSafe> ptwSafes = ptwSafeDao.queryPtwSafeListByWtId( 1612 );
        for ( PtwSafe ptwSafe : ptwSafes ) {
            System.out.println(ptwSafe);
        }
    }

    //@Test
    public void testBatchInsertPtwSafe() {
        PtwSafe ptwSafe = new PtwSafe();
        ptwSafe.setWtId( 1612 );
        ptwSafe.setSafeOrder( 1 );
        ptwSafe.setSafeType( 1 );
        ptwSafe.setSafeContent( "abc" );
        
        List<PtwSafe> ptwSafes = new ArrayList<PtwSafe>();
        ptwSafes.add( ptwSafe );
        ptwSafeDao.batchInsertPtwSafe( ptwSafes );
        ptwSafes = ptwSafeDao.queryPtwSafeListByWtId( 1612 );
        for ( PtwSafe ptwSafe1 : ptwSafes ) {
            System.out.println(ptwSafe1);
        }
    }

    //@Test
    public void testDeletePtwSafeByWtId() {
        ptwSafeDao.deletePtwSafeByWtId( 1612 );
    }

}
