package com.timss.pms.dao;

import org.apache.ibatis.annotations.Param;

import com.timss.pms.bean.PrifixSequence;

public interface PrifixSequenceDao {
	/**
	 * 获取流水号值
	 * @Title: getNextSequenceVal
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param prifix
	 * @param siteid
	 * @return
	 */
	public PrifixSequence getNextSequenceVal(@Param("prifix") String prifix,@Param("type") String type);
	
	/**
	 * 流水号递增
	 * @Title: increaseSequence
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param prifix
	 * @param siteid
	 */
	public int increaseSequence(@Param("prifix") String prifix,@Param("type") String type);
	
	public int insertSequence(PrifixSequence prifixSequence);
}
