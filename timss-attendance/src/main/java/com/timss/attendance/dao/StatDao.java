package com.timss.attendance.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.timss.attendance.bean.StatBean;
import com.timss.attendance.bean.StatItemBean;
import com.timss.attendance.vo.StatDetailVo;
import com.timss.attendance.vo.StatVo;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;

/**
 * 休假统计
 */
public interface StatDao {

    /**
     * 
     * @description:时间段查询请假单
     * @author: fengzt
     * @createDate: 2014年9月16日
     * @param params
     * @return:List<StatVo>
     */
    public List<StatVo> queryLeaveByDate(Map<String, Object> params);

    /**
     * 
     * @description:时间段查询加班申请单
     * @author: fengzt
     * @createDate: 2014年9月16日
     * @param params
     * @return:queryOvertimeByDate
     */
    public List<StatVo> queryOvertimeByDate(Map<String, Object> params);

    /**
     * 
     * @description:批量插入stat
     * @author: fengzt
     * @createDate: 2014年9月17日
     * @param insertList
     * @return:int
     */
    public int insertBatchStat(List<StatVo> list );

    /**
     * 
     * @description:批量更新
     * @author: fengzt
     * @createDate: 2014年9月17日
     * @param updateList
     * @return:int
     */
    public int updateBatchStat(List<StatVo> list);

