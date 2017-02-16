package com.timss.finance.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;

import com.timss.finance.vo.FinanceManagementApplyDtlVo;
import com.timss.finance.vo.FinanceManagementPayDtlVo;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

public class ParsePrivilegeUtil {
	public static Map<String,Object> getPrivilegeMap(String propertiesName,String siteid){
		Properties properties=PropertyReaderUtil.readProperties(propertiesName);
		return parsePropertiesAndGetPrivilege(properties,siteid);
	}

	private static Map<String, Object> parsePropertiesAndGetPrivilege(
			Properties properties,String siteid) {
		
		return null;
	}

	public static Map<String, Object> getFMAPrivilegeMap(
			FinanceManagementApplyDtlVo financeManagementApplyDtlVo,UserInfo userInfo,WorkflowService workflowService) {
		Map<String,Object> object=new HashMap<String, Object>();
		
		Map<String, Object> buttonMap=new HashMap<String, Object>();
		object.put("buttons", buttonMap);
		
		String status=financeManagementApplyDtlVo.getStatus();
		String currentUserId=userInfo.getUserId();
		if(StatusEnum.DRAFT.toString().equals(status)){
			if(currentUserId.equals(financeManagementApplyDtlVo.getCreateuser())){
				buttonMap.put("fin-submit", true);
				buttonMap.put("fin-del", true);
				object.put("editDetailList", "Y");
				object.put("attach", "Y");
				object.put("formEdit","all");
			}
		}else if(StatusEnum.APPROVING.toString().equals(status)){
			String processInstId=financeManagementApplyDtlVo.getProInstId();
			if(StringUtils.isNotBlank(processInstId)){
				
				List<Task> tasks=workflowService.getActiveTasks(processInstId);
				String taskId=tasks.get(0).getId();
				List<String> users=workflowService.getCandidateUsers(taskId);
				object.put("taskId",taskId);
				object.put("processInstId", processInstId);
				if(inStringList(currentUserId, users)){
					buttonMap.put("fin-shenpi", true);
					//获取节点信息
					Map<String, String> workflowMap=workflowService.getElementInfo(taskId);
					String modifiable=workflowMap.get("modifiable");
					if(StringUtils.isNotBlank(modifiable)){
						Map<String, Object> modifiableMap=JsonHelper.fromJsonStringToBean(modifiable, Map.class);
						if("Y".equals(modifiableMap.get("attach"))){
							object.put("attach", "Y");
						}
						if("Y".equals(modifiableMap.get("editDetailList"))){
							object.put("editDetailList", "Y");
						}
						if("Y".equals(modifiableMap.get("zuofei"))){
							buttonMap.put("fin-zuofei", true);
						}
						if(StringUtils.isNotBlank((String) modifiableMap.get("formEdit"))){
							object.put("formEdit", modifiableMap.get("formEdit"));
						}
						if("Y".equals(modifiableMap.get("needZJL"))){
							object.put("needZJL", true);
						}
						if("Y".equals(modifiableMap.get("needHQ"))){
							object.put("needHQ", true);
						}
					}
					
				}
			}
		}else if(StatusEnum.APPROVED.toString().equals(status)){
			String processInstId=financeManagementApplyDtlVo.getProInstId();
			if(StringUtils.isNotBlank(processInstId)){
				object.put("processInstId", processInstId);
			}
		}
		return object;
	}
	
	private static  boolean inStringList(String string,List<String> list){
		boolean result=false;
		if(list!=null && string!=null){
			for(int i=0;i<list.size();i++){
				if(list.get(i).equals(string)){
					result=true;
					break;
				}
			}
		}
		return result;
	}
	/**
	 * 获取行政报销的权限属性(后期可考虑放到前端)
	 * @Title:getFMPPrivilegeMap
	 * @Description:TODO
	 * @param fmpDtlVo 行政报销对象
	 * @param workflowMap 
	 * @param itcMvcService
	 * @return Map<String,Object> 按钮权限以及表单是否可编辑状态信息
	 */
	public static Map<String, Object> getFMPPrivilegeMap(
			FinanceManagementPayDtlVo fmpDtlVo,Map<String,Object> workflowMap,ItcMvcService itcMvcService) {
		Map<String,Object> map=new HashMap<String, Object>();
		Map<String,Object> buttonMap=new HashMap<String, Object>();
		String userId=itcMvcService.getUserInfoScopeDatas().getUserId();
		String status=fmpDtlVo.getStatus();
		//初始化设为只读模式
		map.put("readOnly",true);
		if("draft".equals(status)){
			if(userId.equals(fmpDtlVo.getCreateuser())){
				//设为编辑模式
				map.put("readOnly",false);
				//关闭按钮(编辑模式)
				buttonMap.put("fin-close-edit",true);
				//暂存按钮
				buttonMap.put("fin-tmpSave",true);
				//提交按钮
				buttonMap.put("fin-submit",true);
				//删除按钮
				buttonMap.put("fin-del", true);
			}else {
				//关闭按钮(只读模式)
				buttonMap.put("fin-close-readOnly",true);
			}
		}else {
			//关闭按钮(只读模式)
			buttonMap.put("fin-close-readOnly",true);
			//流程信息按钮
			buttonMap.put("fin-workflow-show", true);
			if("approving".equals(status)){
				//如果是审核中，且当前用户为当前流程节点的候选人时，需要通知前端，并赋予显示审批按钮的权限
				if("true".equals(workflowMap.get("isCandidate"))){
					//获取节点信息(更优的做法是把buttonMap的值设置到modifiable中)
					String modifiable=(null==workflowMap.get("modifiable"))?"":workflowMap.get("modifiable").toString();
					Map<String, Object> modifiableMap=JsonHelper.fromJsonStringToBean(modifiable, HashMap.class);
					//记录为审批状态
					workflowMap.put("approve", true);
					//审批按钮(普通环节)
					buttonMap.put("fin-approve", true);
					if(null!=modifiableMap&&"Y".equals(modifiableMap.get("isApply"))){
						//退回第一环节时
						//审批按钮(普通环节)
						buttonMap.put("fin-approve", false);
						//作废按钮
						buttonMap.put("fin-workflow-del", true);
						//审批按钮(被退回第一环节，需要保存页面表单)
						buttonMap.put("fin-approve-rvk", true);
						map.put("readOnly",false);
					}else if(null!=modifiableMap&&"Y".equals(modifiableMap.get("isSubmitPaperMeterial"))){
						//提交纸质环节时
						//审批按钮(普通环节)
						buttonMap.put("fin-approve", false);
						//打印按钮
						buttonMap.put("fin-print", true);
						//审批按钮(提交纸质环节，需要保存页面表单)
						buttonMap.put("fin-approve-submitPaperMaterial", true);
					}
				}
			}
		}
		map.put("buttonPriv", buttonMap);
		return map;
	}
}
