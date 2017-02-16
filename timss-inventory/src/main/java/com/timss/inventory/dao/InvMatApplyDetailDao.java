package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import com.timss.asset.bean.AssetBean;
import com.timss.inventory.bean.InvMatApplyDetail;
import com.timss.inventory.vo.InvMatApplyDetailVO;
import com.timss.inventory.vo.InvMatApplyToWorkOrder;
import com.timss.inventory.vo.InvMatRecipientsDetailVO;
import com.timss.inventory.vo.InvMatTranRecVO;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatApplyDetailDao.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatApplyDetailDao {

    /**
     * @description:查询列表信息
     * @author: 890166
     * @createDate: 2014-7-28
     * @param page
     * @return:
     */
    List<InvMatApplyDetailVO> queryMatApplyDetailList(Page<?> page);

    List<InvMatApplyDetailVO> queryMatApplyDetailCSList(Page<?> page);

    /**
     * @description:弹出窗口中列表数据
     * @author: 890166
     * @createDate: 2014-7-28
     * @param page
     * @return:
     */
    List<InvMatApplyDetailVO> queryConsumingList(Page<?> page);

    /**
     * @description:根据imaid删除明细表信息
     * @author: 890166
     * @createDate: 2014-7-28
     * @param imaid
     * @return:
     */
    int deleteInvMatApplyDetailByImaId(String imaid);

    /**
     * @description:插入详细信息
     * @author: 890166
     * @createDate: 2014-7-28
     * @param imad
     * @return:
     */
    int insertInvMatApplyDetail(InvMatApplyDetail imad);

    /**
     * @description: 更新
     * @author: 890166
     * @createDate: 2014-10-11
     * @param imad
     * @return:
     */
    int updateInvMatApplyDetail(InvMatApplyDetail imad);

    /**
     * @description:通过列表生成物资领料
     * @author: 890166
     * @createDate: 2014-8-25
     * @param codes
     * @return:
     */
    List<InvMatApplyDetailVO> queryItemInfoToMatApply(Map<String, Object> map);

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
    Double queryApplyBudget(Map<String, Object> map) throws Exception;

    /**
     * @description:通过woid查询领用申请信息
     * @author: 890166
     * @createDate: 2014-9-30
     * @param page
     * @return
     * @throws Exception:
     */
    List<InvMatApplyToWorkOrder> queryMatApplyByWoId(Page<?> page) throws Exception;
    
    /**
     * @description:通过imaid查询列表信息
     * @author: 890191
     * @createDate: 2015-7-7
     * @param imaid
     * @return
     */
    List<InvMatApplyDetailVO> queryApplyDetailList(String imaid);
    
    /**
     * @description:查询领料单某个物资详情
     * @author: 890151
     * @createDate: 2016-7-26
     * @param imadId
     * @return
     */
	InvMatApplyDetailVO queryInvMatApplyDetailByImadId(String imadId);

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
    
}
