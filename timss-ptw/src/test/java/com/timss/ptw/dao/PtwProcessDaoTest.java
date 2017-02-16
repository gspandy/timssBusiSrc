package com.timss.ptw.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PtwProcessDaoTest extends TestUnit{
    @Autowired
    private PtwProcessDao ptwProcessDao;
    @Test
    public void testQueryPtwProcessByPtwId() {
        System.out.println(ptwProcessDao.queryPtwProcessByPtwId( 1532 ));
    }

    @Test
    public void testQueryPtwProcessByWtIdAndStatus() {
        
    }

    @Test
    public void testInsertPtwProcess() {
    }

}
