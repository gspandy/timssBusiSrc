package com.timss.finance.flow.itc.managementcostapply.v001;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.facade.util.InitUserAndSiteIdNewUtil;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.service.FinanceManagementApplySpecialService;
import com.timss.finance.util.ChangeStatusUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class Apply extends TaskHandlerBase {
	
	@Autowired
	private ItcMvcService itcMvcService;
	
	@Autowired
	WorkflowService wfs; //by type
	
	@Autowired
	FinanceManagementApplySpecialService financeManagementApplySpecialService;
	
	private Logger logger = Logger.getLogger(Xzbnbhq.class);
	
	public void init(TaskInfo taskInfo) {
		
		String id = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" );
		financeManagementApplySpecialService.updateFinanceManageApplyFlowStatus(id, "apply");
		
		
	}
	
	public void onComplete(TaskInfo taskInfo) {
		UserInfoScope userInfoScope=(UserInfoScope) itcMvcService.getUserInfoScopeDatas();
		FinanceManagementApply fma=getProjectFromBrowser("管理费用申请工作流节点"+taskInfo.getTaskDefKey()+" 获取的信息");
		if(fma!=null){
			ChangeStatusUtil.changeToApprovingStatus(fma);
			InitUserAndSiteIdNewUtil.initUpdate(fma, itcMvcService);
			List<FinanceMainDetail> list=getFinanceMainDetail();
			financeManagementApplySpecialService.updateFinanceManagementApplyApproving(fma, list);
			
		}else{
			//移动端处理
			String workflowId=taskInfo.getProcessInstanceId();
			String businessId=(String) wfs.getVariable(workflowId, "businessId");
			financeManagementApplySpecialService.updateFinanceManagementApplyApproving(businessId);
		}
		
		logger.info("完成管理费用申请工作流节点"+taskInfo.getTaskDefKey()+"对信息的更新");
	}
	
	private FinanceManagementApply getProjectFromBrowser(String prefix) {
		UserInfoScope userInfoScope=(UserInfoScope) itcMvcService.getUserInfoScopeDatas();
		try{
			String fmaString=userInfoScope.getParam("businessData");
			logger.info(prefix+":"+fmaString);
			FinanceManagementApply project=null;
			if(fmaString!=null && !fmaString.equals("")){
				project=JsonHelper.fromJsonStringToBean(fmaString, FinanceManagementApply.class);
			}else{
				return null;
			}
			
			return project;
		}catch (Exception e) {
			throw new RuntimeException("管理费用申请工作流获取信息失败",e);
		}
	}
	
	private List<FinanceMainDetail> getFinanceMainDetail(){
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String businessDataString=null;
		try {
			businessDataString=userInfoScope.getParam("businessData");
		} catch (Exception e) {
			throw new RuntimeException("获取前端的businessData出错",e);
		}
		List<FinanceMainDetail> lists=null;
		JSONObject jsonObject=JSONObject.fromObject(businessDataString);
		if(!"null".equals(jsonObject.get("details").toString())){
			JSONArray jsonArray=(JSONArray) jsonObject.get("details");
			lists=fromJsonStringToList(jsonArray.toString(), FinanceMainDetail.class);
		}
		
		return lists;
	}
	
	private static <T> List<T> fromJsonStringToList(String json,Class<T> c){
		//字符串为空时，返回空对象
		if(json==null || json.equals("")){
			return null;
		}
		//将json字符串解析为jsonArray
		JSONArray jsonArray=JSONArray.fromObject(json);
		List<T> list=new ArrayList<T>();
		for(int i=0;i<jsonArray.size();i++){
			//jsonArray的每一项转换为一个bean对象
			T t = (T)JsonHelper.fromJsonStringToBean(jsonArray.getString(i), c);
			list.add(t);
		}
		
		return list;
	}
	
}
