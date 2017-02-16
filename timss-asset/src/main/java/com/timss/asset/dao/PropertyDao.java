package com.timss.asset.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.asset.bean.PropertyBean;
import com.yudean.itc.dto.Page;

/**
 * 物业管理的dao
 * @author 890147
 *
 */
public interface PropertyDao {
	/**
	 * 根据siteid获得一个站点的根节点id
	 * @param siteId
	 * @return
	 */
	String queryPropertyRootIdBySite(@Param("siteId")String siteId);
	
	/**
	 * 分页查询房产列表
	 * @param page
	 * @return
	 */
	List<PropertyBean> queryList(Page<PropertyBean> page);
	
	/**
	 * 查询房产详情
	 * @param propertyId
	 * @return
	 */
	PropertyBean queryDetail(@Param("id")String propertyId);
	
	/**
	 * 新建房产
	 * @param bean
	 * @return
	 */
	int insert(PropertyBean bean);
	
	/**
	 * 更新房产
	 * @param bean
	 * @return
	 */
	int update(PropertyBean bean);
	
	/**
	 * 根据id删除房产
	 * @param id
	 * @return
	 */
	int deleteById(@Param("id")String id);

	/**
	 * 根据父节点查询所有的子节点
	 * @param parentId
	 * @return
	 */
	List<PropertyBean> queryChildren(@Param("parentId")String parentId);

	/**
	 * 查询指定站点的房产用于模糊搜索
	 * @param site
	 * @param keyWord
	 * @return
	 */
	List<Map<String, Object>> queryForHint(@Param("site")String site, @Param("kw")String keyWord);

	/**
	 * 查询给定id的房产的所有父节点，直到根节点
	 * @param id
	 * @return
	 */
	List<String> queryParents(@Param("id")String id);
}