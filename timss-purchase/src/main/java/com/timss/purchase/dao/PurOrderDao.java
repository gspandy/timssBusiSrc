package com.timss.purchase.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.purchase.bean.PurOrder;
import com.timss.purchase.bean.PurOrderItem;
import com.timss.purchase.bean.PurPolicy;
import com.timss.purchase.bean.PurPolicyTemp;
import com.timss.purchase.vo.PurApplyOrderItemVO;
import com.timss.purchase.vo.PurOrderItemVO;
import com.timss.purchase.vo.PurOrderVO;
import com.yudean.itc.dto.Page;

/*
 * @description: {desc}
 * @company: gdyd
 * @className: PurOrderDao.java
 * @author: 890166
 * @createDate: 2014-6-30
 * @updateUser: 890166
 * @version: 1.0
 */
public interface PurOrderDao {

  /*
   * @description:查询采购单列表
   * 
   * @author: 890166
   * 
   * @createDate: 2014-6-30
   * 
   * @param page
   * 
   * @return:
   */
  List<PurOrderVO> queryPurOrder(Page<?> page);

  /*
   * @description:查询详细信息页面的时候表单信息获取
   * 
   * @author: 890166
   * 
   * @createDate: 2014-6-30
   * 
   * @param sheetId
   * 
   * @return:
   */
  List<PurOrderVO> queryPurOrderInfoBySheetId(String sheetId);

  /*
   * @description:详细表单中列表
   * 
   * @author: 890166
   * 
   * @createDate: 2014-7-1
   * 
   * @param page
   * 
   * @return:
   */
  List<PurOrderItemVO> queryPurOrderItemList(Page<?> page);

  /*
   * @description:物资合并查询
   * 
   * @author: 890166
   * 
   * @createDate: 2014-7-1
   * 
   * @param userInfo
   * 
   * @param sheetId
   * 
   * @return
   * 
   * @throws Exception :
   */
  List<PurOrderItemVO> queryPurOrderItemListExce(Page<?> page);

  /*
   * @description:根据sheetid查询PurOrder
   * 
   * @author: 890166
   * 
   * @createDate: 2014-7-1
   * 
   * @param sheetId
   * 
   * @return:
   */
  List<PurOrder> queryPurOrderBySheetId(String sheetId);

  /*
   * @description:根据sheetid查询PurOrder(外部接口查询)
   * 
   * @author: 890166
   * 
   * @createDate: 2014-8-26
   * 
   * @param map
   * 
   * @return:
   */
  List<PurOrder> queryPurOrderBySheetIds(Map<String, Object> map);

  /*
   * @description:通过sheetid找到PurOrderItem
   * 
   * @author: 890166
   * 
   * @createDate: 2014-7-1
   * 
   * @param sheetId
   * 
   * @return
   * 
   * @throws Exception
   */
  List<String> queryPurOrderItemExists(String sheetId);

  /*
   * @description:更新PurOrder信息
   * 
   * @author: 890166
   * 
   * @createDate: 2014-7-1
   * 
   * @param po
   * 
   * @return
   * 
   * @throws Exception
   */
  int updatePurOrderInfo(PurOrder po);

  /*
   * @description:插入PurOrder信息
   * 
   * @author: 890166
   * 
   * @createDate: 2014-7-1
   * 
   * @param po
   * 
   * @return
   * 
   * @throws Exception
   */
  int insertPurOrderInfo(PurOrder po);

  /*
   * @description:调用插入PurOrderItem的存储过程
   * 
   * @author: 890166
   * 
   * @createDate: 2014-7-1
   * 
   * @param list
   * 
   * @throws Exception
   */
  void callProcPurOrderItemInsert(Map<String, Object> params);

  /*
   * @description:调用删除PurOrderItem的存储过程
   * 
   * @author: 890166
   * 
   * @createDate: 2014-7-1
   * 
   * @param list
   * 
   * @throws Exception
   */
  void callProcPurOrderItemDelete(Map<String, Object> params);

  /*
   * @description:通过flowNo查询到sheetid
   * 
   * @author: 890166
   * 
   * @createDate: 2014-9-23
   * 
   * @param sheetNo
   * 
   * @param siteId
   * 
   * @return:
   */
  String querySheetIdByFlowNo(Map<String, Object> map);

  /*
   * @description: 通过sheetid查询flowno
   * 
   * @author: 890166
   * 
   * @createDate: 2015-2-26
   * 
   * @param sheetId
   * 
   * @param siteId
   * 
   * @return
   * 
   * @throws Exception:
   */
  String queryFlowNoBySheetId(Map<String, Object> map);

  /*
   * @description: 通过时间段查询采购总额
   * 
   * @author: 890166
   * 
   * @createDate: 2015-2-4
   * 
   * @return
   * 
   * @throws Exception
   */
  BigDecimal queryPurPriceTotal(Map<String, Object> map);

  /*
   * @description: 通过poItem的联合主键查询采购单(合同)之明细
   * 
   * @author: 890162
   * 
   * @createDate: 2015-9-17
   * 
   * @return
   * 
   * @throws Exception
   */
  List<PurOrderItem> queryPurOrderItem(PurOrderItem poItem);

