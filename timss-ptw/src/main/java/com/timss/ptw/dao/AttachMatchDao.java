package com.timss.ptw.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.ptw.bean.PtoAttachment;

/**
 * @title: AttachMatch
 * @description: 附件操作
 * @company: gdyd
 * @className: AttachMatch.java
 * @author: gchw
 * @createDate: 2015-7-14
 * @updateUser: gchw
 * @version: 1.0
 */
public interface AttachMatchDao {

	int insertAttachMatch(PtoAttachment woAttachment);
	
	
	List<PtoAttachment> queryAttachMatchById(@Param("id") String id,@Param("type")String type,
			@Param("loadPhase")String loadPhase);
	
	
	int deleteAttachMatch(@Param("id") String id,@Param("attachId")String attachId,
			@Param("type")String type,@Param("deleteUser")String deleteUser);
	
	
}
