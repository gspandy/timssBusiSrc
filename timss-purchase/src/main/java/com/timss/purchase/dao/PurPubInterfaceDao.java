package com.timss.purchase.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvMatApplyVO;
import com.timss.purchase.bean.PurInvoiceBean;
import com.timss.purchase.bean.PurInvoiceItemBean;
import com.timss.purchase.vo.PurApplyStockItemVO;
import com.timss.purchase.vo.PurApplyVO;
import com.timss.purchase.vo.PurInvoiceAssetVo;
import com.timss.purchase.vo.PurOrderVO;
import com.yudean.itc.dto.Page;

/**
 * @title: 采购公开接口
 * @description: 采购公开接口
 * @company: gdyd
 * @className: PurPubInterfaceDao.java
 * @author: yuanzh
 * @createDate: 2015-9-22
 * @updateUser: yuanzh
 * @version: 1.0
 */
public interface PurPubInterfaceDao {

  /**
   * @description:使用ihint来查询采购合同信息
   * @author: yuanzh
   * @createDate: 2015-9-22
   * @param paramMap
   * @return:
   */
  List<PurOrderVO> queryPurOrderByIHint(Map<String, String> paramMap);

  /**
   * @description:根据采购合同id查询采购合同主单信息
   * @author: yuanzh
   * @createDate: 2015-9-22
   * @param sheetId
   * @return:
   */
  List<PurOrderVO> queryPurOrderVOBySheetId(Map<String, String> paramMap);

  /**
   * @description:根据采购和同id查询物资信息
   * @author: user
   * @createDate: 2015-9-22
   * @param paramMap
   * @return:
   */
  List<PurInvoiceAssetVo> queryPurInvoiceAssetVoBySheetId(Map<String, String> paramMap);

  /**
   * 
   * @description:根据采购和同id查询物资信息(datagrid列表)
   * @author: fengzt
   * @createDate: 2015年9月23日
   * @param paramsPage
   * @return:List<PurInvoiceAssetVo>
   */
  List<PurInvoiceAssetVo> queryWuziByContractId(Page<PurInvoiceAssetVo> paramsPage);

  /**
   * @description: 根据发票信息更新交易表中入库价格
   * @author: yuanzh
   * @createDate: 2015-9-23
   * @param piib
   * @return:
   */
  int updateTranDetailByInvoice(@Param("PurInvoiceItemBean") PurInvoiceItemBean piib,
      @Param("purOrderId") String purOrderId, @Param("siteId") String siteId);

  /**
   * @description:通过采购合同id查询所有关联的发票
   * @author: yuanzh
   * @createDate: 2015-9-24
   * @param paramMap
   * @return:
   */
  List<PurInvoiceBean> queryInvoiceRelationByContractId(Map<String, String> paramMap);

  /**
   * @description:如果已接收数量等于采购数量，且有关联的未报账的发票记录，系统发送站内信息给这些发票的录入人
   * @author: yuanzh
   * @createDate: 2015-9-29
   * @param imtid
   * @param siteId
   * @return:
   */
  List<PurInvoiceBean> queryInvoice2SendNotice(@Param("sheetNo") String sheetNo,
      @Param("siteId") String siteId);

  /**
   * @description:已入库物资列表
   * @author: user
   * @createDate: 2016-1-22
   * @param page
   * @return:
   */
  List<PurApplyStockItemVO> queryPurApplyStockItemList(Page<?> page);
  
  /**
   * @description:执行情况列表
   * @author: 890162
   * @createDate: 2016-7-1
   * @param page
   * @return: List<PurApplyStockItemVO>
   */
  List<PurApplyStockItemVO> queryPurApplyImplemetationStatusList(Page<?> page);
  
  /**
   * @description:物资验收根据采购申请id返回入库量大于0的物资明细
   * @author: 890162
   * @createDate: 2016-8-25
   * @param String sheetId
   * @param String siteid
   * @return: List<InvItemVO>
   */
  List<InvItemVO> queryPurApplyItemListByIdInIMA(@Param("sheetId") String sheetId,@Param("siteid") String siteid);
  /**
   * @description:相关领料列表
   * @author: 890162
   * @createDate: 2016-7-7
   * @param page
   * @return: List<InvMatApplyVO>
   */
  List<InvMatApplyVO> queryRelateMatApplyList(Page<?> page);
  
  /**
   * 
   * @description:物资领料查询采购申请
   * @author: 890162
   * @createDate: 2016-07-08
   * @param page
   * @return:List<PurApplyVO>
   */
  List<PurApplyVO> queryPurApplyListForMatApply(Page<PurApplyVO> page);
}
