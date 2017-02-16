package com.timss.ptw.service.core;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.ptw.bean.PtwInfo;
import com.timss.ptw.bean.PtwType;
import com.timss.ptw.bean.PtwTypeDefine;
import com.timss.ptw.dao.PtwTypeDefineDao;
import com.timss.ptw.service.PtwInfoService;
import com.timss.ptw.service.PtwTypeDefineService;
import com.timss.ptw.service.PtwTypeService;
@Service
public class PtwTypeDefineServiceImpl implements PtwTypeDefineService {
    
    private static final Logger log = Logger.getLogger(PtwTypeDefineService.class);
    
    @Autowired
    private PtwTypeDefineDao ptwTypeDefineDao;
    @Autowired
    private PtwInfoService ptwInfoService;
    @Autowired
    private PtwTypeService ptwTypeService;

    @Override
    public PtwTypeDefine queryPtwTypeDefineById(int id){
        return ptwTypeDefineDao.queryPtwTypeDefineById( id );
    }

    @Override
    public PtwTypeDefine queryPtwTypeDefineByPtwId(int wtId) {
        PtwInfo ptwInfo = ptwInfoService.queryPtwInfoById( wtId );
        PtwType ptwType = ptwTypeService.queryPtwTypeById( ptwInfo.getWtTypeId() );
        PtwTypeDefine ptwTypeDefine = queryPtwTypeDefineById( ptwType.getWtTypeDefineId() );
        return ptwTypeDefine;
    }
   

}
