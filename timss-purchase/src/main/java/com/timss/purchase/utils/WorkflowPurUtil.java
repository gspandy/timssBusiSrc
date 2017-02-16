package com.timss.purchase.utils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.timss.purchase.bean.PurApply;
import com.timss.purchase.bean.PurApplyItem;
import com.timss.purchase.bean.PurProcessMapping;
import com.timss.purchase.service.PurApplyService;
import com.timss.purchase.service.PurOrderService;
import com.timss.purchase.service.PurPayService;
import com.timss.purchase.service.PurProcessMappingService;
import com.timss.purchase.vo.PurOrderVO;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;
import com.yudean.workflow.utils.WorkFlowConstants;

/**
 * @title: 采购模块流程工具类
 * @description: 采购模块流程工具类
 * @company: gdyd
 * @className: WorkflowPurUtil.java
 * @author: yuanzh
 * @createDate: 2016-2-2
 * @updateUser: yuanzh
 * @version: 1.0
 */
@Component
public class WorkflowPurUtil {

  private WorkflowPurUtil() {

  }

  /**
   * 系统service注入
   */
  @Autowired
  public ItcMvcService itcMvcService;
  @Autowired
  private WorkflowService workflowService;
  @Autowired
  private HomepageService homepageService;
  @Autowired
  public IAuthorizationManager authManager;

  /**
   * 业务实现类注入
   */
  @Autowired
  @Qualifier("PurApplyServiceImpl")
  private PurApplyService purApplyService;
  @Autowired
  @Qualifier("PurOrderServiceImpl")
  private PurOrderService purOrderService;
  @Autowired
  @Qualifier("PurPayServiceImpl")
  private PurPayService purPayService;
  @Autowired
  @Qualifier("PurProcessMappingServiceImpl")
  private PurProcessMappingService purProcessMappingService;

  /**
   * log4j
   */
  private static Logger LOG = Logger.getLogger(WorkflowPurUtil.class);

  /**
   * @description:流程执行中更新业务表中的当前处理人字段
   * @author: yuanzh
   * @createDate: 2016-2-2
   * @param taskInfo
   * @param PurType
   * @return:
   */
  @SuppressWarnings("unchecked")
  public String update2Transactor(TaskInfo taskInfo, String PurType) {
    long startTime = System.currentTimeMillis();// 执行开始时间

    UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();
    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    try {

      // 获取人员id
      String userId = null;
      List<String> uIdList = (List<String>) workflowService.getVariable(
          taskInfo.getProcessInstanceId(), WorkFlowConstants.CURRENT_TASK_CANDIDATE_USER);
      if (null == uIdList || uIdList.isEmpty()) {
        uIdList = Collections.emptyList();
      } else {
        userId = uIdList.get(0);
      }
      if (StringUtils.isNotEmpty(userInfoScope.getParam("userId"))) {
        // 流程退回
        userId = userInfoScope.getParam("userId");
      }

      // 获取业务编号
      String businessId = null;
      Object business = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId");
      if (null != business) {
        businessId = business.toString();
      }

      String siteId = userInfo.getSiteId();
      if (StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(siteId)) {
        SecureUser user = authManager.retriveUserById(userId, siteId);
        if (StringUtils.isNotEmpty(businessId)) {

          if ("PurApply".equals(PurType)) {
            purApplyService.updatePurApplyInfoTransactor(businessId, user.getName());

            LOG.info(">>>>>>>>>>>>>>>>>>> update2Transactor 更新采购申请Transactor方法，businessId为："
                + businessId + " | 操作人为：" + user.getName());

          } else if ("PurOrder".equals(PurType)) {
            purOrderService.updatePurOrderTransactor(businessId, user.getName());

            LOG.info(">>>>>>>>>>>>>>>>>>> update2Transactor 更新采购合同Transactor方法，businessId为："
                + businessId + " | 操作人为：" + user.getName());
          }
        }
      }
    } catch (Exception e) {
      LOG.info(">>>>>>>>>>>>>>>>>>> update2Transactor 方法获取节点候选人异常：", e);
    }
    long endTime = System.currentTimeMillis();// 执行结束时间

    return String.valueOf((endTime - startTime) / 1000);// 返回执行时间
  }

