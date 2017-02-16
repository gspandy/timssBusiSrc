package com.timss.pms.vo;

import java.util.Map;

/**
 * 包含流程信息的项目
 * @ClassName:     ProjectWFVo
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-4 下午4:01:53
 */
public class ProjectWFVo extends ProjectDtlVo{
	private Map elementMap;

	/**
	 * 获得流程节点的附加信息
	 * @Title: getElementMap
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return
	 */
	public Map getElementMap() {
		return elementMap;
	}

	public void setElementMap(Map elementMap) {
		this.elementMap = elementMap;
	}

	
	
	
	
}
