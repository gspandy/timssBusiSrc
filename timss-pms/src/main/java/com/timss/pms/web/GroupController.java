package com.timss.pms.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;

@Controller
@RequestMapping("pms/group")
public class GroupController {
	@Autowired
	IAuthorizationManager iAuthorizationManager;
	
	@RequestMapping("/queryUserListByGroupId")
	public ModelAndViewAjax queryUserListByGroupId(String groupId){
		List<SecureUser> list=iAuthorizationManager.retriveUsersWithSpecificGroup(groupId, null, false, false);
		List<List> resultList=new ArrayList<List>();
		
		if(list!=null){
			for(int i=0;i<list.size();i++){
				List<String> record=new ArrayList<String>();
				SecureUser secureUser=list.get(i);
				record.add(secureUser.getId());
				record.add(secureUser.getName());
				resultList.add(record);
			}
		}
		return ViewUtil.Json(resultList);
	}
}
