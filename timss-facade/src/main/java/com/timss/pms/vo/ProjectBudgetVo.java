package com.timss.pms.vo;

import java.util.HashMap;

import com.timss.pms.bean.Project;

/**
 * 包含项目预算使用的情况的vo
 * @ClassName:     ProjectBudgetVo
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-14 上午8:58:54
 */
public class ProjectBudgetVo extends Project{
	private HashMap budget;

	public HashMap getBudget() {
		return budget;
	}

	public void setBudget(HashMap budget) {
		this.budget = budget;
	}
	
	
}