  /**
   * @description:流程最后环节将处理人的名字擦除
   * @author: yuanzh
   * @createDate: 2016-2-2
   * @param taskInfo
   * @param PurType
   * @return:
   */
  public String update2TransactorOver(TaskInfo taskInfo, String PurType) {
    long startTime = System.currentTimeMillis();// 执行开始时间
    // 获取业务编号
    String businessId = null;
    Object business = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId");
    if (null != business) {
      businessId = business.toString();
    }

    if (StringUtils.isNotEmpty(businessId)) {
      try {

        if ("PurApply".equals(PurType)) {
          purApplyService.updatePurApplyInfoTransactor(businessId, "");

          LOG.info(">>>>>>>>>>>>>>>>>>> update2TransactorOver 更新采购申请Transactor方法，businessId为："
              + businessId);

        } else if ("PurOrder".equals(PurType)) {
          purOrderService.updatePurOrderTransactor(businessId, "");

          LOG.info(">>>>>>>>>>>>>>>>>>> update2TransactorOver 更新采购合同Transactor方法，businessId为："
              + businessId);
        }

      } catch (Exception e) {
        LOG.info(">>>>>>>>>>>>>>>>>>> update2TransactorOver 更新办理人异常：", e);
      }
    }
    long endTime = System.currentTimeMillis();// 执行结束时间

    return String.valueOf((endTime - startTime) / 1000);// 返回执行时间
  }

