package com.timss.inventory.web;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.timss.inventory.bean.InvBin;
import com.timss.inventory.bean.InvItem;
import com.timss.inventory.bean.InvWarehouse;
import com.timss.inventory.service.InvBinService;
import com.timss.inventory.service.InvCategroyService;
import com.timss.inventory.service.InvEquipItemMappingService;
import com.timss.inventory.service.InvItemService;
import com.timss.inventory.service.InvMatTranDetailService;
import com.timss.inventory.service.InvRealTimeDataService;
import com.timss.inventory.service.InvWarehouseItemService;
import com.timss.inventory.service.InvWarehouseService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvWarehouseItemVO;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.view.ModelAndViewAjax;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvItemController.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invitem")
public class InvItemController {

    /**
     * service 注入
     */
    @Autowired
    private InvItemService invItemService;

    @Autowired
    private InvWarehouseItemService invWarehouseItemService;

    @Autowired
    private InvWarehouseService invWarehouseService;

    @Autowired
    private InvCategroyService invCategroyService;

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private InvEquipItemMappingService invEquipItemMappingService;
    
    @Autowired
    private InvMatTranDetailService invMatTranDetailService;
    
    @Autowired
    private InvBinService invBinService;

    @Autowired
    private InvRealTimeDataService invRealTimeDataService;
    /**
     * 物资入库页面跳转
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/invItemList", method = RequestMethod.GET)
    public ModelAndView invItemList() {
        ModelAndView mav = new ModelAndView( "/invitem/invItemList.jsp" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        mav.addObject( "siteId", siteId );

        // 根据站点查询有效仓库，判断是否具有移库功能
        List<InvWarehouse> wareHouseList = invWarehouseService.queryAllWarehouseBySiteId( siteId );
        if ( wareHouseList.size() > 1 ) {
            mav.addObject( "multi_warehouse", "true" );
        } else {
            mav.addObject( "multi_warehouse", "false" );
        }
        return mav;
    }

    /**
     * 历史库存页面
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/invItemHistoryList", method = RequestMethod.GET)
    public String invItemHistoryList() {
        return "/invitem/invItemHistoryList.jsp";
    }

    /**
     * 安全库存页面跳转
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/invItemSafetyStock")
    public ModelAndView invItemSafetyStock() {
        String jumpPage = "/invitem/invItemSafetyStock.jsp";
        ModelAndView mav = new ModelAndView( jumpPage );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        //查询可选仓库
        List<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        List<InvWarehouse> wareHouseList = invWarehouseService.queryAllWarehouseBySiteId( userInfoScope.getSiteId() );
        for ( InvWarehouse wareHouse : wareHouseList ) {
            ArrayList<Object> row = new ArrayList<Object>();
            row.add( wareHouse.getWarehouseid() );
            row.add( wareHouse.getWarehousename() );
            result.add( row );
        }
        mav.addObject( "wareHouseArray", JSON.toJSONString(result) );
        
        return mav;
    }

    @RequestMapping(value = "/querySafetyStock")
    public Page<InvItemVO> querySafetyStock() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<InvItemVO> page = userInfoScope.getPage();
        Map<String, String[]> params = userInfoScope.getParamMap();

        if ( params.containsKey( "search" ) ) {
            String fuzzySearchParams = userInfoScope.getParam( "search" );
            Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( fuzzySearchParams );
            page.setFuzzyParams( fuzzyParams );
        }

        // 设置排序内容
        if ( params.containsKey( "sort" ) ) {
            String sortKey = userInfoScope.getParam( "sort" );
            if ( "itemname".equals( sortKey ) ) {
                sortKey = "NLSSORT(itemname,'NLS_SORT = SCHINESE_PINYIN_M')";
            } else if ( "cusmodel".equals( sortKey ) ) {
                sortKey = "NLSSORT(cusmodel,'NLS_SORT = SCHINESE_PINYIN_M')";
            } else if ( "unitname".equals( sortKey ) ) {
                sortKey = "NLSSORT(unitname,'NLS_SORT = SCHINESE_PINYIN_M')";
            }
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        }

        String type = userInfoScope.getParam( "type" );
        String itemInfo = userInfoScope.getParam( "itemInfo" );
        page.setParameter( "searchType", type );
        page.setParameter( "iteminfo", itemInfo );
        page.setParameter( "siteid", userInfoScope.getSiteId() );
        invItemService.queryInvItemSafetyStock( page );
        return page;
    }

    /**
     * 跳转到货物资页面
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-24
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/invArrivalItemList", method = RequestMethod.GET)
    public String invArrivalItemList() {
        return "/invitem/invArrivalItemList.jsp";
    }

    /**
     * 提示用户选择仓库
     * 
     * @description:
     * @author: 890157
     * @createDate: 2014年9月2日
     * @return:
     */
    @RequestMapping(value = "/invTreeNoSelectWarehouse", method = RequestMethod.GET)
    public String invTreeNoSelectWarehouse() {
        return "/invitem/invTreeNoSelectWarehouse.jsp";
    }

