package com.timss.inventory.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.timss.inventory.bean.InvBin;
import com.timss.inventory.bean.InvWarehouse;
import com.timss.inventory.service.InvBinService;
import com.timss.inventory.service.InvWarehouseService;
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
 * @className: InvBinController.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890147
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invbin")
public class InvBinController {

    /**
     * service 注入
     */
    @Autowired
    private InvBinService invBinService;
    @Autowired
    private InvWarehouseService invWarehouseService;
    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * @description:通过物资类别查询所有对应的货柜
     * @author: 890166
     * @createDate: 2014-7-17
     * @param categoryId
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryBinByCategory", method = RequestMethod.POST)
    public Map<String, Object> queryBinByCategory(String categoryId) throws Exception {
        Map<String, Object> reMap = new HashMap<String, Object>();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> reVal = invBinService.queryBinByCategory( userInfo, categoryId );
        String data = String.valueOf( reVal.get( "defBin" ) );
        String warehouseid = String.valueOf( reVal.get( "warehouseid" ) );
        reMap.put( "data", data );
        reMap.put( "warehouseid", warehouseid );
        return reMap;
    }

    /**
     * 货柜列表页面跳转
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/invBinList", method = RequestMethod.GET)
    public String invBinList() {
        return "/invbin/invBinList.jsp";
    }

    /**
     * 货柜表单页面跳转
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/invBinForm", method = RequestMethod.GET)
    public String invBinForm() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        return "/invbin/invBinForm.jsp?mode=" + userInfoScope.getParam( "mode" ) + "&id="
                + userInfoScope.getParam( "id" );
    }

    @RequestMapping(value = "/queryList", method = RequestMethod.POST)
    public Page<InvBin> queryBinList() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<InvBin> page = userInfoScope.getPage();
        Map<String, String[]> params = userInfoScope.getParamMap();

        if ( params.containsKey( "search" ) ) {
            String fuzzySearchParams = userInfoScope.getParam( "search" );
            Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( fuzzySearchParams );
            page.setFuzzyParams( fuzzyParams );
        }

        // 设置排序内容
        if ( params.containsKey( "sort" ) ) {
            String sortKey = userInfoScope.getParam( "sort" );
            if ( "binname".equals( sortKey ) ) {
                sortKey = "NLSSORT(binname,'NLS_SORT = SCHINESE_PINYIN_M')";
            } else if ( "warehousename".equals( sortKey ) ) {
                sortKey = "NLSSORT(warehousename,'NLS_SORT = SCHINESE_PINYIN_M')";
            }
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        } else {
            // 设置默认的排序字段
            page.setSortKey( "binid" );
            page.setSortOrder( "asc" );
        }
        page.setParameter( "siteId", userInfoScope.getSiteId() );
        invBinService.queryBinListBySiteId( page );
        return page;
    }

    @RequestMapping(value = "/queryDetail", method = RequestMethod.POST)
    public InvBin queryBinDetail() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String id = userInfo.getParam( "id" );
        return invBinService.queryBinDetail( id );
    }

    @RequestMapping(value = "/isBinNameExist", method = RequestMethod.POST)
    public ModelAndViewAjax isBinNameExist() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String binname = userInfo.getParam( "binname" );
        String warehouseid = userInfo.getParam( "warehouseid" );
        String binid = userInfo.getParam( "binid" );
        InvBin bean = invBinService.queryBinByNameAndWarehouseId( binname, warehouseid );
        if ( bean != null && !binid.equals( bean.getBinid() ) ) {
            return itcMvcService.jsons( "货柜名称已经存在该仓库中" );
        } else {
            return itcMvcService.jsons( "true" );
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @VaildParam(paramName = { "bin" })
    public ModelAndViewAjax createBin() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        InvBin bean = userInfo.getJavaBeanParam( "bin", InvBin.class );
        String siteId = userInfo.getSiteId();
        bean.setSiteId( siteId );

        invBinService.insertBinInfo( bean );
        Map<String, Object> result = new HashMap<String, Object>();
        if ( bean.getBinid() != null ) {
            result.put( "status", 1 );
            result.put( "bin", bean );
        } else {
            result.put( "status", 0 );
        }
        return itcMvcService.jsons( result );
    }

    @RequestMapping(value = "/update")
    @VaildParam(paramName = { "bin" })
    public ModelAndViewAjax updateBin() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> result = new HashMap<String, Object>();
        InvBin bean = userInfoScope.getJavaBeanParam( "bin", InvBin.class );
        String siteId = userInfoScope.getSiteId();
        bean.setSiteId( siteId );
        if ( invBinService.updateBinInfo( bean ) == 1 ) {
            result.put( "result", "ok" );
            result.put( "bin", bean );
        }
        return ViewUtil.Json( result );
    }

    @RequestMapping(value = "/delete")
    public ModelAndViewAjax deleteBin() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> result = new HashMap<String, Object>();
        String ids = userInfoScope.getParam( "ids" );
        String[] id = ids.split( "," );
        Integer successNum = 0;
        for ( String tmp : id ) {
            if ( invBinService.deleteBin( tmp ) == 1 ) {
                successNum++;
            }
        }
        result.put( "successNum", successNum );
        result.put( "failNum", id.length - successNum );
        return ViewUtil.Json( result );
    }

    @RequestMapping(value = "/queryWarehouseList")
    public ModelAndViewAjax queryWarehouseList() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        List<InvWarehouse> list = invWarehouseService.queryAllWarehouseBySiteId( userInfo.getSiteId() );
        result.put( "warehouseList", list );
        return ViewUtil.Json( result );
    }
}
