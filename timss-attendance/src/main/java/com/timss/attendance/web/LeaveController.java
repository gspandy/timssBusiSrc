package com.timss.attendance.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.attendance.bean.LeaveBean;
import com.timss.attendance.bean.LeaveItemBean;
import com.timss.attendance.service.LeaveService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.VOUtil;
import com.timss.attendance.vo.StatVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.bean.userinfo.impl.UserInfoScopeImpl;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 请假申请controller
 * @description: 
 * @company: gdyd
 * @className: LeaveController.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("attendance/leave")
public class LeaveController {
    private Logger log = Logger.getLogger( LeaveController.class );
    
    @Autowired
    private LeaveService leaveService;
   
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private AtdUserPrivUtil privUtil;
    
    /**
     * 
     * @description:请假申请明细 新建页面
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    @RequestMapping("/insertLeaveItemToPage")
    @ReturnEnumsBind("ATD_LEI_CATEGORY")
    public ModelAndView insertLeaveItemToPage(){
     
        return new ModelAndView( "checkin/LeaveItem-insertLeaveItem.jsp", null );
    }
    
    /**
     * 
     * @description:请假申请明细 新建页面
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @param id 
     *          --从页面传进来
     * @return:String
     */
    @RequestMapping("/updateLeaveToPage")
    @ReturnEnumsBind("ATD_LEI_CATEGORY")
    public ModelAndView updateLeaveToPage(String id ){
        Map<String , Object> map = new HashMap<String, Object>();
        map.put( "id", id );
        map.put( "siteId", privUtil.getUserInfoScope().getSiteId() );
        
        return new ModelAndView( "checkin/Leave-updateLeave.jsp", map );
    }
    
