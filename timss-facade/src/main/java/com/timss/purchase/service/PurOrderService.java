package com.timss.purchase.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.timss.purchase.bean.PurOrder;
import com.timss.purchase.bean.PurOrderItem;
import com.timss.purchase.bean.PurPolicy;
import com.timss.purchase.bean.PurPolicyTemp;
import com.timss.purchase.bean.PurProcessMapping;
import com.timss.purchase.vo.PurApplyOrderItemVO;
import com.timss.purchase.vo.PurOrderItemVO;
import com.timss.purchase.vo.PurOrderVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurOrderService.java
 * @author: 890166
 * @createDate: 2014-6-30
 * @updateUser: 890166
 * @version: 1.0
 */
public interface PurOrderService {

    /**
     * @description:查询采购单列表
     * @author: 890166
     * @createDate: 2014-6-30
     * @param userinfo
     * @param purOrderVO
     * @return
     * @throws Exception
     */
    Page<PurOrderVO> queryPurOrder(UserInfo userinfo, PurOrderVO purOrderVO) throws Exception;

    /**
     * @description:查询详细信息页面的时候表单信息获取
     * @author: 890166
     * @createDate: 2014-6-30
     * @param sheetId
     * @return
     * @throws Exception
     */
    List<PurOrderVO> queryPurOrderInfoBySheetId(String sheetId) throws Exception;

    /**
     * @description:弹出框内的申请物资信息列表
     * @author: 890166
     * @createDate: 2014-6-30
     * @modifyDate:2016-11-16
     * @modifyUser:890162
     * @param userInfo
     * @param purOrderItemVO
     * @param includeitems
     * @return
     * @throws Exception
     */
    Page<PurOrderItemVO> queryItemByEntity(UserInfoScope userInfo, PurOrderItemVO purOrderItemVO, List<PurOrderItemVO> includeitems) throws Exception;

    /**
     * @description:弹出框内的申请物资信息列表
     * @author: 890166
     * @createDate: 2014-7-1
     * @param list
     * @return:
     */
    List<PurOrderItemVO> queryItemByList(List<String> list) throws Exception;

    /**
     * @description:详细表单中列表
     * @author: 890166
     * @createDate: 2014-7-1
     * @param userInfo
     * @param sheetId
     * @return
     * @throws Exception
     */
    Page<PurOrderItemVO> queryPurOrderItemList(UserInfoScope userInfo, String sheetId) throws Exception;

    /**
     * @description:物资合并查询
     * @author: 890166
     * @createDate: 2014-7-1
     * @param userInfo
     * @param sheetId
     * @return
     * @throws Exception
     */
    Page<PurOrderItemVO> queryPurOrderItemListExce(UserInfoScope userInfo, String sheetId) throws Exception;

    /**
     * @description:根据sheetid查询PurOrder
     * @author: 890166
     * @createDate: 2014-7-1
     * @param sheetId
     * @return
     * @throws Exception
     */
    PurOrder queryPurOrderBySheetId(String sheetId) throws Exception;

    /**
     * @description:根据sheetid查询PurOrder(外部接口使用)
     * @author: 890166
     * @createDate: 2014-8-26
     * @param paramIds
     * @return
     * @throws Exception:
     */
    List<PurOrder> queryPurOrderBySheetIds(String paramIds) throws Exception;

    /**
     * @description:更新PurOrder信息
     * @author: 890166
     * @createDate: 2014-7-1
     * @param po
     * @return
     * @throws Exception
     */
    int updatePurOrderInfo(PurOrder po) throws Exception;

    /**
     * @description:插入PurOrder信息
     * @author: 890166
     * @createDate: 2014-7-1
     * @param po
     * @return
     * @throws Exception
     */
    int insertPurOrderInfo(PurOrder po) throws Exception;

    /**
     * @description:调用插入PurOrderItem的存储过程
     * @author: 890166
     * @createDate: 2014-7-1
     * @param list
     * @throws Exception
     */
    void callProcPurOrderItemInsert(Map<String, Object> params) throws Exception;

