package test.com.timss.purchase.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.purchase.listener.PurOrderToEipMobile;
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
 * @className: PurOrderToEipMobileTest.java
 * @author: 890166
 * @createDate: 2014-9-24
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml" })
public class PurOrderToEipMobileTest extends TestUnit {

  private static final Logger LOG = Logger.getLogger(PurOrderToEipMobileTest.class);

  @Autowired
  private PurOrderToEipMobile eipMobileInterface;

  /**
   * 界面展示
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-9-24:
   */
  public void testRetrieveWorkflowFormDetails() {
    TestUnitGolbalService.SetCurentUserById("664838", "ITC");
    ParamDetailBean empb = new ParamDetailBean();
    empb.setFlowNo("PO20150402002");
    empb.setProcessId("527215");
    RetContentBean eip = eipMobileInterface.retrieveWorkflowFormDetails(empb);
    String reVal = JsonHelper.fromBeanToJsonString(eip);
    LOG.debug(reVal);
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
    TestUnitGolbalService.SetCurentUserById("664838", "ITC");
    List<String> nextUser = new ArrayList<String>();
    nextUser.add("890127");

    ParamProcessBean empb = new ParamProcessBean();
    empb.setFlowID("next");
    empb.setFlowNo("PO20150402002");
    empb.setNextUser(nextUser);
    empb.setProcessId("527215");
    empb.setOpinion("请领导审批 -- yj");
    empb.setTaskKey("procurement1");
    RetProcessBean emrb = eipMobileInterface.processWorkflow(empb);

    String reVal = JsonHelper.fromBeanToJsonString(emrb);
    LOG.debug(reVal);
  }

  /**
   * 不同意退回到首环节
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-9-24:
   */

  public void testprocessWorkflowNotAgree() {
    TestUnitGolbalService.SetCurentUserById("664838", "ITC");

    ParamProcessBean empb = new ParamProcessBean();
    empb.setFlowID("rollback");
    empb.setFlowNo("PO20150402001");
    empb.setProcessId("526406");
    empb.setOpinion("不同意采购--yj");
    RetProcessBean emrb = eipMobileInterface.processWorkflow(empb);

    String reVal = JsonHelper.fromBeanToJsonString(emrb);
    LOG.debug(reVal);
  }

}
