package com.timss.asset.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.asset.bean.SwLedgerAppBean;
import com.timss.asset.bean.SwLedgerBean;
import com.yudean.itc.dto.Page;

/**
 * 软件台账的dao
 * @author 890147
 *
 */
public interface SwLedgerDao {
	/**
	 * 分页查询列表
	 * @param page
	 * @return
	 */
	List<SwLedgerBean> queryList(Page<SwLedgerBean> page);
	
	/**
	 * 查询给定软件台账id的应用（简单信息，ip）
	 * @param id
	 * @return
	 */
	List<SwLedgerAppBean> queryApps(@Param("id")String id);
	
	/**
	 * 查询详情
	 * @param id
	 * @return
	 */
	SwLedgerBean queryDetail(@Param("id")String id);
	
	/**
	 * 查询给定软件台账id的所有应用（所有信息）
	 * @param id
	 * @return
	 */
	List<SwLedgerAppBean> queryAppsDetail(@Param("id")String id);
	
	/**
	 * 查询站点下给定名称的软件台账，用于判重
	 * @param siteId
	 * @param name
	 * @return
	 */
	SwLedgerBean querySwLedgerByName(@Param("siteId")String siteId,@Param("name")String name);
	
	/**
	 * 查询软件台账中给定名称的应用，用于判重
	 * @param swId
	 * @param name
	 * @return
	 */
	SwLedgerAppBean querySwLedgerAppByName(@Param("swId")String swId,@Param("name")String name);
	
	/**
	 * 新建
	 * @param bean
	 * @return
	 */
	int insert(SwLedgerBean bean);
	
	/**
	 * 新建应用
	 * @param bean
	 * @return
	 */
	int insertApp(SwLedgerAppBean bean);
	
	/**
	 * 更新
	 * @param bean
	 * @return
	 */
	int update(SwLedgerBean bean);
	
	/**
	 * 更新应用
	 * @param bean
	 * @return
	 */
	int updateApp(SwLedgerAppBean bean);
	
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	int deleteById(@Param("id")String id);
	
	/**
	 * 根据id删除应用
	 * @param id
	 * @return
	 */
	int deleteAppById(@Param("id")String id);
	
	/**
	 * 根据硬件台账id查询部署在上面的应用
	 * @param hwId
	 * @return
	 */
	List<SwLedgerAppBean> querySwLedgerAppByHwId(@Param("hwId")String hwId);
}