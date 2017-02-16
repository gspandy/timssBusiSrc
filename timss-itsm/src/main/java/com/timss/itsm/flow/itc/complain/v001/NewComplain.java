package com.timss.itsm.flow.itc.complain.v001;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.bean.ItsmComplainRd;
import com.timss.itsm.dao.ItsmComplainRdDao;
import com.timss.itsm.service.ItsmComplainRdService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.util.ItsmConstant;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;


/*
 * 新建
 */
public class NewComplain extends TaskHandlerBase{
	
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
    private WorkflowService workflowService;
	@Autowired
	private ItsmComplainRdService complainRdService;
	@Autowired
	private ItsmComplainRdDao complainRdDao;
	@Autowired
	private ItsmWoUtilService woUtilService;
	@Autowired
    private HomepageService homepageService;
	
	private static final Logger LOG = Logger.getLogger( NewComplain.class );
	 
	
	public void init(TaskInfo taskInfo){
		String complainRdId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
		ItsmComplainRd complainRd = complainRdDao.queryCpRdById(complainRdId);
    	String currstatus = complainRd.getcurrStatus();
		if(!"draft".equals(currstatus)){
    		currstatus="newCp";
    	}else{
    		currstatus="draft";
    	}
    	complainRdService.updateComplainRdStatus(complainRdId, currstatus);
	}
	public void onComplete(TaskInfo taskInfo){
		LOG.debug( "-------------进入‘新建投诉记录’的onComplete(),开始处理业务逻辑-----------------" );
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		if(userInfoScope!=null){
			
			String businessData = null;
			try {
				businessData = userInfoScope.getParam( "businessData" );
				JSONObject businessDataobj = JSONObject.fromObject( businessData );
				String complainRdForm = businessDataobj.getString( "complainRdForm" );
				ItsmComplainRd complainRd=JsonHelper.toObject(complainRdForm, ItsmComplainRd.class);
				complainRdDao.updateComplainRd(complainRd);
				
				String complainRdId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
				String attachmentIds = businessDataobj.getString( "attachmentIds" );
                // 操作附件
                // 先删掉所有相关的附件数据
                woUtilService.deleteAttachment(complainRdId, null, "Cp");
                // 插入附件的相关数据
                woUtilService.insertAttachment(complainRdId, attachmentIds, "Cp", "newCp");
			if(!"".equals(complainRd.getWorkflowId())){	
              //更新待办列表
                String flowCode = complainRd.getCode();
                String content = complainRd.getContent();
                int size = (content.length() > 100) ? 100 : content.length();
                String flowName=content.substring(0, size);
                String jumpPath = "itsm/complainRecords/openComplainPage.do?complainRdId=" + complainRd.getId();
                // 构建Bean
                HomepageWorkTask homeworkTask = woUtilService.joinHomepageWorkTask
                		(flowCode, flowName, complainRd.getWorkflowId(), "审批",  "投诉记录", jumpPath);
                homepageService.create( homeworkTask, userInfoScope, null );
			}
				complainRdService.updateCurrHandlerUser(complainRdId, userInfoScope, "normal");
			} catch (Exception e) {
				LOG.error( e.getMessage() );
				throw new RuntimeException( e );
			}
			
		  }
	 }
	public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
		
	}
	
}
