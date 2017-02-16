package com.timss.itsm.dao;


import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.timss.itsm.bean.ItsmAttachment;

/**
 * @title: {title}  附件操作
 * @description: {desc}
 * @company: gdyd
 * @className:AttachmentDao.java
 * @author: yangkun
 * @createDate: 2016-8-23
 * @updateUser: yangkun
 * @version: 2.0
 */
public interface ItsmAttachmentDao {

	int insertAttachment(ItsmAttachment attachment);
	
	
	List<ItsmAttachment> queryAttachmentById(@Param("id") String id,@Param("type")String type,
			@Param("loadPhase")String loadPhase);
	
	
	int deleteAttachment(@Param("id") String id,@Param("attachId")String attachId,
			@Param("type")String type,@Param("deleteUser")String deleteUser);
	
	
}
