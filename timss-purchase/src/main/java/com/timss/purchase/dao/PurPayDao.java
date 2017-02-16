package com.timss.purchase.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.purchase.bean.PurPay;
import com.timss.purchase.bean.PurPayDtl;
import com.timss.purchase.vo.PurPayDtlVO;
import com.timss.purchase.vo.PurPayStatVO;
import com.timss.purchase.vo.PurPayVO;

/*
 * @title: PurPayDao
 * @description: 采购合同付款的Dao
 * @company: gdyd
 * @className: PurPayDao.java
 * @author: 890162
 * @createDate: 2016-3-22
 * @updateUser: 890162
 * @version: 1.0
 */
public interface PurPayDao {

  
  /**
   * @Title:queryBlankPurPayBySheetId
   * @Description:根据sheetId生成一份空白的付款记录
   * @param sheetId
   * @return List<PurPayVO>
   * @throws
   */
  List<PurPayVO> queryBlankPurPayBySheetId(@Param("sheetId")String sheetId);
  /**
   * @Title:queryPurPayByCondition
   * @Description:按条件查询付款信息
   * @param useInnerJoin
   * @param sheetId
   * @param payId
   * @return List<PurPayVO>
   * @throws
   */
  List<PurPayVO> queryPurPayByCondition(@Param("sheetId")String sheetId,@Param("payId")String payId);

  /**
   * queryPurPayDtlListByCondition
   * @Description:按照payId查询付款详情--编辑付款信息时用
   * @param payId
   * @param sheetId
   * @return List<PurPayDtlVO>
   * @throws
   */
  List<PurPayDtlVO> queryPurPayDtlListByCondition(@Param("payId")String payId,@Param("sheetId")String sheetId,@Param("payType")String payType);

  /**
   * @Title:queryPurPaySheetClassId
   * @Description:获取付款信息对应采购合同的采购类型
   * @param sheetId
   * @return List<String>
   * @throws
   */
  List<String> queryPurPaySheetClassId(@Param( "sheetId" )String sheetId);
  
  /**
   * @Title:queryPurPayItemClassId
   * @Description:获取付款信息对应采购申请单的物资类型
   * @param sheetId
   * @return List<String>
   * @throws
   */
  List<String> queryPurPayItemClassId(@Param( "sheetId" )String sheetId);
  
  /**
   * queryArrivePurPayHasQualityPurPay
   * @Description:获取当前报账单是否有非删除/非作废的关联报账单
   * @param payId
   * @return List<String>
   * @throws
   */
  List<String> queryArrivePurPayHasQualityPurPay(@Param( "payId" )String payId);
  
  /**
   * @Title:queryPurPayByProcInstId
   * @Description:根据流程实例id查询付款
   * @param procInstId
   * @return List<PurPayVO>
   * @throws
   */
  List<PurPayVO> queryPurPayByProcInstId(@Param( "procInstId" )String procInstId);
  
  /**
   * @Title:queryPayPriceWithWID
   * @Description:按仓库 物资分类 审批年月查询报账总额和不含税报账总额
   * @param  siteid
   * @param  month
   * @param  warehouseid
   * @param  invcateid
   * @return List<PurPayStatVO>
   * @throws
   */
  List<PurPayStatVO> queryPayPriceWithWID(@Param( "siteid" )String siteid,@Param( "month" )String month,@Param( "warehouseid" )String warehouseid,@Param( "invcateid" )String invcateid);
  
  /**
   * @Title:updatePurPayInfo
   * @Description:更新付款基本信息
   * @param purPay
   * @return int
   * @throws
   */
  int updatePurPayInfo(PurPay purPay);
  
  /**
   * updatePurPayProcInst
   * @Description:更新付款信息流程实例ID
   * @param purPay
   * @return int
   * @throws
   */
  int updatePurPayProcInst(PurPay purPay);
  
  /**
   * @Title:updatePurPayStatusInfo
   * @Description:更新付款基本信息的状态和待办人
   * @param payId
   * @param transactor
   * @param status
   * @return int
   * @throws
   */
  int updatePurPayERPStatus(@Param("payId")String payId,@Param("erpStatus")String erpStatus,@Param("erpDate")Date erpDate);
  
  /**
   * @Title:updatePurPayStatusInfo
   * @Description:更新付款基本信息的状态和待办人
   * @param payId
   * @param transactor
   * @param status
   * @return int
   * @throws
   */
  int updatePurPayStatusInfo(@Param("payId")String payId,@Param("transactor")String transactor,@Param("status")String status);
  
  /**
   * @Title:updatePurpayPayNo
   * @Description:更新采购付款号
   * @param payId
   * @param payType
   * @param siteId
   * @return int
   */
  int updatePurpayPayNo(@Param("payId")String payId,@Param("payType")String payType,@Param("siteId")String siteId);
  
  /**
   * @Title:updatePurpaySpecProperties
   * @Description:更新采购付款某些特定字段
   * @param payId
   * @param properties
   * @return int
   */
  int updatePurpaySpecProperties(@Param("payId")String payId,@Param("properties")Map<String, Object> properties);
  
  /**
   * @Title:updateInvMatTranDetailPrice
   * @Description:更新流水记录价格
   * @param imtdid
   * @param price
   * @return int
   */
  int updateInvMatTranDetailPrice(@Param("imtdid")String imtdid,@Param("price")Double price);
  
  /**
   * @Title:updateInvMatTranDetailPrice
   * @Description:更新流水记录价格
   * @param imtdid
   * @param price
   * @return int
   */
  int updateInvMatTranRecPrice(@Param("imtdid")String imtdid,@Param("price")Double price,@Param("tax")Double tax);
  
  /**
   * @Title:insertPurPayInfo
   * @Description:新增付款基本信息
   * @param purPay
   * @return int
   * @throws
   */
  int insertPurPayInfo(PurPay purPay);

  /**
   * @Title:insertPurPayDtlInfo
   * @Description:新增付款详情
   * @param purPayDtlList
   * @return int
   * @throws
   */
  int insertPurPayDtlInfo(List<PurPayDtl> purPayDtlList);
  
  /**
   * @Title:deletePurPayDtlByPayId
   * @Description:根据payId删除付款详情信息
   * @param payId
   * @return int
   * @throws
   */
  int deletePurPayDtlByPayId(String payId);
  /**
   * @description:通过flowNo查询到sheetid
   * @author: 890166
   * @createDate: 2014-9-23
   * @param sheetNo
   * @param siteId
   * @return:
   */
  List<PurPayVO> queryPayVoByFlowNo(Map<String, Object> map);

}
