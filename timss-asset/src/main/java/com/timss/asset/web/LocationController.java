package com.timss.asset.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timss.asset.bean.AssetBean;
import com.timss.asset.service.AssetInfoService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.mvc.view.ModelAndViewPage;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: LocationController.java
 * @author: 890165
 * @createDate: 2014-6-26
 * @updateUser: 890147（替换掉所有的位置bean为新资产bean，删除不使用的内容）
 * @version: 1.1
 */
@Controller
@RequestMapping(value = "asset/location")
public class LocationController {
    @Autowired
    private ItcMvcService timssService;
    @Autowired
    @Qualifier("assetInfoServiceImpl")
    private AssetInfoService assetInfoService;

    static Logger logger = Logger.getLogger( LocationController.class );

    /**
     * @description:获取设备树
     * @author: 890165
     * @createDate: 2014-6-27
     * @return
     * @throws Exception:
     */
    @RequestMapping("/getAssetTree")
    public ModelAndViewAjax getAssetTree() throws Exception {
        UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
        String locationId = userInfoScope.getParam( ("id") );
        
        List<HashMap<String, Object>> ret2 = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> pNode2 = new HashMap<String, Object>();
        if ( locationId == null ) {
            locationId = userInfoScope.getSiteId();
            AssetBean assetBean = assetInfoService.queryAssetTreeRootBySiteId(locationId);
            pNode2.put( "id", assetBean.getAssetId());
            pNode2.put( "text", assetBean.getAssetName());
            pNode2.put( "state", "open" );
            pNode2.put( "type", "root" );
            pNode2.put("forbidDelete", assetBean.getForbidDelete());
            pNode2.put("forbidMove", assetBean.getForbidMove());
            pNode2.put("forbidUpdate", assetBean.getForbidUpdate());
            pNode2.put("assetType", assetBean.getAssetType());
            pNode2.put("assetCode", assetBean.getAssetCode());
            pNode2.put("iconCls", "icon-folder");
            pNode2.put( "children", assetInfoService.queryChildrenNode( assetBean.getAssetId() ) );
            ret2.add( pNode2 );
        } else {
            ret2 = assetInfoService.queryChildrenNode( locationId );
        }
        return timssService.jsons( ret2 );
    }

    /**
     * @description:查询location下面的子节点（包括位置和资产设备）
     * @author: 890165
     * @createDate: 2014-6-27
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/getLocationListData")
    public Page<AssetBean> getLocationListData() throws Exception {
        UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
        Page<AssetBean> page = userInfoScope.getPage();
        
        String location = userInfoScope.getParam( "location" );
        if( location == null ){//如果为空，则获得根节点的id
            location = assetInfoService.queryAssetTreeRootBySiteId(userInfoScope.getSiteId()).getAssetId();
        }
        
        // 获取表头搜索的参数，Dao的xml文件里面不用写 if is null这些方法了
        Map<String, String[]> params = userInfoScope.getParamMap();

        // 设置排序内容
        if ( params.containsKey( "sort" ) ) { 
            String sortKey = userInfoScope.getParam( "sort" );
            
            // 如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
            //sortKey = propertyColumnMap.get( sortKey );
            
            if("assetName".equals(sortKey)){
            	sortKey="NLSSORT(assetname,'NLS_SORT = SCHINESE_PINYIN_M')";
            }
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        } else {
            // 设置默认的排序字段
            //page.setSortKey( "assetname" );
        }
        
        page.setParameter( "location", location );
        page = assetInfoService.queryAssetListByParentId( page );
        //page.setTotalRecord(0);//用于测试空数据时的页面
        return page;
    }

    /**
     * @description:返回位置列表页面并传递location参数
     * @author: 890165
     * @createDate: 2014-6-27
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/locationList", method = RequestMethod.GET)
    public ModelAndViewPage locationList() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String location = userInfo.getParam( "location" );
        if ( location == null ) { 
            location = assetInfoService.queryAssetTreeRootBySiteId(userInfo.getSiteId()).getAssetId();
        }
        return timssService.Pages( "/assetinfo/locationList.jsp", "location", location );
    }
    
    
    /**
     * @description:资产统计表打印页面跳转
     * @author: 890199
     * @createDate: 2016-8-9
     * @return
     * @throws Exception:
     */
    
    @RequestMapping(value = "/assetStatisticPage", method = RequestMethod.GET)
    public ModelAndView assetStatisticPage() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
    	ModelAndView mav = new ModelAndView( "/assetinfo/assetStatistic.jsp" );
    	mav.addObject( "siteid" , userInfo.getSiteId() );
        return mav;
    }

    
    
    
}
