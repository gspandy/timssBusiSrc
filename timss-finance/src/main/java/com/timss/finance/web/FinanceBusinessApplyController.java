package com.timss.finance.web;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.timss.facade.util.CreateReturnMapUtil;
import com.timss.facade.util.GetBeanFromBrowerUtil;
import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.service.FinanceManagementApplyService;
import com.timss.finance.service.FinanceManagementApplySpecialService;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.workflow.service.WorkflowService;

@Controller
@RequestMapping(value = "finance/fBusinessApply")
public class FinanceBusinessApplyController {
    @Autowired
    FinanceManagementApplyService financeManagementApplyService;
    @Autowired
    FinanceManagementApplySpecialService finaceManagementApplySpecialService;
    @Autowired
    ItcMvcService itcMvcService;
    @Autowired
    WorkflowService workflowService;
    
    Logger logger = Logger.getLogger( FinanceBusinessApplyController.class );

    @RequestMapping("/tmpInsertFinBusinessApply")
    @ValidFormToken
    public ModelAndViewAjax tmpInsertFinanceTravelApply() throws Exception {
        FinanceManagementApply financeManagementApply = GetBeanFromBrowerUtil.getBeanFromBrower( "暂存时获取行政报销详细信息",
                "financeBusinessApply", FinanceManagementApply.class, itcMvcService );

        String id = finaceManagementApplySpecialService.tmpInsertFinanceManagementApply( financeManagementApply, null );
        @SuppressWarnings("unchecked")
        Map<String, Object> map = CreateReturnMapUtil.createMap( CreateReturnMapUtil.SUCCESS_FLAG, "", id );
        return ViewUtil.Json( map );
    }

    @RequestMapping("/insertFinBusinessApplyAndStartWorkflow")
    @ValidFormToken
    public ModelAndViewAjax insertFinanceTravelApplyAndStartWorkflow() throws Exception {

        FinanceManagementApply financeManagementApply = GetBeanFromBrowerUtil.getBeanFromBrower( "",
                "financeBusinessApply", FinanceManagementApply.class, itcMvcService );

        Map<String, Object> data = finaceManagementApplySpecialService.insertFinanceManagementApplyAndStartWorkflow(
                financeManagementApply, null );

        @SuppressWarnings("unchecked")
        Map<String, Object> map = CreateReturnMapUtil.createMap( CreateReturnMapUtil.SUCCESS_FLAG, "", data );
        return ViewUtil.Json( map );
    }

    @RequestMapping("/updateFinBusinessApplyAndStartWorkflow")
    @ValidFormToken
    public ModelAndViewAjax updateFinanceTravelApplyAndStartWorkflow() throws Exception {
       
        FinanceManagementApply financeManagementApply = GetBeanFromBrowerUtil.getBeanFromBrower( "",
                "financeBusinessApply", FinanceManagementApply.class, itcMvcService );
         
        Map<String, Object> data = finaceManagementApplySpecialService.updateFinanceManagementApplyAndStartWorkflow(
                financeManagementApply, null );

        @SuppressWarnings("unchecked")
        Map<String, Object> map = CreateReturnMapUtil.createMap( CreateReturnMapUtil.SUCCESS_FLAG, "", data );
        return ViewUtil.Json( map );
    }

    @RequestMapping("/tmpUpdateFinBusinessApply")
    public ModelAndViewAjax tmpUpdateFinanceTravelApply() throws Exception {
        FinanceManagementApply financeManagementApply = GetBeanFromBrowerUtil.getBeanFromBrower( "",
                "financeBusinessApply", FinanceManagementApply.class, itcMvcService );
        
        String id = finaceManagementApplySpecialService.tmpUpdateFinanceManagementApply( financeManagementApply,
                null );

        @SuppressWarnings("unchecked")
        Map<String, Object> map = CreateReturnMapUtil.createMap( CreateReturnMapUtil.SUCCESS_FLAG, "", id );
        return ViewUtil.Json( map );
    }


   
}
