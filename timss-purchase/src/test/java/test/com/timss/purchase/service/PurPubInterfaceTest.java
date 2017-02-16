package test.com.timss.purchase.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.bean.PurInvoiceBean;
import com.timss.purchase.bean.PurInvoiceItemBean;
import com.timss.purchase.service.PurPubInterface;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

public class PurPubInterfaceTest extends TestUnit {

  private static final Logger LOG = Logger.getLogger(PurPubInterfaceTest.class);

  @Autowired
  private PurPubInterface purPubInterface;

  public void testPurOrderStopProcess() {
    TestUnitGolbalService.SetCurentUserById("950059", "ITC");

    List<PurInvoiceItemBean> piibList = new ArrayList<PurInvoiceItemBean>();
    PurInvoiceItemBean piib = new PurInvoiceItemBean();
    piib.setImtNo("SR20140919001");
    piib.setItemId("III1285");
    piib.setNoTaxSumPrice(43);
    piibList.add(piib);

    String purOrderId = "PO20140915001";
    boolean flag = purPubInterface.updateTranDetailByInvoice(piibList, purOrderId);

    LOG.info("更新结果：" + flag);
  }

  @Test
  public void testQueryInvoiceRelationBycontractId() {
    TestUnitGolbalService.SetCurentUserById("950059", "ITC");

    String contractId = "7580";
    List<PurInvoiceBean> pibList = purPubInterface.queryInvoiceRelationByContractId(contractId);
    for (PurInvoiceBean pib : pibList) {
      LOG.info("查询结果：" + pib.getStatus());
    }

  }
}
