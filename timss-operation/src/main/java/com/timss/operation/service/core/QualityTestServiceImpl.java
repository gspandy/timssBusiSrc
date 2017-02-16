package com.timss.operation.service.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.operation.bean.QualityTestBean;
import com.timss.operation.dao.QualityTestDao;
import com.timss.operation.service.QualityTestService;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.dto.Page;

@Service
public class QualityTestServiceImpl implements QualityTestService {
    @Autowired
    private QualityTestDao qualityTestDao;
    static Logger logger = Logger.getLogger( QualityTestServiceImpl.class );
	
	@Override
	public Page<QualityTestBean> queryList(Page<QualityTestBean> page)
			throws Exception {
		//查询质检日志的列表
		List<QualityTestBean> list=qualityTestDao.queryList(page);
		page.setResults(list);
		return page;
	}
	
	@Override
	public QualityTestBean queryDetail(String qtId) throws Exception {
		//查询质检日志的详情
		QualityTestBean bean=qualityTestDao.queryDetail(qtId);
		return bean;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int insert(@CUDTarget QualityTestBean bean) throws Exception {
		return qualityTestDao.insert(bean);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int update(@CUDTarget QualityTestBean bean) throws Exception {
		return qualityTestDao.update(bean);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int deleteById(String id) throws Exception {
		return qualityTestDao.deleteById(id);
	}    
	
}