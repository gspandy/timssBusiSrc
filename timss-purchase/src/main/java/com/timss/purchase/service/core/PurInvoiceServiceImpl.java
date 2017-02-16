package com.timss.purchase.service.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jodd.util.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.purchase.bean.PurInvoiceBean;
import com.timss.purchase.bean.PurInvoiceItemBean;
import com.timss.purchase.dao.PurInvoiceDao;
import com.timss.purchase.service.PurInvoiceService;
import com.timss.purchase.service.PurPubInterface;
import com.timss.purchase.utils.CommonUtil;
import com.timss.purchase.vo.PurInvoiceAssetVo;
import com.timss.purchase.vo.PurInvoiceScheduleVo;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 发票管理
 * @description: {desc}
 * @company: gdyd
 * @className: PurInvoiceServiceImpl.java
 * @author: fengzt
 * @createDate: 2015年9月18日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("purInvoiceService")
@Transactional(propagation = Propagation.SUPPORTS)
public class PurInvoiceServiceImpl implements PurInvoiceService {

  private Logger LOG = Logger.getLogger(PurInvoiceServiceImpl.class);

  // 发票有效天数
  private final static int EFFECTIVEDATE = 180;

  @Autowired
  private ItcMvcService itcMvcService;

  @Autowired
  private PurInvoiceDao purInvoiceDao;

  @Autowired
  private HomepageService homepageService;

  @Autowired
  private PurPubInterface pubInterface;

  /**
   * 
   * @description:通过站点查询发票
   * @author: fengzt
   * @createDate: 2015年9月18日
   * @param paramsPage
   * @return: Page<PurInvoiceBean>
   */
  @Override
  public Page<PurInvoiceBean> queryInvoiceBySiteId(Page<PurInvoiceBean> paramsPage) {
    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    String siteId = userInfoScope.getSiteId();

    paramsPage.setParameter("siteId", siteId);
    paramsPage.setParameter("isDelete", "N");

    List<PurInvoiceBean> invoiceBeans = purInvoiceDao.queryInvoiceBySiteId(paramsPage);
    paramsPage.setResults(invoiceBeans);
    return paramsPage;
  }

  /**
   * 
   * @description:发票号或者合同号 模糊查询发票
   * @author: fengzt
   * @createDate: 2015年9月18日
   * @param paramsPage
   * @return:Page<PurInvoiceBean>
   */
  @Override
  public Page<PurInvoiceBean> queryInvoiceBySearch(Page<PurInvoiceBean> paramsPage) {
    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    String siteId = userInfoScope.getSiteId();

    paramsPage.setParameter("siteId", siteId);
    paramsPage.setParameter("isDelete", "N");

    List<PurInvoiceBean> invoiceBeans = purInvoiceDao.queryInvoiceBySearch(paramsPage);
    paramsPage.setResults(invoiceBeans);
    return paramsPage;
  }

  /***
   * 
   * @description:通过合同Id查询物资清单
   * @author: fengzt
   * @createDate: 2015年9月22日
   * @param paramsPage
   * @return:Page<PurInvoiceAssetVo>
   */
  @Override
  public Page<PurInvoiceAssetVo> queryWuziByContractId(Page<PurInvoiceAssetVo> paramsPage) {
    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    String siteId = userInfoScope.getSiteId();
    paramsPage.setParameter("siteId", siteId);

    List<PurInvoiceAssetVo> result = pubInterface.queryWuziByContractId(paramsPage);
    paramsPage.setResults(result);
    return paramsPage;
  }

