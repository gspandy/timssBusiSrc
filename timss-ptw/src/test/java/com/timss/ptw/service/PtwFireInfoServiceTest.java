package com.timss.ptw.service;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PtwFireInfoServiceTest  extends TestUnit {
    @Autowired
    PtwFireInfoService ptwFireInfoService ;
    @Test
    public void testFindPtwInfoByNo() {
        ptwFireInfoService.updatePtwFireFinAppr( 1828, "890107", "周保康", new Date() );
    }

}
