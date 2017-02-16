package com.timss.pms.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.dao.WorkflowBusinessDao;
import com.timss.pms.listener.fordelete.BidResultDraftDelete;
import com.timss.pms.listener.fordelete.CheckoutDraftDelete;
import com.timss.pms.listener.fordelete.ContractChangeDraftDelete;
import com.timss.pms.listener.fordelete.IDraftDelete;
import com.timss.pms.listener.fordelete.PayDraftDelete;
import com.timss.pms.listener.fordelete.ProjectDraftDelete;
import com.timss.pms.service.core.WFServiceImpl;
import com.yudean.homepage.bean.DeleteDraftParam;
import com.yudean.itc.annotation.HopAnnotation;
import com.yudean.itc.annotation.HopAnnotation.ProType;
@Service
public class PmsITCDelete {
	@Autowired
	WorkflowBusinessDao workflowBusinessDao;
	@Autowired
	ProjectDraftDelete projectDraftDelete;
	@Autowired
	BidResultDraftDelete bidResultDraftDelete;
	@Autowired
	CheckoutDraftDelete checkoutDraftDelete;
	@Autowired
	ContractChangeDraftDelete changeDraftDelete;
	@Autowired
	PayDraftDelete payDraftDelete;
	@HopAnnotation(value = "itc_pms", type = ProType.DeleteDraft, Sync=true)
    public void deleteDraft(DeleteDraftParam param){
        String flowId=param.getFlowId();
        String businessIdWithPrifix=workflowBusinessDao.queryBusinessIdById(flowId);
        String businessId=getRealBusinessId(businessIdWithPrifix);
        IDraftDelete iDraftDelete=getIDraftDelete(businessIdWithPrifix);
        iDraftDelete.deleteDraft(businessId);
        
        
    }
	private IDraftDelete getIDraftDelete(String businessIdWithPrifix) {
		if(businessIdWithPrifix.indexOf(WFServiceImpl.projectPrefix)!=-1){
			return projectDraftDelete;
		}else if(businessIdWithPrifix.indexOf(WFServiceImpl.bidResultPrefix)!=-1){
			return bidResultDraftDelete;
		}else if(businessIdWithPrifix.indexOf(WFServiceImpl.checkoutPrefix)!=-1){
			return checkoutDraftDelete;
		}else if(businessIdWithPrifix.indexOf(WFServiceImpl.payPrefix)!=-1 || businessIdWithPrifix.indexOf(WFServiceImpl.receiptPrefix)!=-1){
			return payDraftDelete;
		}else if(businessIdWithPrifix.indexOf(WFServiceImpl.contractPrefix)!=-1 ){
			return changeDraftDelete;
		}
		return null;
	}
	private String getRealBusinessId(String businessIdWithPrifix) {
		String businessId=businessIdWithPrifix.substring(businessIdWithPrifix.lastIndexOf("_")+1);
		return businessId;
	}
}
