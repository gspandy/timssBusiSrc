package com.timss.ptw.service.core;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.ptw.service.PtwIsolationPointService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-webserviceClient-config.xml",
        "classpath:config/context/applicationContext-workflow.xml"})
public class PtwIsolationPointServiceImplTest extends TestUnit  {
    
    @Autowired
    PtwIsolationPointService ptwIsolationPointService;
    
    @Test
    public void testSaveIslMethodByMethodIds() {
        TestUnitGolbalService.SetCurentUserById("200624", "SJC");
        Map<String, Object> map = ptwIsolationPointService.saveIslMethodByMethodIds( "243", "AST-00010699" );
        
        System.out.println( map );
        
    }

}
