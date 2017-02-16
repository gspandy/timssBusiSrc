package com.timss.itsm.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.itsm.bean.ItsmCustomerConf;
import com.yudean.itc.dto.Page;

public interface ItsmCustomerConfDao {
	
	ItsmCustomerConf queryCustomerConfById(@Param("id") int id);

	void insertCustomerConfInfo(ItsmCustomerConf tempBean);

	void updateCustomerConfInfo(ItsmCustomerConf tempBean);

	List<ItsmCustomerConf> queryCustomerConfList(Page<ItsmCustomerConf> page);

	int getNextParamsConfId();

	void deleteCustomerConf(@Param("id") int customerConfId,
			@Param("modifydate") Date modifydate,@Param("modifyuser") String modifyuser);

	int judgeRepeatCustomerConf(@Param("customerCode") String customerCode);

	ItsmCustomerConf getInitPriority(@Param("customerCode") String customerCode,
			@Param("faultTypeId") String faultTypeId);  
	 
}