    /**
     * 物资入库页面跳转
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/invTreeItemList")
    public ModelAndView invTreeItemList(@RequestParam("embbed") String embbed, @RequestParam("opentype") String opentype)
            throws Exception {
        ModelAndView mav = new ModelAndView( "/invitem/invTreeItemList.jsp" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        mav.addObject( "embbed", embbed );
        mav.addObject( "opentype", opentype );
        String siteId = userInfoScope.getSiteId();
        mav.addObject( "siteId", siteId );

        String warehouseid = userInfoScope.getParam( "warehouseid" );
        String categoryname = userInfoScope.getParam( "categoryname" );
        String cateId = userInfoScope.getParam( "cateId" );
        String active = userInfoScope.getParam( "active" );
        if ( null != categoryname && null == cateId ) {
            List<String> cateList = invCategroyService.queryCategroyIdByName( userInfoScope, categoryname );
            if ( null != cateList && !cateList.isEmpty() ) {
                cateId = cateList.get( 0 );
            }
        }
        mav.addObject( "cateId", cateId );
        
        if ( null == active || "".equals( active ) ){
        	mav.addObject( "active", "" );
        }else{
        	mav.addObject( "active", active );
        }

        if ( null == warehouseid || "".equals( warehouseid ) ) {
            List<InvWarehouse> iwList = invWarehouseService.queryWarehouseByCategoryId( userInfoScope, cateId );
            if ( null != iwList && !iwList.isEmpty() ) {
                InvWarehouse iw = iwList.get( 0 );
                warehouseid = iw.getWarehouseid();
            }
        }

        mav.addObject( "wareId", warehouseid );
        // 根据站点查询有效仓库，判断是否具有移库功能
        List<InvWarehouse> wareHouseList = invWarehouseService.queryAllWarehouseBySiteId( siteId );
        if ( wareHouseList.size() > 1 ) {
            mav.addObject( "multi_warehouse", "true" );
        } else {
            mav.addObject( "multi_warehouse", "false" );
        }
        return mav;
    }

    /**
     * 详细页面跳转
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-14
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/invTreeItemDetail", method = RequestMethod.GET)
    public String invTreeItemDetail() {
        return "/invitem/invTreeItemDetail.jsp";
    }

    /**
     * 添加到仓库
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-16
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/addToWarehouse", method = RequestMethod.GET)
    public ModelAndView addToWarehouse(@RequestParam("type") String type) {
        ModelAndView mav = new ModelAndView( "/invitem/addToWarehouse.jsp" );
        mav.addObject( "type", type );
        return mav;
    }

    /**
     * 树跳转
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-11
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/invItemTree", method = RequestMethod.GET)
    public ModelAndView invItemTree(@RequestParam("opentype") String opentype) {
        ModelAndView mav = new ModelAndView( "/invitem/invItemTree.jsp" );
        mav.addObject( "opentype", opentype );
        return mav;
    }

    /**
     * 主项目列表跳转
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-15
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/invMainItemDetail", method = RequestMethod.GET)
    @ReturnEnumsBind("INV_SPMATERIAL,ITEMMAJOR_TYPE")
    public ModelAndView invMainItemList() throws Exception {
        ModelAndView mav = new ModelAndView( "/invitem/invMainItemDetail.jsp" );

        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String itemId = userInfo.getParam( "itemId" ) == null ? "" : userInfo.getParam( "itemId" );
        List<InvWarehouseItemVO> iwivList = invWarehouseItemService.queryInvWarehouseItem( userInfo, itemId );
        if ( !iwivList.isEmpty() ) {
            mav.addObject( "showWarehouse", "hide" );
        }
        return mav;
    }

    /**
     * 获取物资列表
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryItemsList", method = RequestMethod.POST)
    public Page<InvItemVO> queryItemsList(String search, String cateId, String embbed, String opentype, String isspare,
            String invmatapply, String categoryname, int ishis, String warehouseId) throws Exception {
        InvItemVO iiv = new InvItemVO();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

        // 若表头查询参数不为空
        if ( StringUtils.isNotBlank( search ) ) {
            iiv = JsonHelper.fromJsonStringToBean( search, InvItemVO.class );
        }
        iiv.setIshis( ishis );

        // 树节点点击事件触发，传categoryid
        String categoryId = cateId == null ? "" : cateId;
        if ( !"".equals( categoryId ) ) {
            iiv.setCateId( categoryId );
        }

        String embbedStr = embbed == null ? "" : embbed;
        if ( !"".equals( embbedStr ) ) {
            iiv.setEmbbed( embbedStr );
        }

        String opentypeStr = opentype == null ? "" : opentype;
        if ( !"".equals( opentypeStr ) ) {
            iiv.setOpentype( opentypeStr );
        }

        String isspareStr = isspare == null ? "" : isspare;
        if ( !"".equals( isspareStr ) ) {
            iiv.setIsspare( isspareStr );
        }

        String invmatapplyStr = invmatapply == null ? "" : invmatapply;
        if ( !"".equals( invmatapplyStr ) ) {
            iiv.setInvmatapplyStatus( invmatapplyStr );
        }

        String matSwitch = CommonUtil.getProperties( "matSwitch" );
        if ( matSwitch.indexOf( userInfo.getSiteId() ) > -1 ) {
            String categorynameStr = categoryname == null ? "" : categoryname;
            if ( !"".equals( categorynameStr ) ) {
                iiv.setCateType( categorynameStr );
            }
        }

        String warehouseIdStr = warehouseId == null ? "" : warehouseId;
        if ( !"".equals( warehouseIdStr ) ) {
            iiv.setWarehouseid( warehouseIdStr );
        }

        return invItemService.queryItemsList( userInfo, iiv );
    }

    /**
     * 生成物资树
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-11
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/getItemsTree")
    public ModelAndViewAjax getItemsTree(String id, String type, String opentype, String categoryname) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        List<HashMap<String, Object>> ret1 = null;
        List<HashMap<String, Object>> ret2 = new ArrayList<HashMap<String, Object>>();
        String nodeId = id == null ? "" : id;
        String nodeType = type == null ? "init" : type;
        String warehouseid = userInfoScope.getParam( "warehouseid" );

        categoryname = URLDecoder.decode( categoryname, "UTF-8" );

        HashMap<String, Object> pNode2 = new HashMap<String, Object>();
        // 若树为初始化的时候
        if ( "".equals( nodeId ) ) {
            List<Map<String, String>> mList = invItemService.querySiteById( siteId );
            if ( null != mList && !mList.isEmpty() ) {
                Map<String, String> map = mList.get( 0 );
                // 将根节点的信息赋值
                pNode2.put( "id", map.get( "SITE_ID" ) );
                pNode2.put( "text", map.get( "SITE_NAME" ) );
                pNode2.put( "state", "open" );
                pNode2.put( "type", "root" );
                pNode2.put( "selected", "true" );
                // 查询根节点之后的第一层节点信息（仓库）
                List<HashMap<String, Object>> hmList = null;

                // 20160108 modify by yuanzh
                // 增加多一层判断当页面传一个warehouseid过来的时候就查询该仓库下的所有物资类型
                if ( "".equals( warehouseid ) ) {
                    hmList = invWarehouseService.queryWarehouseNode( map.get( "SITE_ID" ) );
                } else {
                    Map<String, Object> paramMap = new HashMap<String, Object>();
                    paramMap.put( "siteId", map.get( "SITE_ID" ) );
                    paramMap.put( "warehouseid", "'" + warehouseid + "'" );
                    hmList = invWarehouseService.queryWarehouseNodeById( paramMap );
                }

                for ( HashMap<String, Object> hm : hmList ) {
                    String idInSide = String.valueOf( hm.get( "id" ) );

                    // 遍历仓库信息后再去查询仓库底下的节点信息
                    ret1 = queryNode( idInSide, siteId, nodeType, opentype, categoryname );
                    // 将整理好的信息重新放入
                    hm.put( "children", ret1 );
                }
                pNode2.put( "children", hmList );
            }
            // 生成一颗完整的物资树
            ret2.add( pNode2 );
        } else {
            // 若通过查询定位获取树
            ret2 = queryNode( nodeId, siteId, nodeType, opentype, categoryname );
        }
        return itcMvcService.jsons( ret2 );
    }

    /**
     * 通过查询框查询出节点
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-12
     * @param search
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryTreeNode", method = RequestMethod.POST)
    public Map<String, Object> queryTreeNode(String search) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> map = new HashMap<String, Object>();
        String id = null;
        String ids = null;
        String type = null;
        String strId = null;
        List<String> cateIdList = invCategroyService.queryCategroyIdByName( userInfoScope, search );
        if ( null != cateIdList && !cateIdList.isEmpty() ) {
            StringBuilder cateSB = new StringBuilder( "" );
            for ( int i = 0; i < cateIdList.size(); i++ ) {
                strId = cateIdList.get( i );
                cateSB.append( "'" ).append( strId ).append( "'," );
                if ( i == 0 ) {
                    id = strId;
                }
            }
            ids = cateSB.toString().substring( 0, cateSB.toString().length() - 1 );
            type = "category";
        }
        map.put( "ids", ids );
        map.put( "id", id );
        map.put( "type", type );
        return map;
    }

    /**
     * 打开查看详细信息表单
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-14
     * @param itemId
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryInvItemDetail", method = RequestMethod.POST)
    public InvItemVO queryInvItemDetail(String itemCode) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();

        InvItemVO iiv = null;
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();

        map.put( "siteId", userInfoScope.getSiteId() );
        map.put( "itemCode", itemCode );

        String cateId = userInfoScope.getParam( "cateId" );
        if ( cateId.indexOf( "ICI" ) > -1 ) {
            map.put( "cateId", cateId );
            InvWarehouse iw = invWarehouseService.queryWarehouseByCategoryId( userInfoScope, cateId ).get( 0 );
            map.put( "wareId", iw.getWarehouseid() );
        }

        List<InvItemVO> iivList = invItemService.queryInvItemDetail( map );
        if ( null != iivList && !iivList.isEmpty() ) {
            iiv = iivList.get( 0 );
        }
        return iiv;
    }

    /**
     * 查询root节点以下的节点信息
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-12
     * @param cateId
     * @param siteId
     * @param type
     * @return
     * @throws Exception:
     */
    private List<HashMap<String, Object>> queryNode(String id, String siteId, String type, String opertype,
            String categoryname) throws Exception {
        List<HashMap<String, Object>> ret2 = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> ret = new ArrayList<HashMap<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();

        map.put( "siteId", siteId );
        map.put( "opertype", opertype );
        if ( !"init".equals( type ) ) {
            if ( "warehouse".equals( type ) ) {
                map.put( "warehouseid", id );
                ret2 = invWarehouseService.queryWarehouseNodeById( map );
            } else {
                map.put( "categoryid", id );
                map.put( "categoryname", categoryname );
                ret2 = invCategroyService.queryAllCategroyNodeById( map );
            }
        } else {
            map.put( "warehouseid", "'" + id + "'" );
            map.put( "parentid", id );
            map.put( "categoryname", categoryname );
            ret = invCategroyService.queryAllCategroyNodeById( map );
        }

        ret2.addAll( ret );
        return ret2;
    }

