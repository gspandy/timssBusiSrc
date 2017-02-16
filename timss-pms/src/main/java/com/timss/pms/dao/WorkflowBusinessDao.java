package com.timss.pms.dao;

import org.apache.ibatis.annotations.Param;

import com.timss.pms.bean.WorkflowBusiness;

/**
 * 
 * @ClassName:     WorkflowBusinessDao
 * @company: gdyd
 * @Description: 流程实例id与业务id关联对应dao
 * @author:    黄晓岚
 * @date:   2014-6-23 上午11:46:40
 */
public interface WorkflowBusinessDao {
	/**
	 * 
	 * @Title: insertWorkflowBusiness
	 * @Description: 插入流程与业务关联记录
	 * @param wBusiness
	 * @return
	 */
	int insertWorkflowBusiness(WorkflowBusiness wBusiness);
	
	/**
	 * 
	 * @Title: deleteWorkflwoBusinessByWFId
	 * @Description: 删除流程id对应的所有记录
	 * @param wfId 流程id
	 * @return
	 */
	int deleteWorkflwoBusinessByWFId(String wfId);
	
	/**
	 * 
	 * @Title: getWorkflowIdByBusinessIdAndInstancePreffix
	 * @Description: 根据业务id和流程id的前缀 查询流程id
	 * @param bid 业务id
	 * @param instancePreffix 流程id前缀
	 * @return 流程id
	 */
	String queryWorkflowIdByBusinessIdAndInstancePreffix(@Param("bid")String bid,@Param("instancePreffix")String instancePreffix);
	
	/**
	 * 
	 * @Title: getBusinessIdByWorkflowId
	 * @Description: 根据流程id查询业务id
	 * @param wfId 流程id
	 * @return 业务id
	 */
	String queryBusinessIdByWorkflowId(String wfId);
	
	/**
	 * 根据业务id，查询工作流id
	 * @Title: queryWorkflowIdByBusinessId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param bId
	 * @return
	 */
	String queryWorkflowIdByBusinessId(String bId);
	
	String queryBusinessIdById(String id);
	
	int updateWorkflowBusiness(WorkflowBusiness workflowBusiness);
    
	String queryIdByBusinessId(String bId);
	
}
