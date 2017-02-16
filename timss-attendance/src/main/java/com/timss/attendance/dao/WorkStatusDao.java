package com.timss.attendance.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.timss.attendance.bean.WorkStatusBean;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 员工出勤情况表
 * @description: {desc}
 * @company: gdyd
 * @className: WorkStatusDao.java
 * @author: fengzt
 * @createDate: 2015年6月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface WorkStatusDao {

    /**
     * 
     * @description:插入
     * @author: fengzt
     * @createDate: 2015年6月4日
     * @param bean
     * @return:int
     */
    int insertWorkStatus(WorkStatusBean bean);
    
    /**
     * 
     * @description:更新
     * @author: fengzt
     * @createDate: 2015年6月4日
     * @param bean
     * @return:int
     */
    int updateWorkStatus(WorkStatusBean bean);

    /**
     * 删除
     * @param bean
     * @return
     */
    int deleteWorkstatus(WorkStatusBean bean);
    
    /**
     * 
     * @description:查询出勤情况--高级查询
     * @author: fengzt
     * @createDate: 2015年6月8日
     * @param pageVo
     * @return:List<WorkStatusBean>
     */
    List<WorkStatusBean> queryWorkStatusListBySearch(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:批量更新WorkStatus
     * @author: fengzt
     * @createDate: 2015年6月15日
     * @param list
     * @return:int
     */
    int updateBatchWorkStatus(List<WorkStatusBean> list);

    /**
     * 
     * @description:批量插入
     * @author: fengzt
     * @createDate: 2015年6月18日
     * @param list
     * @return:int
     */
    int insertBatchWorkStatus(List<WorkStatusBean> list);

    /**
     * 批量删除
     * @param list
     * @return
     */
    int deleteBatchWorkStatus(List<WorkStatusBean> list);
    
    /**
     * 删除指定站点指定日期前的打卡管理的数据
     * 若站点为空，则清理所有站点
     * @param endTimeStr
     * @param siteId
     * @return
     */
    int deleteWorkStatusByEndTime(@Param("endTimeStr")String endTimeStr, @Param("siteId")String siteId);
    
    /**
     * 根据起止时间查询站点中已存在的考勤结果
     * @param siteId 为空则查询所有站点
     * @param startDateStr 为空则开始时间无限制
     * @param endDateStr 为空则结束时间无限制
     * @return 以日期_用户id为key的map
     */
    @MapKey("flag")
    Map<String, WorkStatusBean> queryExistWorkStatusBySiteAndTime(@Param("siteId")String siteId,@Param("userId")String userId,
    		@Param("startDateStr")String startDateStr,@Param("endDateStr")String endDateStr);
}
