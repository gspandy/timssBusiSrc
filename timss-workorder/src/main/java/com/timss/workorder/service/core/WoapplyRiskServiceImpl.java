package com.timss.workorder.service.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.bean.WoapplyRisk;
import com.timss.workorder.dao.WoapplyRiskDao;
import com.timss.workorder.dao.WoapplyWorkerDao;
import com.timss.workorder.service.WoUtilService;
import com.timss.workorder.service.WoapplyRiskService;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class WoapplyRiskServiceImpl implements WoapplyRiskService {
    @Autowired
    private WoapplyRiskDao woapplyRiskDao;
	
    private static Logger logger = Logger.getLogger(WoapplyRiskServiceImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<WoapplyRisk> queryWoapplyRiskList(String woapplyId) {
        return woapplyRiskDao.queryAllWoapplyRisk( woapplyId );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertWorkapplyList(List<WoapplyRisk> woapplyRiskList) {
        for ( WoapplyRisk woapplyRisk : woapplyRiskList ) {
            woapplyRisk.setId( null );
            woapplyRiskDao.insertWoapplyRisk( woapplyRisk );
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteRiskListByWoapplyId(String woapplyId) {
        return woapplyRiskDao.deleteRiskListByWoapplyId( woapplyId );
    }

    
	

	 

}
