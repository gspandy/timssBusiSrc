package com.timss.pms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.pms.bean.Project;
import com.timss.pms.vo.ProjectDtlVo;
import com.timss.pms.vo.ProjectVo;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;
/**
 * 
 * @ClassName:     ProjectDao
 * @company: gdyd
 * @Description:项目立项dao接口类
 * @author:    黄晓岚
 * @date:   2014-6-20 上午11:25:07
 */
public interface ProjectDao {
	/**
	 * 
	 * @Title: retrivePlanList
	 * @Description: 查询项目立项信息
	 * @param page 查询的条件
	 * @return: List<Project>  查询结果
	 * @throws
	 */
	List<ProjectVo> queryProjectList(Page<ProjectVo> page);
	
	/**
	 * 
	 * @Title: queryProjectListAndFilter
	 * @Description: 查询项目立项信息，然后限制审批人员才能查看
	 * @param page 查询的条件
	 * @return: List<Project>  查询结果
	 * @throws
	 */
	@RowFilter(flowIdColumn="flowid",exclusiveRule="PMS_EXCLUDE",exclusiveDeptRule="PMS_DEPT_EXCLUDE",isRouteFilter=true)
	List<ProjectVo> queryProjectListAndFilter(Page<ProjectVo> page);
	
	/**
	 * 
	 * @Title: retrivePlanList
	 * @Description: 查询年度计划所属的项目立项信息
	 * @param planId 年度计划id
	 * @return: List<Project>  查询结果
	 * @throws
	 */
	List<ProjectVo> queryProjectListByPlanId(int planId);
	
	/**
	 * 
	 * @Title: insertProject
	 * @Description: 新建项目立项，id自动生成
	 * @param Project  插入项目立项信息，
	 * @return: void   
	 * @throws
	 */
	int insertProject(Project project);
	
	/**
	 * 
	 * @Title: updateProject
	 * @Description: 修改项目立项信息
	 * @param Project 项目立项信息
	 * @return
	 */
    int updateProject(Project project);
    
    /**
     * 
     * @Title: queryProjectById
     * @Description: 根据id查询项目立项
     * @param id
     * @return
     */
    ProjectDtlVo queryProjectById(int id);
    
    /**
     * 
     * @Title: deleteProject
     * @Description: 根据id删除项目立项,物理删除
     * @param id
     * @return
     */
    int deleteProject(int id);
    
    /**
     * 查询符合条件的项目数量
     * @Title: selectByCodeAndSiteid
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param projectCode
     * @param siteid
     * @return
     */
    int selectByCodeAndSiteid(@Param("projectCode")String projectCode,@Param("siteid") String siteid);
    
    int selectByNameAndSiteid(@Param("projectName")String projectName,@Param("siteid") String siteid);
    
    /**
     * 查询项目的实际结算值
     * @Title: queryProjectPaySumById
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param projectId
     * @return
     */
    Double queryProjectPaySumById(int projectId);
    
    /**
     * 根据年份查询项目的统计信息，包括项目的总金额，总结算金额
     * @Title: queryProjectDetailByYear
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param pyear
     * @param stieid
     * @return
     */
    List<ProjectVo> queryProjectDetailByYear(@Param("pyear")String pyear,@Param("siteid")String siteid);
    
    /**
     * @Title:queryProjectListWithCodePrefix
     * @Description:根据项目编号前缀，查询项目列表
     * @param prefix
     * @return List<ProjectVo>
     * @throws
     */
    List<ProjectVo> queryProjectListWithCodePrefix(@Param("prefix")String prefix);
    
    /**
     * @Title:queryProjectListWithFlowPrefix
     * @Description:根据项目流水号前缀，查询项目列表
     * @param prefix
     * @return List<ProjectVo>
     * @throws
     */
    List<ProjectVo> queryProjectListWithFlowNoPrefix(@Param("prefix")String prefix);
    
    /**
     * @Title: selectByFlowNoAndSiteid
     * @Description: 查询符合条件的项目数量
     * @param flowNo
     * @param siteid
     * @return
     */
    int selectByFlowNoAndSiteid(@Param("flowNo")String flowNo,@Param("siteid") String siteid);

}
