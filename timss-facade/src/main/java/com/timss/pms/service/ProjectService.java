package com.timss.pms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.workflow.service.WorkflowService;
import com.timss.pms.bean.Milestone;
import com.timss.pms.bean.Outsourcing;
import com.timss.pms.bean.Project;
import com.timss.pms.bean.Workload;
import com.timss.pms.vo.ProjectBudgetVo;
import com.timss.pms.vo.ProjectDtlVo;
import com.timss.pms.vo.ProjectVo;
import com.timss.pms.vo.ProjectWFVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @ClassName:     ProjectService
 * @company: gdyd
 * @Description:项目立项service类
 * @author:    黄晓岚
 * @date:   2014-6-20 上午10:53:34
 */
public interface ProjectService extends FlowVoidService{
	/**
	 * 
	 * @Title: queryProjectList
	 * @Description: 查询项目立项信息
	 * @param: @param page 查询条件，包括分页信息
	 * @param: @param userInfo 用户信息
	 * @return: Page<Project>  包含的分页信息的项目立项信息
	 * @throws
	 */
	Page<ProjectVo> queryProjectList(Page<ProjectVo> page,UserInfoScope userInfo);
	
	/**
	 * 
	 * @Title: insertProject
	 * @Description: 插入一个项目立项
	 * @param: @param project  项目立项bean类
	 * @return: void   
	 * @throws
	 */
	void insertProject(Project project);
	
	/**
	 * 
	 * @Title: tmpInsertProject
	 * @Description: 暂存一个项目立项
	 * @param: @param project  项目立项bean类
	 * @return: void   
	 * @throws
	 */
	void tmpInsertProject(Project project,List<Milestone> milestones);
	
	void tmpInsertProject(Project project,List<Milestone> milestones,List<Outsourcing> outsourcings,List<Workload> workloads);
	
	/**
	 * 
	 * @Title: insertProjectWithWorkflow
	 * @Description: 插入一个项目立项，并启动审批流程
	 * @param: @param project  项目立项bean类
	 * @return: void   
	 * @throws
	 */
	HashMap insertProjectWithWorkflow(Project project,List<Milestone> milestones);
	
	HashMap insertProjectWithWorkflow(Project project,List<Milestone> milestones,List<Outsourcing> outsourcings,List<Workload> workloads);
	
	void updateProjectApproving(Project project);
	
	void updateProjectApproving(Integer projectId);
	
	void updateProjectApproved(Integer projectId);
	
	/**
	 * 
	 * @Title: updateProject
	 * @Description: 修改项目立项信息
	 * @param project
	 */
	void updateProject(Project project);
	
	void updateProject(Project project,List<Milestone> milestones);
	
	void updateProject(Project project,List<Milestone> milestones,List<Outsourcing> outsourcings,List<Workload> workloads);
	
	/**
	 * 
	 * @Title: tmpUpdateProject
	 * @Description: 修改项目立项信息，只是暂存
	 * @param project
	 */
	void tmpUpdateProject(Project project,List<Milestone> milestones);
	
	void tmpUpdateProject(Project project,List<Milestone> milestones,List<Outsourcing> outsourcings,List<Workload> workloads);
	
	
	
	/**
	 * 
	 * @Title: queryProjectById
	 * @Description: 根据id查询项目立项
	 * @param id
	 * @return
	 */
	ProjectDtlVo queryProjectById(String id);
	
	/**
	 * 
	 * @Title: queryProcessInstIdById
	 * @Description: 根据流程id查询项目信息
	 * @param id
	 * @return
	 */
	ProjectWFVo queryProcessInstIdById(String id,String taskId);
	
	/**
	 * 
	 * @Title: deleteProject
	 * @Description: 根据id删除项目立项
	 * @param id
	 * @return
	 */
	int deleteProject(String id);
	
	/**
	 * 设置项目立项工作流流程变量
	 * @Title: setWFVariable
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param taskId
	 * @param processInstId
	 */
	void setWFVariable(String taskId,String processInstId,Project project);
	
	/**
	 * 根据项目id，查询项目基本信息以及项目预算的使用信息
	 * @Title: queryProjectBudgetById
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	ProjectBudgetVo queryProjectBudgetById(String id);
	
	/**
	 * 终止项目立项流程，并修改项目状态
	 * @Title: stopWorkflow
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param project
	 * @param procInstId
	 * @return
	 */
	int stopWorkflow(Project project,String processInstId,String reason);
	
	/**
	 * 判断当前站点下编号是否存在,排除统一
	 * @Title: isProjectCodeExisted
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param projectCode
	 * @return
	 */
	boolean isProjectCodeExisted(String projectCode,String projectId);
	
	/**
	 * 
	 * @Title: queryProjectDetailById
	 * @Description: 根据id查询项目信息，包含验收，结算等信息
	 * @param id
	 * @return
	 */
	ProjectDtlVo queryProjectDetailById(String id);
	
	Page<ProjectVo> queryProjectDetailList(Page<ProjectVo> page,UserInfoScope userInfo);
	
	/**
	 * 
	 * @Title: queryProjectListByKeyWord
	 * @Description: 根据关键字，即项目名称模块查询最多10条记录
	 * @param kw
	 * @return
	 */
	public List<Map> queryProjectListByKeyWord(String kw);
	
	
	boolean isProjectNameExisted(String projectCode,String projectId);

	/**
	 * 根据年份和项目类型查询对应的项目列表
	 * @Title: queryProjectByYearAndType
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param year
	 * @param property
	 * @param siteid
	 * @return
	 */
	List<ProjectVo> queryProjectByYearAndProperty(String year, String property,
			String siteid);
	
	
	List<ProjectVo> queryProjectTotal(String year,String siteid);
	
	/**
	 * @Title:generateNewProjectCode
	 * @Description:生产成新的项目编号
	 * @param prefix
	 * @return String
	 * @throws
	 */
	String generateNewProjectCode(String prefix);
	/**
         * generateNewFlowNo
         * @Description:新的流水号
         * @param prefix
         * @return String
         * @throws
         */
        String generateNewFlowNo(String prefix);
        /**
         * @Title: isFlowNoExisted
         * @Description:判断当前站点下编号是否存在,排除统一 
         * @param flowNo
         * @return
         */
        boolean isFlowNoExisted(String flowNo,String projectId);
}
