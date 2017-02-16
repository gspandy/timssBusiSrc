package com.timss.itsm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.itsm.bean.ItsmInfoWo;
import com.yudean.itc.dto.Page;

public interface ItsmInfoWoDao {

	List<ItsmInfoWo> queryItsmInfoWoList(Page<ItsmInfoWo> page);
	
	ItsmInfoWo queryItsmInfoWoById(@Param("id")String infoWoId);
	
	int insertItsmInfoWo(ItsmInfoWo ItsmInfoWo);
	
	int updateItsmInfoWo(ItsmInfoWo ItsmInfoWo);
	
	int deleteItsmInfoWo(@Param("id")String id);


	/**
	 * @description:更新知识的状态
	 * @author: 王中华
	 * @createDate: 2015-5-11
	 * @param infoWoId
	 * @param klStatus:
	 */
	int updateItsmInfoWoStatus(@Param("id")String infoWoId ,@Param("status")String status);

	/**
	 * @description:修改当前处理人的信息
	 * @author: 王中华
	 * @createDate: 2015-5-11
	 * @param infoWoId
	 * @param currHandlerUserId
	 * @param currHandlerUserName:
	 */
	int updateInfoWoCurrHandlerUser(@Param("id")String infoWoId, @Param("currHandler")String currHandlerUserId,
			@Param("currHandlerName")String currHandlerUserName);

	/**
	 * @description: 清空当前处理人信息
	 * @author: 王中华
	 * @createDate: 2015-5-11
	 * @param infoWoId:
	 */
	int clearInfoWoCurrHandlerUser(@Param("id")String infoWoId);

}
