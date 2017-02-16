package com.timss.inventory.utils;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.inventory.bean.InvMatAccept;
import com.timss.inventory.bean.InvMatAcceptDetail;
import com.timss.inventory.vo.InvMatAcceptDetailVO;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;


/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: GetAcceptDataInWorkflowUtil.java
 * @author: 890145
 * @createDate: 2015-11-5
 * @updateUser: 890145
 * @version: 1.0
 */
@Service
public class GetAcceptDataInWorkflowUtil {
    @Autowired
    ItcMvcService itcMvcService;
    /**
     * 将前端参数转为Object对象
     * @description:
     * @author: 890145
     * @createDate: 2015-11-17
     * @param param
     * @param c
     * @return
     * @throws Exception:
     */
    public <T> T getPoFromBrower(String param,Class<T> c) throws Exception{
    	UserInfoScope userInfo=itcMvcService.getUserInfoScopeDatas();
    	String paramValue=userInfo.getParam(param);
    	T invMatAccept= JsonHelper.fromJsonStringToBean(paramValue, c);
    	return invMatAccept;
    }
    
    public <T> List<T> getListFromBrower(String param,Class<T> c) throws Exception{
    	UserInfoScope userInfo=itcMvcService.getUserInfoScopeDatas();
    	String paramValue=userInfo.getParam(param);
    	paramValue=paramValue.replaceAll("\n", "\\n");
    	List<T> invMatAcceptDetails=JsonHelper.toList(paramValue, c);
    	return invMatAcceptDetails;
    }
    /**
     * 将前端参数转为List对象
     * @description:
     * @author: 890145
     * @createDate: 2015-11-17
     * @param param
     * @param c
     * @return
     * @throws Exception:
     */
    public  InvMatAccept getAccept(){
    	UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String businessDataString=null;
		try {
			businessDataString=userInfoScope.getParam("businessData");
		} catch (Exception e) {
			throw new RuntimeException("获取前端的businessData出错",e);
		}
		if(StringUtils.isNotBlank(businessDataString)){
			businessDataString=businessDataString.replaceAll("\n", "\\n");
		}
		JSONObject jsonObject=JSONObject.fromObject(businessDataString);
		InvMatAccept lists=null;
		if(!"null".equals(jsonObject.get("invMatAccept").toString())){
		
			lists=JsonHelper.fromJsonStringToBean(jsonObject.get("invMatAccept").toString(), InvMatAccept.class);
		}
		
		return lists;
    }
    
    public  List<InvMatAcceptDetail> getDetails(){
    	UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String businessDataString=null;
		try {
			businessDataString=userInfoScope.getParam("businessData");
		} catch (Exception e) {
			throw new RuntimeException("获取前端的businessData出错",e);
		}
		if(StringUtils.isNotBlank(businessDataString)){
			businessDataString=businessDataString.replaceAll("\n", "\\n");
		}
		JSONObject jsonObject=JSONObject.fromObject(businessDataString);
		List<InvMatAcceptDetail> lists=null;
		if(!"null".equals(jsonObject.get("invMatAcceptDetails").toString())){
			lists=JsonHelper.toList(jsonObject.get("invMatAcceptDetails").toString(), InvMatAcceptDetail.class);
		}
		
		return lists;
    }
    
    public  List<InvMatAcceptDetailVO> getDetailVOs(){
    	UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String businessDataString=null;
		try {
			businessDataString=userInfoScope.getParam("businessData");
		} catch (Exception e) {
			throw new RuntimeException("获取前端的businessData出错",e);
		}
		if(StringUtils.isNotBlank(businessDataString)){
			businessDataString=businessDataString.replaceAll("\n", "\\n");
		}
		JSONObject jsonObject=JSONObject.fromObject(businessDataString);
		List<InvMatAcceptDetailVO> lists=null;
		if(!"null".equals(jsonObject.get("invMatAcceptDetails").toString())){
			lists=JsonHelper.toList(jsonObject.get("invMatAcceptDetails").toString(), InvMatAcceptDetailVO.class);
		}
		
		return lists;
    }
}
