package com.timss.atd.flow.dpp.leave.v001;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activiti.engine.task.Task;
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
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: 提交请假申请
 * @description: {desc}
 * @company: gdyd
 * @className: LeaveApplyHandler.java
 * @author: yyn
 * @createDate: 2016年1月25日
 * @updateUser: yyn
 * @version: 1.0
 */
public class LeaveYwzzqrHandler extends TaskHandlerBase {
    
    private Logger log = Logger.getLogger( LeaveYwzzqrHandler.class );
    
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
    
    String handlerName="DPP LeaveYwsqrHandler";
    
    /**
     * 回滚前
     */
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
        log.info( handlerName+" beforeRollback" );
        super.beforeRollback( sourceTaskInfo, destTaskKey );
    }

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {
        log.info( handlerName+" init" );
        String instanceId = taskInfo.getProcessInstanceId();
        
        String id = workflowService.getVariable( instanceId, "businessId" ).toString();
        LeaveBean leaveBean = leaveDao.queryLeaveById( Integer.parseInt( id ) );
        List<LeaveItemBean> leaveItemVos = leaveDao.queryLeaveItemList( id );
        
        setFlowParamsByLeave(instanceId,leaveBean,leaveItemVos);
        wfUtil.updateLeaveAuditStatusByTask(taskInfo, 
        		wfUtil.checkIsCommited(instanceId)?ProcessStatusUtil.DPP_LEAVE_YSZZQR:ProcessStatusUtil.CAOGAO);
        super.init( taskInfo );
    }

    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
        log.info( handlerName+" onComplete" );
         
        super.onComplete( taskInfo );
    }

    //设置工作流需要的参数
    private void setFlowParamsByLeave(String instanceId,LeaveBean leaveBean,List<LeaveItemBean> leaveItemVos){
        String applicant=leaveBean.getCreateBy();
        String orgId=leaveBean.getDeptId();
        //设置orgId 用户所属部门
        workflowService.setVariable( instanceId, "orgId", orgId );
        //设置countDay 请假天数
        workflowService.setVariable( instanceId, "countDay", leaveBean.getLeaveDays() );
        
        //班组意见还是部门审批
        String goTo="BMSP";
    	 
       //设置结束节点
        String finish="FGLDSP";//默认到最后节点结束
        if ( leaveItemVos != null && !leaveItemVos.isEmpty() ) {
        	Boolean isAppear=false;//是否有病假、事假或补休假
        	Double sickDay=0.0,eventDay=0.0,compensateDay=0.0;//病假、事假、补休假天数
        	for ( LeaveItemBean vo : leaveItemVos ){
            	String category=vo.getCategory();
            	Double days=vo.getLeaveDays();
			    if ("dpp_le_category_3".equals(category)){
                	sickDay+=days;
                	isAppear=true;
                }else if ("dpp_le_category_2".equals(category)){
                	eventDay+=days;
                	isAppear=true;
                }else if ("dpp_le_category_6".equals(category)){
                	compensateDay+=days;
                	isAppear=true;
                }
	        }
            if(isAppear&&compensateDay<=5&&sickDay<=1&&eventDay<=1){
            	finish="BMSP";
            }else if(!(eventDay>=3||sickDay>=10)){
            	finish="RLZYBSP";
            }
        }
        workflowService.setVariable( instanceId, "finish", finish );
    }
    
    /**
     * @description:转化为List bean
     * @author: fengzt
     * @createDate: 2014年9月9日
     * @param rowDatas
     * @param leaveId  
     * @return:List<LeaveItemBean>
     */
    private List<LeaveItemBean> setleaveItemBeans( String rowDatas, int leaveId ) {
        List<LeaveItemBean> leaveItemBeans = new ArrayList<LeaveItemBean>();
        try {
            leaveItemBeans = VOUtil.fromJsonToListObject( rowDatas, LeaveItemBean.class );
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        } finally {
            if ( leaveItemBeans.isEmpty() ) {
                List<LeaveItemVo> leaveItemVos = VOUtil.fromJsonToListObject( rowDatas, LeaveItemVo.class );

                // vo to bean
                for ( LeaveItemVo vo : leaveItemVos ) {
                    LeaveItemBean leaveItemBean = new LeaveItemBean();
                    String startDateStr = vo.getStartDate();
                    String endDateStr = vo.getEndDate();
                    Date startDate = DateFormatUtil.parseDate( startDateStr, "yyyy-MM-dd hh:mm" );
                    Date endDate = DateFormatUtil.parseDate( endDateStr, "yyyy-MM-dd hh:mm" );

                    leaveItemBean.setStartDate( startDate );
                    leaveItemBean.setEndDate( endDate );
                    leaveItemBean.setLeaveDays( vo.getLeaveDays() );
                    leaveItemBean.setCategory( vo.getCategory() );
                    leaveItemBeans.add( leaveItemBean );
                }
            }
        }
        
        //新增没有父级ID
        if( !leaveItemBeans.isEmpty() ){
            for( LeaveItemBean item : leaveItemBeans ){
                item.setLeaveId( leaveId );
            }
        }

        return leaveItemBeans;
    }
    
    @Override
    public void onShowAudit(String taskId) {
    	 Task taskInfo = workflowService.getTaskByTaskId(taskId);
    	 String instanceId = taskInfo.getProcessInstanceId();
         String id = workflowService.getVariable( instanceId, "businessId" ).toString();
         LeaveBean leaveBean = leaveDao.queryLeaveById( Integer.parseInt( id ) );
         List<LeaveItemBean> leaveItemVos = leaveDao.queryLeaveItemList( id );
         setFlowParamsByLeave(instanceId,leaveBean,leaveItemVos);
    	 super.onShowAudit(taskId);
    }
}
