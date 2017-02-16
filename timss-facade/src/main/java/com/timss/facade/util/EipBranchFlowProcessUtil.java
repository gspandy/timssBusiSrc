package com.timss.facade.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.yudean.itc.dto.interfaces.eip.mobile.RetKeyValue;
import com.yudean.itc.dto.interfaces.eip.mobile.RetTask;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.util.ReflectionUtils;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.workflow.bean.TaskDefInfo;
import com.yudean.workflow.service.WorkflowService;

public class EipBranchFlowProcessUtil {
	Logger logger = Logger.getLogger(EipBranchFlowProcessUtil.class);
	private WorkflowService workflowService;

	public EipBranchFlowProcessUtil(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	/**
	 * 在提交到下一个节点前，先根据流程节点配置信息,设置流程信息
	 * 
	 * @Title: setFlowVariablesBeforeCommit
	 * @param processId
	 *            流程实例id
	 * @param taskDefKey
	 *            流程节点key
	 * @param businessObj 
	 *        业务对象bean(用于设置变量时，可能用到bean的属性取值，如果不需要，可设置为null)  
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean setFlowVariablesBeforeCommit(String processId,
			String taskDefKey,Object businessObj) throws Exception {
		//根据流程实例id和流程定义key获取流程节点属性
		Map<String, String> elementMap = getFlowElementProperty(processId,taskDefKey);
		// 解析流程节点信息，并设置流程变量
		if (elementMap != null) {
			String modifiable = (String) elementMap.get("modifiable");
			// 解析modifiable节点
			if (StringUtils.isNotBlank(modifiable)) {
				Map<String, Object> modifiableMaps = null;
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					modifiableMaps = objectMapper.readValue(modifiable,
							Map.class);
				} catch (Exception e) {
					throw new Exception("流程定义id为" + processId + ",taskDefKey为"
							+ taskDefKey + "的流程配置modifiable有错误", e);
				}
				// 设置流程变量
				setFlowVariablesByFlowProperty(modifiableMaps, processId,businessObj);
				// 查询流程变量是否设置正确
				isFlowVariablesCorrent(processId, taskDefKey);
			}
		}
		return true;
	}
    
	//根据流程实例id和流程定义key获取流程节点属性
	private Map<String, String> getFlowElementProperty(String processId,
			String taskDefKey) {
		// 根据流程实例id获取流程定义key
		String processDefKey = workflowService.getDefKeyByProcessInstId(processId);
		// 获取节点配置信息
		Map<String, String> elementMap = workflowService.getElementInfo(processDefKey, taskDefKey);
		return elementMap;
	}

	private void isFlowVariablesCorrent(String processId, String taskDefKey) {

	}

	// 根据流程配置设置流程变量
	private void setFlowVariablesByFlowProperty(
			Map<String, Object> modifiableMaps, String processId, Object businessObj) {
		Object setBranchVariables = modifiableMaps.get("setBranchVariables");
		// 如果setBranchVariables存在就需要处理
		if (setBranchVariables != null) {

			if (setBranchVariables instanceof Map) {
				// setBranchVariables只能为Map对象，否则不处理
				setFlowVariableByVariableMap(processId, setBranchVariables,businessObj);

			} else {
				logger.warn("流程id为" + processId
						+ "的setBranchVariables属性值不是对象,可能导致分支节点不生效");
			}
		}

	}
    
	//根据具体流程变量map，设置工作流信息
	private void setFlowVariableByVariableMap(String processId,
			Object setBranchVariables, Object businessObj) {
		Map<String, Object> setBranchVariablesMap = (Map<String, Object>) setBranchVariables;
		for (Map.Entry<String, Object> entry : setBranchVariablesMap.entrySet()) {
			String fieldName = entry.getKey();
			Object value = entry.getValue();
			if(fieldName.equals( "budget" ) && businessObj != null){
			    //查询对应的申请单的金额数量 by ahua1009
			    value = ReflectionUtils.obtainFieldValue( businessObj, fieldName );
			}
			workflowService.setVariable(processId, fieldName, value);
		}
	}
    
	/**
	 * 根据流程节点信息获取所有下一节点信息
	 * @Title: getNextElementsByFlowProperty
	 * @param processId
	 * @param currentTask
	 * @return
	 */
	public List<RetTask> getNextElementsByFlowProperty(String processId, Task currentTask
			) throws Exception {
		List<RetTask> rtList=new ArrayList<RetTask>();
		
		String taskDefKey=currentTask.getTaskDefinitionKey();
		//根据流程实例id和流程定义key获取流程节点属性
		Map<String, String> elementMap = getFlowElementProperty(processId,taskDefKey);
		Boolean isBranchElement=false;
		Map<String, Object> modifiableMaps = null;
		// 解析流程节点信息，并判断是否为分支节点
		if (elementMap != null) {
			String modifiable = (String) elementMap.get("modifiable");
			// 解析modifiable节点
			if (StringUtils.isNotBlank(modifiable)) {
				
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					modifiableMaps = objectMapper.readValue(modifiable,
							Map.class);
				} catch (Exception e) {
					throw new Exception("流程定义id为" + processId + ",taskDefKey为"
							+ taskDefKey + "的流程配置modifiable有错误", e);
				}
				//判断是否为分支节点
				isBranchElement=validBranchElement(modifiableMaps,processId);
			}
		}
		
		if(isBranchElement){
			getAllNextElementForBranch(modifiableMaps,processId,rtList,currentTask);
		}else{
			setNextLinkBean(processId, currentTask,rtList);
		}
		
		return rtList;
	}
    
