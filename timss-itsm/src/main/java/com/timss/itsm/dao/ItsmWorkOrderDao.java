package com.timss.itsm.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.itsm.bean.ItsmWorkOrder;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;

public interface ItsmWorkOrderDao {

	/**
	 * @description: 插入工单信息
	 * @author: 王中华
	 * @createDate: 2014-7-9
	 * @param workOrder:
	 */
	int insertWorkOrder(ItsmWorkOrder workOrder);
	
	int updateWorkOrder(ItsmWorkOrder workOrder);
	
	int updateInitWorkOrder(@Param("workOrder")ItsmWorkOrder workOrder,@Param("params") String[] params);
	/**
	 * @description:给工单添加工作票ID信息
	 * @author: 王中华
	 * @createDate: 2014-7-24
	 * @param woId
	 * @param ptwId:
	 */
	int updateWOAddPTWId(@Param("woId") String woId, @Param("ptwId") int ptwId);
	
	List<ItsmWorkOrder> queryAllWO(Page<ItsmWorkOrder> page);
	
	@RowFilter(flowIdColumn="WO_CODE")
	List<ItsmWorkOrder> queryFilterAllWO(Page<ItsmWorkOrder> page);
	
	List<ItsmWorkOrder> queryAllItWO(Page<ItsmWorkOrder> page);
	
	/**
	 * @description:查询工单基本数据（电厂）
	 * @author: 王中华
	 * @createDate: 2014-9-9
	 * @param id
	 * @return:
	 */
	ItsmWorkOrder queryWOById(String id);
	
	/**
	 * @description:根据流程实例ID查询工单基本数据
	 * @author: 王中华
	 * @createDate: 2016-8-15
	 * @param processInstId
	 * @return:
	 */
	ItsmWorkOrder queryWOByProcessInstId(@Param( "processInstId" )String processInstId,@Param("siteid")String siteid);
	/**
	 * @description:查询工单基本数据（ITC）
	 * @author: 王中华
	 * @createDate: 2014-9-9
	 * @param id
	 * @return:
	 */
	ItsmWorkOrder queryItWOById(String woId);
	
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
	ItsmWorkOrder queryWOBaseInfoByWOCode(@Param("woCode") String woCode,@Param("siteid") String siteId);
	
	
	/**
	 * @description: 删除工单（标记删除）
	 * @author: 王中华
	 * @createDate: 2014-7-31
	 * @param woId:
	 */
	int deleteWorkOrder(@Param("id") String woId);
	
	/**
	 * @description:  根据工单编号和站点删除工单
	 * @author: 王中华
	 * @createDate: 2014-11-21
	 * @param woCode
	 * @param siteid:
	 */
	void deleteWorkOrderByWoCode(@Param("woCode")String woCode, @Param("siteid")String siteid);
	
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
	int updateWOCurrWindSpeed(@Param("id") String woId,@Param("windSpeed") String windSpeed);

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
	int updateClearPartnerInfo(String id);

	/**
	 * @description:修改工单的流程ID
	 * @author: 王中华
	 * @createDate: 2014-9-19
	 * @param workflowId:
	 */
	void updateWorkflowId(@Param("workflowId")String workflowId,@Param("id")String woId);

	/**
	 * @description: 开工时间
	 * @author: 王中华
	 * @createDate: 2014-10-28
	 * @param woId
	 * @param beginTime:
	 */
	void updateWoBeginTime(@Param("woId")String woId,@Param("beginTime") Date beginTime,@Param("resopndLen") int resopndLen);
	
	/**
	 * @description: 派单时间 
	 * @author: 王中华
	 * @createDate: 2014-10-28
	 * @param woId
	 * @param sendWoTime:
	 */
	void updateSendWoTime(@Param("woId")String woId,@Param("sendWoTime") Date sendWoTime);

	/**
	 * @description:查询所有工单（不分站点），有优先级，处于“workPlan”状态 （用于定时任务）
	 * @author: 王中华
	 * @createDate: 2014-10-31
	 * @return:
	 */
	List<ItsmWorkOrder> queryAllWoNoSiteId();

