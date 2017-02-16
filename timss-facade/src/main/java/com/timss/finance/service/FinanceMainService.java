package com.timss.finance.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.activiti.engine.task.Task;
//import org.springframework.scheduling.config.Task;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.timss.finance.bean.FinInsertParams;
import com.timss.finance.bean.FinanceMain;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 值别Service
 * @description: 
 * @company: gdyd
 * @className: FinanceMainService.java
 * @author: 吴圣
 * @createDate: 2014年6月13日
 * @updateUser: 吴圣
 * @version: 1.0
 */

public interface FinanceMainService {
	 /**
     * 
     * @description:查询结果中返回FinanceMainList的方法
     * @author: 吴圣
     * @createDate: 2014年6月17日
     * @param 
     * @return:List<FinanceMain>
	 * @ 
     */
	 Page<FinanceMain> queryFinanceMainList(Page<FinanceMain> page,UserInfoScope userInfoScope,String search) ;
	
	 
	 /**
	 * @description: 更加申请单的ID，查询此申请单管理的费用报销单列表信息
	 * @author: 王中华
	 * @createDate: 2016-6-8
	 * @param applyId
	 * @return:
	 */
	List<FinanceMain> queryFinanceMainListByApplyId(String applyId,String siteid) ;
	
	 /**
	 * 
	 * @description:查询结果中返回FinanceMain的方法
	 * @author: 吴圣
	 * @createDate: 2014年6月23日
	 * @param 
	 * @return:
	 * @ 
	 */
	 Map<String, Object> queryFinanceMainByFid(String fid);
	 
	 
	 /**
	  * 
	  * @description:更新ByFid
	  * @author: 吴圣
	  * @createDate: 2014-6-25
	  * @param financeType,formData,detail,submitType,fid
	  * 
	  * */
//	 FinanceMain updateFinanceMainByFid( String formData,
//				String detail,String submitType,String fid) ;
//	 
	 
	 /**
	 * @description:重构
	 * @author: 王中华
	 * @createDate: 2015-8-22
	 * @param insertParams
	 * @param fid
	 * @return:
	 */
	FinanceMain updateFinanceMainByFid( FinInsertParams insertParams,String fid) ;
	 
	 
	 /**
	  * 
	  * @description:updateFinanceMainStatus,主要用于流程时更新状态
	  * @author: 吴圣
	  * @createDate: 2014-6-26
	  * @param task,processInstanceId
	  * 
	  * */
	 FinanceMain updateFinanceMainStatus(Task task,String processInstanceId,String flowStatus) ;
	 
	 /**
	  * 
	  * @description:updateFinanceMainStatus,主要用于流程时更新状态
	  * @author: 吴圣
	  * @createDate: 2014-7-31
	  * @param fid,业务id
	  * @param flowStatus 流程状态
	  * */
	 FinanceMain updateFinanceMainStatusByFid(String flowStatus,String fid) ;
	 
		/**
		 * 删除FinanceMain信息的列表
		 * 
		 * @description:
		 * @author: 吴圣
		 * @createDate: 2014-6-16
		 * @param financeMain
		 * @return:
		 */
	 FinanceMain deleteFinanceMain(FinanceMain financeMain) ;
	 
		/**
		 * 查询TaskId表
		 * 
		 * @description:
		 * @author: 吴圣
		 * @createDate: 2014-7-1
		 * @param pid
		 * @param String
		 * @return:
		 */
	 
	 String queryTaskIdByPid(String pid);

	/**
	 * @description: 更新状态，删除专用
	 * @author: 吴圣
	 * @createDate: 2014-7-14
	 * @return: String
	 * @; 
	 */
	String deleteAndUpdateFinanceMain(String fid);

	/**
	  * @description:updateFinanceMainStatusByDid,主要用于流程时更新状态
	  * @author: 吴圣
	  * @createDate: 2014-10-26
	  * @param fid,业务id
	  * @param flowStatus 流程状态
	  * */
	public FinanceMain updateFinanceMainStatusByDid(String flowStatus, String did);

	/** 
	 * @description: 插入报销主表
	 * @author: 890170
	 * @createDate: 2014-12-24
	 */
//	Map<String, Object> insertFinanceMain(String formData, JSONArray jsonArr,
//			String submitType, String finNameEn, String finTypeEn);
	 /**
	 * @description: 插入报销主表(重构by ahua)
	 * @author: 王中华
	 * @createDate: 2015-8-20
	 * @param insertParamsMap
	 * @return:
	*/
	Map<String, Object> insertFinanceMain(FinInsertParams insertParams);
	
	/** 
	 * @description: 更新报销主表
	 * @author: 890170
	 * @createDate: 2015-3-30
	 */
	FinanceMain updateFinanceMainByFid(FinanceMain fm);


        /**
         * @description:申请人修改环节，修改业务数据
         * @author: 王中华
         * @createDate: 2016-9-8:
         */
        void updateFinInfoByApplicant(FinInsertParams insertParams);


        /**
         * @description:修改报销出差补贴标准
         * @author: 王中华
         * @createDate: 2016-9-26
         * @param insertParams:
         */
        void updateFinAllowanceType(FinInsertParams insertParams); 

   
}