  /**
   * @description:启动流程抽象方法
   * @author: yuanzh
   * @createDate: 2016-2-4
   * @param paramMap
   * @return:
   */
  @SuppressWarnings("unchecked")
  public void startupProcess(Map<String, Object> paramMap, UserInfoScope userInfo, String operType)
      throws Exception {
    String processId = null;
    String userId = userInfo.getUserId();
    String siteId = userInfo.getSiteId();
    String loperType = operType.toLowerCase();
    String taskId = paramMap.get("taskId") == null ? "" : String.valueOf(paramMap.get("taskId")); // 任务id

    Task task = null;

    // 当前环节是否首环节
    if ("".equals(taskId)) {// 若是新建
      // 判断流程类型
      String processDefKey = ("purchase_[@@@]_" + loperType).replace("[@@@]", siteId.toLowerCase());
      String processKey = workflowService.queryForLatestProcessDefKey(processDefKey);
      Map<String, Object> flowMap = (Map<String, Object>) paramMap.get("flowMap");
      ProcessInstance p = workflowService.startLatestProcessInstanceByDefKey(processKey, userId,
          flowMap);

      processId = p.getProcessInstanceId();

      List<Task> taskList = workflowService.getActiveTasks(processId);
      if (null != taskList && !taskList.isEmpty()) {
        task = taskList.get(0);
      }

    } else {// 若是编辑
      task = workflowService.getTaskByTaskId(taskId);

      PurProcessMapping ppm = new PurProcessMapping();
      ppm.setMasterkey(String.valueOf(paramMap.get("sheetId")));
      ppm.setModeltype(loperType);
      ppm.setSiteid(siteId);
      processId = purProcessMappingService.queryProcessIdByParams(ppm);
    }

    String assetNature = paramMap.get("assetNature") == null ? "" : String.valueOf(paramMap
        .get("assetNature"));
    if (!"".equals(assetNature)) {
      workflowService.setVariable(processId, "assetNature", assetNature);
    }

    paramMap.put("task", task);
    paramMap.put("processInstanceId", processId);
  }
  /**
   * @description:启动终止流程方法,如果已经启动了流程，就把流程信息解读出来
   * @author: gucw
   * @createDate: 2016-11-30
   * @param paramMap
   * @return:
   */
  @SuppressWarnings("unchecked")
  public void startupStopProcess(Map<String, Object> paramMap, UserInfoScope userInfo, String operType)
      throws Exception {
    String stopProcInstId = null;
    Task task = null;
    Boolean isNew = false;
    String userId = userInfo.getUserId();
    String siteId = userInfo.getSiteId();
    String loperType = operType.toLowerCase();
    String sheetId = paramMap.get("sheetId") == null ? "" : String.valueOf(paramMap.get("sheetId")); // 采购申请id
    PurApply purApply = purApplyService.queryPurApplyBySheetId( sheetId );
    stopProcInstId = null!=purApply?purApply.getStopProcInstId():"";
    if (StringUtils.isEmpty( stopProcInstId )) {// 若是新建
      // 判断流程类型
      String processDefKey = ("purchase_[@@@]_" + loperType).replace("[@@@]", siteId.toLowerCase());
      String processKey = workflowService.queryForLatestProcessDefKey(processDefKey);
      Map<String, Object> flowMap = (Map<String, Object>) paramMap.get("flowMap");
      ProcessInstance p = workflowService.startLatestProcessInstanceByDefKey(processKey, userId,
          flowMap);
      stopProcInstId = p.getProcessInstanceId();
      isNew = true;
    } 
    List<Task> taskList = workflowService.getActiveTasks(stopProcInstId);
    if (null != taskList && !taskList.isEmpty()) {
      task = taskList.get(0);
    }
    paramMap.put("task", task);
    paramMap.put("processInstanceId", stopProcInstId);
    paramMap.put("isNew", isNew);
  }
  /**
   * @description:采购申请流程环节结束设置流程变量
   * @author: yuanzh
   * @createDate: 2016-2-4
   * @param taskInfo
   * @return:
   */
  public String paProcessOnComplete(TaskInfo taskInfo) {
    long startTime = System.currentTimeMillis();// 执行开始时间

    String processId = taskInfo.getProcessInstanceId();
    UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();

    Object sheetIdObj = workflowService.getVariable(processId, "sheetId");

    try {
      if (null != sheetIdObj) {
        List<PurApply> paList = purApplyService.queryPurApplyInfoBySheetId(userInfo,
            String.valueOf(sheetIdObj));
        if (null != paList && !paList.isEmpty()) {
          PurApply pa = paList.get(0);
          workflowService.setVariable(processId, "businessId", pa.getSheetId());
          workflowService.setVariable(processId, "tatolcost", pa.getTatolcost());
          workflowService.setVariable(processId, "sheetclassid", pa.getSheetclassid());

          // 20151214 分支分组选择 JIRA==>TIM330
          List<AppEnum> aeList = itcMvcService.getEnum("ITEMAPPLY_TYPE");
          for (AppEnum ae : aeList) {
            if (pa.getSheetclassid().equals(ae.getCode())) {
              workflowService.setVariable(processId, "branch", ae.getAttribute1());
              break;
            }
          }
        }
        // 遍历采购单下所有的采购申请，检查是否存在草稿或正在申请中的物资，如果没有就将采购申请状态设为已归档
        List<PurApplyItem> pai = purApplyService.queryPurApplyItemListBySheetId(String
            .valueOf(sheetIdObj));
        boolean isAllPass = true;
        for (PurApplyItem purApplyItem : pai) {
          if (StringUtils.isNotBlank(purApplyItem.getStatus())
              && Integer.valueOf(purApplyItem.getStatus()) < 2) {
            isAllPass = false;
          }
        }
        if (isAllPass) {
          PurApply purApply = new PurApply();
          purApply.setPurchstatus("3");
          purApply.setSheetId(String.valueOf(String.valueOf(sheetIdObj)));
          purApplyService.updatePurApplyInfo(purApply);
        }
      }
    } catch (Exception e) {
      LOG.info(">>>>>>>>>>>>>>>>>>> paProcessOnComplete 设置流程变量异常：", e);
    }

    long endTime = System.currentTimeMillis();// 执行结束时间

    return String.valueOf((endTime - startTime) / 1000);// 返回执行时间
  }

