package com.timss.itsm.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.itsm.bean.ItsmAttachment;
import com.timss.itsm.bean.ItsmWoAttachment;
import com.timss.itsm.dao.ItsmAttachmentDao;
import com.timss.itsm.dao.ItsmWoAttachmentDao;
import com.timss.itsm.service.ItsmAttachmentService;
import com.timss.itsm.service.ItsmWoAttachmentService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class ItsmAttachmentServiceImpl implements ItsmAttachmentService {
	@Autowired
	ItsmAttachmentDao attachmentDao;
	
	@Override
	public List<ItsmAttachment> queryAttachmentById(String id, String type) {
		return attachmentDao.queryAttachmentById(id,type,null);
	}

	@Override
	public void insertAttachment(ItsmAttachment attachment) {
		attachmentDao.insertAttachment(attachment);
		
	}

	@Override
	public void deleteAttachment(String businessId, String removeId,
			String type, String userId) {
		attachmentDao.deleteAttachment(businessId, removeId, type, userId);
	}
}