    /**
     * 主项目表单信息
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-16
     * @param itemId
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryInvMainItemDetail", method = RequestMethod.POST)
    public InvItemVO queryInvMainItemDetail(String itemId) throws Exception {
        InvItemVO iiv = new InvItemVO();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        List<InvItemVO> iivList = invItemService.queryInvMainItemDetail( userInfoScope, itemId );
        if ( null != iivList && !iivList.isEmpty() ) {
            iiv = iivList.get( 0 );
        } else {
            iiv.setIsspare( "0" );
        }
        return iiv;
    }

    /**
     * 查询绑定设备信息
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-16
     * @param itemId
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryEquipInfo", method = RequestMethod.POST)
    public Map<String, Object> queryEquipInfo(String itemId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        List<Map<String, Object>> mList = invEquipItemMappingService.queryEquipInfo( userInfo, itemId );

        if ( null != mList && !mList.isEmpty() ) {
            StringBuilder sb = new StringBuilder( "" );
            sb.append( "{ \"total\":" ).append( mList.size() ).append( ",\"rows\":[" );
            for ( int i = 0; i < mList.size(); i++ ) {
                Map<String, Object> m = mList.get( i );
                sb.append( "{\"ck\":\"" ).append( i ).append( "\",\"equipid\":\"" )
                        .append( m.get( "EQUIPID" ) == null ? "" : m.get( "EQUIPID" ) ).append( "\",\"equipname\":\"" )
                        .append( m.get( "EQUIPNAME" ) == null ? "" : m.get( "EQUIPNAME" ) ).append( "\"}," );
            }
            String result = sb.toString().substring( 0, sb.toString().length() - 1 );
            result += "]}";
            map.put( "result", result );
        }
        return map;
    }

    /**
     * 保存主项目
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-16
     * @param formData
     * @param listData
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/saveItem", method = RequestMethod.POST)
    public Map<String, Object> saveItem(String formData, String listData) throws Exception {    	
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>(); // 参数map
        boolean flag = true;
        String itemid = null;
        String isspare = null;
        List<InvWarehouseItemVO> iwiList = new ArrayList<InvWarehouseItemVO>();

        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

        InvItem ii = JsonHelper.fromJsonStringToBean( formData, InvItem.class );

        if ( !"".equals( listData ) ) {
            iwiList = JsonHelper.toList( listData, InvWarehouseItemVO.class );
        }
        
        //先解除绑定
        /*String[] delArr = delData.split(",");
        for(String del:delArr){
        	invMatTranDetailService.deleteBindWarehouse( del );
        }*/
        
