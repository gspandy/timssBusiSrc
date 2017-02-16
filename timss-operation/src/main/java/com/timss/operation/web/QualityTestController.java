package com.timss.operation.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.timss.operation.bean.QualityTestBean;
import com.timss.operation.service.QualityTestService;
import com.timss.operation.util.OprUserPrivUtil;
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
 * 考勤机的controller
 * @author 890147
 *
 */
@Controller
@RequestMapping(value = "operation/qualityTest")
public class QualityTestController {
	@Autowired
	private QualityTestService qualityTestService;
	@Autowired
    private ItcMvcService timssService;
	@Autowired
    private OprUserPrivUtil privUtil;
	
    static Logger logger = Logger.getLogger( QualityTestController.class );
    
    /**
     * 跳转到列表页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/listPage")
    public String qualityTestListPage() throws Exception {
        return "qualityTest/list.jsp";
    }
    
    /**
     * 跳转到详情页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detailPage")
    public ModelAndViewPage qualityTestDetailPage(@ModelAttribute("qtId") String qtId,@ModelAttribute("mode") String mode) throws Exception {
        QualityTestBean qualityTestBean=new QualityTestBean();
        if ( qtId != null&&!"".equals(qtId) ) {//初始查询 
        	qualityTestBean=qualityTestService.queryDetail(qtId);
        }else{
        	UserInfoScope userInfo = privUtil.getUserInfoScope();
        	qualityTestBean.setUserName(userInfo.getUserName());
        	qualityTestBean.setDeptName(userInfo.getOrgName());
        }
        Map<String, String>result=new HashMap<String, String>();
        result.put("bean", JsonHelper.toJsonString(qualityTestBean));
        return timssService.Pages( "qualityTest/detail.jsp", "params",result);
    }
    
    /**
     * 分页查询列表
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getList")
    public Page<QualityTestBean> getQualityTestList() throws Exception {
        UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        Page<QualityTestBean> page = userInfoScope.getPage();
        page.setParameter("siteId", userInfoScope.getSiteId());
        
        // 获取表头搜索的参数，Dao的xml文件里面不用写 if is null这些方法了
        Map<String, String[]> params = userInfoScope.getParamMap();
        
        if (params.containsKey("search")) {
			String fuzzySearchParams = userInfoScope.getParam("search");
			Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( fuzzySearchParams );
			if(fuzzyParams.containsKey("qtDate")){
				fuzzyParams.put("to_char(qtDate,'yyyy-mm-dd')", fuzzyParams.get("qtDate"));
				fuzzyParams.remove("qtDate");
			}
			if(fuzzyParams.containsKey("createdate")){
				fuzzyParams.put("to_char(createdate,'yyyy-mm-dd hh24:mi')", fuzzyParams.get("createdate"));
				fuzzyParams.remove("createdate");
			}
			page.setFuzzyParams(fuzzyParams);
		}
        
        // 设置排序内容
        if ( params.containsKey( "sort" ) ) { 
            String sortKey = userInfoScope.getParam( "sort" );
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        }
        
        page = qualityTestService.queryList( page );
        return page;
    }

    /**
     * 查询详情
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getDetail")
    public ModelAndViewAjax getQualityTestDetail(String qtId) throws Exception {
        QualityTestBean bean = qualityTestService.queryDetail( qtId );
        
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put( "bean", bean );
        return timssService.jsons(result);       
    }
    
    @RequestMapping(value = "/insertQualityTest")
    @VaildParam(paramName="qualityTestBean")
    public ModelAndViewAjax insertQualityTest() throws Exception {
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        QualityTestBean bean = userInfo.getJavaBeanParam( "jsonData", QualityTestBean.class );
        String siteId = userInfo.getSiteId();
        bean.setSiteid(siteId);
        Date date=new Date();
        bean.setCreatedate(date);
        bean.setCreateuser(userInfo.getUserId());
        int rst=-1;
        rst=qualityTestService.insert(bean);
        
        Map<String, Object>result= new HashMap<String, Object>();
        if(rst==1&&bean.getQtId()!=null){
        	result.put("result", "success");
        	result.put("bean", bean);
        }else{
        	result.put("result", "fail");
        }
        return timssService.jsons(result);
    }
    
    /**
     * 删除
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/deleteQualityTest")
    @VaildParam(paramName="qualityTestBean")
    public ModelAndViewAjax deleteQualityTest() throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        QualityTestBean bean = userInfoScope.getJavaBeanParam( "jsonData", QualityTestBean.class );
        if(qualityTestService.deleteById(bean.getQtId())>0){
            result.put( "result", "success" );
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
    @RequestMapping(value = "/updateQualityTest")
    @VaildParam(paramName="qualityTestBean")
    public ModelAndViewAjax updateQualityTest() throws Exception {
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        QualityTestBean bean = userInfo.getJavaBeanParam( "jsonData", QualityTestBean.class );
        String siteId = userInfo.getSiteId();
        bean.setSiteid(siteId);
        Date date=new Date();
        bean.setModifydate(date);
        bean.setModifyuser(userInfo.getUserId());
        int rst=-1;
        rst=qualityTestService.update(bean);
        
        Map<String, Object>result= new HashMap<String, Object>();
        if(rst==1&&bean.getQtId()!=null){
        	result.put("result", "success");
        	result.put("bean", bean);
        }else{
        	result.put("result", "fail");
        }
        return timssService.jsons(result);
    }
}
