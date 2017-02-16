package com.timss.ptw.service.core;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.ptw.bean.PtwFireInfo;
import com.timss.ptw.dao.PtwFireInfoDao;
import com.timss.ptw.service.PtwFireInfoService;
import com.timss.ptw.service.PtwInfoService;
@Service
public class PtwFireInfoServiceImpl implements PtwFireInfoService {
    
    private static final Logger log = Logger.getLogger(PtwInfoService.class);
    
    @Autowired
    private PtwFireInfoDao ptwFireInfoDao;    
    
    @Override
    public PtwFireInfo queryPtwFireInfoByWtId(int wtId) {
        return ptwFireInfoDao.queryPtwFireInfoByWtId( wtId );
    }
    
    @Override
    public List<Integer> queryFireIdsByAttachWtId(int attachWtId) {
        return ptwFireInfoDao.queryFireIdsByAttachWtId( attachWtId );
    }

    @Override
    public int insertPtwFireBaseInfo(PtwFireInfo ptwFireInfo) {
    	log.info("准备插入动火票的信息:"+ptwFireInfo.toString());
        return ptwFireInfoDao.insertPtwFireBaseInfo( ptwFireInfo );
    }

    @Override
    public int updatePtwFireBaseInfo(PtwFireInfo ptwFireInfo) {
        return ptwFireInfoDao.updatePtwFireBaseInfo( ptwFireInfo );
    }

    @Override
    public int updatePtwFireFlowApprInfo(PtwFireInfo ptwFireInfo) {
        return ptwFireInfoDao.updatePtwFireFlowApprInfo( ptwFireInfo );
    }

    @Override
    public int updatePtwFireFlowConfirmInfo(PtwFireInfo ptwFireInfo) {
        return ptwFireInfoDao.updatePtwFireFlowConfirmInfo( ptwFireInfo );
    }

    @Override
    public int updatePtwFireFlowFinInfo(PtwFireInfo ptwFireInfo) {
        return ptwFireInfoDao.updatePtwFireFlowFinInfo( ptwFireInfo );
    }

    @Override
    public int updatePtwFirePic(PtwFireInfo ptwFireInfo) {
        return ptwFireInfoDao.updatePtwFirePic( ptwFireInfo );
    }
    
    private int updatePtwFireAuditInfo(int wtId,String columnName,String userId,String userName,Date date){
    	int updated = ptwFireInfoDao.updatePtwFireAuditInfo( wtId,columnName+"No", columnName, columnName+"Time", userId, userName, date );
    	return updated;
    }

    @Override
    public int updatePtwFireIssueAj(int wtId,String userId, String userName, Date date) {
        String columnName = "appvAj";
        return updatePtwFireAuditInfo(wtId, columnName, userId, userName, date);
    }
    
    @Override
    public int updatePtwFireIssueGuard(int wtId,String userId, String userName, Date date) {
        String columnName = "appvSecuCo";
        return updatePtwFireAuditInfo(wtId, columnName, userId, userName, date);
    }

    @Override
    public int updatePtwFireIssueCj(int wtId, String userId, String userName, Date date) {
        String columnName = "appvCj";
        return updatePtwFireAuditInfo(wtId, columnName, userId, userName, date);
    }
    
    @Override
    public int updatePtwFireIssueBm(int wtId, String userId, String userName, Date date) {
        String columnName = "appvBm";
        return updatePtwFireAuditInfo(wtId, columnName, userId, userName, date);
    }
    
    @Override
    public int updatePtwFireIssueXf(int wtId, String userId, String userName, Date date) {
        String columnName = "appvXf";
        return updatePtwFireAuditInfo(wtId, columnName, userId, userName, date);
    }

    @Override
    public int updatePtwFireLicWpic(int wtId, String userId, String userName, Date date) {
        String columnName = "cfmWpic";
        return updatePtwFireAuditInfo(wtId, columnName, userId, userName, date);
    }

    @Override
    public int updatePtwFireLicWpExec(int wtId, String userId, String userName, Date date) {
        String columnName = "licWpExec";
        return updatePtwFireAuditInfo(wtId, columnName, userId, userName, date);
    }

    @Override
    public int updatePtwFireLicAppr(int wtId, String userId, String userName, Date date) {
        String columnName = "cfmApprv";
        return updatePtwFireAuditInfo(wtId, columnName, userId, userName, date);
    }
    
    @Override
    public int updatePtwFireLicGuard(int wtId, String userId, String userName, Date date) {
        String columnName = "licSecu";
        return updatePtwFireAuditInfo(wtId, columnName, userId, userName, date);
    }
    
    @Override
    public int updatePtwFireLicXf(int wtId, String userId, String userName, Date date) {
        String columnName = "cfmGuardXf";
        return updatePtwFireAuditInfo(wtId, columnName, userId, userName, date);
    }

    @Override
    public int updatePtwFireLicAj(int wtId, String userId, String userName, Date date) {
        String columnName = "licPicAj";
        return updatePtwFireAuditInfo(wtId, columnName, userId, userName, date);
    }

    @Override
    public int updatePtwFireLicCj(int wtId, String userId, String userName, Date date) {
        String columnName = "licPicCj";
        return updatePtwFireAuditInfo(wtId, columnName, userId, userName, date);
    }

    @Override
    public int updatePtwFireFinWpic(int wtId, String userId, String userName, Date date) {
        String columnName = "finWpic";
        return updatePtwFireAuditInfo(wtId, columnName, userId, userName, date);
    }

    @Override
    public int updatePtwFireFinWpExec(int wtId, String userId, String userName, Date date) {
        String columnName = "finExec";
        return updatePtwFireAuditInfo(wtId, columnName, userId, userName, date);
    }

    @Override
    public int updatePtwFireFinAppr(int wtId, String userId, String userName, Date date) {
        String columnName = "finApprv";
        return updatePtwFireAuditInfo(wtId, columnName, userId, userName, date);
    }

    @Override
    public int updatePtwFireFinXf(int wtId, String userId, String userName, Date date) {
        String columnName = "finGuardXf";
        return updatePtwFireAuditInfo(wtId, columnName, userId, userName, date);
    }

}
