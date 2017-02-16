package com.timss.attendance.listener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.attendance.service.AbnormityService;
import com.timss.attendance.service.LeaveService;
import com.timss.attendance.service.OvertimeService;
import com.yudean.homepage.bean.DeleteDraftParam;
import com.yudean.itc.annotation.HopAnnotation;
import com.yudean.itc.annotation.HopAnnotation.ProType;

/**
 * 
 * @title: 考勤模块响应首页草稿
 * @description: {desc}
 * @company: gdyd
 * @className: AttendanceDraftListener.java
 * @author: fengzt
 * @createDate: 2015年1月14日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Component
public class AttendanceDraftListener {
    private Logger log = Logger.getLogger( AttendanceDraftListener.class );

    @Autowired
    AbnormityService abnormityService;
    
    @Autowired
    LeaveService leaveService;
    
    @Autowired
    OvertimeService overtimeService;
    
    
    
    /**
     * 
     * @description:考勤异常删除草稿
     * @author: fengzt
     * @createDate: 2015年1月14日
     * @param param:
     */
    @HopAnnotation(value = "abnormity", type = ProType.DeleteDraft, Sync = true)
    public void deleteDraftAbnormity(DeleteDraftParam param) {
        log.info( "首页删除草稿 考勤异常-- 传入的参数：" + param.toString() );
        String flowId = param.getFlowId();
        String siteid = param.getSiteid();
        
        int id = abnormityService.queryIdByFlowNo( flowId, siteid );
        if( id > 0 ){
            int count = abnormityService.deleteAbnormity( id );
            log.info( "删除条数：" + count );
        }
        
    }
    
    /**
     * 
     * @description:加班申请删除草稿
     * @author: fengzt
     * @createDate: 2015年1月14日
     * @param param:
     */
    @HopAnnotation(value = "overtime", type = ProType.DeleteDraft, Sync = true)
    public void deleteDraftOvertime(DeleteDraftParam param) {
        log.info( "首页删除草稿 加班申请-- 传入的参数：" + param.toString() );
        String flowId = param.getFlowId();
        String siteid = param.getSiteid();
        
        int id = overtimeService.queryIdByFlowNo( flowId, siteid );
        if( id > 0 ){
            int count = overtimeService.deleteOvertime( id );
            log.info( "加班申请 ---删除条数：" + count );
        }
        
    }
    
    /**
     * 
     * @description:加班申请删除草稿
     * @author: fengzt
     * @createDate: 2015年1月14日
     * @param param:
     */
    @HopAnnotation(value = "leave", type = ProType.DeleteDraft, Sync = true)
    public void deleteDraftLeave(DeleteDraftParam param) {
        log.info( "首页删除草稿 请假申请-- 传入的参数：" + param.toString() );
        String flowId = param.getFlowId();
        String siteid = param.getSiteid();
        
        int id = leaveService.queryIdByFlowNo( flowId, siteid );
        if( id > 0 ){
            int count = leaveService.deleteLeave( id );
            log.info( "请假申请 ---删除条数：" + count );
        }else{
        	log.error("请假申请 ---查询不到该请假单："+siteid+" num:"+flowId);
        }
    }
}
