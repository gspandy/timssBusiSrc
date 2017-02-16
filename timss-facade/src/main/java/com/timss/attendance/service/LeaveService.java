package com.timss.attendance.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.attendance.bean.DefinitionBean;
import com.timss.attendance.bean.LeaveBean;
import com.timss.attendance.bean.LeaveItemBean;
import com.timss.attendance.vo.LeaveContainItemVo;
import com.timss.attendance.vo.ShiftOprVo;
import com.timss.attendance.vo.StatVo;
import com.yudean.itc.dto.Page;


/**
 * 
 * @title: 请假申请
 * @description: {desc}
 * @company: gdyd
 * @className: LeaveService.java
 * @author: fengzt
 * @createDate: 2014年9月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface LeaveService {

    /**
     * 
     * @description:请假申请
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @param ids 
     * @param rowData 
     * @return:Map<String, Object>
     */
    Map<String, Object> insertLeave(String formData, String rowData, String ids);

    /**
     * 
     * @description:通过Id查询请假申请信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param id
     * @return:Map<String, Object>
     */
    Map<String, Object> queryLeaveById(int id);
    
    /**
     * 查询请假详情，包括请假项
     * @param id
     * @return
     */
    LeaveBean queryLeaveBeanById(int id);

    /**
     * 
     * @description:更新请假申请信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param vo
     * @return:int
     */
    int updateLeave(LeaveBean vo);

    /**
     * 
     * @description:通过站点拿到所有请假申请信息
     * @author: fengzt
     * @param pageVo 
     * @createDate: 2014年9月2日
     * @return:List<LeaveBean>
     */
    List<LeaveBean> queryLeaveBySiteId(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:高级搜索（也分站点）
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param map
     * @param pageVo
     * @return:List<LeaveBean>
     */
    List<LeaveBean> queryLeaveBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:终止流程
     * @author: fengzt
     * @param businessId 
     * @param message 
     * @param owner 
     * @param assignee 
     * @param taskId 
     * @createDate: 2014年9月3日
     * @return:
     */
    int deleteFlowLeave(String taskId, String assignee, String owner, String message, String businessId);

    /**
     * 
     * @description:通过请假申请查询明细
     * @author: fengzt
     * @createDate: 2014年9月9日
     * @param leaveId
     * @return:List<LeaveItemBean>
     */
    List<LeaveItemBean> queryLeaveItemList(String leaveId);

    /**
     * 
     * @description:通过请假申请查询附件明细
     * @author: fengzt
     * @createDate: 2014年9月9日
     * @param leaveId
     * @return:ArrayList<HashMap<String, Object>>
     */
    List<Map<String, Object>> queryFileByLeaveId(String leaveId);
    
    /**
     * 
     * @description:请假附件
     * @author: fengzt
     * @createDate: 2014年9月5日
     * @param leaveBean
     * @param fileIds
     * @return:int
     */
    int insertLeaveFile(LeaveBean leaveBean, String fileIds);

    /**
     * 
     * @description:通过请假主表删除附件
     * @author: fengzt
     * @createDate: 2014年9月12日
     * @param leaveId
     * @return:int
     */
    int deleteFileByLeaveId(int leaveId );

    /**
     * 
     * @description:剔除假期、周末 计算请假天数
     * @author: fengzt
     * @createDate: 2014年10月9日
     * @param start
     * @param end
     * @param siteId
     * @return:double
     */
    double getLeavesDays(Date start, Date end, String siteId);

    /**
     * 
     * @description:包含节假日 、 周末 （for 婚假、产假）
     * @author: fengzt
     * @createDate: 2014年10月9日
     * @param start
     * @param end
     * @param siteId
     * @return:double
     */
    double getLeavesDaysContainFes(Date start, Date end, String siteId);

    /**
     * 
     * @description:插入or更新请假信息
     * @author: fengzt
     * @createDate: 2014年10月14日
     * @param formData
     * @param rowData
     * @param fileIds
     * @return:Map<String, Object> 
     */
    Map<String, Object> insertOrUpdateLeave(String formData, String rowData, String fileIds);

    /**
     * 
     * @description:通过NUM查找Id
     * @author: fengzt
     * @createDate: 2014年10月16日
     * @param flowNo
     * @param siteId
     * @return:int
     */
    int queryIdByFlowNo(String flowNo, String siteId);

    /**
     * 
     * @description:计算请假时间
     * @author: fengzt
     * @createDate: 2014年11月18日
     * @param startDate
     * @param endDate
     * @param category
     * @return:map
     * @throws Exception 
     */
    Map<String, Object> queryDiffLeaveDay(String startDate, String endDate, String category) throws Exception;

    /**
     * 
     * @description:删除 & 删除代办
     * @author: fengzt
     * @createDate: 2015年1月13日
     * @param id
     * @return:int
     */
    int deleteLeave(int id);

    /***
     * 
     * @description:作废 & 删除代办
     * @author: fengzt
     * @createDate: 2015年1月13日
     * @param id
     * @return:int
     */
    int invalidLeave(int id);

    /**
     * 
     * @description:通过月份查询
     * @author: fengzt
     * @createDate: 2015年2月15日
     * @param year
     * @param month
     * @return:List<StatVo>
     */
    List<StatVo> queryCalendarByMonth(int year, int month);

    /**
     * 
     * @description:通过天查询
     * @author: fengzt
     * @createDate: 2014年10月26日
     * @param ctime
     * @return:Map<String, Object>
     */
    Map<String, Object> queryCalendarByDay(String ctime);
    
    /**
     * 
     * @description:查询班组人员值班的班次
     * @author: fengzt
     * @createDate: 2015年3月30日
     * @param siteId
     * @param userId --用户名
     * @param dateStr --某日 ( YYYY-MM-DD )
     * @return:List<ShiftOprVo>
     */
    List<ShiftOprVo> queryShiftOprByUser( String siteId, String userId, String dateStr );
    
    /**
     * 
     * @description:计算班组请假天数
     * @author: fengzt
     * @createDate: 2015年3月30日
     * @param start
     * @param end
     * @param userId
     * @return:double
     */
    double queryLeaveDaysByBanzu(Date start, Date end, String userId);

    /**
     * 
     * @description:是否电厂运行班组人员
     * @author: fengzt
     * @createDate: 2015年3月31日
     * @param userId
     * @param siteId
     * @return:boolean
     */
    boolean isRoleBanzu(String userId, String siteId);

    /**
     * 
     * @description:请假申请审批跨月列表
     * @author: fengzt
     * @createDate: 2015年4月7日
     * @param pageVo
     * @return:List<LeaveBean>
     */
    List<LeaveBean> queryExceptionLeaveList(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:请假申请审批跨月列表-查询
     * @author: fengzt
     * @createDate: 2015年4月7日
     * @param pageVo
     * @return:List<LeaveBean>
     */
    List<LeaveBean> queryExceptionLeaveListBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:检查用户这天是否上班 
     * @author: fengzt
     * @createDate: 2015年6月8日
     * @param userId
     * @param siteId
     * @param workDate （需要检查的天 格式：yyyy-MM-dd ）
     * @return:
     */
    boolean isWorkDate(String userId, String siteId, String workDate);

    /**
     * 
     * @description:查询时间区域内请假信息
     * @author: fengzt
     * @createDate: 2015年6月12日
     * @param siteId
     * @param userIds
     * @param startTimeStr
     * @param endTimeStr
     * @param category
     * @return:List<LeaveContainItemVo>
     * @update:yyn 20160119
     */
    List<LeaveContainItemVo> queryLeaveByDiffDay(String siteId,String[] userIds, String startTimeStr, String endTimeStr,String category);

    /**
     * 计算一个时间段内的有效请假天数
	 * 根据请假类型的配置决定是否将周六日和节假日计入，如果计入，当工作日处理请假天数
	 * 工作日的请假时间和工作时间的交集才是有效请假时间，计算该交集时间占一天工作时间的比例即为有效请假天数
	 * 排除非工作时间
	 * @param category
     * @param definitionBean
     * @param userId
     * @param isOpr
     * @param startDate
     * @param endDate
     * @return
     */
	Double countLeaveDays(String category,DefinitionBean definitionBean, String userId,
			Boolean isOpr, Date startDate, Date endDate) throws Exception;

	/**
     * 审批时更新状态
     * @return
     */
	Boolean updateAuditStatus(Integer leaveId, String instanceId, String status);
	
}
