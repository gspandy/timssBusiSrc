package com.timss.finance.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.yudean.itc.dto.sec.Role;
import com.yudean.mvc.bean.userinfo.UserInfoScope;


/**
 * 
 * @title: 
 * @description: json转编码
 * @company: gdyd
 * @className: myUtil.java
 * @author: wus
 * @createDate: 2014年6月19日
 * @updateUser: wus
 * @version: 1.0
 */
public class FinanceUtil {
//	private static Logger logger = Logger.getLogger(FinanceUtil.class);
	
	
	
	
    
    /** 
     * @description: 在json中通过key获取value
     * @author: 890170
     * @createDate: 2014-12-25
     */
    public static String getJsonFieldString(String Jason,String Name){
		JSONObject jsonObj = JSONObject.fromObject(Jason);
		String value;
		if(jsonObj.containsKey( Name )){
		    value = jsonObj.getString(Name);
		}else{
		    value = "";
		}
    	return value;
    }
    
    public static long getJsonFieldLong(String Jason,String Name){
		JSONObject jsonObj = JSONObject.fromObject(Jason);
		long value = jsonObj.getLong(Name);
    	return value;
    }
    
    /*
    public ModelAndView goToPage(UserInfoScope userInfoScope,ModelMap model,String page) throws Exception{
		userInfoScope.getUserName();
		userInfoScope.getUserId();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("creator", userInfoScope.getUserName());
		model.addAttribute("nowdate",dateFormat.format( now ));
		page="/financeDetail/"+page+".jsp";
    	return new ModelAndView(page,model);
    }
    */
    
    public ModelAndView goToPage(UserInfoScope userInfoScope,ModelMap model,String page,String finNameEn,String finTypeEn,String finNameCn,String finTypeCn) throws Exception{
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String applyId = userInfoScope.getParam( "applyId" );
		String allowanceType = userInfoScope.getParam( "allowanceType" );
		
		ArrayList<HashMap<String,Object>> fileMap = new ArrayList<HashMap<String,Object>>();
		JSONArray jsonArray = JSONArray.fromObject(fileMap);
		model.addAttribute("uploadFiles", jsonArray);
		
		model.addAttribute("applyId", applyId);
		model.addAttribute("allowanceType", allowanceType);
		model.addAttribute("creatorname", userInfoScope.getUserName());
		model.addAttribute("createid", userInfoScope.getUserId());
		model.addAttribute("nowdate",dateFormat.format( now ));
		model.addAttribute("finTypeEn", finTypeEn);
		model.addAttribute("finNameEn", finNameEn);
		model.addAttribute("finTypeCn", finTypeCn);
		model.addAttribute("finNameCn", finNameCn);
		model.addAttribute("oprModeEn", "add");
		model.addAttribute("oprModeCn", "新建");
		
		page="/financeDetail/"+page+".jsp";
    	return new ModelAndView(page,model);
    }
    
    
    
    /** 
     * @description: 根据数据库中的finflow名称生成报销名称(包括英文中文)
     * @author: 890170
     * @createDate: 2014-11-21
     */
    public static Map<String, String> genFinNameByFinFlow( String finFlow ) {
    	String finNameEn = "";
    	String finNameCn = "";
    	Map<String, String> finNameMap = new HashMap<String, String>();
    	
    	if(finFlow.contains("备用金")) {
			finNameEn = "pettycash";
			finNameCn = "备用金";
		} else if(finFlow.contains("培训费")) {
			finNameEn = "traincost";
			finNameCn = "培训费";
		} else if(finFlow.contains("探亲路费")) {
			finNameEn = "hometripcost";
			finNameCn = "探亲路费";
		} else if(finFlow.contains("补充医疗保险")) {
			finNameEn = "medicalinsurance";
			finNameCn = "补充医疗保险";
		} else if(finFlow.contains("家属医药费")) {
			finNameEn = "familymedicinecost";
			finNameCn = "家属医药费";
		} else if(finFlow.contains("汽车费")) {
			finNameEn = "carcost";
			finNameCn = "汽车费";
		} else if(finFlow.contains("办公费")) {
			finNameEn = "officecost";
			finNameCn = "办公费";
		} else if(finFlow.contains("福利费")) {
			finNameEn = "welfarism";
			finNameCn = "福利费";
		} else if(finFlow.contains("业务招待费")) {
			finNameEn = "businessentertainment";
			finNameCn = "业务招待费";
		} else if(finFlow.contains("会议费")) {
			finNameEn = "meetingcost";
			finNameCn = "会议费";
		} else if(finFlow.contains("差旅费")) {
			finNameEn = "travelcost";
			finNameCn = "差旅费";
		}
    	
    	finNameMap.put("finNameEn", finNameEn);
    	finNameMap.put("finNameCn", finNameCn);
    	
    	return finNameMap;
    }
    
    /** 
     * @description: 根据数据库中的finType名称生成报销类型(包括英文中文)
     * @author: 890170
     * @createDate: 2014-11-21
     */
    public static Map<String, String> genFinTypeByFinType( String finType ) {
    	String finTypeEn = "";
    	String finTypeCn = "";
    	Map<String, String> finTypeMap = new HashMap<String, String>();
    	
		if(finType.contains("自己")) {
			finTypeEn="only";
			finTypeCn="自己";
		} else if(finType.contains("他人")) {
			finTypeEn="other";
			finTypeCn="他人";
		} else if(finType.contains("多人")) {
			finTypeEn="more";
			finTypeCn="多人";
		}
		
		finTypeMap.put("finTypeEn", finTypeEn);
		finTypeMap.put("finTypeCn", finTypeCn);
    	
    	return finTypeMap;
    }
    
