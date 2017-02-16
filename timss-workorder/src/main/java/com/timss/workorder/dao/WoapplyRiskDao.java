package com.timss.workorder.dao;


import java.util.List;

import com.timss.workorder.bean.WoapplyRisk;

/**
 * @title: {title}风险评估
 * @description: {desc}
 * @company: gdyd
 * @className: WoapplyRiskDao.java
 * @author: 王中华
 * @createDate: 2016-1-8
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface WoapplyRiskDao {

	/**
	 * @description:插入某个开工申请的风险评估
	 * @author: 王中华
	 * @createDate: 2016-1-8
	 * @param woapplyRiskList
	 * @return:
	 */
	int insertWoapplyRisk(WoapplyRisk woapplyRisk);
	
	/**
	 * @description: 删除某个开工申请的所有风险评估
	 * @author: 王中华
	 * @createDate: 2014-7-1
	 * @param id
	 * @return:
	 */
	int deleteRiskListByWoapplyId(String woapplyId); 
	
	/**
	 * @description: 查询某个开工申请的所有风险评估
	 * @author: 王中华
	 * @createDate: 2016-1-8
	 * @param woapplyId
	 * @return:
	 */
	List<WoapplyRisk> queryAllWoapplyRisk(String woapplyId);

}
