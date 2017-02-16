package com.timss.ptw.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PtwWaitRestoreDaoTest extends TestUnit{
    @Autowired
    private PtwWaitRestoreDao ptwWaitRestoreDao;
    @Test
    public void testQueryPtwWaitRestoreByPtwId() {
        ptwWaitRestoreDao.queryPtwWaitRestoreByPtwId( 1354 );
    }

    @Test
    public void testInsertPtwWait() {
    }

    @Test
    public void testUpdatePtwRestore() {
    }

}
