package com.timss.pms.flow.sjw.project.v001;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.yudean.workflow.utils.WorkFlowConstants;

/**
 * 商务估价流程
 * @ClassName:     swgj
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-4 下午4:43:26
 */
public class Gczzgs extends TaskHandlerBase {
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	ProjectService projectService;
	private static final Logger LOGGER=Logger.getLogger(Gczzgs.class);
	@Override
	public void init(TaskInfo taskInfo) {
	       String procInstId = taskInfo.getProcessInstanceId();
	       UserInfoScope userInfoScope=(UserInfoScope) itcMvcService.getUserInfoScopeDatas();
               Project project=getProjectFromBrowser("项目立项工作流节点"+taskInfo.getTaskDefKey()+" 获取的项目信息");
	       @SuppressWarnings("unchecked")
	       List<String> uIdList = (List<String>) workflowService.getVariable(procInstId,WorkFlowConstants.CURRENT_TASK_CANDIDATE_USER);
	       try {
	          String businessLeader = userInfoScope.getParam("userId");
	          if (StringUtils.isNotEmpty(businessLeader)) {
	              // 退回
	              project.setBusinessLeader( businessLeader );
                      projectService.updateProject( project );
                      projectService.updateProjectApproving( project.getId() );
	          }else{
	              if (null == uIdList || uIdList.isEmpty()) {
	                  uIdList = Collections.emptyList();
	              } else {
	                  businessLeader = uIdList.get( 0 );
	                  project.setBusinessLeader( businessLeader );
	                  projectService.updateProject( project );
	                  projectService.updateProjectApproving( project.getId() );
	              }
	          }
	          LOGGER.info( ">>>>>>>>>>>>>>>>>>> 更新商务负责人成功,更新为"+businessLeader);
	       } catch (Exception e) {
	          LOGGER.info( ">>>>>>>>>>>>>>>>>>> 更新商务负责人异常", e );
	       } 
	}
	public void onComplete(TaskInfo taskInfo) {
		UserInfoScope userInfoScope=(UserInfoScope) itcMvcService.getUserInfoScopeDatas();
		Project project=getProjectFromBrowser("项目立项工作流节点"+taskInfo.getTaskDefKey()+" 获取的项目信息");
		projectService.updateProjectApproving(project);
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
