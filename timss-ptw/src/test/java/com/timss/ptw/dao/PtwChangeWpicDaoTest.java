package com.timss.ptw.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.ptw.bean.PtwChangeWpic;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PtwChangeWpicDaoTest  extends TestUnit{
    @Autowired
    private PtwChangeWpicDao ptwChangeWpicDao;
    @Test
    public void testQueryPtwChangeWpicByPtwId() {
        System.out.println(ptwChangeWpicDao.queryPtwChangeWpicByPtwId( 1354 ));
    }

    @Test
    public void testInsertPtwChangeWpic() {
        PtwChangeWpic ptwChangeWpic = new PtwChangeWpic();
        ptwChangeWpic.setWtId( 1354 );
//        ptwChangeWpicDao.insertPtwChangeWpic( ptwChangeWpic );
    }

}
