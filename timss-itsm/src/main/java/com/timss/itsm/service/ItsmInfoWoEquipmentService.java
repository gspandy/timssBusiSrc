package com.timss.itsm.service;

import java.util.List;

import com.timss.itsm.bean.ItsmInfoWoEquipment;
import com.yudean.itc.dto.Page;

public interface ItsmInfoWoEquipmentService {
	
	/**
	 * @description:删除
	 * @author: 王中华
	 * @createDate: 2016-11-2
	 * @param infoWoId:
	 */
	void deleteItsmInfoWoEquipment(String infoWoId);
	
	/**
	 * @description:插入
	 * @author: 王中华
	 * @createDate: 2016-11-2
	 * @param itsmInfoWo
	 * @return:
	 */
	int insertItsmInfoWoEquipment(ItsmInfoWoEquipment itsmInfoWoEquipment);
	
	/**
	 * @description:查询列表
	 * @author: 王中华
	 * @createDate: 2016-11-2
	 * @param page
	 * @return:
	 */
	List<ItsmInfoWoEquipment> queryItsmInfoWoEquipmentList(String infoWoId);

    /**
     * @description:修改
     * @author: 王中华
     * @createDate: 2016-11-5
     * @param infoWoEquipment:
     */
    void updateItsmInfoWoEquipment(ItsmInfoWoEquipment infoWoEquipment);
	
}
