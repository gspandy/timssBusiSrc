package test.com.timss.purchase.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.purchase.bean.PurVendor;
import com.timss.purchase.service.PurVendorService;
import com.yudean.interfaces.service.IEsbInterfaceService;
import com.yudean.itc.dto.interfaces.esb.SupBean;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurVendorServiceTest.java
 * @author: 890166
 * @createDate: 2015-5-27
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml" })
public class PurVendorServiceTest extends TestUnit {

  @Autowired
  IEsbInterfaceService iEsbInterfaceService;

  @Autowired
  PurVendorService purVendorService;

  @Autowired
  private ItcMvcService itcMvcService;

  /**
   * @description:从商务网获取供应商信息脚本
   * @author: 890166
   * @createDate: 2015-5-27:
   */
  @Test
  public void testGetESBVendorData() {
    TestUnitGolbalService.SetCurentUserById("326519", "ZJW");
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

    try {
      PurVendor pv = null;
      List<SupBean> sbList = iEsbInterfaceService.getAllSupplier("ZJW");

      if (!sbList.isEmpty() && sbList.size() > 0) {
        for (SupBean supB : sbList) {
          pv = new PurVendor();
          pv.setSiteid(supB.getCompCode()); // 所属站点
          pv.setCompanyNo(supB.getVendorCode());// 公司编码
          pv.setName(supB.getVendorName());// 公司名称
          pv.setType(supB.getVendorType());// 公司类别
          pv.setAddress(supB.getVendorSite());// 公司地址
          pv.setContact(supB.getContactName());// 联系人
          pv.setTel(supB.getContactTel());// 联系人电话
          pv.setFax(supB.getContactFax());// 传真
          pv.setBankName(supB.getBankName());// 银行名称
          pv.setBankAccount(supB.getBankAccount());// 开户账号
          pv.setSource("ESB");// 数据来源
          pv.setActiveflag("ACTIVE");// 是否可用

          purVendorService.purVendorSaveOrUpdate(userInfo, pv);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(
          "---------PurVendorServiceTest 中的 testGetESBVendorData 方法抛出异常---------：", e);
    }
  }
}
