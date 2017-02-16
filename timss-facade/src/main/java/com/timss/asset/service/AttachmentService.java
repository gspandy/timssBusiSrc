package com.timss.asset.service;


/**
 * @title: 附件的Service
 * @description: {desc}
 * @company: gdyd
 * @className: AttachmentService.java
 * @author: 890165
 * @createDate: 2014-7-3
 * @updateUser: 890165
 * @version: 1.0
 */
public interface AttachmentService {
    
    
    /**
     * 
     * @description:插入数据到AST_ATTACH_MATCH用于关联附件
     * @author: 890165
     * @createDate: 2014-7-3
     * @param assetId
     * @param uploadIds
     * @return 插入数据的行数
     * @throws Exception:
     */
    int insertAttachment(String assetId,String uploadIds,String siteId) throws Exception;
    
    /**
     * 
     * @description:获取assetId的附件，返回Json格式的字符串
     * @author: 890165
     * @createDate: 2014-7-3
     * @param assetId
     * @return
     * @throws Exception:
     */
    String queryAttachmentInfo(String assetId,String site) throws Exception;
    
    /**
     * 
     * @description:根据assetid和site删除ast_attach_match表中的数据
     * @author: 890165
     * @createDate: 2014-7-4
     * @param assetId
     * @param site
     * @return
     * @throws Exception:
     */
    int deleteAssetAttach(String assetId,String site) throws Exception;
}
