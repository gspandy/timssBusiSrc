package com.timss.operation.service.core;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.timss.operation.service.DutyService;
import com.yudean.mvc.testunit.TestUnit;


@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@TransactionConfiguration( defaultRollback = true )
public class DutyServiceImplTest  extends TestUnit {
    
    @Autowired
    private DutyService dutyService;

    @Test
    public void testQuerySortTypeByStationId() {
        
        String stationId = "ITC_opr_duty_mgr";
        
        dutyService.querySortTypeByStationId( stationId );
    }

}
