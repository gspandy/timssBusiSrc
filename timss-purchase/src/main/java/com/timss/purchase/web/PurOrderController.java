package com.timss.purchase.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.timss.purchase.bean.PurInvoiceBean;
import com.timss.purchase.bean.PurOrder;
import com.timss.purchase.bean.PurOrderItem;
import com.timss.purchase.bean.PurOrderPurchaserBean;
import com.timss.purchase.bean.PurPolicy;
import com.timss.purchase.bean.PurPolicyTemp;
import com.timss.purchase.service.PurOrderService;
import com.timss.purchase.service.PurPubInterface;
import com.timss.purchase.service.PurPurchaserService;
import com.timss.purchase.utils.CommonUtil;
import com.timss.purchase.utils.WorkflowPurUtil;
import com.timss.purchase.vo.PurApplyItemVO;
import com.timss.purchase.vo.PurApplyOrderItemVO;
import com.timss.purchase.vo.PurApplyStockItemVO;
import com.timss.purchase.vo.PurOrderItemVO;
import com.timss.purchase.vo.PurOrderVO;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurOrderController.java
 * @author: 890166
 * @createDate: 2014-6-30
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "purchase/purorder")
public class PurOrderController {

  /**
   * log4j输出
   */
  private static final Logger LOG = Logger.getLogger(PurOrderController.class);

  @Autowired
  private PurOrderService purOrderService;

  @Autowired
  private ItcMvcService itcMvcService;

  @Autowired
  private WorkflowService workflowService;

  @Autowired
  private WorkflowPurUtil workflowPurUtil;

  @Autowired
  private PurPubInterface purPubInterface;

  @Autowired
  private PurPurchaserService purPurchaserService;

