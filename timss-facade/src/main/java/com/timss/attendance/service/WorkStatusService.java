package com.timss.attendance.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.timss.attendance.bean.AbnormityBean;
import com.timss.attendance.bean.CardDataBean;
import com.timss.attendance.bean.DefinitionBean;
import com.timss.attendance.bean.LeaveBean;
import com.timss.attendance.bean.WorkStatusBean;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.AppEnum;

/**
 * 
 * @title: 员工出勤情况 - 打卡机
 * @description: {desc}
 * @company: gdyd
 * @className: WorkStatusService.java
 * @author: fengzt
 * @createDate: 2015年6月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface WorkStatusService {

    /**
     * 
     * @description:出勤情况列表--高级查询
     * @author: fengzt
     * @createDate: 2015年6月8日
     * @param map
     * @param pageVo
     * @return:List<WorkStatusBean>
     */
    List<WorkStatusBean> queryWorkStatusListBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);
    
    /**
     * 根据考勤规则确定打卡记录的考勤结果
     * @param cardData
     * @param definitionBean
     * @return 枚举ATD_MACHINE_WORK_STATUS
     */
    String checkWorkStatusByCardData(CardDataBean cardData,DefinitionBean definitionBean);
    
    /**
     * 删除指定站点指定日期前的打卡管理的数据
     * 若站点为空，则清理所有站点
     * @param endTimeStr
     * @param siteId
     * @return
     * @throws Exception
     */
    Integer deleteWorkStatusByEndTime(String endTimeStr,String siteId) throws Exception;
    
    /**
     * 生成站点用户打卡管理的数据
     * 由打卡记录、考勤异常、请假申请影响考勤结果
     * 参数null可查询所有站点
     * @throws Exception
     */
    void checkWorkStatus(String siteId) throws Exception;
    
    /**
     * 根据一个请假申请更新打卡管理
     * @throws Exception
     */
    void checkLeaveWorkStatus(LeaveBean bean,DefinitionBean definitionBean)throws Exception;
    
    /**
     * 根据一个考勤异常更新打卡管理
     * @throws Exception
     */
    void checkAbnormityWorkStatus(AbnormityBean bean,DefinitionBean definitionBean)throws Exception;

    /**
     * 根据打卡记录更新的时间来更新打卡统计
     * @param siteId
     * @param startTimeStr
     * @param endTimeStr
     * @throws Exception
     */
	void checkCardDataWorkStatus(String siteId,String startTimeStr,String endTimeStr) throws Exception;
	
	/**
	 * 根据指定人员指定日期更新打卡统计
	 * @param siteId 必须指定站点id，不然无法查询人员信息
	 * @param startDate
	 * @param endDate
	 * @param userIdList
	 * @throws Exception
	 */
	void checkPersonsWorkStatus(String siteId,Date startDate,Date endDate,Set<String>userIdSet)throws Exception;
}
