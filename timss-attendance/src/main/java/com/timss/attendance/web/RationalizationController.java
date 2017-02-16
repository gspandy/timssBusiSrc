package com.timss.attendance.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.attendance.bean.RationalizationBean;
import com.timss.attendance.service.RationalzationService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.VOUtil;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.VaildParam;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.manager.sec.ISecResourceSyncManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.service.ItcSiteService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.mvc.view.ModelAndViewPage;
import com.yudean.workflow.service.SelectUserService;


/**
 * @description: 合理化建议模块
 * @author: liuk	
 * @createDate: 20161013	
 * @param 
 * @return:
 */

@Controller
@RequestMapping(value = "attendance/Rationalization")
public class RationalizationController {

	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
    private AtdUserPrivUtil privUtil;
	@Autowired
	private RationalzationService rationalzationService;
	@Autowired
    private ItcMvcService timssService;
	@Autowired
    private SelectUserService userService;
	@Autowired
	private ItcSiteService siteService;
	@Autowired
    private ISecResourceSyncManager syncManager;
	
	static Logger log = Logger.getLogger( RationalizationController.class );
	
	@RequestMapping(value = "/commit")
	@ReturnEnumsBind("ATD_RATION_TYPE")
	public ModelAndViewAjax commitRationaliation(String formData, String fileIds) throws Exception{
		RationalizationBean bean=VOUtil.fromJsonToVoUtil( formData, RationalizationBean.class);
		HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        log.info("msg="+bean.getRationalType());
        if ( !StringUtils.isBlank( fileIds ) ) {
    		String[] fileArr = fileIds.split( "," );
            bean.setFileIds(fileArr);
    	}
        //这里封装了instanceId
        bean=rationalzationService.commit(bean,userInfoScope.getSecureUser());
        if(bean!=null){
            result.put( "result", "success" );
            result.put( "bean", bean );
        }else{
        	result.put( "result", "fail" );
        }
        return ViewUtil.Json(result);
	}
	
	
	@RequestMapping(value = "/saveRat")
	@ReturnEnumsBind("ATD_RATION_TYPE")
	public ModelAndViewAjax saveRationaliation(String formData, String fileIds) throws Exception {
		log.info("进入暂存方法=====");
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
    	RationalizationBean bean=VOUtil.fromJsonToVoUtil( formData, RationalizationBean.class );
    	if (bean!=null) {
    		if ( !StringUtils.isBlank( fileIds ) ) {
    			String[] fileArr = fileIds.split( "," );
    			bean.setFileIds(fileArr);
    		}
		}
    	bean=rationalzationService.save(bean,userInfo.getSecureUser());
    	rationalzationService.queryWorkFlow(bean, userInfo.getSecureUser());
        Map<String, Object>result= new HashMap<String, Object>();
        if(bean!=null){
        	result.put("result", "success");
        	result.put("bean", bean);
        }else{
        	result.put("result", "fail");
        }
        return timssService.jsons(result);
    }
	