  /**
   * @description:采购合同流程环节结束设置流程变量
   * @author: yuanzh
   * @createDate: 2016-2-4
   * @param taskInfo
   * @return:
   */
  public String poProcessOnComplete(TaskInfo taskInfo) {
    long startTime = System.currentTimeMillis();// 执行开始时间

    String processId = taskInfo.getProcessInstanceId();
    Object sheetIdObj = workflowService.getVariable(processId, "sheetId");

    try {
      if (null != sheetIdObj) {
        List<PurOrderVO> poList = purOrderService.queryPurOrderInfoBySheetId(String
            .valueOf(sheetIdObj));
        if (null != poList && !poList.isEmpty()) {
          PurOrderVO pov = poList.get(0);
          workflowService.setVariable(processId, "businessId", pov.getSheetId());
          workflowService.setVariable(processId, "sheetIType", pov.getSheetIType());
          workflowService.setVariable(processId, "totalPrice", new BigDecimal(pov.getTotalPrice()));

          // 20151214 分支分组选择 JIRA==>TIM330
          List<AppEnum> aeList = itcMvcService.getEnum("ITEMORDER_TYPE");
          for (AppEnum ae : aeList) {
            if (pov.getSheetIType().equals(ae.getCode())) {
              workflowService.setVariable(processId, "branch", ae.getAttribute1());
              break;
            }
          }
        }
      }
    } catch (Exception e) {
      LOG.info(">>>>>>>>>>>>>>>>>>> poProcessOnComplete 设置流程变量异常：", e);
    }

    long endTime = System.currentTimeMillis();// 执行结束时间

    return String.valueOf((endTime - startTime) / 1000);// 返回执行时间
  }
  /**
   * @Title:revertPaItemStatusForZJW
   * @Description:湛江风电执行采购环节 回退时需要更新采购明细的
   * @param @param taskInfo
   * @param @return
   * @return int
   * @throws
   */
  public int revertPaItemStatusForZJW(TaskInfo taskInfo) {
      String siteId = itcMvcService.getUserInfoScopeDatas().getSiteId();
      String taskKey  = taskInfo.getTaskDefKey();
      Integer result = 0;
      if ( "ZJW".equals( siteId )&&("Procurement".equals( taskKey )) ) {
          LOG.info(">>>>>>>>>>>>>>>>>>> revertPaItemStatusForZJW 执行");
          String businessId = null;
          Object business = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId");
          if (null != business) {
            businessId = business.toString();
          }
          try {
            result = purApplyService.revertPurApplyItemToApplyStatus( businessId );
          } catch (Exception e) {
              LOG.info(">>>>>>>>>>>>>>>>>>> revertPaItemStatusForZJW 异常：", e);
          }  
      }
      return result;
  }
  
  /**
   * @description:更新付款处理人方法
   * @author: gucw
   * @createDate: 2016-12-21
   * @param taskInfo
   * @param PurType
   * @return:
   */
  @SuppressWarnings("unchecked")
  public String update2Transactor4PurPay(TaskInfo taskInfo) {
    long startTime = System.currentTimeMillis();// 执行开始时间
    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    String procInstId = taskInfo.getProcessInstanceId();
    String businessId = workflowService.getVariable(procInstId, "businessId") == null ? "": String.valueOf(workflowService.getVariable(procInstId, "businessId"));
    String userId = "";
    String userName = "";
    String siteId = userInfoScope.getSiteId();
    List<String> uIdList = (List<String>) workflowService.getVariable(procInstId,WorkFlowConstants.CURRENT_TASK_CANDIDATE_USER);
    try {
        if (null == uIdList || uIdList.isEmpty()) {
            uIdList = Collections.emptyList();
        } else {
            StringBuffer sbuffer = new StringBuffer("");
            for (String userIdStr : uIdList) {
               SecureUser user = authManager.retriveUserById(userIdStr, siteId);
               sbuffer.append(user.getName()).append(",");
            }
            userName = sbuffer.substring(0, sbuffer.length() - 1);
        }
        if (StringUtils.isNotEmpty(userInfoScope.getParam("userId"))) {
            // 退回
            userId = userInfoScope.getParam("userId");
            userName = authManager.retriveUserById(userId, siteId).getName();
        }
        purPayService.updatePurPayInfoTransactor( businessId, userName );
    } catch (Exception e) {
        LOG.info( ">>>>>>>>>>>>>>>>>>> 更新状态和待办人异常", e );
    }
    long endTime = System.currentTimeMillis();// 执行结束时间

    return String.valueOf((endTime - startTime) / 1000);// 返回执行时间
  }
}
