package com.timss.operation.service.core;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.operation.service.PersonJobsService;
import com.timss.operation.vo.PersonDutyVo;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PersonJobsServiceTest extends TestUnit{
    @Autowired
    private PersonJobsService personJobsService;
    
    /**
     * 
     * @description:测试update
     * @author: fengzt
     * @createDate: 2014年7月11日:
     */
    @Test
    public void testUpdatePersonJobs() {
        PersonDutyVo personJobs = new PersonDutyVo();
        personJobs.setDutyId( 3 );
        personJobs.setJobsId( 1 );
        personJobs.setStationId( "ITC_opr_duty_mgr" );
        
        String userDel = "";
        String userAdd = "898014,890163,890162";
        
        //personJobsService.updatePersonJobs( personJobs, userDel, userAdd );
    }
    
    /**
     * 
     * @description:测试update
     * @author: fengzt
     * @createDate: 2014年7月11日:
     */
    @Test
    public void testQueryOrgsRelatedToUsers() {
        PersonDutyVo vo = new PersonDutyVo();
        vo.setDutyId( 3 );
        vo.setJobsId( 1 );
        vo.setStationId( "ITC_opr_duty_mgr" );
        
        
        personJobsService.queryOrgsRelatedToUsers( vo );
    }

}
