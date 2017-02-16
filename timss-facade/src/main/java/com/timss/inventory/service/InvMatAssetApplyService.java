package com.timss.inventory.service;

import java.util.List;

import com.timss.inventory.bean.InvMatAssetApply;

/**
 * @title: 资产化申请接口
 * @company: gdyd
 * @author: 890199
 * @createDate: 2016-
 * @updateUser: 890199
 * @version:1.0
 */
public interface InvMatAssetApplyService {
	/**
	 * @title: 插入资产申请
	 * @company: gdyd
	 * @author: 890199
	 * @createDate: 2016-10-10
	 * @updateUser: 890199
	 * @version:1.0
	 */
	int insertAssetApply(InvMatAssetApply invMatAssetApply);
	
	/**
	 * @title: 获取资产申请
	 * @company: gdyd
	 * @author: 890199
	 * @createDate: 2016-10-10
	 * @updateUser: 890199
	 * @version:1.0
	 */
	List<InvMatAssetApply> queryAssetApplyById(String astApplyId);
	
	/**
	 * @title: 通过imadId获取资产申请
	 * @company: gdyd
	 * @author: 890199
	 * @createDate: 2016-10-12
	 * @updateUser: 890199
	 * @version:1.0
	 */
	List<InvMatAssetApply> queryAssetApplyByImadId(String imadId);
	
	/**
	 * @title: 删除固定资产申请
	 * @company: gdyd
	 * @author: 890199
	 * @createDate: 2016-10-17
	 * @updateUser: 890199
	 * @version:1.0
	 */
	int removeAssetApply(String astApplyId);
}
