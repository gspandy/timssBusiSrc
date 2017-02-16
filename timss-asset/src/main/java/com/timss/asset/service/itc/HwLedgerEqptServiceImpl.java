package com.timss.asset.service.itc;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.HwLedgerBean;
import com.timss.asset.bean.HwLedgerNetworkBean;
import com.timss.asset.bean.HwLedgerRoomEqptBean;
import com.timss.asset.bean.HwLedgerStorageBean;
import com.timss.asset.dao.HwLedgerDao;
import com.timss.asset.dao.HwLedgerEqptDao;
import com.timss.asset.service.HwLedgerEqptService;
import com.yudean.itc.annotation.CUDTarget;

@Service
public class HwLedgerEqptServiceImpl implements HwLedgerEqptService {
	@Autowired
    private HwLedgerDao hwDao;
    @Autowired
    private HwLedgerEqptDao hwEqptDao;    
    
    static Logger logger = Logger.getLogger( HwLedgerEqptServiceImpl.class );
    
	@Override
	public HwLedgerNetworkBean queryNetworkDetailById(String id)
			throws Exception {
		HwLedgerNetworkBean bean=hwEqptDao.queryNetworkDetailById(id);
		return bean;
	}
	
	@Override
	public HwLedgerStorageBean queryStorageDetailById(String id)
			throws Exception {
		HwLedgerStorageBean bean=hwEqptDao.queryStorageDetailById(id);
		return bean;
	}
	
	@Override
	public HwLedgerRoomEqptBean queryRoomEqptDetailById(String id)
			throws Exception {
		HwLedgerRoomEqptBean bean=hwEqptDao.queryRoomEqptDetailById(id);
		return bean;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int insertRoomEqpt(@CUDTarget HwLedgerRoomEqptBean bean) throws Exception {
		int rst=1;
		//查重名
		
		if(rst==1){
			//插入树节点
			hwDao.insertHwLedger(bean);
			//插入eqpt节点
			hwEqptDao.insertEqpt(bean);
			//插入表节点
			hwEqptDao.insertRoomEqpt(bean);
		}
		return rst;//数字表示是否进行了插入
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int updateRoomEqpt(@CUDTarget HwLedgerRoomEqptBean bean) throws Exception {
		int rst=1;
		//查重名
		
		if(rst==1){
			//更新树节点
			hwDao.updateHwLedger(bean);
			//更新eqpt节点
			hwEqptDao.updateEqpt(bean);
			//更新表节点
			hwEqptDao.updateRoomEqpt(bean);
		}
		return rst;//数字表示是否进行了更新
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int insertStorage(@CUDTarget HwLedgerStorageBean bean) throws Exception {
		int rst=1;
		//查重名
		
		if(rst==1){
			//插入树节点
			hwDao.insertHwLedger(bean);
			//插入eqpt节点
			hwEqptDao.insertEqpt(bean);
			//插入表节点
			hwEqptDao.insertStorage(bean);
		}
		return rst;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int updateStorage(@CUDTarget HwLedgerStorageBean bean) throws Exception {
		int rst=1;
		//查重名
		
		if(rst==1){
			//更新树节点
			hwDao.updateHwLedger(bean);
			//更新eqpt节点
			hwEqptDao.updateEqpt(bean);
			//更新表节点
			hwEqptDao.updateStorage(bean);
		}
		return rst;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int insertNetwork(@CUDTarget HwLedgerNetworkBean bean) throws Exception {
		int rst=1;
		//查重名
		
		if(rst==1){
			//插入树节点
			hwDao.insertHwLedger(bean);
			//插入eqpt节点
			hwEqptDao.insertEqpt(bean);
			//插入表节点
			hwEqptDao.insertNetwork(bean);
		}
		return rst;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int updateNetwork(@CUDTarget HwLedgerNetworkBean bean) throws Exception {
		int rst=1;
		//查重名
		
		if(rst==1){
			//更新树节点
			hwDao.updateHwLedger(bean);
			//更新eqpt节点
			hwEqptDao.updateEqpt(bean);
			//更新表节点
			hwEqptDao.updateNetwork(bean);
		}
		return rst;
	}

	@Override
	public HwLedgerBean queryHwLedgerByAssetCode(String siteId, String code)
			throws Exception {
		return hwEqptDao.queryHwLedgerByAssetCode(siteId,code);
	}
}