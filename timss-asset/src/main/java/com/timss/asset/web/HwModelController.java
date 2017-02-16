package com.timss.asset.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.asset.bean.HwModelBean;
import com.timss.asset.service.HwModelService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.VaildParam;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.mvc.view.ModelAndViewPage;

/**
 * 硬件类型维护的controller
 * @author 890147
 *
 */
@Controller
@RequestMapping(value = "asset/hwModel")
public class HwModelController {
	@Autowired
    private ItcMvcService timssService;
	@Autowired
	private HwModelService hwModelService;
	
    static Logger logger = Logger.getLogger( HwModelController.class );
    
    /**
     * 跳转到列表页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/listPage")
    @ReturnEnumsBind("AST_HW_MODEL_TYPE")
    public String hwModelListPage() throws Exception {
        return "/hwModel/list.jsp";
    }
    
    /**
     * 跳转到详情页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detailPage")
    @ReturnEnumsBind("AST_HW_MODEL_TYPE,AST_HW_SERVICE_STATUS")
    public ModelAndViewPage hwModelDetailPage() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String hwModelId = userInfo.getParam( "hwModelId" );
        HwModelBean hwModelBean=null;
        if ( hwModelId != null&&!"".equals(hwModelId) ) {//初始查询 
        	hwModelBean=hwModelService.queryDetail(hwModelId);
        }
        Map<String, String>result=new HashMap<String, String>();
        result.put("hwModelBean", JsonHelper.toJsonString(hwModelBean));
        return timssService.Pages( "/hwModel/detail.jsp", "result",result);
    }
    
    /**
     * 分页查询列表
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getList")
    public Page<HwModelBean> getHwModelList() throws Exception {
        UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
        Page<HwModelBean> page = userInfoScope.getPage();
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
            
            if("modelName".equals(sortKey)){
            	sortKey="NLSSORT(modelName,'NLS_SORT = SCHINESE_PINYIN_M')";
            }else if("modelType".equals(sortKey)){
            	sortKey="NLSSORT(modelType,'NLS_SORT = SCHINESE_PINYIN_M')";
            }
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        } else {
            // 设置默认的排序字段
            page.setSortKey( "NLSSORT(modelName,'NLS_SORT = SCHINESE_PINYIN_M')" );
        }
        
        page = hwModelService.queryList( page );
        return page;
    }

    /**
     * 查询详情
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getDetail")
    public ModelAndViewAjax getHwModelDetail() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String hwModelId = userInfo.getParam( "hwModelId" );
        HwModelBean bean = hwModelService.queryDetail( hwModelId );
        
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put( "hwModelBean", bean );
        return timssService.jsons(result);       
    }
    
    /**
     * 判断站点下同一类型的硬件型号名称是否已存在
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/isNameExistByType")
    public ModelAndViewAjax isModelNameExistByType() throws Exception {
    	UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String modelName = userInfo.getParam( "modelName" );
        String modelType = userInfo.getParam( "modelType" );
        String modelId = userInfo.getParam( "modelId" );
        return timssService.jsons( checkModelNameExistByType(modelType,modelName,modelId,userInfo.getSiteId()) );     
    }
    
    /**
     * 检查站点下同一类型的硬件型号名称是否已存在
     * @param type
     * @param name
     * @param excludeId：
     * @param siteId
     * @return
     * @throws Exception
     */
    private String checkModelNameExistByType(String type,String name,String excludeId,String siteId) throws Exception {
    	HwModelBean infoBean = hwModelService.queryHwModelByNameAndType(type,name,siteId);
        if(infoBean!=null&&!excludeId.equals(infoBean.getModelId()))
            return "硬件类型已经存在";
        else
            return "true" ;
    }
    
    @RequestMapping(value = "/insertHwModel")
    @VaildParam(paramName="hwModelBean")
    public ModelAndViewAjax insertHwModel() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        HwModelBean bean = userInfo.getJavaBeanParam( "hwModelBean", HwModelBean.class );
        String siteId = userInfo.getSiteId();
        bean.setSiteid(siteId);
        Date date=new Date();
        bean.setCreatedate(date);
        bean.setCreateuser(userInfo.getUserId());
        int rst=-1;
        if("true".equals(checkModelNameExistByType(bean.getModelType(),bean.getModelName(),"",userInfo.getSiteId()))){
        	rst=hwModelService.insert(bean);
        }
        
        Map<String, Object>result= new HashMap<String, Object>();
        if(rst==1&&bean.getModelId()!=null){
        	result.put("status", 1);
        	result.put("hwModelBean", bean);
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
    @RequestMapping(value="/deleteHwModel")
    public ModelAndViewAjax deleteHwModel() throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
        String hwModelId = userInfoScope.getParam( "hwModelId" );
        if(hwModelService.deleteById(hwModelId)>0){
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
    @RequestMapping(value = "/updateHwModel")
    @VaildParam(paramName="hwModelBean")
    public ModelAndViewAjax updateHwModel() throws Exception {
    	UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        HwModelBean bean = userInfo.getJavaBeanParam( "hwModelBean", HwModelBean.class );
        String siteId = userInfo.getSiteId();
        bean.setSiteid(siteId);
        Date date=new Date();
        bean.setModifydate(date);
        bean.setModifyuser(userInfo.getUserId());
        int rst=-1;
        if("true".equals(checkModelNameExistByType(bean.getModelType(),bean.getModelName(),bean.getModelId(),userInfo.getSiteId()))){
        	rst=hwModelService.update(bean);
        }
        
        Map<String, Object>result= new HashMap<String, Object>();
        if(rst==1&&bean.getModelId()!=null){
        	result.put("status", 1);
        	result.put("hwModelBean", bean);
        }else if(rst==-1){
			//有重名
        	result.put("status", -1);
		}else{
        	result.put("status", 0);
        }
        return timssService.jsons(result);
    }
    
    /**
     * 
     * @description:通过类型查询硬件型号
     * @author: fengzt
     * @createDate: 2014年12月18日
     * @param rows
     * @param page
     * @param search
     * @param sort
     * @param order
     * @param modelType
     * @return:Map<String, Object>
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    @RequestMapping("/queryHwModelByType")
    public @ResponseBody Map<String, Object> queryHwModelByType( int rows, int page, String search,
            String sort, String order, String modelType ) throws JsonParseException, JsonMappingException{
       Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        
        List<HwModelBean> hwModelBeans = new ArrayList<HwModelBean>();
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        }
        
        //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            Map<String, Object> map = MapHelper.jsonToHashMap( search);
            hwModelBeans = hwModelService.queryHwModelByTypeSearch( map, pageVo, modelType );
            
        }else{
            //默认分页
            hwModelBeans = hwModelService.queryHwModelByType( pageVo, modelType );
        }
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", hwModelBeans );
        if( ! hwModelBeans.isEmpty() ){
            dataMap.put( "total",  pageVo.getTotalRecord() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
        
    }
}
