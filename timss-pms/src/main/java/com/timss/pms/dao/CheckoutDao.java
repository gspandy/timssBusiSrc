package com.timss.pms.dao;

import java.util.List;

import com.timss.pms.bean.Checkout;
import com.timss.pms.vo.CheckoutDtlVo;
import com.timss.pms.vo.CheckoutVo;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;

/**
 * 项目验收dao接口
 * @ClassName:     CheckoutDao
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2014-7-22 上午8:44:47
 */
public interface CheckoutDao {
	
	/**
	 * 插入验收信息
	 * @Title: insertCheckout
	 * @param checkout
	 * @return
	 */
	int insertCheckout(Checkout checkout);
	
	int updateCheckout(Checkout checkout);
	
	/**
	 * 按需更新验收信息
	 * @Title: updateByPrimaryKeySelective
	 * @param checkout
	 * @return
	 */
	int updateByPrimaryKeySelective(Checkout checkout);
	
	
	/**
	 * 物理删除验收信息
	 * @Title: deleteCheckout
	 * @param id
	 * @return
	 */
	int deleteCheckout(int id);
	
	/**
	 * 根据合同查询所属的验收的列表信息
	 * @Title: queryCheckoutListByContractId
	 * @param id
	 * @return
	 */
	List<CheckoutVo> queryCheckoutListByContractId(Integer id);
	
	/**
	 * 根据id查询 验收的详细信息
	 * @Title: queryCheckoutById
	 * @param id
	 * @return
	 */
	CheckoutDtlVo queryCheckoutById(Integer id);
	
	/**
	 * 根据结算计划id，查询验收列表
	 * @Title: queryCheckoutListByPayplanId
	 * @param id
	 * @return
	 */
	List<CheckoutVo> queryCheckoutListByPayplanId(Integer id);
	
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
	@RowFilter(flowIdColumn="flowid",exclusiveRule="PMS_EXCLUDE",exclusiveDeptRule="PMS_DEPT_EXCLUDE",isRouteFilter=true)
	List<CheckoutVo> queryCheckoutListAndFilter(Page<CheckoutVo> page);
	
	List<CheckoutVo> queryCheckoutListByProjectId(int projectId);
	
	
}
