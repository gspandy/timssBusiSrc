package com.timss.operation.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.operation.bean.Dept;
import com.timss.operation.service.DeptService;
import com.timss.operation.util.VOUtil;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 部门controller
 * @description:
 * @company: gdyd
 * @className: DeptController.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("operation/dept")
public class DeptController {
    private Logger log = Logger.getLogger( DeptController.class );

    @Autowired
    private DeptService deptService;

    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * @description:新建一条部门
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData JSON String
     * @return:Map<String, Object>
     */
    @RequestMapping("/insertDept")
    public Map<String, Object> insertDept(String formData) {
        
        Dept dept = VOUtil.fromJsonToVoUtil( formData, Dept.class );
        //用户登录的站点
        String siteId = null;
        String userId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            siteId = userInfoScope.getSecureUser().getCurrentSite();
            userId = userInfoScope.getUserId();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }

        //siteId + "_opr_" + 用户输入
        //String deptId = siteId + "_opr_" + dept.getDeptId().trim() ;
        String deptId = siteId + "_opr_" + UUID.randomUUID() ;
        dept.setDeptId( deptId );
        dept.setActive( "Y" );
        
        dept.setUpdateBy( userId );
        dept.setSiteId( siteId );
        dept.setUpdateTime( new Date() );
        
        
        int count = deptService.insertDept( dept );

        Map<String, Object> map = new HashMap<String, Object>();

        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;

    }

    /**
     * @description:部门列表 分页
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param rows 一页有多少条
     * @param page 第几页
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryAllDeptList")
    public Map<String, Object> queryAllDeptList(int rows, int page, String search ) {
        
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        
        //相应的用户只能查看单个站点的部门列表
        //用户登录的站点
        String siteId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            siteId = userInfoScope.getSecureUser().getCurrentSite();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }

        String deptId = siteId + "_opr_" ;
        pageVo.setParameter( "deptId", deptId );
        
        List<Dept> deptList = new ArrayList<Dept>();
        
        //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            HashMap<String, Object> map = VOUtil.fromJsonToHashMap( search);
            deptList = deptService.queryDeptBySearch( map, pageVo );
            
        }else{
            //默认分页
            deptList = deptService.queryDeptByPage( pageVo );
        }


        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", deptList );
        dataMap.put( "total", pageVo.getTotalRecord() );
        return dataMap;
    }
    
    /**
     * @description:更新部门
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/updateDept") 
    public Map<String, Object> updateDept(String formData) {

        Dept dept = VOUtil.fromJsonToVoUtil( formData, Dept.class );

       int count = deptService.updateDept( dept );

        Map<String, Object> map = new HashMap<String, Object>();

        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;

    }
    
    /**
     * @description:删除部门
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/deleteDept")
    public Map<String, Object> deleteDept(String formData) {
        
        Dept dept = VOUtil.fromJsonToVoUtil( formData, Dept.class );
        
        int count = 0 ;
        if(StringUtils.isNotBlank( dept.getDeptId() ) ){
            count = deptService.deleteDeptById( dept.getDeptId() );
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
     * @description:通过部门ID查找dept
     * @author: fengzt
     * @createDate: 2014年7月8日
     * @param deptId
     * @return:map
     */
    @RequestMapping("/queryDeptByDeptId")
    public @ResponseBody Map<String, Object> queryDeptByDeptId( String deptId ){
        Dept deptVo = deptService.queryDeptById( deptId );
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "result", deptVo );
        return map;
    }
    
}
