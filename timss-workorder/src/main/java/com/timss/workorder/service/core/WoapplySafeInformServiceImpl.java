package com.timss.workorder.service.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.bean.WoapplySafeInform;
import com.timss.workorder.dao.WoapplySafeInformDao;
import com.timss.workorder.service.WoapplySafeInformService;

@Service
public class WoapplySafeInformServiceImpl implements WoapplySafeInformService {
	@Autowired
        private WoapplySafeInformDao woapplySafeInformDao;
	
	private static Logger logger = Logger.getLogger(WoapplySafeInformServiceImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<WoapplySafeInform> queryWoapplySafeInformList(String woapplyId) {
        return woapplySafeInformDao.queryWoSafeInform( woapplyId );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertWoapplySafeInformList(List<WoapplySafeInform> woapplySafeInformList) {
        for ( WoapplySafeInform woapplySafeInform : woapplySafeInformList ) {
            woapplySafeInform.setId( null );
            woapplySafeInformDao.insertSafeInform( woapplySafeInform ); 
        }
        //woapplySafeInformDao.insertSafeInformList( woapplySafeInformList );        
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteSafeInformByWoapplyId(String woapplyId) {
        return woapplySafeInformDao.deleteWoSafeInformByWoapplyId( woapplyId );
    }

   

	 

}
