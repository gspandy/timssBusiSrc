package com.timss.workorder.dao;


import java.util.List;

import com.timss.workorder.bean.WoapplySafeInform;
 
/**
 * @title: {title}安全交底
 * @description: {desc}
 * @company: gdyd
 * @className: WoapplySafeInformDao.java
 * @author: 王中华
 * @createDate: 2016-1-8
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface WoapplySafeInformDao {

	/**
	 * @description:插入安全交底
	 * @author: 王中华
	 * @createDate: 2016-1-8
	 * @param safeInformList
	 * @return:
	 */
	int insertSafeInform(WoapplySafeInform safeInform);
	
	
	/**
	 * @description:删除安全交底
	 * @author: 王中华
	 * @createDate: 2016-1-8
	 * @param woapplyId
	 * @return:
	 */
	int deleteWoSafeInformByWoapplyId(String woapplyId); 
	
	/**
	 * @description:查询安全交底
	 * @author: 王中华
	 * @createDate: 2016-1-8
	 * @param woapplyId
	 * @return:
	 */
	List<WoapplySafeInform> queryWoSafeInform(String woapplyId);

	
}
