package com.timss.pms.service;

import com.timss.pms.bean.PrifixSequence;

public interface PrifixSequenceService {
	/**
	 * 获取流水号值
	 * @Title: getNextSequenceVal
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param prifix 前缀
	 * @param siteid 站点
	 * @return
	 */
	public String getNextSequenceVal(String prifix,String type);
	
	/**
	 * 获取流水号值
	 * @Title: getNextSequenceVal
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param prifix
	 * @param siteid
	 * @param prifixSequence 当流水号不存在时，插入的记录
	 * @return
	 */
	public String getNextSequenceVal(String prifix,String type,PrifixSequence prifixSequence);
	
	/**
	 * 流水号递增
	 * @Title: increaseSequence
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param prifix
	 * @param siteid
	 */
	public void increaseSequence(String prifix,String type);
	
	public void increaseSequence(String prifix,String type,PrifixSequence prifixSequence);
}
