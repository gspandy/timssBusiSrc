package com.timss.itsm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.itsm.bean.ItsmKnowledge;
import com.yudean.itc.dto.Page;

public interface ItsmKnowledgeDao {

	List<ItsmKnowledge> queryItsmKnowledgeList(Page<ItsmKnowledge> page);
	
	ItsmKnowledge queryItsmKnowledgeById(@Param("id")int klId);
	
	void insertItsmKnowledge(ItsmKnowledge itsmKnowledge);
	
	void updateItsmKnowledge(ItsmKnowledge itsmKnowledge);
	
	void deleteItsmKnowLedge(@Param("id")int id);

	int getNextKLId();

	/**
	 * @description:更新知识的状态
	 * @author: 王中华
	 * @createDate: 2015-5-11
	 * @param klId
	 * @param klStatus:
	 */
	void updateItsmKnowledgeStatus(@Param("id")String klId ,@Param("klStatus")String klStatus);

	/**
	 * @description:修改当前处理人的信息
	 * @author: 王中华
	 * @createDate: 2015-5-11
	 * @param klId
	 * @param currHandlerUserId
	 * @param currHandlerUserName:
	 */
	void updateKlCurrHandlerUser(@Param("id")String klId, @Param("currHandlerUserId")String currHandlerUserId,
			@Param("currHandlerUserName")String currHandlerUserName);

	/**
	 * @description: 清空当前处理人信息
	 * @author: 王中华
	 * @createDate: 2015-5-11
	 * @param klId:
	 */
	void clearklCurrHandlerUser(@Param("id")int klId);

}