  /**
   * 
   * @description:插入或者更新发票
   * @author: fengzt
   * @createDate: 2015年9月22日
   * @param formData
   * @param rowData
   * @return:int
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public int insertOrUpdateInvoice(String formData, String rowData) {
    LOG.debug("[PurInvoiceServiceImpl-insertOrUpdateInvoice]传入的参数 : formData = " + formData
        + " --- rowData = " + rowData);

    PurInvoiceBean purInvoiceBean = JsonHelper.fromJsonStringToBean(formData, PurInvoiceBean.class);
    List<PurInvoiceAssetVo> invoiceAssetVos = new ArrayList<PurInvoiceAssetVo>();
    // 判断是否有物资清单
    if (StringUtil.isNotBlank(rowData)) {
      invoiceAssetVos = JsonHelper.toList(rowData, PurInvoiceAssetVo.class);
    }
    // 主表单更新条数
    int count = 0;
    String id = purInvoiceBean.getId();
    // 计算不含税金额
    purInvoiceBean = sumNoTaxPrice(purInvoiceBean, invoiceAssetVos);
    // 已经存在 物资先删再插入
    if (StringUtils.isNotBlank(id)) {
      count = updateInvoice(purInvoiceBean);
      int delCount = deleteInvoiceByInvoiceId(id);
      int insertCount = insertWuziItem(invoiceAssetVos, purInvoiceBean.getId());
      LOG.info("PurInvoiceServiceImpl-updateInvoice 更新条数 ：" + count
          + " -- deleteInvoiceByInvoiceId 删除条数： " + delCount + " -- insertWuziItem插入物资条数："
          + insertCount);
    } else {
      count = insertInvoice(purInvoiceBean);
      int insertCount = insertWuziItem(invoiceAssetVos, purInvoiceBean.getId());
      LOG.info("PurInvoiceServiceImpl-updateInvoice 更新条数 ：" + count + " -- insertWuziItem插入物资条数："
          + insertCount);
    }

    return count;
  }

  /**
   * 
   * @description:根据发票ID删除明细
   * @author: fengzt
   * @createDate: 2015年9月22日
   * @param invoiceId
   * @return:int
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public int deleteInvoiceByInvoiceId(String invoiceId) {
    return purInvoiceDao.deleteInvoiceByInvoiceId(invoiceId);
  }

  /**
   * 
   * @description:更新发票主信息
   * @author: fengzt
   * @createDate: 2015年9月22日
   * @param purInvoiceBean
   * @return:int
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public int updateInvoice(PurInvoiceBean bean) {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

    bean.setModifyuser(userInfo.getUserId());
    bean.setModifyUserName(userInfo.getUserName());
    bean.setModifydate(new Date());
    // 到期日期
    Date endDate = DateUtils.addDays(bean.getInvoiceCreateDate(), EFFECTIVEDATE);
    bean.setEndDate(endDate);
    return purInvoiceDao.updateInvoice(bean);
  }

  /**
   * 
   * @description:插入物资明细
   * @author: fengzt
   * @createDate: 2015年9月22日
   * @param invoiceAssetVos
   * @param string
   * @return:int
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public int insertWuziItem(List<PurInvoiceAssetVo> invoiceAssetVos, String invoiceId) {
    List<PurInvoiceItemBean> itemBeans = new ArrayList<PurInvoiceItemBean>();
    for (PurInvoiceAssetVo vo : invoiceAssetVos) {
      PurInvoiceItemBean bean = new PurInvoiceItemBean();
      bean.setId(UUIDGenerator.getUUID());
      bean.setNoTaxSumPrice(vo.getNoTaxSumPrice());
      bean.setInvoiceId(invoiceId);

      bean.setImtNo(vo.getImtNo());
      bean.setItemId(vo.getItemId());
      bean.setTax(vo.getTaxSum());
      itemBeans.add(bean);
    }
    if (itemBeans.size() > 0) {
      return purInvoiceDao.insertWuziItem(itemBeans);
    }
    return 0;
  }

  /**
   * 
   * @description:插入发票
   * @author: fengzt
   * @createDate: 2015年9月22日
   * @param purInvoiceBean
   * @return:int
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public int insertInvoice(PurInvoiceBean bean) {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

    bean.setId(UUIDGenerator.getUUID());
    bean.setSiteid(userInfo.getSiteId());
    bean.setDeptid(userInfo.getOrgId());
    bean.setCreatedate(new Date());

    bean.setCreateuser(userInfo.getUserId());
    bean.setCreateUserName(userInfo.getUserName());
    bean.setIsDelete("N");
    bean.setEffectiveDate(EFFECTIVEDATE);

    // 到期日期
    Date endDate = DateUtils.addDays(bean.getInvoiceCreateDate(), EFFECTIVEDATE);
    bean.setEndDate(endDate);
    // 未报账
    bean.setStatus("PUR_INVOICE_STATUS_1");

    return purInvoiceDao.insertInvoice(bean);
  }

  /**
   * 
   * @description:计算不含税金额
   * @author: fengzt
   * @createDate: 2015年9月22日
   * @param purInvoiceBean
   * @param invoiceAssetVos
   * @return:PurInvoiceBean
   */
  private PurInvoiceBean sumNoTaxPrice(PurInvoiceBean purInvoiceBean,
      List<PurInvoiceAssetVo> invoiceAssetVos) {
    if (invoiceAssetVos.size() > 0) {
      double sumPrice = 0.0;
      for (PurInvoiceAssetVo vo : invoiceAssetVos) {
        sumPrice += vo.getNoTaxSumPrice();
      }
      purInvoiceBean.setNoTaxSumPrice(sumPrice);
    }

    return purInvoiceBean;
  }

