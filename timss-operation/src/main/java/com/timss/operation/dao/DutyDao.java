package com.timss.operation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.timss.operation.bean.Duty;
import com.timss.operation.bean.PersonJobs;
import com.timss.operation.vo.DutyPersonShiftVo;
import com.timss.operation.vo.RoleVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: DutyDao 
 * @description: mybatis 接口
 * @company: gdyd
 * @className: DutyDao.java
 * @author: fengzt
 * @createDate: 2014年6月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface DutyDao {
	
    /**
     * 
     * @description:新建一个值别
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param duty  其中id为自增，不需要设置
     */
    public void insertDuty(Duty duty );
    
    /**
     * 
     * @description:批量增加值别用户
     * @author: zhuw
     * @createDate: 2016年1月22日
     * @param userIdList
     */
    public void batchInsertDutyPerson(List<Object> userIdList );
    
    /**
     * 
     * @description:批量删除值别用户
     * @author: fengzt
     * @createDate: 2016年1月22日
     * @param userIdList
     */
    public void batchDeleteDutyPerson(List<Object> userIdList );
    
    /**
     * 
     * @description:更新值别表
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param Duty:
     * @return int 更新个数
     */
    public int updateDuty(Duty Duty);
    
    /**
     * 
     * @description:通过Id拿到值别表
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param id
     * @return:Duty
     */
    public Duty queryDutyById(int id);
    
    /**
     * 
     * @description:通过ID 删除 duty
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param id:
     * @return 
     */
    public int deleteDutyById(int id);

    /**
     * 
     * @description:更新duty
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param DutyMap:
     */
    public void updateDutyByMap(HashMap<?, ?> DutyMap);
    
    /**
     * 
     * @description:批量插入
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param list:
     */
    public void batchInsertDuty(List<Duty> list);   
    
    /**
     * 
     * @description:duty 分页
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param page
     * @return:
     */
    public List<Duty> queryDutyByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:高级搜索 查询值别列表
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param page
     * @return: List<Duty>
     */
    public List<Duty> queryDutyBySearch(Page<HashMap<?, ?>> page );
    
    /**
     * 
     * @description:duty 分页 (返回hashmap)
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param page
     * @return: List<HashMap<?, ?>>
     */
    public List<HashMap<?, ?>> queryDutyMapByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:拿出所有值别duty
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @return:
     */
    public List<Duty> queryAllDuty();
    
    /**
     * 
     * @description:通过hashmap 拿到List
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param map
     * @return:List<Duty>
     */
    public List<Duty> queryDutyByMap(HashMap<?, ?> map);

    /**
     * 
     * @description:通过岗位ID查找值别
     * @author: fengzt
     * @createDate: 2014年6月11日
     * @param stationId
     * @return:List<Duty>
     */
    public List<Duty> queryDutyByStationId(String stationId);

    /**
     * 
     * @description:通过当站点取岗位信息
     * @author: fengzt
     * @createDate: 2014年6月24日
     * @param roleId
     * @return: List<RoleVo>
     */
    public List<RoleVo> queryStationInfoBySitId(String roleId);

    /**
     * 
     * @description:通过UUID查找dutyList
     * @author: fengzt
     * @createDate: 2014年7月28日
     * @param uuid
     * @return:List<Duty>
     */
    public List<Duty> queryDutyByUuid(String uuid);

    /**
     * 
     * @description:通过岗位拿到sortType
     * @author: fengzt
     * @createDate: 2014年7月29日
     * @param stationId
     * @return:String
     */
    public String querySortTypeByStationId(String stationId);
    
    /**
     * 查询指定站点指定时间内人员的排班情况
     * @param siteId 为空则查询所有站点
     * @param startDate 为空则开始时间无限制
     * @param endDate 为空则结束时间无限制
     * @return 以日期_用户id为key的map
     */
    @MapKey("flag")
    Map<String, DutyPersonShiftVo>queryDutyPersonAndShiftBySiteAndTime(@Param("siteId")String siteId,@Param("userId")String userId,
    		@Param("startDate")String startDate,@Param("endDate")String endDate);
    
    /**
     * 查询指定站点运行人员的id列表
     * 去重
     * @param siteId
     * @return
     */
    public List<String>queryOprPersonIdBySite(@Param("siteId")String siteId);
    
    /**
     * 查询关系用户
     * @param id
     * @return
     */
    public List<String> queryOrgsRelatedToUsers(@Param("id")int id);
    
    /**
     * 
     * @description:判断值别选人用户是否已经存在其他值别里
     * @author: zhuw
     * @createDate: 2016年1月26日
     * @param userId siteId
     * @return: String
     */
    public String hasDutyPerson(@Param("userId")String userId, @Param("siteId")String siteId);
    
    /**
     * 查询指定值别的人员信息
     * 多条件指定查询
     * @param dutyId
     * @param siteId
     * @param userId
     * @param userKw 模糊匹配工号或用户名
     * @return
     */
    List<PersonJobs> queryDutyPersons(@Param("dutyId")Integer dutyId,@Param("siteId")String siteId,
    		@Param("userId")String userId,@Param("userKw")String userKw);
}
