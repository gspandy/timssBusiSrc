package com.timss.operation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.operation.bean.Jobs;
import com.timss.operation.bean.Patrol;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 点检日志service
 * @description: {desc}
 * @company: gdyd
 * @className: PatrolService.java
 * @author: fengtw
 * @createDate: 2015年10月29日
 * @updateUser: fengtw
 * @version: 1.0
 */
public interface PatrolService {

    /**
     * 
     * @description:插入/更新点检日志
     * @author: fengtw
     * @createDate: 2015年10月30日
     * @param 点检日志bean patrol
     * @return:
     */
    Map<String, Object> insertOrUpdatePatrol(String formData);

    /**
     * 
     * @description:提交点检日志
     * @author: fengtw
     * @createDate: 2015年11月3日
     * @param 点检日志bean patrol
     * @return:
     */
	Map<String, Object> insertPatrol(String formData);

    /**
     * 
     * @description:删除点检日志
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param id
     * @return:
     */
    int deletePatrolById(String patrolId);
    
    /**
     * 
     * @description:更新点检日志
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param patrol
     * @return:
     */
    int updatePatrol(Patrol patrol);

    /**
     * 
     * @description:通过id查询点检日志
     * @author: fengtw
     * @createDate: 2015年10月30日
     * @param id
     * @return:
     */
    Map<String, Object> queryPatrolById(String patrolId);
    
    /**
     * 
     * @description:点击日志分页
     * @author: fengtw
     * @createDate: 2015年11月2日
     * @param pageVo
     * @return:
     */
    List<Patrol> queryPatrolByPage(Page<HashMap<?, ?>> pageVo);
    
    /**
     * 
     * @description:通过高级搜索查询点检日志
     * @author: fengtw
     * @createDate: 2015年10月29日
     * @param map
     * @param pageVo
     * @return:
     */
    List<Patrol> queryPatrolBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:作废流程
     * @author: fengtw
     * @createDate: 2015年11月4日
     * @param map
     * @param pageVo
     * @return:
     */
	int invalidPatrol(String patrolId);


}
