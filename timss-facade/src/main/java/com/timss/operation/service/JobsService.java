package com.timss.operation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.operation.bean.Jobs;
import com.timss.operation.bean.PersonJobs;
import com.timss.operation.vo.ModeListVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 工种service
 * @description: {desc}
 * @company: gdyd
 * @className: JobsService.java
 * @author: fengzt
 * @createDate: 2014年7月7日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface JobsService {

    /**
     * 
     * @description:插入工种
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param 工种bean jobs
     * @return:
     */
	Map<String, Object> insertJobs(Jobs jobs, HashMap<String, Object> treeMap);

    /**
     * 
     * @description:通过高级搜索查询工种
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param map
     * @param pageVo
     * @return:
     */
    List<Jobs> queryJobsBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:工种分页
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param pageVo
     * @return:
     */
    List<Jobs> queryJobsByPage(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:通过ID查询工种
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param id
     * @return:
     */
    Jobs queryJobsById(int id);
    
    /**
     * 
     * @description:更新工种
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param jobs
     * @return:
     */
    String updateJobs(Jobs jobs, String userDel, String userAdd);

    /**
     * 
     * @description:删除工种
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param id
     * @return:
     */
    int deleteJobsById(int id);

    /**
     * 
     * @description:通过岗位ID拿到工种
     * @author: fengzt
     * @createDate: 2014年7月10日
     * @param stationId
     * @return:List<Jobs> 
     */
    List<Jobs> queryJobsByStationId(String stationId);

    /**
     * 
     * @description:通过jobId查询工种、岗位信息
     * @author: fengzt
     * @createDate: 2015年10月30日
     * @param jobId
     * @return:ModeListVo
     */
    ModeListVo queryJobDeptByJobId(int jobId);
    
    /**
     * 
     * @description:列表双击跳转修改页面查询关系用户
     * @author: yyn
     * @createDate: 2016年2月18日
     * @param id
     * @return:Map
     */
    Map<String, Object> queryOrgsRelatedToUsers(int id);
    
    /**
     * 查询用户所属的岗位id字符串
     * @param userId
     * @return
     * @throws Exception
     */
    String queryJobsIdStrByUserId(String userId)throws Exception;
    
    /**
     * 查询指定岗位中的人
     * @param jobsId
     * @return
     */
    List<PersonJobs> queryJobsPersons(int jobsId);
}
