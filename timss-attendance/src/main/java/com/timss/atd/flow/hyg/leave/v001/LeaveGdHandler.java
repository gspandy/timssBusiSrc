package com.timss.atd.flow.hyg.leave.v001;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.bean.LeaveBean;
import com.timss.attendance.bean.LeaveItemBean;
import com.timss.attendance.dao.LeaveDao;
import com.timss.attendance.service.LeaveService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.DateFormatUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.timss.attendance.util.VOUtil;
import com.timss.attendance.vo.LeaveItemVo;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: 归档
 * @description: {desc}
 * @company: gdyd
 * @className: LeaveApplyHandler.java
 * @author: fengzt
 * @createDate: 2014年9月1日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class LeaveGdHandler extends TaskHandlerBase {
    
    private Logger log = Logger.getLogger( LeaveGdHandler.class );

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private LeaveDao leaveDao;
    
    @Autowired
    private LeaveService leaveService;

    @Autowired
    private AtdUserPrivUtil privUtil;

    @Autowired
    private AtdWorkFlowUtil wfUtil;
    
    final String handlerName="HYG LeaveGdHandler";
     

    /**
     * 回滚前
     */
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
        log.info( handlerName + " beforeRollback" );
        super.beforeRollback( sourceTaskInfo, destTaskKey );
    }

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {

        String instanceId = taskInfo.getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId" ).toString();
        LeaveBean leaveBean = leaveService.queryLeaveBeanById(Integer.parseInt( id ));
        List<LeaveItemBean> leaveItemVos = leaveBean.getItemList();
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status",ProcessStatusUtil.HYG_LEAVE_QJGD);
        parmas.put( "instanceId", instanceId );
        parmas.put( "id", id );
        leaveDao.updateOperUserById( parmas );
        super.init( taskInfo );
    }

    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
    	String instanceId = taskInfo.getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId" ).toString();
        LeaveBean leaveBean = leaveService.queryLeaveBeanById(Integer.parseInt( id ));
        List<LeaveItemBean> leaveItemVos = leaveBean.getItemList();
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", ProcessStatusUtil.HYG_LEAVE_CLOSED);
        parmas.put( "instanceId", instanceId );
        parmas.put( "id", id );
        leaveDao.updateOperUserById( parmas );
        try {
			wfUtil.checkWorkStatusAndStatByLeaveId(Integer.parseInt(id));
		} catch (Exception e) {
			log.error("error checkWorkStatusAndStatByOvertimeId("+id+")",e);
		} 
        super.onComplete( taskInfo );
    }
    
   
}
