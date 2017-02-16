package com.timss.operation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.operation.bean.PersonJobs;
import com.timss.operation.vo.PersonDutyVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: PersonJobsMapper 
 * @description: mybatis 接口
 * @company: gdyd
 * @className: PersonJobsMapper.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Deprecated
public interface PersonJobsDao {
	
    /**
     * @description:插入一条人员值别
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param personJobs  其中id为自增，不需要设置
     * @return 
     */
    public int insertPersonJobs(PersonJobs personJobs );
    
    /**
     * 
     * @description:更新人员值别表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param PersonJobs:
     * @return int 更新个数
     */
    public int updatePersonJobs(PersonJobs PersonJobs);
    
    /**
     * 
     * @description:通过Id拿到人员值别表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param id
     * @return:PersonJobs
     */
    public PersonJobs queryPersonJobsById(int id);
    
    /**
     * 
     * @description:通过ID 删除 personJobs
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param personDutyVo:
     * @return 
     */
    public int deletePersonJobs(PersonDutyVo personDutyVo);

    /**
     * 
     * @description:personJobs 分页
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param page
     * @return:
     */
    public List<PersonDutyVo> queryPersonJobsByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:高级搜索 查询人员值别列表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param page
     * @return: List<PersonJobs>
     */
    public List<PersonDutyVo> queryPersonJobsBySearch(Page<HashMap<?, ?>> page );
    
    /**
     * 
     * @description:拿出所有人员值别personJobs
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @return:
     */
    public List<PersonJobs> queryAllPersonJobs();

    /**
     * 
     * @description:批量插入
     * @author: fengzt
     * @createDate: 2014年7月10日
     * @param perJobsList
     * @return:int
     */
    public int batchInsertPersonJobs(List<PersonJobs> perJobsList);

    /**
     * 
     * @description:通过userId删除 in 的方式
     * @author: fengzt
     * @createDate: 2014年7月11日
     * @param paramsMap
     * @return:int 
     */
    public int deletePersonJobsByUserId(HashMap<String, Object> paramsMap);

    /**
     * 
     * @description:查找详细通过 jobsId dutyId stationId
     * @author: fengzt
     * @createDate: 2014年7月14日
     * @param vo
     * @return:Map<String, Object>
     */
    public List<PersonJobs> queryAllPersonJobsByPersonDutyVo(PersonDutyVo vo);

    /**
     * 
     * @description:通过工号获取personJobs
     * @author: huanglw
     * @createDate: 2014年7月14日
     * @param userId
     * @return:
     */
    public List<PersonJobs> queryPersonJobsByUserId(String userId);

    /**
     * 
     * @description:找到属于同一值别的员工
     * @author: huanglw
     * @createDate: 2014年7月16日
     * @param dutyId
     * @return:
     */
    public List<PersonJobs> queryPersonJobsByDutyId(int dutyId);

    /**
     * 
     * @description:日志查询页面通过岗位和工种查询人员，工种可选
     * @author: huanglw
     * @createDate: 2014年7月31日
     * @param paramsMap
     * @return:List<PersonJobs>
     */
    public List<PersonJobs> queryPersonInfoHistory(Map<String, Object> paramsMap);

    /**
     * 
     * @description:通过岗位id找到所有同岗位人员
     * @author: huanglw
     * @createDate: 2014年7月31日
     * @param stationId
     * @return:List<PersonJobs>
     */
    public List<PersonJobs> queryPersonJobsByStationId(String stationId);

}
