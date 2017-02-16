package com.timss.itsm.service;

import java.util.List;
import java.util.Map;

import com.timss.itsm.bean.ItsmQuestionRd;
import com.timss.itsm.vo.ItsmQuestionRdVo;
import com.yudean.itc.dto.Page;
/***
 * 问题  Service 操作
 * @author 谷传伟
 * 2014-6-11
 */
public interface ItsmQuestionRdService {
	/**
	 * @description: 添加/更新问题
	 * @author: 谷传伟
	 * @createDate: 2015-6-1
	 * @param questionRd
	 * @param startWorkFlow
	 * @throws Exception 
	 */
	Map<String, Object> saveOrUpdateQuestionRd(ItsmQuestionRdVo questionRdVo,boolean startWorkFlow) throws Exception; 
	/***
	 * @description:查询所有问题
	 * @author: 谷传伟
	 * @createDate: 2015-6-1
	 * @throws Exception 
	 */
	Page<ItsmQuestionRd> queryQuestionRd(Page<ItsmQuestionRd> page) throws Exception;
	/**
	 * @description:根据ID  查询问题
	 * @author: 谷传伟
	 * @createDate: 2015-6-1
	 * @param id
	 * @return
	 */
	Map<String, Object> queryQuestionRdById(int id);
	/**
	 * @description:删除问题（标记删除）
	 * @author: 谷传伟
	 * @createDate: 2015-6-1
	 * @param id:
	 */
	void deleteQuestionRd(int id);
	/**
	 * @description:终止问题
	 * @author: 谷传伟
	 * @createDate: 2015-6-1
	 * @param woId:
	 */
	void stopQuestionRd(Map<String, Object> parmas);
	/**
	 * @description:取消提交问题
	 * @author: 谷传伟
	 * @createDate: 2015-6-1
	 * @param id:
	 */
	void cancelCommitQuestionRd(int id);
	void obsoleteQuestionRd(int id);
	
	/**
	 * @description:更新问题单里面的知识单id
	 * @author: 王中华
	 * @createDate: 2015-6-11
	 * @param prombleId
	 * @param konwledgeId
	 * @return:
	 */
	int knowledgeIdAddToPromble(int prombleId,int konwledgeId);
	
	//获取远程工单数据的方法
	List<Map<String, Object>> queryWOFuzzyByName(String name);
}
