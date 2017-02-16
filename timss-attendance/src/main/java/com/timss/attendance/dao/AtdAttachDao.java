package com.timss.attendance.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 通用附件的dao
 * @author 890147
 */
public interface AtdAttachDao {
    
	/**
	 * 根据业务类型和id查询所有的附件
	 * @param itemType
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	List<String> queryAll(@Param("itemType")String itemType,@Param("itemId")String itemId) throws Exception;

	/**
	 * 关联业务附件
	 * @param itemType
	 * @param itemId
	 * @param attachIds
	 * @return
	 * @throws Exception
	 */
	Integer insert(@Param("itemType")String itemType,@Param("itemId")String itemId,@Param("attachIds")String[]attachIds) throws Exception;
	
	/**
	 * 删除业务附件
	 * @param itemType
	 * @param itemId
	 * @param attachIds 为空删掉所有
	 * @return
	 * @throws Exception
	 */
	Integer delete(@Param("itemType")String itemType,@Param("itemId")String itemId,@Param("attachIds")String[]attachIds) throws Exception;
}