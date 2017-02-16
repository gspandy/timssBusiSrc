package com.timss.purchase.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.inventory.bean.InvWarehouse;
import com.timss.purchase.bean.PurApply;
import com.timss.purchase.bean.PurApplyItem;
import com.timss.purchase.vo.EamItemVO;
import com.timss.purchase.vo.PurApplyItemVO;
import com.timss.purchase.vo.PurApplyVO;
import com.timss.purchase.vo.PurOrderItemVO;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurApplyDao.java
 * @author: 890166
 * @createDate: 2014-6-20
 * @updateUser: 890166
 * @version: 1.0
 */
public interface PurApplyDao {

  /**
   * @description:查询采购申请列表
   * @author: 890166
   * @createDate: 2014-6-20
   * @param page
   * @UPDATEDATE:2015-10-19 GUCW
   * @return:
   */
  List<PurApplyVO> queryPurApply(Page<?> page);

  /**
   * @description:根据sheetid查询出采购申请表单详细信息
   * @author: 890166
   * @createDate: 2014-6-23
   * @param sheetId
   * @return:
   */
  List<PurApply> queryPurApplyInfoBySheetId(Page<?> page);

  /**
   * @description:根据sheetid查询出采购申请表单详细信息
   * @author: 890162
   * @createDate: 2015-9-28
   * @param sheetId
   * @return:
   */
  PurApply queryPurApplyBySheetId(String sheetId);

  List<PurApply> queryPurApplyInfoBySheetIdNoZh(Page<?> page);

  /**
   * @description:查询采购申请物资列表
   * @author: 890166
   * @createDate: 2014-6-26
   * @param page
   * @return:
   */
  List<PurApplyItemVO> queryPurApplyItemList(Page<?> page);

  /**
   * @description:弹出框内的申请物资信息列表
   * @author: 890166
   * @createDate: 2014-6-30
   * @param userInfo
   * @param purOrderItemVO
   * @return
   * @throws Exception :
   */
  List<PurOrderItemVO> queryApplyItemByEntity(Page<?> page);

  /**
   * @description:弹出框内的申请物资信息列表
   * @author: 890166
   * @createDate: 2014-7-1
   * @param list
   * @return:
   */
  List<PurOrderItemVO> queryApplyItemByList(List<String> list);
  
  /**
   * @Title:queryPurApplyWarehouse
   * @Description:查询采购申请项目名称关联的仓库信息
   * @param siteId
   * @return List<InvWarehouse>
   * @throws
   */
  List<InvWarehouse> queryPurApplyWarehouse(String siteId);

  /**
   * @description:新建PurApply信息
   * @author: 890166
   * @createDate: 2014-6-24
   * @param purApply
   * @return
   * @throws Exception :
   */
  int insertPurApplyInfo(PurApply purApply);

  /**
   * @description:批量插入PurApplyItem信息(页面数据直接插入)
   * @author: 890166
   * @createDate: 2014-6-26
   * @param paiList
   * @return:
   */
  int insertPurApplyItemWithList(List<PurApplyItem> paiList);

  /**
   * @description:根据sheetid删除PurApplyItem信息
   * @author: 890166
   * @createDate: 2014-6-24
   * @param sheetId
   * @return
   * @throws Exception :
   */
  int deletePurApplyItemBySheetId(String sheetId);

  /**
   * @description:批量更新采购申请明细是否已经提交商务网状态
   * @author: 890166
   * @createDate: 2014-7-3
   * @param itemIds
   * @return
   * @throws Exception :
   */
  int updatePurApplyItemBusinessStatus(Map<String, Object> map);

  /**
   * @description:更新状态
   * @author: 890166
   * @createDate: 2014-8-8
   * @param map
   * @return:
   */
  int updatePurApplyItemStatus(Map<String, Object> map);

  /**
   * @description:更新PurApply信息
   * @author: 890166
   * @createDate: 2014-6-24
   * @param purApply
   * @return
   * @throws Exception :
   */
  int updatePurApplyInfo(PurApply purApply);

  /**
   * @description:通过flowNo查询到sheetid
   * @author: 890166
   * @createDate: 2014-9-23
   * @param sheetNo
   * @param siteId
   * @return:
   */
  String querySheetIdByFlowNo(Map<String, Object> map);

  /**
   * @description: 通过sheetid查询flowno
   * @author: 890166
   * @createDate: 2015-2-26
   * @param sheetId
   * @param siteId
   * @return
   * @throws Exception:
   */
  String queryFlowNoBySheetId(Map<String, Object> map);