  /**
   * @description:列表页面跳转
   * @author: 890166
   * @createDate: 2014-8-16
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/purOrderList", method = RequestMethod.GET)
  public String purOrderList() {
    return "/purorder/purOrderList.jsp";
  }

  
  @RequestMapping(value = "/purOrderDoingStatus", method = RequestMethod.GET)
  public String purOrderDoingStatusList() {
    return "/purorder/purOrderDoingStatusList.jsp";
  }
  
  @RequestMapping(value = "/queryPurApplyImplemetationStatus", method = RequestMethod.GET)
  public String queryPurApplyImplemetationStatus() {
    return "/purorder/purApplyImplemetationStatusList.jsp";
  }
  
  @RequestMapping(value = "/orderDoingStatusList", method = RequestMethod.POST)
  public Page<PurOrderItemVO>  orderDoingStatusList(String search) throws Exception {
      UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
      String sheetNo = userInfoScope.getParam("sheetNo");
      Page<PurOrderItemVO> result = purOrderService.queryItemDoingStatus(userInfoScope,sheetNo);
      return result;
  }
  /**
   * @description:表单页面跳转
   * @author: 890166
   * @createDate: 2014-8-16
   * @param type
   * @param sheetId
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/purOrderForm", method = RequestMethod.GET)
  @ReturnEnumsBind("ITEMORDER_TYPE,PUR_ORDER_TAXRATE,PUR_ORDER_ISGM,PUR_PAYTYPE,PUR_PAYSTATUS,ACPT_STATUS,ACPT_CNLUS,PURCHORDER_FLOWSTATUS,PUR_BID_TYPE")
  public ModelAndView purOrderForm(@RequestParam String type, @RequestParam String sheetId)
      throws Exception {
    ModelAndView mav = new ModelAndView("/purorder/purOrderForm.jsp");

    if ("".equals(sheetId)) {
      UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
      String sheetNo = userInfo.getParam("sheetNo");
      if (null != sheetNo) {
        sheetId = purOrderService.querySheetIdByFlowNo(sheetNo, userInfo.getSiteId());
      }
    }
    mav.addObject("sheetId", sheetId);

    // 生成采购合同表单页面refactor -- start
    Map<String, Object> result = purOrderService.initPurOrderForm(type, sheetId);
    Set<String> keySet = result.keySet();
    for (String key : keySet) {
      mav.addObject(key, result.get(key));
    }

    // 生成采购合同表单页面refactor -- end
    return mav;
  }

  /**
   * @description:添加申请采购物资跳转
   * @author: 890166
   * @createDate: 2014-8-16
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/purOrderItemList", method = RequestMethod.GET)
  public ModelAndView purOrderItemList() {
    ModelAndView result = new ModelAndView("/purorder/purOrderItemList.jsp");
    try {
        String includeitems = String.valueOf( itcMvcService.getUserInfoScopeDatas().getParam( "includeitems" ));
        result.addObject( "includeitems", includeitems );
    } catch (Exception e) {
        LOG.warn( "传递参数异常", e  );
    }
    return result;
  }

  /**
   * @description:添加供应商跳转
   * @author: 890166
   * @createDate: 2014-8-16
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/purOrderCompanyList", method = RequestMethod.GET)
  public String purOrderCompanyList() {
    return "/purorder/purOrderCompanyList.jsp";
  }

  /**
   * @description:添加标准条款跳转
   * @author: 890162
   * @createDate: 2015-9-22
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/purOrderPolicyTempList", method = RequestMethod.GET)
  public String purOrderPolicyTempList() {
    return "/purorder/purOrderPolicyTempList.jsp";
  }

  /**
   * @description:查询采购单列表
   * @author: 890166
   * @createDate: 2014-6-26
   * @param search
   *          页面传来查询参数
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/queryPurOrder", method = RequestMethod.POST)
  public Page<PurOrderVO> queryPurOrder(String search) throws Exception {
    PurOrderVO purOrderVO = null;
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    if (StringUtils.isNotBlank(search)) {
      purOrderVO = JsonHelper.fromJsonStringToBean(search, PurOrderVO.class);
    }
    return purOrderService.queryPurOrder(userInfo, purOrderVO);
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
  @RequestMapping(value = "/queryPurOrderDetail", method = RequestMethod.POST)
  public PurOrderVO queryPurOrderDetail(String sheetId) throws Exception {
    PurOrderVO purOrderVO = null;
    List<PurOrderVO> list = purOrderService.queryPurOrderInfoBySheetId(sheetId);
    if (null != list && !list.isEmpty()) {
      purOrderVO = list.get(0);
    }
    // 需要添加买方信息
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    String siteId = userInfo.getSiteId();
    String needBuySite = CommonUtil.getProperties("needBuySite");
    if (needBuySite.indexOf(siteId) > -1) {
      PurOrderPurchaserBean pob = purPurchaserService.queryPurOrderPurchaserBySheetId(sheetId,
          siteId);
      if (null != pob) {
        purOrderVO.setPurchaserName(pob.getPurchaserName());
      }
    }
    return purOrderVO;
  }

  /**
   * @description:弹出框内的申请物资信息列表
   * @author: 890166
   * @createDate: 2014-6-26
   * @param search
   *          页面传过来的查询条件
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/queryItemList", method = RequestMethod.POST)
  public Page<PurOrderItemVO> queryItemList(String search) throws Exception {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    String includeitems = StringUtils.trimToEmpty( userInfo.getParam( "includeitems" ) );
    String[] includeItemsArray = new String[]{};
    List<PurOrderItemVO> includeItemsList = new ArrayList<PurOrderItemVO>(0);
    if ( StringUtils.isNotEmpty( includeitems ) ) {
        includeItemsArray = JsonHelper.fromJsonStringToBean( includeitems, String[].class );
    }
    for ( String includeItem : includeItemsArray ) {
        PurOrderItemVO tmpItemVO = new PurOrderItemVO();
        tmpItemVO.setItemid( includeItem.split( "_" )[0] );
        tmpItemVO.setInvcateid( includeItem.split( "_" )[1] );
        tmpItemVO.setSheetno( ( includeItem.split( "_" )[2] ) );
        includeItemsList.add( tmpItemVO );
    }
    PurOrderItemVO purOrderItemVO = null;
    if (StringUtils.isNotBlank(search)) {
      purOrderItemVO = JsonHelper.fromJsonStringToBean(search, PurOrderItemVO.class);
    }
    return purOrderService.queryItemByEntity(userInfo, purOrderItemVO,includeItemsList);
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
  @RequestMapping(value = "/queryPurOrderItemList", method = RequestMethod.POST)
  public Page<PurOrderItemVO> queryPurOrderItemList(String sheetId, String queryMode)
      throws Exception {
    Page<PurOrderItemVO> page = new Page<PurOrderItemVO>();
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

    if ("0".equals(queryMode)) { // queryMode：0流程中，1创建、草稿、结束
      page = purOrderService.queryPurOrderItemListExce(userInfo, sheetId);
    } else {
      page = purOrderService.queryPurOrderItemList(userInfo, sheetId);
    }

    return page;
  }

  /**
   * @description:弹出窗口添加物资返回数据
   * @author: 890166
   * @createDate: 2014-6-26
   * @param itemids
   *          弹出框获取到的itemid
   * @param ogriIds
   *          列表中本来存在的itemid
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/queryPurOrderItems", method = RequestMethod.POST)
  public Map<String, Object> queryPurOrderItems(String itemids, String ogriIds) throws Exception {

    Map<String, Object> map = new HashMap<String, Object>();
    // 去掉重复选项
    // 将弹出框中选择的itemid获取并转换成List
    String[] itemidArr = itemids.split(",");
    List<String> itList = Arrays.asList(itemidArr);

    // 将页面中已有的itemid获取并转换成List
    String[] ogriidArr = ogriIds.split(",");
    List<String> ogList = Arrays.asList(ogriidArr);

    // 将全部的itemid放在一个allItems中
    // （因为Arrays.asList是一个虚拟的List，没有add、remove等操作List的方法的所以要将里面的数据放到另外的一个List中）
    List<String> allItems = new ArrayList<String>();
    allItems.addAll(itList);
    allItems.addAll(ogList);

    // 使用工具类，将list中的数据进行比较，将不同的部分获取出来重新拼接组织成sql条件
    Map<String, List<String>> maps = CommonUtil.getListDifferAndSimilar(ogList, allItems);
    // 弹出窗口添加物资返回数据refactor---start
    List<String> diffList = maps.get("diff");
    String json = getDiffListToJsonStr(diffList);
    if (StringUtils.isNotEmpty(json)) {
      map.put("result", json);
    }
    // 弹出窗口添加物资返回数据refactor---end
    return map;
  }

  /**
   * @description:弹出窗口添加物资返回数据
   * @author: 890162 refactor
   * @createDate: 2015-10-23
   * @param diffList
   * @return diffList格式为 sheetId_itemId
   * @throws Exception
   */
  private String getDiffListToJsonStr(List<String> diffList) throws Exception {
    String json = "";
    if (null != diffList && !diffList.isEmpty()) {
      StringBuilder jsonStr = new StringBuilder("");
      List<PurOrderItemVO> poivList = purOrderService.queryItemByList(diffList);
      for (PurOrderItemVO poiv : poivList) {
        double itemNum = Double.valueOf(poiv.getItemnum());
        if (itemNum == 0) {
          continue;
        }
        poiv.setApplySheetId(poiv.getSheetId());
        poiv.setSheetId("");

        if (null == poiv.getTax()) {
          poiv.setTax("0.00");
        }
        double averprice = Double
            .valueOf(poiv.getAverprice() == null ? "0.0" : poiv.getAverprice());
        double cost = averprice;
        poiv.setCost(String.valueOf(cost));
        poiv.setTempCost(new BigDecimal(cost));
        jsonStr.append(JsonHelper.fromBeanToJsonString(poiv)).append(";");
      }
      json = jsonStr.toString();
      if (json.length() > 0) {
        json = json.substring(0, json.length() - 1);
      }
    }
    return json;
  }

