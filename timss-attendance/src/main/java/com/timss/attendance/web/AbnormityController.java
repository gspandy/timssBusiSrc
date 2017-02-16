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

import com.timss.attendance.bean.AbnormityBean;
import com.timss.attendance.service.AbnormityService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.VOUtil;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 考勤异常controller
 * @description: 
 * @company: gdyd
 * @className: AbnormityController.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("attendance/abnormity")
public class AbnormityController {
    private Logger log = Logger.getLogger( AbnormityController.class );
    
    @Autowired
    private AbnormityService abnormityService;
   
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private AtdUserPrivUtil privUtil;
    
    /**
     * 详情页
     * 页面模式add（新建）edit（编辑）view（只读）
     * @return
     */
    @RequestMapping("/detail")
    @ReturnEnumsBind("ATD_AB_CATEGORY")
    public ModelAndView detailPage(String mode,Integer id){
    	UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        String userId = userInfoScope.getUserId();
        String userName = userInfoScope.getUserName();
        String deptId = userInfoScope.getOrgId();
        String deptName = userInfoScope.getOrgName();
        String siteId = userInfoScope.getSiteId();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "userId", userId );
        dataMap.put( "userName", userName );
        dataMap.put( "deptId", deptId );
        dataMap.put( "deptName", deptName );
        dataMap.put( "siteId", siteId );
        dataMap.put( "mode", mode );
        dataMap.put( "id", id );
        return new ModelAndView( "checkin/Abnormity-detail.jsp", dataMap );
    }
    /**
     * 子项详情页
     * 页面模式add（新建）edit（编辑）view（只读）
     * @return
     */
    @RequestMapping("/item")
    @ReturnEnumsBind("ATD_AB_CATEGORY")
    public ModelAndView itemPage(String mode,String isToUnify){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "mode", mode );
        dataMap.put( "isToUnify", isToUnify );
        return new ModelAndView( "checkin/Abnormity-item.jsp", dataMap );
    }
    
    /**
     * 
     * @description:考勤异常 新建页面
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    @RequestMapping("/insertAbnormityMenu")
    @ReturnEnumsBind("ATD_AB_CATEGORY")
    public ModelAndView insertAbnormityMenu(){
    	return new ModelAndView("redirect:/detail.do?mode=add");
      //登录用户
        /*UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        
        String userId = userInfoScope.getUserId();
        String userName = userInfoScope.getUserName();
        String deptId = userInfoScope.getOrgId();
        String deptName = userInfoScope.getOrgName();
        String siteId = userInfoScope.getSiteId();
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "userId", userId );
        dataMap.put( "userName", userName );
        dataMap.put( "deptId", deptId );
        dataMap.put( "deptName", deptName );
        dataMap.put( "siteId", siteId );
        return new ModelAndView( "checkin/Abnormity-insertAbnormity.jsp", dataMap );*/
    }

    /**
     * 
     * @description:通过请假申请查询附件明细
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return: Map<String, Object> 
     */
    @RequestMapping("/queryFileByAbnormityId")
    public @ResponseBody Map<String, Object> queryFileByAbnormityId(Integer abnormityId){
        List<Map<String,Object>> fileMaps = abnormityService.queryFileByAbnormityId( abnormityId );
        
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
     * @description:通过Id查询考勤异常
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return: Map<String, Object> 
     */
    @RequestMapping("/queryAbnormityById")
    public @ResponseBody Map<String, Object> queryAbnormityById( int id ){
        Map<String, Object> map = abnormityService.queryAbnormityById( id );
        
        if( !map.isEmpty() ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }
    
    /**
     * 
     * @description:通过站点查询考勤异常
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return: Map<String, Object> 
     */
    @RequestMapping("/queryAbnormityBySiteId")
    public @ResponseBody Map<String, Object> queryAbnormityBySiteId( int rows, int page, String search,String sort, String order ){
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        List<AbnormityBean> abnormityBeans = new ArrayList<AbnormityBean>();
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setParameter( "sort", sort );
            pageVo.setParameter( "order", order );
        }
        
      //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            HashMap<String, Object> map = VOUtil.fromJsonToHashMap( search);
            abnormityBeans = abnormityService.queryAbnormityBySearch( map, pageVo );
            
        }else{
            //默认分页
            abnormityBeans = abnormityService.queryAbnormityBySiteId( pageVo );
        }
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", abnormityBeans );
        if( ! abnormityBeans.isEmpty() ){
            dataMap.put( "total",  pageVo.getTotalRecord() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
        
    }
    
    /**
     * 暂存考勤异常
     * @param formData
     * @param fileIds
     * @param addRows
     * @param delRows
     * @param updateRows
     * @return
     */
    @RequestMapping(value="/saveAbnormity",method=RequestMethod.POST)
    public @ResponseBody Map<String, Object> saveAbnormity( String formData, String fileIds, String addRows, String delRows, String updateRows ){
    	AbnormityBean bean=abnormityService.convertBean(formData, fileIds, addRows, delRows, updateRows);
        Map<String, Object> map = abnormityService.saveAbnormity(bean);
        return map;
    }
    
    /**
     * 提交考勤异常
     * @param formData
     * @param fileIds
     * @param addRows
     * @param delRows
     * @param updateRows
     * @return
     */
    @RequestMapping(value="/submitAbnormity",method=RequestMethod.POST)
    public @ResponseBody Map<String, Object> submitAbnormity( String formData, String fileIds, String addRows, String delRows, String updateRows ){
    	AbnormityBean bean=abnormityService.convertBean(formData, fileIds, addRows, delRows, updateRows);
        Map<String, Object> map = abnormityService.submitAbnormity(bean);
        return map;
    }
    
    @RequestMapping("/updateAbnormityMenu")
    @ReturnEnumsBind("ATD_AB_CATEGORY")
    public ModelAndView updateAbnormityMenu(Integer id,String siteId) throws Exception{
    	return detailPage("edit",id);
      //登录用户
        /*UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "id", userInfoScope.getParam("id") );
        dataMap.put( "siteId", userInfoScope.getParam("siteId") );
        return new ModelAndView( "checkin/Abnormity-updateAbnormity.jsp", dataMap );*/
    }
    
    /**
     * 
     * @description:终止流程
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @return: Map<String, Object>
     */
    @RequestMapping(value="/deleteFlowAbnormity",method=RequestMethod.POST)
    public Map<String, Object> deleteFlowAbnormity(
            String taskId,
            String assignee,
            String owner,
            String message,
            String businessId            
            ){
        
        int count = abnormityService.deleteFlowAbnormity( taskId, assignee, owner, message, businessId );
        
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
     * @description:删除&删除代办
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @return: Map<String, Object>
     */
    @RequestMapping(value="/deleteAbnormity")
    public Map<String, Object> deleteAbnormity( int id ){
        
        int count = abnormityService.deleteAbnormity( id );
        
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
     * @description:删除&删除代办
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @return: Map<String, Object>
     */
    @RequestMapping(value="/invalidAbnormity")
    public Map<String, Object> invalidAbnormity( int id ){
        
        int count = abnormityService.invalidAbnormity( id ) ;
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    
}
