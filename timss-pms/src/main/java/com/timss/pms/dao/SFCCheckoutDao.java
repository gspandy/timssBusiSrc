package com.timss.pms.dao;

import java.util.List;

import com.timss.pms.vo.CheckoutDtlVo;
import com.timss.pms.vo.CheckoutVo;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;

public interface SFCCheckoutDao {
	
	

	
	/**
	 * 根据id查询 验收的详细信息
	 * @Title: queryCheckoutById
	 * @param id
	 * @return
	 */
	CheckoutDtlVo queryCheckoutById(Integer id);
	

	
	/**
	 * 
	 * @Title: queryCheckoutList
	 * @Description: 查询验收信息
	 * @param page 查询的条件
	 * @return: List<CheckoutVo>  查询结果
	 * @throws
	 */
	List<CheckoutVo> queryCheckoutList(Page<CheckoutVo> page);
	
	/**
	 * 
	 * @Title: queryCheckoutListAndFilter
	 * @Description: 查询验收信息并过滤
	 * @param page 查询的条件
	 * @return: List<CheckoutVo>  查询结果
	 * @throws
	 */
	@RowFilter(flowIdColumn="flowId",exclusiveRule="PMS_EXCLUDE",isRouteFilter=true)
	List<CheckoutVo> queryCheckoutListAndFilter(Page<CheckoutVo> page);
	
}