    /**
     * 
     * @description:默认分页
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @param pageVo
     * @return:List<StatVo>
     */
    public List<StatVo> queryStatBySiteId(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:hr经理高级查询
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @param pageVo
     * @return:List<StatVo>
     */
    public List<StatVo> queryStatBySiteIdSearch(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:删除时间段的临时数据
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @param params
     * @return:int
     */
    public int deleteStatTempByMap(Map<String, Object> params);

    /**
     * 
     * @description:批量插入临时数据
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @param result
     * @return:int
     */
    public int insertBatchStatTemp(List<StatVo> result);

    /**
     * 
     * @description:HR查询统计临时表
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @param pageVo
     * @return: List<StatVo>
     */
    public List<StatVo> queryStatTemp(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:部门经理查询统计临时表
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @param pageVo
     * @return: List<StatVo>
     */
    public List<StatVo> queryStatTempByDept(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:用户查询统计临时表
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @param pageVo
     * @return:List<StatVo>
     */
    public List<StatVo> queryStatTempByUser(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:整个站点统一核减年假
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @param params
     * @return:int
     */
    public int updateStatSubAnnual(Map<String, Object> params);

    /**
     * 
     * @description:查询2014年1月到2014年3月的历史数据
     * @author: fengzt
     * @createDate: 2014年9月24日
     * @param map
     * @return:List<StatVo>
     */
    public List<StatVo> queryAllStatHistory(Map<String, Object> map);

    /**
     * 
     * @description:查询所有用户统计信息
     * @author: fengzt
     * @createDate: 2014年9月24日
     * @param map
     * @return:
     */
    public List<StatVo> queryAllStat(Map<String, Object> map);

    /**
     * 
     * @description:批量更新结转
     * @author: fengzt
     * @createDate: 2014年10月17日
     * @param result
     * @return:int
     */
    public int updateCompensateBatch(List<StatVo> result);

    /**
     * 
     * @description:通过ID查询stat
     * @author: fengzt
     * @createDate: 2014年11月11日
     * @param id
     * @return:StatVo
     */
    public StatVo queryStatById(int id);

    /**
     * 
     * @description:更新结转信息
     * @author: fengzt
     * @createDate: 2014年11月11日
     * @param statVo
     * @return:int
     */
    public int updateStatRemain(StatVo statVo);

    /**
     * 
     * @description:查找加班时间段的明细
     * @author: fengzt
     * @createDate: 2014年11月12日
     * @param params
     * @return:List<StatDetailVo>
     */
    public List<StatDetailVo> queryStatOvertimeDetail(Map<String, Object> params);

    /**
     * 
     * @description:查找请假时间段的明细
     * @author: fengzt
     * @createDate: 2014年11月12日
     * @param params
     * @return:List<StatDetailVo>
     */
    public List<StatDetailVo> queryStatLeaveDetail(Map<String, Object> params);

    /**
     * 
     * @description:更新用户状态--批量
     * @author: fengzt
     * @createDate: 2015年5月5日
     * @param uList
     * @return:int
     */
    public int updateBatchStatStatus(List<StatVo> list);
    
    /**
     * 查询当前休假统计
     * 查询主表，传入userId查询个人，传入deptId查询部门，否则查询全站点
     * 可查询指定年份
     * @param page
     * @param year 非空查询指定年份
     * @param siteId 必填
     * @param deptId 非空查询指定部门
     * @param userId 非空则查询指定用户
     * @return
     */
    List<StatBean>queryStatList(Page<StatBean>page);
    
    /**
     * 查询至指定月份为止的休假统计
     * 查询子表关联主表，传入userId查询个人，传入deptId查询部门，否则查询全站点
     * @param page
     * @param year 必填
     * @param month 必填
     * @param siteId 必填
     * @param deptId 非空查询指定部门
     * @param userId 非空则查询指定用户
     * @return
     */
    List<StatBean>queryStatListUntilMonth(Page<StatBean>page);
    
    /**
     * 查询指定月份当月的休假统计
     * @param page
     * @param year 必填
     * @param month 必填
     * @param siteId 必填
     * @param deptId 非空查询指定部门
     * @param userId 非空则查询指定用户
     * @return
     */
    List<StatBean>queryStatListInMonth(Page<StatBean>page);
    
    /**
     * 清空指定年份的休假统计数据
     * 如果参数均为空，会清空整张表，物理删除，需慎重！！
     * @param siteId 非空则清理所有站点
     * @param startYear 清理开始的年份，即year>=startYear
     * @param endYear 清理结束的年份，即year<=endYear
     * @param userStatus 删除指定的用户状态的数据
     * @return
     */
    Integer deleteStatByYear(@Param("siteId")String siteId,@Param("startYear")Integer startYear,@Param("endYear")Integer endYear,@Param("userStatus")String userStatus);
    
    /**
     * 清空指定年份的休假统计的子表数据，用于重建子表
     * @param siteId
     * @param userIds 指定用户，否则全站点
     * @param startYear
     * @param endYear
     * @return
     */
    Integer deleteStatItemByYear(@Param("siteId")String siteId,@Param("userIds")String[] userIds,@Param("startYear")Integer startYear,@Param("endYear")Integer endYear);
    
    /**
     * 查询指定年份的休假统计数据，返回以year_userid为标识的map
     * @param siteId
     * @param userIds 查询指定用户
     * @param startYear
     * @param endYear
     * @return
     */
    @MapKey("flag")
    Map<String, StatBean> queryStatMap(@Param("siteId")String siteId,
    		@Param("userIds")String[] userIds,
    		@Param("startYear")Integer startYear,@Param("endYear")Integer endYear);
    
    /**
     * 创建指定用户和年份的休假统计数据
     * @param userList
     * @param yearList
     * @return
     */
    Integer createStatByUserAndYear(@Param("siteId")String siteId,
    		@Param("userList")List<SecureUser>userList,@Param("yearList")List<Integer>yearList);

    /**
     * 批量插入休假统计月份数据
     * @param list
     * @return
     */
	Integer batchInsertStatItem(@Param("list")List<StatItemBean> list);
	/**
	 * 插入休假统计月份数据
	 * @param bean
	 * @return
	 */
	Integer insertStatItem(@Param("item")StatItemBean bean);
	/**
	 * 更新休假统计月份数据，在原有假期天数上增加天数
	 * @param bean
	 * @return
	 */
	Integer updateStatItemPlusDays(@Param("item")StatItemBean bean);
	/**
     * 批量更新休假统计月份数据，在原有假期天数上增加天数
     * @param list
     * @return
     */
	Integer batchUpdateStatItemPlusDays(@Param("list")List<StatItemBean> list);	
	
	/**
     * 批量插入休假统计年份数据
     * @param list
     * @return
     */
	Integer batchInsertStat(@Param("list")List<StatBean> list);
	/**
	 * 插入休假统计年份数据
	 * @param bean
	 * @return
	 */
	Integer insertStat(@Param("item")StatBean bean);
	/**
	 * 更新休假统计年份数据，在原有假期天数上增加天数
	 * @param bean
	 * @return
	 */
	Integer updateStatPlusDaysByStatItem(@Param("item")StatItemBean bean);
	/**
     * 批量更新休假统计年份数据，在原有假期天数上增加天数
     * @param list
     * @return
     */
	Integer batchUpdateStatPlusDaysByStatItem(@Param("list")List<StatItemBean> list);	
	/**
	 * 更新休假统计年份数据的可休年假和用户信息
	 * @param bean
	 * @return
	 */
	Integer updateStatAnnualAndUser(@Param("item")StatBean bean);
	/**
     * 批量更新休假统计年份数据的可休年假和用户信息
     * @param list
     * @return
     */
	Integer batchUpdateStatAnnualAndUser(@Param("list")List<StatBean> list);	
	
	/**
	 * 更新stat的用户状态
	 * @param updateUser
	 * @param status
	 * @return
	 */
	Integer updateUserStatStatus(@Param("list")List<String> updateUser,@Param("status")String status);

	/**
     * 查询指定年份的休假统计的月度数据，返回以year-month_userid为标识的map
     * @param siteId
     * @param userIds 查询指定用户
     * @param startYear
     * @param endYear
     * @return
     */
    @MapKey("flag")
    Map<String, StatItemBean> queryStatItemMap(@Param("siteId")String siteId,
    		@Param("userIds")String[] userIds,
    		@Param("startYear")Integer startYear,@Param("endYear")Integer endYear);
    
}
