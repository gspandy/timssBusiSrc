package com.timss.workorder.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.spring.annotations.ProcessId;
import org.apache.ibatis.annotations.Param;

import com.timss.workorder.bean.WorkOrder;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;

public interface WorkOrderDao {

    /**
     * @description: 插入工单信息
     * @author: 王中华
     * @createDate: 2014-7-9
     * @param workOrder:
     */
    int insertWorkOrder(WorkOrder workOrder);

    int updateWorkOrder(WorkOrder workOrder);

    /**
     * @description: 更新工单确认缺陷信息
     * @author: 890151
     * @createDate: 2015年11月30日
     * @param workOrder:
     */
    int updateWorkOrderConfirm(WorkOrder workOrder);

    /**
     * @description: 更新工单专工处理信息
     * @author: 890151
     * @createDate: 2015年11月30日
     * @param workOrder:
     */
    int updateWorkOrderExpert(WorkOrder workOrder);

    /**
     * @description: 更新工单策划信息
     * @author: 890151
     * @createDate: 2015年11月30日
     * @param workOrder:
     */
    int updateWorkOrderPlan(WorkOrder workOrder);

    /**
     * @description: 更新工单完工汇报信息
     * @author: 890151
     * @createDate: 2015年11月30日
     * @param workOrder:
     */
    int updateWorkOrderReport(WorkOrder workOrder);

    /**
     * @description: 更新湛江风电工单审批过程中的信息
     * @author: 890151
     * @createDate: 2015年12月16日
     * @param workOrder:
     */
    int updateWoAuditInfoZJW(WorkOrder workOrder);

    /**
     * @description: 清除工单开始结束时间
     * @author: 890151
     * @createDate: 2015年12月16日
     * @param workOrder:
     */
    int cleanWoReportInfoZJW(WorkOrder workOrder);

    /**
     * @description:给工单添加工作票ID信息
     * @author: 王中华
     * @createDate: 2014-7-24
     * @param woId
     * @param ptwId:
     */
    int updateWOAddPTWId(@Param("woId") int woId, @Param("ptwId") int ptwId);

    List<WorkOrder> queryAllWO(Page<WorkOrder> page);

    @RowFilter(flowIdColumn = "WO_CODE")
    List<WorkOrder> queryFilterAllWO(Page<WorkOrder> page);

    List<WorkOrder> queryAllItWO(Page<WorkOrder> page);

    /**
     * @description:查询工单基本数据（电厂）
     * @author: 王中华
     * @createDate: 2014-9-9
     * @param id
     * @return:
     */
    WorkOrder queryWOById(int id);

    /**
     * @description:查询工单基本数据（ITC）
     * @author: 王中华
     * @createDate: 2014-9-9
     * @param id
     * @return:
     */
    WorkOrder queryItWOById(int woId);

    int getNextWOId();

    /**
     * @description: 修改工单处理方式信息
     * @author: 王中华
     * @createDate: 2014-7-9
     * @param updateParams: 需要修改的参数
     */
    int updateWOHandlerStyle(Map<String, Object> updateParams);

    /**
     * @description: 修改工单的状态
     * @author: 王中华
     * @createDate: 2014-7-10
     * @param parmas:
     */
    int updateWOStatus(Map<String, Object> parmas);

    /**
     * @description:工作策划阶段，修改工单信息（是否走工作票，作业方案ID）
     * @author: 王中华
     * @createDate: 2014-7-11
     * @param parmas:
     */
    int updateWOOnPlan(Map<String, Object> parmas);

    /**
     * @description:工单验收阶段，修改工单信息（批准停机时间、损失电量）
     * @author: 王中华
     * @createDate: 2014-7-12
     * @param parmas:
     */
    int updateWOOnAcceptance(Map<String, Object> parmas);

    /**
     * @description:工单完工汇报阶段，修改工单信息（实际开工时间，实际完工时间，是否有遗留问题，完工汇报内容）
     * @author: 王中华
     * @createDate: 2014-7-12
     * @param parmas:
     */
    int updateWOOnReport(Map<String, Object> parmas);

    /**
     * @description:审批环节中添加 审批人的信息
     * @author: 王中华
     * @createDate: 2014-8-7
     * @param parmas:
     */
    int updateOperUserById(Map<String, String> parmas);

    /**
     * @description:更新当前处理人信息（工号列表和姓名列表，“,”分隔）
     * @author: 王中华
     * @createDate: 2014-9-5
     * @param parmas:
     */
    int updateCurrHandUserById(Map<String, String> parmas);

    /**
     * @description: 根据工单编号查询工单基本信息
     * @author: 王中华
     * @createDate: 2014-7-24
     * @param woCode
     * @return:
     */
    WorkOrder queryWOBaseInfoByWOCode(@Param("woCode") String woCode, @Param("siteid") String siteId);

    /**
     * @description: 删除工单（标记删除）
     * @author: 王中华
     * @createDate: 2014-7-31
     * @param woId:
     */
    int deleteWorkOrder(@Param("id") int woId);

    /**
     * @description: 根据工单编号和站点删除工单
     * @author: 王中华
     * @createDate: 2014-11-21
     * @param woCode
     * @param siteid:
     */
    void deleteWorkOrderByWoCode(@Param("woCode") String woCode, @Param("siteid") String siteid);

