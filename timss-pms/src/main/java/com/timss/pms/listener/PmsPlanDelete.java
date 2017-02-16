package com.timss.pms.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.dao.WorkflowBusinessDao;
import com.timss.pms.listener.fordelete.IDraftDelete;
import com.timss.pms.listener.fordelete.PlanDraftDelete;
import com.yudean.homepage.bean.DeleteDraftParam;
import com.yudean.itc.annotation.HopAnnotation;
import com.yudean.itc.annotation.HopAnnotation.ProType;

@Service
public class PmsPlanDelete {
	@Autowired
	WorkflowBusinessDao workflowBusinessDao;
	@Autowired
	PlanDraftDelete planDraftDelete;
	@HopAnnotation(value = "itc_pms_plan", type = ProType.DeleteDraft, Sync=true)
    public void deleteDraft(DeleteDraftParam param){
        String flowId=param.getFlowId();
      
        String businessId=getRealBusinessId(flowId);
        IDraftDelete iDraftDelete=planDraftDelete;
        iDraftDelete.deleteDraft(businessId);
        
        
    }
	
	private String getRealBusinessId(String businessIdWithPrifix) {
		String businessId=businessIdWithPrifix.substring(4);
		return businessId;
	}
}
