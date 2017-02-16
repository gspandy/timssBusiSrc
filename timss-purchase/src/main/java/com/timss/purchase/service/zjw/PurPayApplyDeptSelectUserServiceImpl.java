package com.timss.purchase.service.zjw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.purchase.bean.PurApply;
import com.timss.purchase.dao.PurApplyDao;
import com.timss.purchase.dao.PurOrderDao;
import com.timss.purchase.dao.PurPayDao;
import com.timss.purchase.vo.PurOrderItemVO;
import com.timss.purchase.vo.PurPayVO;
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
@Component("PurPayApplyDeptSelectUserServiceImpl")
public class PurPayApplyDeptSelectUserServiceImpl implements SelectUserInterface {

  @Autowired
  private PurPayDao purPayDao;
  
  @Autowired
  private PurOrderDao purOrderDao;
  
  @Autowired
  private PurApplyDao purApplyDao;

  @Autowired
  private ItcMvcService itcMvcService;
  
  @Autowired
  private SelectUserService selectUserService;

  /**
   * 湛江风电根据付款所属采购合同第一条物资明细对应采购申请的申请人所在部门id，找部长角色用户
   */
  @Override
  public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
    // 初始化返回列表
    List<SecureUser> suList = null;
    List<SecureUser> reList = null;
    // 人员信息变量
    UserInfo uInfo = null;
    // 获取当前流程实例id
    String processId = selectUserInfo.getProcessInstId();
    // 通过流程实例id找到PurApply中的创建人id
    List<PurPayVO> purPayVOs = purPayDao.queryPurPayByProcInstId(processId);
    // 如果没有为空的情况
    if (null != purPayVOs && !purPayVOs.isEmpty()) {
        String sheetId = purPayVOs.get( 0 ).getSheetId();
        Map<String, Object> paramMap = new HashMap<String, Object>(0);
        paramMap.put( "sheetId", sheetId );
        List<PurOrderItemVO> purOrderItemVOs = purOrderDao.queryPurOrderItemListByCondition( paramMap );
        if ( purOrderItemVOs.size()>0 ) {
            String applySheetId = purOrderItemVOs.get( 0 ).getApplySheetId();
            PurApply purApply = purApplyDao.queryPurApplyBySheetId( applySheetId );
            uInfo = itcMvcService.getUserInfoById(purApply.getCreateuser());
            String orgId = uInfo.getOrgs().get(0).getCode(); 
            suList = selectUserService.byRoleAndOrg("ZJW_BMBZ",orgId ,"U");
            reList = new ArrayList<SecureUser>(0);
            for (SecureUser su : suList) {
                if (!reList.contains(su)) {
                  reList.add(su);
                }
              }
        }
    }
    return reList;
  }

}
