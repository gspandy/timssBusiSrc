package test.com.timss.inventory.service;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvTest.java
 * @author: 890166
 * @createDate: 2015-3-3
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml" })
public class InvTest extends TestUnit {

    /**
     * @description: 领料申请结束流程（后门操作）
     * @author: 890166
     * @createDate: 2015-3-3:
     */
    @Test
    public void testInvMatApplyStopProcess() {
        System.out.print( "123" );
    }

}
