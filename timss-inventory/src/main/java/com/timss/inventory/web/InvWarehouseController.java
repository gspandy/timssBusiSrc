package com.timss.inventory.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.timss.inventory.bean.InvWarehouse;
import com.timss.inventory.service.InvWarehouseService;
import com.yudean.itc.annotation.VaildParam;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvWarehouseController.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invwarehouse")
public class InvWarehouseController {

    /**
     * service 注入
     */
    @Autowired
    private InvWarehouseService invWarehouseService;

    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * 物资仓库列表页面跳转
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/invWarehouseList", method = RequestMethod.GET)
    public String invWarehouseList() {
        return "/invwarehouse/invWarehouseList.jsp";
    }

    /**
     * 物资仓库表单页面跳转
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/invWarehouseForm", method = RequestMethod.GET)
    public String invWarehouseForm() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        return "/invwarehouse/invWarehouseForm.jsp?mode=" + userInfoScope.getParam( "mode" ) + "&id="
                + userInfoScope.getParam( "id" ) + "&forbidEdit=" + userInfoScope.getParam( "forbidEdit" );
    }

    @RequestMapping(value = "/queryList", method = RequestMethod.POST)
    public Page<InvWarehouse> queryWarehouseList() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<InvWarehouse> page = userInfoScope.getPage();
        Map<String, String[]> params = userInfoScope.getParamMap();

        if ( params.containsKey( "search" ) ) {
            String fuzzySearchParams = userInfoScope.getParam( "search" );
            Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( fuzzySearchParams );
            page.setFuzzyParams( fuzzyParams );
        }

        // 设置排序内容
        if ( params.containsKey( "sort" ) ) {
            String sortKey = userInfoScope.getParam( "sort" );
            if ( "warehousename".equals( sortKey ) ) {
                sortKey = "NLSSORT(warehousename,'NLS_SORT = SCHINESE_PINYIN_M')";
            }
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        } else {
            // 设置默认的排序字段
            page.setSortKey( "warehouseid" );
            page.setSortOrder( "asc" );
        }
        page.setParameter( "siteId", userInfoScope.getSiteId() );
        invWarehouseService.queryWarehouseListBySiteId( page );
        return page;
    }

    @RequestMapping(value = "/queryDetail", method = RequestMethod.POST)
    public InvWarehouse queryWarehouseDetail() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String id = userInfo.getParam( "id" );
        InvWarehouse warehouse = invWarehouseService.queryWarehouseDetail( id, userInfo.getSiteId() );
        return warehouse;
    }

    @RequestMapping(value = "/isCodeExist", method = RequestMethod.POST)
    public ModelAndViewAjax isCodeExist() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String code = userInfo.getParam( "code" );
        String id = userInfo.getParam( "id" );
        InvWarehouse bean = invWarehouseService.queryWarehouseByCodeAndSiteId( userInfo.getSiteId(), code );
        if ( bean != null && !id.equals( bean.getWarehouseid() ) ) {
            return itcMvcService.jsons( "仓库编码已经存在" );
        } else {
            return itcMvcService.jsons( "true" );
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @VaildParam(paramName = { "warehouse" })
    public ModelAndViewAjax createWarehouse() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        InvWarehouse bean = userInfo.getJavaBeanParam( "warehouse", InvWarehouse.class );
        String siteId = userInfo.getSiteId();
        bean.setSiteId( siteId );
        bean.setCreatedate( new Date() );
        bean.setCreateuser( userInfo.getUserId() );
        invWarehouseService.insertWarehouseInfo( bean );
        Map<String, Object> result = new HashMap<String, Object>();
        if ( bean.getWarehouseid() != null ) {
            result.put( "status", 1 );
            result.put( "warehouse", bean );
        } else {
            result.put( "status", 0 );
        }
        return itcMvcService.jsons( result );
    }

    @RequestMapping(value = "/update")
    @VaildParam(paramName = { "warehouse" })
    public ModelAndViewAjax updateWarehouse() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> result = new HashMap<String, Object>();
        InvWarehouse bean = userInfoScope.getJavaBeanParam( "warehouse", InvWarehouse.class );
        String siteId = userInfoScope.getSiteId();
        bean.setSiteId( siteId );
        bean.setModifydate( new Date() );
        bean.setModifyuser( userInfoScope.getUserId() );
        if ( invWarehouseService.updateWarehouseInfo( bean ) == 1 ) {
            result.put( "result", "ok" );
            result.put( "warehouse", bean );
        }
        return ViewUtil.Json( result );
    }

    @RequestMapping(value = "/delete")
    public ModelAndViewAjax deleteWarehouse() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> result = new HashMap<String, Object>();
        String ids = userInfoScope.getParam( "ids" );
        String[] id = ids.split( "," );
        String siteId = userInfoScope.getSiteId();
        Integer successNum = 0;
        for ( String tmp : id ) {
            if ( invWarehouseService.deleteWarehouse( tmp, siteId ) == 1 ) {
                successNum++;
            }
        }
        result.put( "successNum", successNum );
        result.put( "failNum", id.length - successNum );
        return ViewUtil.Json( result );
    }

    @RequestMapping(value = "/changeState")
    public ModelAndViewAjax changeWarehouseState() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> result = new HashMap<String, Object>();
        String state = userInfoScope.getParam( "state" );
        String siteId = userInfoScope.getSiteId();
        String id = userInfoScope.getParam( "id" );
        if ( invWarehouseService.updateWarehouseState( id, siteId, state ) == 1 ) {
            result.put( "result", "success" );
        } else {
            result.put( "result", "fail" );
        }
        return ViewUtil.Json( result );
    }

    /**
     * @description:查询仓库表单信息
     * @author: 890166
     * @createDate: 2014-8-2
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/queryWarehouseDetailInfo", method = RequestMethod.POST)
    public InvWarehouse queryWarehouseDetailInfo(String warehouseId) throws Exception {
        InvWarehouse iw = null;
        List<InvWarehouse> iwList = invWarehouseService.queryWarehouseById( warehouseId );
        if ( null != iwList && !iwList.isEmpty() ) {
            iw = iwList.get( 0 );
        }
        return iw;
    }

    /**
     * 获取仓库列表
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryWarehouse", method = RequestMethod.POST)
    public Page<InvWarehouse> queryWarehouse(String search) throws Exception {
        InvWarehouse iWarehouse = null;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        if ( StringUtils.isNotBlank( search ) ) {
            iWarehouse = JsonHelper.fromJsonStringToBean( search, InvWarehouse.class );
        }
        return invWarehouseService.queryWarehouse( userInfo, iWarehouse );
    }

    /**
     * 获取仓库
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-22
     * @param categoryId
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/queryWarehouseByCategoryId", method = RequestMethod.POST)
    public Map<String, Object> queryWarehouseByCategoryId(String categoryId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        List<InvWarehouse> iwList = invWarehouseService.queryWarehouseByCategoryId( userInfo, categoryId );
        if ( null != iwList && !iwList.isEmpty() ) {
            StringBuilder sb = new StringBuilder( "[" );
            for ( InvWarehouse iw : iwList ) {
                sb.append( "[\"" ).append( iw.getWarehouseid() ).append( "\",\"" ).append( iw.getWarehousename() )
                        .append( "\"]," );
            }
            String reJson = sb.toString().substring( 0, sb.toString().length() - 1 );
            reJson += "]";
            map.put( "jData", reJson );
            map.put( "result", "success" );
        }
        return map;
    }

    /**
     * 查询采购类型数据(以后若可以在页面使用枚举，这个方法将弃用)
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-6-26
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/queryAllWarehouse", method = RequestMethod.POST)
    public List<HashMap<String, String>> queryAllWarehouse() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        List<HashMap<String, String>> mapList = invWarehouseService.queryWarehouse( userInfo );
        return mapList;
    }

    @RequestMapping(value = "/queryWarehouseInList", method = RequestMethod.POST)
    public JSONObject queryWarehouseInList() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        List<HashMap<String, String>> mapList = invWarehouseService.queryWarehouse( userInfo );
        JSONObject json = new JSONObject();
        for ( Map<String, String> map : mapList ) {
            json.put( map.get( "key" ), map.get( "value" ) );
        }
        return json;
    }
}
