package com.timss.ptw.service.core;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.ptw.bean.PtwExtand;
import com.timss.ptw.dao.PtwExtandDao;
import com.timss.ptw.service.PtwExtandService;

@Service
public class PtwExtandServiceImpl implements PtwExtandService {
    private static final Logger log = Logger.getLogger(PtwExtandService.class);
    
    @Autowired
    private PtwExtandDao ptwExtandDao;
    @Override
    public PtwExtand queryPtwExtandByPtwId(int wtId) {
        return ptwExtandDao.queryPtwExtandByPtwId( wtId );
    }

    @Override
    public int insertPtwExtand(PtwExtand ptwExtand) {
        return ptwExtandDao.insertPtwExtand( ptwExtand );
    }

}
