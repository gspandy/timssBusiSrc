package com.timss.workorder.dao;


import java.util.List;

import com.timss.workorder.bean.WoapplyWorker;

/**
 * @title: {title}外来施工人员信息
 * @description: {desc}
 * @company: gdyd
 * @className: WoapplyWorkerDao.java
 * @author: 王中华
 * @createDate: 2016-1-8
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface WoapplyWorkerDao {

	/**
	 * @description:插入外来施工人员信息
	 * @author: 王中华
	 * @createDate: 2016-1-8
	 * @param woQx
	 * @return:
	 */
	int insertWoapplyWorker(WoapplyWorker woapplyWorker);
	
	
	/**
	 * @description:删除外来施工人员信息
	 * @author: 王中华
	 * @createDate: 2016-1-8
	 * @param woapplyId
	 * @return:
	 */
	int deleteWorkerByWoapplyId(String woapplyId); 
	
	/**
	 * @description:查询外来施工人员信息
	 * @author: 王中华
	 * @createDate: 2016-1-8
	 * @param woapplyId
	 * @return:
	 */
	List<WoapplyWorker> queryWoapplyWorker(String woapplyId);

	
}
