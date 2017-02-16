package com.timss.pms.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.timss.pms.bean.Plan;

/**
 * 返回前台的年度计划详细vo
 * @ClassName:     PlanDtlVo
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-10 上午8:14:26
 */
public class PlanDtlVo extends Plan {
	private  ArrayList<HashMap<String,Object>> attachMap;
	
	private List<ProjectVo> projectVos;
    
	/**
	 * 返回前台的附件信息
	 * @Title: getAttachMap
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getAttachMap() {
		return attachMap;
	}

	public void setAttachMap(ArrayList<HashMap<String, Object>> attachMap) {
		this.attachMap = attachMap;
	}

	public List<ProjectVo> getProjectVos() {
		return projectVos;
	}

	public void setProjectVos(List<ProjectVo> projectVos) {
		this.projectVos = projectVos;
	}
	
	

}
