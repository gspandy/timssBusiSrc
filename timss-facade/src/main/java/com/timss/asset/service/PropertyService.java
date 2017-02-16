package com.timss.asset.service;

import java.util.List;
import java.util.Map;

import com.timss.asset.bean.PropertyBean;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.dto.Page;

/**
 * 物业管理的service
 * @author 890147
 *
 */
public interface PropertyService {
	/**
	 * 根据siteid获得一个站点的根节点id
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	String queryPropertyRootIdBySite(String siteId) throws Exception;

	/**
	 * 分页查询房产列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	Page<PropertyBean> queryList(Page<PropertyBean> page) throws Exception;

	/**
	 * 查询房产详情
	 * @param propertyId
	 * @return
	 * @throws Exception
	 */
	PropertyBean queryDetail(String propertyId) throws Exception;  
	
	/**
	 * 新建房产
	 * @param bean
	 * @return
	 */
	int insert(@CUDTarget PropertyBean bean) throws Exception;
	
	/**
	 * 更新房产
	 * @param bean
	 * @return
	 */
	int update(@CUDTarget PropertyBean bean) throws Exception;
	
	/**
	 * 根据id删除房产
	 * @param id
	 * @return
	 */
	int deleteById(String id) throws Exception;

	/**
	 * 根据父节点查询所有的子节点
	 * @param parentId
	 * @return
	 */
	List<PropertyBean> queryChildren(String parentId) throws Exception;

	/**
	 * 查询指定站点的房产用于模糊搜索
	 * @param site
	 * @param keyWord
	 * @return
	 */
	List<Map<String, Object>> queryForHint(String site, String keyWord) throws Exception;

	/**
	 * 查询给定id的房产的所有父节点，直到根节点
	 * @param id
	 * @return
	 */
	List<String> queryParents(String id) throws Exception;
}
