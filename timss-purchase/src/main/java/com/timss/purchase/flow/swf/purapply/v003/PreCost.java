package com.timss.purchase.flow.swf.purapply.v003;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.purchase.flow.abstr.DefApplyProcess;
import com.timss.purchase.service.PurApplyService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;
import com.yudean.workflow.utils.WorkFlowConstants;

/**
 * @title: PreCost工作流控制类
 * @description:
 * @company: gdyd
 * @className: PreCost.java
 * @author: 890162
 * @createDate: 2015-9-22
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class PreCost extends DefApplyProcess {

  @Autowired
  public ItcMvcService itcMvcService;
  @Autowired
  private WorkflowService workflowService;
  @Autowired
  public IAuthorizationManager authManager;
  @Autowired
  @Qualifier("PurApplyServiceImpl")
  private PurApplyService purApplyService;
  private static Logger LOG = Logger.getLogger(PreCost.class);

  @SuppressWarnings("unchecked")
@Override
  public void init(TaskInfo taskInfo) {
    UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();
    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    String processInst = taskInfo.getProcessInstanceId();
    String userId = null;
    String userName = null;
    String siteId = userInfo.getSiteId();

    try {

      List<String> uIdList = (List<String>) workflowService.getVariable(processInst,
          WorkFlowConstants.CURRENT_TASK_CANDIDATE_USER);
      String businessId = String.valueOf(workflowService.getVariable(processInst, "businessId"));

      if (null == uIdList || uIdList.isEmpty()) {
        uIdList = Collections.emptyList();
      } else {
        // 20151127 JIRA ==> TIM219
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
      }

      if (StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(siteId)) {
        SecureUser user = authManager.retriveUserById(userId, siteId);
        userName = user.getName();
      }

      purApplyService.updatePurApplyInfoTransactor(businessId, userName);
      workflowService.setVariable(processInst, "isLastStep", false);
    } catch (Exception e) {
      LOG.info(">>>>>>>>>>>>>>>>>>> 更新待办人异常", e);
    }
  }
}
