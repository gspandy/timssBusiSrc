package com.timss.attendance.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.attendance.bean.DefinitionBean;
import com.timss.attendance.bean.LeaveBean;
import com.timss.attendance.bean.OvertimeBean;
import com.timss.attendance.bean.StatBean;
import com.timss.attendance.bean.StatItemBean;
import com.timss.attendance.vo.LeaveContainItemVo;
import com.timss.attendance.vo.StatDetailVo;
import com.timss.attendance.vo.StatVo;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;

/**
 * 
 * @title: 统计service
 * @description: {desc}
 * @company: gdyd
 * @className: StatService.java
 * @author: fengzt
 * @createDate: 2014年9月15日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface StatService {

    /**
     * 
     * @description:高级搜索
     * @author: fengzt
     * @createDate: 2014年9月15日
     * @param map
     * @param pageVo
     * @param year 
     * @return:List<StatVo>
     */
    List<StatVo> queryStatBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo, int year);

    /**
     * 
     * @description:默认分页列表
     * @author: fengzt
     * @createDate: 2014年9月15日
     * @param pageVo
     * @param year 
     * @return:List<StatVo>
     */
    List<StatVo> queryStatBySiteId(Page<HashMap<?, ?>> pageVo, int year);
    
    /**
     * 
     * @description:构造统计报表
     * @author: fengzt
     * @createDate: 2014年9月15日
     * @param pageVo
     * @return:List<StatVo>
     */
    List<StatVo> queryStatByDiffDate( Date startDate, Date endDate );

    /**
     * 
     * @description:时间段查询
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @param map
     * @param pageVo
     * @return: List<StatVo>
     */
    List<StatVo> queryStatByTimeSearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:核减年假
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @param subAnnualDays
     * @param remark 
     * @return:Map<String, Object>
     */
    Map<String, Object> updateStatSubAnnual(double subAnnualDays, String remark);

    /**
     * 
     * @description:拿到登录用户的统计信息
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @return:StatVo
     */
    StatVo queryStatByLogin();
    
    /**
     * 
     * @description:更新一年的统计数据
     * @author: fengzt
     * @createDate: 2014年9月24日
     * @return:int
     */
    int updateCurrentYearStat();

    /**
     * 
     * @description:定时任务-考勤统计数据
     * @author: fengzt
     * @createDate: 2014年10月17日:
     */
    void countStatData();

    /**
     * 
     * @description:通过ID查询stat
     * @author: fengzt
     * @createDate: 2014年11月11日
     * @param id
     * @return:StatVo
     */
    StatVo queryStatById(int id);

    /**
     * 
     * @description:更新结转信息
     * @author: fengzt
     * @createDate: 2014年11月11日
     * @param formData
     * @return:int
     */
    int updateStatRemain(String formData);

    /**
     * 
     * @description:查询请假（加班）明细
     * @author: fengzt
     * @createDate: 2014年11月12日
     * @param userId
     * @param searchDateFrom
     * @param searchDateEnd
     * @param field
     * @return:List<StatDetailVo>
     */
    List<StatDetailVo> queryStatLeaveDetail(String userId, String searchDateFrom, String searchDateEnd, String field);
    /**
     * 查询加班或请假的年份或月份明细
     * @param userId
     * @param year
     * @param month
     * @param field
     * @return
     * @throws Exception 
     */
    List<LeaveContainItemVo> queryStatDetail(String userId, Integer year, Integer month, String field) throws Exception;
    
    /**
     * 
     * @description:查询个人考勤统计信息
     * @author: fengzt
     * @createDate: 2015年2月28日
     * @return:StatVo
     */
    StatVo queryPersonStat();
    
    /***
     * 
     * @description:通过时间间隔统计报表--用来缓存
     * @author: fengzt
     * @createDate: 2015年2月28日
     * @param startDate
     * @param endDate
     * @return:List<StatVo> 
     */
    public List<StatVo> queryStatByDiffDateForCache(Date startDate, Date endDate);

    /**
     * 
     * @description:批量更新用户状态
     * @author: fengzt
     * @createDate: 2015年5月5日
     * @param uList
     * @return:int --更新条目
     */
    int updateBatchStatStatus(List<StatVo> uList);
    
    /**
     * 重建休假统计的月度数据
     * @param isUpdateStat 是否更新主表，是则会把算出来的天数加到主表（年度休假统计）上
     * @param siteId
     * @param definitionBean 为空则内部进行查询
     * @param userIds 指定用户id
     * @param startYear
     * @param endYear
     * @return
     * @throws Exception
     */
    Boolean rebuildStatItem(Boolean isUpdateStat,String siteId,DefinitionBean definitionBean,String[]userIds,Integer startYear,Integer endYear)throws Exception;

    /**
     * 根据站点人员生成指定年份的休假统计的主表数据
     * @param siteId
     * @param startYear
     * @param endYear
     * @return
     * @throws Exception
     */
    Integer createStatBySiteUser(String siteId,Integer startYear,Integer endYear)throws Exception;
    
    /**
     * 批量插入休假统计月份数据
     * @param list
     * @return
     * @throws Exception
     */
    Integer batchInsertStatItem(List<StatItemBean> list)throws Exception;
    /**
     * 批量插入休假统计年份数据
     * @param list
     * @return
     * @throws Exception
     */
    Integer batchInsertStat(List<StatBean> list)throws Exception;
    /**
     * 批量更新statItem叠加天数
     * @param list
     * @return
     * @throws Exception
     */
    Integer batchUpdateStatItemPlusDays(List<StatItemBean> list)throws Exception;
    /**
     * 批量更新stat叠加天数
     * @param list
     * @return
     * @throws Exception
     */
	Integer batchUpdateStatPlusDaysByStatItem(List<StatItemBean> list)throws Exception;
	/**
     * 批量更新stat可休年假和用户信息
     * @param list
     * @return
     * @throws Exception
     */
	Integer batchUpdateStatAnnualAndUser(List<StatBean> list)throws Exception;
	
    /**
     * 检查指定用户的休假统计的状态：
     * 检查是否离职，离职更新休假统计的用户状态
     * 检查今年的休假统计主表数据是否存在，不存在则从去年数据结转新建，存在则检查可休年假
     * @param isRebuild 是否重建stat，会清空原来的站点的stat
     * @param isInherited 重建的时候，是否从旧数据继承（除了重算的天数，其他信息继承）
     * @param siteId
     * @param checkYear 非空则指定要检查的年份
     * @param definitionBean 为空则内部进行查询
     * @param userList 为空则检查整个站点人员
     * @return
     * @throws Exception
     */
	Boolean checkPersonStat(Boolean isRebuild,Boolean isInherited,String siteId,Integer checkYear,DefinitionBean definitionBean,List<SecureUser> userList)throws Exception;

	/**
	 * 检查请假归档时休假统计的更新
	 * @param bean
	 * @param definitionBean
	 * @throws Exception
	 */
	void checkLeaveStat(LeaveBean bean, DefinitionBean definitionBean)throws Exception;

	/**
	 * 检查加班归档时休假统计的更新
	 * @param bean
	 * @param definitionBean
	 * @throws Exception
	 */
	void checkOvertimeStat(OvertimeBean bean, DefinitionBean definitionBean)throws Exception;

	/**
	 * 休假统计分页查询
	 * @param map
	 * @param pageVo
	 * @return
	 * @throws Exception
	 */
	Page<StatBean> queryStatList(Page<StatBean> page)throws Exception;
	
	/**
	 * 查询指定用户和指定年份的休假统计信息，返回year_userid为key的map
	 * @param siteId
	 * @param userIds
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	Map<String, StatBean> queryStatMap(String siteId,String[] userIds,
    		Integer startYear,Integer endYear);
}
