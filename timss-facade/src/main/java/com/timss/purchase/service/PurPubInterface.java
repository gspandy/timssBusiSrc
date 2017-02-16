package com.timss.purchase.service;

import java.util.List;

import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvMatApplyVO;
import com.timss.purchase.bean.PurInvoiceBean;
import com.timss.purchase.bean.PurInvoiceItemBean;
import com.timss.purchase.vo.PurApplyStockItemVO;
import com.timss.purchase.vo.PurApplyVO;
import com.timss.purchase.vo.PurInvoiceAssetVo;
import com.timss.purchase.vo.PurOrderVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: 采购公开接口
 * @description: 采购公开接口
 * @company: gdyd
 * @className: PurPubInterface.java
 * @author: yuanzh
 * @createDate: 2015-9-22
 * @updateUser: yuanzh
 * @version: 1.0
 */
public interface PurPubInterface {

    /**
     * @description: 使用ihint来查询采购合同信息
     * @author: yuanzh
     * @createDate: 2015-9-22
     * @param inputStr : 可以输入采购合同编码或者中文名来查询
     * @return:
     */
    public List<PurOrderVO> queryPurOrderByIHint(String inputStr);

    /**
     * @description: 根据采购合同id查询采购合同主单信息
     * @author: yuanzh
     * @createDate: 2015-9-22
     * @param sheetId
     * @return:
     */
    public PurOrderVO queryPurOrderVOBySheetId(String sheetId);

    /**
     * @description: 根据采购和同id查询物资信息
     * @author: yuanzh
     * @createDate: 2015-9-22
     * @param sheetId
     * @return:
     */
    public List<PurInvoiceAssetVo> queryPurInvoiceAssetVoBySheetId(String sheetId);

    /**
     * @description: 根据发票信息更新交易表中入库价格
     * @author: yuanzh
     * @createDate: 2015-9-23
     * @param piibList ：发票子项列表
     * @param purOrderId ：采购合同id值
     * @return:
     */
    public boolean updateTranDetailByInvoice(List<PurInvoiceItemBean> piibList, String purOrderId);

    /**
     * @description:根据采购和同id查询物资信息(datagrid列表)
     * @author: fengzt
     * @createDate: 2015年9月23日
     * @param paramsPage
     * @return:List<PurInvoiceAssetVo>
     */
    public List<PurInvoiceAssetVo> queryWuziByContractId(Page<PurInvoiceAssetVo> paramsPage);

    /**
     * @description:通过采购合同id查询所有关联的发票
     * @author: yuanzh
     * @createDate: 2015-9-24
     * @param contractId
     * @return:
     */
    public List<PurInvoiceBean> queryInvoiceRelationByContractId(String contractId);

    /**
     * @description: 如果已接收数量等于采购数量，且有关联的未报账的发票记录，系统发送站内信息给这些发票的录入人
     * @author: yuanzh
     * @createDate: 2015-9-29
     * @param imtid 物资接收id
     * @param siteId 站点id
     * @return:
     */
    public List<PurInvoiceBean> queryInvoice2SendNotice(String sheetNo, String siteId);

    /**
     * @description:已入库物资列表
     * @author: user
     * @createDate: 2016-1-22
     * @param userInfo
     * @param sheetId
     * @return:
     */
    Page<PurApplyStockItemVO> queryPurApplyStockItemList(UserInfoScope userInfo, String sheetId);
    
    /**
     * @description:执行情况列表
     * @author: 890162
     * @createDate: 2016-7-1
     * @param userInfo
     * @param sheetId
     * @return: Page<PurApplyStockItemVO>
     */
    Page<PurApplyStockItemVO> queryPurApplyImplemetationStatusList(UserInfoScope userInfo, String sheetId);
    
    /**
     * @description:执行情况列表
     * @author: 890162
     * @createDate: 2016-7-11
     * @param userInfo
     * @param sheetId
     * @return: Page<PurApplyStockItemVO>
     */
    List<PurApplyStockItemVO> queryPurApplyImplemetationStatusListAsList(UserInfoScope userInfo, String sheetId);
    
    /**
     * @description:相关领料列表
     * @author: 890162  
     * @createDate: 2016-7-7
     * @param userInfo
     * @param sheetId
     * @return:
     */
    Page<InvMatApplyVO> queryRelateMatApplyList(UserInfoScope userInfo, String sheetId);
    
    /**
     * @description:根据执行情况列表的已入库量 判断是否存在已入库>0
     * @author: 890162
     * @createDate: 2016-7-7
     * @param sheetId
     * @return: Boolean
     */
    Boolean isContainsInQty(UserInfoScope userInfo,String sheetId);
    
    /**
     * @description:物资领料采购申请列表查询
     * @author: 890162  
     * @createDate: 2016-7-8
     * @param userInfo
     * @return: Page<PurApplyVO>
     */
    Page<PurApplyVO> queryPurApplyList(UserInfo userinfo) throws Exception;
    /**
     * @description: 物资领料根据采购申请id返回对应的入库量大于0的物资明细
     * @author: 890162
     * @createDate: 2016-8-25
     * @param userInfo
     * @return List<InvItemVO>
     * @throws Exception :
     */
    List<InvItemVO> queryPurApplyItemListByIdInIMA(String sheetId,String siteid) throws Exception;
    
}
