package test.com.timss.purchase.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.purchase.bean.PurVendor;
import com.timss.purchase.dao.PurVendorDao;
import com.timss.purchase.service.PurVendorService;
import com.yudean.mvc.testunit.TestUnit;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurVendorDaoTest.java
 * @author: 890166
 * @createDate: 2014-6-20
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml" })
public class PurVendorDaoTest extends TestUnit {

  private static final Logger LOG = Logger.getLogger(PurVendorDaoTest.class);

  @Autowired
  PurVendorDao purVendorDao;

  @Autowired
  PurVendorService purVendorService;

  @Test
  public void testQueryPurVendorBycompanyNos() {
    List<String> sList = new ArrayList<String>();
    sList.add("SU00048");
    sList.add("SU00013");
    sList.add("SU00091");
    sList.add("SU00700");
    sList.add("SU02546");
    try {
      List<PurVendor> pvList = purVendorService.queryPurVendorBycompanyNos(sList, "ITC");
      for (PurVendor pv : pvList) {
        LOG.debug(pv.getCompanyNo() + "--------" + pv.getName());
      }
    } catch (Exception e) {
      LOG.debug(e.getMessage());
    }
  }
}
