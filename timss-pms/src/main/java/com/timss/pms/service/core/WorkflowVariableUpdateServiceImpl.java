package com.timss.pms.service.core;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.service.WorkflowVariableUpdateService;
import com.yudean.workflow.service.WorkflowService;

@Service
public class WorkflowVariableUpdateServiceImpl implements WorkflowVariableUpdateService{

	@Autowired
	WorkflowService workflowService;
	@Override
	@Transactional
	public boolean setWFVariable(String taskId, String processInstId, Map data) {
		Map<String,String> elementMap=workflowService.getElementInfo(taskId);
		if(elementMap!=null){
			String template=(String) elementMap.get("template");
			if(template!=null && !template.equals("")){
				//对template进行解析, 其基本格式为  value1,value2->variableName 
				String []tems=template.split(",");
				for(int i=0;i<tems.length;i++){
					String curValue=tems[i];
					String [] keyValue=curValue.split("->");
					
					String fieldName=keyValue[0];
					String variableName=fieldName;
					if(keyValue.length>1){
						variableName=keyValue[1];
					}
					
					Object value=data.get(fieldName);
					workflowService.setVariable(processInstId, variableName, value);
				}
			}
		}
		return true;
	}

}
