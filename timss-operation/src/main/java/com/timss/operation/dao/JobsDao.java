package com.timss.operation.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.operation.bean.Jobs;
import com.timss.operation.bean.PersonJobs;
import com.timss.operation.vo.ModeListVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: JobsMapper 
 * @description: mybatis 接口
 * @company: gdyd
 * @className: JobsMapper.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface JobsDao {
	
    /**
     * @description:插入一条工种
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param jobs  其中id为自增，不需要设置
     * @return 
     */
    public int insertJobs(Jobs jobs );
    
    /**
     * 
     * @description:批量增加岗位用户
     * @author: yyn
     * @createDate: 2016年2月17日
     * @param userIdList
     */
    public int batchInsertJobsPerson(List<Object> userIdList );
    
    /**
     * 
     * @description:批量删除岗位用户,userIdList不能为空，否则报错
     * @author: yyn
     * @createDate: 2016年2月17日
     * @param userIdList
     */
    public int batchDeleteJobsPerson(List<Object> userIdList );
    
    /**
     * 
     * @description:更新工种表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param Jobs:
     * @return int 更新个数
     */
    public int updateJobs(Jobs Jobs);
    
    /**
     * 
     * @description:通过Id拿到工种表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param id
     * @return:Jobs
     */
    public Jobs queryJobsById(int id);
    
    /**
     * 
     * @description:通过ID 删除 jobs
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param id:
     * @return 
     */
    public int deleteJobsById(int id);

    /**
     * 
     * @description:jobs 分页
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param page
     * @return:
     */
    public List<Jobs> queryJobsByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:高级搜索 查询工种列表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param page
     * @return: List<Jobs>
     */
    public List<Jobs> queryJobsBySearch(Page<HashMap<?, ?>> page );
    
    /**
     * 
     * @description:拿出所有工种jobs
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @return:
     */
    public List<Jobs> queryAllJobs();

    /**
     * 
     * @description:通过岗位ID拿到工种
     * @author: fengzt
     * @createDate: 2014年7月10日
     * @param stationId
     * @return:List<Jobs> 
     */
    public List<Jobs> queryJobsByStationId(String stationId);

    /**
     * 
     * @description:通过jobId查询工种、岗位信息
     * @author: fengzt
     * @createDate: 2015年10月30日
     * @param jobId
     * @return:ModeListVo
     */
    public ModeListVo queryJobDeptByJobId(int jobId);
    
    /**
     * 查询关系用户
     * @param id
     * @return
     */
    public List<String> queryOrgsRelatedToUsers(@Param("id")int id);
    
    /**
     * 查询用户所属的岗位
     * @param userId
     * @return
     */
    List<Jobs>queryJobsByUserId(@Param("userId")String userId);
    
    /**
     * 查询指定岗位的人员信息
     * @param jobsId
     * @return
     */
    List<PersonJobs> queryJobsPersons(@Param("jobsId")int jobsId);
}
