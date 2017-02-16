package com.timss.purchase.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.net.URLDecoder;
import net.sf.json.JSONArray;

import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.timss.inventory.bean.InvWarehouse;
import com.timss.inventory.service.InvWarehouseService;
import com.timss.inventory.vo.InvMatApplyVO;
import com.timss.purchase.bean.PurApply;
import com.timss.purchase.bean.PurApplyItem;
import com.timss.purchase.bean.PurOrder;
import com.timss.purchase.service.PurApplyService;
import com.timss.purchase.service.PurPubInterface;
import com.timss.purchase.utils.CommonUtil;
import com.timss.purchase.utils.ReflectionUtil;
import com.timss.purchase.utils.WorkflowPurUtil;
import com.timss.purchase.vo.PurApplyItemVO;
import com.timss.purchase.vo.PurApplyVO;
import com.timss.purchase.vo.PurOrderItemVO;
import com.yudean.homepage.bean.ProcessFucExtParam;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.HistoryInfoService.HistoricTask;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.utils.WorkFlowConstants;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurApplyController.java
 * @author: 890166
 * @createDate: 2014-6-20
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "purchase/purapply")
public class PurApplyController {

  /**
   * log4j输出
   */
  private static final Logger LOG = Logger.getLogger(PurApplyController.class);

  @Autowired
  private PurApplyService purApplyService;

  @Autowired
  private ItcMvcService itcMvcService;

  @Autowired
  private WorkflowService workflowService;

  @Autowired
  private HomepageService homepageService;

  @Autowired
  private WorkflowPurUtil workflowPurUtil;
  
  @Autowired
  private PurPubInterface purPubInterface;
  
  @Autowired
  private InvWarehouseService invWarehouseService;

