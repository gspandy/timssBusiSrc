package com.timss.pms.service;

/**
 * 生成编号
 * @ClassName:     SequenceService
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2014-10-20 上午8:56:01
 */
public interface SequenceService {
	
	String createContractSequenceService(String siteName,String type);
	
	String createProjectSequenceService(String type,String year);
	
	/**
	 * 合同编号增加,如果插入的合同编号等于数据库编号，则编号增加
	 * @Title: updateContractSequence
	 */
	void updateContractSequence(String contractCode);
	
	/**
	 * 项目编号增加
	 */
	void updateProjectSequece(String projectCode);
}