    /**
     * @description:通过sheetid找到PurOrderItem
     * @author: 890166
     * @createDate: 2014-7-1
     * @param sheetId
     * @return
     * @throws Exception
     */
    List<String> queryPurOrderItemExists(String sheetId) throws Exception;

    /**
     * @description:调用删除PurOrderItem的存储过程
     * @author: 890166
     * @createDate: 2014-7-1
     * @param list
     * @throws Exception
     */
    void callProcPurOrderItemDelete(Map<String, Object> params) throws Exception;

    /**
     * @description:新增流程映射记录
     * @author: 890166
     * @createDate: 2014-7-4
     * @param sheetId
     * @param siteId
     * @param modelType
     * @return
     * @throws Exception
     */
    int insertPurProcessMapping(PurProcessMapping ppm) throws Exception;

    /**
     * @description:更新流程映射记录
     * @author: 890166
     * @createDate: 2014-7-4
     * @param sheetId
     * @param siteId
     * @param modelType
     * @return
     * @throws Exception :
     */
    int updatePurProcessMapping(PurProcessMapping ppm) throws Exception;

    /**
     * @description:采购单统一保存方法
     * @author: 890166
     * @createDate: 2014-7-7
     * @return
     * @throws Exception
     */
    boolean saveOrUpdatePurOrder(UserInfoScope userInfo, PurOrder po, List<PurOrderItem> poiList,
            Map<String, Object> paramMap) throws Exception;

    /**
     * @description: 通过sheetNo和站点id找到申请表id
     * @author: 890166
     * @createDate: 2014-9-23
     * @param sheetNo
     * @param siteId
     * @return
     * @throws Exception:
     */
    String querySheetIdByFlowNo(String sheetNo, String siteId) throws Exception;

    /**
     * @description: 通过时间段查询采购总额
     * @author: 890166
     * @createDate: 2015-2-4
     * @param beginDate
     * @param endDate
     * @return
     * @throws Exception
     */
    String queryPurPriceTotal(String beginDate, String endDate, String siteId) throws Exception;

    /**
     * @description: 通过sheetid查询flowno
     * @author: 890166
     * @createDate: 2015-2-26
     * @param sheetId
     * @param siteId
     * @return
     * @throws Exception:
     */
    String queryFlowNoBySheetId(String sheetId, String siteId) throws Exception;

    /**
     * @description: 处理商务网传送过来的数据
     * @author: 890166
     * @createDate: 2015-3-17
     * @param date
     * @throws Exception:
     */
    void processACDataFormBusi(Date date) throws Exception;

    /**
     * @description: 保存或更新采购合同明细
     * @author: 890162
     * @createDate: 2015-9-18
     * @param date
     * @throws Exception:
     */
    void saveOrUpdatePurOrderItem(PurOrderItem poItem) throws Exception;

    /**
     * @description:标准合同条款查询
     * @author: 890162
     * @createDate: 2015-9-23
     * @param userInfo
     * @return
     * @throws Exception
     */
    Page<PurPolicyTemp> queryPurPolicyTemp(UserInfoScope userInfo) throws Exception;

    /**
     * @description:标准合同条款查询得到完整列表
     * @author: 890162
     * @createDate: 2015-9-24
     * @param userInfo
     * @return
     * @throws Exception
     */
    List<PurPolicyTemp> queryPurPolicyTempList(UserInfoScope userInfo) throws Exception;

    /**
     * @description:合同条款查询得到列表
     * @author: 890162
     * @createDate: 2015-9-24
     * @param userInfo
     * @return
     * @throws Exception
     */
    List<PurPolicy> queryPurPolicyList(PurPolicy purPolicy) throws Exception;

    /**
     * @description:标准合同条款查询返回单个
     * @author: 890162
     * @createDate: 2015-9-23
     * @param id
     * @return
     * @throws Exception
     */
    PurPolicyTemp queryPurPolicyTempById(String id) throws Exception;