  /**
   * @description:列表页面跳转
   * @author: 890166
   * @createDate: 2014-8-16
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/purApplyList", method = RequestMethod.GET)
  public ModelAndView purApplyList() {
      //湛江风电需要传仓库数据
      List<InvWarehouse> warehouses = purApplyService.queryPurApplyWarehouse(itcMvcService.getUserInfoScopeDatas().getSiteId() );
      String[][] warehousesArray = new String[warehouses.size()][];
      for ( int i=0;i<warehouses.size();i++ ) {
          warehousesArray[i]=new String[2];
          warehousesArray[i][0] = warehouses.get( i ).getWarehouseid();
          warehousesArray[i][1] = warehouses.get( i ).getWarehousename();
      }
      ModelAndView mav = new ModelAndView("/purapply/purApplyList.jsp");
      mav.addObject( "warehouse", JsonHelper.toJsonString( warehousesArray ) );
      return mav;
  }

  /**
   * @description:详细信息页面跳转
   * @author: 890166
   * @createDate: 2014-8-16
   * @param type
   * @param sheetId
   * @return
   * @throws Exception
   *           :
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/purApplyForm", method = RequestMethod.GET)
  @ReturnEnumsBind("ITEMAPPLY_TYPE,PUR_APPLY_ISAUTH,ITEMCLASS_TYPE,PROJECTCLASS_TYPE,ITEMMAJOR_TYPE,PUR_ASSETNATURE,INV_PROCESS_STATUS,PUR_APPLY_STOPSTATUS,ASSET_NATURE")
  public ModelAndView purApplyForm(@RequestParam String type, @RequestParam String sheetId)
      throws Exception {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    ModelAndView mav = new ModelAndView("/purapply/purApplyForm.jsp");
    String createUserId = "";
    String stopProcInstId = "";
    String stopStatus = "";
    String canStop = "";
    String stopProcessStatus = "";
    PurApply purApply = null;
    // 生成采购申请表单页面refactor -- start
    if ("null".equals(sheetId)) {
      UserInfoScope uis = itcMvcService.getUserInfoScopeDatas();
      String sheetNo = uis.getParam("sheetNo");
      sheetId = purApplyService.querySheetIdByFlowNo(sheetNo, uis.getSiteId());
      mav.addObject("sheetId", sheetId);
    }
    if(StringUtils.isNotEmpty( sheetId )){
        purApply = purApplyService.queryPurApplyBySheetId( sheetId );   
        if(null!=purApply){
            createUserId = purApply.getCreateuser();
            //获取终止申请的有关信息
            stopProcInstId = purApply.getStopProcInstId();
            stopStatus = purApply.getStopStatus();
            if ( StringUtils.isNotEmpty( stopProcInstId ) ) {
                List<Task> tasks = workflowService.getActiveTasks( stopProcInstId );
                if ( 0<tasks.size()&&null!=tasks.get( 0 ) )  {
                    Task task = tasks.get( 0 );
                    if ( "procurement".equals( task.getTaskDefinitionKey() ) ) {
                        List<String> users = workflowService.getCandidateUsers( task.getId() );
                        if ( users.size()>0 && users.contains( userInfo.getUserId() )){
                            canStop = "true";
                        }
                    }else {
                        List<String> uIdList = (List<String>) workflowService.getVariable(
                                task.getProcessInstanceId(), WorkFlowConstants.CURRENT_TASK_CANDIDATE_USER);
                        if ( 0<uIdList.size() ) {
                            for ( String user : uIdList ) {
                                if ( userInfo.getUserId().equals( user )){
                                    canStop = "true";
                                    break;
                                }
                            }
                        }
                    } 
                    stopProcessStatus = tasks.get( 0 ).getTaskDefinitionKey();
                }
            }
        }
    }
    Map<String, Object> result = purApplyService.initPurApplyForm(type, sheetId);
    Set<String> keySet = result.keySet();
    for (String key : keySet) {
      mav.addObject(key, result.get(key));
    }
    String cateName = userInfo.getParam( "cateName" );
    cateName = URLDecoder.decode( StringUtils.trimToEmpty(cateName),"UTF-8" );
    mav.addObject( "cateName", cateName );
    Boolean isContainsInQty = purPubInterface.isContainsInQty(userInfo, sheetId);
    mav.addObject( "isContainsInQty", isContainsInQty );
    Boolean allItemsApplying = purApplyService.allItemsApplying(sheetId);
    mav.addObject( "allItemsApplying", allItemsApplying );
    Boolean hasItemsBusinessApplying = purApplyService.hasItemsBusinessApplying(sheetId);
    mav.addObject( "hasItemsBusinessApplying", hasItemsBusinessApplying );
    Boolean hasItemApplying = purApplyService.hasItemApplying(sheetId);
    mav.addObject( "hasItemApplying", hasItemApplying );
    mav.addObject( "isCreator", StringUtils.equals( createUserId, userInfo.getUserId() )?true:false );
    mav.addObject( "isLastStepAssignee",false );
    if ( null != result.get( "processInstId" ) ) {
        //最近完成的任务排在最后面
        List<HistoricTask> historicTasks = workflowService.getPreviousTasks(String.valueOf( result.get( "processInstId" )  ));
        int size = historicTasks.size();
        if(0<historicTasks.size()&&historicTasks.get(size-1).getTaskDefinitionKey().indexOf( "procurement")>-1){
            if ( userInfo.getUserId().equals( historicTasks.get(size-1).getAssignee() ) ) {
                mav.addObject( "isLastStepAssignee",true );
            }
        }
    }
    mav.addObject( "stopProcInstId", stopProcInstId );
    mav.addObject( "stopStatus", stopStatus );
    mav.addObject( "canStop", canStop );
    mav.addObject( "stopProcessStatus", stopProcessStatus );
    // 生成采购申请表单页面refactor -- end
    //湛江风电需要传仓库数据
    List<InvWarehouse> warehouses = invWarehouseService.queryAllWarehouseBySiteId( itcMvcService.getUserInfoScopeDatas().getSiteId() );
    String[][] warehousesArray = new String[warehouses.size()][];
    for ( int i=0;i<warehouses.size();i++ ) {
        warehousesArray[i]=new String[2];
        warehousesArray[i][0] = warehouses.get( i ).getWarehouseid();
        warehousesArray[i][1] = warehouses.get( i ).getWarehousename();
    }
    mav.addObject( "warehouse", JsonHelper.toJsonString( warehousesArray ) );
    return mav;
  }

  /**
   * @description:弹出窗口物资列表跳转
   * @author: 890166
   * @createDate: 2014-8-16
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/purApplyItemList", method = RequestMethod.GET)
  public String purApplyItemList() {
    return "/purapply/purApplyItemList.jsp";
  }

  /**
   * @description:查询采购申请列表(单独申请)
   * @author: 890166
   * @createDate: 2014-6-26
   * @param search
   *          页面传来查询参数
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/queryPurApply", method = RequestMethod.POST)
  public Page<PurApplyVO> queryPurApply(String search) throws Exception {
    PurApplyVO purApplyVO = null;
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    Page<PurApplyVO> pavPage = purApplyService.queryPurApply(userInfo, purApplyVO);
    return pavPage;
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
  @RequestMapping(value = "/queryPurApplyDetail", method = RequestMethod.POST)
  public PurApplyVO queryPurApplyDetail(String sheetId) throws Exception {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

    PurApplyVO purApplyVO = new PurApplyVO();

    List<PurApply> list = purApplyService.queryPurApplyInfoBySheetId(userInfo, sheetId);
    if (null != list && !list.isEmpty()) {
      PurApply purApply = list.get(0);

      if (null != purApply) {
        purApplyVO = (PurApplyVO) ReflectionUtil.conventBean2Bean(purApply, purApplyVO);
        BigDecimal totalCost = purApply.getTatolcost();
        if (null != totalCost) {
          totalCost = totalCost.setScale(2, BigDecimal.ROUND_HALF_EVEN);
          purApplyVO.setTotalcost(totalCost.toString());
        }
      }
    }
    purApplyVO.setSheetId(sheetId);

    return purApplyVO;
  }

  /**
   * @description:查询采购申请物资列表
   * @author: 890166
   * @createDate: 2014-6-26
   * @param sheetId
   *          审批单id
   * @param type
   *          审批类型
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/queryPurApplyItemList", method = RequestMethod.POST)
  public Page<PurApplyItemVO> queryPurApplyItemList(String sheetId, String type, String sendType)
      throws Exception {
    Page<PurApplyItemVO> page = new Page<PurApplyItemVO>();
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    if ("single".equals(type)) {// 若是单独审批
      page = purApplyService.queryPurApplyItemList(userInfo, sheetId, sendType);
    }
    return page;
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
  @RequestMapping(value = "/queryRelateMatApplyList", method = RequestMethod.POST)
  public Page<InvMatApplyVO> queryRelateMatApplyList(String sheetId) throws Exception {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    return purPubInterface.queryRelateMatApplyList(userInfo, sheetId);
  }
    

  /**
   * @description:保存 、 提交采购申请方法
   * @author: 890166
   * @createDate: 2014-6-24
   * @param formData 表单数据
   * @param listData 列表数据
   * @param type 点击方式：提交（submit）、保存（save）
   * @param oper 操作类型：单独审批（single）、汇总审批（sum）
   * @param sheetId 汇总审批中需要绑定单独审批的审批id
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/commitApply", method = RequestMethod.POST)
  @ValidFormToken
  public Map<String, Object> commitApply(String formData, String listData, String type,
      String oper, String sheetId, String taskId, String activeStatus) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();

    boolean flag = true;
    // 将表单的数据转换成实体类
    PurApply pa = JsonHelper.fromJsonStringToBean(formData, PurApply.class);
    if ("safetyStock".equals(pa.getSource())) {
      pa.setSource("1");
      pa.setSheetname(pa.getSheetname() + "(安全库存采购申请)");
    }
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    //新建保存的时候，listData中的批复数量由于数据类型的设定，被设为0，之前在CommonUtil中强行把0转为itemnum却会导致无法将批复数量设为0的bug
    Boolean isNew = StringUtils.isEmpty( sheetId ) ?true:false;
    // 将json列表数据转换成List数据
    List<PurApplyItem> paiList = CommonUtil.conventJsonToPurApplyItemList(listData,isNew);
    List<PurApplyItem> paiListTmp = new ArrayList<PurApplyItem>(0);
    for ( PurApplyItem purApplyItem : paiList ) {
        purApplyItem.setSiteid( userInfo.getSiteId());
        paiListTmp.add( purApplyItem );
    }
    paiList = paiListTmp;
    
    // 添加提交参数
    Map<String, Object> paramMap = new HashMap<String, Object>(); // 参数map
    paramMap.put("type", type);
    paramMap.put("oper", oper);
    paramMap.put("sheetId", sheetId);
    paramMap.put("taskId", taskId);
    paramMap.put("assetNature", pa.getAssetNature());

    // 20160107 add by yuanzh 采购申请加上附件上传功能
    String uploadIds = userInfo.getParam("uploadIds");
    if (StringUtils.isNotEmpty(uploadIds)) {
      uploadIds = uploadIds.replace("\"", "");
      paramMap.put("uploadIds", uploadIds);
    }
    if("submit".equals(activeStatus)){
    	String activeItemCode = purApplyService.queryActiveByPurApply(paiList);
    	if(StringUtils.isNotEmpty(activeItemCode)){
    		result.put("result", "false");
    		result.put("msg", activeItemCode + "物资的分类绑定已变化，请删除明细后重新添加");
    		return result;
    	}
    }
    // 如果在提交操作时，物资类表中没有任何数据，则不让提交
    flag = commitApply(userInfo, pa, paiList, paramMap);
    result.put("sheetId", paramMap.get("sheetId"));
    result.put("taskId", paramMap.get("taskId"));
    result.put("sheetNo", paramMap.get("sheetNo"));
    result.put("processInstanceId", paramMap.get("processInstanceId"));
    result.put("status", paramMap.get("status"));
    if (flag) {
      result.put("result", "success");
    } else {
      result.put("result", "false");
      result.put("msg", "提交失败");
    }
    return result;
  }
  /** 
   * @description:申请终止采购申请方法
   * @author: 890162
   * @createDate: 2016-11-28
   * @param sheetId 采购申请id
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/applyStop", method = RequestMethod.POST)
  @ValidFormToken
  public Map<String, Object> applyStop(String sheetId,String orgProcessInstId) throws Exception {
      Map<String, Object> result = new HashMap<String, Object>(0);
      String resultMsg = "";
      resultMsg = purApplyService.checkStopable(sheetId);
      Boolean isNew = false ;
      Boolean res = false ;
      if (0==resultMsg.length()) {
          //开启流程          
          UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
          Map<String, Object> paramMap = new HashMap<String, Object>(0);
          paramMap.put( "sheetId", sheetId );
          Map<String, Object> flowMap = new HashMap<String, Object>();
          flowMap.put("businessId", sheetId);
          flowMap.put("orgProcessInstId", orgProcessInstId);
          paramMap.put("flowMap", flowMap);
          workflowPurUtil.startupStopProcess(paramMap, userInfo, "PurApplyStop");
          Task task = (Task) paramMap.get("task");
          isNew = Boolean.valueOf( String.valueOf( paramMap.get( "isNew" ) ) );
          if (isNew) {
            String type = "edit_single" ;
            PurApply pa = purApplyService.queryPurApplyBySheetId( sheetId );
            homepageService.createProcess(
                String.valueOf(pa.getSheetno()),
                task.getProcessInstanceId(),
                task.getName(),
                pa.getSheetname(),
                task.getName(),
                "/purchase/purapply/purApplyForm.do?taskId=" + task.getId() + "&processInstId="
                    + task.getProcessInstanceId() + "&type=" + type + "&sheetId=" + sheetId,userInfo,  new ProcessFucExtParam());
            //后台触发第一环节的审批操作
            List<String> nkeyList = workflowService.getNextTaskDefKeys(task.getProcessInstanceId(),task.getTaskDefinitionKey());
            Map<String, List<String>> map = new HashMap<String, List<String>>(0);
            if (null != nkeyList && !nkeyList.isEmpty()) {
                String nextDefKey = nkeyList.get(0);
                List<SecureUser> candidateUsers = workflowService.selectUsersByTaskKey( task.getProcessInstanceId(), nextDefKey, null );
                List<String> tempList = new ArrayList<String>(0);
                for ( SecureUser user : candidateUsers ) {
                    tempList.add( user.getId() );
                }
                map.put(nextDefKey, tempList);
            }
            //workflowService.complete( task.getId(), userInfo.getUserId(), userInfo.getUserId(), map, "申请终止采购申请", false);
          }
          res = true;
          result.put( "stopTaskId", task.getId() );
          result.put( "stopProcInstId", task.getProcessInstanceId() );
      } 
      result.put("result", String.valueOf(res));
      result.put("msg", resultMsg);
      result.put( "isNew", String.valueOf( isNew ) );
      return result;
  }
  /**
   * @description:作废终止采购申请
   * @author: 890162
   * @createDate: 2016-11-29
   * @param sheetId 采购申请id
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/nullifyStopPurApply", method = RequestMethod.POST)
  @ValidFormToken
  public Map<String, Object> nullifyStopPurApply(String sheetId) throws Exception {
      Map<String, Object> result = new HashMap<String, Object>(0);
      boolean nullifyResult = purApplyService.nullifyStopPurApply( sheetId );
      result.put("result", String.valueOf( nullifyResult ));
      return result;
  }
  
  /**
   * @description:删除终止采购申请待办
   * @author: 890162
   * @createDate: 2016-12-06
   * @param sheetId 采购申请id
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/removeStopPurApply", method = RequestMethod.POST)
  @ValidFormToken
  public Map<String, Object> removeStopPurApply(String sheetId,String procInstId) throws Exception {
      Map<String, Object> result = new HashMap<String, Object>(0);
      boolean removeResult = purApplyService.removeStopPurApply( sheetId,procInstId );
      result.put("result", String.valueOf( removeResult ));
      return result;
  }
  
  /**
   * @description:保存页面传过来的表单和列表信息
   * @author: 890166
   * @createDate: 2014-6-26
   * @param pa
   *          主单信息
   * @param paiList
   *          子单信息
   * @param oper
   *          操作类型：单独审批（single）、汇总审批（sum）
   * @param sheetId
   *          汇总审批中需要绑定单独审批的审批id
   * @return:
   */
  private boolean saveApply(UserInfoScope userInfo, PurApply pa, List<PurApplyItem> paiList,
      Map<String, Object> paramMap) throws Exception {
    return purApplyService.saveOrUpdatePurApply(userInfo, pa, paiList, paramMap);
  }

