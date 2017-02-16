package test.com.timss.purchase.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.purchase.bean.PurApply;
import com.timss.purchase.bean.PurApplyItem;
import com.timss.purchase.dao.PurApplyDao;
import com.timss.purchase.vo.EamItemVO;
import com.timss.purchase.vo.PurApplyItemVO;
import com.timss.purchase.vo.PurApplyVO;
import com.timss.purchase.vo.PurOrderItemVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurApplyDaoTest.java
 * @author: 890166
 * @createDate: 2014-6-27
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml" })
public class PurApplyDaoTest extends TestUnit {

  private static final Logger LOG = Logger.getLogger(PurApplyDaoTest.class);

  @Autowired
  PurApplyDao purApplyDao;

  @Autowired
  private ItcMvcService itcMvcService;

  /**
   * 测试查询采购申请列表
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-6-20:
   */
  public void testQueryPurApply() {
    Page<PurApplyVO> page = new Page<PurApplyVO>();
    page.setParameter("sheetno", "0005");
    page.setParameter("siteid", "ITC");
    List<PurApplyVO> pavList = purApplyDao.queryPurApply(page);
    LOG.debug("-------------查询采购申请列表 开始-------------");
    for (PurApplyVO pav : pavList) {
      LOG.debug("审批单总价为：" + pav.getTotalcost());
    }
    LOG.debug("-------------查询采购申请列表 结束-------------");
  }

  /**
   * 测试根据sheetid查询出采购申请表单详细信息
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-6-27:
   */
  public void testQueryPurApplyInfoBySheetId() {
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    UserInfoScope scope = userInfo;

    Page<PurApply> page = scope.getPage();
    page.setParameter("sheetId", "7256");
    page.setParameter("siteId", scope.getSiteId());

    List<PurApply> paList = purApplyDao.queryPurApplyInfoBySheetId(page);
    LOG.debug("-------------根据sheetid查询出采购申请表单详细信息 开始-------------");
    for (PurApply pa : paList) {
      LOG.debug("采购信息为：" + pa.getSheetno());
    }
    LOG.debug("-------------根据sheetid查询出采购申请表单详细信息 结束-------------");
  }

  /**
   * 测试查询采购申请物资列表
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-6-27:
   */
  public void testQueryPurApplyItemList() {
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");
    Page<PurApplyItemVO> page = new Page<PurApplyItemVO>();
    page.setParameter("sheetId", "4862");
    List<PurApplyItemVO> paivList = purApplyDao.queryPurApplyItemList(page);
    LOG.debug("-------------查询采购申请物资列表 开始-------------");
    for (PurApplyItemVO paiv : paivList) {
      LOG.debug("采购信息为：" + paiv.getItemid());
    }
    LOG.debug("-------------查询采购申请物资列表 结束-------------");
  }

  /**
   * 测试更新PurApply信息
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-6-27:
   */
  public void testUpdatePurApplyInfo() {
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");
    PurApply purApply = new PurApply();
    purApply.setSheetId("7257");
    purApply.setDhdate(new Date());
    int count = purApplyDao.updatePurApplyInfo(purApply);
    LOG.debug("更新了" + count + "条数据！");
  }

  /**
   * 测试新建PurApply信息
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-6-27:
   */
  public void testInsertPurApplyInfo() {
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");
    PurApply purApply = new PurApply();
    purApply.setCreateaccount("890135");
    purApply.setCreatedate(new Date());
    purApply.setItemnum(new Long("5"));
    purApply.setModifyaccount("890135");
    purApply.setModifydate(new Date());
    purApply.setOrderaccount("");
    purApply.setPurchstatus("1");
    purApply.setPurchtype("0");
    purApply.setRemark("123");
    purApply.setSecuritystoreid(new Long("123124"));
    purApply.setSheetclassid("ddfdf");
    purApply.setSheetname("ceshisdfdfdf");
    purApply.setSheetno("20140627002");
    purApply.setSiteid("ITC");
    purApply.setSumflag("0");
    purApply.setTatolcost(new BigDecimal("1"));
    purApply.setDhdate(new Date());
    int count = purApplyDao.insertPurApplyInfo(purApply);
    LOG.debug("新增了" + count + "条数据！");
  }

  /**
   * 测试根据sheetid删除PurApplyItem信息
   * 
   * @description:
   * @author: 890166
   * @createDate: 2014-6-27:
   */
  public void testDeletePurApplyItemBySheetId() {
    int count = purApplyDao.deletePurApplyItemBySheetId("0526");
    LOG.debug("删除了" + count + "条数据");
  }

  /**
   * 测试根据sheetid查询出采购申请表单详细信息
   * 
   * @description:
   * @author: 890166
   * @createDate: 2015-5-15:
   */
  public void testQueryPurApplyInfoBySheetIdNoZh() {
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    UserInfoScope scope = userInfo;
    Page<PurApply> page = scope.getPage();

    page.setParameter("sheetId", "0377");
    page.setParameter("siteId", scope.getSiteId());
    List<PurApply> paList = purApplyDao.queryPurApplyInfoBySheetIdNoZh(page);
    LOG.debug("-------------根据sheetid查询出采购申请表单详细信息 开始-------------");
    for (PurApply pa : paList) {
      LOG.debug("采购信息为：" + pa.getSheetno());
    }
    LOG.debug("-------------根据sheetid查询出采购申请表单详细信息 结束-------------");
  }

  /**
   * 弹出框内的申请物资信息列表
   * 
   * @description:
   * @author: 890166
   * @createDate: 2015-5-15:
   */
  public void testQueryApplyItemByList() {
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");
    List<String> diffList = new ArrayList<String>();
    diffList.add("0277_M070160");
    diffList.add("0277_M070164");
    List<PurOrderItemVO> poList = purApplyDao.queryApplyItemByList(diffList);
    LOG.debug("-------------弹出框内的申请物资信息列表 开始-------------");
    for (PurOrderItemVO po : poList) {
      LOG.debug("采购物资信息为：" + po.getItemname());
    }
    LOG.debug("-------------弹出框内的申请物资信息列表 结束-------------");
  }

