package com.timss.finance.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.timss.facade.util.CreateReturnMapUtil;
import com.timss.facade.util.GetBeanFromBrowerUtil;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.workflow.service.WorkflowService;

@Controller
@RequestMapping("fin/wf")
public class WorkFlowController {
	@Autowired
	WorkflowService workflowService;
	@Autowired
	ItcMvcService itcMvcService;
	
	@RequestMapping("/setWFVariable")
	public ModelAndViewAjax setWFVariable(String taskId,String processInstId){
		Map<String,Object> data=GetBeanFromBrowerUtil.getMapFromBrower("设置process" + processInstId + " 属性：", "data", itcMvcService);
		Map elementMap=workflowService.getElementInfo(taskId);
		if(elementMap!=null){
			String template=(String) elementMap.get("template");
			if(template!=null && !template.equals("")){
				String []tems=template.split(",");
				for(int i=0;i<tems.length;i++){
					String fieldName=tems[i];
					Object value=data.get(fieldName);
					workflowService.setVariable(processInstId, fieldName, value);
				}
			}
		}
		Map<String,Object> map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
}
