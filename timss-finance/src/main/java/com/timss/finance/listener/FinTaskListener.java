package com.timss.finance.listener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.finance.service.FinanceMainService;
import com.yudean.homepage.bean.DeleteDraftParam;
import com.yudean.itc.annotation.HopAnnotation;
import com.yudean.itc.annotation.HopAnnotation.ProType;

/** 
 * @description: 任务监听
 * @author: 890170
 * @createDate: 2014-12-23
 */
@Component
public class FinTaskListener {
	private Logger logger = Logger.getLogger(FinTaskListener.class);
	
	@Autowired
	private FinanceMainService financeMainService;
	
	/** 
	 * @description: 删除待办栏里的草稿
	 * @author: 890170
	 * @createDate: 2014-12-23
	 */
	@HopAnnotation(value="finance", type=ProType.DeleteDraft, Sync=true)
	public void deleteTodoDraft(DeleteDraftParam param) {
		
//		String flowno=param.getFlowId();
		logger.info("报销单编号: " + param.getFlowId()); //报销单编号
		logger.info("报销单名称: " + param.getName()); //报销单名称
		logger.info("流程实例编号: " + param.getProcessInsId()); //流程实例编号
		logger.info("站点编号: " + param.getSiteid()); //站点编号
		
		//逻辑删除对应在模块页面显示的的报销单
//		String result = financeMainService.deleteAndUpdateFinanceMain(param.getFlowId());
		financeMainService.deleteAndUpdateFinanceMain(param.getFlowId());
			
		return;
	}
}