    /**
     * @description: 保存或更新采购合同条款
     * @author: 890162
     * @createDate: 2015-9-23
     * @param date
     * @throws Exception:
     */
    Map<String, Object> saveOrUpdatePurPolicyTemp(PurPolicyTemp ppt) throws Exception;

    /**
     * @description: 删除采购合同条款
     * @author: 890162
     * @createDate: 2015-9-23
     * @param date
     * @throws Exception:
     */
    Map<String, Object> deletePurPolicyTemp(UserInfoScope userInfo) throws Exception;

    /**
     * @description: 查询采购合同条款
     * @author: 890162
     * @createDate: 2015-9-29
     * @param date
     * @throws Exception:
     */
    List<PurOrderItemVO> queryPurOrderItemListExceAsList(UserInfoScope userInfo, String sheetId) throws Exception;

    /**
     * @description: 终止合同
     * @author: 890162
     * @createDate: 2015-9-29
     * @param date
     * @throws Exception:
     */
    int stopPurOrder(String sheetId) throws Exception;

    /**
     * @description:更新待办人
     * @author: 890162
     * @createDate: 2015-10-19
     * @param sheetId
     * @param transactor
     * @return int
     * @throws Exception :
     */
    int updatePurOrderTransactor(String sheetId, String transactor) throws Exception;

    /**
     * @description:生成采购合同表单数据[refactor]
     * @author: 890162
     * @createDate: 2015-10-23
     * @param UserInfoScope
     * @param type
     * @param sheetId
     * @return
     * @throws Exception
     */
    public Map<String, Object> initPurOrderForm(String type, String sheetId) throws Exception;

    /**
     * @description:生成采购合同的合同条款[refactor]
     * @author: 890162
     * @createDate: 2015-10-23
     * @param UserInfoScope
     * @param sheetId
     * @return
     * @throws Exception
     */
    List<PurPolicy> generatePurPolicyListBySheetId(UserInfoScope userInfo, String sheetId) throws Exception;

    /**
     * @description:已执行采购物资列表
     * @author: user
     * @createDate: 2016-1-22
     * @param userInfo
     * @param sheetId
     * @return:
     */
    Page<PurApplyOrderItemVO> queryPurApplyOrderItemList(UserInfoScope userInfo, String sheetId);
    
    /**
     * @description:已执行采购物资列表(不分页)
     * @author: user
     * @createDate: 2016-1-22
     * @param sheetId  采购合同
     * @param siteId   站点id
     * @return:
     */
    List<PurOrderItemVO> queryPurApplyOrderItemList(String sheetId,String siteId);


    /**
     * @description:标准合同条款操作
     * @author: yuanzh
     * @createDate: 2016-2-3
     * @param operType
     * @return:
     */
    Map<String, Object> purPolicyTempOperation(String operType);

    /**
     * @description:查询合同的执行情况
     * @author: 王中华
     * @createDate: 2016-3-22
     * @param userInfoScope
     * @param purOrderItemVO
     * @return:
     */
    Page<PurOrderItemVO> queryItemDoingStatus(UserInfoScope userInfoScope,String sheetNo);
    
    /**
     * @description:查询本年度合同（审批通过）下各专业物资采购金额总和 
     * @author: 890151
     * @createDate: 2016-04-01
     * @param userInfoScope
     * @return:
     */
    public List<Map<String, BigDecimal>> queryMajorPurchase(UserInfoScope userInfo);
    
    /**
     * @description:当前站点库存报账金额统计（分已报账和未报账）
     * @author: 890151
     * @createDate: 2016-10-26
     * @param userInfoScope
     * @param wareHouseId 仓库ID，不限传入null
     * @return:
     */
    public Map<String, BigDecimal> reimbursedMoneyStatistic(UserInfoScope userInfo, String wareHouseId) throws Exception;
    
    /**
     * @Title: isSpNoExisted
     * @Description: 判断当前站点下自定义编号是否存在,排除统一
     * @param spNo
     * @param sheetId
     * @return
     */
    boolean isSpNoExisted(String spNo,String sheetId);
}
