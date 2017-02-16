package test.com.timss.purchase.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.purchase.listener.PurchaseListener;
import com.yudean.homepage.bean.DeleteDraftParam;
import com.yudean.homepage.service.HomepageFrontService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml",
    "classpath:config/context/applicationContext-webserviceClient-config.xml" })
public class PurApplyMsgTest extends TestUnit {

  private static final Logger LOG = Logger.getLogger(PurApplyMsgTest.class);

  @Autowired
  HomepageFrontService homepageFrontService;

  @Autowired
  PurchaseListener pl;

  // 测试添加发送信息
  public void testSendMsg() {
    homepageFrontService.deleteBusinessTask(null);
  }

  /**
   * @description:采购申请结束流程（后门维护程序）
   * @author: 890166
   * @createDate: 2015-3-3:
   */
  public void testPurApplyStopProcess() {
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");

    DeleteDraftParam ddp = new DeleteDraftParam();
    ddp.setFlowId("PAPA201503020030467");
    ddp.setName("测试采购申请单测试 001");
    ddp.setProcessInsId("488815");
    ddp.setSiteid("ITC");
    pl.purApplyDeleteDraft(ddp);
  }

  /**
   * @description:采购单结束流程（后门维护程序）
   * @author: 890166
   * @createDate: 2015-3-3:
   */
  public void testPurOrderStopProcess() {
    TestUnitGolbalService.SetCurentUserById("950059", "ITC");

    DeleteDraftParam ddp = new DeleteDraftParam();
    ddp.setFlowId("PO20150302001");
    ddp.setName("测试采购单草稿001");
    ddp.setProcessInsId("488501");
    ddp.setSiteid("ITC");
    pl.purOrderDeleteDraft(ddp);
  }
}
