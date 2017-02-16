package com.timss.purchase.service;

import java.util.List;

import com.timss.purchase.bean.PurInvoiceBean;
import com.timss.purchase.vo.PurInvoiceAssetVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 发票管理
 * @description: {desc}
 * @company: gdyd
 * @className: PurInvoiceService.java
 * @author: fengzt
 * @createDate: 2015年9月18日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface PurInvoiceService {

    /**
     * 
     * @description:通过站点查询发票
     * @author: fengzt
     * @createDate: 2015年9月18日
     * @param paramsPage
     * @return: Page<PurInvoiceBean> 
     */
    Page<PurInvoiceBean> queryInvoiceBySiteId(Page<PurInvoiceBean> paramsPage);

    /**
     * 
     * @description:发票号或者合同号 模糊查询发票
     * @author: fengzt
     * @createDate: 2015年9月18日
     * @param paramsPage
     * @return:Page<PurInvoiceBean>
     */
    Page<PurInvoiceBean> queryInvoiceBySearch(Page<PurInvoiceBean> paramsPage);

    /***
     * 
     * @description:通过合同Id查询物资清单
     * @author: fengzt
     * @createDate: 2015年9月22日
     * @param paramsPage
     * @return:Page<PurInvoiceAssetVo>
     */
    Page<PurInvoiceAssetVo> queryWuziByContractId(Page<PurInvoiceAssetVo> paramsPage);

    /**
     * 
     * @description:插入或者更新发票
     * @author: fengzt
     * @createDate: 2015年9月22日
     * @param formData
     * @param rowData
     * @return:int
     */
    int insertOrUpdateInvoice(String formData, String rowData);

    /**
     * 
     * @description:插入发票
     * @author: fengzt
     * @createDate: 2015年9月22日
     * @param purInvoiceBean
     * @return:int
     */
    int insertInvoice(PurInvoiceBean purInvoiceBean);

    /**
     * 
     * @description:插入物资明细
     * @author: fengzt
     * @createDate: 2015年9月22日
     * @param invoiceAssetVos
     * @param string 
     * @return:int
     */
    int insertWuziItem(List<PurInvoiceAssetVo> invoiceAssetVos, String invoiceId);


    /**
     * 
     * @description:更新发票主信息
     * @author: fengzt
     * @createDate: 2015年9月22日
     * @param purInvoiceBean
     * @return:int
     */
    int updateInvoice(PurInvoiceBean bean);

    /**
     * 
     * @description:根据发票ID删除明细
     * @author: fengzt
     * @createDate: 2015年9月22日
     * @param invoiceId
     * @return:int
     */
    int deleteInvoiceByInvoiceId(String invoiceId);

    /**
     * 
     * @description:根据采购合同id查询采购合同物资列表信息
     * @author: fengzt
     * @createDate: 2015年9月24日
     * @param contractId
     * @return:List<PurInvoiceAssetVo>
     */
    List<PurInvoiceAssetVo> queryWuziByContractId(String contractId);

    /**
     * 
     * @description:发票提醒
     * @author: fengzt
     * @param siteId 
     * @createDate: 2015年9月24日:
     */
    void remindInvoice(String siteId);

    /**
     * 
     * @description:通过ID查询发票主信息
     * @author: fengzt
     * @createDate: 2015年9月25日
     * @param id
     * @return:PurInvoiceBean
     */
    PurInvoiceBean queryInvoiceById(String id);

    /**
     * 
     * @description:通过发票ID查询物资子项
     * @author: fengzt
     * @createDate: 2015年9月25日
     * @param invoiceId
     * @return:List<PurInvoiceAssetVo>
     */
    List<PurInvoiceAssetVo> queryInvoiceItemById(String invoiceId);

    /**
     * 
     * @description:通过发票ID，更新业务状态（非物理删除）
     * @author: fengzt
     * @createDate: 2015年10月8日
     * @param id
     * @return:count
     */
    int deleteInvoiceById(String id);

    /**
     * 
     * @description:发票报账
     * @author: fengzt
     * @createDate: 2015年10月8日
     * @param id
     * @return:int
     */
    int updateInvoiceStatus(String id);
    
    /**
     * 
     * @description:根据合同ID查询发票基本信息
     * @author: fengzt
     * @createDate: 2015年10月8日
     * @param contractId
     * @return:List<PurInvoiceBean>
     */
    List<PurInvoiceBean> queryInvoiceBaseInfoByContractId(String contractId);

    /**
     * 
     * @description:检查同一个站点下发票号是否重复
     * @author: fengzt
     * @createDate: 2015年10月10日
     * @param invoiceNo
     * @param id
     * @return: List<PurInvoiceBean>
     */
    List<PurInvoiceBean> queryCheckInvoiceNo(String invoiceNo, String id);

    /**
     * 
     * @description:查询所有站点
     * @author: fengzt
     * @createDate: 2015年10月13日
     * @return:List<String>
     */
    List<String> queryAllSite();

}
