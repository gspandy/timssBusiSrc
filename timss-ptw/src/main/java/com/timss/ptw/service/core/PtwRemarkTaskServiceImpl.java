package com.timss.ptw.service.core;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.ptw.bean.PtwRemarkTask;
import com.timss.ptw.dao.PtwRemarkTaskDao;
import com.timss.ptw.service.PtwRemarkTaskService;

@Service
public class PtwRemarkTaskServiceImpl implements PtwRemarkTaskService {
    private static final Logger log = Logger.getLogger(PtwRemarkTaskService.class);
    
    @Autowired
    private PtwRemarkTaskDao ptwRemarkTaskDao;
    @Override
    public PtwRemarkTask queryPtwRemarkTaskByPtwId(int wtId) {
        return ptwRemarkTaskDao.queryPtwRemarkTaskByPtwId( wtId );
    }

    @Override
    public int insertPtwRemarkTask(PtwRemarkTask ptwRemarkTask) {
        return ptwRemarkTaskDao.insertPtwRemarkTask( ptwRemarkTask );
    }

}
