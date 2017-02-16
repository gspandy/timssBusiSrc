package com.timss.itsm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.itsm.bean.ItsmQuestionRd;
import com.yudean.itc.dto.Page;

public interface ItsmQuestionRdDao {

	
	/**
	 * @description: 插入问题信息
	 * @author: g[抄]中华
	 * @createDate: 2015-6-1
	 * @param questionRd:
	 */
	int insertQuestionRd(ItsmQuestionRd questionRd);
	/**
	 * @description: 更新问题信息
	 * @author: g[抄]中华
	 * @createDate: 2015-6-1
	 * @param questionRd:
	 */
	int updateQuestionRd(@Param("question")ItsmQuestionRd question,@Param("params") String[] params);
	/**
	 * @description: 查找问题信息列表
	 * @author: g[抄]中华
	 * @createDate: 2015-6-1
	 * @param page
	 */
	List<ItsmQuestionRd> queryQuestionRd(Page<ItsmQuestionRd> page);
	
	/**
	 * @description:查询问题基本数据
	 * @author: g[抄]中华
	 * @createDate: 2015-6-1
	 * @param id
	 * @return:
	 */
	ItsmQuestionRd queryQuestionRdById(int id);
	
	/**
	 * @description:更新问题单的“知识单ID”字段信息
	 * @author: 王中华
	 * @createDate: 2015-6-11
	 * @param woId
	 * @return:
	 */
	int updateKlIdOfQuest(@Param("id") int Id,@Param("klId") int klId);
	
	

}
