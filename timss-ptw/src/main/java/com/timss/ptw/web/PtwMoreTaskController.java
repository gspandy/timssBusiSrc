package com.timss.ptw.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.timss.ptw.bean.PtwChangeWpic;
import com.timss.ptw.bean.PtwExtand;
import com.timss.ptw.bean.PtwRemarkTask;
import com.timss.ptw.bean.PtwWaitRestore;
import com.timss.ptw.service.PtwChangeWpicService;
import com.timss.ptw.service.PtwExtandService;
import com.timss.ptw.service.PtwRemarkTaskService;
import com.timss.ptw.service.PtwWaitRestoreService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 更多办理
 * @description: {desc}
 * @company: gdyd
 * @className: PtwMoreTaskController.java
 * @author: 周保康
 * @createDate: 2014-8-21
 * @updateUser: 周保康
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "ptw/ptwMore")
public class PtwMoreTaskController {
    private static final Logger log = Logger.getLogger(PtwMoreTaskController.class);
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private PtwChangeWpicService ptwChangeWpicService;
    
    @Autowired
    private PtwExtandService ptwExtandService;
    
    @Autowired
    private PtwWaitRestoreService ptwWaitRestoreService;
    
    @Autowired
    private PtwRemarkTaskService ptwRemarkTaskService;
    
    @RequestMapping("/moreChangeWpicPage")
    public ModelAndView moreChangeWpicPage( ) throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String chaOldWpic = userInfoScope.getParam( "chaOldWpic" );
        String ptwTypeCode = userInfoScope.getParam( "ptwTypeCode" );
        
        Map<String, Object> map = new HashMap<String, Object>( 0 );
        map.put( "chaOldWpic", chaOldWpic );
        map.put( "ptwTypeCode", ptwTypeCode );
        
        String jumpUrl = "moreChangeWpic.jsp";  //跳转页面
        ModelAndView modelAndView = new ModelAndView( jumpUrl, map );
        return modelAndView ;
    }
    /**
     * 插入工作负责人变更信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-21
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/insertPtwChangeWpic")
    public HashMap<String, Object> insertPtwChangeWpic() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String changeWpic = userInfoScope.getParam( "changeWpicParam" );
        PtwChangeWpic ptwChangeWpic = JsonHelper.fromJsonStringToBean( changeWpic, PtwChangeWpic.class );
        ptwChangeWpic.setChaWl( userInfoScope.getUserName() );
        ptwChangeWpic.setChaWlNo( userInfoScope.getUserId() );
        ptwChangeWpicService.insertPtwChangeWpic( ptwChangeWpic );
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "result", "ok" );
        return result;
    }
    
    @RequestMapping(value = "/insertPtwExtand")
    public HashMap<String, Object> insertPtwExtand() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String extand = userInfoScope.getParam( "extandParam" );
        PtwExtand ptwExtand = JsonHelper.fromJsonStringToBean( extand, PtwExtand.class );
        ptwExtand.setExtWl( userInfoScope.getUserName() );
        ptwExtand.setExtWlNo( userInfoScope.getUserId() );
        ptwExtandService.insertPtwExtand( ptwExtand );
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "result", "ok" );
        return result;
    }
    
    @RequestMapping(value = "/insertPtwWait")
    public HashMap<String, Object> insertPtwWait() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String wait = userInfoScope.getParam( "waitParam" );
        PtwWaitRestore ptwWaitRestore = JsonHelper.fromJsonStringToBean( wait, PtwWaitRestore.class );
        ptwWaitRestore.setWitWl( userInfoScope.getUserName() );
        ptwWaitRestore.setWitWlNo( userInfoScope.getUserId() );
        ptwWaitRestoreService.insertPtwWait( ptwWaitRestore );
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "result", "ok" );
        result.put( "ptwWaitRestoreId", ptwWaitRestore.getId() );
        return result;
    }
    
    @RequestMapping(value = "/upatePtwRestore")
    public HashMap<String, Object> upatePtwRestore() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String restore = userInfoScope.getParam( "restoreParam" );
        PtwWaitRestore ptwWaitRestore = JsonHelper.fromJsonStringToBean( restore, PtwWaitRestore.class );
        ptwWaitRestore.setResWl( userInfoScope.getUserName() );
        ptwWaitRestore.setResWlNo( userInfoScope.getUserId() );
        ptwWaitRestoreService.updatePtwRestore( ptwWaitRestore );
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "result", "ok" );
        return result;
    }
    
    @RequestMapping(value = "/insertRemarkTask")
    public HashMap<String, Object> insertRemarkTask() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String remarkTask = userInfoScope.getParam( "remarkTaskParam" );
        PtwRemarkTask ptwRemarkTask = JsonHelper.fromJsonStringToBean( remarkTask, PtwRemarkTask.class );
        ptwRemarkTask.setRemarkWl( userInfoScope.getUserName() );
        ptwRemarkTask.setRemarkWlNo( userInfoScope.getUserId() );
        ptwRemarkTaskService.insertPtwRemarkTask( ptwRemarkTask );
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "result", "ok" );
        return result;
    }
}
