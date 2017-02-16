package com.timss.pms.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.dao.WorkflowBusinessDao;
import com.timss.pms.listener.fordelete.ContractDraftDelete;
import com.timss.pms.listener.fordelete.IDraftDelete;
import com.yudean.homepage.bean.DeleteDraftParam;
import com.yudean.itc.annotation.HopAnnotation;
import com.yudean.itc.annotation.HopAnnotation.ProType;

@Service
public class PmsContractDelete {
	@Autowired
	WorkflowBusinessDao workflowBusinessDao;
	@Autowired
	ContractDraftDelete contractDraftDelete;
	@HopAnnotation(value = "itc_pms_contract", type = ProType.DeleteDraft, Sync=true)
    public void deleteDraft(DeleteDraftParam param){
        String flowId=param.getFlowId();
        
        String businessId=getRealBusinessId(flowId);
        IDraftDelete iDraftDelete=contractDraftDelete;
        iDraftDelete.deleteDraft(businessId);
        
        
    }
	
	private String getRealBusinessId(String businessIdWithPrifix) {
		String businessId=businessIdWithPrifix.substring(8);
		return businessId;
	}
}