  /**
   * 弹出框内的申请物资信息列表
   * 
   * @description:
   * @author: 890166
   * @createDate: 2015-5-15:
   */
  public void testQueryApplyItemByEntity() {
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    UserInfoScope scope = userInfo;
    Page<PurOrderItemVO> page = scope.getPage();

    page.setParameter("siteid", userInfo.getSiteId());
    List<PurOrderItemVO> paList = purApplyDao.queryApplyItemByEntity(page);
    LOG.debug("-------------弹出框内的申请物资信息列表 开始-------------");
    for (PurOrderItemVO pa : paList) {
      LOG.debug("采购信息为：" + pa.getSheetno());
    }
    LOG.debug("-------------弹出框内的申请物资信息列表 结束-------------");
  }

  /**
   * 批量插入PurApplyItem信息(页面数据直接插入)
   * 
   * @description:
   * @author: 890166
   * @createDate: 2015-5-15:
   */
  public void testInsertPurApplyItemWithList() {
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");
    List<PurApplyItem> paiList = new ArrayList<PurApplyItem>();
    PurApplyItem pai = new PurApplyItem();
    pai.setSheetId("0277");
    pai.setItemid("M070162");
    pai.setItemnum(4D);
    pai.setAverprice(new BigDecimal("32.65"));
    pai.setCommitcommecnetwk("0");
    pai.setRepliednum(6D);
    pai.setStatus("6");
    pai.setStatusdate(new Date());

    paiList.add(pai);
    int count = purApplyDao.insertPurApplyItemWithList(paiList);
    LOG.debug("-------------批量插入PurApplyItem信息(页面数据直接插入) 开始-------------");
    LOG.debug("插入条目为：" + count);
    LOG.debug("-------------批量插入PurApplyItem信息(页面数据直接插入) 结束-------------");
  }

  /**
   * @description: 更新发送到商务网状态
   * @author: 890166
   * @createDate: 2015-5-18:
   */
  public void testUpdatePurApplyItemBusinessStatus() {
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("sheetId", "0269");
    map.put("itemId", "'M105796'");
    map.put("busiStatus", 0);
    map.put("itemStatus", "3");
    int count = purApplyDao.updatePurApplyItemBusinessStatus(map);
    LOG.debug("-------------更新发送到商务网状态 开始-------------");
    LOG.debug("更新条目为：" + count);
    LOG.debug("-------------更新发送到商务网状态 结束-------------");
  }

  /**
   * @description:更新状态
   * @author: 890166
   * @createDate: 2015-5-18:
   */
  public void testUpdatePurApplyItemStatus() {
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("status", "9");
    map.put("sheetId", "0269");
    map.put("itemId", "M105796");
    int count = purApplyDao.updatePurApplyItemStatus(map);
    LOG.debug("-------------更新状态 开始-------------");
    LOG.debug("更新条目为：" + count);
    LOG.debug("-------------更新状态 结束-------------");
  }

  /**
   * @description:通过flowNo查询到sheetid
   * @author: 890166
   * @createDate: 2015-5-18:
   */
  public void testQuerySheetIdByFlowNo() {
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("sheetNo", "PA20141208003");
    map.put("siteId", "ITC");
    String sheetId = purApplyDao.querySheetIdByFlowNo(map);
    LOG.debug("-------------通过flowNo查询到sheetid 开始-------------");
    LOG.debug("查询条目为：" + sheetId);
    LOG.debug("-------------通过flowNo查询到sheetid 结束-------------");
  }

  /**
   * @description:通过flowNo查询到sheetid
   * @author: 890166
   * @createDate: 2015-5-18:
   */
  public void testQueryFlowNoBySheetId() {
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("sheetId", "0392");
    map.put("siteId", "ITC");
    String sheetId = purApplyDao.queryFlowNoBySheetId(map);
    LOG.debug("-------------通过flowNo查询到sheetid 开始-------------");
    LOG.debug("查询条目为：" + sheetId);
    LOG.debug("-------------通过flowNo查询到sheetid 结束-------------");
  }

  /**
   * @description:查询采购申请单详细信息
   * @author: 890166
   * @createDate: 2015-5-18:
   */
  public void testQueryPurApplyItemBySheetIdAndItems() {
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("sheetId", "0269");
    map.put("itemId", "'M105796'");
    map.put("siteId", "ITC");
    List<PurApplyItemVO> paivList = purApplyDao.queryPurApplyItemBySheetIdAndItems(map);
    LOG.debug("-------------查询采购申请单详细信息 开始-------------");
    for (PurApplyItemVO paiv : paivList) {
      LOG.debug("查询条目物资名称为：" + paiv.getItemname());
    }
    LOG.debug("-------------查询采购申请单详细信息 结束-------------");
  }

  /**
   * @description:查询今天发送到商务网的物资信息
   * @author: 890166
   * @createDate: 2015-5-18:
   */
  public void testQueryHhcSyncDataInTimss() {
    TestUnitGolbalService.SetCurentUserById("890127", "ITC");
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("siteId", "ITC");
    List<EamItemVO> eivList = purApplyDao.queryHhcSyncDataInTimss(map);
    LOG.debug("-------------查询今天发送到商务网的物资信息 开始-------------");
    for (EamItemVO eiv : eivList) {
      LOG.debug("查询条目物资名称为：" + eiv.getDescription());
    }
    LOG.debug("-------------查询今天发送到商务网的物资信息 结束-------------");
  }
}
