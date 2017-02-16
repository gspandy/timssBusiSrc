package com.timss.purchase.service.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.service.InvMatTranDetailService;
import com.timss.purchase.bean.PurApply;
import com.timss.purchase.bean.PurAttachMapping;
import com.timss.purchase.bean.PurInvoiceBean;
import com.timss.purchase.bean.PurOrder;
import com.timss.purchase.bean.PurOrderItem;
import com.timss.purchase.bean.PurOrderPurchaserBean;
import com.timss.purchase.bean.PurPaSyncHhc;
import com.timss.purchase.bean.PurPoSyncHhc;
import com.timss.purchase.bean.PurPolicy;
import com.timss.purchase.bean.PurPolicyTemp;
import com.timss.purchase.bean.PurProcessMapping;
import com.timss.purchase.bean.PurPurchaserBean;
import com.timss.purchase.dao.PurApplyDao;
import com.timss.purchase.dao.PurOrderDao;
import com.timss.purchase.dao.PurProcessMappingDao;
import com.timss.purchase.service.PurApplyService;
import com.timss.purchase.service.PurAttachMappingService;
import com.timss.purchase.service.PurOrderService;
import com.timss.purchase.service.PurPaSyncHhcService;
import com.timss.purchase.service.PurPoSyncHhcService;
import com.timss.purchase.service.PurProcessMappingService;
import com.timss.purchase.service.PurPubInterface;
import com.timss.purchase.service.PurPurchaserService;
import com.timss.purchase.utils.CommonUtil;
import com.timss.purchase.vo.PurApplyItemVO;
import com.timss.purchase.vo.PurApplyOrderItemVO;
import com.timss.purchase.vo.PurBuisCalaVO;
import com.timss.purchase.vo.PurOrderItemVO;
import com.timss.purchase.vo.PurOrderVO;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.bean.ProcessFucExtParam;
import com.yudean.homepage.bean.WorktaskBean;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dao.support.LogMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.dto.support.AppLog;
import com.yudean.itc.dto.support.Configuration;
import com.yudean.itc.util.ClassCastUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.itc.webservice.HhcWebEamPoService;
import com.yudean.itc.webservice.bean.EamPo;
import com.yudean.itc.webservice.bean.EamPoCom;
import com.yudean.itc.webservice.bean.EamPoLine;
import com.yudean.itc.webservice.bean.EamPoTerm;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.bean.userinfo.impl.UserInfoScopeImpl;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurOrderServiceImpl.java
 * @author: 890166
 * @createDate: 2014-6-30
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("PurOrderServiceImpl")
public class PurOrderServiceImpl implements PurOrderService {

  /**
   * log4j输出
   */
  private static final Logger LOG = Logger.getLogger(PurOrderServiceImpl.class);

  /**
   * 注入Dao
   */
  @Autowired
  private PurOrderDao purOrderDao;

  @Autowired
  private PurApplyDao purApplyDao;

  @Autowired
  private PurProcessMappingDao purProcessMappingDao;

  @Autowired
  private HhcWebEamPoService hhcWebEamPoService;

  @Autowired
  private ItcMvcService itcMvcService;

  @Autowired
  private PurPoSyncHhcService purPoSyncHhcService;

  @Autowired
  private PurPaSyncHhcService purPaSyncHhcService;

  @Autowired
  private WorkflowService workflowService;

  @Autowired
  private HomepageService homepageService;

  @Autowired
  private PurPubInterface purPubInterface;

  @Autowired
  private InvMatTranDetailService invMatTranDetailService;
  
  @Autowired
  private PurPurchaserService purPurchaserService;

  @Autowired
  private PurAttachMappingService purAttachMappingService;

  @Autowired
  private AttachmentMapper attachmentMapper;

  @Autowired
  private PurProcessMappingService purProcessMappingService;

  @Autowired
  private LogMapper lMapper;
  
  @Autowired
  private PurApplyService purApplyService;

  /**
   * @description:查询采购单列表
   * @author: 890166
   * @createDate: 2014-6-30
   * @param userinfo
   * @param purOrderVO
   * @return
   * @throws Exception
   */
  @Override
  public Page<PurOrderVO> queryPurOrder(@Operator UserInfo userinfo,
      @LogParam("purOrderVO") PurOrderVO purOrderVO) throws Exception {
    UserInfoScope scope = (UserInfoScope) userinfo;
    Page<PurOrderVO> page = scope.getPage();
    page.setParameter("siteId", scope.getSiteId());
    page.setParameter("userId", scope.getUserId());
    String searchStr = String.valueOf(scope.getParam("searchStr") == null ? "" : scope.getParam("searchStr"));
    String sort = String.valueOf(scope.getParam("sort") == null ? "" : scope.getParam("sort"));
    sort = sort.replace( "totalPrice", " cast (total_price as decimal )" );
    String order = String.valueOf(scope.getParam("order") == null ? "" : scope.getParam("order"));
    if (!"".equals(sort) && !"".equals(order)) {
      page.setSortKey(sort);
      page.setSortOrder(order);
    } else {
      page.setSortKey("createdatesec");
      page.setSortOrder("desc");
    }
    if ( StringUtils.isNotBlank( searchStr ) ) {
        Map<String, Object> spParams = MapHelper.jsonToHashMap( searchStr );
        if ( spParams.containsKey( "item" ) ) {
            page.setParameter( "item", spParams.get( "item" ) );
        }
    }
    // 查询参数处理
    Map<String, String[]> params = scope.getParamMap();
    if (params.containsKey("search")) {
        String fuzzySearchParams = scope.getParam("search");
        LOG.info("查询合同列表的条件：" + fuzzySearchParams);
        Map<String, Object> fuzzySearchParamsMap = MapHelper.jsonToHashMap(fuzzySearchParams);
        Map<String, Object> fuzzyParams = new HashMap<String, Object>(0);
        if(fuzzySearchParamsMap.containsKey( "spNo" )){
            fuzzyParams.put( "spNo", fuzzySearchParamsMap.get( "spNo" ) );
        }
        // 自动的会封装模糊搜索条件
        page.setFuzzyParams(fuzzyParams);
      }
    if (null != purOrderVO) {
      page.setParameter("sheetno", purOrderVO.getSheetno());
      page.setParameter("sheetname", purOrderVO.getSheetname());
      page.setParameter("username", purOrderVO.getUsername());
      page.setParameter("department", purOrderVO.getDepartment());
      page.setParameter("createdate", purOrderVO.getCreatedate());
      page.setParameter("dhdate", purOrderVO.getDhdate());
      page.setParameter("totalPrice", purOrderVO.getTotalPrice());
      page.setParameter("statusName", purOrderVO.getStatusName());
      page.setParameter("supComName", purOrderVO.getSupComName());
      page.setParameter("applysheetno", purOrderVO.getApplysheetno());
      page.setParameter("curHandler", purOrderVO.getCurHandler());
      page.setParameter("itemCode", purOrderVO.getItemCode());
      page.setParameter("itemName", purOrderVO.getItemName());
    }
    List<PurOrderVO> ret = purOrderDao.queryPurOrder(page);
    page.setResults(ret);
    return page;
  }

  /**
   * @description:查询详细信息页面的时候表单信息获取
   * @author: 890166
   * @createDate: 2014-6-26
   * @param sheetId
   *          审批id
   * @return
   * @throws Exception
   */
  @Override
  public List<PurOrderVO> queryPurOrderInfoBySheetId(String sheetId) throws Exception {
    return purOrderDao.queryPurOrderInfoBySheetId(sheetId);
  }

  /**
   * @description:弹出框内的申请物资信息列表
   * @author: 890166
   * @createDate: 2014-6-30
   * @param userInfo
   * @param purOrderItemVO
   * @return
   * @throws Exception
   */
  @Override
  public Page<PurOrderItemVO> queryItemByEntity(@Operator UserInfoScope userInfo,
      @LogParam("purOrderItemVO") PurOrderItemVO purOrderItemVO,List<PurOrderItemVO> includeitems) throws Exception {
    UserInfoScope scope = userInfo;
    Page<PurOrderItemVO> page = scope.getPage();
    page.setParameter("siteid", userInfo.getSiteId());

    if (null != purOrderItemVO) {
      page.setParameter("sheetId", purOrderItemVO.getSheetId());
      page.setParameter("sheetno", purOrderItemVO.getSheetno());
      page.setParameter("sheetName", purOrderItemVO.getSheetName());
      page.setParameter("itemid", purOrderItemVO.getItemid());
      page.setParameter("itemname", purOrderItemVO.getItemname());
      page.setParameter("itemcus", purOrderItemVO.getItemcus());
      page.setParameter("itemnum", purOrderItemVO.getItemnum());
    }
    if ( 0< includeitems.size() ) {
        StringBuffer sqlString = new StringBuffer(" or (  0=1 ");
        for ( PurOrderItemVO tmp : includeitems ) {
            sqlString.append( "or t.itemid = '"+tmp.getItemid()+ "' and t.invcateid = '"+tmp.getInvcateid()+"' and t.sheetno = '"+tmp.getSheetno()+"' " );
        }
        sqlString .append( ")   and t.siteid = '"+userInfo.getSiteId()+"' and t.itemnum = 0 " );
        page.setParameter("includeitems", sqlString.toString());
    }
    List<PurOrderItemVO> ret = purApplyDao.queryApplyItemByEntity(page);
    page.setResults(ret);
    return page;
  }

