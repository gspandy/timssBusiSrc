package com.timss.inventory.service.core;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.inventory.bean.InvMatAssetApply;
import com.timss.inventory.dao.InvMatAssetApplyDao;
import com.timss.inventory.service.InvMatAssetApplyService;

/**
 * @title: 资产化申请
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatAssetApplyServiceImpl.java
 * @author: user
 * @createDate: 2016-10-10
 * @updateUser: user
 * @version: 1.0
 */
@Service("invMatAssetApplyService")
public class InvMatAssetApplyServiceImpl implements InvMatAssetApplyService{
	/**
	 * 注入DAO
	 */
	@Autowired
	private InvMatAssetApplyDao invMatAssetApplyDao;
	
	/**
	 * @title: 插入资产申请
	 * @company: gdyd
	 * @author: 890199
	 * @createDate: 2016-10-10
	 * @updateUser: 890199
	 * @version:1.0
	 */
	@Override
	public int insertAssetApply(InvMatAssetApply invMatAssetApply){
		return invMatAssetApplyDao.insertAssetApply(invMatAssetApply);
	}
	
	/**
	 * @title: 获取资产申请
	 * @company: gdyd
	 * @author: 890199
	 * @createDate: 2016-10-10
	 * @updateUser: 890199
	 * @version:1.0
	 */
	@Override
	public List<InvMatAssetApply> queryAssetApplyById(String astApplyId){
		return invMatAssetApplyDao.queryAssetApplyById(astApplyId);
	}
	
	/**
	 * @title: 通过imadId获取资产申请
	 * @company: gdyd
	 * @author: 890199
	 * @createDate: 2016-10-12
	 * @updateUser: 890199
	 * @version:1.0
	 */
	@Override
	public List<InvMatAssetApply> queryAssetApplyByImadId(String imadId){
		return invMatAssetApplyDao.queryAssetApplyByImadId(imadId);
	}
	
	/**
	 * @title: 删除固定资产申请
	 * @company: gdyd
	 * @author: 890199
	 * @createDate: 2016-10-12
	 * @updateUser: 890199
	 * @version:1.0
	 */
	@Override
	public int removeAssetApply(String astApplyId){
		return invMatAssetApplyDao.removeAssetApply(astApplyId);
	}
}
