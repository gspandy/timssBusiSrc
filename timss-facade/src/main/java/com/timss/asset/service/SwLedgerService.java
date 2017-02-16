package com.timss.asset.service;

import java.util.List;

import com.timss.asset.bean.SwLedgerAppBean;
import com.timss.asset.bean.SwLedgerBean;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.dto.Page;

/**
 * 软件台账的service
 * @author 890147
 *
 */
public interface SwLedgerService {

	/**
	 * 分页查询列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	Page<SwLedgerBean> queryList(Page<SwLedgerBean> page) throws Exception;

	/**
	 * 查询详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	SwLedgerBean queryDetail(String id) throws Exception;  
	
	/**
	 * 新建
	 * @param bean
	 * @return
	 */
	int insert(@CUDTarget SwLedgerBean bean) throws Exception;
	
	/**
	 * 更新
	 * @param bean
	 * @return
	 */
	int update(@CUDTarget SwLedgerBean bean) throws Exception;
	
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	int deleteById(String id) throws Exception;
	
	/**
	 * 查询站点下给定名称的软件台账，用于判重
	 * @param siteId
	 * @param name
	 * @return
	 */
	SwLedgerBean querySwLedgerByName(String siteId,String name) throws Exception;
	
	/**
	 * 查询软件台账中给定名称的应用，用于判重
	 * @param swId
	 * @param name
	 * @return
	 */
	SwLedgerAppBean querySwLedgerAppByName(String swId,String name) throws Exception;
	
	/**
	 * 新建应用
	 * @param bean
	 * @return
	 */
	int insertApp(@CUDTarget SwLedgerAppBean bean) throws Exception;
	
	/**
	 * 更新应用
	 * @param bean
	 * @return
	 */
	int updateApp(@CUDTarget SwLedgerAppBean bean) throws Exception;
	
	/**
	 * 根据id删除应用
	 * @param id
	 * @return
	 */
	int deleteAppById(String id) throws Exception;
	
	/**
	 * 根据硬件台账id查找部署在上面的所有的软件台账的应用
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<SwLedgerAppBean> querySwLedgerAppByHwId(String id) throws Exception;
}