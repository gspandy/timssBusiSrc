package com.timss.workorder.service;

import java.util.List;
import java.util.Map;

import com.timss.workorder.bean.WorkOrder;
import com.yudean.itc.dto.Page;

/***
 * 工单 Service 操作
 * 
 * @author 王中华 2014-6-11
 */
public interface WorkOrderService {
    /**
     * 添加 工单
     * 
     * @param WODataMap
     * @throws Exception
     */
    Map<String, Object> insertWorkOrder(Map<String, String> WODataMap) throws Exception;

    /***
     * 更新 工单
     * 
     * @param workOrder
     * @throws Exception
     */
    Map<String, String> updateWorkOrder(Map<String, String> addWODataMap) throws Exception;

    /**
     * @description: 给工单添加工作票ID信息
     * @author: 王中华
     * @createDate: 2014-7-24
     * @param woId
     * @param ptwId
     * @throws Exception:
     */
    void updateWOAddPTWId(int woId, int ptwId);

    /**
     * @description: 审批环节添加审批人信息进工单表
     * @author: 王中华
     * @createDate: 2014-8-7
     * @param parmas:
     */
    void updateOperUserById(Map<String, String> parmas);

    /***
     * 查询所有工单
     * 
     * @throws Exception
     */
    Page<WorkOrder> queryAllWO(Page<WorkOrder> page) throws Exception;

    /**
     * 根据ID 查询工单
     * 
     * @param id
     * @return
     */
    Map<String, Object> queryWOById(int id);

    /**
     * @description:根据ID 查询工单(ITC)
     * @author: 王中华
     * @createDate: 2014-9-9
     * @param woId
     * @return:
     */
    Map<String, Object> queryItWOById(int woId);

    /**
     * @description:根据工单编号查询工单基本信息
     * @author: 王中华
     * @createDate: 2014-7-24
     * @param woCode
     * @return:
     */
    Map<String, Object> queryWOBaseInfoByWOCode(String woCode, String siteId);

    /**
     * @description: 获取下一个插入的工单ID
     * @author: 王中华
     * @createDate: 2014-6-25
     * @return:
     */
    int getNextWOId();

    /**
     * @description:存储工单的基本信息（form表单信息）
     * @author: 王中华
     * @createDate: 2014-7-9
     * @param addWODataMap:
     */
    Map<String, Object> saveWorkOrder(Map<String, String> addWODataMap) throws Exception;

    /**
     * @description:修改工单状态
     * @author: 王中华
     * @createDate: 2014-7-10
     * @param parmas:
     */
    void updateWOStatus(Map<String, Object> parmas);

    /**
     * @description:修改工单的处理方式（班长、助理、场长）
     * @author: 王中华
     * @createDate: 2014-7-10
     * @param handStyleMap:
     */
    void updateWOHandlerStyle(Map<String, Object> handStyleMap);

    /**
     * @description:工作策划阶段，修改工单信息（是否走工作票，作业方案ID）
     * @author: 王中华
     * @createDate: 2014-7-11
     * @param parmas:
     */
    void updateWOOnPlan(Map<String, Object> parmas);

    /**
     * @description:工单验收阶段，修改工单信息（批准停机时间、损失电量）
     * @author: 王中华
     * @createDate: 2014-7-12
     * @param parmas:
     */
    void updateWOOnAcceptance(Map<String, Object> parmas);

    /**
     * @description:工单完工汇报阶段，修改工单信息（实际开工时间，实际完工时间，是否有遗留问题，完工汇报内容）
     * @author: 王中华
     * @createDate: 2014-7-12
     * @param parmas:
     */
    void updateWOOnReport(Map<String, Object> parmas);

    /**
     * @description:删除工单（标记删除）
     * @author: 王中华
     * @createDate: 2014-7-31
     * @param woId:
     */
    void deleteWorkOrder(int woId);

    /**
     * @description:根据工单编号和站点，删除工单
     * @author: 王中华
     * @createDate: 2014-11-21
     * @param woCode
     * @param siteid:
     */
    void deleteWorkOrderByWoCode(String woCode, String siteid);

    /**
     * @description:工作票流程走完后，结束工单流程中的节点，走到下一节点
     * @author: 王中华
     * @createDate: 2014-7-24
     * @param woId
     * @param ptwId:
     */
    void inPtwToNextStep(int woId, String userId);

    /**
     * @description:终止工单
     * @author: 王中华
     * @createDate: 2014-8-13
     * @param woId:
     */
    void stopWorkOrder(Map<String, Object> parmas);

    /**
     * @description: 修改工单的流程ID
     * @author: 王中华
     * @createDate: 2014-9-19
     * @param string:
     */
    void updateWorkflowId(String string, String woId);

    /**
     * @description:判断登录用户是否是客服（或者类似角色）
     * @author: 王中华
     * @createDate: 2014-9-23
     * @param siteId
     * @param loginUerId
     * @return:
     */
    boolean loginUserIsCusSer();

    void cycMtpObjToWo(String mtpId, String todoId) throws Exception;

    Map<String, String> cancelCommitWO(String woId);

    Map<String, String> saveWOOnPlan() throws Exception;

    /**
     * @description: 作废工单
     * @author: 王中华
     * @createDate: 2014-12-4
     * @param woId:
     */
    void obsoleteWorkOrder(String woIdString);

    /**
     * @description: 回退到创建人之后，再提交
     * @author: 王中华
     * @createDate: 2014-12-5
     * @param addWODataMap:
     */
    void rollbackCommitWo(Map<String, String> addWODataMap) throws Exception;

    /**
     * @description: 更新工单的基本信息
     * @author: 王中华
     * @createDate: 2014-12-8
     * @return:
     */
    Map<String, String> updateWoBaseInfo() throws Exception;

    /**
     * @description: 获取某个工程师手上的工单数量
     * @author: 王中华
     * @createDate: 2015-1-14
     * @param userId
     * @param siteId
     * @return:
     */
    int getUserWoSum(String userId, String siteId);

    /**
     * @description: 将工单回退到某个阶段
     * @author: 王中华
     * @createDate: 2015-1-14
     * @param woId
     * @param woStepFlag 某阶段的Id
     * @return:
     */
    Map<String, String> wobackToSomeStep(String woId, String woStepFlag) throws Exception;

    /**
     * @description:查询与缺陷单相关的工单记录
     * @author: 王中华
     * @createDate: 2015-6-3
     * @param woQxId
     * @return:
     */
    Page<WorkOrder> queryAllRelateWoOfQx(Page<WorkOrder> page);

    /**
     * @description: 查询所有有延迟长度的工单（各个站点可能筛选条件不同）
     * @author: 王中华
     * @createDate: 2016-6-22
     * @return:
     */
    List<WorkOrder> queryAllDelayWoNoSiteId();

    /**
     * @description:获取延期之后重新启动的下一个节点KEY
     * @author: 王中华
     * @createDate: 2016-6-22
     * @return:
     */
    String getDelayRestartNextTaskKey();

    /**
     * @description:更新当前处理人
     * @author: 王中华
     * @createDate: 2016-6-22
     * @param parmas:
     */
    void updateCurrHandUserById(Map<String, String> parmas);

}
