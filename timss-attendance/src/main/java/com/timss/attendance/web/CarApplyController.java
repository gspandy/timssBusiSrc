package com.timss.attendance.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.timss.attendance.bean.CarApplyBean;
import com.timss.attendance.service.AtdAttachService;
import com.timss.attendance.service.CarApplyService;
import com.timss.attendance.vo.CarApplyVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.VaildParam;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.view.ModelAndViewAjax;

/**
 * @description:
 * @author: 
 * @createDate: 
 * @param 
 * @return:
 */
@Controller
@RequestMapping(value = "attendance/carApply")
public class CarApplyController {
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private CarApplyService carApplyService;
	@Autowired 
    private AtdAttachService attachService;
	
	static Logger logger = Logger.getLogger( CarApplyController.class );
	
	@RequestMapping(value = "/carApplyList")
	@ReturnEnumsBind("ATD_CARAPPLY_STATUS")
	public String complainRecordsList() {
		return "/carapply/carApplyList.jsp";
	}
	/**
     * 页面跳转
     * @return
     */
	@RequestMapping(value = "/openCarApplyPage")
	@ReturnEnumsBind("ATD_CARAPPLY_DESTTYPE")
	public String carApplyDetailPage(){
		return "/carapply/carApplyDetail.jsp";
	}
	