  /**
   * @description: 提交方法 参数待定（先做保存操作，之后再加入流程信息）
   * @author: 890166
   * @createDate: 2014-6-24
   * @return
   * @throws Exception
   */
  private boolean commitApply(UserInfoScope userInfo, PurApply pa, List<PurApplyItem> paiList,
      Map<String, Object> paramMap) throws Exception {
    boolean flag = true;

    String oper = paramMap.get("oper") == null ? "" : String.valueOf(paramMap.get("oper"));
    String taskId = paramMap.get("taskId") == null ? "" : String.valueOf(paramMap.get("taskId")); // 任务id

    Map<String, Object> flowMap = new HashMap<String, Object>();
    flowMap.put("sheetclassid", pa.getSheetclassid());
    flowMap.put("itemclassid", pa.getItemclassid());
    flowMap.put("tatolcost", pa.getTatolcost());
    flowMap.put("isLastStep", false);
    paramMap.put("flowMap", flowMap);

    workflowPurUtil.startupProcess(paramMap, userInfo, "PurApply");
    Task task = (Task) paramMap.get("task");

    if ("".equals(taskId)) {
      paramMap.put("status", "1");
    }

    if (null != task) {
      paramMap.put("taskId", task.getId());
      paramMap.put("taskName", task.getName());
      // 采购申请执行环节
      if (task.getTaskDefinitionKey().indexOf(CommonUtil.getProperties("procurement")) > -1) {
        paramMap.put("status", "3");
      }
      if ("draft".equals(task.getTaskDefinitionKey())) {
        paramMap.put("proStatus", "draft");
      }
    }

    flag = saveApply(userInfo, pa, paiList, paramMap);
    workflowService.setVariable( task.getProcessInstanceId(), "businessId", pa.getSheetId() );
    if (flag&&"".equals(taskId)) {// 若是新建
      String type = "edit_" + oper;
      homepageService.createProcess(
          String.valueOf(paramMap.get("sheetNo")),
          task.getProcessInstanceId(),
          "采购申请",
          pa.getSheetname(),
          task.getName(),
          "/purchase/purapply/purApplyForm.do?taskId=" + task.getId() + "&processInstId="
              + task.getProcessInstanceId() + "&type=" + type + "&sheetId=" + pa.getSheetId(),
          userInfo,  new ProcessFucExtParam());

    }else if ( "draft".equals( task.getTaskDefinitionKey() ) ) {
        homepageService.modify( null, String.valueOf(paramMap.get("sheetNo")), null, pa.getSheetname(), null, null, null, null );
    }

    return flag;
  }

