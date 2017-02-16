package com.timss.purchase.listener;

import java.util.List;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.purchase.bean.PurOrder;
import com.timss.purchase.bean.PurProcessMapping;
import com.timss.purchase.service.PurApplyService;
import com.timss.purchase.service.PurOrderService;
import com.timss.purchase.service.PurProcessMappingService;
import com.timss.purchase.vo.PurApplyItemVO;
import com.yudean.homepage.bean.DeleteDraftParam;
import com.yudean.itc.annotation.HopAnnotation;
import com.yudean.itc.annotation.HopAnnotation.ProType;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

@Component
public class PurchaseListener {

  @Autowired
  private ItcMvcService itcMvcService;
  @Autowired
  private WorkflowService workflowService;
  @Autowired
  private PurApplyService purApplyService;
  @Autowired
  private PurOrderService purOrderService;
  @Autowired
  private PurProcessMappingService purProcessMappingService;

  /**
   * @description: 采购申请草稿删除功能
   * @author: 890166
   * @createDate: 2015-2-27
   * @param param
   * @throws Exception
   *           :
   */
  @HopAnnotation(value = "purchApply", type = ProType.DeleteDraft, Sync = true)
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void purApplyDeleteDraft(DeleteDraftParam param) {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

    String curUser = userInfo.getUserId();
    String flowId = param.getFlowId();
    String siteId = param.getSiteid();
    String modelType = "purapply";

    try {
      String sheetId = purApplyService.querySheetIdByFlowNo(flowId, siteId);
      List<PurApplyItemVO> paiList = purApplyService.queryPurApplyItemList(userInfo, sheetId, null)
          .getResults();
      for (PurApplyItemVO paiv : paiList) {
        purApplyService.updatePurApplyItemStatus("7", sheetId, paiv.getItemid(),paiv.getInvcateid());
      }
      deleteDraft(sheetId, siteId, curUser, modelType);

    } catch (Exception e) {
      throw new RuntimeException(
          "---------PurchaseListener 中的 purApplyDeleteDraft 方法抛出异常---------：", e);
    }
  }

  /**
   * @description: 采购单草稿删除功能
   * @author: 890166
   * @createDate: 2015-2-27
   * @param param
   * @throws Exception
   *           :
   */
  @HopAnnotation(value = "purchOrder", type = ProType.DeleteDraft, Sync = true)
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void purOrderDeleteDraft(DeleteDraftParam param) {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    String curUser = userInfo.getUserId();
    String flowId = param.getFlowId();
    String siteId = param.getSiteid();
    String modelType = "purorder";

    try {
      String sheetId = purOrderService.querySheetIdByFlowNo(flowId, siteId);
      PurOrder po = new PurOrder();
      po.setSheetId(sheetId);
      po.setStatus("5");
      purOrderService.updatePurOrderInfo(po);

      deleteDraft(sheetId, siteId, curUser, modelType);

    } catch (Exception e) {
      throw new RuntimeException(
          "---------PurchaseListener 中的 purOrderDeleteDraft 方法抛出异常---------：", e);
    }
  }

  /**
   * @description:统一删除草稿
   * @author: 890166
   * @createDate: 2015-2-27
   * @param sheetId
   * @param siteId
   * @param curUser
   * @param modelType
   * @throws Exception
   *           :
   */
  private void deleteDraft(String sheetId, String siteId, String curUser, String modelType)
      throws Exception {
    PurProcessMapping ppm = new PurProcessMapping();
    ppm.setMasterkey(sheetId);
    ppm.setSiteid(siteId);
    ppm.setModeltype(modelType);
    String processInst = purProcessMappingService.queryProcessIdByParams(ppm);
    List<Task> tasks = workflowService.getActiveTasks(processInst);
    if (null != tasks && !tasks.isEmpty()) {
      for (Task task : tasks) {
        workflowService.stopProcess(task.getId(), curUser, curUser, "此流程已被中止");
      }
    }
  }
}
