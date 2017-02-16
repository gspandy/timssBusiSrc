package com.timss.operation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.operation.bean.Duty;
import com.timss.operation.bean.PersonJobs;
import com.timss.operation.vo.DutyPersonShiftVo;
import com.timss.operation.vo.RoleVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 值别Service
 * @description: 
 * @company: gdyd
 * @className: DutyService.java
 * @author: fengzt
 * @createDate: 2014年6月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface DutyService {

    /**
     * 
     * @description:新建一个值别
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param duty  其中id为自增，不需要设置
     * @return:Map
     * @throws Exception 
     */
    public Map<String, Object> insertDuty(Duty duty, HashMap<String, Object> treeMap ) throws Exception;
    
    /**
     * 
     * @description:更新值别表
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param Duty userDel userAdd
     * @return int
     * @throws Exception 
     */
    public String updateDuty(Duty Duty, String userDel, String userAdd) throws Exception;
    
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
     * @return:List<Duty>
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
     * @description:值别列表 高级搜索
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param map
     * @param pageVo
     * @return:
     */
    public List<Duty> queryDutyBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);

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
     * @param siteId
     * @return: List<RoleVo>
     */
    public List<RoleVo> queryStationInfoBySitId(String siteId);

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
     * @return:int
     */
    public int querySortTypeByStationId(String stationId);
    
    /**
     * 查询指定日期指定站点人员的排班情况
     * @param siteId 为空则查询所有站点
     * @param startDate yyyy-MM-dd的日期或以此开头的时间（会截取日期部分）为空则开始时间无限制
     * @param endDate yyyy-MM-dd的日期或以此开头的时间（会截取日期部分）为空则结束时间无限制
     * @return 以日期_用户id为key的map
     * @throws Exception
     */
    Map<String, DutyPersonShiftVo>queryDutyPersonAndShiftBySiteAndTime(String siteId,String userId,String startDateStr,String endDateStr)throws Exception;
    
    /**
     * 查询站点的运行人员id，逗号分隔，用于判断是否运行人员
     * @param siteId
     * @return
     * @throws Exception
     */
    String queryOprPersonsBySite(String siteId)throws Exception;
    
    /**
     * 
     * @description:列表双击跳转修改页面查询关系用户
     * @author: zhuw
     * @createDate: 2016年1月22日
     * @param id
     * @return:Map
     */
    public Map<String, Object> queryOrgsRelatedToUsers(int id);
    
    /**
     * 查询指定值别的人员信息
     * 多条件指定查询
     * @param dutyId
     * @param siteId
     * @param userId
     * @param userKw 模糊匹配工号和用户名
     * @return
     */
    List<PersonJobs> queryDutyPersons(Integer dutyId,String siteId,String userId,String userKw)throws Exception;
    
    /**
     * 多条件指定查询运行人员，用于hint组件
     * @param dutyId
     * @param siteId
     * @param userKw 模糊匹配
     * @return
     * @throws Exception
     */
    List<Map<String, Object>>queryDutyPersonsForHint(Integer dutyId,String siteId,String userKw)throws Exception;
}
