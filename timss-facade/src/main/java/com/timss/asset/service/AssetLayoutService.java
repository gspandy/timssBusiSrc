package com.timss.asset.service;

import java.util.List;

import com.timss.asset.bean.AssetLayoutBean;

/**
 * 设备配置的service
 * @author 890147
 */
public interface AssetLayoutService {

	/**
	 * 查询资产台账关联的设备配置
	 * @param assetId
	 * @return
	 */
	List<AssetLayoutBean>queryAllByAssetId(String assetId)throws Exception;
	/**
	 * 批量插入资产台账的设备配置
	 * @param list
	 * @param assetId
	 * @return
	 */
	Integer batchInsert(List<AssetLayoutBean>addList,String assetId)throws Exception;
	/**
	 * 批量更新设备配置
	 * @param list
	 * @return
	 */
	Integer batchUpdate(List<AssetLayoutBean>updateList)throws Exception;
	/**
	 * 批量删除设备配置
	 * @param list
	 * @return
	 */
	Integer batchDelete(List<AssetLayoutBean>deleteList)throws Exception;
}