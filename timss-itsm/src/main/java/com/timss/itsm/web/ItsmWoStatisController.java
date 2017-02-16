package com.timss.itsm.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.itsm.bean.ItsmWoPriority;
import com.timss.itsm.service.ItsmHomePageCardService;
import com.timss.itsm.service.ItsmWoStatisService;
import com.timss.itsm.service.ItsmWoStatisticUtilService;
import com.timss.itsm.vo.ItsmWoStatisticVO;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUserGroup;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Controller
@RequestMapping(value = "/itsm/woStatis")
public class ItsmWoStatisController {
	@Autowired
	private ItsmHomePageCardService itsmHomePageCardService;
	@Autowired
	ItsmWoStatisService itWoStatisService;
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	ItsmWoStatisticUtilService woStatisticUtilService;
	
	private static final Logger LOG = Logger.getLogger(ItsmWoStatisController.class);
	
	
	/**
	 * @description:石碑山风机统计
	 * @author: 王中华
	 * @createDate: 2014-11-25
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value="/woStatisList",method=RequestMethod.GET)
	@ReturnEnumsBind("ITSM_WORKTEAM")
	public String maintainPlanList(){
		return "/woStatisReportList.jsp";
	}
	
	/**
	 * @description:IT运维团队工作量统计
	 * @author: 王中华
	 * @createDate: 2014-11-25
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value="/woItTeamStatistcList",method=RequestMethod.GET)
	public String woItTeamStatistcList() throws Exception{
		return "/woStatistcReport/woItTeamStatistcList.jsp";
	}
	
	/**
	 * @description:IT个人运维工作量统计
	 * @author: 王中华
	 * @createDate: 2014-11-25
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value="/woItPersonStatistcList",method=RequestMethod.GET)
	public String woItPersonStatistcList() throws Exception{
		return "/woStatistcReport/woItPersonStatistcList.jsp";
	}
	
	@RequestMapping(value = "/woItMaintainKPIData",method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> woItMaintainKPIData() throws Exception {
		LOG.info("----------------------IT团队、个人KPI统计查询----------------");
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteid = userInfoScope.getSiteId();
		
		Page<ItsmWoStatisticVO> page = userInfoScope.getPage();
		page.setPageSize(50);
		Long begin = Long.valueOf(userInfoScope.getParam("begin"));
		Long end = Long.valueOf(userInfoScope.getParam("end"));
		String statisticType = userInfoScope.getParam("statisticType");
		
		Date beginTime = new Date(begin);
		Date endTime = new Date(end);
		
		Map<String, Object> itWoStatisticVOMap = itWoStatisService.queryItWoStatisticVO(beginTime, endTime, siteid, statisticType);
		int printNum =  (Integer) itWoStatisticVOMap.get("printNum");
		int serLevelSum = (Integer) itWoStatisticVOMap.get("serLevelSum");
		List<Integer> mergeNumList = (List<Integer>) itWoStatisticVOMap.get("mergeNumList"); //用户个人、团队运维KPI统计，合并行
		int oneLevFTSum =(Integer) itWoStatisticVOMap.get("oneLevFTSum");
		List<ItsmWoStatisticVO> itWoStatisticVOList = (List<ItsmWoStatisticVO>) itWoStatisticVOMap.get("data");
		
		page.setResults(itWoStatisticVOList);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", itWoStatisticVOList.size());
		result.put("rows", page.getResults());
		result.put("printNum", printNum);
		result.put("serLevelSum", serLevelSum);  //服务级别的数量
		result.put("oneLevFTSum", oneLevFTSum);  //一级服务目录的数量，包括“小计”
		result.put("mergeNumList", mergeNumList); //用户个人、团队运维KPI统计，合并行
		return result;
	}
	/**
	 * @description:请求管理KPI月报统计
	 * @author: 王中华
	 * @createDate: 2014-11-25
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value="/woRequstManageKPIList",method=RequestMethod.GET)
	public String woRequstManageKPIList() throws Exception{
		return "/woStatistcReport/woRequstManageKPIList.jsp";
	}

	/**
	 * @description:事件管理KPI月报统计
	 * @author: 王中华
	 * @createDate: 2014-11-26
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value="/woEventManageKPIList",method=RequestMethod.GET)
	public String woEventManageKPIList() throws Exception{
		return "/woStatistcReport/woEventManageKPIList.jsp";
	}
	
	@RequestMapping(value = "/woItManageKPIData",method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> woItRequstManageKPIData() throws Exception {
		LOG.info("----------------------IT请求、事件管理统计查询----------------");
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteid = userInfoScope.getSiteId();
		
		Page<ItsmWoStatisticVO> page = userInfoScope.getPage();
		page.setPageSize(50);
		Long begin = Long.valueOf(userInfoScope.getParam("begin"));
		Long end = Long.valueOf(userInfoScope.getParam("end"));
		String statisticType = userInfoScope.getParam("statisticType");
		
		Date beginTime = new Date(begin);
		Date endTime = new Date(end);
		
		Map<String, Object> itWoStatisticVOMap = itWoStatisService.queryItWoStatisticVO(beginTime, endTime, siteid, statisticType);
		int printNum =  (Integer) itWoStatisticVOMap.get("printNum");
		int serLevelSum = (Integer) itWoStatisticVOMap.get("serLevelSum");
		int oneLevFTSum =(Integer) itWoStatisticVOMap.get("oneLevFTSum");
		List<ItsmWoStatisticVO> itWoStatisticVOList = (List<ItsmWoStatisticVO>) itWoStatisticVOMap.get("data");
		//删掉所有统计数据都是0的记录
//		ItWoStatisticVOList = deleteZeroObj(ItWoStatisticVOList);
		
		page.setResults(itWoStatisticVOList);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", itWoStatisticVOList.size());
		result.put("rows", page.getResults());
		result.put("printNum", printNum);
		result.put("serLevelSum", serLevelSum);  //服务级别的数量
		result.put("oneLevFTSum", oneLevFTSum);  //一级服务目录的数量，包括“小计”
		return result;
	}
	
	
	@RequestMapping(value = "/sdCardStatistic",method=RequestMethod.GET)
	public @ResponseBody String sdCardStatistic() throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		reMap.put("status", "ok");
		
		Map<String, Object> tempResult = new HashMap<String, Object>();
		tempResult = itsmHomePageCardService.sdCardStatistic();
		Object[][] result = (Object[][]) tempResult.get("woSDstatistic");
		//[[桌面终端类, 10.0], [OA系统, 10.0], [商务网, 10.0], [统一沟通平台, 20.0], [EIP系统, 10.0], [ERP系统, 10.0], [其他应用, 20.0], [硬件类, 10.0]]
		List<Object> data = new ArrayList<Object>();
		for(Object[] resultItem : result){
			List<Object> dataItem = new ArrayList<Object>();
			if(resultItem[0] != null && !"null".equals(resultItem[0])){
				dataItem.add(resultItem[0]);
				dataItem.add(resultItem[1]);
				data.add(dataItem);
			}
			
		}
		Map<String, Object> seriesItem = new HashMap<String, Object>();
		seriesItem.put("type", "pie");
		seriesItem.put("name", "占比");
		seriesItem.put("data", data);
		List<Object> series = new ArrayList<Object>();
		series.add(seriesItem);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("series", series);
		reMap.put("data", dataMap);
		return JsonHelper.toJsonString(reMap);
	}
	
	
	@RequestMapping(value = "/itsmTeamRespondSolvestatistic",method=RequestMethod.GET)
	public @ResponseBody String itsmTeamRespondSolvestatistic() throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		reMap.put("status", "ok");
		
		//维护组查询(要统计的维护组)
		List<SecureUserGroup> userGroupList = woStatisticUtilService.retrieveGroupsByKeyword("itc_itsm_wt");
		
		Map<String, Object> tempResult = new HashMap<String, Object>();
		tempResult = itsmHomePageCardService.itsmTeamRespondSolvestatistic();
		Map<String, Double>  solveRatioresult = (Map<String, Double> ) tempResult.get("teamOnTimeSolveRatio");
		Map<String, Double>  respondRatioresult = (Map<String, Double> ) tempResult.get("teamOnTimeRespondRatio");
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		Map<String, Object> xAxisMap = new HashMap<String, Object>();
		List<Object> xAxisMapData = new ArrayList<Object>();
		List<Double> respondData = new ArrayList<Double>();
		List<Double> solveData = new ArrayList<Double>();
		for (int i = 0; i < userGroupList.size(); i++) {
			xAxisMapData.add(userGroupList.get(i).getName());
			respondData.add(respondRatioresult.get(userGroupList.get(i).getId()));
			solveData.add(solveRatioresult.get(userGroupList.get(i).getId()));
		}

		
		xAxisMap.put("categories", xAxisMapData);
		dataMap.put("xAxis", xAxisMap);
		
		
		List<Object> series = new ArrayList<Object>();
		Map<String, Object> seriesItem1 = new HashMap<String, Object>();
		seriesItem1.put("name", "及时响应率");
		seriesItem1.put("data", respondData);
		Map<String, Object> seriesItem2 = new HashMap<String, Object>();
		seriesItem2.put("name", "及时解决率");
		seriesItem2.put("data", solveData);
		
		series.add(seriesItem1);
		series.add(seriesItem2);
		dataMap.put("series", series);
		
		reMap.put("data", dataMap);
		return JsonHelper.toJsonString(reMap);
	}
	
	
	@RequestMapping(value = "/itsmUnOkWostatistic",method=RequestMethod.GET)
	public @ResponseBody String itsmUnOkWostatistic() throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		reMap.put("status", "ok");
		
		Map<String, Object> tempResult = new HashMap<String, Object>();
		tempResult = itsmHomePageCardService.itsmUnOkWostatistic();
		Map<String, Object>  unOKwoResult =  (Map<String, Object>) tempResult.get("unOKwoStatistic");
		
		double unOkRatio = (Double) unOKwoResult.get("ratio");
		double unOkSum =  (Double) unOKwoResult.get("sum");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("unOkRatio", (int)Math.rint(unOkRatio));
		data.put("unOkSum", (int)unOkSum);
		
		reMap.put("data", data);
		return JsonHelper.toJsonString(reMap);
	}
	
	@RequestMapping(value = "/itsmWoSolveAbilitystatistic",method=RequestMethod.GET)
	public @ResponseBody String itsmWoSolveAbilitystatistic() throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		reMap.put("status", "ok");
		
		Map<String, Object> tempResult = new HashMap<String, Object>();
		tempResult = itsmHomePageCardService.itsmWoSolveAbilitystatistic();
		Map<String, Object>  woAbilityMap =  (Map<String, Object>) tempResult.get("abilityResult");
		Map<String, Integer> respondResult= (Map<String, Integer>) woAbilityMap.get("respondResult");
		Map<String, Integer> solveResult= (Map<String, Integer>) woAbilityMap.get("solveResult");
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("respondData",respondResult.get("ratio") );
		data.put("respondFlag",respondResult.get("respondFlag") );
		data.put("solveData", solveResult.get("ratio"));
		data.put("solveFlag",solveResult.get("solveFlag") );
		
		reMap.put("data", data);
		return JsonHelper.toJsonString(reMap);
	}
	
	@RequestMapping(value = "/itsmWoAvgRespondTimestatistic",method=RequestMethod.GET)
	public @ResponseBody String itsmWoAvgRespondTimestatistic() throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		reMap.put("status", "ok");
		
		Map<String, Object> tempResult = new HashMap<String, Object>();
		tempResult = itsmHomePageCardService.woAvgRespondTimesCard();
		Map<String, Object>  respondCarddataMap =  (Map<String, Object>) tempResult.get("respondCard");
		List<ItsmWoPriority> woPriorityList =  (List<ItsmWoPriority>) tempResult.get("woPriorityList");
		
		List<Object> prorityRowData = new ArrayList<Object>();
		for (int i = 0; i < woPriorityList.size(); i++) {
			String priorityName = woPriorityList.get(i).getName();
			int priorityId = woPriorityList.get(i).getId();
			Map<String, Double> tempMap = (Map<String, Double>) respondCarddataMap.get(String.valueOf(priorityId));
			double secondNum = tempMap.get("len");
			double standardLen = tempMap.get("standardLen");
			Map<String, Object> tempItemData = new HashMap<String, Object>();
			tempItemData.put("name", priorityName);
			double second = (int)secondNum;
			String timeValue = String.valueOf((int)second);
			String timeUnit = "秒";
			if(second/60 >= 1 &&second/60 <= 60){
				timeValue = String.valueOf((int)second/60); timeUnit = "分钟";
			}else if(second/3600 > 0 &&second/3600 <= 60){
				timeValue =String.valueOf((double)Math.round(secondNum/3600*10)/10);
				timeUnit = "小时";
			}
			
			tempItemData.put("second", timeValue);
			tempItemData.put("unit", timeUnit);
			tempItemData.put("standardLen", (int)standardLen);
			
			prorityRowData.add(tempItemData);
		}
		
		Collections.reverse(prorityRowData);
		
		reMap.put("data", prorityRowData);
		return JsonHelper.toJsonString(reMap);
	}
}
