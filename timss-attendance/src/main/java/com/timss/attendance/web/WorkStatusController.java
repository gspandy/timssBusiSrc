package com.timss.attendance.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.attendance.bean.WorkStatusBean;
import com.timss.attendance.service.WorkStatusService;
import com.timss.attendance.service.WorkStatusService;
import com.timss.attendance.util.DateFormatUtil;
import com.timss.attendance.util.VOUtil;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.Configuration;
import com.yudean.itc.manager.support.IConfigurationManager;

/**
 * 
 * @title: 打卡记录
 * @description: {desc}
 * @company: gdyd
 * @className: WorkStatusController.java
 * @author: fengzt
 * @createDate: 2015年6月8日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("attendance/cardData")
public class WorkStatusController {
    
    @Autowired
    private WorkStatusService workStatusService;
    @Autowired
    private IConfigurationManager confManager;
    
    /**
     * 
     * @description:通过站点查询打卡管理列表
     * @author: fengzt
     * @createDate: 2015年6月7日
     * @return: Map<String, Object> 
     */
    @RequestMapping("/queryWorkStatusList")
    public @ResponseBody Map<String, Object> queryWorkStatusList( int rows, int page, String search,String sort, String order,Boolean isOpr,String checkInvalid,String checkExclude ){
       Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        List<WorkStatusBean> result = new ArrayList<WorkStatusBean>();
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setParameter( "sort", sort );
            pageVo.setParameter( "order", order );
        }
        pageVo.setParameter( "isOpr", isOpr?"Y":"N" );
        pageVo.setParameter( "checkInvalid", checkInvalid);
        pageVo.setParameter( "checkExclude", checkExclude);
        
      //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            HashMap<String, Object> map = VOUtil.fromJsonToHashMap( search);
            result = workStatusService.queryWorkStatusListBySearch( map, pageVo );
        }else{
            //默认分页
            result = workStatusService.queryWorkStatusListBySearch( null,pageVo );
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

    @RequestMapping("/report")
    public ModelAndView report(){
        Map<String, Object> map = new HashMap<String, Object>();
        Configuration configuration = confManager.query( "num_of_retain_atd_machine_data_years", "NaN", "NaN" );
        Date now=new Date();
    	Date startDate=DateFormatUtil.addDate(now, "y", -(Integer.parseInt(configuration.getVal())));//该日期后有报表数据
    	Calendar calendar=DateFormatUtil.fromDateToCalendar( startDate );
        map.put( "startYear", calendar.get(Calendar.YEAR) );
        map.put( "startMonth", calendar.get(Calendar.MONTH) );
        return new ModelAndView("checkin/WorkStatus-report.jsp", map);
    }
}