  /**
   * @description:提交操作
   * @author: 890166
   * @createDate: 2014-7-1
   * @param formData
   * @param listData
   * @param type
   * @param sheetId
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/commitApply", method = RequestMethod.POST)
  @ValidFormToken
  public Map<String, Object> commitApply(String formData, String listData, String type,
      String sheetId, String taskId, String delFlag) throws Exception {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    Map<String, Object> result = new HashMap<String, Object>();
    boolean flag = true;

    // 添加提交参数
    Map<String, Object> paramMap = new HashMap<String, Object>(); // 参数map
    paramMap.put("sheetId", sheetId);
    paramMap.put("taskId", taskId);
    paramMap.put("type", type);

    paramMap.put("delFlag", delFlag);

    // 将表单的数据转换成实体类
    PurOrderVO pov = JsonHelper.fromJsonStringToBean(formData, PurOrderVO.class);
    String isGm = pov.getPurOrderIsGm();
    paramMap.put("isGm", isGm);

    PurOrder po = JsonHelper.fromJsonStringToBean(formData, PurOrder.class);
    po.setSiteid(userInfo.getSiteId());
    // 将json列表数据转换成List数据
    List<PurOrderItem> poiList = CommonUtil.conventJsonToPurOrderItemList(listData);
    List<PurOrderItem> poiListTmp = new ArrayList<PurOrderItem>(0); 
    for ( PurOrderItem purOrderItem : poiList ) {
        purOrderItem.setSiteid( userInfo.getSiteId() );
        poiListTmp.add( purOrderItem );
    }
    poiList = poiListTmp;
    // 如果在提交操作时，物资类表中没有任何数据，则不让提交
    flag = commitApply(userInfo, po, poiList, paramMap);
    result.put("sheetId", paramMap.get("sheetId"));
    result.put("sheetNo", paramMap.get("sheetNo"));
    result.put("taskId", paramMap.get("taskId"));
    result.put("processInstanceId", paramMap.get("processInstanceId"));

    if (flag) {
      result.put("result", "success");
    } else {
      result.put("result", "false");
    }
    return result;
  }

  /**
   * @description: 提交方法 参数待定（先做保存操作，之后再加入流程信息）
   * @author: 890166
   * @createDate: 2014-6-24
   * @return
   * @throws Exception
   */
  private boolean commitApply(UserInfoScope userInfo, PurOrder po, List<PurOrderItem> poiList,
      Map<String, Object> paramMap) throws Exception {
    boolean flag = true;
    String uploadIds = userInfo.getParam("uploadIds");
    String isFormal = userInfo.getParam("isFormal");
    if (StringUtils.isNotEmpty(uploadIds)) {
      uploadIds = uploadIds.replace("\"", "");
      paramMap.put("uploadIds", uploadIds);
    }
    if (StringUtils.isNotEmpty(isFormal) ) {
      paramMap.put("isFormal", isFormal);  
    }

    String taskId = paramMap.get("taskId") == null ? "" : String.valueOf(paramMap.get("taskId")); // 任务id
    // 记录初始时，taskID是否为空，此条件决定是否创建待办
    paramMap.put("isTaskIdEmpty", "".equals(taskId));

    workflowPurUtil.startupProcess(paramMap, userInfo, "PurOrder");
    Task task = (Task) paramMap.get("task");

    // 当前环节是否首环节
    if ("".equals(taskId)) {
      paramMap.put("status", "4"); // 当为4的时候就是草稿 对应枚举PURCHORDER_FLOWSTATUS
    }

    if (null != task) {
      paramMap.put("taskId", task.getId());
      paramMap.put("taskName", task.getName());
      // 环节到达物资采购
      if (task.getTaskDefinitionKey().indexOf(CommonUtil.getProperties("procurement")) > -1) {
        paramMap.put("status", "1");
      }
    }

    // 判断当前是否特定环节，若是特定环节可以修改的就先保存
    flag = purOrderService.saveOrUpdatePurOrder(userInfo, po, poiList, paramMap);

    return flag;
  }

