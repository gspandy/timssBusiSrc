package com.timss.ptw.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;
import com.timss.ptw.bean.PtwInfo;
import com.timss.ptw.vo.PtwInfoVoList;
import com.yudean.itc.dto.Page;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PtwInfoDaoTest extends TestUnit {
    @Autowired
    PtwInfoDao ptwInfoDao;
    
    //@Test
    public void testQueryPtwInfo() {
        Page<PtwInfoVoList> page = new Page<PtwInfoVoList>();

        page.setParameter( "isStdWt", 0 );
        page.setParameter( "siteId", "ITC" );
        
        List<PtwInfoVoList> list = ptwInfoDao.queryPtwInfoVoList( page );
        for ( PtwInfoVoList ptwInfo : list ) {
            System.out.println(ptwInfo);
        }
    }
    
    //@Test
    public void testQueryPtwInfoById(){
        
    }
    
    @Test 
    public void testUpdateAttachFile(){
        PtwInfo ptwInfo = new PtwInfo();
        ptwInfo.setId( 1612 );
        ptwInfo.setAddFile1( "" );
        ptwInfo.setAddFile2( null );
        ptwInfo.setAddFile3( "abc" );
        ptwInfoDao.updatePtwAttachFiles( ptwInfo );
        System.out.println(ptwInfo = ptwInfoDao.queryPtwInfoById( 1612 ));
    }

}
