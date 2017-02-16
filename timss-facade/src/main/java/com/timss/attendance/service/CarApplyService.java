package com.timss.attendance.service;

import com.timss.attendance.bean.CarApplyBean;
import com.timss.attendance.vo.CarApplyVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @description:用车申请
 * @author: yangk
 * @createDate: 
 * @param 
 * @return:
 */
public interface CarApplyService {
	
	/**插入
	 * @param carApplyBean
	 * @return
	 * @throws Exception 
	 */
	CarApplyVo insertCarApply(CarApplyBean bean) throws Exception ;
	/**暂存
	 * @param carApplyBean
	 * @return
	 * @throws Exception 
	 */
	CarApplyVo saveCarApply(CarApplyBean bean) throws Exception ;
	/**
	 * 更新不启动流程
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
	CarApplyVo updateCarApply(CarApplyBean bean) throws Exception;
	/**
	 * 更新启动流程
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
	CarApplyVo update(CarApplyBean bean) throws Exception;
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	int deleteById(String id) throws Exception;
	/**
	 *  作废
	 * @param id
	 * @return
	 */
	int obsoleteCarApply(String id) throws Exception;
	
	/**
	 * 列表页面查询
	 * @param page
	 * @return
	 */
	Page<CarApplyBean> queryList(Page<CarApplyBean> page);
	
	/**
	 * 根据id查找
	 * @param caId
	 * @return
	 */
	CarApplyVo queryById(String caId);
	
	/**
	 * @description:修改当前处理人
	 * @param caId
	 * @param userInfoScope
	 * @param flag :正常流程时为“normal”，回退时为“rollback”
	 */
	void updateCurrHandlerUser(String caId,UserInfoScope userInfo,String flag) throws Exception;
}
