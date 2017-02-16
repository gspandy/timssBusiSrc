package com.timss.attendance.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.logging.Log;
import com.timss.attendance.bean.DefinitionBean;
import com.timss.attendance.service.CheckMachineService;
import com.timss.attendance.service.DefinitionService;
import com.timss.attendance.service.WorkStatusService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.configs.MvcWebConfig;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 考勤系统参数定义
 * @description: 休假规则
 * @company: gdyd
 * @className: DefinitionController.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("attendance/definition")
public class DefinitionController {
    
    @Autowired
    private DefinitionService definitionService;
    @Autowired
    private CheckMachineService checkMachineService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkStatusService workStatusService;
    @Autowired
    private AtdUserPrivUtil privUtil;
    
    /**
     * 
     * @description:插入考勤系统参数
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @param formData
     * @return:Map<String, Object>
     */
    @RequestMapping("/insertDefinition")
    public Map<String, Object> insertDefinition( String formData ){
        DefinitionBean definitionBean = JsonHelper.fromJsonStringToBean( formData, DefinitionBean.class );
        
        int count = definitionService.insertDefinition( definitionBean );
        Map<String, Object> map = new HashMap<String, Object>();

        if ( count == 10000000 ) {
            map.put( "result", "fail" );
            map.put( "reason", "当前站点已经存在休假规则！" );
        } else if ( count == 1 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * 
     * @description:通过站点查询考勤系统参数定义
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return: Map<String, Object> 
     */
    @RequestMapping("/queryDefinitionBySite")
    public @ResponseBody Map<String, Object> queryDefinitionBySite(){
        DefinitionBean definitionBean = definitionService.queryDefinitionBySite();
        Map<String, Object> map = new HashMap<String, Object>();
        
        if( definitionBean != null ){
            map.put( "rowData", definitionBean );
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
            if(privUtil.getUserInfoScope().getSecureUser().isSuperAdmin()){
            	map.put( "redirect", MvcWebConfig.serverBasePath+"page/attendance/core/checkin/Definition-insertDefinition.jsp" );
            }
        }
        
        return map;
    }
    
    /**
     * 
     * @description:更新考勤系统定义
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @return: Map<String, Object>
     */
    @RequestMapping("/updateDefinition")
    public Map<String, Object> updateDefinition( String formData ){
        
        DefinitionBean vo = JsonHelper.fromJsonStringToBean( formData, DefinitionBean.class );
        
        int count = definitionService.updateDefinition( vo );
        
        Map<String, Object> map = new HashMap<String, Object>();

        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    @RequestMapping("/checkMachine")
    public Map<String, Object> checkMachine()throws Exception{
    	UserInfoScope user=privUtil.getUserInfoScope();
    	Map<String, Object> map = new HashMap<String, Object>();
    	if(user==null){
    		map.put( "result", "logout" );
    	}else{
    		checkMachineService.importCheckMachineData(user.getSiteId());
            map.put( "result", "success" );
    	}
    	
        return map;
    }
    
    @RequestMapping("/checkMachineStatus")
    public Map<String, Object> checkMachineStatus()throws Exception{
    	UserInfoScope user=privUtil.getUserInfoScope();
    	Map<String, Object> map = new HashMap<String, Object>();
    	if(user==null){
    		map.put( "result", "logout" );
    	}else{
    		checkMachineService.checkMachineStatusAndNotify(user.getSiteId());
            map.put( "result", "success" );
    	}
    	
        return map;
    }
    
    @RequestMapping("/checkWorkStatus")
    public Map<String, Object> checkWorkStatus()throws Exception{
    	UserInfoScope user=privUtil.getUserInfoScope();
        Map<String, Object> map = new HashMap<String, Object>();
        if(user==null){
    		map.put( "result", "logout" );
    	}else{
    		workStatusService.checkWorkStatus(user.getSiteId());
            map.put( "result", "success" );
    	}
        return map;
    }
}
