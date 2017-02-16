package com.timss.asset.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 目录controller
 * @description: 资产台账目录
 * @company: gdyd
 * @className: AssetMenuController.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("asset/assetMenu")
public class AssetMenuController {
    
    @Autowired
    private ItcMvcService itcMvcService;
    


    /**
     * 
     * @description:硬件台账
     * @author: fengzt
     * @createDate: 2014年11月21日
     * @return:String
     */
    @RequestMapping("/queryHardwareMenu")
    @ReturnEnumsBind("AST_HW_TYPE,AST_HW_SERVICE_STATUS,AST_HW_YES_NO,AST_HW_VMDEPLOC,AST_SW_APP_TYPE,AST_HW_NETAREA")
    public String queryHardwareMenu(){
        return "hardware/HwLedger-updateHwLedger.jsp";
    }
    
    /**
     * 
     * @description:软件台账
     * @author: fengzt
     * @createDate: 2014年11月21日
     * @return:String
     */
    @RequestMapping("/querySoftwareMenu")
    public String querySoftwareMenu(){
        return "assetinfo/assetTree.jsp";
    }
    
    /**
     * 
     * @description:硬件类型维护
     * @author: fengzt
     * @createDate: 2014年11月21日
     * @return:String
     */
    @RequestMapping("/queryHardwareMaintainMenu")
    public String queryHardwareMaintainMenu(){
        return "assetinfo/assetTree.jsp";
    }
    
    /**
     * 
     * @description:CMDB参数项列表
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @return:String
     */
    @RequestMapping("/queryCmdbParamsMenu")
    @ReturnEnumsBind("CMDB_PARAM_TYPE,CMDB_CI_TYPE")
    public String queryCmdbParamsMenu(){
        return "cmdb/CmdbParams-getAllCmdbParamsList.jsp";
    }
    
    /**
     * 
     * @description:CMDB配置项列表
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @return:String
     */
    @RequestMapping("/queryCmdbCiMenu")
    @ReturnEnumsBind("CMDB_CI_TYPE")
    public ModelAndView queryCmdbCiMenu(){
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", userInfoScope.getSiteId() );
        
        return new ModelAndView( "cmdb/CmdbCi-getAllCmdbCiList.jsp", map );
    }
    
    
}
