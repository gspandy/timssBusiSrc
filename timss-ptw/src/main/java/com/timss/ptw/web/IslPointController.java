package com.timss.ptw.web;

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

import com.timss.asset.bean.AssetBean;
import com.timss.asset.service.AssetInfoService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.view.ModelAndViewAjax;

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
@RequestMapping(value = "ptw/islPoint")
public class IslPointController {
    @Autowired
    private ItcMvcService timssService;
    @Autowired
    @Qualifier("assetInfoServiceForIsolatedPointImpl")
    private AssetInfoService assetInfoService;

    static Logger logger = Logger.getLogger( IslPointController.class );

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
            pNode2.put("assetType", assetBean.getAssetType());
            pNode2.put("assetCode", assetBean.getAssetCode());
            pNode2.put( "children", assetInfoService.queryChildrenNode( assetBean.getAssetId() ) );
            ret2.add( pNode2 );
        } else {
            ret2 = assetInfoService.queryChildrenNode( locationId );
        }
        return timssService.jsons( ret2 );
    }

    
    /**
     * 用于搜索框的查询
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/assetHint", method = RequestMethod.POST)
    public ModelAndViewAjax assetSearchHint() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        List<Map<String, Object>> result=new ArrayList<Map<String,Object>>();
        String keyWord = userInfo.getParam( "kw" );
        String site = userInfo.getSiteId();
        if(keyWord!=null||!"".equals(keyWord)){
                result = assetInfoService.queryAssetForHint( site,keyWord );//可性能优化，前台只展示前十条，可考虑只查询10条，已优化
        }
        return timssService.jsons(result);       
    }
    
    /**
     * 用于搜索框的查询
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/assetParents", method = RequestMethod.POST)
    public ModelAndViewAjax assetParentIds() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        List<String> result=new ArrayList<String>();
        String assetId = userInfo.getParam( "id" );
        if(assetId!=null||!"".equals(assetId)){
                result = assetInfoService.queryAssetParents(assetId);
        }
        return timssService.jsons(result);       
    }


}
