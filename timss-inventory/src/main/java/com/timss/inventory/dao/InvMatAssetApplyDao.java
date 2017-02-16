package com.timss.inventory.dao;

import java.util.List;

import com.timss.inventory.bean.InvMatAssetApply;

/**
 * @title: 资产化申请DAO
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatAssetApplyDao.java
 * @author: user
 * @createDate: 2016-10-10
 * @updateUser: user
 * @version: 1.0
 */
public interface InvMatAssetApplyDao {
	/**
	 * @description: 插入资产化申请
	 * @author: 890199
	 * @createDate: 2016-10-10
	 * @param: 
	 * @return:
	 * @throws Exception:
	 */
	int insertAssetApply(InvMatAssetApply invMatAssetApply);
	
	/**
	 * @description: 通过资产ID获取资产化申请信息
	 * @author: 890199
	 * @createDate: 2016-10-10
	 * @param: 
	 * @return:
	 * @throws Exception:
	 */
	List<InvMatAssetApply> queryAssetApplyById(String astApplyId);
	
	/**
	 * @description: 通过imadId获取资产化申请信息
	 * @author: 890199
	 * @createDate: 2016-10-12
	 * @param: 
	 * @return:
	 * @throws Exception:
	 */
	List<InvMatAssetApply> queryAssetApplyByImadId(String imadId);
	
	/**
	 * @description: 删除固定资产申请
	 * @author: 890199
	 * @createDate: 2016-10-12
	 * @param: 
	 * @return:
	 * @throws Exception:
	 */
	int removeAssetApply(String astApplyId);
}
