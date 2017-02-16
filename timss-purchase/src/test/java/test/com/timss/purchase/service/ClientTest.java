package test.com.timss.purchase.service;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.timss.purchase.service.PurOrderService;
import com.yudean.itc.webservice.HhcWebEamItemService;
import com.yudean.itc.webservice.HhcWebEamPrService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml" })
public class ClientTest extends TestUnit {
  private static final Logger LOG = Logger.getLogger(ClientTest.class);

  @Autowired
  HhcWebEamItemService eamItemService;// 物资主项目接口

  @Autowired
  HhcWebEamPrService eamPrService;// 采购订单接口

  @Autowired
  private PurOrderService purOrderService;

  @Test
  @Transactional
  public void syncDataFromHhcEamTest() throws Exception {
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");
    Date reDate = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(reDate);
    c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
    reDate = c.getTime();

    purOrderService.processACDataFormBusi(reDate);
  }
}
