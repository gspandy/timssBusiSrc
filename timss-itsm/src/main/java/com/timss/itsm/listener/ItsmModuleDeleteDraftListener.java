package com.timss.itsm.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.itsm.service.ItsmWorkOrderService;
import com.yudean.homepage.bean.DeleteDraftParam;
import com.yudean.itc.annotation.HopAnnotation;
import com.yudean.itc.annotation.HopAnnotation.ProType;

@Component
public class ItsmModuleDeleteDraftListener {

    @Autowired
    ItsmWorkOrderService workOrderService;

    @HopAnnotation(value = "itsm", type = ProType.DeleteDraft, Sync = true)
    public void deleteDraft(DeleteDraftParam param) {
        String flowId = param.getFlowId();
        String siteid = param.getSiteid();

        workOrderService.deleteWorkOrderByWoCode( flowId, siteid );
    }
}
