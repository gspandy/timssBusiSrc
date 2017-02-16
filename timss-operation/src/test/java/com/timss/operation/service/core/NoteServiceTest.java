package com.timss.operation.service.core;

import java.text.ParseException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.timss.operation.service.NoteService;
import com.yudean.mvc.testunit.TestUnit;

/**
 * 
 * @title: 运行记事单元测试
 * @description: {desc}
 * @company: gdyd
 * @className: NoteServiceTest.java
 * @author: fengzt
 * @createDate: 2014年7月16日
 * @updateUser: fengzt
 * @version: 1.0
 */
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@TransactionConfiguration( defaultRollback = true )
public class NoteServiceTest extends TestUnit {
    
    @Autowired
    private NoteService noteService;
    
    /**
     * 
     * @description:测试查询历史
     * @author: fengzt
     * @createDate: 2014年7月16日:
     */
    @Test
    public void testQueryNoteHistory() {
        String stationId = "ITC_opr_mgr";
        String date = "2014-07-18";
        int jobsId = 6;
       // noteService.queryNoteHistory( stationId, date );
    }
    /**
     * 
     * @description:测试查询历史
     * @author: fengzt
     * @throws ParseException 
     * @createDate: 2014年7月16日:
     */
    @Test
    public void testIsOnDuty() throws ParseException {
        String userId = "890107";
        noteService.isOnDuty( userId );
        // noteService.queryNoteHistory( stationId, date );
    }

    @Test
    public void testQueryNoteListByJobsLevel() {
        noteService.queryNoteListByJobsLevel( "45", "62", "60924" );
    }

}
