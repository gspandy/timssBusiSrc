package com.timss.workorder.service.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.bean.WoapplyWorker;
import com.timss.workorder.dao.WoapplyWorkerDao;
import com.timss.workorder.service.WoapplyWorkerService;

@Service
public class WoapplyWorkerServiceImpl implements WoapplyWorkerService {
	@Autowired
        private WoapplyWorkerDao woapplyWorkerDao;
	
	private static Logger logger = Logger.getLogger(WoapplyWorkerServiceImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<WoapplyWorker> queryWoapplyWorkerList(String woapplyId) {
        return woapplyWorkerDao.queryWoapplyWorker( woapplyId );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertWoapplyWorkerList(List<WoapplyWorker> woapplyWorkerList) {
        for ( WoapplyWorker  woapplyWorker: woapplyWorkerList ) {
            woapplyWorker.setId( null );
            woapplyWorkerDao.insertWoapplyWorker( woapplyWorker );
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteWoapplyWorkerByWoapplyId(String woapplyId) {
        return woapplyWorkerDao.deleteWorkerByWoapplyId( woapplyId );
    }

    
	

	 

}
