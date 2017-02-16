package com.timss.inventory.service;

import java.util.List;
import java.util.Map;

import com.timss.asset.bean.AssetBean;
import com.timss.inventory.vo.InvMatApplyDetailVO;
import com.timss.inventory.vo.InvMatRecipientsDetailVO;
import com.timss.inventory.vo.InvMatTranRecVO;
import com.timss.purchase.vo.PurOrderVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatApplyDetailService.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatApplyDetailService {

    /**
     * @description:查询列表信息
     * @author: 890166
     * @createDate: 2014-7-28
     * @param userInfo
     * @param imaid
     * @return
     * @throws Exception:
     */
    Page<InvMatApplyDetailVO> queryMatApplyDetailList(UserInfoScope userInfo, String imaid) throws Exception;

    Page<InvMatApplyDetailVO> queryMatApplyDetailCSList(UserInfoScope userInfo, String imaid, String embed)
            throws Exception;

    /**
     * @description:弹出窗口中列表数据
     * @author: 890166
     * @createDate: 2014-7-28
     * @param userInfo
     * @param imad
     * @return
     * @throws Exception:
     */
    Page<InvMatApplyDetailVO> queryConsumingList(UserInfoScope userInfo, InvMatApplyDetailVO imad) throws Exception;

    /**
     * @description:通过列表生成物资领料
     * @author: 890166
     * @createDate: 2014-8-26
     * @param codes
     * @return
     * @throws Exception:
     */
    List<InvMatApplyDetailVO> queryItemInfoToMatApply(String codes) throws Exception;

    /**
     * @description:查询今年内至今为止同部门的人员所有申请单总金额
     * @author: 890166
     * @createDate: 2014-9-18
     * @param deptId
     * @param nowMonth
     * @param siteId
     * @return
     * @throws Exception:
     */
    Double queryApplyBudget(String userIds, String nowMonth, String siteId) throws Exception;

    /**
     * @description:更新最新库存数量情况
     * @author: 890166
     * @createDate: 2014-10-28
     * @param imaid
     * @throws Exception:
     */
    int updateNewStockQtyByImaid(UserInfoScope userInfo, String imaid) throws Exception;
  
    /**
     * @description:查询领料单某个物资详情
     * @author: 890151
     * @createDate: 2016-7-26
     * @param imadId
     * @return
     */
    InvMatApplyDetailVO queryInvMatApplyDetailByImadId(String imadId) throws Exception;

    /**
     * @description:查询领料单详情某个物资的发料总数
     * @author: 890151
     * @createDate: 2016-7-26
     * @param imadId
     * @return
     */
    int queryMatApplyDetailSendTotal(String imadId) throws Exception;

    /**
     * @description:查询领料单详情某个物资相关联的资产
     * @author: 890151
     * @createDate: 2016-7-26
     * @param imadId
     * @return
     */
    List<AssetBean> queryRelateAssetByImadId(String imadId) throws Exception;
    
    /**
     * @description:查询领料单详情某个物资相关联的发料单
     * @author: 890151
     * @createDate: 2016-7-26
     * @param imadId
     * @return
     */
    List<InvMatRecipientsDetailVO> queryRelateRecipientsByImadId(String imadId) throws Exception;
 
    /**
     * @description:查询领料单详情某个物资相关联的交易流水信息
     * @author: 890151
     * @createDate: 2016-7-26
     * @param imadId
     * @return
     */
    List<InvMatTranRecVO> queryRelateTranRecByImadId(String imadId) throws Exception;
    
    /**
     * @description:查询合同相关信息
     * @author: 890151
     * @createDate: 2016-7-26
     * @param imadId
     * @return
     */
    Map<String,Object> queryPoInfoByPoId(UserInfoScope userInfo, String poId) throws Exception;
}
