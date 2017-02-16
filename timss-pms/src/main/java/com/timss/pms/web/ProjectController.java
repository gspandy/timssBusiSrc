package com.timss.pms.web;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timss.pms.bean.FlowVoidParamBean;
import com.timss.pms.bean.Milestone;
import com.timss.pms.bean.Outsourcing;
import com.timss.pms.bean.Project;
import com.timss.pms.bean.Workload;
import com.timss.pms.service.MilestoneService;
import com.timss.pms.service.PrivilegeService;
import com.timss.pms.service.ProjectQueryService;
import com.timss.pms.service.ProjectService;
import com.timss.pms.service.SupplierService;
import com.timss.pms.util.CreateReturnMapUtil;
import com.timss.pms.util.GetBeanFromBrowerUtil;
import com.timss.pms.vo.ProjectDtlVo;
import com.timss.pms.vo.ProjectVo;
import com.timss.pms.vo.ProjectWFVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
/**
 * 
 * @ClassName: ProjectController
 * @company: gdyd
 * @Description:项目立项控制器类
 * @author: 黄晓岚
 * @date: 2014-6-20 上午10:52:40
 */
@Controller
@RequestMapping(value = "pms/project")
public class ProjectController  {
	
	private static final Logger LOGGER=Logger.getLogger(ProjectController.class);
	@Autowired
	private ProjectService projectService;

	@Autowired
	private ItcMvcService itcMvcService;
	
	@Autowired
	private PrivilegeService privilegeService;
        
	@Autowired
	private MilestoneService milestoneService;
        
        @Autowired
        private ProjectQueryService projectQueryService;
        
        @Autowired
        private IAuthorizationManager iAuthorizationManager; 

        @Autowired
        private SupplierService supplierService;
	/**
	 * 
	 * @Title: projectList
	 * @Description: 转发请求到项目立项列表页
	 * @param: @return
	 * @return: String
	 * @throws
	 */
	@RequestMapping(value = "/projectList")
	@ReturnEnumsBind(value="PMS_PROJECT_SUBCOMP")
	public String projectList() {
		return "project/projectList.jsp";
	}
	/**
	 * 
	 * @Title: projectList
	 * @Description: 转发请求到项目统计列表页
	 * @param: @return
	 * @return: String
	 * @throws
	 */
	@RequestMapping(value = "/detailProjectList")
	public String detailProjectList() {
		return "project/detailProjectList.jsp";
	}
	
	/**
	 * 
	 * @Title: projectList
	 * @Description: 转发请求到 项目详细页面，包含合同，结算，验收等信息
	 * @param: @return
	 * @return: String
	 * @throws
	 */
	@RequestMapping(value = "/showDetailProject")
	@ReturnEnumsBind(value="pms_project_type,pms_project_property,PMS_WORKLOAD_TYPE,PMS_CONTRACTING_MODE,PMS_CONTRACT_AWARD")
	public String showDetailProject() {
		return "project/showDetailProject.jsp";
	}
	
	@RequestMapping(value = "/produceProjectCode")
	@ReturnEnumsBind(value="PMS_PCODE_TYPE")
	public String produceProjectCodeJsp() {
		return "project/produceProjectCode.jsp";
	}

	/**
	 * 
	 * @Title: insertProjectJsp
	 * @Description: 转发到新建项目立项页面
	 * @return
	 */
	@RequestMapping(value = "/addProjectJsp")
	@ReturnEnumsBind(value="pms_project_type,pms_project_property,PMS_WORKLOAD_TYPE,PMS_CONTRACTING_MODE,PMS_CONTRACT_AWARD")
	public String insertProjectJsp() {
		return "project/addProject.jsp";
	}
	/**
	 * 
	 * @Title: insertProjectJsp
	 * @Description: 转发到项目立项审批页面
	 * @return
	 */
	@RequestMapping(value = "/approveProjectJsp")
	@ReturnEnumsBind(value="pms_project_type,pms_project_property,PMS_CONTRACTING_MODE,PMS_CONTRACT_AWARD")
	@Deprecated
	public String approveProjectJsp() {
		return "project/approveProject.jsp";
	}

