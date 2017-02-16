package com.timss.attendance.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.attendance.bean.DefinitionBean;
import com.timss.attendance.bean.OvertimeBean;
import com.timss.attendance.vo.LeaveContainItemVo;
import com.timss.attendance.vo.OvertimePageVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 加班申请
 * @description: {desc}
 * @company: gdyd
 * @className: OvertimeService.java
 * @author: fengzt
 * @createDate: 2014年8月28日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface OvertimeService {

    /**
     * 
     * @description:通过Id查询加班申请信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param id
     * @return:Map<String, Object>
     */
    Map<String, Object> queryOvertimeById(int id);

    /**
     * 
     * @description:更新加班申请信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param vo
     * @return:int
     */
    int updateOvertime(OvertimeBean vo);
    /**
     * 审批时更新状态
     * @return
     */
    Boolean updateAuditStatus(Integer overtimeId,String instanceId,String status);
    /**
     * 更新加班单的申请时间
     * 需传入id
     * createday为空会取当前时间
     * @param bean
     * @return
     */
    Integer updateOvertimeCreateDay(OvertimeBean bean);
    
    /**
     * 
     * @description:通过站点拿到所有加班申请信息
     * @author: fengzt
     * @param pageVo 
     * @createDate: 2014年9月2日
     * @return:List<OvertimeBean>
     */
    List<OvertimeBean> queryOvertimeBySiteId(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:高级搜索（也分站点）
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param map
     * @param pageVo
     * @return:List<OvertimeBean>
     */
    List<OvertimeBean> queryOvertimeBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);

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
    int deleteFlowOvertime(String taskId, String assignee, String owner, String message, String businessId);

    /**
     * 
     * @description:查找附件
     * @author: fengzt
     * @createDate: 2014年9月11日
     * @param overtimeId
     * @return:Map<String, Object>
     */
    List<Map<String, Object>> queryFileByOvertimeId(String overtimeId);

    /**
     * 
     * @description:更新加班申请
     * @author: fengzt
     * @createDate: 2014年9月11日
     * @param parmas
     * @return:int
     */
    int updateOperUserById(HashMap<String, Object> parmas);

    /**
     * 
     * @description:草稿模式编辑更新
     * @author: fengzt
     * @createDate: 2014年9月11日
     * @param vo
     * @param fileIds
     * @return:int
     */
    int updateOvertimeByCg(OvertimePageVo vo, String fileIds);

    /**
     * 
     * @description:通过num拿到ID
     * @author: fengzt
     * @createDate: 2014年10月16日
     * @param flowNo
     * @param siteId
     * @return:int
     */
    int queryIdByFlowNo(String flowNo, String siteId);

    /**
     * 
     * @description:删除 & 删除代办
     * @author: fengzt
     * @createDate: 2015年1月13日
     * @param id
     * @return:int
     */
    int deleteOvertime(int id);

    /**
     * 
     * @description:作废 
     * @author: fengzt
     * @createDate: 2015年1月13日
     * @param id
     * @return:int
     */
    int invalidOvertime(int id);

    /**
     * 保存加班单，用于暂存
     * @param bean
     * @return
     */
    Map<String, Object> saveOvertime(OvertimeBean bean);
    
    /**
     * 提交加班单，进入流程
     * @param bean
     * @return
     */
    Map<String, Object> submitOvertime(OvertimeBean bean);
    
    /**
     * 新建或更新加班单
     * @param bean
     * @return
     */
    Integer insertOrUpdateOvertime(OvertimeBean bean);
    
    /**
     * 核定加班时长
     * @param bean
     * @return
     */
    Integer updateOvertimeRealOverHours(OvertimeBean bean);
    /**
     * 核定加班转补休时长
     * @param bean
     * @param isFromRealOverHours 是否从核定加班时长更新
     * @return
     */
    Integer updateOvertimeTransferCompensate(OvertimeBean bean,Boolean isFromRealOverHours);
    /**
     * 将收到的数据转成bean
     * @param formData
     * @param fileIds
     * @param addRows
     * @param delRows
     * @param updateRows
     * @return
     */
    OvertimeBean convertBean( String formData, String fileIds, String addRows, String delRows, String updateRows );

    /**
     * 根据给定月份查询该月累计加班时长
     * @param userId 为空则统计整个站点
     * @param siteId
     * @param monthStr 格式如“2016-05”
     * @return
     */
	Double queryOvertimeTotalHoursByMonth(String userId, String siteId,
			String monthStr);
    
	/**
	 * 查询一段时间的加班项，返回统一格式的数据
	 * @param siteId 非空
	 * @param userIds 指定查询的用户
	 * @param startTimeStr 格式为yyyy-MM-dd HH:mm
	 * @param endTimeStr
	 * @return
	 */
	List<LeaveContainItemVo> queryOvertimeByDiffDay(String siteId,String[] userIds, String startTimeStr, String endTimeStr);

	/**
	 * 查询加班单，包括加班项
	 * @param id
	 * @return
	 */
	OvertimeBean queryOvertimeBeanById(Integer id);

	/**
	 * 计算一个时间段内的有效加班时长
	 * 周六日和节假日记为1天
	 * 排除工作时间
	 * 计入工作日的非工作时间
	 * @return
	 * @throws Exception 
	 */
	Double countOvertimeHours(DefinitionBean definitionBean,String userId,Boolean isOpr,
			Date startDate,Date endDate) throws Exception;
}
