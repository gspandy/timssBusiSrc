package com.timss.attendance.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.attendance.bean.TrainingBean;
import com.timss.attendance.bean.TrainingItemBean;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;

/**
 * 培训申请的dao
 * @author 890147
 */
public interface TrainingDao {
	/**
	 * 分页查询列表
	 * @param page
	 * @return
	 */
	//@RowFilter(flowIdColumn="TRAININGNO",exclusiveRule="ATD_EXCLUDE",exclusiveDeptRule="ATD_DEPT_EXCLUDE")
	List<TrainingBean> queryList(Page<TrainingBean> page);
	
	/**
	 * 查询详情
	 * @param id
	 * @return
	 */
	TrainingBean queryDetail(@Param("trainingId")String trainingId);
	
	/**
	 * 新建
	 * @param bean
	 * @return
	 */
	Integer insert(TrainingBean bean);
	
	/**
	 * 更新
	 * @param bean
	 * @return
	 */
	Integer update(TrainingBean bean);
	
	/**
	 * 删除，需赋值id
	 * @param bean
	 * @return
	 */
	Integer delete(TrainingBean bean);
	
	/**
	 * 更新审批状态信息
	 * @param trainingId
	 * @param instanceId
	 * @param status
	 * @param modifyuser
	 * @return
	 */
	Integer updateAuditStatus(@Param("trainingId")String trainingId, @Param("instanceId")String instanceId,
			@Param("status")String status, @Param("modifyuser")String modifyuser);
	
	/**
	 * 更新申请时间（提交时间）
	 * @param trainingId
	 * @param createDate
	 * @param modifyuser
	 * @return
	 */
	Integer updateCreateDate(@Param("trainingId")String trainingId, @Param("createDate")Date createDate,
			@Param("modifyuser")String modifyuser);
	
	/**
     * 批量插入详情
     * @param trainingId
     * @param itemList
     * @return
     */
	Integer batchInsertItem(@Param("trainingId")String trainingId,@Param("itemList")List<TrainingItemBean>itemList);    
    /**
     * 批量更新详情
     * @param itemList
     * @return 无返回值
     */
	Integer batchUpdateItem(@Param("itemList")List<TrainingItemBean>itemList);  
    /**
     * 批量删除详情
     * @param itemList
     * @return
     */
	Integer batchDeleteItem(@Param("itemList")List<TrainingItemBean>itemList);
	
	/**
	 * 查询详情项
	 * @param trainingId
	 * @return
	 */
	List<TrainingItemBean> queryItemList(@Param("trainingId")String trainingId);
	
	/**
     * @title: 获取用户职位信息
     * @company: gdyd
     * @author: 890199
     * @createDate: 2017-02-07
     * @return
     * @throws Exception
     */
	TrainingItemBean queryUserItemByUserId(String userId);
}