	/**
	 * 
	 * @Title: editProjectJsp
	 * @Description: 转发到查看项目立项详细信息页面
	 * @return
	 */
	@RequestMapping(value = "/editProjectJsp")
	@ReturnEnumsBind(value="pms_project_type,pms_project_property,PMS_WORKLOAD_TYPE,PMS_CONTRACTING_MODE,PMS_CONTRACT_AWARD")
	public String editProjectJsp(String id) {
		return "project/editProject.jsp?id=" + id;
	}

	/**
	 * 
	 * @Title: projectListData
	 * @Description: 查询项目立项信息入口
	 * @param projectName
	 *            要查询的项目立项的名称，采用模糊搜索
	 * @return: Page<Project> 项目立项信息及分页信息
	 * @throws
	 */
	@RequestMapping(value = "/projectListData", method = RequestMethod.POST)
	public Page<ProjectVo> projectListData(String projectName) throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		Page<ProjectVo> page = userInfoScope.getPage();

		// 查询参数处理
		Map<String, String[]> params = userInfoScope.getParamMap();
		// 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
		Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap("projectMap", "pms", "ProjectDao");
		
		
		if (params.containsKey("search")) {
			String fuzzySearchParams = userInfoScope.getParam("search");
			LOGGER.info("查询项目立项的条件：" + fuzzySearchParams);

			// 调用工具类将jsonString转为HashMap
			Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap(fuzzySearchParams);
					

			// 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
			fuzzyParams = MapHelper.fromPropertyToColumnMap((HashMap<String, Object>) fuzzyParams,
					propertyColumnMap);
			if ( null!=fuzzyParams.get( "company_id" ) ) {
                            fuzzyParams.put( "company_name", fuzzyParams.get( "company_id" ).toString() );
                            fuzzyParams.remove( "company_id" );
                        }
			if ( null!=fuzzyParams.get( "property" )){
                            fuzzyParams.put( "propertyName", fuzzyParams.get( "property" ).toString() );
                            fuzzyParams.remove( "property" );
                        }
                        if ( null!=fuzzyParams.get( "start_time" )){
                            fuzzyParams.put( "timeArrange", fuzzyParams.get( "start_time" ).toString() );
                            fuzzyParams.remove( "start_time" );
                        }
                        if ( null!=fuzzyParams.get( "statusValue" )){
                            fuzzyParams.put( "statusName", fuzzyParams.get( "statusValue" ).toString() );
                            fuzzyParams.remove( "statusValue" );
                        }
			// 自动的会封装模糊搜索条件
			page.setFuzzyParams(fuzzyParams);

		}

