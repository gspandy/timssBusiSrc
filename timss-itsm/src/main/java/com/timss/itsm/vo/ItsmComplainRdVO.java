package com.timss.itsm.vo;

import java.io.Serializable;
import java.util.List;

import com.timss.itsm.bean.ItsmComplainRd;

/**
 * @description:
 * @author: 
 * @createDate: 
 * @param 
 * @return:
 */
/**
 * @author Administrator
 *
 */

public class ItsmComplainRdVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6180410198301266712L;
	
	private ItsmComplainRd complainRds;

	private String commitStyle;//提交方式，是暂存save还是提交commit
	
	private String uploadIds;//附件id
	
	private String taskId;//taskId
	
	private String workflowId;//流程id
	
	private String complainId;
	
	public String getComplainId() {
		return complainId;
	}

	public void setComplainId(String complainId) {
		this.complainId = complainId;
	}

	public String getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCommitStyle() {
		return commitStyle;
	}

	public void setCommitStyle(String commitStyle) {
		this.commitStyle = commitStyle;
	}

	public String getUploadIds() {
		return uploadIds;
	}

	public void setUploadIds(String uploadIds) {
		this.uploadIds = uploadIds;
	}

	public ItsmComplainRd getComplainRds() {
		return complainRds;
	}

	public void setComplainRds(ItsmComplainRd complainRd) {
		this.complainRds = complainRd;
	}
}
