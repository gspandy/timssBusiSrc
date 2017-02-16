package test.com.timss.purchase.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.purchase.bean.PurOrder;
import com.timss.purchase.bean.PurVendor;
import com.timss.purchase.dao.PurApplyDao;
import com.timss.purchase.dao.PurOrderDao;
import com.timss.purchase.dao.PurProcessMappingDao;
import com.timss.purchase.dao.PurVendorDao;
import com.timss.purchase.vo.PurOrderItemVO;
import com.timss.purchase.vo.PurOrderVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurOrderDaoTest.java
 * @author: 890166
 * @createDate: 2014-7-2
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml" })
public class PurOrderDaoTest extends TestUnit {

  private static final Logger LOG = Logger.getLogger(PurOrderDaoTest.class);

  @Autowired
  PurOrderDao purOrderDao;

  @Autowired
  PurApplyDao purApplyDao;

  @Autowired
  PurProcessMappingDao purProcessMappingDao;

  @Autowired
  PurVendorDao purVendorDao;

  /**
   * 测试查询采购单列表
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-7-2:
   */
  @Test
  public void testQueryPurOrder() {
    Page<PurOrderVO> page = new Page<PurOrderVO>();
    page.setParameter("sheetno", "PO201407027280");
    List<PurOrderVO> povList = purOrderDao.queryPurOrder(page);
    for (PurOrderVO pov : povList) {
      LOG.debug("审批单总价为：" + pov.getTotalPrice());
    }
  }

  /**
   * 测试弹出框内的申请物资信息列表
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-7-2:
   */
  @Test
  public void testQueryPurOrderInfoBySheetId() {
    List<PurOrderVO> povList = purOrderDao.queryPurOrderInfoBySheetId("7280");
    for (PurOrderVO pov : povList) {
      LOG.debug("审批单总价为：" + pov.getTotalPrice());
    }
  }

  /**
   * 测试弹出框内的申请物资信息列表
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-7-2:
   */
  @Test
  public void testQueryItemByEntity() {
    Page<PurOrderVO> page = new Page<PurOrderVO>();
    page.setParameter("siteid", "ITC");
    page.setParameter("sheetId", "643");
    List<PurOrderItemVO> poivList = purApplyDao.queryApplyItemByEntity(page);
    for (PurOrderItemVO poiv : poivList) {
      LOG.debug("单价为：" + poiv.getAverprice());
    }
  }

  /**
   * 测试弹出框内的申请物资信息列表
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-7-2:
   */
  @Test
  public void testQueryItemByList() {
    List<String> strList = new ArrayList<String>();
    strList.add("6579_M200592");
    strList.add("6579_M200581");
    strList.add("6579_M200355");

    List<PurOrderItemVO> poivList = purApplyDao.queryApplyItemByList(strList);
    for (PurOrderItemVO poiv : poivList) {
      LOG.debug("单价为：" + poiv.getAverprice());
    }
  }

  /**
   * 测试详细表单中列表
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-7-2:
   */
  @Test
  public void testQueryPurOrderItemList() {
    Page<PurOrderItemVO> page = new Page<PurOrderItemVO>();
    page.setParameter("siteid", "ITC");
    page.setParameter("sheetId", "643");
    List<PurOrderItemVO> poivList = purOrderDao.queryPurOrderItemList(page);
    for (PurOrderItemVO poiv : poivList) {
      LOG.debug("单价为：" + poiv.getAverprice());
    }
  }

  /**
   * 测试物资合并查询
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-7-2:
   */
  @Test
  public void testQueryPurOrderItemListExce() {
    Page<PurOrderItemVO> page = new Page<PurOrderItemVO>();
    page.setParameter("siteid", "ITC");
    page.setParameter("sheetId", "643");
    List<PurOrderItemVO> poivList = purOrderDao.queryPurOrderItemListExce(page);
    for (PurOrderItemVO poiv : poivList) {
      LOG.debug("单价为：" + poiv.getAverprice());
    }
  }

  /**
   * 测试查询供应商信息
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-7-2:
   */
  @Test
  public void testQueryCompanyList() {
    Page<PurVendor> page = new Page<PurVendor>();
    List<PurVendor> pvList = purVendorDao.queryCompanyList(page);
    for (PurVendor pv : pvList) {
      LOG.debug("供应商编号：" + pv.getCompanyNo());
    }
  }

  /**
   * 测试根据sheetid查询PurOrder
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-7-2:
   */
  @Test
  public void testQueryPurOrderBySheetId() {
    List<PurOrder> poList = purOrderDao.queryPurOrderBySheetId("643");
    for (PurOrder po : poList) {
      LOG.debug("审批单编号为：" + po.getSheetno());
    }
  }

  /**
   * 测试更新PurOrder信息
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-7-2:
   */
  @Test
  public void testUpdatePurOrderInfo() {
    PurOrder po = new PurOrder();
    po.setSheetId("7280");
    po.setDhdate(new Date());
    int count = purOrderDao.updatePurOrderInfo(po);
    LOG.debug("共修改了 " + count + " 条数据！");
  }

  /**
   * 测试插入PurOrder信息
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-7-2:
   */
  @Test
  public void testInsertPurOrderInfo() {
    PurOrder po = new PurOrder();
    po.setCompanyNo("1");
    po.setCompanyRemark("2");
    po.setCreateaccount("3");
    po.setCreatedate(new Date());
    po.setDhdate(new Date());
    po.setModifyaccount("4");
    po.setModifydate(new Date());
    po.setRemark("5");
    po.setSheetClass("6");
    po.setSheetIType("7");
    po.setSheetname("8");
    po.setSheetno("9");
    po.setSiteid("ITC");
    po.setStatus("1");
    po.setTotalPrice(new BigDecimal("11"));
    int count = purOrderDao.insertPurOrderInfo(po);
    LOG.debug("共新增了 " + count + " 条数据！");
  }

  /**
   * 测试通过sheetid找到PurOrderItem
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-7-2:
   */
  @Test
  public void testQueryPurOrderItemExists() {
    List<String> strList = purOrderDao.queryPurOrderItemExists("7280");
    for (String str : strList) {
      LOG.debug(str);
    }
  }

  /**
   * 测试调用插入PurOrderItem的存储过程
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-7-2:
   */
  @Test
  public void testCallProcPurOrderItemInsert() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("sheetId", "7280");
    params.put("applySheetId", "2760");
    params.put("itemid", "M070073");
    params.put("price", new BigDecimal("45"));
    params.put("tax", new BigDecimal("25"));
    params.put("cost", new BigDecimal("35"));
    params.put("itemnum", new Long("435"));
    params.put("remark", "测试");
    purOrderDao.callProcPurOrderItemInsert(params);
  }

}
