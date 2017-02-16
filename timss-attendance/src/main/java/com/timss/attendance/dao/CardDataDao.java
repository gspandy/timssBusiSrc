package com.timss.attendance.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.attendance.bean.CardDataBean;
import com.timss.attendance.bean.MachineBean;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 考勤机数据DAO
 * @description: {desc}
 * @company: gdyd
 * @className: CardDataDao.java
 * @author: fengzt
 * @createDate: 2015年6月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface CardDataDao {

    /**
     * 
     * @description:批量插入考勤机数据
     * @author: fengzt
     * @createDate: 2015年6月3日
     * @param list
     * @return:int
     */
    int insertBatchCardData(List<CardDataBean> list);

    /**
     * 
     * @description:通过站点查询考勤机数据表中data
     * @author: fengzt
     * @createDate: 2015年6月3日
     * @param siteId
     * @return:List<CardDataBean>
     */
    List<CardDataBean> queryCardDataBySite(String siteId);
    
    /**
     * 
     * @description:插入单条记录
     * @author: fengzt
     * @createDate: 2015年6月4日
     * @param bean
     * @return:int
     */
    int insertCardData( CardDataBean bean );

    /**
     * 
     * @description:列表查询--高级查询
     * @author: fengzt
     * @createDate: 2015年6月8日
     * @param pageVo
     * @return:List<CardDataBean>
     */
    List<CardDataBean> queryCardDataListBySearch(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:查询区间内的整个站点的打卡记录
     * @author: fengzt
     * @createDate: 2015年6月26日
     * @param map
     * @return:List<CardDataBean> 
     * @update:yyn 20160120 增加指定只查询有效的打卡记录
     */
    List<CardDataBean> queryCardDataListByDiffDate(Map<String, Object> map);    

    /**
     * 删除指定站点指定日期前的打卡记录 
     * 若站点为空，则清理所有站点
     * @param endTimeStr
     * @param siteId
     * @return
     */
    int deleteCardDataByEndTime(@Param("endTimeStr")String endTimeStr, @Param("siteId")String siteId);
    
    /**
     * 删除打卡机的数据
     * 需指定打卡机id
     * 可指定startTime和endTime
     * @param bean
     * @return
     */
    int deleteCardDataByMachine(@Param("bean")MachineBean bean);
    
    /**
     * 删除指定打卡日期或指定用户的打卡记录
     * @param dateStr 日期或带日期开头的时间
     * @param userId
     * @return
     */
    int deleteCardDataByDateAndUserId(@Param("date")String dateStr, @Param("userId")String userId);
    
    /**
	 * 检索所有打卡记录
	 * @param siteId 指定站点，为空所有站点
	 * @param userIds 指定用户
	 * @param startTimeStr
	 * @param endTimeStr
	 * @throws Exception
	 */
    List<CardDataBean>queryAll(@Param("siteId")String siteId,@Param("userIds")String[]userIds,@Param("startTimeStr")String startTimeStr,@Param("endTimeStr")String endTimeStr);
    
    /**
	 * 单条更新打卡记录
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	Integer updateCardData(@Param("bean")CardDataBean bean);
	
	/**
	 * 批量更新打卡记录
	 * @param list
	 * @return
	 * @throws Exception
	 */
	Integer batchUpdateCardDataList(@Param("list")List<CardDataBean>list);
	
}