  /**
   * @description: 查询采购申请单详细信息
   * @author: 890166
   * @createDate: 2014-10-29
   * @param map
   * @return:
   */
  List<PurApplyItemVO> queryPurApplyItemBySheetIdAndItems(Map<String, Object> map);

  /**
   * @description: 查询今天发送到商务网的物资信息
   * @author: 890166
   * @createDate: 2014-10-30
   * @return:
   */
  List<EamItemVO> queryHhcSyncDataInTimss(Map<String, Object> map);

  /**
   * @description:更新PurApply 待办人
   * @author: 890162
   * @createDate: 2015-9-22
   * @param purApply
   * @return
   * @throws Exception :
   */
  int updatePurApplyInfoTransactor(PurApply purApply);

  /**
   * @description:更新PurApply 是否发送到商务网
   * @author: 890162
   * @createDate: 2015-9-28
   * @param purApply
   * @return
   * @throws Exception :
   */
  int updatePurApplyInfoIsToBusiness(PurApply purApply);

  /**
   * @description:根据sheetId查询采购申请物资列表
   * @author: 890162
   * @createDate: 2015-09-28
   * @param page
   * @return:
   */
  List<PurApplyItem> queryPurApplyItemListBySheetId(String sheetId);

  /**
   * @description:根据sheetId查询采购申请物资列表
   * @author: 890162
   * @createDate: 2015-09-28
   * @param page
   * @return:
   */
  List<PurApplyItemVO> queryPurApplyItemListAsList(@Param("sheetId") String sheetId,
      @Param("purApplyItemVOCon") PurApplyItemVO purApplyItemVOCon);

  /**
   * @description: 通过processId关联到purorder表，然后再通过purorder表找到purapply信息
   * @author: yuanzh
   * @createDate: 2015-12-2
   * @param processId
   * @return:
   */
  List<PurApply> queryPurApplyByProcessIdAndPurOrder(@Param("processId") String processId);

  /**
   * @description: 查询合同的执行情况
   * @author: 王中华
   * @createDate: 2016-3-22
   * @param page
   * @return:
   */
  List<PurOrderItemVO> queryOrderItemDoingStatus(Page<PurOrderItemVO> page);

  /**
   * @description:查询采购申请的物资是否分类启用
   * @author: 890191
   * @createDate: 2016-8-12
   * @param paiList
   * @return map
   * @throws Exception :
   */
  String queryActiveByPurApply(List<PurApplyItem> paiList);
  /**
   * @description:查询采购申请明细对应物资编号，物资分类以及仓库对应的物资绑定信息都是否都被禁用 库存二次改造前 会出现一些无绑定货柜的状态为禁用的物资绑定脏数据
   * @author: 890162
   * @createDate: 2016-12-14
   * @param paiList
   * @return map
   * @throws Exception :
   */
  String queryNotAllActiveByPurApply(List<PurApplyItem> paiList);
  /**	
   * @Title:updatePurApplyItemStatusByApplyId
   * @Description:将SHEET_ID为purApplyId的采购申请下所有fromStatus状态明细更新为toStatus
   * @param  purApplyId
   * @param  fromStatus
   * @param  toStatus
   * @return int
   * @throws
   */
   int updatePurApplyItemStatusByApplyId(@Param("purApplyId")String purApplyId,@Param("fromStatus")String fromStatus,@Param("toStatus")String toStatus);
   /**
    * @Title:queryPAItemsInPO
    * @Description:查询采购合同正在审批中的采购申请明细
    * @param sheetId
    * @param status
    * @return List<PurApplyItemVO>
    */
   List<PurApplyItemVO>queryPAItemsInPO(@Param("sheetId")String sheetId,@Param("status")String status);
   /**
    * @Title:queryPAItemsInBusinessApproving
    * @Description:查询发送商务网采购申请明细
    * @param sheetId
    * @return List<PurApplyItemVO>
    */
   List<PurApplyItemVO>queryPAItemsInBusinessApproving(String sheetId);
   /**
    * @Title:updatePurApplyInfoStopProcInfo
    * @Description:更新终止采购申请信息
    * @param sheetId
    * @param processInstId
    * @param stopStatus
    * @return int
    * @throws
    */
   int updatePurApplyInfoStopProcInfo(@Param("sheetId")String sheetId,@Param("processInstId")String processInstId,@Param("stopStatus")String stopStatus);
}
