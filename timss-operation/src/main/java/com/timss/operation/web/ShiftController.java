package com.timss.operation.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.operation.bean.Shift;
import com.timss.operation.service.ShiftService;
import com.timss.operation.util.VOUtil;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 班次controller
 * @description:
 * @company: gdyd
 * @className: ShiftController.java
 * @author: huanglw
 * @createDate: 2014年6月11日
 * @updateUser: huanglw
 * @version: 1.0
 */
@Controller
@RequestMapping("operation/shift")
public class ShiftController {
    private Logger log = Logger.getLogger( ShiftController.class );

    @Autowired
    private ShiftService shiftService;
    
    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * @description:保存班次
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param formData JSON String
     * @return:Map<String, Object>
     */
    @RequestMapping("/insertShift")
    public Map<String, Object> insertShift(String formData) {
        
        Shift shift = VOUtil.fromJsonToVoUtil( formData, Shift.class );
        shift.setNum( "" );
        //用户登录的站点
        String siteId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            siteId = userInfoScope.getSecureUser().getCurrentSite();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        shift.setSiteId( siteId );
        shift = shiftService.insertShift( shift );

        Map<String, Object> map = new HashMap<String, Object>();

        if ( shift.getId() > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;

    }

    /**
     * @description:班次列表 分页
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param rows 一页有多少条
     * @param page 第几页
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryAllShiftList")
    public Map<String, Object> queryAllShiftList(int rows, int page, String search ) {
        
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        
        //用户登录的站点
        String siteId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            siteId = userInfoScope.getSecureUser().getCurrentSite();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        pageVo.setParameter( "siteId", siteId );
        
        List<Shift> shiftList = new ArrayList<Shift>();
        
        //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            HashMap<String, Object> map = VOUtil.fromJsonToHashMap( search);
            shiftList = shiftService.queryShiftBySearch( map, pageVo );
            
        }else{
            //默认分页
            shiftList = shiftService.queryShiftByPage( pageVo );
        }


        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", shiftList );
        dataMap.put( "total", pageVo.getTotalRecord() );
        return dataMap;
    }
    
    /**
     * @description:更新班次
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/updateShift") 
    public Map<String, Object> updateShift(String formData) {

        Shift shift = VOUtil.fromJsonToVoUtil( formData, Shift.class );

       int count = shiftService.updateShift( shift );

        Map<String, Object> map = new HashMap<String, Object>();

        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;

    }
    
    /**
     * @description:删除班次
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/deleteShift")
    public Map<String, Object> deleteShift(String formData) {
        
        Shift shift = VOUtil.fromJsonToVoUtil( formData, Shift.class );
        
        int count = 0 ;
        if( shift.getId() > 0 ){
            count = shiftService.deleteShiftById( shift.getId() );
        }
        
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
     * @description:通过岗位ID获取班次
     * @author: huanglw
     * @createDate: 2014年6月16日
     * @param stationId
     * @return:
     */
    @RequestMapping("/queryShiftByStationId")
    public Map<String, Object> queryShiftByStationId(String stationId){
       
        List<Shift> shiftList = new ArrayList<Shift>();
        shiftList = shiftService.queryShiftByStationId( stationId );
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", shiftList );
        dataMap.put( "total", shiftList.size());
        return dataMap;
    }
    
    /**
     * 
     * @description:通过ID获取班次
     * @author: huanglw
     * @createDate: 2014年6月16日
     * @param stationId
     * @return:
     */
    @RequestMapping("/queryShiftById")
    public @ResponseBody Map<String, Object> queryShiftById(int id){
        
        Shift shift = shiftService.queryShiftById( id );
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "result", shift );
        return dataMap;
    }

    /**
     * 
     * @description:通过岗位拿到排序值
     * @author: fengzt
     * @createDate: 2014年7月29日
     * @param stationId
     * @return:Map<String, Object>
     */
    @RequestMapping("/querySortTypeByStationId")
    public Map<String, Object> querySortTypeByStationId( String stationId ){
        int sortType = shiftService.querySortTypeByStationId( stationId );
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "result", sortType );
        return dataMap;
        
    }
    
}
