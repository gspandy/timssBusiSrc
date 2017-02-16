package com.timss.ptw.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.asset.bean.AssetBean;
import com.timss.asset.service.AssetInfoService;
import com.timss.ptw.service.PtwIsolationPointService;
import com.timss.ptw.vo.PtwIsolationPointVo;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PtwIsolationPointController.java
 * @author: 王中华
 * @createDate: 2014-10-22
 * @updateUser: 王中华
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "ptw/ptwIsolationPoint")
public class PtwIsolationPointController {
    private static final Logger log = Logger.getLogger(PtwIsolationPointController.class);
    
    @Autowired
    private PtwIsolationPointService ptwIsolationPointService;
    
    @Autowired
    @Qualifier("assetInfoServiceImpl")
    private AssetInfoService assetInfoService;
    @Autowired
    private ItcMvcService itcMvcService;
    
    @RequestMapping(value="/openIsolationPointListPage")
   	public String openIslMethDefListPage() throws Exception{
   		return "/IsolationPointList.jsp";
   	}
    
    @RequestMapping(value="/selectIslMethodPage")
   	public String selectIslMethodPage() throws Exception{
   		return "/ptwBaseConf/SelectIslMethDefList.jsp";
   	}
    
    @RequestMapping(value="isolationPointListData")
	public Page<AssetBean> isolationPointListData() throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        
        String location = userInfoScope.getParam( "location" );
        if( location == null ){//如果为空，则获得根节点的id
            location = assetInfoService.queryAssetTreeRootBySiteId(userInfoScope.getSiteId()).getAssetId();
        }
        Page<AssetBean> page = userInfoScope.getPage();
        
        String fuzzySearchParams = userInfoScope.getParam("search");
        
        if(fuzzySearchParams!=null){
        	HashMap<String, Object> fuzzyParams = (HashMap<String, Object>)MapHelper.jsonToHashMap( fuzzySearchParams );
        	page.setFuzzyParams(fuzzyParams);
        } 
        
        // 设置排序内容
		if (userInfoScope.getParamMap().containsKey("sort")) {
			String sortKey = userInfoScope.getParam("sort");
			page.setSortKey(sortKey);
			page.setSortOrder(userInfoScope.getParam("order"));
		} else {
			// 设置默认的排序字段
			page.setSortKey("assetId");
			page.setSortOrder("desc");
			page.setParameter("siteId", siteId);
		}
        
		  page.setParameter( "location", location );
	      page = assetInfoService.queryAssetListByParentId(page);
        
