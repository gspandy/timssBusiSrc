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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.operation.bean.Duty;
import com.timss.operation.service.DutyService;
import com.timss.operation.util.OprUserPrivUtil;
import com.timss.operation.util.VOUtil;
import com.timss.operation.vo.RoleVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: 值别表controller
 * @description: {desc}
 * @company: gdyd
 * @className: DutyController.java
 * @author: fengzt
 * @createDate: 2014年6月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("operation/duty")
public class DutyController {
    private Logger log = Logger.getLogger( DutyController.class );

    @Autowired
    private DutyService dutyService;
    @Autowired
    private OprUserPrivUtil OprUserPrivUtil;

    /**
     * @description:保存值别表
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     * @throws Exception 
     */
    @RequestMapping("/insertDuty")
    public Map<String, Object> insertDuty(String formData, String treeData) throws Exception {

        Duty duty = VOUtil.fromJsonToVoUtil( formData, Duty.class );
        duty.setNum( "" );

        //用户登录的站点
        String siteId = null;
        try {
            UserInfoScope userInfoScope = OprUserPrivUtil.getUserInfoScope();
            siteId = userInfoScope.getSecureUser().getCurrentSite();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        duty.setSiteId( siteId );
        
        HashMap<String, Object> treeMap = StringUtils.isEmpty(treeData)?new HashMap<String, Object>():VOUtil.fromJsonToHashMap( treeData );
        
        Map<String, Object>  insertMap = new HashMap<String, Object>();
        insertMap = dutyService.insertDuty( duty, treeMap );

        Map<String, Object> map = new HashMap<String, Object>();
        if(insertMap.containsKey("success")){
        	duty = (Duty) insertMap.get("success");
        	 if ( duty.getId() > 0 ) {
                 map.put( "result", "success" );
             } else {
                 map.put( "result", "fail" );
             }
        }else if(insertMap.containsKey("fail")){
        	map.put("result", insertMap.get("fail"));
        }
       
        return map;

    }

    /**
     * @description:值别列表 分页
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param rows 一页有多少条
     * @param page 第几页
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryAllDutyList")
    public Map<String, Object> queryAllDutyList(int rows, int page, String search ) {
        
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        //用户登录的站点
        String siteId = null;
        try {
            UserInfoScope userInfoScope = OprUserPrivUtil.getUserInfoScope();
            siteId = userInfoScope.getSecureUser().getCurrentSite();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        pageVo.setParameter( "siteId", siteId );
        
        List<Duty> dutyList = new ArrayList<Duty>();
        
        //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            HashMap<String, Object> map = VOUtil.fromJsonToHashMap( search);
            dutyList = dutyService.queryDutyBySearch( map, pageVo );
            
        }else{
            //默认分页
            dutyList = dutyService.queryDutyByPage( pageVo );
        }
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", dutyList );
        dataMap.put( "total", pageVo.getTotalRecord() );
        return dataMap;
    }
    
    /**
     * @description:更新值别
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     * @throws Exception 
     */
    @RequestMapping("/updateDuty")
    public Map<String, Object> updateDuty(String formData, String userDel, String userAdd) throws Exception {

        Duty duty = VOUtil.fromJsonToVoUtil( formData, Duty.class );

       String updateInfo = dutyService.updateDuty( duty, userDel, userAdd );

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("result", updateInfo);
        
        return map;

    }
    
    /**
     * @description:删除值别
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/deleteDuty")
    public Map<String, Object> deleteDuty(String formData) {
        
        Duty duty = VOUtil.fromJsonToVoUtil( formData, Duty.class );
        
        int count = 0 ;
        if( duty.getId() > 0 ){
            count = dutyService.deleteDutyById( duty.getId() );
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
     * @description:通过岗位ID获取值别
     * @author: huanglw
     * @createDate: 2014年6月18日
     * @param stationId
     * @return:
     */
    @RequestMapping("/queryDutyByStationId")
    public Map<String, Object> queryDutyByStationId(String stationId){
       
        List<Duty> dutyList = new ArrayList<Duty>();
        dutyList = dutyService.queryDutyByStationId( stationId );
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", dutyList );
        dataMap.put( "total", dutyList.size());
        return dataMap;
    }
    
    /**
     * 
     * @description:通过当前登录站点取岗位信息
     * @author: fengzt
     * @createDate: 2014年6月24日
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryStationInfoBySitId")
    public Map<String, Object> queryStationInfoBySitId(){
        //用户登录的站点
        String siteId = null;
        try {
            UserInfoScope userInfoScope = OprUserPrivUtil.getUserInfoScope();
            siteId = userInfoScope.getSecureUser().getCurrentSite();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }

        List<RoleVo> rolesList = new ArrayList<RoleVo>();
        rolesList = dutyService.queryStationInfoBySitId( siteId );
        
        //后期 加上岗位默认值
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "result", rolesList );
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
    @RequestMapping("/queryDutyById")
    public @ResponseBody Map<String, Object> queryDutyById(int id){
        
        Duty duty = dutyService.queryDutyById( id );
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "result", duty );
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
        int sortType = dutyService.querySortTypeByStationId( stationId );
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "result", sortType );
        return dataMap;
        
    }
    
    /**
     * 
     * @description:列表双击跳转修改页面查询关系用户
     * @author: zhuw
     * @createDate: 2016年1月22日
     * @param formData
     * @return:
     */
    @RequestMapping("/updateDutyForm")
    public ModelAndView updateDutyForm(int id){
        Map<String, Object> dataMap = dutyService.queryOrgsRelatedToUsers( id );
        return new ModelAndView( "schedule/Duty-updateDuty.jsp", dataMap );
    }
    
    /**
     * 用于搜索框的查询
     * 根据工号或姓名检索站点下的运行人员
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryDutyPersonHint", method = RequestMethod.POST)
    public @ResponseBody String queryDutyPersonHint(Integer dutyId,String kw) throws Exception {
        UserInfoScope userInfo = OprUserPrivUtil.getUserInfoScope();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (kw!=null||!"".equals(kw)){
        	result=dutyService.queryDutyPersonsForHint(null,userInfo.getSiteId(),kw);// 可性能优化，前台只展示前十条，可考虑只查询10条，已优化
        }
        return VOUtil.fromVoToJsonUtil(result);
    }
}
