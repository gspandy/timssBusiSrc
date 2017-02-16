package com.timss.ptw.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PtwInfoServiceTest  extends TestUnit {
    @Autowired
    PtwInfoService ptwInfoService;
    @Test
    public void testFindPtwInfoByNo() {
        System.out.println(ptwInfoService.queryPtwInfoByNo( "DQ1-2014-00016", "ITC" ));
    }

}
