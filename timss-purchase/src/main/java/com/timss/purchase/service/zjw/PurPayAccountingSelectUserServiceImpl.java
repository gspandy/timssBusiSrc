package com.timss.purchase.service.zjw;

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
@Component("PurPayAccountingSelectUserServiceImpl")
public class PurPayAccountingSelectUserServiceImpl implements SelectUserInterface {

  @Autowired
  private PurPayDao purPayDao;
    
  @Autowired
  private PurOrderDao purOrderDao;
    
  @Autowired
  private PurApplyDao purApplyDao;

  @Autowired
  private SelectUserService selectUserService;
  
  @Autowired
  private ItcMvcService itcMvcService;

  /**
   * 湛江风电根据付款单所属合同对应的所有采购申请，但凡有一个为固定资产的资产属性选ZJW_KJ_GDZC,否则ZJW_KJ_YCL
   */
  @Override
  public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
      // 初始化返回列表
      List<SecureUser> suList = null;
      // 获取当前流程实例id
      String processId = selectUserInfo.getProcessInstId();
      // 通过流程实例id找到PurApply中的创建人id
      List<PurPayVO> purPayVOs = purPayDao.queryPurPayByProcInstId(processId);
      Boolean isFixedAsset = false;
      // 如果没有为空的情况
      if (null != purPayVOs && !purPayVOs.isEmpty()) {
          for ( PurPayVO purPayVO : purPayVOs ) {
              String sheetId = purPayVO.getSheetId();
              Map<String, Object> paramMap = new HashMap<String, Object>(0);
              paramMap.put( "sheetId", sheetId );
              List<PurOrderItemVO> purOrderItemVOs = purOrderDao.queryPurOrderItemListByCondition( paramMap );
              for ( PurOrderItemVO purOrderItemVO : purOrderItemVOs ) {
                  String applySheetId = purOrderItemVO.getApplySheetId();
                  PurApply purApply = purApplyDao.queryPurApplyBySheetId( applySheetId );
                  if ( "固定资产".equals( purApply.getSheetclassid() ) ) {
                      isFixedAsset = true;
                  }
              }
          }
      }
      if ( isFixedAsset ) {
          suList = selectUserService.byGroup( "ZJW_KJ_GDZC" );
      }else {
          suList = selectUserService.byGroup( "ZJW_KJ_YCL" );  
      }
      return suList;
  }
}
