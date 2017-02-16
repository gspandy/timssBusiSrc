package com.timss.finance.service;

import java.util.List;
import java.util.Map;

import com.timss.finance.bean.FinInsertParams;
import com.yudean.mvc.bean.userinfo.UserInfo;

import net.sf.json.JSONArray;

/**
 * 
 * @title: 值别Service
 * @description: 为便于作为多表的事务控制，使用统一的汇总的事务控制，该实现类中，主要调用于各业务表的增删改
 * @company: gdyd
 * @className: FinanceSummaryService.java
 * @author: 吴圣
 * @createDate: 2014年7月4日
 * @updateUser: 吴圣
 * @version: 1.0
 */
/** 
* @ClassName: FinanceSummaryService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author A18ccms a18ccms_gmail_com 
* @date 2014-11-13 上午10:07:27 
*  
*/
public interface FinanceSummaryService {
	/**
	 * @description:updateFinance
	 * @author: 吴圣
	 * @createDate: 2014年7月4日
	 * @param
	 * @return:Map
	 * */
//	Map<String, Object> updateFinanceByFid(String formData, String detail,
//			String finNameEn, String finTypeEn, String submitType, String fid,
//			String flowName, String beneficiaryid, JSONArray jsonArr,String uploadIds);
	
	/**
	 * @description:重构
	 * @author: 王中华
	 * @createDate: 2015-8-22
	 * @param insertParams
	 * @param fid
	 * @param flowName
	 * @return:
	 */
	Map<String, Object> updateFinanceByFid(FinInsertParams insertParams,String fid,String flowName);

	/**
	 * @description:通用方法，作为
	 * @author: 吴圣
	 * @createDate: 2014年7月22日
	 * @param
	 * @return:
	 * */
	void createDraft(String flow, String typeName, String name,
			String statusName, String url, UserInfo userInfo) ;

	/**
	 * @description:通用方法，作为
	 * @author: 吴圣
	 * @createDate: 2014年7月22日
	 * @param
	 * @return:
	 * */
	void createProcess(String flow, String processInstId, String typeName,
			String name, String statusName, List<String> operUser, String url,
			UserInfo userInfo);

	/** 
	 * @description: 新增报销信息
	 * @author: 890170
	 * @createDate: 2014-12-25
	 */
//	Map<String, Object> insertFinanceInfo(String formData,
//			String beneficiaryid, String detail, String uploadIds,
//			String finNameEn, String finTypeEn, String submitType);
	

    /**
     * @description: 重载插入的方法，改变参数个数
     * @author: 王中华
     * @createDate: 2015-8-20
     * @param insertParamsMap
     * @return:
     */
    Map<String, Object> insertFinanceInfo(FinInsertParams insertParams);
}
