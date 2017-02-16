package com.timss.attendance.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.timss.attendance.bean.DefinitionBean;
import com.timss.attendance.dao.DefinitionDao;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@TransactionConfiguration( defaultRollback = true )
public class DefinitionServiceTest extends TestUnit {

    @Autowired
    private DefinitionDao definitionService;
    
    @Test
    public void testInsertDefinition() {
        DefinitionBean vo = new DefinitionBean();
        
        vo.setAfterEndDate( "1700" );
        vo.setAfterStartDate( "1400" );
        vo.setCompensateRatio( 1 );
        vo.setEffectiveDays( 1 );
        
        vo.setFirstLevelDays( 5 );
        vo.setForeEndDate( "1200" );
        vo.setForeStartDate( "0800" );
        vo.setSecondLevelDays( 10 );
        
        vo.setServiceYear( 1 );
        vo.setSiteId( "ITC" );
        vo.setThirdLevelDays( 20 );
        vo.setYearRatio( 1 );
        
        int count = definitionService.insertDefinition( vo );
        
        System.out.println( count + "---------------------" + vo.getId() );
    }

}
