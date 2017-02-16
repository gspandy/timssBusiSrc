package com.timss.asset.dao;

import org.apache.ibatis.annotations.Param;

import com.timss.asset.bean.HwLedgerBean;
import com.timss.asset.bean.HwLedgerEqptBean;
import com.timss.asset.bean.HwLedgerNetworkBean;
import com.timss.asset.bean.HwLedgerRoomEqptBean;
import com.timss.asset.bean.HwLedgerStorageBean;

/**
 * 硬件台账的关于eqpt的dao
 * 包括机房设备、网络设备、存储设备
 * @author 890147
 *
 */
public interface HwLedgerEqptDao {
	/**
	 * 查询网络设备详情
	 * @param id
	 * @return
	 */
	HwLedgerNetworkBean queryNetworkDetailById(@Param("id")String id);
	
	/**
	 * 查询存储设备详情
	 * @param id
	 * @return
	 */
	HwLedgerStorageBean queryStorageDetailById(@Param("id")String id);
	
	/**
	 * 查询机房设备详情
	 * @param id
	 * @return
	 */
	HwLedgerRoomEqptBean queryRoomEqptDetailById(@Param("id")String id);
		
	/**
	 * 新建eqpt
	 * @param bean
	 * @return
	 */
	int insertEqpt(HwLedgerEqptBean bean);
	
	/**
	 * 更新eqpt
	 * @param bean
	 * @return
	 */
	int updateEqpt(HwLedgerEqptBean bean);
	
	/**
	 * 新建roomEqpt
	 * @param bean
	 * @return
	 */
	int insertRoomEqpt(HwLedgerRoomEqptBean bean);
	
	/**
	 * 更新roomEqpt
	 * @param bean
	 * @return
	 */
	int updateRoomEqpt(HwLedgerRoomEqptBean bean);
	
	/**
	 * 新建storage
	 * @param bean
	 * @return
	 */
	int insertStorage(HwLedgerStorageBean bean);
	
	/**
	 * 更新storage
	 * @param bean
	 * @return
	 */
	int updateStorage(HwLedgerStorageBean bean);
	
	/**
	 * 新建network
	 * @param bean
	 * @return
	 */
	int insertNetwork(HwLedgerNetworkBean bean);
	
	/**
	 * 更新network
	 * @param bean
	 * @return
	 */
	int updateNetwork(HwLedgerNetworkBean bean);

	/**
	 * 
	 * @description:通过id 查询 AST_HW_L_EQPT bean (无继承)
	 * @author: fengzt
	 * @createDate: 2014年12月23日
	 * @param hwId
	 * @return:HwLedgerEqptBean
	 */
	HwLedgerEqptBean queryHwLedgerEqptById(String id);

	/**
	 * 检查站点下硬件台账的资产编号是否已存在
	 * @param siteId
	 * @param code
	 * @return
	 */
	HwLedgerBean queryHwLedgerByAssetCode(@Param("siteId")String siteId, @Param("code")String code);
}