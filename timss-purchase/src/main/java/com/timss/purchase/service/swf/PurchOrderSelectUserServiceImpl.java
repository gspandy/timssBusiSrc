package com.timss.purchase.service.swf;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.SelectUserService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: 湛江生物质自定义选人逻辑
 * @description: 湛江生物质自定义选人逻辑
 * @company: gdyd
 * @className: PurchOrderSelectUserServiceImpl.java
 * @author: gucw
 * @createDate: 2016-10-31
 * @updateUser: gucw
 * @version: 1.0
 */
@Component("PurchOrderSelectUserServiceImpl")
public class PurchOrderSelectUserServiceImpl implements SelectUserInterface {
  @Autowired
  private SelectUserService selectUserService;
  
  @Autowired
  private WorkflowService workflowService;
  /**
   * 湛江生物质根据采购合同中申请环节处理人选取其对应的执行采购人
   */
  @Override
  public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
    // 初始化返回列表
    List<SecureUser> suList = new ArrayList<SecureUser>(0);
    // 获取当前流程实例id
    String processId = selectUserInfo.getProcessInstId();
    String userId = String.valueOf( workflowService.getVariable( processId, "apply" ));
    if ( StringUtils.isNotEmpty( userId ) ) {
        suList = selectUserService.byUserId( userId  ) ;
    }
    return suList;
  }
}
