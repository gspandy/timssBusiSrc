package com.timss.atd.flow.dpp.sealapply.v001;

import java.util.Date;
import java.util.List;

import jodd.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.bean.SealApplyBean;
import com.timss.attendance.dao.SealApplyDao;
import com.timss.attendance.vo.SealApplyVo;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;
/**
 * @title: 填写用章申请
 * @company: gdyd
 * @author: 890199
 * @createDate: 2016-09-07
 * @updateUser: 890199
 * @version:1.0
 */
public class CommitSealApply extends TaskHandlerBase{
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired 
	private SealApplyDao sealApplyDao;
	
	private static Logger logger = Logger.getLogger(CommitSealApply.class);
	/**
	 * 初始化
	 */
	@Override
	public void init(TaskInfo taskInfo){
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String instantId = taskInfo.getProcessInstanceId();
        String saId = workflowService.getVariable( instantId, "businessId").toString();
        logger.info( "CommitSealApplyHandler init --- instantId = " + instantId + "-- saId = " + saId );
        List<SealApplyVo> sabList = sealApplyDao.querySealApplyById(saId);
        SealApplyVo sab = sabList.get(0);
        //首次提交不更新状态，其他节点退回更新为当前节点状态
        if(StringUtil.isNotBlank(sab.getStatus()) && (!"draft".equals(sab.getStatus())) ){
            sab.setStatus("commit_seal_apply");
        }
        sab.setSaId(saId);
        sab.setInstanceId(instantId);
        sab.setModifydate(new Date());
        sab.setModifyuser(userInfo.getUserId());
        sealApplyDao.updateSealApply(sab);
        super.init( taskInfo );
	}
	
	/**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
    	logger.info("CommitSealApply onComplete");
    	 UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
         try {
             //update的时候拿出传入的参数
             String formData = userInfo.getParam( "businessData" );
             if( StringUtils.isNotBlank( formData ) ){
                 SealApplyBean sab = JsonHelper.fromJsonStringToBean( formData, SealApplyBean.class );
                 sab.setModifydate(new Date());
                 sab.setModifyuser(userInfo.getUserId());
                 int count = sealApplyDao.updateSealApply(sab);
                 logger.info( "CommitSealApply update sab count=" + count );
             }
         } catch (Exception e) {
             logger.error( e.getMessage(), e );
         }
        
    	/*
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String instantId = taskInfo.getProcessInstanceId();
        String saId = workflowService.getVariable( instantId, "businessId").toString();
        logger.info( "CommitSealApplyHandler init --- instantId = " + instantId + "-- saId = " + saId );
        SealApplyBean sab = new SealApplyBean();
        sab.setSaId(saId);
        sab.setApproveUserId("");
        sab.setApproveUserName("");
        sab.setApproveTime("");
        sab.setModifydate(new Date());
        sab.setModifyuser(userInfo.getUserId());
        sealApplyDao.updateSealApply(sab);
        */
    	//提供一个方法更新下一步审批人
    	
        super.onComplete( taskInfo );
    }
}
