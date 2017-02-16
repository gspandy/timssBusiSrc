package com.timss.pms.flow.itc.project.v007;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.pms.bean.Project;
import com.timss.pms.service.ProjectService;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 商务估价流程
 * @ClassName:     swgj
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-4 下午4:43:26
 */
public class Swzzhscbhbj extends TaskHandlerBase {
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	ProjectService projectService;
	private static final Logger LOGGER=Logger.getLogger(Swzzhscbhbj.class);
	
	public void onComplete(TaskInfo taskInfo) {
		UserInfoScope userInfoScope=(UserInfoScope) itcMvcService.getUserInfoScopeDatas();
		Project project=getProjectFromBrowser("项目立项工作流节点"+taskInfo.getTaskDefKey()+" 获取的项目信息");
		if(project!=null){
			projectService.updateProjectApproving(project);
		}
		
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
}
