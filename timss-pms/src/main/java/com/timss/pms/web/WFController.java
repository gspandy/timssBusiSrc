package com.timss.pms.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.timss.pms.service.WorkflowVariableUpdateService;
import com.timss.pms.util.CreateReturnMapUtil;
import com.timss.pms.util.GetBeanFromBrowerUtil;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;

@Controller
@RequestMapping("pms/wf")
public class WFController {
	@Autowired
	WorkflowVariableUpdateService wfService;
	@Autowired
	ItcMvcService itcMvcService;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/setWFVariable")
	public ModelAndViewAjax setWFVariable(String taskId,String processInstId){
		Map data=GetBeanFromBrowerUtil.getMapFromBrower("设置process" + processInstId + " 属性：", "data", itcMvcService);
		wfService.setWFVariable(taskId, processInstId, data);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "");
		return ViewUtil.Json(map);
	}
}
