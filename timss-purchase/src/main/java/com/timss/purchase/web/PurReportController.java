package com.timss.purchase.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.inventory.service.InvItemService;
import com.timss.inventory.service.InvMatTranDetailService;
import com.timss.inventory.vo.InvSafetyStockVO;
import com.timss.purchase.service.PurOrderService;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurApplyController.java
 * @author: 890166
 * @createDate: 2014-6-20
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "purchase/purreport")
public class PurReportController {

  @Autowired
  private ItcMvcService itcMvcService;

  @Autowired
  private PurOrderService purOrderService;

  @Autowired
  private InvMatTranDetailService invMatTranDetailService;

  @Autowired
  private InvItemService invItemService;

  /**
   * 查询采购申请列表数据
   * 
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/queryPurApplyInfoReport", method = RequestMethod.GET)
  public ModelAndView queryPurApplyInfoReport() throws Exception {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    ModelAndView mav = new ModelAndView("/purreport/purApplyReport.jsp");

    mav.addObject("siteId", userInfo.getSiteId());
    return mav;
  }

  /**
   * 采购汇总报表数据
   * 
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/queryPurGroupReport", method = RequestMethod.GET)
  public ModelAndView queryPurGroupReport() throws Exception {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    ModelAndView mav = new ModelAndView("/purreport/purGroupReport.jsp");

    mav.addObject("siteId", userInfo.getSiteId());
    return mav;
  }

  /**
   * 采购金额卡片查询
   * 
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/purPriceTotal", method = RequestMethod.GET)
  @ResponseBody
  public String purPriceTotal() throws Exception {

    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    String siteId = userInfo.getSiteId();

    Map<String, Object> reMap = new HashMap<String, Object>();
    reMap.put("status", "ok");

    Calendar c = Calendar.getInstance();

    String beginDate = c.get(Calendar.YEAR) + "-01-01";
    String endDate = c.get(Calendar.YEAR) + "-12-31";
    String totalPrice = purOrderService.queryPurPriceTotal(beginDate, endDate, siteId);

    Map<String, Object> data = new HashMap<String, Object>();
    data.put("totalPrice", totalPrice == null ? "0" : totalPrice);
    reMap.put("data", data);
    return JsonHelper.toJsonString(reMap);
  }

  /**
   * 暂估库存金额卡片查询（已报账+未报账，原为"库存金额"卡片）
   * 
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/invPriceTotal", method = RequestMethod.GET)
  @ResponseBody
  public String invPriceTotal() throws Exception {
	    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
	    Map<String, Object> reMap = new HashMap<String, Object>();
	    reMap.put("status", "ok");
	
	    //TIM-2274 原为"库存金额"卡片，后改为"暂估库存金额",取值等于"当前库存金额统计"卡片的已报账、未报账金额的总和
		Map<String, BigDecimal> tempResult = purOrderService.reimbursedMoneyStatistic(userInfo, null);
		BigDecimal stockMoneyTotal = tempResult.get("stockMoneyTotal");
	    Map<String, Object> data = new HashMap<String, Object>();
	    data.put("totalPrice", stockMoneyTotal);
	    reMap.put("data", data);
	    return JsonHelper.toJsonString(reMap);
  }

  /**
   * 实际库存金额卡片查询（已报账）
   * 
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/invActualTotal", method = RequestMethod.GET)
  @ResponseBody
  public String invActualTotal() throws Exception {
	    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
	    Map<String, Object> reMap = new HashMap<String, Object>();
	    reMap.put("status", "ok");
	
	    //TIM-2274  增加"实际库存金额"卡片，取值等于"当前库存金额统计"卡片中的"已报账"的值
		Map<String, BigDecimal> tempResult = purOrderService.reimbursedMoneyStatistic(userInfo, null);
		BigDecimal reimbursedMoneyTotal = tempResult.get("reimbursedMoneyTotal");
	    Map<String, Object> data = new HashMap<String, Object>();
	    data.put("totalPrice", reimbursedMoneyTotal);
	    reMap.put("data", data);
	    return JsonHelper.toJsonString(reMap);
  }
  
  /**
   * 库存不足提醒卡片查询
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/invSafetyStock", method = RequestMethod.GET)
  @ResponseBody
  public String invSafetyStock() throws Exception {
    Map<String, Object> reMap = new HashMap<String, Object>();
    reMap.put("status", "ok");

    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    String siteId = userInfo.getSiteId();

    JSONArray jsonArr = null;
    List<InvSafetyStockVO> issvList = invItemService.querySafetyStockNow(siteId);
    if (null == issvList || issvList.isEmpty()) {
      InvSafetyStockVO issv = new InvSafetyStockVO();
      issv.setItemname("当前库存量充足");
      issvList = new ArrayList<InvSafetyStockVO>();
      issvList.add(issv);
    }
    jsonArr = JSONArray.fromObject(issvList);
    reMap.put("data", jsonArr);
    return JsonHelper.toJsonString(reMap);
  }

  /**
   * 查询本年度合同（审批通过）下各专业物资采购金额总和 
   * 
   * @return
   * @throws Exception
   */
	@RequestMapping(value = "/majorPurchaseStatistic")
	public @ResponseBody String sdCardStatistic() throws Exception {
	    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		Map<String, Object> reMap = new HashMap<String, Object>();
		reMap.put("status", "ok");
		
		List<Map<String, BigDecimal>> tempResult = purOrderService.queryMajorPurchase(userInfo);
		//[[桌面终端类, 10.0], [OA系统, 10.0], [商务网, 10.0], [统一沟通平台, 20.0], [EIP系统, 10.0], [ERP系统, 10.0], [其他应用, 20.0], [硬件类, 10.0]]
		//求采购总额
		List<Object> data = new ArrayList<Object>();
		double total = 0;
		for(Map<String, BigDecimal> resultItem : tempResult){
			if(resultItem.containsKey("MAJOR_TOTAL") && resultItem.get("MAJOR_TOTAL")!=null){
				total += Double.valueOf(resultItem.get("MAJOR_TOTAL").toString());
			}	
		}
		//组合数据
		for(Map<String, BigDecimal> resultItem : tempResult){
			List<Object> dataItem = new ArrayList<Object>();
			if(resultItem.containsKey("MAJOR_NAME") && resultItem.get("MAJOR_NAME")!=null && (!"".equals(resultItem.get("MAJOR_NAME")))){
				dataItem.add(resultItem.get("MAJOR_NAME"));
			}
			else{
				dataItem.add("未知专业");
			}
			dataItem.add(Double.valueOf(resultItem.get("MAJOR_TOTAL").toString()));
			data.add(dataItem);
		}
		Map<String, Object> seriesItem = new HashMap<String, Object>();
		seriesItem.put("type", "pie");
		seriesItem.put("name", "占比");
		seriesItem.put("total", total);
		seriesItem.put("data", data);
		List<Object> series = new ArrayList<Object>();
		series.add(seriesItem);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("series", series);
		reMap.put("data", dataMap);
		return JsonHelper.toJsonString(reMap);
	}
	
