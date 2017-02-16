package com.timss.attendance.web;

import java.util.ArrayList;
import java.util.Calendar;
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

import com.timss.asset.bean.SwLedgerBean;
import com.timss.attendance.bean.StatBean;
import com.timss.attendance.bean.StatItemBean;
import com.timss.attendance.bean.WorkStatusBean;
import com.timss.attendance.service.StatService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.DateFormatUtil;
import com.timss.attendance.util.VOUtil;
import com.timss.attendance.vo.LeaveContainItemVo;
import com.timss.attendance.vo.StatDetailVo;
import com.timss.attendance.vo.StatVo;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.Role;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.bean.userinfo.impl.UserInfoScopeImpl;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 统计controller
 * @description: 
 * @company: gdyd
 * @className: StatController.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("attendance/stat")
public class StatController {
    private Logger log = Logger.getLogger( StatController.class );
    
    @Autowired
    private StatService statService;
   
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private AtdUserPrivUtil privUtil;
    
    @RequestMapping("/queryStatList")
    public Page<StatBean> queryStatList() throws Exception{
    	UserInfoScope userInfoScope=privUtil.getUserInfoScope();
    	Page<StatBean> page = userInfoScope.getPage();
    	page = statService.queryStatList( page );
        return page;
    }
    
    /**
     * 
     * @description:通过站点统计考勤信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return: Map<String, Object> 
     */
    @RequestMapping("/queryStatBySiteId")
    public @ResponseBody Map<String, Object> queryStatBySiteId( int rows, int page, String search,String sort, String order ){
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        List<StatVo> result = new ArrayList<StatVo>();
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setParameter( "sort", sort );
            pageVo.setParameter( "order", order );
        }else{
            pageVo.setSortKey( "USERSTATUS asc, USERNAME " );
            pageVo.setSortOrder( "ASC" );
        }
        
