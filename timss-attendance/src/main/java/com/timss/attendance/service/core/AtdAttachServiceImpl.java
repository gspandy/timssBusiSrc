package com.timss.attendance.service.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.timss.attendance.dao.AtdAttachDao;
import com.timss.attendance.service.AtdAttachService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;

@Service
public class AtdAttachServiceImpl implements AtdAttachService {
    @Autowired
    private AtdAttachDao atdAttachDao;
    @Autowired
    private AttachmentMapper attachmentMapper;
    static Logger logger = Logger.getLogger( AtdAttachServiceImpl.class );
	
	@Override
	public List<Map<String, Object>> queryAll(String itemType,
			String itemId) throws Exception {
		List<String> ids = atdAttachDao.queryAll( itemType,itemId );
		List<Map<String, Object>> fileMap = new ArrayList<Map<String,Object>>();
        if ( ids != null && !ids.isEmpty() ) {
            // 文件附件MAP
            fileMap = FileUploadUtil.getJsonFileList( Constant.basePath, ids );
        }
        return fileMap;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public Integer insert(String itemType, String itemId, String[] attachIds)
			throws Exception {
		Integer result=atdAttachDao.insert(itemType, itemId, attachIds);
		attachmentMapper.setAttachmentsBinded( attachIds, 1 );
		logger.info("insert atd attach->type:"+itemType+" id:"+itemId+" attachIds:"+attachIds.toString()+" result:"+result);
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public Integer delete(String itemType, String itemId, String[] attachIds)
			throws Exception {
		Integer result=atdAttachDao.delete(itemType, itemId, attachIds);
		logger.info("delete atd attach->type:"+itemType+" id:"+itemId+" attachIds:"+(attachIds==null?0:attachIds.toString())+" result:"+result);
		return result;
	}    
	
}