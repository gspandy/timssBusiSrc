package com.timss.asset.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.timss.asset.bean.PropertyBean;
import com.timss.asset.service.PropertyService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.mvc.view.ModelAndViewPage;

/**
 * 物业管理的controller
 * @author 890147
 *
 */
@Controller
@RequestMapping(value = "asset/property")
public class PropertyController {
	@Autowired
    private ItcMvcService timssService;
	@Autowired
	private PropertyService propertyService;
	
    static Logger logger = Logger.getLogger( PropertyController.class );
    
    /**
     * 跳转到物业列表页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/listPage")
    @ReturnEnumsBind("AST_HOUSE_TYPE")
    public ModelAndViewPage propertyListPage() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String propertyId = userInfo.getParam( "propertyId" );
        if ( propertyId == null ) {//初始查询根节点 
        	propertyId = propertyService.queryPropertyRootIdBySite(userInfo.getSiteId());
        }
        return timssService.Pages( "/property/list.jsp", "propertyId", propertyId );
    }
    
    /**
     * 跳转到物业详情页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detailPage")
    @ReturnEnumsBind("AST_HOUSE_TYPE")
    public ModelAndViewPage propertyDetailPage() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String propertyId = userInfo.getParam( "propertyId" );
        if ( propertyId == null ) {//初始查询 
        	propertyId = propertyService.queryPropertyRootIdBySite(userInfo.getSiteId());
        }
        PropertyBean propertyBean=propertyService.queryDetail(propertyId);
        Map<String, String>result=new HashMap<String, String>();
        result.put("propertyBean", JsonHelper.toJsonString(propertyBean));
        return timssService.Pages( "/property/detail.jsp", "result",result);
    }
    
    /**
     * 分页查询房产列表
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getList")
    public Page<PropertyBean> getPropertyList() throws Exception {
        UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
        Page<PropertyBean> page = userInfoScope.getPage();
        
        String propertyId = userInfoScope.getParam( "propertyId" );
        if( propertyId == null ){//如果为空，则获得根节点的id
        	propertyId = propertyService.queryPropertyRootIdBySite(userInfoScope.getSiteId());
        }
        
        // 获取表头搜索的参数，Dao的xml文件里面不用写 if is null这些方法了
        Map<String, String[]> params = userInfoScope.getParamMap();

        // 设置排序内容
        if ( params.containsKey( "sort" ) ) { 
            String sortKey = userInfoScope.getParam( "sort" );
            
            if("houseName".equals(sortKey)){
            	sortKey="NLSSORT(houseName,'NLS_SORT = SCHINESE_PINYIN_M')";
            }
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        } else {
            // 设置默认的排序字段
            //page.setSortKey( "assetname" );
        }
        
        page.setParameter( "propertyId", propertyId );
        page = propertyService.queryList( page );
        return page;
    }

    /**
     * 查询房产详情
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getDetail")
    public ModelAndViewAjax getPropertyDetail() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String propertyId = userInfo.getParam( "propertyId" );
        PropertyBean bean = propertyService.queryDetail( propertyId );
        
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put( "propertyBean", bean );
        return timssService.jsons(result);       
    }
    
    /**
     * 查询房产树
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getTree")
    public ModelAndViewAjax getPropertyTree() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String propertyId = userInfo.getParam( "id" );
        HashMap<String, Object> result = new HashMap<String, Object>();
        if( propertyId == null||"".equals(propertyId) ){//如果为空，则获得根节点的id
        	propertyId = propertyService.queryPropertyRootIdBySite(userInfo.getSiteId());
        	PropertyBean bean = propertyService.queryDetail( propertyId );       	
        	result.put( "parent", bean);           
        }
        result.put( "children", propertyService.queryChildren( propertyId ) );
        return timssService.jsons(result);       
    }
    
    /**
     * 用于搜索框的查询
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/hint")
    public ModelAndViewAjax searchHint() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        List<Map<String, Object>> result=new ArrayList<Map<String,Object>>();
        String keyWord = userInfo.getParam( "kw" );
        String site = userInfo.getSiteId();
        if(keyWord!=null||!"".equals(keyWord)){
        	result = propertyService.queryForHint( site,keyWord );//可性能优化，前台只展示前十条，可考虑只查询10条，已优化
        }
        return timssService.jsons(result);       
    }
    
    /**
     * 用于搜索框的查询
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/hintParents")
    public ModelAndViewAjax searchHintParentIds() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        List<String> result=new ArrayList<String>();
        String id = userInfo.getParam( "id" );
        if(id!=null||!"".equals(id)){
        	result = propertyService.queryParents(id);
        }
        return timssService.jsons(result);       
    }
    
    @RequestMapping(value = "/insertProperty")
    public ModelAndViewAjax insertProperty() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        PropertyBean bean = userInfo.getJavaBeanParam( "propertyBean", PropertyBean.class );
        String siteId = userInfo.getSiteId();
        bean.setSiteid(siteId);
        Date date=new Date();
        bean.setCreatedate(date);
        bean.setCreateuser(userInfo.getUserId());
        int rst=propertyService.insert(bean);
        
        Map<String, Object>result= new HashMap<String, Object>();
        if(rst==1&&bean.getParentId()!=null){
        	result.put("status", 1);
        	result.put("propertyBean", bean);
        }else{
        	result.put("status", 0);
        }
        return timssService.jsons(result);
    }
    
    /**
     * 删除房产及其子房产
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/deleteProperty")
    public ModelAndViewAjax deleteProperty() throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
        String propertyId = userInfoScope.getParam( "propertyId" );
        String siteId=userInfoScope.getSiteId();
        String rootId=propertyService.queryPropertyRootIdBySite(siteId);
        
        if(rootId.equals(propertyId)){
        	result.put( "result", "forbidDelRoot" );
        	result.put( "msg", "不可删除根节点" );
        }else if(propertyService.deleteById(propertyId)>0){
            result.put( "result", "ok" );
        }
        return ViewUtil.Json(result);
    }
    
    /**
     * 更新房产
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateProperty")
    public ModelAndViewAjax updateProperty() throws Exception {
    	UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        PropertyBean bean = userInfo.getJavaBeanParam( "propertyBean", PropertyBean.class );
        String siteId = userInfo.getSiteId();
        bean.setSiteid(siteId);
        Date date=new Date();
        bean.setModifydate(date);
        bean.setModifyuser(userInfo.getUserId());
        int rst=propertyService.update(bean);
        
        Map<String, Object>result= new HashMap<String, Object>();
        if(rst==1&&bean.getParentId()!=null){
        	result.put("status", 1);
        	result.put("propertyBean", bean);
        }else{
        	result.put("status", 0);
        }
        return timssService.jsons(result);
    }
}
