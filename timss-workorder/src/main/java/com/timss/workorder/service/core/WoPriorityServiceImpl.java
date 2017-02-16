package com.timss.workorder.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.bean.WoPriConfig;
import com.timss.workorder.bean.WoPriority;
import com.timss.workorder.dao.WoPriConfigDao;
import com.timss.workorder.dao.WoPriorityDao;
import com.timss.workorder.service.WoPriorityService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service("WoPriorityServiceImpl")
public class WoPriorityServiceImpl implements WoPriorityService {
	@Autowired
	private ItcMvcService ItcMvcService;
	@Autowired
	private WoPriorityDao woPriorityDao;
	@Autowired
	private WoPriConfigDao woPriConfigDao;
	
	private static Logger logger = Logger.getLogger(WoPriorityServiceImpl.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void insertWoPriority(Map<String, String> addWoLabelDataMap) {
		UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		String deptId = userInfoScope.getOrgId();
		String userId = userInfoScope.getUserId();
		String woPriorityForm = addWoLabelDataMap.get("woPriorityForm");
		String woPriConfData = addWoLabelDataMap.get("woPriConfData");
		JSONObject woPriConfJsonObj = JSONObject.fromObject(woPriConfData);
		int woPriConfDatagridNum = 0;
		JSONArray woPriConfJsonArray = null;
		if(woPriConfJsonObj.containsKey("total")){
			woPriConfDatagridNum =Integer.valueOf(woPriConfJsonObj.get("total").toString());  //记录数
			woPriConfJsonArray = woPriConfJsonObj.getJSONArray("rows"); //记录数组
		}
		int id = woPriorityDao.getNextParamsConfId();
		WoPriority woPriority = JsonHelper.fromJsonStringToBean(woPriorityForm, WoPriority.class);
		woPriority.setSiteid(siteId);
		woPriority.setDeptid(deptId);
		woPriority.setCreatedate(new Date());
		woPriority.setCreateuser(userId);
		woPriority.setId(id);
		woPriority.setYxbz(1);
		//插入服务级别基本信息
		woPriorityDao.insertWoPriority(woPriority);
		//插入服务级别对应的“紧急度”和“影响范围”
		for(int i=0; i<woPriConfDatagridNum; i++){
			String itemsRecord = woPriConfJsonArray.get(i).toString();  //某条记录的字符串表示
			WoPriConfig woPriConfig = JsonHelper.fromJsonStringToBean(itemsRecord, WoPriConfig.class);
			woPriConfig.setPriId(id);
			woPriConfig.setSiteid(siteId);
			woPriConfigDao.insertWoPriConfig(woPriConfig);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateWoPriority(Map<String, String> addWoLabelDataMap) {
		UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
		String userId = userInfoScope.getUserId();
		String siteid = userInfoScope.getSiteId();
		
		String woPriorityForm = addWoLabelDataMap.get("woPriorityForm");
		String woPriConfData = addWoLabelDataMap.get("woPriConfData");
		JSONObject woPriConfJsonObj = JSONObject.fromObject(woPriConfData);
		int woPriConfDatagridNum = 0;
		JSONArray woPriConfJsonArray = null;
		if(woPriConfJsonObj.containsKey("total")){
			woPriConfDatagridNum =Integer.valueOf(woPriConfJsonObj.get("total").toString());  //记录数
			woPriConfJsonArray = woPriConfJsonObj.getJSONArray("rows"); //记录数组
		}
		
		WoPriority woPriority;
		try {
			woPriority = JsonHelper.toObject(woPriorityForm, WoPriority.class);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} 
		woPriority.setModifydate(new Date());
		woPriority.setModifyuser(userId);
		woPriorityDao.updateWoPriority(woPriority);
		woPriConfigDao.deleteWoPriConfig(woPriority.getId(), siteid);
		
		//插入服务级别对应的“紧急度”和“影响范围”
		for(int i=0; i<woPriConfDatagridNum; i++){
			String itemsRecord = woPriConfJsonArray.get(i).toString();  //某条记录的字符串表示
			WoPriConfig woPriConfig = JsonHelper.fromJsonStringToBean(itemsRecord, WoPriConfig.class);
			woPriConfig.setPriId(woPriority.getId());
			woPriConfig.setSiteid(siteid);
			woPriConfigDao.insertWoPriConfig(woPriConfig);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public HashMap<String, Object> queryWoPriorityById(int id,String siteid) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		WoPriority woPriority = woPriorityDao.queryWoPriorityById(id);
		List<WoPriConfig> woPriConfigList = woPriConfigDao.queryWoPriConfigListById(id, siteid);
		result.put("baseData", woPriority);
		result.put("datagridData", woPriConfigList);
		return result;
	}

	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Page<WoPriority> queryWoPriorityList(Page<WoPriority> page) {

		List<WoPriority> ret = woPriorityDao.queryWoPriorityList(page);
		page.setResults(ret);
		logger.info("查询优先级列表信息");
		
		return page;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public HashMap<String, String> getPriIdValByUrgentInfluence(String urgentVal,
			String influenceVal) {
		HashMap<String, String> result = new HashMap<String, String>();
		UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
		String siteid = userInfoScope.getSiteId();
		int priId = 0 ;
		String priName ="";
		WoPriConfig woPriConfig = woPriConfigDao.queryWoPriConfigByOtherCode(urgentVal, influenceVal,siteid);
		if(woPriConfig != null){
			priId = woPriConfig.getPriId();
			priName = woPriorityDao.queryWoPriorityById(priId).getName();
		}
		result.put("priId", String.valueOf(priId));
		result.put("priName", priName);
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public HashMap<String, Object> deleteWoPriority(int woPriorityId,
			String siteid) {
		woPriConfigDao.deleteWoPriConfig(woPriorityId, siteid);
		woPriorityDao.deleteWoPriority(woPriorityId,siteid);
		return null;
	}

}
