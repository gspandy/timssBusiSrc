package com.timss.attendance.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.attendance.bean.RationalizationBean;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * 培训申请的dao
 * @author 890205
 */
public interface RationalizationDao {
	/**
	 * 分页查询列表
	 * @param page
	 * @return
	 */
	//@RowFilter(flowIdColumn="RATIONALNO",exclusiveRule="ATD_EXCLUDE",exclusiveDeptRule="ATD_DEPT_EXCLUDE")
	List<RationalizationBean> queryList(Page<RationalizationBean> page);
	
	
	
	/**
	 * 查询详情
	 * @param id
	 * @return
	 */
	RationalizationBean queryDetail(@Param("rationalId")String rationalId);



	Integer insert(RationalizationBean bean);
	
	/**
	 * 更新申请时间（提交时间）
	 * @param rationalId
	 * @param createDate
	 * @param modifyuser
	 * @return
	 */
	Integer updateCreateDate(@Param("rationalId")String rationalId, @Param("createDate")Date createDate,
			@Param("modifyuser")String modifyuser);
	
	/**
	 * 更新审批状态信息
	 * @param trainingId
	 * @param instanceId
	 * @param status
	 * @param modifyuser
	 * @return
	 */
	Integer updateAuditStatus(@Param("rationalId")String rationalId, @Param("instanceId")String instanceId,
			@Param("status")String status, @Param("modifyuser")String modifyuser,@Param("userId")String userId);

	
	/**
	 * 删除，需赋值id
	 * @param bean
	 * @return
	 */
	Integer delete(RationalizationBean bean);
	
	
	/**
	 * 更新
	 * @param bean
	 * @return
	 */
	Integer update(RationalizationBean bean); 
		
	/**
	 * 查找userId对应的处理人姓名
	 * @param bean
	 * @return
	 */
	Integer updateById(RationalizationBean bean);
	
	
	/*当状态为作废或己归档时清空处理人信息*/
	int setRecomNameNull(String rationalId);



	void updateCurrHandUserById(Map<String, String> parmas);
	
	/*更新多个处理人方法*/
	String selectNameByUserList(List list);
}