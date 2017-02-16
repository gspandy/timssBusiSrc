package com.timss.ptw.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.ptw.bean.PtwChangeWpic;
import com.timss.ptw.dao.PtwChangeWpicDao;
import com.timss.ptw.service.PtwChangeWpicService;

@Service
public class PtwChangeWpicServiceImpl implements PtwChangeWpicService {
    
    @Autowired
    private PtwChangeWpicDao ptwChangeWpicDao;
    @Override
    public PtwChangeWpic queryPtwChangeWpicByPtwId(int wtId) {
        return ptwChangeWpicDao.queryPtwChangeWpicByPtwId( wtId );
    }

    @Override
    public int insertPtwChangeWpic(PtwChangeWpic ptwChangeWpic) {
        return ptwChangeWpicDao.insertPtwChangeWpic( ptwChangeWpic );
    }

	@Override
	public List<PtwChangeWpic> queryPtwChangeWpicByNewNo(String chaNewWpicNo) {
		 return ptwChangeWpicDao.queryPtwChangeWpicByNewNo( chaNewWpicNo );
	}

}
