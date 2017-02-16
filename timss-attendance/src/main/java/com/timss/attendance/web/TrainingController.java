package com.timss.attendance.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.bean.TrainingBean;
import com.timss.attendance.service.TrainingService;
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
@RequestMapping(value = "attendance/training")
public class TrainingController {
	@Autowired
	private TrainingService trainingService;
	@Autowired
    private ItcMvcService timssService;
	@Autowired
    private AtdUserPrivUtil privUtil;
	
    static Logger logger = Logger.getLogger( TrainingController.class );
    
    /**
     * 跳转到列表页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/listPage")
    public String trainingListPage() throws Exception {
        return "training/list.jsp";
    }
    
    /**
     * 跳转到详情页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detailPage")
    public ModelAndViewPage trainingDetailPage(@ModelAttribute("trainingId") String trainingId,@ModelAttribute("mode") String mode) throws Exception {
        TrainingBean trainingBean=new TrainingBean();
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
    	Map<String, Object>result=new HashMap<String, Object>();
        if ( trainingId != null&&!"".equals(trainingId) ) {//初始查询 
        	trainingBean=trainingService.queryDetail(trainingId);
        	trainingService.queryWorkFlow(trainingBean, userInfo.getSecureUser());
        }else{
        	trainingBean.setUserName(userInfo.getUserName());
        	trainingBean.setDeptName(userInfo.getOrgName());
        	trainingBean.setSiteid(userInfo.getSiteId());
        }
        result.put("bean", JsonHelper.toJsonString(trainingBean));
        return timssService.Pages( "training/detail.jsp", "params",result);
    }
    /**
     * 跳转到添加人员页面
     * @return
     * @throws Exception
     */
    @RequestMapping("/insertItemToPage")
    public ModelAndView insertOvertimeItemToPage(String isToUnify){
    	Map<String, Object>map=new HashMap<String, Object>();
    	map.put("isToUnify", isToUnify);
        return new ModelAndView( "training/insertItem.jsp", map );
    }
    /**
     * 分页查询列表
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getList")
    public Page<TrainingBean> getTrainingList() throws Exception {
        UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        Page<TrainingBean> page = userInfoScope.getPage();
        page.setParameter("siteId", userInfoScope.getSiteId());
        
        // 获取表头搜索的参数，Dao的xml文件里面不用写 if is null这些方法了
        Map<String, String[]> params = userInfoScope.getParamMap();
        
        if (params.containsKey("search")) {
			String fuzzySearchParams = userInfoScope.getParam("search");
			Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( fuzzySearchParams );
			/*if(fuzzyParams.containsKey("trainingDate")){
				fuzzyParams.put("to_char(trainingDate,'yyyy-mm-dd')", fuzzyParams.get("trainingDate"));
				fuzzyParams.remove("trainingDate");
			}*/
			if(fuzzyParams.containsKey("createdate")){
				fuzzyParams.put("to_char(createdate,'yyyy-mm-dd hh24:mi')", fuzzyParams.get("createdate"));
				fuzzyParams.remove("createdate");
			}
			if(fuzzyParams.containsKey("startDate")){
				fuzzyParams.put("to_char(startDate,'yyyy-mm-dd')", fuzzyParams.get("startDate"));
				fuzzyParams.remove("startDate");
			}
			if(fuzzyParams.containsKey("endDate")){
				fuzzyParams.put("to_char(endDate,'yyyy-mm-dd')", fuzzyParams.get("endDate"));
				fuzzyParams.remove("endDate");
			}
			page.setFuzzyParams(fuzzyParams);
		}
        
        // 设置排序内容
        if ( params.containsKey( "sort" ) ) { 
            String sortKey = userInfoScope.getParam( "sort" );
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        }else{
        	page.setSortKey( "createdate" );
            page.setSortOrder( "desc" );
        }
        
        page = trainingService.queryList( page );
        return page;
    }

    /**
     * 查询详情
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getDetail")
    public ModelAndViewAjax getTrainingDetail(String trainingId) throws Exception {
        TrainingBean bean = trainingService.queryDetail( trainingId );
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        trainingService.queryWorkFlow(bean, userInfo.getSecureUser());
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put( "bean", bean );
        return timssService.jsons(result);       
    }
    
    /**
     * 新建
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/insertTraining")
    @VaildParam(paramName="trainingBean")
    public ModelAndViewAjax insertTraining() throws Exception {
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        TrainingBean bean = userInfo.getJavaBeanParam( "jsonData", TrainingBean.class );
        int rst=-1;
        rst=trainingService.insert(bean,userInfo.getSecureUser());
        
        Map<String, Object>result= new HashMap<String, Object>();
        if(rst==1&&bean.getTrainingId()!=null){
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
    @RequestMapping(value="/deleteTraining")
    @VaildParam(paramName="trainingBean")
    public ModelAndViewAjax deleteTraining(String formData, String fileIds, String addRows, String delRows, String updateRows) throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        TrainingBean bean = trainingService.convertBean(formData, fileIds, addRows, delRows, updateRows);
        if(trainingService.delete(bean,userInfoScope.getSecureUser())>0){
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
    @RequestMapping(value = "/updateTraining")
    @VaildParam(paramName="trainingBean")
    public ModelAndViewAjax updateTraining() throws Exception {
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        TrainingBean bean = userInfo.getJavaBeanParam( "jsonData", TrainingBean.class );
        int rst=-1;
        rst=trainingService.update(bean,userInfo.getSecureUser());
        
        Map<String, Object>result= new HashMap<String, Object>();
        if(rst==1&&bean.getTrainingId()!=null){
        	result.put("result", "success");
        	result.put("bean", bean);
        }else{
        	result.put("result", "fail");
        }
        return timssService.jsons(result);
    }
    
    /**
     * 作废
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/invalidTraining")
    @VaildParam(paramName="trainingBean")
    public ModelAndViewAjax invalidTraining(String formData, String fileIds, String addRows, String delRows, String updateRows) throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        TrainingBean bean = trainingService.convertBean(formData, fileIds, addRows, delRows, updateRows);
        bean=trainingService.invalid(bean,userInfoScope.getSecureUser());
        if(bean!=null){
            result.put( "result", "success" );
        }else{
        	result.put( "result", "fail" );
        }
        return ViewUtil.Json(result);
    }
    
    /**
     * 提交
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/commit")
    @VaildParam(paramName="trainingBean")
    public ModelAndViewAjax commit(String formData, String fileIds, String addRows, String delRows, String updateRows) throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        TrainingBean bean = trainingService.convertBean(formData, fileIds, addRows, delRows, updateRows);
        bean=trainingService.commit(bean,userInfoScope.getSecureUser());
        if(bean!=null){
            result.put( "result", "success" );
            result.put( "bean", bean );
        }else{
        	result.put( "result", "fail" );
        }
        return ViewUtil.Json(result);
    }
    
    /**
     * 暂存
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveTraining")
    @VaildParam(paramName="trainingBean")
    public ModelAndViewAjax saveTraining(String formData, String fileIds, String addRows, String delRows, String updateRows) throws Exception {
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        TrainingBean bean = trainingService.convertBean(formData, fileIds, addRows, delRows, updateRows);
        bean=trainingService.save(bean,userInfo.getSecureUser());
        trainingService.queryWorkFlow(bean, userInfo.getSecureUser());
        
        Map<String, Object>result= new HashMap<String, Object>();
        if(bean!=null){
        	result.put("result", "success");
        	result.put("bean", bean);
        }else{
        	result.put("result", "fail");
        }
        return timssService.jsons(result);
    }
    
    /**
     * @title: 获取用户职位信息
     * @company: gdyd
     * @author: 890199
     * @createDate: 2017-02-07
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getUserJob")
    public Map<String, Object> getJob(String userId) throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();
    	String job = trainingService.getUserJob(userId); 
    	if(job!=null){
    		map.put("job", job);
    		map.put("result", "success");
    	}else{
    		map.put("result", "fail");
    	}
    	return map;
    }
}
