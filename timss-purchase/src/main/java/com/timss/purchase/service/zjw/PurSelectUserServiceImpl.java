package com.timss.purchase.service.zjw;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.purchase.bean.PurApply;
import com.timss.purchase.dao.PurApplyDao;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.SelectUserService;

/**
 * @title: 湛江风电自定义选人逻辑
 * @description: 湛江风电自定义选人逻辑
 * @company: gdyd
 * @className: PurSelectUserServiceImpl.java
 * @author: yuanzh
 * @createDate: 2015-12-2
 * @updateUser: yuanzh
 * @version: 1.0
 */
@Component("PurSelectUserServiceImpl")
public class PurSelectUserServiceImpl implements SelectUserInterface {

  @Autowired
  private PurApplyDao purApplyDao;

  @Autowired
  private ItcMvcService itcMvcService;
  
  @Autowired
  private SelectUserService selectUserService;

  /**
   * 湛江风电根据采购申请中创建人选取其对应的部门部长
   */
  @Override
  public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
    // 初始化返回列表
    List<SecureUser> reList = new ArrayList<SecureUser>();
    List<SecureUser> tempList = new ArrayList<SecureUser>();
    List<SecureUser> suList = null;
    // 人员信息变量
    UserInfo uInfo = null;

    // 获取当前流程实例id
    String processId = selectUserInfo.getProcessInstId();
    // 通过流程实例id找到PurApply中的创建人id
    List<PurApply> paList = purApplyDao.queryPurApplyByProcessIdAndPurOrder(processId);
    // 如果没有为空的情况
    if (null != paList && !paList.isEmpty()) {
      // 通过循环集合（由于是通过采购合同找回采购申请所以这里可能会出现多条采购申请的情况）
      for (PurApply pa : paList) {
        // 获取该创建人信息
        uInfo = itcMvcService.getUserInfoById(pa.getCreateaccount());
        // 根据创建人和用户组为条件查询对应的部门的部门经理人员
        /*
        suList = selectUserInfo.getAm().retriveUsersWithSpecificGroup("ZJW_PUR_deptmanager",
            uInfo.getOrgId(), true, true);
            */
        suList = selectUserService.byGroupAndOrg("ZJW_PUR_deptmanager", uInfo.getOrgId(),"U");
        // 将部门经理人员add到返回的list里面
        tempList.addAll(suList);
      }
      // 去掉有可能的重复值
      for (SecureUser su : tempList) {
        if (!reList.contains(su)) {
          reList.add(su);
        }
      }
    }
    return reList;
  }

}