	@RequestMapping(value = "/carApplyListData", method = RequestMethod.POST)
	public Page<CarApplyBean> complainRecordsListData()throws  Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		Page<CarApplyBean> page = userInfoScope.getPage();
		// 表头搜索相关的
		Map<String, String[]> params = userInfoScope.getParamMap();
		Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap(
				"carApplyMap", "attendance", "CarApplyDao" );
		 if(params.containsKey("search")){
	        	String fuzzySearchParams = userInfoScope.getParam("search");
	        	Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap(fuzzySearchParams);
				fuzzyParams = MapHelper.fromPropertyToColumnMap( fuzzyParams, propertyColumnMap );
				if(fuzzyParams.containsKey("START_TIME")){
					fuzzyParams.put("to_char(start_time,'yyyy-mm-dd hh24:mi:ss')", fuzzyParams.get("START_TIME"));
					fuzzyParams.remove("START_TIME");
				}
				if(fuzzyParams.containsKey("END_TIME")){
					fuzzyParams.put("to_char(end_time,'yyyy-mm-dd hh24:mi:ss')", fuzzyParams.get("END_TIME"));
					fuzzyParams.remove("END_TIME");
				}
				if(fuzzyParams.containsKey("CREATE_DATE")){
					fuzzyParams.put("to_char(create_date,'yyyy-mm-dd')", fuzzyParams.get("CREATE_DATE"));
					fuzzyParams.remove("CREATE_DATE");
				}
				page.setFuzzyParams(fuzzyParams);
			}
		// 设置排序内容
		if ( params.containsKey( "sort" ) ) { 
            String sortKey = userInfoScope.getParam( "sort" );
            sortKey = propertyColumnMap.get( sortKey );
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        }else {//设置默认排序字段
        	page.setSortKey("CREATE_DATE");
			page.setSortOrder("desc");
        }
		page.setParameter("siteId", siteId);
		page = carApplyService.queryList(page);
		return page;
	}
	
	@RequestMapping(value = "/queryCarApplyById", method = RequestMethod.POST)
	public  ModelAndViewAjax queryCarApplyById() throws Exception{
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String caId = userInfo.getParam("caId");
		List<Map<String,Object>> fileMaps = attachService .queryAll("carApply", caId);
		CarApplyVo carApplyVo = carApplyService.queryById(caId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("carApplyBean", carApplyVo.getCarApplyBean());
		result.put("taskId",carApplyVo.getTaskId());
		if(fileMaps.size()>0){
			result.put("fileMaps",fileMaps);
		}
		return itcMvcService.jsons(result);
	}
	/*
	 * 暂存，不启动流程
	 */	
	@RequestMapping(value = "/saveCarApply",method = RequestMethod.POST)
	@VaildParam(paramName="carApplyBean")
	public ModelAndViewAjax saveCarApply() throws Exception{
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		CarApplyBean carApplyBean = userInfo.getJavaBeanParam("carApplyBean", CarApplyBean.class);
		String[] uploadIds = null;
		if(!"".equals(userInfo.getParam("uploadIds"))){
			uploadIds = userInfo.getParam("uploadIds").split(",");
		}
		carApplyBean.setUploadIds(uploadIds);
		CarApplyVo carApplyVo = carApplyService.saveCarApply(carApplyBean);
		String caId = carApplyVo.getCarApplyBean().getCaId();
		Map<String, Object> result = new HashMap<String, Object>();
		if(caId != null){
			result.put("status", 1);//1 成功  0失败
			result.put("caId", caId);
		}else{
			result.put("status", 0);
		}
		return itcMvcService.jsons(result);
	}
	
	/*
	 * 跟新，不启动流程(草稿的跟新)
	 */	
	@RequestMapping(value = "/updateCarApply",method = RequestMethod.POST)
	@VaildParam(paramName="carApplyBean")
	public ModelAndViewAjax updateCarApply() throws Exception{
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		CarApplyBean carApplyBean = userInfo.getJavaBeanParam("carApplyBean", CarApplyBean.class);
		String[] uploadIds = null;
		if(!"".equals(userInfo.getParam("uploadIds"))){
			uploadIds = userInfo.getParam("uploadIds").split(",");
		}
		carApplyBean.setUploadIds(uploadIds);
		CarApplyVo carApplyVo = carApplyService.updateCarApply(carApplyBean);
		String caId = carApplyVo.getCarApplyBean().getCaId();
		Map<String, Object> result = new HashMap<String, Object>();
		if(caId != null){
			result.put("status", 1);//1 成功  0失败
			result.put("carApplyVo", carApplyVo);
		}else{
			result.put("status", 0);
		}
		return itcMvcService.jsons(result);
	}
	/*
	 * 提交，启动流程
	 */	
	@RequestMapping(value = "/insertAndStartWorkflow",method = RequestMethod.POST)
	@VaildParam(paramName="carApplyBean")
	public ModelAndViewAjax insertAndStartWorkflow() throws Exception{
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		CarApplyBean carApplyBean = userInfo.getJavaBeanParam("carApplyBean", CarApplyBean.class);
		String[] uploadIds = null;
		if(!"".equals(userInfo.getParam("uploadIds"))){
			uploadIds = userInfo.getParam("uploadIds").split(",");
		}
		carApplyBean.setUploadIds(uploadIds);
		CarApplyVo carApplyVo = carApplyService.insertCarApply(carApplyBean);
		String caId = carApplyVo.getCarApplyBean().getCaId();
		Map<String, Object> result = new HashMap<String, Object>();
		if(caId != null){
			result.put("status", 1);//1 成功  0失败
			result.put("carApplyVo", carApplyVo);
		}else{
			result.put("status", 0);
		}
		return itcMvcService.jsons(result);
	}
	/*
	 * 跟新，启动流程()
	 */	
	@RequestMapping(value = "/updateAndStartWorkflow",method = RequestMethod.POST)
	@VaildParam(paramName="carApplyBean")
	public ModelAndViewAjax updateAndStartWorkflow() throws Exception{
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		CarApplyBean carApplyBean = userInfo.getJavaBeanParam("carApplyBean", CarApplyBean.class);
		String[] uploadIds = null;
		if(!"".equals(userInfo.getParam("uploadIds"))){
			uploadIds = userInfo.getParam("uploadIds").split(",");
		}
		carApplyBean.setUploadIds(uploadIds);
		CarApplyVo carApplyVo = carApplyService.update(carApplyBean);
		String caId = carApplyVo.getCarApplyBean().getCaId();
		Map<String, Object> result = new HashMap<String, Object>();
		if(caId != null){
			result.put("status", 1);//1 成功  0失败
			result.put("carApplyVo", carApplyVo);
		}else{
			result.put("status", 0);
		}
		return itcMvcService.jsons(result);
	}
	/*
	 * 删除
	 */	
	@RequestMapping(value = "/deleteCarApply",method = RequestMethod.POST)
	@VaildParam(paramName="carApplyBean")
	public ModelAndViewAjax deleteCarApply() throws Exception{
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String caId = userInfo.getParam("caId");
		int rst = -1;
		rst = carApplyService.deleteById(caId);
		Map<String, Object> result = new HashMap<String, Object>();
		if(rst != -1){
			result.put("result", "success");
		}else{
			result.put("result", "fail");
		}
		return itcMvcService.jsons(result);
	}
	
	/*
	 * 作废
	 */	
	@RequestMapping(value = "/obsoleteCarApply",method = RequestMethod.POST)
	@VaildParam(paramName="carApplyBean")
	public ModelAndViewAjax obsoleteCarApply() throws Exception{
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String caId = userInfo.getParam("caId");
		int rst = -1;
		rst = carApplyService.obsoleteCarApply(caId);
		Map<String, Object> result = new HashMap<String, Object>();
		if(rst != -1){
			result.put("result", "success");
		}else{
			result.put("result", "fail");
		}
		return itcMvcService.jsons(result);
	}
}