	//详情页面
	@RequestMapping(value = "/detailPage")
	@ReturnEnumsBind("ATD_RATION_TYPE")
    public ModelAndViewPage rationalzationDetailPage(@ModelAttribute("rationalId") String rationalId,@ModelAttribute("mode") String mode) throws Exception {
		RationalizationBean rationalzationBean=new RationalizationBean();
		UserInfoScope userInfo = privUtil.getUserInfoScope();
    	Map<String, Object>result=new HashMap<String, Object>();
        if (rationalId != null&&!"".equals(rationalId) ) {//初始查询 
        	rationalzationBean=rationalzationService.queryDetail(rationalId);
        	if (rationalzationBean.getBonusSplit().equals("0")&&rationalzationBean.getImpDept().equals("0")) {
        		rationalzationBean.setBonusSplit("");
        		rationalzationBean.setImpDept("");
			}
        	rationalzationService.queryWorkFlow(rationalzationBean, userInfo.getSecureUser());
        }else{
        	rationalzationBean.setUserName(userInfo.getUserName());
        	rationalzationBean.setDeptName(userInfo.getOrgName());
        	rationalzationBean.setSiteid(userInfo.getSiteId());
        }
        result.put("bean", JsonHelper.toJsonString(rationalzationBean));
        return timssService.Pages( "rationalization/detail.jsp", "params",result);
    }
	
	
	@RequestMapping(value = "/getList")
	@ReturnEnumsBind("ATD_RATION_TYPE")
	public Page<RationalizationBean> getRationalzationList() throws Exception {
		UserInfoScope userInfoScope = privUtil.getUserInfoScope();
		Page<RationalizationBean> page = userInfoScope.getPage();
		page.setParameter("siteId", userInfoScope.getSiteId());
		// 获取表头搜索的参数，Dao的xml文件里面不用写 if is null这些方法了
        Map<String, String[]> params = userInfoScope.getParamMap();
		log.info("params="+params);
		if (params.containsKey("search")) {
			String fuzzySearchParams = userInfoScope.getParam("search");
			Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( fuzzySearchParams );
			if(fuzzyParams.containsKey("trainingDate")){
				fuzzyParams.put("to_char(trainingDate,'yyyy-mm-dd')", fuzzyParams.get("trainingDate"));
				fuzzyParams.remove("trainingDate");
			}
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
        page = rationalzationService.queryList(page);
        return page;
    }
	
	
  	//点击合理化建议后的列表页面
 	@RequestMapping(value = "/listPage")
 	@ReturnEnumsBind("ATD_RATION_TYPE")
    public String rationalizationPage() throws Exception {
        return "rationalization/list.jsp";
    }
	 
	 
	@RequestMapping(value = "/deleteRationalization")
	@VaildParam(paramName="RationalizationBean")
	public ModelAndViewAjax deleteRationalization(String formData, String fileIds) throws Exception {
		HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        RationalizationBean bean=VOUtil.fromJsonToVoUtil(formData, RationalizationBean.class );
    	if (bean!=null) {
    		if ( !StringUtils.isBlank( fileIds ) ) {
    			String[] fileArr = fileIds.split( "," );
    			bean.setFileIds(fileArr);
    		}
		}
        if(rationalzationService.delete(bean,userInfoScope.getSecureUser())>0){
            result.put( "result", "success" );
        }else{
        	result.put( "result", "fail" );
        }
        return ViewUtil.Json(result);
    }
	
	/**
     * @description: 获取站点下所有部门
     * @author: yangk
     * @createDate: 2016年10月8日
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/getDeptments")
    public @ResponseBody List<List<Object>> getDeptments() throws Exception {
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        //根据站点id获取根节点的orgcode
        List<Map<String, Object>> list =  siteService.selectSiteOrg(null, siteId);
        String ordCode = (String) list.get(0).get("ORG_CODE");
        //根据根节点获取子节点
        List<Organization> depts = syncManager.getDeptByParents(ordCode);
        List<List<Object>> result = new ArrayList<List<Object>>();
        for (Organization organization : depts) {
             List<Object> row = new ArrayList<Object>();
        	 row.add(organization.getCode());
        	 row.add(organization.getName());
        	 result.add(row);
        }
        return result;
    }
	
	
	
	/**
     * @description: 根据部门id获取部门负责人
     * @author: yangk
     * @createDate: 2016-10-9
     * @return
     * @throws Exception:
      */
	
    @RequestMapping(value = "/getBmfzrIds")
    @ResponseBody
    public List<JSONObject> getBmfzrIds() throws Exception { 
         UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
         String orgId = userInfoScope.getParam( "orgId" );
         String groupId = "DPP_HLH_ZZ";
         List<SecureUser> list = userService.byGroupAndOrgCode(groupId, orgId, "D");
         List<JSONObject> result = new ArrayList<JSONObject>();
         for (SecureUser su : list) {
        	 JSONObject row = new JSONObject();
        	 row.put("Id", su.getId());
        	 result.add(row);
		}
    	 return result;
    } 
   
    
     /**
     * 作废
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/invalidRationalization")
    @VaildParam(paramName="RationalizationBean")
    public ModelAndViewAjax invalidTraining(String formData, String fileIds) throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        RationalizationBean bean=VOUtil.fromJsonToVoUtil(formData, RationalizationBean.class );
        //这里是作废流程，该 过程要执行将处理清空
        bean=rationalzationService.invalid(bean,userInfoScope.getSecureUser());
        String instanceId = bean.getInstanceId();
        if(bean!=null){
            result.put( "result", "success" );
        }else{
        	result.put( "result", "fail" );
        }
        return ViewUtil.Json(result);
    }
}
