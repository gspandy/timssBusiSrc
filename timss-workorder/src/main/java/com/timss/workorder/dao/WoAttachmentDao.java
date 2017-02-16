package com.timss.workorder.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.workorder.bean.WoAttachment;

/**
 * @title: {title}  附件操作
 * @description: {desc}
 * @company: gdyd
 * @className: WoAttachmentDao.java
 * @author: 王中华
 * @createDate: 2015-1-19
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface WoAttachmentDao {

	int insertWoAttachment(WoAttachment woAttachment);
	
	
	List<WoAttachment> queryWoAttachmentById(@Param("id") String id,@Param("type")String type,
			@Param("loadPhase")String loadPhase);
	
	
	int deleteWoAttachment(@Param("id") String id,@Param("attachId")String attachId,
			@Param("type")String type,@Param("deleteUser")String deleteUser);
	
	
}
