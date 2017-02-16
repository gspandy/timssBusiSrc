package com.timss.workorder.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.workorder.dao.WOStatisDao;
import com.timss.workorder.service.WOStatisService;
import com.timss.workorder.vo.WoStatisVO;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class WOStatisServiceImpl implements WOStatisService {
	@Autowired
	private ItcMvcService ItcMvcService;
	@Autowired
	private WOStatisDao woStatisDao;

	private static Logger logger = Logger.getLogger(WOStatisServiceImpl.class);
	

	@Override
	public Page<WoStatisVO> queryStatisWO(Page<WoStatisVO> page) {
		
		List<WoStatisVO> ret = woStatisDao.queryStatisWO(page);
		for (int i = 0; i < ret.size(); i++) {
			WoStatisVO woStatisVO = ret.get(i);

			if(woStatisVO.getEndTime()!= null && woStatisVO.getDiscoverTime()!=null ){
				Long num = woStatisVO.getEndTime().getTime() - woStatisVO.getDiscoverTime().getTime() ;
				woStatisVO.setFaultStopTime(new Long(num/1000/60/60).intValue());
			}
		}
		page.setResults(ret);
		logger.info("查询工单统计信息");
		return page;
	}



	@Override
	public int queryYearOfFirstWO() {
		UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		int minYear = woStatisDao.queryYearOfFirstWO(siteId);
		return minYear;
	}


	@Override
	public HashMap<String, Object> queryAllWorkTeam() {
		UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		
		List<AppEnum> workteam = ItcMvcService.getEnum("WO_WORKTEAM");
		HashMap<String, Object> resultHashMap  = new HashMap<String, Object>();
		LinkedHashMap<String, String> workteamMap = new LinkedHashMap<String, String>();
		ArrayList<String> workteamCodeList = new ArrayList<String>();
		workteamCodeList.add("all_workteam");
		workteamMap.put("all_workteam", "全场");
		LinkedHashMap<String, Integer> workteamWindSumHashMap = new LinkedHashMap<String, Integer>();
		int allWindSum = 0;
		for(int i=0; i<workteam.size(); i++){
			AppEnum workteamAppEnum = workteam.get(i);
			String workteamCode = workteamAppEnum.getCode();
			workteamCodeList.add(workteamCode);
			
			workteamMap.put(workteamCode, workteamAppEnum.getLabel());
			int windSum = woStatisDao.queryWorkTeamWindSum(siteId,workteamCode);
			allWindSum = allWindSum + windSum;
			workteamWindSumHashMap.put(workteamCode, windSum);
		}
		workteamWindSumHashMap.put("all_workteam", allWindSum);
		resultHashMap.put("workteamCodeList", workteamCodeList);
		resultHashMap.put("workTeamName", workteamMap);
		resultHashMap.put("workTeamWindCount", workteamWindSumHashMap);
		//resultHashMap.put("workTeamCount", workteam.size()+1);
		return resultHashMap;
	}


	@Override
	public HashMap<String, Object> getEveryTeamFaultWInfo(String workTeamCode,
			Date beginDate, Date endDate) {
		HashMap<String, Object> resultHashMap  = new HashMap<String, Object>();
		HashMap<String, Object> workTeamFaultInfoHashMap  = new HashMap<String, Object>();
		
		UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
//		String siteId = userInfoScope.getSiteId();
		
		List<AppEnum> workteam = ItcMvcService.getEnum("WO_WORKTEAM");
		for(int i=0; i<workteam.size(); i++){
			AppEnum workteamAppEnum = workteam.get(i);  //那个维护组
			String workteamCode = workteamAppEnum.getCode();
			
			//停机时间
			//TODO
			workTeamFaultInfoHashMap.put("StopTime", null);
			//故障次数
			
			workTeamFaultInfoHashMap.put("faultCount", null);
			
			
			resultHashMap.put(workteamCode, workTeamFaultInfoHashMap);
		}
		return null;
	}



	@Override
	public Map<String, Object> queryCountAndStopTime(
			Map<String, Object> params) {
		
		HashMap<String, Object> resultHashMap = new HashMap<String, Object>();
		int falutSum = woStatisDao.queryWorkTeamFaultSum(params);
		Float faultStopTime = woStatisDao.queryWorkTeamFaultStopTime(params);
		if(faultStopTime==null){
			faultStopTime = (float) 0.0;
		}
		resultHashMap.put("count", falutSum);
		resultHashMap.put("stopTime", faultStopTime);
		return resultHashMap;
	}
	
	@Override
	public int printWOStatistic(Map<String, Object> parmas) {
		String workteamString  = (String) parmas.get("workteam");
		if(null==workteamString){
			parmas.remove("workteam");
		}
		List<WoStatisVO> ret = woStatisDao.printWOStatistic(parmas);
		int printNum = woStatisDao.getNextPrintNum();
		
		for (int i = 0; i < ret.size(); i++) {
			WoStatisVO woStatisVO = ret.get(i);
			if(woStatisVO.getEndTime()!= null && woStatisVO.getDiscoverTime()!=null ){
				Long num = woStatisVO.getEndTime().getTime() - woStatisVO.getDiscoverTime().getTime() ;
				woStatisVO.setFaultStopTime(new Long(num/1000/60/60).intValue());
				woStatisVO.setPrintNum(printNum);
			}
		}
		if(ret.size() != 0){
			woStatisDao.insertBatchWoStatisData(ret);
		}
		
		return printNum;
	}

}
