package com.timss.attendance.service.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.attendance.bean.MachineBean;
import com.timss.attendance.dao.MachineDao;
import com.timss.attendance.service.MachineService;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.dto.Page;

@Service
public class MachineServiceImpl implements MachineService {
    @Autowired
    private MachineDao machineDao;
    static Logger logger = Logger.getLogger( MachineServiceImpl.class );
	
	@Override
	public Page<MachineBean> queryList(Page<MachineBean> page)
			throws Exception {
		//查询考勤机的列表
		List<MachineBean> list=machineDao.queryList(page);
		
		page.setResults(list);
		return page;
	}
	
	@Override
	public List<MachineBean> queryAll()
			throws Exception {
		//查询所有考勤机
		List<MachineBean> list=machineDao.queryAll();
		
		return list;
	}
	
	@Override
	public List<MachineBean> queryBySiteId(String siteId)
			throws Exception {
		//查询站点的考勤机
		List<MachineBean> list=machineDao.queryBySiteId(siteId);
		
		return list;
	}
	
	@Override
	public MachineBean queryDetail(String swId) throws Exception {
		//查询考勤机的详情
		MachineBean bean=machineDao.queryDetail(swId);
		
		return bean;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int insert(@CUDTarget MachineBean bean) throws Exception {
		return machineDao.insert(bean);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int update(@CUDTarget MachineBean bean) throws Exception {
		return machineDao.update(bean);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int updateSync(@CUDTarget MachineBean bean) throws Exception {
		return machineDao.updateSync(bean);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int deleteById(String id) throws Exception {
		return machineDao.deleteById(id);
	}    
	
}