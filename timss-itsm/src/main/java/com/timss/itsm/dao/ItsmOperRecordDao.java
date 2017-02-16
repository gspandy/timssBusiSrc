package com.timss.itsm.dao;


import com.timss.itsm.bean.ItsmOperRecord;



public interface ItsmOperRecordDao {
	
	/**
	 * @description:插入一条统计记录
	 * @author: 王中华
	 * @createDate: 2014-11-25
	 * @param operRecord
	 * @return:
	 */
	int insertOperRecord(ItsmOperRecord operRecord);
	

	
}
