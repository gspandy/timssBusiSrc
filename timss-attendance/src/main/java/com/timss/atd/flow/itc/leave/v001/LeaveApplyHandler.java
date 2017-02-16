package com.timss.atd.flow.itc.leave.v001;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

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
    
    String handlerName="ITC LeaveApplyHandler";
    
    /**
     * @description:拿到用户角色( 如： 部门经理、总经理、副总、员工 )
     * @author: fengzt
     * @createDate: 2014年9月9日
     * @return:String
     */
    private String getUserRoleType() {
        String type = null;
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        List<Role> roles = userInfo.getRoles();
        String siteId = userInfo.getSiteId();
        for ( Role role : roles ) {
            String roleId = role.getId();
            if ( roleId.equalsIgnoreCase( siteId + "_ZJL" ) ) {
                type = "chiefMgr";
                break;
            } else if ( roleId.equalsIgnoreCase( siteId + "_FGJSFZJL" ) || roleId.equalsIgnoreCase( siteId + "_FGJYFZJL" ) ) {
                type = "viceMgr";
                break;
            } else if ( roleId.equalsIgnoreCase( siteId + "_BMJL" ) ) {
                type = "deptMgr";
                break;
            } else {
                type = "normal";
            }
        }
        return type;
    }

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

        String typeTemp = (String)workflowService.getVariable( instanceId, "type" );
        if( StringUtils.isBlank( typeTemp ) ){
            // 用户角色
            String type = getUserRoleType();
    
            workflowService.setVariable( instanceId, "type", type );
        }

        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", wfUtil.checkIsCommited(instanceId)?ProcessStatusUtil.LEAVEAPPLY:ProcessStatusUtil.CAOGAO );
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
        log.info( handlerName+" onComplete" );
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

            // 设置参数 请假天数
            workflowService.setVariable( instanceId, "countDay", leaveBean.getLeaveDays() );

            if ( leaveItemVos != null && !leaveItemVos.isEmpty() ) {
                String applyType = null;
                for ( LeaveItemBean vo : leaveItemVos ) {
                    // 含有多条明细，如果有非病假事假就走非病假事假流程
                    if ( vo.getCategory().equalsIgnoreCase( "itc_le_category_2" )
                            || vo.getCategory().equalsIgnoreCase( "itc_le_category_3" ) ) {
                        applyType = "sick";
                    } else {
                        applyType = "nosick";
                        break;
                    }
                }
                // 设置参数 是否病假
                workflowService.setVariable( instanceId, "applyType", applyType );
            }

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
