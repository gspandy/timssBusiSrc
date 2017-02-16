package com.timss.workorder.dao;


import com.timss.workorder.bean.OperRecord;



public interface OperRecordDao {
	
	/**
	 * @description:插入一条统计记录
	 * @author: 王中华
	 * @createDate: 2014-11-25
	 * @param operRecord
	 * @return:
	 */
	int insertOperRecord(OperRecord operRecord);
	

	
}
