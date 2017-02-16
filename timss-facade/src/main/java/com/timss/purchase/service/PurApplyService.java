package com.timss.purchase.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvWarehouse;
import com.timss.purchase.bean.PurApply;
import com.timss.purchase.bean.PurApplyItem;
import com.timss.purchase.vo.PurApplyItemVO;
import com.timss.purchase.vo.PurApplyVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurApplyService.java
 * @author: 890166
 * @createDate: 2014-6-20
 * @updateUser: 890166
 * @version: 1.0
 */
public interface PurApplyService {

    /***************************************** query begin ****************************************************/
    /**
     * @description:查询采购申请列表(单独申请)
     * @author: 890166
     * @createDate: 2014-6-26
     * @param userinfo
     * @param purApplyVO
     * @return
     * @throws Exception
     */
    Page<PurApplyVO> queryPurApply(UserInfo userinfo, PurApplyVO purApplyVO) throws Exception;

    /**
     * @description:根据sheetid查询采购申请物资列表
     * @author: 890166
     * @createDate: 2014-6-26
     * @param userInfo
     * @param sheetId
     * @return
     * @throws Exception
     */
    Page<PurApplyItemVO> queryPurApplyItemList(UserInfoScope userInfo, String sheetId, String sendType)
            throws Exception;

    /**
     * @description:根据sheetid查询出采购申请表单详细信息
     * @author: 890166
     * @createDate: 2014-6-23
     * @param sheetId
     * @return
     * @throws Exception
     */
    List<PurApply> queryPurApplyInfoBySheetId(UserInfo userinfo, String sheetId) throws Exception;

    /**
     * @description:查询物资列表
     * @author: 890162
     * @createDate: 2015-9-28
     * @param sheetId
     * @return
     * @throws Exception :
     */
    List<PurApplyItem> queryPurApplyItemListBySheetId(String sheetId);

    /**
     * @description:根据sheetid查询采购申请物资列表 返回LIST
     * @author: 890162
     * @createDate: 2015-9-29
     * @param userInfo
     * @param sheetId
     * @return
     * @throws Exception
     */
    List<PurApplyItemVO> queryPurApplyItemListAsList(UserInfoScope userInfo, String sheetId,
            PurApplyItemVO purApplyItemVOCon) throws Exception;

    /**
     * @description:查询采购申请
     * @author: 890162
     * @createDate: 2015-9-28
     * @param sheetId
     * @return
     * @throws Exception :
     */
    PurApply queryPurApplyBySheetId(String sheetId);

    /**
     * @description:生成采购申请表单数据[refactor]
     * @author: 890162
     * @createDate: 2015-10-26
     * @param UserInfoScope
     * @param type
     * @param sheetId
     * @return
     * @throws Exception
     */
    Map<String, Object> initPurApplyForm(String type, String sheetId) throws Exception;

    /**
     * @description:查询生成的采购单
     * @author: 890166
     * @createDate: 2014-7-7
     * @param appSheetId
     * @return
     * @throws Exception :
     */
    Map<String, Object> queryGenerateOrder(UserInfoScope userInfo, String appSheetId, String itemIds) throws Exception;

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
     * @Title:queryPurApplyWarehouse
     * @Description:查询采购申请仓库细腻
     * @param siteId
     * @return List<InvWarehouse>
     */
    List<InvWarehouse> queryPurApplyWarehouse(String siteId) ;
    
    /***************************************** query end ****************************************************/

    /***************************************** update begin ****************************************************/
    /**
     * @description:更新PurApply信息
     * @author: 890166
     * @createDate: 2014-6-24
     * @param purApply
     * @return
     * @throws Exception
     */
    int updatePurApplyInfo(PurApply purApply) throws Exception;

    /**
     * @description:批量更新采购申请明细是否已经提交商务网状态
     * @author: 890166
     * @createDate: 2014-7-3
     * @param itemIds
     * @return
     * @throws Exception :
     */
    int updatePurApplyItemBusinessStatus(String sheetId, String[] itemIds, int busiStatus, String itemStatus)
            throws Exception;

    /**
     * @description:更新状态
     * @author: 890166
     * @createDate: 2014-8-8
     * @param status
     * @param sheetId
     * @return
     * @throws Exception :
     */
    int updatePurApplyItemStatus(String status, String sheetId, String itemId ,String categoryId) throws Exception;

    /**
     * @description:更新待办人
     * @author: 890162
     * @createDate: 2015-9-22
     * @param sheetId
     * @param transactor
     * @return
     * @throws Exception :
     */
    int updatePurApplyInfoTransactor(String sheetId, String transactor) throws Exception;

    /**
     * @description:更新采购申请是否发送到商务网
     * @author: 890162
     * @createDate: 2015-9-28
     * @param sheetId
     * @param sendToBusiness
     * @return
     * @throws Exception :
     */
    int updatePurApplyInfoIsToBusiness(String sheetId, String sendToBusiness);

    /**
     * @description:流程结束时，更新采购申请状态和待办人
     * @author: 890162
     * @createDate: 2015-10-12
     * @param sheetId
     * @param taskName
     * @return
     * @throws Exception :
     */
    int updatePurApplyToPass(String sheetId, String taskName) throws Exception;

    /***************************************** update end ****************************************************/