  /**
   * 
   * @description:根据采购合同id查询采购合同物资列表信息
   * @author: fengzt
   * @createDate: 2015年9月24日
   * @param contractId
   * @return:Map<String, Object>
   */
  @Override
  public List<PurInvoiceAssetVo> queryWuziByContractId(String contractId) {
    List<PurInvoiceAssetVo> vos = pubInterface.queryPurInvoiceAssetVoBySheetId(contractId);
    // 去掉没有接收的物资
    List<PurInvoiceAssetVo> result = new ArrayList<PurInvoiceAssetVo>();
    for (PurInvoiceAssetVo vo : vos) {
      if (StringUtils.isNotBlank(vo.getImtNo())) {
        vo.setNoTaxSumPrice(vo.getNoTaxInvoicePrice() * vo.getReceivedMount());
        result.add(vo);
      }
    }

    return result;
  }

  /**
   * 
   * @description:发票提醒
   * @author: fengzt
   * @createDate: 2015年9月24日:
   */
  @Override
  public void remindInvoice(String siteId) {
    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    Map<String, Object> paramMap = new HashMap<String, Object>();
    paramMap.put("siteId", siteId);
    paramMap.put("status", "PUR_INVOICE_STATUS_1");
    paramMap.put("isDelete", "N");
    paramMap.put("endDate", CommonUtil.formatDate(DateUtils.addDays(new Date(), 31), "yyyy-MM-dd"));
    // 发票提醒 endDate - sysDate <= 30 siteId
    List<PurInvoiceScheduleVo> result = purInvoiceDao.remindInvoice(paramMap);

    for (PurInvoiceScheduleVo vo : result) {
      HomepageWorkTask task = new HomepageWorkTask();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      String flowNo = "SS" + sdf.format(new Date())
          + String.valueOf(100 + new Random().nextInt(900));
      task.setFlow(flowNo);
      task.setProcessInstId(vo.getId());

      double laveDay = CommonUtil.dateDiff("dd", new Date(), vo.getEndDate());

      String name = "";
      if (laveDay > 0) {
        name = "发票" + vo.getInvoiceNo() + "还有" + new Double(laveDay).intValue() + "天过期，请及时报账。";
      } else {
        name = "发票" + vo.getInvoiceNo() + "已经过期" + -new Double(laveDay).intValue() + "天，请及时报账。";
      }
      // 名称
      task.setName(name);
      task.setStatusName("发票过期提醒");
      task.setTypeName("发票过期提醒");
      task.setUrl("purchase/purInvoice/updateInvoiceToPage.do?id=" + vo.getId());

      // 提醒用户
      List<String> userIds = new ArrayList<String>();
      userIds.add(vo.getCreateuser());
      userIds.add(vo.getContractUser());
      for (String uId : userIds) {
        List<String> uIds = new ArrayList<String>();
        uIds.add(uId);
        task.setProcessInstId(vo.getId() + uId);
        homepageService.createNoticeWithOutWorkflow(task, uIds, userInfoScope, "WARN");
      }
    }

  }

  /**
   * 
   * @description:通过ID查询发票主信息
   * @author: fengzt
   * @createDate: 2015年9月25日
   * @param id
   * @return:PurInvoiceBean
   */
  @Override
  public PurInvoiceBean queryInvoiceById(String id) {
    return purInvoiceDao.queryInvoiceById(id);
  }