    /**
     * 
     * @description:请假申请 新建页面
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    @RequestMapping("/insertLeaveToPage")
    @ReturnEnumsBind("ATD_LEI_CATEGORY")
    public ModelAndView insertLeaveToPage( ){
      //登录用户
        UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        String userId = userInfoScope.getUserId();
        String userName = userInfoScope.getUserName();
        String deptId = userInfoScope.getOrgId();
        String deptName = userInfoScope.getOrgName();
        String siteId = userInfoScope.getSiteId();
        Date dateArrival = userInfoScope.getSecureUser().getDateArrival();
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "userId", userId );
        dataMap.put( "userName", userName );
        dataMap.put( "deptId", deptId );
        dataMap.put( "deptName", deptName );
        dataMap.put( "siteId", siteId );
        dataMap.put( "dateArrival", dateArrival );
        
        
        return new ModelAndView( "checkin/Leave-insertLeave.jsp", dataMap );
    }

    /**
     * 
     * @description:插入请假申请
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @param formData
     * @param rowData
     * @param fileIds
     * @return:Map<String, Object>
     */
    @RequestMapping(value="/insertLeave",method=RequestMethod.POST)
    @ValidFormToken
    public @ResponseBody Map<String, Object> insertLeave( String formData, String rowData, String fileIds ){
        
        Map<String, Object> map = leaveService.insertLeave( formData, rowData, fileIds );

        Object taskId = map.get( "taskId" );
        if ( taskId  != null && StringUtils.isNotBlank( taskId.toString() ) ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * 
     * @description:插入、更新请假申请
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @param formData
     * @param rowData
     * @param fileIds
     * @return:Map<String, Object>
     */
    @RequestMapping(value="/insertOrUpdateLeave",method=RequestMethod.POST)
    public @ResponseBody Map<String, Object> insertOrUpdateLeave( String formData, String rowData, String fileIds ){
        Map<String, Object> map = leaveService.insertOrUpdateLeave( formData, rowData, fileIds );
        
        int count = (Integer)map.get( "count" );
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * 
     * @description:通过Id查询请假申请
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return: Map<String, Object> 
     */
    @RequestMapping("/queryLeaveById")
    public @ResponseBody Map<String, Object> queryLeaveById( int id ){
       Map<String, Object> map = leaveService.queryLeaveById( id );
        
        if( !map.isEmpty() ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }
    
    /**
     * 
     * @description:通过站点查询请假申请
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return: Map<String, Object> 
     */
    @RequestMapping("/queryLeaveBySiteId")
    public @ResponseBody Map<String, Object> queryLeaveBySiteId( int rows, int page, String search,String sort, String order ){
       Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        List<LeaveBean> leaveBeans = new ArrayList<LeaveBean>();
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setParameter( "sort", sort );
            pageVo.setParameter( "order", order );
        }
        
      //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            HashMap<String, Object> map = VOUtil.fromJsonToHashMap( search);
            leaveBeans = leaveService.queryLeaveBySearch( map, pageVo );
            
        }else{
            //默认分页
            leaveBeans = leaveService.queryLeaveBySiteId( pageVo );
        }
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", leaveBeans );
        if( ! leaveBeans.isEmpty() ){
            dataMap.put( "total",  pageVo.getTotalRecord() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
        
    }
    
    /**
     * 
     * @description:通过请假申请查询明细
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return: Map<String, Object> 
     */
    @RequestMapping("/queryLeaveItemList")
    public @ResponseBody Map<String, Object> queryLeaveItemList(String leaveId ){
        List<LeaveItemBean> leaveItemBeans = leaveService.queryLeaveItemList( leaveId );
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", leaveItemBeans );
        if( ! leaveItemBeans.isEmpty() ){
            dataMap.put( "total", leaveItemBeans.size() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
        
    }
    
    /**
     * 
     * @description:通过请假申请查询附件明细
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return: Map<String, Object> 
     */
    @RequestMapping("/queryFileByLeaveId")
    public @ResponseBody Map<String, Object> queryFileByLeaveId(String leaveId ){
        List<Map<String,Object>> fileMaps = leaveService.queryFileByLeaveId( leaveId );
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        if( fileMaps != null && !fileMaps.isEmpty() ){
            dataMap.put( "fileMap", fileMaps );
            dataMap.put( "result", "success" );
        }else{
            dataMap.put( "result", "fail" );
        }
        
        return dataMap;
        
    }
    
    /**
     * 
     * @description:终止流程
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @return: Map<String, Object>
     */
    @RequestMapping(value="/deleteFlowLeave",method=RequestMethod.POST)
    public Map<String, Object> deleteFlowLeave(
            String taskId,
            String assignee,
            String owner,
            String message,
            String businessId  
            ){
        int count = leaveService.deleteFlowLeave( taskId, assignee, owner, message, businessId );
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * 
     * @description:计算请假时间
     * @author: fengzt
     * @createDate: 2014年9月12日
     * @param startDate
     * @param endDate
     * @param category
     * @return:Map
     * @throws Exception 
     */
    @RequestMapping("/queryDiffLeaveDay")
    public Map<String, Object> queryDiffLeaveDay( String startDate, String endDate, String category ) throws Exception{
        Map<String,Object> map = leaveService.queryDiffLeaveDay( startDate, endDate, category );
        
        return map;
    }
    
    /**
     * 
     * @description:删除&删除代办
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @return: Map<String, Object>
     */
    @RequestMapping(value="/deleteLeave")
    public Map<String, Object> deleteLeave( int id ){
        
        int count = leaveService.deleteLeave( id );
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * 
     * @description:作废&删除代办
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @return: Map<String, Object>
     */
    @RequestMapping(value="/invalidLeave")
    public Map<String, Object> invalidAbnormity( int id ){
        
        int count = leaveService.invalidLeave( id ) ;
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * 
     * @description:查询日历--月
     * @author: fengzt
     * @createDate: 2014年9月12日
     * @param year
     * @param month
     * @return:Map
     */
    @RequestMapping(value="/queryCalendarByMonth")
    public @ResponseBody Map<String, Object> queryCalendarByMonth( int year, int month ){
        Map<String,Object> map = new HashMap<String, Object>();
        
        List<StatVo> statVos = leaveService.queryCalendarByMonth( year, month );
        
        map.put( "statVos", statVos );
        if( statVos != null && statVos.size() > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }
    
    /**
     * 
     * @description:查询日历--天
     * @author: fengzt
     * @createDate: 2014年9月12日
     * @param day
     * @return:Map
     */
    @RequestMapping(value="/queryCalendarByDay")
    public @ResponseBody Map<String, Object> queryCalendarByDay( String ctime ){
        Map<String,Object> map = leaveService.queryCalendarByDay( ctime );
        
        return map;
    }
    
    
    /**
     * 
     * @description:通过站点查询请假申请异常列表
     * @author: fengzt
     * @createDate: 2015年4月7日
     * @return: Map<String, Object> 
     */
    @RequestMapping("/queryExceptionLeaveList")
    public @ResponseBody Map<String, Object> queryExceptionLeaveList( int rows, int page, String search,String sort, String order ){
       Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        List<LeaveBean> leaveBeans = new ArrayList<LeaveBean>();
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setParameter( "sort", sort );
            pageVo.setParameter( "order", order );
        }
        
      //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            HashMap<String, Object> map = VOUtil.fromJsonToHashMap( search);
            leaveBeans = leaveService.queryExceptionLeaveListBySearch( map, pageVo );
            
        }else{
            //默认分页
            leaveBeans = leaveService.queryExceptionLeaveList( pageVo );
        }
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", leaveBeans );
        if( ! leaveBeans.isEmpty() ){
            dataMap.put( "total",  pageVo.getTotalRecord() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
        
    }
    
}
