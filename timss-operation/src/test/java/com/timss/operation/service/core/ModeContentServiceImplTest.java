package com.timss.operation.service.core;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.timss.operation.service.ModeContentService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@TransactionConfiguration( defaultRollback = true )
public class ModeContentServiceImplTest extends TestUnit{

    @Autowired
    private ModeContentService modeContentService;
    
    @Test
    public void testInsertOrUpdateModeContent() {
        TestUnitGolbalService.SetCurentUserById("890167", "ITC");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "AST-00000228", "开" );
        map.put( "AST-00000229", "开" );
        map.put( "AST-00000230", "开" );
        
        int count = modeContentService.insertOrUpdateModeContent( map, 87, 66, 181,"");
        System.out.println( count );
        
    }

}