  /**
   * 
   * @description:通过发票ID查询物资子项
   * @author: fengzt
   * @createDate: 2015年9月25日
   * @param invoiceId
   * @return:List<PurInvoiceAssetVo>
   */
  @Override
  public List<PurInvoiceAssetVo> queryInvoiceItemById(String invoiceId) {
    // 发票主信息
    PurInvoiceBean purInvoiceBean = queryInvoiceById(invoiceId);
    // 通过合同ID拿出物质明细
    List<PurInvoiceAssetVo> vos = pubInterface.queryPurInvoiceAssetVoBySheetId(purInvoiceBean
        .getContractId());
    // 发票id拿出明细
    List<PurInvoiceItemBean> itemBeans = purInvoiceDao.queryInvoiceItemById(invoiceId);

    List<PurInvoiceAssetVo> resultList = new ArrayList<PurInvoiceAssetVo>();
    for (PurInvoiceItemBean item : itemBeans) {
      for (PurInvoiceAssetVo vo : vos) {
        // 筛选出当前发票有的物资
        if (StringUtils.equals(vo.getImtNo(), item.getImtNo())
            && StringUtils.equals(vo.getItemId(), item.getItemId())) {
          vo.setNoTaxSumPrice(item.getNoTaxSumPrice());
          vo.setTaxSum(item.getTax());
          resultList.add(vo);
          break;
        }
      }
    }
    return resultList;
  }

  /**
   * 
   * @description:通过发票ID，更新业务状态（非物理删除）
   * @author: fengzt
   * @createDate: 2015年10月8日
   * @param id
   * @return:count
   */
  @Override
  public int deleteInvoiceById(String id) {
    PurInvoiceBean bean = new PurInvoiceBean();
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

    bean.setModifyuser(userInfo.getUserId());
    bean.setModifyUserName(userInfo.getUserName());
    bean.setModifydate(new Date());
    bean.setId(id);
    bean.setIsDelete("Y");

    return purInvoiceDao.updateInvoiceById(bean);
  }

  /**
   * 
   * @description:发票报账
   * @author: fengzt
   * @createDate: 2015年10月8日
   * @param id
   * @return:int
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public int updateInvoiceStatus(String id) {
    PurInvoiceBean bean = new PurInvoiceBean();
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

    bean.setModifyuser(userInfo.getUserId());
    bean.setModifyUserName(userInfo.getUserName());
    bean.setModifydate(new Date());
    bean.setId(id);
    bean.setStatus("PUR_INVOICE_STATUS_2");

    // 拿出id的bean
    PurInvoiceBean infoBean = queryInvoiceById(id);
    // 物资列表
    List<PurInvoiceItemBean> piibList = purInvoiceDao.queryInvoiceItemById(id);
    List<PurInvoiceItemBean> itemBeans = new ArrayList<PurInvoiceItemBean>();

    // 去掉已接收批次为空
    for (PurInvoiceItemBean item : piibList) {
      if (StringUtils.isNotBlank(item.getImtNo())) {
        itemBeans.add(item);
      }
    }

    // 报账接口
    pubInterface.updateTranDetailByInvoice(itemBeans, infoBean.getContractId());

    return purInvoiceDao.updateInvoiceById(bean);
  }

  /**
   * 
   * @description:根据合同ID查询发票基本信息
   * @author: fengzt
   * @createDate: 2015年10月8日
   * @param contractId
   * @return:List<PurInvoiceBean>
   */
  @Override
  public List<PurInvoiceBean> queryInvoiceBaseInfoByContractId(String contractId) {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("siteId", userInfo.getSiteId());
    map.put("isDelete", "N");
    map.put("contractId", contractId);

    return purInvoiceDao.queryInvoiceBaseInfoByContractId(map);
  }

  /**
   * 
   * @description:检查同一个站点下发票号是否重复
   * @author: fengzt
   * @createDate: 2015年10月10日
   * @param invoiceNo
   * @param id
   * @return: List<PurInvoiceBean>
   */
  @Override
  public List<PurInvoiceBean> queryCheckInvoiceNo(String invoiceNo, String id) {
    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("siteId", userInfoScope.getSiteId());
    if (StringUtils.isNotBlank(invoiceNo)) {
      invoiceNo = StringUtils.trim(invoiceNo);
    }
    map.put("invoiceNo", invoiceNo);
    map.put("isDelete", "N");
    if (StringUtils.isNotBlank(id)) {
      map.put("id", id);
    }

    return purInvoiceDao.queryCheckInvoiceNo(map);
  }

  /**
   * 
   * @description:查询所有站点
   * @author: fengzt
   * @createDate: 2015年10月13日
   * @return:List<String>
   */
  @Override
  public List<String> queryAllSite() {
    return purInvoiceDao.queryAllSite();
  }

}
