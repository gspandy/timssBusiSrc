package test.com.timss.purchase.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.purchase.listener.PurApplyToEipMobile;
import com.yudean.interfaces.service.IEipInterfaceService;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamDetailBean;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamProcessBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetDetailBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetProcessBean;
import com.yudean.itc.util.MD5;
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
@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml",
    "classpath:config/context/applicationContext-webserviceClient-config.xml" })
public class PurApplyToEipMobileTest extends TestUnit {
  private static final Logger LOG = Logger.getLogger(PurApplyToEipMobileTest.class);

  @Autowired
  IEipInterfaceService eipInterfaceService;

  @Autowired
  private PurApplyToEipMobile eipMobileInterface;

  /**
   * 界面展示
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-9-24:
   */
  @Test
  public void testRetrieveWorkflowFormDetails() {
    TestUnitGolbalService.SetCurentUserById("950022", "ITC");
    ParamDetailBean empb = new ParamDetailBean();
    empb.setFlowNo("PA20150424005");
    empb.setProcessId("674986");
    RetContentBean eip = eipMobileInterface.retrieveWorkflowFormDetails(empb);
    String reVal = JsonHelper.fromBeanToJsonString(eip);
    LOG.debug(reVal);
  }

  /**
   * 测试Eip接口获取某张代办单详情
   * 
   * @description:
   * @author: kChen
   * @createDate: 2014-9-26:
   */

  public void testRetrieveInterface() {
    RetDetailBean dto = eipInterfaceService.getTaskDetailMobile("890157", MD5.GetMD5Code("123456"),
        "PA20140923001", "ITC");
    LOG.info(JsonHelper.fromBeanToJsonString(dto));
    LOG.info("测试主框架测试容器加载成功。");
  }

  /**
   * 同意并提交到下一环节
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-9-24:
   */

  public void testprocessWorkflowAgree() {
    TestUnitGolbalService.SetCurentUserById("664838", "ITC");
    List<String> nextUser = new ArrayList<String>();
    nextUser.add("890127");

    ParamProcessBean empb = new ParamProcessBean();
    empb.setFlowID("next");
    empb.setFlowNo("PA20150402003");
    empb.setNextUser(nextUser);
    empb.setProcessId("526817");
    empb.setOpinion("ok-- ");
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
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");

    ParamProcessBean empb = new ParamProcessBean();
    empb.setFlowID("rollback");
    empb.setFlowNo("PA20150402003");
    empb.setProcessId("526817");
    empb.setOpinion("不同意采购--lmj");
    RetProcessBean emrb = eipMobileInterface.processWorkflow(empb);

    String reVal = JsonHelper.fromBeanToJsonString(emrb);
    LOG.debug(reVal);
  }

  /**
   * @description:终止流程
   * @author: 890166
   * @createDate: 2014-10-25:
   */

  public void testprocessWorkflowStop() {
    TestUnitGolbalService.SetCurentUserById("113489", "ITC");

    ParamProcessBean empb = new ParamProcessBean();
    empb.setFlowID("stop");
    empb.setFlowNo("PA20150402001");
    empb.setProcessId("525801");
    empb.setOpinion("结束流程");
    RetProcessBean emrb = eipMobileInterface.processWorkflow(empb);

    String reVal = JsonHelper.fromBeanToJsonString(emrb);
    LOG.debug(reVal);
  }

}
