package com.timss.workorder.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.workorder.service.ItWoStatisService;
import com.timss.workorder.service.WOStatisService;
import com.timss.workorder.util.WoNoUtil;
import com.timss.workorder.vo.ItWoStatisticVO;
import com.timss.workorder.vo.WoStatisVO;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Controller
@RequestMapping(value = "/workorder/woStatis")
public class WoStatisController{

	@Autowired
	private WOStatisService woStatisService;

	@Autowired
	private ItcMvcService itcMvcService;
	
	private static Logger logger = Logger.getLogger(WoStatisController.class);
	
	
	/**
	 * @description:石碑山风机统计
	 * @author: 王中华
	 * @createDate: 2014-11-25
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value="/woStatisList",method=RequestMethod.GET)
	@ReturnEnumsBind("WO_WORKTEAM")
	public String maintainPlanList() throws Exception{
		return "/woStatisReportList.jsp";
	}
	

	/**
	 * @description:查询工单列表数据
	 * @author: 王中华
	 * @createDate: 2014-8-12
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/WOStatisListdata",method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> woStatisVOListData() throws Exception {
		logger.info("----------------------统计报表查询----------------");
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		
		Page<WoStatisVO> page = userInfoScope.getPage();
		int year =  Integer.valueOf(userInfoScope.getParam("year"));
		int month = Integer.valueOf(userInfoScope.getParam("month"));
		String workteam = userInfoScope.getParam("workteam");
		Date beginTimeDate = WoNoUtil.getFirstDayOfLastMonth(year, month);
		Date endTimeDate = WoNoUtil.getFirstDayOfMonth(year, month);
		page.setParameter("siteid", siteId);
		page.setParameter("beginTime", beginTimeDate );
		page.setParameter("endTime", endTimeDate );
//		System.out.println(userInfoScope.getParam("year"));
		if("all_workteam".equals(workteam)){
			page.setParameter("workteam",null);
		}else{
			page.setParameter("workteam", workteam);
		}
		page = woStatisService.queryStatisWO(page);
		Map<String, ArrayList<Float>> rateAndStopTime = queryWorkTeamRateAndStopTime();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", page.getTotalRecord());
		result.put("rows", page.getResults());
		result.put("rateAndStopTime", rateAndStopTime);
		return result;
	}
	
	@RequestMapping(value = "/printWOStatistic",method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> printWOStatistic() throws Exception {
		logger.info("----------------------统计报表打印----------------");
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		
		Map<String, Object> params = new HashMap<String, Object>();
		int year =  Integer.valueOf(userInfoScope.getParam("year"));
		int month = Integer.valueOf(userInfoScope.getParam("month"));
		String workteam = userInfoScope.getParam("workteam");
		Date beginTimeDate = WoNoUtil.getFirstDayOfLastMonth(year, month+1);
		Date endTimeDate = WoNoUtil.getFirstDayOfMonth(year, month+1);
		params.put("siteId", siteId);
		params.put("beginTime", beginTimeDate );
		params.put("endTime", endTimeDate );
		if("all_workteam".equals(workteam)){
			params.put("workteam",null);
		}else{
			params.put("workteam", workteam);
		}
		int printNum = woStatisService.printWOStatistic(params);
		
		Map<String, ArrayList<Float>> rateAndStopTime = queryWorkTeamRateAndStopTime();
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("printNum", printNum);
		result.put("result", "success");
		return result;
	}
	/**
	 * @description: 查询一些基本的数据，用于初始化页面（维护班组，班组负责多少台机组等等）
	 * @author: 王中华
	 * @createDate: 2014-8-12
	 * @return:
	 */
	@RequestMapping(value = "/initPageBaseData",method=RequestMethod.POST)
	public Map<String, Object> initPageBaseData() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int firstYear = woStatisService.queryYearOfFirstWO();
		if(firstYear == 0){
			Calendar cal = Calendar.getInstance();
			firstYear = cal.get(Calendar.YEAR);
		}
		Map<String, Object> workTeamBaseDate = woStatisService.queryAllWorkTeam();
		resultMap.put("firstYear", firstYear);
		resultMap.put("workTeamBaseDate", workTeamBaseDate);
		resultMap.put("result", "success");
		return resultMap;
	}
	
	/**
	 * @description:查询每个班组的故障率和停机时间
	 * @author: 王中华
	 * @createDate: 2014-8-12
	 * @return:
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public Map<String, ArrayList<Float>> queryWorkTeamRateAndStopTime() throws  Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
//		String siteId = userInfoScope.getSiteId();
		 
		int year =  Integer.valueOf(userInfoScope.getParam("year"));
		int month = Integer.valueOf(userInfoScope.getParam("month"));
		Date beginTimeDate = WoNoUtil.getFirstDayOfLastMonth(year, month);
		Date endTimeDate = WoNoUtil.getFirstDayOfMonth(year, month);
		
		//HashMap<String, Object> resultHashMap = new HashMap<String, Object>();
		
		Map<String, Object> workTeamBaseDate = woStatisService.queryAllWorkTeam();
		
		HashMap<String, Integer> windCount = (HashMap<String, Integer>) workTeamBaseDate.get("workTeamWindCount");
		ArrayList<String> workTeamCodeArrayList = (ArrayList<String>) workTeamBaseDate.get("workteamCodeList");
		LinkedHashMap<String, ArrayList<Float>> ratoAndStopTime = new LinkedHashMap<String,  ArrayList<Float>>();
		for (int i = 0; i < workTeamCodeArrayList.size(); i++) {
			String workTeamCode = workTeamCodeArrayList.get(i);
			HashMap<String,Object>  params = new HashMap<String, Object>();
			params.put("beginTime", beginTimeDate);
			params.put("endTime", endTimeDate);
			if("all_workteam".equals(workTeamCode)){
				params.remove("workteam");
			}else{
				params.put("workteam", workTeamCode);
			}
			
			Map<String, Object> countAndStopTime = woStatisService.queryCountAndStopTime(params);
			int count = (Integer) countAndStopTime.get("count");
			float stopTimeLength = (Float) countAndStopTime.get("stopTime");
			ArrayList<Float>  statisticData = new ArrayList<Float>();
			int wtWindCount = windCount.get(workTeamCode);
			float rato = (float)count/wtWindCount*100;
			statisticData.add(rato);
			statisticData.add(stopTimeLength);
			
			ratoAndStopTime.put(workTeamCode, statisticData);
		}
		return ratoAndStopTime;
	}

	
}
