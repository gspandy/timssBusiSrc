package com.timss.pms.listener.fordelete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.service.BidResultService;

@Service
public class BidResultDraftDelete implements IDraftDelete{
    @Autowired
    BidResultService bidResultService;
	@Override
	public void deleteDraft(String id) {
		bidResultService.deleteBidResult(Integer.valueOf(id));
		
	}

}
