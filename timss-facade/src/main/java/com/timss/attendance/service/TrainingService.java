package com.timss.attendance.service;

import java.util.Date;

import com.timss.attendance.bean.TrainingBean;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;

/**
 * 培训申请的service
 * @author 890147
 */
public interface TrainingService {

	/**
	 * 分页查询列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	Page<TrainingBean> queryList(Page<TrainingBean> page) throws Exception;

	/**
	 * 查询详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	TrainingBean queryDetail(String id) throws Exception;  
	
	/**
	 * 查询申请单的工作流信息
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	TrainingBean queryWorkFlow(TrainingBean bean, SecureUser operator) throws Exception;  
	
	/**
	 * 新建
	 * @param bean
	 * @return
	 */
	Integer insert(@CUDTarget TrainingBean bean,@Operator SecureUser operator) throws Exception;
	
	/**
	 * 更新
	 * @param bean
	 * @return
	 */
	Integer update(@CUDTarget TrainingBean bean,@Operator SecureUser operator) throws Exception;
	
	/**
	 * 删除,需赋值id
	 * @param bean
	 * @return
	 */
	Integer delete(@CUDTarget TrainingBean bean,@Operator SecureUser operator) throws Exception;
	
	/**
	 * 作废,需赋值id
	 * @param bean
	 * @return
	 */
	TrainingBean invalid(@CUDTarget TrainingBean bean,@Operator SecureUser operator) throws Exception;
	
	/**
	 * 提交到流程
	 * @param bean
	 * @return
	 */
	TrainingBean commit(@CUDTarget TrainingBean bean,@Operator SecureUser operator) throws Exception;
	
	/**
     * 新建或更新
     * @param bean
     * @return
     */
    Integer insertOrUpdate(@CUDTarget TrainingBean bean,@Operator SecureUser operator) throws Exception;
    
	/**
     * 暂存，需要写草稿
     * @param bean
     * @return
     */
    TrainingBean save(@CUDTarget TrainingBean bean,@Operator SecureUser operator) throws Exception;
    
	/**
     * 将收到的数据转成bean
     * @param formData
     * @param fileIds
     * @param addRows
     * @param delRows
     * @param updateRows
     * @return
     */
    TrainingBean convertBean( String formData, String fileIds, String addRows, String delRows, String updateRows )throws Exception;
    /**
     * 审批时更新状态
     * instanceId 可为空
     * @return
     */
    Integer updateAuditStatus(String trainingId,String instanceId,String status,@Operator SecureUser operator)throws Exception;
    /**
     * 更新提交申请时间（创建时间）
     * 需传入id
     * createdate为空会取当前时间
     * @return
     */
    Integer updateCreateDate(String trainingId,Date createDate,@Operator SecureUser operator)throws Exception;
    
    /**
     * @title: 获取用户职位信息
     * @company: gdyd
     * @author: 890199
     * @createDate: 2017-02-07
     * @return
     * @throws Exception
     */
    String getUserJob(String userId) throws Exception;
}