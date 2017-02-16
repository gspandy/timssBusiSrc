package com.timss.workorder.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timss.workorder.bean.WoQx;
import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.service.WoQxService;
import com.timss.workorder.service.WorkOrderService;
import com.timss.workorder.util.WoConstant;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: WorkorderController.java
 * @author: 王中华
 * @createDate: 2014-6-23
 * @updateUser: 王中华
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "/workorder/woQx")
public class WoQxRecordController {
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WoQxService woQxService;
	@Autowired
	private WorkOrderService workOrderService;
	@Autowired
    private IAuthorizationManager authManager;
	
	private static Logger logger = Logger.getLogger(WoQxRecordController.class);
	/**
	 * @description: 打开缺陷列表页面
	 * @author: 王中华
	 * @createDate: 2015-5-28
	 * @return
	 * @throws Exception:
	 * 
	 */
	@RequestMapping(value="/woQxRecordList",method=RequestMethod.GET)
	public String woQxRecordList() throws Exception{
		return "/woQxRecordList.jsp";
	}
	
	
//	@RequestMapping(value="/newQxPage",method=RequestMethod.GET)
//	@ReturnEnumsBind("WO_SPEC,WO_STATUS")
//	public String newBdzqxPage() throws Exception{
//		return "/woQxInfo.jsp";
//	}
	
	
	@RequestMapping(value="/newQxPage",method=RequestMethod.GET)
	@ReturnEnumsBind("WO_SPEC,WO_STATUS")
	public ModelAndView newBdzqxPage() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>(0);
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
                String siteId = userInfoScope.getSiteId();
		ArrayList<ArrayList<Object>> result1 = getUserGroupComboxList(siteId+WoConstant.ONDUTYUSER);
		ArrayList<ArrayList<Object>> result2 = getUserGroupComboxList(siteId+WoConstant.SOLVEDEFECTUSER);
		ArrayList<ArrayList<Object>> result3 = getUserGroupComboxList(siteId+WoConstant.RUNNINGUSER);
		ArrayList<ArrayList<Object>> result4 = getUserGroupComboxList(siteId+WoConstant.AUDITLEADER);
		map.put("onDutyUserGroup", JsonHelper.toJsonString(result1));
		map.put("defectSolveUserGroup", JsonHelper.toJsonString(result2));
		map.put("runningUserGroup", JsonHelper.toJsonString(result3));
		map.put("instructionsUserGroup", JsonHelper.toJsonString(result4));
		ModelAndView modelAndView=new ModelAndView("/woQxInfo.jsp",map);
		return modelAndView;
	}
	
	private  ArrayList<ArrayList<Object>> getUserGroupComboxList(String userGroup){
		 List<SecureUser> users = authManager.retriveUsersWithSpecificGroup(userGroup, null, false, true);
	        ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
	      
	        for ( SecureUser secureUser : users ) {
	            ArrayList<Object> row = new ArrayList<Object>();
	            row.add(secureUser.getId());
	            row.add(secureUser.getName());
	            result.add(row);
	        }
	        return result;
	}
	@RequestMapping(value="/woQxListdata",method=RequestMethod.POST)
	public Page<WoQx> woQxListdata() throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		Page<WoQx> page = userInfoScope.getPage();
		String selectTreeId = userInfoScope.getParam("selectTreeId");
		String fuzzySearchParams = userInfoScope.getParam("search");
        HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "woQxMap", "workorder","WoQxDao");
        
        if(fuzzySearchParams!=null){
        	HashMap<String, Object> fuzzyParams = (HashMap<String, Object>)MapHelper.jsonToHashMap(fuzzySearchParams );
        	if(fuzzyParams.get("selectTreeId") != null){
        		selectTreeId = fuzzyParams.get("selectTreeId").toString();  
            	fuzzyParams.remove("selectTreeId");  //因为选择左边树的查询不同与表头查询，所有要移除
        	}
        	
            fuzzyParams = MapHelper.fromPropertyToColumnMap(fuzzyParams, propertyColumnMap);
        	page.setFuzzyParams(fuzzyParams);
        } 
        
        // 设置排序内容
		if (userInfoScope.getParamMap().containsKey("sort")) {
			String sortKey = userInfoScope.getParam("sort");
			// 如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
			sortKey = propertyColumnMap.get(sortKey);
	
			page.setSortKey(sortKey);
			page.setSortOrder(userInfoScope.getParam("order"));
		} else {
			// 设置默认的排序字段
			page.setSortKey("MODIFYDATE");
			page.setSortOrder("desc");
			
		}
		page.setParameter("siteid", siteId);
		if(selectTreeId != null && !"null".equals(selectTreeId)){
			page.setParameter("selectTreeId", selectTreeId);  //设置树查询参数
		}
		
		page = woQxService.queryAllWoQx(page);
		
		return page;
	}
	
	
//	@RequestMapping(value = "/getUserGroupUsers")
//    public @ResponseBody ArrayList<ArrayList<Object>> getUserGroupUsers() throws Exception{
//        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
//        String userGroup = userInfoScope.getParam("userGroup");
//        List<SecureUser> users = authManager.retriveUsersWithSpecificGroup(userGroup, null, false, true);
//        ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
//      
//        for ( SecureUser secureUser : users ) {
//            ArrayList<Object> row = new ArrayList<Object>();
//            row.add(secureUser.getId());
//            row.add(secureUser.getName());
//            result.add(row);
//        }
//        return result;
//    }
	
	
	@RequestMapping(value = "/commitWoQxdata",method=RequestMethod.POST)
	public Map<String,String> commitWoQx() throws Exception {
		logger.info("---------------进入提交缺陷记录-------------------------");
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String woQxRecordFormDate = userInfoScope.getParam("woQxRecordForm");//获取前台传过来的form表单数据
		WoQx  woQx= JsonHelper.toObject(woQxRecordFormDate, WoQx.class);
		//删掉描述类字段中的回车键标识符
		woQx.setDefectDes( woQx.getDefectDes() );
		woQx.setDefectSolveDes( woQx.getDefectSolveDes() );
		woQx.setLeaderInstructions( woQx.getLeaderInstructions() );
		woQx.setLeftProblem( woQx.getLeftProblem() );
		
		Map<String, String> insertResultMap = woQxService.insertUpdateWoQx(woQx);
		
		return insertResultMap;
	}
	
	
	@RequestMapping(value = "/queryWoQxDataById",method=RequestMethod.POST)
    public WoQx queryWoQxDataById() throws Exception{
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String woQxId = userInfoScope.getParam("woQxId");
    	WoQx returnData = woQxService.queryWoQxById(woQxId);
    	return returnData;
    }
	
	
	@RequestMapping(value = "/deleteWoQx",method=RequestMethod.POST)
	public Map<String,String> deleteWoQx() throws Exception {
		logger.info("---------------进入删除缺陷记录-------------------------");
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String woQxId = userInfoScope.getParam("woQxId");//获取前台传过来的form表单数据
		Map<String, String> deleteResultMap = woQxService.deleteWoQxById(woQxId);
		
		return deleteResultMap;
	}
	
	
	
	@RequestMapping(value="/qxRelateWoListdata",method=RequestMethod.POST)
	public Page<WorkOrder> qxRelateWoListdata() throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		Page<WorkOrder> page = userInfoScope.getPage();
		page.setSortKey("CREATEDATE");
		page.setSortOrder("desc");
		
		String woQxId = userInfoScope.getParam("woQxId");
		page.setParameter("woQxId", woQxId);
		
		page = workOrderService.queryAllRelateWoOfQx(page);
		return page;
	}
}
