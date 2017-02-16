package com.timss.pms.flow.itc.project.v007;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.pms.bean.Milestone;
import com.timss.pms.bean.Outsourcing;
import com.timss.pms.bean.Project;
import com.timss.pms.bean.Workload;
import com.timss.pms.exception.PmsBasicException;
import com.timss.pms.service.MilestoneService;
import com.timss.pms.service.OutsourcingService;
import com.timss.pms.service.ProjectService;
import com.timss.pms.service.WorkloadService;
import com.timss.pms.util.JsonUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 售前主管审批节点
 * @ClassName:     Sqzgbxkxxbg
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-4 下午4:43:26
 */
public class Xmfzrbxjsfa extends TaskHandlerBase {
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	ProjectService projectService;
	@Autowired
	OutsourcingService outsourcingService;
	@Autowired
	WorkloadService workloadService;
	@Autowired
	MilestoneService milestoneService;
	private static final Logger LOGGER=Logger.getLogger(Xmfzrbxjsfa.class);
	
	public void onComplete(TaskInfo taskInfo) {
		UserInfoScope userInfoScope=(UserInfoScope) itcMvcService.getUserInfoScopeDatas();
		Project project=getProjectFromBrowser("项目立项工作流节点"+taskInfo.getTaskDefKey()+" 获取的项目信息");
		if(project==null){
			return ;
		}
		List<Outsourcing> outsourcings=getOutsourcing();
		List<Workload> workloads=getWorkload();
		projectService.updateProjectApproving(project);
		outsourcingService.updateOutsourcingList(outsourcings, project);
		workloadService.updateWorklaodList(workloads, project);
		List<Milestone> milestones=getMilestones();
		milestoneService.updateMilestoneList(milestones, project);
		LOGGER.info("完成项目立项工作流节点"+taskInfo.getTaskDefKey()+"对项目信息的更新");
	
	}
	
	private Project getProjectFromBrowser(String prefix) {
		UserInfoScope userInfoScope=(UserInfoScope) itcMvcService.getUserInfoScopeDatas();
		try{
			String projectString=userInfoScope.getParam("businessData");
			LOGGER.info(prefix+":"+projectString);
			Project project=null;
			if(projectString!=null && !projectString.equals("")){
				project=JsonHelper.fromJsonStringToBean(projectString, Project.class);
			}else{
				//如果是移动eip端则放回null
				return null;
			}
			//临时处理boolean，等待前端完善
			if(project.getIsRs()==null){
				project.setIsRs(false);
			}
			return project;
		}catch (Exception e) {
			throw new RuntimeException("项目立项工作流获取项目信息失败",e);
		}
		
	}
	
	private List<Outsourcing> getOutsourcing(){
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String businessDataString=null;
		try {
			businessDataString=userInfoScope.getParam("businessData");
		} catch (Exception e) {
			throw new PmsBasicException("获取前端的businessData出错",e);
		}
		List<Outsourcing> lists=null;
		JSONObject jsonObject=JSONObject.fromObject(businessDataString);
		if(!"null".equals(jsonObject.get("outsourcings").toString())){
			JSONArray jsonArray=(JSONArray) jsonObject.get("outsourcings");
			lists=JsonUtil.fromJsonStringToList(jsonArray.toString(), Outsourcing.class);
		}
		
		return lists;
	}
	
	private List<Workload> getWorkload(){
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String businessDataString=null;
		try {
			businessDataString=userInfoScope.getParam("businessData");
		} catch (Exception e) {
			throw new PmsBasicException("获取前端的businessData出错",e);
		}
		JSONObject jsonObject=JSONObject.fromObject(businessDataString);
		List<Workload> lists=null;
		if(!"null".equals(jsonObject.get("workloads").toString())){
			JSONArray jsonArray=(JSONArray) jsonObject.get("workloads");
			lists=JsonUtil.fromJsonStringToList(jsonArray.toString(), Workload.class);
		}
		
		return lists;
	}
	
	private List<Milestone> getMilestones(){
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String businessDataString=null;
		try {
			businessDataString=userInfoScope.getParam("businessData");
		} catch (Exception e) {
			throw new PmsBasicException("获取前端的businessData出错",e);
		}
		JSONObject jsonObject=JSONObject.fromObject(businessDataString);
		List<Milestone> lists=null;
		if(!"null".equals(jsonObject.get("milestones").toString())){
			JSONArray jsonArray=(JSONArray) jsonObject.get("milestones");
			lists=JsonUtil.fromJsonStringToList(jsonArray.toString(), Milestone.class);
		}
		
		return lists;
	}
}