    /**
     * @description:终止工单
     * @author: 王中华
     * @createDate: 2014-8-13:
     */
    int updateStopWO(Map<String, Object> parmas);

    /**
     * @description:修改工单的当前风速
     * @author: 王中华
     * @createDate: 2014-8-15
     * @param windSpeedMap:
     */
    int updateWOCurrWindSpeed(@Param("id") String woId, @Param("windSpeed") String windSpeed);

    /**
     * @description:IT工单中，添加协助人员信息
     * @author: 王中华
     * @createDate: 2014-9-11
     * @param parmas:
     */
    int updatePartnerInfo(Map<String, Object> parmas);

    /**
     * @description: IT工单中，添加延时信息（上门延时，完工延时）
     * @author: 王中华
     * @createDate: 2014-9-11
     * @param parmas:
     */
    int updateDelayInfo(Map<String, Object> parmas);

    /**
     * @description: IT工单中，添加工单回访结果信息
     * @author: 王中华
     * @createDate: 2014-9-11
     * @param parmas:
     */
    int updateWOOnFeedback(Map<String, Object> parmas);

    /**
     * @description:处理中状态时，选择释放，则将之前策划的内容全部清掉
     * @author: 王中华
     * @createDate: 2014-9-12
     * @param id:
     */
    int updateClearPartnerInfo(int id);

    /**
     * @description:修改工单的流程ID
     * @author: 王中华
     * @createDate: 2014-9-19
     * @param workflowId:
     */
    void updateWorkflowId(@Param("workflowId") String workflowId, @Param("id") String woId);

    /**
     * @description: 开工时间
     * @author: 王中华
     * @createDate: 2014-10-28
     * @param woId
     * @param beginTime:
     */
    void updateWoBeginTime(@Param("woId") String woId, @Param("beginTime") Date beginTime);

    /**
     * @description: 派单时间
     * @author: 王中华
     * @createDate: 2014-10-28
     * @param woId
     * @param sendWoTime:
     */
    void updateSendWoTime(@Param("woId") String woId, @Param("sendWoTime") Date sendWoTime);

    /**
     * @description:查询所有工单（不分站点），有优先级，处于“workPlan”状态 （用于定时任务）
     * @author: 王中华
     * @createDate: 2014-10-31
     * @return:
     */
    List<WorkOrder> queryAllWoNoSiteId();

    /**
     * @description:查询所有排序的IT工单
     * @author: 王中华
     * @createDate: 2014-11-24
     * @param page
     * @return:
     */
    List<WorkOrder> queryAllSortedItWO(Page<WorkOrder> page);

    /**
     * @description:查询所有排序的IT工单(行权限过滤)
     * @author: 王中华
     * @createDate: 2014-11-24
     * @param page
     * @return:
     */
    @RowFilter(flowIdColumn = "WO_CODE")
    List<WorkOrder> queryFilterAllSortedItWO(Page<WorkOrder> page);

    /**
     * @description: 工单自动释放时，修改自动释放记录字段
     * @author: 王中华
     * @createDate: 2014-11-20
     * @param valueOf:
     */
    void updateReleaseWo(@Param("woId") String woId);

    /**
     * @description: 查询某段时间内新建的工单记录
     * @author: 王中华
     * @createDate: 2014-11-25
     * @param beginTime
     * @param endTime
     * @param siteid
     * @return:
     */
    ArrayList<WorkOrder> queryNewWoList(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
            @Param("siteid") String siteid);

    /**
     * @description: 查询某个用户上手的工单数量
     * @author: 王中华
     * @createDate: 2015-1-14
     * @param userId
     * @param siteId
     * @return:
     */
    int getUserWoSum(@Param("userId") String userId, @Param("siteid") String siteId);

    /**
     * @description:查询与缺陷单相关的工单记录
     * @author: 王中华
     * @createDate: 2015-6-3
     * @param woQxId
     * @return:
     */
    List<WorkOrder> queryAllRelateWoOfQx(Page<WorkOrder> page);

    /**
     * @description:查询工单列表 包含优先级
     * @author: 890151
     * @createDate: 2015年12月7日
     * @param
     * @return:
     */
    List<WorkOrder> queryAllWoWithPriority(Page<WorkOrder> page);

    /**
     * @description:获取当前站点的某个电厂今天的工单数
     * @author: 王中华
     * @createDate: 2016-5-20
     * @param siteId 站点
     * @param plantCode 电厂
     * @return:
     */
    int getTodayWoSumByCondition(@Param("siteid")String siteid, @Param("plantCode")String plantCode);

    /**
     * @description:查询延迟的工单（可能不同站点，添加不同的过滤条件）
     * @author: 王中华
     * @createDate: 2016-6-22 
     * @param params 
     * @return:
     */
    List<WorkOrder> queryAllDelayWoNoSiteId(Map<String, String> params);

    /**
     * @description:查询“值长启动工单”状态的单
     * @author: 王中华
     * @createDate: 2017-1-20
     * @param siteid
     * @return:
     */
    List<WorkOrder> queryWillDutyRestartWo(String siteid);

}