  /**
   * @description:结束流程
   * @author: 890166
   * @createDate: 2014-10-25
   * @param taskId
   * @param processId
   * @param message
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/stopProcess", method = RequestMethod.POST)
  public Map<String, Object> stopProcess(@RequestParam("taskId") String taskId,
      @RequestParam("message") String message, @RequestParam("sheetId") String sheetId) {
    Map<String, Object> result = new HashMap<String, Object>();
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    String curUser = userInfo.getUserId();
    try {
       PurOrder purOrder = purOrderService.queryPurOrderBySheetId( sheetId );
       // 如果不是草稿的话，就设为5 作废 并且更新待办人为空
       if (! "4".equals( purOrder.getStatus() ) ) {
           PurOrder po = new PurOrder();
           po.setSheetId(sheetId);
           po.setStatus("5");
           po.setTransactor("");
           purOrderService.updatePurOrderInfo(po);
       }else{
           PurOrder po = new PurOrder();
           po.setSheetId(sheetId);
           po.setStatus("-1");
           purOrderService.updatePurOrderInfo(po);
       }
      workflowService.stopProcess(taskId, curUser, curUser, message);
      result.put("result", "success");
    } catch (Exception e) {
      LOG.info("--------------------------------------------");
      LOG.info("- PurOrderController 中的 stopProcess 方法抛出异常： ", e);
      LOG.info("--------------------------------------------");
      result.put("result", "error");
    }
    return result;
  }

  /**
   * 查询合同标准条款
   * 
   * @Title: purPolicyTempListData
   * @Description:
   * @return: Page<PurPolicyTemp> 合同标准条款信息及分页信息
   * @throws
   */
  @RequestMapping(value = "/purPolicyTempListData", method = RequestMethod.POST)
  public Page<PurPolicyTemp> purPolicyTempListData() throws Exception {
    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    Page<PurPolicyTemp> page = userInfoScope.getPage();
    page = purOrderService.queryPurPolicyTemp(userInfoScope);
    return page;
  }

