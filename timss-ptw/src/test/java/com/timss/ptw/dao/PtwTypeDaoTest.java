package com.timss.ptw.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;
import com.timss.ptw.bean.PtwType;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PtwTypeDaoTest extends TestUnit {
    @Autowired
    PtwTypeDao ptwTypeDao;
    
    @Test
    public void testQueryTypesBySiteId() {
        List<PtwType> list = ptwTypeDao.queryTypesBySiteId( "SBS",0 );
        for ( PtwType ptwType : list ) {
            System.out.println(ptwType);
        }
    }

}