        //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            result = statSearch( search, pageVo );
        }else{
            int year = DateFormatUtil.fromDateToCalendar( new Date() ).get( Calendar.YEAR );
            //默认分页
            result = statService.queryStatBySiteId( pageVo, year );
        }
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", result );
        if( ! result.isEmpty() ){
            dataMap.put( "total",  pageVo.getTotalRecord() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
        
    }
    
    /**
     * 
     * @description:考勤统计高级搜索
     * @author: fengzt
     * @createDate: 2014年12月19日
     * @param search
     * @param pageVo
     * @return:List<StatVo>
     */
    private List<StatVo> statSearch(String search, Page<HashMap<?, ?>> pageVo) {
        HashMap<String, Object> map = VOUtil.fromJsonToHashMap( search);
        String searchDateFrom =  (String) map.get( "searchDateFrom" );
        String searchDateEnd =  (String) map.get( "searchDateEnd" );
        List<StatVo> result = new ArrayList<StatVo>();
        if( !StringUtils.isBlank( searchDateFrom) && !StringUtils.isBlank( searchDateEnd ) ){
            //包含1月1号 和 12月31就是整年
            int yearStart = Integer.parseInt( searchDateFrom.substring( 0, 4 ) );
            int yearEnd = Integer.parseInt( searchDateEnd.substring( 0, 4 ) );
            if( yearStart == yearEnd && searchDateFrom.indexOf( "-01-01" ) > 0
                    && searchDateEnd.indexOf( "-12-31" ) > 0 ){
                int year = Integer.parseInt( searchDateFrom.substring( 0, 4 ) );
                //result = statService.queryStatBySiteId( pageVo, year );
                result = statService.queryStatBySearch( map, pageVo, year );
            }else{
                result  = statService.queryStatByTimeSearch( map, pageVo );
            }
            
        }else{
            int yearStart = DateFormatUtil.fromDateToCalendar( new Date() ).get( Calendar.YEAR );
            result = statService.queryStatBySearch( map, pageVo, yearStart );
        }
        return result;
    }

    /**
     * 
     * @description:核减年假
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @param subAnnualDays
     * @return:Map<String, Object>
     */
    @RequestMapping(value = "/updateStatSubAnnual",method=RequestMethod.POST )
    public Map<String, Object> updateStatSubAnnual( double subAnnualDays, String remark ){
        Map<String, Object> map = statService.updateStatSubAnnual( subAnnualDays, remark );
        
        return map;
    }
    
    /**
     * 
     * @description:登录个人统计信息
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @param subAnnualDays
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryStatByLogin")
    public @ResponseBody Map<String, Object> queryStatByLogin( ){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        //StatVo vo = statService.queryStatByLogin( );
        StatBean stat=getPersonStatByLogin();
        if(stat!=null){
            dataMap.put( "rowData", stat );
            dataMap.put( "result", "success" );
        }else{
            dataMap.put( "result", "fail" );
        }
        
        return dataMap;
    }
    
    /**
     * 查询当前用户当年的休假统计情况
     * @return
     */
    private StatBean getPersonStatByLogin(){
    	int year = DateFormatUtil.fromDateToCalendar( new Date() ).get( Calendar.YEAR );
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        Map<String, StatBean>result=statService.queryStatMap(userInfo.getSiteId(), new String[]{userInfo.getUserId()}, year, year);
        if( result != null&&!result.isEmpty() ){
        	return result.get(year+"_"+userInfo.getUserId());
        }else{
        	return null;
        }
    }
    
    /**
     * 
     * @description:跳到编辑结转页面
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    @RequestMapping("/updateStatRemainToPage")
    public ModelAndView updateStatRemainToPage( String id ){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "id", id );
        
        return new ModelAndView( "checkin/Stat-updateStatRemain.jsp", map );
    }
    
    /**
     * 
     * @description:跳到明细页面
     * @author: fengzt
     * @createDate: 2014年11月11日
     * @param userId
     * @param searchDateFrom
     * @param searchDateEnd
     * @param field
     * @return:
     */
    @RequestMapping("/queryStatLeaveDetailToPage")
    public ModelAndView queryStatLeaveDetailToPage( String userId, Integer year, Integer month, String field ){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "userId", userId );
        map.put( "year", year );
        map.put( "month", month );
        map.put( "field", field );
        
        return new ModelAndView( "checkin/Stat-queryStatLeaveDetail.jsp", map );
    }
    
    /**
     * 
     * @description:请假（加班）明细
     * @author: fengzt
     * @createDate: 2014年11月11日
     * @param userId
     * @param searchDateFrom
     * @param searchDateEnd
     * @param field
     * @return:
     * @throws Exception 
     */
    @RequestMapping("/queryStatLeaveDetail")
    public @ResponseBody Map<String, Object> queryStatLeaveDetail( String userId, Integer year, Integer month, String field ) throws Exception{
        Map<String, Object> dataMap = new HashMap<String, Object>();
        
        List<LeaveContainItemVo> result = statService.queryStatDetail( userId, year, month, field );
        
        dataMap.put( "rows", result );
        if( ! result.isEmpty() ){
            dataMap.put( "total",  result.size() );
        }else{
            dataMap.put( "total", 0 );
        }
        
        return dataMap;
    }
    
    /**
     * 
     * @description:通过ID查询stat信息
     * @author: fengzt
     * @createDate: 2014年11月11日
     * @param id
     * @return:Map<String, Object> 
     */
    @RequestMapping("/queryStatById")
    public @ResponseBody Map<String, Object> queryStatById( int id ){
        Map<String, Object> map = new HashMap<String, Object>();
        
        StatVo vo = statService.queryStatById( id );
        if( vo != null && vo.getId() > 0 ){
            map.put( "result", "success" );
            map.put( "rowData", vo );
        }else{
            map.put( "result", "fail" );
        }
        
        return map ;
    }
    
    /**
     * 
     * @description:更新结转信息
     * @author: fengzt
     * @createDate: 2014年11月11日
     * @param formData
     * @return:Map<String, Object> 
     */
    @RequestMapping("/updateStatRemain")
    public Map<String, Object> updateStatRemain( String formData ){
        Map<String, Object> map = new HashMap<String, Object>();
        
        //用户权限 只有人事经理才能修改
        String roleFlag = privUtil.getStatPrivLevel();
        if( "normal".equals( roleFlag )  ){
            map.put( "result", "fail" );
            map.put( "reason", "没有权限" );
            return map;
        }
        
        int count = statService.updateStatRemain( formData );
        if( count > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        return map ;
    }
    
    /**
     * 
     * @description:查询个人考勤统计信息
     * @author: fengzt
     * @createDate: 2015年2月28日
     * @return:Map<String, Object> 
     */
    @RequestMapping("/queryPersonStat")
    public @ResponseBody Map<String, Object> queryPersonStat( ){
        Map<String, Object> map = new HashMap<String, Object>();
        
        //StatVo vo = statService.queryPersonStat( );
        StatBean stat=getPersonStatByLogin();
        map.put( "data", stat );
        if( stat != null && StringUtils.isNotBlank( stat.getUserName() ) ){
            map.put( "status", "ok" );
        }
        return map ;
    }
    
    @RequestMapping("/rebuildStat")
    public @ResponseBody Map<String, Object> rebuildStat(String siteId,Boolean isRebuildMain,Integer year ) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        
        if(statService.rebuildStatItem(false,privUtil.getUserInfoScope().getSiteId(), null,null,year, year)){
            map.put( "result", "success" );
        }
        return map ;
    }
    
    @RequestMapping("/createStat")
    public @ResponseBody Map<String, Object> createStat(String siteId,Boolean isRebuildMain,Integer year ) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        if(statService.checkPersonStat(true,true,privUtil.getUserInfoScope().getSiteId(),year, null,null)){
            map.put( "result", "success" );
        }
        return map ;
    }
    @RequestMapping("/checkPersonStat")
    public @ResponseBody Map<String, Object> checkPersonStat(Integer year) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        if(statService.checkPersonStat(false,false,privUtil.getUserInfoScope().getSiteId(), year,null,null)){
            map.put( "result", "success" );
        }
        return map ;
    }
}
