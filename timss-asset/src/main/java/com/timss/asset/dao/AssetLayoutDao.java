package com.timss.asset.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.asset.bean.AssetLayoutBean;

/**
 * 设备配置的dao
 * @author 890147
 */
public interface AssetLayoutDao {
	/**
	 * 查询资产台账关联的设备配置
	 * @param assetId
	 * @return
	 */
	List<AssetLayoutBean>queryAllByAssetId(@Param("assetId")String assetId);
	/**
	 * 批量插入资产台账的设备配置
	 * @param list
	 * @param assetId
	 * @param createUser
	 * @param siteId
	 * @return
	 */
	Integer batchInsert(@Param("itemList")List<AssetLayoutBean>list,@Param("assetId")String assetId,
			@Param("createUser")String createUser,@Param("siteId")String siteId);
	/**
	 * 批量更新设备配置
	 * @param list
	 * @param modifyUser
	 * @return
	 */
	Integer batchUpdate(@Param("itemList")List<AssetLayoutBean>list,@Param("modifyUser")String modifyUser);
	/**
	 * 批量删除设备配置
	 * @param list
	 * @return
	 */
	Integer batchDelete(@Param("itemList")List<AssetLayoutBean>list);
}