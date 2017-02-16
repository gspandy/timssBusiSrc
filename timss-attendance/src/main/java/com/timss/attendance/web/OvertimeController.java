package com.timss.attendance.web;

import java.util.ArrayList;
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

import com.timss.attendance.bean.OvertimeBean;
import com.timss.attendance.service.OvertimeService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.VOUtil;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 加班申请controller
 * @description: 
 * @company: gdyd
 * @className: OvertimeController.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("attendance/overtime")
public class OvertimeController {
    private Logger log = Logger.getLogger( OvertimeController.class );
    
    @Autowired
    private OvertimeService overtimeService;
   
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private AtdUserPrivUtil privUtil;
    
    /**
     * 
     * @description:加班申请 新建页面
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    @RequestMapping("/insertOvertimeMenu")
    public ModelAndView insertOvertimeMenu( ){
      //登录用户
        UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        String userId = userInfoScope.getUserId();
        String userName = userInfoScope.getUserName();
        String deptId = userInfoScope.getOrgId();
        String deptName = userInfoScope.getOrgName();
        String siteId=userInfoScope.getSiteId();
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "userId", userId );
        dataMap.put( "userName", userName );
        dataMap.put( "deptId", deptId );
        dataMap.put( "deptName", deptName );
        dataMap.put( "siteId", siteId );
        return new ModelAndView( "checkin/Overtime-insertOvertime.jsp", dataMap );
    }
    
    /**
     * 
     * @description:通过Id查询加班申请
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return: Map<String, Object> 
     */
    @RequestMapping("/queryOvertimeById")
    public @ResponseBody Map<String, Object> queryOvertimeById( int id ){
        Map<String, Object> map = overtimeService.queryOvertimeById( id );
        
        if( !map.isEmpty() ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }
    
    /**
     * 查询加班时长
     * @param userId
     * @param siteId
     * @param startTimeStr
     * @param endTimeStr
     * @return
     */
    @RequestMapping("/queryOvertimeHoursByTime")
    public @ResponseBody Map<String, Object> queryOvertimeHoursByTime(String userId,String siteId,String startTimeStr,String endTimeStr){
    	Map<String, Object> map = new HashMap<String, Object>();
    	if(startTimeStr!=null&&!"".equals(startTimeStr)){
    		String monthStr=startTimeStr.substring(0,7);
    		double num=overtimeService.queryOvertimeTotalHoursByMonth(userId,siteId,monthStr);
    		map.put( "totalHours", num );
    		map.put( "result", "success" );
    	}else{
    		map.put( "result", "fail" );
    	}
    	
        return map;
    }
    
    /**
     * 
     * @description:通过站点查询加班申请
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return: Map<String, Object> 
     */
    @RequestMapping("/queryOvertimeBySiteId")
    public @ResponseBody Map<String, Object> queryOvertimeBySiteId( int rows, int page, String search,String sort, String order ){
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        List<OvertimeBean> overtimeBeans = new ArrayList<OvertimeBean>();
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setParameter( "sort", sort );
            pageVo.setParameter( "order", order );
        }
        
      //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            HashMap<String, Object> map = VOUtil.fromJsonToHashMap( search);
            overtimeBeans = overtimeService.queryOvertimeBySearch( map, pageVo );
        }else{
            //默认分页
            overtimeBeans = overtimeService.queryOvertimeBySiteId( pageVo );
        }
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", overtimeBeans );
        if( ! overtimeBeans.isEmpty() ){
            dataMap.put( "total",  pageVo.getTotalRecord() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
        
    }
    
    /**
     * 
     * @description:更新加班申请
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @return: Map<String, Object>
     */
    @Deprecated
    @RequestMapping("/updateOvertime")
    public Map<String, Object> updateOvertime( String formData ){
        
        OvertimeBean vo = JsonHelper.fromJsonStringToBean( formData, OvertimeBean.class );
        
        int count = overtimeService.updateOvertime( vo );
        
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
     * @description:终止流程
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @return: Map<String, Object>
     */
    @RequestMapping(value="/deleteFlowOvertime",method=RequestMethod.POST)
    public Map<String, Object> deleteFlowOvertime( 
            String taskId,
            String assignee,
            String owner,
            String message,
            String businessId 
            ){
        int count = overtimeService.deleteFlowOvertime( taskId, assignee, owner, message, businessId  );
        
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
     * @description:通过加班申请ID查询附件明细
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return: Map<String, Object> 
     */
    @RequestMapping("/queryFileByOvertimeId")
    public @ResponseBody Map<String, Object> queryFileByOvertimeId(String overtimeId ){
        List<Map<String,Object>> fileMaps = overtimeService.queryFileByOvertimeId( overtimeId );
        
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
     * @description:删除&删除代办
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @return: Map<String, Object>
     */
    @RequestMapping(value="/deleteOvertime")
    public Map<String, Object> deleteOvertime( int id ){
        
        int count = overtimeService.deleteOvertime( id );
        
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
    @RequestMapping(value="/invalidOvertime")
    public Map<String, Object> invalidOvertime( int id ){
        
        int count = overtimeService.invalidOvertime( id ) ;
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    @RequestMapping("/insertOvertimeItemToPage")
    public ModelAndView insertOvertimeItemToPage(String isToUnify){
    	Map<String, Object>map=new HashMap<String, Object>();
    	map.put("isToUnify", isToUnify);
        return new ModelAndView( "checkin/OvertimeItem-insertOvertimeItem.jsp", map );
    }
    
    /**
     * 暂存加班
     * @param formData
     * @param fileIds
     * @param addRows
     * @param delRows
     * @param updateRows
     * @return
     */
    @RequestMapping(value="/saveOvertime",method=RequestMethod.POST)
    public @ResponseBody Map<String, Object> saveOvertime( String formData, String fileIds, String addRows, String delRows, String updateRows ){
    	OvertimeBean bean=overtimeService.convertBean(formData, fileIds, addRows, delRows, updateRows);
        Map<String, Object> map = overtimeService.saveOvertime(bean);
        return map;
    }
    
    /**
     * 提交加班
     * @param formData
     * @param fileIds
     * @param addRows
     * @param delRows
     * @param updateRows
     * @return
     */
    @RequestMapping(value="/submitOvertime",method=RequestMethod.POST)
    public @ResponseBody Map<String, Object> submitOvertime( String formData, String fileIds, String addRows, String delRows, String updateRows ){
    	OvertimeBean bean=overtimeService.convertBean(formData, fileIds, addRows, delRows, updateRows);
        Map<String, Object> map = overtimeService.submitOvertime(bean);
        return map;
    }
    
    /*
     * 将jumpPath的路径换成.do跳转
     */
    @RequestMapping("/updateOvertimePage")
    public ModelAndView updateOvertimePage(String id){
    	Map<String, Object>map=new HashMap<String, Object>();
    	map.put("id", id);
        return new ModelAndView( "checkin/Overtime-updateOvertime.jsp", map );
    }
}
