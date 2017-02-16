package com.timss.asset.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.timss.asset.bean.SwLedgerAppBean;
import com.timss.asset.bean.SwLedgerBean;
import com.timss.asset.service.SwLedgerService;
import com.timss.asset.util.VOUtil;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.VaildParam;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.JavaBeanHelper;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.mvc.view.ModelAndViewPage;

/**
 * 软件台账的controller
 * @author 890147
 *
 */
@Controller
@RequestMapping(value = "asset/swLedger")
public class SwLedgerController {
	@Autowired
    private ItcMvcService timssService;
	@Autowired
	private SwLedgerService swLedgerService;
	
    static Logger logger = Logger.getLogger( SwLedgerController.class );
    
    /**
     * 跳转到列表页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/listPage")
    public String swLedgerListPage() throws Exception {
        return "/swLedger/list.jsp";
    }
    
    /**
     * 跳转到详情页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detailPage")
    @ReturnEnumsBind("AST_SW_APP_TYPE")
    public ModelAndViewPage swLedgerDetailPage() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String swLedgerId = userInfo.getParam( "swLedgerId" );
        SwLedgerBean swLedgerBean=null;
        if ( swLedgerId != null&&!"".equals(swLedgerId) ) {//初始查询 
        	swLedgerBean=swLedgerService.queryDetail(swLedgerId);
        }
        Map<String, String>result=new HashMap<String, String>();
        result.put("swLedgerBean", JsonHelper.toJsonString(swLedgerBean));
        return timssService.Pages( "/swLedger/detail.jsp", "result",result);
    }
    
    /**
     * 分页查询列表
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getList")
    public Page<SwLedgerBean> getSwLedgerList() throws Exception {
        UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
        Page<SwLedgerBean> page = userInfoScope.getPage();
        page.setParameter("siteId", userInfoScope.getSiteId());
        
        // 获取表头搜索的参数，Dao的xml文件里面不用写 if is null这些方法了
        Map<String, String[]> params = userInfoScope.getParamMap();
        
        if (params.containsKey("search")) {
			String fuzzySearchParams = userInfoScope.getParam("search");
			Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( fuzzySearchParams );
			page.setFuzzyParams(fuzzyParams);
		}
        
        // 设置排序内容
        if ( params.containsKey( "sort" ) ) { 
            String sortKey = userInfoScope.getParam( "sort" );
            
            if("swName".equals(sortKey)){
            	sortKey="NLSSORT(swName,'NLS_SORT = SCHINESE_PINYIN_M')";
            }else if("attr01".equals(sortKey)){
            	sortKey="NLSSORT(attr01,'NLS_SORT = SCHINESE_PINYIN_M')";
            }else if("attr02".equals(sortKey)){
            	sortKey="NLSSORT(attr02,'NLS_SORT = SCHINESE_PINYIN_M')";
            }
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        } else {
            // 设置默认的排序字段
            page.setSortKey( "NLSSORT(swName,'NLS_SORT = SCHINESE_PINYIN_M')" );
        }
        
        page = swLedgerService.queryList( page );
        return page;
    }

    /**
     * 查询详情
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getDetail")
    public ModelAndViewAjax getSwLedgerDetail() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String swLedgerId = userInfo.getParam( "swLedgerId" );
        SwLedgerBean bean = swLedgerService.queryDetail( swLedgerId );
        
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put( "swLedgerBean", bean );
        return timssService.jsons(result);       
    }
    
    /**
     * 判断站点下软件台账名称是否已存在
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/isNameExist")
    public ModelAndViewAjax isSwLedgerNameExist() throws Exception {
    	UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String swName = userInfo.getParam( "swName" );
        String swId = userInfo.getParam( "swId" );
        return timssService.jsons( checkSwLedgerNameExist(swName,swId,userInfo.getSiteId()) );     
    }
    
    /**
     * 检查站点下软件台账名称是否已存在
     * @param type
     * @param name
     * @param excludeId：
     * @param siteId
     * @return
     * @throws Exception
     */
    private String checkSwLedgerNameExist(String name,String excludeId,String siteId) throws Exception {
    	SwLedgerBean infoBean = swLedgerService.querySwLedgerByName(siteId,name);
        if(infoBean!=null&&!excludeId.equals(infoBean.getSwId()))
            return "软件台账名称已经存在";
        else
            return "true" ;
    }
    
    /**不用
     * 判断站点下软件台账应用的名称是否已存在
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/isAppNameExist")
    public ModelAndViewAjax isSwLedgerAppNameExist() throws Exception {
    	UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String appName = userInfo.getParam( "appName" );
        String appId = userInfo.getParam( "appId" );
        String swId = userInfo.getParam( "swId" );
        List<SwLedgerAppBean> apps=converAppListFormString(userInfo.getParam("apps"));
        return timssService.jsons( checkSwLedgerAppNameExist(appName,swId,appId,apps) );     
    }
    
    /**不用
     * 检查站点下软件台账应用的名称是否已存在
     * @param name
     * @param swId
     * @param excludeId
     * @param apps,前台该软件台账的所有硬件台账
     * @return
     * @throws Exception
     */
    private String checkSwLedgerAppNameExist(String name,String swId,String excludeId,List<SwLedgerAppBean> apps) throws Exception {
    	String str="软件台账名称已经存在";
    	//判断数据库中是否已存在重名
    	SwLedgerAppBean infoBean = swLedgerService.querySwLedgerAppByName(swId,name);
        if(infoBean!=null&&!excludeId.equals(infoBean.getAppId()))
            return str;
        //判断前台是否已使用
        for (SwLedgerAppBean swLedgerAppBean : apps) {
			if(swLedgerAppBean.getAppName().equals(name)&&!swLedgerAppBean.getAppId().equals(excludeId)){
				return str;
			}
		}
        
        return "true" ;
    }
    
