package com.timss.asset.service;

import com.timss.asset.bean.HwLedgerBean;
import com.timss.asset.bean.HwLedgerNetworkBean;
import com.timss.asset.bean.HwLedgerRoomEqptBean;
import com.timss.asset.bean.HwLedgerStorageBean;
import com.yudean.itc.annotation.CUDTarget;

/**
 * 硬件台账的eqpt的service
 * @author 890147
 *
 */
public interface HwLedgerEqptService {

	/**
	 * 查询网络设备详情
	 * @param id
	 * @return
	 */
	HwLedgerNetworkBean queryNetworkDetailById(String id) throws Exception;  
	
	/**
	 * 查询存储设备详情
	 * @param id
	 * @return
	 */
	HwLedgerStorageBean queryStorageDetailById(String id) throws Exception;
	
	/**
	 * 查询机房设备详情
	 * @param id
	 * @return
	 */
	HwLedgerRoomEqptBean queryRoomEqptDetailById(String id) throws Exception;
	
	/**
	 * 新建roomEqpt
	 * @param bean
	 * @return
	 */
	int insertRoomEqpt(@CUDTarget HwLedgerRoomEqptBean bean) throws Exception;
	
	/**
	 * 更新roomEqpt
	 * @param bean
	 * @return
	 */
	int updateRoomEqpt(@CUDTarget HwLedgerRoomEqptBean bean) throws Exception;
	
	/**
	 * 新建storage
	 * @param bean
	 * @return
	 */
	int insertStorage(@CUDTarget HwLedgerStorageBean bean) throws Exception;
	
	/**
	 * 更新storage
	 * @param bean
	 * @return
	 */
	int updateStorage(@CUDTarget HwLedgerStorageBean bean) throws Exception;
	
	/**
	 * 新建network
	 * @param bean
	 * @return
	 */
	int insertNetwork(@CUDTarget HwLedgerNetworkBean bean) throws Exception;
	
	/**
	 * 更新network
	 * @param bean
	 * @return
	 */
	int updateNetwork(@CUDTarget HwLedgerNetworkBean bean) throws Exception;

	/**
	 * 检查站点下硬件台账的资产编号是否已存在
	 * @param siteId
	 * @param code
	 * @return
	 * @throws Exception
	 */
	HwLedgerBean queryHwLedgerByAssetCode(String siteId, String code) throws Exception;
	
}