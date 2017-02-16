package com.timss.itsm.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.itsm.bean.ItsmWoFaultType;
import com.timss.itsm.service.ItsmWoFaultTypeService;
import com.timss.itsm.util.ItsmConstant;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.view.ModelAndViewAjax;

@Controller
@RequestMapping(value = "/itsm/itsmFaultType")
public class ItsmFaultTypeController {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmWoFaultTypeService woFaultTypeService;
    
    /**
     * @description:故障类型列表页面
     * @author: 王中华
     * @createDate: 2014-8-22
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/faultTypeList")
    public String faultTypeList() {
        return "/woFaultTypeList.jsp";
    }

    @RequestMapping(value = "/faultTypeListData")
    @ResponseBody
    public Map<String, Object> faultTypeListData() throws JsonParseException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        Page<ItsmWoFaultType> page = userInfoScope.getPage();

        String fuzzySearchParams = userInfoScope.getParam( "search" );
        String faultTypeId = userInfoScope.getParam( "faultTypeId" );

        Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "faultTypeMap",
                ItsmConstant.MODULENAME, "ItsmWoFaultTypeDao" );

        if ( fuzzySearchParams != null ) {
            Map<String, Object> fuzzyParams = (HashMap<String, Object>) MapHelper.jsonToHashMap( fuzzySearchParams );
            fuzzyParams = MapHelper.fromPropertyToColumnMap( fuzzyParams, propertyColumnMap );
            page.setFuzzyParams( fuzzyParams );
        }
        if ( faultTypeId != null ) { // 精确查找
            page.setParameter( "id", faultTypeId );
        }
        // 设置排序内容
        if ( userInfoScope.getParamMap().containsKey( "sort" ) ) {
            String sortKey = userInfoScope.getParam( "sort" );

            // 如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
            sortKey = propertyColumnMap.get( sortKey );

            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        } else {
            // 设置默认的排序字段
            page.setSortKey( "DEFAULT_SCORE" );
            page.setSortOrder( "desc" );
        }
        page.setParameter( "siteid", siteId );
        page = woFaultTypeService.queryAllFaultType( page );

        ItsmWoFaultType woFaultType = woFaultTypeService.queryFaultTypeRootBySiteId( siteId );
        int rootId = woFaultType.getId();

        Map<String, Object> result = new HashMap<String, Object>();
        result.put( "rows", page.getResults() == null ? new String[0] : page.getResults() );
        result.put( "total", page.getTotalRecord() );
        result.put( "rootId", rootId );
        return result;
    }

    /**
     * @description:打开故障类型树页面
     * @author: 王中华
     * @createDate: 2014-9-18
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/openFaultTypeTreePage")
    public String openFaultTypeTreePage() {
        return "/woParamsConf/faultTypeTree.jsp";
    }

    /**
     * @description:获取故障类型树数据
     * @author: 王中华
     * @createDate: 2014-9-18
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/getFaultTypeTree")
    public ModelAndViewAjax getFaultTypeTree() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        String faultTypeId = userInfoScope.getParam( "id" );
        String treeType = userInfoScope.getParam( "treeType" ); // "SD"：只显示服务目录，不显示服务性质
        if ( "".equals( treeType ) || "null".equals( treeType ) ) {
            treeType = null;
        }

        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        Map<String, Object> pNode = new HashMap<String, Object>();

        if ( faultTypeId == null ) {
            ItsmWoFaultType woFaultType = woFaultTypeService.queryFaultTypeRootBySiteId( userInfoScope.getSiteId() );
            pNode.put( "id", woFaultType.getId() );
            pNode.put( "text", woFaultType.getName() );
            pNode.put( "state", "open" );
            pNode.put( "type", "root" );
            pNode.put( "faultTypeCode", woFaultType.getFaultTypeCode() );
            String parentIdString = String.valueOf( woFaultType.getId() );
            pNode.put( "children", woFaultTypeService.queryOneLevelChildrenNodes( parentIdString, treeType ) );
            ret.add( pNode );

        } else {
            ItsmWoFaultType woFaultType = woFaultTypeService.queryWoFaultTypeById( Integer.valueOf( faultTypeId ) );
            ret = woFaultTypeService.queryChildrenNodes( faultTypeId, treeType,woFaultType.getSiteid() );
        }

        return itcMvcService.jsons( ret );
    }

    
    @RequestMapping(value = "/getSerCatalogTree")
    public ModelAndViewAjax getSerCatalogTree() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        String faultTypeId = userInfoScope.getParam( "id" );
        String treeType = userInfoScope.getParam( "treeType" ); // "SD"：只显示服务目录，不显示服务性质
        if ( "".equals( treeType ) || "null".equals( treeType ) ) {
            treeType = null;
        }

        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        Map<String, Object> pNode = new HashMap<String, Object>();

        if ( faultTypeId == null ) {
            ItsmWoFaultType woFaultType = woFaultTypeService.querySerCatalogRootBySiteId( userInfoScope.getSiteId() );
            pNode.put( "id", woFaultType.getId() );
            pNode.put( "text", woFaultType.getName() );
            pNode.put( "state", "open" );
            pNode.put( "type", "root" );
            pNode.put( "faultTypeCode", woFaultType.getFaultTypeCode() );
            String parentIdString = String.valueOf( woFaultType.getId() );
            pNode.put( "children", woFaultTypeService.queryOneLevelSerCataNodes( parentIdString, treeType, siteid) );
            ret.add( pNode );

        } else {
            ItsmWoFaultType woFaultType = woFaultTypeService.queryWoFaultTypeById( Integer.valueOf( faultTypeId ) );
            ret = woFaultTypeService.queryChildrenNodes( faultTypeId, treeType,woFaultType.getSiteid() );
        }

        return itcMvcService.jsons( ret );
    }
    
    
    @RequestMapping(value = "/openFaultTypePage")
    public String openFaultTypePage() {
        return "/woParamsConf/newFaultType.jsp";
    }

    /**
     * @description:故障类型查询
     * @author: 王中华
     * @createDate: 2014-9-18
     * @return
     * @throws NumberFormatException
     * @throws Exception:
     */
    @RequestMapping(value = "/queryFaultTypeDataById")
    public Map<String, Object> queryFaultTypeDataById() throws NumberFormatException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int faultTypeId = Integer.valueOf( userInfoScope.getParam( "faultTypeId" ) );
        ItsmWoFaultType woFaultType = woFaultTypeService.queryWoFaultTypeById( faultTypeId );

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put( "faultTypeForm", woFaultType );
        return resultMap;
    }

    /**
     * @description: 提交故障类型
     * @author: 王中华
     * @createDate: 2014-9-18
     * @return
     * @throws NumberFormatException
     * @throws Exception:
     */
    @RequestMapping(value = "/commitFaultType")
    public Map<String, String> commitFaultType() throws NumberFormatException, Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int faultTypeId = Integer.valueOf( userInfoScope.getParam( "faultTypeId" ) );

        String faultTypeForm = userInfoScope.getParam( "faultTypeForm" );// 获取前台传过来的form表单数据

        Map<String, String> addFaultTypeDataMap = new HashMap<String, String>();
        addFaultTypeDataMap.put( "woFaultTypeForm", faultTypeForm );

        if ( faultTypeId == 0 ) {
            woFaultTypeService.insertWoFaultType( addFaultTypeDataMap );
        } else {
            woFaultTypeService.updateWoFaultType( addFaultTypeDataMap );
        }

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }

    @RequestMapping(value = "/deleteFaultType")
    public Map<String, String> deleteFaultType() throws NumberFormatException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int faultTypeId = Integer.valueOf( userInfoScope.getParam( "faultTypeId" ) );
        woFaultTypeService.deleteFaultTypeById( faultTypeId );

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }


   
   

 
    @RequestMapping(value = "/faultTypeParents", method = RequestMethod.POST)
    public ModelAndViewAjax assetParentIds() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        List<String> result = new ArrayList<String>();
        String faultTypeId = userInfo.getParam( "id" );
        if ( faultTypeId != null || !"".equals( faultTypeId ) ) {
            result = woFaultTypeService.queryFaultTypeParents( faultTypeId );
        }
        return itcMvcService.jsons( result );
    }
    
    /**
     * 用于搜索框的查询
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/faultTypeHint", method = RequestMethod.POST)
    public ModelAndViewAjax assetSearchHint() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        List<Map<String, Object>> result=new ArrayList<Map<String,Object>>();
        String keyWord = userInfo.getParam( "kw" ).trim();
        if(keyWord!=null && keyWord.length()>0){
                result = woFaultTypeService.queryFaultTypeForHint( keyWord );//可性能优化，前台只展示前十条，可考虑只查询10条，已优化
        }
        return itcMvcService.jsons(result);       
    }
    
    @RequestMapping(value = "/queryOneLevelFT")
    @ResponseBody
    public List<List<Object>> queryOneLevelFTBySiteId(){
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfo.getSiteId();
        List<ItsmWoFaultType> oneLevelFtList = woFaultTypeService.queryOneLevelFTBySiteId( siteid );
        List<List<Object>> result = new ArrayList<List<Object>>();
        for ( ItsmWoFaultType itsmWoFaultType : oneLevelFtList ) {
            List<Object> row = new ArrayList<Object>();
            row.add( itsmWoFaultType.getId() );
            row.add( itsmWoFaultType.getName() );
            result.add( row );
        }
        return result;
    }
    
    @RequestMapping(value = "/querychildrenFtById")
    @ResponseBody
    public List<List<Object>> querychildrenFtById() throws Exception{
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String parentId = userInfo.getParam( "faultTypeId" );
        List<ItsmWoFaultType> oneLevelFtList = woFaultTypeService.querychildrenFtById( parentId, "SD" );
        List<List<Object>> result = new ArrayList<List<Object>>();
        for ( ItsmWoFaultType itsmWoFaultType : oneLevelFtList ) {
            List<Object> row = new ArrayList<Object>();
            row.add( itsmWoFaultType.getId() );
            row.add( itsmWoFaultType.getName() );
            result.add( row );
        }
        return result;
    }
    
}
