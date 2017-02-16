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

import com.timss.attendance.bean.AbnormityBean;
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
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.SelectUserService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: 提交请假申请
 * @description: {desc}
 * @company: gdyd
 * @className: LeaveApplyHandler.java
 * @author: fengzt
 * @createDate: 2014年9月1日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class LeaveApplyHandler extends TaskHandlerBase {
    
    private Logger log = Logger.getLogger( LeaveApplyHandler.class );

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
    
    @Autowired
    private SelectUserService selectUserService;
    
    @Autowired
    private HomepageService homepageService;

    
    @Autowired
    private IAuthorizationManager manager;
    final String handlerName="HYG LeaveApplyHandler";
    
     /**
     * 回滚前
     */
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
        log.info( " LeaveApplyHandler beforeRollback" );
        super.beforeRollback( sourceTaskInfo, destTaskKey );
    }

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {
        log.info( handlerName + "init" );
        String instanceId = taskInfo.getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId" ).toString();
        // 设置参数 部门id
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", wfUtil.checkIsCommited(instanceId)?ProcessStatusUtil.HYG_LEAVE_TJSQ:ProcessStatusUtil.HYG_LEAVE_CAOGAO );
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
        log.info( "LeaveApplyHandler onComplete" );
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        String instanceId = taskInfo.getProcessInstanceId();
        // update的时候拿出传入的参数
        String dataStr = null;
        try {
            dataStr = userInfo.getParam( "businessData" );
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        log.info( "businessData=" +  dataStr);

        if ( StringUtils.isNotBlank( dataStr ) ) {
            JSONObject jsonObject = JSONObject.fromObject( dataStr );
            String formData = jsonObject.getString( "formData" );
            String rowDatas = jsonObject.getString( "rowDatas" );
            // 是否在编辑模式（update时候"edit" 其他情况:other）
            String editFlag = jsonObject.getString( "editFlag" );
            String fileIds = jsonObject.getString( "fileIds" );

            LeaveBean leaveBean = JsonHelper.fromJsonStringToBean( formData, LeaveBean.class );
            // 转化为list<Object>
            List<LeaveItemBean> leaveItemVos = setleaveItemBeans( rowDatas , leaveBean.getId() );
            leaveBean.setUpdateBy( userInfo.getUserId() );
            leaveBean.setUpdateDate( new Date() );
            if ( !leaveItemVos.isEmpty() ) {
                String category = leaveItemVos.get( 0 ).getCategory();
                Date startDate = leaveItemVos.get( 0 ).getStartDate();
                leaveBean.setCategory( category );
                leaveBean.setCreateDay( startDate );
            }

            // 编辑模式才更新
            if ( editFlag.equalsIgnoreCase( "edit" ) ) {
                // update
                int count = leaveDao.updateLeave( leaveBean );
                int fileDelCount = leaveService.deleteFileByLeaveId( leaveBean.getId() );
                int addFileCount = leaveService.insertLeaveFile( leaveBean, fileIds );
                
                int delItemCount = leaveDao.deleteLeaveItemByLeaveId( leaveBean.getId() );
                int addItemCount = leaveDao.insertBatchLeaveItem( leaveItemVos );
                log.info( "update leave count= " + count + " delete item count = " + delItemCount + " add item count = "
                        + addItemCount + ";fileDelCount= "+ fileDelCount + ";addFileCount=" + addFileCount );
            }
            // 设置参数 请假天数
            workflowService.setVariable( instanceId, "leaveDays", leaveBean.getLeaveDays() );
        }
        
        
        /*更新待办*/
        try {
        	String id = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId").toString();
        	LeaveBean  bean = leaveService.queryLeaveBeanById(Integer.parseInt(id));
        	String flowCode = bean.getNum();
            String reason = bean.getReason();
            if( reason.length() > 50 ){
                reason = reason.substring( 0, 47 ) + "...";
            }
            homepageService.modify(null, flowCode, null, reason, null, null, null, null);
        } catch (Exception e) {
        	log.error( e.getMessage(), e );
        }
        
        wfUtil.setIsCommited(instanceId);
        super.onComplete( taskInfo );
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

    
    
}
