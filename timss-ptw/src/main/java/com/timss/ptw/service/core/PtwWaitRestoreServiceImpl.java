package com.timss.ptw.service.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.ptw.bean.PtwWaitRestore;
import com.timss.ptw.dao.PtwWaitRestoreDao;
import com.timss.ptw.service.PtwWaitRestoreService;
@Service
public class PtwWaitRestoreServiceImpl implements PtwWaitRestoreService {
    private static final Logger log = Logger.getLogger(PtwWaitRestoreService.class);
    @Autowired
    private PtwWaitRestoreDao ptwWaitRestoreDao;
    public List<PtwWaitRestore> queryPtwWaitRestoreByPtwId(int wtId) {
        return ptwWaitRestoreDao.queryPtwWaitRestoreByPtwId( wtId );
    }

    @Override
    public int insertPtwWait(PtwWaitRestore ptwWaitRestore) {
        return ptwWaitRestoreDao.insertPtwWait( ptwWaitRestore );
    }

    @Override
    public int updatePtwRestore(PtwWaitRestore ptwWaitRestore) {
        return ptwWaitRestoreDao.updatePtwRestore( ptwWaitRestore );
    }

    @Override
    public boolean ptwNeedRestore(List<PtwWaitRestore> ptwWaitRestores) {
        if ( ptwWaitRestores == null || ptwWaitRestores.size() == 0 ) {
            return false;
        }
        PtwWaitRestore ptwWaitRestore = ptwWaitRestores.get( ptwWaitRestores.size() - 1 );
        if ( ptwWaitRestore.getResTime() == null ) {
            return true;
        }
        return false;
    }

}
