package com.timss.attendance.service;

import java.util.List;
import java.util.Map;

/**
 * 通用附件的service
 * @author 890147
 *
 */
public interface AtdAttachService {

	/**
	 * 根据业务类型和id查询所有的附件
	 * @param itemType
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> queryAll(String itemType,String itemId) throws Exception;

	/**
	 * 关联业务附件
	 * @param itemType
	 * @param itemId
	 * @param attachIds
	 * @return
	 * @throws Exception
	 */
	Integer insert(String itemType,String itemId,String[]attachIds) throws Exception;
	
	/**
	 * 删除业务附件
	 * @param itemType
	 * @param itemId
	 * @param attachIds 为空删掉所有
	 * @return
	 * @throws Exception
	 */
	Integer delete(String itemType,String itemId,String[]attachIds) throws Exception;
	
}