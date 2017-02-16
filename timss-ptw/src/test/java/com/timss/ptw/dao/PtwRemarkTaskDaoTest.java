package com.timss.ptw.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;
import com.timss.ptw.bean.PtwRemarkTask;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PtwRemarkTaskDaoTest  extends TestUnit{
    @Autowired
    private PtwRemarkTaskDao ptwRemarkTaskDao;
    @Test
    public void testQueryPtwRemarkTaskByPtwId() {
        System.out.println(ptwRemarkTaskDao.queryPtwRemarkTaskByPtwId( 1354 ));
    }

    @Test
    public void testInsertPtwRemarkTask() {
        PtwRemarkTask ptwRemarkTask = new PtwRemarkTask();
        ptwRemarkTask.setWtId( 1354 );
//        ptwRemarkTaskDao.insertPtwRemarkTask( ptwRemarkTask );
    }

}
