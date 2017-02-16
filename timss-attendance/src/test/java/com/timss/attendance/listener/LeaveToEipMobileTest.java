package com.timss.attendance.listener;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.itc.dto.interfaces.eip.mobile.ParamDetailBean;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamProcessBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetProcessBean;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

/**
 * 
 * @title:请假申请EIP接口测试
 * @description: {desc}
 * @company: gdyd
 * @className: AbnormityToEipMobileTest.java
 * @author: fengzt
 * @createDate: 2014年10月15日
 * @updateUser: fengzt
 * @version: 1.0
 */
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class LeaveToEipMobileTest extends TestUnit {
    @Autowired
    private LeaveToEipMobile leaveToEipMobile;
    
    /**
     * 界面展示
     * @description:
     * @author: 890166
     * @createDate: 2014-9-24:
     */
    @Test
    public void testRetrieveWorkflowFormDetails() {
            TestUnitGolbalService.SetCurentUserById("890167", "ITC");
            ParamDetailBean empb = new ParamDetailBean();
            empb.setFlowNo("AL20150115003");
            empb.setProcessId("466401");
            RetContentBean eip = leaveToEipMobile.retrieveWorkflowFormDetails(empb);
            String reVal = JsonHelper.fromBeanToJsonString(eip);
            System.out.println( "result ------" +  reVal );
    }
    
    /**
     * 同意并提交到下一环节
     * @description:
     * @author: 890166
     * @throws UnsupportedEncodingException 
     * @createDate: 2014-9-24:
     */
    @Test
    public void testprocessWorkflowAgree() throws UnsupportedEncodingException {
            TestUnitGolbalService.SetCurentUserById("890139", "ITC");
            List<String> nextUser = new ArrayList<String>();
            nextUser.add("890139");
            
            ParamProcessBean empb = new ParamProcessBean();
            empb.setFlowID("next");
            empb.setFlowNo("AL20141016001");
            empb.setNextUser(nextUser);
            empb.setProcessId("299902");
            String value = URLDecoder.decode("testzhongwen测试中文", "UTF-8");
            empb.setOpinion( value );
            RetProcessBean emrb = leaveToEipMobile.processWorkflow(empb);
            String reVal = JsonHelper.fromBeanToJsonString(emrb);
            System.out.println(reVal);
    }
    
    /**
     * 不同意退回到首环节
     * @description:
     * @author: 890166
     * @throws UnsupportedEncodingException 
     * @createDate: 2014-9-24:
     */
    @Test
    public void testprocessWorkflowNotAgree() throws UnsupportedEncodingException {
            TestUnitGolbalService.SetCurentUserById("950011", "ITC");
            
            ParamProcessBean empb = new ParamProcessBean();
            empb.setFlowID("rollback");
            empb.setFlowNo("AL20141016001");
            empb.setProcessId("299902");
            String value = URLDecoder.decode("testzhongwen测试中文", "UTF-8");
            empb.setOpinion( value );
            RetProcessBean emrb = leaveToEipMobile.processWorkflow(empb);
            
            String reVal = JsonHelper.fromBeanToJsonString(emrb);
            System.out.println(reVal);
    }

}
