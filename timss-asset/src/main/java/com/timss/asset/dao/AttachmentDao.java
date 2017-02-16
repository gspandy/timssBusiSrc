package com.timss.asset.dao;

import java.util.List;

import com.timss.asset.bean.AssetAttachBean;
import com.timss.asset.bean.AttachmentBean;

/**
 * @title: 附件的Dao
 * @description: {desc}
 * @company: gdyd
 * @className: AttachmentDao.java
 * @author: 890165
 * @createDate: 2014-7-3
 * @updateUser: 890165
 * @version: 1.0
 */
public interface AttachmentDao {
    /**
     * 
     * @description:插入附件id关联数据
     * @author: 890165
     * @createDate: 2014-7-3
     * @param assetId
     * @param uploadId
     * @param siteId
     * @return:
     */
    int insertAttachment(String assetId,String uploadId,String siteId);
    
    /**
     * 
     * @description:根据assetid和site查找ast_attach_match表中的attachid 
     * @author: 890165
     * @createDate: 2014-7-3
     * @param assetId
     * @param site
     * @return:
     */
    List<AssetAttachBean>  queryAssetAttach(String assetId,String site);
    
    /**
     * 
     * @description:根据atch_id查找b_attachment表中的值 
     * @author: 890165
     * @createDate: 2014-7-4
     * @param attachId
     * @return:
     */
    AttachmentBean queryAttachment(String attachId);
    
    /**
     * 根据assetid和site删除ast_attach_match表中的数据
     * @description:
     * @author: 890165
     * @createDate: 2014-7-4
     * @param assetId
     * @param site
     * @return:
     */
    int deleteAssetAttach(String assetId,String site);
}