    @RequestMapping(value = "/insertSwLedger")
    @VaildParam( paramName={ "swLedgerBean", "addApps", "updateApps" } )
    public ModelAndViewAjax insertSwLedger() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        SwLedgerBean bean = userInfo.getJavaBeanParam( "swLedgerBean", SwLedgerBean.class );
        String siteId = userInfo.getSiteId();
        bean.setSiteid(siteId);
        Date date=new Date();
        bean.setCreatedate(date);
        bean.setCreateuser(userInfo.getUserId());
        int rst=-1;
        if("true".equals(checkSwLedgerNameExist(bean.getSwName(),"",userInfo.getSiteId()))){
        	rst=swLedgerService.insert(bean);
        }
        
        Map<String, Object>result= new HashMap<String, Object>();
        if(rst==1&&bean.getSwId()!=null){
        	//插入应用信息
        	updateApps(bean.getSwId(),userInfo);
        	bean=swLedgerService.queryDetail( bean.getSwId() );//重新查询bean
        	result.put("status", 1);
        	result.put("swLedgerBean", bean);
        }else if(rst==-1){
			//有重名
        	result.put("status", -1);
		}else{
        	result.put("status", 0);
        }
        return timssService.jsons(result);
    }
    
    /**
     * 删除
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/deleteSwLedger")
    public ModelAndViewAjax deleteSwLedger() throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
        String swLedgerId = userInfoScope.getParam( "swLedgerId" );
        if(swLedgerService.deleteById(swLedgerId)>0){
            result.put( "result", "ok" );
        }else{
        	result.put( "result", "fail" );
        }
        return ViewUtil.Json(result);
    }
    
    /**
     * 更新
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateSwLedger")
    @VaildParam( paramName={ "swLedgerBean", "addApps", "updateApps" } )
    public ModelAndViewAjax updateSwLedger() throws Exception {
    	UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        SwLedgerBean bean = userInfo.getJavaBeanParam( "swLedgerBean", SwLedgerBean.class );
        String siteId = userInfo.getSiteId();
        bean.setSiteid(siteId);
        Date date=new Date();
        bean.setModifydate(date);
        bean.setModifyuser(userInfo.getUserId());
        int rst=-1;
        if("true".equals(checkSwLedgerNameExist(bean.getSwName(),bean.getSwId(),userInfo.getSiteId()))){
        	rst=swLedgerService.update(bean);
        }
        
        Map<String, Object>result= new HashMap<String, Object>();
        if(rst==1&&bean.getSwId()!=null){
        	//插入应用信息
        	updateApps(bean.getSwId(),userInfo);
        	bean=swLedgerService.queryDetail( bean.getSwId() );//重新查询bean
        	result.put("status", 1);
        	result.put("swLedgerBean", bean);
        }else if(rst==-1){
			//有重名
        	result.put("status", -1);
		}else{
        	result.put("status", 0);
        }
        return timssService.jsons(result);
    }
    
    private void updateApps(String swId,UserInfoScope userInfo) throws Exception{
    	List<SwLedgerAppBean> addApps=converAppListFormString(userInfo.getParam("addApps"));
    	List<SwLedgerAppBean> updateApps=converAppListFormString(userInfo.getParam("updateApps"));
    	List<SwLedgerAppBean> delApps=converAppListFormString(userInfo.getParam("delApps"));
    	SwLedgerBean swl=new SwLedgerBean();
    	swl.setSwId(swId);
    	
    	for (SwLedgerAppBean bean : addApps) {
    		bean.setCreatedate(new Date());
    		bean.setCreateuser(userInfo.getUserId());
    		bean.setSiteid(userInfo.getSiteId());
    		bean.setSwl(swl);
			swLedgerService.insertApp(bean);
		}
    	
    	for (SwLedgerAppBean bean : updateApps) {
    		bean.setModifydate(new Date());
    		bean.setModifyuser(userInfo.getUserId());
    		bean.setSiteid(userInfo.getSiteId());
    		bean.setSwl(swl);
			swLedgerService.updateApp(bean);
		}
    	
    	for (SwLedgerAppBean bean : delApps) {
			swLedgerService.deleteAppById(bean.getAppId());
		}
    }
    
    public static List<SwLedgerAppBean> converAppListFormString(String str){  
        if (str == null || "".equals(str))  
            return new ArrayList<SwLedgerAppBean>();  
        List<SwLedgerAppBean> list2 = VOUtil.fromJsonToListObject(str, SwLedgerAppBean.class);
        /*JSONArray jsonArray = JSONArray.fromObject(str);  
        @SuppressWarnings("unchecked")//kCHEN 2015-5-6修改，解决转存编码错误的问题。
        Collection<SwLedgerAppBean> collention = JSONArray.toCollection(jsonArray, SwLedgerAppBean.class);  
        List<SwLedgerAppBean> list = new ArrayList<SwLedgerAppBean>();
        for(SwLedgerAppBean bean : collention){
        	list.add(JavaBeanHelper.transDefineMuliteCode(bean));
        }*/
        
        return list2;  
    } 
}