  /**
   * 弹出框内的申请物资信息列表
   */
  @Override
  public List<PurOrderItemVO> queryItemByList(List<String> diffList) throws Exception {
    List<PurOrderItemVO> ret = new ArrayList<PurOrderItemVO>();
    if (null != diffList && !diffList.isEmpty()) {
      ret = purApplyDao.queryApplyItemByList(diffList);
    }
    return ret;
  }

  /**
   * @description:详细表单中列表
   * @author: 890166
   * @createDate: 2014-7-1
   * @param userInfo
   * @param sheetId
   * @return
   * @throws Exception
   */
  @Override
  public Page<PurOrderItemVO> queryPurOrderItemList(@Operator UserInfoScope userInfo,
      @LogParam("sheetId") String sheetId) throws Exception {
    UserInfoScope scope = userInfo;
    Page<PurOrderItemVO> page = scope.getPage();

    String pageSize = CommonUtil.getProperties("pageSize");
    page.setPageSize(Integer.valueOf(pageSize));

    page.setParameter("sheetId", sheetId);
    page.setParameter("siteid", userInfo.getSiteId());
    List<PurOrderItemVO> ret = purOrderDao.queryPurOrderItemList(page);
    page.setResults(ret);
    return page;
  }

  /**
   * @description:物资合并查询
   * @author: 890166
   * @createDate: 2014-7-1
   * @param userInfo
   * @param sheetId
   * @return
   * @throws Exception
   */
  @Override
  public Page<PurOrderItemVO> queryPurOrderItemListExce(@Operator UserInfoScope userInfo,
      @LogParam("sheetId") String sheetId) throws Exception {
    UserInfoScope scope = userInfo;
    Page<PurOrderItemVO> page = scope.getPage();

    String pageSize = CommonUtil.getProperties("pageSize");
    page.setPageSize(Integer.valueOf(pageSize));

    page.setParameter("sheetId", sheetId);
    page.setParameter("siteid", userInfo.getSiteId());
    List<PurOrderItemVO> ret = purOrderDao.queryPurOrderItemListExce(page);
    page.setResults(ret);
    return page;
  }

  /**
   * @description:通过sheetid找到PurOrderItem
   * @author: 890166
   * @createDate: 2014-7-1
   * @param sheetId
   * @return
   * @throws Exception
   */
  @Override
  public List<String> queryPurOrderItemExists(String sheetId) throws Exception {
    return purOrderDao.queryPurOrderItemExists(sheetId);
  }

  /**
   * @description:根据sheetid查询PurOrder
   * @author: 890166
   * @createDate: 2014-7-1
   * @param sheetId
   * @return:
   */
  @Override
  public PurOrder queryPurOrderBySheetId(String sheetId) throws Exception {
    PurOrder po = null;
    List<PurOrder> poList = purOrderDao.queryPurOrderBySheetId(sheetId);
    if (null != poList && !poList.isEmpty()) {
      po = poList.get(0);
    }
    return po;
  }

