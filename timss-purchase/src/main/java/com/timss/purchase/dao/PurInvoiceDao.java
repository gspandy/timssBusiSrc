package com.timss.purchase.dao;

import java.util.List;
import java.util.Map;

import com.timss.purchase.bean.PurInvoiceBean;
import com.timss.purchase.bean.PurInvoiceItemBean;
import com.timss.purchase.vo.PurInvoiceScheduleVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 发票
 * @description: {desc}
 * @company: gdyd
 * @className: PurInvoiceDao.java
 * @author: fengzt
 * @createDate: 2015年9月18日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface PurInvoiceDao {

    /**
     * 
     * @description:通过站点查询发票
     * @author: fengzt
     * @createDate: 2015年9月18日
     * @param paramsPage
     * @return:List<PurInvoiceBean>
     */
    List<PurInvoiceBean> queryInvoiceBySiteId(Page<PurInvoiceBean> paramsPage);

    /**
     * 
     * @description:通过发票号或者合同号查询发票（忽略大小写）
     * @author: fengzt
     * @createDate: 2015年9月18日
     * @param paramsPage
     * @return:List<PurInvoiceBean>
     */
    List<PurInvoiceBean> queryInvoiceBySearch(Page<PurInvoiceBean> paramsPage);

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
     * @description:插入物资明细（批量插入）
     * @author: fengzt
     * @createDate: 2015年9月22日
     * @param itemBeans
     * @return:int
     */
    int insertWuziItem(List<PurInvoiceItemBean> list);

    /**
     * 
     * @description:插入发票
     * @author: fengzt
     * @createDate: 2015年9月22日
     * @param purInvoiceBean
     * @return:int
     */
    int insertInvoice(PurInvoiceBean bean);

    /**
     * 
     * @description:更新发票主信息
     * @author: fengzt
     * @createDate: 2015年9月22日
     * @param bean
     * @return:int
     */
    int updateInvoice(PurInvoiceBean bean);

    /**
     * 
     * @description:发票过期提醒
     * @author: fengzt
     * @createDate: 2015年9月24日
     * @param map
     * @return:List<PurInvoiceScheduleVo>
     */
    List<PurInvoiceScheduleVo> remindInvoice( Map<String, Object> map );

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
     * @description:通过发票Id查询子项
     * @author: fengzt
     * @createDate: 2015年9月25日
     * @param invoiceId
     * @return:List<PurInvoiceItemBean>
     */
    List<PurInvoiceItemBean> queryInvoiceItemById(String invoiceId);

    /**
     * 
     * @description:通过Id更新业务表的状态
     * @author: fengzt
     * @createDate: 2015年10月8日
     * @param bean
     * @return:count
     */
    int updateInvoiceById(PurInvoiceBean bean);

    /**
     * 
     * @description:根据合同ID查询发票基本信息
     * @author: fengzt
     * @createDate: 2015年10月8日
     * @param map
     * @return:List<PurInvoiceBean>
     */
    List<PurInvoiceBean> queryInvoiceBaseInfoByContractId(Map<String, Object> map);

    /**
     * 
     * @description:检查同一个站点下发票号是否重复
     * @author: fengzt
     * @createDate: 2015年10月10日
     * @param map
     * @return:List<PurInvoiceBean>
     */
    List<PurInvoiceBean> queryCheckInvoiceNo(Map<String, Object> map);

    /**
     * 
     * @description:查询所有站点
     * @author: fengzt
     * @createDate: 2015年10月13日
     * @return:List<String>
     */
    List<String> queryAllSite();

}
