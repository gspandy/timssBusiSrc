package test.com.timss.inventory.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.listener.InvMatApplyToEipMobile;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamDetailBean;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamProcessBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetProcessBean;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurApplyToEipMobileTest.java
 * @author: 890166
 * @createDate: 2014-9-23
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml" })
public class InvMatApplyToEipMobileTest extends TestUnit {

    private static final Logger LOG = Logger.getLogger( InvMatApplyToEipMobileTest.class );

    @Autowired
    private InvMatApplyToEipMobile eipMobileInterface;

    /**
     * 界面展示
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-9-24:
     */
    public void testRetrieveWorkflowFormDetails() {
        TestUnitGolbalService.SetCurentUserById( "113489", "ITC" );
        ParamDetailBean empb = new ParamDetailBean();
        empb.setFlowNo( "SA20150402001" );
        empb.setProcessId( "527701" );
        RetContentBean eip = eipMobileInterface.retrieveWorkflowFormDetails( empb );
        String reVal = JsonHelper.fromBeanToJsonString( eip );
        LOG.debug( reVal );
    }

    /**
     * 同意并提交到下一环节
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-9-24:
     */
    public void testprocessWorkflowAgree() {
        TestUnitGolbalService.SetCurentUserById( "113489", "ITC" );
        List<String> nextUser = new ArrayList<String>();
        nextUser.add( "890131" );

        ParamProcessBean empb = new ParamProcessBean();
        empb.setFlowID( "next" );
        empb.setFlowNo( "SA20150402001" );
        empb.setNextUser( nextUser );
        empb.setProcessId( "527701" );
        empb.setOpinion( "请领导审批 -- zcb" );
        empb.setTaskKey( "comleader" );
        RetProcessBean emrb = eipMobileInterface.processWorkflow( empb );
        String reVal = JsonHelper.fromBeanToJsonString( emrb );
        LOG.debug( reVal );
    }

    /**
     * 不同意退回到首环节
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-9-24:
     */
    public void testprocessWorkflowNotAgree() {
        TestUnitGolbalService.SetCurentUserById( "950059", "ITC" );

        ParamProcessBean empb = new ParamProcessBean();
        empb.setFlowID( "rollback" );
        empb.setFlowNo( "SA20150402001" );
        empb.setProcessId( "527701" );
        empb.setOpinion( "不同意采购--zhx" );
        RetProcessBean emrb = eipMobileInterface.processWorkflow( empb );

        String reVal = JsonHelper.fromBeanToJsonString( emrb );
        LOG.debug( reVal );
    }

    public void testprocessWorkflowStop() {
        TestUnitGolbalService.SetCurentUserById( "890130", "ITC" );

        ParamProcessBean empb = new ParamProcessBean();
        empb.setFlowID( "stop" );
        empb.setFlowNo( "SA20140925001" );
        empb.setProcessId( "261702" );
        empb.setOpinion( "不同意采购--cyr" );
        RetProcessBean emrb = eipMobileInterface.processWorkflow( empb );

        String reVal = JsonHelper.fromBeanToJsonString( emrb );
        LOG.debug( reVal );
    }

}
