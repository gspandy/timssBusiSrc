package com.timss.itsm.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.itsm.bean.ItsmWoAttachment;
import com.timss.itsm.dao.ItsmWoAttachmentDao;
import com.timss.itsm.service.ItsmWoAttachmentService;

@Service
public class ItsmWoAttachmentServiceImpl implements ItsmWoAttachmentService {
	@Autowired
	ItsmWoAttachmentDao woAttachmentDao;
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor ={Exception.class})
	public void insertWoAttachment(ItsmWoAttachment woAttachment) {
		woAttachmentDao.insertWoAttachment(woAttachment);
	}

	@Override
	public List<ItsmWoAttachment> queryWoAttachmentById(String id, String type) {
		return woAttachmentDao.queryWoAttachmentById(id,type,null);
	}

	@Override
	public void deleteWoAttachment(String businessId, String removeId, String type,
			String userId) {
		woAttachmentDao.deleteWoAttachment(businessId, removeId, type,userId);
		
	}
	
}
