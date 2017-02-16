package com.timss.asset.service.core;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.AssetAttachBean;
import com.timss.asset.bean.AttachmentBean;
import com.timss.asset.dao.AttachmentDao;
import com.timss.asset.service.AttachmentService;
import com.yudean.itc.dao.support.AttachmentMapper;

/**
 * @title: 附件的service实现
 * @description: {desc}
 * @company: gdyd
 * @className: AttachmentServiceImpl.java
 * @author: 890165
 * @createDate: 2014-7-3
 * @updateUser: 890165
 * @version: 1.0
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {
    @Autowired
    private AttachmentDao attachmentDao;
    @Autowired
    private AttachmentMapper attachmentMapper;
    static Logger logger = Logger.getLogger( AttachmentServiceImpl.class );

    @Transactional(propagation = Propagation.REQUIRED)
    public int insertAttachment(String assetId, String uploadIds, String siteId) throws Exception {
        int count = 0;
        if(uploadIds==null||"".equals( uploadIds )){
            return count;
        }
        String[] uploadId = uploadIds.split( "," );
        logger.info( "----"+uploadIds+"----length-----"+uploadId.length);
        for ( int i = 0; i < uploadId.length; i++ ) {
            // 是否应该添加是否存在关联附件的判断？
            count += attachmentDao.insertAttachment( assetId, uploadId[i], siteId );
        }
        attachmentMapper.setAttachmentsBinded( uploadId, 1 ); //存于attachment中，避免2小时后删除
        return count;
    }
    
    public String queryAttachmentInfo(String assetId,String site) throws Exception{
        String jsonString = "[]";
        List<AssetAttachBean> list = attachmentDao.queryAssetAttach( assetId, site );
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(int i = 0;i<list.size();i++){
            AssetAttachBean assetAttachBean = list.get( i );
            AttachmentBean attachmentBean = attachmentDao.queryAttachment( assetAttachBean.getAttachId() );
            jsonObject.put( "fileID", attachmentBean.getFileID() );
            jsonObject.put( "fileName", attachmentBean.getFileName() );
            jsonObject.put( "fileSize", attachmentBean.getFileSize() );
            jsonArray.add( jsonObject );
        }
        jsonString = jsonArray.toString();
        return jsonString;
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteAssetAttach(String assetId,String site) throws Exception{
        return attachmentDao.deleteAssetAttach( assetId, site );
    }
}
