package com.timss.itsm.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.itsm.bean.ItsmCustomerLoc;

public interface ItsmWoUtilDao {
	
	ItsmCustomerLoc queryCustomerLocByUserId(@Param("customerCode") String customerCode);

	void insertCustomerLocInfo(ItsmCustomerLoc tempBean);

	void updateCustomerLocInfo(Map<String, Object> parma);
	
}