  /**
   * @description:根据sheetid查询PurOrder(外部接口使用)
   * @author: 890166
   * @createDate: 2014-8-26
   * @param paramIds
   * @return
   * @throws Exception
   *           :
   */
  @Override
  public List<PurOrder> queryPurOrderBySheetIds(String paramIds) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("sheetId", paramIds);
    List<PurOrder> poList = purOrderDao.queryPurOrderBySheetIds(map);
    return poList;
  }

  /**
   * @description: 保存主单信息
   * @author: 890166
   * @createDate: 2015-3-24
   * @param po
   * @param sheetId
   * @param status
   * @param type
   * @param userInfo
   * @return
   * @throws Exception
   *           :
   */
  private PurOrder savePurOrderInfo(PurOrder po, String sheetId, String status, String type,
      UserInfoScope userInfo) throws Exception {
    if ( "SWF".equals( userInfo.getSiteId() ) && StringUtils.isNotEmpty( po.getSpNo() )) {
      po.setSpNo( "YD/ZSD-WS-"+po.getSpNo().replaceAll( "YD/ZSD-WS-", "" ) );
    }
    if (!"".equals(sheetId) && !"null".equals(sheetId)) {
      po.setSheetId(sheetId);
      po.setModifyaccount(userInfo.getUserId());
      po.setModifydate(new Date());
      po.setStatus(status);
      updatePurOrderInfo(po);
    } else {
      if ("submit".equals(type)) {
        po.setStatus(status);
      } else {
        po.setStatus("4");
      }
      po.setSheetno(null);
      po.setSiteid(userInfo.getSiteId());
      po.setCreateaccount(userInfo.getUserId());
      po.setCreatedate(new Date());
      insertPurOrderInfo(po);
      // 需要添加买方信息
      String siteId = userInfo.getSiteId();
      String needBuySite = CommonUtil.getProperties("needBuySite");
      if (needBuySite.indexOf(siteId) > -1) {
        savePurOrderPurchaser(po, userInfo);
      }
    }
    return po;
  }

  /**
   * @description: 保存买方信息
   * @author: yuanzh
   * @createDate: 2015-10-21
   * @param po
   * @param userInfo
   *          :
   */
  private void savePurOrderPurchaser(PurOrder po, UserInfoScope userInfo) {
    PurPurchaserBean pb = purPurchaserService.queryPurPurchaserBySiteId(userInfo);
    PurOrderPurchaserBean pob = new PurOrderPurchaserBean();
    pob.setSheetId(po.getSheetId());
    pob.setAccount(pb.getAccount());
    pob.setAddress(pb.getAddress());
    pob.setBank(pb.getBank());
    pob.setFax(pb.getFax());
    pob.setPhone(pb.getPhone());
    pob.setPurchaserName(pb.getPurchaserName());
    pob.setSiteid(userInfo.getSiteId());
    purPurchaserService.insertPurOrderPurchaser(userInfo, pob);
  }

  /**
   * @description: 保存合同条款信息
   * @author: yuanzh
   * @createDate: 2015-9-29
   * @param po
   * @param ppList
   *          :
   */
  private void savePurPolicyInfo(PurOrder po, List<PurPolicy> ppList) {
    for (PurPolicy pp : ppList) {
      pp.setSheetId(po.getSheetId());
      purOrderDao.insertPurOrderPolicy(pp);
    }
  }

  /**
   * @description: 保存流程实例id信息到外部表中
   * @author: 890166
   * @createDate: 2015-3-30
   * @param po
   * @param userInfo
   * @param taskName
   * @param processInstanceId
   * @throws Exception
   *           :
   */
  private void savePurProcessMapping(PurOrder po, UserInfoScope userInfo, String taskName,
      String processInstanceId) throws Exception {

    // 当前任务id为空且流程实例不为空的时候，保存流程实例信息
    PurProcessMapping ppm = new PurProcessMapping();
    ppm.setMasterkey(String.valueOf(po.getSheetId()));
    ppm.setModeltype("purorder");
    ppm.setSiteid(po.getSiteid());
    ppm.setCurHandler(userInfo.getUserName());
    ppm.setCurLink(taskName);
    String processId = purProcessMappingDao.queryProcessIdByEntity(ppm);

    if (!"".equals(processId) && null != processId) {
      ppm.setProcessid(processId);
      updatePurProcessMapping(ppm);
    } else {
      ppm.setProcessid(processInstanceId);
      insertPurProcessMapping(ppm);
    }
  }

  /**
   * @description: 保存子单数据
   * @author: 890166
   * @createDate: 2015-3-24
   * @param poiList
   * @param po
   * @return
   * @throws Exception
   *           :
   */
  private boolean savePurOrderItemInfo(List<PurOrderItem> poiList, PurOrder po, String delFlag)
      throws Exception {
    boolean flag = false;
    try {
      List<String> pageArr = new ArrayList<String>();
      double rowTotals = 0.000000D;
      for (PurOrderItem poi : poiList) {
        double tax = poi.getPrice().doubleValue() * (poi.getTaxRate().doubleValue() * 0.01);
        poi.setTax(new BigDecimal(tax));
        poi.setSheetId(po.getSheetId());

        saveOrUpdatePurOrderItem(poi);

        pageArr.add(poi.getItemid() + "_" + poi.getApplySheetId() + "_" + poi.getWarehouseid()+"_"+ poi.getInvcateid());

        rowTotals += poi.getCost().doubleValue()
            * poi.getItemnum().doubleValue();
      }
      po.setTotalPrice(new BigDecimal(rowTotals));
      purOrderDao.updatePurOrderInfo(po);

      List<String> pOrderIList = this.queryPurOrderItemExists(po.getSheetId());
      pOrderIList.addAll(pageArr);

      Map<String, List<String>> maps = CommonUtil.getListDifferAndSimilar(pageArr, pOrderIList);
      // diffList格式为 itemId_applySheetId
      List<String> diffList = maps.get("diff");
      if (null != diffList && !diffList.isEmpty()) {
        Map<String, Object> deleteParams = null;

        for (String difId : diffList) {
          String[] idArr = difId.split("_");
          deleteParams = new HashMap<String, Object>();
          deleteParams.put("itemid", idArr[0]);
          deleteParams.put("applySheetId", idArr[1]);
          deleteParams.put("sheetId", po.getSheetId());
          deleteParams.put("siteId", po.getSiteid());
          deleteParams.put("warehouseid", idArr[2]);
          deleteParams.put("invcateid", idArr[3]);
          deleteParams.put("delFlag", delFlag);
          this.callProcPurOrderItemDelete(deleteParams);
        }
      }
      flag = true;
    } catch (Exception e) {
      LOG.info("--------------------------------------------");
      LOG.info("- PurOrderServiceImpl 中的 savePurOrderItemInfo 方法抛出异常：", e);
      LOG.info("--------------------------------------------");
      flag = false;
    }
    return flag;
  }

  /**
   * @description:更新PurOrder信息
   * @author: 890166
   * @createDate: 2014-7-1
   * @param po
   * @return
   * @throws Exception
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int updatePurOrderInfo(PurOrder po) throws Exception {
    return purOrderDao.updatePurOrderInfo(po);
  }

  /**
   * @description:插入PurOrder信息
   * @author: 890166
   * @createDate: 2014-7-1
   * @param po
   * @return
   * @throws Exception
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int insertPurOrderInfo(PurOrder po) throws Exception {
    return purOrderDao.insertPurOrderInfo(po);
  }

  /**
   * @description:调用插入PurOrderItem的存储过程
   * @author: 890166
   * @createDate: 2014-7-1
   * @param list
   * @throws Exception
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void callProcPurOrderItemInsert(Map<String, Object> params) throws Exception {
    purOrderDao.callProcPurOrderItemInsert(params);
  }

  /**
   * @description:调用删除PurOrderItem的存储过程
   * @author: 890166
   * @createDate: 2014-7-1
   * @param list
   * @throws Exception
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void callProcPurOrderItemDelete(Map<String, Object> params) throws Exception {
    // purOrderDao.callProcPurOrderItemDelete(params);
    // 用java代码代替存储过程 refactor --begin
    BigDecimal orderNum = BigDecimal.valueOf( 0 );
    BigDecimal applyItemNum = BigDecimal.valueOf( 0 );
    String status = "4";
    // 查询采购合同物资信息
    Map<String, Object> params_tmp = new HashMap<String, Object>(0);
    params_tmp.put("itemid", params.get("itemid"));
    params_tmp.put("applySheetId", params.get("applySheetId"));
    params_tmp.put("sheetId", params.get("sheetId"));
    params_tmp.put("warehouseid", params.get("warehouseid"));
    params_tmp.put("invcateid", params.get("invcateid"));
    List<PurOrderItemVO> orderList = purOrderDao.queryPurOrderItemListByCondition(params_tmp);
    if (0 < orderList.size()) {
      orderNum = BigDecimal.valueOf(Double.valueOf(orderList.get(0).getItemnum()));
    }
    LOG.info("--callProcPurOrderItemDelete orderNum:" + orderNum + " --");
    // 删除采购合同物资信息
    purOrderDao.deletePurOrderItemListByCondition(params_tmp);
    // 查询对应采购申请的物资信息
    params_tmp = new HashMap<String, Object>(0);
    params_tmp.put("itemid", params.get("itemid"));
    params_tmp.put("applySheetId", params.get("applySheetId"));
    params_tmp.put("warehouseid", params.get("warehouseid"));
    List<PurOrderItemVO> applyList = purOrderDao.queryPurOrderItemListByCondition(params_tmp);
    if (0 < applyList.size()) {
      applyItemNum = BigDecimal.valueOf(Double.valueOf(applyList.get(0).getItemnum()));
    }
    LOG.info("--callProcPurOrderItemDelete applyItemNum:" + applyItemNum + " --");
    // 根据申请与预定的数量设置申请物资信息的状态
    if (1==applyItemNum.compareTo(orderNum) ) {
      status = "5";
    }
    // 更新采购申请的物资信息
    params_tmp = new HashMap<String, Object>(0);
    params_tmp.put("itemid", params.get("itemid"));
    params_tmp.put("applySheetId", params.get("applySheetId"));
    params_tmp.put("status", status);
    params_tmp.put("warehouseid", params.get("warehouseid"));

    boolean delFlag = Boolean.valueOf(String.valueOf(params.get("delFlag")));
    if (!delFlag) {
      purOrderDao.updatePurOrderItemByCondition(params_tmp);
    }

    LOG.info("--callProcPurOrderItemDelete end --");
    // 用java代码代替存储过程 refactor --end
  }

  /**
   * @description:新增流程映射记录
   * @author: 890166
   * @createDate: 2014-7-4
   * @param sheetId
   * @param siteId
   * @param modelType
   * @return
   * @throws Exception
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int insertPurProcessMapping(PurProcessMapping ppm) throws Exception {
    return purProcessMappingDao.insertPurProcessMapping(ppm);
  }

  /**
   * @description:更新流程映射记录
   * @author: 890166
   * @createDate: 2014-7-4
   * @param sheetId
   * @param siteId
   * @param modelType
   * @return
   * @throws Exception
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int updatePurProcessMapping(PurProcessMapping ppm) throws Exception {
    return purProcessMappingDao.updatePurProcessMapping(ppm);
  }

  /**
   * @description:采购单统一保存方法
   * @author: 890166
   * @createDate: 2014-7-7
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public boolean saveOrUpdatePurOrder(@Operator UserInfoScope userInfo,
      @LogParam("po") PurOrder po, List<PurOrderItem> poiList, Map<String, Object> paramMap)
      throws Exception {
    boolean flag = true;
    boolean saveFlag = true;
    String policyListStr = itcMvcService.getUserInfoScopeDatas().getParam("policyList");
    List<Map<String, Object>> policyList = new ArrayList<Map<String, Object>>(0);
    if (StringUtils.isNotEmpty(policyListStr)) {
      policyList = JsonHelper.fromJsonStringToBean(policyListStr, List.class);
    }
    try {
      String type = paramMap.get("type") == null ? "" : String.valueOf(paramMap.get("type"));
      String sheetId = paramMap.get("sheetId") == null ? "" : String.valueOf(paramMap
          .get("sheetId"));
      String processInstanceId = paramMap.get("processInstanceId") == null ? "" : String
          .valueOf(paramMap.get("processInstanceId")); // 流程实例
      String taskName = paramMap.get("taskName") == null ? "" : String.valueOf(paramMap
          .get("taskName")); // 任务名称
      String status = paramMap.get("status") == null ? "0" : String.valueOf(paramMap.get("status")); // 状态

      String delFlag = String.valueOf(paramMap.get("delFlag"));
      
      //原来设置在采购申请最后一个环节的 更新采购申请明细状态的操作移动到这个地方执行 --begin
      if("null".equals( sheetId ) ){
          for (PurOrderItem poi :poiList ) {
              String applySheetId = poi.getApplySheetId();
              String itemId = poi.getItemid();
              String invcateId = poi.getInvcateid();
              purApplyService.updatePurApplyItemStatus("3", applySheetId, itemId, invcateId);
          }
      }
      //原来设置在采购申请最后一个环节的 更新采购申请明细状态的操作移动到这个地方执行 --end
      if ("save".equals(type)) {
        status = "4"; // 4 是草稿
      }
      if (null != po) {
        PurOrder newPurOrder = savePurOrderInfo(po, sheetId, status, type, userInfo);
        // add by yuanzh 添加流程使用变量
        workflowService.setVariable(String.valueOf(paramMap.get("processInstanceId")), "sheetId",
            newPurOrder.getSheetId());
        workflowService.setVariable(String.valueOf(paramMap.get("processInstanceId")), "isGm",
            String.valueOf(paramMap.get("isGm")));
        if ( null !=paramMap.get("isFormal") ) {
            workflowService.setVariable(String.valueOf(paramMap.get("processInstanceId")), "isFormal",
                    String.valueOf(paramMap.get("isFormal")));
        }
        sheetId = newPurOrder.getSheetId();
        if (!"1".equals(status)) {
          // 先删除
          purOrderDao.deletePurOrderPolicyBySheetId(sheetId);
          // 再添加
          for (Map<String, Object> map : policyList) {
            PurPolicy purPolicy = new PurPolicy();
            purPolicy.setPolicyContent(map.get("policyContent").toString());
            purPolicy.setSort(Integer.parseInt(map.get("sort").toString()));
            purPolicy.setSheetId(sheetId);
            purOrderDao.insertPurOrderPolicy(purPolicy);
          }
        }
        if (null != newPurOrder) {
          paramMap.put("sheetId", newPurOrder.getSheetId());
          paramMap.put("sheetNo", newPurOrder.getSheetno());
          paramMap.put("purorder", newPurOrder);
          if (null != poiList && !poiList.isEmpty()) {
            saveFlag = savePurOrderItemInfo(poiList, po, delFlag);
          }else{
            poiList = new ArrayList<PurOrderItem>(0);
            saveFlag = savePurOrderItemInfo(poiList, po, delFlag);
          }
          if (saveFlag) {
            savePurProcessMapping(newPurOrder, userInfo, taskName, processInstanceId);
          }
        }
        // 附件上传---湛江风电场
        String uploadIds = paramMap.get("uploadIds") == null ? "" : String.valueOf(paramMap
            .get("uploadIds"));
        purAttachMappingService.deletePurAttachMappingBySheetno(newPurOrder.getSheetno());
        if (!"".equals(uploadIds)) {
          String[] ids = uploadIds.split(",");
          // 先删除后插入
          for (int i = 0; i < ids.length; i++) {
            PurAttachMapping pam = new PurAttachMapping();
            pam.setSheetno(newPurOrder.getSheetno());
            pam.setAttachid(ids[i]);
            purAttachMappingService.insertPurAttachMapping(pam);
          }
          attachmentMapper.setAttachmentsBinded(ids, 1);
        }
      }
      // refactor：把设置流程变量和发送待办从controller中转移到service层 --start
      String processId = null == paramMap.get("processInstanceId") ? "" : String.valueOf(paramMap
          .get("processInstanceId"));
      String taskId = null == paramMap.get("taskId") ? "" : String.valueOf(paramMap.get("taskId")); // taskId
      Boolean isTaskIdEmpty = null == paramMap.get("isTaskIdEmpty") ? false : (Boolean) (paramMap
          .get("isTaskIdEmpty")); // isTaskIdEmpty
      // 补充设置流程变量 -start
      String isGm = null == paramMap.get("isGm") ? "" : String.valueOf(paramMap.get("isGm")); // 是否提交总经理
      String isFormal = null == paramMap.get("isFormal") ? "" : String.valueOf(paramMap.get("isFormal")); // 是否正式的采购合同
      if (StringUtils.isNotEmpty(processId)) {
        workflowService.setVariable(processId, "businessId", po.getSheetId());
        workflowService.setVariable(processId, "sheetIType", po.getSheetIType());
        workflowService.setVariable(processId, "totalPrice", po.getTotalPrice());
        workflowService.setVariable(processId, "isGm", isGm);
        if ( StringUtils.isNotEmpty( isFormal ) ) {
            workflowService.setVariable(processId, "isFormal", isFormal);
        }
      }
      // 补充设置流程变量 -end
      if (isTaskIdEmpty) {
        homepageService.createProcess(String.valueOf(paramMap.get("sheetNo")), processId, "采购单",
            po.getSheetname(), taskName, "/purchase/purorder/purOrderForm.do?taskId=" + taskId
                + "&processInstId=" + processId + "&type=edit&sheetId=" + po.getSheetId(),
            userInfo,  new ProcessFucExtParam());
      }else {
          homepageService.modify( null, String.valueOf(paramMap.get("sheetNo")), null, po.getSheetname(), null, null, null, null );
      }
      // refactor：把设置流程变量和发送待办从controller中转移到service层 --end

    } catch (Exception e) {
      LOG.info("--------------------------------------------");
      LOG.info("- PurOrderServiceImpl 中的 saveOrUpdatePurOrder 方法抛出异常：", e);
      LOG.info("--------------------------------------------");
      flag = false;
    }
    return flag;
  }

  /**
   * @description: 通过sheetNo和站点id找到申请表id
   * @author: 890166
   * @createDate: 2014-9-23
   * @param sheetNo
   * @param siteId
   * @return
   * @throws Exception
   *           :
   */
  @Override
  public String querySheetIdByFlowNo(String sheetNo, String siteId) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("sheetNo", sheetNo);
    map.put("siteId", siteId);
    return purOrderDao.querySheetIdByFlowNo(map);
  }

  /**
   * @description: 通过sheetid查询flowno
   * @author: 890166
   * @createDate: 2015-2-26
   * @param sheetId
   * @param siteId
   * @return
   * @throws Exception
   *           :
   */
  @Override
  public String queryFlowNoBySheetId(String sheetId, String siteId) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("sheetId", sheetId);
    map.put("siteId", siteId);
    return purOrderDao.queryFlowNoBySheetId(map);
  }

  /**
   * @description: 通过时间段查询采购总额
   * @author: 890166
   * @createDate: 2015-2-4
   * @param beginDate
   * @param endDate
   * @return
   * @throws Exception
   */
  @Override
  public String queryPurPriceTotal(String beginDate, String endDate, String siteId)
      throws Exception {
    String reStr = null;

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("beginDate", beginDate);
    map.put("endDate", endDate);
    map.put("siteId", siteId);
    BigDecimal bdTotal = purOrderDao.queryPurPriceTotal(map);
    if (null != bdTotal) {
      reStr = bdTotal.toString();
    }
    return reStr;
  }

  /**
   * @description: 处理商务网传送过来的数据
   * @author: 890166
   * @createDate: 2015-3-17
   * @param date
   * @throws Exception
   *           :
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void processACDataFormBusi(Date date) throws Exception {
    String parent = null;
    String child = null;
    List<AppEnum> aeList = itcMvcService.getEnum("SITE_IN_BUSI_MAPPING");
    for (AppEnum ae : aeList) {
      String[] siteInfo = ae.getCode().toLowerCase().split("_");
      if ("parent".equals(siteInfo[1])) {
        parent = siteInfo[0].toUpperCase();
      } else {
        child = siteInfo[0].toUpperCase();
      }
    }

    String noLocalProcess = CommonUtil.getProperties("noLocalProcess");
    // 1. 先增量获取商务网审批完成数据
    LOG.info("-------------------开始获取商务网信息-------------------");
    List<EamPoCom> epcList = hhcWebEamPoService.getPoIncrement(date, parent, child);
    LOG.info("-------------------结束获取商务网信息-------------------");

    if (null != epcList && !epcList.isEmpty()) {
      // 2. 通过循环epcList获取到多张审批完成的采购订单信息
      for (EamPoCom epc : epcList) {
        // 3. 获取每张采购订单的主单和子单的信息
        EamPo eamPo = epc.getEamPo();
        List<EamPoLine> eamPoLines = epc.getEamPoLines();

        // 3.1 获取每张采购合同条款信息
        //返回TIMSS的合同条款是否取TIMSS的标准条款
        boolean switchFlag = false;
        Configuration cf = itcMvcService.getConfiguration("purchase_item");
        if(null != cf){
        	switchFlag = Boolean.valueOf(cf.getVal());
        }
        List<EamPoTerm> eptList = new ArrayList<EamPoTerm>();
        if(true == switchFlag){
            PurPolicyTemp ppt = new PurPolicyTemp();
            UserInfoScope scope = itcMvcService.getUserInfoScopeDatas();
            ppt.setSiteid(scope.getSiteId());
            List<PurPolicyTemp> ret = purOrderDao.queryPurPolicyTempList(ppt);
            for(PurPolicyTemp item:ret){
            	EamPoTerm element = new EamPoTerm();
            	//获取内容
            	element.setDescription(item.getPolicyContent());
            	//获取排序
            	element.setSeqnum(Long.valueOf(item.getSort()));
            	//获取站点
            	element.setSiteid(item.getSiteid());
            	
            	eptList.add(element);
            }
        }else{
        	eptList = epc.getEamPoTerm();
        }
        

        // 4. 主单信息转换成purchOrder对应格式
        Map<String, Object> map = conventEamPo2Map(eamPo);
        map.put("timssSite", child == null ? parent : child);

        // 4.1 转换合同信息到合同条款表
        map = conventEamPoTerm2Map(eptList, map);

        // 5. 子单信息转换成purchOrderDetail对应格式
        map = conventEamPoLines2Map(eamPoLines, map);
        map.put("type", "submit");

        // 6. 保存采购订单信息
        boolean flag = saveHhcData2PurOrder(map);

        // 7. 启动新流程
        if (flag && noLocalProcess.indexOf(String.valueOf(map.get("timssSite"))) == -1) {
          startProcessFormHhcData(map);
        }
      }
    } else {
      LOG.info("-------------------没有获取到商务网数据-------------------");
    }
  }

  /**
   * @description: 保存商务网数据到采购业务表中
   * @author: 890166
   * @createDate: 2015-3-24
   * @param map
   * @return:
   */
  @SuppressWarnings("unchecked")
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  private boolean saveHhcData2PurOrder(Map<String, Object> map) {
    boolean flag = true;
    boolean saveFlag = false;
    String type = map.get("type") == null ? "" : String.valueOf(map.get("type"));
    String timssSite = map.get("timssSite") == null ? "" : String.valueOf(map.get("timssSite"));
    String status = null;
    String noLocalProcess = CommonUtil.getProperties("noLocalProcess");
    try {
      // 获取采购订单主单信息
      PurOrder po = (PurOrder) map.get("purOrder");
      po.setSiteid(timssSite);

      // 重新将采购订单的创建者设置userinfo信息
      UserInfo uInfo = itcMvcService.getUserInfo(po.getCreateaccount(), po.getSiteid());
      UserInfoScopeImpl userInfo = ClassCastUtil.castAllField2Class(UserInfoScopeImpl.class, uInfo);

      // 获取子单信息
      List<PurOrderItem> poiList = (List<PurOrderItem>) map.get("poiList");
      // 获取合同信息
      List<PurPolicy> ppList = (List<PurPolicy>) map.get("ppList");

      if (null != poiList && !poiList.isEmpty()) {

        // 新建主单信息
        if (noLocalProcess.indexOf(timssSite) == -1) {
          status = "0";
        } else {
          status = "1";
        }
        savePurOrderInfo(po, "", status, type, userInfo);

        // 若返回的po不为null，则保存子单信息
        if (null != po) {
          // 保存采购合同合同条款信息
          if (null != ppList && !ppList.isEmpty()) {
            savePurPolicyInfo(po, ppList);
          }

          // 将生成了sheetid和sheetno的po重新保存到map里面去，后面启动流程的时候有用
          map.put("purOrder", po);
          saveFlag = savePurOrderItemInfo(poiList, po, "false");
          // 若子单信息保存成功则保存采购订单中间表信息
          if (saveFlag) {
            Set<String> prSet = (Set<String>) map.get("prSet");
            // 获取中间表信息，并重新将timss的采购订单编号绑定到中间表中
            PurPoSyncHhc purPoSyncHhc = (PurPoSyncHhc) map.get("purPoSyncHhc");
            purPoSyncHhc.setSiteid(po.getSiteid());
            purPoSyncHhc.setTimPono(po.getSheetno());
            // 由于商务网中的数据有可能是打乱的，存在多个采购申请单的物资同在一个单中询价
            // 所以这里hhcpano有可能是多个，这里就要通过循环插入到中间表中
            for (String hhcPano : prSet) {
              purPoSyncHhc.setHhcPano(hhcPano);
              purPoSyncHhcService.insertPurPoSyncHhcInfo(purPoSyncHhc);
              // 2016-02-17 add by yuanzh 增加与采购申请对应的采购类型
              Map<String, Object> paraMap = new HashMap<String, Object>();
              paraMap.put("siteid", po.getSiteid());
              paraMap.put("hhcPano", hhcPano);
              List<PurPaSyncHhc> ppashList = purPaSyncHhcService.queryPurPaSyncHhcByMap(paraMap);

              if (null != ppashList && !ppashList.isEmpty()) {
                paraMap.put("sheetNo", ppashList.get(0).getTimPano());
                paraMap.put("siteId", po.getSiteid());
                String sheetId = purApplyDao.querySheetIdByFlowNo(paraMap);
                PurApply pa = purApplyDao.queryPurApplyBySheetId(sheetId);

                List<AppEnum> aeList = itcMvcService.getEnum("ITEMORDER_TYPE");
                for (AppEnum ae : aeList) {
                  if (ae.getLabel().equals(pa.getSheetclassid())) {
                    po.setSheetIType(ae.getCode());
                    purOrderDao.updatePurOrderInfo(po);
                    break;
                  }
                }
              }
            }
          }
        }

        // 重新更新一下采购申请单中的状态位
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("commitcommecnetwk", "0");

        if (noLocalProcess.indexOf(timssSite) == -1) {
          // 要走流程
          paramMap.put("status", "3");
        } else {
          // 不走流程
          paramMap.put("status", "4");
          // 不走流程就要模拟一个完成的流程信息出来
          PurProcessMapping ppm = new PurProcessMapping();
          ppm.setMasterkey(po.getSheetId());
          // 模拟一个流程id
          String random = String.valueOf(System.currentTimeMillis());
          ppm.setProcessid(random.substring(random.length() - 7));
          ppm.setModeltype("purorder");
          ppm.setSiteid(po.getSiteid());
          purProcessMappingDao.insertPurProcessMapping(ppm);
        }
        for (PurOrderItem poi : poiList) {
          paramMap.put("sheetId", poi.getApplySheetId());
          paramMap.put("itemId", poi.getItemid());
          paramMap.put("invcateId", poi.getInvcateid() );
          purApplyDao.updatePurApplyItemStatus(paramMap);
        }
        // 2016-3-8 modify by yuanzh 回写编号到商务网ponum字段
        hhcWebEamPoService.updateEamPoSeriesPonum(po.getSheetno(), po.getBusinessno());

        flag = true;
      } else {
        flag = false;
      }
    } catch (Exception e) {
      LOG.info("--------------------------------------------");
      LOG.info("- PurOrderServiceImpl 中的 saveHhcData2PurOrder 方法抛出异常：", e);
      LOG.info("--------------------------------------------");
      flag = false;
    }
    return flag;
  }

  /**
   * @description: 从后台直接触发采购单流程信息，第一环节所有采购专责都会受到信息
   * @author: 890166
   * @createDate: 2015-3-24
   * @param map
   * @throws Exception
   *           :
   */
  private void startProcessFormHhcData(Map<String, Object> map) throws Exception {
    // 获取到采购订单信息
    PurOrder po = (PurOrder) map.get("purOrder");
    // 获取站点信息和创建人信息
    String siteId = po.getSiteid();
    String userId = po.getCreateaccount();
    UserInfo uInfo = itcMvcService.getUserInfoById(userId);

    // 拿到相应的流程defkey创建流程（这里与设计不一样直接创建采购单流程）
    Task task = null;
    /*
     * String processDefKey = CommonUtil.getProperties("purorderKey"); String[] keyArr =
     * processDefKey.split(","); for (String key : keyArr) { if (key.indexOf(siteId.toLowerCase()) >
     * -1) { processDefKey = key; } }
     */
    String processDefKey = "purchase_[@@@]_purorder".replace("[@@@]", siteId.toLowerCase());
    String processKey = workflowService.queryForLatestProcessDefKey(processDefKey);
    ProcessInstance p = workflowService
        .startLatestProcessInstanceByDefKey(processKey, userId, null);

    // 创建流程后获取流程实例id，查询当前流程的第一个节点
    String processId = p.getProcessInstanceId();
    List<Task> taskList = workflowService.getActiveTasks(processId);
    if (null != taskList && !taskList.isEmpty()) {
      task = taskList.get(0);
    }

    // 创建一个list存放采购专责人员名单
    List<String> list = CommonUtil.returnUserIdInRole("cgzz", siteId, null);

    // 如果创建第一个环节成功并且获取到人员名单后，则添加到流程候选人中，并创建首页待办
    if (null != task && !list.isEmpty()) {
      UserInfoScopeImpl userInfo = ClassCastUtil.castAllField2Class(UserInfoScopeImpl.class, uInfo);
      savePurProcessMapping(po, userInfo, task.getName(), task.getProcessInstanceId());
      List<String> list2 = new ArrayList<String>(0);
      
      for ( String tmpUser : list ) {
          if ( !list2.contains( tmpUser )&&!tmpUser.equals( userId ) ) {
              list2.add( tmpUser );
          }
      }
      workflowService.addCandidateUsers(task.getId(), list2);
      homepageService.createProcess(
          po.getSheetno(),
          task.getProcessInstanceId(),
          task.getName(),
          po.getSheetname(),
          task.getName(),
          "/purchase/purorder/purOrderForm.do?taskId=" + task.getId() + "&processInstId="
              + task.getProcessInstanceId() + "&type=edit&sheetId=" + po.getSheetId(), uInfo, new ProcessFucExtParam());
      homepageService.Process(task.getId(), processId, task.getName(), list, uInfo, new ProcessFucExtParam() );
    }
  }

  /**
   * @description: 商务网接口数据转换
   * @author: 890166
   * @createDate: 2015-3-24
   * @param eamPo
   * @return
   * @throws Exception
   *           :
   */
  private Map<String, Object> conventEamPo2Map(EamPo eamPo) throws Exception {
    String createAccount = null;
    List<String> list = CommonUtil.returnUserIdInRole("cgzz", eamPo.getSiteid(),
        eamPo.getCreateby());
    if (null == list || list.isEmpty()) {
      list = CommonUtil.returnUserIdInRole("cgzz", eamPo.getSiteid(), null);
    }

    if (null != list && !list.isEmpty()) {
      createAccount = list.get(0);
    }

    Map<String, Object> reMap = new HashMap<String, Object>();
    // 获取商务网数据并转换成timss需要的数据格式
    PurOrder po = new PurOrder();
    po.setDhdate(eamPo.getRequireddate() == null ? new Date() : eamPo.getRequireddate());
    po.setCompanyNo(eamPo.getVendor());
    po.setCompanyRemark(eamPo.getContact());
    po.setCreateaccount(createAccount);
    po.setCreatedate(new Date());
    po.setModifyaccount(createAccount);
    po.setModifydate(eamPo.getChangedate());
    po.setSiteid(eamPo.getSiteid());
    po.setSheetname("商务网订单(" + eamPo.getDescription() + ")采购合同");
    po.setTotalPrice(new BigDecimal(eamPo.getTotalcost()));
    // 获取商务网订单号
    po.setBusinessno(eamPo.getSwwponum());

    // 将转换好的数据存放在一个map里面返回到上一级方法
    reMap.put("purOrder", po);

    // 在同一个业务中也转换采购订单中间表中需要的数据，也是通过map来返回到上一级方法
    PurPoSyncHhc ppsh = new PurPoSyncHhc();
    ppsh.setHhcPono(eamPo.getSwwponum());
    ppsh.setCreatedate(new Date());
    ppsh.setSiteid(eamPo.getSiteid());
    reMap.put("purPoSyncHhc", ppsh);

    return reMap;
  }

  /**
   * 
   * @description: 转换采购合同条款到timss中
   * @author: yuanzh
   * @createDate: 2015-9-29
   * @param eptList
   * @param map
   * @return
   * @throws Exception
   *           :
   */
  private Map<String, Object> conventEamPoTerm2Map(List<EamPoTerm> eptList, Map<String, Object> map)
      throws Exception {
    if (null != eptList && !eptList.isEmpty()) {
      PurPolicy pp = null;
      List<PurPolicy> ppList = new ArrayList<PurPolicy>();
      for (EamPoTerm ept : eptList) {
        pp = new PurPolicy();
        pp.setPolicyContent(ept.getDescription());// 获取内容
        pp.setSort(ept.getSeqnum().intValue());// 获取排序
        pp.setSiteid(ept.getSiteid());// 获取站点
        pp.setCreatedate(new Date());// 获取时间
        ppList.add(pp);
      }
      map.put("ppList", ppList);
    }
    return map;
  }

  /**
   * @description: 转换子单的数据到map
   * @author: 890166
   * @createDate: 2015-3-24
   * @param eplList
   * @param map
   * @return
   * @throws Exception
   *           :
   */
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  private Map<String, Object> conventEamPoLines2Map(List<EamPoLine> eplList, Map<String, Object> map)
      throws Exception {
    AppLog aLog = null;
    String noLocalProcess = CommonUtil.getProperties("noLocalProcess");
    String timssSite = map.get("timssSite") == null ? "" : String.valueOf(map.get("timssSite"));
    List<PurOrderItem> poiList = new ArrayList<PurOrderItem>();
    // 新建一个set集合存放不重复的商务网采购申请编码
    Set<String> prSet = new HashSet<String>();

    Map<String, Object> paramMap = null;
    BigDecimal itemnum = null;
    if (null != eplList && !eplList.isEmpty()) {
      for (EamPoLine epl : eplList) {
        if (epl.getPrnum().indexOf("TIM") > -1) {
          itemnum = new BigDecimal(epl.getOrderqty());

          // 20160120 modify by yuanzh TIM-456 商务网撤回功能 >>>>>>>begin
          paramMap = new HashMap<String, Object>();
          paramMap.put("paBNo", epl.getPrnum());
          paramMap.put("paSiteid", timssSite);

          if (epl.getItemnum().indexOf("IWI") > -1&&epl.getItemnum().indexOf("ICI") > -1) {
            paramMap.put("paItemCode",
                epl.getItemnum().substring(timssSite.length(), epl.getItemnum().indexOf("IWI")));
            paramMap.put("paWarehouseid",
                epl.getItemnum().substring(epl.getItemnum().indexOf("IWI"), epl.getItemnum().indexOf("ICI")));
            paramMap.put("paInvcateid",
                    epl.getItemnum().substring(epl.getItemnum().indexOf("ICI")));
          }else if (epl.getItemnum().indexOf("IWI") > -1&&epl.getItemnum().indexOf("ICI") < 0) {
            String itemCode = epl.getItemnum().substring(timssSite.length(), epl.getItemnum().indexOf("IWI"));
            String warehouseId =  epl.getItemnum().substring(epl.getItemnum().indexOf("IWI"));   
            paramMap.put("paItemCode",itemCode);
            paramMap.put("paWarehouseid",warehouseId);
            Map<String, Object> condition = new HashMap<String, Object>(0);
            condition.put( "siteId", timssSite );
            condition.put( "itemCode", itemCode);
            condition.put( "warehouseId", warehouseId );
            List<String> invList = purOrderDao.queryInvcateIdByCondition(condition);
            if ( 0<invList.size() ) {
                paramMap.put("paInvcateid",invList.get( 0 ));
            }
          } else {
            paramMap.put("paItemCode", epl.getItemnum().substring(timssSite.length()));
          }

          PurBuisCalaVO pbc = purPaSyncHhcService.queryPurBuisCala(paramMap);
          if (null != pbc) {
            // 先用本地的两个采购申请数量与采购合同数量进行比较
            // 若采购申请数量>采购合同数量，则表示还有物资没有生成采购合同
            // 若采购申请数量<=采购合同数量，则忽略该物资
            if (pbc.getPaItemnum().compareTo(pbc.getPoItemnum()) > 0) {
              // 若采购申请数量 - 采购合同数量 >= 商务网返回数量，则取商务网数量
              // 相反就取采购申请数量 - 采购合同数量
              if (pbc.getPaItemnum().subtract(pbc.getPoItemnum())
                  .compareTo(new BigDecimal(epl.getOrderqty())) < 0) {
                itemnum = pbc.getPaItemnum().subtract(pbc.getPoItemnum());
              }
            } else {
              continue;
            }
          }
          // 20160120 modify by yuanzh TIM-456 商务网撤回功能 >>>>>>>end

          PurOrderItem poi = new PurOrderItem();
          // 查询发送数据中间表，看是否存在发送商务网数据

          paramMap.put("hhcPano", epl.getPrnum());
          paramMap.put("siteid", timssSite);
          List<PurPaSyncHhc> ppshList = purPaSyncHhcService.queryPurPaSyncHhcByMap(paramMap);
          // 若存在则获取到timss对应的采购申请编号
          if (null != ppshList && !ppshList.isEmpty()) {
            String timPano = ppshList.get(0).getTimPano();
            paramMap.put("sheetNo", timPano);
            paramMap.put("siteId", timssSite);
            // 用采购申请编号查询，得到timss采购申请流水号
            String appSheetId = purApplyDao.querySheetIdByFlowNo(paramMap);
            poi.setApplySheetId(appSheetId);
            poi.setCost(new BigDecimal((epl.getLinecost() + epl.getTax1()) / epl.getOrderqty()));

            poi.setItemnum(itemnum);
            poi.setPrice(new BigDecimal(epl.getUnitcost()));

            if (epl.getItemnum().indexOf("IWI") > -1&&epl.getItemnum().indexOf("ICI") > -1) {
              poi.setItemid(epl.getItemnum().substring(timssSite.length(),
                  epl.getItemnum().indexOf("IWI")));
              poi.setWarehouseid(epl.getItemnum().substring(epl.getItemnum().indexOf("IWI"),epl.getItemnum().indexOf("ICI")));
              poi.setInvcateid(epl.getItemnum().substring(epl.getItemnum().indexOf("ICI")));
            }else if (epl.getItemnum().indexOf("IWI") > -1&&epl.getItemnum().indexOf("ICI") < 0) {
              //兼容没有ICI的数据
              String itemId = epl.getItemnum().substring(timssSite.length(),epl.getItemnum().indexOf("IWI"));
              String warehouseId =  epl.getItemnum().substring(epl.getItemnum().indexOf("IWI")); 
              poi.setItemid(itemId);
              poi.setWarehouseid(warehouseId);
              Map<String, Object> condition = new HashMap<String, Object>(0);
              condition.put( "siteId", timssSite );
              condition.put( "itemCode", itemId);
              condition.put( "warehouseId", warehouseId );
              List<String> invList = purOrderDao.queryInvcateIdByCondition(condition);
              if ( 0<invList.size() ) {
                  poi.setInvcateid(invList.get( 0 ));
              }
            } else {
              poi.setItemid(epl.getItemnum().substring(timssSite.length()));

              // 返回的物资信息加上对应的物资分类id
              paramMap.put("sheetId", appSheetId);
              paramMap.put("itemId", "  t.itemid = '" + poi.getItemid() + "'  and t.invcateid = '"+poi.getInvcateid()+"' ");
              List<PurApplyItemVO> paivList = purApplyDao
                  .queryPurApplyItemBySheetIdAndItems(paramMap);

              if (null == paivList || paivList.isEmpty()) {
                // 记录没有找到申请记录信息
                LOG.info(">>>>>>>>>>>>>>>>>>>>> 获取商务网数据在本地数据库中找不到对应的采购申请记录......");
                StringBuffer msgBuffer = new StringBuffer(" 商务网返回的参数信息为：");
                if (null != paramMap) {
                  for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                    msgBuffer.append(entry.getKey()).append(" : ").append(entry.getValue())
                        .append(" | ");
                  }
                }
                aLog = new AppLog();
                aLog.setCategoryId(11);
                aLog.setAttr1(timssSite);
                aLog.setDescription(msgBuffer.toString());
                aLog.setOperator("superadmin");
                aLog.setOperationTime(new Date());
                lMapper.insert(aLog);

                // 保存了失败信息后让循环继续往下执行
                continue;
              } else {
                poi.setWarehouseid(paivList.get(0).getWarehouseid());
                poi.setInvcateid( paivList.get(0).getInvcateid() );
              }
            }

            if (noLocalProcess.indexOf(timssSite) == -1) {
              poi.setStatus("0");
            } else {
              poi.setStatus("1");
            }

            poi.setTax(new BigDecimal(epl.getUnitcost()).multiply(new BigDecimal(epl.getTax1code()
                .replace("%", "")).multiply(new BigDecimal("0.01"))));
            poi.setTaxRate(new BigDecimal(epl.getTax1code().replace("%", "")));
            poiList.add(poi);
          }
          prSet.add(epl.getPrnum());
        }
      }
    }
    map.put("poiList", poiList);
    map.put("prSet", prSet);
    return map;
  }

  /**
   * @description:调用SaveOrUpdatePurOrderItem
   * @author: 890162
   * @createDate: 2015-9-17
   * @param list
   * @throws Exception
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void saveOrUpdatePurOrderItem(PurOrderItem poItem) throws Exception {
    List<PurOrderItem> poItems = purOrderDao.queryPurOrderItem(poItem);
    if (null != poItems && !poItems.isEmpty()) {
      purOrderDao.updatePurOrderItem(poItem);
    } else {
      poItem.setStatus("0");
      purOrderDao.insertPurOrderItem(poItem);
    }
  }

  @Override
  public Page<PurPolicyTemp> queryPurPolicyTemp(UserInfoScope userInfo) throws Exception {
    UserInfoScope scope = (UserInfoScope) userInfo;
    Page<PurPolicyTemp> page = scope.getPage();
    // 查询参数处理
    Map<String, String[]> params = scope.getParamMap();
    // 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
    Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap("PurPolicyTempMap",
        "purchase", "PurOrderDao");
    if (params.containsKey("search")) {
      String fuzzySearchParams = scope.getParam("search");
      LOG.info("查询合同列表的条件：" + fuzzySearchParams);
      // 调用工具类将jsonString转为HashMap
      Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap(fuzzySearchParams);
      // 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
      fuzzyParams = MapHelper.fromPropertyToColumnMap((HashMap<String, Object>) fuzzyParams,
          propertyColumnMap);
      // 自动的会封装模糊搜索条件
      page.setFuzzyParams(fuzzyParams);
    }
    // 设置排序内容
    if (params.containsKey("sort")) {
      String sortKey = scope.getParam("sort");
      // 如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
      sortKey = propertyColumnMap.get(sortKey);
      page.setSortKey(sortKey);
      page.setSortOrder(scope.getParam("order"));
    } else {
      // 设置默认的排序字段
      page.setSortKey("sort");
      page.setSortOrder("asc");
    }
    List<PurPolicyTemp> ret = purOrderDao.queryPurPolicyTempListPage(page);
    page.setResults(ret);
    return page;
  }

  @Override
  public List<PurPolicyTemp> queryPurPolicyTempList(UserInfoScope userInfo) throws Exception {
    UserInfoScope scope = (UserInfoScope) userInfo;
    PurPolicyTemp ppt = new PurPolicyTemp();
    ppt.setSiteid(scope.getSiteId());
    List<PurPolicyTemp> ret = purOrderDao.queryPurPolicyTempList(ppt);
    return ret;
  }

  @Override
  public List<PurPolicy> queryPurPolicyList(PurPolicy purPolicy) throws Exception {
    List<PurPolicy> ret = purOrderDao.queryPurPolicyList(purPolicy);
    return ret;
  }

  @Override
  public PurPolicyTemp queryPurPolicyTempById(String id) throws Exception {
    PurPolicyTemp result = null;
    List<PurPolicyTemp> purPolicyTemps = purOrderDao.queryPurPolicyTempById(id);
    if (0 < purPolicyTemps.size()) {
      result = purPolicyTemps.get(0);
    }
    return result;
  }

  @Override
  public Map<String, Object> saveOrUpdatePurPolicyTemp(PurPolicyTemp ppt) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>(0);
    UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();
    int resultCount = 0;
    if (StringUtils.isNotEmpty(ppt.getPolicyId())) {
      ppt.setModifydate(new Date());
      ppt.setModifyuser(userInfo.getUserId());
      resultCount = purOrderDao.updatePurPolicyTemp(ppt);
    } else {
      ppt.setPolicyId(null);
      ppt.setCreatedate(new Date());
      ppt.setCreateuser(userInfo.getUserId());
      ppt.setSiteid(userInfo.getSiteId());
      resultCount = purOrderDao.insertPurPolicyTemp(ppt);
    }
    if (0 < resultCount) {
      result.put("success", true);
    } else {
      result.put("success", false);
    }
    return result;
  }

  @Override
  public Map<String, Object> deletePurPolicyTemp(UserInfoScope userInfo) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>(0);
    String id = userInfo.getParam("policyId");
    int resultCount = 0;
    PurPolicyTemp ppt = new PurPolicyTemp();
    ppt.setPolicyId(id);
    ppt.setSiteid(userInfo.getSiteId());
    resultCount = purOrderDao.deletePurPolicyTemp(ppt);
    if (0 < resultCount) {
      result.put("success", true);
    } else {
      result.put("success", false);
    }
    return result;
  }

  @Override
  public List<PurOrderItemVO> queryPurOrderItemListExceAsList(UserInfoScope userInfo, String sheetId)
      throws Exception {
    String siteId = userInfo.getSiteId();
    List<PurOrderItemVO> ret = purOrderDao.queryPurOrderItemListExceAsList(sheetId, siteId);
    return ret;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int stopPurOrder(String sheetId) throws Exception {
    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    int result = 0;
    // 1.更新合同
    List<PurOrder> pos = purOrderDao.queryPurOrderBySheetId(sheetId);
    if (null != pos && !pos.isEmpty()) {
      PurOrder po = pos.get(0);
      po.setStatus("10");// 10作为终止合同的状态值
      result = purOrderDao.updatePurOrderInfo(po);
    }
    LOG.info("--------------------------------------------");
    LOG.info("- PurOrderServiceImpl 中的 stopPurOrder 方法已更新合同 - ");
    // 2.查询未入账的发票，生成站内消息
    List<PurInvoiceBean> purInvoiceBeans = purPubInterface
        .queryInvoiceRelationByContractId(sheetId);
    List<String> creatorList = new ArrayList<String>(0);
    HomepageWorkTask task = null;
    for (PurInvoiceBean pb : purInvoiceBeans) {
      if ("未报账".equals(pb.getStatus())) {
        task = new HomepageWorkTask();
        String flowNo = pb.getInvoiceNo();
        String flowCode = pb.getId();
        task.setFlow(flowNo);
        task.setProcessInstId(userInfoScope.getSiteId() + "_purInvoiceno_" + flowCode);
        task.setName("发票 [" + flowNo + "] 尚未报账，但合同已终止，请及时报账。");
        task.setTypeName("报账提醒");
        task.setStatusName("报账提醒");
        // 报账的地址
        task.setUrl("purchase/purInvoice/updateInvoiceToPage.do?id=" + pb.getId());
        creatorList.add(pb.getCreateuser());
        homepageService.createNoticeWithOutWorkflow(task, creatorList, userInfoScope, "WARN");
      }
    }
    LOG.info("- PurOrderServiceImpl 中的 stopPurOrder 方法查询未入账的发票，生成站内消息 - ");
    // 3.删除未完结的物资领用的待办
    List<String> sheetNos = invMatTranDetailService.queryMatTranSheetNoByOutterId(sheetId);
    for (String sheetNo : sheetNos) {
      WorktaskBean wtb = homepageService.getOneTaskByFlowNo(sheetNo, userInfoScope);
      if (null != wtb) {
        invMatTranDetailService.deleteNoMainDetailDataBydbId(wtb.getExtCode());
        homepageService.Delete(wtb.getExtCode(), userInfoScope);
      }
    }
    LOG.info("- PurOrderServiceImpl 中的 stopPurOrder 方法删除未完结的物资领用的待办 - ");
    LOG.info("--------------------------------------------");
    return result;
  }

  @Override
  public int updatePurOrderTransactor(String sheetId, String transactor) throws Exception {
    PurOrder purOrder = new PurOrder();
    purOrder.setSheetId(sheetId);
    purOrder.setTransactor(transactor);
    int result = purOrderDao.updatePurOrderTransactor(purOrder);
    LOG.info("- 更新采购合同待办人：  " + result + " -");
    return result;
  }

  /**
   * @description:生成采购合同的合同条款[refactor]
   * @author: 890162
   * @createDate: 2015-10-23
   * @param UserInfoScope
   * @param sheetId
   * @return
   * @throws Exception
   */
  @Override
  public List<PurPolicy> generatePurPolicyListBySheetId(UserInfoScope userInfo, String sheetId)
      throws Exception {
    List<PurPolicy> policyList = new ArrayList<PurPolicy>(0);
    if (StringUtils.isEmpty(sheetId)) {
      // 新建合同时，返回标准合同条款
      List<PurPolicyTemp> list = queryPurPolicyTempList(userInfo);
      for (PurPolicyTemp purPolicyTemp : list) {
        PurPolicy pp = new PurPolicy();
        pp.setSort(purPolicyTemp.getSort());
        pp.setPolicyContent(purPolicyTemp.getPolicyContent());
        policyList.add(pp);
      }
    } else {
      // 编辑合同时，返回采购合同的条款副本
      PurPolicy pp = new PurPolicy();
      pp.setSiteid(userInfo.getSiteId());
      pp.setSheetId(sheetId);
      policyList = queryPurPolicyList(pp);
    }
    return policyList;
  }

  /**
   * @description:生成采购合同表单数据[refactor]
   * @author: 890162
   * @createDate: 2015-10-23
   * @param UserInfoScope
   * @param type
   * @param sheetId
   * @return
   * @throws Exception
   */
  @Override
  public Map<String, Object> initPurOrderForm(String type, String sheetId) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>(0);
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    String siteId = userInfo.getSiteId();
    String curUserId = userInfo.getUserId();
    String defKey = "purchase_[@@@]_purorder".replace("[@@@]", siteId.toLowerCase());
    map.put("defKey", defKey);
    map.put("siteId", siteId);
    map.put("curUserId", curUserId);
    String sheetNo = queryFlowNoBySheetId(sheetId, siteId);
    map.put("sheetNo", sheetNo);
    String status = "";
    if (!"new".equals(type)) {
      //还是需要从status字段判断采购单的作废状态
      status = purOrderDao.queryStatusBySheetId( sheetId ).get( 0 );
      PurProcessMapping ppm = new PurProcessMapping();
      ppm.setMasterkey(sheetId);
      ppm.setSiteid(siteId);
      ppm.setModeltype("purorder");
      String processInst = purProcessMappingService.queryProcessIdByParams(ppm);
      map.put("processInstId", processInst);

      // modify by yuanzh 20160104 将查询流程环节的动作放在有sheetNo的地方
      WorktaskBean wtBean = homepageService.getOneTaskByFlowNo(sheetNo, userInfo);
      if (null != wtBean) {
        map.put("classType", wtBean.getClasstype().toString());
      }

      if (!"".equals(processInst) && null != processInst) {
        List<Task> activities = workflowService.getActiveTasks(processInst);
        if (null != activities && !activities.isEmpty()) {
          Task task = activities.get(0);
          List<String> userIdList = workflowService.getCandidateUsers(task.getId());
          for (String userId : userIdList) {
            if (userId.equals(curUserId)) {
              map.put("isEdit", "editable");
              break;
            }
          }
          Map<String, String> processAttr = workflowService.getElementInfo(task.getId());
          map.put("oper", processAttr.get("modifiable"));
          map.put("taskId", task.getId());
          map.put("process", task.getTaskDefinitionKey());
          map.put("processName", task.getName());
        }
      } else {
        map.put("isEdit", "editable");
      }
      List<Map<String, Object>> fileMap = purAttachMappingService.queryPurAttach(sheetNo);
      JSONArray jsonArray = JSONArray.fromObject(fileMap);
      map.put("uploadFiles", jsonArray);
    } else {
      map.put("isEdit", "editable");
    }
    map.put( "status", status );
    return map;
  }

  /**
   * @description:已执行采购物资列表
   * @author: user
   * @createDate: 2016-1-22
   * @param userInfo
   * @param sheetId
   * @return:
   */
  @Override
  public Page<PurApplyOrderItemVO> queryPurApplyOrderItemList(UserInfoScope userInfo, String sheetId) {
    UserInfoScope scope = userInfo;
    Page<PurApplyOrderItemVO> page = scope.getPage();

    String pageSize = CommonUtil.getProperties("pageSize");
    page.setPageSize(Integer.valueOf(pageSize));

    page.setParameter("sheetId", sheetId);
    page.setParameter("siteId", userInfo.getSiteId());
    List<PurApplyOrderItemVO> ret = purOrderDao.queryPurApplyOrderItemList(page);
    page.setResults(ret);
    return page;
  }

  /**
   * @description:标准合同条款操作
   * @author: yuanzh
   * @createDate: 2016-2-3
   * @param operType
   * @return:
   */
  @Override
  public Map<String, Object> purPolicyTempOperation(String operType) {
    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();

    Map<String, Object> result = new HashMap<String, Object>();
    Map<String, Object> map = new HashMap<String, Object>();

    try {

      if ("Delete".equals(operType)) {
        result = deletePurPolicyTemp(userInfoScope);
      } else {
        String policyTempString = userInfoScope.getParam("policyTemp");
        LOG.info("合同标准条款信息为：" + policyTempString);
        PurPolicyTemp ppt = JsonHelper.fromJsonStringToBean(policyTempString, PurPolicyTemp.class);
        result = saveOrUpdatePurPolicyTemp(ppt);
      }

    } catch (Exception e) {
      LOG.info("--------------------------------------------");
      LOG.info("- PurOrderServiceImpl 中的 purPolicyTempOperation 方法抛出异常：", e);
      LOG.info("--------------------------------------------");
      result.put("success", false);
    }

    map.put("flag", "success");
    map.put("msg", "");
    map.put("data", result);

    return map;
  }

    @Override
    public Page<PurOrderItemVO> queryItemDoingStatus(UserInfoScope userInfoScope,String sheetNo) {
        UserInfoScope scope = userInfoScope;
        Page<PurOrderItemVO> page = scope.getPage();
        page.setParameter("siteId", userInfoScope.getSiteId());
        page.setParameter("sheetNo", sheetNo);
        
        List<PurOrderItemVO> ret = purApplyDao.queryOrderItemDoingStatus(page);
        page.setResults(ret);
        return page;
    }

    /**
     * 查询本年度各专业采购金额占比的指标卡片
     */
    public List<Map<String, BigDecimal>> queryMajorPurchase(UserInfoScope userInfo){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("siteId", userInfo.getSiteId());
    	return purOrderDao.queryMajorPurchase(map);
    }

    @Override
    public List<PurOrderItemVO> queryPurApplyOrderItemList(String sheetId,String siteId) {
        Page<PurOrderItemVO> page = new Page<PurOrderItemVO>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sheetNo", sheetId);
        params.put("siteId", siteId);
        page.setParams(params);
        List<PurOrderItemVO> list = purApplyDao.queryOrderItemDoingStatus(page);
	return list;
    }

    @Override
    public boolean isSpNoExisted(String spNo, String sheetId) {
        spNo = "YD/ZSD-WS-"+spNo.replaceAll( "YD/ZSD-WS-", "" ) ;
        int num=purOrderDao.selectBySpNoAndSiteid(spNo, itcMvcService.getUserInfoScopeDatas().getSiteId());
        if(num==0){
                return false;
        }
        if(StringUtils.isEmpty( sheetId )){
                return true;
        }
        List<PurOrder> purOrders = purOrderDao.queryPurOrderBySheetId( sheetId ); 
        if(1==purOrders.size() && spNo.equals(purOrders.get( 0 ).getSpNo())){
                return false;
        }
        return true;
    }

    /**
     * @description:当前站点库存报账金额统计（分已报账和未报账）
     * @author: 890151
     * @createDate: 2016-10-26
     * @param userInfoScope
     * @param wareHouseId 仓库ID，不限传入null
     * @return:
     */
	@Override
	public Map<String, BigDecimal> reimbursedMoneyStatistic(UserInfoScope userInfo, String wareHouseId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("siteId", userInfo.getSiteId());
        map.put("priceType", "noTaxPrice");
        if(wareHouseId!=null){
        	map.put("wareHouseId", wareHouseId);
        }
        //查询审批完成的合同总金额
    	BigDecimal purOrderTotalMoney = purOrderDao.queryPurOrderTotalMoney(map);
    	//审批完成的合同相关已报账金额
    	BigDecimal purOrderReimbursedTotalMoney = purOrderDao.queryPurOrderReimbursedTotalMoney(map);
    	//查询审批完成的合同未报账金额
    	BigDecimal purOrderUnReimbursedTotalMoney = purOrderTotalMoney.subtract(purOrderReimbursedTotalMoney);
    	//根据站点和仓库查询库存总金额
		BigDecimal stockMoneyTotal = invMatTranDetailService.queryStockMoneyTotal(userInfo.getSiteId(), null);
    	//已报账金额
        BigDecimal reimbursedMoneyTotal = stockMoneyTotal.subtract(purOrderUnReimbursedTotalMoney);

    	//构造返回值
    	Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
    	BigDecimal divisor = new BigDecimal(10000);
    	result.put("reimbursedMoneyTotal", reimbursedMoneyTotal.divide(divisor, 4, BigDecimal.ROUND_HALF_UP));
    	result.put("stockMoneyTotal", stockMoneyTotal.divide(divisor, 4, BigDecimal.ROUND_HALF_UP));
    	result.put("unReimbursedMoneyTotal", purOrderUnReimbursedTotalMoney.divide(divisor, 4, BigDecimal.ROUND_HALF_UP));
		return result;
	}
    
}
