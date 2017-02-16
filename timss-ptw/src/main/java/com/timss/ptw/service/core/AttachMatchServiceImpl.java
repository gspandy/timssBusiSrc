package com.timss.ptw.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.ptw.bean.PtoAttachment;
import com.timss.ptw.dao.AttachMatchDao;
import com.timss.ptw.service.AttachMatchService;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class AttachMatchServiceImpl implements AttachMatchService {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private HomepageService homepageService;
    @Autowired
    AttachmentMapper attachmentMapper;
    @Autowired
    private AttachMatchDao attachMatchDao;

    // private static final Logger LOG = Logger.getLogger(
    // AttachMatchServiceImpl.class );

    @Override
    public void deleteAttachMatch(String businessId, String attachId, String type) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        List<PtoAttachment> oldAttachmentList = queryPtoAttachmentById( businessId, type );
        ArrayList<String> oldIds = new ArrayList<String>();
        for ( int i = 0; i < oldAttachmentList.size(); i++ ) {
            oldIds.add( oldAttachmentList.get( i ).getAttachId() );
        }
        // 删掉附件
        for ( int i = 0; i < oldIds.size(); i++ ) {
            deleteWoAttachment( businessId, oldIds.get( i ), type, userId );
        }
        String[] tmp = new String[] {};
        if(tmp.length>0){
            attachmentMapper.setAttachmentsBinded( oldIds.toArray( tmp ), 0 );
        }
    }

    @Override
    public void insertAttachMatch(String businessId, String fileIds, String type, String loadPhase) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        String userId = userInfoScope.getUserId();
        if ( "".equals( fileIds ) ) {
            fileIds = null;
        }
        if ( fileIds != null && fileIds != "" ) {
            String[] ids = fileIds.split( "," );
            ArrayList<String> newIds = new ArrayList<String>(); // 新的附件ids
            for ( int i = 0; i < ids.length; i++ ) {
                newIds.add( ids[i] );
            }
            for ( int i = 0; i < newIds.size(); i++ ) {

                PtoAttachment ptoAttachment = new PtoAttachment();
                ptoAttachment.setId( businessId );
                ptoAttachment.setType( type );
                ptoAttachment.setAttachId( newIds.get( i ) );
                ptoAttachment.setLoadPhase( loadPhase );
                ptoAttachment.setLoadTime( new Date() );
                ptoAttachment.setLoadUser( userId );
                ptoAttachment.setSiteid( siteId );
                ptoAttachment.setDelFlag( "N" );
                insertWoAttachment( ptoAttachment );
            }
            attachmentMapper.setAttachmentsBinded( ids, 1 );
        }
    }

    @Override
    public List<PtoAttachment> queryPtoAttachmentById(String businessId, String type) {
        List<PtoAttachment> result = new ArrayList<PtoAttachment>( 0 );
        result = attachMatchDao.queryAttachMatchById( businessId, type, null );
        return result;
    }

    private int insertWoAttachment(PtoAttachment ptoAttachment) {
        int result = 0;
        result = attachMatchDao.insertAttachMatch( ptoAttachment );
        return result;
    }

    private int deleteWoAttachment(String businessId, String id, String type, String userId) {
        int result = 0;
        result = attachMatchDao.deleteAttachMatch( businessId, id, type, userId );
        return result;
    }
}
