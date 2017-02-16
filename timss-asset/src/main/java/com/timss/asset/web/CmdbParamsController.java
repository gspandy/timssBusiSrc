package com.timss.asset.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.asset.bean.CmdbParamsBean;
import com.timss.asset.service.CmdbParamsServie;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 参数项值
 * @description: {desc}
 * @company: gdyd
 * @className: CmdbParamsController.java
 * @author: fengzt
 * @createDate: 2015年8月11日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("asset/cmdbParams")
public class CmdbParamsController {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private CmdbParamsServie cmdbParamsServie;
    
    /**
     * 
     * @description:查看参数项明细page
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param id
     * @return:ModelAndView
     */
    @RequestMapping("/insertCmdbParamsMenu")
    @ReturnEnumsBind("CMDB_PARAM_TYPE,CMDB_CI_TYPE")
    public ModelAndView insertCmdbParamsMenu( ){
        return new ModelAndView( "cmdb/CmdbParams-insertCmdbParams.jsp");
    }
    /**
     * 
     * @description:查看参数项明细page
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param id
     * @return:ModelAndView
     */
    @RequestMapping("/queryCmdbParamsDetailMenu")
    @ReturnEnumsBind("CMDB_PARAM_TYPE,CMDB_CI_TYPE")
    public ModelAndView queryCmdbParamsDetailMenu( String id ){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "id", id );
        
        return new ModelAndView( "cmdb/CmdbParams-updateCmdbParams.jsp", map );
    }
    
    /**
     * 
     * @description:新增
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param formData
     * @return:
     */
    @RequestMapping(value = "/insertCmdbParams",method=RequestMethod.POST)
    public Map<String, Object> insertCmdbParams( String formData ){
        Map<String, Object> map = new HashMap<String, Object>();
        CmdbParamsBean bean = JsonHelper.fromJsonStringToBean( formData, CmdbParamsBean.class );
        int count = cmdbParamsServie.insertOrUpdateCmdbParams( bean );
        
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        
        return map;
        
        
    }

    /**
     * @description:查询列表
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param rows
     * @param page
     * @param search
     * @param sort
     * @param order
     * @return:
     * @throws Exception 
     * @throws JsonParseException 
     */
    @RequestMapping("/queryCmdbParamsBySiteId")
    public Page<CmdbParamsBean> queryCmdbParamsBySiteId(int rows, int page, String search, String sort, String order) throws JsonParseException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<CmdbParamsBean> paramsPage = userInfoScope.getPage();
        paramsPage.setParameter( "siteId", userInfoScope.getSiteId() );

        // 获取表头搜索的参数，Dao的xml文件里面不用写 if is null这些方法了
        if ( StringUtils.isNotBlank( search ) ) {
            Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( search );
            paramsPage.setFuzzyParams( fuzzyParams );
        }

        // 设置排序内容
        if ( StringUtils.isNotBlank( sort ) ) {
            paramsPage.setSortKey( sort );
            paramsPage.setSortOrder( order );
        } else {
            // 设置默认的排序字段
            paramsPage.setSortKey( "ciType, paramType, orderType" );
            paramsPage.setSortOrder( "ASC" );
        }

        paramsPage = cmdbParamsServie.queryCmdbParamsBySiteId( paramsPage );
        return paramsPage;
    }
    
    /**
     * 
     * @description:当改变CI类型、参数项时触发
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param ciType
     * @param paramType
     * @return:Map<String, Object> --排序最大值 + 10
     */
    @RequestMapping("/queryOderTypeByType")
    public @ResponseBody Map<String, Object> queryOderTypeByType( String ciType, String paramType ){
        //递减 desc 递增 asc
        String sort = "DESC";
        List<CmdbParamsBean> list = cmdbParamsServie.queryOderTypeByType( ciType, paramType, sort, null);
        
        int orderType = 10;
        if( list != null && list.size() > 0 ){
            CmdbParamsBean bean = list.get( 0 );
            orderType += bean.getOrderType();
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "result", orderType );
        return map;
    }
    
    /**
     * 
     * @description:通过ID查询参数项值
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param id
     * @return:Map<String, Object> --排序最大值 + 10
     */
    @RequestMapping("/queryCmdbParamsById")
    public @ResponseBody Map<String, Object> queryCmdbParamsById( String id ){
        CmdbParamsBean bean = cmdbParamsServie.queryCmdbParamsById( id );
        
        Map<String, Object> map = new HashMap<String, Object>();
        if( bean != null && StringUtils.isNotBlank( bean.getId() ) ){
            map.put( "result", "success" );
            map.put( "bean", bean );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }
    
    /**
     * 
     * @description:通过ID删除参数项值
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param id
     * @return:Map<String, Object> --排序最大值 + 10
     */
    @RequestMapping("/deleteCmdbParamsById")
    public @ResponseBody Map<String, Object> deleteCmdbParamsById( String id ){
        int count = cmdbParamsServie.deleteCmdbParamsById( id );
        
        Map<String, Object> map = new HashMap<String, Object>();
        if( count > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }
    
    
}
