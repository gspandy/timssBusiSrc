package com.timss.itsm.service;

import java.util.Map;

import com.timss.itsm.bean.ItsmKnowledge;
import com.yudean.itc.dto.Page;

public interface ItsmKnowledgeService {

	Map<String, Object> queryItsmKnowledgeById(int id);
	
	void updateItsmKnowledge(ItsmKnowledge itsmKnowledge);
	
	void deleteItsmKnowledge(int klId);
	
	Map<String, Object> insertItsmKnowledge(Map<String, String> addkldata) throws Exception;
	
	Page<ItsmKnowledge> queryItsmKnowledgeList(Page<ItsmKnowledge> page);

	void deleteKnowledge(int klId);
	
	void obsoleteWorkOrder(int klId);
}
