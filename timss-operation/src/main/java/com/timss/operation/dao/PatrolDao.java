package com.timss.operation.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.operation.bean.Patrol;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: PatrolMapper 
 * @description: mybatis 接口
 * @company: gdyd
 * @className: PatrolMapper.java
 * @author: fengtw
 * @createDate: 2015年10月30日
 * @updateUser: fengtw
 * @version: 1.0
 */
public interface PatrolDao {
	
    /**
     * @description:插入一条点检日志
     * @author: fengtw
     * @createDate: 2015年10月30日
     * @param：Patrol
     * @return 
     */
    public int insertPatrol(Patrol patrol);
    
    /**
     * 
     * @description:通过id删除点检日志
     * @author: fengtw
     * @createDate: 2015年10月30日
     * @param：id
     * @return 
     */
    public int deletePatrolById(String patrolId);
    
    /**
     * 
     * @description:更新点检日志
     * @author: fengtw
     * @createDate: 2015年10月30日
     * @param： Patrol
     * @return： int 更新个数
     */
    public int updatePatrol(Patrol patrol);
    
    /**
     * 
     * @description:更新点检日志流程信息
     * @author: fengtw
     * @createDate: 2015年11月3日
     * @param： Patrol
     * @return： int 更新个数
     */
    public int updatePatrolStatus(@Param("patrol") HashMap<String, Object> parmas);
    
    /**
     * 
     * @description:通过Id拿到点检日志
     * @author: fengtw
     * @createDate: 2015年10月30日
     * @param： id
     * @return: Patrol
     */
    public Patrol queryPatrolById(String patrolId);

    /**
     * 
     * @description:点检日志分页
     * @author: fengtw
     * @createDate: 2015年10月30日
     * @param: page
     * @return
     */
    public List<Patrol> queryPatrolByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:高级搜索 查询工种列表
     * @author: fengtw
     * @createDate: 2015年10月30日
     * @param:page
     * @return: List<Patrol>
     */
    public List<Patrol> queryPatrolBySearch(Page<HashMap<?, ?>> page );
    
    /**
     * 
     * @description:拿出所有点检日志
     * @author: fengtw
     * @createDate: 2015年10月30日
     * @return:
     */
    public List<Patrol> queryAllPatrol();

}
