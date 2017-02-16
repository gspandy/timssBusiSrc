package com.timss.attendance.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.attendance.bean.CardDataBean;
import com.timss.attendance.bean.DefinitionBean;
import com.timss.attendance.bean.MachineBean;
import com.timss.operation.vo.DutyPersonShiftVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 考勤机数据service
 * @description: {desc}
 * @company: gdyd
 * @className: CardDataService.java
 * @author: fengzt
 * @createDate: 2015年6月3日
 * @updateUser: yyn
 * @version: 1.0
 */
public interface CardDataService {
	/**
	 * 更新打卡记录
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public Integer updateCardDataList(List<CardDataBean>list)throws Exception;
	/**
	 * 单条更新打卡记录
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public Integer updateCardData(CardDataBean bean)throws Exception;
	/**
	 * 批量更新打卡记录
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public Integer batchUpdateCardDataList(List<CardDataBean>list)throws Exception;
    /**
     * 
     * @description:批量插入
     * @author: fengzt
     * @createDate: 2015年6月3日
     * @param list
     * @return:int
     * @throws Exception 
     */
    public int insertBatchCardData( List<CardDataBean> list );

    /**
     * 
     * @description:查询打卡记录列表
     * @author: fengzt
     * @createDate: 2015年6月8日
     * @param pageVo
     * @return:List<CardDataBean>
     */
    public List<CardDataBean> queryCardDataList(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:查询打卡记录列表 -- 高级查询
     * @author: fengzt
     * @createDate: 2015年6月8日
     * @param map
     * @param pageVo
     * @return:List<CardDataBean>
     */
    public List<CardDataBean> queryCardDataListBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);
    
    /**
     * 保存站点的考勤机的打卡数据
     * @param machineBeans
     * @param siteId
     * @return
     * @throws Exception
     */
    Map<String, Integer> insertOrUpdateMachineListCardData(List<MachineBean>machineBeans,String siteId) throws Exception;
    
    /**
     * 保存考勤机的打卡数据
     * @param machineBean
     * @return
     * @throws Exception
     */
    Integer saveMachineCardData(MachineBean machineBean)throws Exception;
    
    /**
     * 删除指定站点指定日期前的打卡记录
     * 若站点为空，则清理所有站点
     * @param endTimeStr
     * @param siteId
     * @return
     * @throws Exception
     */
    Integer deleteCardDataByEndTime(String endTimeStr,String siteId) throws Exception;
    
    /**
     * 清除打卡机的打卡记录
     * 可指定时间
     * @param bean
     * @return
     * @throws Exception
     */
    
    Integer deleteCardDataByMachine(MachineBean bean)throws Exception;
    
    /**
     * 查询给定站点时间内有效的打卡记录
     * @param startDate
     * @param endDate
     * @param siteId
     * @param userId 可只查某个用户，为空则查站点所有用户
     * @return
     */
    List<CardDataBean> queryValidCardDataListByDiffDate( String startDate, String endDate, String siteId,String userId );
    
    /**
	 * 检索所有打卡记录
	 * @param siteId 指定站点，为空所有站点
	 * @param userIds 指定用户
	 * @param startTimeStr
	 * @param endTimeStr
	 * @throws Exception
	 */
    List<CardDataBean>queryAll(String siteId,String[]userIds,String startTimeStr,String endTimeStr)throws Exception;
    
    /**
     * 根据打卡记录的信息构建一个打卡记录bean
     * @param userId 打卡人id
     * @param userName 打卡人姓名
     * @param checkTime 打卡时间
     * @param definitionBean 站点配置
     * @param userMap 用户信息缓存
     * @param isOpr 是否运行人员
     * @param dutyPersonShiftMap 运行人员排班
     * @return
     */
    CardDataBean setupCardDataBean(String userId,String userName,String checkTime,DefinitionBean definitionBean,
    		Map<String, Map<String, String>>userMap,Boolean isOpr,Map<String, DutyPersonShiftVo>dutyPersonShiftMap);
    
    /**
     * 设置运行人员的排班信息到carddata里
     * 考虑班次跨天的问题，依次取当天、前一天、后一天的班次的上下班时间，比较打卡时间与其时间差是否在有效时间内，是则获得该打卡时间对应的班次
     * @param bean 
     * @param validMin 有效打卡时间分钟数，上下班时间点前后有效分钟数内的打卡记录是有效打卡记录
     * @param dutyPersonShiftMap 运行人员排班
     */
    void setOprPersonDutyToCardDataBean(CardDataBean bean,Integer validMin,Map<String, DutyPersonShiftVo>dutyPersonShiftMap);
}
