package com.timss.workorder.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.workorder.service.WorkOrderService;
import com.yudean.homepage.bean.DeleteDraftParam;
import com.yudean.itc.annotation.HopAnnotation;
import com.yudean.itc.annotation.HopAnnotation.ProType;

@Component
public class ModuleDeleteDraftListener {

	@Autowired
	WorkOrderService workOrderService;
	
	  @HopAnnotation(value = "workOrder", type = ProType.DeleteDraft, Sync=true)
      public void deleteDraft(DeleteDraftParam param){
		  String flowId = param.getFlowId();
		  String siteid = param.getSiteid();
		  
		  workOrderService.deleteWorkOrderByWoCode(flowId, siteid);
      }
}
