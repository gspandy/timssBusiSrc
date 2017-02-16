package com.timss.pms.service;

import java.util.List;
import java.util.Map;

import com.timss.pms.bean.Checkout;
import com.timss.pms.bean.Project;
import com.timss.pms.vo.CheckoutDtlVo;
import com.timss.pms.vo.CheckoutVo;
import com.timss.pms.vo.ContractVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * 项目验收service接口
 * @ClassName:     CheckoutService
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-22 上午9:49:22
 */
public interface CheckoutService extends FlowVoidService{
	
	/**
	 * 插入验收信息
	 * @Title: insertCheckOut
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param checkout
	 */
	void insertCheckOut(Checkout checkout);
	
	/**
	 * 插入操作流程信息，并启动流程
	 * @Title: insertCheckoutWorkflow
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param checkout
	 */
	 Map tmpInsertCheckout(Checkout checkout);
	
	/**
	 * 根据合同id，查询对应的验收信息列表
	 * @Title: queryCheckoutListByContractId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	List<CheckoutVo> queryCheckoutListByContractId(String id);
	
	/**
	 * 根据验收id，查询验收的详细信息
	 * @Title: queryCheckoutById
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	CheckoutDtlVo queryCheckoutById(String id);
	
	/**
	 * 根据合同id，设置相应的验收信息
	 * @Title: queryCheckoutByContractId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	CheckoutDtlVo queryCheckoutByContractId(String contractId,CheckoutDtlVo checkoutDtlVo);
	
	/**
	 * 根据结算计划的id,查询对应的验收信息
	 * @Title: queryCheckoutByPayplanId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param payplanId
	 * @param contractId
	 * @return
	 */
	CheckoutDtlVo queryCheckoutByPayplanId(String payplanId,String contractId);
	
	/**
	 * 更新验收的信息，并更新状态的为审批通过
	 * @Title: updateCheckoutApproving
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param checkout
	 */
	void updateCheckoutApproving(Checkout checkout);
	
	void updateCheckoutApproving(int checkoutId);
	
	void updateCheckoutApproved(Checkout checkout);
	
	void updateCheckoutApproved(int checkoutId);
	
	void tmpUpdateCheckout(Checkout checkout);
	
	void deleteCheckout(String id);
	
	/**
	 * 终止项目验收流程，并修改验收状态
	 * @Title: stopWorkflow
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param project
	 * @param procInstId
	 * @return
	 */
	int stopWorkflow(Checkout checkout,String processInstId,String reason);
	
	/**
	 * 
	 * @Title: queryCheckoutList
	 * @Description: 查询合同信息
	 * @param: @param page 查询条件，包括分页信息
	 * @param: @param userInfo 用户信息
	 * @return: Page<CheckoutVo>  包含的分页信息的合同信息
	 * @throws
	 */
	Page<CheckoutVo> queryCheckoutList(Page<CheckoutVo> page,UserInfoScope userInfo);
	
}
