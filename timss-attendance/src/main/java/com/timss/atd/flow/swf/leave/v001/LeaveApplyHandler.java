package com.timss.atd.flow.swf.leave.v001;

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
 * @author: yyn
 * @createDate: 2016年1月25日
 * @updateUser: yyn
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
    
    String handlerName="SWF LeaveApplyHandler";
    
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
        
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", wfUtil.checkIsCommited(instanceId)?ProcessStatusUtil.LEAVEAPPLY:ProcessStatusUtil.CAOGAO );//未提交还是草稿，避免后面取消提交引起的在列表中显示本记录的缺陷
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

            setFlowParamsByLeave(instanceId,leaveBean,leaveItemVos);

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

    //设置工作流需要的参数
    private void setFlowParamsByLeave(String instanceId,LeaveBean leaveBean,List<LeaveItemBean> leaveItemVos){
        String applicant=leaveBean.getCreateBy();
        String orgId=leaveBean.getDeptId();
        
        //设置orgId 用户所属部门
        workflowService.setVariable( instanceId, "orgId", orgId );
        
        //设置countDay 请假天数
        workflowService.setVariable( instanceId, "countDay", leaveBean.getLeaveDays() );

        //设置grade 级别（0普通/1副职/2正职）
        Integer grade=0;
        if(privUtil.isBelongGroup(applicant,"SWF_BMLD")){//部门领导组
        	grade=2;
        }else if(privUtil.isBelongGroup(applicant,"SWF_QJ_BMFZ")){//部门副职组
        	grade=1;
        }
        workflowService.setVariable( instanceId, "grade", grade );
        
        //设置goto（BMLD/BZYJ/next） 班组的判断，跳转到（部门领导/班组意见/下一步）
        String isBZFZR="N";//是否班组负责人
        if(privUtil.isBelongGroup(applicant,"SWF_BZFZR")){
        	isBZFZR="Y";
        }
        workflowService.setVariable( instanceId, "isBZFZR", isBZFZR );
        String isBZCY="N";//是否班组成员
        if(privUtil.isBelongGroup(applicant,"SWF_BZCY")){
        	isBZCY="Y";
        }
        workflowService.setVariable( instanceId, "isBZCY", isBZCY );
        String gotoStr="BMLD";
        if("Y".equals(isBZCY)){//如果是班组成员
        	if("N".equals(isBZFZR)){//非班组负责人
        		gotoStr="BZYJ";
        	}
        }else{
        	if(grade==2){//非班组的正职
        		gotoStr="next";
        	}
        }
        workflowService.setVariable( instanceId, "goto", gotoStr );
        
        //设置isFromRLDQ 是否来自人力党群部
        String isFromRLDQ="N";
        if(privUtil.isBelongGroup(applicant,"SWF_QJ_RLDQB")){
        	isFromRLDQ="Y";
        }
        workflowService.setVariable( instanceId, "isFromRLDQ", isFromRLDQ );
        
        //设置type（GH/JS/other） 请假类型（工会类/计生类/其他）
        String type="other";
        if ( leaveItemVos != null && !leaveItemVos.isEmpty() ) {
        	String typeGH="swf_le_category_8,";//工会类假
        	String typeJS="swf_le_category_4,swf_le_category_5,swf_le_category_9,swf_le_category_10,swf_le_category_12,swf_le_category_17,";//计生类假
            for ( LeaveItemBean vo : leaveItemVos ){
            	String category=vo.getCategory()+",";
                if (typeGH.indexOf(category)>=0){
                	type = "GH";
                    break;
                }else if(typeJS.indexOf(category)>=0){
                	type = "JS";
                    break;
                }
            }
        }
        workflowService.setVariable( instanceId, "type", type );
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
