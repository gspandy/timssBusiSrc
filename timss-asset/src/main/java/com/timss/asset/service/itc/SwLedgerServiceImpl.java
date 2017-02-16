package com.timss.asset.service.itc;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.SwLedgerAppBean;
import com.timss.asset.bean.SwLedgerBean;
import com.timss.asset.dao.SwLedgerDao;
import com.timss.asset.service.SwLedgerService;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.dto.Page;

@Service
public class SwLedgerServiceImpl implements SwLedgerService {
    @Autowired
    private SwLedgerDao swDao;
    static Logger logger = Logger.getLogger( SwLedgerServiceImpl.class );
	
	@Override
	public Page<SwLedgerBean> queryList(Page<SwLedgerBean> page)
			throws Exception {
		//查询软件台账的列表
		List<SwLedgerBean> list=swDao.queryList(page);
		
		//查询每个软件台账的app
		for (SwLedgerBean bean : list) {
			List<SwLedgerAppBean> apps=swDao.queryApps(bean.getSwId());
			bean.setApps(apps);
		}
		
		page.setResults(list);
		return page;
	}
	
	@Override
	public SwLedgerBean queryDetail(String swId) throws Exception {
		//查询软件台账的详情
		SwLedgerBean bean=swDao.queryDetail(swId);
		
		//查询软件台账的应用
		List<SwLedgerAppBean> apps=swDao.queryAppsDetail(bean.getSwId());
		bean.setApps(apps);

		return bean;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int insert(@CUDTarget SwLedgerBean bean) throws Exception {
		return swDao.insert(bean);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int update(@CUDTarget SwLedgerBean bean) throws Exception {
		return swDao.update(bean);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int deleteById(String id) throws Exception {
		return swDao.deleteById(id);
	}    
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int insertApp(@CUDTarget SwLedgerAppBean bean) throws Exception {
		return swDao.insertApp(bean);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int updateApp(@CUDTarget SwLedgerAppBean bean) throws Exception {
		return swDao.updateApp(bean);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int deleteAppById(String id) throws Exception {
		return swDao.deleteAppById(id);
	}

	@Override
	public SwLedgerBean querySwLedgerByName(String siteId, String name)
			throws Exception {
		return swDao.querySwLedgerByName(siteId,name);
	}

	@Override
	public SwLedgerAppBean querySwLedgerAppByName(String swId, String name)
			throws Exception {
		return swDao.querySwLedgerAppByName(swId,name);
	}

	@Override
	public List<SwLedgerAppBean> querySwLedgerAppByHwId(String id)
			throws Exception {
		return swDao.querySwLedgerAppByHwId(id);
	}
}