  /**
   * @description:发送商务网
   * @author: 890166
   * @createDate: 2014-7-3
   * @param itemids
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/sendToBusiness", method = RequestMethod.POST)
  public Map<String, Object> sendToBusiness(String sheetId, String itemids) {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

    Map<String, Object> result = new HashMap<String, Object>();
    // 获取页面传过来的id
    JSONArray array = JSONArray.fromObject(itemids);
    String[] itemidArr = (String[]) JSONArray.toArray(array, String.class);

    try {
      // 调用商务网接口
      boolean flag = purApplyService.sendDataToBussiness(userInfo, sheetId, itemidArr);
      // 这里补从发送商务网方法
      if (flag) {
        // 发送商务网状态为1，物资状态为2
        int count = purApplyService.updatePurApplyItemBusinessStatus(sheetId, itemidArr, 1, "2");
        if (count == itemidArr.length) {
          result.put("result", "success");
        } else {
          result.put("result", "false");
        }

        purApplyService.syncDataToEamItem4RealTime(itemidArr, userInfo.getSiteId());
      } else {
        result.put("result", "false");
      }
    } catch (Exception e) {
      LOG.info("--------------------------------------------");
      LOG.info("- PurApplyController 中的 sendToBusiness 方法抛出异常：", e);
      LOG.info("--------------------------------------------");
      result.put("result", "false");
    }

    return result;
  }

  /**
   * @description:自动生成采购单
   * @author: 890166
   * @createDate: 2014-7-7
   * @param appSheetId
   * @throws Exception
   *           :
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/autoGenerateOrder", method = RequestMethod.GET)
  @ReturnEnumsBind("ITEMORDER_TYPE,PUR_ORDER_TAXRATE")
  public ModelAndView autoGenerateOrder(String appSheetId, String itemIds) throws Exception {
    ModelAndView mav = new ModelAndView("/purorder/purOrderForm.jsp");

    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    // 抽取采购申请中与采购单中公共信息
    Map<String, Object> map = purApplyService.queryGenerateOrder(userInfo, appSheetId, itemIds);

    // 抽取主单信息
    PurOrder purOrder = (PurOrder) map.get("purOrder");
    String purOrderStr = JsonHelper.fromBeanToJsonString(purOrder);
    mav.addObject("poData", purOrderStr);
    // 抽取列表信息
    List<PurOrderItemVO> poiList = (List<PurOrderItemVO>) map.get("poiList");
    if (null != poiList && !poiList.isEmpty()) {
      JSONArray jArray = JSONArray.fromObject(poiList);
      mav.addObject("poListData", jArray.toString());
    }
    mav.addObject("operOrder", "autoGenerate");
    mav.addObject("siteId", userInfo.getSiteId().toLowerCase());
    return mav;
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
      List<PurApplyItemVO> paiList = purApplyService.queryPurApplyItemList(userInfo, sheetId, null)
          .getResults();
      for (PurApplyItemVO paiv : paiList) {
        purApplyService.updatePurApplyItemStatus("7", sheetId, paiv.getItemid(),paiv.getInvcateid());
      }
      // 如果不是草稿的话，就设为4 作废 并且更新待办人为空
      PurApply pa = purApplyService.queryPurApplyBySheetId(sheetId);
      if (!"0".equals(pa.getPurchstatus())) {
        pa.setPurchstatus("4");
        pa.setSheetId(sheetId);
        pa.setModifyaccount(userInfo.getUserId());
        pa.setModifydate(new Date());
        purApplyService.updatePurApplyInfoTransactor( sheetId, "" );
        purApplyService.updatePurApplyInfo(pa);
      }
      workflowService.stopProcess(taskId, curUser, curUser, message);
      result.put("result", "success");
    } catch (Exception e) {
      LOG.info("--------------------------------------------");
      LOG.info("- PurApplyController 中的 stopProcess 方法抛出异常：", e);
      LOG.info("--------------------------------------------");
      result.put("result", "error");
    }
    return result;
  }

  /**
   * @description:提交商务网后更新采购申请状态
   * @author: 890166
   * @createDate: 2014-12-8
   * @param sheetId
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/buiss2UpdatePAStatus", method = RequestMethod.POST)
  public Map<String, Object> updatePurApplyStatus(String sheetId) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      PurApply purApply = new PurApply();
      purApply.setSheetId(sheetId);
      purApply.setPurchstatus("2");
      purApplyService.updatePurApplyInfo(purApply);
      result.put("result", "success");
    } catch (Exception e) {
      LOG.info("--------------------------------------------");
      LOG.info("- PurApplyController 中的 updatePurApplyStatus 方法抛出异常：", e);
      LOG.info("--------------------------------------------");
      result.put("result", "error");
    }
    return result;
  }

  @RequestMapping("/itemDetail")
  public ModelAndView itemDetail(String sheetId, String itemid) throws Exception {
    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    Map<String, Object> result = new HashMap<String, Object>(0);
    PurApplyItemVO purApplyItemVOCon = new PurApplyItemVO();
    purApplyItemVOCon.setItemid(itemid);
    purApplyItemVOCon.setCommitcommecnetwk("0");
    List<PurApplyItemVO> purApplyItems = purApplyService.queryPurApplyItemListAsList(userInfoScope,
        sheetId, purApplyItemVOCon);
    if (null != purApplyItems && !purApplyItems.isEmpty()) {
      PurApplyItemVO purApplyItem = purApplyItems.get(0);
      result.put("result", JsonHelper.fromBeanToJsonString(purApplyItem));
    }
    return new ModelAndView("purapply/itemDtl.jsp", result);
  }

  /**
   * @description: 撤回商务网询价
   * @author: yuanzh
   * @createDate: 2016-1-20
   * @param sheetId
   * @param itemids
   * @return:
   */
  @RequestMapping(value = "/withdrawBusiness", method = RequestMethod.POST)
  public Map<String, Object> withdrawBusiness(String sheetId, String itemids) {
    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    Map<String, Object> result = new HashMap<String, Object>();
    // 获取页面传过来的id
    JSONArray array = JSONArray.fromObject(itemids);
    String[] itemidArr = (String[]) JSONArray.toArray(array, String.class);
    LOG.info(">>>>>>>>>>>>>>>> 用户 " + userInfoScope.getUserName() + " 在 "
        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 点击撤回商务网操作");
    LOG.info(">>>>>>>>>>>>>>>> 采购申请单id号为：" + sheetId + " | 撤回的物资编码为：" + itemids);
    try {
      // 撤回的时候商务网状态为0,审批状态为3
      int count = purApplyService.updatePurApplyItemBusinessStatus(sheetId, itemidArr, 0, "3");
      if (count == itemidArr.length) {
        LOG.info(">>>>>>>>>>>>>>>> 商务网撤回操作成功");
        result.put("result", "success");
      } else {
        LOG.info(">>>>>>>>>>>>>>>> 商务网撤回操作失败");
        result.put("result", "false");
      }
    } catch (Exception e) {
      LOG.info("--------------------------------------------");
      LOG.info("- PurApplyController 中的 withdrawBusiness 方法抛出异常：", e);
      LOG.info("--------------------------------------------");
      result.put("result", "false");
    }
    return result;
  }
}