    /***************************************** insert begin ****************************************************/
    /**
     * @description:新建PurApply信息
     * @author: 890166
     * @createDate: 2014-6-24
     * @param purApply
     * @return
     * @throws Exception
     */
    int insertPurApplyInfo(PurApply purApply) throws Exception;

    /**
     * @description:批量插入PurApplyItem信息(页面数据直接插入)
     * @author: 890166
     * @createDate: 2014-6-25
     * @param sheetId
     * @param paiList
     * @return
     * @throws Exception
     */
    int insertPurApplyItemWithList(List<PurApplyItem> paiList) throws Exception;

    /***************************************** insert end ****************************************************/

    /***************************************** delete begin ****************************************************/
    /**
     * @description:根据sheetid删除PurApplyItem信息
     * @author: 890166
     * @createDate: 2014-6-24
     * @param sheetId
     * @return
     * @throws Exception :
     */
    int deletePurApplyItemBySheetId(String sheetId) throws Exception;

    /**
     * @description:删除采购申请信息
     * @author: 890166
     * @createDate: 2014-8-12
     * @param sheetId
     * @return
     * @throws Exception :
     */
    boolean deletePurApplyData(String sheetId) throws Exception;

    /***************************************** delete end ****************************************************/
    /**
     * @description:采购申请统一保存方法
     * @author: 890166
     * @createDate: 2014-7-4
     * @param pa
     * @param paiList
     * @param paramMap
     * @return
     * @throws Exception :
     */
    boolean saveOrUpdatePurApply(UserInfoScope userInfo, PurApply pa, List<PurApplyItem> paiList,
            Map<String, Object> paramMap) throws Exception;

    /***************************************** bussiness begin ****************************************************/
    /**
     * @description: 发送数据到商务网接口
     * @author: 890166
     * @createDate: 2014-10-29
     * @return
     * @throws Exception:
     */
    boolean sendDataToBussiness(UserInfoScope userInfo, String sheetId, String[] itemidArr) throws Exception;

    /**
     * @description:定时器同步已发送到商务网数据的物资信息到商务网物资表
     * @author: 890166
     * @createDate: 2014-10-30
     * @throws Exception:
     */
    void syncDataToEamItem(Date syncDate) throws Exception;

    /**
     * @description: 实时同步物资数据到商务网
     * @author: 890166
     * @createDate: 2015-3-25
     * @param itemCodes
     * @param siteId
     * @throws Exception:
     */
    void syncDataToEamItem4RealTime(String[] itemidArr, String siteId) throws Exception;

    /***************************************** bussiness end ****************************************************/
    
    /**
     * @description:查询采购申请的物资是否分类启用
     * @author: 890191
     * @createDate: 2016-8-12
     * @param paiList
     * @return String
     * @throws Exception :
     */
    String queryActiveByPurApply(List<PurApplyItem> paiList) throws Exception;
    
    /**
     * @Title:autoSendDataToBusiness
     * @Description:自动发送商务网数据
     * @param businessId
     * @param taskName
     * @param itemids
     * @return Map<String,String>
     * @throws
     */
    Map<String, String> autoSendDataToBusiness(String businessId,String taskName,String itemids) throws Exception;
    
    /**
     * @Title:revertPurApplyItemToApplyStatus
     * @Description:将制定采购申请下明细状态为完成审批状态退回到提交申请
     * @param sheetId
     * @throws Exception
     * @return int
     * @throws
     */
    int revertPurApplyItemToApplyStatus(String sheetId) throws Exception;
    /**
     * @Title:noItemBeSent
     * @Description:返回是否所有采购申请明细均处于申请状态
     * @param  sheetId
     * @return Boolean
     */
    public Boolean allItemsApplying(String sheetId) ;
    /**
     * @Title:hasItemsBusinessApplying
     * @Description:返回是否采购申请明细处于发送商务网状态
     * @param  sheetId
     * @return Boolean
     */
    public Boolean hasItemsBusinessApplying(String sheetId);
    /**
     * @Title:hasItemApplying
     * @Description:返回是否采购申请明细处于申请状态
     * @param  sheetId
     * @return Boolean
     */
    public Boolean hasItemApplying(String sheetId);
    /**
     * @Title:checkStopable
     * @Description:检查采购申请是否可以终止
     * @param sheetId
     * @return String
     * @throws
     */
    public String checkStopable(String sheetId);
    /**
     * @Title:stopPurApply
     * @Description:终止采购申请
     * @param sheetId 
     * @return boolean
     * @throws
     */
    public boolean stopPurApply(String sheetId) throws Exception ;
    /**
     * @Title:startStopPurApply
     * @Description:开始终止采购申请
     * @param sheetId
     * @param processInstId  
     * @return boolean
     * @throws
     */
    public boolean startStopPurApply(String sheetId,String processInstId)  throws Exception ;
    /**
     * @Title:nullifyStopPurApply
     * @Description:作废终止采购申请
     * @param sheetId
     * @return boolean
     * @throws
     */
    public boolean nullifyStopPurApply(String sheetId)  throws Exception ;
    /**
     * removeStopPurApply
     * @Description:删除终止采购申请待办
     * @param sheetId
     * @param procInstId
     * @return boolean
     * @throws
     */
    public boolean removeStopPurApply(String sheetId,String procInstId)  throws Exception ;
}
