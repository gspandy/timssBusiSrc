package com.timss.ptw.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PtwTypeDefineDaoTest extends TestUnit {
    @Autowired
    PtwTypeDefineDao ptwTypeDefineDao;
    
    @Test
    public void testQueryPtwTypeDefineById() {
        System.out.println(ptwTypeDefineDao.queryPtwTypeDefineById( 14 ));
    }

}
