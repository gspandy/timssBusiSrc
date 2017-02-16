package com.timss.itsm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.itsm.bean.ItsmInfoWoEquipment;

public interface ItsmInfoWoEquipmentDao {

	List<ItsmInfoWoEquipment> queryItsmInfoWoEquipmentList(@Param("infoWoId")String id);
	
	
	int insertItsmInfoWoEquipment(ItsmInfoWoEquipment ItsmInfoWoEquipment);
	
	
	int deleteItsmInfoWoEquipment(@Param("infoWoId")String id); 


    void updateItsmInfoWoEquipment(ItsmInfoWoEquipment infoWoEquipment);

}
