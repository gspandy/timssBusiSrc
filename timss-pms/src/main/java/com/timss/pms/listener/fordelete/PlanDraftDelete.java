package com.timss.pms.listener.fordelete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.service.PlanService;

@Service
public class PlanDraftDelete implements IDraftDelete{
    @Autowired
    PlanService planService;
	@Override
	public void deleteDraft(String id) {
		planService.deletePlan(id);
		
	}

}