  /*
   * @description: 更新采购单(合同)之明细
   * 
   * @author: 890162
   * 
   * @createDate: 2015-9-17
   * 
   * @return
   * 
   * @throws Exception
   */
  int updatePurOrderItem(PurOrderItem poItem);

  /*
   * @description: 新增采购单(合同)之明细
   * 
   * @author: 890162
   * 
   * @createDate: 2015-9-17
   * 
   * @return
   * 
   * @throws Exception
   */
  int insertPurOrderItem(PurOrderItem poItem);

  /*
   * @description:查询标准合同条款列表
   * 
   * @author: 890162
   * 
   * @createDate: 2015-9-22
   * 
   * @param page
   * 
   * @return:
   */
  List<PurPolicyTemp> queryPurPolicyTempListPage(Page<?> page);

  /*
   * @description:查询标准合同条款列表
   * 
   * @author: 890162
   * 
   * @createDate: 2015-9-24
   * 
   * @param page
   * 
   * @return:
   */
  List<PurPolicyTemp> queryPurPolicyTempList(PurPolicyTemp purPolicyTemp);

  /*
   * @description:查询合同条款列表
   * 
   * @author: 890162
   * 
   * @createDate: 2015-9-24
   * 
   * @param page
   * 
   * @return:
   */
  List<PurPolicy> queryPurPolicyList(PurPolicy purPolicy);

  /*
   * @description:根据ID查询标准合同条款
   * 
   * @author: 890162
   * 
   * @createDate: 2015-9-22
   * 
   * @param Id
   * 
   * @return:
   */
  List<PurPolicyTemp> queryPurPolicyTempById(String id);

  /*
   * @description:插入标准合同条款
   * 
   * @author: 890162
   * 
   * @createDate: 2015-9-22
   * 
   * @param ppt
   * 
   * @return
   * 
   * @throws Exception
   */
  int insertPurPolicyTemp(PurPolicyTemp ppt);

  /*
   * @description:更新标准合同条款
   * 
   * @author: 890162
   * 
   * @createDate: 2015-9-22
   * 
   * @param ppt
   * 
   * @return
   * 
   * @throws Exception
   */
  int updatePurPolicyTemp(PurPolicyTemp ppt);

  /*
   * @description:删除标准合同条款
   * 
   * @author: 890162
   * 
   * @createDate: 2015-9-22
   * 
   * @param ppt
   * 
   * @return
   * 
   * @throws Exception
   */
  int deletePurPolicyTemp(PurPolicyTemp ppt);

  /*
   * @description:删除合同条款
   * 
   * @author: 890162
   * 
   * @createDate: 2015-9-24
   * 
   * @param sheetId
   * 
   * @return
   * 
   * @throws Exception
   */
  int deletePurOrderPolicyBySheetId(String sheetId);

  /*
   * @description:插入合同条款
   * 
   * @author: 890162
   * 
   * @createDate: 2015-9-24
   * 
   * @param pp
   * 
   * @return
   * 
   * @throws Exception
   */
  int insertPurOrderPolicy(PurPolicy pp);

  /*
   * @description:查询合同条款
   * 
   * @author: 890162
   * 
   * @createDate: 2015-9-29
   * 
   * @param sheetId
   * 
   * @param siteId
   * 
   * @return
   * 
   * @throws Exception
   */
  List<PurOrderItemVO> queryPurOrderItemListExceAsList(@Param("sheetId") String sheetId,
      @Param("siteId") String siteId);

  /*
   * @description:更新PurOrder 待办人
   * 
   * @author: 890162
   * 
   * @createDate: 2015-10-19
   * 
   * @param purOrder
   * 
   * @return
   * 
   * @throws Exception :
   */
  int updatePurOrderTransactor(PurOrder purOrder);

  List<PurOrderItemVO> queryPurOrderItemListByCondition(Map<String, Object> paramMap);

  void deletePurOrderItemListByCondition(Map<String, Object> paramMap);

  void updatePurOrderItemByCondition(Map<String, Object> paramMap);

  List<PurApplyOrderItemVO> queryPurApplyOrderItemList(Page<?> page);
  
  /**
   * 查询本年度合同（审批通过）下各专业物资采购金额总和
   * @param map
   * @return
   */
  List<Map<String, BigDecimal>> queryMajorPurchase(Map<String, Object> map);
  
  /**
   * 查询物资分类id
   * @param map
   * @return
   */
  List<String> queryInvcateIdByCondition(Map<String, Object> map);
  /**
   * 查询采购单状态
   * @param map
   * @return
   */
  List<String> queryStatusBySheetId(@Param("sheetId") String sheetId);
  /**
   * 查询符合条件的项目数量
   * @Title: selectBySpNoAndSiteid
   * @param spNo
   * @param siteid
   * @return
   */
  int selectBySpNoAndSiteid(@Param("spNo")String spNo,@Param("siteid") String siteid);
  
  /**
   * 查询审批完成的合同总金额
   * @param map
   * @return
   */
  BigDecimal queryPurOrderTotalMoney(Map<String, Object> map) throws Exception;
  
  /**
   * 审批完成的合同相关已报账金额
   * @param map
   * @return
   */
  BigDecimal queryPurOrderReimbursedTotalMoney(Map<String, Object> map) throws Exception;

}