		// 设置排序内容
		if (params.containsKey("sort")) {
			String sortKey = userInfoScope.getParam("sort");
			if(StringUtils.isNotEmpty( sortKey ) &&sortKey.contains( "statusValue" )){
                            sortKey = sortKey.replace( "statusValue", "status" );
                        }
			// 如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
			propertyColumnMap.put( "status", "status" );
			sortKey = propertyColumnMap.get(sortKey);

			page.setSortKey(sortKey);
			page.setSortOrder(userInfoScope.getParam("order"));
		} else {
			// 设置默认的排序字段
			page.setSortKey("id");
			page.setSortOrder("desc");
		}
		page = projectQueryService.queryProjectListAndFilter(page, userInfoScope);
		return page;
	}
	
	/**
	 * 
	 * @Title: detailProjectListData
	 * @Description: 查询项目统计列表信息入口
	 * @return: Page<ProjectVo> 项目立项信息及分页信息
	 * @throws
	 */
	@RequestMapping(value = "/detailProjectListData")
	@Deprecated
	public Page<ProjectVo> detailProjectListData(String projectName) throws Exception {

		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		Page<ProjectVo> page = userInfoScope.getPage();

		// 查询参数处理
		Map<String, String[]> params = userInfoScope.getParamMap();
		// 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
		Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap("projectMap", "pms", "ProjectDao");
		
		
		if (params.containsKey("search")) {
			String fuzzySearchParams = userInfoScope.getParam("search");
			LOGGER.info("查询项目立项的条件：" + fuzzySearchParams);

			// 调用工具类将jsonString转为HashMap
			Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap(fuzzySearchParams);
					

			// 如果Dao中使用resultMap来将查询结果转化为bean，则需要调用此工具类将Map中的key做转换，变成数据库可以识别的列名
			fuzzyParams = MapHelper.fromPropertyToColumnMap((HashMap<String, Object>) fuzzyParams,
					propertyColumnMap);
			
			// 自动的会封装模糊搜索条件
			page.setFuzzyParams(fuzzyParams);

		}

		// 设置排序内容
		if (params.containsKey("sort")) {
			String sortKey = userInfoScope.getParam("sort");

			// 如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
			sortKey = propertyColumnMap.get(sortKey);

			page.setSortKey(sortKey);
			page.setSortOrder(userInfoScope.getParam("order"));
		} else {
			// 设置默认的排序字段
			page.setSortKey("id");
			page.setSortOrder("desc");
		}
		page = projectService.queryProjectDetailList(page, userInfoScope);

		return page;
	}
    /**
     * 根据id查询项目信息,同时查询项目的权限信息和流程信息
     * @Title: queryProjectById
     * @Description: 根据id查询项目信息
     * @param id
     * @return
     * @throws Exception
     */
	@RequestMapping(value = "/queryProjectById", method = RequestMethod.POST)
	public ModelAndViewAjax queryProjectById(String id,String processInstId) throws Exception {

		ProjectDtlVo project = projectService.queryProjectById(id);
		Map priMap=privilegeService.createProjectPrivilege(project, itcMvcService);
		
		Map map = null;
		if (project != null) {
			map = CreateReturnMapUtil.createMapWithPrivilege(CreateReturnMapUtil.SUCCESS_FLAG,
					"查询成功", project,priMap);
		} else {
			map = CreateReturnMapUtil
					.createMap(CreateReturnMapUtil.FAIL_FLAG, "查询结果为空");
		}
		return ViewUtil.Json(map);
	}

	 /**
     * 根据id查询项目信息,同时查询项目的权限信息和流程信息,还是项目的验收，结算信息
     * @Title: queryProjectById
     * @Description: 根据id查询项目信息
     * @param id
     * @return
     * @throws Exception
     */
	@RequestMapping(value = "/queryProjectDetailById", method = RequestMethod.POST)
	public ModelAndViewAjax queryProjectDetailById(String id,String processInstId) throws Exception {

		ProjectDtlVo project = projectService.queryProjectDetailById(id);
		Map priMap=privilegeService.createProjectPrivilege(project, itcMvcService);
		
		Map map = null;
		if (project != null) {
			map = CreateReturnMapUtil.createMapWithPrivilege(CreateReturnMapUtil.SUCCESS_FLAG,
					"查询成功", project,priMap);
		} else {
			map = CreateReturnMapUtil
					.createMap(CreateReturnMapUtil.FAIL_FLAG, "查询结果为空");
		}
		return ViewUtil.Json(map);
	}
	
	/**
	 * 
	 * @Title: queryProcessInstIdById
	 * @Description: 根据流程实例id查询项目信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryProcessInstIdById", method = RequestMethod.POST)
	public ModelAndViewAjax queryProcessInstIdById(String id,String taskId) throws Exception {

		ProjectWFVo project = projectService.queryProcessInstIdById(id,taskId);
		Map map = null;
		if (project != null) {
			map = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG,
					"查询成功", project);
		} else {
			map = CreateReturnMapUtil
					.createMap(CreateReturnMapUtil.FAIL_FLAG, "查询结果为空");
		}
		return ViewUtil.Json(map);
	}

	/**
	 * 
	 * @Title: insertProject
	 * @Description: 插入项目立项
	 * @param project
	 *            项目立项信息
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertProject", method = RequestMethod.POST)
	@ValidFormToken
	public ModelAndViewAjax insertProject(Project project) throws Exception {
		projectService.insertProject(project);
		Map map = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG,
				"新建项目立项成功");
		return ViewUtil.Json(map);
	}

	/**
	 * 
	 * @Title: tmpInsertProject
	 * @Description: 暂存项目立项
	 * @param project 项目立项信息
	 * @throws Exception
	 */
	@RequestMapping(value = "/tmpInsertProject", method = RequestMethod.POST)
	@ValidFormToken
	public ModelAndViewAjax tmpInsertProject() throws Exception {
		Project project=getProjectFromBrower("插入并暂存项目立项数据");
		List<Milestone> milestones=GetBeanFromBrowerUtil.getBeanListFromBrower("插入项目时获取里程碑信息：", "milestones", Milestone.class, itcMvcService);
		List<Workload> workloads=GetBeanFromBrowerUtil.getBeanListFromBrower("插入项目时获取工作量信息：", "workloads", Workload.class, itcMvcService);
		List<Outsourcing> outsourcings=GetBeanFromBrowerUtil.getBeanListFromBrower("插入项目时获取外购信息：", "outsourcings", Outsourcing.class, itcMvcService);
		projectService.tmpInsertProject(project,milestones,outsourcings,workloads);
		Map map = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG,
				"新建项目立项成功");
		return ViewUtil.Json(map);
	}

	/**
	 * 
	 * @Title: insertProjectWithWorkflow
	 * @Description: 插入项目立项,并启动流程
	 * @param project
	 *            项目立项信息
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertProjectWithWorkflow", method = RequestMethod.POST)
	@ValidFormToken
	public ModelAndViewAjax insertProjectWithWorkflow()
			throws Exception {
		Project project=getProjectFromBrower("插入并提交项目立项数据");
		List<Milestone> milestones=GetBeanFromBrowerUtil.getBeanListFromBrower("插入项目时获取里程碑信息：", "milestones", Milestone.class, itcMvcService);
		List<Workload> workloads=GetBeanFromBrowerUtil.getBeanListFromBrower("插入项目时获取工作量信息：", "workloads", Workload.class, itcMvcService);
		List<Outsourcing> outsourcings=GetBeanFromBrowerUtil.getBeanListFromBrower("插入项目时获取外购信息：", "outsourcings", Outsourcing.class, itcMvcService);
		Map map = projectService.insertProjectWithWorkflow(project,milestones,outsourcings,workloads);
		Map m = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "添加成功",
				map);
		return ViewUtil.Json(m);
	}

	/**
	 * 
	 * @Title: updateProject
	 * @Description: 修改项目立项信息，并提交
	 * @param project
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateProject", method = RequestMethod.POST)
	public ModelAndViewAjax updateProject() throws Exception {
		Project project=getProjectFromBrower("修改并提交项目立项数据");
		List<Milestone> milestones=GetBeanFromBrowerUtil.getBeanListFromBrower("修改项目时获取里程碑信息：", "milestones", Milestone.class, itcMvcService);
		List<Workload> workloads=GetBeanFromBrowerUtil.getBeanListFromBrower("插入项目时获取工作量信息：", "workloads", Workload.class, itcMvcService);
		List<Outsourcing> outsourcings=GetBeanFromBrowerUtil.getBeanListFromBrower("插入项目时获取外购信息：", "outsourcings", Outsourcing.class, itcMvcService);
		projectService.updateProject(project,milestones,outsourcings,workloads);
		Map map = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG,
				"修改项目立项成功");
		return ViewUtil.Json(map);
	}
	
	/**
	 * 
	 * @Title: updateProject
	 * @Description: 修改里程碑信息
	 * @param project
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateMilestone")
	public ModelAndViewAjax updateMilestone(String projectId) throws Exception {
		Project project=new Project();
		project.setId(Integer.valueOf(projectId));
		List<Milestone> milestones=GetBeanFromBrowerUtil.getBeanListFromBrower("修改里程碑信息：", "milestones", Milestone.class, itcMvcService);
		milestoneService.updateMilestoneList(milestones, project);
		Map map = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG,
				"修改里程碑成功");
		return ViewUtil.Json(map);
	}
	
	/**
	 * 
	 * @Title: updateProject
	 * @Description: 修改里程碑信息,并记录里程碑的变更信息
	 * @param project
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeMilestone")
	public ModelAndViewAjax changeMilestone(String projectId) throws Exception {
		Project project=new Project();
		project.setId(Integer.valueOf(projectId));
		List<Milestone> milestones=GetBeanFromBrowerUtil.getBeanListFromBrower("修改里程碑信息：", "milestones", Milestone.class, itcMvcService);
		milestoneService.updateMilestoneAndRecordChange(milestones, project);
		Map map = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG,
				"修改里程碑,并记录里程碑的变更信息成功");
		return ViewUtil.Json(map);
	}

	/**
	 * 
	 * @Title: tmpUpdateProject
	 * @Description: 修改项目立项信息，只是暂存
	 * @param project
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/tmpUpdateProject", method = RequestMethod.POST)
	@ValidFormToken
	public ModelAndViewAjax tmpUpdateProject() throws Exception {
		Project project=getProjectFromBrower("修改并暂存项目立项数据");
		List<Milestone> milestones=GetBeanFromBrowerUtil.getBeanListFromBrower("修改项目时获取里程碑信息：", "milestones", Milestone.class, itcMvcService);
		List<Workload> workloads=GetBeanFromBrowerUtil.getBeanListFromBrower("插入项目时获取工作量信息：", "workloads", Workload.class, itcMvcService);
		List<Outsourcing> outsourcings=GetBeanFromBrowerUtil.getBeanListFromBrower("插入项目时获取外购信息：", "outsourcings", Outsourcing.class, itcMvcService);
		projectService.tmpUpdateProject(project,milestones,outsourcings,workloads);
		Map map = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG,
				"修改项目立项成功");
		return ViewUtil.Json(map);
	}

	/**
	 * 
	 * @Title: deleteProject
	 * @Description: 删除年度计划信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteProject", method = RequestMethod.POST)
	public ModelAndViewAjax deleteProject(String id) throws Exception {
		int count = projectService.deleteProject(id);
		Map map = null;
		if (count == 1) {
			map = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG,
					"删除项目立项成功");
		} else {
			map = CreateReturnMapUtil.createMap(CreateReturnMapUtil.FAIL_FLAG,
					"删除项目立项失败，请检查该项目立项是否存在");
		}

		return ViewUtil.Json(map);
	}
	
	/**
	 * 将前台的project 字符串转换为对象
	 * @Title: getProjectFromBrower
	 * @param prefix
	 * @return
	 * @throws Exception
	 */
	private Project getProjectFromBrower(String prefix) throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String projectString = userInfoScope.getParam("project");
		LOGGER.info(prefix+":"+projectString);
		// jsonString To JavaBean的方法
		
		Project project = JsonHelper.fromJsonStringToBean(projectString,Project.class);
		//临时处理boolean，等待前端完善
		if(project.getIsRs()==null){
			project.setIsRs(false);
		}

		return project;
	}
	
	/**
	 * 设置项目立项工作流属性
	 * @Title: setWFVariable
	 * @param taskId
	 * @param processInstId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="setWFVariable",method=RequestMethod.POST)
	public ModelAndViewAjax setWFVariable(String taskId,String processInstId) throws Exception{
		Project project=getProjectFromBrower("设置流程信息");
		projectService.setWFVariable(taskId, processInstId, project);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	/**
	 * 终止流程
	 * @Title: stopWorkflow
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("stopWorkflow")
    @Deprecated
	public ModelAndViewAjax stopWorkflow(String processInstId,String reason) throws Exception {
		Project project=getProjectFromBrower("项目立项流程的终止信息");
		projectService.stopWorkflow(project,processInstId,reason);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	/**
	 * 判断项目编码编码是否存在
	 * @Title: isProjectCodeExisted
	 * @param projectCode
	 * @return
	 */
	@RequestMapping("isProjectCodeExisted")
	public ModelAndViewAjax isProjectCodeExisted(String projectCode,String projectId){
		boolean b=projectService.isProjectCodeExisted(projectCode,projectId);
		String result="";
		if(!b){
			result="true";
		}
		return ViewUtil.Json(result);
		
	}
	
	@RequestMapping("isProjectNameExisted")
	public ModelAndViewAjax isProjectNameExisted(String projectName,String projectId){
		boolean b=projectService.isProjectNameExisted(projectName,projectId);
		String result="";
		if(!b){
			result="true";
		}
		return ViewUtil.Json(result);
		
	}
	
	/**
	 * 
	 * @Title: queryProjectByKeyWord
	 * @Description: 根据关键字即项目名称查询10条记录，用于前端hint模块的调用
	 * @param kw
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryProjectByKeyWord", method = RequestMethod.POST)
	public ModelAndViewAjax queryProjectByKeyWord(String kw) throws Exception{
		
		List<Map> map=projectService.queryProjectListByKeyWord(kw);
		
		return ViewUtil.Json(map);
	}
	
	/**
	 * 流程作废
	 * @Title: voidFlow
	 * @return
	 */
	@RequestMapping(value="/voidFlow")
	public ModelAndViewAjax voidFlow(String businessId,String processInstId,String taskId,String message){
		UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
		FlowVoidParamBean flowVoidParamBean=new FlowVoidParamBean(processInstId, taskId, message, userInfo.getUserId(),
				userInfo.getUserId(), userInfo, businessId);
		projectService.voidFlow(flowVoidParamBean);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
	
	/**
	 * 根据年份和
	 * @Title: queryProjectByYearAndType
	 * @param year
	 * @param property
	 * @return
	 */
	@RequestMapping(value="/queryProjectByYearAndType")
	public ModelAndViewAjax queryProjectByYearAndType(String year,String property){
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String siteid=userInfoScope.getSiteId();
		List<ProjectVo> projectDtlVos=projectService.queryProjectByYearAndProperty(year,property,siteid);
		
		return ViewUtil.Json(projectDtlVos);
	}
	/**
	 * 根据年份，统计成本项目总成本和总收入
	 * @Title: queryProjectTotal
	 * @param year
	 * @return
	 */
	@RequestMapping(value="/queryProjectTotal")
	public ModelAndViewAjax queryProjectTotal(String year){
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String siteid=userInfoScope.getSiteId();
		List<ProjectVo> projectDtlVos=projectService.queryProjectTotal(year,siteid);
		
		return ViewUtil.Json(projectDtlVos);
	}
	
	/**
	 * @Title:generateNewProjectCode
	 * @Description:生成新的项目编号
	 * @param companyId
	 * @return ModelAndViewAjax
	 * @throws
	 */
	@RequestMapping(value="/generateNewProjectCode")
	public ModelAndViewAjax generateNewProjectCode(String companyId){
	    Map<String, String> companyMap = new HashMap<String, String>(0);
	    companyMap.put( "hq","HQ");
	    companyMap.put( "jd","JD");
	    companyMap.put( "lw","LW");
	    companyMap.put( "wy","WY");
	    companyMap.put( "zhf","ZH");
	    companyMap.put( "cw","CW");
	    companyMap.put( "zh","ZL");
	    String prefix = companyMap.get( companyId );
	    String dateStr = new SimpleDateFormat("yyyyMMdd").format( new Date() );
	    String newCode = projectService.generateNewProjectCode( prefix+"-"+dateStr+"-" );
	    newCode = prefix+"-"+dateStr+"-"+newCode;
	    Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
            map.put( "newCode", newCode );
            return ViewUtil.Json(map);
	}
	
	/**
         * @Title:generateNewFlowNo
         * @Description:生成新的流水号
         * @param companyId
         * @return ModelAndViewAjax
         * @throws
         */
        @RequestMapping(value="/generateNewFlowNo")
        public ModelAndViewAjax generateNewFlowNo(){
            UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
            String prefix = userInfo.getUserId();
            String dateStr = new SimpleDateFormat("yyyyMMdd").format( new Date() );
            String newFlowNo = projectService.generateNewFlowNo( prefix+dateStr );
            newFlowNo = prefix+dateStr+newFlowNo;
            Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
            map.put( "newFlowNo", newFlowNo );
            return ViewUtil.Json(map);
        }
        
        /**
         * 判断项目编码编码是否存在
         * @Title: isFlowNoExisted
         * @param projectCode
         * @return
         */
        @RequestMapping("isFlowNoExisted")
        public ModelAndViewAjax isFlowNoExisted(String flowNo,String projectId){
            boolean b=projectService.isFlowNoExisted(flowNo,projectId);
            String result="";
            if(!b){
                result="true";
            }
            return ViewUtil.Json(result);
        }
        @RequestMapping("/queryUserListByGroupId")
        public ModelAndViewAjax queryUserListByGroupId(String groupId,String kw){
                LOGGER.info("查询关键字为"+kw+"的用户名");
                List<SecureUser> list=iAuthorizationManager.retriveUsersWithSpecificGroup(groupId, null, false, false);
                List<Map<String,String>> maps = new ArrayList<Map<String,String>>(0);
                if(list!=null){
                        for(int i=0;i<list.size();i++){
                                if ( 10<maps.size() ) {
                                    break;
                                }
                                SecureUser secureUser=list.get(i);
                                if ( secureUser.getName().contains( kw ) ) {
                                    Map<String,String> map=new HashMap<String,String>(0);
                                    map.put("id", secureUser.getId());
                                    map.put("name", secureUser.getName());
                                    maps.add(map);
                                }
                        }
                }
                return ViewUtil.Json(maps);
        }
        @RequestMapping("queryUserById")
        public ModelAndViewAjax queryUserById(String userId){
            String siteId = itcMvcService.getUserInfoScopeDatas().getSiteId();
            Map<String,String> map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
            SecureUser user = iAuthorizationManager.retriveUserById( userId,siteId );
            map.put( "userId", user.getId() );
            map.put( "userName", user.getName() );
            return ViewUtil.Json( map );
        }
        @RequestMapping("updateBidCompName")
        public ModelAndView updateBidCompName(String id,String editable,String bidids){
            ModelAndView mav = new ModelAndView("project/updateBidCompName.jsp");
            Map<String, Object> modelMap = new HashMap<String, Object>(0);
            List<Map<String, String>> bidComp = new ArrayList<Map<String,String>>(0);
            if( StringUtils.isNotEmpty( bidids ) ){
                String[] bidIdArrays =  bidids.split( "," );
                for ( int i =0;i<bidIdArrays.length;i++ ) {
                    Map<String,String> map = new HashMap<String, String>(0);
                    map.put( "id", bidIdArrays[i] );
                    Map<String , String > bidMap = (Map<String, String>)supplierService.querySupplierById( bidIdArrays[i] );
                    map.put( "title", bidMap.get( "name" ) );
                    bidComp.add( map );
                }
            }
            modelMap.put( "id", id );
            modelMap.put( "bidComps", JsonHelper.toJsonString( bidComp ) );
            editable = "true";
            modelMap.put( "editable", editable );
            mav.addAllObjects( modelMap );
            return mav ;
        }
}
