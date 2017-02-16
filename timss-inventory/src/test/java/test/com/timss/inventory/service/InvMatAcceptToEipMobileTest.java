package test.com.timss.inventory.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.inventory.listener.InvMatAcceptToEipMobile;
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
 * @className: InvMaAcceptToEipMobileTest.java
 * @author: 890166
 * @createDate: 2014-9-23
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatAcceptToEipMobileTest extends TestUnit {

    private static final Logger LOG = Logger.getLogger( InvMatAcceptToEipMobileTest.class );

    @Autowired
    private InvMatAcceptToEipMobile eipMobileInterface;

    /**
     * 界面展示
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-9-24:
     */
    @Test
    public void testRetrieveWorkflowFormDetails() {
        TestUnitGolbalService.SetCurentUserById( "880040", "SWF" );
        ParamDetailBean empb = new ParamDetailBean();
        empb.setFlowNo( "YS20160808003" );
        empb.setProcessId( "2415415" );
        RetContentBean eip = eipMobileInterface.retrieveWorkflowFormDetails( empb );
        String reVal = JsonHelper.fromBeanToJsonString( eip );
        LOG.debug( "--------------------------------------------" );
        LOG.debug( reVal );
    }

    /**
     * 同意并提交到下一环节
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-9-24:
     */
    @Test
    public void testprocessWorkflowAgree() {
        TestUnitGolbalService.SetCurentUserById( "130614", "SWF" );
        List<String> nextUser = new ArrayList<String>();
        nextUser.add( "880040" );

        ParamProcessBean empb = new ParamProcessBean();
        empb.setFlowID( "next" );
        empb.setFlowNo( "YS20160809001" );
        empb.setNextUser( nextUser );
        empb.setProcessId( "2416701" );
        empb.setOpinion( "同意杨露璐执行验收" );
        //empb.setTaskKey( "cgsqrzxys" );
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
    @Test
    public void testprocessWorkflowNotAgree() {
        TestUnitGolbalService.SetCurentUserById( "130412", "SWF" );
        List<String> nextUser = new ArrayList<String>();
        nextUser.add( "880040" );

        ParamProcessBean empb = new ParamProcessBean();
        empb.setFlowID( "rollback" );
        empb.setFlowNo( "YS20160808002" );
        empb.setNextUser( nextUser );
        empb.setProcessId( "2415250" );
        empb.setOpinion( "退回执行验收" );
        empb.setTaskKey( "cgytcyssq" );
        RetProcessBean emrb = eipMobileInterface.processWorkflow( empb );
        String reVal = JsonHelper.fromBeanToJsonString( emrb );
        LOG.debug( reVal );
    }

    @Test
    public void testprocessWorkflowStop() {
        TestUnitGolbalService.SetCurentUserById( "130412", "SWF" );
        List<String> nextUser = new ArrayList<String>();
        nextUser.add( "880040" );

        ParamProcessBean empb = new ParamProcessBean();
        empb.setFlowID( "rollback" );
        empb.setFlowNo( "YS20160808002" );
        empb.setNextUser( nextUser );
        empb.setProcessId( "2415250" );
        empb.setOpinion( "退回执行验收" );
        empb.setTaskKey( "cgytcyssq" );
        RetProcessBean emrb = eipMobileInterface.processWorkflow( empb );
        String reVal = JsonHelper.fromBeanToJsonString( emrb );
        LOG.debug( reVal );
    }

}
