package com.timss.ptw.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.ptw.bean.PtwType;
import com.timss.ptw.bean.PtwTypeDefine;
import com.timss.ptw.service.PtwTypeDefineService;
import com.timss.ptw.service.PtwTypeService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 工作票类型
 * @description: {desc}
 * @company: gdyd
 * @className: PtwTypeController.java
 * @author: 周保康
 * @createDate: 2014-7-8
 * @updateUser: 周保康
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "ptw/ptwType")
public class PtwTypeController {
    private static final Logger log = Logger.getLogger(PtwTypeController.class);
    
    @Autowired
    private PtwTypeService ptwTypeService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private PtwTypeDefineService ptwTypeDefineService;
    
    /**
     * 工作票类型
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-8
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryPtwTypes")
    public @ResponseBody ArrayList<ArrayList<Object>> queryPtwTypes() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String queryType = userInfoScope.getParam( "queryType" );
        String defaultTypeIdStr = userInfoScope.getParam( "defaultTypeId" );
        Integer defaultTypeId = defaultTypeIdStr == null ? null : Integer.parseInt( defaultTypeIdStr );
        //查询参数中的是否为动火票信息
        Integer isFireWt = null;
        String ptwOrIsl = userInfoScope.getParam( "ptwOrIsl" );
        String defaultType = "工作票类型";
        if ( ptwOrIsl != null && ptwOrIsl.equals( "isl" ) ) {
            defaultType = "隔离证类型";
        }
        ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        if ( queryType == null) {
            isFireWt = null;
            ArrayList<Object> def = new ArrayList<Object>();
            def.add(0);
            def.add(defaultType);
            def.add(true);
            result.add(def);
        } else if ( queryType.equals( "normal" ) ) {
            isFireWt = 0;
        } else if ( queryType.equals( "fire" ) ) {
            isFireWt = 1;
        }else if( queryType.equals( "all" )){
            isFireWt = null;
        }
        List<PtwType> ptwTypes = ptwTypeService.queryTypesBySiteId( userInfoScope.getSiteId(),isFireWt );
        for ( PtwType ptwType : ptwTypes ) {
            ArrayList<Object> row = new ArrayList<Object>();
            row.add(ptwType.getId());
            row.add(ptwType.getTypeName());
            if (defaultTypeId != null &&  ptwType.getId() == defaultTypeId ) {
                row.add( true );
            }
            result.add(row);
        }
        return result;
    }
    
    /**
     * 查询工作票类型及其定义的信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-14
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryPtwTypeInfo")
    public @ResponseBody HashMap<String, Object> queryPtwTypeInfo() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int ptwTypeId = Integer.parseInt(userInfoScope.getParam( "ptwTypeId" ));
        PtwType ptwType = ptwTypeService.queryPtwTypeById( ptwTypeId );
        PtwTypeDefine ptwTypeDefine = ptwTypeDefineService.queryPtwTypeDefineById( ptwType.getWtTypeDefineId() );
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "ptwType", ptwType );
        result.put( "ptwTypeDefine", ptwTypeDefine );
        return result;
    }
}
