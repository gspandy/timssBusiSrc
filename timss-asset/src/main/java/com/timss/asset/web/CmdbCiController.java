package com.timss.asset.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.asset.bean.CmdbParamsBean;
import com.timss.asset.bean.CmdbPubCiBean;
import com.timss.asset.bean.CmdbRelationBean;
import com.timss.asset.service.CmdbCiServie;
import com.timss.asset.service.CmdbParamsServie;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.AppLog;
import com.yudean.itc.manager.support.LogTempService;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.supplier.bean.SupBaseInfo;
import com.yudean.supplier.service.SupplierForModelService;
import com.yudean.supplier.vo.SupBaseInfoVo;

/**
 * 
 * @title: Ci配置项
 * @description: {desc}
 * @company: gdyd
 * @className: CmdbCiController.java
 * @author: fengzt
 * @createDate: 2015年8月24日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("asset/cmdbCi")
public class CmdbCiController {
    private Logger log = Logger.getLogger( CmdbCiController.class );
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private CmdbCiServie cmdbCiServie;
    
    @Autowired
    private CmdbParamsServie cmdbParamsServie;
    
    @Autowired
    private LogTempService logTempService;
    @Autowired
    SupplierForModelService supplierForModelService;

    /**
     * 
     * @description:新建page
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param id
     * @return:ModelAndView
     */
    @RequestMapping("/insertCmdbPubCiMenu")
    @ReturnEnumsBind("CMDB_CI_TYPE")
    public ModelAndView insertCmdbPubCiMenu( ){
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String userName = userInfoScope.getUserName();
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put( "userId", userId );
        map.put( "userName", userName );
        
        return new ModelAndView( "cmdb/CmdbCi-insertCmdbCi.jsp", map );
    }
    
    /**
     * 
     * @description:关联关系列表page
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param id
     * @return:ModelAndView
     */
    @RequestMapping("/queryCmdbRelationMenu")
    @ReturnEnumsBind("CMDB_CI_TYPE")
    public ModelAndView queryCmdbRelationMenu( String ciId ){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "ciId", ciId );
        return new ModelAndView( "cmdb/CmdbCi-getCmdbCiRelationList.jsp", map );
    }
    
    /**
     * 
     * @description:查询日志PAGE
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param id
     * @return:ModelAndView
     */
    @RequestMapping("/queryCmdbCiLogMenu")
    public ModelAndView queryCmdbCiLogMenu( String ciId ){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "ciId", ciId );
        return new ModelAndView( "cmdb/CmdbCi-getCmdbCiLogList.jsp", map );
    }
    
    /**
     * 
     * @description:新建page
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param id
     * @return:ModelAndView
     */
    @RequestMapping("/insertCmdbRelationMenu")
    @ReturnEnumsBind("CMDB_CI_TYPE")
    public ModelAndView insertCmdbRelationMenu( String type, String ciId ){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "type", type );
        map.put( "ciId", ciId );
        CmdbPubCiBean bean = cmdbCiServie.queryCmdbPubCiById( ciId );
        map.put( "bean", JsonHelper.fromBeanToJsonString( bean ) );
        
        return new ModelAndView( "cmdb/CmdbCi-insertCmdbRelation.jsp", map );
    }
    
    /**
     * 
     * @description:更新page
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param id
     * @return:ModelAndView
     */
    @RequestMapping("/updateCmdbRelationMenu")
    @ReturnEnumsBind("CMDB_CI_TYPE")
    public ModelAndView updateCmdbRelationMenu( String id, String ciId ){
        Map<String, Object> map = new HashMap<String, Object>();
        String type = null;
       
        map.put( "ciId", ciId );
        CmdbRelationBean bean = cmdbCiServie.queryCmdbRelationById( id );
        //判断现在是上联 还是下联
        if( StringUtils.equals( ciId, bean.getUpCiId() ) ){
            type = "down";
        }else{
            type = "up";
        }
        map.put( "type", type );
        map.put( "bean", JsonHelper.fromBeanToJsonString( bean ) );
        
        return new ModelAndView( "cmdb/CmdbCi-updateCmdbRelation.jsp", map );
    }
    
    /**
     * 
     * @description:查看配置项明细page
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param id
     * @return:ModelAndView
     */
    @RequestMapping("/queryCmdbPubCiDetailMenu")
    @ReturnEnumsBind("CMDB_PARAM_TYPE,CMDB_CI_TYPE")
    public ModelAndView queryCmdbPubCiDetailMenu( String id, String ciType ){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "id", id );
        map.put( "ciType", ciType );
        //CI子类
        String subType = "CMDB_PARAM_1";
        //CI状态
        String status = "CMDB_PARAM_2";
        //递减 desc 递增 asc
        String sort = "DESC";
        List<CmdbParamsBean> subTypeList = cmdbParamsServie.queryOderTypeByType( ciType, subType, sort, null );
        List<CmdbParamsBean> statusList = cmdbParamsServie.queryOderTypeByType( ciType, status, sort, null );
        
        map.put( "subTypeList", JsonHelper.toJsonString( subTypeList ) );
        map.put( "statusList", JsonHelper.toJsonString( statusList ) );
        
        return new ModelAndView( "cmdb/CmdbCi-updateCmdbCi.jsp", map );
    }
    
    /**
     * 
     * @description:新增
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param formData
     * @return:
     */
    @RequestMapping(value = "/insertCmdbPubCi",method=RequestMethod.POST)
    public Map<String, Object> insertCmdbPubCi( String formData ){
        Map<String, Object> map = new HashMap<String, Object>();
        CmdbPubCiBean bean = JsonHelper.fromJsonStringToBean( formData, CmdbPubCiBean.class );
        int count = cmdbCiServie.insertOrUpdateCmdbPubCi( bean );
        
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
    @RequestMapping("/queryCmdbPubCiBySiteId")
    public Page<CmdbPubCiBean> queryCmdbPubCiBySiteId(int rows, int page, String search, String sort, String order) throws JsonParseException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<CmdbPubCiBean> paramsPage = userInfoScope.getPage();
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
            paramsPage.setSortKey( "ciType, name" );
            paramsPage.setSortOrder( "ASC" );
        }

        paramsPage = cmdbCiServie.queryCmdbPubCiBySiteId( paramsPage );
        return paramsPage;
    }
    
    /**
     * 
     * @description:检查CI名称在同一个站点下是否存在
     * @author: fengzt
     * @createDate: 
     * @return:BOOLEAN
     */
    @RequestMapping(value = "/queryCheckCmdbCiName")
    public @ResponseBody Boolean queryCheckCmdbCiName( String paramsMap ) {
        JSONObject object = JSONObject.fromObject(paramsMap);
        String name = object.get("name").toString();
        String id = object.get("id").toString().trim();
        
        List<CmdbPubCiBean> result = cmdbCiServie.queryCheckCmdbCiName( name, id );
        
        if( result != null && result.size() > 0  ){
            return false;
        }
        return true ;    
    }
    
    /**
     * @description:查询上下关联关系列表
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param rows
     * @param page
     * @param search
     * @param sort
     * @param order
     * @param ciId
     * @return:
     * @throws Exception 
     * @throws JsonParseException 
     */
    @RequestMapping("/queryCiRelation")
    public Page<CmdbRelationBean> queryCiRelation(int rows, int page, String search, String sort, String order, String ciId ) throws JsonParseException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<CmdbRelationBean> paramsPage = userInfoScope.getPage();
        paramsPage.setParameter( "siteId", userInfoScope.getSiteId() );
        paramsPage.setParameter( "ciId", ciId );
        
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
            paramsPage.setSortKey( "upCiType, upCiName" );
            paramsPage.setSortOrder( "ASC" );
        }
        
        paramsPage = cmdbCiServie.queryCiRelation( paramsPage );
        return paramsPage;
    }
    
    /**
     * 
     * @description:当改变CI类型、配置项时触发
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param ciType
     * @param paramType
     * @param paramVal --值
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryOderTypeByType")
    public @ResponseBody Map<String, Object> queryOderTypeByType( String ciType, String paramType, String paramVal ){
        //递减 desc 递增 asc
        String sort = "DESC";
        List<CmdbParamsBean> list = cmdbParamsServie.queryOderTypeByType( ciType, paramType, sort, paramVal );
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "result", list );
        return map;
    }
    
    /**
     * 
     * @description:通过ID查询配置项值
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param id
     * @return:Map<String, Object> --排序最大值 + 10
     */
    @RequestMapping("/queryCmdbPubCiById")
    public ModelAndViewAjax queryCmdbPubCiById( String id ){
        CmdbPubCiBean bean = cmdbCiServie.queryCmdbPubCiById( id );
        
        Map<String, Object> map = new HashMap<String, Object>();
        if( bean != null && StringUtils.isNotBlank( bean.getId() ) ){
            map.put( "result", "success" );
            map.put( "bean", bean );
        }else{
            map.put( "result", "fail" );
        }
        
        return itcMvcService.jsons( map );
    }
    
    /**
     * 
     * @description:通过ID删除配置项值
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param id
     * @return:Map<String, Object> --排序最大值 + 10
     */
    @RequestMapping("/deleteCmdbPubCiById")
    public @ResponseBody Map<String, Object> deleteCmdbPubCiById( String id ){
        int count = cmdbCiServie.deleteCmdbPubCiById( id );
        
        Map<String, Object> map = new HashMap<String, Object>();
        if( count > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }
    
    /**
     * 
     * @description:查询参数iHint
     * @author: fengzt
     * @createDate: 2015年8月26日
     * @param kw
     * @param paramType
     * @return:ModelAndViewAjax
     */
    @RequestMapping(value = "/searchCmdbParamsHint")
    public ModelAndViewAjax searchCmdbParamsHint(String kw, String paramType ) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        // 可性能优化，前台只展示前十条，可考虑只查询10条
        result = cmdbParamsServie.searchCmdbParamsHint( kw, paramType );
        return itcMvcService.jsons( result );
    }
    
    /**
     * 
     * @description:通过CI的名称模糊搜索
     * @author: fengzt
     * @createDate: 2015年8月26日
     * @param kw
     * @param ciId
     * @param ciType
     * @return:ModelAndViewAjax
     */
    @RequestMapping(value = "/queryHintCmdbRelationByName")
    public ModelAndViewAjax queryHintCmdbRelationByName(String kw, String ciId, String ciType ) {
        log.debug( "[queryHintCmdbRelationByName]传入参数 kw = " + kw + " --- ciId = " + ciId + "---ciType = " + ciType );
        List<CmdbPubCiBean> result = new ArrayList<CmdbPubCiBean>();
        // 可性能优化，前台只展示前十条，可考虑只查询10条
        result = cmdbCiServie.queryHintCmdbRelationByName( kw, ciId, ciType );
        
        //list bean transfer map
        List<Map<String, Object>> resultMap = new ArrayList<Map<String,Object>>();
        for( CmdbPubCiBean bean : result ){
            String key = bean.getId() + "##" + bean.getCiType();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put( "id", key );
            map.put( "name", bean.getName() );
            resultMap.add( map );
        }
        
        return itcMvcService.jsons( resultMap );
    }
    
    /**
     * 
     * @description:新增--关联关系
     * @author: fengzt
     * @createDate: 2015年8月11日
     * @param formData
     * @return:
     */
    @RequestMapping(value = "/insertCmdbRealation",method=RequestMethod.POST)
    public Map<String, Object> insertCmdbRealation( String formData ){
        Map<String, Object> map = new HashMap<String, Object>();
        CmdbRelationBean bean = JsonHelper.fromJsonStringToBean( formData, CmdbRelationBean.class );
        int count = cmdbCiServie.insertOrUpdateCmdbRelation( bean );
        
        if ( count > 0 && count != 999 ) {
            map.put( "result", "success" );
        } else if( count == 999 ){
            map.put( "result", "fail" );
            map.put( "msg", "已经存在这两个CI的关联关系！" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }
    
    /**
     * 
     * @description:通过ID删除关联关系
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param id
     * @return:Map<String, Object> --排序最大值 + 10
     */
    @RequestMapping("/deleteCmdbRelationById")
    public @ResponseBody Map<String, Object> deleteCmdbRelationById( String id ){
        int count = cmdbCiServie.deleteCmdbRelationById( id );
        
        Map<String, Object> map = new HashMap<String, Object>();
        if( count > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }
    
    /**
     * @description:查询日志列表
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
    @RequestMapping("/queryCmdbPubCiLog")
    public Page<AppLog> queryCmdbPubCiLog(int rows, int page, String search, String sort, String order, String ciId ) throws JsonParseException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<AppLog> paramsPage = userInfoScope.getPage();
        paramsPage.setParameter( "attr1", userInfoScope.getSiteId() );
        paramsPage.setParameter( "attr2", "cmdb" );
        paramsPage.setParameter( "attr3", ciId );

        // 获取表头搜索的参数，Dao的xml文件里面不用写 if is null这些方法了
        if ( StringUtils.isNotBlank( search ) ) {
            Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( search );
            paramsPage.setFuzzyParams( fuzzyParams );
        }

        // 设置排序内容
        if ( StringUtils.isNotBlank( sort ) ) {
            paramsPage.setSortKey( sort );
            paramsPage.setSortOrder( order );
        }

        List<AppLog> reuslt = logTempService.queryAllLogByAttr( paramsPage );
        paramsPage.setResults( reuslt );
        return paramsPage;
    }
    
    /**
     * 
     * @description:通过供应商的名称模糊搜索
     * @author: fengzt
     * @createDate: 2015年8月26日
     * @param kw
     * @return:ModelAndViewAjax
     */
    @RequestMapping(value = "/queryHintSupplierByName")
    public ModelAndViewAjax queryHintSupplierByName(String kw) {
        log.debug( "[queryHintSupplierByName]传入参数 kw = " + kw );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        SupBaseInfo supBaseInfo = new SupBaseInfo();
        supBaseInfo.setName( kw );
        //供应商接口
        List<SupBaseInfoVo> result = supplierForModelService.querySupplierList(userInfoScope, supBaseInfo);
        //list bean transfer map
        List<Map<String, Object>> resultMap = new ArrayList<Map<String,Object>>();
        for( SupBaseInfoVo bean : result ){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put( "id", bean.getId() );
            //组装显示名称
            StringBuffer name = new StringBuffer();
            name.append( StringUtils.isNotBlank( bean.getShortName() )? bean.getShortName() : bean.getName() )
            .append( StringUtils.isNotBlank( bean.getSpiName() )?"/" + bean.getSpiName(): "" )
            .append( StringUtils.isNotBlank( bean.getSpiCellphone() )?"/" + bean.getSpiCellphone(): ""  );
            map.put( "name", name.toString() );
            resultMap.add( map );
        }
        
        return itcMvcService.jsons( resultMap );
    }
    
    /**
     * 
     * @description:通过CI类型中，名称的模糊搜索
     * @author: fengzt
     * @createDate: 2015年8月26日
     * @param kw
     * @return:ModelAndViewAjax
     */
    @RequestMapping(value = "/queryHintCmdbByCiTypeAndName")
    public ModelAndViewAjax queryHintCmdbByCiTypeAndName(String kw, String ciType ) {
        log.debug( "[queryHintCmdbByCiTypeAndName]传入参数 kw = " + kw + " -- ciType = " + ciType );
        
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<CmdbPubCiBean> paramsPage = userInfoScope.getPage();
        paramsPage.setParameter( "siteId", userInfoScope.getSiteId() );
        paramsPage.setPageNo( 1 );
        paramsPage.setPageSize( 20 );

        //设置表头搜索的参数
        Map<String, Object> fuzzyParams = new HashMap<String, Object>();
        fuzzyParams.put( "ciType", ciType );
        fuzzyParams.put( "name", kw );
        paramsPage.setFuzzyParams( fuzzyParams );

        // 设置默认的排序字段
        paramsPage.setSortKey( "ciType, name" );
        paramsPage.setSortOrder( "ASC" );
        
        //利用查询列表时候的高级搜索函数
        paramsPage = cmdbCiServie.queryCmdbPubCiBySiteId( paramsPage );
       
        List<CmdbPubCiBean> result = paramsPage.getResults();
        //list bean transfer map
        List<Map<String, Object>> resultMap = new ArrayList<Map<String,Object>>();
        for( CmdbPubCiBean bean : result ){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put( "id", bean.getId() );
            //组装显示名称
            map.put( "name", bean.getName() );
            resultMap.add( map );
        }
        
        return itcMvcService.jsons( resultMap );
    }
    
    /**
     * @description:CI类型为机房设施，CI子类型为机柜的名称的模糊搜索
     * @author: yaoyn
     * @createDate: 2015年11月25日
     * @param kw
     * @return:ModelAndViewAjax
     */
    @RequestMapping(value = "/queryHintCmdbCabinetByName")
    public ModelAndViewAjax queryHintCmdbCabinetByName(String kw) {
        log.debug( "[queryHintCmdbCabinetByName]传入参数 kw = " + kw );
        
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<CmdbPubCiBean> paramsPage = userInfoScope.getPage();
        paramsPage.setParameter( "siteId", userInfoScope.getSiteId() );
        paramsPage.setPageNo( 1 );
        paramsPage.setPageSize( 20 );

        //设置表头搜索的参数
        Map<String, Object> fuzzyParams = new HashMap<String, Object>();
        fuzzyParams.put( "name", kw );
        paramsPage.setFuzzyParams( fuzzyParams );

        // 设置默认的排序字段
        paramsPage.setSortKey( "name" );
        paramsPage.setSortOrder( "ASC" );
        
        //设置函数调用参数，设置为查找机房设施为机柜的ci
        paramsPage.setParameter( "ciType", "CMDB_TYPE_10" );
        paramsPage.setParameter( "subTypeName", "机柜" );
        //利用根据ci类型和子类型查找ci的函数
        paramsPage = cmdbCiServie.queryCmdbPubCiByCiTypeAndSubType( paramsPage );
       
        List<CmdbPubCiBean> result = paramsPage.getResults();
        //list bean transfer map
        List<Map<String, Object>> resultMap = new ArrayList<Map<String,Object>>();
        for( CmdbPubCiBean bean : result ){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put( "id", bean.getId() );
            //组装显示名称
            map.put( "name", bean.getName() );
            resultMap.add( map );
        }
        
        return itcMvcService.jsons( resultMap );
    }
}
