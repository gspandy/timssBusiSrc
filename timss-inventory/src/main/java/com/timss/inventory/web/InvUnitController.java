package com.timss.inventory.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.timss.inventory.bean.InvUnit;
import com.timss.inventory.service.InvUnitService;
import com.yudean.itc.annotation.VaildParam;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvUnitController.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890147
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invunit")
public class InvUnitController {

    /**
     * service 注入
     */
    @Autowired
    private InvUnitService invUnitService;

    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * 计量单位列表页面跳转
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/invUnitList", method = RequestMethod.GET)
    public String invUnitList() {
        return "/invunit/invUnitList.jsp";
    }

    /**
     * @description:弹出框查询计量单位
     * @author: 890166
     * @createDate: 2014-8-29
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/invUnitQueryList", method = RequestMethod.GET)
    public String invUnitQueryList() {
        return "/invunit/invUnitQueryList.jsp";
    }

    /**
     * 计量单位表单页面跳转
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/invUnitForm", method = RequestMethod.GET)
    public String invUnitForm() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        return "/invunit/invUnitForm.jsp?mode=" + userInfoScope.getParam( "mode" ) + "&id="
                + userInfoScope.getParam( "id" );
    }

    @RequestMapping(value = "/queryList", method = RequestMethod.POST)
    public Page<InvUnit> queryUnitList() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<InvUnit> page = userInfoScope.getPage();
        Map<String, String[]> params = userInfoScope.getParamMap();

        if ( params.containsKey( "search" ) ) {
            String fuzzySearchParams = userInfoScope.getParam( "search" );
            Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( fuzzySearchParams );
            page.setFuzzyParams( fuzzyParams );
        }

        String embbed = userInfoScope.getParam( "embbed" );
        if ( "1".equals( embbed ) ) {
            page.setParameter( "active", "Y" );
        }

        // 设置排序内容
        if ( params.containsKey( "sort" ) ) {
            String sortKey = userInfoScope.getParam( "sort" );
            if ( "unitname".equals( sortKey ) ) {
                sortKey = "NLSSORT(unitname,'NLS_SORT = SCHINESE_PINYIN_M')";
            }
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        } else {
            // 设置默认的排序字段
            page.setSortKey( "unitid" );
            page.setSortOrder( "asc" );
        }
        page.setParameter( "siteId", userInfoScope.getSiteId() );
        invUnitService.queryUnitListBySiteId( page );
        return page;
    }

    @RequestMapping(value = "/queryDetail", method = RequestMethod.POST)
    public InvUnit queryUnitDetail() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String id = userInfo.getParam( "id" );
        return invUnitService.queryUnitDetail( id, userInfo.getSiteId() );
    }

    @RequestMapping(value = "/isCodeExist", method = RequestMethod.POST)
    public ModelAndViewAjax isCodeExist() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String code = userInfo.getParam( "code" );
        String id = userInfo.getParam( "id" );
        InvUnit bean = invUnitService.queryUnitByCodeAndSiteId( userInfo.getSiteId(), code );
        if ( bean != null && !id.equals( bean.getUnitid() ) ) {
            return itcMvcService.jsons( "计量单位编码已经存在" );
        } else {
            return itcMvcService.jsons( "true" );
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @VaildParam(paramName = { "unit" })
    public ModelAndViewAjax createUnit() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        InvUnit bean = userInfo.getJavaBeanParam( "unit", InvUnit.class );
        String siteId = userInfo.getSiteId();
        bean.setSiteId( siteId );

        invUnitService.insertUnitInfo( bean );
        Map<String, Object> result = new HashMap<String, Object>();
        if ( bean.getUnitid() != null ) {
            result.put( "status", 1 );
            result.put( "unit", bean );
        } else {
            result.put( "status", 0 );
        }
        return itcMvcService.jsons( result );
    }

    @RequestMapping(value = "/update")
    @VaildParam(paramName = { "unit" })
    public ModelAndViewAjax updateUnit() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> result = new HashMap<String, Object>();
        InvUnit bean = userInfoScope.getJavaBeanParam( "unit", InvUnit.class );
        String siteId = userInfoScope.getSiteId();
        bean.setSiteId( siteId );
        if ( invUnitService.updateUnitInfo( bean ) == 1 ) {
            result.put( "result", "ok" );
            result.put( "unit", bean );
        }
        return ViewUtil.Json( result );
    }

    @RequestMapping(value = "/delete")
    public ModelAndViewAjax deleteUnit() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> result = new HashMap<String, Object>();
        String ids = userInfoScope.getParam( "ids" );
        String[] id = ids.split( "," );
        String siteId = userInfoScope.getSiteId();
        Integer successNum = 0;
        for ( String tmp : id ) {
            if ( invUnitService.deleteUnit( tmp, siteId ) == 1 ) {
                successNum++;
            }
        }
        result.put( "successNum", successNum );
        result.put( "failNum", id.length - successNum );
        return ViewUtil.Json( result );
    }

    @RequestMapping(value = "/changeState")
    public ModelAndViewAjax changeUnitState() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> result = new HashMap<String, Object>();
        String state = userInfoScope.getParam( "state" );
        String siteId = userInfoScope.getSiteId();
        String id = userInfoScope.getParam( "id" );
        if ( invUnitService.updateUnitState( id, siteId, state ) == 1 ) {
            result.put( "result", "success" );
        } else {
            result.put( "result", "fail" );
        }
        return ViewUtil.Json( result );
    }
}