  /**
   * @description:表单页面跳转
   * @author: 890162
   * @createDate: 2015-9-23
   * @param type
   * @param id
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/purOrderPolicyTempForm", method = RequestMethod.GET)
  public ModelAndView purOrderPolicyTempForm(@RequestParam String type,
      @RequestParam String policyId) throws Exception {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    ModelAndView mav = new ModelAndView("/purorder/purOrderPolicyTempForm.jsp");
    mav.addObject("siteid", userInfo.getSiteId());
    mav.addObject("curUserId", userInfo.getUserId());
    mav.addObject("type", type);

    if (!"new".equals(type)) {
      PurPolicyTemp ppt = purOrderService.queryPurPolicyTempById(policyId);
      mav.addObject("ppt", JsonHelper.fromBeanToJsonString(ppt));
    }

    return mav;
  }

  /**
   * @description:保存页面传过来的合同标准条款
   * @author: 890162
   * @createDate: 2015-9-23
   * @param
   * @return:
   */
  @RequestMapping(value = "savePurOrderPolicyTemp", method = RequestMethod.POST)
  @ValidFormToken
  public ModelAndViewAjax savePurOrderPolicyTemp() throws Exception {
    Map<String, Object> map = purOrderService.purPolicyTempOperation("Save");
    return ViewUtil.Json(map);
  }

  /**
   * @description:删除
   * @author: 890162
   * @createDate: 2015-9-23
   * @param
   * @return:
   */
  @RequestMapping(value = "purOrderPolicyTempDelete", method = RequestMethod.POST)
  @ValidFormToken
  public ModelAndViewAjax purOrderPolicyTempDelete() throws Exception {
    Map<String, Object> map = purOrderService.purPolicyTempOperation("Delete");
    return ViewUtil.Json(map);
  }

