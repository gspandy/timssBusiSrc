package com.timss.operation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.operation.bean.PersonJobs;
import com.timss.operation.vo.PersonDutyVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 人员值别service
 * @description: {desc}
 * @company: gdyd
 * @className: PersonJobsService.java
 * @author: fengzt
 * @createDate: 2014年7月7日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Deprecated
public interface PersonJobsService {

    /**
     * 
     * @description:插入人员值别
     * @author: fengzt
     * @param personJobs 
     * @param treeMap 
     * @createDate: 2014年7月4日
     * @param 人员值别bean personJobs
     * @return:
     */
    int insertPersonJobs(PersonJobs personJobs, HashMap<String, Object> treeMap);

    /**
     * 
     * @description:通过高级搜索查询人员值别
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param map
     * @param pageVo
     * @return:
     */
    List<PersonDutyVo> queryPersonJobsBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:人员值别分页
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param pageVo
     * @return:
     */
    List<PersonDutyVo> queryPersonJobsByPage(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:通过ID查询人员值别
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param id
     * @return:
     */
    PersonJobs queryPersonJobsById(int id);
    
    /**
     * 
     * @description:更新人员值别
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param personDutyVo
     * @param userAdd 
     * @param userDel 
     * @return:int
     */
    int updatePersonJobs(PersonDutyVo personDutyVo, String userDel, String userAdd);

    /**
     * 
     * @description:删除人员值别
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param personDutyVo
     * @return:
     */
    int deletePersonJobs(PersonDutyVo personDutyVo);

    /**
     * 
     * @description:查找详细通过 jobsId dutyId stationId
     * @author: fengzt
     * @createDate: 2014年7月14日
     * @param vo
     * @return:Map<String, Object>
     */
    Map<String, Object> queryOrgsRelatedToUsers(PersonDutyVo vo);

    /**
     * 
     * @description:通过userId查找员工值别和工种
     * @author: huanglw
     * @createDate: 2014年7月14日
     * @param userId
     * @return:
     */
    List<PersonJobs> queryPersonJobsByUserId(String userId);

    /**
     * @description:查询某个值别下的所有人员
     * @author: huanglw
     * @createDate: 2014年7月16日
     * @param dutyId
     * @return:List<PersonJobs>
     */
    List<PersonJobs> queryPersonJobsByDutyId(int dutyId);

    /**
     * 
     * @description:查询某个岗位下的所有人员
     * @author: huanglw
     * @createDate: 2014年7月31日
     * @param stationId
     * @return:List<PersonJobs>
     */
    List<PersonJobs> queryPersonJobsByStationId(String stationId);
}
