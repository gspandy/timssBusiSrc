package com.timss.attendance.service;

import java.util.Date;
import java.util.List;

import com.timss.attendance.bean.RationalizationBean;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.workflow.task.TaskInfo;

/**
 * 合理化建议的service
 * @author 890205
 */
public interface RationalzationService {

	/**
	 * 分页查询列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	Page<RationalizationBean> queryList(Page<RationalizationBean> page) throws Exception;

	
	/**
	 * 查询详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	RationalizationBean queryDetail(String id) throws Exception;
	
	
	/**
     * 将收到的数据转成bean
     * @param formData
     * @return
     */
    RationalizationBean convertBean(String formData)throws Exception;
    
    
    
    
    RationalizationBean save(@CUDTarget RationalizationBean bean,@Operator SecureUser operator) throws Exception;

    
    
    /**
     * 新建或更新
     * @param bean
     * @return
     */
    Integer insertOrUpdate(@CUDTarget RationalizationBean bean,@Operator SecureUser operator) throws Exception;


    /**
	 * 新建
	 * @param bean
	 * @return
	 */
	Integer insert(@CUDTarget RationalizationBean bean,@Operator SecureUser operator) throws Exception;
	
	
	/**
	 * 提交到流程
	 * @param bean
	 * @return
	 */
	RationalizationBean commit(@CUDTarget RationalizationBean bean,@Operator SecureUser operator) throws Exception;
	
	
	/**
	 * 查询申请单的工作流信息
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	RationalizationBean queryWorkFlow(RationalizationBean bean, SecureUser operator) throws Exception; 
	
	
	/**
     * 更新提交申请时间（创建时间）
     * 需传入id
     * createdate为空会取当前时间
     * @return
     */
    Integer updateCreateDate(String rationalId,Date createDate,@Operator SecureUser operator)throws Exception;
    
    
    /**
	 * 从taskinfo中获取申请单数据更新节点状态
	 * @param taskInfo
	 * @param status
	 * @return
	 * @throws Exception 
	 */
	public Boolean updateRationAuditStatusByTask(TaskInfo taskInfo,String status,SecureUser operator,String userId) throws Exception;



	/**
	 * 删除,需赋值id
	 * @param bean
	 * @return
	 */
	Integer delete(@CUDTarget RationalizationBean bean,@Operator SecureUser operator) throws Exception;
	
	
	/**
	 * 更新
	 * @param bean
	 * @return
	 */
	Integer update(@CUDTarget RationalizationBean bean,@Operator SecureUser operator) throws Exception;
	
	/**
    * 审批时更新状态
    * instanceId 可为空
    * @return
    */
   Integer updateAuditStatus(String rationalId,String instanceId,String status,@Operator SecureUser operator, String userId)throws Exception;
   
   
   /**
    *   更新bean的实施简况，主要实施部门
    * instanceId 可为空
    * @return
   Integer updateBeanProperty(RationalizationBean bean);
   
   	/**
	 * 更新bean的鉴定、评价、评奖或评分属性
	 * @param bean
	 * @return
	
   Integer updateBeanzzcp(RationalizationBean bean);
 
	
	/**
	 * 更新bean地的对口专业评审小组是否通过
	 * @param bean
	 * @return
	
	Integer duiKouPass(RationalizationBean bean);
    
	/**
	 * 更新bean地的运行部门
	 * @param bean
	 * @return
	
	Integer updateOperDept(RationalizationBean bean);
	 */
	
	/**
	 * 查找userId对应的处理人姓名
	 * @param bean
	 * @return
	 */
	Integer updateById(RationalizationBean bean);
	
	
	/**
	 * 作废,需赋值id
	 * @param bean
	 * @return
	 */
	RationalizationBean invalid(@CUDTarget RationalizationBean bean,@Operator SecureUser operator) throws Exception;
	
	
	
	 /**
     * 
     * @description:合理化建议专业分类枚举code to name
     * @createDate: 2014年10月11日
     * @param flowCode
     * @return:
     */
	String getCategoryName(List<AppEnum> emList, String enumId )throws Exception;

	/*
	 * 审批退回更新处理人
	  */
	void updateCurrHandlerUser(String rationId, UserInfoScope userInfoScope,
			String string)throws Exception;
}