  /**
   * @description:查询合同条款
   * @author: 890162
   * @createDate: 2015-9-24
   * @param sheetId
   *          审批id
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/queryPurOrderPolicyAndReceipt", method = RequestMethod.POST)
  public ModelAndViewAjax queryPurOrderPolicyAndReceipt(String sheetId) throws Exception {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    Map<String, Object> map = new HashMap<String, Object>(0);
    // 获取合同条款
    List<PurPolicy> policyList = purOrderService.generatePurPolicyListBySheetId(userInfo, sheetId);
    map.put("contractDetailList", policyList);
    // 获取发票列表
    List<PurInvoiceBean> invoiceBeanList = purPubInterface
        .queryInvoiceRelationByContractId(sheetId);
    map.put("contractReceiptList", invoiceBeanList);
    return ViewUtil.Json(map);
  }

  @RequestMapping("/itemDetail")
  public ModelAndView itemDetail(String sheetId, String itemid) throws Exception {
    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    Map<String, Object> result = new HashMap<String, Object>(0);
    PurApplyItemVO purApplyItemVOCon = new PurApplyItemVO();
    purApplyItemVOCon.setItemid(itemid);
    purApplyItemVOCon.setCommitcommecnetwk("0");
    List<PurOrderItemVO> purOrderItems = purOrderService.queryPurOrderItemListExceAsList(
        userInfoScope, sheetId);
    if (null != purOrderItems && !purOrderItems.isEmpty()) {
      PurOrderItemVO purOrderItem = purOrderItems.get(0);
      result.put("result", JsonHelper.fromBeanToJsonString(purOrderItem));
    }
    return new ModelAndView("purorder/itemDtl.jsp", result);
  }

  /**
   * @description: 终止合同
   * @author: 890162
   * @createDate: 2015-9-29
   * @param
   * @return:
   */
  @RequestMapping(value = "stopOrder", method = RequestMethod.POST)
  @ValidFormToken
  public ModelAndViewAjax stopOrder() throws Exception {
    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    String sheetId = userInfoScope.getParam("sheetId");
    int result = 0;
    Map<String, Object> map = new HashMap<String, Object>(0);
    try {
      result = purOrderService.stopPurOrder(sheetId);
    } catch (Exception e) {
      LOG.info("--------------------------------------------");
      LOG.info("- PurOrderController 中的 stopOrder 方法抛出异常：", e);
      LOG.info("--------------------------------------------");
      map.put("flag", "failed");
    }
    map.put("flag", "success");
    map.put("msg", "");
    map.put("data", result);
    return ViewUtil.Json(map);
  }

  /**
   * @description: 已执行采购物资列表
   * @author: user
   * @createDate: 2016-1-22
   * @param sheetId
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/queryPurApplyOrderItemList", method = RequestMethod.POST)
  public Page<PurApplyOrderItemVO> queryPurApplyOrderItemList(String sheetId) throws Exception {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    return purOrderService.queryPurApplyOrderItemList(userInfo, sheetId);
  }

  /**
   * @description:已入库物资列表
   * @author: user
   * @createDate: 2016-1-22
   * @param sheetId
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/queryPurApplyStockItemList", method = RequestMethod.POST)
  public Page<PurApplyStockItemVO> queryPurApplyStockItemList(String sheetId) throws Exception {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    return purPubInterface.queryPurApplyStockItemList(userInfo, sheetId);
  }
  
  /**
   * @description:执行情况列表
   * @author: 890162
   * @createDate: 2016-7-1
   * @param sheetId
   * @return Page<PurApplyStockItemVO>
   * @throws Exception
   */
  @RequestMapping(value = "/queryPurApplyImplemetationStatusList", method = RequestMethod.POST)
  public Page<PurApplyStockItemVO> queryPurApplyImplemetationStatusList(String sheetId) throws Exception {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    return purPubInterface.queryPurApplyImplemetationStatusList(userInfo, sheetId);
  }
  
  /**
   * 
   * @Title:isSpNoExisted
   * @Description:判断自定义编码是否存在
   * @param:contractCode
   * @return
   */
  @RequestMapping("isSpNoExisted")
  public ModelAndViewAjax isSpNoExisted(String spNo,String sheetId){
      boolean isExist=purOrderService.isSpNoExisted(spNo,sheetId);
      Map<String, String> result= new HashMap<String, String>(0);
      if(!isExist){
          result.put( "isExisted", "false" );
      }else {
          result.put( "isExisted", "true" );
      }
      return ViewUtil.Json(result);
  }
}
