package com.timss.ptw.dao;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;
import com.timss.ptw.bean.PtwExtand;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PtwExtandDaoTest  extends TestUnit{
    @Autowired
    private PtwExtandDao ptwExtandDao;
    @Test
    public void testQueryPtwExtandByPtwId() {
        System.out.println(ptwExtandDao.queryPtwExtandByPtwId( 1354 ));
    }
    @Test
    public void testInsertPtwExtand() {
        PtwExtand ptwExtand = new PtwExtand();
        ptwExtand.setWtId( 1354 );
        ptwExtand.setExtApprTime( new Date() );
        ptwExtand.setExtSignTime( new Date() );
        ptwExtand.setExtWl( "" );
        ptwExtand.setExtWlNo( "" );
        ptwExtand.setExtWpic( "test" );
        ptwExtand.setExtWpicNo( "test" );
//        ptwExtandDao.insertPtwExtand( ptwExtand );
    }

}