    /**
     * @description:当前站点库存报账金额统计（分已报账和未报账）
     * @author: 890151
     * @createDate: 2016-10-26
     * @param userInfoScope
     * @throws Exception
     */
	@RequestMapping(value = "/reimbursedMoneyStatistic")
	public @ResponseBody String reimbursedMoneyStatistic() throws Exception {
	    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		Map<String, Object> reMap = new HashMap<String, Object>();
        String wareHouseId = userInfo.getParam( "wareHouseId" );
		//查询当前仓库库存金额统计
		Map<String, BigDecimal> tempResult = purOrderService.reimbursedMoneyStatistic(userInfo, wareHouseId);
		//数据形式：[[桌面终端类, 10.0], [OA系统, 10.0]]
		List<Object> data = new ArrayList<Object>();
		//组合数据
		List<Object> reimbursedObject = new ArrayList<Object>();
		reimbursedObject.add("已报账");
		reimbursedObject.add(tempResult.get("reimbursedMoneyTotal"));
		data.add(reimbursedObject);
		
		List<Object> unReimbursedObject = new ArrayList<Object>();
		unReimbursedObject.add("未报账");
		unReimbursedObject.add(tempResult.get("unReimbursedMoneyTotal"));
		data.add(unReimbursedObject);
		
		Map<String, Object> seriesItem = new HashMap<String, Object>();
		List<Object> series = new ArrayList<Object>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		seriesItem.put("type", "pie");
		seriesItem.put("name", "占比");
		seriesItem.put("total", tempResult.get("stockMoneyTotal"));
		seriesItem.put("data", data);
		series.add(seriesItem);
		dataMap.put("series", series);
		reMap.put("data", dataMap);
		reMap.put("status", "ok");
		return JsonHelper.toJsonString(reMap);
	}
}