        return page;
	}
    
    
    @RequestMapping(value="/openPtwIsolationPointPage")
	public String openPtwIsolationPointPage() throws Exception{
		return "/ptwBaseConf/IsolationPoint.jsp";
	}
   
    /**
     * @description:查询隔离点配置信息（隔离点信息、隔离点与隔离方法的信息）
     * @author: 王中华
     * @createDate: 2014-10-17
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryInfoByIsolationPointId")
    public @ResponseBody HashMap<String, Object> queryPtwIsolationPointById() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        String pointNo = userInfoScope.getParam( "id" );
        AssetBean assetInfoBean = assetInfoService.queryAssetDetail(pointNo);
        ArrayList<PtwIsolationPointVo> dataList = ptwIsolationPointService.queryBeanByIsolationPointNo(pointNo,siteId);
        
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "result", "success" );
        result.put( "formDate",  JsonHelper.toJsonString(assetInfoBean)); //隔离点信息
        result.put("islMethodData", JsonHelper.toJsonString(dataList)); //隔离点与隔离方法的关系数据
        return result;
    }
    
    
    
    /**
     * @description:提交对隔离点的设置（主要是设置隔离点与隔离方法的关联）
     * @author: 王中华
     * @createDate: 2014-10-17
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/commitPtwIsolationPoint")
    @Transactional(propagation = Propagation.REQUIRED)
    public @ResponseBody HashMap<String, Object> commitPtwIsolationPoint() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String isolationMethodDate = userInfoScope.getParam( "isolationMethodDate" );
        String isolationPointNo = userInfoScope.getParam( "isolationPointNo" );
        
        HashMap<String,String> paramsDataMap = new HashMap<String, String>();
        paramsDataMap.put("isolationPointNo", isolationPointNo);
        paramsDataMap.put("isolationMethodDate", isolationMethodDate);
		
        ptwIsolationPointService.insertPtwIsolationPoint(paramsDataMap);
        
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "result", "success" );
        return result;
    }
    
    /**
     * @description: 为隔离症配置隔离点与隔离方法的弹出框列表
     * @author: 王中华
     * @createDate: 2014-10-22
     * @return
     * @throws Exception:
     */
    @RequestMapping(value="islPointMethodListData")
    public Page<PtwIsolationPointVo> islMethDefListData() throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        Page<PtwIsolationPointVo> page = userInfoScope.getPage();
        
        String fuzzySearchParams = userInfoScope.getParam("search");
        
        if(fuzzySearchParams!=null){
        	HashMap<String, Object> fuzzyParams = (HashMap<String, Object>)MapHelper.jsonToHashMap( fuzzySearchParams );
        	page.setFuzzyParams(fuzzyParams);
        } 
            
            // 设置排序内容
    	if (userInfoScope.getParamMap().containsKey("sort")) {
    	    String sortKey = userInfoScope.getParam("sort");
    	    page.setSortKey(sortKey);
    	    page.setSortOrder(userInfoScope.getParam("order"));
    	} else {
    	    // 设置默认的排序字段
    	    page.setSortKey("NO");
    	    page.setSortOrder("asc");
    	    page.setParameter("siteId", siteId);
    	}
            
        page = ptwIsolationPointService.queryAllBeanBySiteId(page);
        
        return page;
    }
    
    /**
     * 
     * @description:动态建立隔离点与隔离方法关联关系
     * @author: fengzt
     * @createDate: 2014年11月10日
     * @param methodIds
     * @param pointNo
     * @return:
     */
    @RequestMapping("/saveIslMethodByMethodIds")
    public @ResponseBody Map<String, Object> saveIslMethodByMethodIds( String methodIds, String pointNo ) {
        Map<String, Object> map = ptwIsolationPointService.saveIslMethodByMethodIds( methodIds, pointNo );
        
        return map;
    }
    
    /**
     * @description:提交对隔离点的设置（主要是设置隔离点与隔离方法的关联）
     * @author: 王中华
     * @createDate: 2014-10-17
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/insertOrUpdatePtwIsolationPoint")
    public @ResponseBody HashMap<String, Object> insertOrUpdatePtwIsolationPoint() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String isolationMethodDate = userInfoScope.getParam( "isolationMethodDate" );
        String formData = userInfoScope.getParam( "formData" );
        
        AssetBean assetBean = JsonHelper.fromJsonStringToBean( formData, AssetBean.class );
        
        HashMap<String,String> paramsDataMap = new HashMap<String, String>();
        paramsDataMap.put("isolationMethodDate", isolationMethodDate);
                
        HashMap<String, Object> result = ptwIsolationPointService.insertOrUpdatePtwIsolationPoint(paramsDataMap, assetBean );
        
        return result;
    }
   
    /**
     * 
     * @description:跳到新建隔离点页面
     * @author: fengzt
     * @createDate: 2015年1月6日
     * @param assetName
     * @param assetId
     * @return:ModelAndView
     * @throws Exception 
     */
    @RequestMapping("/insertIslPointPage")
    public ModelAndView insertIslPointPage( String assetName, String assetId ) throws Exception {
        log.info( "params assetName = " + assetName + " ---- assetId= " + assetId );
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "assetId", assetId );
        if( StringUtils.isNotBlank( assetName ) ){
            map.put( "assetName", assetName );
        }else{
            AssetBean assetBean = assetInfoService.queryAssetDetail( assetId );
            map.put( "assetName", assetBean.getAssetName() );
        }
        
        return new ModelAndView( "ptwBaseConf/insertIsolationPoint.jsp", map );
    }
    
     /**
      * 
      * @description:删除隔离点
      * @author: fengzt
      * @createDate: 2015年1月6日
      * @param assetId
      * @returnMap
      * @throws Exception:
      */
    @RequestMapping("/deleteIsolationPoint")
    public @ResponseBody Map<String, Object> deleteIsolationPoint( String assetId ) throws Exception {
        log.info( "params ---- assetId= " + assetId );
        
        Map<String, Object> map = new HashMap<String, Object>();
        int count = ptwIsolationPointService.deleteIsolationPoint( assetId );
        
        if( count > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        return map;
    }
}
