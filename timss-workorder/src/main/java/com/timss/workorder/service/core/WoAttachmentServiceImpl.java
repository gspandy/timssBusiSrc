package com.timss.workorder.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.bean.WoAttachment;
import com.timss.workorder.dao.WoAttachmentDao;
import com.timss.workorder.service.WoAttachmentService;

@Service
public class WoAttachmentServiceImpl implements WoAttachmentService {
	@Autowired
	WoAttachmentDao woAttachmentDao;
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void insertWoAttachment(WoAttachment woAttachment) {
		woAttachmentDao.insertWoAttachment(woAttachment);
	}

	@Override
	public List<WoAttachment> queryWoAttachmentById(String id, String type) {
		return woAttachmentDao.queryWoAttachmentById(id,type,null);
	}
	
}
