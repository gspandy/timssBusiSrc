package com.timss.ptw.service;

import java.util.List;

import com.timss.ptw.bean.PtoAttachment;

/***
 * 附件 Service
 * 
 * @author gucw 2015-7-9
 */
public interface AttachMatchService {
    /**
     * @Title:deleteAttachMatch
     * @Description:删除附件
     * @param businessId
     * @param attachId
     * @param type
     * @return void
     * @throws
     */
    public void deleteAttachMatch(String businessId, String attachId, String type);

    /**
     * @Title:insertAttachMatch
     * @Description:新建附件
     * @param businessId
     * @param fileIds
     * @param type
     * @param loadPhase
     * @throws Exception
     * @return void
     * @throws
     */
    public void insertAttachMatch(String businessId, String fileIds, String type, String loadPhase) throws Exception;
    
    /**
     * @Title:queryWoAttachmentById
     * @Description:查询附件
     * @param @param businessId
     * @param @param type
     * @param @return
     * @return List<PtoAttachment>
     * @throws
     */
    public List<PtoAttachment> queryPtoAttachmentById(String businessId, String type);

}