    /** 
     * @description: 由报销名称英文生成中文
     * @author: 890170
     * @createDate: 2014-11-24
     */
    public static String genFinNameCn(String finNameEn) {
    	String finNameCn = "";
    	
    	if( finNameEn.equals("pettycash") ) {
			finNameCn = "备用金";
		} else if( finNameEn.equals("traincost") ) {
			finNameCn = "培训费";
		} else if( finNameEn.equals("hometripcost") ) {
			finNameCn = "探亲路费";
		} else if( finNameEn.equals("medicalinsurance") ) {
			finNameCn = "补充医疗保险";
		} else if( finNameEn.equals("familymedicinecost") ) {
			finNameCn = "家属医药费";
		} else if( finNameEn.equals("carcost") ) {
			finNameCn = "汽车费";
		} else if( finNameEn.equals("officecost") ) {
			finNameCn = "办公费";
		} else if( finNameEn.equals("welfarism") ) {
			finNameCn = "福利费";
		} else if( finNameEn.equals("businessentertainment") ) {
			finNameCn = "业务招待费";
		} else if( finNameEn.equals("meetingcost") ) {
			finNameCn = "会议费";
		} else if( finNameEn.equals("travelcost") ) {
			finNameCn = "差旅费";
		} else if( "administractiveExpenses".equals(finNameEn)) {
			finNameCn = "行政";
		}
    	
    	return finNameCn;
    }
    
    /** 
     * @description: 由报销类型英文生成中文
     * @author: 890170
     * @createDate: 2014-11-21
     */
    public static String genFinTypeCn(String finTypeEn) {
    	String finTypeCn = "";
    	
    	if( finTypeEn.equals("only") ) {
			finTypeCn = "自己";
		} else if( finTypeEn.equals("other") ) {
			finTypeCn = "他人";
		} else if( finTypeEn.equals("more") ) {
			finTypeCn = "多人";
		} else if( finTypeEn.equals("external")){
			finTypeCn = "对外";
		} else {
			finTypeCn = "";
		}
    	
    	return finTypeCn;
    }

	 /**
     * @description: 按一定规则生成流程实例编号(不带版本号)
     * @author: 梁兆麟
     * @createDate: 2014年10月11日
     * @param finNameEn
     * @param finTypeEn
     * @return: String
     */
//	public static String genFlowNameEn(String finNameEn, String finTypeEn) {
//		String flowNameEn = new String();
//		
//		if( finTypeEn.equals("only") || finTypeEn.equals("other") ) {
//			if( finNameEn.equals( "businessentertainment" ) ) {
//				flowNameEn = "finance_itc_commondeptchooseone";
//			} else if( finNameEn.equals( "travelcost" ) || finNameEn.equals( "officecost" ) || finNameEn.equals( "meetingcost" ) || finNameEn.equals( "welfarism" ) ) {
//				flowNameEn = "finance_itc_commonone";
//			} else if( finNameEn.equals( "pettycash" ) ) {
//				flowNameEn = "finance_itc_pettycashone";
//			} else if( finNameEn.equals( "carcost" ) ) {
//				flowNameEn = "finance_itc_carcostone";
//			} else if( finNameEn.equals( "traincost" ) || finNameEn.equals( "hometripcost" ) ) {
//				flowNameEn = "finance_itc_commonrespone";
//			} else if( finNameEn.equals( "medicalinsurance" )|| finNameEn.equals( "familymedicinecost" ) ) {
//				flowNameEn = "finance_itc_commonlabourunionone";
//			} else {
//				flowNameEn = "finance_itc_commonone";
//			}
//		} else if( finTypeEn.equals("more") ) {
//			if( finNameEn.equals( "businessentertainment" ) || finNameEn.equals( "travelcost" ) || finNameEn.equals( "officecost" ) || finNameEn.equals( "meetingcost" ) || finNameEn.equals( "welfarism" ) ) {
//				flowNameEn = "finance_itc_commonmore";
//			} else if( finNameEn.equals( "pettycash" ) ) {
//				flowNameEn = "finance_itc_pettycashmore";
//			} else if( finNameEn.equals( "carcost" ) ) {
//				flowNameEn = "finance_itc_carcostmore";
//			} else if( finNameEn.equals( "traincost" ) || finNameEn.equals( "hometripcost" ) ) {
//				flowNameEn = "finance_itc_commonrespmore";
//			} else if( finNameEn.equals( "medicalinsurance" )|| finNameEn.equals( "familymedicinecost" ) ) {
//				flowNameEn = "finance_itc_commonlabourunionmore";
//			} else {
//				flowNameEn = "finance_itc_commonmore";
//			}
//		}
//
//		logger.info("流程编号: " + flowNameEn);
//		
//
//		return flowNameEn;
//	}
	
	//判断用户的角色列表是否存在某个角色
	public static boolean containsRole(List<Role> roleList, String roleId) {
		String id="";
		
		for( int i=0; i<roleList.size(); i++ ) {
			id = roleList.get(i).getId().toString();
			if(id.equals(roleId)) {
				return true;
			}
		}
		
		return false;
	}
}