        Map<String, Object> reMap = invItemService.saveItem( userInfo, ii, iwiList, paramMap );

        flag = Boolean.valueOf( reMap.get( "flag" ) == null ? "false" : String.valueOf( reMap.get( "flag" ) ) );
        itemid = reMap.get( "itemid" ) == null ? "" : String.valueOf( reMap.get( "itemid" ) );
        isspare = reMap.get( "isspare" ) == null ? "" : String.valueOf( reMap.get( "isspare" ) );
        if ( flag ) {
            result.put( "itemid", itemid );
            result.put( "isspare", isspare );
            result.put( "result", "success" );
        } else {
            result.put( "result", "false" );
        }
        return result;
    }

    @RequestMapping(value = "/queryArrivalItem", method = RequestMethod.POST)
    public Page<InvItemVO> queryArrivalItem(String search, String pruordernoVal) throws Exception {
        InvItemVO iiv = new InvItemVO();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        // 若表头查询参数不为空
        if ( StringUtils.isNotBlank( search ) ) {
            iiv = JsonHelper.fromJsonStringToBean( search, InvItemVO.class );
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put( "pruordernoVal", pruordernoVal );
        return invItemService.queryArrivalItem( userInfo, iiv, paramMap );
    }

    /**
     * @description:查询物资详细信息
     * @author: 890166
     * @createDate: 2014-8-6
     * @param itemid
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryItemInfo", method = RequestMethod.GET)
    public ModelAndView queryItemInfo(@RequestParam("itemcode") String itemcode,
            @RequestParam("warehouseid") String warehouseid,@RequestParam("invcateid") String invcateid) throws Exception {
        ModelAndView mav = new ModelAndView( "/invitem/invItemInfo.jsp" );
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        InvItemVO iiv = null;
        List<InvItemVO> iivList = invItemService.queryItemInfo( userInfo, itemcode, warehouseid, invcateid );
        if ( null != iivList && !iivList.isEmpty() ) {
            iiv = iivList.get( 0 );
            // iiv.setBestockqty( iiv.getQtyStock() );
        }
        if( iiv!=null ){
            String iivStr = JsonHelper.fromBeanToJsonString( iiv );//用JSON.toString有些为null的属性返回到前台就没有了
            mav.addObject( "iivData", iivStr );
        }
        else{
            mav.addObject( "iivData", "{}" );
        }
        return mav;
    }

    /**
     * @description:转为历史库存
     * @author: 890166
     * @createDate: 2014-7-16
     * @param itemId
     * @return
     */
    @RequestMapping(value = "/updateTurnToHistory", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateTurnToHistory(String itemCodes) {
        Map<String, Object> map = invItemService.updateTurnToHistory( itemCodes );

        int count = (Integer) map.get( "count" );
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }

    /**
     * @description:转为历史库存
     * @author: 890166
     * @createDate: 2014-7-16
     * @param itemId
     * @return
     */
    @RequestMapping(value = "/updateTurnToWuzi", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateTurnToWuzi(String itemCodes) {
        Map<String, Object> map = invItemService.updateTurnToWuzi( itemCodes );

        int count = (Integer) map.get( "count" );
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * @description:根据站点查询有效仓库
     * @author: 890191
     * @createDate: 2016-04-18
     * @param
     * @return
     */
    @RequestMapping(value = "/queryAllWarehouseBySiteId", method = RequestMethod.GET)
    public @ResponseBody ArrayList<ArrayList<Object>> queryAllWarehouseBySiteId() {
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
    	// 根据站点查询有效仓库
        List<InvWarehouse> wareHouseList = invWarehouseService.queryAllWarehouseBySiteId( siteId );
        
        ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        for ( InvWarehouse invWarehouse : wareHouseList ) {
            ArrayList<Object> row = new ArrayList<Object>();
            row.add(invWarehouse.getWarehouseid());
            row.add(invWarehouse.getWarehousename());
            result.add(row);
        }
        return result;
    }
    
    /**
     * @description:根据仓库id查询货柜
     * @author: 890191
     * @createDate: 2016-04-18
     * @param warehouseid
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/queryBinByWarehouseId", method = RequestMethod.GET)
    public @ResponseBody ArrayList<ArrayList<Object>> queryBinByWarehouseId(
    		@RequestParam("warehouseid") String warehouseid) throws Exception {
    	// 根据仓库id查询货柜
        List<InvBin> ibList = invBinService.queryBinByWarehouseId( warehouseid );
        
        ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        for ( InvBin invBin : ibList ) {
            ArrayList<Object> row = new ArrayList<Object>();
            row.add(invBin.getBinid());
            row.add(invBin.getBinname());
            result.add(row);
        }
        return result;
    }
    
    /**
     * @description:根据仓库id查询物资分类
     * @author: 890191
     * @createDate: 2016-04-18
     * @param warehouseid
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryCategoryByWarehouseId", method = RequestMethod.GET)
    public @ResponseBody ArrayList<ArrayList<Object>> queryCategoryByWarehouseId(
    		@RequestParam("warehouseid") String warehouseid) throws Exception {
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        List<HashMap<String, Object>> ret2 = queryNode( warehouseid, siteId, "init", "", "" );
        for(HashMap<String, Object> iNode:ret2){
        	ArrayList<Object> row = new ArrayList<Object>();
    	    row.add(iNode.get("id"));
            row.add(iNode.get("text"));
            result.add(row);
            if(!"".equals(iNode.get("children"))){
            	List<HashMap<String, Object>> retInside = new ArrayList<HashMap<String, Object>>();
            	retInside = (List<HashMap<String, Object>>) iNode.get("children");
            	for(HashMap<String, Object> pNode:retInside){
                	ArrayList<Object> row2 = new ArrayList<Object>();
            	    row2.add(pNode.get("id"));
                    row2.add("----"+pNode.get("text"));
                    result.add(row2);
                }
            }
        }
        return result;
    }
    /**
     * 根据ImtId得到各自的sheetId
     * @description:
     * @author: 890162
     * @createDate: 2016-4-26
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/querySheetIdByImtId", method = RequestMethod.POST)
    public Map<String, Object> querySheetIdByImtId(@RequestParam("imtid") String imtid,@RequestParam("types") String types) {
        Map<String, Object> result = new HashMap<String, Object>(0);
        String sheetId = "";
        sheetId = invItemService.getSheetIdByImtId(imtid,types);
        result.put( "sheetId", sheetId );
        return result;
    }

    /**
     * 初始化实时数据表
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/initRealTimeData", method = RequestMethod.GET)
    public Map<String, Object> initRealTimeData() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String itemCodes = userInfoScope.getParam( "itemCodes" );
        String siteId = userInfoScope.getSiteId();
        Map<String, Object> map = new HashMap<String, Object>(0);
    	invRealTimeDataService.caluSiteInvData(siteId, itemCodes);
        map.put( "result", "success" );
        return map;
    }
}