	/**
	 * @description:查询所有排序的IT工单（严格按照流程图中的配置过滤）
	 * @author: 王中华
	 * @createDate: 2014-11-24
	 * @param page
	 * @return:
	 */
	@RowFilter(flowIdColumn="WO_CODE",isRouteFilter=true,exclusiveRule="ITSM_ITC_WO")
	List<ItsmWorkOrder> queryAllSortedItWO(Page<ItsmWorkOrder> page);
	
	/**
	 * @description:查询所有排序的IT工单(行权限过滤,仅仅显示与自己相关工单)
	 * @author: 王中华
	 * @createDate: 2014-11-24
	 * @param page
	 * @return:
	 */
	@RowFilter(flowIdColumn="WO_CODE",exclusiveRule="ITSM_ITC_WO")
	List<ItsmWorkOrder> queryFilterAllSortedItWO(Page<ItsmWorkOrder> page);
	
	/**
	 * @description: 工单自动释放时，修改自动释放记录字段
	 * @author: 王中华
	 * @createDate: 2014-11-20
	 * @param valueOf: 
	 */
	void updateReleaseWo(@Param("woId") String woId);

	
	int queryNewWoSize(@Param("beginTime")Date beginTime, @Param("endTime")Date endTime,
			@Param("siteid")String siteid);
	/**
	 * @description: 查询某段时间内新建的工单记录
	 * @author: 王中华 
	 * @createDate: 2014-11-25
	 * @param beginTime
	 * @param endTime
	 * @param siteid
	 * @return:
	 */
	ArrayList<ItsmWorkOrder> queryNewWoList(@Param("beginTime")Date beginTime, @Param("endTime")Date endTime,
			@Param("siteid")String siteid,@Param("selectIndex")int selectIndex,@Param("selectSize")int selectSize);

	/**
	 * @description: 查询某个用户上手的工单数量
	 * @author: 王中华
	 * @createDate: 2015-1-14
	 * @param userId
	 * @param siteId
	 * @return:
	 */
	int getUserWoSum(@Param("userId")String userId, @Param("siteid")String siteId);

	/**
	 * @description: 查询某段时间内新建的工单
	 * @author: 王中华
	 * @createDate: 2015-2-15
	 * @param beginTime
	 * @param endTime
	 * @return:
	 */
	List<ItsmWorkOrder> queryAllWoOfSomePeriod(@Param("beginTime")Date beginTime,
			@Param("endTime")Date endTime, @Param("selectIndex")int selectIndex,
			@Param("selectSize")int selectSize);

	/**
	 * @description:查询某段时间的工单总数量 
	 * @author: 王中华
	 * @createDate: 2015-3-3
	 * @param beginTime
	 * @param endTime
	 * @return:
	 */
	int queryAllWoSumOfSomePeriod(@Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

	/**
	 * @description: 此接口专用于客服直接派单，然后在弹出的审批框中点击“取消”，然后回界面之后修改工单内容，然后再审批时调用
	 * @author: 王中华
	 * @createDate: 2015-3-10
	 * @param workOrder:
	 */
	void updateWOByCsOnCommit(ItsmWorkOrder workOrder);

	/**
	 * @description:打数据补丁需要，查询有多少条需要添加响应时长和解决时长的记录，便于分段处理
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @return:
	 */
	int queryAddResponTimeLenSum();

	/**
	 * @description: 查询出需要添加响应时长和解决时长的工单记录
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @param n
	 * @param selectSize 
	 * @return:
	 */
	List<ItsmWorkOrder> queryAddResponTimeLen(@Param("selectIndex")int selectIndex,
			@Param("selectSize")int selectSize);

	/**
	 * @description:添加响应时间和解决时间的时长
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @param id 
	 * @param respondLen
	 * @param solveLen:
	 */
	void updateRespondSolveLen(Map<String, Object> parmas);

	/**
	 * @description:
	 * @author: 王中华
	 * @createDate: 2015-3-29
	 * @param woId 工单ID
	 * @param faultTypeId  服务目录ID
	 * @param serCharacterId 服务性质ID
	 * @param priorityId:  服务级别ID
	 */
	void updateWoOnSendWo(@Param("woId")String woId, @Param("faultTypeId")String faultTypeId,
			@Param("serCharacterId")String serCharacterId, @Param("priorityId")String priorityId);
	
}
