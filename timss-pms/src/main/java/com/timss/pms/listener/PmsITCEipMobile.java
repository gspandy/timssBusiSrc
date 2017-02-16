package com.timss.pms.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.dao.WorkflowBusinessDao;
import com.timss.pms.service.core.WFServiceImpl;
import com.yudean.interfaces.interfaces.EipMobileInterface;
import com.yudean.itc.annotation.EipAnnotation;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamDetailBean;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamProcessBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetProcessBean;
@Service
@EipAnnotation("pms_itc")
public class PmsITCEipMobile implements EipMobileInterface{
    @Autowired
    BidResultToEipMobile bidResultToEipMobile;
    @Autowired
    CheckoutToEipMobile checkoutToEipMobile;
    @Autowired
    ContractToEipMobile contractToEipMobile;
    @Autowired
    PayToEipMobile payToEipMobile;
    @Autowired
    ProjectToEipMobile projectToEipMobile;
    @Autowired
    ReceiptToEipMobile receiptToEipMobile;
    @Autowired
	WorkflowBusinessDao workflowBusinessDao;
    @Autowired
    ContractAppToEipMobile contractAppToEipMobile;
	@Override
	public RetProcessBean processWorkflow(ParamProcessBean processBean) {
		
		EipMobileInterface eipMobileInterface=getTrueEipMobileInterface(processBean);
		RetProcessBean retProcessBean=eipMobileInterface.processWorkflow(processBean);
		return retProcessBean;
	}
	
	private EipMobileInterface getTrueEipMobileInterface(ParamProcessBean processBean){
		String flowNo = processBean.getFlowNo();
		EipMobileInterface eipMobileInterface=getTrueEipMobileInterface(flowNo);
		return eipMobileInterface;
	}
	
	private EipMobileInterface getTrueEipMobileInterface(ParamDetailBean paramDetailBean){
		String flowNo = paramDetailBean.getFlowNo();
	
		EipMobileInterface eipMobileInterface=getTrueEipMobileInterface(flowNo);
		return eipMobileInterface;
	}
	
	private EipMobileInterface getTrueEipMobileInterface(String flowNo){
		
		String businessId=workflowBusinessDao.queryBusinessIdById(flowNo);
		EipMobileInterface eipMobileInterface=null;
		if(businessId.indexOf(WFServiceImpl.bidResultPrefix)!=-1){
			eipMobileInterface=bidResultToEipMobile;
		}else if(businessId.indexOf(WFServiceImpl.checkoutPrefix)!=-1){
			eipMobileInterface=checkoutToEipMobile;
		}else if(businessId.indexOf(WFServiceImpl.contractPrefix)!=-1){
			eipMobileInterface=contractToEipMobile;
		}else if(businessId.indexOf(WFServiceImpl.payPrefix)!=-1){
			eipMobileInterface=payToEipMobile;
		}else if(businessId.indexOf(WFServiceImpl.receiptPrefix)!=-1){
			eipMobileInterface=receiptToEipMobile;
		}else if(businessId.indexOf(WFServiceImpl.projectPrefix)!=-1){
			eipMobileInterface=projectToEipMobile;
		}else if(businessId.indexOf(WFServiceImpl.contractAppPrefix)!=-1){
			eipMobileInterface=contractAppToEipMobile;
		}
		return eipMobileInterface;
	}

	@Override
	public RetContentBean retrieveWorkflowFormDetails(ParamDetailBean paramDetailBean) {
		EipMobileInterface eipMobileInterface=getTrueEipMobileInterface(paramDetailBean);
		RetContentBean retContentBean=eipMobileInterface.retrieveWorkflowFormDetails(paramDetailBean);
		return retContentBean;
	}

}
