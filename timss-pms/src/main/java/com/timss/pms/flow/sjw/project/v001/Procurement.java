package com.timss.pms.flow.sjw.project.v001;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.pms.bean.Project;
import com.timss.pms.dao.WorkflowBusinessDao;
import com.timss.pms.service.ProjectService;
import com.timss.pms.util.ChangeStatusUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 项目编号节点类
 * @ClassName:     Xmbhsc
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-9 上午11:43:06
 */
public class Procurement extends TaskHandlerBase{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	ProjectService projectService;
	@Autowired
        WorkflowBusinessDao workflowBusinessDao;
	private static final Logger LOGGER=Logger.getLogger(Procurement.class);
	
	public void onComplete(TaskInfo taskInfo) {
		Project project=getProjectFromBrowser("项目立项工作流节点"+taskInfo.getTaskDefKey()+" 获取的项目信息",taskInfo);
		ChangeStatusUtil.changeSToValue(project, ChangeStatusUtil.approvalCode);
		projectService.updateProject(project);
		LOGGER.info("完成项目立项工作流节点"+taskInfo.getTaskDefKey()+"对项目信息的更新");
	}
	
	private Project getProjectFromBrowser(String prefix,TaskInfo taskInfo) {
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		Project project=null;
		try{
			String projectString=userInfoScope.getParam("businessData");
			if ( StringUtils.isEmpty( projectString ) ) {
			    //从新建合同处触发的项目审批
			    String procInstantId = taskInfo.getProcessInstanceId();
			    String businessIdString  = workflowBusinessDao.queryBusinessIdByWorkflowId( procInstantId );
			    if ( businessIdString.contains( "_" ) ) {
			        businessIdString = businessIdString.split( "_" )[1];
                            }
			    project = projectService.queryProjectById( businessIdString );
                        }else {
                            LOGGER.info(prefix+":"+projectString);
                            if(projectString!=null && !projectString.equals("")){
                                    project=JsonHelper.fromJsonStringToBean(projectString, Project.class);
                            }
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