	//为分支节点获取所以可能的下一节点
	private void getAllNextElementForBranch(Map<String, Object> modifiableMaps,
			String processId, List<RetTask> rtList,Task currentTask) throws Exception {
		Object branchVariables = modifiableMaps.get("branchVariables");
		// 如果setBranchVariables存在就需要处理
		if (branchVariables != null) {
			if (branchVariables instanceof Map) {
				// setBranchVariables只能为Map对象，否则不处理
				Map<String, Object> branchVariableMaps = (Map<String, Object>) branchVariables;
				//变量branchVariableMaps，获取所有可能的分支节点对应的流程变量
				for (Map.Entry<String, Object> entry : branchVariableMaps.entrySet()) {
					String fieldName = entry.getKey();
					Object value = entry.getValue();
					if(value!=null && value instanceof Map){
						//设置具体流程变量
						setFlowVariableByVariableMap(processId,value,null);
						setNextLinkBean(processId, currentTask,rtList);
					}else{
						throw new Exception("流程定义id为" + processId +  "的流程配置modifiable有错误,是流程分支节点但是变量信息branchVariables格式出错");
					}
				}
				//检查下一节点是否设置正确
                checkNextElements(rtList);
			} else {
				throw new Exception("流程定义id为" + processId +  "的流程配置modifiable有错误,是流程分支节点但是确没有配置变量信息branchVariables");
			}
		}

		
	}

	private void checkNextElements(List<RetTask> rtList) {
		// TODO Auto-generated method stub
		
	}

	//判断是否为分支节点
	private Boolean validBranchElement(Map<String, Object> modifiableMaps,String processId) {
		//默认返回false值，表示该节点不是分支节点
		Boolean result=false;
		//获取是否是分支流程判断条件
		Object isBranchElement = modifiableMaps.get("isBranchElement");
		//如果分支流程标志存在并且值为true，则该流程节点为分支流程节点
		if (isBranchElement != null) {
			if (isBranchElement instanceof Boolean) {
				result=(Boolean)isBranchElement;
			} else {
				logger.warn("流程id为 " + processId+ " 的isBranchElement属性值不是boolean对象,是否配置错误");
			}
		}
		
		return result;
	}

	// 设置下一节点信息
	public void setNextLinkBean(String processId, Task task,
			List<RetTask> rtList) {
		// 获取下一节点定义id
		List<String> nkeyList = workflowService.getNextTaskDefKeys(processId,task.getTaskDefinitionKey());
		if (null != nkeyList && nkeyList.size() > 0) {
			for (String nextKey : nkeyList) {
				RetTask rt = new RetTask();
				// 找到下一个节点的具体信息
				ProcessInstance pi = workflowService.getProcessByProcessInstId(processId);
				TaskDefInfo tdi = workflowService.getTaskDefInfoByTaskDefKey(pi.getProcessDefinitionId(), nextKey);
				rt.setTask(new RetKeyValue(tdi.getDefKey(), tdi.getName()));

				// 找到下一个节点的人员信息
				List<RetKeyValue> rkvList = new ArrayList<RetKeyValue>();
				List<SecureUser> suList = workflowService.selectUsersByTaskKey(processId, nextKey, task.getId());
				for (SecureUser su : suList) {
					rkvList.add(new RetKeyValue(su.getId(), su.getName()));
				}
				rt.setUser(rkvList);
				rtList.add(rt);
			}
		} else {
			RetTask rt = new RetTask();
			rt.setTask(new RetKeyValue("end", "审批结束"));
			rt.setUser(new ArrayList<RetKeyValue>());
			rtList.add(rt);
		}
	}
}
